/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 3835 $
 *
 */
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.graphic.HtmlColorSet;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ElementButton extends AbstractElementText implements Element {

	private final double stroke = 2.5;
	private final double marginX = 2;
	private final double marginY = 2;

	public ElementButton(String text, UFont font, ISkinSimple spriteContainer) {
		super(text, font, true, spriteContainer);
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		Dimension2D dim = getTextDimensionAt(stringBounder, x + stroke + marginX);
		dim = Dimension2DDouble.delta(dim, 2 * marginX, 2 * marginY);
		return Dimension2DDouble.delta(dim, 2 * stroke);
	}

	public void drawU(UGraphic ug, int zIndex, Dimension2D dimToUse) {
		if (zIndex != 0) {
			return;
		}
		final Dimension2D dim = getPreferredDimension(ug.getStringBounder(), 0, 0);
		ug = ug.apply(new UStroke(stroke));
		ug.apply(new UChangeBackColor(HtmlColorSet.getInstance().getColorIfValid("#EEEEEE"))).apply(new UTranslate(stroke, stroke))
				.draw(new URectangle(dim.getWidth() - 2 * stroke, dim.getHeight() - 2 * stroke, 10, 10));
		final Dimension2D dimPureText = getPureTextDimension(ug.getStringBounder());
		drawText(ug, (dim.getWidth() - dimPureText.getWidth()) / 2, stroke + marginY);
	}
}
