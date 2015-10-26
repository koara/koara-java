package io.koara;

import java.util.ArrayList;
import java.util.List;

import io.koara.ast.Node;

public class TreeState {

	private List<Node> nodes;
	private List<Integer> marks;
	private int nodesOnStack;
	private int currentMark;

	public TreeState() {
		nodes = new ArrayList<Node>();
		marks = new ArrayList<Integer>();
		nodesOnStack = 0;
		currentMark = 0;
	}

	private void pushNode(Node n) {
		nodes.add(n);
		++nodesOnStack;
	}

	protected Node popNode() {
		if (--nodesOnStack < currentMark) {
			currentMark = marks.remove(marks.size() - 1);
		}
		return nodes.remove(nodes.size() - 1);
	}

	protected void clearNodeScope(Node n) {
		while (nodesOnStack > currentMark) {
			popNode();
		}
		currentMark = marks.remove(marks.size() - 1);
	}

	protected void openNodeScope(Node n) {
		marks.add(currentMark);
		currentMark = nodesOnStack;
		n.jjtOpen();
	}

	protected void closeNodeScope(Node n) {
		int a = nodeArity();
		currentMark = marks.remove(marks.size() - 1);
		while (a-- > 0) {
			Node c = popNode();
			c.jjtSetParent(n);
			n.jjtAddChild(c, a);
		}
		n.jjtClose();
		pushNode(n);
	}

	private int nodeArity() {
		return nodesOnStack - currentMark;
	}
	
}