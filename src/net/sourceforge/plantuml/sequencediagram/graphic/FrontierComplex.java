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

import java.util.Arrays;

class FrontierComplex implements Frontier {

	private final double freeY[];

	public FrontierComplex(double freeY, int rangeEnd) {
		this.freeY = new double[rangeEnd + 1];
		for (int i = 0; i <= rangeEnd; i++) {
			this.freeY[i] = freeY;
		}
	}

	private FrontierComplex(double freeY[]) {
		this.freeY = freeY;
	}

	private FrontierComplex(double freeY[], double delta, ParticipantRange range) {
		this(freeY.clone());
		final double newV = getFreeY(range) + delta;
		for (int i = range.start(); i <= range.end(); i++) {
			this.freeY[i] = newV;
		}
	}

	public double getFreeY(ParticipantRange range) {
		if (range == null) {
			throw new IllegalArgumentException();
		}
		double result = freeY[range.start()];
		for (int i = range.start(); i <= range.end(); i++) {
			if (freeY[i] > result) {
				result = freeY[i];
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return Arrays.toString(freeY);
	}

	public FrontierComplex add(double delta, ParticipantRange range) {
		if (range == null) {
			throw new IllegalArgumentException();
		}
		return new FrontierComplex(freeY, delta, range);
	}

	FrontierComplex copy() {
		return new FrontierComplex(freeY.clone());
	}
	
	FrontierComplex mergeMax(FrontierComplex other) {
		if (this.freeY.length != other.freeY.length) {
			throw new IllegalArgumentException();
		}
		final FrontierComplex result = new FrontierComplex(new double[freeY.length]);
		for (int i=0; i<freeY.length; i++) {
			result.freeY[i] = Math.max(this.freeY[i], other.freeY[i]);
		}
		return result;
	}

	// public double diff(Frontier other) {
	// return freeY - other.freeY;
	// }

}
