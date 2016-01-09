/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 18280 $
 *
 */
package net.sourceforge.plantuml.geom;

public class InflateData implements Comparable<InflateData> {

	private final int pos;
	private final int inflation;

	public InflateData(int pos, int inflation) {
		if (inflation % 2 != 0) {
			throw new IllegalArgumentException();
		}
		this.pos = pos;
		this.inflation = inflation;
	}

	public final int getPos() {
		return pos;
	}

	public final int getInflation() {
		return inflation;
	}

	public int compareTo(InflateData other) {
		return -(this.pos - other.pos);
	}

	@Override
	public String toString() {
		return "" + pos + " (" + inflation + ")";
	}
}
