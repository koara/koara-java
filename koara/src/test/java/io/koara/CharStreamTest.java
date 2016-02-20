package io.koara;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class CharStreamTest {

	private CharStream cs;
	
	@Test
	public void testBeginToken() throws Exception {
		cs = new CharStream(new StringReader("abcd"));
		assertEquals('a', cs.beginToken());
		assertEquals(1, cs.getBeginColumn());
		assertEquals(1, cs.getBeginLine());
		assertEquals(1, cs.getEndColumn());
		assertEquals(1, cs.getEndColumn());
	}
	
	@Test
	public void testReadChar() throws Exception {
		cs = new CharStream(new StringReader("abcd"));
		assertEquals('a', cs.readChar());
		assertEquals('b', cs.readChar());
		assertEquals('c', cs.readChar());
		assertEquals('d', cs.readChar());
	}
	
	@Test(expected=IOException.class)
	public void testReadCharTillEof() throws Exception {
		cs = new CharStream(new StringReader("abcd"));
		cs.readChar();
		cs.readChar();
		cs.readChar();
		cs.readChar();
		cs.readChar();
	}
	
	@Test
	public void testGetImage() throws Exception {
		cs = new CharStream(new StringReader("abcd"));
		cs.readChar();
		cs.readChar();
		assertEquals("ab", cs.getImage());
	}
	
	@Test
	public void testBeginTokenWithUnicode() throws Exception {
		cs = new CharStream(new StringReader("ðinæ"));
		assertEquals('ð', cs.beginToken());
		assertEquals(1, cs.getBeginColumn());
		assertEquals(1, cs.getBeginLine());
		assertEquals(1, cs.getEndColumn());
		assertEquals(1, cs.getEndColumn());
	}
	
	@Test
	public void testReadCharWithUnicode() throws IOException {
		cs = new CharStream(new StringReader("ðinæ"));
		assertEquals('ð', cs.readChar());
		assertEquals('i', cs.readChar());
		assertEquals('n', cs.readChar());
		assertEquals('æ', cs.readChar());
	}
	
	@Test(expected=IOException.class)
	public void testReadCharTillEofWithUnicode() throws IOException {
		cs = new CharStream(new StringReader("ðinæ"));
		cs.readChar();
		cs.readChar();
		cs.readChar();
		cs.readChar();
		cs.readChar();
	}
	
	@Test
	public void testGetImageWithUnicode() throws Exception {
		cs = new CharStream(new StringReader("ðinæ"));
		cs.readChar();
		cs.readChar();
		assertEquals("ði", cs.getImage());
	}
	
}
