package io.koara.renderer;

import io.koara.Node;
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

public interface Renderer {
	
	//void visit(Node node);

	void visit(Document node);

	void visit(Heading node);

	void visit(Blockquote node);

	void visit(List node);

	void visit(ListItem node);

	void visit(CodeBlock node);

	void visit(Paragraph node);

	void visit(Image node);

	void visit(Link node);

	void visit(Text node);

	void visit(Strong node);

	void visit(Em node);

	void visit(Code node);

	void visit(LineBreak node);
	
}