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

import java.io.IOException;
import java.io.Reader;

public class CharStream {

    private int available = 4096;
    private int bufsize = 4096;
    private int tokenBegin;
    private int bufcolumn[] = new int[4096];;
    private int bufpos = -1;
    private int bufline[] = new int[4096];
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
        tokenBegin = bufpos;
        return c;
    }

    public char readChar() throws IOException {
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

    private void fillBuff() throws IOException {
        if (maxNextCharInd == available) {
            if (available == bufsize) {
                bufpos = 0;
                maxNextCharInd = 0;
                if (tokenBegin > 2048) {
                    available = tokenBegin;
                }
            } else {
                available = bufsize;
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
            --bufpos;
            backup(0);
            if (tokenBegin == -1) {
                tokenBegin = bufpos;
            }
            throw e;
        }
    }

    public void backup(int amount) {
        inBuf += amount;
        if ((bufpos -= amount) < 0) {
            bufpos += bufsize;
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
        bufline[bufpos] = line;
        bufcolumn[bufpos] = column;
    }

    public String getImage() {
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