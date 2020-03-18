/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.project.time;

import net.sourceforge.plantuml.project.Value;

public class Wink implements Value, Comparable<Wink> {

	private final int wink;

	public Wink(int wink) {
		this.wink = wink;
	}

	@Override
	public String toString() {
		return "(Wink +" + wink + ")";
	}

	public Wink increment() {
		return new Wink(wink + 1);
	}

	public Wink decrement() {
		return new Wink(wink - 1);
	}

	public final int getWink() {
		return wink;
	}

	public int compareTo(Wink other) {
		return this.wink - other.wink;
	}

	public String toShortString() {
		return "" + (wink + 1);
	}

	public static Wink min(Wink wink1, Wink wink2) {
		if (wink2.wink < wink1.wink) {
			return wink2;
		}
		return wink1;
	}

	public static Wink max(Wink wink1, Wink wink2) {
		if (wink2.wink > wink1.wink) {
			return wink2;
		}
		return wink1;
	}

}
