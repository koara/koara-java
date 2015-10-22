package io.koara.ast;

import io.koara.renderer.Renderer;

public class CodeBlock extends BlockElement {

	private String language;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void accept(Renderer renderer) {
		renderer.visit(this);
	}

}
