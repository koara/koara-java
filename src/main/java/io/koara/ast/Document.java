package io.koara.ast;

import io.koara.renderer.Renderer;

public class Document extends Node {

	public void accept(Renderer renderer) {
		renderer.visit(this);
	}

}
