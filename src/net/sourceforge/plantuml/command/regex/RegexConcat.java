/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.command.regex;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.StringLocated;

public final class RegexConcat extends RegexComposed implements IRegex {

	private static final ConcurrentMap<Object, RegexConcat> cache = new ConcurrentHashMap<Object, RegexConcat>();
	private final AtomicLong foxRegex = new AtomicLong(-1L);

	// private static final Set<String> PRINTED2 = new HashSet<String>();

	public static void printCacheInfo() {
		int nbCompiled = 0;
		int nbInvoked = 0;
		for (RegexConcat reg : cache.values()) {
			if (reg.isCompiled()) {
				nbCompiled++;
			}
			if (reg.invoked()) {
				nbInvoked++;
			}
		}
		Log.info("Regex total/invoked/compiled " + cache.size() + "/" + nbInvoked + "/" + nbCompiled);
		Log.info("Matches created " + nbCreateMatches.get());
	}

	public RegexConcat(IRegex... partials) {
		super(partials);
	}

	private long foxRegex() {
		if (foxRegex.get() == -1L) {
			long tmp = 0L;
			for (int i = 1; i < partials().size() - 1; i++) {
				final IRegex part = partials().get(i);
				if (part instanceof RegexLeaf) {
					final RegexLeaf leaf = (RegexLeaf) part;
					tmp = tmp | leaf.getFoxSignature();
				}
			}
			foxRegex.set(tmp);
		}
		return foxRegex.get();
	}

	public static RegexConcat build(String key, IRegex... partials) {
		// return buildInternal(partials);
		RegexConcat result = cache.get(key);
		if (result == null) {
			cache.putIfAbsent(key, buildInternal(partials));
			result = cache.get(key);
			// System.err.println("cache size=" + cache.size());
			// } else {
			// synchronized (PRINTED2) {
			// if (PRINTED2.contains(key) == false) {
			// System.err.println("if (key.equals(\"" + key + "\")) return buildInternal(partials);");
			// }
			// PRINTED2.add(key);
		}
		return result;
	}

	private static RegexConcat buildInternal(IRegex... partials) {
		final RegexConcat result = new RegexConcat(partials);
		assert partials[0] == RegexLeaf.start();
		assert partials[partials.length - 1] == RegexLeaf.end();
		return result;
	}

	private boolean invoked() {
		return foxRegex.get() != -1L;
	}

	@Override
	public boolean match(StringLocated s) {
		final long foxRegex = foxRegex();
		if (foxRegex != 0L) {
			final long foxLine = s.getFoxSignature();
			final long check = foxRegex & foxLine;
			// System.err.println("r=" + getFullSlow() + " s=" + s + " line=" + foxLine + " regex" + foxRegex + " "
			// + check + " <" + FoxSignature.backToString(check) + ">");
			if (check != foxRegex) {
				return false;
			}

		}
		return super.match(s);
	}

	@Override
	protected String getFullSlow() {
		final StringBuilder sb = new StringBuilder();
		for (IRegex p : partials()) {
			sb.append(p.getPattern());
		}
		return sb.toString();
	}

}
