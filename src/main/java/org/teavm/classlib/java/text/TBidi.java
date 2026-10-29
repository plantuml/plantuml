/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for java.text.Bidi.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.teavm.classlib.java.text;

import java.text.AttributedCharacterIterator;

public final class TBidi {
	public static final int DIRECTION_LEFT_TO_RIGHT = 0;
	public static final int DIRECTION_RIGHT_TO_LEFT = 1;
	public static final int DIRECTION_DEFAULT_LEFT_TO_RIGHT = -2;
	public static final int DIRECTION_DEFAULT_RIGHT_TO_LEFT = -1;

	public TBidi(String paragraph, int flags) {
	}

	public TBidi(AttributedCharacterIterator paragraph) {
	}

	public int getLevelAt(int offset) {
		return 0;
	}

	public boolean isLeftToRight() {
		return true;
	}

	public boolean isRightToLeft() {
		return false;
	}

	public boolean isMixed() {
		return false;
	}
}
