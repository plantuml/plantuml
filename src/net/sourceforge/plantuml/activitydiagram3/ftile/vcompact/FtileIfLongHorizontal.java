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
import java.util.Collection;
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
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidthCentered;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.MergeStrategy;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond.FtileIfWithLinks;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside2;
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

class FtileIfLongHorizontal extends AbstractFtile {

	private final double xSeparation = 20;

	private final List<Ftile> tiles;
	private final Ftile tile2;
	private final List<Ftile> diamonds;
	private final List<Ftile> couples = new ArrayList<Ftile>();

	private final Rainbow arrowColor;

	private FtileIfLongHorizontal(List<Ftile> diamonds, List<Double> inlabelSizes, List<Ftile> tiles, Ftile tile2,
			Rainbow arrowColor) {
		super(tiles.get(0).skinParam());
		if (diamonds.size() != tiles.size()) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < diamonds.size(); i++) {
			final Ftile diamond = diamonds.get(i);
			final FtileAssemblySimple tmp = new FtileAssemblySimple(diamond, tiles.get(i));
			couples.add(FtileUtils.addHorizontalMargin(tmp, inlabelSizes.get(i), 0));
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

	static Ftile create(Swimlane swimlane, HColor borderColor, HColor backColor, Rainbow arrowColor,
			FtileFactory ftileFactory, ConditionStyle conditionStyle, List<Branch> thens, Branch branch2,
			FontConfiguration fcArrow, LinkRendering topInlinkRendering, LinkRendering afterEndwhile,
			FontConfiguration fcTest) {
		if (afterEndwhile == null) {
			throw new IllegalArgumentException();
		}
		final List<Ftile> tiles = new ArrayList<Ftile>();

		for (Branch branch : thens) {
			tiles.add(new FtileMinWidthCentered(branch.getFtile(), 30));
		}

		final Ftile tile2 = new FtileMinWidthCentered(branch2.getFtile(), 30);

		List<Ftile> diamonds = new ArrayList<Ftile>();
		List<Double> inlabelSizes = new ArrayList<Double>();
		for (Branch branch : thens) {
			final TextBlock tb1 = branch.getLabelPositive().create(fcArrow, HorizontalAlignment.LEFT,
					ftileFactory.skinParam());
			final TextBlock tbTest = branch.getLabelTest().create(fcTest,
					ftileFactory.skinParam().getDefaultTextAlignment(HorizontalAlignment.LEFT),
					ftileFactory.skinParam());
			final HColor diamondColor = branch.getColor() == null ? backColor : branch.getColor();

			FtileDiamondInside2 diamond = new FtileDiamondInside2(branch.skinParam(), diamondColor, borderColor,
					swimlane, tbTest);
			TextBlock tbInlabel = null;
			if (Display.isNull(branch.getInlabel())) {
				inlabelSizes.add(0.0);
			} else {
				tbInlabel = branch.getInlabel().create(fcArrow, HorizontalAlignment.LEFT, ftileFactory.skinParam());
				inlabelSizes.add(tbInlabel.calculateDimension(ftileFactory.getStringBounder()).getWidth());
				diamond = diamond.withWest(tbInlabel);
			}
			diamond = diamond.withNorth(tb1);
			diamonds.add(diamond);
		}

		final TextBlock tb2 = branch2.getLabelPositive().create(fcArrow, HorizontalAlignment.LEFT,
				ftileFactory.skinParam());
		final int last = diamonds.size() - 1;
		diamonds.set(last, ((FtileDiamondInside2) diamonds.get(last)).withEast(tb2));

		diamonds = alignDiamonds(diamonds, ftileFactory.getStringBounder());

		final FtileIfLongHorizontal result = new FtileIfLongHorizontal(diamonds, inlabelSizes, tiles, tile2,
				arrowColor);
		final List<Connection> conns = new ArrayList<Connection>();

		int nbOut = 0;

		for (int i = 0; i < thens.size(); i++) {
			final Ftile ftile = tiles.get(i);
			final Ftile diam = diamonds.get(i);

			final Rainbow rainbowIn = FtileIfWithLinks.getInColor(thens.get(i), arrowColor);
			final Branch branch = thens.get(i);

			if (branch.getFtile().calculateDimension(ftileFactory.getStringBounder()).hasPointOut()) {
				nbOut++;
			}
			final Rainbow rainbowOut = branch.getInlinkRenderingColorAndStyle();
			TextBlock out2 = null;
			if (branch.getSpecial() != null) {
				out2 = branch.getSpecial().getDisplay().create(fcTest, HorizontalAlignment.LEFT,
						ftileFactory.skinParam());
			}
			conns.add(result.new ConnectionVerticalIn(diam, ftile, rainbowIn.size() == 0 ? arrowColor : rainbowIn));
			conns.add(result.new ConnectionVerticalOut(ftile, rainbowOut.size() == 0 ? arrowColor : rainbowOut, out2));
		}

		final Rainbow topInColor = topInlinkRendering.getRainbow(arrowColor);
		for (int i = 0; i < diamonds.size() - 1; i++) {
			final Ftile diam1 = diamonds.get(i);
			final Ftile diam2 = diamonds.get(i + 1);
			conns.add(result.new ConnectionHorizontal(diam1, diam2, arrowColor));
		}
		conns.add(result.new ConnectionIn(topInColor));
		TextBlock out2 = null;
		if (branch2.getSpecial() != null) {
			out2 = branch2.getSpecial().getDisplay().create(fcTest, HorizontalAlignment.LEFT, ftileFactory.skinParam());
		}

		conns.add(result.new ConnectionLastElseIn(FtileIfWithLinks.getInColor(branch2, arrowColor)));
		conns.add(result.new ConnectionLastElseOut(arrowColor, out2, nbOut));
		final boolean horizontalOut = nbOut > 0;
		if (horizontalOut) {
			final Rainbow horizontalOutColor = afterEndwhile.getRainbow(arrowColor);
			conns.add(result.new ConnectionHline(horizontalOutColor));
		}

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

			final Snake snake = new Snake(arrowHorizontalAlignment(), color, Arrows.asToRight());
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = getFtile1().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(dimDiamond1.getLeft() * 2,
					getYdiamontOutToLeft(dimDiamond1, stringBounder));

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
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
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
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			snake.addPoint(p1);
			snake.addPoint(p2.getX(), p1.getY());
			snake.addPoint(p2);
			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = getFtile1().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(dimDiamond1.getLeft() * 2,
					getYdiamontOutToLeft(dimDiamond1, stringBounder));
			return getTranslateDiamond1(getFtile1(), stringBounder).getTranslated(p);
		}

	}

	class ConnectionLastElseOut extends AbstractConnection {

		private final Rainbow arrowColor;
		private final TextBlock out2;
		private final int nbOut;

		public ConnectionLastElseOut(Rainbow arrowColor, TextBlock out2, int nbOut) {
			super(tile2, null);
			this.arrowColor = arrowColor;
			this.out2 = out2;
			this.nbOut = nbOut;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final UTranslate tr1 = getTranslate2(stringBounder);
			final FtileGeometry dim = getFtile1().calculateDimension(stringBounder);
			if (dim.hasPointOut() == false) {
				return;
			}
			final Point2D p1 = tr1.getTranslated(dim.getPointOut());
			final FtileGeometry full = calculateDimensionInternal(stringBounder);
			final double totalHeight = full.getHeight();
			final Point2D p2 = new Point2D.Double(p1.getX(), totalHeight);

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			snake.setLabel(out2);
			snake.addPoint(p1);
			snake.addPoint(p2);
			if (nbOut == 0) {
				snake.addPoint(new Point2D.Double(full.getLeft(), totalHeight));
			}
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

			final Snake snake = new Snake(arrowHorizontalAlignment(), color, Arrows.asToDown());
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

			final Snake snake = new Snake(arrowHorizontalAlignment(), color, Arrows.asToDown());

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
		private final TextBlock out2;

		public ConnectionVerticalOut(Ftile tile, Rainbow color, TextBlock out2) {
			super(tile, null);
			this.color = color;
			this.out2 = out2;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final double totalHeight = calculateDimensionInternal(stringBounder).getHeight();
			final Point2D p1 = getP1(stringBounder);
			if (p1 == null) {
				return;
			}
			final Point2D p2 = new Point2D.Double(p1.getX(), totalHeight);

			final Snake snake = new Snake(arrowHorizontalAlignment(), color, Arrows.asToDown());
			snake.setLabel(out2);
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

			final List<Ftile> allTiles = new ArrayList<Ftile>(couples);
			allTiles.add(tile2);

			final double[] minmax;
			if (ug instanceof UGraphicInterceptorOneSwimlane) {
				final UGraphicInterceptorOneSwimlane interceptor = (UGraphicInterceptorOneSwimlane) ug;
				final List<Swimlane> allSwimlanes = interceptor.getOrderedListOfAllSwimlanes();
				minmax = getMinmax(stringBounder, totalDim.getWidth(), allTiles, interceptor.getSwimlane(),
						allSwimlanes);
			} else {
				minmax = getMinmaxSimple(stringBounder, totalDim.getWidth(), allTiles);
			}

			final double minX = minmax[0];
			final double maxX = minmax[1];
			if (Double.isNaN(minX) || Double.isNaN(maxX)) {
				return;
			}

			final Snake s = new Snake(arrowHorizontalAlignment(), arrowColor);
			s.goUnmergeable(MergeStrategy.NONE);
			s.addPoint(minX, totalDim.getHeight());
			s.addPoint(maxX, totalDim.getHeight());
			ug.draw(s);
		}

		private Double getLeftOut(final StringBounder stringBounder) {
			final FtileGeometry dim = calculateDimension(stringBounder);
			if (dim.hasPointOut()) {
				return dim.getLeft();
			}
			return null;
		}

		private double[] getMinmax(StringBounder stringBounder, double width, List<Ftile> allTiles, Swimlane intoSw,
				List<Swimlane> allSwimlanes) {
			final int current = allSwimlanes.indexOf(intoSw);
			final Double leftOut = getLeftOut(stringBounder);
			if (leftOut == null)
				return new double[] { Double.NaN, Double.NaN };

			if (current == -1) {
				throw new IllegalStateException();
			}
			final int first = getFirstSwimlane(stringBounder, allTiles, allSwimlanes);
			final int last = getLastSwimlane(stringBounder, allTiles, allSwimlanes);
			if (current < first || current > last)
				return new double[] { Double.NaN, Double.NaN };
			double minX = current != first ? 0 : width;
			double maxX = current != last ? width : 0;
			minX = Math.min(minX, leftOut);
			maxX = Math.max(maxX, leftOut);
			for (Ftile tmp : allTiles) {
				if (tmp.calculateDimension(stringBounder).hasPointOut() == false) {
					continue;
				}
				if (ftileDoesOutcomeInThatSwimlane(tmp, intoSw) == false) {
					continue;
				}
				final UTranslate ut = getTranslateFor(tmp, stringBounder);
				final double out = tmp.calculateDimension(stringBounder).translate(ut).getLeft();
				minX = Math.min(minX, out);
				maxX = Math.max(maxX, out);
			}
			return new double[] { minX, maxX };
		}

		private double[] getMinmaxSimple(StringBounder stringBounder, double width, List<Ftile> allTiles) {
			final Double leftOut = getLeftOut(stringBounder);
			if (leftOut == null)
				return new double[] { Double.NaN, Double.NaN };
			double minX = width / 2;
			double maxX = width / 2;
			minX = Math.min(minX, leftOut);
			maxX = Math.max(maxX, leftOut);
			for (Ftile tmp : allTiles) {
				if (tmp.calculateDimension(stringBounder).hasPointOut() == false) {
					continue;
				}
				final UTranslate ut = getTranslateFor(tmp, stringBounder);
				final double out = tmp.calculateDimension(stringBounder).translate(ut).getLeft();
				minX = Math.min(minX, out);
				maxX = Math.max(maxX, out);
			}
			return new double[] { minX, maxX };
		}

		private int getFirstSwimlane(StringBounder stringBounder, List<Ftile> allTiles, List<Swimlane> allSwimlanes) {
			for (int i = 0; i < allSwimlanes.size(); i++) {
				if (atLeastOne(stringBounder, allSwimlanes.get(i), allTiles)) {
					return i;
				}
			}
			throw new IllegalStateException();
		}

		private int getLastSwimlane(StringBounder stringBounder, List<Ftile> allTiles, List<Swimlane> allSwimlanes) {
			for (int i = allSwimlanes.size() - 1; i >= 0; i--) {
				if (atLeastOne(stringBounder, allSwimlanes.get(i), allTiles)) {
					return i;
				}
			}
			throw new IllegalStateException();
		}

		private boolean atLeastOne(StringBounder stringBounder, Swimlane intoSw, List<Ftile> allTiles) {
			for (Ftile tmp : allTiles)
				if (tmp.calculateDimension(stringBounder).hasPointOut() && ftileDoesOutcomeInThatSwimlane(tmp, intoSw))
					return true;
			return false;
		}

		private boolean ftileDoesOutcomeInThatSwimlane(Ftile ftile, Swimlane swimlane) {
			return ftile.getSwimlaneOut() == swimlane && ftile.getSwimlanes().contains(swimlane);
		}

	}

	@Override
	public Collection<Ftile> getMyChildren() {
		final List<Ftile> result = new ArrayList<Ftile>(tiles);
		result.add(tile2);
		return Collections.unmodifiableList(result);
	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == tile2) {
			return getTranslate2(stringBounder);
		}
		if (couples.contains(child)) {
			return getTranslateCouple1(child, stringBounder);
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

	private UTranslate getTranslate1(Ftile tile, StringBounder stringBounder) {
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
		Dimension2D dimTile2 = tile2.calculateDimension(stringBounder);
		dimTile2 = Dimension2DDouble.delta(dimTile2, 0, getDiamondsHeight(stringBounder) / 2);
		result = Dimension2DDouble.mergeLR(result, dimTile2);
		result = Dimension2DDouble.delta(result, xSeparation * couples.size(), 100);

		return new FtileGeometry(result, result.getWidth() / 2, 0);
	}

	private double getDiamondsHeight(StringBounder stringBounder) {
		double height = 0;
		for (Ftile diamond : diamonds) {
			height = Math.max(height, diamond.calculateDimension(stringBounder).getHeight());
		}
		return height;
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
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
