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
		left.push("  ");
		node.childrenAccept(this);
		left.pop();
	}

	@Override
	public void visit(ListItem node) {
		indent();
		out.append("- ");
		node.childrenAccept(this);
	}

	@Override
	public void visit(CodeBlock node) {
	}

	@Override
	public void visit(Paragraph node) {
		node.childrenAccept(this);
		if(!node.isNested()) {
			out.append("\n");
		}
		out.append("\n");
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
	}
	
	private void indent() {
		for(String s : left) {
			out.append(s);
		}
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

	public String getOutput() {
		return out.toString().trim();
	}
	
	
}
