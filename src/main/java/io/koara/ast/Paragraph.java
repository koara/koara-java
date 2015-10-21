package io.koara.ast;

import io.koara.KoaraVisitor;

public
class Paragraph extends BlockElement {
  
  public Object jjtAccept(KoaraVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
