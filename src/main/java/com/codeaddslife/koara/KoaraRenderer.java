package com.codeaddslife.koara;

import java.util.Stack;

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

public class KoaraRenderer implements Renderer {

	private StringBuilder out;
	private Stack<String> left;
	private boolean hardWrap;
	
	@Override
	public void visit(Document node) {
		out = new StringBuilder();
		left = new Stack<String>();
		node.childrenAccept(this);
	}

	@Override
	public void visit(Heading node) {
		if(!node.isFirstChild()) {
			indent();
		}
		for(int i=0; i<node.getLevel(); i++) {
			out.append("=");
		}
		if(node.hasChildren()) {
		  out.append(" ");
		  node.childrenAccept(this);
		}
		out.append("\n");
		if(!node.isLastChild()) {
			indent();
			out.append("\n");
		}
	}

	@Override
	public void visit(BlockQuote node) {
		if(!node.isFirstChild()) {
			indent();
		}
		
		if(node.hasChildren()) {
			out.append("> ");
			left.push("> ");
			node.childrenAccept(this);
			left.pop();
		} else {
			out.append(">\n");
		}
		if(!node.isNested()) {
			out.append("\n");
		}
	}

	@Override
	public void visit(ListBlock node) {
		node.childrenAccept(this);
		if(!node.isLastChild()) {
			indent();
			out.append("\n");
			Object next = node.next();
			if(next instanceof ListBlock && ((ListBlock) next).isOrdered() == node.isOrdered() ) {
				out.append("\n");
			}
		}
	}

	@Override
	public void visit(ListItem node) {
		if(!node.getParent().isNested() || !node.isFirstChild() || !node.getParent().isFirstChild()) {
			indent();
		}
		left.push("  ");
		if(node.getNumber() != null) {			
			out.append(node.getNumber() + ".");
		} else {
			out.append("-");
		}
		if(node.hasChildren()) {
			out.append(" ");
			node.childrenAccept(this);
		} else {
			out.append("\n");
		}
		left.pop();
	}

	@Override
	public void visit(CodeBlock node) {
		StringBuilder indent = new StringBuilder();
		for(String s : left) {
			indent.append(s);
		}
		
		out.append("```");
		if(node.getLanguage() != null) {
			out.append(node.getLanguage());
		}
		out.append("\n");
		
		
		
		out.append(node.getValue().toString().replaceAll("(?m)^", indent.toString()));
		out.append("\n");
		indent();
		out.append("```");
		out.append("\n");
		
		if(!node.isLastChild()) {
			indent();
			out.append("\n");
		}
	}

	@Override
	public void visit(Paragraph node) {
		if(!node.isFirstChild()) {
			indent();
		}
		node.childrenAccept(this);
		out.append("\n");
		if(!node.isNested() || (node.getParent() instanceof ListItem && (node.next() instanceof Paragraph) && !node.isLastChild())) {
			out.append("\n");
		} else if(node.getParent() instanceof BlockQuote && (node.next() instanceof Paragraph)) {
			indent();
			out.append("\n");
		}
	}

	@Override
	public void visit(BlockElement node) {
		if(!node.isFirstChild()) {
			indent();
		}
		node.childrenAccept(this);
		out.append("\n");
		if(!node.isNested() || (node.getParent() instanceof ListItem && (node.next() instanceof Paragraph) && !node.isLastChild())) {
			out.append("\n");
		} else if(node.getParent() instanceof BlockQuote && (node.next() instanceof Paragraph)) {
			indent();
			out.append("\n");
		}
	}

	@Override
	public void visit(Image node) {
		out.append("[image: ");
		node.childrenAccept(this);
		out.append("]");
		if(node.getValue() != null && node.getValue().toString().trim().length() > 0) {
			out.append("(");
			out.append(escapeUrl(node.getValue().toString()));
			out.append(")");
		}
	}

	@Override
	public void visit(Link node) {
		out.append("[");
		node.childrenAccept(this);
		out.append("]");
		if(node.getValue() != null && node.getValue().toString().trim().length() > 0) {
			out.append("(");
			out.append(escapeUrl(node.getValue().toString()));
			out.append(")");
		}
	}

	@Override
	public void visit(Text node) {
		if(node.getParent() instanceof Code) {
			out.append(node.getValue().toString());
		} else {
			out.append(escape(node.getValue().toString()));
		}
	}

	@Override
	public void visit(Strong node) {
		out.append("*");
		node.childrenAccept(this);
		out.append("*");
	}

	@Override
	public void visit(Em node) {
		out.append("_");
		node.childrenAccept(this);
		out.append("_");
	}

	@Override
	public void visit(Code node) {
		out.append("`");
		node.childrenAccept(this);
		out.append("`");
	}

	@Override
	public void visit(LineBreak node) {
		if(hardWrap || node.isExplicit()) {
			out.append("  ");
		}
		out.append("\n");
		indent();
	}
	
	public String escapeUrl(String text) {
		return text.replaceAll("\\(", "\\\\(")
				.replaceAll("\\)", "\\\\)");
	}
	
	public String escape(String text) {
		return text.replaceAll("\\[", "\\\\[")
				.replaceAll("\\]", "\\\\]")
				.replaceAll("\\*", "\\\\*")
				.replaceAll("\\_", "\\\\_")
				.replaceFirst("\\`", "\\\\`")
				.replaceFirst("\\=", "\\\\=")
				.replaceFirst("\\>", "\\\\>")
				.replaceFirst("\\-", "\\\\-")
				.replaceFirst("(\\d+)\\.", "\\\\$1.");
	}
	
	private void indent() {
		for(String s : left) {
			out.append(s);
		}
	}
	
	public void setHardWrap(boolean hardWrap) {
		this.hardWrap = hardWrap;
	}

	public String getOutput() {
		return out.toString().trim();
	}
	
	
}
