package io.koara;

public class TreeState {
  private java.util.List<Node> nodes;
  private java.util.List<Integer> marks;

  private int nodesOnStack;
  private int currentMark;
  private boolean node_created;

  public TreeState() {
    nodes = new java.util.ArrayList<Node>();
    marks = new java.util.ArrayList<Integer>();
    nodesOnStack = 0;
    currentMark = 0;
  }

  public boolean nodeCreated() {
    return node_created;
  }

  public Node rootNode() {
    return nodes.get(0);
  }

  public void pushNode(Node n) {
    nodes.add(n);
    ++nodesOnStack;
  }

  public Node popNode() {
    if (--nodesOnStack < currentMark) {
      currentMark = marks.remove(marks.size()-1);
    }
    return nodes.remove(nodes.size()-1);
  }

  public Node peekNode() {
    return nodes.get(nodes.size()-1);
  }

  public int nodeArity() {
    return nodesOnStack - currentMark;
  }

  public void clearNodeScope(Node n) {
    while (nodesOnStack > currentMark) {
      popNode();
    }
    currentMark = marks.remove(marks.size()-1);
  }

  public void openNodeScope(Node n) {
    marks.add(currentMark);
    currentMark = nodesOnStack;
    n.jjtOpen();
  }

  public void closeNodeScope(Node n, int num) {
    currentMark = marks.remove(marks.size()-1);
    while (num-- > 0) {
      Node c = popNode();
      c.jjtSetParent(n);
      n.jjtAddChild(c, num);
    }
    n.jjtClose();
    pushNode(n);
    node_created = true;
  }

  public void closeNodeScope(Node n, boolean condition) {
    if (condition) {
      int a = nodeArity();
      currentMark = marks.remove(marks.size()-1);
      while (a-- > 0) {
        Node c = popNode();
        c.jjtSetParent(n);
        n.jjtAddChild(c, a);
      }
      n.jjtClose();
      pushNode(n);
      node_created = true;
    } else {
      currentMark = marks.remove(marks.size()-1);
      node_created = false;
    }
  }
}