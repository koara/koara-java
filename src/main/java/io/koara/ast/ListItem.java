package io.koara.ast;

import io.koara.KoaraVisitor;
import io.koara.SimpleNode;

public
class ListItem extends SimpleNode {

  private Integer number;
	
  public ListItem(int id) {
    super(id);
  }

  public Integer getNumber() {
	return number;
  }
  
  public void setNumber(Integer number) {
	this.number = number;
  }
  
  public Object jjtAccept(KoaraVisitor visitor, Object data) {
	  return visitor.visit(this, data);
  }
}
