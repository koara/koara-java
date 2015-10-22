package io.koara.ast;

import io.koara.Node;
import io.koara.renderer.Renderer;

public
class Strong extends Node {
  

  public void accept(Renderer visitor) {
    visitor.visit(this);
  }
}
