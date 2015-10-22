package io.koara.ast;

import io.koara.renderer.Renderer;

public class List extends BlockElement {

	private boolean ordered;

	public void accept(Renderer renderer) {
		renderer.visit(this);
	}

	public boolean isOrdered() {
		return ordered;
	}

	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

}
