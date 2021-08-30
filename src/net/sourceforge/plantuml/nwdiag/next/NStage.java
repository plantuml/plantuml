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
 */
package net.sourceforge.plantuml.nwdiag.next;

public class NStage implements Comparable<NStage> {

	private final int number;

	public NStage(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "S" + number;
	}

	@Override
	public int compareTo(NStage other) {
		return Integer.compare(this.number, other.number);
	}

	public int getNumber() {
		return number;
	}

	public static NStage getMin(NStage stage1, NStage stage2) {
		if (stage1.number < stage2.number) {
			return stage1;
		}
		return stage2;
	}

	public static NStage getMax(NStage stage1, NStage stage2) {
		if (stage1.number > stage2.number) {
			return stage1;
		}
		return stage2;
	}

}
