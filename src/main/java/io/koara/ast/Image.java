package io.koara.ast;

import io.koara.KoaraVisitor;
import io.koara.Parser;
import io.koara.SimpleNode;

public
class Image extends SimpleNode {
  public Image(int id) {
    super(id);
  }

  public Image(Parser p, int id) {
    super(p, id);
  }

  public Object jjtAccept(KoaraVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
}