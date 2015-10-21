package io.koara.ast;

import io.koara.KoaraVisitor;
import io.koara.SimpleNode;

public
class Em extends SimpleNode {
  public Em(int id) {
    super(id);
  }

  public Object jjtAccept(KoaraVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
