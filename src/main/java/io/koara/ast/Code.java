package io.koara.ast;

import io.koara.renderer.Renderer;

public class Code extends Node {

	public void accept(Renderer renderer) {
		renderer.visit(this);
	}

}
