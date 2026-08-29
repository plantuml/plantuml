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

import java.util.EnumSet;
import java.util.Objects;

import net.sourceforge.plantuml.url.Url;

public class StyleKey {

	public final EnumSet<SName> snames;
	public final boolean isStared;
	public final int level;

	/**
	 * A bit per {@code snames} member ({@code 1L << (name.ordinal() & 63)}), maintained
	 * incrementally alongside {@code snames} instead of ever being derived from it. It exists
	 * only to keep {@link #hashCode()} off {@code EnumSet}'s own iterator: on the JVM,
	 * {@code EnumSet.hashCode()} is cheap, but under TeaVM every {@code next()} call on a
	 * {@code GenericEnumSet}'s iterator rebuilds the enum's whole {@code values()} array from
	 * scratch (no caching there, unlike {@code Class} on the JVM) -- and {@code StyleKey}'s own
	 * per-instance hash cache below doesn't help the very common case of a brand-new instance
	 * (every {@code with*}-style method here returns one), so that cost was paid on nearly every
	 * style lookup. A single {@code long} can't distinguish all of {@link SName}'s own values
	 * (there are more than 64 of them) the way a real {@code Set} membership test can, so this
	 * is deliberately NOT used for {@link #equals(Object)} or any exactness-sensitive check --
	 * {@code equals} keeps comparing the real {@code snames} sets, and two different SNames
	 * whose ordinals happen to collide modulo 64 just make for a very slightly worse (never
	 * wrong) hash, which is all a hash code ever has to be.
	 */
	private final long snameBits;

	private StyleKey(EnumSet<SName> snames, long snameBits, int level, boolean isStared) {
		this.level = level;
		this.isStared = isStared;
		this.snames = snames;
		this.snameBits = snameBits;
	}

	private static long bitFor(SName name) {
		return 1L << (name.ordinal() & 63);
	}

	public static StyleKey empty() {
		return new StyleKey(EnumSet.noneOf(SName.class), 0L, -1, false);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(snames + " ");
		if (level != -1)
			sb.append(" " + level);
		if (isStared)
			sb.append(" (*)");
		return sb.toString();
	}

	public StyleKey addClickable(Url url) {
		if (url == null)
			return this;

		final EnumSet<SName> result = snames.clone();
		result.add(SName.clickable);

		return new StyleKey(result, snameBits | bitFor(SName.clickable), level, isStared);

	}

	public StyleKey addLevel(int level) {
		return new StyleKey(snames, snameBits, level, isStared);
	}

	public StyleKey addSName(SName name) {
		final EnumSet<SName> result = snames.clone();
		result.add(name);
		return new StyleKey(result, snameBits | bitFor(name), level, isStared);
	}

	public StyleKey addStar() {
		return new StyleKey(snames, snameBits, level, true);
	}

	public static StyleKey ofSNames(SName... names) {

		final EnumSet<SName> result = EnumSet.noneOf(SName.class);
		long bits = 0L;
		for (SName name : names) {
			result.add(name);
			bits |= bitFor(name);
		}
		return new StyleKey(result, bits, -1, false);
	}

	public StyleKey mergeWith(StyleKey other) {

		final EnumSet<SName> result1 = snames.clone();
		result1.addAll(other.snames);

		return new StyleKey(result1, snameBits | other.snameBits, Math.max(level, other.level),
				isStared || other.isStared);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof StyleKey))
			return false;
		final StyleKey other = (StyleKey) obj;
		return Objects.equals(snames, other.snames) && isStared == other.isStared && level == other.level;
	}

	private transient int cachedHashCode = 0;

	@Override
	public int hashCode() {
		int result = cachedHashCode;
		if (result == 0) {
			result = 31 * (31 * Long.hashCode(snameBits) + Boolean.hashCode(isStared)) + level;
			if (result == 0)
				result = 1;
			cachedHashCode = result;
		}
		return result;
	}

}