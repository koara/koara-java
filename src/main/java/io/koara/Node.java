package io.koara;

public
interface Node {

  public void jjtOpen();

  public void jjtClose();

  public void jjtSetParent(Node n);
  public Node jjtGetParent();

  public void jjtAddChild(Node n, int i);

  public Node jjtGetChild(int i);

  public int jjtGetNumChildren();

  public int getId();

  public Object jjtAccept(KoaraVisitor visitor, Object data);
}
