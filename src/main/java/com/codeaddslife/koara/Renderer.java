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

import com.codeaddslife.koara.ast.BlockElement;
import com.codeaddslife.koara.ast.BlockQuote;
import com.codeaddslife.koara.ast.Code;
import com.codeaddslife.koara.ast.CodeBlock;
import com.codeaddslife.koara.ast.Document;
import com.codeaddslife.koara.ast.Em;
import com.codeaddslife.koara.ast.Heading;
import com.codeaddslife.koara.ast.Image;
import com.codeaddslife.koara.ast.LineBreak;
import com.codeaddslife.koara.ast.Link;
import com.codeaddslife.koara.ast.ListBlock;
import com.codeaddslife.koara.ast.ListItem;
import com.codeaddslife.koara.ast.Paragraph;
import com.codeaddslife.koara.ast.Strong;
import com.codeaddslife.koara.ast.Text;

public interface Renderer {

    void visit(Document node);

    void visit(Heading node);

    void visit(BlockQuote node);

    void visit(ListBlock node);

    void visit(ListItem node);

    void visit(CodeBlock node);

    void visit(Paragraph node);

    void visit(BlockElement node);

    void visit(Image node);

    void visit(Link node);

    void visit(Text node);

    void visit(Strong node);

    void visit(Em node);

    void visit(Code node);

    void visit(LineBreak node);

}