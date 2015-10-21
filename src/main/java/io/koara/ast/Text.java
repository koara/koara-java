package io.koara.ast;

import io.koara.KoaraVisitor;
import io.koara.SimpleNode;

public
class Text extends SimpleNode {
  public Text(int id) {
    super(id);
  }

  public Object jjtAccept(KoaraVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
