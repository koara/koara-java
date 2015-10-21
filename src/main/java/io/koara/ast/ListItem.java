package io.koara.ast;

import io.koara.KoaraVisitor;
import io.koara.Node;

public
class ListItem extends Node {

  private Integer number;

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
