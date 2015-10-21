package io.koara.ast;

import io.koara.KoaraVisitor;
import io.koara.SimpleNode;

public
class Code extends SimpleNode {
  public Code(int id) {
    super(id);
  }

  public Object jjtAccept(KoaraVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
