package io.koara.ast;

import io.koara.renderer.Renderer;

public class Paragraph extends BlockElement {

	public void accept(Renderer visitor) {
		visitor.visit(this);
	}
	
}
