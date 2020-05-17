package net.sourceforge.plantuml.creole.rosetta;

public enum WikiLanguage {
	DOKUWIKI, MARKDOWN, ASCIIDOC, MEDIAWIKI, CREOLE, UNICODE, HTML_DEBUG;

	public String toString() {
		return super.toString().toLowerCase();
	}

	static class MyChar {

		final String unicode;
		final String htmlDebug;

		MyChar(String unicode, String htmlDebug) {
			this.unicode = unicode;
			this.htmlDebug = htmlDebug;
		}

		String toRightSyntax(WikiLanguage lg) {
			if (lg == WikiLanguage.UNICODE) {
				return "" + unicode;
			} else if (lg == WikiLanguage.HTML_DEBUG) {
				return "" + htmlDebug;
			}
			throw new UnsupportedOperationException();
		}

		public String toHtmlDebug(String s) {
			return s.replace(unicode, htmlDebug);
		}
	}

	static public String toHtmlDebug(String s) {
		s = START.toHtmlDebug(s);
		s = END.toHtmlDebug(s);
		s = SEMICOLON.toHtmlDebug(s);
		s = EQUALS.toHtmlDebug(s);
		s = EOB.toHtmlDebug(s);
		return s;
	}

	private static final MyChar START = new MyChar("\uF800", "<<{{");
	private static final MyChar END = new MyChar("\uF802", "<</{{");
	private static final MyChar SEMICOLON = new MyChar("\uF810", ";;;");
	private static final MyChar EQUALS = new MyChar("\uF811", "===");
	private static final MyChar EOB = new MyChar("\uF899", "}}>>");

	public static String hideCharsF7(String s) {
		final StringBuilder sb = new StringBuilder(s.length());
		for (char ch : s.toCharArray()) {
			sb.append(toF7(ch));
		}
		return sb.toString();
	}

	public static String restoreAllCharsF7AndDoEscapeForBackSlash(String s) {
		return restoreAllCharsF7(s);
	}

	public static String restoreAllCharsF7(String s) {
		final StringBuilder sb = new StringBuilder(s.length());
		for (char ch : s.toCharArray()) {
			if (ch >= '\uF700' && ch <= '\uF7FF') {
				ch = (char) (ch - '\uF700');
			}
			sb.append(ch);
		}
		return sb.toString();
	}

	public static char toF7(char ch) {
		if (ch < 127) {
			return (char) ('\uF700' + ch);
		}
		return ch;
	}

	public String slashTag(String tagName) {
		final StringBuilder tmp = new StringBuilder();
		tmp.append(END.toRightSyntax(this));
		tmp.append(tagName);
		tmp.append(SEMICOLON.toRightSyntax(this));
		tmp.append(EOB.toRightSyntax(this));
		return tmp.toString();
	}

	public String tag(String tagName) {
		final StringBuilder tmp = new StringBuilder();
		tmp.append(START.toRightSyntax(this));
		tmp.append(tagName);
		tmp.append(SEMICOLON.toRightSyntax(this));
		tmp.append(EOB.toRightSyntax(this));
		return tmp.toString();
	}

	public String tag(String tagName, String name, String value) {
		final StringBuilder tmp = new StringBuilder();
		tmp.append(START.toRightSyntax(this));
		tmp.append(tagName);
		tmp.append(SEMICOLON.toRightSyntax(this));
		tmp.append(name);
		tmp.append(EQUALS.toRightSyntax(this));
		tmp.append(value);
		tmp.append(SEMICOLON.toRightSyntax(this));
		tmp.append(EOB.toRightSyntax(this));
		return tmp.toString();
	}

}
