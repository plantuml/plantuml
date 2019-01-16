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
package net.sourceforge.plantuml.real;

import java.util.concurrent.atomic.AtomicInteger;

abstract class RealMoveable extends AbstractReal implements Real {

	public static final AtomicInteger CPT = new AtomicInteger();
	private final int cpt = CPT.getAndIncrement();
	private final String name;
	private final Throwable creationPoint;

	RealMoveable(RealLine line, String name) {
		super(line);
		this.name = name;
		this.creationPoint = new Throwable();
		this.creationPoint.fillInStackTrace();
	}

	abstract void move(double delta);

	final public void printCreationStackTrace() {
		creationPoint.printStackTrace();
	}

	final public Real addFixed(double delta) {
		return new RealDelta(this, delta);
	}

	@Override
	public final String toString() {
		return "#" + cpt + "_" + name;
	}

	final public String getName() {
		return name;
	}
}
