package net.sourceforge.plantuml.test;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;

public class StringTestUtils {

	/**
	 * This can be replaced by String.join() when we move to Java 1.8
	 */
	public static String join(CharSequence delimiter, CharSequence... elements) {
		requireNonNull(delimiter);
		requireNonNull(elements);
		
		if (elements.length == 0) return "";
		
		final StringBuilder b = new StringBuilder();
		for (int i = 0; i < elements.length; i++) {
			if (i > 0) b.append(delimiter);
			b.append(elements[i]);
		}
		return b.toString();
	}

	/**
	 * This can be replaced by String.join() when we move to Java 1.8
	 */
	public static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
		requireNonNull(delimiter);
		requireNonNull(elements);
		
		final Iterator<? extends CharSequence> i = elements.iterator();
		if (!i.hasNext()) return "";
		
		final StringBuilder b = new StringBuilder();
		while(true) {
			b.append(i.next());
			if (i.hasNext()) {
				b.append(delimiter);
			}
			else {
				break;
			}
		}
		return b.toString();
	}
}
