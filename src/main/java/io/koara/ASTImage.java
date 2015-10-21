package io.koara;

public
class ASTImage extends SimpleNode {
  public ASTImage(int id) {
    super(id);
  }

  public ASTImage(Koara p, int id) {
    super(p, id);
  }

  public Object jjtAccept(KoaraVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
}