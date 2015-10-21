package io.koara.ast;

import io.koara.KoaraVisitor;

public
class Blockquote extends BlockElement {
  public Blockquote(int id) {
    super(id);
  }


  public Object jjtAccept(KoaraVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
