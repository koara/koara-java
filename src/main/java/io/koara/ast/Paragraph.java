package io.koara.ast;

import io.koara.KoaraVisitor;

public
class Paragraph extends BlockElement {
  public Paragraph(int id) {
    super(id);
  }

  public Object jjtAccept(KoaraVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
