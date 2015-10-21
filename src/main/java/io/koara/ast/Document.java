package io.koara.ast;

import io.koara.KoaraVisitor;
import io.koara.Node;

public class Document extends Node {
  

  public Object accept(KoaraVisitor visitor) {

    return
    visitor.visit(this, null);
  }
}
