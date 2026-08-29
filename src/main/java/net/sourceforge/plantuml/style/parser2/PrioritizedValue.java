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
 * One property value, together with the priority it was assigned when merged in (see
 * {@link MergedStyleNode#mergeRule}) -- the direct counterpart of the legacy
 * {@code net.sourceforge.plantuml.style.DarkString}, kept under the same name it had before
 * this class grew a dark half.
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
	private final int priority;

	private PrioritizedValue(String light, String dark, int priority) {
		this.light = light;
		this.dark = dark;
		this.priority = priority;
	}

	public static PrioritizedValue light(String value, int priority) {
		return new PrioritizedValue(value, null, priority);
	}

	public static PrioritizedValue dark(String value, int priority) {
		return new PrioritizedValue(null, value, priority);
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

	public int getPriority() {
		return priority;
	}

	/**
	 * Merges this (incoming) value with {@code existing} (already accumulated for the same
	 * property), mirroring {@code DarkString#mergeWith} exactly:
	 * <ul>
	 * <li>if both are the same kind (both light-only, or both dark-only) -- or either one is
	 * already a combined light+dark value -- the strictly higher priority wins outright, and on
	 * an exact tie {@code existing} wins, not the incoming value;</li>
	 * <li>if one is light-only and the other is dark-only, they combine into one value carrying
	 * both, keeping whichever priority belonged to the light-only side of the pair.</li>
	 * </ul>
	 * {@code existing} may be null (nothing accumulated yet), in which case this value is
	 * returned unconditionally.
	 */
	public PrioritizedValue mergeWith(PrioritizedValue existing) {
		if (existing == null)
			return this;

		if ((this.dark == null && existing.dark == null) || (this.light == null && existing.light == null))
			return isBigger(this.priority, existing.priority) ? this : existing;

		if (this.dark == null && existing.light == null)
			return new PrioritizedValue(this.light, existing.dark, this.priority);

		if (existing.dark == null && this.light == null)
			return new PrioritizedValue(existing.light, this.dark, existing.priority);

		return isBigger(this.priority, existing.priority) ? this : existing;
	}

	private static boolean isBigger(int a, int b) {
		return a > b;
	}

	public PrioritizedValue shiftPriority(int delta) {
		return new PrioritizedValue(light, dark, priority + delta);
	}

	@Override
	public String toString() {
		return light + "/" + dark + " (" + priority + ")";
	}

}
