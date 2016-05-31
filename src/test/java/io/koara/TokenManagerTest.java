package io.koara;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

public class TokenManagerTest {

	@Test
	public void testEof() {
		Token token = new TokenManager(new CharStream(new StringReader(""))).getNextToken();
		assertEquals(TokenManager.EOF, token.kind);
	}
	
	@Test
	public void testAsterisk() {
		Token token = new TokenManager(new CharStream(new StringReader("*"))).getNextToken();
		assertEquals(TokenManager.ASTERISK, token.kind);
		assertEquals("*", token.image);
	}
	
	@Test
	public void testBackslash() {
		Token token = new TokenManager(new CharStream(new StringReader("\\"))).getNextToken();
		assertEquals(TokenManager.BACKSLASH, token.kind);
		assertEquals("\\", token.image);
	}
	
	@Test
	public void testBacktick() {
		Token token = new TokenManager(new CharStream(new StringReader("`"))).getNextToken();
		assertEquals(TokenManager.BACKTICK, token.kind);
		assertEquals("`", token.image);
	}
	
	@Test
	public void testCharSequenceLowerCase() {
		Token token = new TokenManager(new CharStream(new StringReader("m"))).getNextToken();
		assertEquals(TokenManager.CHAR_SEQUENCE, token.kind);
		assertEquals("m", token.image);
	}
	
	@Test
	public void testCharSequenceUpperCase() {
		Token token = new TokenManager(new CharStream(new StringReader("C"))).getNextToken();
		assertEquals(TokenManager.CHAR_SEQUENCE, token.kind);
		assertEquals("C", token.image);
	}
	
	@Test
	public void testColon() {
		Token token = new TokenManager(new CharStream(new StringReader(":"))).getNextToken();
		assertEquals(TokenManager.COLON, token.kind);
		assertEquals(":", token.image);
	}
	
	@Test
	public void testDash() {
		Token token = new TokenManager(new CharStream(new StringReader("-"))).getNextToken();
		assertEquals(TokenManager.DASH, token.kind);
		assertEquals("-", token.image);
	}
	
	@Test
	public void testDigits() {
		Token token = new TokenManager(new CharStream(new StringReader("4"))).getNextToken();
		assertEquals(TokenManager.DIGITS, token.kind);
		assertEquals("4", token.image);
	}
	
	@Test
	public void testDot() {
		Token token = new TokenManager(new CharStream(new StringReader("."))).getNextToken();
		assertEquals(TokenManager.DOT, token.kind);
		assertEquals(".", token.image);
	}
	
	@Test
	public void testEol() {
		Token token = new TokenManager(new CharStream(new StringReader("\n"))).getNextToken();
		assertEquals(TokenManager.EOL, token.kind);
		assertEquals("\n", token.image);
	}
	
	@Test
	public void testEq() {
		Token token = new TokenManager(new CharStream(new StringReader("="))).getNextToken();
		assertEquals(TokenManager.EQ, token.kind);
		assertEquals("=", token.image);
	}
	
	@Test
	public void testEscapedChar() {
		Token token = new TokenManager(new CharStream(new StringReader("\\*"))).getNextToken();
		assertEquals(TokenManager.ESCAPED_CHAR, token.kind);
		assertEquals("\\*", token.image);
	}
	
	@Test
	public void testGt() {
		Token token = new TokenManager(new CharStream(new StringReader(">"))).getNextToken();
		assertEquals(TokenManager.GT, token.kind);
		assertEquals(">", token.image);
	}
	
	@Test
	public void testImageLabel() {
		Token token = new TokenManager(new CharStream(new StringReader("image:"))).getNextToken();
		assertEquals(TokenManager.IMAGE_LABEL, token.kind);
		assertEquals("image:", token.image);
	}
	
	@Test
	public void testLbrack() {
		Token token = new TokenManager(new CharStream(new StringReader("["))).getNextToken();
		assertEquals(TokenManager.LBRACK, token.kind);
		assertEquals("[", token.image);
	}
	
	@Test
	public void testLparen() {
		Token token = new TokenManager(new CharStream(new StringReader("("))).getNextToken();
		assertEquals(TokenManager.LPAREN, token.kind);
		assertEquals("(", token.image);
	}
	
	@Test
	public void testLt() {
		Token token = new TokenManager(new CharStream(new StringReader("<"))).getNextToken();
		assertEquals(TokenManager.LT, token.kind);
		assertEquals("<", token.image);
	}
	
	@Test
	public void testRbrack() {
		Token token = new TokenManager(new CharStream(new StringReader("]"))).getNextToken();
		assertEquals(TokenManager.RBRACK, token.kind);
		assertEquals("]", token.image);
	}
	
	@Test
	public void testRparen() {
		Token token = new TokenManager(new CharStream(new StringReader(")"))).getNextToken();
		assertEquals(TokenManager.RPAREN, token.kind);
		assertEquals(")", token.image);
	}
	
	@Test
	public void testSpace() {
		Token token = new TokenManager(new CharStream(new StringReader(" "))).getNextToken();
		assertEquals(TokenManager.SPACE, token.kind);
		assertEquals(" ", token.image);
	}
	
	@Test
	public void testTab() {
		Token token = new TokenManager(new CharStream(new StringReader("\t"))).getNextToken();
		assertEquals(TokenManager.TAB, token.kind);
		assertEquals("\t", token.image);
	}
	
	@Test
	public void testUnderscore() {
		Token token = new TokenManager(new CharStream(new StringReader("_"))).getNextToken();
		assertEquals(TokenManager.UNDERSCORE, token.kind);
		assertEquals("_", token.image);
	}
	
}
