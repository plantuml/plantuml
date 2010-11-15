/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 5289 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

public class UStroke {

	private final int dash;
	private final double thickness;

	public UStroke(int dash, double thickness) {
		this.dash = dash;
		this.thickness = thickness;
	}

	public UStroke(double thickness) {
		this(0, thickness);
	}

	public UStroke() {
		this(1.0);
	}

	public int getDash() {
		return dash;
	}

	public double getThickness() {
		return thickness;
	}

	public String getDasharraySvg() {
		if (dash == 0) {
			return null;
		}
		return "" + dash + "," + dash;
	}

	public String getDasharrayEps() {
		if (dash == 0) {
			return null;
		}
		return "" + dash + " " + (2*dash);
	}

}
