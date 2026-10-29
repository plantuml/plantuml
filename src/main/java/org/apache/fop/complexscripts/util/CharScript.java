/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for org.apache.fop.complexscripts.util.CharScript.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.apache.fop.complexscripts.util;

public final class CharScript {
	private CharScript() {
	}

	public static String scriptTagFromCode(int code) {
		return null;
	}

	public static int dominantScript(CharSequence cs) {
		return 0;
	}
}
