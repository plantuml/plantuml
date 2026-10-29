/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for java.awt.color.ICC_Profile.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.teavm.classlib.java.awt.color;

public class TICC_Profile {
	public int getNumComponents() {
		return 0;
	}

	public byte[] getData() {
		return null;
	}
}
