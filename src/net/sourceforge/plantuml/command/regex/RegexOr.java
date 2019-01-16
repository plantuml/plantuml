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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegexOr extends RegexComposed implements IRegex {

	private final Pattern2 full;
	private final String name;

	public RegexOr(IRegex... partial) {
		this(null, partial);
	}

	public RegexOr(String name, IRegex... partial) {
		super(partial);
		this.name = name;
		final StringBuilder sb = new StringBuilder("(");
		if (name == null) {
			sb.append("?:");
		}
		for (IRegex p : partial) {
			sb.append(p.getPattern());
			sb.append("|");
		}
		sb.setLength(sb.length() - 1);
		sb.append(')');
		this.full = MyPattern.cmpileNockeck(sb.toString());
	}

	@Override
	protected Pattern2 getFull() {
		return full;
	}

	protected int getStartCount() {
		return 1;
	}

	final public Map<String, RegexPartialMatch> createPartialMatch(Iterator<String> it) {
		final Map<String, RegexPartialMatch> result = new HashMap<String, RegexPartialMatch>();
		final String fullGroup = name == null ? null : it.next();
		result.putAll(super.createPartialMatch(it));
		if (name != null) {
			final RegexPartialMatch m = new RegexPartialMatch(name);
			m.add(fullGroup);
			result.put(name, m);
		}
		return result;
	}

}
