package io.koara.ast;

import io.koara.KoaraVisitor;

public
class Blockquote extends BlockElement {
  
  public Object jjtAccept(KoaraVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
