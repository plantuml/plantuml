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
package net.sourceforge.plantuml.descdiagram;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UCenteredCharacter;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

public class BoxedCharacter extends AbstractTextBlock implements TextBlock {

	private final String c;
	private final UFont font;
	private final HColor innerCircle;
	private final HColor circle;
	private final HColor fontColor;
	private final double radius;

	public BoxedCharacter(char c, double size, UFont font, HColor innerCircle, HColor circle, HColor fontColor) {
		this.c = "" + c;
		this.font = font;
		this.innerCircle = innerCircle;
		this.circle = circle;
		this.fontColor = fontColor;
		this.radius = size;
	}

	public void drawU(UGraphic ug) {
		if (circle != null) {
			ug = ug.apply(circle);
		}
		if (innerCircle == null) {
			ug = ug.apply(new HColorNone().bg());
		} else {
			ug = ug.apply(innerCircle.bg());
		}
		ug.draw(new URectangle(2 * radius, 2 * radius));
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

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(getPreferredWidth(stringBounder), getPreferredHeight(stringBounder));
	}
}
