package io.koara.ast;

import io.koara.KoaraVisitor;

public
class CodeBlock extends BlockElement {
 	
  private String language;
	
  public CodeBlock(int id) {
    super(id);
  }


  public Object jjtAccept(KoaraVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  public String getLanguage() {
	return language;
}
  
  public void setLanguage(String language) {
	this.language = language;
}
  
}
