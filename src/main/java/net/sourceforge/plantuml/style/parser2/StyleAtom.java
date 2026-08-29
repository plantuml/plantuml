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
package net.sourceforge.plantuml.style.parser2;

import net.sourceforge.plantuml.style.SName;

/**
 * One "tag" a style declaration or a query element can carry, put under a single total order
 * so a {@link StyleAtomTrie} can index both families together instead of keeping a separate
 * plain/legacy split: a {@link SName} (the common, fast path -- compared by ordinal) or a
 * stereotype name (the rarer path -- compared lexicographically). {@code SName} atoms always
 * sort before stereotype atoms; within a family, atoms sort as described.
 *
 * This is exactly the "order on SName" the trie design relies on to walk a query's atoms once,
 * in order, matching them against trie edges instead of re-testing every stored declaration.
 */
public final class StyleAtom implements Comparable<StyleAtom> {

	private final SName sname;
	private final String stereotype;

	private StyleAtom(SName sname, String stereotype) {
		this.sname = sname;
		this.stereotype = stereotype;
	}

	public static StyleAtom of(SName sname) {
		if (sname == null)
			throw new IllegalArgumentException("sname");
		return new StyleAtom(sname, null);
	}

	/** The stereotype text without its leading dot, already lower-cased. */
	public static StyleAtom ofStereotype(String stereotype) {
		if (stereotype == null)
			throw new IllegalArgumentException("stereotype");
		return new StyleAtom(null, stereotype.toLowerCase());
	}

	public boolean isName() {
		return sname != null;
	}

	/** Non-null only when {@link #isName()} is true. */
	public SName getSName() {
		return sname;
	}

	/** Non-null only when {@link #isName()} is false. */
	public String getStereotype() {
		return stereotype;
	}

	@Override
	public int compareTo(StyleAtom other) {
		if (this.sname != null && other.sname != null)
			return this.sname.compareTo(other.sname);
		if (this.sname != null)
			return -1;
		if (other.sname != null)
			return 1;
		return this.stereotype.compareTo(other.stereotype);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StyleAtom == false)
			return false;
		final StyleAtom other = (StyleAtom) obj;
		if (this.sname != null)
			return this.sname == other.sname;
		return this.stereotype.equals(other.stereotype);
	}

	@Override
	public int hashCode() {
		return sname != null ? sname.hashCode() : stereotype.hashCode();
	}

	@Override
	public String toString() {
		return sname != null ? sname.toString() : ("." + stereotype);
	}

}
