package io.koara.ast;

import io.koara.KoaraVisitor;
import io.koara.Node;

public
class Text extends Node {

  public Object jjtAccept(KoaraVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
