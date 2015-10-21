package io.koara;

public class ASTBlockElement extends SimpleNode {

	public ASTBlockElement(int i) {
		super(i);
	}
	
	public boolean isNested() {
		return !(parent instanceof Document);
	}
	
	public boolean isSingleChild() {
		return ((SimpleNode) this.parent).children.length == 1;
	}

}
