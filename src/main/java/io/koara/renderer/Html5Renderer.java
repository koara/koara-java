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

import java.util.Stack;

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
public class Html5Renderer implements Renderer {

	private StringBuffer out;
	private int level;
	private Stack<Integer> listSequence = new Stack<Integer>();

	public void visit(Document node) {
		out = new StringBuffer();
		node.childrenAccept(this);
	}
	
	public void visit(Heading node) {
		out.append(indent() + "<h" + node.getValue() + ">");
		node.childrenAccept(this);
		out.append("</h" + node.getValue() + ">\n");
		if(!node.isNested()) { out.append("\n"); }
	}
	
	public void visit(BlockQuote node) {
		out.append(indent() + "<blockquote>");
		if(node.getChildren() != null && node.getChildren().length > 0) { out.append("\n"); }
		level++;
		node.childrenAccept(this);
		level--;
		out.append(indent() + "</blockquote>\n");
		if(!node.isNested()) { out.append("\n"); }
	}
	
	public void visit(ListBlock node) {
		listSequence.push(0);
		String tag = node.isOrdered() ? "ol" : "ul";
		out.append(indent() + "<" + tag + ">\n");
		level++;
		node.childrenAccept(this);
		level--;
		out.append(indent() + "</" + tag + ">\n");
		if(!node.isNested()) { out.append("\n"); }
		listSequence.pop();
	}
	
	public void visit(ListItem node) {
		Integer seq = listSequence.peek() + 1;		
		listSequence.set(listSequence.size() - 1, seq);
		out.append(indent() + "<li");
		if(node.getNumber() != null && (seq.equals(node.getNumber()))) {
			out.append(" value=\"" + node.getNumber() + "\"");
			listSequence.push(node.getNumber());
		}
		out.append(">");
		if(node.getChildren() != null) {
			boolean block = (node.getChildren()[0].getClass() == Paragraph.class || node.getChildren()[0].getClass() == BlockElement.class);
			
			if(node.getChildren().length > 1 || !block) { out.append("\n"); }
			level++;
			node.childrenAccept(this);
			level--;
			if(node.getChildren().length > 1 || !block) { out.append(indent()); }
		}
		out.append("</li>\n");
	}
	
	public void visit(CodeBlock node) {
		out.append(indent() + "<pre><code");
		if(node.getLanguage() != null) {
			out.append(" class=\"language-" + escape(node.getLanguage()) + "\"");
		}
		out.append(">");
		out.append(escape(node.getValue().toString()) + "</code></pre>\n");
		if(!node.isNested()) { out.append("\n"); }
	}

	public void visit(Paragraph node) {
		if(node.isNested() && (node.getParent() instanceof ListItem) && node.isSingleChild()) {
			node.childrenAccept(this);
		} else {
			out.append(indent() + "<p>");
			node.childrenAccept(this);
			out.append("</p>\n");
			if(!node.isNested()) { out.append("\n"); }
		}
	}
	
	@Override
	public void visit(BlockElement node) {
		if(node.isNested() && (node.getParent() instanceof ListItem) && node.isSingleChild()) {
			node.childrenAccept(this);
		} else {
			out.append(indent());
			node.childrenAccept(this);
			if(!node.isNested()) { out.append("\n"); }
		}
	}
		
	public void visit(Image node) {
		out.append("<img src=\"" + escapeUrl(node.getValue().toString()) + "\" alt=\"");
		node.childrenAccept(this);
		out.append("\" />");
	}
	
	public void visit(Link node) {
		out.append("<a href=\"" + escapeUrl(node.getValue().toString()) + "\">");
		node.childrenAccept(this);
		out.append("</a>");
	}
	
	public void visit(Strong node) {
		out.append("<strong>");
		node.childrenAccept(this);
		out.append("</strong>");
	}
	
	public void visit(Em node) {
		out.append("<em>");
		node.childrenAccept(this);
		out.append("</em>");
	}
	
	public void visit(Code node) {
		out.append("<code>");
		node.childrenAccept(this);
		out.append("</code>");
	}
		
	public void visit(Text node) {
		out.append(escape(node.getValue().toString()));
	}
	
	public String escape(String text) {
		return text.replaceAll("&", "&amp;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;")
				.replaceAll("\"", "&quot;");
	}
	
	public void visit(LineBreak node) {
		out.append("<br>\n" + indent());
		node.childrenAccept(this);
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
	
	public String getOutput() {
        return out.toString().trim();
    }

}