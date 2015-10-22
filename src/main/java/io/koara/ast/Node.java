package io.koara.ast;

import io.koara.renderer.Renderer;

public abstract class Node {

	public Node parent;
	public Node[] children;
	protected int id;
	public Object value;

	public void jjtOpen() {
	}

	public void jjtClose() {
	}

	public void jjtSetParent(Node n) {
		parent = n;
	}

	public Node jjtGetParent() {
		return parent;
	}

	public void jjtAddChild(Node n, int i) {
		if (children == null) {
			children = new Node[i + 1];
		} else if (i >= children.length) {
			Node c[] = new Node[i + 1];
			System.arraycopy(children, 0, c, 0, children.length);
			children = c;
		}
		children[i] = n;
	}

	public Node jjtGetChild(int i) {
		return children[i];
	}

	public int jjtGetNumChildren() {
		return (children == null) ? 0 : children.length;
	}

	public void jjtSetValue(Object value) {
		this.value = value;
	}

	public Object jjtGetValue() {
		return value;
	}

	public abstract void accept(Renderer renderer);

	public void childrenAccept(Renderer renderer) {
		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				children[i].accept(renderer);
			}
		}
	}

	public String toString(String prefix) {
		return prefix + toString();
	}

	public void dump(String prefix) {
		System.out.println(toString(prefix));
		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				Node n = (Node) children[i];
				if (n != null) {
					n.dump(prefix + " ");
				}
			}
		}
	}

	public int getId() {
		return id;
	}
}