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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

public class Boundary extends AbstractTextBlock {

	private final double margin = 4;

	private final double radius = 12;
	private final double left = 17;
	
	private final SymbolContext symbolContext;

	public Boundary(SymbolContext symbolContext) {
		this.symbolContext = symbolContext;
	}

	public void drawU(UGraphic ug) {
		double x = 0;
		double y = 0;
		x += margin;
		y += margin;
		ug = symbolContext.apply(ug);
		final UEllipse circle = new UEllipse(radius * 2, radius * 2);
		circle.setDeltaShadow(symbolContext.getDeltaShadow());

		final UPath path1 = new UPath();
		path1.moveTo(0, 0);
		path1.lineTo(0, radius * 2);
		path1.setDeltaShadow(symbolContext.getDeltaShadow());

		final UPath path = new UPath();
		path.moveTo(0, 0);
		path.lineTo(0, radius * 2);
		path.moveTo(0, radius);
		path.lineTo(left, radius);
		path.setDeltaShadow(symbolContext.getDeltaShadow());
		ug.apply(new UTranslate(x, y)).apply(new HColorNone().bg()).draw(path);

		// final ULine line1 = ULine.dy(radius * 2);
		// line1.setDeltaShadow(deltaShadow);
		// ug.apply(new UTranslate(x, y)).draw(line1);
		// final ULine line2 = new ULine(left, 0);
		// line2.setDeltaShadow(deltaShadow);
		// ug.apply(new UTranslate(x, y + radius)).draw(line2);

		ug.apply(new UTranslate(x + left, y)).draw(circle);

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(radius * 2 + left + 2 * margin, radius * 2 + 2 * margin);
	}

}
