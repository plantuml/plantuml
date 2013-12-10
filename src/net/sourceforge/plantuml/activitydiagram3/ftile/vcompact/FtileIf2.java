/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidth;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileIf2 extends AbstractFtile {

	private final double xSeparation = 20;

	private final List<Ftile> tiles;
	private final Ftile tile2;
	private final List<Ftile> diamonds;

	private final HtmlColor arrowColor;

	private FtileIf2(List<Ftile> diamonds, List<Ftile> tiles, Ftile tile2, HtmlColor arrowColor) {
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
			Branch branch2) {

		final List<Ftile> tiles = new ArrayList<Ftile>();

		for (Branch branch : thens) {
			tiles.add(new FtileMinWidth(branch.getFtile(), 30));
		}

		final Ftile tile2 = new FtileMinWidth(branch2.getFtile(), 30);

		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);

		final List<Ftile> diamonds = new ArrayList<Ftile>();
		final List<Connection> conns = new ArrayList<Connection>();
		for (Branch branch : thens) {
			final TextBlock tb1 = TextBlockUtils.create(branch.getLabelPositive(), fc, HorizontalAlignment.LEFT,
					ftileFactory);
			final TextBlock tbTest = TextBlockUtils.create(branch.getLabelTest(), fc, HorizontalAlignment.LEFT,
					ftileFactory);
			FtileDiamondInside diamond = new FtileDiamondInside(branch.shadowing(), backColor, borderColor, swimlane,
					tbTest);
			diamond = diamond.withNorth(tb1);
			diamonds.add(diamond);
		}

		final TextBlock tb2 = TextBlockUtils.create(branch2.getLabelPositive(), fc, HorizontalAlignment.LEFT,
				ftileFactory);
		final int last = diamonds.size() - 1;
		diamonds.set(last, ((FtileDiamondInside) diamonds.get(last)).withEast(tb2));

		final FtileIf2 result = new FtileIf2(diamonds, tiles, tile2, arrowColor);

		for (int i = 0; i < thens.size(); i++) {
			final Ftile ftile = tiles.get(i);
			final Ftile diam = diamonds.get(i);

			final HtmlColor color = thens.get(i).getInlinkRenderingColor();
			conns.add(result.new ConnectionHorizontalIn(diam, ftile, color == null ? arrowColor : color));
			conns.add(result.new ConnectionHorizontalOut(ftile, arrowColor));
		}

		for (int i = 0; i < diamonds.size() - 1; i++) {
			final Ftile diam1 = diamonds.get(i);
			final Ftile diam2 = diamonds.get(i + 1);
			conns.add(result.new ConnectionVertical(diam1, diam2, arrowColor));
		}
		conns.add(result.new ConnectionIn(arrowColor));
		conns.add(result.new ConnectionLastElseIn(arrowColor));
		conns.add(result.new ConnectionLastElseOut(arrowColor));
		conns.add(result.new ConnectionHline(arrowColor));

		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionVertical extends AbstractConnection {

		private final HtmlColor color;

		public ConnectionVertical(Ftile diam1, Ftile diam2, HtmlColor color) {
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
			final Dimension2D dimDiamond1 = getFtile1().asTextBlock().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(dimDiamond1.getWidth(), dimDiamond1.getHeight() / 2);

			return getTranslateDiamond1(getFtile1(), stringBounder).getTranslated(p);
		}

		private Point2D getP2(StringBounder stringBounder) {
			final Dimension2D dimDiamond1 = getFtile2().asTextBlock().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(0, dimDiamond1.getHeight() / 2);
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
			final Point2D p1 = getPointIn(ug.getStringBounder());
			final UTranslate tr = getTranslateDiamond1(getFtile2(), ug.getStringBounder());
			final Point2D p2 = tr.getTranslated(getFtile2().getPointIn(ug.getStringBounder()));
			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
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
			final Point2D p2 = tr2.getTranslated(getFtile2().getPointIn(ug.getStringBounder()));
			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			snake.addPoint(p1);
			snake.addPoint(p2.getX(), p1.getY());
			snake.addPoint(p2);
			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			final Dimension2D dimDiamond1 = getFtile1().asTextBlock().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(dimDiamond1.getWidth(), dimDiamond1.getHeight() / 2);
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
			final Point2D p1 = tr1.getTranslated(getFtile1().getPointOut(stringBounder));
			final double totalHeight = calculateDimensionInternal(stringBounder).getHeight();
			final Point2D p2 = new Point2D.Double(p1.getX(), totalHeight);

			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

	}

	class ConnectionHorizontalIn extends AbstractConnection {

		private final HtmlColor color;

		public ConnectionHorizontalIn(Ftile diamond, Ftile tile, HtmlColor color) {
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
			final Point2D p = getFtile1().getPointOut(stringBounder);
			return getTranslateDiamond1(getFtile1(), stringBounder).getTranslated(p);
		}

		private Point2D getP2(StringBounder stringBounder) {
			final Point2D p = getFtile2().getPointIn(stringBounder);
			return getTranslate1(getFtile2(), stringBounder).getTranslated(p);
		}

	}

	class ConnectionHorizontalOut extends AbstractConnection {

		private final HtmlColor color;

		public ConnectionHorizontalOut(Ftile tile, HtmlColor color) {
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
			final Point2D p = getFtile1().getPointOut(stringBounder);
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
			double minX = Double.MAX_VALUE;
			double maxX = 0;
			for (Ftile tmp : all) {
				if (tmp.isKilled()) {
					continue;
				}
				final UTranslate ut = getTranslateFor(tmp, stringBounder);
				final double middle = ut.getTranslated(tmp.getPointOut(stringBounder)).getX();
				minX = Math.min(minX, middle);
				maxX = Math.max(maxX, middle);
			}

			final Snake s = new Snake(arrowColor);
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
		// final Dimension2D dimDiamond1 = diamond1.asTextBlock().calculateDimension(stringBounder);
		final Dimension2D dim2 = tile2.asTextBlock().calculateDimension(stringBounder);

		final double x2 = dimTotal.getWidth() - dim2.getWidth();

		final double h = getAllDiamondsHeight(stringBounder);
		final double y2 = (dimTotal.getHeight() - h * 2 - dim2.getHeight()) / 2 + h;

		// final double y2 = (dimTotal.getHeight() - dimDiamond1.getHeight() * 2 - dim2.getHeight()) / 2
		// + dimDiamond1.getHeight();
		return new UTranslate(x2, y2);

	}

	private UTranslate getTranslateDiamond1(Ftile diamond1, StringBounder stringBounder) {
		double x1 = 0;

		for (Ftile diamond : diamonds) {
			final Dimension2D dim1 = dimDiamondAndTile(stringBounder, diamond);
			if (diamond == diamond1) {
				final Dimension2D dimDiamond = diamond.asTextBlock().calculateDimension(stringBounder);
				return new UTranslate(x1 + (dim1.getWidth() - dimDiamond.getWidth()) / 2, 25);
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
				final Dimension2D dimTile = tile.asTextBlock().calculateDimension(stringBounder);
				final double h = getAllDiamondsHeight(stringBounder);
				final double y1 = (dimTotal.getHeight() - 2 * h - dimTile.getHeight()) / 2 + h;
				return new UTranslate(x1 + (dim1.getWidth() - dimTile.getWidth()) / 2, y1);
			}
			x1 += dim1.getWidth() + xSeparation;
		}
		throw new IllegalArgumentException();

	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				for (Ftile tile : tiles) {
					ug.apply(getTranslate1(tile, stringBounder)).draw(tile);
				}
				for (Ftile diamond : diamonds) {
					ug.apply(getTranslateDiamond1(diamond, stringBounder)).draw(diamond);
				}

				// ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
				// ug.apply(getTranslate1(stringBounder)).draw(tile1);
				ug.apply(getTranslate2(stringBounder)).draw(tile2);
				// ug.apply(getTranslateDiamond2(stringBounder)).draw(diamond2);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return calculateDimensionInternal(stringBounder);
			}

		};
	}

	public boolean isKilled() {
		for (Ftile tile : tiles) {
			if (tile.isKilled() == false) {
				return false;
			}
		}
		return tile2.isKilled();
	}

	private Dimension2D dimDiamondAndTile(StringBounder stringBounder, Ftile tileOrDiamond) {
		for (int i = 0; i < tiles.size(); i++) {
			final Ftile tile = tiles.get(i);
			final Ftile diamond = diamonds.get(i);
			if (tile != tileOrDiamond && diamond != tileOrDiamond) {
				continue;
			}
			final Dimension2D dimTile = tile.asTextBlock().calculateDimension(stringBounder);
			final Dimension2D dimDiamond = diamond.asTextBlock().calculateDimension(stringBounder);
			return Dimension2DDouble.mergeTB(dimDiamond, dimTile);
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
			final Dimension2D dimTile = tile.asTextBlock().calculateDimension(stringBounder);
			final Dimension2D dimDiamond = diamond.asTextBlock().calculateDimension(stringBounder);
			final Dimension2D both = Dimension2DDouble.mergeTB(dimDiamond, dimTile);
			dimOnlyTiles = Dimension2DDouble.mergeLR(dimOnlyTiles, dimTile);
			dimOnlyDiamond = Dimension2DDouble.mergeLR(dimOnlyDiamond, dimDiamond);
			dimBoth = Dimension2DDouble.mergeLR(dimBoth, both);
		}
		final Dimension2D dimTile2 = tile2.asTextBlock().calculateDimension(stringBounder);
		dimOnlyTiles = Dimension2DDouble.mergeLR(dimOnlyTiles, dimTile2);
		dimBoth = Dimension2DDouble.mergeLR(dimBoth, dimTile2);

		final Dimension2D result = new Dimension2DDouble(dimBoth.getWidth(), dimOnlyDiamond.getHeight() * 4
				+ dimOnlyTiles.getHeight());
		return Dimension2DDouble.delta(result, xSeparation * tiles.size(), 50);
	}

	private double getAllDiamondsHeight(StringBounder stringBounder) {
		Dimension2D dimOnlyDiamond = new Dimension2DDouble(0, 0);
		for (Ftile diamond : diamonds) {
			final Dimension2D dimDiamond = diamond.asTextBlock().calculateDimension(stringBounder);
			dimOnlyDiamond = Dimension2DDouble.mergeLR(dimOnlyDiamond, dimDiamond);
		}
		return dimOnlyDiamond.getHeight();
	}

	public Point2D getPointIn(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		return new Point2D.Double(dimTotal.getWidth() / 2, 0);
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		return new Point2D.Double(dimTotal.getWidth() / 2, dimTotal.getHeight());
	}

}
