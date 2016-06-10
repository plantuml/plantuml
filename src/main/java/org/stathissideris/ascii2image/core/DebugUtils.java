package org.stathissideris.ascii2image.core;

public class DebugUtils {
	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
}
