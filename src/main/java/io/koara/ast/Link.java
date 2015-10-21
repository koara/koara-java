package io.koara.ast;

import io.koara.KoaraVisitor;
import io.koara.SimpleNode;

public
class Link extends SimpleNode {
  public Link(int id) {
    super(id);
  }

  public Object jjtAccept(KoaraVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
