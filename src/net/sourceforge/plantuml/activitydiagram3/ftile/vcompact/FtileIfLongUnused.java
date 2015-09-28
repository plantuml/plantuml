/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 8475 $
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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidth;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileIfLongUnused extends AbstractFtile {

	private final double xSeparation = 20;

	private final List<Ftile> tiles;
	private final Ftile tile2;
	private final List<Ftile> diamonds;

	private final HtmlColor arrowColor;

	private FtileIfLongUnused(List<Ftile> diamonds, List<Ftile> tiles, Ftile tile2, HtmlColor arrowColor) {
		super(tiles.get(0).shadowing() || tile2.shadowing());
		this.diamonds = diamonds;
		this.tiles = tiles;
		this.tile2 = tile2;

		this.arrowColor = arrowColor;

	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		if (getSwimlaneIn() != null) {
			result.add(getSwimlaneIn());
		}
		for (Ftile tile : tiles) {
			result.addAll(tile.getSwimlanes());
		}
		result.addAll(tile2.getSwimlanes());
		return Collections.unmodifiableSet(result);
	}

	public Swimlane getSwimlaneIn() {
		return diamonds.get(0).getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	static Ftile create(Swimlane swimlane, HtmlColor borderColor, HtmlColor backColor, UFont font,
			HtmlColor arrowColor, FtileFactory ftileFactory, ConditionStyle conditionStyle, List<Branch> thens,
			Branch branch2, HtmlColor hyperlinkColor, boolean useUnderlineForHyperlink) {

		final List<Ftile> tiles = new ArrayList<Ftile>();

		for (Branch branch : thens) {
			tiles.add(new FtileMinWidth(branch.getFtile(), 30));
		}

		final Ftile tile2 = new FtileMinWidth(branch2.getFtile(), 30);

		final FontConfiguration fc = null;
		// new FontConfiguration(font, HtmlColorUtils.BLACK, hyperlinkColor, useUnderlineForHyperlink);

		final List<Ftile> diamonds = new ArrayList<Ftile>();
		final List<Connection> conns = new ArrayList<Connection>();
		for (Branch branch : thens) {
			final TextBlock tb1 = branch.getLabelPositive().create(fc, HorizontalAlignment.LEFT, ftileFactory);
			final TextBlock tbTest = branch.getLabelTest().create(fc, HorizontalAlignment.LEFT, ftileFactory);
			FtileDiamondInside diamond = new FtileDiamondInside(branch.shadowing(), backColor, borderColor, swimlane,
					tbTest);
			diamond = diamond.withNorth(tb1);
			diamonds.add(diamond);
		}

		final TextBlock tb2 = branch2.getLabelPositive().create(fc, HorizontalAlignment.LEFT, ftileFactory);
		final int last = diamonds.size() - 1;
		diamonds.set(last, ((FtileDiamondInside) diamonds.get(last)).withEast(tb2));

		final FtileIfLongUnused result = new FtileIfLongUnused(diamonds, tiles, tile2, arrowColor);

		for (int i = 0; i < thens.size(); i++) {
			final Ftile ftile = tiles.get(i);
			final Ftile diam = diamonds.get(i);

			final HtmlColor color = thens.get(i).getInlinkRenderingColor();
			conns.add(result.new ConnectionVerticalIn(diam, ftile, color == null ? arrowColor : color));
			conns.add(result.new ConnectionVerticalOut(ftile, arrowColor));
		}

		for (int i = 0; i < diamonds.size() - 1; i++) {
			final Ftile diam1 = diamonds.get(i);
			final Ftile diam2 = diamonds.get(i + 1);
			conns.add(result.new ConnectionHorizontal(diam1, diam2, arrowColor));
		}
		conns.add(result.new ConnectionIn(arrowColor));
		conns.add(result.new ConnectionLastElseIn(arrowColor));
		conns.add(result.new ConnectionLastElseOut(arrowColor));
		conns.add(result.new ConnectionHline(arrowColor));

		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionHorizontal extends AbstractConnection {

		private final HtmlColor color;

		public ConnectionHorizontal(Ftile diam1, Ftile diam2, HtmlColor color) {
			super(diam1, diam2);
			this.color = color;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);

			final Snake snake = new Snake(color, Arrows.asToRight());
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = getFtile1().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(dimDiamond1.getWidth(), dimDiamond1.getOutY() / 2);

			return getTranslateDiamond1(getFtile1(), stringBounder).getTranslated(p);
		}

		private Point2D getP2(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = getFtile2().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(0, dimDiamond1.getOutY() / 2);
			return getTranslateDiamond1(getFtile2(), stringBounder).getTranslated(p);
		}

	}

	class ConnectionIn extends AbstractConnection {

		private final HtmlColor arrowColor;

		public ConnectionIn(HtmlColor arrowColor) {
			super(null, diamonds.get(0));
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final UTranslate tr = getTranslateDiamond1(getFtile2(), ug.getStringBounder());
			final Point2D p2 = tr.getTranslated(getFtile2().calculateDimension(ug.getStringBounder()).getPointIn());
			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			final Point2D p1 = calculateDimension(ug.getStringBounder()).getPointIn();

			snake.addPoint(p1);
			snake.addPoint(p2.getX(), p1.getY());
			snake.addPoint(p2);
			ug.draw(snake);
		}

	}

	class ConnectionLastElseIn extends AbstractConnection {

		private final HtmlColor arrowColor;

		public ConnectionLastElseIn(HtmlColor arrowColor) {
			super(diamonds.get(diamonds.size() - 1), tile2);
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final Point2D p1 = getP1(ug.getStringBounder());
			final UTranslate tr2 = getTranslate2(ug.getStringBounder());
			final Point2D p2 = tr2.getTranslated(getFtile2().calculateDimension(ug.getStringBounder()).getPointIn());
			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			snake.addPoint(p1);
			snake.addPoint(p2.getX(), p1.getY());
			snake.addPoint(p2);
			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = getFtile1().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(dimDiamond1.getWidth(), dimDiamond1.getOutY() / 2);
			return getTranslateDiamond1(getFtile1(), stringBounder).getTranslated(p);
		}

	}

	class ConnectionLastElseOut extends AbstractConnection {

		private final HtmlColor arrowColor;

		public ConnectionLastElseOut(HtmlColor arrowColor) {
			super(tile2, null);
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final UTranslate tr1 = getTranslate2(stringBounder);
			final FtileGeometry dim = getFtile1().calculateDimension(stringBounder);
			if (dim.hasPointOut() == false) {
				return;
			}
			final Point2D p1 = tr1.getTranslated(dim.getPointOut());
			final double totalHeight = calculateDimensionInternal(stringBounder).getHeight();
			final Point2D p2 = new Point2D.Double(p1.getX(), totalHeight);

			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

	}

	class ConnectionVerticalIn extends AbstractConnection {

		private final HtmlColor color;

		public ConnectionVerticalIn(Ftile diamond, Ftile tile, HtmlColor color) {
			super(diamond, tile);
			this.color = color;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);

			final Snake snake = new Snake(color, Arrows.asToDown());
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			final Point2D p = getFtile1().calculateDimension(stringBounder).getPointOut();
			return getTranslateDiamond1(getFtile1(), stringBounder).getTranslated(p);
		}

		private Point2D getP2(StringBounder stringBounder) {
			final Point2D p = getFtile2().calculateDimension(stringBounder).getPointIn();
			return getTranslate1(getFtile2(), stringBounder).getTranslated(p);
		}

	}

	class ConnectionVerticalOut extends AbstractConnection {

		private final HtmlColor color;

		public ConnectionVerticalOut(Ftile tile, HtmlColor color) {
			super(tile, null);
			this.color = color;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final double totalHeight = calculateDimensionInternal(stringBounder).getHeight();
			final Point2D p1 = getP1(stringBounder);
			if (p1 == null) {
				return;
			}
			final Point2D p2 = new Point2D.Double(p1.getX(), totalHeight);

			final Snake snake = new Snake(color, Arrows.asToDown());
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			final FtileGeometry geo = getFtile1().calculateDimension(stringBounder);
			if (geo.hasPointOut() == false) {
				return null;
			}
			final Point2D p = geo.getPointOut();
			return getTranslate1(getFtile1(), stringBounder).getTranslated(p);
		}

	}

	class ConnectionHline extends AbstractConnection {

		private final HtmlColor arrowColor;

		public ConnectionHline(HtmlColor arrowColor) {
			super(null, null);
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Dimension2D totalDim = calculateDimensionInternal(stringBounder);

			final List<Ftile> all = new ArrayList<Ftile>(tiles);
			all.add(tile2);
			double minX = totalDim.getWidth() / 2;
			double maxX = totalDim.getWidth() / 2;
			for (Ftile tmp : all) {
				if (tmp.calculateDimension(stringBounder).hasPointOut() == false) {
					continue;
				}
				final UTranslate ut = getTranslateFor(tmp, stringBounder);
				final double out = tmp.calculateDimension(stringBounder).translate(ut).getLeft();
				minX = Math.min(minX, out);
				maxX = Math.max(maxX, out);
			}

			final Snake s = new Snake(arrowColor);
			s.goUnmergeable();
			final double height = totalDim.getHeight();
			s.addPoint(minX, height);
			s.addPoint(maxX, height);
			ug.draw(s);
		}
	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == tile2) {
			return getTranslate2(stringBounder);
		}
		if (tiles.contains(child)) {
			return getTranslate1(child, stringBounder);
		}
		throw new UnsupportedOperationException();
	}

	private UTranslate getTranslate2(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dim2 = tile2.calculateDimension(stringBounder);

		final double x2 = dimTotal.getWidth() - dim2.getWidth();

		final double h = getAllDiamondsHeight(stringBounder);
		final double y2 = (dimTotal.getHeight() - h * 2 - dim2.getHeight()) / 2 + h;

		return new UTranslate(x2, y2);

	}

	private UTranslate getTranslateDiamond1(Ftile diamond1, StringBounder stringBounder) {
		double x1 = 0;

		for (Ftile diamond : diamonds) {
			final FtileGeometry dim1 = dimDiamondAndTile(stringBounder, diamond);
			if (diamond == diamond1) {
				final FtileGeometry dimDiamond = diamond.calculateDimension(stringBounder);
				double xresult = x1 + dim1.getLeft() - dimDiamond.getLeft();
				return new UTranslate(xresult, 25);
			}
			x1 += dim1.getWidth() + xSeparation;
		}
		throw new IllegalArgumentException();

	}

	private UTranslate getTranslate1(Ftile tile1, StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		double x1 = 0;

		for (Ftile tile : tiles) {
			final Dimension2D dim1 = dimDiamondAndTile(stringBounder, tile);
			if (tile == tile1) {
				final Dimension2D dimTile = tile.calculateDimension(stringBounder);
				final double h = getAllDiamondsHeight(stringBounder);
				final double y1 = (dimTotal.getHeight() - 2 * h - dimTile.getHeight()) / 2 + h;
				return new UTranslate(x1 + (dim1.getWidth() - dimTile.getWidth()) / 2, y1);
			}
			x1 += dim1.getWidth() + xSeparation;
		}
		throw new IllegalArgumentException();

	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		for (Ftile tile : tiles) {
			ug.apply(getTranslate1(tile, stringBounder)).draw(tile);
		}
		for (Ftile diamond : diamonds) {
			ug.apply(getTranslateDiamond1(diamond, stringBounder)).draw(diamond);
		}

		ug.apply(getTranslate2(stringBounder)).draw(tile2);
	}

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);

		final List<Ftile> all = new ArrayList<Ftile>(tiles);
		all.add(tile2);
		for (Ftile tmp : all) {
			if (tmp.calculateDimension(stringBounder).hasPointOut()) {
				return new FtileGeometry(dimTotal, dimTotal.getWidth() / 2, 0, dimTotal.getHeight());
			}
		}
		return new FtileGeometry(dimTotal, dimTotal.getWidth() / 2, 0);

	}

	private FtileGeometry dimDiamondAndTile(StringBounder stringBounder, Ftile tileOrDiamond) {
		for (int i = 0; i < tiles.size(); i++) {
			final Ftile tile = tiles.get(i);
			final Ftile diamond = diamonds.get(i);
			if (tile != tileOrDiamond && diamond != tileOrDiamond) {
				continue;
			}
			final FtileGeometry dimTile = tile.calculateDimension(stringBounder);
			final FtileGeometry dimDiamond = diamond.calculateDimension(stringBounder);
			return dimDiamond.appendBottom(dimTile);
		}
		throw new UnsupportedOperationException();

	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		Dimension2D dimOnlyTiles = new Dimension2DDouble(0, 0);
		Dimension2D dimOnlyDiamond = new Dimension2DDouble(0, 0);
		Dimension2D dimBoth = new Dimension2DDouble(0, 0);
		for (int i = 0; i < tiles.size(); i++) {
			final Ftile tile = tiles.get(i);
			final Ftile diamond = diamonds.get(i);
			final FtileGeometry dimTile = tile.calculateDimension(stringBounder);
			final FtileGeometry dimDiamond = diamond.calculateDimension(stringBounder);
			final FtileGeometry both = dimDiamond.appendBottom(dimTile);
			dimOnlyTiles = Dimension2DDouble.mergeLR(dimOnlyTiles, dimTile);
			dimOnlyDiamond = Dimension2DDouble.mergeLR(dimOnlyDiamond, dimDiamond);
			dimBoth = Dimension2DDouble.mergeLR(dimBoth, both);
		}
		final FtileGeometry dimTile2 = tile2.calculateDimension(stringBounder);
		dimOnlyTiles = Dimension2DDouble.mergeLR(dimOnlyTiles, dimTile2);
		dimBoth = Dimension2DDouble.mergeLR(dimBoth, dimTile2);

		final Dimension2D result = new Dimension2DDouble(dimBoth.getWidth(), dimOnlyDiamond.getHeight() * 4
				+ dimOnlyTiles.getHeight());
		return Dimension2DDouble.delta(result, xSeparation * tiles.size(), 40);
	}

	private double getAllDiamondsHeight(StringBounder stringBounder) {
		Dimension2D dimOnlyDiamond = new Dimension2DDouble(0, 0);
		for (Ftile diamond : diamonds) {
			final Dimension2D dimDiamond = diamond.calculateDimension(stringBounder);
			dimOnlyDiamond = Dimension2DDouble.mergeLR(dimOnlyDiamond, dimDiamond);
		}
		return dimOnlyDiamond.getHeight();
	}

}
