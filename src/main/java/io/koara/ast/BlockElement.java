package io.koara.ast;

public abstract class BlockElement extends Node {

	public boolean isNested() {
		return !(getParent() instanceof Document);
	}

	public boolean isSingleChild() {
		return ((Node) this.getParent()).getChildren().length == 1;
	}

}
