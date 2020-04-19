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
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class ElementRadioCheckbox extends AbstractElement {

	private static final int RECTANGLE = 10;
	private static final int ELLIPSE = 10;
	private static final int ELLIPSE2 = 4;
	private final TextBlock block;
	private final int margin = 20;
	private final double stroke = 1.5;
	private final boolean radio;
	private final boolean checked;

	public ElementRadioCheckbox(List<String> text, UFont font, boolean radio, boolean checked,
			ISkinSimple spriteContainer) {
		final FontConfiguration config = FontConfiguration.blackBlueTrue(font);
		this.block = Display.create(text).create(config, HorizontalAlignment.LEFT, spriteContainer);
		this.radio = radio;
		this.checked = checked;
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		final Dimension2D dim = block.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, margin, 0);
	}

	public void drawU(UGraphic ug, int zIndex, Dimension2D dimToUse) {
		if (zIndex != 0) {
			return;
		}
		block.drawU(ug.apply(UTranslate.dx(margin)));

		final Dimension2D dim = getPreferredDimension(ug.getStringBounder(), 0, 0);
		final double height = dim.getHeight();

		ug = ug.apply(new UStroke(stroke));
		if (radio) {
			drawRadio(ug, height);
		} else {
			drawOther(ug, height);
		}
	}

	private void drawOther(UGraphic ug, final double height) {
		ug.apply(new UTranslate(2, (height - RECTANGLE) / 2)).draw(new URectangle(RECTANGLE, RECTANGLE));
		if (checked) {
			final UPolygon poly = new UPolygon();
			poly.addPoint(0, 0);
			poly.addPoint(3, 3);
			poly.addPoint(10, -6);
			poly.addPoint(3, 1);
			ug = ug.apply(HColorUtils.changeBack(ug));
			ug = ug.apply(new UTranslate(3, 6));
			ug.draw(poly);
		}
	}

	private void drawRadio(UGraphic ug, final double height) {
		ug.apply(new UTranslate(2, (height - ELLIPSE) / 2)).draw(new UEllipse(ELLIPSE, ELLIPSE));
		if (checked) {
			ug = ug.apply(HColorUtils.changeBack(ug));
			ug = ug.apply(new UTranslate(2 + (ELLIPSE - ELLIPSE2) / 2, (height - ELLIPSE2) / 2));
			ug.draw(new UEllipse(ELLIPSE2, ELLIPSE2));
		}
	}
}
