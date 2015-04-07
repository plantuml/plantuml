/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.real;

class PositiveForce {

	private final Real fixedPoint;
	private final RealMoveable movingPoint;
	private final double minimunDistance;

	public PositiveForce(Real fixedPoint, RealMoveable movingPoint, double minimunDistance) {
		if (fixedPoint == movingPoint) {
			throw new IllegalArgumentException();
		}
		this.fixedPoint = fixedPoint;
		this.movingPoint = movingPoint;
		this.minimunDistance = minimunDistance;
	}

	@Override
	public String toString() {
		return "PositiveForce fixed=" + fixedPoint + " moving=" + movingPoint + " min=" + minimunDistance;
	}

	public boolean apply() {
		final double distance = movingPoint.getCurrentValue() - fixedPoint.getCurrentValue();
		final double diff = distance - minimunDistance;
		if (diff >= 0) {
			return false;
		}
		// System.err.println("moving " + (-diff) + " " + movingPoint);
		movingPoint.move(-diff);
		return true;
	}

}
