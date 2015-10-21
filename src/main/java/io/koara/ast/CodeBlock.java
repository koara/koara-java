/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
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
