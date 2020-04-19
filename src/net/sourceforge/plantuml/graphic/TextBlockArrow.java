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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class TextBlockArrow extends AbstractTextBlock implements TextBlock {

	private final double size;
	private final Direction arrow;
	private final HColor color;

	public TextBlockArrow(Direction arrow, FontConfiguration fontConfiguration) {
		if (arrow == null) {
			throw new IllegalArgumentException();
		}
		this.arrow = arrow;
		// this.size = fontConfiguration.getFont().getSize2D() * 0 + 30;
		this.size = fontConfiguration.getFont().getSize2D();
		this.color = fontConfiguration.getColor();

	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(color.bg());
		ug = ug.apply(color);
		int triSize = (int) (size * .8 - 3);
		if (triSize % 2 == 1) {
			triSize--;
		}
		final UPolygon triangle = getTriangle(triSize);
		if (arrow == Direction.RIGHT || arrow == Direction.LEFT) {
			ug.apply(new UTranslate(2, (size - triSize) - 2)).draw(triangle);
		} else {
			ug.apply(new UTranslate(2, (size - triSize) - 2)).draw(triangle);
		}
	}

	private UPolygon getTriangle(int triSize) {
		final UPolygon triangle = new UPolygon();
		if (arrow == Direction.RIGHT) {
			triangle.addPoint(0, 0);
			triangle.addPoint(triSize, triSize / 2);
			triangle.addPoint(0, triSize);
			triangle.addPoint(0, 0);
		} else if (arrow == Direction.LEFT) {
			triangle.addPoint(triSize, 0);
			triangle.addPoint(0, triSize / 2);
			triangle.addPoint(triSize, triSize);
			triangle.addPoint(triSize, 0);
		} else if (arrow == Direction.UP) {
			triangle.addPoint(0, triSize);
			triangle.addPoint(triSize / 2, 0);
			triangle.addPoint(triSize, triSize);
			triangle.addPoint(0, triSize);
		} else if (arrow == Direction.DOWN) {
			triangle.addPoint(0, 0);
			triangle.addPoint(triSize / 2, triSize);
			triangle.addPoint(triSize, 0);
			triangle.addPoint(0, 0);
		} else {
			throw new IllegalStateException();
		}
		return triangle;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(size, size);
	}
}