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
 * Revision $Revision: 6590 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowPart;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ComponentRoseArrow extends AbstractComponentRoseArrow {

	private final HorizontalAlignement messagePosition;

	public ComponentRoseArrow(HtmlColor foregroundColor, HtmlColor fontColor, UFont font,
			List<? extends CharSequence> stringsToDisplay, ArrowConfiguration arrowConfiguration,
			HorizontalAlignement messagePosition) {
		super(foregroundColor, fontColor, font, stringsToDisplay, arrowConfiguration);
		this.messagePosition = messagePosition;
	}

	@Override
	public void drawInternalU(UGraphic ug, Dimension2D dimensionToUse) {
		final StringBounder stringBounder = ug.getStringBounder();
		final int textHeight = (int) getTextHeight(stringBounder);
		ug.getParam().setColor(getForegroundColor());

		final int x2 = (int) dimensionToUse.getWidth();

		if (getArrowConfiguration().isDotted()) {
			stroke(ug, 2, 2);
		}

		//
		ug.draw(0, textHeight, new ULine(x2, 0));
		if (getArrowConfiguration().isDotted()) {
			ug.getParam().setStroke(new UStroke());
		}
		final int direction = getDirection();
		if (direction == 1) {
			if (getArrowConfiguration().isASync()) {
				if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
					ug.draw(x2, textHeight, new ULine(-getArrowDeltaX(), -getArrowDeltaY()));
				}
				if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
					ug.draw(x2, textHeight, new ULine(-getArrowDeltaX(), getArrowDeltaY()));
				}
			} else {
				ug.getParam().setBackcolor(getForegroundColor());
				final UPolygon polygon = getPolygonNormal(textHeight, x2);
				ug.draw(0, 0, polygon);
				ug.getParam().setBackcolor(null);
			}
		} else {
			if (getArrowConfiguration().isASync()) {
				if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
					ug.draw(0, textHeight, new ULine(getArrowDeltaX(), -getArrowDeltaY()));
				}
				if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
					ug.draw(0, textHeight, new ULine(getArrowDeltaX(), getArrowDeltaY()));
				}
			} else {
				ug.getParam().setBackcolor(getForegroundColor());
				final UPolygon polygon = getPolygonReverse(textHeight);
				ug.draw(0, 0, polygon);
				ug.getParam().setBackcolor(null);
			}
		}
		final double textPos;
		if (messagePosition == HorizontalAlignement.CENTER) {
			final double textWidth = getTextBlock().calculateDimension(stringBounder).getWidth();
			textPos = (dimensionToUse.getWidth() - textWidth) / 2;
		} else if (messagePosition == HorizontalAlignement.RIGHT) {
			final double textWidth = getTextBlock().calculateDimension(stringBounder).getWidth();
			textPos = dimensionToUse.getWidth() - textWidth - getMarginX2();
		} else {
			textPos = getMarginX1();
		}
		getTextBlock().drawU(ug, textPos, 0);
	}

	private UPolygon getPolygonNormal(final int textHeight, final int x2) {
		final UPolygon polygon = new UPolygon();
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
		return polygon;
	}

	private UPolygon getPolygonReverse(final int textHeight) {
		final UPolygon polygon = new UPolygon();
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
		return polygon;
	}

	public Point2D getStartPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(stringBounder);
		if (getDirection() == 1) {
			return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
		}
		return new Point2D.Double(dimensionToUse.getWidth() + getPaddingX(), textHeight + getPaddingY());
	}

	public Point2D getEndPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(stringBounder);
		if (getDirection() == 1) {
			return new Point2D.Double(dimensionToUse.getWidth() + getPaddingX(), textHeight + getPaddingY());
		}
		return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
	}

	final protected int getDirection() {
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
