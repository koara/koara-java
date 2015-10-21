package io.koara;

public interface KoaraConstants {

  int EOF = 0;
  int ASTERISK = 1;
  int BACKSLASH = 2;
  int BACKTICK = 3;
  int CHAR_SEQUENCE = 4;
  int COLON = 5;
  int DASH = 6;
  int DIGITS = 7;
  int DOT = 8;
  int EOL = 9;
  int EQ = 10;
  int ESCAPED_CHAR = 11;
  int GT = 12;
  int IMAGE_LABEL = 13;
  int LBRACK = 14;
  int LPAREN = 15;
  int LT = 16;
  int RBRACK = 17;
  int RPAREN = 18;
  int SPACE = 19;
  int TAB = 20;
  int UNDERSCORE = 21;

  int DEFAULT = 0;

  String[] tokenImage = {
    "<EOF>",
    "\"*\"",
    "\"\\\\\"",
    "\"`\"",
    "<CHAR_SEQUENCE>",
    "\":\"",
    "\"-\"",
    "<DIGITS>",
    "\".\"",
    "<EOL>",
    "\"=\"",
    "<ESCAPED_CHAR>",
    "\">\"",
    "\"image:\"",
    "\"[\"",
    "\"(\"",
    "\"<\"",
    "\"]\"",
    "\")\"",
    "\" \"",
    "\"\\t\"",
    "\"_\"",
  };

}
