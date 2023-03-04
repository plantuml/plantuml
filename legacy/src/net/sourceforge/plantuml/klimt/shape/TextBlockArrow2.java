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
package net.sourceforge.plantuml.klimt.shape;

import java.util.Objects;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.svek.GuideLine;

public class TextBlockArrow2 extends AbstractTextBlock implements TextBlock {
    // ::remove file when __HAXE__

	private final double size;
	private final GuideLine angle;
	private final HColor color;

	public TextBlockArrow2(GuideLine angle, FontConfiguration fontConfiguration) {
		this.angle = Objects.requireNonNull(angle);
		this.size = fontConfiguration.getFont().getSize2D();
		this.color = fontConfiguration.getColor();

	}

	public void drawU(UGraphic ug) {
		// final double triSize = size * .80;
		final int triSize = (int) (size * .80);

		ug = ug.apply(color);
		ug = ug.apply(color.bg());
		ug = ug.apply(new UTranslate(triSize / 2, size / 2));

		final UPolygon triangle = new UPolygon();
		final double beta = Math.PI * 4 / 5;
		triangle.addPoint(getPoint(triSize / 2, angle.getArrowDirection2()));
		triangle.addPoint(getPoint(triSize / 2, angle.getArrowDirection2() + beta));
		triangle.addPoint(getPoint(triSize / 2, angle.getArrowDirection2() - beta));
		triangle.addPoint(getPoint(triSize / 2, angle.getArrowDirection2()));
		ug.draw(triangle);
	}

	private XPoint2D getPoint(double len, double alpha) {
		final double dx = len * Math.sin(alpha);
		final double dy = len * Math.cos(alpha);
		return new XPoint2D(dx, dy);
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return new XDimension2D(size, size);
	}
}