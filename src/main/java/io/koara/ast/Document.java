package io.koara.ast;

import io.koara.KoaraVisitor;
import io.koara.SimpleNode;

public class Document extends SimpleNode {
  public Document(int id) {
    super(id);
  }


  public Object accept(KoaraVisitor visitor) {

    return
    visitor.visit(this, null);
  }
}
