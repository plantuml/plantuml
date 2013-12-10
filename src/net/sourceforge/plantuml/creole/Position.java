/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 11025 $
 *
 */
package net.sourceforge.plantuml.creole;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Position {

	private final double x;
	private final double y;
	private final Dimension2D dim;

	public Position(double x, double y, Dimension2D dim) {
		this.x = x;
		this.y = y;
		this.dim = dim;
		if (dim.getHeight() == 0) {
			throw new IllegalArgumentException();
		}
		if (dim.getWidth() == 0) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString() {
		return "x=" + x + " y=" + y + " dim=" + dim;
	}

	public Position align(double height) {
		final double dy = height - dim.getHeight();
		return translateY(dy);
	}

	// public final double getX() {
	// return x;
	// }
	//
	public final double getMinY() {
		return y;
	}

	public final double getMaxY() {
		return y + getHeight();
	}

	//
	// public final Dimension2D getDim() {
	// return dim;
	// }
	//
	public UGraphic translate(UGraphic ug) {
		return ug.apply(new UTranslate(x, y));
	}

	public Position translateY(double dy) {
		return new Position(x, y + dy, dim);
	}

	public MinMax update(MinMax minMax) {
		return minMax.addPoint(x + dim.getWidth(), y + dim.getHeight());
	}

	public void drawDebug(UGraphic ug) {
		// ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK)).apply(new UChangeBackColor(HtmlColorUtils.LIGHT_GRAY));
		ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK)).apply(new UChangeBackColor(null));
		ug = ug.apply(new UTranslate(x, y));
		ug.draw(new URectangle(dim));
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
