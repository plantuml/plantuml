package org.luaj.vm2.python;

/** A lexical token of the Python subset. */
final class Token {
	static final int NAME = 1;
	static final int KEYWORD = 2;
	static final int NUMBER = 3;
	static final int STRING = 4;
	static final int OP = 5;
	static final int NEWLINE = 6;
	static final int INDENT = 7;
	static final int DEDENT = 8;
	static final int END = 9;

	final int kind;
	/** The name, keyword or operator; the source text for the other kinds. */
	final String text;
	final int line;
	final int col;
	/** NUMBER: a Long or a Double. STRING: a {@link StringLit}. */
	final Object value;

	Token(int kind, String text, int line, int col, Object value) {
		this.kind = kind;
		this.text = text;
		this.line = line;
		this.col = col;
		this.value = value;
	}

	boolean isOp(String op) {
		return kind == OP && text.equals(op);
	}

	boolean isKeyword(String kw) {
		return kind == KEYWORD && text.equals(kw);
	}

	@Override
	public String toString() {
		switch (kind) {
		case NEWLINE:
			return "NEWLINE";
		case INDENT:
			return "INDENT";
		case DEDENT:
			return "DEDENT";
		case END:
			return "END";
		default:
			return text;
		}
	}

	/** A string literal: its source body and prefix flags. */
	static final class StringLit {
		final String body;
		final boolean raw;
		final boolean fstring;

		StringLit(String body, boolean raw, boolean fstring) {
			this.body = body;
			this.raw = raw;
			this.fstring = fstring;
		}
	}
}
