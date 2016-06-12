/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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

enum ItemCaract {
	BEGIN(NumericType.INSTANT), //
	COMPLETED(NumericType.INSTANT), //
	DURATION(NumericType.DURATION), //
	LOAD(NumericType.LOAD), //
	WORK(NumericType.NUMBER);

	private final NumericType type;

	private ItemCaract(NumericType type) {
		this.type = type;
	}

	public NumericType getNumericType() {
		return type;
	}

	public Numeric getData(Item item) {
		if (this == BEGIN) {
			return item.getBegin();
		}
		if (this == COMPLETED) {
			return item.getCompleted();
		}
		if (this == DURATION) {
			return item.getDuration();
		}
		if (this == LOAD) {
			return item.getLoad();
		}
		if (this == WORK) {
			return item.getWork();
		}
		throw new UnsupportedOperationException();
	}
}
