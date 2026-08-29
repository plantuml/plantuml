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
package net.sourceforge.plantuml.style;

/**
 * How specific a style declaration is, modeled the way real CSS specificity actually works: an
 * ordered tuple, compared one component at a time from most to least significant -- never a
 * single number that has to rely on one component's magnitude never reaching into (or being
 * dominated by) another's.
 *
 * That was the previous design's real bug, not just an aesthetic complaint: the priority used to
 * be a single {@code int}, with a stereotype-boosted declaration getting a flat
 * {@code +DELTA_PRIORITY_FOR_STEREOTYPE} added on top of a plain file-order counter, and the
 * wbs/mindmap ancestor-inheritance cascade ({@code WElement}/{@code Idea}) shifting starred
 * declarations by a second, much larger constant on the same assumption -- one relying on its
 * magnitude alone to swamp the other two tiers. That constant ({@code STEP_BY_PARENT * 1000})
 * silently overflowed {@code int} by about 4.7x, wrapping to an unrelated (if still, by luck,
 * usable) value. Giving the ancestor cascade its own dedicated boolean tier below, instead of a
 * magnitude that has to out-scale everything below it, sidesteps that whole class of mistake:
 * nothing here is ever added to, or multiplied with, anything else.
 *
 * From most to least significant:
 * <ul>
 * <li>{@link #forcedOverride()}: an explicit, programmatic override (see
 * {@code Style#eventuallyOverride}) that must always win, whatever it is compared against --
 * the direct replacement for the old scheme's own {@code Integer.MAX_VALUE} sentinel, which had
 * the exact same "hope nothing else gets this big" problem the rest of this type exists to
 * avoid;
 * <li>ancestor cascade: whether this declaration is being applied through the mindmap/wbs
 * ancestor-inheritance cascade (see {@code StyleBuilder#getMergedStyleSpecial}) at all -- a
 * starred declaration reached that way always outranks an ordinary, non-cascaded declaration,
 * exactly as the old scheme's huge additive constant always did (whatever the ordinary
 * declaration's own stereotype count or file order);
 * <li>ancestor rank: only meaningful when the tier above is set -- a nearer ancestor's matching
 * declaration always beats a farther ancestor's (0 is the element's own level, more negative is
 * farther up);
 * <li>stereotype count: how many stereotypes the declaration's selector requires -- a plain
 * SName-only selector is 0, {@code .foo} is 1, the nested-selector equivalent of a compound
 * {@code .foo.bar} is 2 -- exactly like counting classes in a CSS selector's specificity, so a
 * declaration naming more stereotypes always outranks one naming fewer, whichever file order
 * they were declared in;
 * <li>order: the position this declaration was parsed at. The lowest-priority tier, breaking
 * ties between two otherwise equally specific declarations in favor of whichever came later --
 * exactly like CSS's own source-order tiebreak.
 * </ul>
 */
public final class Specificity implements Comparable<Specificity> {

	private final boolean forced;
	private final boolean ancestorCascade;
	private final int ancestorRank;
	private final int stereotypeCount;
	private final int order;

	private Specificity(boolean forced, boolean ancestorCascade, int ancestorRank, int stereotypeCount, int order) {
		this.forced = forced;
		this.ancestorCascade = ancestorCascade;
		this.ancestorRank = ancestorRank;
		this.stereotypeCount = stereotypeCount;
		this.order = order;
	}

	/** A plain declaration's specificity, at the position it was parsed at. */
	public static Specificity atOrder(int order) {
		return new Specificity(false, false, 0, 0, order);
	}

	/**
	 * An explicit, programmatic override that must always win over anything it is merged
	 * against -- see this class's own javadoc for why a dedicated tier is used here instead of
	 * reusing the largest possible value of the other fields.
	 */
	public static Specificity forcedOverride() {
		return new Specificity(true, false, 0, 0, 0);
	}

	/** This same specificity, but requiring {@code count} stereotypes instead of however many it did before. */
	public Specificity withStereotypeCount(int count) {
		return new Specificity(forced, ancestorCascade, ancestorRank, count, order);
	}

	/**
	 * This same specificity, but applied through the ancestor-inheritance cascade at ancestor rank
	 * {@code rank} (0 = the element's own level, more negative = farther up) instead of however it
	 * was resolved before.
	 */
	public Specificity withAncestorRank(int rank) {
		return new Specificity(forced, true, rank, stereotypeCount, order);
	}

	/** Whether the declaration this specificity was computed for requires at least one stereotype. */
	public boolean hasStereotype() {
		return stereotypeCount > 0;
	}

	@Override
	public int compareTo(Specificity other) {
		int c = Boolean.compare(this.forced, other.forced);
		if (c != 0)
			return c;

		c = Boolean.compare(this.ancestorCascade, other.ancestorCascade);
		if (c != 0)
			return c;

		c = Integer.compare(this.ancestorRank, other.ancestorRank);
		if (c != 0)
			return c;

		c = Integer.compare(this.stereotypeCount, other.stereotypeCount);
		if (c != 0)
			return c;

		return Integer.compare(this.order, other.order);
	}

	public boolean isBiggerThan(Specificity other) {
		return compareTo(other) > 0;
	}

	@Override
	public String toString() {
		if (forced)
			return "override";
		if (ancestorCascade)
			return order + ",stereo=" + stereotypeCount + ",ancestor=" + ancestorRank;
		if (stereotypeCount != 0)
			return order + ",stereo=" + stereotypeCount;
		return Integer.toString(order);
	}

}
