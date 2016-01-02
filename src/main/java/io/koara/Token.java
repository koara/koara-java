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

public class Token {

	protected int kind;
	protected int beginLine;
	protected int beginColumn;
	protected int endLine;
	protected int endColumn;
	protected String image;
	protected Token next;
	protected Token specialToken;

	public Token() {
	}

	public Token(int kind, int beginLine, int beginColumn, int endLine, int endColumn, String image) {
		this.kind = kind;
		this.beginLine = beginLine;
		this.beginColumn = beginColumn;
        this.endLine = endLine;
		this.endColumn = endColumn;
		this.image = image;
	}

}