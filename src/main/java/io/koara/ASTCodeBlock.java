/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package io.koara;

public
class ASTCodeBlock extends ASTBlockElement {
 	
  private String language;
	
  public ASTCodeBlock(int id) {
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
