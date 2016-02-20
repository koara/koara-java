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
package io.koara.renderer;

import io.koara.ast.BlockElement;
import io.koara.ast.BlockQuote;
import io.koara.ast.Code;
import io.koara.ast.CodeBlock;
import io.koara.ast.Document;
import io.koara.ast.Em;
import io.koara.ast.Heading;
import io.koara.ast.Image;
import io.koara.ast.LineBreak;
import io.koara.ast.Link;
import io.koara.ast.ListBlock;
import io.koara.ast.ListItem;
import io.koara.ast.Paragraph;
import io.koara.ast.Strong;
import io.koara.ast.Text;
public class XmlRenderer implements Renderer {

	private StringBuffer out;
	private int level;
	
	@Override
	public void visit(Document node) {
		out = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		if(node.getChildren() != null && node.getChildren().length > 0) {
			out.append("<document>\n");
			node.childrenAccept(this);
			out.append("</document>");
		} else {
			out.append("<document />");
		}
	}

	@Override
	public void visit(Heading node) {
		level++;
		out.append(indent() + "<heading level=\"" + node.getValue() + "\"");
		if(node.getChildren() != null && node.getChildren().length > 0) {
			out.append(">\n");
			level++;
			node.childrenAccept(this);
			level--;
			out.append(indent() + "</heading>\n");
		} else {
			out.append(" />\n");
		}
		level--;
	}

	@Override
	public void visit(BlockQuote node) {
		level++;
		out.append(indent() + "<blockquote");
		if(node.getChildren() != null && node.getChildren().length > 0) {
			out.append(">\n");
			level++;
			node.childrenAccept(this);
			level--;
			out.append(indent() + "</blockquote>\n");
			level--;
		} else {
			out.append(" />\n");
		}
	}

	@Override
	public void visit(ListBlock node) {
		level++;
		out.append(indent() + "<list ordered=\"" + node.isOrdered() + "\">\n");
		node.childrenAccept(this);
		out.append(indent() + "</list>\n");
		level--;
	}

	@Override
	public void visit(ListItem node) {
		level++;
		out.append(indent() + "<listitem");
		if(node.getNumber() != null) {
			out.append(" number=\"" + node.getNumber() + "\"");
		}
		if(node.getChildren() != null && node.getChildren().length > 0) {
			out.append(">\n");
			node.childrenAccept(this);
			out.append(indent() + "</listitem>\n");
		} else {
			out.append(" />\n");
		}
		level--;
	}

	@Override
	public void visit(CodeBlock node) {
		level++;
		out.append(indent() + "<codeblock");
		if(node.getLanguage() != null) {
			out.append(" language=\"" + node.getLanguage() + "\"");
		}
		if(node.getValue() != null && node.getValue().toString().length() > 0) {
			out.append(">\n");
			level++;
			out.append(escape(node.getValue().toString()));
			level--;
			out.append(indent() + "</codeblock>\n");
			level--;
		} else {
			out.append(" />\n");
		}
	}

	@Override
	public void visit(Paragraph node) {
		level++;
		out.append(indent() + "<paragraph>\n");
		level++;
		node.childrenAccept(this);
		level--;
		out.append(indent() + "</paragraph>\n");
		level--;
	}

	@Override
	public void visit(BlockElement node) {
	}

	@Override
	public void visit(Image node) {
		out.append(indent() + "<image url=\"" + escapeUrl(node.getValue().toString()) + "\">\n");
		level++;
		node.childrenAccept(this);
		level--;
		out.append(indent() + "</image>\n");
	}

	@Override
	public void visit(Link node) {
		out.append(indent() + "<link url=\"" + escapeUrl(node.getValue().toString()) + "\">\n");
		level++;
		node.childrenAccept(this);
		level--;
		out.append(indent() + "</link>\n");
	}

	@Override
	public void visit(Text node) {
		out.append(indent() + "<text>");
		out.append(escape(node.getValue().toString()));
		out.append("</text>\n");
	}

	@Override
	public void visit(Strong node) {
		out.append(indent() + "<strong>\n");
		level++;
		node.childrenAccept(this);
		level--;
		out.append(indent() + "</strong>\n");
	}

	@Override
	public void visit(Em node) {
		out.append(indent() + "<em>\n");
		level++;
		node.childrenAccept(this);
		level--;
		out.append(indent() + "</em>\n");
	}

	@Override
	public void visit(Code node) {
		out.append(indent() + "<code>\n");
		level++;
		node.childrenAccept(this);
		level--;
		out.append(indent() + "</code>\n");
	}

	@Override
	public void visit(LineBreak node) {
		out.append(indent() + "<linebreak />\n");
	}

	public String escapeUrl(String text) {
		return text.replaceAll(" ", "%20")
				.replaceAll("\"", "%22")
				.replaceAll("`", "%60")
				.replaceAll("<", "%3C")
				.replaceAll(">", "%3E")
				.replaceAll("\\[", "%5B")
				.replaceAll("\\]", "%5D")
				.replaceAll("\\\\", "%5C");
	}
	
	public String indent() {
		int repeat = level * 2;
	    final char[] buf = new char[repeat];
		for (int i = repeat - 1; i >= 0; i--) {
		 buf[i] = ' ';
		} 
		return new String(buf);
	}
	
	public String escape(String text) {
		return text.replaceAll("&", "&amp;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;")
				.replaceAll("\"", "&quot;");
	}
	
	public String getOutput() {
		return out.toString();
	}
	
}