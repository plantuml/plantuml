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

import net.sourceforge.plantuml.style.Specificity;

/**
 * One property value, together with the {@link Specificity} it was assigned when merged in (see
 * {@link MergedStyleNode#mergeRule}) -- the direct counterpart of the legacy
 * {@code net.sourceforge.plantuml.style.DarkString}, kept under the same name it had before
 * this class grew a dark half.
 *
 * This carries a full {@link Specificity} rather than a bare {@code int} priority on purpose:
 * an earlier version of this class (and of {@code net.sourceforge.plantuml.style.StyleMerge})
 * used a single {@code int}, boosted by a flat constant for a stereotype match and shifted by a
 * second, much larger one for the mindmap/wbs ancestor cascade -- exactly the design
 * {@link Specificity}'s own javadoc explains was replaced for silently overflowing. Sharing that
 * same tiered type here (rather than reinventing an equivalent) means a value resolved through
 * this {@code parser2} engine and one resolved through the legacy
 * {@code net.sourceforge.plantuml.style.StyleBuilder} path compare exactly the same way, however
 * they got combined.
 *
 * A property can carry a {@link #getLight()} value, a {@link #getDark()} one, or both: a
 * plain declaration (outside any {@code @media} block) sets only {@link #getLight()}: a
 * declaration inside {@code @media (prefers-color-scheme:dark) { ... }} sets only
 * {@link #getDark()}. When both a light and a dark declaration reach the very same property
 * of the very same selector, {@link #mergeWith(PrioritizedValue)} folds them into ONE value
 * carrying both -- exactly what lets the final color object (see
 * {@code net.sourceforge.plantuml.style.ValueImpl#asColor}, {@code HColor#withDark}) embed
 * both variants and let the actual theme be picked at render/output time (e.g. so a single
 * generated SVG can follow the viewer's {@code prefers-color-scheme} setting), instead of one
 * theme being baked in once and for all when the style sheet is resolved.
 */
public final class PrioritizedValue {

	private final String light;
	private final String dark;
	private final Specificity specificity;

	private PrioritizedValue(String light, String dark, Specificity specificity) {
		this.light = light;
		this.dark = dark;
		this.specificity = specificity;
	}

	public static PrioritizedValue light(String value, Specificity specificity) {
		return new PrioritizedValue(value, null, specificity);
	}

	public static PrioritizedValue dark(String value, Specificity specificity) {
		return new PrioritizedValue(null, value, specificity);
	}

	/** Null if this value was never given a light (regular) declaration. */
	public String getLight() {
		return light;
	}

	/** Null if this value was never given a dark ({@code @media}) declaration. */
	public String getDark() {
		return dark;
	}

	/**
	 * The light value if there is one, else the dark one -- for callers that do not (yet) care
	 * about the light/dark distinction and just want a single string, the way
	 * {@link #getProperties()}-style call sites used before this class carried both.
	 */
	public String getValue() {
		return light != null ? light : dark;
	}

	public Specificity getSpecificity() {
		return specificity;
	}

	/**
	 * Merges this (incoming) value with {@code existing} (already accumulated for the same
	 * property), mirroring {@code DarkString#mergeWith} exactly:
	 * <ul>
	 * <li>if both are the same kind (both light-only, or both dark-only) -- or either one is
	 * already a combined light+dark value -- the strictly more specific one wins outright, and on
	 * an exact tie {@code existing} wins, not the incoming value;</li>
	 * <li>if one is light-only and the other is dark-only, they combine into one value carrying
	 * both, keeping whichever specificity belonged to the light-only side of the pair.</li>
	 * </ul>
	 * {@code existing} may be null (nothing accumulated yet), in which case this value is
	 * returned unconditionally.
	 */
	public PrioritizedValue mergeWith(PrioritizedValue existing) {
		if (existing == null)
			return this;

		if ((this.dark == null && existing.dark == null) || (this.light == null && existing.light == null))
			return this.specificity.isBiggerThan(existing.specificity) ? this : existing;

		if (this.dark == null && existing.light == null)
			return new PrioritizedValue(this.light, existing.dark, this.specificity);

		if (existing.dark == null && this.light == null)
			return new PrioritizedValue(existing.light, this.dark, existing.specificity);

		return this.specificity.isBiggerThan(existing.specificity) ? this : existing;
	}

	/**
	 * This same value, but applied through the mindmap/wbs ancestor-inheritance cascade at
	 * ancestor rank {@code rank} -- mirroring {@code ValueImpl#withAncestorRank}/
	 * {@code DarkString#withAncestorRank} exactly, via {@link Specificity#withAncestorRank(int)}.
	 */
	public PrioritizedValue withAncestorRank(int rank) {
		return new PrioritizedValue(light, dark, specificity.withAncestorRank(rank));
	}

	/**
	 * This same value, but requiring {@code count} stereotypes -- mirroring
	 * {@code ValueImpl#withStereotypeCount}, via {@link Specificity#withStereotypeCount(int)}.
	 */
	public PrioritizedValue withStereotypeCount(int count) {
		return new PrioritizedValue(light, dark, specificity.withStereotypeCount(count));
	}

	@Override
	public String toString() {
		return light + "/" + dark + " (" + specificity + ")";
	}

}
