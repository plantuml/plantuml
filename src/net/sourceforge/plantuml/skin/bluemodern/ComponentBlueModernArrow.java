/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.ArrowPart;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentBlueModernArrow extends AbstractComponentBlueModernArrow {

	public ComponentBlueModernArrow(HtmlColor foregroundColor, boolean useUnderlineForHyperlink,
			FontConfiguration font, Display stringsToDisplay, ArrowConfiguration arrowConfiguration,
			ISkinSimple spriteContainer) {
		super(foregroundColor, font, stringsToDisplay, arrowConfiguration, spriteContainer);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		if (getArrowConfiguration().isHidden()) {
			return;
		}
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final StringBounder stringBounder = ug.getStringBounder();
		final int textHeight = (int) getTextHeight(stringBounder);

		ug = ug.apply(new UChangeColor(getForegroundColor()));
		ug = ug.apply(new UChangeBackColor(getForegroundColor()));

		final int x2 = (int) dimensionToUse.getWidth();

		if (getArrowConfiguration().isDotted()) {
			ug = ArrowConfiguration.stroke(ug, 5, 2, 1);
		} else {
			ug = ug.apply(new UStroke(2));
		}

		ug.apply(new UTranslate(2, textHeight)).draw(new ULine(x2 - 4, 0));
		ug = ug.apply(new UStroke());

		final int direction = getDirection();
		final UPolygon polygon = new UPolygon();

		if (getArrowConfiguration().isAsync()) {
			ug = ug.apply(new UStroke(1.5));
			if (direction == 1) {
				if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
					ug.apply(new UTranslate(x2 - getArrowDeltaX2(), textHeight - getArrowDeltaY2())).draw(
							new ULine(getArrowDeltaX2(), getArrowDeltaY2()));
				}
				if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
					ug.apply(new UTranslate(x2 - getArrowDeltaX2(), textHeight + getArrowDeltaY2())).draw(
							new ULine(getArrowDeltaX2(), -getArrowDeltaY2()));
				}
			} else {
				if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
					ug.apply(new UTranslate(getArrowDeltaX2(), textHeight - getArrowDeltaY2())).draw(
							new ULine(-getArrowDeltaX2(), getArrowDeltaY2()));
				}
				if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
					ug.apply(new UTranslate(getArrowDeltaX2(), textHeight + getArrowDeltaY2())).draw(
							new ULine(-getArrowDeltaX2(), -getArrowDeltaY2()));
				}
			}
			ug = ug.apply(new UStroke());
		} else if (direction == 1) {
			createPolygonNormal(textHeight, x2, polygon);
		} else {
			createPolygonReverse(textHeight, polygon);
		}
		ug.draw(polygon);

		getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), 0)));
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
		if (getArrowConfiguration().getArrowDirection() == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
			return 1;
		}
		if (getArrowConfiguration().getArrowDirection() == ArrowDirection.RIGHT_TO_LEFT_REVERSE) {
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

	public Point2D getStartPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(stringBounder);
		if (getArrowConfiguration().getArrowDirection() == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
			return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
		}
		return new Point2D.Double(dimensionToUse.getWidth() + getPaddingX(), textHeight + getPaddingY());
	}

	public Point2D getEndPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(stringBounder);
		if (getArrowConfiguration().getArrowDirection() == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
			return new Point2D.Double(dimensionToUse.getWidth() + getPaddingX(), textHeight + getPaddingY());
		}
		return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
	}

}
