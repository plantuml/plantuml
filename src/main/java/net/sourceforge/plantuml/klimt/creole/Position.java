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
package net.sourceforge.plantuml.klimt.creole;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.URectangle;

public class Position {

	private final double x;
	private final double y;
	private final XDimension2D dim;

	public Position(double x, double y, XDimension2D dim) {
		this.x = x;
		this.y = y;
		this.dim = dim;
//		if (dim.getHeight() == 0) {
//			throw new IllegalArgumentException();
//		}
//		if (dim.getWidth() == 0) {
//			throw new IllegalArgumentException();
//		}
	}

	@Override
	public String toString() {
		return "x=" + x + " y=" + y + " dim=" + dim;
	}

	public Position align(double height) {
		final double dy = height - dim.getHeight();
		return translateY(dy);
	}

	public final double getMinY() {
		return y;
	}

	public final double getMaxY() {
		return y + getHeight();
	}

	public UGraphic translate(UGraphic ug) {
		return ug.apply(new UTranslate(x, y));
	}

	public Position translateY(double dy) {
		return new Position(x, y + dy, dim);
	}

	public Position translateX(double dx) {
		return new Position(x + dx, y, dim);
	}

	public MinMax update(MinMax minMax) {
		return minMax.addPoint(x + dim.getWidth(), y + dim.getHeight());
	}

	public void drawDebug(UGraphic ug) {
		ug = ug.apply(HColors.BLACK).apply(HColors.none().bg());
		ug = ug.apply(new UTranslate(x, y));
		ug.draw(URectangle.build(dim));
	}

	public double getHeight() {
		return dim.getHeight();
	}

	public double getWidth() {
		return dim.getWidth();
	}

	public UTranslate getTranslate() {
		return new UTranslate(x, y);
	}

}
