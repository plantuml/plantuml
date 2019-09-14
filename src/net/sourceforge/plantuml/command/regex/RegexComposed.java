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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringLocated;

public abstract class RegexComposed implements IRegex {

	protected static final AtomicInteger nbCreateMatches = new AtomicInteger();
	private final List<IRegex> partials;

	protected final List<IRegex> partials() {
		return partials;
	}

	abstract protected String getFullSlow();

	private final AtomicReference<Pattern2> fullCached = new AtomicReference<Pattern2>();

	private Pattern2 getPattern2() {
		Pattern2 result = fullCached.get();
		if (result == null) {
			final String fullSlow = getFullSlow();
			result = MyPattern.cmpile(fullSlow, Pattern.CASE_INSENSITIVE);
			fullCached.set(result);
		}
		return result;
	}

	final protected boolean isCompiled() {
		return fullCached.get() != null;
	}

	public RegexComposed(IRegex... partial) {
		this.partials = Collections.unmodifiableList(Arrays.asList(partial));
	}

	public Map<String, RegexPartialMatch> createPartialMatch(Iterator<String> it) {
		nbCreateMatches.incrementAndGet();
		final Map<String, RegexPartialMatch> result = new HashMap<String, RegexPartialMatch>();
		for (IRegex r : partials) {
			result.putAll(r.createPartialMatch(it));
		}
		return result;
	}

	final public int count() {
		int cpt = getStartCount();
		for (IRegex r : partials) {
			cpt += r.count();
		}
		return cpt;
	}

	protected int getStartCount() {
		return 0;
	}

	public RegexResult matcher(String s) {
		final Matcher2 matcher = getPattern2().matcher(s);
		if (matcher.find() == false) {
			return null;
		}

		final Iterator<String> it = new MatcherIterator(matcher);
		return new RegexResult(createPartialMatch(it));
	}

	public boolean match(StringLocated s) {
		return getPattern2().matcher(s.getString()).find();
	}

	final public String getPattern() {
		return getPattern2().pattern();
	}

	final protected List<IRegex> getPartials() {
		return partials;
	}

}
