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
	// ::remove file when __HAXE__

	public final EnumSet<SName> snames;
	public final boolean isStared;
	public final int level;

	private StyleKey(EnumSet<SName> snames, int level, boolean isStared) {
		this.level = level;
		this.isStared = isStared;
		this.snames = snames;
	}

	public static StyleKey empty() {
		return new StyleKey(EnumSet.noneOf(SName.class), -1, false);
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

		return new StyleKey(result, level, isStared);

	}

	public StyleKey addLevel(int level) {
		return new StyleKey(snames, level, isStared);
	}

	public StyleKey addSName(SName name) {
		final EnumSet<SName> result = snames.clone();
		result.add(name);
		return new StyleKey(result, level, isStared);
	}

	public StyleKey addStar() {
		return new StyleKey(snames, level, true);
	}

	public static StyleKey of(SName... names) {

		final EnumSet<SName> result = EnumSet.noneOf(SName.class);
		for (SName name : names)
			result.add(name);
		return new StyleKey(result, -1, false);
	}

	public StyleKey mergeWith(StyleKey other) {

		final EnumSet<SName> result1 = snames.clone();
		result1.addAll(other.snames);

		return new StyleKey(result1, Math.max(level, other.level), isStared || other.isStared);
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
			result = Objects.hash(snames, isStared, level);
			cachedHashCode = result;
		}
		return result;
	}

}