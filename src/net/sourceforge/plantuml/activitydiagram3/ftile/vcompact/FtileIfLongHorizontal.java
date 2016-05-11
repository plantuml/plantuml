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
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileAssemblySimple;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidth;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond.FtileIfWithLinks;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside2;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileIfLongHorizontal extends AbstractFtile {

	private final double xSeparation = 20;

	private final List<Ftile> tiles;
	private final Ftile tile2;
	private final List<Ftile> diamonds;
	private final List<Ftile> couples = new ArrayList<Ftile>();

	private final Rainbow arrowColor;

	private FtileIfLongHorizontal(List<Ftile> diamonds, List<Ftile> tiles, Ftile tile2, Rainbow arrowColor) {
		super(tiles.get(0).shadowing() || tile2.shadowing());
		if (diamonds.size() != tiles.size()) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < diamonds.size(); i++) {
			couples.add(new FtileAssemblySimple(diamonds.get(i), tiles.get(i)));
		}
		this.tile2 = tile2;
		this.diamonds = new ArrayList<Ftile>(diamonds);
		this.tiles = new ArrayList<Ftile>(tiles);

		this.arrowColor = arrowColor;

	}

	private static List<Ftile> alignDiamonds(List<Ftile> diamonds, StringBounder stringBounder) {
		double maxOutY = 0;
		for (Ftile diamond : diamonds) {
			maxOutY = Math.max(maxOutY, diamond.calculateDimension(stringBounder).getOutY());
		}
		final List<Ftile> result = new ArrayList<Ftile>();
		for (int i = 0; i < diamonds.size(); i++) {
			Ftile diamond = diamonds.get(i);
			final double missing = maxOutY - diamond.calculateDimension(stringBounder).getOutY();
			assert missing >= 0;
			diamond = FtileUtils.addVerticalMargin(diamond, missing / 2, 20);
			result.add(diamond);
		}
		return result;
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		if (getSwimlaneIn() != null) {
			result.add(getSwimlaneIn());
		}
		for (Ftile tile : couples) {
			result.addAll(tile.getSwimlanes());
		}
		result.addAll(tile2.getSwimlanes());
		return Collections.unmodifiableSet(result);
	}

	public Swimlane getSwimlaneIn() {
		return couples.get(0).getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	static Ftile create(Swimlane swimlane, HtmlColor borderColor, HtmlColor backColor, Rainbow arrowColor,
			FtileFactory ftileFactory, ConditionStyle conditionStyle, List<Branch> thens, Branch branch2,
			FontConfiguration fc, LinkRendering topInlinkRendering, LinkRendering afterEndwhile) {
		if (afterEndwhile == null) {
			throw new IllegalArgumentException();
		}
		final List<Ftile> tiles = new ArrayList<Ftile>();

		for (Branch branch : thens) {
			tiles.add(new FtileMinWidth(branch.getFtile(), 30));
		}

		final Ftile tile2 = new FtileMinWidth(branch2.getFtile(), 30);

		List<Ftile> diamonds = new ArrayList<Ftile>();
		for (Branch branch : thens) {
			final TextBlock tb1 = branch.getLabelPositive().create(fc, HorizontalAlignment.LEFT, ftileFactory);
			final TextBlock tbTest = branch.getLabelTest().create(fc, HorizontalAlignment.LEFT, ftileFactory);
			FtileDiamondInside2 diamond = new FtileDiamondInside2(branch.shadowing(), backColor, borderColor, swimlane,
					tbTest);
			diamond = diamond.withNorth(tb1);
			diamonds.add(diamond);
		}

		final TextBlock tb2 = branch2.getLabelPositive().create(fc, HorizontalAlignment.LEFT, ftileFactory);
		final int last = diamonds.size() - 1;
		diamonds.set(last, ((FtileDiamondInside2) diamonds.get(last)).withEast(tb2));

		diamonds = alignDiamonds(diamonds, ftileFactory.getStringBounder());

		final FtileIfLongHorizontal result = new FtileIfLongHorizontal(diamonds, tiles, tile2, arrowColor);
		final List<Connection> conns = new ArrayList<Connection>();

		for (int i = 0; i < thens.size(); i++) {
			final Ftile ftile = tiles.get(i);
			final Ftile diam = diamonds.get(i);

			final Rainbow color = FtileIfWithLinks.getInColor(thens.get(i), arrowColor);
			conns.add(result.new ConnectionVerticalIn(diam, ftile, color == null ? arrowColor : color));
			conns.add(result.new ConnectionVerticalOut(ftile, arrowColor));
		}

		final Rainbow topInColor = topInlinkRendering.getRainbow(arrowColor);
		for (int i = 0; i < diamonds.size() - 1; i++) {
			final Ftile diam1 = diamonds.get(i);
			final Ftile diam2 = diamonds.get(i + 1);
			conns.add(result.new ConnectionHorizontal(diam1, diam2, topInColor));
		}
		conns.add(result.new ConnectionIn(topInColor));
		conns.add(result.new ConnectionLastElseIn(FtileIfWithLinks.getInColor(branch2, arrowColor)));
		conns.add(result.new ConnectionLastElseOut(arrowColor));
		final Rainbow horizontalOutColor = afterEndwhile.getRainbow(arrowColor);
		conns.add(result.new ConnectionHline(horizontalOutColor));
		// conns.add(result.new ConnectionHline(HtmlColorUtils.BLUE));

		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionHorizontal extends AbstractConnection {

		private final Rainbow color;

		public ConnectionHorizontal(Ftile diam1, Ftile diam2, Rainbow color) {
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
			final Point2D p = new Point2D.Double(dimDiamond1.getLeft() * 2, getYdiamontOutToLeft(dimDiamond1,
					stringBounder));

			return getTranslateDiamond1(getFtile1(), stringBounder).getTranslated(p);
		}

		private Point2D getP2(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = getFtile2().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(0, getYdiamontOutToLeft(dimDiamond1, stringBounder));
			return getTranslateDiamond1(getFtile2(), stringBounder).getTranslated(p);
		}

	}

	static private double getYdiamontOutToLeft(FtileGeometry dimDiamond1, StringBounder stringBounder) {
		return (dimDiamond1.getInY() + dimDiamond1.getOutY()) / 2;
	}

	class ConnectionIn extends AbstractConnection {

		private final Rainbow arrowColor;

		public ConnectionIn(Rainbow arrowColor) {
			super(null, diamonds.get(0));
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final UTranslate tr = getTranslateDiamond1(getFtile2(), ug.getStringBounder());
			final Point2D p2 = tr.getTranslated(getFtile2().calculateDimension(ug.getStringBounder()).getPointIn());
			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			final Point2D p1 = calculateDimensionInternal(ug.getStringBounder()).getPointIn();

			snake.addPoint(p1);
			snake.addPoint(p2.getX(), p1.getY());
			snake.addPoint(p2);
			ug.draw(snake);
		}

	}

	class ConnectionLastElseIn extends AbstractConnection {

		private final Rainbow arrowColor;

		public ConnectionLastElseIn(Rainbow arrowColor) {
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
			final Point2D p = new Point2D.Double(dimDiamond1.getLeft() * 2, getYdiamontOutToLeft(dimDiamond1,
					stringBounder));
			return getTranslateDiamond1(getFtile1(), stringBounder).getTranslated(p);
		}

	}

	class ConnectionLastElseOut extends AbstractConnection {

		private final Rainbow arrowColor;

		public ConnectionLastElseOut(Rainbow arrowColor) {
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

	class ConnectionVerticalIn extends AbstractConnection implements ConnectionTranslatable {

		private final Rainbow color;

		public ConnectionVerticalIn(Ftile diamond, Ftile tile, Rainbow color) {
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

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final Point2D p1 = getP1(ug.getStringBounder());
			final Point2D p2 = getP2(ug.getStringBounder());

			final Snake snake = new Snake(color, Arrows.asToDown());

			final Point2D mp1a = translate1.getTranslated(p1);
			final Point2D mp2b = translate2.getTranslated(p2);
			final double middle = mp1a.getY() + 4;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			snake.addPoint(mp2b);
			ug.draw(snake);
		}

	}

	class ConnectionVerticalOut extends AbstractConnection {

		private final Rainbow color;

		public ConnectionVerticalOut(Ftile tile, Rainbow color) {
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

		private final Rainbow arrowColor;

		public ConnectionHline(Rainbow arrowColor) {
			super(null, null);
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Dimension2D totalDim = calculateDimensionInternal(stringBounder);

			final Swimlane intoSw;
			if (ug instanceof UGraphicInterceptorOneSwimlane) {
				intoSw = ((UGraphicInterceptorOneSwimlane) ug).getSwimlane();
			} else {
				intoSw = null;
			}

			final List<Ftile> all = new ArrayList<Ftile>(couples);
			all.add(tile2);
			double minX = totalDim.getWidth() / 2;
			double maxX = totalDim.getWidth() / 2;
			boolean atLeastOne = false;
			for (Ftile tmp : all) {
				if (tmp.calculateDimension(stringBounder).hasPointOut() == false) {
					continue;
				}
				if (intoSw != null && tmp.getSwimlanes().contains(intoSw) == false) {
					continue;
				}
				if (intoSw != null && tmp.getSwimlaneOut() != intoSw) {
					continue;
				}
				atLeastOne = true;
				final UTranslate ut = getTranslateFor(tmp, stringBounder);
				final double out = tmp.calculateDimension(stringBounder).translate(ut).getLeft();
				minX = Math.min(minX, out);
				maxX = Math.max(maxX, out);
			}
			if (atLeastOne == false) {
				return;
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
		if (couples.contains(child)) {
			return getTranslateCouple1(child, stringBounder);
		}
		throw new UnsupportedOperationException();
	}

	private UTranslate getTranslate2(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dim2 = tile2.calculateDimension(stringBounder);

		final double x2 = dimTotal.getWidth() - dim2.getWidth();

		final double h = 0; // getAllDiamondsHeight(stringBounder);
		final double y2 = (dimTotal.getHeight() - h * 2 - dim2.getHeight()) / 2 + h;

		return new UTranslate(x2, y2);

	}

	private UTranslate getTranslateDiamond1(Ftile diamond, StringBounder stringBounder) {
		final int idx = diamonds.indexOf(diamond);
		if (idx == -1) {
			throw new IllegalArgumentException();
		}
		final UTranslate trCouple = getTranslateCouple1(couples.get(idx), stringBounder);
		final UTranslate in = couples.get(idx).getTranslateFor(diamond, stringBounder);
		return trCouple.compose(in);
	}

	public UTranslate getTranslate1(Ftile tile, StringBounder stringBounder) {
		final int idx = tiles.indexOf(tile);
		if (idx == -1) {
			throw new IllegalArgumentException();
		}
		final UTranslate trCouple = getTranslateCouple1(couples.get(idx), stringBounder);
		final UTranslate in = couples.get(idx).getTranslateFor(tile, stringBounder);
		return trCouple.compose(in);
	}

	private UTranslate getTranslateCouple1(Ftile candidat, StringBounder stringBounder) {
		double x1 = 0;

		for (Ftile couple : couples) {
			final FtileGeometry dim1 = couple.calculateDimension(stringBounder);
			if (couple == candidat) {
				return new UTranslate(x1, 25);
			}
			x1 += dim1.getWidth() + xSeparation;
		}
		throw new IllegalArgumentException();

	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		for (Ftile couple : couples) {
			ug.apply(getTranslateCouple1(couple, stringBounder)).draw(couple);
		}

		ug.apply(getTranslate2(stringBounder)).draw(tile2);
	}

	private FtileGeometry calculateDimensionInternal(StringBounder stringBounder) {
		Dimension2D result = new Dimension2DDouble(0, 0);
		for (Ftile couple : couples) {
			result = Dimension2DDouble.mergeLR(result, couple.calculateDimension(stringBounder));
		}
		final FtileGeometry dimTile2 = tile2.calculateDimension(stringBounder);
		result = Dimension2DDouble.mergeLR(result, dimTile2);
		result = Dimension2DDouble.delta(result, xSeparation * couples.size(), 100);

		return new FtileGeometry(result, result.getWidth() / 2, 0);
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

}
