package org.luaj.vm2.python;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Splits Python source into tokens, producing INDENT / DEDENT / NEWLINE the way
 * CPython does: no newline token inside brackets, blank and comment-only lines
 * ignored, backslash continuation. Tabs are rejected.
 */
final class PyLexer {
	static final Set<String> KEYWORDS = new HashSet<String>(Arrays.asList(
			"False", "None", "True", "and", "as", "assert", "async", "await", "break", "class",
			"continue", "def", "del", "elif", "else", "except", "finally", "for", "from",
			"global", "if", "import", "in", "is", "lambda", "nonlocal", "not", "or", "pass",
			"raise", "return", "try", "while", "with", "yield"));

	private static final String[] OPS3 = { "**=", "//=", ">>=", "<<=", "..." };
	private static final String[] OPS2 = { "**", "//", ">>", "<<", "<=", ">=", "==", "!=", "+=", "-=", "*=",
			"/=", "%=", "&=", "|=", "^=", "->", ":=", "@=" };
	private static final String OPS1 = "+-*/%&|^~<>()[]{},:.;=@";

	private final String src;
	private int pos;
	private int line = 1;
	private int lineStart;
	private final List<Token> tokens = new ArrayList<Token>();

	private PyLexer(String source) {
		this.src = source.replace("\r\n", "\n").replace('\r', '\n');
	}

	static List<Token> tokenize(String source) {
		return new PyLexer(source).run();
	}

	private PySyntaxError error(String message) {
		return new PySyntaxError(message, line, pos - lineStart);
	}

	private void add(int kind, String text, int startLine, int startCol, Object value) {
		tokens.add(new Token(kind, text, startLine, startCol, value));
	}

	private List<Token> run() {
		int n = src.length();
		int[] indents = new int[100];
		int depthIndent = 0;
		int paren = 0;
		boolean lineStartPending = true;
		while (true) {
			if (lineStartPending && paren == 0) {
				// measure the indentation of the next logical line
				int col = 0;
				int p = pos;
				while (p < n && src.charAt(p) == ' ') {
					p++;
					col++;
				}
				if (p < n && (src.charAt(p) == '\t' || src.charAt(p) == '\f')) {
					pos = p;
					throw error("tabs are not allowed; indent with spaces");
				}
				if (p >= n) {
					pos = p;
					break;
				}
				char c = src.charAt(p);
				if (c == '\n') {
					pos = p + 1;
					line++;
					lineStart = pos;
					continue;
				}
				if (c == '#') {
					while (p < n && src.charAt(p) != '\n')
						p++;
					pos = p;
					continue;
				}
				pos = p;
				if (col > indents[depthIndent]) {
					if (depthIndent + 1 >= indents.length)
						throw error("too many levels of indentation");
					indents[++depthIndent] = col;
					add(Token.INDENT, "", line, 0, null);
				} else {
					while (col < indents[depthIndent]) {
						depthIndent--;
						add(Token.DEDENT, "", line, 0, null);
					}
					if (col != indents[depthIndent])
						throw error("unindent does not match any outer indentation level");
				}
				lineStartPending = false;
			}
			if (pos >= n)
				break;
			char c = src.charAt(pos);
			if (c == ' ') {
				pos++;
			} else if (c == '\t' || c == '\f') {
				throw error("tabs are not allowed; indent with spaces");
			} else if (c == '#') {
				while (pos < n && src.charAt(pos) != '\n')
					pos++;
			} else if (c == '\n') {
				if (paren == 0) {
					add(Token.NEWLINE, "", line, pos - lineStart, null);
					lineStartPending = true;
				}
				pos++;
				line++;
				lineStart = pos;
			} else if (c == '\\') {
				if (pos + 1 < n && src.charAt(pos + 1) == '\n') {
					pos += 2;
					line++;
					lineStart = pos;
				} else {
					throw error("unexpected character after line continuation character");
				}
			} else if (isDigit(c) || (c == '.' && pos + 1 < n && isDigit(src.charAt(pos + 1)))) {
				number();
			} else if (isIdentStart(c)) {
				nameOrString();
			} else if (c == '"' || c == '\'') {
				string(pos, false, false);
			} else {
				if ("([{".indexOf(c) >= 0)
					paren++;
				else if (")]}".indexOf(c) >= 0) {
					paren--;
					if (paren < 0)
						throw error("unmatched '" + c + "'");
				}
				operator();
			}
		}
		if (paren > 0)
			throw error("unexpected end of file: unclosed bracket");
		if (!tokens.isEmpty() && tokens.get(tokens.size() - 1).kind != Token.NEWLINE
				&& tokens.get(tokens.size() - 1).kind != Token.DEDENT
				&& tokens.get(tokens.size() - 1).kind != Token.INDENT)
			add(Token.NEWLINE, "", line, pos - lineStart, null);
		while (depthIndent > 0) {
			depthIndent--;
			add(Token.DEDENT, "", line, 0, null);
		}
		add(Token.END, "", line, 0, null);
		return tokens;
	}

	private static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private static boolean isIdentStart(char c) {
		return c == '_' || Character.isLetter(c);
	}

	private static boolean isIdentPart(char c) {
		return c == '_' || Character.isLetterOrDigit(c);
	}

	private void operator() {
		int col = pos - lineStart;
		for (String op : OPS3)
			if (src.startsWith(op, pos)) {
				add(Token.OP, op, line, col, null);
				pos += 3;
				return;
			}
		for (String op : OPS2)
			if (src.startsWith(op, pos)) {
				add(Token.OP, op, line, col, null);
				pos += 2;
				return;
			}
		char c = src.charAt(pos);
		if (OPS1.indexOf(c) >= 0) {
			add(Token.OP, String.valueOf(c), line, col, null);
			pos++;
			return;
		}
		throw error("invalid character '" + c + "'");
	}

	private void nameOrString() {
		int start = pos;
		int col = pos - lineStart;
		while (pos < src.length() && isIdentPart(src.charAt(pos)))
			pos++;
		String word = src.substring(start, pos);
		if (pos < src.length() && (src.charAt(pos) == '"' || src.charAt(pos) == '\'') && word.length() <= 2) {
			String p = word.toLowerCase();
			if (p.equals("r") || p.equals("f") || p.equals("u") || p.equals("rf") || p.equals("fr")) {
				string(start, p.indexOf('r') >= 0, p.indexOf('f') >= 0);
				return;
			}
		}
		if (KEYWORDS.contains(word))
			add(Token.KEYWORD, word, line, col, null);
		else
			add(Token.NAME, word, line, col, null);
	}

	/** Reads a string whose prefix starts at {@code start}; pos is on the opening quote. */
	private void string(int start, boolean raw, boolean fstring) {
		int startLine = line;
		int col = start - lineStart;
		char q = src.charAt(pos);
		boolean triple = src.startsWith(String.valueOf(q) + q + q, pos);
		pos += triple ? 3 : 1;
		int bodyStart = pos;
		int n = src.length();
		while (true) {
			if (pos >= n)
				throw new PySyntaxError("unterminated string literal", startLine, col);
			char c = src.charAt(pos);
			if (c == '\\') {
				if (pos + 1 < n && src.charAt(pos + 1) == '\n') {
					line++;
					lineStart = pos + 2;
				}
				pos += 2;
			} else if (c == '\n') {
				if (!triple)
					throw new PySyntaxError("unterminated string literal", startLine, col);
				pos++;
				line++;
				lineStart = pos;
			} else if (c == q && (!triple || src.startsWith(String.valueOf(q) + q + q, pos))) {
				break;
			} else {
				pos++;
			}
		}
		String body = src.substring(bodyStart, pos);
		pos += triple ? 3 : 1;
		add(Token.STRING, src.substring(start, pos), startLine, col, new Token.StringLit(body, raw, fstring));
	}

	private void number() {
		int start = pos;
		int col = pos - lineStart;
		int n = src.length();
		char c = src.charAt(pos);
		if (c == '0' && pos + 1 < n && "xXoObB".indexOf(src.charAt(pos + 1)) >= 0) {
			char kind = Character.toLowerCase(src.charAt(pos + 1));
			int radix = kind == 'x' ? 16 : kind == 'o' ? 8 : 2;
			pos += 2;
			int digitsStart = pos;
			while (pos < n && (Character.digit(src.charAt(pos), 16) >= 0 || src.charAt(pos) == '_'))
				pos++;
			String digits = checkUnderscores(src.substring(digitsStart, pos));
			if (digits.isEmpty())
				throw error("invalid number literal");
			long v;
			try {
				v = Long.parseLong(digits, radix);
			} catch (NumberFormatException e) {
				throw error("invalid or too large number literal");
			}
			endOfNumber();
			add(Token.NUMBER, src.substring(start, pos), line, col, Long.valueOf(v));
			return;
		}
		boolean isFloat = false;
		while (pos < n && (isDigit(src.charAt(pos)) || src.charAt(pos) == '_'))
			pos++;
		if (pos < n && src.charAt(pos) == '.') {
			isFloat = true;
			pos++;
			while (pos < n && (isDigit(src.charAt(pos)) || src.charAt(pos) == '_'))
				pos++;
		}
		if (pos < n && (src.charAt(pos) == 'e' || src.charAt(pos) == 'E')) {
			int p = pos + 1;
			if (p < n && (src.charAt(p) == '+' || src.charAt(p) == '-'))
				p++;
			if (p < n && isDigit(src.charAt(p))) {
				isFloat = true;
				pos = p;
				while (pos < n && (isDigit(src.charAt(pos)) || src.charAt(pos) == '_'))
					pos++;
			}
		}
		String text = checkUnderscores(src.substring(start, pos));
		endOfNumber();
		if (isFloat) {
			add(Token.NUMBER, src.substring(start, pos), line, col, Double.valueOf(Double.parseDouble(text)));
		} else {
			if (text.length() > 1 && text.charAt(0) == '0' && !text.matches("0+"))
				throw error("leading zeros in decimal integer literals are not permitted");
			long v;
			try {
				v = Long.parseLong(text);
			} catch (NumberFormatException e) {
				throw error("integer literal too large");
			}
			add(Token.NUMBER, src.substring(start, pos), line, col, Long.valueOf(v));
		}
	}

	private String checkUnderscores(String s) {
		if (s.indexOf('_') < 0)
			return s;
		if (s.startsWith("_") || s.endsWith("_") || s.contains("__") || s.contains("_.") || s.contains("._")
				|| s.contains("_e") || s.contains("_E") || s.contains("e_") || s.contains("E_"))
			throw error("invalid decimal literal");
		return s.replace("_", "");
	}

	private void endOfNumber() {
		if (pos < src.length()) {
			char c = src.charAt(pos);
			if (c == 'j' || c == 'J')
				throw error("complex numbers are not supported");
			if (isIdentPart(c))
				throw error("invalid number literal");
		}
	}
}
