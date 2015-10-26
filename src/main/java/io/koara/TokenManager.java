package io.koara;

public class TokenManager {

	public static final int EOF = 0;
	public static final int ASTERISK = 1;
	public static final int BACKSLASH = 2;
	public static final int BACKTICK = 3;
	public static final int CHAR_SEQUENCE = 4;
	public static final int COLON = 5;
	public static final int DASH = 6;
	public static final int DIGITS = 7;
	public static final int DOT = 8;
	public static final int EOL = 9;
	public static final int EQ = 10;
	public static final int ESCAPED_CHAR = 11;
	public static final int GT = 12;
	public static final int IMAGE_LABEL = 13;
	public static final int LBRACK = 14;
	public static final int LPAREN = 15;
	public static final int LT = 16;
	public static final int RBRACK = 17;
	public static final int RPAREN = 18;
	public static final int SPACE = 19;
	public static final int TAB = 20;
	public static final int UNDERSCORE = 21;
	public static final int DEFAULT = 0;

	private final int jjStopStringLiteralDfa_0(int pos, long active0) {
		switch (pos) {
		case 0:
			if ((active0 & 0x2000L) != 0L) {
				jjmatchedKind = 4;
				return 0;
			}
			if ((active0 & 0x180000L) != 0L)
				return 8;
			if ((active0 & 0x4L) != 0L)
				return 7;
			return -1;
		case 1:
			if ((active0 & 0x2000L) != 0L) {
				jjmatchedKind = 4;
				jjmatchedPos = 1;
				return 0;
			}
			return -1;
		case 2:
			if ((active0 & 0x2000L) != 0L) {
				jjmatchedKind = 4;
				jjmatchedPos = 2;
				return 0;
			}
			return -1;
		case 3:
			if ((active0 & 0x2000L) != 0L) {
				jjmatchedKind = 4;
				jjmatchedPos = 3;
				return 0;
			}
			return -1;
		case 4:
			if ((active0 & 0x2000L) != 0L) {
				jjmatchedKind = 4;
				jjmatchedPos = 4;
				return 0;
			}
			return -1;
		default:
			return -1;
		}
	}

	private final int jjStartNfa_0(int pos, long active0) {
		return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
	}

	private int jjStopAtPos(int pos, int kind) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		return pos + 1;
	}

	private int jjMoveStringLiteralDfa0_0() {
		switch (curChar) {
		case 9:
			return jjStartNfaWithStates_0(0, 20, 8);
		case 32:
			return jjStartNfaWithStates_0(0, 19, 8);
		case 40:
			return jjStopAtPos(0, 15);
		case 41:
			return jjStopAtPos(0, 18);
		case 42:
			return jjStopAtPos(0, 1);
		case 45:
			return jjStopAtPos(0, 6);
		case 46:
			return jjStopAtPos(0, 8);
		case 58:
			return jjStopAtPos(0, 5);
		case 60:
			return jjStopAtPos(0, 16);
		case 61:
			return jjStopAtPos(0, 10);
		case 62:
			return jjStopAtPos(0, 12);
		case 91:
			return jjStopAtPos(0, 14);
		case 92:
			return jjStartNfaWithStates_0(0, 2, 7);
		case 93:
			return jjStopAtPos(0, 17);
		case 95:
			return jjStopAtPos(0, 21);
		case 96:
			return jjStopAtPos(0, 3);
		case 73:
		case 105:
			return jjMoveStringLiteralDfa1_0(0x2000L);
		default:
			return jjMoveNfa_0(6, 0);
		}
	}

	private int jjMoveStringLiteralDfa1_0(long active0) {
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(0, active0);
			return 1;
		}
		switch (curChar) {
		case 77:
		case 109:
			return jjMoveStringLiteralDfa2_0(active0, 0x2000L);
		default:
			break;
		}
		return jjStartNfa_0(0, active0);
	}

	private int jjMoveStringLiteralDfa2_0(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjStartNfa_0(0, old0);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(1, active0);
			return 2;
		}
		switch (curChar) {
		case 65:
		case 97:
			return jjMoveStringLiteralDfa3_0(active0, 0x2000L);
		default:
			break;
		}
		return jjStartNfa_0(1, active0);
	}

	private int jjMoveStringLiteralDfa3_0(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjStartNfa_0(1, old0);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(2, active0);
			return 3;
		}
		switch (curChar) {
		case 71:
		case 103:
			return jjMoveStringLiteralDfa4_0(active0, 0x2000L);
		default:
			break;
		}
		return jjStartNfa_0(2, active0);
	}

	private int jjMoveStringLiteralDfa4_0(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjStartNfa_0(2, old0);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(3, active0);
			return 4;
		}
		switch (curChar) {
		case 69:
		case 101:
			return jjMoveStringLiteralDfa5_0(active0, 0x2000L);
		default:
			break;
		}
		return jjStartNfa_0(3, active0);
	}

	private int jjMoveStringLiteralDfa5_0(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjStartNfa_0(3, old0);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(4, active0);
			return 5;
		}
		switch (curChar) {
		case 58:
			if ((active0 & 0x2000L) != 0L)
				return jjStopAtPos(5, 13);
			break;
		default:
			break;
		}
		return jjStartNfa_0(4, active0);
	}

	private int jjStartNfaWithStates_0(int pos, int kind, int state) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return pos + 1;
		}
		return jjMoveNfa_0(state, pos + 1);
	}

	static final long[] jjbitVec0 = { 0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL,
			0xffffffffffffffffL };
	static final long[] jjbitVec2 = { 0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL };

	private int jjMoveNfa_0(int startState, int curPos) {
		int startsAt = 0;
		jjnewStateCnt = 8;
		int i = 1;
		jjstateSet[0] = startState;
		int kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				do {
					switch (jjstateSet[--i]) {
					case 6:
						if ((0x880098feffffd9ffL & l) != 0L) {
							if (kind > 4)
								kind = 4;
							{
								jjCheckNAdd(0);
							}
						} else if ((0x3ff000000000000L & l) != 0L) {
							if (kind > 7)
								kind = 7;
							{
								jjCheckNAdd(1);
							}
						} else if ((0x2400L & l) != 0L) {
							if (kind > 9)
								kind = 9;
						} else if ((0x100000200L & l) != 0L) {
							jjCheckNAddStates(0, 2);
						}
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 8:
						if ((0x2400L & l) != 0L) {
							if (kind > 9)
								kind = 9;
						} else if ((0x100000200L & l) != 0L) {
							jjCheckNAddStates(0, 2);
						}
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 0:
						if ((0x880098feffffd9ffL & l) == 0L)
							break;
						kind = 4; {
						jjCheckNAdd(0);
					}
						break;
					case 1:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 7)
							kind = 7; {
						jjCheckNAdd(1);
					}
						break;
					case 2:
						if ((0x100000200L & l) != 0L) {
							jjCheckNAddStates(0, 2);
						}
						break;
					case 3:
						if ((0x2400L & l) != 0L && kind > 9)
							kind = 9;
						break;
					case 4:
						if (curChar == 10 && kind > 9)
							kind = 9;
						break;
					case 5:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 7:
						if ((0x77ff670000000000L & l) != 0L && kind > 11)
							kind = 11;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				do {
					switch (jjstateSet[--i]) {
					case 6:
						if ((0xfffffffe47ffffffL & l) != 0L) {
							if (kind > 4)
								kind = 4;
							{
								jjCheckNAdd(0);
							}
						} else if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 7;
						break;
					case 0:
						if ((0xfffffffe47ffffffL & l) == 0L)
							break;
						kind = 4; {
						jjCheckNAdd(0);
					}
						break;
					case 7:
						if ((0x1b8000000L & l) != 0L && kind > 11)
							kind = 11;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				do {
					switch (jjstateSet[--i]) {
					case 6:
					case 0:
						if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
							break;
						if (kind > 4)
							kind = 4; {
						jjCheckNAdd(0);
					}
						break;
					default:
						if (i1 == 0 || l1 == 0 || i2 == 0 || l2 == 0)
							break;
						else
							break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 8 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	static final int[] jjnextStates = { 2, 3, 5, };

	private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
		switch (hiByte) {
		case 0:
			return ((jjbitVec2[i2] & l2) != 0L);
		default:
			if ((jjbitVec0[i1] & l1) != 0L)
				return true;
			return false;
		}
	}

	public static final String[] jjstrLiteralImages = { "", "\52", "\134", "\140", null, "\72", "\55", null, "\56",
			null, "\75", null, "\76", null, "\133", "\50", "\74", "\135", "\51", "\40", "\11", "\137", };

	protected Token jjFillToken() {
		final Token t;
		final String curTokenImage;
		final int beginLine;
		final int endLine;
		final int beginColumn;
		final int endColumn;
		String im = jjstrLiteralImages[jjmatchedKind];
		curTokenImage = (im == null) ? input_stream.getImage() : im;
		beginLine = input_stream.getBeginLine();
		beginColumn = input_stream.getBeginColumn();
		endLine = input_stream.getEndLine();
		endColumn = input_stream.getEndColumn();

		t = new Token(jjmatchedKind, beginLine, beginColumn, endLine, endColumn, curTokenImage);

		t.beginLine = beginLine;
		t.endLine = endLine;
		t.beginColumn = beginColumn;
		t.endColumn = endColumn;

		return t;
	}

	int curLexState = 0;
	int defaultLexState = 0;
	int jjnewStateCnt;
	int jjround;
	int jjmatchedPos;
	int jjmatchedKind;

	public Token getNextToken() {
		Token matchedToken;
		int curPos = 0;

		EOFLoop: for (;;) {
			try {
				curChar = input_stream.beginToken();
			} catch (java.io.IOException e) {
				jjmatchedKind = 0;
				jjmatchedPos = -1;
				matchedToken = jjFillToken();
				return matchedToken;
			}

			jjmatchedKind = 0x7fffffff;
			jjmatchedPos = 0;
			curPos = jjMoveStringLiteralDfa0_0();
			if (jjmatchedKind != 0x7fffffff) {
				if (jjmatchedPos + 1 < curPos)
					input_stream.backup(curPos - jjmatchedPos - 1);
				matchedToken = jjFillToken();
				return matchedToken;
			}
			int error_line = input_stream.getEndLine();
			int error_column = input_stream.getEndColumn();
			String error_after = null;
			boolean EOFSeen = false;
			try {
				input_stream.readChar();
				input_stream.backup(1);
			} catch (java.io.IOException e1) {
				EOFSeen = true;
				error_after = curPos <= 1 ? "" : input_stream.getImage();
				if (curChar == '\n' || curChar == '\r') {
					error_line++;
					error_column = 0;
				} else
					error_column++;
			}
			if (!EOFSeen) {
				input_stream.backup(1);
				error_after = curPos <= 1 ? "" : input_stream.getImage();
			}
			throw new RuntimeException();
		}
	}

	private void jjCheckNAdd(int state) {
		if (jjrounds[state] != jjround) {
			jjstateSet[jjnewStateCnt++] = state;
			jjrounds[state] = jjround;
		}
	}

	private void jjCheckNAddStates(int start, int end) {
		do {
			jjCheckNAdd(jjnextStates[start]);
		} while (start++ != end);
	}

	public TokenManager(CharStream stream) {
		input_stream = stream;
	}

	public TokenManager(CharStream stream, int lexState) {
		jjmatchedPos = jjnewStateCnt = 0;
		curLexState = defaultLexState;
		input_stream = stream;
		ReInitRounds();
		SwitchTo(lexState);
	}

	private void ReInitRounds() {
		int i;
		jjround = 0x80000001;
		for (i = 8; i-- > 0;)
			jjrounds[i] = 0x80000000;
	}

	public void SwitchTo(int lexState) {
		if (lexState >= 1 || lexState < 0)
			throw new RuntimeException();
		else
			curLexState = lexState;
	}

	public static final String[] lexStateNames = { "DEFAULT", };
	protected CharStream input_stream;
	private final int[] jjrounds = new int[8];
	private final int[] jjstateSet = new int[2 * 8];
	protected char curChar;
}