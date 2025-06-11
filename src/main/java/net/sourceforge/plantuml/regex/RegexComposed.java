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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import net.sourceforge.plantuml.text.StringLocated;

public abstract class RegexComposed implements IRegex {

//	protected static final AtomicInteger nbCreateMatches = new AtomicInteger();
//	protected static final AtomicInteger vtot = new AtomicInteger();
//	protected static final AtomicInteger vescaped = new AtomicInteger();

	private final List<IRegex> partials;

	protected final List<IRegex> partials() {
		return partials;
	}

	private final AtomicReference<Pattern2> fullCached = new AtomicReference<>();

	private Pattern2 getPattern2() {
		final Pattern2 result = fullCached.get();
		if (result != null)
			return result;

		final Pattern2 computed = Pattern2.cmpile(getPatternAsString());
		if (fullCached.compareAndSet(null, computed))
			return computed;

		return fullCached.get();
	}

	final protected boolean isCompiled() {
		return fullCached.get() != null;
	}

	public RegexComposed(IRegex... partial) {
		this.partials = Collections.unmodifiableList(Arrays.asList(partial));
	}

	public Map<String, RegexPartialMatch> createPartialMatch(Iterator<String> it) {
		// nbCreateMatches.incrementAndGet();
		final Map<String, RegexPartialMatch> result = new HashMap<String, RegexPartialMatch>();
		for (IRegex r : partials)
			result.putAll(r.createPartialMatch(it));

		return result;
	}

	final public int count() {
		int cpt = getStartCount();
		for (IRegex r : partials)
			cpt += r.count();

		return cpt;
	}

	protected int getStartCount() {
		return 0;
	}

	public RegexResult matcher(String s) {
		final Matcher2 matcher = getPattern2().matcher(s);
		if (matcher.find() == false)
			return null;

		final Iterator<String> it = new MatcherIterator(matcher);
		return new RegexResult(createPartialMatch(it));
	}

	public boolean match(StringLocated s) {
		final String tmp = s.getString();
		final Matcher2 matcher = getPattern2().matcher(tmp);
		if (matcher == null)
			return false;

		return matcher.find();
	}

	final public String getPattern() {
		// return getFullSlow();
		return getPattern2().pattern();
	}

	final protected List<IRegex> getPartials() {
		return partials;
	}

}
