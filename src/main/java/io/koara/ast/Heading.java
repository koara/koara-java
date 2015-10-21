package io.koara.ast;

import io.koara.KoaraVisitor;

public
class Heading extends BlockElement {
  public Heading(int id) {
    super(id);
  }



  public Object jjtAccept(KoaraVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
