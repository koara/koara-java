package io.koara.ast;

import io.koara.renderer.Renderer;

public class Heading extends BlockElement {

	public void accept(Renderer renderer) {
		renderer.visit(this);
	}
	
}
