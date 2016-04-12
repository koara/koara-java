package io.koara;

import java.util.Stack;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

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

public class KoaraRenderer implements Renderer {

	private StringBuilder out;
	private Stack<String> left;
	
	@Override
	public void visit(Document node) {
		out = new StringBuilder();
		left = new Stack<String>();
		node.childrenAccept(this);
	}

	@Override
	public void visit(Heading node) {
		for(int i=0; i<node.getLevel(); i++) {
			out.append("=");
		}
		if(node.hasChildren()) {
		  out.append(" ");
		  node.childrenAccept(this);
		}
		out.append("\n\n");
	}

	@Override
	public void visit(BlockQuote node) {
	}

	@Override
	public void visit(ListBlock node) {
		node.childrenAccept(this);
		Object next = node.next();
		out.append("\n");
		if(next instanceof ListBlock && ((ListBlock) next).isOrdered() == node.isOrdered() ) {
			out.append("\n");
		}
	}

	@Override
	public void visit(ListItem node) {
		indent();
		if(node.getNumber() != null) {
			out.append(node.getNumber() + ".");		
		} else {
			out.append("-");		
		}
		left.push("  ");
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
	}

	@Override
	public void visit(Paragraph node) {
		if(!(node.getParent() instanceof ListItem) || !node.isFirstChild()) {
		indent();
		}
		node.childrenAccept(this);
		out.append("\n");
		if(!node.isNested() || node.next() instanceof Paragraph) {
			out.append("\n");
		} else if (!node.isSingleChild() && node.isLastChild()) {
			out.append("\n");
		}
	} 

	@Override
	public void visit(BlockElement node) {
	}

	@Override
	public void visit(Image node) {
	}

	@Override
	public void visit(Link node) {
	}

	@Override
	public void visit(Text node) {
		out.append(escape(node.getValue().toString()));
	}

	@Override
	public void visit(Strong node) {
	}

	@Override
	public void visit(Em node) {
	}

	@Override
	public void visit(Code node) {
	}

	@Override
	public void visit(LineBreak node) {
		out.append("\n");
		indent();
	}
	
	public String escape(String text) {
		return text.replaceAll("\\[", "\\\\[")
				.replaceAll("\\]", "\\\\]")
				.replaceAll("\\*", "\\\\*")
				.replaceAll("\\_", "\\\\_")
				.replaceAll("`", "\\`")
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

	public String getOutput() {
		return out.toString().trim();
	}
	
	
}
