package io.koara;

public interface KoaraTreeConstants
{
  public int JJTDOCUMENT = 0;
  public int JJTVOID = 1;
  public int JJTHEADING = 2;
  public int JJTBLOCKQUOTE = 3;
  public int JJTLIST = 4;
  public int JJTLISTITEM = 5;
  public int JJTCODEBLOCK = 6;
  public int JJTPARAGRAPH = 7;
  public int JJTIMAGE = 8;
  public int JJTLINK = 9;
  public int JJTTEXT = 10;
  public int JJTSTRONG = 11;
  public int JJTEM = 12;
  public int JJTCODE = 13;
  public int JJTLINEBREAK = 14;


  public String[] jjtNodeName = {
    "Document",
    "void",
    "Heading",
    "Blockquote",
    "List",
    "ListItem",
    "CodeBlock",
    "Paragraph",
    "Image",
    "Link",
    "Text",
    "Strong",
    "Em",
    "Code",
    "LineBreak",
  };
}