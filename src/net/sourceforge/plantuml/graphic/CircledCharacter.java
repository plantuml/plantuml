/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.awt.geom.Dimension2D;
import net.sourceforge.plantuml.ugraphic.UCenteredCharacter;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorAutomatic;

public class CircledCharacter extends AbstractTextBlock implements TextBlock {

	private final String c;
	private final UFont font;
	private final HColor spotBackColor;
	private final HColor spotBorder;
	private final HColor fontColor;
	private final double radius;

	public CircledCharacter(char c, double radius, UFont font, HColor spotBackColor, HColor spotBorder,
			HColor fontColor) {
		this.c = "" + c;
		this.radius = radius;
		this.font = font;
		this.spotBackColor = spotBackColor;
		this.spotBorder = spotBorder;
		if (fontColor instanceof HColorAutomatic)
			this.fontColor = ((HColorAutomatic) fontColor).getAppropriateColor(spotBackColor);
		else
			this.fontColor = fontColor;
	}

	public void drawU(UGraphic ug) {
		if (spotBorder != null)
			ug = ug.apply(spotBorder);

		ug = ug.apply(spotBackColor.bg());
		ug.draw(new UEllipse(radius * 2, radius * 2));
		ug = ug.apply(fontColor);
		ug = ug.apply(new UTranslate(radius, radius));
		ug.draw(new UCenteredCharacter(c.charAt(0), font));
	}

	final public double getPreferredWidth(StringBounder stringBounder) {
		return 2 * radius;
	}

	final public double getPreferredHeight(StringBounder stringBounder) {
		return 2 * radius;
	}

	// private PathIterator getPathIteratorCharacter(FontRenderContext frc) {
	// final TextLayout textLayout = new TextLayout(c, font.getFont(), frc);
	// final Shape s = textLayout.getOutline(null);
	// return s.getPathIterator(null);
	// }
	//
	// private UPath getUPath(FontRenderContext frc) {
	// final UPath result = new UPath();
	//
	// final PathIterator path = getPathIteratorCharacter(frc);
	//
	// final double coord[] = new double[6];
	// while (path.isDone() == false) {
	// final int code = path.currentSegment(coord);
	// result.add(coord, USegmentType.getByCode(code));
	// path.next();
	// }
	//
	// return result;
	// }

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(getPreferredWidth(stringBounder), getPreferredHeight(stringBounder));
	}
}
