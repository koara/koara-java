package io.koara.ast;

import io.koara.renderer.Renderer;

public
class Blockquote extends BlockElement {
  
  public void accept(Renderer renderer) {
     renderer.visit(this);
  }
}
