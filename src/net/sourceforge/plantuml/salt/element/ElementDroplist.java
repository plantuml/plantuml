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
 * Revision $Revision: 3835 $
 *
 */
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;

public class ElementDroplist extends AbstractElementText implements Element {

	private final int box = 12;

	public ElementDroplist(String text, UFont font) {
		super(text, font, true);
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		final Dimension2D dim = getTextDimensionAt(stringBounder, x + 2);
		return Dimension2DDouble.delta(dim, 4 + box, 4);
	}

	public void drawU(UGraphic ug, double x, double y, int zIndex, Dimension2D dimToUse) {
		if (zIndex != 0) {
			return;
		}
		drawText(ug, x + 2, y + 2);
		final Dimension2D dim = getPreferredDimension(ug.getStringBounder(), 0, 0);
		ug.draw(x, y, new URectangle(dim.getWidth() - 1, dim.getHeight() - 1));
		final double xline = dim.getWidth() - box;
		ug.draw(x + xline, y, new ULine(0, dim.getHeight() - 1));

		final UPolygon poly = new UPolygon();
		poly.addPoint(0, 0);
		poly.addPoint(box - 6, 0);
		final Dimension2D dimText = getPureTextDimension(ug.getStringBounder());
		poly.addPoint((box - 6) / 2, dimText.getHeight() - 8);
		ug.getParam().setBackcolor(ug.getParam().getColor());

		ug.draw(x + xline + 3, y + 6, poly);
		ug.getParam().setBackcolor(null);
	}
}
