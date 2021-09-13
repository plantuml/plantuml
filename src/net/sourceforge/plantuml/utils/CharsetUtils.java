package net.sourceforge.plantuml.utils;

import java.nio.charset.Charset;

import net.sourceforge.plantuml.Log;

public class CharsetUtils {

	public static Charset charsetOrDefault(String charsetName) {

		if (charsetName == null) {
			Log.info("Using default charset");
			return Charset.defaultCharset();
		} else {
			Log.info("Using charset " + charsetName);
			return Charset.forName(charsetName);
		}
	}
}
