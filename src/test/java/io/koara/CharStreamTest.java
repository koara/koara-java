package io.koara;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharStreamTest {

	private CharStream cs;
	
	@Before
	public void setUp() {
		cs = new CharStream(new StringReader("abcd"));
	}
	
	@Test
	public void testBeginToken() throws Exception {
		assertEquals('a', cs.beginToken());
		assertEquals(1, cs.getBeginColumn());
		assertEquals(1, cs.getBeginLine());
		assertEquals(1, cs.getEndColumn());
		assertEquals(1, cs.getEndColumn());
	}
	
	@Test
	public void testReadChar() throws Exception {
		assertEquals('a', cs.readChar());
		assertEquals('b', cs.readChar());
		assertEquals('c', cs.readChar());
		assertEquals('d', cs.readChar());
	}
	
	@Test(expected=IOException.class)
	public void testReadCharTillEof() throws Exception {
		cs.readChar();
		cs.readChar();
		cs.readChar();
		cs.readChar();
		cs.readChar();
	}
	
	@Test
	public void testGetImage() throws Exception {
		cs.readChar();
		cs.readChar();
		assertEquals("ab", cs.getImage());
	}
	
}
