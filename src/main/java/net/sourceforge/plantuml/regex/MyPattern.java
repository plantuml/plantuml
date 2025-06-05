/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.regex;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.utils.Log;

class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
	private final int maxSize;

	public MaxSizeHashMap(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > maxSize;
	}
}

// Splitter.java to be finished
public abstract class MyPattern {

	private static final Map<String, Pattern2> cache = new MaxSizeHashMap<>(30_000);

	private static final Pattern2 EMPTY = new Pattern2(Pattern.compile(""));

//	static int CPT1;
//	static int CPT2;

	// private static final Map<String, AtomicInteger> COUNTER = new
	// ConcurrentHashMap<>();

	public static Pattern2 cmpile(final String p) {
		if (p == null || p.length() == 0)
			return EMPTY;

		// int value = COUNTER.computeIfAbsent(p, key -> new
		// AtomicInteger(0)).incrementAndGet();
//		synchronized (COUNTER) {
//			if (value > 1000) {
//				Log.logStackTrace();
//				try {
//					Thread.sleep(1000L * 3600);
//				} catch (InterruptedException e) {
//				}
//			}
//			printPopularPatterns();
//		}

//		CPT1++;
		Pattern2 result = null;
		synchronized (cache) {
			result = cache.get(p);
			if (result != null) {
				return result;
			}
		}
		assert result == null;
		result = new Pattern2(Pattern.compile(transform(p), Pattern.CASE_INSENSITIVE));

		synchronized (cache) {
			cache.put(p, result);
//			Log.perflog("size=" + cache.size());
//			Log.perflog(p);
//			CPT2++;
//			System.err.println("CPT= " + CPT1 + " / " + CPT2 + " " + cache.size());
		}

		return result;
	}

//	public static void printPopularPatterns() {
//		Log.deletePerfLogFile();
//		for (Map.Entry<String, AtomicInteger> entry : COUNTER.entrySet()) {
//			int count = entry.getValue().get();
//			if (count > 500) {
//				Log.perflog("Pattern: " + entry.getKey() + " | Count: " + count);
//			}
//		}
//	}

	private static String transform(String p) {
		// Replace ReadLineReader.java
		// p = p.replace("%pLN", "\\p{L}0-9"); // Unicode Letter, digit
		p = p.replace("%pLN", "\\p{L}\\p{N}"); // Unicode Letter, digit
		p = p.replace("%s", "\\s\u00A0"); // space
		p = p.replace("%q", "'\u2018\u2019"); // quote
		p = p.replace("%g", "\"\u201c\u201d" + Jaws.BLOCK_E1_INVISIBLE_QUOTE); // double quote
		return p;
	}

	public static CharSequence removeAll(CharSequence src, String regex) {
		return src.toString().replaceAll(transform(regex), "");
	}

}
