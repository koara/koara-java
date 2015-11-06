package io.koara;

import java.io.IOException;

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

	private CharStream cs;
	private int[] jjrounds = new int[8];
	private int[] jjstateSet = new int[2 * 8];
	private char curChar;
	private long[] jjbitVec0 = { 0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL };
	private long[] jjbitVec2 = { 0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL };
	private int[] jjnextStates = { 2, 3, 5, };
	private String[] jjstrLiteralImages = { "", "\52", "\134", "\140", null, "\72", "\55", null, "\56",
			null, "\75", null, "\76", null, "\133", "\50", "\74", "\135", "\51", "\40", "\11", "\137", };

	private int jjnewStateCnt;
	private int round;
	private int matchedPos;
	private int matchedKind;
	
	public TokenManager(CharStream stream) {
		cs = stream;
	}

	public Token getNextToken() {
		Token matchedToken;
		int curPos = 0;

		while (true) {
			try {
				curChar = cs.beginToken();
			} catch (java.io.IOException e) {
				matchedKind = 0;
				matchedPos = -1;
				matchedToken = fillToken();
				return matchedToken;
			}

			matchedKind = 0x7fffffff;
			matchedPos = 0;
			curPos = moveStringLiteralDfa0_0();
			if (matchedKind != 0x7fffffff) {
				if (matchedPos + 1 < curPos) {
					cs.backup(curPos - matchedPos - 1);
				}
				matchedToken = fillToken();
				return matchedToken;
			}
		}
	}
	
	private Token fillToken() {
		String im = jjstrLiteralImages[matchedKind];
		String curTokenImage = (im == null) ? cs.getImage() : im;
		return new Token(matchedKind, cs.getBeginLine(), cs.getBeginColumn(), cs.getEndLine(), cs.getEndColumn(), curTokenImage);
	}

	private int moveStringLiteralDfa0_0() {
		switch (curChar) {
		case 9:
			return startNfaWithStates_0(0, 20, 8);
		case 32:
			return startNfaWithStates_0(0, 19, 8);
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
			return startNfaWithStates_0(0, 2, 7);
		case 93:
			return jjStopAtPos(0, 17);
		case 95:
			return jjStopAtPos(0, 21);
		case 96:
			return jjStopAtPos(0, 3);
		case 73:
		case 105:
			return moveStringLiteralDfa1_0(0x2000L);
		default:
			return moveNfa_0(6, 0);
		}
	}

	private int startNfaWithStates_0(int pos, int kind, int state) {
		matchedKind = kind;
		matchedPos = pos;
		try {
			curChar = cs.readChar();
		} catch (java.io.IOException e) {
			return pos + 1;
		}
		return moveNfa_0(state, pos + 1);
	}
	
	private int jjStopAtPos(int pos, int kind) {
		matchedKind = kind;
		matchedPos = pos;
		return pos + 1;
	}

	private int moveStringLiteralDfa1_0(long active) {
		try {
			curChar = cs.readChar();
		} catch (IOException e) {
			stopStringLiteralDfa_0(0, active);
			return 1;
		}
		switch (curChar) {
		case 77:
		case 109:
			return moveStringLiteralDfa2_0(active, 0x2000L);
		default:
			break;
		}
		return startNfa_0(0, active);
	}
	
	private int moveStringLiteralDfa2_0(long old0, long active) {
		if (((active &= old0)) == 0L)
			return startNfa_0(0, old0);
		try {
			curChar = cs.readChar();
		} catch (IOException e) {
			stopStringLiteralDfa_0(1, active);
			return 2;
		}
		switch (curChar) {
		case 65:
		case 97:
			return moveStringLiteralDfa3_0(active, 0x2000L);
		default:
			break;
		}
		return startNfa_0(1, active);
	}
	
	private int moveStringLiteralDfa3_0(long old, long active) {
		if (((active &= old)) == 0L)
			return startNfa_0(1, old);
		try {
			curChar = cs.readChar();
		} catch (IOException e) {
			stopStringLiteralDfa_0(2, active);
			return 3;
		}
		switch (curChar) {
		case 71:
		case 103:
			return moveStringLiteralDfa4_0(active, 0x2000L);
		default:
			break;
		}
		return startNfa_0(2, active);
	}
	
	private int moveStringLiteralDfa4_0(long old, long active) {
		if (((active &= old)) == 0L)
			return startNfa_0(2, old);
		try {
			curChar = cs.readChar();
		} catch (IOException e) {
			stopStringLiteralDfa_0(3, active);
			return 4;
		}
		switch (curChar) {
		case 69:
		case 101:
			return moveStringLiteralDfa5_0(active, 0x2000L);
		default:
			break;
		}
		return startNfa_0(3, active);
	}
	
	private int moveStringLiteralDfa5_0(long old, long active) {
		if (((active &= old)) == 0L)
			return startNfa_0(3, old);
		try {
			curChar = cs.readChar();
		} catch (java.io.IOException e) {
			stopStringLiteralDfa_0(4, active);
			return 5;
		}
		switch (curChar) {
		case 58:
			if ((active & 0x2000L) != 0L)
				return jjStopAtPos(5, 13);
			break;
		default:
			break;
		}
		return startNfa_0(4, active);
	}
	
	private int startNfa_0(int pos, long active) {
		return moveNfa_0(stopStringLiteralDfa_0(pos, active), pos + 1);
	}

	private int moveNfa_0(int startState, int curPos) {
		int startsAt = 0;
		jjnewStateCnt = 8;
		int i = 1;
		jjstateSet[0] = startState;
		int kind = 0x7fffffff;
		for (;;) {
			if (++round == 0x7fffffff) {
				round = 0x80000001;
			}
			if (curChar < 64) {
				long l = 1L << curChar;
				do {
					switch (jjstateSet[--i]) {
					case 6:
						if ((0x880098feffffd9ffL & l) != 0L) {
							if (kind > 4)
								kind = 4;
							{
								checkNAdd(0);
							}
						} else if ((0x3ff000000000000L & l) != 0L) {
							if (kind > 7)
								kind = 7;
							{
								checkNAdd(1);
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
						checkNAdd(0);
					}
						break;
					case 1:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 7)
							kind = 7; {
						checkNAdd(1);
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
								checkNAdd(0);
							}
						} else if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 7;
						break;
					case 0:
						if ((0xfffffffe47ffffffL & l) == 0L)
							break;
						kind = 4; {
						checkNAdd(0);
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
						if (!canMove_0(hiByte, i1, i2, l1, l2))
							break;
						if (kind > 4)
							kind = 4; {
						checkNAdd(0);
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
				matchedKind = kind;
				matchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 8 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = cs.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}
	
	private boolean canMove_0(int hiByte, int i1, int i2, long l1, long l2) {
		switch (hiByte) {
		case 0:
			return ((jjbitVec2[i2] & l2) != 0L);
		default:
			if ((jjbitVec0[i1] & l1) != 0L)
				return true;
			return false;
		}
	}
	
	private void jjCheckNAddStates(int start, int end) {
		do {
			checkNAdd(jjnextStates[start]);
		} while (start++ != end);
	}
	
	private void checkNAdd(int state) {
		if (jjrounds[state] != round) {
			jjstateSet[jjnewStateCnt++] = state;
			jjrounds[state] = round;
		}
	}
	
	private final int stopStringLiteralDfa_0(int pos, long active) {
		switch (pos) {
		case 0:
			if ((active & 0x2000L) != 0L) {
				matchedKind = 4;
				return 0;
			}
			if ((active & 0x180000L) != 0L)
				return 8;
			if ((active & 0x4L) != 0L)
				return 7;
			return -1;
		case 1:
			if ((active & 0x2000L) != 0L) {
				matchedKind = 4;
				matchedPos = 1;
				return 0;
			}
			return -1;
		case 2:
			if ((active & 0x2000L) != 0L) {
				matchedKind = 4;
				matchedPos = 2;
				return 0;
			}
			return -1;
		case 3:
			if ((active & 0x2000L) != 0L) {
				matchedKind = 4;
				matchedPos = 3;
				return 0;
			}
			return -1;
		case 4:
			if ((active & 0x2000L) != 0L) {
				matchedKind = 4;
				matchedPos = 4;
				return 0;
			}
			return -1;
		default:
			return -1;
		}
	}
	
}
