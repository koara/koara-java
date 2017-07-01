/*

 * Copyright 2015-2016 the original author or authors.
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
package com.codeaddslife.koara;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class CharStream {

    private int available = 4096;
    private int bufSize = 4096;
    private int tokenBegin;
    private int bufColumn[] = new int[4096];;
    private int bufPos = -1;
    private int bufLine[] = new int[4096];
    private int column = 0;
    private int line = 1;
    private boolean prevCharIsLF;
    private Reader reader;
    private char[] buffer = new char[4096];
    private int maxNextCharInd = 0;
    private int inBuf = 0;
    private int tabSize = 4;

    public CharStream(Reader reader) {
        this.reader = reader;
    }

    public char beginToken() throws IOException {
        tokenBegin = -1;
        char c = readChar();
        tokenBegin = bufPos;
        return c;
    }

    public char readChar() throws IOException {
        if (inBuf > 0) {
            --inBuf;
            if (++bufPos == bufSize) {
                bufPos = 0;
            }
            return buffer[bufPos];
        }
        if (++bufPos >= maxNextCharInd) {
            fillBuff();
        }
        char c = buffer[bufPos];
        updateLineColumn(c);
        return c;
    }

    private void fillBuff() throws IOException {
        if (maxNextCharInd == available) {
            if (available == bufSize) {
                bufPos = 0;
                maxNextCharInd = 0;
                if (tokenBegin > 2048) {
                    available = tokenBegin;
                }
            } else {
                available = bufSize;
            }
        }
        int i;
        
        try {
            if ((i = reader.read(buffer, maxNextCharInd, available - maxNextCharInd)) == -1) {
            	reader.close();
                throw new IOException();
            } else {
                maxNextCharInd += i;
            }
        } catch (IOException e) {
            --bufPos;
            backup(0);
            if (tokenBegin == -1) {
                tokenBegin = bufPos;
            }
            throw e;
        }
    }

    public void backup(int amount) {
        inBuf += amount;
        if ((bufPos -= amount) < 0) {
            bufPos += bufSize;
        }
    }

    private void updateLineColumn(char c) {
        column++;
        if (prevCharIsLF) {
            prevCharIsLF = false;
            column = 1;
            line += column;
        }

        switch (c) {
        case '\n':
            prevCharIsLF = true;
            break;
        case '\t':
            column--;
            column += (tabSize - (column % tabSize));
            break;
        }
        bufLine[bufPos] = line;
        bufColumn[bufPos] = column;
    }

    public String getImage() {
        if (bufPos >= tokenBegin) {
            return new String(buffer, tokenBegin, bufPos - tokenBegin + 1);
        }
        return new String(buffer, tokenBegin, bufSize - tokenBegin) + new String(buffer, 0, bufPos + 1);
    }

    public int getEndColumn() {
        return bufColumn[bufPos];
    }

    public int getEndLine() {
        return bufLine[bufPos];
    }

    public int getBeginColumn() {
        return bufColumn[tokenBegin];
    }

    public int getBeginLine() {
        return bufLine[tokenBegin];
    }

}