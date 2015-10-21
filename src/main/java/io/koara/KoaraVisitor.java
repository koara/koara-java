package io.koara;

import io.koara.ast.Blockquote;
import io.koara.ast.Code;
import io.koara.ast.CodeBlock;
import io.koara.ast.Em;
import io.koara.ast.Heading;
import io.koara.ast.Image;
import io.koara.ast.LineBreak;
import io.koara.ast.Link;
import io.koara.ast.List;
import io.koara.ast.ListItem;
import io.koara.ast.Paragraph;
import io.koara.ast.Strong;
import io.koara.ast.Text;
import io.koara.ast.Document;

public interface KoaraVisitor
{
  public Object visit(Node node, Object data);
  public Object visit(Document node, Object data);
  public Object visit(Heading node, Object data);
  public Object visit(Blockquote node, Object data);
  public Object visit(List node, Object data);
  public Object visit(ListItem node, Object data);
  public Object visit(CodeBlock node, Object data);
  public Object visit(Paragraph node, Object data);
  public Object visit(Image node, Object data);
  public Object visit(Link node, Object data);
  public Object visit(Text node, Object data);
  public Object visit(Strong node, Object data);
  public Object visit(Em node, Object data);
  public Object visit(Code node, Object data);
  public Object visit(LineBreak node, Object data);
}