package io.koara.ast;

import io.koara.KoaraVisitor;

public
class Heading extends BlockElement {

  public Object jjtAccept(KoaraVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
