/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for java.awt.Font.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.teavm.classlib.java.awt;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.teavm.classlib.java.awt.font.TFontRenderContext;
import org.teavm.classlib.java.awt.font.TGlyphVector;

public class TFont {
	public static final int PLAIN = 0;
	public static final int BOLD = 1;
	public static final int ITALIC = 2;

	public TFont(String name, int style, int size) {
	}

	public TFont deriveFont(Map<?, ?> attributes) {
		return this;
	}

	public TFont deriveFont(int style, float size) {
		return this;
	}

	public TFont deriveFont(float size) {
		return this;
	}

	public static TFont decode(String str) {
		return null;
	}

	public static TFont createFont(int fontFormat, InputStream stream) throws IOException {
		throw new UnsupportedOperationException();
	}

	public TGlyphVector layoutGlyphVector(TFontRenderContext frc, char[] chars, int start, int limit, int flags) {
		return null;
	}

	public Map<?, ?> getAttributes() {
		return null;
	}

	public int canDisplayUpTo(String str) {
		return -1;
	}
}
