package io.koara;

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

public class KoaraRenderer implements Renderer {

	private StringBuffer out;
	private Stack<String> pre;
	
	@Override
	public void visit(Document node) {
		out = new StringBuffer();
		pre = new Stack<String>();
		node.childrenAccept(this);
	}

	@Override
	public void visit(Heading node) {
		Integer level = (Integer) node.getValue();
		for(int i=0; i < level; i++) {
			out.append("=");
		}
		if(node.getChildren() != null && node.getChildren().length > 0) {
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
		if(node.isNested()) { 
			pre.push("  ");
		}
		node.childrenAccept(this);
		if(node.isNested()) {
			pre.pop();
		}
	}

	@Override
	public void visit(ListItem node) {
		boolean ordered = ((ListBlock) node.getParent()).isOrdered();
		out.append(indent());
		out.append(ordered ? node.getNumber() + "." : "-");
		out.append(" ");
			pre.push("  ");
		node.childrenAccept(this);
			pre.pop();
	}

	@Override
	public void visit(CodeBlock node) {
	}

	@Override
	public void visit(Paragraph node) {
		if(!node.isFirstChild()) {
			out.append(indent());
		}
		node.childrenAccept(this);
		out.append("\n");
		if(!node.isSingleChild() && (!node.isNested() || node.next() instanceof Paragraph)) {
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
		out.append("\n" + indent());
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
	
	public String indent() {
		StringBuilder str = new StringBuilder();
		for(String s : pre) {
			str.append(s);
		}
		return str.toString();
	}

	public String getOutput() {
		return out.toString().trim();
	}
	
}
