package io.koara.renderer;

import java.util.Stack;

import io.koara.KoaraDefaultVisitor;
import io.koara.ast.Blockquote;
import io.koara.ast.Code;
import io.koara.ast.CodeBlock;
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
import io.koara.ast.Document;

public class HtmlRenderer extends KoaraDefaultVisitor {

	private StringBuffer out;
	private int level;
	private Stack<Integer> listSequence = new Stack<Integer>();

	@Override
	public Object visit(Document node, Object data) {
		out = new StringBuffer();
		node.childrenAccept(this, data);
		return null;
	}
	
	@Override
	public Object visit(Heading node, Object data) {
		out.append(indent() + "<h" + node.value + ">");
		super.visit(node, data);
		out.append("</h" + node.value + ">\n");
		if(!node.isNested()) { out.append("\n"); }
		return null;
	}
	
	@Override
	public Object visit(Blockquote node, Object data) {
		out.append(indent() + "<blockquote>");
		if(node.children != null && node.children.length > 0) { out.append("\n"); }
		level++;
		super.visit(node, data);
		level--;
		out.append(indent() + "</blockquote>\n");
		if(!node.isNested()) { out.append("\n"); }
		return null;
	}
	
	@Override
	public Object visit(List node, Object data) {
		listSequence.push(0);
		String tag = node.isOrdered() ? "ol" : "ul";
		out.append(indent() + "<" + tag + ">\n");
		level++;
		super.visit(node, data);
		level--;
		out.append(indent() + "</" + tag + ">\n");
		if(!node.isNested()) { out.append("\n"); }
		listSequence.pop();
		return null;
	}
	
	@Override
	public Object visit(ListItem node, Object data) {
		Integer seq = listSequence.peek() + 1;		
		listSequence.set(listSequence.size() - 1, listSequence.peek() + 1);
		out.append(indent() + "<li");
		if(node.getNumber() != null && (seq != node.getNumber())) {
			out.append(" value=\"" + node.getNumber() + "\"");
			listSequence.set(listSequence.size() - 1, node.getNumber());
		}
		out.append(">");
		if(node.children != null) {
			if(node.children.length > 1 || !(node.children[0] instanceof Paragraph)) { out.append("\n"); }
			level++;
			super.visit(node, data);
			level--;
			if(node.children.length > 1 || !(node.children[0] instanceof Paragraph)) { out.append(indent()); }
		}
		out.append("</li>\n");
		return null;
	}
	
	@Override
	public Object visit(CodeBlock node, Object data) {
		out.append(indent() + "<pre><code");
		if(node.getLanguage() != null) {
			out.append(" class=\"language-" + node.getLanguage() + "\"");
		}
		out.append(">");
		out.append(escape(node.value.toString()) + "</code></pre>\n");
		if(!node.isNested()) { out.append("\n"); }
		return null;
	}
		
	@Override
	public Object visit(Paragraph node, Object data) {
		if(node.isNested() && (node.parent instanceof ListItem) && node.isSingleChild()) {
			super.visit(node, data);
		} else {
			out.append(indent() + "<p>");
			super.visit(node, data);
			out.append("</p>\n");
			if(!node.isNested()) { out.append("\n"); }
		}
		return null;
	}
		
	@Override
	public Object visit(Image node, Object data) {
		out.append("<img src=\"" + escapeUrl(node.value.toString()) + "\" alt=\"");
		super.visit(node, data);
		out.append("\" />");
		return null;
	}
	
	@Override
	public Object visit(Link node, Object data) {
		out.append("<a href=\"" + escapeUrl(node.value.toString()) + "\">");
		super.visit(node, data);
		out.append("</a>");
		return null;
	}
	
	@Override
	public Object visit(Strong node, Object data) {
		out.append("<strong>");
		super.visit(node, data);
		out.append("</strong>");
		return null;
	}
	
	@Override
	public Object visit(Em node, Object data) {
		out.append("<em>");
		super.visit(node, data);
		out.append("</em>");
		return null;
	}
	
	@Override
	public Object visit(Code node, Object data) {
		out.append("<code>");
		super.visit(node, data);
		out.append("</code>");
		return null;
	}
		
	@Override
	public Object visit(Text node, Object data) {
		out.append(escape(node.value.toString()));
		return null;
	}
	
	public String escape(String text) {
		return text.replaceAll("&", "&amp;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;")
				.replaceAll("\"", "&quot;");
	}
	
	@Override
	public Object visit(LineBreak node, Object data) {
		out.append("<br>\n" + indent());
		return super.visit(node, data);
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