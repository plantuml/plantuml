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
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ElementRadioCheckbox implements Element {

	private static final int RECTANGLE = 10;
	private static final int ELLIPSE = 10;
	private static final int ELLIPSE2 = 4;
	private final TextBlock block;
	private final int margin = 20;
	private final double stroke = 1.5;
	private final boolean radio;
	private final boolean checked;

	public ElementRadioCheckbox(List<String> text, UFont font, boolean radio, boolean checked) {
		final FontConfiguration config = new FontConfiguration(font, HtmlColor.BLACK);
		this.block = TextBlockUtils.create(text, config, HorizontalAlignement.LEFT);
		this.radio = radio;
		this.checked = checked;
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		final Dimension2D dim = block.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, margin, 0);
	}

	public void drawU(UGraphic ug, double x, double y, int zIndex, Dimension2D dimToUse) {
		if (zIndex != 0) {
			return;
		}
		block.drawU(ug, x + margin, y);

		final Dimension2D dim = getPreferredDimension(ug.getStringBounder(), 0, 0);
		final double height = dim.getHeight();

		ug.getParam().setStroke(new UStroke(stroke));
		if (radio) {
			ug.draw(x + 2, y + (height - ELLIPSE) / 2, new UEllipse(ELLIPSE, ELLIPSE));
			if (checked) {
				ug.getParam().setBackcolor(ug.getParam().getColor());
				ug
						.draw(x + 2 + (ELLIPSE - ELLIPSE2) / 2, y + (height - ELLIPSE2) / 2, new UEllipse(ELLIPSE2,
								ELLIPSE2));
				ug.getParam().setBackcolor(null);
			}
		} else {
			ug.draw(x + 2, y + (height - RECTANGLE) / 2, new URectangle(RECTANGLE, RECTANGLE));
			if (checked) {
				ug.getParam().setBackcolor(ug.getParam().getColor());
				final UPolygon poly = new UPolygon();
				poly.addPoint(0, 0);
				poly.addPoint(3, 3);
				poly.addPoint(10, -6);
				poly.addPoint(3, 1);
				ug.draw(x + 3, y + 6, poly);
				ug.getParam().setBackcolor(null);
			}
		}
		ug.getParam().setStroke(new UStroke());

		// ug.getParam().setColor(HtmlColor.BLACK);
		// final Dimension2D dim = getDimension(ug.getStringBounder());
		// ug.getParam().setStroke(new UStroke(stroke));
		// ug.draw(x, y, new URectangle(dim.getWidth() - 2 * stroke,
		// dim.getHeight() - 2 * stroke, 10, 10));
		// ug.getParam().setStroke(new UStroke());
	}
}
