package io.koara.ast;

import io.koara.renderer.Renderer;

public abstract class Node {

	private Node parent;
	private Node[] children;
	private Object value;

	public void addChild(Node n, int i) {
		if (children == null) {
			children = new Node[i + 1];
		} else if (i >= children.length) {
			Node c[] = new Node[i + 1];
			System.arraycopy(children, 0, c, 0, children.length);
			children = c;
		}
		children[i] = n;
	}

	public void childrenAccept(Renderer renderer) {
		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				children[i].accept(renderer);
			}
		}
	}
	
	public abstract void accept(Renderer renderer);

	public Node[] getChildren() {
		return children;
	}
	
	public void setChildren(Node[] children) {
		this.children = children;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
}