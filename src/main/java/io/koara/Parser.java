/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.koara;

import static io.koara.TokenManager.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import io.koara.ast.Blockquote;
import io.koara.ast.Code;
import io.koara.ast.CodeBlock;
import io.koara.ast.Document;
import io.koara.ast.Em;
import io.koara.ast.Heading;
import io.koara.ast.Image;
import io.koara.ast.LineBreak;
import io.koara.ast.Link;
import io.koara.ast.List;
import io.koara.ast.ListItem;
import io.koara.ast.Paragraph;
import io.koara.ast.Strong;
import io.koara.ast.Text;

public class Parser {

	private CharStream cs;
	private Token token, nextToken, scanPosition, lastPosition;
	private TokenManager tm;
	private TreeState tree;
	private int currentBlockLevel;
	private int currentQuoteLevel;
	private int lookAhead;
	private int nextTokenKind;
	private boolean lookingAhead = false;
	private boolean semanticLookAhead;	
	private LookaheadSuccess lookAheadSuccess = new LookaheadSuccess();
	private class LookaheadSuccess extends Error {}
	
	public Document parse(String text) {
		return parse(new StringReader(text));
	}
	
	public Document parse(File file) throws IOException {
		return parse(new FileReader(file));
	}
	
	private Document parse(Reader reader) {
		cs = new CharStream(reader);
		tm = new TokenManager(cs);
		token = new Token();
		tree = new TreeState();
		nextTokenKind = -1;
		
		Document document = new Document();
		tree.openScope(document);
		do {
			consumeToken(EOL);
		} while (getNextTokenKind() == EOL);
		whiteSpace();
		if (hasAnyBlockElementsAhead()) {
			blockElement();
			while (blockAhead()) {
				while (getNextTokenKind() == EOL) {
					consumeToken(EOL);
					whiteSpace();			
				}
				blockElement();
			}
			do {
				consumeToken(EOL);
			} while(getNextTokenKind() == EOL);
			whiteSpace();
		} 
		consumeToken(EOF);
		tree.closeScope(document);
		return document;
	}
	
	private void blockElement() {
		currentBlockLevel++;
		if (headingAhead(1)) {
			heading();
		} else if(getNextTokenKind() == GT) {
			blockquote();
		} else if(getNextTokenKind() == DASH) {
			unorderedList();
		} else if(hasOrderedListAhead()) {
			orderedList();
		} else if(hasFencedCodeBlockAhead()) {
			fencedCodeBlock();
		} else {
			paragraph();
		}
		currentBlockLevel--;
	}
	
	private void heading() {
		Heading heading = new Heading();
		tree.openScope(heading);
		int headingLevel = 0;

		while(getNextTokenKind() == EQ) {
			consumeToken(EQ);
			headingLevel++;
		}
		whiteSpace();
	    while (headingHasInlineElementsAhead()) {
			if (hasTextAhead()) {
				text();
			} else if (hasImageAhead()) {
				image();
			} else if (hasLinkAhead()) {
				link();
			} else if (hasInlineStrongAhead()) {
				strong();
			} else if (hasInlineEmAhead()) {
				em();
			} else if (hasCodeAhead()) {
				code();
			} else {
				looseChar();
			}
		}
		heading.setValue(headingLevel);
		tree.closeScope(heading);
	}
	
	private void blockquote() {
		Blockquote blockquote = new Blockquote();
		tree.openScope(blockquote);
		currentQuoteLevel++;
		consumeToken(GT);
		while (blockquoteHasEmptyLineAhead()) {
			blockquoteEmptyLine();
		}
		whiteSpace(); 
		if (blockquoteHasAnyBlockElementseAhead()) {
			blockElement();
			while (blockAhead()) {
				while (getNextTokenKind() == EOL) {
					consumeToken(EOL);
					whiteSpace();
					blockquotePrefix();
				}
				blockElement();
			}
		}
		while (hasBlockquoteEmptyLines()) {
			blockquoteEmptyLine();
		}
		currentQuoteLevel--;
		tree.closeScope(blockquote);
	}
		
	private void blockquotePrefix() {
		int i = 0;
		do {
			consumeToken(GT);
			whiteSpace();	
		} while(++i < currentQuoteLevel);
	}

	private void blockquoteEmptyLine() {
		consumeToken(EOL);
		whiteSpace();
		do {
			consumeToken(GT);
			whiteSpace();
		} while(getNextTokenKind() == GT);
	}
	
	private void unorderedList() {
		List list = new List(false);
		tree.openScope(list);
		unorderedListItem();
		while (listItemAhead(false)) {
			while (getNextTokenKind() == EOL) {
				consumeToken(EOL);
			}
			whiteSpace();
			unorderedListItem();
		}
		tree.closeScope(list);
	}

	private void unorderedListItem() {
		ListItem listItem = new ListItem();
		tree.openScope(listItem);

		consumeToken(DASH);
		whiteSpace();
		if (listItemHasInlineElements()) { 
			blockElement();
			while (blockAhead()) {
				while (getNextTokenKind() == EOL) {
					consumeToken(EOL);
					whiteSpace();
					if (currentQuoteLevel > 0) {
						blockquotePrefix();
					}
				}
				blockElement();
			}
		}
		tree.closeScope(listItem);
	}

	private void orderedList() {
		List list = new List(true);
		tree.openScope(list);
		orderedListItem();
		while (listItemAhead(true)) {
			while (getNextTokenKind() == EOL) {
				consumeToken(EOL);
			}
			whiteSpace();
			orderedListItem();
		}
		tree.closeScope(list);
	}

	private void orderedListItem() {
		ListItem listItem = new ListItem();
		tree.openScope(listItem);
		Token t;
		t = consumeToken(DIGITS);
		consumeToken(DOT);
		whiteSpace();
		if (listItemHasInlineElements()) { 
			blockElement();
			while (blockAhead()) {
				while (getNextTokenKind() == EOL) {
					consumeToken(EOL);
					whiteSpace();
					if (currentQuoteLevel > 0) {
						blockquotePrefix();
					}
				}
				blockElement();
			}
		}
		listItem.setNumber(Integer.valueOf(Integer.valueOf(t.image)));
		tree.closeScope(listItem);
	}

	private void fencedCodeBlock() {
		CodeBlock codeBlock = new CodeBlock();
		tree.openScope(codeBlock);
		StringBuilder s = new StringBuilder();
		int beginColumn = consumeToken(BACKTICK).beginColumn;
		do {
			consumeToken(BACKTICK);
		} while(getNextTokenKind() == BACKTICK);
		whiteSpace();
		if (getNextTokenKind() == CHAR_SEQUENCE) {
			codeBlock.setLanguage(codeLanguage()); 
		}
		if (getNextTokenKind() != EOF && !fencesAhead()) {
			consumeToken(EOL);
			levelWhiteSpace(beginColumn);
		}
		while (fencedCodeBlockHasInlineTokens()) {
			switch (getNextTokenKind()) {
			case ASTERISK: 		s.append(consumeToken(ASTERISK).image); break;
			case BACKSLASH: 	s.append(consumeToken(BACKSLASH).image); break;
			case CHAR_SEQUENCE: s.append(consumeToken(CHAR_SEQUENCE).image); break;
			case COLON: 		s.append(consumeToken(COLON).image); break;
			case DASH: 			s.append(consumeToken(DASH).image); break;
			case DIGITS: 		s.append(consumeToken(DIGITS).image); break;
			case DOT: 			s.append(consumeToken(DOT).image); break;
			case EQ: 			s.append(consumeToken(EQ).image); break;
			case ESCAPED_CHAR: 	s.append(consumeToken(ESCAPED_CHAR).image); break;
			case IMAGE_LABEL: 	s.append(consumeToken(IMAGE_LABEL).image); break;
			case LT: 			s.append(consumeToken(LT).image); break;
			case GT: 			s.append(consumeToken(GT).image); break;
			case LBRACK:		s.append(consumeToken(LBRACK).image); break;
			case RBRACK:		s.append(consumeToken(RBRACK).image); break;
			case LPAREN:		s.append(consumeToken(LPAREN).image); break;
			case RPAREN:		s.append(consumeToken(RPAREN).image); break;
			case UNDERSCORE:	s.append(consumeToken(UNDERSCORE).image); break;
			case BACKTICK:		s.append(consumeToken(BACKTICK).image); break;
			default:
				if (!nextAfterSpace(EOL, EOF)) {
					switch (getNextTokenKind()) {
					case SPACE: s.append(consumeToken(SPACE).image); break;
					case TAB: consumeToken(TAB); s.append("    "); break;
					}
				} else if (!fencesAhead()) {
					consumeToken(EOL);
					s.append("\n");
					levelWhiteSpace(beginColumn);
				}
			}
		}
		if (fencesAhead()) {
			consumeToken(EOL);
			whiteSpace();
			while (getNextTokenKind() == BACKTICK) {
				consumeToken(BACKTICK);
			}
		}
		codeBlock.setValue(s.toString());
		tree.closeScope(codeBlock);
	}
	
	private void paragraph() {
		Paragraph paragraph = new Paragraph();
		tree.openScope(paragraph);
		inline();
		while (textAhead()) {
			lineBreak();
			whiteSpace();
			while (getNextTokenKind() == GT) {
				consumeToken(GT);
				whiteSpace();
			}
			inline();
		}
		tree.closeScope(paragraph);
	}
	
	private void text() {
		Text text = new Text();
		tree.openScope(text);
		StringBuffer s = new StringBuffer();
		while (textHasTokensAhead()) {
			switch (getNextTokenKind()) {
			case BACKSLASH: 	s.append(consumeToken(BACKSLASH).image); break;
			case CHAR_SEQUENCE:	s.append(consumeToken(CHAR_SEQUENCE).image); break;
			case COLON: 		s.append(consumeToken(COLON).image); break;
			case DASH: 			s.append(consumeToken(DASH).image); break;
			case DIGITS: 		s.append(consumeToken(DIGITS).image); break;
			case DOT: 			s.append(consumeToken(DOT).image); break;
			case EQ: 			s.append(consumeToken(EQ).image); break;
			case ESCAPED_CHAR:	s.append(consumeToken(ESCAPED_CHAR).image.substring(1)); break;
			case GT: 			s.append(consumeToken(GT).image); break;
			case IMAGE_LABEL: 	s.append(consumeToken(IMAGE_LABEL).image); break;
			case LPAREN: 		s.append(consumeToken(LPAREN).image); break;
			case LT: 			s.append(consumeToken(LT).image); break;
			case RBRACK: 		s.append(consumeToken(RBRACK).image); break;
			case RPAREN: 		s.append(consumeToken(RPAREN).image); break;
			default:
				if (!nextAfterSpace(EOL, EOF)) {
					switch (getNextTokenKind()) {
					case SPACE:	s.append(consumeToken(SPACE).image); break;
					case TAB:	consumeToken(TAB); s.append("    "); break;
					}
				}
			}
		}
		text.setValue(s.toString());
		tree.closeScope(text);
	}
	
	private void image() {
		Image image = new Image();
		tree.openScope(image);
		String ref = "";
		consumeToken(LBRACK);
		whiteSpace();
		consumeToken(IMAGE_LABEL);
		whiteSpace();
		while (imageHasAnyElements()) {
			if (hasTextAhead()) {
				resourceText();
			} else {
				looseChar();
			}
		}
		whiteSpace();
		consumeToken(RBRACK);
		if (imageHasResourceUrlAhead()) {
			ref = resourceUrl();
		}
		image.setValue(ref);
		tree.closeScope(image);
	}
	
	private void link() {
		Link link = new Link();
		tree.openScope(link);
		String ref = "";
		consumeToken(LBRACK);
		whiteSpace();
		while (linkHasAnyElements()) {
			if (hasImageAhead()) {
				image();
			} else if (linkStrongAhead()) {
				strong();
			} else if (linkEmAhead()) {
				em();
			} else if (linkCodeAhead()) {
				code();
			} else if (linkResourceTextAhead()) {
				resourceText();
			} else {
				looseChar();
			}
		}
		whiteSpace();
		consumeToken(RBRACK);
		if (linkHasUrl()) {
			ref = resourceUrl();
		}
		link.setValue(ref);
		tree.closeScope(link);
	}
	
	private void strong() {
		Strong strong = new Strong();
		tree.openScope(strong);
		consumeToken(ASTERISK);
		while (strongHasElements()) {
			if (hasTextAhead()) {
				text();
			} else if (strongHasImage()) {
				image();
			} else if (strongHasLink()) {
				link();
			} else if (multilineAhead(BACKTICK)) {
				codeMultiline();
			} else if (strongEmWithinStrongAhead()) {
				emWithinStrong();
			} else {
				switch (getNextTokenKind()) {
				case BACKTICK: 		tree.addSingleValue(new Text(), consumeToken(BACKTICK)); break;
				case LBRACK:		tree.addSingleValue(new Text(), consumeToken(LBRACK)); break;
				case UNDERSCORE:	tree.addSingleValue(new Text(), consumeToken(UNDERSCORE)); break;
				}
			}
		}
		consumeToken(ASTERISK);
		tree.closeScope(strong);
	}
	
	private void em() {
		Em em = new Em();
		tree.openScope(em);
		consumeToken(UNDERSCORE);
		while (emHasElements()) {
			if (emHasText()) {
				text();
			} else if (emHasImage()) {
				image();
			} else if (emHasLink()) {
				link();
			} else if (emHasCode()) {
				code();
			} else if (emHasStrongWithinEm()) {
				strongWithinEm();
			} else {
				switch (getNextTokenKind()) {
				case ASTERISK:	tree.addSingleValue(new Text(), consumeToken(ASTERISK)); break;
				case BACKTICK:	tree.addSingleValue(new Text(), consumeToken(BACKTICK)); break;
				case LBRACK: 	tree.addSingleValue(new Text(), consumeToken(LBRACK));	break;
				}
			}
		}
		consumeToken(UNDERSCORE);
		tree.closeScope(em);
	}

	private void code() {
		Code code = new Code();
		tree.openScope(code);
		consumeToken(BACKTICK);
		codeText();
		consumeToken(BACKTICK);
		tree.closeScope(code);
	}

	private void codeText() {
		Text text = new Text();
		tree.openScope(text);
		StringBuffer s = new StringBuffer();
		do {
			switch (getNextTokenKind()) {
			case ASTERISK: 		s.append(consumeToken(ASTERISK).image); break;
			case BACKSLASH: 	s.append(consumeToken(BACKSLASH).image); break;
			case CHAR_SEQUENCE: s.append(consumeToken(CHAR_SEQUENCE).image); break;
			case COLON: 		s.append(consumeToken(COLON).image); break;
			case DASH: 			s.append(consumeToken(DASH).image); break;
			case DIGITS:		s.append(consumeToken(DIGITS).image); break;
			case DOT:			s.append(consumeToken(DOT).image); break;
			case EQ:			s.append(consumeToken(EQ).image); break;
			case ESCAPED_CHAR:	s.append(consumeToken(ESCAPED_CHAR).image); break;
			case IMAGE_LABEL:	s.append(consumeToken(IMAGE_LABEL).image); break;
			case LT:			s.append(consumeToken(LT).image); break;
			case LBRACK:		s.append(consumeToken(LBRACK).image); break;
			case RBRACK:		s.append(consumeToken(RBRACK).image); break;
			case LPAREN:		s.append(consumeToken(LPAREN).image); break;
			case GT:			s.append(consumeToken(GT).image); break;
			case RPAREN:		s.append(consumeToken(RPAREN).image); break;
			case UNDERSCORE:	s.append(consumeToken(UNDERSCORE).image); break;
			default:
				if (!nextAfterSpace(EOL, EOF)) {
					switch (getNextTokenKind()) {
					case SPACE:	s.append(consumeToken(SPACE).image); break;
					case TAB: consumeToken(TAB); s.append("    "); break;
					}
				}
			}
		} while(codeTextHasAnyTokenAhead());
		text.setValue(s.toString());
		tree.closeScope(text);
	}

	private void looseChar() {
		Text text = new Text();
		tree.openScope(text);
		switch (getNextTokenKind()) {
		case ASTERISK:		text.setValue(consumeToken(ASTERISK).image); break;
		case BACKTICK:		text.setValue(consumeToken(BACKTICK).image); break;
		case LBRACK:		text.setValue(consumeToken(LBRACK).image); break;
		case UNDERSCORE:	text.setValue(consumeToken(UNDERSCORE).image); break;		
		}
		tree.closeScope(text);
	}

	private void lineBreak() {
		LineBreak linebreak = new LineBreak();
		tree.openScope(linebreak);
		while (getNextTokenKind() == SPACE || getNextTokenKind() == TAB) {
			consumeToken(getNextTokenKind());
		}
		consumeToken(EOL);
		tree.closeScope(linebreak);
	}
	
	private void levelWhiteSpace(int threshold) {
		int currentPos = 1;
		while ((getNextTokenKind() == SPACE || getNextTokenKind() == TAB) && currentPos < (threshold - 1)) {
			currentPos = consumeToken(getNextTokenKind()).beginColumn;
		}
	}

	private String codeLanguage() {
		StringBuilder s = new StringBuilder();
		do {
			switch (getNextTokenKind()) {
			case CHAR_SEQUENCE:	s.append(consumeToken(CHAR_SEQUENCE).image); break;
			case BACKTICK: s.append(consumeToken(BACKTICK).image); break;
			}
		} while (getNextTokenKind() == BACKTICK || getNextTokenKind() == CHAR_SEQUENCE);
		return s.toString();
	}

	private void inline() {
		do {
			if (hasInlineTextAhead()) {
				text();
			} else if (hasImageAhead()) {
				image();
			} else if (hasLinkAhead()) {
				link();
			} else if (multilineAhead(ASTERISK)) {
				strongMultiline();
			} else if (multilineAhead(UNDERSCORE)) {
				emMultiline();
			} else if (multilineAhead(BACKTICK)) {
				codeMultiline();
			} else {
				looseChar();
			}
		} while (hasInlineElementAhead());
	}

	private void resourceText() {
		Text text = new Text();
		tree.openScope(text);
		StringBuilder s = new StringBuilder();
		do {
			switch (getNextTokenKind()) {
			case BACKSLASH:		s.append(consumeToken(BACKSLASH).image); break;
			case COLON:			s.append(consumeToken(COLON).image); break;
			case CHAR_SEQUENCE:	s.append(consumeToken(CHAR_SEQUENCE).image); break;
			case DASH:			s.append(consumeToken(DASH).image); break;
			case DIGITS:		s.append(consumeToken(DIGITS).image); break;
			case DOT:			s.append(consumeToken(DOT).image); break;
			case EQ:			s.append(consumeToken(EQ).image); break;
			case ESCAPED_CHAR:	s.append(consumeToken(ESCAPED_CHAR).image.substring(1)); break;
			case IMAGE_LABEL:	s.append(consumeToken(IMAGE_LABEL).image); break;
			case GT:			s.append(consumeToken(GT).image); break;
			case LPAREN:		s.append(consumeToken(LPAREN).image); break;
			case LT:			s.append(consumeToken(LT).image); break;
			case RPAREN:		s.append(consumeToken(RPAREN).image); break;
			default:
				if (!nextAfterSpace(RBRACK)) {
					switch (getNextTokenKind()) {
					case SPACE:	s.append(consumeToken(SPACE).image); break;
					case TAB:	consumeToken(TAB); s.append("    "); break;
					}
				}
			}
		} while(resourceHasElementAhead());
		text.setValue(s.toString());
		tree.closeScope(text);
	}

	private String resourceUrl() {
		consumeToken(LPAREN);
		whiteSpace();
		String ref = resourceUrlText();
		whiteSpace();
		consumeToken(RPAREN);
		return ref;
	}

	private String resourceUrlText() {
		StringBuilder s = new StringBuilder();
		while (resourceTextHasElementsAhead()) {
			switch (getNextTokenKind()) {
			case ASTERISK: 		s.append(consumeToken(ASTERISK).image); break;
			case BACKSLASH:		s.append(consumeToken(BACKSLASH).image); break;
			case BACKTICK:		s.append(consumeToken(BACKTICK).image); break;
			case CHAR_SEQUENCE:	s.append(consumeToken(CHAR_SEQUENCE).image); break;
			case COLON:			s.append(consumeToken(COLON).image); break;
			case DASH:			s.append(consumeToken(DASH).image); break;
			case DIGITS:		s.append(consumeToken(DIGITS).image); break;
			case DOT:			s.append(consumeToken(DOT).image); break;
			case EQ:			s.append(consumeToken(EQ).image); break;
			case ESCAPED_CHAR:	s.append(consumeToken(ESCAPED_CHAR).image.substring(1)); break;
			case IMAGE_LABEL:	s.append(consumeToken(IMAGE_LABEL).image); break;
			case GT:			s.append(consumeToken(GT).image); break;
			case LBRACK:		s.append(consumeToken(LBRACK).image); break;
			case LPAREN:		s.append(consumeToken(LPAREN).image); break;
			case LT:			s.append(consumeToken(LT).image); break;
			case RBRACK:		s.append(consumeToken(RBRACK).image); break;
			case UNDERSCORE:	s.append(consumeToken(UNDERSCORE).image); break;
			default:
				if (!nextAfterSpace(RPAREN)) {
					switch (getNextTokenKind()) {
					case SPACE:	s.append(consumeToken(SPACE).image); break;
					case TAB:	consumeToken(TAB); s.append("    "); break;
					}
				}
			}
		}
		return s.toString();
	}

	private void strongMultiline() {
		Strong strong = new Strong();
		tree.openScope(strong);
		consumeToken(ASTERISK);
		strongMultilineContent();
		while (textAhead()) {
			lineBreak();
			strongMultilineContent();
		}
		consumeToken(ASTERISK);
		tree.closeScope(strong);
	}

	private void strongMultilineContent() {
		do {
			if (hasTextAhead()) {
				text();
			} else if (hasImageAhead()) {
				image();
			} else if (hasLinkAhead()) {
				link();
			} else if (hasCodeAhead()) {
				code();
			} else if (hasEmWithinStrongMultiline()) {
				emWithinStrongMultiline();
			} else {
				switch (getNextTokenKind()) {
				case BACKTICK:		tree.addSingleValue(new Text(), consumeToken(BACKTICK)); break;
				case LBRACK:		tree.addSingleValue(new Text(), consumeToken(LBRACK)); break;
				case UNDERSCORE:	tree.addSingleValue(new Text(), consumeToken(UNDERSCORE)); break;
				}
			}
		} while(strongMultilineHasElementsAhead());
	}

	private void strongWithinEmMultiline() {
		Strong strong = new Strong();
		tree.openScope(strong);
		consumeToken(ASTERISK);
		strongWithinEmMultilineContent();
		while (textAhead()) {
			lineBreak();
			strongWithinEmMultilineContent();
		}
		consumeToken(ASTERISK);
		tree.closeScope(strong);

	}

	private void strongWithinEmMultilineContent() {
		do {
			if (hasTextAhead()) {
				text();
			} else if (hasImageAhead()) {
				image();
			} else if (hasLinkAhead()) {
				link();
			} else if (hasCodeAhead()) {
				code();
			} else {
				switch (getNextTokenKind()) {
				case BACKTICK:		tree.addSingleValue(new Text(), consumeToken(BACKTICK)); break;
				case LBRACK:		tree.addSingleValue(new Text(), consumeToken(LBRACK)); break;
				case UNDERSCORE:	tree.addSingleValue(new Text(), consumeToken(UNDERSCORE)); break;
				}
			}
		} while(strongWithinEmMultilineHasElementsAhead());
	}

	private void strongWithinEm() {
		Strong strong = new Strong();
		tree.openScope(strong);
		consumeToken(ASTERISK);
		do {
			if (hasTextAhead()) {
				text();
			} else if (hasImageAhead()) {
				image();
			} else if (hasLinkAhead()) {
				link();
			} else if (hasCodeAhead()) {
				code();
			} else {
				switch (getNextTokenKind()) {
				case BACKTICK:		tree.addSingleValue(new Text(), consumeToken(BACKTICK)); break;
				case LBRACK:		tree.addSingleValue(new Text(), consumeToken(LBRACK)); break;
				case UNDERSCORE:	tree.addSingleValue(new Text(), consumeToken(UNDERSCORE)); break;
				}
			}
		} while(strongWithinEmHasElementsAhead());
		consumeToken(ASTERISK);
		tree.closeScope(strong);
	}

	private void emMultiline() {
		Em em = new Em();
		tree.openScope(em);
		consumeToken(UNDERSCORE);
		emMultilineContent();
		while (textAhead()) {
			lineBreak();
			emMultilineContent();
		}
		consumeToken(UNDERSCORE);
		tree.closeScope(em);
	}

	private void emMultilineContent() {
		do {
			if (hasTextAhead()) {
				text();
			} else if (hasImageAhead()) {
				image();
			} else if (hasLinkAhead()) {
				link();
			} else if (multilineAhead(BACKTICK)) {
				codeMultiline();
			} else if (hasStrongWithinEmMultilineAhead()) {
				strongWithinEmMultiline();
			} else {
				switch (getNextTokenKind()) {
				case ASTERISK:	tree.addSingleValue(new Text(), consumeToken(ASTERISK)); break;
				case BACKTICK:	tree.addSingleValue(new Text(), consumeToken(BACKTICK)); break;
				case LBRACK:	tree.addSingleValue(new Text(), consumeToken(LBRACK)); break;
				}
			}
		} while(emMultilineContentHasElementsAhead());
	}

	private void emWithinStrongMultiline() {
		Em em = new Em();
		tree.openScope(em);
		consumeToken(UNDERSCORE);
		emWithinStrongMultilineContent();
		while (textAhead()) {
			lineBreak();
			emWithinStrongMultilineContent();
		}
		consumeToken(UNDERSCORE);
		tree.closeScope(em);
	}

	private void emWithinStrongMultilineContent() {
		do {
			if (hasTextAhead()) {
				text();
			} else if (hasImageAhead()) {
				image();
			} else if (hasLinkAhead()) {
				link();
			} else if (hasCodeAhead()) {
				code();
			} else {
				switch (getNextTokenKind()) {
				case ASTERISK: 	tree.addSingleValue(new Text(), consumeToken(ASTERISK)); break;
				case BACKTICK:	tree.addSingleValue(new Text(), consumeToken(BACKTICK)); break;
				case LBRACK:	tree.addSingleValue(new Text(), consumeToken(LBRACK)); break;
				}
			}
		} while (emWithinStrongMultilineContentHasElementsAhaed());
	}

	private void emWithinStrong() {
		Em em = new Em();
		tree.openScope(em);
		consumeToken(UNDERSCORE);
		do {
			if (hasTextAhead()) {
				text();
			} else if (hasImageAhead()) {
				image();
			} else if (hasLinkAhead()) {
				link();
			} else if (hasCodeAhead()) {
				code();
			} else {
				switch (getNextTokenKind()) {
				case ASTERISK:	tree.addSingleValue(new Text(), consumeToken(ASTERISK)); break;
				case BACKTICK:	tree.addSingleValue(new Text(), consumeToken(BACKTICK)); break;
				case LBRACK:	tree.addSingleValue(new Text(), consumeToken(LBRACK)); break;
				}
			}
		} while(emWithinStrongHasElementsAhead());
		consumeToken(UNDERSCORE);
		tree.closeScope(em);
	}

	private void codeMultiline() {
		Code code = new Code();
		tree.openScope(code);
		consumeToken(BACKTICK);
		codeText();
		while (textAhead()) {
			lineBreak();
			whiteSpace();
			while (getNextTokenKind() == GT) {
				consumeToken(GT);
				whiteSpace();
			}
			codeText();
		}
		consumeToken(BACKTICK);
		tree.closeScope(code);
	}
	
	private void whiteSpace() {
		while (getNextTokenKind() == SPACE || getNextTokenKind() == TAB) {
			consumeToken(getNextTokenKind());
		}
	}
	
	private int getNextTokenKind() {
		if(nextTokenKind != -1) { 
			return nextTokenKind; 
		} else if ((nextToken = token.next) == null) {
			return (nextTokenKind = (token.next = tm.getNextToken()).kind);
		}
		return (nextTokenKind = nextToken.kind);
	}
	
	private Token consumeToken(int kind) {
		Token old = token;
		if (token.next != null) {
			token = token.next;
		} else {
			token = token.next = tm.getNextToken();
		}
		nextTokenKind = -1;
		if (token.kind == kind) {
			return token;
		}
		token = old;
		return token;
	}
	
	private Token getToken(int index) {
		Token t = lookingAhead ? scanPosition : token;
		for (int i = 0; i < index; i++) {
			if (t.next != null) {
				t = t.next;
			} else {
				t = t.next = tm.getNextToken();
			}
		}
		return t;
	}
	
	private boolean hasAnyBlockElementsAhead() {
		try {
			lookAhead = 1;
			lastPosition = scanPosition = token;
			return !scanMoreBlockElements();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}
	
	private boolean blockAhead() {
		if (getToken(1).kind == EOL) {
			Token t;
			int i = 2;
			int eol = 0;
			int quoteLevel;
			do {
				quoteLevel = 0;
				do {
					t = getToken(i++);
					if (t.kind == EOL && currentBlockLevel > 0 && ++eol > 2) {
						return false;
					}
					if (t.kind == GT) {
						if (t.beginColumn == 1 && currentBlockLevel > 0 && currentQuoteLevel == 0) {
							return false;
						}
						quoteLevel++;
					}
				} while (t.kind == GT || t.kind == SPACE || t.kind == TAB);
				if (quoteLevel > currentQuoteLevel) {
					return true;
				}
				if (quoteLevel < currentQuoteLevel) {
					return false;
				}
			} while (t.kind == EOL);
			return (t.kind != EOF) && ((quoteLevel > 0 && quoteLevel == currentQuoteLevel)
					|| (t.beginColumn > ((currentBlockLevel * 4) - 2)));
		}
		return false;
	}
	
	private boolean fencesAhead() {
		if (getToken(1).kind == EOL) {
			int i = skip(2, SPACE, TAB);
			if (getToken(i).kind == BACKTICK && getToken(i + 1).kind == BACKTICK && getToken(i + 2).kind == BACKTICK) {
				i = skip(i + 3, SPACE, TAB);
				return getToken(i).kind == EOL || getToken(i).kind == EOF;
			}
		}
		return false;
	}

	private boolean listItemAhead(boolean ordered) {
		if (getToken(1).kind == EOL) {
			for (int i = 2, eol = 1;; i++) {
				Token t = getToken(i);
				if (t.kind == EOL && ++eol > 2) {
					return false;
				} else if (t.kind != SPACE && t.kind != TAB && t.kind != EOL) {
					if (currentQuoteLevel > 0) {
						return false;
					}
					if (ordered) {
						return (t.kind == DIGITS && getToken(i + 1).kind == DOT);
					}
					return (t.kind == DASH);
				}
			}
		}
		return false;
	}

	private boolean multilineAhead(Integer token) {
		if (getToken(1).kind == token && getToken(2).kind != token && getToken(2).kind != EOL) {
			for (int i = 2;; i++) {
				Token t = getToken(i);
				if (t.kind == token) {
					return true;
				} else if (t.kind == EOL) {
					i = skip(i + 1, SPACE, TAB);
					int quoteLevel = newQuoteLevel(i);
					if (quoteLevel == currentQuoteLevel) {
						i = skip(i, SPACE, TAB, GT);
						if (getToken(i).kind == token || getToken(i).kind == EOL || getToken(i).kind == DASH
								|| (getToken(i).kind == DIGITS && getToken(i + 1).kind == DOT)
								|| (getToken(i).kind == BACKTICK && getToken(i + 1).kind == BACKTICK && getToken(i + 2).kind == BACKTICK)
								|| headingAhead(i)) {
							return false;
						}
					} else {
						return false;
					}
				} else if (t.kind == EOF) {
					return false;
				}
			}
		}
		return false;
	}

	private boolean textAhead() {
		int i = skip(1, SPACE, TAB);
		if (getToken(i).kind == EOL && getToken(i + 1).kind != EOL && getToken(i + 1).kind != EOF) {
			i = skip(i + 1, SPACE, TAB);
			int quoteLevel = newQuoteLevel(i);
			if (quoteLevel == currentQuoteLevel) {
				i = skip(i, SPACE, TAB, GT);
				return getToken(i).kind != EOL && getToken(i).kind != DASH
						&& !(getToken(i).kind == DIGITS && getToken(i + 1).kind == DOT)
						&& !(getToken(i).kind == BACKTICK && getToken(i + 1).kind == BACKTICK && getToken(i + 2).kind == BACKTICK)
						&& !headingAhead(i);
			}
		}
		return false;
	}

	private boolean nextAfterSpace(Integer... tokens) {
		int i = skip(1, SPACE, TAB);
		return Arrays.asList(tokens).contains(getToken(i).kind);
	}

	private int newQuoteLevel(int offset) {
		int quoteLevel = 0;
		for (int i = offset;; i++) {
			Token t = getToken(i);
			if (t.kind == GT) {
				quoteLevel++;
			} else if (t.kind != SPACE && t.kind != TAB) {
				return quoteLevel;
			}
		}
	}

	private int skip(int offset, Integer... tokens) {
		for (int i = offset;; i++) {
			Token t = getToken(i);
			if (t.kind == EOF || !Arrays.asList(tokens).contains(t.kind)) {
				return i;
			}
		}
	}
	
	private boolean headingAhead(int offset) {
		if (getToken(offset).kind == EQ) {
			int heading = 1;
			for (int i = (offset + 1);; i++) {
				if (getToken(i).kind != EQ) {
					return true;
				}
				if (++heading > 6) {
					return false;
				}
			}
		}
		return false;
	}
	
	private boolean hasOrderedListAhead() {
		lookAhead = 2;
		lastPosition = scanPosition = token;
		try {
			return !scanToken(DIGITS) && !scanToken(DOT);
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}
	
	private boolean hasFencedCodeBlockAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanFencedCodeBlock();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}
	
	
	private boolean headingHasInlineElementsAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			Token xsp = scanPosition;
			if (scanTextTokens()) {
				scanPosition = xsp;
				if (scanImage()) {
					scanPosition = xsp;
					if (scanLink()) {
						scanPosition = xsp;
						if (scanStrong()) {
							scanPosition = xsp;
							if (scanEm()) {
								scanPosition = xsp;
								if (scanCode()) {
									scanPosition = xsp;
									if (scanLooseChar()) {
										return false;
									}
								}
							}
						}
					}
				}
			}
			return true;
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}
	
	private boolean hasTextAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanTextTokens();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean hasImageAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanImage();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}
	
	private boolean blockquoteHasEmptyLineAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scranBlockquoteEmptyLine();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean hasLinkAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanLink();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean hasInlineStrongAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanStrong();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean hasInlineEmAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanEm();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean hasCodeAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanCode();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean blockquoteHasAnyBlockElementseAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanMoreBlockElements();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean hasBlockquoteEmptyLines() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanBlockquoteEmptyLines();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean listItemHasInlineElements() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanMoreBlockElements();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean fencedCodeBlockHasInlineTokens() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanFencedCodeBlockTokens();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean hasInlineTextAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanTextTokens();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean hasInlineElementAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanInlineElement();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean imageHasAnyElements() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanImageElement();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean imageHasResourceUrlAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanResourceUrl();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean linkStrongAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanStrong();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean linkEmAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanEm();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean linkCodeAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanCode();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean linkResourceTextAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanResourceElements();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean linkHasAnyElements() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanLinkElement();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean linkHasUrl() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanResourceUrl();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean resourceHasElementAhead() {
		lookAhead = 2;
		lastPosition = scanPosition = token;
		try {
			return !scanResourceElement();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean resourceTextHasElementsAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanResourceTextElement();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean hasEmWithinStrongMultiline() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanEmWithinStrongMultiline();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean strongMultilineHasElementsAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanStrongMultilineElements();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}
	
	private boolean strongWithinEmMultilineHasElementsAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanStrongWithinEmMultilineElements();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean strongHasImage() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanImage();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean strongHasLink() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanLink();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean strongEmWithinStrongAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanEmWithinStrong();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean strongHasElements() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanStrongElements();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean strongWithinEmHasElementsAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanStrongWithinEmElements();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean hasStrongWithinEmMultilineAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanStrongWithinEmMultiline();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean emMultilineContentHasElementsAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanEmMultilineContentElements();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean emWithinStrongMultilineContentHasElementsAhaed() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanEmWithinStrongMultilineContent();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean emHasText() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanTextTokens();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean emHasImage() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanImage();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean emHasLink() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanLink();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean emHasCode() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanCode();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean emHasStrongWithinEm() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !scanStrongWithinEm();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean emHasElements() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanEmElements();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean emWithinStrongHasElementsAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanEmWithinStrongElements();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean codeTextHasAnyTokenAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanCodeTextTokens();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean textHasTokensAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanText();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean scanLooseChar() {
		Token xsp = scanPosition;
		if (scanToken(ASTERISK)) {
			scanPosition = xsp;
			if (scanToken(BACKTICK)) {
				scanPosition = xsp;
				if (scanToken(LBRACK)) {
					scanPosition = xsp;
					return scanToken(UNDERSCORE);
				}
			}
		}
		return false;
	}

	private boolean scanText() {
		Token xsp = scanPosition;
		if (scanToken(BACKSLASH)) {
			scanPosition = xsp;
			if (scanToken(CHAR_SEQUENCE)) {
				scanPosition = xsp;
				if (scanToken(COLON)) {
					scanPosition = xsp;
					if (scanToken(DASH)) {
						scanPosition = xsp;
						if (scanToken(DIGITS)) {
							scanPosition = xsp;
							if (scanToken(DOT)) {
								scanPosition = xsp;
								if (scanToken(EQ)) {
									scanPosition = xsp;
									if (scanToken(ESCAPED_CHAR)) {
										scanPosition = xsp;
										if (scanToken(GT)) {
											scanPosition = xsp;
											if (scanToken(IMAGE_LABEL)) {
												scanPosition = xsp;
												if (scanToken(LPAREN)) {
													scanPosition = xsp;
													if (scanToken(LT)) {
														scanPosition = xsp;
														if (scanToken(RBRACK)) {
															scanPosition = xsp;
															if (scanToken(RPAREN)) {
																scanPosition = xsp;
																lookingAhead = true;
																semanticLookAhead = !nextAfterSpace(EOL, EOF);
																lookingAhead = false;
																if (!semanticLookAhead || scanWhitspaceToken()) {
																	return true;
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanTextTokens() {
		if (scanText()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanText()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}
	
	private boolean scanCodeTextTokens() {
		Token xsp = scanPosition;
		if (scanToken(ASTERISK)) {
			scanPosition = xsp;
			if (scanToken(BACKSLASH)) {
				scanPosition = xsp;
				if (scanToken(CHAR_SEQUENCE)) {
					scanPosition = xsp;
					if (scanToken(COLON)) {
						scanPosition = xsp;
						if (scanToken(DASH)) {
							scanPosition = xsp;
							if (scanToken(DIGITS)) {
								scanPosition = xsp;
								if (scanToken(DOT)) {
									scanPosition = xsp;
									if (scanToken(EQ)) {
										scanPosition = xsp;
										if (scanToken(ESCAPED_CHAR)) {
											scanPosition = xsp;
											if (scanToken(IMAGE_LABEL)) {
												scanPosition = xsp;
												if (scanToken(LT)) {
													scanPosition = xsp;
													if (scanToken(LBRACK)) {
														scanPosition = xsp;
														if (scanToken(RBRACK)) {
															scanPosition = xsp;
															if (scanToken(LPAREN)) {
																scanPosition = xsp;
																if (scanToken(GT)) {
																	scanPosition = xsp;
																	if (scanToken(RPAREN)) {
																		scanPosition = xsp;
																		if (scanToken(UNDERSCORE)) {
																			scanPosition = xsp;
																			lookingAhead = true;
																			semanticLookAhead = !nextAfterSpace(EOL, EOF);
																			lookingAhead = false;
																			if (!semanticLookAhead || scanWhitspaceToken()) {
																				return true;
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}


	private boolean scanCode() {
		return scanToken(BACKTICK) || scanCodeTextTokensAhead() || scanToken(BACKTICK);
	}

	private boolean scanCodeMultiline() {
		if (scanToken(BACKTICK) || scanCodeTextTokensAhead()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (hasCodeTextOnNextLineAhead()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(BACKTICK);
	}
	
	private boolean scanCodeTextTokensAhead() {
		if (scanCodeTextTokens()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanCodeTextTokens()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean hasCodeTextOnNextLineAhead() {
		if (scanWhitespaceTokenBeforeEol()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanToken(GT)) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanCodeTextTokensAhead();
	}
	
	private boolean scanWhitspaceTokens() {
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanWhitspaceToken()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean scanWhitespaceTokenBeforeEol() {
		return scanWhitspaceTokens() || scanToken(EOL);
	}

	private boolean scanEmWithinStrongElements() {
		Token xsp = scanPosition;
		if (scanTextTokens()) {
			scanPosition = xsp;
			if (scanImage()) {
				scanPosition = xsp;
				if (scanLink()) {
					scanPosition = xsp;
					if (scanCode()) {
						scanPosition = xsp;
						if (scanToken(ASTERISK)) {
							scanPosition = xsp;
							if (scanToken(BACKTICK)) {
								scanPosition = xsp;
								return scanToken(LBRACK);
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanEmWithinStrong() {
		if (scanToken(UNDERSCORE) || scanEmWithinStrongElements()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanEmWithinStrongElements()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(UNDERSCORE);
	}

	private boolean scanEmElements() {
		Token xsp = scanPosition;
		if (scanTextTokens()) {
			scanPosition = xsp;
			if (scanImage()) {
				scanPosition = xsp;
				if (scanLink()) {
					scanPosition = xsp;
					if (scanCode()) {
						scanPosition = xsp;
						if (scanStrongWithinEm()) {
							scanPosition = xsp;
							if (scanToken(ASTERISK)) {
								scanPosition = xsp;
								if (scanToken(BACKTICK)) {
									scanPosition = xsp;
									return scanToken(LBRACK);
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanEm() {
		if (scanToken(UNDERSCORE) || scanEmElements()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanEmElements()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(UNDERSCORE);
	}

	private boolean scanEmWithinStrongMultilineContent() {
		Token xsp = scanPosition;
		if (scanTextTokens()) {
			scanPosition = xsp;
			if (scanImage()) {
				scanPosition = xsp;
				if (scanLink()) {
					scanPosition = xsp;
					if (scanCode()) {
						scanPosition = xsp;
						if (scanToken(ASTERISK)) {
							scanPosition = xsp;
							if (scanToken(BACKTICK)) {
								scanPosition = xsp;
								return scanToken(LBRACK);
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean hasNoEmWithinStrongMultilineContentAhead() {
		if (scanEmWithinStrongMultilineContent()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanEmWithinStrongMultilineContent()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean scanEmWithinStrongMultiline() {
		if (scanToken(UNDERSCORE) || hasNoEmWithinStrongMultilineContentAhead()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanWhitespaceTokenBeforeEol() || hasNoEmWithinStrongMultilineContentAhead()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(UNDERSCORE);
	}

	private boolean scanEmMultilineContentElements() {
		Token xsp = scanPosition;
		if (scanTextTokens()) {
			scanPosition = xsp;
			if (scanImage()) {
				scanPosition = xsp;
				if (scanLink()) {
					scanPosition = xsp;
					lookingAhead = true;
					semanticLookAhead = multilineAhead(BACKTICK);
					lookingAhead = false;
					if (!semanticLookAhead || scanCodeMultiline()) {
						scanPosition = xsp;
						if (scanStrongWithinEmMultiline()) {
							scanPosition = xsp;
							if (scanToken(ASTERISK)) {
								scanPosition = xsp;
								if (scanToken(BACKTICK)) {
									scanPosition = xsp;
									return scanToken(LBRACK);
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanStrongWithinEmElements() {
		Token xsp = scanPosition;
		if (scanTextTokens()) {
			scanPosition = xsp;
			if (scanImage()) {
				scanPosition = xsp;
				if (scanLink()) {
					scanPosition = xsp;
					if (scanCode()) {
						scanPosition = xsp;
						if (scanToken(BACKTICK)) {
							scanPosition = xsp;
							if (scanToken(LBRACK)) {
								scanPosition = xsp;
								return scanToken(UNDERSCORE);
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanStrongWithinEm() {
		if (scanToken(ASTERISK) || scanStrongWithinEmElements()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanStrongWithinEmElements()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(ASTERISK);
	}

	private boolean scanStrongElements() {
		Token xsp = scanPosition;
		if (scanTextTokens()) {
			scanPosition = xsp;
			if (scanImage()) {
				scanPosition = xsp;
				if (scanLink()) {
					scanPosition = xsp;
					lookingAhead = true;
					semanticLookAhead = multilineAhead(BACKTICK);
					lookingAhead = false;
					if (!semanticLookAhead || scanCodeMultiline()) {
						scanPosition = xsp;
						if (scanEmWithinStrong()) {
							scanPosition = xsp;
							if (scanToken(BACKTICK)) {
								scanPosition = xsp;
								if (scanToken(LBRACK)) {
									scanPosition = xsp;
									return scanToken(UNDERSCORE);
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanStrong() {
		if (scanToken(ASTERISK) || scanStrongElements()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanStrongElements()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(ASTERISK);
	}

	private boolean scanStrongWithinEmMultilineElements() {
		Token xsp = scanPosition;
		if (scanTextTokens()) {
			scanPosition = xsp;
			if (scanImage()) {
				scanPosition = xsp;
				if (scanLink()) {
					scanPosition = xsp;
					if (scanCode()) {
						scanPosition = xsp;
						if (scanToken(BACKTICK)) {
							scanPosition = xsp;
							if (scanToken(LBRACK)) {
								scanPosition = xsp;
								return scanToken(UNDERSCORE);
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanForMoreStrongWithinEmMultilineElements() {
		if (scanStrongWithinEmMultilineElements()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanStrongWithinEmMultilineElements()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean scanStrongWithinEmMultiline() {
		if (scanToken(ASTERISK) || scanForMoreStrongWithinEmMultilineElements()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanWhitespaceTokenBeforeEol() || scanForMoreStrongWithinEmMultilineElements()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(ASTERISK);
	}

	private boolean scanStrongMultilineElements() {
		Token xsp = scanPosition;
		if (scanTextTokens()) {
			scanPosition = xsp;
			if (scanImage()) {
				scanPosition = xsp;
				if (scanLink()) {
					scanPosition = xsp;
					if (scanCode()) {
						scanPosition = xsp;
						if (scanEmWithinStrongMultiline()) {
							scanPosition = xsp;
							if (scanToken(BACKTICK)) {
								scanPosition = xsp;
								if (scanToken(LBRACK)) {
									scanPosition = xsp;
									return scanToken(UNDERSCORE);
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanResourceTextElement() {
		Token xsp = scanPosition;
		if (scanToken(ASTERISK)) {
			scanPosition = xsp;
			if (scanToken(BACKSLASH)) {
				scanPosition = xsp;
				if (scanToken(BACKTICK)) {
					scanPosition = xsp;
					if (scanToken(CHAR_SEQUENCE)) {
						scanPosition = xsp;
						if (scanToken(COLON)) {
							scanPosition = xsp;
							if (scanToken(DASH)) {
								scanPosition = xsp;
								if (scanToken(DIGITS)) {
									scanPosition = xsp;
									if (scanToken(DOT)) {
										scanPosition = xsp;
										if (scanToken(EQ)) {
											scanPosition = xsp;
											if (scanToken(ESCAPED_CHAR)) {
												scanPosition = xsp;
												if (scanToken(IMAGE_LABEL)) {
													scanPosition = xsp;
													if (scanToken(GT)) {
														scanPosition = xsp;
														if (scanToken(LBRACK)) {
															scanPosition = xsp;
															if (scanToken(LPAREN)) {
																scanPosition = xsp;
																if (scanToken(LT)) {
																	scanPosition = xsp;
																	if (scanToken(RBRACK)) {
																		scanPosition = xsp;
																		if (scanToken(UNDERSCORE)) {
																			scanPosition = xsp;
																			lookingAhead = true;
																			semanticLookAhead = !nextAfterSpace(RPAREN);
																			lookingAhead = false;
																			return (!semanticLookAhead || scanWhitspaceToken());
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanImageElement() {
		Token xsp = scanPosition;
		if (scanResourceElements()) {
			scanPosition = xsp;
			if (scanLooseChar()) {
				return true;
			}
		}
		return false;
	}

	private boolean scanResourceTextElements() {
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanResourceTextElement()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean scanResourceUrl() {
		return scanToken(LPAREN) || scanWhitspaceTokens() || scanResourceTextElements() || scanWhitspaceTokens() || scanToken(RPAREN);
	}

	private boolean scanLinkElement() {
		Token xsp = scanPosition;
		if (scanImage()) {
			scanPosition = xsp;
			if (scanStrong()) {
				scanPosition = xsp;
				if (scanEm()) {
					scanPosition = xsp;
					if (scanCode()) {
						scanPosition = xsp;
						if (scanResourceElements()) {
							scanPosition = xsp;
							return scanLooseChar();
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanResourceElement() {
		Token xsp = scanPosition;
		if (scanToken(BACKSLASH)) {
			scanPosition = xsp;
			if (scanToken(COLON)) {
				scanPosition = xsp;
				if (scanToken(CHAR_SEQUENCE)) {
					scanPosition = xsp;
					if (scanToken(DASH)) {
						scanPosition = xsp;
						if (scanToken(DIGITS)) {
							scanPosition = xsp;
							if (scanToken(DOT)) {
								scanPosition = xsp;
								if (scanToken(EQ)) {
									scanPosition = xsp;
									if (scanToken(ESCAPED_CHAR)) {
										scanPosition = xsp;
										if (scanToken(IMAGE_LABEL)) {
											scanPosition = xsp;
											if (scanToken(GT)) {
												scanPosition = xsp;
												if (scanToken(LPAREN)) {
													scanPosition = xsp;
													if (scanToken(LT)) {
														scanPosition = xsp;
														if (scanToken(RPAREN)) {
															scanPosition = xsp;
															lookingAhead = true;
															semanticLookAhead = !nextAfterSpace(RBRACK);
															lookingAhead = false;
															return (!semanticLookAhead || scanWhitspaceToken());
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanResourceElements() {
		if (scanResourceElement()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanResourceElement()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean scanLink() {
		if (scanToken(LBRACK) || scanWhitspaceTokens() || scanLinkElement()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanLinkElement()) {
				scanPosition = xsp;
				break loop;
			}
		}
		if (scanWhitspaceTokens() || scanToken(RBRACK)) {
			return true;
		}
		xsp = scanPosition;
		if (scanResourceUrl()) {
			scanPosition = xsp;
		}
		return false;
	}

	private boolean scanImage() {
		if (scanToken(LBRACK) || scanWhitspaceTokens() || scanToken(IMAGE_LABEL) || scanImageElement()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanImageElement()) {
				scanPosition = xsp;
				break loop;
			}
		}
		if (scanWhitspaceTokens() || scanToken(RBRACK)) {
			return true;
		}
		xsp = scanPosition;
		if (scanResourceUrl()) {
			scanPosition = xsp;
		}
		return false;
	}

	private boolean scanInlineElement() {
		Token xsp = scanPosition;
		if (scanTextTokens()) {
			scanPosition = xsp;
			if (scanImage()) {
				scanPosition = xsp;
				if (scanLink()) {
					scanPosition = xsp;
					lookingAhead = true;
					semanticLookAhead = multilineAhead(ASTERISK);
					lookingAhead = false;
					if (!semanticLookAhead || scanToken(ASTERISK)) {
						scanPosition = xsp;
						lookingAhead = true;
						semanticLookAhead = multilineAhead(UNDERSCORE);
						lookingAhead = false;
						if (!semanticLookAhead || scanToken(UNDERSCORE)) {
							scanPosition = xsp;
							lookingAhead = true;
							semanticLookAhead = multilineAhead(BACKTICK);
							lookingAhead = false;
							if (!semanticLookAhead || scanCodeMultiline()) {
								scanPosition = xsp;
								return scanLooseChar();
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanParagraph() {
		Token xsp;
		if (scanInlineElement()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (scanInlineElement()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean scanForCodeLanguageElement() {
		Token xsp = scanPosition;
		if (scanToken(CHAR_SEQUENCE)) {
			scanPosition = xsp;
			if (scanToken(BACKTICK)) {
				return true;
			}
		}
		return false;
	}

	private boolean scanForCodeLanguageElements() {
		if (scanForCodeLanguageElement()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanForCodeLanguageElement()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean scanWhitspaceToken() {
		Token xsp = scanPosition;
		if (scanToken(SPACE)) {
			scanPosition = xsp;
			if (scanToken(TAB)) {
				return true;
			}
		}
		return false;
	}
	private boolean scanNoFencedCodeBlockAhead() {
		if (scanToken(EOL) || scanWhitspaceTokens() || scanToken(BACKTICK)) {
			return true;
		}
		Token xsp;
		while (true) {
			xsp = scanPosition;
			if (scanToken(BACKTICK)) {
				scanPosition = xsp;
				break;
			}
		}
		return false;
	}
	
	private boolean scanFencedCodeBlockTokens() {
		Token xsp = scanPosition;
		if (scanToken(ASTERISK)) {
			scanPosition = xsp;
			if (scanToken(BACKSLASH)) {
				scanPosition = xsp;
				if (scanToken(CHAR_SEQUENCE)) {
					scanPosition = xsp;
					if (scanToken(COLON)) {
						scanPosition = xsp;
						if (scanToken(DASH)) {
							scanPosition = xsp;
							if (scanToken(DIGITS)) {
								scanPosition = xsp;
								if (scanToken(DOT)) {
									scanPosition = xsp;
									if (scanToken(EQ)) {
										scanPosition = xsp;
										if (scanToken(ESCAPED_CHAR)) {
											scanPosition = xsp;
											if (scanToken(IMAGE_LABEL)) {
												scanPosition = xsp;
												if (scanToken(LT)) {
													scanPosition = xsp;
													if (scanToken(GT)) {
														scanPosition = xsp;
														if (scanToken(LBRACK)) {
															scanPosition = xsp;
															if (scanToken(RBRACK)) {
																scanPosition = xsp;
																if (scanToken(LPAREN)) {
																	scanPosition = xsp;
																	if (scanToken(RPAREN)) {
																		scanPosition = xsp;
																		if (scanToken(UNDERSCORE)) {
																			scanPosition = xsp;
																			if (scanToken(BACKTICK)) {
																				scanPosition = xsp;
																				lookingAhead = true;
																				semanticLookAhead = !nextAfterSpace(EOL, EOF);
																				lookingAhead = false;
																				if (!semanticLookAhead || scanWhitspaceToken()) {
																					scanPosition = xsp;
																					lookingAhead = true;
																					semanticLookAhead = !fencesAhead();
																					lookingAhead = false;
																					return !semanticLookAhead || scanToken(EOL) || scanWhitspaceTokens();
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanFencedCodeBlock() {
		if (scanToken(BACKTICK) || scanToken(BACKTICK) || scanToken(BACKTICK)) {
			return true;
		}
		Token xsp;
		while (true) {
			xsp = scanPosition;
			if (scanToken(BACKTICK)) {
				scanPosition = xsp;
				break;
			}
		}
		if (scanWhitspaceTokens()) {
			return true;
		}
		xsp = scanPosition;
		if (scanForCodeLanguageElements()) {
			scanPosition = xsp;
		}
		xsp = scanPosition;
		if (scanToken(EOL) || scanWhitspaceTokens()) {
			scanPosition = xsp;
		}
		while (true) {
			xsp = scanPosition;
			if (scanFencedCodeBlockTokens()) {
				scanPosition = xsp;
				break;
			}
		}
		xsp = scanPosition;
		if (scanNoFencedCodeBlockAhead()) {
			scanPosition = xsp;
		}
		return false;
	}

	private boolean scanBlockquoteEmptyLines() {
		return scranBlockquoteEmptyLine() || scanToken(EOL);
	}

	private boolean scranBlockquoteEmptyLine() {
		if (scanToken(EOL) || scanWhitspaceTokens() || scanToken(GT) || scanWhitspaceTokens()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanToken(GT) || scanWhitspaceTokens()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean scanForHeadersigns() {
		if (scanToken(EQ)) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (scanToken(EQ)) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean scanMoreBlockElements() {
		Token xsp = scanPosition;
		lookingAhead = true;
		semanticLookAhead = headingAhead(1);
		lookingAhead = false;
		if (!semanticLookAhead || scanForHeadersigns()) {
			scanPosition = xsp;
			if (scanToken(GT)) {
				scanPosition = xsp;
				if (scanToken(DASH)) {
					scanPosition = xsp;
					if (scanToken(DIGITS) || scanToken(DOT)) {
						scanPosition = xsp;
						if (scanFencedCodeBlock()) {
							scanPosition = xsp;
							return scanParagraph();
						}
					}
				}
			}
		}
		return false;
	}

	private boolean scanToken(int kind) {
		if (scanPosition == lastPosition) {
			lookAhead--;
			if (scanPosition.next == null) {
				lastPosition = scanPosition = scanPosition.next = tm.getNextToken();
			} else {
				lastPosition = scanPosition = scanPosition.next;
			}
		} else {
			scanPosition = scanPosition.next;
		}
		if (scanPosition.kind != kind) {
			return true;
		}
		if (lookAhead == 0 && scanPosition == lastPosition) {
			throw lookAheadSuccess;
		}
		return false;
	}
	
}
