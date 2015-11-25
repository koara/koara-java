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
package io.koara.renderer;

import io.koara.ast.BlockElement;
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

public interface Renderer {

	void visit(Document node);

	void visit(Heading node);

	void visit(Blockquote node);

	void visit(List node);

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