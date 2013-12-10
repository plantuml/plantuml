/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.project;

class NumericNumber implements Numeric {

	private final int value;

	public NumericNumber(int v) {
		this.value = v;
	}

	public Numeric add(Numeric other) {
		if (other.getNumericType() != getNumericType()) {
			throw new IllegalArgumentException();
		}
		return new NumericNumber(value + ((NumericNumber) other).value);
	}

	public NumericType getNumericType() {
		return NumericType.NUMBER;
	}

	public int getIntValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Number:" + value;
	}

	public int compareTo(Numeric other) {
		final NumericNumber this2 = (NumericNumber) other;
		if (this2.value > value) {
			return -1;
		}
		if (this2.value < value) {
			return 1;
		}
		return 0;
	}

}
