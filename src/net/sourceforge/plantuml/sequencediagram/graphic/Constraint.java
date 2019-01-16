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
package net.sourceforge.plantuml.sequencediagram.graphic;

public class Constraint {

	private final Pushable p1;
	private final Pushable p2;
	private double value;

	public Constraint(Pushable p1, Pushable p2) {
		if (p1 == null || p2 == null) {
			throw new IllegalArgumentException();
		}
		this.p1 = p1;
		this.p2 = p2;
	}

	public final Pushable getParticipant1() {
		return p1;
	}

	public final Pushable getParticipant2() {
		return p2;
	}

	public final double getValue() {
		return value;
	}

	public final void ensureValue(double newValue) {
		if (newValue > value) {
			this.value = newValue;
		}
	}

	public void push(double delta) {
		value += delta;
	}

	@Override
	public String toString() {
		return "Constraint " + p1 + " " + p2 + " " + value;
	}

}
