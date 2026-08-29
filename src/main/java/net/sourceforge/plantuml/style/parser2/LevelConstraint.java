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

/**
 * The {@code depth(n)} / {@code *} side of a style declaration or query, encoded as a
 * {@code long} bitmask instead of the ad-hoc {@code level}/{@code isStared} pair the legacy
 * {@code net.sourceforge.plantuml.style.StyleKey} carries -- levels are assumed to fit in
 * {@code 0..63}, which every real mindmap/wbs diagram does.
 *
 * This class is deliberately used on <b>both</b> sides of a match, exactly like the legacy
 * {@code StyleSignatureBasic} is: the very same fields play a different role depending on
 * whether they sit on the declaration or on the query, mirrored here from
 * {@code StyleSignatureBasic#matchAllImpl}:
 *
 * <pre>
 *   if (declaration.hasLevel())
 *     if (declaration.isStar())
 *       require query.hasLevel() &amp;&amp; query.level &gt;= declaration.level      // "at least"
 *     else
 *       require query.hasLevel() &amp;&amp; query.level == declaration.level      // exact
 *   if (query.isStar() &amp;&amp; declaration.isStar() == false)
 *     fail                                                                // star-query gate
 * </pre>
 *
 * On the <b>declaration</b> side: {@code star} turns its own {@code level} from an exact match
 * into "matches this level or any deeper one" -- this is what a literal {@code depth(3)*}
 * selector in a .skin/style file means.
 *
 * On the <b>query</b> side: {@code level} is simply "the element being matched sits at this
 * depth", and {@code star} means "this is an ancestor-inheritance lookup" (see
 * {@code net.sourceforge.plantuml.mindmap.Idea#getStyle()}, which walks ancestors with
 * {@code .addStar()}): such a query only ever matches declarations that are themselves
 * {@code star}, which is exactly how the old code restricts ancestor inheritance to
 * catch-all ({@code depth(n)*}, or plain {@code *}) declarations, without needing a separate
 * {@code isStarred} flag anywhere else.
 */
public final class LevelConstraint {

	public static final int NO_LEVEL = -1;
	private static final LevelConstraint NONE = new LevelConstraint(NO_LEVEL, false);
	private static final LevelConstraint NONE_STAR = new LevelConstraint(NO_LEVEL, true);

	private final int level;
	private final boolean star;

	private LevelConstraint(int level, boolean star) {
		this.level = level;
		this.star = star;
	}

	/** No {@code depth(n)} constraint at all: matches at any level (or no level). */
	public static LevelConstraint none() {
		return NONE;
	}

	public static LevelConstraint of(int level, boolean star) {
		if (level == NO_LEVEL)
			return star ? NONE_STAR : NONE;
		if (level < 0 || level >= 64)
			throw new IllegalArgumentException("depth level out of range (must be 0..63): " + level);
		return new LevelConstraint(level, star);
	}

	public int getLevel() {
		return level;
	}

	public boolean hasLevel() {
		return level != NO_LEVEL;
	}

	public boolean isStar() {
		return star;
	}

	/**
	 * The set of query levels this constraint, read as a <b>declaration</b>, accepts: a single
	 * bit for an exact {@code depth(n)}, or every bit from {@code n} upward for a starred
	 * {@code depth(n)*}. Meaningless (and unused) when {@link #hasLevel()} is false.
	 */
	long acceptedLevelsMask() {
		if (hasLevel() == false)
			return -1L;
		return star ? (-1L << level) : (1L << level);
	}

	/**
	 * True if {@code declaration} (read as a stored declaration's constraint) matches
	 * {@code query} (read as the element/ancestor lookup being resolved), per
	 * {@code StyleSignatureBasic#matchAllImpl}.
	 */
	public static boolean matches(LevelConstraint declaration, LevelConstraint query) {
		if (declaration.hasLevel()) {
			if (query.hasLevel() == false)
				return false;
			if ((declaration.acceptedLevelsMask() & (1L << query.level)) == 0)
				return false;
		}

		if (query.star && declaration.star == false)
			return false;

		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LevelConstraint == false)
			return false;
		final LevelConstraint other = (LevelConstraint) obj;
		return this.level == other.level && this.star == other.star;
	}

	@Override
	public int hashCode() {
		return level * 2 + (star ? 1 : 0);
	}

	@Override
	public String toString() {
		if (hasLevel() == false)
			return star ? "*" : "";
		return "depth(" + level + ")" + (star ? "*" : "");
	}

}
