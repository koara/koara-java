package io.koara.ast;

public abstract class BlockElement extends Node {

	public boolean isNested() {
		return !(parent instanceof Document);
	}

	public boolean isSingleChild() {
		return ((Node) this.parent).children.length == 1;
	}

}
