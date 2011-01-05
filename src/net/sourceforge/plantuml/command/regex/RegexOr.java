/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4762 $
 *
 */
package net.sourceforge.plantuml.command.regex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class RegexOr extends RegexComposed implements IRegex {

	private final Pattern full;
	private final String name;

	public RegexOr(IRegex... partial) {
		this(null, partial);
	}

	public RegexOr(String name, IRegex... partial) {
		this(name, false, partial);
	}

	public RegexOr(String name, boolean optionnal, IRegex... partial) {
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
		if (optionnal) {
			sb.append('?');
		}
		this.full = Pattern.compile(sb.toString());
	}

	@Override
	protected Pattern getFull() {
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
