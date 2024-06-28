/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.skin.rose;

import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.ArrowDressing;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.ArrowPart;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.style.Style;

public class ComponentRoseArrow extends AbstractComponentRoseArrow {

	private final HorizontalAlignment messagePosition;
	private final boolean niceArrow;
	private final boolean belowForResponse;
	private final int inclination1;
	private final int inclination2;

	public ComponentRoseArrow(Style style, Display stringsToDisplay, ArrowConfiguration arrowConfiguration,
			HorizontalAlignment messagePosition, ISkinSimple spriteContainer, LineBreakStrategy maxMessageSize,
			boolean niceArrow, boolean belowForResponse) {
		super(style, stringsToDisplay, arrowConfiguration, spriteContainer, maxMessageSize);
		this.messagePosition = messagePosition;
		this.niceArrow = niceArrow;
		this.belowForResponse = belowForResponse;
		this.inclination1 = arrowConfiguration.getInclination1();
		this.inclination2 = arrowConfiguration.getInclination2();
	}

	public static final double spaceCrossX = 6;
	public static final double diamCircle = 8;
	public static final double thinCircle = 1.5;

	@Override
	public void drawInternalU(UGraphic ug, Area area) {
		final ArrowConfiguration arrowConfiguration = getArrowConfiguration();
		if (arrowConfiguration.isHidden())
			return;

		final XDimension2D dimensionToUse = area.getDimensionToUse();
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(getForegroundColor());

		final ArrowDressing dressing1 = arrowConfiguration.getDressing1();
		final ArrowDressing dressing2 = arrowConfiguration.getDressing2();

		double start = 0;
		double len = dimensionToUse.getWidth() - 1;
		final double lenFull = dimensionToUse.getWidth();

		final double pos1 = start + 1;
		final double pos2 = len - 1;


		if (arrowConfiguration.getDecoration2() == ArrowDecoration.CIRCLE) {
			if (dressing2.getHead() == ArrowHead.NONE)
				len -= diamCircle / 2;

			if (dressing2.getHead() != ArrowHead.NONE)
				len -= diamCircle / 2 + thinCircle;
		}

		if (arrowConfiguration.getDecoration1() == ArrowDecoration.CIRCLE) {
			if (dressing1.getHead() == ArrowHead.NONE) {
				start += diamCircle / 2 ;
				len -= diamCircle / 2;
			}
			if (dressing1.getHead() == ArrowHead.ASYNC) {
				start += diamCircle / 2 +thinCircle;
				len -= diamCircle / 2 +thinCircle;
			}
			if (dressing1.getHead() == ArrowHead.NORMAL) {
				start += diamCircle / 2 +thinCircle;
				len -= diamCircle / 2 +thinCircle;
			}
		}

		if (dressing2.getPart() == ArrowPart.FULL && dressing2.getHead() == ArrowHead.NORMAL)
			len -= getArrowDeltaX() / 2;

		if (dressing1.getPart() == ArrowPart.FULL && dressing1.getHead() == ArrowHead.NORMAL) {
			start += getArrowDeltaX() / 2;
			len -= getArrowDeltaX() / 2;
		}

		if (dressing2.getHead() == ArrowHead.CROSSX)
			len -= 2 * spaceCrossX;

		if (dressing1.getHead() == ArrowHead.CROSSX) {
			start += 2 * spaceCrossX;
			len -= 2 * spaceCrossX;
		}

		final double posArrow;
		final double yText;
		if (isBelowForResponse()) {
			posArrow = 0;
			yText = getMarginY();
		} else {
			posArrow = getTextHeight(stringBounder);
			yText = 0;
		}

		drawDressing1(ug.apply(new UTranslate(pos1, posArrow + inclination1)), dressing1,
				arrowConfiguration.getDecoration1(), lenFull);
		drawDressing2(ug.apply(new UTranslate(pos2, posArrow + inclination2)), dressing2,
				arrowConfiguration.getDecoration2(), lenFull);

		if (inclination1 == 0 && inclination2 == 0)
			arrowConfiguration.applyStroke(ug).apply(new UTranslate(start, posArrow)).draw(new ULine(len, 0));
		else if (inclination1 != 0)
			drawLine(arrowConfiguration.applyStroke(ug), start + len, posArrow, 0, posArrow + inclination1);
		else if (inclination2 != 0)
			drawLine(arrowConfiguration.applyStroke(ug), start, posArrow, pos2, posArrow + inclination2);

		final ArrowDirection direction2 = getDirection2();
		final double textPos;
		if (messagePosition == HorizontalAlignment.CENTER) {
			final double textWidth = getTextBlock().calculateDimension(stringBounder).getWidth();
			textPos = (dimensionToUse.getWidth() - Math.abs(area.getTextDeltaX()) - textWidth) / 2;
		} else if (messagePosition == HorizontalAlignment.RIGHT) {
			final double textWidth = getTextBlock().calculateDimension(stringBounder).getWidth();
			textPos = dimensionToUse.getWidth() - Math.abs(area.getTextDeltaX()) - textWidth - getMarginX2()
					- (direction2 == ArrowDirection.LEFT_TO_RIGHT_NORMAL ? getArrowDeltaX() : 0);
		} else {
			textPos = getMarginX1()
					+ (direction2 == ArrowDirection.RIGHT_TO_LEFT_REVERSE || direction2 == ArrowDirection.BOTH_DIRECTION
							? getArrowDeltaX()
							: 0);
		}
		getTextBlock().drawU(ug.apply(new UTranslate(textPos + Math.max(0,area.getTextDeltaX()), yText)));
	}

	private void drawLine(UGraphic ug, double x1, double y1, double x2, double y2) {
		ug = ug.apply(new UTranslate(x1, y1));
		ug.draw(new ULine(x2 - x1, y2 - y1));

	}

	public double getPosArrow(StringBounder stringBounder) {
		if (isBelowForResponse())
			return 0;

		return getTextHeight(stringBounder) - 2 * getMarginY();
	}

	private boolean isBelowForResponse() {
		return belowForResponse && getArrowConfiguration().isReverseDefine();
	}

	private void drawDressing1(UGraphic ug, ArrowDressing dressing, ArrowDecoration decoration, double lenFull) {

		if (decoration == ArrowDecoration.CIRCLE) {
			final UEllipse circle = UEllipse.build(diamCircle, diamCircle);
			ug.apply(UStroke.withThickness(thinCircle)).apply(getForegroundColor())
					.apply(new UTranslate(-diamCircle / 2 - thinCircle, -diamCircle / 2 - thinCircle / 2)).draw(circle);
			if (dressing.getHead() != ArrowHead.CROSSX)
				ug = ug.apply(UTranslate.dx(diamCircle / 2 + thinCircle));
		}

		if (dressing.getHead() == ArrowHead.ASYNC) {
			if (dressing.getPart() != ArrowPart.BOTTOM_PART)
				getArrowConfiguration().applyThicknessOnly(ug).draw(
						new ULine(getArrowDeltaX(), -getArrowDeltaY()).rotate(Math.atan2(-inclination1, lenFull)));

			if (dressing.getPart() != ArrowPart.TOP_PART)
				getArrowConfiguration().applyThicknessOnly(ug)
						.draw(new ULine(getArrowDeltaX(), getArrowDeltaY()).rotate(Math.atan2(-inclination1, lenFull)));

		} else if (dressing.getHead() == ArrowHead.CROSSX) {
			ug = ug.apply(UStroke.withThickness(2));
			ug.apply(new UTranslate(spaceCrossX, -getArrowDeltaX() / 2))
					.draw(new ULine(getArrowDeltaX(), getArrowDeltaX()));
			ug.apply(new UTranslate(spaceCrossX, getArrowDeltaX() / 2))
					.draw(new ULine(getArrowDeltaX(), -getArrowDeltaX()));
		} else if (dressing.getHead() == ArrowHead.NORMAL) {
			final UPolygon polygon = getPolygonReverse(dressing.getPart());

			if (inclination1 != 0)
				polygon.rotate(Math.atan2(-inclination1, lenFull));

			ug.apply(getForegroundColor().bg()).draw(polygon);
		}

	}

	private void drawDressing2(UGraphic ug, ArrowDressing dressing, ArrowDecoration decoration, double lenFull) {

		if (decoration == ArrowDecoration.CIRCLE) {
			ug = ug.apply(UStroke.withThickness(thinCircle)).apply(getForegroundColor());
			final UEllipse circle = UEllipse.build(diamCircle, diamCircle);
			ug.apply(new UTranslate(-diamCircle / 2 + thinCircle, -diamCircle / 2 - thinCircle / 2)).draw(circle);
			ug = ug.apply(UStroke.simple());
			ug = ug.apply(UTranslate.dx(-diamCircle / 2 - thinCircle));
		}

		if (dressing.getHead() == ArrowHead.ASYNC) {
			if (dressing.getPart() != ArrowPart.BOTTOM_PART)
				getArrowConfiguration().applyThicknessOnly(ug).draw(
						new ULine(-getArrowDeltaX(), -getArrowDeltaY()).rotate(Math.atan2(inclination2, lenFull)));

			if (dressing.getPart() != ArrowPart.TOP_PART)
				getArrowConfiguration().applyThicknessOnly(ug)
						.draw(new ULine(-getArrowDeltaX(), getArrowDeltaY()).rotate(Math.atan2(inclination2, lenFull)));

		} else if (dressing.getHead() == ArrowHead.CROSSX) {
			ug = ug.apply(UStroke.withThickness(2));
			ug.apply(new UTranslate(-spaceCrossX - getArrowDeltaX(), -getArrowDeltaX() / 2))
					.draw(new ULine(getArrowDeltaX(), getArrowDeltaX()));
			ug.apply(new UTranslate(-spaceCrossX - getArrowDeltaX(), getArrowDeltaX() / 2))
					.draw(new ULine(getArrowDeltaX(), -getArrowDeltaX()));
		} else if (dressing.getHead() == ArrowHead.NORMAL) {
			final UPolygon polygon = getPolygonNormal(dressing.getPart());

			if (inclination2 != 0)
				polygon.rotate(Math.atan2(inclination2, lenFull));

			ug.apply(getForegroundColor().bg()).draw(polygon);
		}

	}

	private UPolygon getPolygonNormal(ArrowPart part) {
		UPolygon polygon = new UPolygon();
		if (part == ArrowPart.TOP_PART) {
			polygon.addPoint(-getArrowDeltaX(), -getArrowDeltaY());
			polygon.addPoint(0, 0);
			polygon.addPoint(-getArrowDeltaX(), 0);
		} else if (part == ArrowPart.BOTTOM_PART) {
			polygon.addPoint(-getArrowDeltaX(), 0);
			polygon.addPoint(0, 0);
			polygon.addPoint(-getArrowDeltaX(), getArrowDeltaY());
		} else {
			polygon.addPoint(-getArrowDeltaX(), -getArrowDeltaY());
			polygon.addPoint(0, 0);
			polygon.addPoint(-getArrowDeltaX(), +getArrowDeltaY());
			if (niceArrow)
				polygon.addPoint(-getArrowDeltaX() + 4, 0);
		}
		return polygon;
	}

	private UPolygon getPolygonReverse(ArrowPart part) {
		final UPolygon polygon = new UPolygon();
		if (part == ArrowPart.TOP_PART) {
			polygon.addPoint(getArrowDeltaX(), -getArrowDeltaY());
			polygon.addPoint(0, 0);
			polygon.addPoint(getArrowDeltaX(), 0);
		} else if (part == ArrowPart.BOTTOM_PART) {
			polygon.addPoint(getArrowDeltaX(), 0);
			polygon.addPoint(0, 0);
			polygon.addPoint(getArrowDeltaX(), getArrowDeltaY());
		} else {
			polygon.addPoint(getArrowDeltaX(), -getArrowDeltaY());
			polygon.addPoint(0, 0);
			polygon.addPoint(getArrowDeltaX(), getArrowDeltaY());
			if (niceArrow)
				polygon.addPoint(getArrowDeltaX() - 4, 0);

		}
		return polygon;
	}

	public XPoint2D getStartPoint(StringBounder stringBounder, XDimension2D dimensionToUse) {
		final double y = getYPoint(stringBounder);
		if (getDirection2() == ArrowDirection.LEFT_TO_RIGHT_NORMAL)
			return new XPoint2D(getPaddingX(), y + inclination2);

		return new XPoint2D(dimensionToUse.getWidth() + getPaddingX(), y + inclination2);
	}

	public XPoint2D getEndPoint(StringBounder stringBounder, XDimension2D dimensionToUse) {
		final double y = getYPoint(stringBounder);
		if (getDirection2() == ArrowDirection.LEFT_TO_RIGHT_NORMAL)
			return new XPoint2D(dimensionToUse.getWidth() + getPaddingX(), y);

		return new XPoint2D(getPaddingX(), y);
	}

	@Override
	public double getYPoint(StringBounder stringBounder) {
		if (isBelowForResponse())
			return getPaddingY();

		return getTextHeight(stringBounder) + getPaddingY();
	}

	final private ArrowDirection getDirection2() {
		return getArrowConfiguration().getArrowDirection();
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + getArrowDeltaY() + 2 * getPaddingY() + inclination1 + inclination2;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder) + getArrowDeltaX();
	}

}
