/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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

import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidth;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileOverpassing;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond.FtileIfWithLinks;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside3;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileIfLongVertical extends AbstractFtile {

	private final double ySeparation = 20;

	private final double marginy1 = 30;

	private final List<Ftile> tiles;
	private final Ftile tile2;
	private final List<Ftile> diamonds;
	private final Ftile lastDiamond;
	// private final List<Ftile> couples = new ArrayList<Ftile>();

	private final Rainbow arrowColor;

	private FtileIfLongVertical(List<Ftile> diamonds, List<Ftile> tiles, Ftile tile2, Rainbow arrowColor,
			Ftile lastDiamond) {
		super(tiles.get(0).skinParam());
		if (diamonds.size() != tiles.size()) {
			throw new IllegalArgumentException();
		}
		this.lastDiamond = lastDiamond;
		// for (int i = 0; i < diamonds.size(); i++) {
		// couples.add(new FtileAssemblySimple(diamonds.get(i), tiles.get(i)));
		// }
		this.tile2 = tile2;
		this.diamonds = new ArrayList<Ftile>(diamonds);
		this.tiles = new ArrayList<Ftile>(tiles);

		this.arrowColor = arrowColor;

	}

	// private static List<Ftile> alignDiamonds(List<Ftile> diamonds, StringBounder stringBounder) {
	// double maxOutY = 0;
	// for (Ftile diamond : diamonds) {
	// maxOutY = Math.max(maxOutY, diamond.calculateDimension(stringBounder).getOutY());
	// }
	// final List<Ftile> result = new ArrayList<Ftile>();
	// for (int i = 0; i < diamonds.size(); i++) {
	// Ftile diamond = diamonds.get(i);
	// final double missing = maxOutY - diamond.calculateDimension(stringBounder).getOutY();
	// assert missing >= 0;
	// diamond = FtileUtils.addVerticalMargin(diamond, missing / 2, 20);
	// result.add(diamond);
	// }
	// return result;
	// }

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
		return tiles.get(0).getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	static Ftile create(Swimlane swimlane, HtmlColor borderColor, HtmlColor backColor, Rainbow arrowColor,
			FtileFactory ftileFactory, ConditionStyle conditionStyle, List<Branch> thens, Branch branch2,
			FontConfiguration fc, LinkRendering topInlinkRendering, LinkRendering afterEndwhile) {
		final List<Ftile> tiles = new ArrayList<Ftile>();

		for (Branch branch : thens) {
			tiles.add(new FtileMinWidth(branch.getFtile(), 30));
		}

		final Ftile tile2 = new FtileMinWidth(branch2.getFtile(), 30);

		List<Ftile> diamonds = new ArrayList<Ftile>();
		for (Branch branch : thens) {
			final TextBlock tb1 = branch.getLabelPositive().create(fc, HorizontalAlignment.LEFT, ftileFactory.skinParam());
			final TextBlock tbTest = branch.getLabelTest().create(fc, HorizontalAlignment.LEFT, ftileFactory.skinParam());
			FtileDiamondInside3 diamond = new FtileDiamondInside3(branch.skinParam(), backColor, borderColor, swimlane,
					tbTest);
			diamond = diamond.withEast(tb1);
			diamonds.add(diamond);
		}

		final TextBlock tb2 = branch2.getLabelPositive().create(fc, HorizontalAlignment.LEFT, ftileFactory.skinParam());
		final int last = diamonds.size() - 1;
		diamonds.set(last, ((FtileDiamondInside3) diamonds.get(last)).withSouth(tb2));

		// diamonds = alignDiamonds(diamonds, ftileFactory.getStringBounder());

		final Ftile lastDiamond = new FtileDiamond(tiles.get(0).skinParam(), backColor, borderColor, swimlane);

		final FtileIfLongVertical result = new FtileIfLongVertical(diamonds, tiles, tile2, arrowColor, lastDiamond);

		final List<Connection> conns = new ArrayList<Connection>();
		for (int i = 0; i < thens.size(); i++) {
			final Ftile ftile = tiles.get(i);
			final Ftile diam = diamonds.get(i);

			final Rainbow color = FtileIfWithLinks.getInColor(thens.get(i), arrowColor);
			conns.add(result.new ConnectionVerticalIn(diam, ftile, color == null ? arrowColor : color));
			// conns.add(result.new ConnectionVerticalOut(ftile, arrowColor));
		}

		for (int i = 0; i < diamonds.size() - 1; i++) {
			conns.add(result.new ConnectionVertical(diamonds.get(i), diamonds.get(i + 1), arrowColor));
		}
		conns.add(result.new ConnectionThenOut(tiles.get(0), arrowColor));
		for (int i = 1; i < tiles.size(); i++) {
			conns.add(result.new ConnectionThenOutConnect(tiles.get(i), arrowColor));
		}

		final Rainbow topInColor = topInlinkRendering.getRainbow(arrowColor);
		// for (int i = 0; i < diamonds.size() - 1; i++) {
		// final Ftile diam1 = diamonds.get(i);
		// final Ftile diam2 = diamonds.get(i + 1);
		// conns.add(result.new ConnectionHorizontal(diam1, diam2, topInColor));
		// }
		conns.add(result.new ConnectionIn(topInColor));
		// conns.add(result.new ConnectionLastElseIn(FtileIfWithLinks.getInColor(branch2, arrowColor)));
		// conns.add(result.new ConnectionLastElseOut(arrowColor));
		// final HtmlColor horizontalOutColor = LinkRendering.getColor(afterEndwhile, arrowColor);
		// conns.add(result.new ConnectionHline(horizontalOutColor));
		conns.add(result.new ConnectionLastElse(topInColor));
		conns.add(result.new ConnectionLastElseOut(arrowColor));

		return FtileUtils.addConnection(result, conns);
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
			final UTranslate tr = getTranslateDiamond(getFtile2(), ug.getStringBounder());
			final Point2D p2 = tr.getTranslated(getFtile2().calculateDimension(ug.getStringBounder()).getPointIn());
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			final Point2D p1 = calculateDimensionInternal(ug.getStringBounder()).getPointIn();

			snake.addPoint(p1);
			snake.addPoint(p1.getX(), (p1.getY() + p2.getY()) / 2);
			snake.addPoint(p2.getX(), (p1.getY() + p2.getY()) / 2);
			snake.addPoint(p2);
			ug.draw(snake);
		}

	}

	class ConnectionVerticalIn extends AbstractConnection {

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
			snake.addPoint(p2.getX(), p1.getY());
			snake.addPoint(p2);
			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			final Dimension2D dimDiamond1 = getFtile1().calculateDimension(stringBounder);
			final double diamondWidth = dimDiamond1.getWidth();
			return getTranslateDiamond(getFtile1(), stringBounder).getTranslated(
					new Point2D.Double(diamondWidth, dimDiamond1.getHeight() / 2));
		}

		private Point2D getP2(StringBounder stringBounder) {
			final Point2D p = getFtile2().calculateDimension(stringBounder).getPointIn();
			return getTranslate1(getFtile2(), stringBounder).getTranslated(p);
		}

	}

	class ConnectionVertical extends AbstractConnection {

		private final Rainbow color;

		public ConnectionVertical(Ftile diamond1, Ftile diamond2, Rainbow color) {
			super(diamond1, diamond2);
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
			return getTranslateFor(getFtile1(), stringBounder).getTranslated(p);
		}

		private Point2D getP2(StringBounder stringBounder) {
			final Point2D p = getFtile2().calculateDimension(stringBounder).getPointIn();
			return getTranslateFor(getFtile2(), stringBounder).getTranslated(p);
		}

	}

	class ConnectionLastElse extends AbstractConnection {

		private final Rainbow arrowColor;

		public ConnectionLastElse(Rainbow arrowColor) {
			super(diamonds.get(diamonds.size() - 1), tile2);
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final UTranslate tr1 = getTranslateDiamond(getFtile1(), stringBounder);
			final FtileGeometry dimDiamond = getFtile1().calculateDimension(stringBounder);
			final Point2D p1 = tr1.getTranslated(dimDiamond.getPointOut());

			final Point2D p2 = getTranslate2(stringBounder).getTranslated(
					getFtile2().calculateDimension(stringBounder).getPointIn());

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			snake.addPoint(p1);
			snake.addPoint(p1.getX(), p2.getY() - 15);
			snake.addPoint(p2.getX(), p2.getY() - 15);
			snake.addPoint(p2);
			ug.draw(snake);
		}

	}

	class ConnectionLastElseOut extends AbstractConnection {

		private final Rainbow arrowColor;

		public ConnectionLastElseOut(Rainbow arrowColor) {
			super(tile2, lastDiamond);
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final FtileGeometry dim1 = getFtile1().calculateDimension(stringBounder);
			if (dim1.hasPointOut() == false) {
				return;
			}
			final Point2D p1 = getTranslate2(stringBounder).getTranslated(dim1.getPointOut());
			final Point2D p2 = getTranslateLastDiamond(stringBounder).getTranslated(
					getFtile2().calculateDimension(stringBounder).getPointIn());

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			snake.addPoint(p1);
			snake.addPoint(p1.getX(), p2.getY() - 15);
			snake.addPoint(p2.getX(), p2.getY() - 15);
			snake.addPoint(p2);
			ug.draw(snake);
		}

	}

	class ConnectionThenOut extends AbstractConnection {

		private final Rainbow arrowColor;

		public ConnectionThenOut(Ftile tile1, Rainbow arrowColor) {
			super(tile1, lastDiamond);
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final FtileGeometry dim1 = getFtile1().calculateDimension(stringBounder);
			if (dim1.hasPointOut() == false) {
				return;
			}
			final Point2D p1 = getTranslate1(getFtile1(), stringBounder).getTranslated(dim1.getPointOut());

			final FtileGeometry dimLastDiamond = getFtile2().calculateDimension(stringBounder);
			Point2D p2 = getTranslateLastDiamond(stringBounder).getTranslated(
					getFtile2().calculateDimension(stringBounder).getPointIn());
			p2 = new UTranslate(dimLastDiamond.getWidth() / 2, dimLastDiamond.getHeight() / 2).getTranslated(p2);

			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToLeft());
			snake.addPoint(p1);
			snake.addPoint(p1.getX(), p1.getY() + 15);
			snake.addPoint(dimTotal.getWidth(), p1.getY() + 15);
			snake.addPoint(dimTotal.getWidth(), p2.getY());
			snake.addPoint(p2);
			ug.draw(snake);
		}
	}

	class ConnectionThenOutConnect extends AbstractConnection {

		private final Rainbow arrowColor;

		public ConnectionThenOutConnect(Ftile tile1, Rainbow arrowColor) {
			super(tile1, lastDiamond);
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final FtileGeometry dim1 = getFtile1().calculateDimension(stringBounder);
			if (dim1.hasPointOut() == false) {
				return;
			}
			final Point2D p1 = getTranslate1(getFtile1(), stringBounder).getTranslated(dim1.getPointOut());

			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);

			final Point2D p2 = new Point2D.Double(dimTotal.getWidth(), p1.getY() + 15);

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToRight());
			snake.addPoint(p1);
			snake.addPoint(p1.getX(), p2.getY());
			snake.addPoint(p2);
			ug.draw(snake);
		}

	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == tile2) {
			return getTranslate2(stringBounder);
		}
		if (child == lastDiamond) {
			return getTranslateLastDiamond(stringBounder);
		}
		if (tiles.contains(child)) {
			return getTranslate1(child, stringBounder);
		}
		if (diamonds.contains(child)) {
			return getTranslateDiamond(child, stringBounder);
		}
		throw new UnsupportedOperationException();
	}

	private UTranslate getTranslateDiamond(Ftile diamond, StringBounder stringBounder) {
		final double allDiamondsWidth = allDiamondsWidth(stringBounder);

		final int idx = diamonds.indexOf(diamond);
		if (idx == -1) {
			throw new IllegalArgumentException();
		}
		final double y1 = getTranslateDy(idx, stringBounder);
		return new UTranslate((allDiamondsWidth - diamond.calculateDimension(stringBounder).getWidth()) / 2, y1);
	}

	private UTranslate getTranslateLastDiamond(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final FtileGeometry dimLast = lastDiamond.calculateDimension(stringBounder);
		final double x = (dimTotal.getWidth() - dimLast.getWidth()) / 2;
		return new UTranslate(x, dimTotal.getHeight() - dimLast.getHeight());
	}

	private UTranslate getTranslate1(Ftile candidat, StringBounder stringBounder) {
		final int idx = tiles.indexOf(candidat);
		if (idx == -1) {
			throw new IllegalArgumentException();
		}
		final double y1 = getTranslateDy(idx, stringBounder);
		final FtileGeometry diam = diamonds.get(idx).calculateDimension(stringBounder);
		final FtileGeometry dim1 = candidat.calculateDimension(stringBounder);
		final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);
		final double allDiamondsWidth = allDiamondsWidth(stringBounder);
		final double x = allDiamondsWidth + (dimTotal.getWidth() - allDiamondsWidth - dim1.getWidth()) / 2;
		return new UTranslate(x, y1 + diam.getHeight());
	}

	private double getTranslateDy(int idx, StringBounder stringBounder) {
		double y1 = marginy1;

		for (int i = 0; i < idx; i++) {
			final FtileGeometry dim1 = tiles.get(i).calculateDimension(stringBounder);
			final FtileGeometry diam = diamonds.get(i).calculateDimension(stringBounder);
			y1 += dim1.getHeight() + diam.getHeight() + ySeparation;
		}
		return y1;
	}

	private UTranslate getTranslate2(StringBounder stringBounder) {
		final double y1 = getTranslateDy(tiles.size(), stringBounder);
		final FtileGeometry dim2 = tile2.calculateDimension(stringBounder);
		final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);
		final double x = (dimTotal.getWidth() - dim2.getWidth()) / 2;
		return new UTranslate(x, y1);

		// final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		// final Dimension2D dim2 = tile2.calculateDimension(stringBounder);
		//
		// final double x2 = dimTotal.getWidth() - dim2.getWidth();
		//
		// final double h = 0; // getAllDiamondsHeight(stringBounder);
		// final double y2 = (dimTotal.getHeight() - h * 2 - dim2.getHeight()) / 2 + h;
		//
		// return new UTranslate(x2, y2);

	}

	// private UTranslate getTranslateCouple1(Ftile candidat, StringBounder stringBounder) {
	// double x1 = 0;
	//
	// for (Ftile couple : couples) {
	// final FtileGeometry dim1 = couple.calculateDimension(stringBounder);
	// if (couple == candidat) {
	// return new UTranslate(x1, 25);
	// }
	// x1 += dim1.getWidth() + xSeparation;
	// }
	// throw new IllegalArgumentException();
	//
	// }

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		for (Ftile tile1 : tiles) {
			ug.apply(getTranslate1(tile1, stringBounder)).draw(tile1);
		}
		for (Ftile diam : diamonds) {
			ug.apply(getTranslateDiamond(diam, stringBounder)).draw(diam);
		}

		ug.apply(getTranslate2(stringBounder)).draw(tile2);
		ug.apply(getTranslateLastDiamond(stringBounder)).draw(lastDiamond);
	}

	private FtileGeometry calculateDimensionInternal(StringBounder stringBounder) {
		// FtileGeometry result = new FtileGeometry(0, marginy1, 0, 0);
		double col1 = 0;
		double col1overpass = 0;
		double col2 = 0;
		double height = marginy1;
		for (int i = 0; i < tiles.size(); i++) {
			final FtileGeometry dim1 = tiles.get(i).calculateDimension(stringBounder);
			final FtileGeometry diamondOverpassing = ((FtileOverpassing) diamonds.get(i))
					.getOverpassDimension(stringBounder);
			final FtileGeometry diamondDim = diamonds.get(i).calculateDimension(stringBounder);

			height += diamondDim.getHeight() + dim1.getHeight();
			col1 = Math.max(col1, diamondDim.getWidth());
			col1overpass = Math.max(col1overpass, diamondOverpassing.getWidth());
			col2 = Math.max(col2, dim1.getWidth());
		}
		final double width = Math.max(col1 + col2, col1overpass);
		FtileGeometry result = new FtileGeometry(width, height, width / 2, 0);

		final FtileGeometry dimTile2 = tile2.calculateDimension(stringBounder);
		result = result.appendBottom(dimTile2);
		final FtileGeometry dimLastDiamond = lastDiamond.calculateDimension(stringBounder);
		final double lastElseArrowHeight = 40;
		result = result.addDim(0, ySeparation * tiles.size() + lastElseArrowHeight + dimLastDiamond.getHeight());

		return new FtileGeometry(result, result.getWidth() / 2, 0);
	}

	private double allDiamondsWidth(StringBounder stringBounder) {
		double width = 0;
		for (Ftile diam : diamonds) {
			width = Math.max(width, diam.calculateDimension(stringBounder).getWidth());
		}
		return width;
	}

	private double allTile1Width(StringBounder stringBounder) {
		double width = 0;
		for (Ftile tile1 : tiles) {
			width = Math.max(width, tile1.calculateDimension(stringBounder).getWidth());
		}
		return width;
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
