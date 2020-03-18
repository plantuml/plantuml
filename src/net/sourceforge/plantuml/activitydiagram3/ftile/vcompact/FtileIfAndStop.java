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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.CreoleParser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.creole.SheetBlock2;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

class FtileIfAndStop extends AbstractFtile {

	private final Ftile tile1;
	private final Ftile diamond1;
	private final Ftile stop2;

	private final Rainbow arrowColor;

	private FtileIfAndStop(Ftile diamond1, Ftile tile1, Rainbow arrowColor, Ftile stopFtile) {
		super(tile1.skinParam());
		this.diamond1 = diamond1;
		this.tile1 = tile1;
		this.stop2 = stopFtile;

		this.arrowColor = arrowColor;

	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		if (getSwimlaneIn() != null) {
			result.add(getSwimlaneIn());
		}
		result.addAll(tile1.getSwimlanes());
		return Collections.unmodifiableSet(result);
	}

	public Swimlane getSwimlaneIn() {
		return diamond1.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	static Ftile create(Swimlane swimlane, HColor borderColor, HColor backColor, Rainbow arrowColor,
			FtileFactory ftileFactory, ConditionStyle conditionStyle, Branch nonStop, ISkinParam skinParam,
			StringBounder stringBounder, Display labelTest) {

		// backColor = HtmlColorUtils.BLUE;

		// final Ftile tileNonStop = new FtileMinWidth(nonStop.getFtile(), 30);
		final Ftile tileNonStop = nonStop.getFtile();

		final FontConfiguration fcTest = new FontConfiguration(skinParam, FontParam.ACTIVITY_DIAMOND, null);

		final Ftile stopFtile = ftileFactory.stop(swimlane);

		// final TextBlock tb1 = Display.create(branch1.getLabelPositive(), fcArrow, HorizontalAlignment.LEFT,
		// ftileFactory);
		// final TextBlock tb2 = Display.create(branch2.getLabelPositive(), fcArrow, HorizontalAlignment.LEFT,
		// ftileFactory);

		final Sheet sheet = new CreoleParser(fcTest, skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT),
				skinParam, CreoleMode.FULL).createSheet(labelTest);
		final SheetBlock1 sheetBlock1 = new SheetBlock1(sheet, LineBreakStrategy.NONE, skinParam.getPadding());
		final TextBlock tbTest = new SheetBlock2(sheetBlock1, Diamond.asStencil(sheetBlock1),
				tileNonStop.getThickness());

		final Ftile diamond1;
		if (conditionStyle == ConditionStyle.INSIDE) {
			diamond1 = new FtileDiamondInside(tileNonStop.skinParam(), backColor, borderColor, swimlane, tbTest);
			// .withWest(tb1).withEast(tb2);
		} else if (conditionStyle == ConditionStyle.DIAMOND) {
			diamond1 = new FtileDiamond(tileNonStop.skinParam(), backColor, borderColor, swimlane).withNorth(tbTest);
			// .withWest(tb1).withEast(tb2).withNorth(tbTest);
		} else {
			throw new IllegalStateException();
		}

		// final Ftile diamond2;
		// if (tile1.calculateDimension(stringBounder).hasPointOut()
		// && tile2.calculateDimension(stringBounder).hasPointOut()) {
		// diamond2 = new FtileDiamond(tile1.shadowing(), backColor, borderColor, swimlane);
		// } else {
		// diamond2 = new FtileEmpty(tile1.shadowing(), Diamond.diamondHalfSize * 2, Diamond.diamondHalfSize * 2,
		// swimlane, swimlane);
		// }
		final FtileIfAndStop result = new FtileIfAndStop(diamond1, tileNonStop, arrowColor, stopFtile);

		final List<Connection> conns = new ArrayList<Connection>();
		conns.add(result.new ConnectionHorizontal(arrowColor));
		// conns.add(result.new ConnectionHorizontalThenVertical(tile2));
		// if (tile1.calculateDimension(stringBounder).hasPointOut()
		// && tile2.calculateDimension(stringBounder).hasPointOut()) {
		// conns.add(result.new ConnectionVerticalThenHorizontal(tile1, branch1.getInlinkRenderingColor()));
		// conns.add(result.new ConnectionVerticalThenHorizontal(tile2, branch2.getInlinkRenderingColor()));
		// } else if (tile1.calculateDimension(stringBounder).hasPointOut()
		// && tile2.calculateDimension(stringBounder).hasPointOut() == false) {
		// conns.add(result.new ConnectionVerticalThenHorizontalDirect(tile1, branch1.getInlinkRenderingColor()));
		// } else if (tile1.calculateDimension(stringBounder).hasPointOut() == false
		// && tile2.calculateDimension(stringBounder).hasPointOut()) {
		// conns.add(result.new ConnectionVerticalThenHorizontalDirect(tile2, branch2.getInlinkRenderingColor()));
		// }
		return FtileUtils.addConnection(result, conns);
		// return result;
	}

	private UTranslate getTranslate1(StringBounder stringBounder) {
		// final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final FtileGeometry dimTotal = calculateDimension(stringBounder);
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final FtileGeometry dim1 = tile1.calculateDimension(stringBounder);

		final double x1 = calculateDimension(stringBounder).getLeft() - dim1.getLeft();
		// final double y1 = (dimTotal.getHeight() - 2 * h - dim1.getHeight()) / 2 + h;
		final double y1 = dimDiamond1.getHeight() + getSuppHeight();
		return new UTranslate(x1, y1);
	}

	private int getSuppHeight() {
		return 30;
	}

	private UTranslate getTranslateDiamond1(StringBounder stringBounder) {
		final double y1 = 0;
		final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		// final double x1 = getLeft(stringBounder) - dimDiamond1.getWidth() / 2;
		final double x1 = calculateDimension(stringBounder).getLeft() - dimDiamond1.getWidth() / 2;
		return new UTranslate(x1, y1);
	}

	private UTranslate getTranslateStop(StringBounder stringBounder) {
		final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final Dimension2D dimStop = stop2.calculateDimension(stringBounder);
		final double y1 = (dimDiamond1.getHeight() - dimStop.getHeight()) / 2;
		final double x1 = calculateDimension(stringBounder).getLeft() + dimDiamond1.getWidth() / 2
				+ getDiamondStopDistance();
		return new UTranslate(x1, y1);
	}

	private double getDiamondStopDistance() {
		return 40;
	}

	class ConnectionHorizontal extends AbstractConnection {

		private final Rainbow color;

		public ConnectionHorizontal(Rainbow color) {
			super(diamond1, stop2);
			this.color = color;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);

			final Snake snake = new Snake(arrowHorizontalAlignment(), color, Arrows.asToRight());
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			final Dimension2D dimDiamond1 = getFtile1().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(dimDiamond1.getWidth(), dimDiamond1.getHeight() / 2);

			return getTranslateDiamond1(stringBounder).getTranslated(p);
		}

		private Point2D getP2(StringBounder stringBounder) {
			final Dimension2D dimStop = getFtile2().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(0, dimStop.getHeight() / 2);
			return getTranslateStop(stringBounder).getTranslated(p);
		}

	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == diamond1) {
			return getTranslateDiamond1(stringBounder);
		}
		if (child == tile1) {
			return getTranslate1(stringBounder);
		}
		// if (child == tile2) {
		// return getTranslate2(stringBounder);
		// }
		// if (child == diamond2) {
		// return getTranslateDiamond2(stringBounder);
		// }
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
		ug.apply(getTranslate1(stringBounder)).draw(tile1);
		ug.apply(getTranslateStop(stringBounder)).draw(stop2);
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final Dimension2D dimStop2 = stop2.calculateDimension(stringBounder);
		final FtileGeometry dim1 = tile1.calculateDimension(stringBounder).addDim(0,
				getDiamondStopDistance() + dimStop2.getWidth());
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		return dimDiamond1.appendBottom(dim1).addDim(0, getSuppHeight());

		// final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		// if (tile1.calculateDimension(stringBounder).hasPointOut()) {
		// return new FtileGeometry(dimTotal, getLeft(stringBounder), 0, dimTotal.getHeight());
		// }
		// return new FtileGeometry(dimTotal, getLeft(stringBounder), 0);
	}

	// private Dimension2D calculateDimensionInternal;
	//
	// private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
	// if (calculateDimensionInternal == null) {
	// calculateDimensionInternal = calculateDimensionInternalSlow(stringBounder);
	// }
	// return calculateDimensionInternal;
	// }
	//
	// private Dimension2D calculateDimensionInternalSlow(StringBounder stringBounder) {
	// final Dimension2D dim1 = tile1.calculateDimension(stringBounder);
	// final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
	// final Dimension2D dimStop2 = stop2.calculateDimension(stringBounder);
	// final double width = Math.max(dim1.getWidth(),
	// dimDiamond1.getWidth() + getDiamondStopDistance() + dimStop2.getWidth());
	// return new Dimension2DDouble(width + 30, dim1.getHeight() + dimDiamond1.getHeight() + 40);
	// }
	//
	// private double getLeft(StringBounder stringBounder) {
	// // return calculateDimension(stringBounder).getLeft();
	// return tile1.calculateDimension(stringBounder).translate(getTranslate1(stringBounder)).getLeft();
	// // final double left1 =
	// tile1.calculateDimension(stringBounder).translate(getTranslate1(stringBounder)).getLeft();
	// // // final double left2 =
	// // // tile2.calculateDimension(stringBounder).translate(getTranslate2(stringBounder)).getLeft();
	// // // return (left1 + left2) / 2;
	// // return left1;
	// }

}
