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
 * Revision $Revision: 7170 $
 *
 */
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowPart;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ComponentBlueModernArrow extends AbstractComponentBlueModernArrow {

	public ComponentBlueModernArrow(HtmlColor foregroundColor, HtmlColor fontColor, UFont font,
			List<? extends CharSequence> stringsToDisplay, ArrowConfiguration arrowConfiguration) {
		super(foregroundColor, fontColor, font, stringsToDisplay, arrowConfiguration);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Dimension2D dimensionToUse, boolean withShadow) {
		final StringBounder stringBounder = ug.getStringBounder();
		final int textHeight = (int) getTextHeight(stringBounder);

		ug.getParam().setColor(getForegroundColor());
		ug.getParam().setBackcolor(getForegroundColor());

		final int x2 = (int) dimensionToUse.getWidth();

		if (getArrowConfiguration().isDotted()) {
			stroke(ug, 5, 2);
		} else {
			ug.getParam().setStroke(new UStroke(2));
		}

		ug.draw(2, textHeight, new ULine(x2 - 4, 0));
		ug.getParam().setStroke(new UStroke());

		final int direction = getDirection();
		final UPolygon polygon = new UPolygon();

		if (getArrowConfiguration().isASync()) {
			ug.getParam().setStroke(new UStroke(1.5));
			if (direction == 1) {
				if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
					ug.draw(x2 - getArrowDeltaX2(), textHeight - getArrowDeltaY2(), new ULine(getArrowDeltaX2(),
							getArrowDeltaY2()));
				}
				if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
					ug.draw(x2 - getArrowDeltaX2(), textHeight + getArrowDeltaY2(), new ULine(getArrowDeltaX2(),
							-getArrowDeltaY2()));
				}
			} else {
				if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
					ug.draw(getArrowDeltaX2(), textHeight - getArrowDeltaY2(), new ULine(-getArrowDeltaX2(),
							getArrowDeltaY2()));
				}
				if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
					ug.draw(getArrowDeltaX2(), textHeight + getArrowDeltaY2(), new ULine(-getArrowDeltaX2(),
							-getArrowDeltaY2()));
				}
			}
			ug.getParam().setStroke(new UStroke());
		} else if (direction == 1) {
			createPolygonNormal(textHeight, x2, polygon);
		} else {
			createPolygonReverse(textHeight, polygon);
		}
		ug.draw(0, 0, polygon);

		getTextBlock().drawU(ug, getMarginX1(), 0);
	}

	private void createPolygonReverse(final int textHeight, final UPolygon polygon) {
		if (getArrowConfiguration().getPart() == ArrowPart.TOP_PART) {
			polygon.addPoint(getArrowDeltaX(), textHeight - getArrowDeltaY());
			polygon.addPoint(0, textHeight);
			polygon.addPoint(getArrowDeltaX(), textHeight);
		} else if (getArrowConfiguration().getPart() == ArrowPart.BOTTOM_PART) {
			polygon.addPoint(getArrowDeltaX(), textHeight);
			polygon.addPoint(0, textHeight);
			polygon.addPoint(getArrowDeltaX(), textHeight + getArrowDeltaY());
		} else {
			polygon.addPoint(getArrowDeltaX(), textHeight - getArrowDeltaY());
			polygon.addPoint(0, textHeight);
			polygon.addPoint(getArrowDeltaX(), textHeight + getArrowDeltaY());
		}
	}

	private void createPolygonNormal(final int textHeight, final int x2, final UPolygon polygon) {
		if (getArrowConfiguration().getPart() == ArrowPart.TOP_PART) {
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight - getArrowDeltaY());
			polygon.addPoint(x2, textHeight);
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight);
		} else if (getArrowConfiguration().getPart() == ArrowPart.BOTTOM_PART) {
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight);
			polygon.addPoint(x2, textHeight);
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight + getArrowDeltaY());
		} else {
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight - getArrowDeltaY());
			polygon.addPoint(x2, textHeight);
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight + getArrowDeltaY());
		}
	}

	protected int getDirection(Graphics2D g2d) {
		return getDirection();
	}

	protected int getDirection() {
		if (getArrowConfiguration().isLeftToRightNormal()) {
			return 1;
		}
		if (getArrowConfiguration().isRightToLeftReverse()) {
			return -1;
		}
		throw new IllegalStateException();
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + getArrowDeltaY() + 2 * getPaddingY();
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder);
	}

}
