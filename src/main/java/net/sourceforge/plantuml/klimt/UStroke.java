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
package net.sourceforge.plantuml.klimt;

public class UStroke implements UChange {

	private final double dashVisible;
	private final double dashSpace;
	private final double thickness;

	// ::comment when __HAXE__

	@Override
	public int hashCode() {
		return Double.hashCode(dashVisible) + Double.hashCode(dashSpace) + Double.hashCode(thickness);
	}

	@Override
	public boolean equals(Object obj) {
		final UStroke other = (UStroke) obj;
		return this.dashVisible == other.dashVisible && this.dashSpace == other.dashSpace
				&& this.thickness == other.thickness;
	}

	// ::done

	@Override
	public String toString() {
		return "" + dashVisible + "-" + dashSpace + "-" + thickness;
	}

	public UStroke(double dashVisible, double dashSpace, double thickness) {
		this.dashVisible = dashVisible;
		this.dashSpace = dashSpace;
		this.thickness = thickness;
	}

	public static UStroke withThickness(double thickness) {
		return new UStroke(0, 0, thickness);
	}

	public static UStroke simple() {
		return new UStroke(0, 0, 1.0);
	}

	public UStroke onlyThickness() {
		return new UStroke(0, 0, thickness);
	}

	public double getDashVisible() {
		return dashVisible;
	}

	public double getDashSpace() {
		return dashSpace;
	}

	public double getThickness() {
		return thickness;
	}

	public String getDasharraySvg() {
		if (dashVisible == 0)
			return null;

		return "" + dashVisible + "," + dashSpace;
	}

	public String getDashTikz() {
		if (dashVisible == 0) {
			return null;
		}
		return "on " + dashVisible + "pt off " + dashSpace + "pt";
	}

	// public String getDasharrayEps() {
	// if (dashVisible == 0) {
	// return null;
	// }
	// return "" + dashVisible + " " + dashSpace;
	// }

}
