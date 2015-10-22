package io.koara;

import java.io.Reader;

public class CharStream {
	
	private int bufsize;
	private int available;
	private int tokenBegin;
	private int bufpos = -1;
	private int bufline[];
	private int bufcolumn[];
	private int column = 0;
	private int line = 1;
	private boolean prevCharIsCR = false;
	private boolean prevCharIsLF = false;
	private java.io.Reader inputStream;
	private char[] buffer;
	private int maxNextCharInd = 0;
	private int inBuf = 0;
	private int tabSize = 4;
	
	public CharStream(Reader dstream) {
		inputStream = dstream;
		line = 1;
		column = 1 - 1;
		available = bufsize = 4096;
		buffer = new char[4096];
		bufline = new int[4096];
		bufcolumn = new int[4096];
	}
	
	public char beginToken() throws java.io.IOException {
		tokenBegin = -1;
		char c = readChar();
		tokenBegin = bufpos;
		return c;
	}
	
	protected char readChar() throws java.io.IOException {
		if (inBuf > 0) {
			--inBuf;
			if (++bufpos == bufsize) {
				bufpos = 0;
			}
			return buffer[bufpos];
		}
		if (++bufpos >= maxNextCharInd) {
			fillBuff();
		}
		char c = buffer[bufpos];
		updateLineColumn(c);
		return c;
	}
	
	private void fillBuff() throws java.io.IOException {
		if (maxNextCharInd == available) {
			if (available == bufsize) {
				if (tokenBegin > 2048) {
					bufpos = maxNextCharInd = 0;
					available = tokenBegin;
				} else if (tokenBegin < 0)
					bufpos = maxNextCharInd = 0;
				else
					expandBuff(false);
			} else if (available > tokenBegin)
				available = bufsize;
			else if ((tokenBegin - available) < 2048)
				expandBuff(true);
			else
				available = tokenBegin;
		}

		int i;
		try {
			if ((i = inputStream.read(buffer, maxNextCharInd, available - maxNextCharInd)) == -1) {
				inputStream.close();
				throw new java.io.IOException();
			} else
				maxNextCharInd += i;
			return;
		} catch (java.io.IOException e) {
			--bufpos;
			backup(0);
			if (tokenBegin == -1)
				tokenBegin = bufpos;
			throw e;
		}
	}
	
	private void expandBuff(boolean wrapAround) {
		char[] newbuffer = new char[bufsize + 2048];
		int newbufline[] = new int[bufsize + 2048];
		int newbufcolumn[] = new int[bufsize + 2048];

		try {
			if (wrapAround) {
				System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
				System.arraycopy(buffer, 0, newbuffer, bufsize - tokenBegin, bufpos);
				buffer = newbuffer;

				System.arraycopy(bufline, tokenBegin, newbufline, 0, bufsize - tokenBegin);
				System.arraycopy(bufline, 0, newbufline, bufsize - tokenBegin, bufpos);
				bufline = newbufline;

				System.arraycopy(bufcolumn, tokenBegin, newbufcolumn, 0, bufsize - tokenBegin);
				System.arraycopy(bufcolumn, 0, newbufcolumn, bufsize - tokenBegin, bufpos);
				bufcolumn = newbufcolumn;

				maxNextCharInd = (bufpos += (bufsize - tokenBegin));
			} else {
				System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
				buffer = newbuffer;

				System.arraycopy(bufline, tokenBegin, newbufline, 0, bufsize - tokenBegin);
				bufline = newbufline;

				System.arraycopy(bufcolumn, tokenBegin, newbufcolumn, 0, bufsize - tokenBegin);
				bufcolumn = newbufcolumn;

				maxNextCharInd = (bufpos -= tokenBegin);
			}
		} catch (Throwable t) {
			throw new Error(t.getMessage());
		}

		bufsize += 2048;
		available = bufsize;
		tokenBegin = 0;
	}
	
	protected void backup(int amount) {
		inBuf += amount;
		if ((bufpos -= amount) < 0) {
			bufpos += bufsize;
		}
	}

	private void updateLineColumn(char c) {
		column++;
		if (prevCharIsLF) {
			prevCharIsLF = false;
			line += (column = 1);
		} else if (prevCharIsCR) {
			prevCharIsCR = false;
			if (c == '\n') {
				prevCharIsLF = true;
			} else
				line += (column = 1);
		}

		switch (c) {
		case '\r':
			prevCharIsCR = true;
			break;
		case '\n':
			prevCharIsLF = true;
			break;
		case '\t':
			column--;
			column += (tabSize - (column % tabSize));
			break;
		default:
			break;
		}
		bufline[bufpos] = line;
		bufcolumn[bufpos] = column;
	}
	
	protected String getImage() {
		if (bufpos >= tokenBegin) {
			return new String(buffer, tokenBegin, bufpos - tokenBegin + 1);
		} else {
			return new String(buffer, tokenBegin, bufsize - tokenBegin) + new String(buffer, 0, bufpos + 1);
		}
	}
	
	public int getEndColumn() {
		return bufcolumn[bufpos];
	}

	public int getEndLine() {
		return bufline[bufpos];
	}

	public int getBeginColumn() {
		return bufcolumn[tokenBegin];
	}

	public int getBeginLine() {
		return bufline[tokenBegin];
	}

}