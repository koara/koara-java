package io.koara.ast;

import io.koara.KoaraVisitor;

public class List extends BlockElement {

	private boolean ordered;

	public Object jjtAccept(KoaraVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public boolean isOrdered() {
		return ordered;
	}

	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

}
