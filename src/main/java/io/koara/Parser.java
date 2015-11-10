package io.koara;

import static io.koara.TokenManager.ASTERISK;
import static io.koara.TokenManager.BACKSLASH;
import static io.koara.TokenManager.BACKTICK;
import static io.koara.TokenManager.CHAR_SEQUENCE;
import static io.koara.TokenManager.COLON;
import static io.koara.TokenManager.DASH;
import static io.koara.TokenManager.DIGITS;
import static io.koara.TokenManager.DOT;
import static io.koara.TokenManager.EOF;
import static io.koara.TokenManager.EOL;
import static io.koara.TokenManager.EQ;
import static io.koara.TokenManager.ESCAPED_CHAR;
import static io.koara.TokenManager.GT;
import static io.koara.TokenManager.IMAGE_LABEL;
import static io.koara.TokenManager.LBRACK;
import static io.koara.TokenManager.LPAREN;
import static io.koara.TokenManager.LT;
import static io.koara.TokenManager.RBRACK;
import static io.koara.TokenManager.RPAREN;
import static io.koara.TokenManager.SPACE;
import static io.koara.TokenManager.TAB;
import static io.koara.TokenManager.UNDERSCORE;

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
	private int nextTokenKind;
	private int lookAhead;
	private boolean lookingAhead = false;
	private boolean semanticLookAhead;
	
	public class LookaheadSuccess extends Error {

	}
	
	private LookaheadSuccess lookAheadSuccess = new LookaheadSuccess();
	
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
		leadingLines: while (true) {
			switch (getNextTokenKind()) {
				case EOL: consumeToken(EOL); break;
				default: break leadingLines;
			}		
		}
		whiteSpace();
		if (hasAnyBlockElementsAhead()) {
			blockElement();
			blockElements: while (true) {
				if (!blockAhead()) {
					break blockElements;
				}
				moreBlockElements: while (true) {
					consumeToken(EOL);
					whiteSpace();			
					if(getNextTokenKind() != EOL) {
						break moreBlockElements;
					}
				}
				blockElement();
			}
			trailingLines: while (true) {
				if(getNextTokenKind() != EOL) {
					break trailingLines;
				}
				consumeToken(EOL);
			}
			whiteSpace();
		} 
		consumeToken(EOF);
		tree.closeScope(document);
		return document;
	}
	
	private boolean hasAnyBlockElementsAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !scanForBlockElement();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private void blockElement() {
		currentBlockLevel++;
		if (headingAhead(1)) {
			heading();
		} else if(getNextTokenKind() == GT) {
			blockquote();
		} else if(getNextTokenKind() == DASH) {
			unorderedList();
		} else if(orderedListAhead(2)) {
			orderedList();
		} else if(fencedCodeBlockAhead(2147483647)) {
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
	
		equalsChars: while (true) {
			consumeToken(EQ);
			headingLevel++;
			if(getNextTokenKind() != EQ) { 
				break equalsChars;
			}
		}
		whiteSpace();
		inline: while (true) {
			if (!headingInlineAhead()) {
				break inline;
			}
			if (inlineTextAhead()) {
				text();
			} else if (inlineImageAhead()) {
				image();
			} else if (jj_2_8(2147483647)) {
				link();
			} else if (jj_2_9(2147483647)) {
				strong();
			} else if (jj_2_10(2147483647)) {
				em();
			} else if (jj_2_11(2147483647)) {
				code();
			} else {
				switch (getNextTokenKind()) {
				case ASTERISK:
				case BACKTICK:
				case LBRACK:
				case UNDERSCORE: {
					looseChar();
					break;
				}
				}
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
		leadingLines: while (true) {
			if (!jj_2_12(2147483647)) {
				break leadingLines;
			}
			blockquoteEmptyLine();
		}
		whiteSpace();
		if (jj_2_13(1)) {
			blockElement();
			label_8: while (true) {
				if (!blockAhead()) {
					break label_8;
				}
				label_9: while (true) {
					consumeToken(EOL);
					whiteSpace();
					blockquotePrefix();
					if(getNextTokenKind() != EOL) {
						break label_9;
					}
				}
				blockElement();
			}
		}
		trailingLines: while (true) {
			if (!jj_2_14(2147483647)) {
				break trailingLines;
			}
			blockquoteEmptyLine();
		}
		currentQuoteLevel--;
		tree.closeScope(blockquote);
	}

	private void blockquotePrefix() {
		int i = 0;
		loop: while (true) {
			consumeToken(GT);
			whiteSpace();
			if (++i >= currentQuoteLevel) {
				break loop;
			}
		}
	}

	private void blockquoteEmptyLine() {
		consumeToken(EOL);
		whiteSpace();
		loop: while (true) {
			consumeToken(GT);
			whiteSpace();
			if(getNextTokenKind() != GT) {
				break loop;
			}
		}
	}

	private void unorderedList() {
		List list = new List();
		tree.openScope(list);
		unorderedListItem();
		label_13: while (true) {
			if (!listItemAhead(false)) {
				break label_13;
			}
			label_14: while (true) {
				consumeToken(EOL);
				if(getNextTokenKind() != EOL) {
					break label_14;
				}
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
		if (jj_2_15(1)) {
			blockElement();
			label_15: while (true) {
				if (!blockAhead()) {
					break label_15;
				}
				label_16: while (true) {
					consumeToken(EOL);
					whiteSpace();
					if (currentQuoteLevel > 0) {
						blockquotePrefix();
					}
					if(getNextTokenKind() != EOL) {
						break label_16;
					}
				}
				blockElement();
			}
		} 
		tree.closeScope(listItem);
	}

	private void orderedList() {
		List list = new List();
		tree.openScope(list);
		orderedListItem();
		label_17: while (true) {
			if (!listItemAhead(true)) {
				break label_17;
			}
			label_18: while (true) {
				consumeToken(EOL);				
				if(getNextTokenKind() != EOL) {
					break label_18;
				}
			}
			whiteSpace();
			orderedListItem();
		}
		list.setOrdered(true);
		tree.closeScope(list);
	}

	private void orderedListItem() {
		ListItem listItem = new ListItem();
		tree.openScope(listItem);
		Token t;
		t = consumeToken(DIGITS);
		consumeToken(DOT);
		whiteSpace();
		if (jj_2_16(1)) {
			blockElement();
			label_19: while (true) {
				if (!blockAhead()) {
					break label_19;
				}
				label_20: while (true) {
					consumeToken(EOL);
					whiteSpace();
					if (currentQuoteLevel > 0) {
						blockquotePrefix();
					}					
					if(getNextTokenKind() != EOL) {
						break label_20;
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
		Token t;
		String language;
		StringBuilder s = new StringBuilder();
		int beginColumn;
		t = consumeToken(BACKTICK);
		beginColumn = t.beginColumn;
		consumeToken(BACKTICK);
		label_21: while (true) {
			consumeToken(BACKTICK);
			if(getNextTokenKind() != BACKTICK) {
				break label_21;
			}
		}
		whiteSpace();
		switch (getNextTokenKind()) {
		case BACKTICK:
		case CHAR_SEQUENCE: {
			language = codeLanguage();
			codeBlock.setLanguage(language);
			break;
		}
		default:
		}
		if (getToken(1).kind != EOF && !fencesAhead()) {
			consumeToken(EOL);
			levelWhiteSpace(beginColumn);
		}
		label_22: while (true) {
			if (!jj_2_17(1)) {
				break label_22;
			}
			switch (getNextTokenKind()) {
			case ASTERISK: {
				s.append(consumeToken(ASTERISK).image);
				break;
			}
			case BACKSLASH: {
				s.append(consumeToken(BACKSLASH).image);
				break;
			}
			case CHAR_SEQUENCE: {
				s.append(consumeToken(CHAR_SEQUENCE).image);
				break;
			}
			case COLON: {
				s.append(consumeToken(COLON).image);
				break;
			}
			case DASH: {
				s.append(consumeToken(DASH).image);
				break;
			}
			case DIGITS: {
				s.append(consumeToken(DIGITS).image);
				break;
			}
			case DOT: {
				s.append(consumeToken(DOT).image);
				break;
			}
			case EQ: {
				s.append(consumeToken(EQ).image);
				break;
			}
			case ESCAPED_CHAR: {
				s.append(consumeToken(ESCAPED_CHAR).image);
				break;
			}
			case IMAGE_LABEL: {
				s.append(consumeToken(IMAGE_LABEL).image);
				break;
			}
			case LT: {
				s.append(consumeToken(LT).image);
				break;
			}
			case GT: {
				s.append(consumeToken(GT).image);
				break;
			}
			case LBRACK: {
				s.append(consumeToken(LBRACK).image);
				break;
			}
			case RBRACK: {
				s.append(consumeToken(RBRACK).image);
				break;
			}
			case LPAREN: {
				s.append(consumeToken(LPAREN).image);
				break;
			}
			case RPAREN: {
				s.append(consumeToken(RPAREN).image);
				break;
			}
			case UNDERSCORE: {
				s.append(consumeToken(UNDERSCORE).image);
				break;
			}
			case BACKTICK: {
				s.append(consumeToken(BACKTICK).image);
				break;
			}
			default:
				if (!nextAfterSpace(EOL, EOF)) {
					switch (getNextTokenKind()) {
					case SPACE: {
						s.append(consumeToken(SPACE).image);
						break;
					}
					case TAB: {
						consumeToken(TAB);
						s.append("    ");
						break;
					}
					}
				} else if (!fencesAhead()) {
					consumeToken(EOL);
					s.append("\u005cn");
					levelWhiteSpace(beginColumn);
				}
			}
		}
		if (fencesAhead()) {
			consumeToken(EOL);
			whiteSpace();
			consumeToken(BACKTICK);
			consumeToken(BACKTICK);
			label_23: while (true) {
				consumeToken(BACKTICK);
				if(getNextTokenKind() != BACKTICK) {
					break label_23;
				}
			}
		}
		codeBlock.setValue(s.toString());
		tree.closeScope(codeBlock);
	}

	private void levelWhiteSpace(int threshold) {
		Token t;
		int currentPos = 1;
		loop: while (true) {
			if (!((getNextTokenKind() == SPACE || getNextTokenKind() == TAB) && currentPos < (threshold - 1))) {
				break loop;
			}
			switch (getNextTokenKind()) {
			case SPACE: {
				t = consumeToken(SPACE);
				currentPos = t.beginColumn;
				break;
			}
			case TAB: {
				t = consumeToken(TAB);
				currentPos = t.beginColumn;
				break;
			}
			}
		}
	}

	private String codeLanguage() {
		StringBuilder s = new StringBuilder();
		loop: while (true) {
			switch (getNextTokenKind()) {
			case CHAR_SEQUENCE: {
				s.append(consumeToken(CHAR_SEQUENCE).image);
				break;
			}
			case BACKTICK: {
				s.append(consumeToken(BACKTICK).image);
				break;
			}
			}
			if(getNextTokenKind() != BACKTICK && getNextTokenKind() != CHAR_SEQUENCE) {
				break loop;
			}
		}
		return s.toString();
	}

	private void paragraph() {
		Paragraph paragraph = new Paragraph();
		tree.openScope(paragraph);
		inline();
		label_26: while (true) {
			if (!textAhead()) {
				break label_26;
			}
			lineBreak();
			whiteSpace();
			label_27: while (true) {
				if(getNextTokenKind() != GT) {
					break label_27;
				}
				consumeToken(GT);
				whiteSpace();
			}
			inline();
		}
		tree.closeScope(paragraph);
	}

	private void inline() {
		loop: while (true) {
			if (jj_2_18(1)) {
				text();
			} else if (jj_2_19(2147483647)) {
				image();
			} else if (jj_2_20(2147483647)) {
				link();
			} else if (multilineAhead(ASTERISK)) {
				strongMultiline();
			} else if (multilineAhead(UNDERSCORE)) {
				emMultiline();
			} else if (multilineAhead(BACKTICK)) {
				codeMultiline();
			} else {
				switch (getNextTokenKind()) {
				case ASTERISK:
				case BACKTICK:
				case LBRACK:
				case UNDERSCORE: {
					looseChar();
					break;
				}
				}
			}
			if (!jj_2_21(1)) {
				break loop;
			}
		}
	}

	private void image() {
		Image image = new Image();
		tree.openScope(image);
		String ref = "";
		consumeToken(LBRACK);
		whiteSpace();
		consumeToken(IMAGE_LABEL);
		whiteSpace();
		loop: while (true) {
			if (jj_2_22(1)) {
				resourceText();
			} else {
				switch (getNextTokenKind()) {
				case ASTERISK:
				case BACKTICK:
				case LBRACK:
				case UNDERSCORE: {
					looseChar();
					break;
				}
				}
			}
			if (!jj_2_23(1)) {
				break loop;
			}
		}
		whiteSpace();
		consumeToken(RBRACK);
		if (jj_2_24(2147483647)) {
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
		loop: while (true) {
			if (jj_2_25(2147483647)) {
				image();
			} else if (jj_2_26(2147483647)) {
				strong();
			} else if (jj_2_27(2147483647)) {
				em();
			} else if (jj_2_28(2147483647)) {
				code();
			} else if (jj_2_29(1)) {
				resourceText();
			} else {
				switch (getNextTokenKind()) {
				case ASTERISK:
				case BACKTICK:
				case LBRACK:
				case UNDERSCORE: {
					looseChar();
					break;
				}
				}
			}
			if (!jj_2_30(1)) {
				break loop;
			}
		}
		whiteSpace();
		consumeToken(RBRACK);
		if (jj_2_31(2147483647)) {
			ref = resourceUrl();
		}
		link.setValue(ref);
		tree.closeScope(link);
	}

	private void resourceText() {
		Text text = new Text();
		tree.openScope(text);
		StringBuilder s = new StringBuilder();
		loop: while (true) {
			switch (getNextTokenKind()) {
			case BACKSLASH: {
				s.append(consumeToken(BACKSLASH).image);
				break;
			}
			case COLON: {
				s.append(consumeToken(COLON).image);
				break;
			}
			case CHAR_SEQUENCE: {
				s.append(consumeToken(CHAR_SEQUENCE).image);
				break;
			}
			case DASH: {
				s.append(consumeToken(DASH).image);
				break;
			}
			case DIGITS: {
				s.append(consumeToken(DIGITS).image);
				break;
			}
			case DOT: {
				s.append(consumeToken(DOT).image);
				break;
			}
			case EQ: {
				s.append(consumeToken(EQ).image);
				break;
			}
			case ESCAPED_CHAR: {
				s.append(consumeToken(ESCAPED_CHAR).image.substring(1));
				break;
			}
			case IMAGE_LABEL: {
				s.append(consumeToken(IMAGE_LABEL).image);
				break;
			}
			case GT: {
				s.append(consumeToken(GT).image);
				break;
			}
			case LPAREN: {
				s.append(consumeToken(LPAREN).image);
				break;
			}
			case LT: {
				s.append(consumeToken(LT).image);
				break;
			}
			case RPAREN: {
				s.append(consumeToken(RPAREN).image);
				break;
			}
			default:
				if (!nextAfterSpace(RBRACK)) {
					switch (getNextTokenKind()) {
					case SPACE: {
						s.append(consumeToken(SPACE).image);
						break;
					}
					case TAB: {
						consumeToken(TAB);
						s.append("    ");
						break;
					}
					}
				} 
			}
			if (!jj_2_32(2)) {
				break loop;
			}
		}
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
		loop: while (true) {
			if (!jj_2_33(1)) {
				break loop;
			}
			switch (getNextTokenKind()) {
			case ASTERISK: {
				s.append(consumeToken(ASTERISK).image);
				break;
			}
			case BACKSLASH: {
				s.append(consumeToken(BACKSLASH).image);
				break;
			}
			case BACKTICK: {
				s.append(consumeToken(BACKTICK).image);
				break;
			}
			case CHAR_SEQUENCE: {
				s.append(consumeToken(CHAR_SEQUENCE).image);
				break;
			}
			case COLON: {
				s.append(consumeToken(COLON).image);
				break;
			}
			case DASH: {
				s.append(consumeToken(DASH).image);
				break;
			}
			case DIGITS: {
				s.append(consumeToken(DIGITS).image);
				break;
			}
			case DOT: {
				s.append(consumeToken(DOT).image);
				break;
			}
			case EQ: {
				s.append(consumeToken(EQ).image);
				break;
			}
			case ESCAPED_CHAR: {
				s.append(consumeToken(ESCAPED_CHAR).image.substring(1));
				break;
			}
			case IMAGE_LABEL: {
				s.append(consumeToken(IMAGE_LABEL).image);
				break;
			}
			case GT: {
				s.append(consumeToken(GT).image);
				break;
			}
			case LBRACK: {
				s.append(consumeToken(LBRACK).image);
				break;
			}
			case LPAREN: {
				s.append(consumeToken(LPAREN).image);
				break;
			}
			case LT: {
				s.append(consumeToken(LT).image);
				break;
			}
			case RBRACK: {
				s.append(consumeToken(RBRACK).image);
				break;
			}
			case UNDERSCORE: {
				s.append(consumeToken(UNDERSCORE).image);
				break;
			}
			default:
				if (!nextAfterSpace(RPAREN)) {
					switch (getNextTokenKind()) {
					case SPACE: {
						s.append(consumeToken(SPACE).image);
						break;
					}
					case TAB: {
						consumeToken(TAB);
						s.append("    ");
						break;
					}
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
		loop: while (true) {
			if (!textAhead()) {
				break loop;
			}
			lineBreak();
			strongMultilineContent();
		}
		consumeToken(ASTERISK);
		tree.closeScope(strong);
	}

	private void strongMultilineContent() {
		loop: while (true) {
			if (jj_2_34(1)) {
				text();
			} else if (jj_2_35(2147483647)) {
				image();
			} else if (jj_2_36(2147483647)) {
				link();
			} else if (jj_2_37(2147483647)) {
				code();
			} else if (jj_2_38(2147483647)) {
				emWithinStrongMultiline();
			} else {
				switch (getNextTokenKind()) {
				case BACKTICK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(BACKTICK).image);
					tree.closeScope(text);
					break;
				}
				case LBRACK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(LBRACK).image);
					tree.closeScope(text);
					break;
				}
				case UNDERSCORE: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(UNDERSCORE).image);
					tree.closeScope(text);
					break;
				}
				}
			}
			if (!jj_2_39(1)) {
				break loop;
			}
		}
	}

	private void strongWithinEmMultiline() {
		Strong strong = new Strong();
		tree.openScope(strong);
		consumeToken(ASTERISK);
		strongWithinEmMultilineContent();
		loop: while (true) {
			if (!textAhead()) {
				break loop;
			}
			lineBreak();
			strongWithinEmMultilineContent();
		}
		consumeToken(ASTERISK);
		tree.closeScope(strong);
		
	}

	private void strongWithinEmMultilineContent() {
		loop: while (true) {
			if (jj_2_40(1)) {
				text();
			} else if (jj_2_41(2147483647)) {
				image();
			} else if (jj_2_42(2147483647)) {
				link();
			} else if (jj_2_43(2147483647)) {
				code();
			} else {
				switch (getNextTokenKind()) {
				case BACKTICK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(BACKTICK).image);
					tree.closeScope(text);
					break;
				}
				case LBRACK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(LBRACK).image);
					tree.closeScope(text);
					break;
				}
				case UNDERSCORE: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(UNDERSCORE).image);
					tree.closeScope(text);
					break;
				}
				}
			}
			if (!jj_2_44(1)) {
				break loop;
			}
		}
	}

	private void strong() {
		Strong strong = new Strong();
		tree.openScope(strong);
		consumeToken(ASTERISK);
		loop: while (true) {
			if (jj_2_45(1)) {
				text();
			} else if (jj_2_46(2147483647)) {
				image();
			} else if (jj_2_47(2147483647)) {
				link();
			} else if (multilineAhead(BACKTICK)) {
				codeMultiline();
			} else if (jj_2_48(2147483647)) {
				emWithinStrong();
			} else {
				switch (getNextTokenKind()) {
				case BACKTICK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(BACKTICK).image);
					tree.closeScope(text);
					break;
				}
				case LBRACK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(LBRACK).image);
					tree.closeScope(text);
					break;
				}
				case UNDERSCORE: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(UNDERSCORE).image);
					tree.closeScope(text);
					break;
				}
				}
			}
			if (!jj_2_49(1)) {
				break loop;
			}
		}
		consumeToken(ASTERISK);
		tree.closeScope(strong);
	
	}

	private void strongWithinEm() {
		Strong strong = new Strong();
		tree.openScope(strong);
		consumeToken(ASTERISK);
		loop: while (true) {
			if (jj_2_50(1)) {
				text();
			} else if (jj_2_51(2147483647)) {
				image();
			} else if (jj_2_52(2147483647)) {
				link();
			} else if (jj_2_53(2147483647)) {
				code();
			} else {
				switch (getNextTokenKind()) {
				case BACKTICK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(BACKTICK).image);
					tree.closeScope(text);
					break;
				}
				case LBRACK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(LBRACK).image);
					tree.closeScope(text);
					break;
				}
				case UNDERSCORE: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(UNDERSCORE).image);
					tree.closeScope(text);
					break;
				}
				}
			}
			if (!jj_2_54(1)) {
				break loop;
			}
		}
		consumeToken(ASTERISK);
		tree.closeScope(strong);
	}

	private void emMultiline() {
		Em em = new Em();
		tree.openScope(em);
		consumeToken(UNDERSCORE);
		emMultilineContent();
		loop: while (true) {
			if (!textAhead()) {
				break loop;
			}
			lineBreak();
			emMultilineContent();
		}
		consumeToken(UNDERSCORE);
		tree.closeScope(em);
	}

	private void emMultilineContent() {
		loop: while (true) {
			if (jj_2_55(1)) {
				text();
			} else if (jj_2_56(2147483647)) {
				image();
			} else if (jj_2_57(2147483647)) {
				link();
			} else if (multilineAhead(BACKTICK)) {
				codeMultiline();
			} else if (jj_2_58(2147483647)) {
				strongWithinEmMultiline();
			} else {
				switch (getNextTokenKind()) {
				case ASTERISK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(ASTERISK).image);
					tree.closeScope(text);
					break;
				}
				case BACKTICK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(BACKTICK).image);
					tree.closeScope(text);
					break;
				}
				case LBRACK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(LBRACK).image);
					tree.closeScope(text);
					break;
				}
				}
			}
			if (!jj_2_59(1)) {
				break loop;
			}
		}
	}

	private void emWithinStrongMultiline() {
		Em em = new Em();
		tree.openScope(em);
		consumeToken(UNDERSCORE);
		emWithinStrongMultilineContent();
		loop: while (true) {
			if (!textAhead()) {
				break loop;
			}
			lineBreak();
			emWithinStrongMultilineContent();
		}
		consumeToken(UNDERSCORE);
		tree.closeScope(em);
	}

	private void emWithinStrongMultilineContent() {
		loop: while (true) {
			if (jj_2_60(1)) {
				text();
			} else if (jj_2_61(2147483647)) {
				image();
			} else if (jj_2_62(2147483647)) {
				link();
			} else if (jj_2_63(2147483647)) {
				code();
			} else {
				switch (getNextTokenKind()) {
				case ASTERISK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(ASTERISK).image);
					tree.closeScope(text);
					break;
				}
				case BACKTICK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(BACKTICK).image);
					tree.closeScope(text);
					break;
				}
				case LBRACK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(LBRACK).image);
					tree.closeScope(text);
					break;
				}
				}
			}
			if (!jj_2_64(1)) {
				break loop;
			}
		}
	}

	private void em() {
		Em em = new Em();
		tree.openScope(em);
		consumeToken(UNDERSCORE);
		loop: while (true) {
			if (jj_2_65(1)) {
				text();
			} else if (jj_2_66(2147483647)) {
				image();
			} else if (jj_2_67(2147483647)) {
				link();
			} else if (jj_2_68(2147483647)) {
				code();
			} else if (jj_2_69(2147483647)) {
				strongWithinEm();
			} else {
				switch (getNextTokenKind()) {
				case ASTERISK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(ASTERISK).image);
					tree.closeScope(text);
					break;
				}
				case BACKTICK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(BACKTICK).image);
					tree.closeScope(text);
					break;
				}
				case LBRACK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(LBRACK).image);
					tree.closeScope(text);
					break;
				}
				}
			}
			if (!jj_2_70(1)) {
				break loop;
			}
		}
		consumeToken(UNDERSCORE);
		tree.closeScope(em);
	}

	private void emWithinStrong() {
		Em em = new Em();
		tree.openScope(em);
		consumeToken(UNDERSCORE);
		loop: while (true) {
			if (jj_2_71(1)) {
				text();
			} else if (jj_2_72(2147483647)) {
				image();
			} else if (jj_2_73(2147483647)) {
				link();
			} else if (jj_2_74(2147483647)) {
				code();
			} else {
				switch (getNextTokenKind()) {
				case ASTERISK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(ASTERISK).image);
					tree.closeScope(text);
					break;
				}
				case BACKTICK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(BACKTICK).image);
					tree.closeScope(text);
					break;
				}
				case LBRACK: {
					Text text = new Text();
					tree.openScope(text);
					text.setValue(consumeToken(LBRACK).image);
					tree.closeScope(text);
					break;
				}
				}
			}
			if (!jj_2_75(1)) {
				break loop;
			}
		}
		consumeToken(UNDERSCORE);
		tree.closeScope(em);
	}

	private void codeMultiline() {
		Code code = new Code();
		tree.openScope(code);
		consumeToken(BACKTICK);
		codeText();
		label_45: while (true) {
			if (!textAhead()) {
				break label_45;
			}
			lineBreak();
			whiteSpace();
			label_46: while (true) {
				if(getNextTokenKind() != GT) {
					break label_46;
				}
				consumeToken(GT);
				whiteSpace();
			}
			codeText();
		}
		consumeToken(BACKTICK);
		tree.closeScope(code);
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
		loop: while (true) {
			switch (getNextTokenKind()) {
			case ASTERISK: {
				s.append(consumeToken(ASTERISK).image);
				break;
			}
			case BACKSLASH: {
				s.append(consumeToken(BACKSLASH).image);
				break;
			}
			case CHAR_SEQUENCE: {
				s.append(consumeToken(CHAR_SEQUENCE).image);
				break;
			}
			case COLON: {
				s.append(consumeToken(COLON).image);
				break;
			}
			case DASH: {
				s.append(consumeToken(DASH).image);
				break;
			}
			case DIGITS: {
				s.append(consumeToken(DIGITS).image);
				break;
			}
			case DOT: {
				s.append(consumeToken(DOT).image);
				break;
			}
			case EQ: {
				s.append(consumeToken(EQ).image);
				break;
			}
			case ESCAPED_CHAR: {
				s.append(consumeToken(ESCAPED_CHAR).image);
				break;
			}
			case IMAGE_LABEL: {
				s.append(consumeToken(IMAGE_LABEL).image);
				break;
			}
			case LT: {
				s.append(consumeToken(LT).image);
				break;
			}
			case LBRACK: {
				s.append(consumeToken(LBRACK).image);
				break;
			}
			case RBRACK: {
				s.append(consumeToken(RBRACK).image);
				break;
			}
			case LPAREN: {
				s.append(consumeToken(LPAREN).image);
				break;
			}
			case GT: {
				s.append(consumeToken(GT).image);
				break;
			}
			case RPAREN: {
				s.append(consumeToken(RPAREN).image);
				break;
			}
			case UNDERSCORE: {
				s.append(consumeToken(UNDERSCORE).image);
				break;
			}
			default:
				if (!nextAfterSpace(EOL, EOF)) {
					switch (getNextTokenKind()) {
					case SPACE: {
						s.append(consumeToken(SPACE).image);
						break;
					}
					case TAB: {
						consumeToken(TAB);
						s.append("    ");
						break;
					}
					}
				} 
			}
			if (!jj_2_76(1)) {
				break loop;
			}
		}
		text.setValue(s.toString());
		tree.closeScope(text);
	}

	private void text() {
		Text text = new Text();
		tree.openScope(text);
		StringBuffer s = new StringBuffer();
		loop: while (true) {
			switch (getNextTokenKind()) {
			case BACKSLASH: {
				s.append(consumeToken(BACKSLASH).image);
				break;
			}
			case CHAR_SEQUENCE: {
				s.append(consumeToken(CHAR_SEQUENCE).image);
				break;
			}
			case COLON: {
				s.append(consumeToken(COLON).image);
				break;
			}
			case DASH: {
				s.append(consumeToken(DASH).image);
				break;
			}
			case DIGITS: {
				s.append(consumeToken(DIGITS).image);
				break;
			}
			case DOT: {
				s.append(consumeToken(DOT).image);
				break;
			}
			case EQ: {
				s.append(consumeToken(EQ).image);
				break;
			}
			case ESCAPED_CHAR: {
				s.append(consumeToken(ESCAPED_CHAR).image.substring(1));
				break;
			}
			case GT: {
				s.append(consumeToken(GT).image);
				break;
			}
			case IMAGE_LABEL: {
				s.append(consumeToken(IMAGE_LABEL).image);
				break;
			}
			case LPAREN: {
				s.append(consumeToken(LPAREN).image);
				break;
			}
			case LT: {
				s.append(consumeToken(LT).image);
				break;
			}
			case RBRACK: {
				s.append(consumeToken(RBRACK).image);
				break;
			}
			case RPAREN: {
				s.append(consumeToken(RPAREN).image);
				break;
			}
			default:
				if (!nextAfterSpace(EOL, EOF)) {
					switch (getNextTokenKind()) {
					case SPACE: {
						s.append(consumeToken(SPACE).image);
						break;
					}
					case TAB: {
						consumeToken(TAB);
						s.append("    ");
						break;
					}
					}
				}
			}
			if (!jj_2_77(1)) {
				break loop;
			}
		}
		text.setValue(s.toString());
		tree.closeScope(text);
	}

	private void looseChar() {
		Text text = new Text();
		tree.openScope(text);
		Token t = null;
		switch (getNextTokenKind()) {
		case ASTERISK: {
			t = consumeToken(ASTERISK);
			break;
		}
		case BACKTICK: {
			t = consumeToken(BACKTICK);
			break;
		}
		case LBRACK: {
			t = consumeToken(LBRACK);
			break;
		}
		case UNDERSCORE: {
			t = consumeToken(UNDERSCORE);
			break;
		}
		}
		text.setValue(t.image);
		tree.closeScope(text);
	}

	private void lineBreak() {
		LineBreak linebreak = new LineBreak();
		tree.openScope(linebreak);
		loop: while (true) {
			if(getNextTokenKind() != SPACE && getNextTokenKind() != TAB) {
				break loop;
			}
			consumeToken(getNextTokenKind());
		}
		consumeToken(EOL);
		tree.closeScope(linebreak);
	}

	private void whiteSpace() {
		loop: while (true) {
			if(getNextTokenKind() != SPACE && getNextTokenKind() != TAB) {
				break loop;
			}
			consumeToken(getNextTokenKind());
		}
	}

	private boolean orderedListAhead(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_227();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean fencedCodeBlockAhead(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_53();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean headingInlineAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			Token xsp;
			xsp = scanPosition;
			if (jj_3R_61()) {
				scanPosition = xsp;
				if (jj_3R_62()) {
					scanPosition = xsp;
					if (jj_3R_63()) {
						scanPosition = xsp;
						if (jj_3R_64()) {
							scanPosition = xsp;
							if (jj_3R_65()) {
								scanPosition = xsp;
								if (jj_3R_66()) {
									scanPosition = xsp;
									if (jj_3R_233())
										return false;
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

	private boolean inlineTextAhead() {
		lookAhead = 1;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean inlineImageAhead() {
		lookAhead = 2147483647;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_8(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_9(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_64();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_10(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_65();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_11(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_12(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_67();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_13(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !scanForBlockElement();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_14(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_14();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_15(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !scanForBlockElement();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_16(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !scanForBlockElement();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_17(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_17();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_18(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_19(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_20(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_21(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_21();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_22(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_94();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_23(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_23();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_24(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_96();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_25(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_26(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_64();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_27(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_65();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_28(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_29(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_94();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_30(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_30();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_31(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_96();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_32(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_32();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_33(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_33();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_34(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_35(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_36(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_37(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_38(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_134();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_39(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_39();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_40(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_41(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_42(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_43(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_44(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_44();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_45(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_46(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_47(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_48(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_148();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_49(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_49();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_50(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_51(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_52(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_53(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_54(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_54();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_55(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_56(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_57(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_58(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_162();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_59(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_59();
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean jj_2_60(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_61(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_62(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_63(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_64(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_64();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_65(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_66(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_67(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_68(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_69(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_176();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_70(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_70();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_71(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_72(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_73(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_74(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_75(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_75();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_76(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_76();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_2_77(int xla) {
		lookAhead = xla;
		lastPosition = scanPosition = token;
		try {
			return !jj_3_77();
		} catch (LookaheadSuccess ls) {
			return true;
		} 
	}

	private boolean jj_3R_259() {
		Token xsp;
		xsp = scanPosition;
		if (scanToken(SPACE)) {
			scanPosition = xsp;
			if (scanToken(TAB)) {
				return true;
			}
		}
		return false;
	}

	private boolean jj_3R_228() {
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3R_259()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean jj_3R_267() {
		Token xsp;
		xsp = scanPosition;
		if (scanToken(SPACE)) {
			scanPosition = xsp;
			if (scanToken(TAB)) {
				return true;
			}
		}
		return false;
	}

	private boolean jj_3R_262() {
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3R_267()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(EOL);
	}

	private boolean jj_3R_233() {
		Token xsp;
		xsp = scanPosition;
		if (scanToken(ASTERISK)) {
			scanPosition = xsp;
			if (scanToken(BACKTICK)) {
				scanPosition = xsp;
				if (scanToken(LBRACK)) {
					scanPosition = xsp;
					if (scanToken(UNDERSCORE)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_222() {
		Token xsp;
		xsp = scanPosition;
		if (scanToken(SPACE)) {
			scanPosition = xsp;
			if (scanToken(TAB)) {
				return true;
			}
		}
		return false;
	}

	private boolean jj_3_77() {
		Token xsp;
		xsp = scanPosition;
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
																if (!semanticLookAhead || jj_3R_222())
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
		return false;
	}

	private boolean jj_3R_61() {
		Token xsp;
		if (jj_3_77()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_77()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean jj_3R_207() {
		Token xsp;
		xsp = scanPosition;
		if (scanToken(SPACE)) {
			scanPosition = xsp;
			if (scanToken(TAB)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean jj_3R_272() {
		if (jj_3R_262()) {
			return true;
		}
		if (jj_3R_228()) {
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
		return jj_3R_236();
	}

	private boolean jj_3_76() {
		Token xsp;
		xsp = scanPosition;
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
																			if (!semanticLookAhead || jj_3R_207())
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
		return false;
	}

	private boolean jj_3R_236() {
		Token xsp;
		if (jj_3_76()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_76()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean jj_3R_66() {
		if (scanToken(BACKTICK)) {
			return true;
		}
		if (jj_3R_236()) {
			return true;
		}
		return scanToken(BACKTICK);
	}

	private boolean jj_3R_242() {
		if (scanToken(BACKTICK)) {
			return true;
		}
		if (jj_3R_236()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3R_272()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(BACKTICK);
	}
	
	private boolean jj_3R_249() {
		if (jj_3R_262()) {
			return true;
		}
		return jj_3R_248();
	}

	private boolean jj_3_75() {
		Token xsp;
		xsp = scanPosition;
		if (jj_3R_61()) {
			scanPosition = xsp;
			if (jj_3R_62()) {
				scanPosition = xsp;
				if (jj_3R_63()) {
					scanPosition = xsp;
					if (jj_3R_66()) {
						scanPosition = xsp;
						if (scanToken(ASTERISK)) {
							scanPosition = xsp;
							if (scanToken(BACKTICK)) {
								scanPosition = xsp;
								if (scanToken(LBRACK))
									return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_148() {
		if (scanToken(UNDERSCORE)) {
			return true;
		}
		Token xsp;
		if (jj_3_75()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_75()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(UNDERSCORE);
	}
	
	private boolean jj_3_70() {
		Token xsp;
		xsp = scanPosition;
		if (jj_3R_61()) {
			scanPosition = xsp;
			if (jj_3R_62()) {
				scanPosition = xsp;
				if (jj_3R_63()) {
					scanPosition = xsp;
					if (jj_3R_66()) {
						scanPosition = xsp;
						if (jj_3R_176()) {
							scanPosition = xsp;
							if (scanToken(ASTERISK)) {
								scanPosition = xsp;
								if (scanToken(BACKTICK)) {
									scanPosition = xsp;
									if (scanToken(LBRACK))
										return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_65() {
		if (scanToken(UNDERSCORE)) {
			return true;
		}
		Token xsp;
		if (jj_3_70()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_70()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(UNDERSCORE);
	}

	private boolean jj_3_64() {
		Token xsp;
		xsp = scanPosition;
		if (jj_3R_61()) {
			scanPosition = xsp;
			if (jj_3R_62()) {
				scanPosition = xsp;
				if (jj_3R_63()) {
					scanPosition = xsp;
					if (jj_3R_66()) {
						scanPosition = xsp;
						if (scanToken(ASTERISK)) {
							scanPosition = xsp;
							if (scanToken(BACKTICK)) {
								scanPosition = xsp;
								if (scanToken(LBRACK))
									return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_248() {
		Token xsp;
		if (jj_3_64())
			return true;
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_64()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean jj_3R_134() {
		if (scanToken(UNDERSCORE)) {
			return true;
		}
		if (jj_3R_248()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3R_249()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(UNDERSCORE);
	}

	private boolean jj_3_59() {
		Token xsp;
		xsp = scanPosition;
		if (jj_3R_61()) {
			scanPosition = xsp;
			if (jj_3R_62()) {
				scanPosition = xsp;
				if (jj_3R_63()) {
					scanPosition = xsp;
					lookingAhead = true;
					semanticLookAhead = multilineAhead(BACKTICK);
					lookingAhead = false;
					if (!semanticLookAhead || jj_3R_242()) {
						scanPosition = xsp;
						if (jj_3R_162()) {
							scanPosition = xsp;
							if (scanToken(ASTERISK)) {
								scanPosition = xsp;
								if (scanToken(BACKTICK)) {
									scanPosition = xsp;
									if (scanToken(LBRACK))
										return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean jj_3R_251() {
		return (jj_3R_262() || jj_3R_250());
	}

	private boolean jj_3_54() {
		Token xsp;
		xsp = scanPosition;
		if (jj_3R_61()) {
			scanPosition = xsp;
			if (jj_3R_62()) {
				scanPosition = xsp;
				if (jj_3R_63()) {
					scanPosition = xsp;
					if (jj_3R_66()) {
						scanPosition = xsp;
						if (scanToken(BACKTICK)) {
							scanPosition = xsp;
							if (scanToken(LBRACK)) {
								scanPosition = xsp;
								if (scanToken(UNDERSCORE))
									return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_176() {
		if (scanToken(ASTERISK)) {
			return true;
		}
		Token xsp;
		if (jj_3_54()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_54()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(ASTERISK);
	}

	private boolean jj_3_49() {
		Token xsp;
		xsp = scanPosition;
		if (jj_3R_61()) {
			scanPosition = xsp;
			if (jj_3R_62()) {
				scanPosition = xsp;
				if (jj_3R_63()) {
					scanPosition = xsp;
					lookingAhead = true;
					semanticLookAhead = multilineAhead(BACKTICK);
					lookingAhead = false;
					if (!semanticLookAhead || jj_3R_242()) {
						scanPosition = xsp;
						if (jj_3R_148()) {
							scanPosition = xsp;
							if (scanToken(BACKTICK)) {
								scanPosition = xsp;
								if (scanToken(LBRACK)) {
									scanPosition = xsp;
									if (scanToken(UNDERSCORE))
										return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean jj_3R_64() {
		if (scanToken(ASTERISK)) {
			return true;
		}
		Token xsp;
		if (jj_3_49()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_49()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(ASTERISK);
	}

	private boolean jj_3_44() {
		Token xsp;
		xsp = scanPosition;
		if (jj_3R_61()) {
			scanPosition = xsp;
			if (jj_3R_62()) {
				scanPosition = xsp;
				if (jj_3R_63()) {
					scanPosition = xsp;
					if (jj_3R_66()) {
						scanPosition = xsp;
						if (scanToken(BACKTICK)) {
							scanPosition = xsp;
							if (scanToken(LBRACK)) {
								scanPosition = xsp;
								if (scanToken(UNDERSCORE))
									return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_250() {
		Token xsp;
		if (jj_3_44()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_44()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean jj_3R_162() {
		if (scanToken(ASTERISK)) {
			return true;
		}
		if (jj_3R_250()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3R_251()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return scanToken(ASTERISK);
	}

	private boolean jj_3_39() {
		Token xsp;
		xsp = scanPosition;
		if (jj_3R_61()) {
			scanPosition = xsp;
			if (jj_3R_62()) {
				scanPosition = xsp;
				if (jj_3R_63()) {
					scanPosition = xsp;
					if (jj_3R_66()) {
						scanPosition = xsp;
						if (jj_3R_134()) {
							scanPosition = xsp;
							if (scanToken(BACKTICK)) {
								scanPosition = xsp;
								if (scanToken(LBRACK)) {
									scanPosition = xsp;
									if (scanToken(UNDERSCORE)) {
										return true;
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

	private boolean jj_3R_133() {
		Token xsp;
		xsp = scanPosition;
		if (scanToken(SPACE)) {
			scanPosition = xsp;
			if (scanToken(TAB)) {
				return true;
			}
		}
		return false;
	}

	private boolean jj_3_33() {
		Token xsp;
		xsp = scanPosition;
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
																			if (!semanticLookAhead || jj_3R_133()) {
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

	private boolean jj_3_23() {
		Token xsp;
		xsp = scanPosition;
		if (jj_3R_94()) {
			scanPosition = xsp;
			if (jj_3R_233()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean jj_3R_243() {
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_33()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean jj_3R_96() {
		if (scanToken(LPAREN)) {
			return true;
		}
		if (jj_3R_228()) {
			return true;
		}
		if (jj_3R_243()) {
			return true;
		}
		if (jj_3R_228()) {
			return true;
		}
		return scanToken(RPAREN);
	}

	private boolean jj_3R_115() {
		Token xsp;
		xsp = scanPosition;
		if (scanToken(SPACE)) {
			scanPosition = xsp;
			if (scanToken(TAB)) {
				return true;
			}
		}
		return false;
	}

	private boolean jj_3_30() {
		Token xsp;
		xsp = scanPosition;
		if (jj_3R_62()) {
			scanPosition = xsp;
			if (jj_3R_64()) {
				scanPosition = xsp;
				if (jj_3R_65()) {
					scanPosition = xsp;
					if (jj_3R_66()) {
						scanPosition = xsp;
						if (jj_3R_94()) {
							scanPosition = xsp;
							if (jj_3R_233())
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3_32() {
		Token xsp;
		xsp = scanPosition;
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
															if (!semanticLookAhead || jj_3R_115())
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
		return false;
	}

	private boolean jj_3R_94() {
		Token xsp;
		if (jj_3_32()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_32()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean jj_3R_63() {
		if (scanToken(LBRACK)) {
			return true;
		}
		if (jj_3R_228()) {
			return true;
		}
		Token xsp;
		if (jj_3_30()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_30()) {
				scanPosition = xsp;
				break loop;
			}
		}
		if (jj_3R_228()) {
			return true;
		}
		if (scanToken(RBRACK)) {
			return true;
		}
		xsp = scanPosition;
		if (jj_3R_96()) {
			scanPosition = xsp;
		}
		return false;
	}

	private boolean jj_3R_62() {
		if (scanToken(LBRACK)) {
			return true;
		}
		if (jj_3R_228()) {
			return true;
		}
		if (scanToken(IMAGE_LABEL)) {
			return true;
		}
		if (jj_3R_228()) {
			return true;
		}
		Token xsp;
		if (jj_3_23()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_23()) {
				scanPosition = xsp;
				break loop;
			}
		}
		if (jj_3R_228()) {
			return true;
		}
		if (scanToken(RBRACK)) {
			return true;
		}
		xsp = scanPosition;
		if (jj_3R_96()) {
			scanPosition = xsp;
		}
		return false;
	}

	private boolean jj_3_21() {
		Token xsp;
		xsp = scanPosition;
		if (jj_3R_61()) {
			scanPosition = xsp;
			if (jj_3R_62()) {
				scanPosition = xsp;
				if (jj_3R_63()) {
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
							if (!semanticLookAhead || jj_3R_242()) {
								scanPosition = xsp;
								if (jj_3R_233())
									return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean jj_3R_232() {
		Token xsp;
		if (jj_3_21()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3_21()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}
	
	private boolean jj_3R_265() {
		Token xsp;
		xsp = scanPosition;
		if (scanToken(CHAR_SEQUENCE)) {
			scanPosition = xsp;
			if (scanToken(BACKTICK)) {
				return true;
			}
		}
		return false;
	}

	private boolean jj_3R_260() {
		Token xsp;
		if (jj_3R_265()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3R_265()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean jj_3R_87() {
		if (scanToken(EOL)) {
			return true;
		}
		return jj_3R_261();
	}

	private boolean jj_3R_266() {
		Token xsp;
		xsp = scanPosition;
		if (scanToken(SPACE)) {
			scanPosition = xsp;
			if (scanToken(TAB)) {
				return true;
			}
		}
		return false;
	}

	private boolean jj_3R_261() {
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3R_266()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}

	private boolean jj_3R_86() {
		Token xsp;
		xsp = scanPosition;
		if (scanToken(SPACE)) {
			scanPosition = xsp;
			if (scanToken(TAB)) {
				return true;
			}
		}
		return false;
	}

	private boolean jj_3R_231() {
		if (scanToken(EOL) || jj_3R_228() || scanToken(BACKTICK)) {
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

	private boolean jj_3_17() {
		Token xsp;
		xsp = scanPosition;
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
																				if (!semanticLookAhead || jj_3R_86()) {
																					scanPosition = xsp;
																					lookingAhead = true;
																					semanticLookAhead = !fencesAhead();
																					lookingAhead = false;
																					if (!semanticLookAhead || jj_3R_87()) {
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
			}
		}
		return false;
	}

	private boolean jj_3R_230() {
		return scanToken(EOL) || jj_3R_261();
	}

	private boolean jj_3R_53() {
		if (scanToken(BACKTICK)) {
			return true;
		}
		if (scanToken(BACKTICK)) {
			return true;
		}
		Token xsp;
		if (scanToken(BACKTICK)) {
			return true;
		}
		while (true) {
			xsp = scanPosition;
			if (scanToken(BACKTICK)) {
				scanPosition = xsp;
				break;
			}
		}
		if (jj_3R_228()) {
			return true;
		}
		xsp = scanPosition;
		if (jj_3R_260()) {
			scanPosition = xsp;
		}
		xsp = scanPosition;
		if (jj_3R_230()) {
			scanPosition = xsp;
		}
		while (true) {
			xsp = scanPosition;
			if (jj_3_17()) {
				scanPosition = xsp;
				break;
			}
		}
		xsp = scanPosition;
		if (jj_3R_231()) {
			scanPosition = xsp;
		}
		return false;
	}

	private boolean jj_3R_237() {
		return scanToken(GT) || jj_3R_228();
	}

	private boolean jj_3R_227() {
		return scanToken(DIGITS) || scanToken(DOT);
	}

	private boolean jj_3_14() {
		return jj_3R_67() || scanToken(EOL);
	}

	private boolean jj_3R_67() {
		if (scanToken(EOL) || jj_3R_228() || jj_3R_237()) {
			return true;
		}
		Token xsp;
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3R_237()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}


	private boolean jj_3R_256() {
		Token xsp;
		if (scanToken(EQ)) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (scanToken(EQ)) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}


	private boolean scanForBlockElement() {
		Token xsp = scanPosition;
		lookingAhead = true;
		semanticLookAhead = headingAhead(1);
		lookingAhead = false;
		if (!semanticLookAhead || jj_3R_256()) {
			scanPosition = xsp;
			if (scanToken(GT)) {
				scanPosition = xsp;
				if (scanToken(DASH)) {
					scanPosition = xsp;
					if (jj_3R_227()) {
						scanPosition = xsp;
						if (jj_3R_53()) {
							scanPosition = xsp;
							if (jj_3R_232())
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	private Token consumeToken(int kind) {
		Token oldToken = token;
		if (oldToken.next != null) {
			token = token.next;
		} else {
			token.next = tm.getNextToken();
			token = token.next;
		}
		nextTokenKind = -1;
		if (token.kind == kind) {
			return token;
		}
		token = oldToken;
		return token;
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

	private int getNextTokenKind() {
		if(nextTokenKind != -1) { 
			return nextTokenKind; 
		}
		if ((nextToken = token.next) == null) {
			return (nextTokenKind = (token.next = tm.getNextToken()).kind);
		}
		return (nextTokenKind = nextToken.kind);
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
								|| (getToken(i).kind == BACKTICK && getToken(i + 1).kind == BACKTICK
										&& getToken(i + 2).kind == BACKTICK)
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
						&& !(getToken(i).kind == BACKTICK && getToken(i + 1).kind == BACKTICK
								&& getToken(i + 2).kind == BACKTICK)
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

}
