package io.koara;

import static io.koara.TokenManager.ASTERISK;
import static io.koara.TokenManager.BACKSLASH;
import static io.koara.TokenManager.BACKTICK;
import static io.koara.TokenManager.CHAR_SEQUENCE;
import static io.koara.TokenManager.COLON;
import static io.koara.TokenManager.DASH;
import static io.koara.TokenManager.DIGITS;
import static io.koara.TokenManager.DOT;
import static io.koara.TokenManager.EOF;
import static io.koara.TokenManager.EOL;
import static io.koara.TokenManager.EQ;
import static io.koara.TokenManager.ESCAPED_CHAR;
import static io.koara.TokenManager.GT;
import static io.koara.TokenManager.IMAGE_LABEL;
import static io.koara.TokenManager.LBRACK;
import static io.koara.TokenManager.LPAREN;
import static io.koara.TokenManager.LT;
import static io.koara.TokenManager.RBRACK;
import static io.koara.TokenManager.RPAREN;
import static io.koara.TokenManager.SPACE;
import static io.koara.TokenManager.TAB;
import static io.koara.TokenManager.UNDERSCORE;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import io.koara.ast.Blockquote;
import io.koara.ast.Code;
import io.koara.ast.CodeBlock;
import io.koara.ast.Document;
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

public class Parser {
    
	private TreeState jjtree = new TreeState();
    private int currentBlockLevel = 0;
    private int currentQuoteLevel = 0;
    private JJCalls[] jj_2_rtns = new JJCalls[77];
    private boolean jj_rescan = false;
    private int jj_gc = 0;
    private TokenManager tm;
    private CharStream cs;
    private Token token;
    private Token jj_nt;
    private int jj_ntk;
    private Token jj_scanpos, jj_lastpos;
    private int jj_la;
    private boolean jj_lookingAhead = false;
    private boolean jj_semLA;
    private int jj_gen;
    private int[] jj_la1 = new int[46];
    
    private java.util.List<int[]> jj_expentries = new ArrayList<int[]>();
    private int[] jj_expentry;
    private int[] jj_lasttokens = new int[100];
    private int jj_endpos;

    private class LookaheadSuccess extends Error { }
    private LookaheadSuccess jj_ls = new LookaheadSuccess();
    
	public Document parse(String text) {
	    cs = new CharStream(new StringReader(text));
	    tm = new TokenManager(cs);
	    token = new Token();
	    jj_ntk = -1;
	    jj_gen = 0;
	    for (int i = 0; i < 46; i++) {
	    	jj_la1[i] = -1;
	    }
	    for (int i = 0; i < jj_2_rtns.length; i++) {
	    	jj_2_rtns[i] = new JJCalls();
	    }
		return document();
	}
	
	private Document document() {
		Document document = new Document();
		boolean jjtc000 = true;
		jjtree.openNodeScope(document);
    try {
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case EOL:{
          ;
          break;
          }
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        jj_consume_token(EOL);
      }
      whiteSpace();
      if (jj_2_1(1)) {
        blockElement();
        label_2:
        while (true) {
          if (blockAhead()) {
            ;
          } else {
            break label_2;
          }
          label_3:
          while (true) {
            jj_consume_token(EOL);
            whiteSpace();
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case EOL:{
              ;
              break;
              }
            default:
              jj_la1[1] = jj_gen;
              break label_3;
            }
          }
          blockElement();
        }
        label_4:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case EOL:{
            ;
            break;
            }
          default:
            jj_la1[2] = jj_gen;
            break label_4;
          }
          jj_consume_token(EOL);
        }
        whiteSpace();
      } else {
        ;
      }
      jj_consume_token(0);
jjtree.closeNodeScope(document, true);
    jjtc000 = false;
{if ("" != null) return document;}
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(document);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(document, true);
    }
    }
    throw new Error("Missing return statement in function");
  }

  private void blockElement() {currentBlockLevel++;
    if (headingAhead(1)) {
      heading();
    } else {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case GT:{
        blockquote();
        break;
        }
      case DASH:{
        unorderedList();
        break;
        }
      default:
        jj_la1[3] = jj_gen;
        if (jj_2_2(2)) {
          orderedList();
        } else if (jj_2_3(2147483647)) {
          fencedCodeBlock();
        } else if (jj_2_4(1)) {
          paragraph();
        } else {
          jj_consume_token(-1);
          throw new RuntimeException();
        }
      }
    }
currentBlockLevel--;
  }

  private void heading() {
                           Heading jjtn000 = new Heading();
                           boolean jjtc000 = true;
                           jjtree.openNodeScope(jjtn000);Token t; int heading=0;
    try {
      label_5:
      while (true) {
        jj_consume_token(EQ);
heading++;
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case EQ:{
          ;
          break;
          }
        default:
          jj_la1[4] = jj_gen;
          break label_5;
        }
      }
      whiteSpace();
      label_6:
      while (true) {
        if (jj_2_5(1)) {
          ;
        } else {
          break label_6;
        }
        if (jj_2_6(1)) {
          text();
        } else if (jj_2_7(2147483647)) {
          image();
        } else if (jj_2_8(2147483647)) {
          link();
        } else if (jj_2_9(2147483647)) {
          strong();
        } else if (jj_2_10(2147483647)) {
          em();
        } else if (jj_2_11(2147483647)) {
          code();
        } else {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case ASTERISK:
          case BACKTICK:
          case LBRACK:
          case UNDERSCORE:{
            looseChar();
            break;
            }
          default:
            jj_la1[5] = jj_gen;
            jj_consume_token(-1);
            throw new RuntimeException();
          }
        }
      }
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.jjtSetValue(heading);
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void blockquote() {
                                  Blockquote jjtn000 = new Blockquote();
                                  boolean jjtc000 = true;
                                  jjtree.openNodeScope(jjtn000);currentQuoteLevel++;
    try {
      jj_consume_token(GT);
      label_7:
      while (true) {
        if (jj_2_12(2147483647)) {
          ;
        } else {
          break label_7;
        }
        blockquoteEmptyLine();
      }
      whiteSpace();
      if (jj_2_13(1)) {
        blockElement();
        label_8:
        while (true) {
          if (blockAhead()) {
            ;
          } else {
            break label_8;
          }
          label_9:
          while (true) {
            jj_consume_token(EOL);
            whiteSpace();
            blockquotePrefix();
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case EOL:{
              ;
              break;
              }
            default:
              jj_la1[6] = jj_gen;
              break label_9;
            }
          }
          blockElement();
        }
      } else {
        ;
      }
      label_10:
      while (true) {
        if (jj_2_14(2147483647)) {
          ;
        } else {
          break label_10;
        }
        blockquoteEmptyLine();
      }
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
currentQuoteLevel--;
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void blockquotePrefix() {int i=0;
    label_11:
    while (true) {
      jj_consume_token(GT);
      whiteSpace();
      if (++i < currentQuoteLevel) {
        ;
      } else {
        break label_11;
      }
    }
  }

  private void blockquoteEmptyLine() {
    jj_consume_token(EOL);
    whiteSpace();
    label_12:
    while (true) {
      jj_consume_token(GT);
      whiteSpace();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case GT:{
        ;
        break;
        }
      default:
        jj_la1[7] = jj_gen;
        break label_12;
      }
    }
  }

  private void unorderedList() {
  List jjtn000 = new List();
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      unorderedListItem();
      label_13:
      while (true) {
        if (listItemAhead(false)) {
          ;
        } else {
          break label_13;
        }
        label_14:
        while (true) {
          jj_consume_token(EOL);
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case EOL:{
            ;
            break;
            }
          default:
            jj_la1[8] = jj_gen;
            break label_14;
          }
        }
        whiteSpace();
        unorderedListItem();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void unorderedListItem() {
  ListItem jjtn000 = new ListItem();
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(DASH);
      whiteSpace();
      if (jj_2_15(1)) {
        blockElement();
        label_15:
        while (true) {
          if (blockAhead()) {
            ;
          } else {
            break label_15;
          }
          label_16:
          while (true) {
            jj_consume_token(EOL);
            whiteSpace();
            if (currentQuoteLevel > 0) {
              blockquotePrefix();
            } else {
              ;
            }
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case EOL:{
              ;
              break;
              }
            default:
              jj_la1[9] = jj_gen;
              break label_16;
            }
          }
          blockElement();
        }
      } else {
        ;
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

 private void orderedList() {
  List jjtn000 = new List();
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      orderedListItem();
      label_17:
      while (true) {
        if (listItemAhead(true)) {
          ;
        } else {
          break label_17;
        }
        label_18:
        while (true) {
          jj_consume_token(EOL);
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case EOL:{
            ;
            break;
            }
          default:
            jj_la1[10] = jj_gen;
            break label_18;
          }
        }
        whiteSpace();
        orderedListItem();
      }
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.setOrdered(true);
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void orderedListItem() {
                                    ListItem jjtn000 = new ListItem();
                                    boolean jjtc000 = true;
                                    jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(DIGITS);
      jj_consume_token(DOT);
      whiteSpace();
      if (jj_2_16(1)) {
        blockElement();
        label_19:
        while (true) {
          if (blockAhead()) {
            ;
          } else {
            break label_19;
          }
          label_20:
          while (true) {
            jj_consume_token(EOL);
            whiteSpace();
            if (currentQuoteLevel > 0) {
              blockquotePrefix();
            } else {
              ;
            }
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case EOL:{
              ;
              break;
              }
            default:
              jj_la1[11] = jj_gen;
              break label_20;
            }
          }
          blockElement();
        }
      } else {
        ;
      }
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.setNumber(Integer.valueOf(Integer.valueOf(t.image)));
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

 private void fencedCodeBlock()  {
                                     CodeBlock jjtn000 = new CodeBlock();
                                     boolean jjtc000 = true;
                                     jjtree.openNodeScope(jjtn000);Token t; String language; StringBuilder s = new StringBuilder(); int beginColumn;
    try {
      t = jj_consume_token(BACKTICK);
beginColumn = t.beginColumn;
      jj_consume_token(BACKTICK);
      label_21:
      while (true) {
        jj_consume_token(BACKTICK);
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case BACKTICK:{
          ;
          break;
          }
        default:
          jj_la1[12] = jj_gen;
          break label_21;
        }
      }
      whiteSpace();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case BACKTICK:
      case CHAR_SEQUENCE:{
        language = codeLanguage();
jjtn000.setLanguage(language);
        break;
        }
      default:
        jj_la1[13] = jj_gen;
        ;
      }
      if (getToken(1).kind != EOF && !fencesAhead()) {
        jj_consume_token(EOL);
        levelWhiteSpace(beginColumn);
      } else {
        ;
      }
      label_22:
      while (true) {
        if (jj_2_17(1)) {
          ;
        } else {
          break label_22;
        }
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case ASTERISK:{
          t = jj_consume_token(ASTERISK);
s.append(t.image);
          break;
          }
        case BACKSLASH:{
          t = jj_consume_token(BACKSLASH);
s.append(t.image);
          break;
          }
        case CHAR_SEQUENCE:{
          t = jj_consume_token(CHAR_SEQUENCE);
s.append(t.image);
          break;
          }
        case COLON:{
          t = jj_consume_token(COLON);
s.append(t.image);
          break;
          }
        case DASH:{
          t = jj_consume_token(DASH);
s.append(t.image);
          break;
          }
        case DIGITS:{
          t = jj_consume_token(DIGITS);
s.append(t.image);
          break;
          }
        case DOT:{
          t = jj_consume_token(DOT);
s.append(t.image);
          break;
          }
        case EQ:{
          t = jj_consume_token(EQ);
s.append(t.image);
          break;
          }
        case ESCAPED_CHAR:{
          t = jj_consume_token(ESCAPED_CHAR);
s.append(t.image);
          break;
          }
        case IMAGE_LABEL:{
          t = jj_consume_token(IMAGE_LABEL);
s.append(t.image);
          break;
          }
        case LT:{
          t = jj_consume_token(LT);
s.append(t.image);
          break;
          }
        case GT:{
          t = jj_consume_token(GT);
s.append(t.image);
          break;
          }
        case LBRACK:{
          t = jj_consume_token(LBRACK);
s.append(t.image);
          break;
          }
        case RBRACK:{
          t = jj_consume_token(RBRACK);
s.append(t.image);
          break;
          }
        case LPAREN:{
          t = jj_consume_token(LPAREN);
s.append(t.image);
          break;
          }
        case RPAREN:{
          t = jj_consume_token(RPAREN);
s.append(t.image);
          break;
          }
        case UNDERSCORE:{
          t = jj_consume_token(UNDERSCORE);
s.append(t.image);
          break;
          }
        case BACKTICK:{
          t = jj_consume_token(BACKTICK);
s.append(t.image);
          break;
          }
        default:
          jj_la1[15] = jj_gen;
          if (!nextAfterSpace(EOL, EOF)) {
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case SPACE:{
              t = jj_consume_token(SPACE);
s.append(t.image);
              break;
              }
            case TAB:{
              t = jj_consume_token(TAB);
s.append("    ");
              break;
              }
            default:
              jj_la1[14] = jj_gen;
              jj_consume_token(-1);
              throw new RuntimeException();
            }
          } else if (!fencesAhead()) {
            t = jj_consume_token(EOL);
s.append("\u005cn");
            levelWhiteSpace(beginColumn);
          } else {
            jj_consume_token(-1);
            throw new RuntimeException();
          }
        }
      }
      if (fencesAhead()) {
        jj_consume_token(EOL);
        whiteSpace();
        jj_consume_token(BACKTICK);
        jj_consume_token(BACKTICK);
        label_23:
        while (true) {
          jj_consume_token(BACKTICK);
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case BACKTICK:{
            ;
            break;
            }
          default:
            jj_la1[16] = jj_gen;
            break label_23;
          }
        }
      } else {
        ;
      }
jjtree.closeNodeScope(jjtn000, true);
  jjtc000 = false;
jjtn000.jjtSetValue(s.toString());
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void levelWhiteSpace(int threshold) {Token t; int currentPos=1;
    label_24:
    while (true) {
      if ((getToken(1).kind == SPACE || getToken(1).kind == TAB) && currentPos < (threshold - 1)) {
        ;
      } else {
        break label_24;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case SPACE:{
        t = jj_consume_token(SPACE);
currentPos = t.beginColumn;
        break;
        }
      case TAB:{
        t = jj_consume_token(TAB);
currentPos = t.beginColumn;
        break;
        }
      default:
        jj_la1[17] = jj_gen;
        jj_consume_token(-1);
        throw new RuntimeException();
      }
    }
  }

  private String codeLanguage() {Token t; StringBuilder s = new StringBuilder();
    label_25:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case CHAR_SEQUENCE:{
        t = jj_consume_token(CHAR_SEQUENCE);
s.append(t.image);
        break;
        }
      case BACKTICK:{
        t = jj_consume_token(BACKTICK);
s.append(t.image);
        break;
        }
      default:
        jj_la1[18] = jj_gen;
        jj_consume_token(-1);
        throw new RuntimeException();
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case BACKTICK:
      case CHAR_SEQUENCE:{
        ;
        break;
        }
      default:
        jj_la1[19] = jj_gen;
        break label_25;
      }
    }
{if ("" != null) return s.toString();}
    throw new Error("Missing return statement in function");
  }

  private void paragraph() {
  Paragraph jjtn000 = new Paragraph();
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      inline();
      label_26:
      while (true) {
        if (textAhead()) {
          ;
        } else {
          break label_26;
        }
        lineBreak();
        whiteSpace();
        label_27:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case GT:{
            ;
            break;
            }
          default:
            jj_la1[20] = jj_gen;
            break label_27;
          }
          jj_consume_token(GT);
          whiteSpace();
        }
        inline();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void inline() {
    label_28:
    while (true) {
      if (jj_2_18(1)) {
        text();
      } else if (jj_2_19(2147483647)) {
        image();
      } else if (jj_2_20(2147483647)) {
        link();
      } else if (multilineAhead(ASTERISK)) {
        strongMultiline();
      } else if (multilineAhead(UNDERSCORE)) {
        emMultiline();
      } else if (multilineAhead(BACKTICK)) {
        codeMultiline();
      } else {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case ASTERISK:
        case BACKTICK:
        case LBRACK:
        case UNDERSCORE:{
          looseChar();
          break;
          }
        default:
          jj_la1[21] = jj_gen;
          jj_consume_token(-1);
          throw new RuntimeException();
        }
      }
      if (jj_2_21(1)) {
        ;
      } else {
        break label_28;
      }
    }
  }

  private void image() {
                       Image jjtn000 = new Image();
                       boolean jjtc000 = true;
                       jjtree.openNodeScope(jjtn000);String ref = "";
    try {
      jj_consume_token(LBRACK);
      whiteSpace();
      jj_consume_token(IMAGE_LABEL);
      whiteSpace();
      label_29:
      while (true) {
        if (jj_2_22(1)) {
          resourceText();
        } else {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case ASTERISK:
          case BACKTICK:
          case LBRACK:
          case UNDERSCORE:{
            looseChar();
            break;
            }
          default:
            jj_la1[22] = jj_gen;
            jj_consume_token(-1);
            throw new RuntimeException();
          }
        }
        if (jj_2_23(1)) {
          ;
        } else {
          break label_29;
        }
      }
      whiteSpace();
      jj_consume_token(RBRACK);
      if (jj_2_24(2147483647)) {
        ref = resourceUrl();
      } else {
        ;
      }
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.jjtSetValue(ref);
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void link() {
                     Link jjtn000 = new Link();
                     boolean jjtc000 = true;
                     jjtree.openNodeScope(jjtn000);String ref = "";
    try {
      jj_consume_token(LBRACK);
      whiteSpace();
      label_30:
      while (true) {
        if (jj_2_25(2147483647)) {
          image();
        } else if (jj_2_26(2147483647)) {
          strong();
        } else if (jj_2_27(2147483647)) {
          em();
        } else if (jj_2_28(2147483647)) {
          code();
        } else if (jj_2_29(1)) {
          resourceText();
        } else {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case ASTERISK:
          case BACKTICK:
          case LBRACK:
          case UNDERSCORE:{
            looseChar();
            break;
            }
          default:
            jj_la1[23] = jj_gen;
            jj_consume_token(-1);
            throw new RuntimeException();
          }
        }
        if (jj_2_30(1)) {
          ;
        } else {
          break label_30;
        }
      }
      whiteSpace();
      jj_consume_token(RBRACK);
      if (jj_2_31(2147483647)) {
        ref = resourceUrl();
      } else {
        ;
      }
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.jjtSetValue(ref);
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void resourceText() {
                             Text jjtn000 = new Text();
                             boolean jjtc000 = true;
                             jjtree.openNodeScope(jjtn000);Token t; StringBuilder s = new StringBuilder();
    try {
      label_31:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case BACKSLASH:{
          t = jj_consume_token(BACKSLASH);
s.append(t.image);
          break;
          }
        case COLON:{
          t = jj_consume_token(COLON);
s.append(t.image);
          break;
          }
        case CHAR_SEQUENCE:{
          t = jj_consume_token(CHAR_SEQUENCE);
s.append(t.image);
          break;
          }
        case DASH:{
          t = jj_consume_token(DASH);
s.append(t.image);
          break;
          }
        case DIGITS:{
          t = jj_consume_token(DIGITS);
s.append(t.image);
          break;
          }
        case DOT:{
          t = jj_consume_token(DOT);
s.append(t.image);
          break;
          }
        case EQ:{
          t = jj_consume_token(EQ);
s.append(t.image);
          break;
          }
        case ESCAPED_CHAR:{
          t = jj_consume_token(ESCAPED_CHAR);
s.append(t.image.substring(1));
          break;
          }
        case IMAGE_LABEL:{
          t = jj_consume_token(IMAGE_LABEL);
s.append(t.image);
          break;
          }
        case GT:{
          t = jj_consume_token(GT);
s.append(t.image);
          break;
          }
        case LPAREN:{
          t = jj_consume_token(LPAREN);
s.append(t.image);
          break;
          }
        case LT:{
          t = jj_consume_token(LT);
s.append(t.image);
          break;
          }
        case RPAREN:{
          t = jj_consume_token(RPAREN);
s.append(t.image);
          break;
          }
        default:
          jj_la1[25] = jj_gen;
          if (!nextAfterSpace(RBRACK)) {
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case SPACE:{
              t = jj_consume_token(SPACE);
s.append(t.image);
              break;
              }
            case TAB:{
              t = jj_consume_token(TAB);
s.append("    ");
              break;
              }
            default:
              jj_la1[24] = jj_gen;
              jj_consume_token(-1);
              throw new RuntimeException();
            }
          } else {
            jj_consume_token(-1);
            throw new RuntimeException();
          }
        }
        if (jj_2_32(2)) {
          ;
        } else {
          break label_31;
        }
      }
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.jjtSetValue(s.toString());
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private String resourceUrl() {String ref = "";
    jj_consume_token(LPAREN);
    whiteSpace();
    ref = resourceUrlText();
    whiteSpace();
    jj_consume_token(RPAREN);
{if ("" != null) return ref;}
    throw new Error("Missing return statement in function");
  }

  private String resourceUrlText() {Token t; StringBuilder s = new StringBuilder();
    label_32:
    while (true) {
      if (jj_2_33(1)) {
        ;
      } else {
        break label_32;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ASTERISK:{
        t = jj_consume_token(ASTERISK);
s.append(t.image);
        break;
        }
      case BACKSLASH:{
        t = jj_consume_token(BACKSLASH);
s.append(t.image);
        break;
        }
      case BACKTICK:{
        t = jj_consume_token(BACKTICK);
s.append(t.image);
        break;
        }
      case CHAR_SEQUENCE:{
        t = jj_consume_token(CHAR_SEQUENCE);
s.append(t.image);
        break;
        }
      case COLON:{
        t = jj_consume_token(COLON);
s.append(t.image);
        break;
        }
      case DASH:{
        t = jj_consume_token(DASH);
s.append(t.image);
        break;
        }
      case DIGITS:{
        t = jj_consume_token(DIGITS);
s.append(t.image);
        break;
        }
      case DOT:{
        t = jj_consume_token(DOT);
s.append(t.image);
        break;
        }
      case EQ:{
        t = jj_consume_token(EQ);
s.append(t.image);
        break;
        }
      case ESCAPED_CHAR:{
        t = jj_consume_token(ESCAPED_CHAR);
s.append(t.image.substring(1));
        break;
        }
      case IMAGE_LABEL:{
        t = jj_consume_token(IMAGE_LABEL);
s.append(t.image);
        break;
        }
      case GT:{
        t = jj_consume_token(GT);
s.append(t.image);
        break;
        }
      case LBRACK:{
        t = jj_consume_token(LBRACK);
s.append(t.image);
        break;
        }
      case LPAREN:{
        t = jj_consume_token(LPAREN);
s.append(t.image);
        break;
        }
      case LT:{
        t = jj_consume_token(LT);
s.append(t.image);
        break;
        }
      case RBRACK:{
        t = jj_consume_token(RBRACK);
s.append(t.image);
        break;
        }
      case UNDERSCORE:{
        t = jj_consume_token(UNDERSCORE);
s.append(t.image);
        break;
        }
      default:
        jj_la1[27] = jj_gen;
        if (!nextAfterSpace(RPAREN)) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case SPACE:{
            t = jj_consume_token(SPACE);
s.append(t.image);
            break;
            }
          case TAB:{
            t = jj_consume_token(TAB);
s.append("    ");
            break;
            }
          default:
            jj_la1[26] = jj_gen;
            jj_consume_token(-1);
            throw new RuntimeException();
          }
        } else {
          jj_consume_token(-1);
          throw new RuntimeException();
        }
      }
    }
{if ("" != null) return s.toString();}
    throw new Error("Missing return statement in function");
  }

  private void strongMultiline() {
  Strong jjtn000 = new Strong();
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(ASTERISK);
      strongMultilineContent();
      label_33:
      while (true) {
        if (textAhead()) {
          ;
        } else {
          break label_33;
        }
        lineBreak();
        strongMultilineContent();
      }
      jj_consume_token(ASTERISK);
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void strongMultilineContent() {Token t;
    label_34:
    while (true) {
      if (jj_2_34(1)) {
        text();
      } else if (jj_2_35(2147483647)) {
        image();
      } else if (jj_2_36(2147483647)) {
        link();
      } else if (jj_2_37(2147483647)) {
        code();
      } else if (jj_2_38(2147483647)) {
        emWithinStrongMultiline();
      } else {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case BACKTICK:{
          t = jj_consume_token(BACKTICK);
Text jjtn001 = new Text();
                                                          boolean jjtc001 = true;
                                                          jjtree.openNodeScope(jjtn001);
          try {
jjtree.closeNodeScope(jjtn001, true);
                                                          jjtc001 = false;
jjtn001.jjtSetValue(t.image);
          } finally {
if (jjtc001) {
                                                            jjtree.closeNodeScope(jjtn001, true);
                                                          }
          }
          break;
          }
        case LBRACK:{
          t = jj_consume_token(LBRACK);
Text jjtn002 = new Text();
                                                          boolean jjtc002 = true;
                                                          jjtree.openNodeScope(jjtn002);
          try {
jjtree.closeNodeScope(jjtn002, true);
                                                          jjtc002 = false;
jjtn002.jjtSetValue(t.image);
          } finally {
if (jjtc002) {
                                                            jjtree.closeNodeScope(jjtn002, true);
                                                          }
          }
          break;
          }
        case UNDERSCORE:{
          t = jj_consume_token(UNDERSCORE);
Text jjtn003 = new Text();
                                                          boolean jjtc003 = true;
                                                          jjtree.openNodeScope(jjtn003);
          try {
jjtree.closeNodeScope(jjtn003, true);
                                                          jjtc003 = false;
jjtn003.jjtSetValue(t.image);
          } finally {
if (jjtc003) {
                                                            jjtree.closeNodeScope(jjtn003, true);
                                                          }
          }
          break;
          }
        default:
          jj_la1[28] = jj_gen;
          jj_consume_token(-1);
          throw new RuntimeException();
        }
      }
      if (jj_2_39(1)) {
        ;
      } else {
        break label_34;
      }
    }
  }

  private void strongWithinEmMultiline() {
  Strong jjtn000 = new Strong();
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(ASTERISK);
      strongWithinEmMultilineContent();
      label_35:
      while (true) {
        if (textAhead()) {
          ;
        } else {
          break label_35;
        }
        lineBreak();
        strongWithinEmMultilineContent();
      }
      jj_consume_token(ASTERISK);
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

 private void strongWithinEmMultilineContent() {Token t;
    label_36:
    while (true) {
      if (jj_2_40(1)) {
        text();
      } else if (jj_2_41(2147483647)) {
        image();
      } else if (jj_2_42(2147483647)) {
        link();
      } else if (jj_2_43(2147483647)) {
        code();
      } else {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case BACKTICK:{
          t = jj_consume_token(BACKTICK);
Text jjtn001 = new Text();
                                                          boolean jjtc001 = true;
                                                          jjtree.openNodeScope(jjtn001);
          try {
jjtree.closeNodeScope(jjtn001, true);
                                                          jjtc001 = false;
jjtn001.jjtSetValue(t.image);
          } finally {
if (jjtc001) {
                                                            jjtree.closeNodeScope(jjtn001, true);
                                                          }
          }
          break;
          }
        case LBRACK:{
          t = jj_consume_token(LBRACK);
Text jjtn002 = new Text();
                                                                  boolean jjtc002 = true;
                                                                  jjtree.openNodeScope(jjtn002);
          try {
jjtree.closeNodeScope(jjtn002, true);
                                                                  jjtc002 = false;
jjtn002.jjtSetValue(t.image);
          } finally {
if (jjtc002) {
                                                                    jjtree.closeNodeScope(jjtn002, true);
                                                                  }
          }
          break;
          }
        case UNDERSCORE:{
          t = jj_consume_token(UNDERSCORE);
Text jjtn003 = new Text();
                                                          boolean jjtc003 = true;
                                                          jjtree.openNodeScope(jjtn003);
          try {
jjtree.closeNodeScope(jjtn003, true);
                                                          jjtc003 = false;
jjtn003.jjtSetValue(t.image);
          } finally {
if (jjtc003) {
                                                            jjtree.closeNodeScope(jjtn003, true);
                                                          }
          }
          break;
          }
        default:
          jj_la1[29] = jj_gen;
          jj_consume_token(-1);
          throw new RuntimeException();
        }
      }
      if (jj_2_44(1)) {
        ;
      } else {
        break label_36;
      }
    }
  }

  private void strong() {
                         Strong jjtn000 = new Strong();
                         boolean jjtc000 = true;
                         jjtree.openNodeScope(jjtn000);Token t;
    try {
      jj_consume_token(ASTERISK);
      label_37:
      while (true) {
        if (jj_2_45(1)) {
          text();
        } else if (jj_2_46(2147483647)) {
          image();
        } else if (jj_2_47(2147483647)) {
          link();
        } else if (multilineAhead(BACKTICK)) {
          codeMultiline();
        } else if (jj_2_48(2147483647)) {
          emWithinStrong();
        } else {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case BACKTICK:{
            t = jj_consume_token(BACKTICK);
Text jjtn001 = new Text();
                                                          boolean jjtc001 = true;
                                                          jjtree.openNodeScope(jjtn001);
            try {
jjtree.closeNodeScope(jjtn001, true);
                                                          jjtc001 = false;
jjtn001.jjtSetValue(t.image);
            } finally {
if (jjtc001) {
                                                            jjtree.closeNodeScope(jjtn001, true);
                                                          }
            }
            break;
            }
          case LBRACK:{
            t = jj_consume_token(LBRACK);
Text jjtn002 = new Text();
                                                                  boolean jjtc002 = true;
                                                                  jjtree.openNodeScope(jjtn002);
            try {
jjtree.closeNodeScope(jjtn002, true);
                                                                  jjtc002 = false;
jjtn002.jjtSetValue(t.image);
            } finally {
if (jjtc002) {
                                                                    jjtree.closeNodeScope(jjtn002, true);
                                                                  }
            }
            break;
            }
          case UNDERSCORE:{
            t = jj_consume_token(UNDERSCORE);
Text jjtn003 = new Text();
                                                          boolean jjtc003 = true;
                                                          jjtree.openNodeScope(jjtn003);
            try {
jjtree.closeNodeScope(jjtn003, true);
                                                          jjtc003 = false;
jjtn003.jjtSetValue(t.image);
            } finally {
if (jjtc003) {
                                                            jjtree.closeNodeScope(jjtn003, true);
                                                          }
            }
            break;
            }
          default:
            jj_la1[30] = jj_gen;
            jj_consume_token(-1);
            throw new RuntimeException();
          }
        }
        if (jj_2_49(1)) {
          ;
        } else {
          break label_37;
        }
      }
      jj_consume_token(ASTERISK);
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void strongWithinEm() {
                                 Strong jjtn000 = new Strong();
                                 boolean jjtc000 = true;
                                 jjtree.openNodeScope(jjtn000);Token t;
    try {
      jj_consume_token(ASTERISK);
      label_38:
      while (true) {
        if (jj_2_50(1)) {
          text();
        } else if (jj_2_51(2147483647)) {
          image();
        } else if (jj_2_52(2147483647)) {
          link();
        } else if (jj_2_53(2147483647)) {
          code();
        } else {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case BACKTICK:{
            t = jj_consume_token(BACKTICK);
Text jjtn001 = new Text();
                                                          boolean jjtc001 = true;
                                                          jjtree.openNodeScope(jjtn001);
            try {
jjtree.closeNodeScope(jjtn001, true);
                                                          jjtc001 = false;
jjtn001.jjtSetValue(t.image);
            } finally {
if (jjtc001) {
                                                            jjtree.closeNodeScope(jjtn001, true);
                                                          }
            }
            break;
            }
          case LBRACK:{
            t = jj_consume_token(LBRACK);
Text jjtn002 = new Text();
                                                                  boolean jjtc002 = true;
                                                                  jjtree.openNodeScope(jjtn002);
            try {
jjtree.closeNodeScope(jjtn002, true);
                                                                  jjtc002 = false;
jjtn002.jjtSetValue(t.image);
            } finally {
if (jjtc002) {
                                                                    jjtree.closeNodeScope(jjtn002, true);
                                                                  }
            }
            break;
            }
          case UNDERSCORE:{
            t = jj_consume_token(UNDERSCORE);
Text jjtn003 = new Text();
                                                          boolean jjtc003 = true;
                                                          jjtree.openNodeScope(jjtn003);
            try {
jjtree.closeNodeScope(jjtn003, true);
                                                          jjtc003 = false;
jjtn003.jjtSetValue(t.image);
            } finally {
if (jjtc003) {
                                                            jjtree.closeNodeScope(jjtn003, true);
                                                          }
            }
            break;
            }
          default:
            jj_la1[31] = jj_gen;
            jj_consume_token(-1);
            throw new RuntimeException();
          }
        }
        if (jj_2_54(1)) {
          ;
        } else {
          break label_38;
        }
      }
      jj_consume_token(ASTERISK);
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void emMultiline() {
  Em jjtn000 = new Em();
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(UNDERSCORE);
      emMultilineContent();
      label_39:
      while (true) {
        if (textAhead()) {
          ;
        } else {
          break label_39;
        }
        lineBreak();
        emMultilineContent();
      }
      jj_consume_token(UNDERSCORE);
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  private void emMultilineContent() {Token t;
    label_40:
    while (true) {
      if (jj_2_55(1)) {
        text();
      } else if (jj_2_56(2147483647)) {
        image();
      } else if (jj_2_57(2147483647)) {
        link();
      } else if (multilineAhead(BACKTICK)) {
        codeMultiline();
      } else if (jj_2_58(2147483647)) {
        strongWithinEmMultiline();
      } else {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case ASTERISK:{
          t = jj_consume_token(ASTERISK);
Text jjtn001 = new Text();
                                                          boolean jjtc001 = true;
                                                          jjtree.openNodeScope(jjtn001);
          try {
jjtree.closeNodeScope(jjtn001, true);
                                                          jjtc001 = false;
jjtn001.jjtSetValue(t.image);
          } finally {
if (jjtc001) {
                                                            jjtree.closeNodeScope(jjtn001, true);
                                                          }
          }
          break;
          }
        case BACKTICK:{
          t = jj_consume_token(BACKTICK);
Text jjtn002 = new Text();
                                                          boolean jjtc002 = true;
                                                          jjtree.openNodeScope(jjtn002);
          try {
jjtree.closeNodeScope(jjtn002, true);
                                                          jjtc002 = false;
jjtn002.jjtSetValue(t.image);
          } finally {
if (jjtc002) {
                                                            jjtree.closeNodeScope(jjtn002, true);
                                                          }
          }
          break;
          }
        case LBRACK:{
          t = jj_consume_token(LBRACK);
Text jjtn003 = new Text();
                                                                  boolean jjtc003 = true;
                                                                  jjtree.openNodeScope(jjtn003);
          try {
jjtree.closeNodeScope(jjtn003, true);
                                                                  jjtc003 = false;
jjtn003.jjtSetValue(t.image);
          } finally {
if (jjtc003) {
                                                                    jjtree.closeNodeScope(jjtn003, true);
                                                                  }
          }
          break;
          }
        default:
          jj_la1[32] = jj_gen;
          jj_consume_token(-1);
          throw new RuntimeException();
        }
      }
      if (jj_2_59(1)) {
        ;
      } else {
        break label_40;
      }
    }
  }

  private void emWithinStrongMultiline() {
  Em jjtn000 = new Em();
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(UNDERSCORE);
      emWithinStrongMultilineContent();
      label_41:
      while (true) {
        if (textAhead()) {
          ;
        } else {
          break label_41;
        }
        lineBreak();
        emWithinStrongMultilineContent();
      }
      jj_consume_token(UNDERSCORE);
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  private void emWithinStrongMultilineContent() {Token t;
    label_42:
    while (true) {
      if (jj_2_60(1)) {
        text();
      } else if (jj_2_61(2147483647)) {
        image();
      } else if (jj_2_62(2147483647)) {
        link();
      } else if (jj_2_63(2147483647)) {
        code();
      } else {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case ASTERISK:{
          t = jj_consume_token(ASTERISK);
Text jjtn001 = new Text();
                                                          boolean jjtc001 = true;
                                                          jjtree.openNodeScope(jjtn001);
          try {
jjtree.closeNodeScope(jjtn001, true);
                                                          jjtc001 = false;
jjtn001.jjtSetValue(t.image);
          } finally {
if (jjtc001) {
                                                            jjtree.closeNodeScope(jjtn001, true);
                                                          }
          }
          break;
          }
        case BACKTICK:{
          t = jj_consume_token(BACKTICK);
Text jjtn002 = new Text();
                                                          boolean jjtc002 = true;
                                                          jjtree.openNodeScope(jjtn002);
          try {
jjtree.closeNodeScope(jjtn002, true);
                                                          jjtc002 = false;
jjtn002.jjtSetValue(t.image);
          } finally {
if (jjtc002) {
                                                            jjtree.closeNodeScope(jjtn002, true);
                                                          }
          }
          break;
          }
        case LBRACK:{
          t = jj_consume_token(LBRACK);
Text jjtn003 = new Text();
                                                                  boolean jjtc003 = true;
                                                                  jjtree.openNodeScope(jjtn003);
          try {
jjtree.closeNodeScope(jjtn003, true);
                                                                  jjtc003 = false;
jjtn003.jjtSetValue(t.image);
          } finally {
if (jjtc003) {
                                                                    jjtree.closeNodeScope(jjtn003, true);
                                                                  }
          }
          break;
          }
        default:
          jj_la1[33] = jj_gen;
          jj_consume_token(-1);
          throw new RuntimeException();
        }
      }
      if (jj_2_64(1)) {
        ;
      } else {
        break label_42;
      }
    }
  }

  private void em() {
                 Em jjtn000 = new Em();
                 boolean jjtc000 = true;
                 jjtree.openNodeScope(jjtn000);Token t;
    try {
      jj_consume_token(UNDERSCORE);
      label_43:
      while (true) {
        if (jj_2_65(1)) {
          text();
        } else if (jj_2_66(2147483647)) {
          image();
        } else if (jj_2_67(2147483647)) {
          link();
        } else if (jj_2_68(2147483647)) {
          code();
        } else if (jj_2_69(2147483647)) {
          strongWithinEm();
        } else {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case ASTERISK:{
            t = jj_consume_token(ASTERISK);
Text jjtn001 = new Text();
                                                          boolean jjtc001 = true;
                                                          jjtree.openNodeScope(jjtn001);
            try {
jjtree.closeNodeScope(jjtn001, true);
                                                          jjtc001 = false;
jjtn001.jjtSetValue(t.image);
            } finally {
if (jjtc001) {
                                                            jjtree.closeNodeScope(jjtn001, true);
                                                          }
            }
            break;
            }
          case BACKTICK:{
            t = jj_consume_token(BACKTICK);
Text jjtn002 = new Text();
                                                          boolean jjtc002 = true;
                                                          jjtree.openNodeScope(jjtn002);
            try {
jjtree.closeNodeScope(jjtn002, true);
                                                          jjtc002 = false;
jjtn002.jjtSetValue(t.image);
            } finally {
if (jjtc002) {
                                                            jjtree.closeNodeScope(jjtn002, true);
                                                          }
            }
            break;
            }
          case LBRACK:{
            t = jj_consume_token(LBRACK);
Text jjtn003 = new Text();
                                                                  boolean jjtc003 = true;
                                                                  jjtree.openNodeScope(jjtn003);
            try {
jjtree.closeNodeScope(jjtn003, true);
                                                                  jjtc003 = false;
jjtn003.jjtSetValue(t.image);
            } finally {
if (jjtc003) {
                                                                    jjtree.closeNodeScope(jjtn003, true);
                                                                  }
            }
            break;
            }
          default:
            jj_la1[34] = jj_gen;
            jj_consume_token(-1);
            throw new RuntimeException();
          }
        }
        if (jj_2_70(1)) {
          ;
        } else {
          break label_43;
        }
      }
      jj_consume_token(UNDERSCORE);
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void emWithinStrong() {
                             Em jjtn000 = new Em();
                             boolean jjtc000 = true;
                             jjtree.openNodeScope(jjtn000);Token t;
    try {
      jj_consume_token(UNDERSCORE);
      label_44:
      while (true) {
        if (jj_2_71(1)) {
          text();
        } else if (jj_2_72(2147483647)) {
          image();
        } else if (jj_2_73(2147483647)) {
          link();
        } else if (jj_2_74(2147483647)) {
          code();
        } else {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case ASTERISK:{
            t = jj_consume_token(ASTERISK);
Text jjtn001 = new Text();
                                                          boolean jjtc001 = true;
                                                          jjtree.openNodeScope(jjtn001);
            try {
jjtree.closeNodeScope(jjtn001, true);
                                                          jjtc001 = false;
jjtn001.jjtSetValue(t.image);
            } finally {
if (jjtc001) {
                                                            jjtree.closeNodeScope(jjtn001, true);
                                                          }
            }
            break;
            }
          case BACKTICK:{
            t = jj_consume_token(BACKTICK);
Text jjtn002 = new Text();
                                                          boolean jjtc002 = true;
                                                          jjtree.openNodeScope(jjtn002);
            try {
jjtree.closeNodeScope(jjtn002, true);
                                                          jjtc002 = false;
jjtn002.jjtSetValue(t.image);
            } finally {
if (jjtc002) {
                                                            jjtree.closeNodeScope(jjtn002, true);
                                                          }
            }
            break;
            }
          case LBRACK:{
            t = jj_consume_token(LBRACK);
Text jjtn003 = new Text();
                                                                  boolean jjtc003 = true;
                                                                  jjtree.openNodeScope(jjtn003);
            try {
jjtree.closeNodeScope(jjtn003, true);
                                                                  jjtc003 = false;
jjtn003.jjtSetValue(t.image);
            } finally {
if (jjtc003) {
                                                                    jjtree.closeNodeScope(jjtn003, true);
                                                                  }
            }
            break;
            }
          default:
            jj_la1[35] = jj_gen;
            jj_consume_token(-1);
            throw new RuntimeException();
          }
        }
        if (jj_2_75(1)) {
          ;
        } else {
          break label_44;
        }
      }
      jj_consume_token(UNDERSCORE);
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void codeMultiline() {
  Code jjtn000 = new Code();
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(BACKTICK);
      codeText();
      label_45:
      while (true) {
        if (textAhead()) {
          ;
        } else {
          break label_45;
        }
        lineBreak();
        whiteSpace();
        label_46:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case GT:{
            ;
            break;
            }
          default:
            jj_la1[36] = jj_gen;
            break label_46;
          }
          jj_consume_token(GT);
          whiteSpace();
        }
        codeText();
      }
      jj_consume_token(BACKTICK);
    } catch (Throwable jjte000) {
if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  private void code() {
  Code jjtn000 = new Code();
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(BACKTICK);
      codeText();
      jj_consume_token(BACKTICK);
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  private void codeText() {
                         Text jjtn000 = new Text();
                         boolean jjtc000 = true;
                         jjtree.openNodeScope(jjtn000);Token t; StringBuffer s = new StringBuffer();
    try {
      label_47:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case ASTERISK:{
          t = jj_consume_token(ASTERISK);
s.append(t.image);
          break;
          }
        case BACKSLASH:{
          t = jj_consume_token(BACKSLASH);
s.append(t.image);
          break;
          }
        case CHAR_SEQUENCE:{
          t = jj_consume_token(CHAR_SEQUENCE);
s.append(t.image);
          break;
          }
        case COLON:{
          t = jj_consume_token(COLON);
s.append(t.image);
          break;
          }
        case DASH:{
          t = jj_consume_token(DASH);
s.append(t.image);
          break;
          }
        case DIGITS:{
          t = jj_consume_token(DIGITS);
s.append(t.image);
          break;
          }
        case DOT:{
          t = jj_consume_token(DOT);
s.append(t.image);
          break;
          }
        case EQ:{
          t = jj_consume_token(EQ);
s.append(t.image);
          break;
          }
        case ESCAPED_CHAR:{
          t = jj_consume_token(ESCAPED_CHAR);
s.append(t.image);
          break;
          }
        case IMAGE_LABEL:{
          t = jj_consume_token(IMAGE_LABEL);
s.append(t.image);
          break;
          }
        case LT:{
          t = jj_consume_token(LT);
s.append(t.image);
          break;
          }
        case LBRACK:{
          t = jj_consume_token(LBRACK);
s.append(t.image);
          break;
          }
        case RBRACK:{
          t = jj_consume_token(RBRACK);
s.append(t.image);
          break;
          }
        case LPAREN:{
          t = jj_consume_token(LPAREN);
s.append(t.image);
          break;
          }
        case GT:{
          t = jj_consume_token(GT);
s.append(t.image);
          break;
          }
        case RPAREN:{
          t = jj_consume_token(RPAREN);
s.append(t.image);
          break;
          }
        case UNDERSCORE:{
          t = jj_consume_token(UNDERSCORE);
s.append(t.image);
          break;
          }
        default:
          jj_la1[38] = jj_gen;
          if (!nextAfterSpace(EOL, EOF)) {
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case SPACE:{
              t = jj_consume_token(SPACE);
s.append(t.image);
              break;
              }
            case TAB:{
              t = jj_consume_token(TAB);
s.append("    ");
              break;
              }
            default:
              jj_la1[37] = jj_gen;
              jj_consume_token(-1);
              throw new RuntimeException();
            }
          } else {
            jj_consume_token(-1);
            throw new RuntimeException();
          }
        }
        if (jj_2_76(1)) {
          ;
        } else {
          break label_47;
        }
      }
jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
jjtn000.jjtSetValue(s.toString());
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  private void text() {
                     Text jjtn000 = new Text();
                     boolean jjtc000 = true;
                     jjtree.openNodeScope(jjtn000);Token t; StringBuffer s = new StringBuffer();
    try {
      label_48:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case BACKSLASH:{
          t = jj_consume_token(BACKSLASH);
s.append(t.image);
          break;
          }
        case CHAR_SEQUENCE:{
          t = jj_consume_token(CHAR_SEQUENCE);
s.append(t.image);
          break;
          }
        case COLON:{
          t = jj_consume_token(COLON);
s.append(t.image);
          break;
          }
        case DASH:{
          t = jj_consume_token(DASH);
s.append(t.image);
          break;
          }
        case DIGITS:{
          t = jj_consume_token(DIGITS);
s.append(t.image);
          break;
          }
        case DOT:{
          t = jj_consume_token(DOT);
s.append(t.image);
          break;
          }
        case EQ:{
          t = jj_consume_token(EQ);
s.append(t.image);
          break;
          }
        case ESCAPED_CHAR:{
          t = jj_consume_token(ESCAPED_CHAR);
s.append(t.image.substring(1));
          break;
          }
        case GT:{
          t = jj_consume_token(GT);
s.append(t.image);
          break;
          }
        case IMAGE_LABEL:{
          t = jj_consume_token(IMAGE_LABEL);
s.append(t.image);
          break;
          }
        case LPAREN:{
          t = jj_consume_token(LPAREN);
s.append(t.image);
          break;
          }
        case LT:{
          t = jj_consume_token(LT);
s.append(t.image);
          break;
          }
        case RBRACK:{
          t = jj_consume_token(RBRACK);
s.append(t.image);
          break;
          }
        case RPAREN:{
          t = jj_consume_token(RPAREN);
s.append(t.image);
          break;
          }
        default:
          jj_la1[40] = jj_gen;
          if (!nextAfterSpace(EOL, EOF)) {
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case SPACE:{
              t = jj_consume_token(SPACE);
s.append(t.image);
              break;
              }
            case TAB:{
              t = jj_consume_token(TAB);
s.append("    ");
              break;
              }
            default:
              jj_la1[39] = jj_gen;
              jj_consume_token(-1);
              throw new RuntimeException();
            }
          } else {
            jj_consume_token(-1);
            throw new RuntimeException();
          }
        }
        if (jj_2_77(1)) {
          ;
        } else {
          break label_48;
        }
      }
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.jjtSetValue(s.toString());
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void looseChar() {
                          Text jjtn000 = new Text();
                          boolean jjtc000 = true;
                          jjtree.openNodeScope(jjtn000);Token t;
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ASTERISK:{
        t = jj_consume_token(ASTERISK);
        break;
        }
      case BACKTICK:{
        t = jj_consume_token(BACKTICK);
        break;
        }
      case LBRACK:{
        t = jj_consume_token(LBRACK);
        break;
        }
      case UNDERSCORE:{
        t = jj_consume_token(UNDERSCORE);
        break;
        }
      default:
        jj_la1[41] = jj_gen;
        jj_consume_token(-1);
        throw new RuntimeException();
      }
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.jjtSetValue(t.image);
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void lineBreak() {
  LineBreak jjtn000 = new LineBreak();
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      label_49:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACE:
        case TAB:{
          ;
          break;
          }
        default:
          jj_la1[42] = jj_gen;
          break label_49;
        }
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACE:{
          jj_consume_token(SPACE);
          break;
          }
        case TAB:{
          jj_consume_token(TAB);
          break;
          }
        default:
          jj_la1[43] = jj_gen;
          jj_consume_token(-1);
          throw new RuntimeException();
        }
      }
      jj_consume_token(EOL);
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  private void whiteSpace() {
    label_50:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case SPACE:
      case TAB:{
        ;
        break;
        }
      default:
        jj_la1[44] = jj_gen;
        break label_50;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case SPACE:{
        jj_consume_token(SPACE);
        break;
        }
      case TAB:{
        jj_consume_token(TAB);
        break;
        }
      default:
        jj_la1[45] = jj_gen;
        jj_consume_token(-1);
        throw new RuntimeException();
      }
    }
  }

  private boolean jj_2_1(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_2_3(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  private boolean jj_2_4(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  private boolean jj_2_5(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  private boolean jj_2_6(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  private boolean jj_2_7(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(6, xla); }
  }

  private boolean jj_2_8(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_8(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(7, xla); }
  }

  private boolean jj_2_9(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_9(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(8, xla); }
  }

  private boolean jj_2_10(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_10(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(9, xla); }
  }

  private boolean jj_2_11(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_11(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(10, xla); }
  }

  private boolean jj_2_12(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_12(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(11, xla); }
  }

  private boolean jj_2_13(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_13(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(12, xla); }
  }

  private boolean jj_2_14(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_14(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(13, xla); }
  }

  private boolean jj_2_15(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_15(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(14, xla); }
  }

  private boolean jj_2_16(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_16(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(15, xla); }
  }

  private boolean jj_2_17(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_17(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(16, xla); }
  }

  private boolean jj_2_18(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_18(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(17, xla); }
  }

  private boolean jj_2_19(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_19(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(18, xla); }
  }

  private boolean jj_2_20(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_20(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(19, xla); }
  }

  private boolean jj_2_21(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_21(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(20, xla); }
  }

  private boolean jj_2_22(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_22(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(21, xla); }
  }

  private boolean jj_2_23(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_23(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(22, xla); }
  }

  private boolean jj_2_24(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_24(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(23, xla); }
  }

  private boolean jj_2_25(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_25(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(24, xla); }
  }

  private boolean jj_2_26(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_26(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(25, xla); }
  }

  private boolean jj_2_27(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_27(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(26, xla); }
  }

  private boolean jj_2_28(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_28(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(27, xla); }
  }

  private boolean jj_2_29(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_29(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(28, xla); }
  }

  private boolean jj_2_30(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_30(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(29, xla); }
  }

  private boolean jj_2_31(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_31(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(30, xla); }
  }

  private boolean jj_2_32(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_32(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(31, xla); }
  }

  private boolean jj_2_33(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_33(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(32, xla); }
  }

  private boolean jj_2_34(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_34(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(33, xla); }
  }

  private boolean jj_2_35(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_35(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(34, xla); }
  }

  private boolean jj_2_36(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_36(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(35, xla); }
  }

  private boolean jj_2_37(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_37(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(36, xla); }
  }

  private boolean jj_2_38(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_38(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(37, xla); }
  }

  private boolean jj_2_39(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_39(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(38, xla); }
  }

  private boolean jj_2_40(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_40(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(39, xla); }
  }

  private boolean jj_2_41(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_41(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(40, xla); }
  }

  private boolean jj_2_42(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_42(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(41, xla); }
  }

  private boolean jj_2_43(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_43(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(42, xla); }
  }

  private boolean jj_2_44(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_44(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(43, xla); }
  }

  private boolean jj_2_45(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_45(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(44, xla); }
  }

  private boolean jj_2_46(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_46(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(45, xla); }
  }

  private boolean jj_2_47(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_47(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(46, xla); }
  }

  private boolean jj_2_48(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_48(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(47, xla); }
  }

  private boolean jj_2_49(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_49(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(48, xla); }
  }

  private boolean jj_2_50(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_50(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(49, xla); }
  }

  private boolean jj_2_51(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_51(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(50, xla); }
  }

  private boolean jj_2_52(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_52(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(51, xla); }
  }

  private boolean jj_2_53(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_53(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(52, xla); }
  }

  private boolean jj_2_54(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_54(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(53, xla); }
  }

  private boolean jj_2_55(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_55(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(54, xla); }
  }

  private boolean jj_2_56(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_56(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(55, xla); }
  }

  private boolean jj_2_57(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_57(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(56, xla); }
  }

  private boolean jj_2_58(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_58(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(57, xla); }
  }

  private boolean jj_2_59(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_59(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(58, xla); }
  }

  private boolean jj_2_60(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_60(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(59, xla); }
  }

  private boolean jj_2_61(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_61(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(60, xla); }
  }

  private boolean jj_2_62(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_62(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(61, xla); }
  }

  private boolean jj_2_63(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_63(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(62, xla); }
  }

  private boolean jj_2_64(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_64(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(63, xla); }
  }

  private boolean jj_2_65(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_65(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(64, xla); }
  }

  private boolean jj_2_66(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_66(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(65, xla); }
  }

  private boolean jj_2_67(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_67(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(66, xla); }
  }

  private boolean jj_2_68(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_68(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(67, xla); }
  }

  private boolean jj_2_69(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_69(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(68, xla); }
  }

  private boolean jj_2_70(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_70(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(69, xla); }
  }

  private boolean jj_2_71(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_71(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(70, xla); }
  }

  private boolean jj_2_72(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_72(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(71, xla); }
  }

  private boolean jj_2_73(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_73(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(72, xla); }
  }

  private boolean jj_2_74(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_74(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(73, xla); }
  }

  private boolean jj_2_75(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_75(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(74, xla); }
  }

  private boolean jj_2_76(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_76(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(75, xla); }
  }

  private boolean jj_2_77(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_77(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(76, xla); }
  }

  private boolean jj_3R_254()
 {
    if (jj_scan_token(SPACE)) return true;
    return false;
  }

  private boolean jj_3R_273()
 {
    if (jj_scan_token(GT)) return true;
    if (jj_3R_228()) return true;
    return false;
  }

  private boolean jj_3R_255()
 {
    if (jj_scan_token(TAB)) return true;
    return false;
  }

  private boolean jj_3R_259()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(19)) {
    jj_scanpos = xsp;
    if (jj_scan_token(20)) return true;
    }
    return false;
  }

  private boolean jj_3R_228()
 {
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_259()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_267()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(19)) {
    jj_scanpos = xsp;
    if (jj_scan_token(20)) return true;
    }
    return false;
  }

  private boolean jj_3R_262()
 {
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_267()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(EOL)) return true;
    return false;
  }

  private boolean jj_3R_233()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(1)) {
    jj_scanpos = xsp;
    if (jj_scan_token(3)) {
    jj_scanpos = xsp;
    if (jj_scan_token(14)) {
    jj_scanpos = xsp;
    if (jj_scan_token(21)) return true;
    }
    }
    }
    return false;
  }

  private boolean jj_3R_222()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_254()) {
    jj_scanpos = xsp;
    if (jj_3R_255()) return true;
    }
    return false;
  }

  private boolean jj_3R_221()
 {
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  private boolean jj_3R_220()
 {
    if (jj_scan_token(RBRACK)) return true;
    return false;
  }

  private boolean jj_3R_219()
 {
    if (jj_scan_token(LT)) return true;
    return false;
  }

  private boolean jj_3R_218()
 {
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  private boolean jj_3R_217()
 {
    if (jj_scan_token(IMAGE_LABEL)) return true;
    return false;
  }

  private boolean jj_3R_208()
 {
    if (jj_scan_token(BACKSLASH)) return true;
    return false;
  }

  private boolean jj_3R_216()
 {
    if (jj_scan_token(GT)) return true;
    return false;
  }

  private boolean jj_3R_215()
 {
    if (jj_scan_token(ESCAPED_CHAR)) return true;
    return false;
  }

  private boolean jj_3R_252()
 {
    if (jj_scan_token(SPACE)) return true;
    return false;
  }

  private boolean jj_3R_214()
 {
    if (jj_scan_token(EQ)) return true;
    return false;
  }

  private boolean jj_3R_213()
 {
    if (jj_scan_token(DOT)) return true;
    return false;
  }

  private boolean jj_3R_212()
 {
    if (jj_scan_token(DIGITS)) return true;
    return false;
  }

  private boolean jj_3R_253()
 {
    if (jj_scan_token(TAB)) return true;
    return false;
  }

  private boolean jj_3R_211()
 {
    if (jj_scan_token(DASH)) return true;
    return false;
  }

  private boolean jj_3R_210()
 {
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  private boolean jj_3R_209()
 {
    if (jj_scan_token(CHAR_SEQUENCE)) return true;
    return false;
  }

  private boolean jj_3_77()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_208()) {
    jj_scanpos = xsp;
    if (jj_3R_209()) {
    jj_scanpos = xsp;
    if (jj_3R_210()) {
    jj_scanpos = xsp;
    if (jj_3R_211()) {
    jj_scanpos = xsp;
    if (jj_3R_212()) {
    jj_scanpos = xsp;
    if (jj_3R_213()) {
    jj_scanpos = xsp;
    if (jj_3R_214()) {
    jj_scanpos = xsp;
    if (jj_3R_215()) {
    jj_scanpos = xsp;
    if (jj_3R_216()) {
    jj_scanpos = xsp;
    if (jj_3R_217()) {
    jj_scanpos = xsp;
    if (jj_3R_218()) {
    jj_scanpos = xsp;
    if (jj_3R_219()) {
    jj_scanpos = xsp;
    if (jj_3R_220()) {
    jj_scanpos = xsp;
    if (jj_3R_221()) {
    jj_scanpos = xsp;
    jj_lookingAhead = true;
    jj_semLA = !nextAfterSpace(EOL, EOF);
    jj_lookingAhead = false;
    if (!jj_semLA || jj_3R_222()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3R_61()
 {
    Token xsp;
    if (jj_3_77()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_77()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_207()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_252()) {
    jj_scanpos = xsp;
    if (jj_3R_253()) return true;
    }
    return false;
  }

  private boolean jj_3R_206()
 {
    if (jj_scan_token(UNDERSCORE)) return true;
    return false;
  }

  private boolean jj_3R_201()
 {
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3R_205()
 {
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  private boolean jj_3R_272()
 {
    if (jj_3R_262()) return true;
    if (jj_3R_228()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_273()) { jj_scanpos = xsp; break; }
    }
    if (jj_3R_236()) return true;
    return false;
  }

  private boolean jj_3R_204()
 {
    if (jj_scan_token(GT)) return true;
    return false;
  }

  private boolean jj_3R_203()
 {
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  private boolean jj_3R_202()
 {
    if (jj_scan_token(RBRACK)) return true;
    return false;
  }

  private boolean jj_3R_200()
 {
    if (jj_scan_token(LT)) return true;
    return false;
  }

  private boolean jj_3R_199()
 {
    if (jj_scan_token(IMAGE_LABEL)) return true;
    return false;
  }

  private boolean jj_3R_198()
 {
    if (jj_scan_token(ESCAPED_CHAR)) return true;
    return false;
  }

  private boolean jj_3R_197()
 {
    if (jj_scan_token(EQ)) return true;
    return false;
  }

  private boolean jj_3R_196()
 {
    if (jj_scan_token(DOT)) return true;
    return false;
  }

  private boolean jj_3R_191()
 {
    if (jj_scan_token(BACKSLASH)) return true;
    return false;
  }

  private boolean jj_3R_195()
 {
    if (jj_scan_token(DIGITS)) return true;
    return false;
  }

  private boolean jj_3_76()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_190()) {
    jj_scanpos = xsp;
    if (jj_3R_191()) {
    jj_scanpos = xsp;
    if (jj_3R_192()) {
    jj_scanpos = xsp;
    if (jj_3R_193()) {
    jj_scanpos = xsp;
    if (jj_3R_194()) {
    jj_scanpos = xsp;
    if (jj_3R_195()) {
    jj_scanpos = xsp;
    if (jj_3R_196()) {
    jj_scanpos = xsp;
    if (jj_3R_197()) {
    jj_scanpos = xsp;
    if (jj_3R_198()) {
    jj_scanpos = xsp;
    if (jj_3R_199()) {
    jj_scanpos = xsp;
    if (jj_3R_200()) {
    jj_scanpos = xsp;
    if (jj_3R_201()) {
    jj_scanpos = xsp;
    if (jj_3R_202()) {
    jj_scanpos = xsp;
    if (jj_3R_203()) {
    jj_scanpos = xsp;
    if (jj_3R_204()) {
    jj_scanpos = xsp;
    if (jj_3R_205()) {
    jj_scanpos = xsp;
    if (jj_3R_206()) {
    jj_scanpos = xsp;
    jj_lookingAhead = true;
    jj_semLA = !nextAfterSpace(EOL, EOF);
    jj_lookingAhead = false;
    if (!jj_semLA || jj_3R_207()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3R_190()
 {
    if (jj_scan_token(ASTERISK)) return true;
    return false;
  }

  private boolean jj_3R_194()
 {
    if (jj_scan_token(DASH)) return true;
    return false;
  }

  private boolean jj_3R_193()
 {
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  private boolean jj_3R_192()
 {
    if (jj_scan_token(CHAR_SEQUENCE)) return true;
    return false;
  }

  private boolean jj_3_31()
 {
    if (jj_3R_96()) return true;
    return false;
  }

  private boolean jj_3R_236()
 {
    Token xsp;
    if (jj_3_76()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_76()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_66()
 {
    if (jj_scan_token(BACKTICK)) return true;
    if (jj_3R_236()) return true;
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3R_242()
 {
    if (jj_scan_token(BACKTICK)) return true;
    if (jj_3R_236()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_272()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3_74()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3_73()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_235()
 {
    if (jj_3R_96()) return true;
    return false;
  }

  private boolean jj_3R_249()
 {
    if (jj_3R_262()) return true;
    if (jj_3R_248()) return true;
    return false;
  }

  private boolean jj_3_72()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3R_189()
 {
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3R_188()
 {
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3R_187()
 {
    if (jj_scan_token(ASTERISK)) return true;
    return false;
  }

  private boolean jj_3R_186()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3R_185()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_184()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_75()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_71()) {
    jj_scanpos = xsp;
    if (jj_3R_184()) {
    jj_scanpos = xsp;
    if (jj_3R_185()) {
    jj_scanpos = xsp;
    if (jj_3R_186()) {
    jj_scanpos = xsp;
    if (jj_3R_187()) {
    jj_scanpos = xsp;
    if (jj_3R_188()) {
    jj_scanpos = xsp;
    if (jj_3R_189()) return true;
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_71()
 {
    if (jj_3R_61()) return true;
    return false;
  }

  private boolean jj_3_69()
 {
    if (jj_3R_176()) return true;
    return false;
  }

  private boolean jj_3_68()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3_67()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3_66()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3R_148()
 {
    if (jj_scan_token(UNDERSCORE)) return true;
    Token xsp;
    if (jj_3_75()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_75()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(UNDERSCORE)) return true;
    return false;
  }

  private boolean jj_3R_183()
 {
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3R_182()
 {
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3R_181()
 {
    if (jj_scan_token(ASTERISK)) return true;
    return false;
  }

  private boolean jj_3R_180()
 {
    if (jj_3R_176()) return true;
    return false;
  }

  private boolean jj_3R_179()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3R_178()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_177()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_63()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3_65()
 {
    if (jj_3R_61()) return true;
    return false;
  }

  private boolean jj_3_70()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_65()) {
    jj_scanpos = xsp;
    if (jj_3R_177()) {
    jj_scanpos = xsp;
    if (jj_3R_178()) {
    jj_scanpos = xsp;
    if (jj_3R_179()) {
    jj_scanpos = xsp;
    if (jj_3R_180()) {
    jj_scanpos = xsp;
    if (jj_3R_181()) {
    jj_scanpos = xsp;
    if (jj_3R_182()) {
    jj_scanpos = xsp;
    if (jj_3R_183()) return true;
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_62()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3_61()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3R_65()
 {
    if (jj_scan_token(UNDERSCORE)) return true;
    Token xsp;
    if (jj_3_70()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_70()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(UNDERSCORE)) return true;
    return false;
  }

  private boolean jj_3R_175()
 {
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3R_174()
 {
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3R_173()
 {
    if (jj_scan_token(ASTERISK)) return true;
    return false;
  }

  private boolean jj_3R_172()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3R_101()
 {
    if (jj_3R_233()) return true;
    return false;
  }

  private boolean jj_3R_171()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_170()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_60()
 {
    if (jj_3R_61()) return true;
    return false;
  }

  private boolean jj_3_64()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_60()) {
    jj_scanpos = xsp;
    if (jj_3R_170()) {
    jj_scanpos = xsp;
    if (jj_3R_171()) {
    jj_scanpos = xsp;
    if (jj_3R_172()) {
    jj_scanpos = xsp;
    if (jj_3R_173()) {
    jj_scanpos = xsp;
    if (jj_3R_174()) {
    jj_scanpos = xsp;
    if (jj_3R_175()) return true;
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_58()
 {
    if (jj_3R_162()) return true;
    return false;
  }

  private boolean jj_3_57()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_248()
 {
    Token xsp;
    if (jj_3_64()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_64()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3_56()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3R_134()
 {
    if (jj_scan_token(UNDERSCORE)) return true;
    if (jj_3R_248()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_249()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(UNDERSCORE)) return true;
    return false;
  }

  private boolean jj_3R_169()
 {
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3R_168()
 {
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3R_167()
 {
    if (jj_scan_token(ASTERISK)) return true;
    return false;
  }

  private boolean jj_3R_166()
 {
    if (jj_3R_162()) return true;
    return false;
  }

  private boolean jj_3R_165()
 {
    if (jj_3R_242()) return true;
    return false;
  }

  private boolean jj_3_29()
 {
    if (jj_3R_94()) return true;
    return false;
  }

  private boolean jj_3R_164()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_163()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_55()
 {
    if (jj_3R_61()) return true;
    return false;
  }

  private boolean jj_3_59()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_55()) {
    jj_scanpos = xsp;
    if (jj_3R_163()) {
    jj_scanpos = xsp;
    if (jj_3R_164()) {
    jj_scanpos = xsp;
    jj_lookingAhead = true;
    jj_semLA = multilineAhead(BACKTICK);
    jj_lookingAhead = false;
    if (!jj_semLA || jj_3R_165()) {
    jj_scanpos = xsp;
    if (jj_3R_166()) {
    jj_scanpos = xsp;
    if (jj_3R_167()) {
    jj_scanpos = xsp;
    if (jj_3R_168()) {
    jj_scanpos = xsp;
    if (jj_3R_169()) return true;
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_53()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3_52()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3_51()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3R_241()
 {
    if (jj_scan_token(UNDERSCORE)) return true;
    return false;
  }

  private boolean jj_3R_251()
 {
    if (jj_3R_262()) return true;
    if (jj_3R_250()) return true;
    return false;
  }

  private boolean jj_3R_161()
 {
    if (jj_scan_token(UNDERSCORE)) return true;
    return false;
  }

  private boolean jj_3R_160()
 {
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3R_159()
 {
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3R_158()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3_28()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3R_157()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_156()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_50()
 {
    if (jj_3R_61()) return true;
    return false;
  }

  private boolean jj_3_54()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_50()) {
    jj_scanpos = xsp;
    if (jj_3R_156()) {
    jj_scanpos = xsp;
    if (jj_3R_157()) {
    jj_scanpos = xsp;
    if (jj_3R_158()) {
    jj_scanpos = xsp;
    if (jj_3R_159()) {
    jj_scanpos = xsp;
    if (jj_3R_160()) {
    jj_scanpos = xsp;
    if (jj_3R_161()) return true;
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_48()
 {
    if (jj_3R_148()) return true;
    return false;
  }

  private boolean jj_3_47()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3_46()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_24()
 {
    if (jj_3R_96()) return true;
    return false;
  }

  private boolean jj_3R_176()
 {
    if (jj_scan_token(ASTERISK)) return true;
    Token xsp;
    if (jj_3_54()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_54()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(ASTERISK)) return true;
    return false;
  }

  private boolean jj_3R_100()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3R_155()
 {
    if (jj_scan_token(UNDERSCORE)) return true;
    return false;
  }

  private boolean jj_3R_154()
 {
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3R_153()
 {
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3R_152()
 {
    if (jj_3R_148()) return true;
    return false;
  }

  private boolean jj_3R_151()
 {
    if (jj_3R_242()) return true;
    return false;
  }

  private boolean jj_3R_150()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_149()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_43()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3_45()
 {
    if (jj_3R_61()) return true;
    return false;
  }

  private boolean jj_3_49()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_45()) {
    jj_scanpos = xsp;
    if (jj_3R_149()) {
    jj_scanpos = xsp;
    if (jj_3R_150()) {
    jj_scanpos = xsp;
    jj_lookingAhead = true;
    jj_semLA = multilineAhead(BACKTICK);
    jj_lookingAhead = false;
    if (!jj_semLA || jj_3R_151()) {
    jj_scanpos = xsp;
    if (jj_3R_152()) {
    jj_scanpos = xsp;
    if (jj_3R_153()) {
    jj_scanpos = xsp;
    if (jj_3R_154()) {
    jj_scanpos = xsp;
    if (jj_3R_155()) return true;
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_42()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_234()
 {
    if (jj_3R_96()) return true;
    return false;
  }

  private boolean jj_3_41()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3R_64()
 {
    if (jj_scan_token(ASTERISK)) return true;
    Token xsp;
    if (jj_3_49()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_49()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(ASTERISK)) return true;
    return false;
  }

  private boolean jj_3_27()
 {
    if (jj_3R_65()) return true;
    return false;
  }

  private boolean jj_3R_147()
 {
    if (jj_scan_token(UNDERSCORE)) return true;
    return false;
  }

  private boolean jj_3R_146()
 {
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3R_145()
 {
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3R_144()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3R_143()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_142()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_40()
 {
    if (jj_3R_61()) return true;
    return false;
  }

  private boolean jj_3_44()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_40()) {
    jj_scanpos = xsp;
    if (jj_3R_142()) {
    jj_scanpos = xsp;
    if (jj_3R_143()) {
    jj_scanpos = xsp;
    if (jj_3R_144()) {
    jj_scanpos = xsp;
    if (jj_3R_145()) {
    jj_scanpos = xsp;
    if (jj_3R_146()) {
    jj_scanpos = xsp;
    if (jj_3R_147()) return true;
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3R_99()
 {
    if (jj_3R_65()) return true;
    return false;
  }

  private boolean jj_3_38()
 {
    if (jj_3R_134()) return true;
    return false;
  }

  private boolean jj_3_37()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3_36()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_250()
 {
    Token xsp;
    if (jj_3_44()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_44()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3_35()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3R_162()
 {
    if (jj_scan_token(ASTERISK)) return true;
    if (jj_3R_250()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_251()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(ASTERISK)) return true;
    return false;
  }

  private boolean jj_3R_141()
 {
    if (jj_scan_token(UNDERSCORE)) return true;
    return false;
  }

  private boolean jj_3R_140()
 {
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3R_139()
 {
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3R_246()
 {
    if (jj_scan_token(SPACE)) return true;
    return false;
  }

  private boolean jj_3R_138()
 {
    if (jj_3R_134()) return true;
    return false;
  }

  private boolean jj_3R_137()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3R_136()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_247()
 {
    if (jj_scan_token(TAB)) return true;
    return false;
  }

  private boolean jj_3R_135()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_34()
 {
    if (jj_3R_61()) return true;
    return false;
  }

  private boolean jj_3_39()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_34()) {
    jj_scanpos = xsp;
    if (jj_3R_135()) {
    jj_scanpos = xsp;
    if (jj_3R_136()) {
    jj_scanpos = xsp;
    if (jj_3R_137()) {
    jj_scanpos = xsp;
    if (jj_3R_138()) {
    jj_scanpos = xsp;
    if (jj_3R_139()) {
    jj_scanpos = xsp;
    if (jj_3R_140()) {
    jj_scanpos = xsp;
    if (jj_3R_141()) return true;
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_26()
 {
    if (jj_3R_64()) return true;
    return false;
  }

  private boolean jj_3R_95()
 {
    if (jj_3R_233()) return true;
    return false;
  }

  private boolean jj_3R_240()
 {
    if (jj_scan_token(ASTERISK)) return true;
    return false;
  }

  private boolean jj_3R_130()
 {
    if (jj_scan_token(LT)) return true;
    return false;
  }

  private boolean jj_3R_133()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_246()) {
    jj_scanpos = xsp;
    if (jj_3R_247()) return true;
    }
    return false;
  }

  private boolean jj_3R_132()
 {
    if (jj_scan_token(UNDERSCORE)) return true;
    return false;
  }

  private boolean jj_3R_131()
 {
    if (jj_scan_token(RBRACK)) return true;
    return false;
  }

  private boolean jj_3R_129()
 {
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  private boolean jj_3R_98()
 {
    if (jj_3R_64()) return true;
    return false;
  }

  private boolean jj_3R_128()
 {
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3R_127()
 {
    if (jj_scan_token(GT)) return true;
    return false;
  }

  private boolean jj_3R_126()
 {
    if (jj_scan_token(IMAGE_LABEL)) return true;
    return false;
  }

  private boolean jj_3R_125()
 {
    if (jj_scan_token(ESCAPED_CHAR)) return true;
    return false;
  }

  private boolean jj_3R_116()
 {
    if (jj_scan_token(ASTERISK)) return true;
    return false;
  }

  private boolean jj_3_33()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_116()) {
    jj_scanpos = xsp;
    if (jj_3R_117()) {
    jj_scanpos = xsp;
    if (jj_3R_118()) {
    jj_scanpos = xsp;
    if (jj_3R_119()) {
    jj_scanpos = xsp;
    if (jj_3R_120()) {
    jj_scanpos = xsp;
    if (jj_3R_121()) {
    jj_scanpos = xsp;
    if (jj_3R_122()) {
    jj_scanpos = xsp;
    if (jj_3R_123()) {
    jj_scanpos = xsp;
    if (jj_3R_124()) {
    jj_scanpos = xsp;
    if (jj_3R_125()) {
    jj_scanpos = xsp;
    if (jj_3R_126()) {
    jj_scanpos = xsp;
    if (jj_3R_127()) {
    jj_scanpos = xsp;
    if (jj_3R_128()) {
    jj_scanpos = xsp;
    if (jj_3R_129()) {
    jj_scanpos = xsp;
    if (jj_3R_130()) {
    jj_scanpos = xsp;
    if (jj_3R_131()) {
    jj_scanpos = xsp;
    if (jj_3R_132()) {
    jj_scanpos = xsp;
    jj_lookingAhead = true;
    jj_semLA = !nextAfterSpace(RPAREN);
    jj_lookingAhead = false;
    if (!jj_semLA || jj_3R_133()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3R_124()
 {
    if (jj_scan_token(EQ)) return true;
    return false;
  }

  private boolean jj_3R_123()
 {
    if (jj_scan_token(DOT)) return true;
    return false;
  }

  private boolean jj_3R_122()
 {
    if (jj_scan_token(DIGITS)) return true;
    return false;
  }

  private boolean jj_3R_121()
 {
    if (jj_scan_token(DASH)) return true;
    return false;
  }

  private boolean jj_3R_120()
 {
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  private boolean jj_3_23()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_22()) {
    jj_scanpos = xsp;
    if (jj_3R_95()) return true;
    }
    return false;
  }

  private boolean jj_3_22()
 {
    if (jj_3R_94()) return true;
    return false;
  }

  private boolean jj_3R_119()
 {
    if (jj_scan_token(CHAR_SEQUENCE)) return true;
    return false;
  }

  private boolean jj_3R_118()
 {
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3R_117()
 {
    if (jj_scan_token(BACKSLASH)) return true;
    return false;
  }

  private boolean jj_3R_243()
 {
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_33()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3_25()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3R_245()
 {
    if (jj_scan_token(TAB)) return true;
    return false;
  }

  private boolean jj_3R_96()
 {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_228()) return true;
    if (jj_3R_243()) return true;
    if (jj_3R_228()) return true;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  private boolean jj_3R_244()
 {
    if (jj_scan_token(SPACE)) return true;
    return false;
  }

  private boolean jj_3R_115()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_244()) {
    jj_scanpos = xsp;
    if (jj_3R_245()) return true;
    }
    return false;
  }

  private boolean jj_3R_111()
 {
    if (jj_scan_token(GT)) return true;
    return false;
  }

  private boolean jj_3R_97()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_30()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_97()) {
    jj_scanpos = xsp;
    if (jj_3R_98()) {
    jj_scanpos = xsp;
    if (jj_3R_99()) {
    jj_scanpos = xsp;
    if (jj_3R_100()) {
    jj_scanpos = xsp;
    if (jj_3_29()) {
    jj_scanpos = xsp;
    if (jj_3R_101()) return true;
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3R_114()
 {
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  private boolean jj_3R_110()
 {
    if (jj_scan_token(IMAGE_LABEL)) return true;
    return false;
  }

  private boolean jj_3R_113()
 {
    if (jj_scan_token(LT)) return true;
    return false;
  }

  private boolean jj_3R_112()
 {
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  private boolean jj_3R_109()
 {
    if (jj_scan_token(ESCAPED_CHAR)) return true;
    return false;
  }

  private boolean jj_3R_108()
 {
    if (jj_scan_token(EQ)) return true;
    return false;
  }

  private boolean jj_3R_107()
 {
    if (jj_scan_token(DOT)) return true;
    return false;
  }

  private boolean jj_3R_106()
 {
    if (jj_scan_token(DIGITS)) return true;
    return false;
  }

  private boolean jj_3R_105()
 {
    if (jj_scan_token(DASH)) return true;
    return false;
  }

  private boolean jj_3R_104()
 {
    if (jj_scan_token(CHAR_SEQUENCE)) return true;
    return false;
  }

  private boolean jj_3R_103()
 {
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  private boolean jj_3R_102()
 {
    if (jj_scan_token(BACKSLASH)) return true;
    return false;
  }

  private boolean jj_3_32()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_102()) {
    jj_scanpos = xsp;
    if (jj_3R_103()) {
    jj_scanpos = xsp;
    if (jj_3R_104()) {
    jj_scanpos = xsp;
    if (jj_3R_105()) {
    jj_scanpos = xsp;
    if (jj_3R_106()) {
    jj_scanpos = xsp;
    if (jj_3R_107()) {
    jj_scanpos = xsp;
    if (jj_3R_108()) {
    jj_scanpos = xsp;
    if (jj_3R_109()) {
    jj_scanpos = xsp;
    if (jj_3R_110()) {
    jj_scanpos = xsp;
    if (jj_3R_111()) {
    jj_scanpos = xsp;
    if (jj_3R_112()) {
    jj_scanpos = xsp;
    if (jj_3R_113()) {
    jj_scanpos = xsp;
    if (jj_3R_114()) {
    jj_scanpos = xsp;
    jj_lookingAhead = true;
    jj_semLA = !nextAfterSpace(RBRACK);
    jj_lookingAhead = false;
    if (!jj_semLA || jj_3R_115()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3R_94()
 {
    Token xsp;
    if (jj_3_32()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_32()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_63()
 {
    if (jj_scan_token(LBRACK)) return true;
    if (jj_3R_228()) return true;
    Token xsp;
    if (jj_3_30()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_30()) { jj_scanpos = xsp; break; }
    }
    if (jj_3R_228()) return true;
    if (jj_scan_token(RBRACK)) return true;
    xsp = jj_scanpos;
    if (jj_3R_235()) jj_scanpos = xsp;
    return false;
  }

  private boolean jj_3_20()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3_19()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3R_62()
 {
    if (jj_scan_token(LBRACK)) return true;
    if (jj_3R_228()) return true;
    if (jj_scan_token(IMAGE_LABEL)) return true;
    if (jj_3R_228()) return true;
    Token xsp;
    if (jj_3_23()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_23()) { jj_scanpos = xsp; break; }
    }
    if (jj_3R_228()) return true;
    if (jj_scan_token(RBRACK)) return true;
    xsp = jj_scanpos;
    if (jj_3R_234()) jj_scanpos = xsp;
    return false;
  }

  private boolean jj_3R_93()
 {
    if (jj_3R_233()) return true;
    return false;
  }

  private boolean jj_3R_92()
 {
    if (jj_3R_242()) return true;
    return false;
  }

  private boolean jj_3R_91()
 {
    if (jj_3R_241()) return true;
    return false;
  }

  private boolean jj_3R_90()
 {
    if (jj_3R_240()) return true;
    return false;
  }

  private boolean jj_3R_89()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_88()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_21()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_18()) {
    jj_scanpos = xsp;
    if (jj_3R_88()) {
    jj_scanpos = xsp;
    if (jj_3R_89()) {
    jj_scanpos = xsp;
    jj_lookingAhead = true;
    jj_semLA = multilineAhead(ASTERISK);
    jj_lookingAhead = false;
    if (!jj_semLA || jj_3R_90()) {
    jj_scanpos = xsp;
    jj_lookingAhead = true;
    jj_semLA = multilineAhead(UNDERSCORE);
    jj_lookingAhead = false;
    if (!jj_semLA || jj_3R_91()) {
    jj_scanpos = xsp;
    jj_lookingAhead = true;
    jj_semLA = multilineAhead(BACKTICK);
    jj_lookingAhead = false;
    if (!jj_semLA || jj_3R_92()) {
    jj_scanpos = xsp;
    if (jj_3R_93()) return true;
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_18()
 {
    if (jj_3R_61()) return true;
    return false;
  }

  private boolean jj_3R_232()
 {
    Token xsp;
    if (jj_3_21()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_21()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_54()
 {
    if (jj_3R_232()) return true;
    return false;
  }

  private boolean jj_3R_269()
 {
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3R_268()
 {
    if (jj_scan_token(CHAR_SEQUENCE)) return true;
    return false;
  }

  private boolean jj_3R_238()
 {
    if (jj_scan_token(SPACE)) return true;
    return false;
  }

  private boolean jj_3R_265()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_268()) {
    jj_scanpos = xsp;
    if (jj_3R_269()) return true;
    }
    return false;
  }

  private boolean jj_3R_239()
 {
    if (jj_scan_token(TAB)) return true;
    return false;
  }

  private boolean jj_3R_260()
 {
    Token xsp;
    if (jj_3R_265()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_265()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_271()
 {
    if (jj_scan_token(TAB)) return true;
    return false;
  }

  private boolean jj_3R_270()
 {
    if (jj_scan_token(SPACE)) return true;
    return false;
  }

  private boolean jj_3R_87()
 {
    if (jj_scan_token(EOL)) return true;
    if (jj_3R_261()) return true;
    return false;
  }

  private boolean jj_3R_266()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_270()) {
    jj_scanpos = xsp;
    if (jj_3R_271()) return true;
    }
    return false;
  }

  private boolean jj_3R_261()
 {
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_266()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_86()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_238()) {
    jj_scanpos = xsp;
    if (jj_3R_239()) return true;
    }
    return false;
  }

  private boolean jj_3R_85()
 {
    if (jj_scan_token(BACKTICK)) return true;
    return false;
  }

  private boolean jj_3_16()
 {
    if (jj_3R_51()) return true;
    return false;
  }

  private boolean jj_3R_84()
 {
    if (jj_scan_token(UNDERSCORE)) return true;
    return false;
  }

  private boolean jj_3R_231()
 {
    if (jj_scan_token(EOL)) return true;
    if (jj_3R_228()) return true;
    if (jj_scan_token(BACKTICK)) return true;
    if (jj_scan_token(BACKTICK)) return true;
    Token xsp;
    if (jj_scan_token(3)) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_scan_token(3)) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_83()
 {
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  private boolean jj_3R_82()
 {
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  private boolean jj_3R_81()
 {
    if (jj_scan_token(RBRACK)) return true;
    return false;
  }

  private boolean jj_3R_80()
 {
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3R_79()
 {
    if (jj_scan_token(GT)) return true;
    return false;
  }

  private boolean jj_3R_78()
 {
    if (jj_scan_token(LT)) return true;
    return false;
  }

  private boolean jj_3R_77()
 {
    if (jj_scan_token(IMAGE_LABEL)) return true;
    return false;
  }

  private boolean jj_3R_76()
 {
    if (jj_scan_token(ESCAPED_CHAR)) return true;
    return false;
  }

  private boolean jj_3R_75()
 {
    if (jj_scan_token(EQ)) return true;
    return false;
  }

  private boolean jj_3R_74()
 {
    if (jj_scan_token(DOT)) return true;
    return false;
  }

  private boolean jj_3R_73()
 {
    if (jj_scan_token(DIGITS)) return true;
    return false;
  }

  private boolean jj_3R_72()
 {
    if (jj_scan_token(DASH)) return true;
    return false;
  }

  private boolean jj_3R_71()
 {
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  private boolean jj_3R_70()
 {
    if (jj_scan_token(CHAR_SEQUENCE)) return true;
    return false;
  }

  private boolean jj_3R_69()
 {
    if (jj_scan_token(BACKSLASH)) return true;
    return false;
  }

  private boolean jj_3R_68()
 {
    if (jj_scan_token(ASTERISK)) return true;
    return false;
  }

  private boolean jj_3_17()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_68()) {
    jj_scanpos = xsp;
    if (jj_3R_69()) {
    jj_scanpos = xsp;
    if (jj_3R_70()) {
    jj_scanpos = xsp;
    if (jj_3R_71()) {
    jj_scanpos = xsp;
    if (jj_3R_72()) {
    jj_scanpos = xsp;
    if (jj_3R_73()) {
    jj_scanpos = xsp;
    if (jj_3R_74()) {
    jj_scanpos = xsp;
    if (jj_3R_75()) {
    jj_scanpos = xsp;
    if (jj_3R_76()) {
    jj_scanpos = xsp;
    if (jj_3R_77()) {
    jj_scanpos = xsp;
    if (jj_3R_78()) {
    jj_scanpos = xsp;
    if (jj_3R_79()) {
    jj_scanpos = xsp;
    if (jj_3R_80()) {
    jj_scanpos = xsp;
    if (jj_3R_81()) {
    jj_scanpos = xsp;
    if (jj_3R_82()) {
    jj_scanpos = xsp;
    if (jj_3R_83()) {
    jj_scanpos = xsp;
    if (jj_3R_84()) {
    jj_scanpos = xsp;
    if (jj_3R_85()) {
    jj_scanpos = xsp;
    jj_lookingAhead = true;
    jj_semLA = !nextAfterSpace(EOL, EOF);
    jj_lookingAhead = false;
    if (!jj_semLA || jj_3R_86()) {
    jj_scanpos = xsp;
    jj_lookingAhead = true;
    jj_semLA = !fencesAhead();
    jj_lookingAhead = false;
    if (!jj_semLA || jj_3R_87()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_15()
 {
    if (jj_3R_51()) return true;
    return false;
  }

  private boolean jj_3R_230()
 {
    if (jj_scan_token(EOL)) return true;
    if (jj_3R_261()) return true;
    return false;
  }

  private boolean jj_3R_229()
 {
    if (jj_3R_260()) return true;
    return false;
  }

  private boolean jj_3R_53()
 {
    if (jj_scan_token(BACKTICK)) return true;
    if (jj_scan_token(BACKTICK)) return true;
    Token xsp;
    if (jj_scan_token(3)) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_scan_token(3)) { jj_scanpos = xsp; break; }
    }
    if (jj_3R_228()) return true;
    xsp = jj_scanpos;
    if (jj_3R_229()) jj_scanpos = xsp;
    xsp = jj_scanpos;
    if (jj_3R_230()) jj_scanpos = xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_17()) { jj_scanpos = xsp; break; }
    }
    xsp = jj_scanpos;
    if (jj_3R_231()) jj_scanpos = xsp;
    return false;
  }

  private boolean jj_3R_237()
 {
    if (jj_scan_token(GT)) return true;
    if (jj_3R_228()) return true;
    return false;
  }

  private boolean jj_3R_227()
 {
    if (jj_scan_token(DIGITS)) return true;
    if (jj_scan_token(DOT)) return true;
    return false;
  }

  private boolean jj_3R_52()
 {
    if (jj_3R_227()) return true;
    return false;
  }

  private boolean jj_3R_264()
 {
    if (jj_scan_token(DASH)) return true;
    return false;
  }

  private boolean jj_3R_258()
 {
    if (jj_3R_264()) return true;
    return false;
  }

  private boolean jj_3_12()
 {
    if (jj_3R_67()) return true;
    return false;
  }

  private boolean jj_3_14()
 {
    if (jj_3R_67()) return true;
    if (jj_scan_token(EOL)) return true;
    return false;
  }

  private boolean jj_3R_67()
 {
    if (jj_scan_token(EOL)) return true;
    if (jj_3R_228()) return true;
    Token xsp;
    if (jj_3R_237()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_237()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3_1()
 {
    if (jj_3R_51()) return true;
    return false;
  }

  private boolean jj_3_11()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3_10()
 {
    if (jj_3R_65()) return true;
    return false;
  }

  private boolean jj_3_9()
 {
    if (jj_3R_64()) return true;
    return false;
  }

  private boolean jj_3_8()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3_7()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_13()
 {
    if (jj_3R_51()) return true;
    return false;
  }

  private boolean jj_3R_257()
 {
    if (jj_scan_token(GT)) return true;
    return false;
  }

  private boolean jj_3R_60()
 {
    if (jj_3R_233()) return true;
    return false;
  }

  private boolean jj_3R_59()
 {
    if (jj_3R_66()) return true;
    return false;
  }

  private boolean jj_3R_58()
 {
    if (jj_3R_65()) return true;
    return false;
  }

  private boolean jj_3R_57()
 {
    if (jj_3R_64()) return true;
    return false;
  }

  private boolean jj_3R_56()
 {
    if (jj_3R_63()) return true;
    return false;
  }

  private boolean jj_3R_55()
 {
    if (jj_3R_62()) return true;
    return false;
  }

  private boolean jj_3_3()
 {
    if (jj_3R_53()) return true;
    return false;
  }

  private boolean jj_3_6()
 {
    if (jj_3R_61()) return true;
    return false;
  }

  private boolean jj_3_5()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_6()) {
    jj_scanpos = xsp;
    if (jj_3R_55()) {
    jj_scanpos = xsp;
    if (jj_3R_56()) {
    jj_scanpos = xsp;
    if (jj_3R_57()) {
    jj_scanpos = xsp;
    if (jj_3R_58()) {
    jj_scanpos = xsp;
    if (jj_3R_59()) {
    jj_scanpos = xsp;
    if (jj_3R_60()) return true;
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3R_263()
 {
    if (jj_scan_token(EQ)) return true;
    return false;
  }

  private boolean jj_3R_256()
 {
    Token xsp;
    if (jj_3R_263()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_263()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3_4()
 {
    if (jj_3R_54()) return true;
    return false;
  }

  private boolean jj_3R_226()
 {
    if (jj_3R_53()) return true;
    return false;
  }

  private boolean jj_3_2()
 {
    if (jj_3R_52()) return true;
    return false;
  }

  private boolean jj_3R_225()
 {
    if (jj_3R_258()) return true;
    return false;
  }

  private boolean jj_3R_224()
 {
    if (jj_3R_257()) return true;
    return false;
  }

  private boolean jj_3R_223()
 {
    if (jj_3R_256()) return true;
    return false;
  }

  private boolean jj_3R_51()
 {
    Token xsp;
    xsp = jj_scanpos;
    jj_lookingAhead = true;
    jj_semLA = headingAhead(1);
    jj_lookingAhead = false;
    if (!jj_semLA || jj_3R_223()) {
    jj_scanpos = xsp;
    if (jj_3R_224()) {
    jj_scanpos = xsp;
    if (jj_3R_225()) {
    jj_scanpos = xsp;
    if (jj_3_2()) {
    jj_scanpos = xsp;
    if (jj_3R_226()) {
    jj_scanpos = xsp;
    if (jj_3_4()) return true;
    }
    }
    }
    }
    }
    return false;
  }
 
  private Token jj_consume_token(int kind) {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = tm.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    throw new RuntimeException();
  }

 
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = tm.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }

  private Token getToken(int index) {
    Token t = jj_lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = tm.getNextToken();
    }
    return t;
  }

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=tm.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }



  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }
  
  private boolean blockAhead() {
      if(getToken(1).kind == EOL) {
        Token t;
        int i = 2;
        int eol = 0;
                int quoteLevel;
                do {
                    quoteLevel = 0;
                do {
               t = getToken(i++);
                   if(t.kind == EOL && currentBlockLevel > 0 && ++eol > 2) { return false; }
                   if(t.kind == GT) {
                           if(t.beginColumn == 1 && currentBlockLevel > 0 && currentQuoteLevel == 0)  {
                                   return false;
                           }
                           quoteLevel++;
                    }
                } while (t.kind == GT || t.kind == SPACE || t.kind == TAB);

                if(quoteLevel > currentQuoteLevel) { return true; }
            if(quoteLevel < currentQuoteLevel) { return false; }
        } while(t.kind == EOL);

        return (t.kind != EOF) && ((quoteLevel > 0 && quoteLevel ==  currentQuoteLevel) || (t.beginColumn > ((currentBlockLevel * 4) - 2)));
      }
      return false;
    }

    private boolean fencesAhead() {
            if(getToken(1).kind == EOL) {
              int i = skip(2, SPACE, TAB);
              if(getToken(i).kind == BACKTICK && getToken(i+1).kind == BACKTICK && getToken(i+2).kind == BACKTICK) {
                 i = skip(i+3, SPACE, TAB);
                 return getToken(i).kind == EOL || getToken(i).kind == EOF;
              }
            }
            return false;
    }

    private boolean headingAhead(int offset) {
      if (getToken(offset).kind == EQ) {
        int heading = 1;
        for(int i=(offset + 1);;i++) {
          if(getToken(i).kind != EQ) { return true; }
          if(++heading > 6) { return false;}
        }
      }
      return false;
    }

        private boolean listItemAhead(boolean ordered) {
        if(getToken(1).kind == EOL) {
          for(int i=2, eol=1;;i++) {
          Token t = getToken(i);

          if(t.kind == EOL && ++eol > 2) {
                  return false;
          } else if(t.kind != SPACE && t.kind != TAB && t.kind != EOL){
                  if(currentQuoteLevel > 0) {return false;}
              if(ordered) {
                      return (t.kind == DIGITS && getToken(i+1).kind == DOT);
                  }
                  return (t.kind == DASH);
          }
      }
        }
    return false;
  }

    private boolean multilineAhead(Integer token) {
      if(getToken(1).kind == token && getToken(2).kind != token && getToken(2).kind != EOL) {
        for(int i=2;;i++) {
          Token t = getToken(i);
          if(t.kind == token) {
                        return true;
          } else if(t.kind == EOL) {
                          i = skip(i+1, SPACE, TAB);
                          int quoteLevel = newQuoteLevel(i);
                      if(quoteLevel == currentQuoteLevel) {
                          i = skip(i, SPACE, TAB, GT);
                          if(getToken(i).kind == token
                                          || getToken(i).kind == EOL
                                  || getToken(i).kind == DASH
                                  || (getToken(i).kind == DIGITS && getToken(i+1).kind == DOT)
                                  || (getToken(i).kind == BACKTICK && getToken(i+1).kind == BACKTICK && getToken(i+2).kind == BACKTICK)
                                  || headingAhead(i)) {
                                  return false;
                          }
                      } else {
                          return false;
                      }
          } else if(t.kind == EOF) {
            return false;
          }
        }
      }
      return false;
    }

    private boolean textAhead() {
      int i = skip(1, SPACE, TAB);
      if(getToken(i).kind == EOL && getToken(i+1).kind != EOL && getToken(i+1).kind != EOF) {
        i = skip(i+1, SPACE, TAB);
        int quoteLevel = newQuoteLevel(i);
        if(quoteLevel == currentQuoteLevel) {
            i = skip(i, SPACE, TAB, GT);
            return getToken(i).kind != EOL
                    && getToken(i).kind != DASH
                && !(getToken(i).kind == DIGITS && getToken(i+1).kind == DOT)
                && !(getToken(i).kind == BACKTICK && getToken(i+1).kind == BACKTICK && getToken(i+2).kind == BACKTICK)
                        && !headingAhead(i);
       }
      }
      return false;
    }

    private boolean nextAfterSpace(Integer... tokens) {
      int i = skip(1, SPACE, TAB);
      return Arrays.asList(tokens).contains(getToken(i).kind);
    }

    private int newQuoteLevel(int offset) {
          int quoteLevel = 0;
          for(int i=offset;;i++) {
                  Token t = getToken(i);
                  if(t.kind == GT) {
                          quoteLevel++;
              } else if(t.kind != SPACE && t.kind != TAB) {
                  return quoteLevel;
              }
          }
    }

    private int skip(int offset, Integer... tokens) {
      for(int i=offset;;i++) {
        Token t = getToken(i);
        if(t.kind == EOF || !Arrays.asList(tokens).contains(t.kind)) { return i; }
      }
    }

}
