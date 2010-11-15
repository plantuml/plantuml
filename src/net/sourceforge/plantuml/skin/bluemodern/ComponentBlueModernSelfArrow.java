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
 * Revision $Revision: 4634 $
 *
 */
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ComponentBlueModernSelfArrow extends AbstractComponentBlueModernArrow {

	private final double arrowWidth = 45;

	public ComponentBlueModernSelfArrow(Color foregroundColor, Color colorFont, Font font,
			List<? extends CharSequence> stringsToDisplay, boolean dotted, boolean full) {
		super(foregroundColor, colorFont, font, stringsToDisplay, dotted, full);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Dimension2D dimensionToUse) {
		final StringBounder stringBounder = ug.getStringBounder();
		final int textHeight = (int) getTextHeight(stringBounder);

		ug.getParam().setBackcolor(getForegroundColor());
		ug.getParam().setColor(getForegroundColor());
		final int x2 = (int) arrowWidth;

		if (isDotted()) {
			stroke(ug, 5, 2);
		} else {
			ug.getParam().setStroke(new UStroke(2));
		}

		ug.draw(0, textHeight, new ULine(x2, 0));

		final int textAndArrowHeight = (int) (textHeight + getArrowOnlyHeight(stringBounder));

		ug.draw(x2, textHeight, new ULine(0, textAndArrowHeight - textHeight));
		ug.draw(x2, textAndArrowHeight, new ULine(2 - x2, 0));

		ug.getParam().setStroke(new UStroke());

		final int delta = (int) getArrowOnlyHeight(stringBounder);

		if (isFull()) {
			final UPolygon polygon = new UPolygon();
			polygon.addPoint(getArrowDeltaX(), textHeight - getArrowDeltaY() + delta);
			polygon.addPoint(0, textHeight + delta);
			polygon.addPoint(getArrowDeltaX(), textHeight + getArrowDeltaY() + delta);
			ug.draw(0, 0, polygon);
		} else {
			ug.getParam().setStroke(new UStroke(1.5));
			ug.draw(getArrowDeltaX2(), textHeight - getArrowDeltaY2() + delta, new ULine(-getArrowDeltaX2(),
					getArrowDeltaY2()));
			ug.draw(getArrowDeltaX2(), textHeight + getArrowDeltaY2() + delta, new ULine(-getArrowDeltaX2(),
					-getArrowDeltaY2()));
			ug.getParam().setStroke(new UStroke());
		}

		getTextBlock().drawU(ug, getMarginX1(), 0);

	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + getArrowDeltaY() + getArrowOnlyHeight(stringBounder) + 2 * getPaddingY();
	}

	private double getArrowOnlyHeight(StringBounder stringBounder) {
		return 13;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return Math.max(getTextWidth(stringBounder), arrowWidth);
	}

}
