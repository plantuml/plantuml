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

import java.util.Iterator;
import java.util.Map;

import net.sourceforge.plantuml.text.StringLocated;

public interface IRegex {

	public String getPatternAsString();

	public int count();

	/**
	 * Consumes from {@code it} the groups this node accounts for, and puts into {@code result} an
	 * entry for each named part it holds.
	 *
	 * Filling a map the caller owns, rather than returning one, is what keeps a match to a single
	 * map: the tree a command is built from is deep, and a node that returned its own map had
	 * every entry re-hashed once more at each level on the way up.
	 *
	 * {@code it} must be advanced by exactly {@link #count()} groups whether or not anything is
	 * recorded, so that the groups stay aligned with the pattern for whatever comes next.
	 */
	public void fillPartialMatch(Iterator<String> it, Map<String, RegexPartialMatch> result);

	public boolean match(StringLocated full);

	public RegexResult matcher(String full);
}