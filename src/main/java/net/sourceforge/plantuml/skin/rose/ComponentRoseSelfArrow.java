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
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.ArrowPart;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;

public class ComponentRoseSelfArrow extends AbstractComponentRoseArrow {

	private final double arrowWidth = 45;
	private final double xRight = arrowWidth - 3;
	private final boolean niceArrow;

	public ComponentRoseSelfArrow(Style style, Display stringsToDisplay, ArrowConfiguration arrowConfiguration,
			ISkinParam skinParam, LineBreakStrategy maxMessageSize, boolean niceArrow) {
		super(style, stringsToDisplay, arrowConfiguration, skinParam, maxMessageSize);
		this.niceArrow = niceArrow;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		if (getArrowConfiguration().isHidden()) {
			return;
		}
		final StringBounder stringBounder = ug.getStringBounder();
		final double textHeight = getTextHeight(stringBounder);
		double prefTextWidth = getPreferredWidth(stringBounder);

		final double arrowHeight = getArrowOnlyHeight(stringBounder);

		ug = ug.apply(getForegroundColor());
		final UGraphic ug2 = getArrowConfiguration().applyStroke(ug);

		if (getArrowConfiguration().isReverseDefine())
			drawLeftSide(ug, ug2, area, arrowHeight, textHeight, prefTextWidth);
		else
			drawRightSide(ug, ug2, area, arrowHeight, textHeight);

		getTextBlock().drawU(ug.apply(UTranslate.dx(getMarginX1())));
	}

	private void drawRightSide(UGraphic ug, UGraphic ug2, Area area, double arrowHeight, double textHeight) {
		double x1 = area.getDeltaX1() < 0 ? area.getDeltaX1() : 0;
		double x2 = area.getDeltaX1() > 0 ? -area.getDeltaX1() : 1;
		final UEllipse circle = UEllipse.build(ComponentRoseArrow.diamCircle, ComponentRoseArrow.diamCircle);
		if (getArrowConfiguration().getDecoration1() == ArrowDecoration.CIRCLE) {
			ug2.apply(UStroke.withThickness(ComponentRoseArrow.thinCircle)).apply(getForegroundColor())
					.apply(getBackgroundColor().bg())
					.apply(new UTranslate(x1 + 1 - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle,
							textHeight - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle / 2))
					.draw(circle);
			x1 += ComponentRoseArrow.diamCircle / 2 + ComponentRoseArrow.thinCircle + 1;
			x1 -= getArrowConfiguration().getDressing1().getHead() == ArrowHead.NONE ? ComponentRoseArrow.thinCircle+1 : 0;
		}
		if (getArrowConfiguration().getDecoration2() == ArrowDecoration.CIRCLE) {
			ug2.apply(UStroke.withThickness(ComponentRoseArrow.thinCircle)).apply(getForegroundColor())
					.apply(getBackgroundColor().bg())
					.apply(new UTranslate(
							x2 - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle,
							textHeight + arrowHeight - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle / 2))
					.draw(circle);
			x2 += ComponentRoseArrow.diamCircle / 2 + ComponentRoseArrow.thinCircle;
		}
		final boolean hasStartingCrossX = getArrowConfiguration().getDressing1().getHead() == ArrowHead.CROSSX;
		if (hasStartingCrossX)
			x1 += 2 * ComponentRoseArrow.spaceCrossX;

		final boolean hasFinalCrossX = getArrowConfiguration().getDressing2().getHead() == ArrowHead.CROSSX;
		if (hasFinalCrossX)
			x2 += 2 * ComponentRoseArrow.spaceCrossX;

		if (getArrowConfiguration().getDressing2().getHead() == ArrowHead.ASYNC)
			x2 -= 1;

		ug2.apply(new UTranslate(x1, textHeight)).draw(ULine.hline(xRight - x1));
		ug2.apply(new UTranslate(xRight, textHeight)).draw(ULine.vline(arrowHeight));
		ug2.apply(new UTranslate(x2, textHeight + arrowHeight)).draw(ULine.hline(xRight - x2));

		if (getArrowConfiguration().getDressing2().getHead() == ArrowHead.ASYNC)
			x2 += 1;

		if (hasStartingCrossX) {
			ug.apply(UStroke.withThickness(2))
					.apply(new UTranslate(ComponentRoseArrow.spaceCrossX, textHeight - getArrowDeltaX() / 2.0))
					.draw(new ULine(getArrowDeltaX(), getArrowDeltaX()));
			ug.apply(UStroke.withThickness(2))
					.apply(new UTranslate(ComponentRoseArrow.spaceCrossX, textHeight + getArrowDeltaX() / 2.0))
					.draw(new ULine(getArrowDeltaX(), -getArrowDeltaX()));
		}  else if (getArrowConfiguration().isAsync1()) {
			if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
				getArrowConfiguration().applyThicknessOnly(ug).apply(new UTranslate(x1, textHeight))
						.draw(new ULine(getArrowDeltaX(), getArrowDeltaY()));
			}
			if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
				getArrowConfiguration().applyThicknessOnly(ug).apply(new UTranslate(x1, textHeight))
						.draw(new ULine(getArrowDeltaX(), -getArrowDeltaY()));
			}
		} else if (getArrowConfiguration().getDressing1().getHead() == ArrowHead.NORMAL) {
			final UPolygon polygon = getPolygon().translate(0, textHeight);
			ug.apply(getForegroundColor().bg()).apply(UTranslate.dx(x1)).draw(polygon);
		}

		if (hasFinalCrossX) {
			ug.apply(UStroke.withThickness(2))
					.apply(new UTranslate(ComponentRoseArrow.spaceCrossX,
							textHeight - getArrowDeltaX() / 2.0 + arrowHeight))
					.draw(new ULine(getArrowDeltaX(), getArrowDeltaX()));
			ug.apply(UStroke.withThickness(2))
					.apply(new UTranslate(ComponentRoseArrow.spaceCrossX,
							textHeight + getArrowDeltaX() / 2.0 + arrowHeight))
					.draw(new ULine(getArrowDeltaX(), -getArrowDeltaX()));
		} else if (getArrowConfiguration().isAsync2()) {
			if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
				getArrowConfiguration().applyThicknessOnly(ug).apply(new UTranslate(x2, textHeight + arrowHeight))
						.draw(new ULine(getArrowDeltaX(), -getArrowDeltaY()));
			}
			if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
				getArrowConfiguration().applyThicknessOnly(ug).apply(new UTranslate(x2, textHeight + arrowHeight))
						.draw(new ULine(getArrowDeltaX(), getArrowDeltaY()));
			}
		} else if (getArrowConfiguration().getDressing2().getHead() == ArrowHead.NORMAL) {
			final UPolygon polygon = getPolygon().translate(0, textHeight + arrowHeight);
			ug.apply(getForegroundColor().bg()).apply(UTranslate.dx(x2)).draw(polygon);
		}
	}

	private void drawLeftSide(UGraphic ug, UGraphic ug2, Area area, double arrowHeight, double textHeight, double prefTextWidth) {
		double deltaSize = area.getLiveDeltaSize();
		double dx = area.getDeltaX1();
		int level = area.getLevel();

		double x1 = 0;
		double x2 = 1;
		double extraLiveDeltaIndent = level * deltaSize;
		if (dx < 0) {
			x1 += 0;
			x2 +=  (level > 0 ? -extraLiveDeltaIndent : deltaSize) ;
		} else if (dx > 0) {
			x1 += (level > 1) ? deltaSize - extraLiveDeltaIndent : 0;
			x2 += (level == 1 ? -deltaSize : 0);
		} else if (level > 1) {
			x1 -= extraLiveDeltaIndent - deltaSize;
			x2 -= extraLiveDeltaIndent - deltaSize;
		}

		final UEllipse circle = UEllipse.build(ComponentRoseArrow.diamCircle, ComponentRoseArrow.diamCircle);
		if (getArrowConfiguration().getDecoration1() == ArrowDecoration.CIRCLE) {
			ug2.apply(UStroke.withThickness(ComponentRoseArrow.thinCircle)).apply(getForegroundColor())
					.apply(getBackgroundColor().bg())
					.apply(new UTranslate(prefTextWidth - x1 + 1 - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle,
							textHeight - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle / 2))
					.draw(circle);
			x1 += ComponentRoseArrow.diamCircle / 2.0 - ComponentRoseArrow.thinCircle;
			x1 += getArrowConfiguration().getDressing1().getHead() == ArrowHead.NONE ? ComponentRoseArrow.thinCircle : 0;
		}
		if (getArrowConfiguration().getDecoration2() == ArrowDecoration.CIRCLE) {
			ug2.apply(UStroke.withThickness(ComponentRoseArrow.thinCircle)).apply(getForegroundColor())
					.apply(getBackgroundColor().bg())
					.apply(new UTranslate(prefTextWidth - x2 + 2 - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle,
							textHeight + arrowHeight - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle / 2))
					.draw(circle);
			x2 += ComponentRoseArrow.diamCircle / 2.0 + ComponentRoseArrow.thinCircle;
		}
		final boolean hasStartingCrossX = getArrowConfiguration().getDressing1().getHead() == ArrowHead.CROSSX;
		if (hasStartingCrossX)
			x1 += ComponentRoseArrow.spaceCrossX + (level > 0 ? extraLiveDeltaIndent : ComponentRoseArrow.spaceCrossX);

		final boolean hasFinalCrossX = getArrowConfiguration().getDressing2().getHead() == ArrowHead.CROSSX;
		if (hasFinalCrossX)
			x2 += ComponentRoseArrow.spaceCrossX + (level > 0 ? extraLiveDeltaIndent : ComponentRoseArrow.spaceCrossX);

		double extraline = 0;
		if (getArrowConfiguration().getDressing2().getPart() == ArrowPart.FULL
				&& getArrowConfiguration().getDressing2().getHead() == ArrowHead.NORMAL)
			extraline = 1;

		x1 += 1;

		ug2.apply(new UTranslate(prefTextWidth-xRight, textHeight)).draw(ULine.hline(xRight - x1));
		ug2.apply(new UTranslate(prefTextWidth-xRight, textHeight)).draw(ULine.vline(arrowHeight));
		ug2.apply(new UTranslate(prefTextWidth-xRight, textHeight + arrowHeight)).draw(ULine.hline(xRight - x2 - extraline));

		if (getArrowConfiguration().getDressing2().getPart() == ArrowPart.FULL
				&& getArrowConfiguration().getDressing2().getHead() == ArrowHead.NORMAL)
			x2 += 1;
		if (getArrowConfiguration().getDressing2().getHead() == ArrowHead.ASYNC)
			x2 += 1;

		if (hasStartingCrossX) {
			ug.apply(UStroke.withThickness(2))
					.apply(new UTranslate(prefTextWidth-x1-ComponentRoseArrow.spaceCrossX/2,
							textHeight - getArrowDeltaX() / 2.0))
					.draw(new ULine(getArrowDeltaX(), getArrowDeltaX()));
			ug.apply(UStroke.withThickness(2))
					.apply(new UTranslate(prefTextWidth-x1-ComponentRoseArrow.spaceCrossX/2, textHeight + getArrowDeltaX() / 2.0))
					.draw(new ULine(getArrowDeltaX(), -getArrowDeltaX()));
		} else if (getArrowConfiguration().getDressing1().getHead() == ArrowHead.NORMAL) {
			final UPolygon polygon = getPolygon().translate(prefTextWidth - x1 , textHeight);
			ug.apply(getForegroundColor().bg()).apply(UTranslate.dx(x1)).draw(polygon);
		}

		if (hasFinalCrossX) {
			ug.apply(UStroke.withThickness(2))
					.apply(new UTranslate(prefTextWidth-x2-ComponentRoseArrow.spaceCrossX/2,
							textHeight - getArrowDeltaX() / 2.0 + arrowHeight))
					.draw(new ULine(getArrowDeltaX(), getArrowDeltaX()));
			ug.apply(UStroke.withThickness(2))
					.apply(new UTranslate(prefTextWidth-x2-ComponentRoseArrow.spaceCrossX/2,
							textHeight + getArrowDeltaX() / 2.0 + arrowHeight))
					.draw(new ULine(getArrowDeltaX(), -getArrowDeltaX()));
		} else if (getArrowConfiguration().isAsync2()) {
			if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
				getArrowConfiguration().applyThicknessOnly(ug).apply(new UTranslate(prefTextWidth - x2 , textHeight + arrowHeight))
						.draw(new ULine(-getArrowDeltaX(), -getArrowDeltaY()));
			}
			if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
				getArrowConfiguration().applyThicknessOnly(ug).apply(new UTranslate(prefTextWidth - x2 , textHeight + arrowHeight))
						.draw(new ULine(-getArrowDeltaX(), getArrowDeltaY()));
			}
		} else if (getArrowConfiguration().getDressing2().getHead() == ArrowHead.NORMAL) {
			final UPolygon polygon = getPolygon().translate(0, textHeight + arrowHeight);
			ug.apply(getForegroundColor().bg()).apply(UTranslate.dx(prefTextWidth-x2)).draw(polygon);
		}
	}

	private UPolygon getPolygon() {
		final UPolygon polygon = new UPolygon();
		final double direction = getArrowConfiguration().isReverseDefine() ? -1 : 1;
		final double x = direction * getArrowDeltaX();
		if (getArrowConfiguration().getPart() == ArrowPart.TOP_PART) {
			polygon.addPoint(x-1, -getArrowDeltaY());
			polygon.addPoint(-1, 0);
			polygon.addPoint(x-1, 0);
		} else if (getArrowConfiguration().getPart() == ArrowPart.BOTTOM_PART) {
			polygon.addPoint(x-1, 0);
			polygon.addPoint(-1, 0);
			polygon.addPoint(x-1, getArrowDeltaY());
		} else {
			polygon.addPoint(x, -getArrowDeltaY());
			polygon.addPoint(0, 0);
			polygon.addPoint(x, getArrowDeltaY());
			if (niceArrow) {
				polygon.addPoint(x - direction * 4, 0);
			}
		}
		return polygon;
	}

	public XPoint2D getStartPoint(StringBounder stringBounder, XDimension2D dimensionToUse) {
		final double textHeight = getTextHeight(stringBounder);
		return new XPoint2D(getPaddingX(), textHeight + getPaddingY());
	}

	public XPoint2D getEndPoint(StringBounder stringBounder, XDimension2D dimensionToUse) {
		final double textHeight = getTextHeight(stringBounder);
		final double textAndArrowHeight = textHeight + getArrowOnlyHeight(stringBounder);
		return new XPoint2D(getPaddingX(), textAndArrowHeight + getPaddingY());
	}

	@Override
	public double getYPoint(StringBounder stringBounder) {
		final double textHeight = getTextHeight(stringBounder);
		final double textAndArrowHeight = textHeight + getArrowOnlyHeight(stringBounder);
		return (textHeight + textAndArrowHeight) / 2 + getPaddingX();
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
		return Math.max(getTextWidth(stringBounder), arrowWidth+5);
	}

	public double getPosArrow(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

}
