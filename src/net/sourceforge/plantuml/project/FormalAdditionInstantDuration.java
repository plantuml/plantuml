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
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.project;

class FormalAdditionInstantDuration implements Formal {

	private final Expression exp1;
	private final Expression exp2;
	private final InstantArithmetic math;

	public FormalAdditionInstantDuration(Expression exp1, Expression exp2, InstantArithmetic math) {
		this.exp1 = exp1;
		this.exp2 = exp2;
		this.math = math;
	}

	public String getDescription() {
		return "addID " + exp1 + " " + exp2;
	}

	public NumericType getNumericType() {
		return exp1.getNumericType();
	}

	public Numeric getValue() {
		if (exp2.getNumericType() == NumericType.NUMBER) {
			final Duration d = new Duration((NumericNumber) exp2.getValue());
			return math.add((Instant) exp1.getValue(), d);
		}

		return math.add((Instant) exp1.getValue(), (Duration) exp2.getValue());
	}

}
