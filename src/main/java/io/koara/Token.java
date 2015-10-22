package io.koara;

public class Token {

	public int kind;
	public int beginLine;
	public int beginColumn;
	public int endLine;
	public int endColumn;
	public String image;
	public Token next;
	public Token specialToken;

	public Token() {
	}

	public Token(int kind, int beginLine, int beginColumn, int endLine, int endColumn, String image) {
		this.kind = kind;
		this.beginLine = beginLine;
		this.beginColumn = beginColumn;
		this.endLine = endLine;
		this.endColumn = endColumn;
		this.image = image;
	}

}