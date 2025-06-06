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
import java.util.function.Function;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.jaws.Jaws;

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

class BoundedPatternCache {
	private final ConcurrentHashMap<String, Pattern2> cache;
	private final AtomicInteger putCount = new AtomicInteger();
	private final int maxSize;

	public BoundedPatternCache(int initialSize, int maxSize) {
		this.cache = new ConcurrentHashMap<>(initialSize);
		this.maxSize = maxSize;
	}

	public Pattern2 computeIfAbsent(String key, Function<String, Pattern2> mappingFunction) {
		return cache.computeIfAbsent(key, k -> {
			final int size = putCount.incrementAndGet();
			// This should never happen, but just in case, to avoid memory leak
			if (size >= maxSize) {
				cache.clear();
				putCount.set(0);
			}
			return mappingFunction.apply(k);
		});
	}

}

public abstract class MyPattern {

	private static final BoundedPatternCache cache = new BoundedPatternCache(12_000, 30_000);

	private static final Pattern2 EMPTY = new Pattern2(Pattern.compile(""));

	private MyPattern() {

	}

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

		return cache.computeIfAbsent(p, key -> new Pattern2(Pattern.compile(transform(key), Pattern.CASE_INSENSITIVE)));

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

}
