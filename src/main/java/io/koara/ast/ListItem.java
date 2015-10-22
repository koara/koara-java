package io.koara.ast;

import io.koara.renderer.Renderer;

public class ListItem extends Node {

	private Integer number;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public void accept(Renderer renderer) {
		renderer.visit(this);
	}
	
}
