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
package net.sourceforge.plantuml.ngdot.layout;

/**
 * The {@code rankdir} graph attribute.
 * <p>
 * Smetana only implements {@code TB} and {@code LR}: {@code BT} and
 * {@code RL} are computed as {@code TB} and {@code LR}, then mirrored, which is
 * what Graphviz does too (the layout is always made top to bottom, then
 * rotated or flipped).
 */
public enum Rankdir {

	TB, LR, BT, RL;

	public static Rankdir fromString(String value) {
		if (value == null)
			return TB;
		final String s = value.trim().toUpperCase();
		if (s.equals("LR"))
			return LR;
		if (s.equals("BT"))
			return BT;
		if (s.equals("RL"))
			return RL;
		return TB;
	}

	/**
	 * {@code true} when the ranks are columns ({@code LR} or {@code RL}).
	 */
	public boolean isHorizontal() {
		return this == LR || this == RL;
	}

}
