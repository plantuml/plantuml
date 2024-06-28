/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class Area {
    // ::remove folder when __HAXE__

	private final XDimension2D dimensionToUse;
	private double deltaX1;
	private double liveDeltaSize = 0.0;
	private int level = 0;
	private double textDeltaX = 0.0;

	@Override
	public String toString() {
		return dimensionToUse.toString() + " (" + deltaX1 + ")";
	}

	public Area(XDimension2D dimensionToUse) {
		this.dimensionToUse = dimensionToUse;
	}

	public static Area create(double with, double height) {
		return new Area(new XDimension2D(with, height));
	}

	public XDimension2D getDimensionToUse() {
		return dimensionToUse;
	}

	public void setDeltaX1(double deltaX1) {
		this.deltaX1 = deltaX1;
	}

	public final double getDeltaX1() {
		return deltaX1;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setLiveDeltaSize(double liveDeltaSize) {
		this.liveDeltaSize = liveDeltaSize;
	}

	public double getLiveDeltaSize() {
		return liveDeltaSize;
	}

	public double getTextDeltaX() {
		return textDeltaX;
	}

	public void setTextDeltaX(double textDeltaX) {
		this.textDeltaX = textDeltaX;
	}
}
