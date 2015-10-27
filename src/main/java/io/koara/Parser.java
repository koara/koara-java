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

	private TreeState tree = new TreeState();
	private int currentBlockLevel = 0;
	private int currentQuoteLevel = 0;
	private JJCalls[] jj_2_rtns = new JJCalls[77];
	private boolean jj_rescan = false;
	private int jj_gc = 0;
	private TokenManager tm;
	private CharStream cs;
	private Token token;
	private Token jj_nt;
	private int nextTokenKind;
	private Token jj_scanpos, jj_lastpos;
	private int jj_la;
	private boolean jj_lookingAhead = false;
	private boolean jj_semLA;
	private int jj_gen;
	private int[] jj_la1 = new int[46];
	private java.util.List<int[]> jj_expentries = new ArrayList<int[]>();
	private int[] jj_expentry;
	private int[] jj_lasttokens = new int[100];
	private int jj_endpos;
	private LookaheadSuccess jj_ls = new LookaheadSuccess();

	public Document parse(String text) {
		cs = new CharStream(new StringReader(text));
		tm = new TokenManager(cs);
		token = new Token();
		nextTokenKind = -1;
		jj_gen = 0;
		for (int i = 0; i < 46; i++) {
			jj_la1[i] = -1;
		}
		for (int i = 0; i < jj_2_rtns.length; i++) {
			jj_2_rtns[i] = new JJCalls();
		}
		return document();
	}

	private Document document() {
		Document document = new Document();
		tree.openNodeScope(document);
		try {
			loop: while (true) {
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
				case EOL: break;
				default:
					jj_la1[0] = jj_gen;
					break loop;
				}
				consumeToken(EOL);
			}
			whiteSpace();
			if (jj_2_1(1)) {
				blockElement();
				label_2: while (true) {
					if (blockAhead()) {
						;
					} else {
						break label_2;
					}
					label_3: while (true) {
						consumeToken(EOL);
						whiteSpace();
						switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
						case EOL: {
							;
							break;
						}
						default:
							jj_la1[1] = jj_gen;
							break label_3;
						}
					}
					blockElement();
				}
				label_4: while (true) {
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case EOL: {
						;
						break;
					}
					default:
						jj_la1[2] = jj_gen;
						break label_4;
					}
					consumeToken(EOL);
				}
				whiteSpace();
			} else {
				;
			}
			consumeToken(0);
		} finally {
			tree.closeNodeScope(document);
		}
		return document;
	}

	private void blockElement() {
		currentBlockLevel++;
		if (headingAhead(1)) {
			heading();
		} else {
			switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
			case GT: {
				blockquote();
				break;
			}
			case DASH: {
				unorderedList();
				break;
			}
			default:
				jj_la1[3] = jj_gen;
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
		boolean jjtc000 = true;
		tree.openNodeScope(heading);
		Token t;
		int headingLevel = 0;
		try {
			label_5: while (true) {
				consumeToken(EQ);
				headingLevel++;
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
				case EQ: {
					;
					break;
				}
				default:
					jj_la1[4] = jj_gen;
					break label_5;
				}
			}
			whiteSpace();
			label_6: while (true) {
				if (jj_2_5(1)) {
					;
				} else {
					break label_6;
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
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case ASTERISK:
					case BACKTICK:
					case LBRACK:
					case UNDERSCORE: {
						looseChar();
						break;
					}
					default:
						jj_la1[5] = jj_gen;
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
			}
			heading.jjtSetValue(headingLevel);
		} finally {
			tree.closeNodeScope(heading);
		}
	}

	private void blockquote() {
		Blockquote blockquote = new Blockquote();
		tree.openNodeScope(blockquote);
		currentQuoteLevel++;
		try {
			consumeToken(GT);
			label_7: while (true) {
				if (jj_2_12(2147483647)) {
					;
				} else {
					break label_7;
				}
				blockquoteEmptyLine();
			}
			whiteSpace();
			if (jj_2_13(1)) {
				blockElement();
				label_8: while (true) {
					if (blockAhead()) {
						;
					} else {
						break label_8;
					}
					label_9: while (true) {
						consumeToken(EOL);
						whiteSpace();
						blockquotePrefix();
						switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
						case EOL: {
							;
							break;
						}
						default:
							jj_la1[6] = jj_gen;
							break label_9;
						}
					}
					blockElement();
				}
			} else {
				;
			}
			label_10: while (true) {
				if (jj_2_14(2147483647)) {
					;
				} else {
					break label_10;
				}
				blockquoteEmptyLine();
			}
			currentQuoteLevel--;
		} finally {
			tree.closeNodeScope(blockquote);
		}
	}

	private void blockquotePrefix() {
		int i = 0;
		label_11: while (true) {
			consumeToken(GT);
			whiteSpace();
			if (++i < currentQuoteLevel) {
				;
			} else {
				break label_11;
			}
		}
	}

	private void blockquoteEmptyLine() {
		consumeToken(EOL);
		whiteSpace();
		label_12: while (true) {
			consumeToken(GT);
			whiteSpace();
			switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
			case GT: {
				;
				break;
			}
			default:
				jj_la1[7] = jj_gen;
				break label_12;
			}
		}
	}

	private void unorderedList() {
		List list = new List();
		tree.openNodeScope(list);
		try {
			unorderedListItem();
			label_13: while (true) {
				if (listItemAhead(false)) {
					;
				} else {
					break label_13;
				}
				label_14: while (true) {
					consumeToken(EOL);
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case EOL: {
						;
						break;
					}
					default:
						jj_la1[8] = jj_gen;
						break label_14;
					}
				}
				whiteSpace();
				unorderedListItem();
			}
		} finally {
			tree.closeNodeScope(list);
		}
	}

	private void unorderedListItem() {
		ListItem listItem = new ListItem();
		tree.openNodeScope(listItem);
		try {
			consumeToken(DASH);
			whiteSpace();
			if (jj_2_15(1)) {
				blockElement();
				label_15: while (true) {
					if (blockAhead()) {
						;
					} else {
						break label_15;
					}
					label_16: while (true) {
						consumeToken(EOL);
						whiteSpace();
						if (currentQuoteLevel > 0) {
							blockquotePrefix();
						} else {
							;
						}
						switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
						case EOL: {
							;
							break;
						}
						default:
							jj_la1[9] = jj_gen;
							break label_16;
						}
					}
					blockElement();
				}
			} 
		} finally {
			tree.closeNodeScope(listItem);
		}
	}

	private void orderedList() {
		List list = new List();
		tree.openNodeScope(list);
		try {
			orderedListItem();
			label_17: while (true) {
				if (!listItemAhead(true)) {
					break label_17;
				}
				label_18: while (true) {
					consumeToken(EOL);
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case EOL: {
						;
						break;
					}
					default:
						jj_la1[10] = jj_gen;
						break label_18;
					}
				}
				whiteSpace();
				orderedListItem();
			}
			list.setOrdered(true);
		} finally {
			tree.closeNodeScope(list);
		}
	}

	private void orderedListItem() {
		ListItem listItem = new ListItem();
		tree.openNodeScope(listItem);
		Token t;
		try {
			t = consumeToken(DIGITS);
			consumeToken(DOT);
			whiteSpace();
			if (jj_2_16(1)) {
				blockElement();
				label_19: while (true) {
					if (blockAhead()) {
						;
					} else {
						break label_19;
					}
					label_20: while (true) {
						consumeToken(EOL);
						whiteSpace();
						if (currentQuoteLevel > 0) {
							blockquotePrefix();
						} else {
							;
						}
						switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
						case EOL: {
							;
							break;
						}
						default:
							jj_la1[11] = jj_gen;
							break label_20;
						}
					}
					blockElement();
				}
			} else {
				;
			}
			listItem.setNumber(Integer.valueOf(Integer.valueOf(t.image)));
		} finally {
			tree.closeNodeScope(listItem);
		}
	}

	private void fencedCodeBlock() {
		CodeBlock codeBlock = new CodeBlock();
		tree.openNodeScope(codeBlock);
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
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
				case BACKTICK: {
					;
					break;
				}
				default:
					jj_la1[12] = jj_gen;
					break label_21;
				}
			}
			whiteSpace();
			switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
			case BACKTICK:
			case CHAR_SEQUENCE: {
				language = codeLanguage();
				codeBlock.setLanguage(language);
				break;
			}
			default:
				jj_la1[13] = jj_gen;
				;
			}
			if (getToken(1).kind != EOF && !fencesAhead()) {
				consumeToken(EOL);
				levelWhiteSpace(beginColumn);
			} else {
				;
			}
			label_22: while (true) {
				if (jj_2_17(1)) {
					;
				} else {
					break label_22;
				}
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
					jj_la1[15] = jj_gen;
					if (!nextAfterSpace(EOL, EOF)) {
						switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
							jj_la1[14] = jj_gen;
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
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case BACKTICK: {
						;
						break;
					}
					default:
						jj_la1[16] = jj_gen;
						break label_23;
					}
				}
			} else {
				;
			}
			codeBlock.jjtSetValue(s.toString());
		} finally {
			tree.closeNodeScope(codeBlock);
		}
	}

	private void levelWhiteSpace(int threshold) {
		Token t;
		int currentPos = 1;
		label_24: while (true) {
			if ((getToken(1).kind == SPACE || getToken(1).kind == TAB) && currentPos < (threshold - 1)) {
				;
			} else {
				break label_24;
			}
			switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
				jj_la1[17] = jj_gen;
				consumeToken(-1);
				throw new RuntimeException();
			}
		}
	}

	private String codeLanguage() {
		Token t;
		StringBuilder s = new StringBuilder();
		label_25: while (true) {
			switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
				jj_la1[18] = jj_gen;
				consumeToken(-1);
				throw new RuntimeException();
			}
			switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
			case BACKTICK:
			case CHAR_SEQUENCE: {
				;
				break;
			}
			default:
				jj_la1[19] = jj_gen;
				break label_25;
			}
		}
		return s.toString();
	}

	private void paragraph() {
		Paragraph paragraph = new Paragraph();
		tree.openNodeScope(paragraph);
		try {
			inline();
			label_26: while (true) {
				if (textAhead()) {
					;
				} else {
					break label_26;
				}
				lineBreak();
				whiteSpace();
				label_27: while (true) {
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case GT: {
						;
						break;
					}
					default:
						jj_la1[20] = jj_gen;
						break label_27;
					}
					consumeToken(GT);
					whiteSpace();
				}
				inline();
			}
		} finally {
			tree.closeNodeScope(paragraph);
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
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
				case ASTERISK:
				case BACKTICK:
				case LBRACK:
				case UNDERSCORE: {
					looseChar();
					break;
				}
				default:
					jj_la1[21] = jj_gen;
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
			if (jj_2_21(1)) {
				;
			} else {
				break label_28;
			}
		}
	}

	private void image() {
		Image image = new Image();
		tree.openNodeScope(image);
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
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case ASTERISK:
					case BACKTICK:
					case LBRACK:
					case UNDERSCORE: {
						looseChar();
						break;
					}
					default:
						jj_la1[22] = jj_gen;
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (jj_2_23(1)) {
					;
				} else {
					break label_29;
				}
			}
			whiteSpace();
			consumeToken(RBRACK);
			if (jj_2_24(2147483647)) {
				ref = resourceUrl();
			} else {
				;
			}
			image.jjtSetValue(ref);
		} finally {
			tree.closeNodeScope(image);
		}
	}

	private void link() {
		Link link = new Link();
		tree.openNodeScope(link);
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
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case ASTERISK:
					case BACKTICK:
					case LBRACK:
					case UNDERSCORE: {
						looseChar();
						break;
					}
					default:
						jj_la1[23] = jj_gen;
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (jj_2_30(1)) {
					;
				} else {
					break label_30;
				}
			}
			whiteSpace();
			consumeToken(RBRACK);
			if (jj_2_31(2147483647)) {
				ref = resourceUrl();
			} else {
				;
			}
			link.jjtSetValue(ref);
		} finally {
			tree.closeNodeScope(link);
		}
	}

	private void resourceText() {
		Text text = new Text();
		tree.openNodeScope(text);
		Token t;
		StringBuilder s = new StringBuilder();
		try {
			label_31: while (true) {
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
					jj_la1[25] = jj_gen;
					if (!nextAfterSpace(RBRACK)) {
						switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
							jj_la1[24] = jj_gen;
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
			text.jjtSetValue(s.toString());
		} finally {
			tree.closeNodeScope(text);
		}
	}

	private String resourceUrl() {
		String ref = "";
		consumeToken(LPAREN);
		whiteSpace();
		ref = resourceUrlText();
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
			switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
				jj_la1[27] = jj_gen;
				if (!nextAfterSpace(RPAREN)) {
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
						jj_la1[26] = jj_gen;
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
		tree.openNodeScope(strong);
		try {
			consumeToken(ASTERISK);
			strongMultilineContent();
			label_33: while (true) {
				if (textAhead()) {
					;
				} else {
					break label_33;
				}
				lineBreak();
				strongMultilineContent();
			}
			consumeToken(ASTERISK);
		} finally {
			tree.closeNodeScope(strong);
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
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
				case BACKTICK: {
					t = consumeToken(BACKTICK);
					Text jjtn001 = new Text();
					boolean jjtc001 = true;
					tree.openNodeScope(jjtn001);
					try {
						tree.closeNodeScope(jjtn001);
						jjtc001 = false;
						jjtn001.jjtSetValue(t.image);
					} finally {
						if (jjtc001) {
							tree.closeNodeScope(jjtn001);
						}
					}
					break;
				}
				case LBRACK: {
					t = consumeToken(LBRACK);
					Text jjtn002 = new Text();
					boolean jjtc002 = true;
					tree.openNodeScope(jjtn002);
					try {
						tree.closeNodeScope(jjtn002);
						jjtc002 = false;
						jjtn002.jjtSetValue(t.image);
					} finally {
						if (jjtc002) {
							tree.closeNodeScope(jjtn002);
						}
					}
					break;
				}
				case UNDERSCORE: {
					t = consumeToken(UNDERSCORE);
					Text jjtn003 = new Text();
					boolean jjtc003 = true;
					tree.openNodeScope(jjtn003);
					try {
						tree.closeNodeScope(jjtn003);
						jjtc003 = false;
						jjtn003.jjtSetValue(t.image);
					} finally {
						if (jjtc003) {
							tree.closeNodeScope(jjtn003);
						}
					}
					break;
				}
				default:
					jj_la1[28] = jj_gen;
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
			if (jj_2_39(1)) {
				;
			} else {
				break label_34;
			}
		}
	}

	private void strongWithinEmMultiline() {
		Strong jjtn000 = new Strong();
		boolean jjtc000 = true;
		tree.openNodeScope(jjtn000);
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
		} catch (Throwable jjte000) {
			if (jjtc000) {
				tree.clearNodeScope(jjtn000);
				jjtc000 = false;
			} else {
				tree.popNode();
			}
			if (jjte000 instanceof RuntimeException) {
				{
					if (true)
						throw (RuntimeException) jjte000;
				}
			}
			{
				if (true)
					throw (Error) jjte000;
			}
		} finally {
			if (jjtc000) {
				tree.closeNodeScope(jjtn000);
			}
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
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
				case BACKTICK: {
					t = consumeToken(BACKTICK);
					Text jjtn001 = new Text();
					boolean jjtc001 = true;
					tree.openNodeScope(jjtn001);
					try {
						tree.closeNodeScope(jjtn001);
						jjtc001 = false;
						jjtn001.jjtSetValue(t.image);
					} finally {
						if (jjtc001) {
							tree.closeNodeScope(jjtn001);
						}
					}
					break;
				}
				case LBRACK: {
					t = consumeToken(LBRACK);
					Text jjtn002 = new Text();
					boolean jjtc002 = true;
					tree.openNodeScope(jjtn002);
					try {
						tree.closeNodeScope(jjtn002);
						jjtc002 = false;
						jjtn002.jjtSetValue(t.image);
					} finally {
						if (jjtc002) {
							tree.closeNodeScope(jjtn002);
						}
					}
					break;
				}
				case UNDERSCORE: {
					t = consumeToken(UNDERSCORE);
					Text jjtn003 = new Text();
					boolean jjtc003 = true;
					tree.openNodeScope(jjtn003);
					try {
						tree.closeNodeScope(jjtn003);
						jjtc003 = false;
						jjtn003.jjtSetValue(t.image);
					} finally {
						if (jjtc003) {
							tree.closeNodeScope(jjtn003);
						}
					}
					break;
				}
				default:
					jj_la1[29] = jj_gen;
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
			if (jj_2_44(1)) {
				;
			} else {
				break label_36;
			}
		}
	}

	private void strong() {
		Strong strong = new Strong();
		tree.openNodeScope(strong);
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
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case BACKTICK: {
						t = consumeToken(BACKTICK);
						Text jjtn001 = new Text();
						boolean jjtc001 = true;
						tree.openNodeScope(jjtn001);
						try {
							tree.closeNodeScope(jjtn001);
							jjtc001 = false;
							jjtn001.jjtSetValue(t.image);
						} finally {
							if (jjtc001) {
								tree.closeNodeScope(jjtn001);
							}
						}
						break;
					}
					case LBRACK: {
						t = consumeToken(LBRACK);
						Text jjtn002 = new Text();
						boolean jjtc002 = true;
						tree.openNodeScope(jjtn002);
						try {
							tree.closeNodeScope(jjtn002);
							jjtc002 = false;
							jjtn002.jjtSetValue(t.image);
						} finally {
							if (jjtc002) {
								tree.closeNodeScope(jjtn002);
							}
						}
						break;
					}
					case UNDERSCORE: {
						t = consumeToken(UNDERSCORE);
						Text jjtn003 = new Text();
						boolean jjtc003 = true;
						tree.openNodeScope(jjtn003);
						try {
							tree.closeNodeScope(jjtn003);
							jjtc003 = false;
							jjtn003.jjtSetValue(t.image);
						} finally {
							if (jjtc003) {
								tree.closeNodeScope(jjtn003);
							}
						}
						break;
					}
					default:
						jj_la1[30] = jj_gen;
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
			tree.closeNodeScope(strong);
		}
	}

	private void strongWithinEm() {
		Strong strong = new Strong();
		tree.openNodeScope(strong);
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
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case BACKTICK: {
						t = consumeToken(BACKTICK);
						Text jjtn001 = new Text();
						boolean jjtc001 = true;
						tree.openNodeScope(jjtn001);
						try {
							tree.closeNodeScope(jjtn001);
							jjtc001 = false;
							jjtn001.jjtSetValue(t.image);
						} finally {
							if (jjtc001) {
								tree.closeNodeScope(jjtn001);
							}
						}
						break;
					}
					case LBRACK: {
						t = consumeToken(LBRACK);
						Text jjtn002 = new Text();
						boolean jjtc002 = true;
						tree.openNodeScope(jjtn002);
						try {
							tree.closeNodeScope(jjtn002);
							jjtc002 = false;
							jjtn002.jjtSetValue(t.image);
						} finally {
							if (jjtc002) {
								tree.closeNodeScope(jjtn002);
							}
						}
						break;
					}
					case UNDERSCORE: {
						t = consumeToken(UNDERSCORE);
						Text jjtn003 = new Text();
						boolean jjtc003 = true;
						tree.openNodeScope(jjtn003);
						try {
							tree.closeNodeScope(jjtn003);
							jjtc003 = false;
							jjtn003.jjtSetValue(t.image);
						} finally {
							if (jjtc003) {
								tree.closeNodeScope(jjtn003);
							}
						}
						break;
					}
					default:
						jj_la1[31] = jj_gen;
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
			tree.closeNodeScope(strong);
		}
	}

	private void emMultiline() {
		Em em = new Em();
		tree.openNodeScope(em);
		try {
			consumeToken(UNDERSCORE);
			emMultilineContent();
			label_39: while (true) {
				if (textAhead()) {
					;
				} else {
					break label_39;
				}
				lineBreak();
				emMultilineContent();
			}
			consumeToken(UNDERSCORE);
		} finally {
			tree.closeNodeScope(em);
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
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
				case ASTERISK: {
					t = consumeToken(ASTERISK);
					Text jjtn001 = new Text();
					boolean jjtc001 = true;
					tree.openNodeScope(jjtn001);
					try {
						tree.closeNodeScope(jjtn001);
						jjtc001 = false;
						jjtn001.jjtSetValue(t.image);
					} finally {
						if (jjtc001) {
							tree.closeNodeScope(jjtn001);
						}
					}
					break;
				}
				case BACKTICK: {
					t = consumeToken(BACKTICK);
					Text jjtn002 = new Text();
					boolean jjtc002 = true;
					tree.openNodeScope(jjtn002);
					try {
						tree.closeNodeScope(jjtn002);
						jjtc002 = false;
						jjtn002.jjtSetValue(t.image);
					} finally {
						if (jjtc002) {
							tree.closeNodeScope(jjtn002);
						}
					}
					break;
				}
				case LBRACK: {
					t = consumeToken(LBRACK);
					Text jjtn003 = new Text();
					boolean jjtc003 = true;
					tree.openNodeScope(jjtn003);
					try {
						tree.closeNodeScope(jjtn003);
						jjtc003 = false;
						jjtn003.jjtSetValue(t.image);
					} finally {
						if (jjtc003) {
							tree.closeNodeScope(jjtn003);
						}
					}
					break;
				}
				default:
					jj_la1[32] = jj_gen;
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
			if (jj_2_59(1)) {
				;
			} else {
				break label_40;
			}
		}
	}

	private void emWithinStrongMultiline() {
		Em em = new Em();
		tree.openNodeScope(em);
		try {
			consumeToken(UNDERSCORE);
			emWithinStrongMultilineContent();
			label_41: while (true) {
				if (textAhead()) {
					;
				} else {
					break label_41;
				}
				lineBreak();
				emWithinStrongMultilineContent();
			}
			consumeToken(UNDERSCORE);
		} finally {
			tree.closeNodeScope(em);
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
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
				case ASTERISK: {
					t = consumeToken(ASTERISK);
					Text jjtn001 = new Text();
					boolean jjtc001 = true;
					tree.openNodeScope(jjtn001);
					try {
						tree.closeNodeScope(jjtn001);
						jjtc001 = false;
						jjtn001.jjtSetValue(t.image);
					} finally {
						if (jjtc001) {
							tree.closeNodeScope(jjtn001);
						}
					}
					break;
				}
				case BACKTICK: {
					t = consumeToken(BACKTICK);
					Text jjtn002 = new Text();
					boolean jjtc002 = true;
					tree.openNodeScope(jjtn002);
					try {
						tree.closeNodeScope(jjtn002);
						jjtc002 = false;
						jjtn002.jjtSetValue(t.image);
					} finally {
						if (jjtc002) {
							tree.closeNodeScope(jjtn002);
						}
					}
					break;
				}
				case LBRACK: {
					t = consumeToken(LBRACK);
					Text jjtn003 = new Text();
					boolean jjtc003 = true;
					tree.openNodeScope(jjtn003);
					try {
						tree.closeNodeScope(jjtn003);
						jjtc003 = false;
						jjtn003.jjtSetValue(t.image);
					} finally {
						if (jjtc003) {
							tree.closeNodeScope(jjtn003);
						}
					}
					break;
				}
				default:
					jj_la1[33] = jj_gen;
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
			if (jj_2_64(1)) {
				;
			} else {
				break label_42;
			}
		}
	}

	private void em() {
		Em em = new Em();
		tree.openNodeScope(em);
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
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case ASTERISK: {
						t = consumeToken(ASTERISK);
						Text jjtn001 = new Text();
						boolean jjtc001 = true;
						tree.openNodeScope(jjtn001);
						try {
							tree.closeNodeScope(jjtn001);
							jjtc001 = false;
							jjtn001.jjtSetValue(t.image);
						} finally {
							if (jjtc001) {
								tree.closeNodeScope(jjtn001);
							}
						}
						break;
					}
					case BACKTICK: {
						t = consumeToken(BACKTICK);
						Text jjtn002 = new Text();
						boolean jjtc002 = true;
						tree.openNodeScope(jjtn002);
						try {
							tree.closeNodeScope(jjtn002);
							jjtc002 = false;
							jjtn002.jjtSetValue(t.image);
						} finally {
							if (jjtc002) {
								tree.closeNodeScope(jjtn002);
							}
						}
						break;
					}
					case LBRACK: {
						t = consumeToken(LBRACK);
						Text jjtn003 = new Text();
						boolean jjtc003 = true;
						tree.openNodeScope(jjtn003);
						try {
							tree.closeNodeScope(jjtn003);
							jjtc003 = false;
							jjtn003.jjtSetValue(t.image);
						} finally {
							if (jjtc003) {
								tree.closeNodeScope(jjtn003);
							}
						}
						break;
					}
					default:
						jj_la1[34] = jj_gen;
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
			tree.closeNodeScope(em);
		}
	}

	private void emWithinStrong() {
		Em em = new Em();
		tree.openNodeScope(em);
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
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case ASTERISK: {
						t = consumeToken(ASTERISK);
						Text jjtn001 = new Text();
						boolean jjtc001 = true;
						tree.openNodeScope(jjtn001);
						try {
							tree.closeNodeScope(jjtn001);
							jjtc001 = false;
							jjtn001.jjtSetValue(t.image);
						} finally {
							if (jjtc001) {
								tree.closeNodeScope(jjtn001);
							}
						}
						break;
					}
					case BACKTICK: {
						t = consumeToken(BACKTICK);
						Text jjtn002 = new Text();
						boolean jjtc002 = true;
						tree.openNodeScope(jjtn002);
						try {
							tree.closeNodeScope(jjtn002);
							jjtc002 = false;
							jjtn002.jjtSetValue(t.image);
						} finally {
							if (jjtc002) {
								tree.closeNodeScope(jjtn002);
							}
						}
						break;
					}
					case LBRACK: {
						t = consumeToken(LBRACK);
						Text jjtn003 = new Text();
						boolean jjtc003 = true;
						tree.openNodeScope(jjtn003);
						try {
							tree.closeNodeScope(jjtn003);
							jjtc003 = false;
							jjtn003.jjtSetValue(t.image);
						} finally {
							if (jjtc003) {
								tree.closeNodeScope(jjtn003);
							}
						}
						break;
					}
					default:
						jj_la1[35] = jj_gen;
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (jj_2_75(1)) {
					;
				} else {
					break label_44;
				}
			}
			consumeToken(UNDERSCORE);
		} finally {
			tree.closeNodeScope(em);
		}
	}

	private void codeMultiline() {
		Code code = new Code();
		tree.openNodeScope(code);
		try {
			consumeToken(BACKTICK);
			codeText();
			label_45: while (true) {
				if (textAhead()) {
					;
				} else {
					break label_45;
				}
				lineBreak();
				whiteSpace();
				label_46: while (true) {
					switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
					case GT: {
						;
						break;
					}
					default:
						jj_la1[36] = jj_gen;
						break label_46;
					}
					consumeToken(GT);
					whiteSpace();
				}
				codeText();
			}
			consumeToken(BACKTICK);
		} finally {
			tree.closeNodeScope(code);
		}
	}

	private void code() {
		Code code = new Code();
		tree.openNodeScope(code);
		try {
			consumeToken(BACKTICK);
			codeText();
			consumeToken(BACKTICK);
		} finally {
			tree.closeNodeScope(code);
		}
	}

	private void codeText() {
		Text text = new Text();
		tree.openNodeScope(text);
		Token t;
		StringBuffer s = new StringBuffer();
		try {
			label_47: while (true) {
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
					jj_la1[38] = jj_gen;
					if (!nextAfterSpace(EOL, EOF)) {
						switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
							jj_la1[37] = jj_gen;
							consumeToken(-1);
							throw new RuntimeException();
						}
					} else {
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (jj_2_76(1)) {
					;
				} else {
					break label_47;
				}
			}
			text.jjtSetValue(s.toString());
		} finally {
			tree.closeNodeScope(text);
		}
	}

	private void text() {
		Text text = new Text();
		tree.openNodeScope(text);
		Token t;
		StringBuffer s = new StringBuffer();
		try {
			label_48: while (true) {
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
					jj_la1[40] = jj_gen;
					if (!nextAfterSpace(EOL, EOF)) {
						switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
							jj_la1[39] = jj_gen;
							consumeToken(-1);
							throw new RuntimeException();
						}
					} else {
						consumeToken(-1);
						throw new RuntimeException();
					}
				}
				if (jj_2_77(1)) {
					;
				} else {
					break label_48;
				}
			}
			text.jjtSetValue(s.toString());
		} finally {
			tree.closeNodeScope(text);
		}
	}

	private void looseChar() {
		Text text = new Text();
		tree.openNodeScope(text);
		Token t;
		try {
			switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
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
				jj_la1[41] = jj_gen;
				consumeToken(-1);
				throw new RuntimeException();
			}
			text.jjtSetValue(t.image);
		} finally {
			tree.closeNodeScope(text);
		}
	}

	private void lineBreak() {
		LineBreak linebreak = new LineBreak();
		tree.openNodeScope(linebreak);
		try {
			label_49: while (true) {
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
				case SPACE:
				case TAB: {
					;
					break;
				}
				default:
					jj_la1[42] = jj_gen;
					break label_49;
				}
				switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
				case SPACE: {
					consumeToken(SPACE);
					break;
				}
				case TAB: {
					consumeToken(TAB);
					break;
				}
				default:
					jj_la1[43] = jj_gen;
					consumeToken(-1);
					throw new RuntimeException();
				}
			}
			consumeToken(EOL);
		} finally {
			tree.closeNodeScope(linebreak);
		}
	}

	private void whiteSpace() {
		label_50: while (true) {
			switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
			case SPACE:
			case TAB: {
				;
				break;
			}
			default:
				jj_la1[44] = jj_gen;
				break label_50;
			}
			switch ((nextTokenKind == -1) ? getNextTokenKind() : nextTokenKind) {
			case SPACE: {
				consumeToken(SPACE);
				break;
			}
			case TAB: {
				consumeToken(TAB);
				break;
			}
			default:
				jj_la1[45] = jj_gen;
				consumeToken(-1);
				throw new RuntimeException();
			}
		}
	}

	private boolean jj_2_1(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_1();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(0, xla);
		}
	}

	private boolean jj_2_2(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_2();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(1, xla);
		}
	}

	private boolean jj_2_3(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_3();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(2, xla);
		}
	}

	private boolean jj_2_4(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_4();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(3, xla);
		}
	}

	private boolean jj_2_5(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_5();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(4, xla);
		}
	}

	private boolean jj_2_6(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_6();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(5, xla);
		}
	}

	private boolean jj_2_7(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_7();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(6, xla);
		}
	}

	private boolean jj_2_8(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_8();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(7, xla);
		}
	}

	private boolean jj_2_9(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_9();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(8, xla);
		}
	}

	private boolean jj_2_10(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_10();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(9, xla);
		}
	}

	private boolean jj_2_11(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_11();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(10, xla);
		}
	}

	private boolean jj_2_12(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_12();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(11, xla);
		}
	}

	private boolean jj_2_13(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_13();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(12, xla);
		}
	}

	private boolean jj_2_14(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_14();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(13, xla);
		}
	}

	private boolean jj_2_15(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_15();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(14, xla);
		}
	}

	private boolean jj_2_16(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_16();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(15, xla);
		}
	}

	private boolean jj_2_17(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_17();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(16, xla);
		}
	}

	private boolean jj_2_18(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_18();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(17, xla);
		}
	}

	private boolean jj_2_19(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_19();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(18, xla);
		}
	}

	private boolean jj_2_20(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_20();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(19, xla);
		}
	}

	private boolean jj_2_21(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_21();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(20, xla);
		}
	}

	private boolean jj_2_22(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_22();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(21, xla);
		}
	}

	private boolean jj_2_23(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_23();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(22, xla);
		}
	}

	private boolean jj_2_24(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_24();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(23, xla);
		}
	}

	private boolean jj_2_25(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_25();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(24, xla);
		}
	}

	private boolean jj_2_26(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_26();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(25, xla);
		}
	}

	private boolean jj_2_27(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_27();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(26, xla);
		}
	}

	private boolean jj_2_28(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_28();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(27, xla);
		}
	}

	private boolean jj_2_29(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_29();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(28, xla);
		}
	}

	private boolean jj_2_30(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_30();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(29, xla);
		}
	}

	private boolean jj_2_31(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_31();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(30, xla);
		}
	}

	private boolean jj_2_32(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_32();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(31, xla);
		}
	}

	private boolean jj_2_33(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_33();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(32, xla);
		}
	}

	private boolean jj_2_34(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_34();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(33, xla);
		}
	}

	private boolean jj_2_35(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_35();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(34, xla);
		}
	}

	private boolean jj_2_36(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_36();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(35, xla);
		}
	}

	private boolean jj_2_37(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_37();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(36, xla);
		}
	}

	private boolean jj_2_38(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_38();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(37, xla);
		}
	}

	private boolean jj_2_39(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_39();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(38, xla);
		}
	}

	private boolean jj_2_40(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_40();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(39, xla);
		}
	}

	private boolean jj_2_41(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_41();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(40, xla);
		}
	}

	private boolean jj_2_42(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_42();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(41, xla);
		}
	}

	private boolean jj_2_43(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_43();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(42, xla);
		}
	}

	private boolean jj_2_44(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_44();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(43, xla);
		}
	}

	private boolean jj_2_45(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_45();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(44, xla);
		}
	}

	private boolean jj_2_46(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_46();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(45, xla);
		}
	}

	private boolean jj_2_47(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_47();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(46, xla);
		}
	}

	private boolean jj_2_48(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_48();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(47, xla);
		}
	}

	private boolean jj_2_49(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_49();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(48, xla);
		}
	}

	private boolean jj_2_50(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_50();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(49, xla);
		}
	}

	private boolean jj_2_51(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_51();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(50, xla);
		}
	}

	private boolean jj_2_52(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_52();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(51, xla);
		}
	}

	private boolean jj_2_53(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_53();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(52, xla);
		}
	}

	private boolean jj_2_54(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_54();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(53, xla);
		}
	}

	private boolean jj_2_55(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_55();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(54, xla);
		}
	}

	private boolean jj_2_56(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_56();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(55, xla);
		}
	}

	private boolean jj_2_57(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_57();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(56, xla);
		}
	}

	private boolean jj_2_58(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_58();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(57, xla);
		}
	}

	private boolean jj_2_59(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_59();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(58, xla);
		}
	}

	private boolean jj_2_60(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_60();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(59, xla);
		}
	}

	private boolean jj_2_61(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_61();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(60, xla);
		}
	}

	private boolean jj_2_62(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_62();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(61, xla);
		}
	}

	private boolean jj_2_63(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_63();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(62, xla);
		}
	}

	private boolean jj_2_64(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_64();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(63, xla);
		}
	}

	private boolean jj_2_65(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_65();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(64, xla);
		}
	}

	private boolean jj_2_66(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_66();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(65, xla);
		}
	}

	private boolean jj_2_67(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_67();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(66, xla);
		}
	}

	private boolean jj_2_68(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_68();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(67, xla);
		}
	}

	private boolean jj_2_69(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_69();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(68, xla);
		}
	}

	private boolean jj_2_70(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_70();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(69, xla);
		}
	}

	private boolean jj_2_71(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_71();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(70, xla);
		}
	}

	private boolean jj_2_72(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_72();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(71, xla);
		}
	}

	private boolean jj_2_73(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_73();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(72, xla);
		}
	}

	private boolean jj_2_74(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_74();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(73, xla);
		}
	}

	private boolean jj_2_75(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_75();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(74, xla);
		}
	}

	private boolean jj_2_76(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_76();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(75, xla);
		}
	}

	private boolean jj_2_77(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_77();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(76, xla);
		}
	}

	private boolean jj_3R_254() {
		if (scanToken(SPACE))
			return true;
		return false;
	}

	private boolean jj_3R_273() {
		if (scanToken(GT))
			return true;
		if (jj_3R_228())
			return true;
		return false;
	}

	private boolean jj_3R_255() {
		if (scanToken(TAB))
			return true;
		return false;
	}

	private boolean jj_3R_259() {
		Token xsp;
		xsp = jj_scanpos;
		if (scanToken(19)) {
			jj_scanpos = xsp;
			if (scanToken(20))
				return true;
		}
		return false;
	}

	private boolean jj_3R_228() {
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_259()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_267() {
		Token xsp;
		xsp = jj_scanpos;
		if (scanToken(19)) {
			jj_scanpos = xsp;
			if (scanToken(20))
				return true;
		}
		return false;
	}

	private boolean jj_3R_262() {
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_267()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (scanToken(EOL))
			return true;
		return false;
	}

	private boolean jj_3R_233() {
		Token xsp;
		xsp = jj_scanpos;
		if (scanToken(1)) {
			jj_scanpos = xsp;
			if (scanToken(3)) {
				jj_scanpos = xsp;
				if (scanToken(14)) {
					jj_scanpos = xsp;
					if (scanToken(21))
						return true;
				}
			}
		}
		return false;
	}

	private boolean jj_3R_222() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_254()) {
			jj_scanpos = xsp;
			if (jj_3R_255())
				return true;
		}
		return false;
	}

	private boolean jj_3R_221() {
		if (scanToken(RPAREN))
			return true;
		return false;
	}

	private boolean jj_3R_220() {
		if (scanToken(RBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_219() {
		if (scanToken(LT))
			return true;
		return false;
	}

	private boolean jj_3R_218() {
		if (scanToken(LPAREN))
			return true;
		return false;
	}

	private boolean jj_3R_217() {
		if (scanToken(IMAGE_LABEL))
			return true;
		return false;
	}

	private boolean jj_3R_208() {
		if (scanToken(BACKSLASH))
			return true;
		return false;
	}

	private boolean jj_3R_216() {
		if (scanToken(GT))
			return true;
		return false;
	}

	private boolean jj_3R_215() {
		if (scanToken(ESCAPED_CHAR))
			return true;
		return false;
	}

	private boolean jj_3R_252() {
		if (scanToken(SPACE))
			return true;
		return false;
	}

	private boolean jj_3R_214() {
		if (scanToken(EQ))
			return true;
		return false;
	}

	private boolean jj_3R_213() {
		if (scanToken(DOT))
			return true;
		return false;
	}

	private boolean jj_3R_212() {
		if (scanToken(DIGITS))
			return true;
		return false;
	}

	private boolean jj_3R_253() {
		if (scanToken(TAB))
			return true;
		return false;
	}

	private boolean jj_3R_211() {
		if (scanToken(DASH))
			return true;
		return false;
	}

	private boolean jj_3R_210() {
		if (scanToken(COLON))
			return true;
		return false;
	}

	private boolean jj_3R_209() {
		if (scanToken(CHAR_SEQUENCE))
			return true;
		return false;
	}

	private boolean jj_3_77() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_208()) {
			jj_scanpos = xsp;
			if (jj_3R_209()) {
				jj_scanpos = xsp;
				if (jj_3R_210()) {
					jj_scanpos = xsp;
					if (jj_3R_211()) {
						jj_scanpos = xsp;
						if (jj_3R_212()) {
							jj_scanpos = xsp;
							if (jj_3R_213()) {
								jj_scanpos = xsp;
								if (jj_3R_214()) {
									jj_scanpos = xsp;
									if (jj_3R_215()) {
										jj_scanpos = xsp;
										if (jj_3R_216()) {
											jj_scanpos = xsp;
											if (jj_3R_217()) {
												jj_scanpos = xsp;
												if (jj_3R_218()) {
													jj_scanpos = xsp;
													if (jj_3R_219()) {
														jj_scanpos = xsp;
														if (jj_3R_220()) {
															jj_scanpos = xsp;
															if (jj_3R_221()) {
																jj_scanpos = xsp;
																jj_lookingAhead = true;
																jj_semLA = !nextAfterSpace(EOL, EOF);
																jj_lookingAhead = false;
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
		if (jj_3_77())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_77()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_207() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_252()) {
			jj_scanpos = xsp;
			if (jj_3R_253())
				return true;
		}
		return false;
	}

	private boolean jj_3R_206() {
		if (scanToken(UNDERSCORE))
			return true;
		return false;
	}

	private boolean jj_3R_201() {
		if (scanToken(LBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_205() {
		if (scanToken(RPAREN))
			return true;
		return false;
	}

	private boolean jj_3R_272() {
		if (jj_3R_262())
			return true;
		if (jj_3R_228())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_273()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (jj_3R_236())
			return true;
		return false;
	}

	private boolean jj_3R_204() {
		if (scanToken(GT))
			return true;
		return false;
	}

	private boolean jj_3R_203() {
		if (scanToken(LPAREN))
			return true;
		return false;
	}

	private boolean jj_3R_202() {
		if (scanToken(RBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_200() {
		if (scanToken(LT))
			return true;
		return false;
	}

	private boolean jj_3R_199() {
		if (scanToken(IMAGE_LABEL))
			return true;
		return false;
	}

	private boolean jj_3R_198() {
		if (scanToken(ESCAPED_CHAR))
			return true;
		return false;
	}

	private boolean jj_3R_197() {
		if (scanToken(EQ))
			return true;
		return false;
	}

	private boolean jj_3R_196() {
		if (scanToken(DOT))
			return true;
		return false;
	}

	private boolean jj_3R_191() {
		if (scanToken(BACKSLASH))
			return true;
		return false;
	}

	private boolean jj_3R_195() {
		if (scanToken(DIGITS))
			return true;
		return false;
	}

	private boolean jj_3_76() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_190()) {
			jj_scanpos = xsp;
			if (jj_3R_191()) {
				jj_scanpos = xsp;
				if (jj_3R_192()) {
					jj_scanpos = xsp;
					if (jj_3R_193()) {
						jj_scanpos = xsp;
						if (jj_3R_194()) {
							jj_scanpos = xsp;
							if (jj_3R_195()) {
								jj_scanpos = xsp;
								if (jj_3R_196()) {
									jj_scanpos = xsp;
									if (jj_3R_197()) {
										jj_scanpos = xsp;
										if (jj_3R_198()) {
											jj_scanpos = xsp;
											if (jj_3R_199()) {
												jj_scanpos = xsp;
												if (jj_3R_200()) {
													jj_scanpos = xsp;
													if (jj_3R_201()) {
														jj_scanpos = xsp;
														if (jj_3R_202()) {
															jj_scanpos = xsp;
															if (jj_3R_203()) {
																jj_scanpos = xsp;
																if (jj_3R_204()) {
																	jj_scanpos = xsp;
																	if (jj_3R_205()) {
																		jj_scanpos = xsp;
																		if (jj_3R_206()) {
																			jj_scanpos = xsp;
																			jj_lookingAhead = true;
																			jj_semLA = !nextAfterSpace(EOL, EOF);
																			jj_lookingAhead = false;
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

	private boolean jj_3R_190() {
		if (scanToken(ASTERISK))
			return true;
		return false;
	}

	private boolean jj_3R_194() {
		if (scanToken(DASH))
			return true;
		return false;
	}

	private boolean jj_3R_193() {
		if (scanToken(COLON))
			return true;
		return false;
	}

	private boolean jj_3R_192() {
		if (scanToken(CHAR_SEQUENCE))
			return true;
		return false;
	}

	private boolean jj_3_31() {
		if (jj_3R_96())
			return true;
		return false;
	}

	private boolean jj_3R_236() {
		Token xsp;
		if (jj_3_76())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_76()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_66() {
		if (scanToken(BACKTICK))
			return true;
		if (jj_3R_236())
			return true;
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3R_242() {
		if (scanToken(BACKTICK))
			return true;
		if (jj_3R_236())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_272()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3_74() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3_73() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_235() {
		if (jj_3R_96())
			return true;
		return false;
	}

	private boolean jj_3R_249() {
		if (jj_3R_262())
			return true;
		if (jj_3R_248())
			return true;
		return false;
	}

	private boolean jj_3_72() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3R_189() {
		if (scanToken(LBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_188() {
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3R_187() {
		if (scanToken(ASTERISK))
			return true;
		return false;
	}

	private boolean jj_3R_186() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3R_185() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_184() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_75() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3_71()) {
			jj_scanpos = xsp;
			if (jj_3R_184()) {
				jj_scanpos = xsp;
				if (jj_3R_185()) {
					jj_scanpos = xsp;
					if (jj_3R_186()) {
						jj_scanpos = xsp;
						if (jj_3R_187()) {
							jj_scanpos = xsp;
							if (jj_3R_188()) {
								jj_scanpos = xsp;
								if (jj_3R_189())
									return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3_71() {
		if (jj_3R_61())
			return true;
		return false;
	}

	private boolean jj_3_69() {
		if (jj_3R_176())
			return true;
		return false;
	}

	private boolean jj_3_68() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3_67() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3_66() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3R_148() {
		if (scanToken(UNDERSCORE))
			return true;
		Token xsp;
		if (jj_3_75())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_75()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (scanToken(UNDERSCORE))
			return true;
		return false;
	}

	private boolean jj_3R_183() {
		if (scanToken(LBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_182() {
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3R_181() {
		if (scanToken(ASTERISK))
			return true;
		return false;
	}

	private boolean jj_3R_180() {
		if (jj_3R_176())
			return true;
		return false;
	}

	private boolean jj_3R_179() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3R_178() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_177() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_63() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3_65() {
		if (jj_3R_61())
			return true;
		return false;
	}

	private boolean jj_3_70() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3_65()) {
			jj_scanpos = xsp;
			if (jj_3R_177()) {
				jj_scanpos = xsp;
				if (jj_3R_178()) {
					jj_scanpos = xsp;
					if (jj_3R_179()) {
						jj_scanpos = xsp;
						if (jj_3R_180()) {
							jj_scanpos = xsp;
							if (jj_3R_181()) {
								jj_scanpos = xsp;
								if (jj_3R_182()) {
									jj_scanpos = xsp;
									if (jj_3R_183())
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

	private boolean jj_3_62() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3_61() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3R_65() {
		if (scanToken(UNDERSCORE))
			return true;
		Token xsp;
		if (jj_3_70())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_70()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (scanToken(UNDERSCORE))
			return true;
		return false;
	}

	private boolean jj_3R_175() {
		if (scanToken(LBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_174() {
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3R_173() {
		if (scanToken(ASTERISK))
			return true;
		return false;
	}

	private boolean jj_3R_172() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3R_101() {
		if (jj_3R_233())
			return true;
		return false;
	}

	private boolean jj_3R_171() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_170() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_60() {
		if (jj_3R_61())
			return true;
		return false;
	}

	private boolean jj_3_64() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3_60()) {
			jj_scanpos = xsp;
			if (jj_3R_170()) {
				jj_scanpos = xsp;
				if (jj_3R_171()) {
					jj_scanpos = xsp;
					if (jj_3R_172()) {
						jj_scanpos = xsp;
						if (jj_3R_173()) {
							jj_scanpos = xsp;
							if (jj_3R_174()) {
								jj_scanpos = xsp;
								if (jj_3R_175())
									return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3_58() {
		if (jj_3R_162())
			return true;
		return false;
	}

	private boolean jj_3_57() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_248() {
		Token xsp;
		if (jj_3_64())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_64()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3_56() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3R_134() {
		if (scanToken(UNDERSCORE))
			return true;
		if (jj_3R_248())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_249()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (scanToken(UNDERSCORE))
			return true;
		return false;
	}

	private boolean jj_3R_169() {
		if (scanToken(LBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_168() {
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3R_167() {
		if (scanToken(ASTERISK))
			return true;
		return false;
	}

	private boolean jj_3R_166() {
		if (jj_3R_162())
			return true;
		return false;
	}

	private boolean jj_3R_165() {
		if (jj_3R_242())
			return true;
		return false;
	}

	private boolean jj_3_29() {
		if (jj_3R_94())
			return true;
		return false;
	}

	private boolean jj_3R_164() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_163() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_55() {
		if (jj_3R_61())
			return true;
		return false;
	}

	private boolean jj_3_59() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3_55()) {
			jj_scanpos = xsp;
			if (jj_3R_163()) {
				jj_scanpos = xsp;
				if (jj_3R_164()) {
					jj_scanpos = xsp;
					jj_lookingAhead = true;
					jj_semLA = multilineAhead(BACKTICK);
					jj_lookingAhead = false;
					if (!jj_semLA || jj_3R_165()) {
						jj_scanpos = xsp;
						if (jj_3R_166()) {
							jj_scanpos = xsp;
							if (jj_3R_167()) {
								jj_scanpos = xsp;
								if (jj_3R_168()) {
									jj_scanpos = xsp;
									if (jj_3R_169())
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

	private boolean jj_3_53() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3_52() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3_51() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3R_241() {
		if (scanToken(UNDERSCORE))
			return true;
		return false;
	}

	private boolean jj_3R_251() {
		if (jj_3R_262())
			return true;
		if (jj_3R_250())
			return true;
		return false;
	}

	private boolean jj_3R_161() {
		if (scanToken(UNDERSCORE))
			return true;
		return false;
	}

	private boolean jj_3R_160() {
		if (scanToken(LBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_159() {
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3R_158() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3_28() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3R_157() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_156() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_50() {
		if (jj_3R_61())
			return true;
		return false;
	}

	private boolean jj_3_54() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3_50()) {
			jj_scanpos = xsp;
			if (jj_3R_156()) {
				jj_scanpos = xsp;
				if (jj_3R_157()) {
					jj_scanpos = xsp;
					if (jj_3R_158()) {
						jj_scanpos = xsp;
						if (jj_3R_159()) {
							jj_scanpos = xsp;
							if (jj_3R_160()) {
								jj_scanpos = xsp;
								if (jj_3R_161())
									return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3_48() {
		if (jj_3R_148())
			return true;
		return false;
	}

	private boolean jj_3_47() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3_46() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_24() {
		if (jj_3R_96())
			return true;
		return false;
	}

	private boolean jj_3R_176() {
		if (scanToken(ASTERISK))
			return true;
		Token xsp;
		if (jj_3_54())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_54()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (scanToken(ASTERISK))
			return true;
		return false;
	}

	private boolean jj_3R_100() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3R_155() {
		if (scanToken(UNDERSCORE))
			return true;
		return false;
	}

	private boolean jj_3R_154() {
		if (scanToken(LBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_153() {
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3R_152() {
		if (jj_3R_148())
			return true;
		return false;
	}

	private boolean jj_3R_151() {
		if (jj_3R_242())
			return true;
		return false;
	}

	private boolean jj_3R_150() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_149() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_43() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3_45() {
		if (jj_3R_61())
			return true;
		return false;
	}

	private boolean jj_3_49() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3_45()) {
			jj_scanpos = xsp;
			if (jj_3R_149()) {
				jj_scanpos = xsp;
				if (jj_3R_150()) {
					jj_scanpos = xsp;
					jj_lookingAhead = true;
					jj_semLA = multilineAhead(BACKTICK);
					jj_lookingAhead = false;
					if (!jj_semLA || jj_3R_151()) {
						jj_scanpos = xsp;
						if (jj_3R_152()) {
							jj_scanpos = xsp;
							if (jj_3R_153()) {
								jj_scanpos = xsp;
								if (jj_3R_154()) {
									jj_scanpos = xsp;
									if (jj_3R_155())
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

	private boolean jj_3_42() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_234() {
		if (jj_3R_96())
			return true;
		return false;
	}

	private boolean jj_3_41() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3R_64() {
		if (scanToken(ASTERISK))
			return true;
		Token xsp;
		if (jj_3_49())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_49()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (scanToken(ASTERISK))
			return true;
		return false;
	}

	private boolean jj_3_27() {
		if (jj_3R_65())
			return true;
		return false;
	}

	private boolean jj_3R_147() {
		if (scanToken(UNDERSCORE))
			return true;
		return false;
	}

	private boolean jj_3R_146() {
		if (scanToken(LBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_145() {
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3R_144() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3R_143() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_142() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_40() {
		if (jj_3R_61())
			return true;
		return false;
	}

	private boolean jj_3_44() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3_40()) {
			jj_scanpos = xsp;
			if (jj_3R_142()) {
				jj_scanpos = xsp;
				if (jj_3R_143()) {
					jj_scanpos = xsp;
					if (jj_3R_144()) {
						jj_scanpos = xsp;
						if (jj_3R_145()) {
							jj_scanpos = xsp;
							if (jj_3R_146()) {
								jj_scanpos = xsp;
								if (jj_3R_147())
									return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_99() {
		if (jj_3R_65())
			return true;
		return false;
	}

	private boolean jj_3_38() {
		if (jj_3R_134())
			return true;
		return false;
	}

	private boolean jj_3_37() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3_36() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_250() {
		Token xsp;
		if (jj_3_44())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_44()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3_35() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3R_162() {
		if (scanToken(ASTERISK))
			return true;
		if (jj_3R_250())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_251()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (scanToken(ASTERISK))
			return true;
		return false;
	}

	private boolean jj_3R_141() {
		if (scanToken(UNDERSCORE))
			return true;
		return false;
	}

	private boolean jj_3R_140() {
		if (scanToken(LBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_139() {
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3R_246() {
		if (scanToken(SPACE))
			return true;
		return false;
	}

	private boolean jj_3R_138() {
		if (jj_3R_134())
			return true;
		return false;
	}

	private boolean jj_3R_137() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3R_136() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_247() {
		if (scanToken(TAB))
			return true;
		return false;
	}

	private boolean jj_3R_135() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_34() {
		if (jj_3R_61())
			return true;
		return false;
	}

	private boolean jj_3_39() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3_34()) {
			jj_scanpos = xsp;
			if (jj_3R_135()) {
				jj_scanpos = xsp;
				if (jj_3R_136()) {
					jj_scanpos = xsp;
					if (jj_3R_137()) {
						jj_scanpos = xsp;
						if (jj_3R_138()) {
							jj_scanpos = xsp;
							if (jj_3R_139()) {
								jj_scanpos = xsp;
								if (jj_3R_140()) {
									jj_scanpos = xsp;
									if (jj_3R_141())
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

	private boolean jj_3_26() {
		if (jj_3R_64())
			return true;
		return false;
	}

	private boolean jj_3R_95() {
		if (jj_3R_233())
			return true;
		return false;
	}

	private boolean jj_3R_240() {
		if (scanToken(ASTERISK))
			return true;
		return false;
	}

	private boolean jj_3R_130() {
		if (scanToken(LT))
			return true;
		return false;
	}

	private boolean jj_3R_133() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_246()) {
			jj_scanpos = xsp;
			if (jj_3R_247())
				return true;
		}
		return false;
	}

	private boolean jj_3R_132() {
		if (scanToken(UNDERSCORE))
			return true;
		return false;
	}

	private boolean jj_3R_131() {
		if (scanToken(RBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_129() {
		if (scanToken(LPAREN))
			return true;
		return false;
	}

	private boolean jj_3R_98() {
		if (jj_3R_64())
			return true;
		return false;
	}

	private boolean jj_3R_128() {
		if (scanToken(LBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_127() {
		if (scanToken(GT))
			return true;
		return false;
	}

	private boolean jj_3R_126() {
		if (scanToken(IMAGE_LABEL))
			return true;
		return false;
	}

	private boolean jj_3R_125() {
		if (scanToken(ESCAPED_CHAR))
			return true;
		return false;
	}

	private boolean jj_3R_116() {
		if (scanToken(ASTERISK))
			return true;
		return false;
	}

	private boolean jj_3_33() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_116()) {
			jj_scanpos = xsp;
			if (jj_3R_117()) {
				jj_scanpos = xsp;
				if (jj_3R_118()) {
					jj_scanpos = xsp;
					if (jj_3R_119()) {
						jj_scanpos = xsp;
						if (jj_3R_120()) {
							jj_scanpos = xsp;
							if (jj_3R_121()) {
								jj_scanpos = xsp;
								if (jj_3R_122()) {
									jj_scanpos = xsp;
									if (jj_3R_123()) {
										jj_scanpos = xsp;
										if (jj_3R_124()) {
											jj_scanpos = xsp;
											if (jj_3R_125()) {
												jj_scanpos = xsp;
												if (jj_3R_126()) {
													jj_scanpos = xsp;
													if (jj_3R_127()) {
														jj_scanpos = xsp;
														if (jj_3R_128()) {
															jj_scanpos = xsp;
															if (jj_3R_129()) {
																jj_scanpos = xsp;
																if (jj_3R_130()) {
																	jj_scanpos = xsp;
																	if (jj_3R_131()) {
																		jj_scanpos = xsp;
																		if (jj_3R_132()) {
																			jj_scanpos = xsp;
																			jj_lookingAhead = true;
																			jj_semLA = !nextAfterSpace(RPAREN);
																			jj_lookingAhead = false;
																			if (!jj_semLA || jj_3R_133())
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

	private boolean jj_3R_124() {
		if (scanToken(EQ))
			return true;
		return false;
	}

	private boolean jj_3R_123() {
		if (scanToken(DOT))
			return true;
		return false;
	}

	private boolean jj_3R_122() {
		if (scanToken(DIGITS))
			return true;
		return false;
	}

	private boolean jj_3R_121() {
		if (scanToken(DASH))
			return true;
		return false;
	}

	private boolean jj_3R_120() {
		if (scanToken(COLON))
			return true;
		return false;
	}

	private boolean jj_3_23() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3_22()) {
			jj_scanpos = xsp;
			if (jj_3R_95())
				return true;
		}
		return false;
	}

	private boolean jj_3_22() {
		if (jj_3R_94())
			return true;
		return false;
	}

	private boolean jj_3R_119() {
		if (scanToken(CHAR_SEQUENCE))
			return true;
		return false;
	}

	private boolean jj_3R_118() {
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3R_117() {
		if (scanToken(BACKSLASH))
			return true;
		return false;
	}

	private boolean jj_3R_243() {
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_33()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3_25() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3R_245() {
		if (scanToken(TAB))
			return true;
		return false;
	}

	private boolean jj_3R_96() {
		if (scanToken(LPAREN))
			return true;
		if (jj_3R_228())
			return true;
		if (jj_3R_243())
			return true;
		if (jj_3R_228())
			return true;
		if (scanToken(RPAREN))
			return true;
		return false;
	}

	private boolean jj_3R_244() {
		if (scanToken(SPACE))
			return true;
		return false;
	}

	private boolean jj_3R_115() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_244()) {
			jj_scanpos = xsp;
			if (jj_3R_245())
				return true;
		}
		return false;
	}

	private boolean jj_3R_111() {
		if (scanToken(GT))
			return true;
		return false;
	}

	private boolean jj_3R_97() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_30() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_97()) {
			jj_scanpos = xsp;
			if (jj_3R_98()) {
				jj_scanpos = xsp;
				if (jj_3R_99()) {
					jj_scanpos = xsp;
					if (jj_3R_100()) {
						jj_scanpos = xsp;
						if (jj_3_29()) {
							jj_scanpos = xsp;
							if (jj_3R_101())
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_114() {
		if (scanToken(RPAREN))
			return true;
		return false;
	}

	private boolean jj_3R_110() {
		if (scanToken(IMAGE_LABEL))
			return true;
		return false;
	}

	private boolean jj_3R_113() {
		if (scanToken(LT))
			return true;
		return false;
	}

	private boolean jj_3R_112() {
		if (scanToken(LPAREN))
			return true;
		return false;
	}

	private boolean jj_3R_109() {
		if (scanToken(ESCAPED_CHAR))
			return true;
		return false;
	}

	private boolean jj_3R_108() {
		if (scanToken(EQ))
			return true;
		return false;
	}

	private boolean jj_3R_107() {
		if (scanToken(DOT))
			return true;
		return false;
	}

	private boolean jj_3R_106() {
		if (scanToken(DIGITS))
			return true;
		return false;
	}

	private boolean jj_3R_105() {
		if (scanToken(DASH))
			return true;
		return false;
	}

	private boolean jj_3R_104() {
		if (scanToken(CHAR_SEQUENCE))
			return true;
		return false;
	}

	private boolean jj_3R_103() {
		if (scanToken(COLON))
			return true;
		return false;
	}

	private boolean jj_3R_102() {
		if (scanToken(BACKSLASH))
			return true;
		return false;
	}

	private boolean jj_3_32() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_102()) {
			jj_scanpos = xsp;
			if (jj_3R_103()) {
				jj_scanpos = xsp;
				if (jj_3R_104()) {
					jj_scanpos = xsp;
					if (jj_3R_105()) {
						jj_scanpos = xsp;
						if (jj_3R_106()) {
							jj_scanpos = xsp;
							if (jj_3R_107()) {
								jj_scanpos = xsp;
								if (jj_3R_108()) {
									jj_scanpos = xsp;
									if (jj_3R_109()) {
										jj_scanpos = xsp;
										if (jj_3R_110()) {
											jj_scanpos = xsp;
											if (jj_3R_111()) {
												jj_scanpos = xsp;
												if (jj_3R_112()) {
													jj_scanpos = xsp;
													if (jj_3R_113()) {
														jj_scanpos = xsp;
														if (jj_3R_114()) {
															jj_scanpos = xsp;
															jj_lookingAhead = true;
															jj_semLA = !nextAfterSpace(RBRACK);
															jj_lookingAhead = false;
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
		if (jj_3_32())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_32()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_63() {
		if (scanToken(LBRACK))
			return true;
		if (jj_3R_228())
			return true;
		Token xsp;
		if (jj_3_30())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_30()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (jj_3R_228())
			return true;
		if (scanToken(RBRACK))
			return true;
		xsp = jj_scanpos;
		if (jj_3R_235())
			jj_scanpos = xsp;
		return false;
	}

	private boolean jj_3_20() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3_19() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3R_62() {
		if (scanToken(LBRACK))
			return true;
		if (jj_3R_228())
			return true;
		if (scanToken(IMAGE_LABEL))
			return true;
		if (jj_3R_228())
			return true;
		Token xsp;
		if (jj_3_23())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_23()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (jj_3R_228())
			return true;
		if (scanToken(RBRACK))
			return true;
		xsp = jj_scanpos;
		if (jj_3R_234())
			jj_scanpos = xsp;
		return false;
	}

	private boolean jj_3R_93() {
		if (jj_3R_233())
			return true;
		return false;
	}

	private boolean jj_3R_92() {
		if (jj_3R_242())
			return true;
		return false;
	}

	private boolean jj_3R_91() {
		if (jj_3R_241())
			return true;
		return false;
	}

	private boolean jj_3R_90() {
		if (jj_3R_240())
			return true;
		return false;
	}

	private boolean jj_3R_89() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_88() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_21() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3_18()) {
			jj_scanpos = xsp;
			if (jj_3R_88()) {
				jj_scanpos = xsp;
				if (jj_3R_89()) {
					jj_scanpos = xsp;
					jj_lookingAhead = true;
					jj_semLA = multilineAhead(ASTERISK);
					jj_lookingAhead = false;
					if (!jj_semLA || jj_3R_90()) {
						jj_scanpos = xsp;
						jj_lookingAhead = true;
						jj_semLA = multilineAhead(UNDERSCORE);
						jj_lookingAhead = false;
						if (!jj_semLA || jj_3R_91()) {
							jj_scanpos = xsp;
							jj_lookingAhead = true;
							jj_semLA = multilineAhead(BACKTICK);
							jj_lookingAhead = false;
							if (!jj_semLA || jj_3R_92()) {
								jj_scanpos = xsp;
								if (jj_3R_93())
									return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3_18() {
		if (jj_3R_61())
			return true;
		return false;
	}

	private boolean jj_3R_232() {
		Token xsp;
		if (jj_3_21())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_21()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_54() {
		if (jj_3R_232())
			return true;
		return false;
	}

	private boolean jj_3R_269() {
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3R_268() {
		if (scanToken(CHAR_SEQUENCE))
			return true;
		return false;
	}

	private boolean jj_3R_238() {
		if (scanToken(SPACE))
			return true;
		return false;
	}

	private boolean jj_3R_265() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_268()) {
			jj_scanpos = xsp;
			if (jj_3R_269())
				return true;
		}
		return false;
	}

	private boolean jj_3R_239() {
		if (scanToken(TAB))
			return true;
		return false;
	}

	private boolean jj_3R_260() {
		Token xsp;
		if (jj_3R_265())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_265()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_271() {
		if (scanToken(TAB))
			return true;
		return false;
	}

	private boolean jj_3R_270() {
		if (scanToken(SPACE))
			return true;
		return false;
	}

	private boolean jj_3R_87() {
		if (scanToken(EOL))
			return true;
		if (jj_3R_261())
			return true;
		return false;
	}

	private boolean jj_3R_266() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_270()) {
			jj_scanpos = xsp;
			if (jj_3R_271())
				return true;
		}
		return false;
	}

	private boolean jj_3R_261() {
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_266()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_86() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_238()) {
			jj_scanpos = xsp;
			if (jj_3R_239())
				return true;
		}
		return false;
	}

	private boolean jj_3R_85() {
		if (scanToken(BACKTICK))
			return true;
		return false;
	}

	private boolean jj_3_16() {
		if (jj_3R_51())
			return true;
		return false;
	}

	private boolean jj_3R_84() {
		if (scanToken(UNDERSCORE))
			return true;
		return false;
	}

	private boolean jj_3R_231() {
		if (scanToken(EOL))
			return true;
		if (jj_3R_228())
			return true;
		if (scanToken(BACKTICK))
			return true;
		if (scanToken(BACKTICK))
			return true;
		Token xsp;
		if (scanToken(3))
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (scanToken(3)) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_83() {
		if (scanToken(RPAREN))
			return true;
		return false;
	}

	private boolean jj_3R_82() {
		if (scanToken(LPAREN))
			return true;
		return false;
	}

	private boolean jj_3R_81() {
		if (scanToken(RBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_80() {
		if (scanToken(LBRACK))
			return true;
		return false;
	}

	private boolean jj_3R_79() {
		if (scanToken(GT))
			return true;
		return false;
	}

	private boolean jj_3R_78() {
		if (scanToken(LT))
			return true;
		return false;
	}

	private boolean jj_3R_77() {
		if (scanToken(IMAGE_LABEL))
			return true;
		return false;
	}

	private boolean jj_3R_76() {
		if (scanToken(ESCAPED_CHAR))
			return true;
		return false;
	}

	private boolean jj_3R_75() {
		if (scanToken(EQ))
			return true;
		return false;
	}

	private boolean jj_3R_74() {
		if (scanToken(DOT))
			return true;
		return false;
	}

	private boolean jj_3R_73() {
		if (scanToken(DIGITS))
			return true;
		return false;
	}

	private boolean jj_3R_72() {
		if (scanToken(DASH))
			return true;
		return false;
	}

	private boolean jj_3R_71() {
		if (scanToken(COLON))
			return true;
		return false;
	}

	private boolean jj_3R_70() {
		if (scanToken(CHAR_SEQUENCE))
			return true;
		return false;
	}

	private boolean jj_3R_69() {
		if (scanToken(BACKSLASH))
			return true;
		return false;
	}

	private boolean jj_3R_68() {
		if (scanToken(ASTERISK))
			return true;
		return false;
	}

	private boolean jj_3_17() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_68()) {
			jj_scanpos = xsp;
			if (jj_3R_69()) {
				jj_scanpos = xsp;
				if (jj_3R_70()) {
					jj_scanpos = xsp;
					if (jj_3R_71()) {
						jj_scanpos = xsp;
						if (jj_3R_72()) {
							jj_scanpos = xsp;
							if (jj_3R_73()) {
								jj_scanpos = xsp;
								if (jj_3R_74()) {
									jj_scanpos = xsp;
									if (jj_3R_75()) {
										jj_scanpos = xsp;
										if (jj_3R_76()) {
											jj_scanpos = xsp;
											if (jj_3R_77()) {
												jj_scanpos = xsp;
												if (jj_3R_78()) {
													jj_scanpos = xsp;
													if (jj_3R_79()) {
														jj_scanpos = xsp;
														if (jj_3R_80()) {
															jj_scanpos = xsp;
															if (jj_3R_81()) {
																jj_scanpos = xsp;
																if (jj_3R_82()) {
																	jj_scanpos = xsp;
																	if (jj_3R_83()) {
																		jj_scanpos = xsp;
																		if (jj_3R_84()) {
																			jj_scanpos = xsp;
																			if (jj_3R_85()) {
																				jj_scanpos = xsp;
																				jj_lookingAhead = true;
																				jj_semLA = !nextAfterSpace(EOL, EOF);
																				jj_lookingAhead = false;
																				if (!jj_semLA || jj_3R_86()) {
																					jj_scanpos = xsp;
																					jj_lookingAhead = true;
																					jj_semLA = !fencesAhead();
																					jj_lookingAhead = false;
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

	private boolean jj_3_15() {
		if (jj_3R_51())
			return true;
		return false;
	}

	private boolean jj_3R_230() {
		if (scanToken(EOL))
			return true;
		if (jj_3R_261())
			return true;
		return false;
	}

	private boolean jj_3R_229() {
		if (jj_3R_260())
			return true;
		return false;
	}

	private boolean jj_3R_53() {
		if (scanToken(BACKTICK))
			return true;
		if (scanToken(BACKTICK))
			return true;
		Token xsp;
		if (scanToken(3))
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (scanToken(3)) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (jj_3R_228())
			return true;
		xsp = jj_scanpos;
		if (jj_3R_229())
			jj_scanpos = xsp;
		xsp = jj_scanpos;
		if (jj_3R_230())
			jj_scanpos = xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3_17()) {
				jj_scanpos = xsp;
				break;
			}
		}
		xsp = jj_scanpos;
		if (jj_3R_231())
			jj_scanpos = xsp;
		return false;
	}

	private boolean jj_3R_237() {
		if (scanToken(GT))
			return true;
		if (jj_3R_228())
			return true;
		return false;
	}

	private boolean jj_3R_227() {
		if (scanToken(DIGITS))
			return true;
		if (scanToken(DOT))
			return true;
		return false;
	}

	private boolean jj_3R_52() {
		if (jj_3R_227())
			return true;
		return false;
	}

	private boolean jj_3R_264() {
		if (scanToken(DASH))
			return true;
		return false;
	}

	private boolean jj_3R_258() {
		if (jj_3R_264())
			return true;
		return false;
	}

	private boolean jj_3_12() {
		if (jj_3R_67())
			return true;
		return false;
	}

	private boolean jj_3_14() {
		if (jj_3R_67())
			return true;
		if (scanToken(EOL))
			return true;
		return false;
	}

	private boolean jj_3R_67() {
		if (scanToken(EOL))
			return true;
		if (jj_3R_228())
			return true;
		Token xsp;
		if (jj_3R_237())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_237()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3_1() {
		if (jj_3R_51())
			return true;
		return false;
	}

	private boolean jj_3_11() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3_10() {
		if (jj_3R_65())
			return true;
		return false;
	}

	private boolean jj_3_9() {
		if (jj_3R_64())
			return true;
		return false;
	}

	private boolean jj_3_8() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3_7() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_13() {
		if (jj_3R_51())
			return true;
		return false;
	}

	private boolean jj_3R_257() {
		if (scanToken(GT))
			return true;
		return false;
	}

	private boolean jj_3R_60() {
		if (jj_3R_233())
			return true;
		return false;
	}

	private boolean jj_3R_59() {
		if (jj_3R_66())
			return true;
		return false;
	}

	private boolean jj_3R_58() {
		if (jj_3R_65())
			return true;
		return false;
	}

	private boolean jj_3R_57() {
		if (jj_3R_64())
			return true;
		return false;
	}

	private boolean jj_3R_56() {
		if (jj_3R_63())
			return true;
		return false;
	}

	private boolean jj_3R_55() {
		if (jj_3R_62())
			return true;
		return false;
	}

	private boolean jj_3_3() {
		if (jj_3R_53())
			return true;
		return false;
	}

	private boolean jj_3_6() {
		if (jj_3R_61())
			return true;
		return false;
	}

	private boolean jj_3_5() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3_6()) {
			jj_scanpos = xsp;
			if (jj_3R_55()) {
				jj_scanpos = xsp;
				if (jj_3R_56()) {
					jj_scanpos = xsp;
					if (jj_3R_57()) {
						jj_scanpos = xsp;
						if (jj_3R_58()) {
							jj_scanpos = xsp;
							if (jj_3R_59()) {
								jj_scanpos = xsp;
								if (jj_3R_60())
									return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_263() {
		if (scanToken(EQ))
			return true;
		return false;
	}

	private boolean jj_3R_256() {
		Token xsp;
		if (jj_3R_263())
			return true;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_263()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3_4() {
		if (jj_3R_54())
			return true;
		return false;
	}

	private boolean jj_3R_226() {
		if (jj_3R_53())
			return true;
		return false;
	}

	private boolean jj_3_2() {
		if (jj_3R_52())
			return true;
		return false;
	}

	private boolean jj_3R_225() {
		if (jj_3R_258())
			return true;
		return false;
	}

	private boolean jj_3R_224() {
		if (jj_3R_257())
			return true;
		return false;
	}

	private boolean jj_3R_223() {
		if (jj_3R_256())
			return true;
		return false;
	}

	private boolean jj_3R_51() {
		Token xsp;
		xsp = jj_scanpos;
		jj_lookingAhead = true;
		jj_semLA = headingAhead(1);
		jj_lookingAhead = false;
		if (!jj_semLA || jj_3R_223()) {
			jj_scanpos = xsp;
			if (jj_3R_224()) {
				jj_scanpos = xsp;
				if (jj_3R_225()) {
					jj_scanpos = xsp;
					if (jj_3_2()) {
						jj_scanpos = xsp;
						if (jj_3R_226()) {
							jj_scanpos = xsp;
							if (jj_3_4())
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
		if ((oldToken = token).next != null)
			token = token.next;
		else
			token = token.next = tm.getNextToken();
		nextTokenKind = -1;
		if (token.kind == kind) {
			jj_gen++;
			if (++jj_gc > 100) {
				jj_gc = 0;
				for (int i = 0; i < jj_2_rtns.length; i++) {
					JJCalls c = jj_2_rtns[i];
					while (c != null) {
						if (c.gen < jj_gen)
							c.first = null;
						c = c.next;
					}
				}
			}
			return token;
		}
		token = oldToken;
		throw new RuntimeException();
	}

	private boolean scanToken(int kind) {
		if (jj_scanpos == jj_lastpos) {
			jj_la--;
			if (jj_scanpos.next == null) {
				jj_lastpos = jj_scanpos = jj_scanpos.next = tm.getNextToken();
			} else {
				jj_lastpos = jj_scanpos = jj_scanpos.next;
			}
		} else {
			jj_scanpos = jj_scanpos.next;
		}
		if (jj_rescan) {
			int i = 0;
			Token tok = token;
			while (tok != null && tok != jj_scanpos) {
				i++;
				tok = tok.next;
			}
			if (tok != null)
				jj_add_error_token(kind, i);
		}
		if (jj_scanpos.kind != kind)
			return true;
		if (jj_la == 0 && jj_scanpos == jj_lastpos)
			throw jj_ls;
		return false;
	}

	private Token getToken(int index) {
		Token t = jj_lookingAhead ? jj_scanpos : token;
		for (int i = 0; i < index; i++) {
			if (t.next != null)
				t = t.next;
			else
				t = t.next = tm.getNextToken();
		}
		return t;
	}

	private int getNextTokenKind() {
		if ((jj_nt = token.next) == null)
			return (nextTokenKind = (token.next = tm.getNextToken()).kind);
		else
			return (nextTokenKind = jj_nt.kind);
	}

	private void jj_add_error_token(int kind, int pos) {
		if (pos >= 100)
			return;
		if (pos == jj_endpos + 1) {
			jj_lasttokens[jj_endpos++] = kind;
		} else if (jj_endpos != 0) {
			jj_expentry = new int[jj_endpos];
			for (int i = 0; i < jj_endpos; i++) {
				jj_expentry[i] = jj_lasttokens[i];
			}
			jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
				int[] oldentry = (int[]) (it.next());
				if (oldentry.length == jj_expentry.length) {
					for (int i = 0; i < jj_expentry.length; i++) {
						if (oldentry[i] != jj_expentry[i]) {
							continue jj_entries_loop;
						}
					}
					jj_expentries.add(jj_expentry);
					break jj_entries_loop;
				}
			}
			if (pos != 0)
				jj_lasttokens[(jj_endpos = pos) - 1] = kind;
		}
	}

	private void jj_save(int index, int xla) {
		JJCalls p = jj_2_rtns[index];
		while (p.gen > jj_gen) {
			if (p.next == null) {
				p = p.next = new JJCalls();
				break;
			}
			p = p.next;
		}
		p.gen = jj_gen + xla - jj_la;
		p.first = token;
		p.arg = xla;
	}

	static final class JJCalls {
		int gen;
		Token first;
		int arg;
		JJCalls next;
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
