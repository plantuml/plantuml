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
package net.sourceforge.plantuml.openiconic;

public class SvgPosition {

	final private SvgCommandNumber x;
	final private SvgCommandNumber y;

	public SvgPosition() {
		this(new SvgCommandNumber("0"), new SvgCommandNumber("0"));
	}

	public SvgPosition(SvgCommandNumber x, SvgCommandNumber y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return x.toSvg() + "," + y.toSvg();
	}

	public SvgPosition(double x, double y) {
		this.x = new SvgCommandNumber(x);
		this.y = new SvgCommandNumber(y);
	}

	public SvgCommandNumber getX() {
		return x;
	}

	public SvgCommandNumber getY() {
		return y;
	}

	public double getXDouble() {
		return x.getDouble();
	}

	public double getYDouble() {
		return y.getDouble();
	}

	public SvgPosition add(SvgPosition other) {
		return new SvgPosition(x.add(other.x), y.add(other.y));
	}

	public SvgPosition getMirror(SvgPosition tobeMirrored) {
		final double centerX = getXDouble();
		final double centerY = getYDouble();
		// x1+x2 = 2*xc
		final double x = 2 * centerX - tobeMirrored.getXDouble();
		final double y = 2 * centerY - tobeMirrored.getYDouble();
		return new SvgPosition(x, y);
	}
}
