package io.koara.ast;

import io.koara.renderer.HtmlRenderer;
import io.koara.renderer.Renderer;

public
class CodeBlock extends BlockElement {
 	
  private String language;

  public void jjtAccept(HtmlRenderer renderer) {
     renderer.visit(this);
  }
  
  public String getLanguage() {
	return language;
}
  
  public void setLanguage(String language) {
	this.language = language;
}

@Override
public void accept(Renderer renderer) {
	renderer.visit(this);
}
  
}
