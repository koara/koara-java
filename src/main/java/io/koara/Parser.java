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
import java.util.ArrayList;
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
	private TreeState tree = new TreeState();
	private TokenManager tm;
	private int currentBlockLevel;
	private int currentQuoteLevel;
	
	
	
	private Token token;
	
	
	private Token jj_nt;
	private int nextTokenKind;
	private Token scanPosition, jj_lastpos;
	private int jjLookAhead;
	private boolean lookingAhead = false;
	private boolean jj_semLA;
	private int jjGen;
	private int[] jjLookaheadArray = new int[46];
	private java.util.List<int[]> jj_expentries = new ArrayList<int[]>();
	private int[] jj_expentry;
	private int[] jj_lasttokens = new int[100];
	private int jj_endpos;
	private LookaheadSuccess jj_ls = new LookaheadSuccess();
	
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
		nextTokenKind = -1;
		jjGen = 0;
		return document();
	}
	
	private Document document() {
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
		jjLookAhead = 1;
		jj_lastpos = scanPosition = token;
		try {
			return !scanForBlockElement();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(0, 1);
		}
	}


	private void blockElement() {
		currentBlockLevel++;
		if (headingAhead(1)) {
			heading();
		} else {
			switch (getNextTokenKind()) {
			case GT: {
				blockquote();
				break;
			}
			case DASH: {
				unorderedList();
				break;
			}
			default:
				jjLookaheadArray[3] = jjGen;
				if (jj_2_2(2)) {
					orderedList();
				} else if (jj_2_3(2147483647)) {
					fencedCodeBlock();
				} else if (jj_2_4(1)) {
					paragraph();
				} else {
					consumeToken(-1);
				}
			}
		}
		currentBlockLevel--;
	}

	private void heading() {
		Heading heading = new Heading();
		tree.openScope(heading);
		int headingLevel = 0;
		try {
			equalsChars: while (true) {
				consumeToken(EQ);
				headingLevel++;
				switch (getNextTokenKind()) {
				case EQ: {
					break;
				}
				default:
					jjLookaheadArray[4] = jjGen;
					break equalsChars;
				}
			}
			whiteSpace();
			inline: while (true) {
				if (!jj_2_5(1)) {
					break inline;
				}
				if (jj_2_6(1)) {
					text();
				} else if (jj_2_7(2147483647)) {
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
					default:
						jjLookaheadArray[5] = jjGen;
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
			}
			heading.setValue(headingLevel);
		} finally {
			tree.closeScope(heading);
		}
	}

	private void blockquote() {
		Blockquote blockquote = new Blockquote();
		tree.openScope(blockquote);
		currentQuoteLevel++;
		try {
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
						switch (getNextTokenKind()) {
						case EOL: {
							break;
						}
						default:
							jjLookaheadArray[6] = jjGen;
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
		} finally {
			tree.closeScope(blockquote);
		}
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
			switch (getNextTokenKind()) {
			case GT: {
				break;
			}
			default:
				jjLookaheadArray[7] = jjGen;
				break loop;
			}
		}
	}

	private void unorderedList() {
		List list = new List();
		tree.openScope(list);
		try {
			unorderedListItem();
			label_13: while (true) {
				if (!listItemAhead(false)) {
					break label_13;
				}
				label_14: while (true) {
					consumeToken(EOL);
					switch (getNextTokenKind()) {
					case EOL: {
						break;
					}
					default:
						jjLookaheadArray[8] = jjGen;
						break label_14;
					}
				}
				whiteSpace();
				unorderedListItem();
			}
		} finally {
			tree.closeScope(list);
		}
	}

	private void unorderedListItem() {
		ListItem listItem = new ListItem();
		tree.openScope(listItem);
		try {
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
						switch (getNextTokenKind()) {
						case EOL: {
							break;
						}
						default:
							jjLookaheadArray[9] = jjGen;
							break label_16;
						}
					}
					blockElement();
				}
			} 
		} finally {
			tree.closeScope(listItem);
		}
	}

	private void orderedList() {
		List list = new List();
		tree.openScope(list);
		try {
			orderedListItem();
			label_17: while (true) {
				if (!listItemAhead(true)) {
					break label_17;
				}
				label_18: while (true) {
					consumeToken(EOL);
					switch (getNextTokenKind()) {
					case EOL: {
						break;
					}
					default:
						jjLookaheadArray[10] = jjGen;
						break label_18;
					}
				}
				whiteSpace();
				orderedListItem();
			}
			list.setOrdered(true);
		} finally {
			tree.closeScope(list);
		}
	}

	private void orderedListItem() {
		ListItem listItem = new ListItem();
		tree.openScope(listItem);
		Token t;
		try {
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
						switch (getNextTokenKind()) {
						case EOL: {
							break;
						}
						default:
							jjLookaheadArray[11] = jjGen;
							break label_20;
						}
					}
					blockElement();
				}
			}
			listItem.setNumber(Integer.valueOf(Integer.valueOf(t.image)));
		} finally {
			tree.closeScope(listItem);
		}
	}

	private void fencedCodeBlock() {
		CodeBlock codeBlock = new CodeBlock();
		tree.openScope(codeBlock);
		Token t;
		String language;
		StringBuilder s = new StringBuilder();
		int beginColumn;
		try {
			t = consumeToken(BACKTICK);
			beginColumn = t.beginColumn;
			consumeToken(BACKTICK);
			label_21: while (true) {
				consumeToken(BACKTICK);
				switch (getNextTokenKind()) {
				case BACKTICK: {
					break;
				}
				default:
					jjLookaheadArray[12] = jjGen;
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
				jjLookaheadArray[13] = jjGen;
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
					t = consumeToken(ASTERISK);
					s.append(t.image);
					break;
				}
				case BACKSLASH: {
					t = consumeToken(BACKSLASH);
					s.append(t.image);
					break;
				}
				case CHAR_SEQUENCE: {
					t = consumeToken(CHAR_SEQUENCE);
					s.append(t.image);
					break;
				}
				case COLON: {
					t = consumeToken(COLON);
					s.append(t.image);
					break;
				}
				case DASH: {
					t = consumeToken(DASH);
					s.append(t.image);
					break;
				}
				case DIGITS: {
					t = consumeToken(DIGITS);
					s.append(t.image);
					break;
				}
				case DOT: {
					t = consumeToken(DOT);
					s.append(t.image);
					break;
				}
				case EQ: {
					t = consumeToken(EQ);
					s.append(t.image);
					break;
				}
				case ESCAPED_CHAR: {
					t = consumeToken(ESCAPED_CHAR);
					s.append(t.image);
					break;
				}
				case IMAGE_LABEL: {
					t = consumeToken(IMAGE_LABEL);
					s.append(t.image);
					break;
				}
				case LT: {
					t = consumeToken(LT);
					s.append(t.image);
					break;
				}
				case GT: {
					t = consumeToken(GT);
					s.append(t.image);
					break;
				}
				case LBRACK: {
					t = consumeToken(LBRACK);
					s.append(t.image);
					break;
				}
				case RBRACK: {
					t = consumeToken(RBRACK);
					s.append(t.image);
					break;
				}
				case LPAREN: {
					t = consumeToken(LPAREN);
					s.append(t.image);
					break;
				}
				case RPAREN: {
					t = consumeToken(RPAREN);
					s.append(t.image);
					break;
				}
				case UNDERSCORE: {
					t = consumeToken(UNDERSCORE);
					s.append(t.image);
					break;
				}
				case BACKTICK: {
					t = consumeToken(BACKTICK);
					s.append(t.image);
					break;
				}
				default:
					jjLookaheadArray[15] = jjGen;
					if (!nextAfterSpace(EOL, EOF)) {
						switch (getNextTokenKind()) {
						case SPACE: {
							t = consumeToken(SPACE);
							s.append(t.image);
							break;
						}
						case TAB: {
							t = consumeToken(TAB);
							s.append("    ");
							break;
						}
						default:
							jjLookaheadArray[14] = jjGen;
							consumeToken(-1);
							throw new RuntimeException();
						}
					} else if (!fencesAhead()) {
						t = consumeToken(EOL);
						s.append("\u005cn");
						levelWhiteSpace(beginColumn);
					} else {
						consumeToken(-1);
						throw new RuntimeException();
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
					switch (getNextTokenKind()) {
					case BACKTICK: {
						break;
					}
					default:
						jjLookaheadArray[16] = jjGen;
						break label_23;
					}
				}
			}
			codeBlock.setValue(s.toString());
		} finally {
			tree.closeScope(codeBlock);
		}
	}

	private void levelWhiteSpace(int threshold) {
		Token t;
		int currentPos = 1;
		label_24: while (true) {
			if (!((getToken(1).kind == SPACE || getToken(1).kind == TAB) && currentPos < (threshold - 1))) {
				break label_24;
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
			default:
				jjLookaheadArray[17] = jjGen;
				consumeToken(-1);
				throw new RuntimeException();
			}
		}
	}

	private String codeLanguage() {
		Token t;
		StringBuilder s = new StringBuilder();
		label_25: while (true) {
			switch (getNextTokenKind()) {
			case CHAR_SEQUENCE: {
				t = consumeToken(CHAR_SEQUENCE);
				s.append(t.image);
				break;
			}
			case BACKTICK: {
				t = consumeToken(BACKTICK);
				s.append(t.image);
				break;
			}
			default:
				jjLookaheadArray[18] = jjGen;
				consumeToken(-1);
				throw new RuntimeException();
			}
			switch (getNextTokenKind()) {
			case BACKTICK:
			case CHAR_SEQUENCE: {
				break;
			}
			default:
				jjLookaheadArray[19] = jjGen;
				break label_25;
			}
		}
		return s.toString();
	}

	private void paragraph() {
		Paragraph paragraph = new Paragraph();
		tree.openScope(paragraph);
		try {
			inline();
			label_26: while (true) {
				if (!textAhead()) {
					break label_26;
				}
				lineBreak();
				whiteSpace();
				label_27: while (true) {
					switch (getNextTokenKind()) {
					case GT: {
						break;
					}
					default:
						jjLookaheadArray[20] = jjGen;
						break label_27;
					}
					consumeToken(GT);
					whiteSpace();
				}
				inline();
			}
		} finally {
			tree.closeScope(paragraph);
		}
	}

	private void inline() {
		label_28: while (true) {
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
				default:
					jjLookaheadArray[21] = jjGen;
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
			if (!jj_2_21(1)) {
				break label_28;
			}
		}
	}

	private void image() {
		Image image = new Image();
		tree.openScope(image);
		String ref = "";
		try {
			consumeToken(LBRACK);
			whiteSpace();
			consumeToken(IMAGE_LABEL);
			whiteSpace();
			label_29: while (true) {
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
					default:
						jjLookaheadArray[22] = jjGen;
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (!jj_2_23(1)) {
					break label_29;
				}
			}
			whiteSpace();
			consumeToken(RBRACK);
			if (jj_2_24(2147483647)) {
				ref = resourceUrl();
			}
			image.setValue(ref);
		} finally {
			tree.closeScope(image);
		}
	}

	private void link() {
		Link link = new Link();
		tree.openScope(link);
		String ref = "";
		try {
			consumeToken(LBRACK);
			whiteSpace();
			label_30: while (true) {
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
					default:
						jjLookaheadArray[23] = jjGen;
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (!jj_2_30(1)) {
					break label_30;
				}
			}
			whiteSpace();
			consumeToken(RBRACK);
			if (jj_2_31(2147483647)) {
				ref = resourceUrl();
			}
			link.setValue(ref);
		} finally {
			tree.closeScope(link);
		}
	}

	private void resourceText() {
		Text text = new Text();
		tree.openScope(text);
		Token t;
		StringBuilder s = new StringBuilder();
		try {
			label_31: while (true) {
				switch (getNextTokenKind()) {
				case BACKSLASH: {
					t = consumeToken(BACKSLASH);
					s.append(t.image);
					break;
				}
				case COLON: {
					t = consumeToken(COLON);
					s.append(t.image);
					break;
				}
				case CHAR_SEQUENCE: {
					t = consumeToken(CHAR_SEQUENCE);
					s.append(t.image);
					break;
				}
				case DASH: {
					t = consumeToken(DASH);
					s.append(t.image);
					break;
				}
				case DIGITS: {
					t = consumeToken(DIGITS);
					s.append(t.image);
					break;
				}
				case DOT: {
					t = consumeToken(DOT);
					s.append(t.image);
					break;
				}
				case EQ: {
					t = consumeToken(EQ);
					s.append(t.image);
					break;
				}
				case ESCAPED_CHAR: {
					t = consumeToken(ESCAPED_CHAR);
					s.append(t.image.substring(1));
					break;
				}
				case IMAGE_LABEL: {
					t = consumeToken(IMAGE_LABEL);
					s.append(t.image);
					break;
				}
				case GT: {
					t = consumeToken(GT);
					s.append(t.image);
					break;
				}
				case LPAREN: {
					t = consumeToken(LPAREN);
					s.append(t.image);
					break;
				}
				case LT: {
					t = consumeToken(LT);
					s.append(t.image);
					break;
				}
				case RPAREN: {
					t = consumeToken(RPAREN);
					s.append(t.image);
					break;
				}
				default:
					jjLookaheadArray[25] = jjGen;
					if (!nextAfterSpace(RBRACK)) {
						switch (getNextTokenKind()) {
						case SPACE: {
							t = consumeToken(SPACE);
							s.append(t.image);
							break;
						}
						case TAB: {
							t = consumeToken(TAB);
							s.append("    ");
							break;
						}
						default:
							jjLookaheadArray[24] = jjGen;
							consumeToken(-1);
							throw new RuntimeException();
						}
					} else {
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (jj_2_32(2)) {
					;
				} else {
					break label_31;
				}
			}
			text.setValue(s.toString());
		} finally {
			tree.closeScope(text);
		}
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
		Token t;
		StringBuilder s = new StringBuilder();
		label_32: while (true) {
			if (!jj_2_33(1)) {
				break label_32;
			}
			switch (getNextTokenKind()) {
			case ASTERISK: {
				t = consumeToken(ASTERISK);
				s.append(t.image);
				break;
			}
			case BACKSLASH: {
				t = consumeToken(BACKSLASH);
				s.append(t.image);
				break;
			}
			case BACKTICK: {
				t = consumeToken(BACKTICK);
				s.append(t.image);
				break;
			}
			case CHAR_SEQUENCE: {
				t = consumeToken(CHAR_SEQUENCE);
				s.append(t.image);
				break;
			}
			case COLON: {
				t = consumeToken(COLON);
				s.append(t.image);
				break;
			}
			case DASH: {
				t = consumeToken(DASH);
				s.append(t.image);
				break;
			}
			case DIGITS: {
				t = consumeToken(DIGITS);
				s.append(t.image);
				break;
			}
			case DOT: {
				t = consumeToken(DOT);
				s.append(t.image);
				break;
			}
			case EQ: {
				t = consumeToken(EQ);
				s.append(t.image);
				break;
			}
			case ESCAPED_CHAR: {
				t = consumeToken(ESCAPED_CHAR);
				s.append(t.image.substring(1));
				break;
			}
			case IMAGE_LABEL: {
				t = consumeToken(IMAGE_LABEL);
				s.append(t.image);
				break;
			}
			case GT: {
				t = consumeToken(GT);
				s.append(t.image);
				break;
			}
			case LBRACK: {
				t = consumeToken(LBRACK);
				s.append(t.image);
				break;
			}
			case LPAREN: {
				t = consumeToken(LPAREN);
				s.append(t.image);
				break;
			}
			case LT: {
				t = consumeToken(LT);
				s.append(t.image);
				break;
			}
			case RBRACK: {
				t = consumeToken(RBRACK);
				s.append(t.image);
				break;
			}
			case UNDERSCORE: {
				t = consumeToken(UNDERSCORE);
				s.append(t.image);
				break;
			}
			default:
				jjLookaheadArray[27] = jjGen;
				if (!nextAfterSpace(RPAREN)) {
					switch (getNextTokenKind()) {
					case SPACE: {
						t = consumeToken(SPACE);
						s.append(t.image);
						break;
					}
					case TAB: {
						t = consumeToken(TAB);
						s.append("    ");
						break;
					}
					default:
						jjLookaheadArray[26] = jjGen;
						consumeToken(-1);
						throw new RuntimeException();
					}
				} else {
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
		}
		return s.toString();
	}

	private void strongMultiline() {
		Strong strong = new Strong();
		tree.openScope(strong);
		try {
			consumeToken(ASTERISK);
			strongMultilineContent();
			label_33: while (true) {
				if (!textAhead()) {
					break label_33;
				}
				lineBreak();
				strongMultilineContent();
			}
			consumeToken(ASTERISK);
		} finally {
			tree.closeScope(strong);
		}
	}

	private void strongMultilineContent() {
		Token t;
		label_34: while (true) {
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
					t = consumeToken(BACKTICK);
					Text jjtn001 = new Text();
					boolean jjtc001 = true;
					tree.openScope(jjtn001);
					try {
						tree.closeScope(jjtn001);
						jjtc001 = false;
						jjtn001.setValue(t.image);
					} finally {
						if (jjtc001) {
							tree.closeScope(jjtn001);
						}
					}
					break;
				}
				case LBRACK: {
					t = consumeToken(LBRACK);
					Text jjtn002 = new Text();
					boolean jjtc002 = true;
					tree.openScope(jjtn002);
					try {
						tree.closeScope(jjtn002);
						jjtc002 = false;
						jjtn002.setValue(t.image);
					} finally {
						if (jjtc002) {
							tree.closeScope(jjtn002);
						}
					}
					break;
				}
				case UNDERSCORE: {
					t = consumeToken(UNDERSCORE);
					Text jjtn003 = new Text();
					boolean jjtc003 = true;
					tree.openScope(jjtn003);
					try {
						tree.closeScope(jjtn003);
						jjtc003 = false;
						jjtn003.setValue(t.image);
					} finally {
						if (jjtc003) {
							tree.closeScope(jjtn003);
						}
					}
					break;
				}
				default:
					jjLookaheadArray[28] = jjGen;
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
			if (!jj_2_39(1)) {
				break label_34;
			}
		}
	}

	private void strongWithinEmMultiline() {
		Strong strong = new Strong();
		tree.openScope(strong);
		try {
			consumeToken(ASTERISK);
			strongWithinEmMultilineContent();
			label_35: while (true) {
				if (textAhead()) {
					;
				} else {
					break label_35;
				}
				lineBreak();
				strongWithinEmMultilineContent();
			}
			consumeToken(ASTERISK);
		} finally {
			tree.closeScope(strong);
		}
	}

	private void strongWithinEmMultilineContent() {
		Token t;
		label_36: while (true) {
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
					t = consumeToken(BACKTICK);
					Text jjtn001 = new Text();
					boolean jjtc001 = true;
					tree.openScope(jjtn001);
					try {
						tree.closeScope(jjtn001);
						jjtc001 = false;
						jjtn001.setValue(t.image);
					} finally {
						if (jjtc001) {
							tree.closeScope(jjtn001);
						}
					}
					break;
				}
				case LBRACK: {
					t = consumeToken(LBRACK);
					Text jjtn002 = new Text();
					boolean jjtc002 = true;
					tree.openScope(jjtn002);
					try {
						tree.closeScope(jjtn002);
						jjtc002 = false;
						jjtn002.setValue(t.image);
					} finally {
						if (jjtc002) {
							tree.closeScope(jjtn002);
						}
					}
					break;
				}
				case UNDERSCORE: {
					t = consumeToken(UNDERSCORE);
					Text jjtn003 = new Text();
					boolean jjtc003 = true;
					tree.openScope(jjtn003);
					try {
						tree.closeScope(jjtn003);
						jjtc003 = false;
						jjtn003.setValue(t.image);
					} finally {
						if (jjtc003) {
							tree.closeScope(jjtn003);
						}
					}
					break;
				}
				default:
					jjLookaheadArray[29] = jjGen;
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
			if (!jj_2_44(1)) {
				break label_36;
			}
		}
	}

	private void strong() {
		Strong strong = new Strong();
		tree.openScope(strong);
		Token t;
		try {
			consumeToken(ASTERISK);
			label_37: while (true) {
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
						t = consumeToken(BACKTICK);
						Text jjtn001 = new Text();
						boolean jjtc001 = true;
						tree.openScope(jjtn001);
						try {
							tree.closeScope(jjtn001);
							jjtc001 = false;
							jjtn001.setValue(t.image);
						} finally {
							if (jjtc001) {
								tree.closeScope(jjtn001);
							}
						}
						break;
					}
					case LBRACK: {
						t = consumeToken(LBRACK);
						Text jjtn002 = new Text();
						boolean jjtc002 = true;
						tree.openScope(jjtn002);
						try {
							tree.closeScope(jjtn002);
							jjtc002 = false;
							jjtn002.setValue(t.image);
						} finally {
							if (jjtc002) {
								tree.closeScope(jjtn002);
							}
						}
						break;
					}
					case UNDERSCORE: {
						t = consumeToken(UNDERSCORE);
						Text jjtn003 = new Text();
						boolean jjtc003 = true;
						tree.openScope(jjtn003);
						try {
							tree.closeScope(jjtn003);
							jjtc003 = false;
							jjtn003.setValue(t.image);
						} finally {
							if (jjtc003) {
								tree.closeScope(jjtn003);
							}
						}
						break;
					}
					default:
						jjLookaheadArray[30] = jjGen;
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (!jj_2_49(1)) {
					break label_37;
				}
			}
			consumeToken(ASTERISK);
		} finally {
			tree.closeScope(strong);
		}
	}

	private void strongWithinEm() {
		Strong strong = new Strong();
		tree.openScope(strong);
		Token t;
		try {
			consumeToken(ASTERISK);
			label_38: while (true) {
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
						t = consumeToken(BACKTICK);
						Text jjtn001 = new Text();
						boolean jjtc001 = true;
						tree.openScope(jjtn001);
						try {
							tree.closeScope(jjtn001);
							jjtc001 = false;
							jjtn001.setValue(t.image);
						} finally {
							if (jjtc001) {
								tree.closeScope(jjtn001);
							}
						}
						break;
					}
					case LBRACK: {
						t = consumeToken(LBRACK);
						Text jjtn002 = new Text();
						boolean jjtc002 = true;
						tree.openScope(jjtn002);
						try {
							tree.closeScope(jjtn002);
							jjtc002 = false;
							jjtn002.setValue(t.image);
						} finally {
							if (jjtc002) {
								tree.closeScope(jjtn002);
							}
						}
						break;
					}
					case UNDERSCORE: {
						t = consumeToken(UNDERSCORE);
						Text jjtn003 = new Text();
						boolean jjtc003 = true;
						tree.openScope(jjtn003);
						try {
							tree.closeScope(jjtn003);
							jjtc003 = false;
							jjtn003.setValue(t.image);
						} finally {
							if (jjtc003) {
								tree.closeScope(jjtn003);
							}
						}
						break;
					}
					default:
						jjLookaheadArray[31] = jjGen;
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (jj_2_54(1)) {
					;
				} else {
					break label_38;
				}
			}
			consumeToken(ASTERISK);
		} finally {
			tree.closeScope(strong);
		}
	}

	private void emMultiline() {
		Em em = new Em();
		tree.openScope(em);
		try {
			consumeToken(UNDERSCORE);
			emMultilineContent();
			label_39: while (true) {
				if (!textAhead()) {
					break label_39;
				}
				lineBreak();
				emMultilineContent();
			}
			consumeToken(UNDERSCORE);
		} finally {
			tree.closeScope(em);
		}
	}

	private void emMultilineContent() {
		Token t;
		label_40: while (true) {
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
					t = consumeToken(ASTERISK);
					Text jjtn001 = new Text();
					boolean jjtc001 = true;
					tree.openScope(jjtn001);
					try {
						tree.closeScope(jjtn001);
						jjtc001 = false;
						jjtn001.setValue(t.image);
					} finally {
						if (jjtc001) {
							tree.closeScope(jjtn001);
						}
					}
					break;
				}
				case BACKTICK: {
					t = consumeToken(BACKTICK);
					Text jjtn002 = new Text();
					boolean jjtc002 = true;
					tree.openScope(jjtn002);
					try {
						tree.closeScope(jjtn002);
						jjtc002 = false;
						jjtn002.setValue(t.image);
					} finally {
						if (jjtc002) {
							tree.closeScope(jjtn002);
						}
					}
					break;
				}
				case LBRACK: {
					t = consumeToken(LBRACK);
					Text jjtn003 = new Text();
					boolean jjtc003 = true;
					tree.openScope(jjtn003);
					try {
						tree.closeScope(jjtn003);
						jjtc003 = false;
						jjtn003.setValue(t.image);
					} finally {
						if (jjtc003) {
							tree.closeScope(jjtn003);
						}
					}
					break;
				}
				default:
					jjLookaheadArray[32] = jjGen;
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
			if (!jj_2_59(1)) {
				break label_40;
			}
		}
	}

	private void emWithinStrongMultiline() {
		Em em = new Em();
		tree.openScope(em);
		try {
			consumeToken(UNDERSCORE);
			emWithinStrongMultilineContent();
			label_41: while (true) {
				if (!textAhead()) {
					break label_41;
				}
				lineBreak();
				emWithinStrongMultilineContent();
			}
			consumeToken(UNDERSCORE);
		} finally {
			tree.closeScope(em);
		}
	}

	private void emWithinStrongMultilineContent() {
		Token t;
		label_42: while (true) {
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
					t = consumeToken(ASTERISK);
					Text jjtn001 = new Text();
					boolean jjtc001 = true;
					tree.openScope(jjtn001);
					try {
						tree.closeScope(jjtn001);
						jjtc001 = false;
						jjtn001.setValue(t.image);
					} finally {
						if (jjtc001) {
							tree.closeScope(jjtn001);
						}
					}
					break;
				}
				case BACKTICK: {
					t = consumeToken(BACKTICK);
					Text jjtn002 = new Text();
					boolean jjtc002 = true;
					tree.openScope(jjtn002);
					try {
						tree.closeScope(jjtn002);
						jjtc002 = false;
						jjtn002.setValue(t.image);
					} finally {
						if (jjtc002) {
							tree.closeScope(jjtn002);
						}
					}
					break;
				}
				case LBRACK: {
					t = consumeToken(LBRACK);
					Text jjtn003 = new Text();
					boolean jjtc003 = true;
					tree.openScope(jjtn003);
					try {
						tree.closeScope(jjtn003);
						jjtc003 = false;
						jjtn003.setValue(t.image);
					} finally {
						if (jjtc003) {
							tree.closeScope(jjtn003);
						}
					}
					break;
				}
				default:
					jjLookaheadArray[33] = jjGen;
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
			if (!jj_2_64(1)) {
				break label_42;
			}
		}
	}

	private void em() {
		Em em = new Em();
		tree.openScope(em);
		Token t;
		try {
			consumeToken(UNDERSCORE);
			label_43: while (true) {
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
						t = consumeToken(ASTERISK);
						Text jjtn001 = new Text();
						boolean jjtc001 = true;
						tree.openScope(jjtn001);
						try {
							tree.closeScope(jjtn001);
							jjtc001 = false;
							jjtn001.setValue(t.image);
						} finally {
							if (jjtc001) {
								tree.closeScope(jjtn001);
							}
						}
						break;
					}
					case BACKTICK: {
						t = consumeToken(BACKTICK);
						Text jjtn002 = new Text();
						boolean jjtc002 = true;
						tree.openScope(jjtn002);
						try {
							tree.closeScope(jjtn002);
							jjtc002 = false;
							jjtn002.setValue(t.image);
						} finally {
							if (jjtc002) {
								tree.closeScope(jjtn002);
							}
						}
						break;
					}
					case LBRACK: {
						t = consumeToken(LBRACK);
						Text jjtn003 = new Text();
						boolean jjtc003 = true;
						tree.openScope(jjtn003);
						try {
							tree.closeScope(jjtn003);
							jjtc003 = false;
							jjtn003.setValue(t.image);
						} finally {
							if (jjtc003) {
								tree.closeScope(jjtn003);
							}
						}
						break;
					}
					default:
						jjLookaheadArray[34] = jjGen;
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (jj_2_70(1)) {
					;
				} else {
					break label_43;
				}
			}
			consumeToken(UNDERSCORE);
		} finally {
			tree.closeScope(em);
		}
	}

	private void emWithinStrong() {
		Em em = new Em();
		tree.openScope(em);
		Token t;
		try {
			consumeToken(UNDERSCORE);
			label_44: while (true) {
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
						t = consumeToken(ASTERISK);
						Text jjtn001 = new Text();
						boolean jjtc001 = true;
						tree.openScope(jjtn001);
						try {
							tree.closeScope(jjtn001);
							jjtc001 = false;
							jjtn001.setValue(t.image);
						} finally {
							if (jjtc001) {
								tree.closeScope(jjtn001);
							}
						}
						break;
					}
					case BACKTICK: {
						t = consumeToken(BACKTICK);
						Text jjtn002 = new Text();
						boolean jjtc002 = true;
						tree.openScope(jjtn002);
						try {
							tree.closeScope(jjtn002);
							jjtc002 = false;
							jjtn002.setValue(t.image);
						} finally {
							if (jjtc002) {
								tree.closeScope(jjtn002);
							}
						}
						break;
					}
					case LBRACK: {
						t = consumeToken(LBRACK);
						Text jjtn003 = new Text();
						boolean jjtc003 = true;
						tree.openScope(jjtn003);
						try {
							tree.closeScope(jjtn003);
							jjtc003 = false;
							jjtn003.setValue(t.image);
						} finally {
							if (jjtc003) {
								tree.closeScope(jjtn003);
							}
						}
						break;
					}
					default:
						jjLookaheadArray[35] = jjGen;
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (!jj_2_75(1)) {
					break label_44;
				}
			}
			consumeToken(UNDERSCORE);
		} finally {
			tree.closeScope(em);
		}
	}

	private void codeMultiline() {
		Code code = new Code();
		tree.openScope(code);
		try {
			consumeToken(BACKTICK);
			codeText();
			label_45: while (true) {
				if (!textAhead()) {
					break label_45;
				}
				lineBreak();
				whiteSpace();
				label_46: while (true) {
					switch (getNextTokenKind()) {
					case GT: {
						break;
					}
					default:
						jjLookaheadArray[36] = jjGen;
						break label_46;
					}
					consumeToken(GT);
					whiteSpace();
				}
				codeText();
			}
			consumeToken(BACKTICK);
		} finally {
			tree.closeScope(code);
		}
	}

	private void code() {
		Code code = new Code();
		tree.openScope(code);
		try {
			consumeToken(BACKTICK);
			codeText();
			consumeToken(BACKTICK);
		} finally {
			tree.closeScope(code);
		}
	}

	private void codeText() {
		Text text = new Text();
		tree.openScope(text);
		Token t;
		StringBuffer s = new StringBuffer();
		try {
			label_47: while (true) {
				switch (getNextTokenKind()) {
				case ASTERISK: {
					t = consumeToken(ASTERISK);
					s.append(t.image);
					break;
				}
				case BACKSLASH: {
					t = consumeToken(BACKSLASH);
					s.append(t.image);
					break;
				}
				case CHAR_SEQUENCE: {
					t = consumeToken(CHAR_SEQUENCE);
					s.append(t.image);
					break;
				}
				case COLON: {
					t = consumeToken(COLON);
					s.append(t.image);
					break;
				}
				case DASH: {
					t = consumeToken(DASH);
					s.append(t.image);
					break;
				}
				case DIGITS: {
					t = consumeToken(DIGITS);
					s.append(t.image);
					break;
				}
				case DOT: {
					t = consumeToken(DOT);
					s.append(t.image);
					break;
				}
				case EQ: {
					t = consumeToken(EQ);
					s.append(t.image);
					break;
				}
				case ESCAPED_CHAR: {
					t = consumeToken(ESCAPED_CHAR);
					s.append(t.image);
					break;
				}
				case IMAGE_LABEL: {
					t = consumeToken(IMAGE_LABEL);
					s.append(t.image);
					break;
				}
				case LT: {
					t = consumeToken(LT);
					s.append(t.image);
					break;
				}
				case LBRACK: {
					t = consumeToken(LBRACK);
					s.append(t.image);
					break;
				}
				case RBRACK: {
					t = consumeToken(RBRACK);
					s.append(t.image);
					break;
				}
				case LPAREN: {
					t = consumeToken(LPAREN);
					s.append(t.image);
					break;
				}
				case GT: {
					t = consumeToken(GT);
					s.append(t.image);
					break;
				}
				case RPAREN: {
					t = consumeToken(RPAREN);
					s.append(t.image);
					break;
				}
				case UNDERSCORE: {
					t = consumeToken(UNDERSCORE);
					s.append(t.image);
					break;
				}
				default:
					jjLookaheadArray[38] = jjGen;
					if (!nextAfterSpace(EOL, EOF)) {
						switch (getNextTokenKind()) {
						case SPACE: {
							t = consumeToken(SPACE);
							s.append(t.image);
							break;
						}
						case TAB: {
							t = consumeToken(TAB);
							s.append("    ");
							break;
						}
						default:
							jjLookaheadArray[37] = jjGen;
							consumeToken(-1);
							throw new RuntimeException();
						}
					} else {
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (!jj_2_76(1)) {
					break label_47;
				}
			}
			text.setValue(s.toString());
		} finally {
			tree.closeScope(text);
		}
	}

	private void text() {
		Text text = new Text();
		tree.openScope(text);
		Token t;
		StringBuffer s = new StringBuffer();
		try {
			label_48: while (true) {
				switch (getNextTokenKind()) {
				case BACKSLASH: {
					t = consumeToken(BACKSLASH);
					s.append(t.image);
					break;
				}
				case CHAR_SEQUENCE: {
					t = consumeToken(CHAR_SEQUENCE);
					s.append(t.image);
					break;
				}
				case COLON: {
					t = consumeToken(COLON);
					s.append(t.image);
					break;
				}
				case DASH: {
					t = consumeToken(DASH);
					s.append(t.image);
					break;
				}
				case DIGITS: {
					t = consumeToken(DIGITS);
					s.append(t.image);
					break;
				}
				case DOT: {
					t = consumeToken(DOT);
					s.append(t.image);
					break;
				}
				case EQ: {
					t = consumeToken(EQ);
					s.append(t.image);
					break;
				}
				case ESCAPED_CHAR: {
					t = consumeToken(ESCAPED_CHAR);
					s.append(t.image.substring(1));
					break;
				}
				case GT: {
					t = consumeToken(GT);
					s.append(t.image);
					break;
				}
				case IMAGE_LABEL: {
					t = consumeToken(IMAGE_LABEL);
					s.append(t.image);
					break;
				}
				case LPAREN: {
					t = consumeToken(LPAREN);
					s.append(t.image);
					break;
				}
				case LT: {
					t = consumeToken(LT);
					s.append(t.image);
					break;
				}
				case RBRACK: {
					t = consumeToken(RBRACK);
					s.append(t.image);
					break;
				}
				case RPAREN: {
					t = consumeToken(RPAREN);
					s.append(t.image);
					break;
				}
				default:
					jjLookaheadArray[40] = jjGen;
					if (!nextAfterSpace(EOL, EOF)) {
						switch (getNextTokenKind()) {
						case SPACE: {
							t = consumeToken(SPACE);
							s.append(t.image);
							break;
						}
						case TAB: {
							t = consumeToken(TAB);
							s.append("    ");
							break;
						}
						default:
							jjLookaheadArray[39] = jjGen;
							consumeToken(-1);
							throw new RuntimeException();
						}
					} else {
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (!jj_2_77(1)) {
					break label_48;
				}
			}
			text.setValue(s.toString());
		} finally {
			tree.closeScope(text);
		}
	}

	private void looseChar() {
		Text text = new Text();
		tree.openScope(text);
		Token t;
		try {
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
			default:
				jjLookaheadArray[41] = jjGen;
				consumeToken(-1);
				throw new RuntimeException();
			}
			text.setValue(t.image);
		} finally {
			tree.closeScope(text);
		}
	}

	private void lineBreak() {
		LineBreak linebreak = new LineBreak();
		tree.openScope(linebreak);
		try {
			label_49: while (true) {
				switch (getNextTokenKind()) {
				case SPACE:
				case TAB: {
					break;
				}
				default:
					jjLookaheadArray[42] = jjGen;
					break label_49;
				}
				switch (getNextTokenKind()) {
				case SPACE: {
					consumeToken(SPACE);
					break;
				}
				case TAB: {
					consumeToken(TAB);
					break;
				}
				default:
					jjLookaheadArray[43] = jjGen;
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
			consumeToken(EOL);
		} finally {
			tree.closeScope(linebreak);
		}
	}

	private void whiteSpace() {
		label_50: while (true) {
			switch (getNextTokenKind()) {
			case SPACE:
			case TAB: {
				break;
			}
			default:
				jjLookaheadArray[44] = jjGen;
				break label_50;
			}
			switch (getNextTokenKind()) {
			case SPACE: {
				consumeToken(SPACE);
				break;
			}
			case TAB: {
				consumeToken(TAB);
				break;
			}
			default:
				jjLookaheadArray[45] = jjGen;
				consumeToken(-1);
				throw new RuntimeException();
			}
		}
	}

	private boolean jj_2_2(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_227();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(1, xla);
		}
	}

	private boolean jj_2_3(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_53();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			////save(2, xla);
		}
	}

	private boolean jj_2_4(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_232();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			////save(3, xla);
		}
	}

	private boolean jj_2_5(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_5();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(4, xla);
		}
	}

	private boolean jj_2_6(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(5, xla);
		}
	}

	private boolean jj_2_7(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(6, xla);
		}
	}

	private boolean jj_2_8(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(7, xla);
		}
	}

	private boolean jj_2_9(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_64();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(8, xla);
		}
	}

	private boolean jj_2_10(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_65();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(9, xla);
		}
	}

	private boolean jj_2_11(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(10, xla);
		}
	}

	private boolean jj_2_12(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_67();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(11, xla);
		}
	}

	private boolean jj_2_13(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !scanForBlockElement();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(12, xla);
		}
	}

	private boolean jj_2_14(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_14();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(13, xla);
		}
	}

	private boolean jj_2_15(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !scanForBlockElement();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(14, xla);
		}
	}

	private boolean jj_2_16(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !scanForBlockElement();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(15, xla);
		}
	}

	private boolean jj_2_17(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_17();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(16, xla);
		}
	}

	private boolean jj_2_18(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(17, xla);
		}
	}

	private boolean jj_2_19(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(18, xla);
		}
	}

	private boolean jj_2_20(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(19, xla);
		}
	}

	private boolean jj_2_21(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_21();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(20, xla);
		}
	}

	private boolean jj_2_22(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_94();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(21, xla);
		}
	}

	private boolean jj_2_23(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_23();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(22, xla);
		}
	}

	private boolean jj_2_24(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_96();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(23, xla);
		}
	}

	private boolean jj_2_25(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(24, xla);
		}
	}

	private boolean jj_2_26(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_64();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(25, xla);
		}
	}

	private boolean jj_2_27(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_65();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(26, xla);
		}
	}

	private boolean jj_2_28(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(27, xla);
		}
	}

	private boolean jj_2_29(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_94();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(28, xla);
		}
	}

	private boolean jj_2_30(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_30();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(29, xla);
		}
	}

	private boolean jj_2_31(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_96();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(30, xla);
		}
	}

	private boolean jj_2_32(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_32();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(31, xla);
		}
	}

	private boolean jj_2_33(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_33();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(32, xla);
		}
	}

	private boolean jj_2_34(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(33, xla);
		}
	}

	private boolean jj_2_35(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(34, xla);
		}
	}

	private boolean jj_2_36(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(35, xla);
		}
	}

	private boolean jj_2_37(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(36, xla);
		}
	}

	private boolean jj_2_38(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_134();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(37, xla);
		}
	}

	private boolean jj_2_39(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_39();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(38, xla);
		}
	}

	private boolean jj_2_40(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(39, xla);
		}
	}

	private boolean jj_2_41(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(40, xla);
		}
	}

	private boolean jj_2_42(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(41, xla);
		}
	}

	private boolean jj_2_43(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(42, xla);
		}
	}

	private boolean jj_2_44(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_44();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(43, xla);
		}
	}

	private boolean jj_2_45(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(44, xla);
		}
	}

	private boolean jj_2_46(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(45, xla);
		}
	}

	private boolean jj_2_47(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(46, xla);
		}
	}

	private boolean jj_2_48(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_148();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(47, xla);
		}
	}

	private boolean jj_2_49(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_49();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(48, xla);
		}
	}

	private boolean jj_2_50(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(49, xla);
		}
	}

	private boolean jj_2_51(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(50, xla);
		}
	}

	private boolean jj_2_52(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(51, xla);
		}
	}

	private boolean jj_2_53(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(52, xla);
		}
	}

	private boolean jj_2_54(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_54();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(53, xla);
		}
	}

	private boolean jj_2_55(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(54, xla);
		}
	}

	private boolean jj_2_56(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(55, xla);
		}
	}

	private boolean jj_2_57(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(56, xla);
		}
	}

	private boolean jj_2_58(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_162();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(57, xla);
		}
	}

	private boolean jj_2_59(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_59();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(58, xla);
		}
	}

	private boolean jj_2_60(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(59, xla);
		}
	}

	private boolean jj_2_61(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(60, xla);
		}
	}

	private boolean jj_2_62(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(61, xla);
		}
	}

	private boolean jj_2_63(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(62, xla);
		}
	}

	private boolean jj_2_64(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_64();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(63, xla);
		}
	}

	private boolean jj_2_65(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(64, xla);
		}
	}

	private boolean jj_2_66(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(65, xla);
		}
	}

	private boolean jj_2_67(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(66, xla);
		}
	}

	private boolean jj_2_68(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(67, xla);
		}
	}

	private boolean jj_2_69(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_176();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(68, xla);
		}
	}

	private boolean jj_2_70(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_70();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(69, xla);
		}
	}

	private boolean jj_2_71(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(70, xla);
		}
	}

	private boolean jj_2_72(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(71, xla);
		}
	}

	private boolean jj_2_73(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(72, xla);
		}
	}

	private boolean jj_2_74(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3R_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(73, xla);
		}
	}

	private boolean jj_2_75(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_75();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(74, xla);
		}
	}

	private boolean jj_2_76(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_76();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(75, xla);
		}
	}

	private boolean jj_2_77(int xla) {
		jjLookAhead = xla;
		jj_lastpos = scanPosition = token;
		try {
			return !jj_3_77();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			//save(76, xla);
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
																jj_semLA = !nextAfterSpace(EOL, EOF);
																lookingAhead = false;
																if (!jj_semLA || jj_3R_222())
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
																			jj_semLA = !nextAfterSpace(EOL, EOF);
																			lookingAhead = false;
																			if (!jj_semLA || jj_3R_207())
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
		while (true) {
			xsp = scanPosition;
			if (jj_3_76()) {
				scanPosition = xsp;
				break;
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
		if (scanToken(BACKTICK)) {
			return true;
		}
		return false;
	}

	private boolean jj_3R_242() {
		if (scanToken(BACKTICK)) {
			return true;
		}
		if (jj_3R_236()) {
			return true;
		}
		Token xsp;
		while (true) {
			xsp = scanPosition;
			if (jj_3R_272()) {
				scanPosition = xsp;
				break;
			}
		}
		if (scanToken(BACKTICK)) {
			return true;
		}
		return false;
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
		while (true) {
			xsp = scanPosition;
			if (jj_3_70()) {
				scanPosition = xsp;
				break;
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
		while (true) {
			xsp = scanPosition;
			if (jj_3_64()) {
				scanPosition = xsp;
				break;
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
		while (true) {
			xsp = scanPosition;
			if (jj_3R_249()) {
				scanPosition = xsp;
				break;
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
					jj_semLA = multilineAhead(BACKTICK);
					lookingAhead = false;
					if (!jj_semLA || jj_3R_242()) {
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
		while (true) {
			xsp = scanPosition;
			if (jj_3_54()) {
				scanPosition = xsp;
				break;
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
					jj_semLA = multilineAhead(BACKTICK);
					lookingAhead = false;
					if (!jj_semLA || jj_3R_242()) {
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
																			jj_semLA = !nextAfterSpace(RPAREN);
																			lookingAhead = false;
																			if (!jj_semLA || jj_3R_133()) {
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
															jj_semLA = !nextAfterSpace(RBRACK);
															lookingAhead = false;
															if (!jj_semLA || jj_3R_115())
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
					jj_semLA = multilineAhead(ASTERISK);
					lookingAhead = false;
					if (!jj_semLA || scanToken(ASTERISK)) {
						scanPosition = xsp;
						lookingAhead = true;
						jj_semLA = multilineAhead(UNDERSCORE);
						lookingAhead = false;
						if (!jj_semLA || scanToken(UNDERSCORE)) {
							scanPosition = xsp;
							lookingAhead = true;
							jj_semLA = multilineAhead(BACKTICK);
							lookingAhead = false;
							if (!jj_semLA || jj_3R_242()) {
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
		if (scanToken(EOL)) {
			return true;
		}
		if (jj_3R_228()) {
			return true;
		}
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
																				jj_semLA = !nextAfterSpace(EOL, EOF);
																				lookingAhead = false;
																				if (!jj_semLA || jj_3R_86()) {
																					scanPosition = xsp;
																					lookingAhead = true;
																					jj_semLA = !fencesAhead();
																					lookingAhead = false;
																					if (!jj_semLA || jj_3R_87())
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
		return false;
	}

	private boolean jj_3R_230() {
		if (scanToken(EOL)) {
			return true;
		}
		return jj_3R_261();
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
		if (jj_3R_231())
			scanPosition = xsp;
		return false;
	}

	private boolean jj_3R_237() {
		if (scanToken(GT)) {
			return true;
		}
		return jj_3R_228();
	}

	private boolean jj_3R_227() {
		if (scanToken(DIGITS)) {
			return true;
		}
		return scanToken(DOT);
	}

	private boolean jj_3_14() {
		if (jj_3R_67()) {
			return true;
		}
		return scanToken(EOL);
	}

	private boolean jj_3R_67() {
		if (scanToken(EOL)) {
			return true;
		}
		if (jj_3R_228()) {
			return true;
		}
		Token xsp;
		if (jj_3R_237()) {
			return true;
		}
		loop: while (true) {
			xsp = scanPosition;
			if (jj_3R_237()) {
				scanPosition = xsp;
				break loop;
			}
		}
		return false;
	}


	private boolean jj_3_5() {
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
									return true;
							}
						}
					}
				}
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
		Token xsp;
		xsp = scanPosition;
		lookingAhead = true;
		jj_semLA = headingAhead(1);
		lookingAhead = false;
		if (!jj_semLA || jj_3R_256()) {
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
		Token oldToken;
		if ((oldToken = token).next != null) {
			token = token.next;
		} else {
			token = token.next = tm.getNextToken();
		}
		nextTokenKind = -1;
		if (token.kind == kind) {
			jjGen++;
//			if (++jj_gc > 100) {
//				jj_gc = 0;
//				for (int i = 0; i < jj2Rtns.length; i++) {
//					JJCalls c = jj2Rtns[i];
//					while (c != null) {
//						if (c.gen < jjGen)
//							c.first = null;
//						c = c.next;
//					}
//				}
//			}
			return token;
		}
		token = oldToken;
		return token;
	}

	private boolean scanToken(int kind) {
		if (scanPosition == jj_lastpos) {
			jjLookAhead--;
			if (scanPosition.next == null) {
				jj_lastpos = scanPosition = scanPosition.next = tm.getNextToken();
			} else {
				jj_lastpos = scanPosition = scanPosition.next;
			}
		} else {
			scanPosition = scanPosition.next;
		}
		if (scanPosition.kind != kind) {
			return true;
		}
		if (jjLookAhead == 0 && scanPosition == jj_lastpos) {
			throw jj_ls;
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
		if(nextTokenKind != -1) { return nextTokenKind; }
		if ((jj_nt = token.next) == null) {
			return (nextTokenKind = (token.next = tm.getNextToken()).kind);
		}
		return (nextTokenKind = jj_nt.kind);
	}

	private void jj_add_error_token(int kind, int pos) {
		if (pos >= 100) {
			return;
		}
		if (pos == jj_endpos + 1) {
			jj_lasttokens[jj_endpos++] = kind;
		} else if (jj_endpos != 0) {
			jj_expentry = new int[jj_endpos];
			for (int i = 0; i < jj_endpos; i++) {
				jj_expentry[i] = jj_lasttokens[i];
			}
			loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
				int[] oldentry = (int[]) (it.next());
				if (oldentry.length == jj_expentry.length) {
					for (int i = 0; i < jj_expentry.length; i++) {
						if (oldentry[i] != jj_expentry[i]) {
							continue loop;
						}
					}
					jj_expentries.add(jj_expentry);
					break loop;
				}
			}
			if (pos != 0) {
				jj_lasttokens[(jj_endpos = pos) - 1] = kind;
			}
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
