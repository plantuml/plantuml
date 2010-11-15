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
 * Revision $Revision: 3836 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import net.sourceforge.plantuml.graphic.StringBounder;

public class VirtualHBar implements Pushable {

	private static int CPT = 0;

	private final double width;
	private final VirtualHBarType type;
	private double x;
	private final double startingY;
	private double endingY;

	private int cpt = CPT++;

	public VirtualHBar(double width, VirtualHBarType type, double startingY) {
		this.width = width;
		this.type = type;
		this.x = width / 2;
		this.startingY = startingY;
	}

	@Override
	public String toString() {
		return "VHB" + cpt + " " + x + " " + startingY + "-" + endingY;
	}

	public VirtualHBarType getType() {
		return type;
	}

	public double getWidth() {
		return width;
	}

	public double getCenterX(StringBounder stringBounder) {
		return x;
	}

	public void pushToLeft(double deltaX) {
		x += deltaX;
	}

	public final double getEndingY() {
		return endingY;
	}

	public final void setEndingY(double endingY) {
		if (endingY <= startingY) {
			throw new IllegalArgumentException();
		}
		this.endingY = endingY;
	}

	public final double getStartingY() {
		return startingY;
	}

	public boolean canBeOnTheSameLine(VirtualHBar bar) {
		if (this.x != bar.x) {
			return false;
		}
		if (this.startingY >= bar.endingY) {
			return true;
		}
		if (this.endingY <= bar.startingY) {
			return true;
		}
		return false;
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

}