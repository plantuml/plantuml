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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Hexagon;
import net.sourceforge.plantuml.activitydiagram3.ftile.MergeStrategy;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.UGraphicInterceptorOneSwimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.svek.ConditionEndStyle;
import net.sourceforge.plantuml.utils.Direction;

public class FtileIfWithLinks extends FtileIfWithDiamonds {

	private final ConditionEndStyle conditionEndStyle;
	private final Rainbow arrowColor;

	public FtileIfWithLinks(Ftile diamond1, Ftile tile1, Ftile tile2, Ftile diamond2, Swimlane in, Rainbow arrowColor,
			ConditionEndStyle conditionEndStyle, StringBounder stringBounder, Collection<PositionedNote> notes) {
		super(diamond1, tile1, tile2, diamond2, in, stringBounder, notes);
		this.arrowColor = arrowColor;
		this.conditionEndStyle = conditionEndStyle;
		if (arrowColor.size() == 0)
			throw new IllegalArgumentException();

	}

	@Override
	protected double getYdeltaForLabels(StringBounder stringBounder) {
		if (diamond2 instanceof FtileDiamond)
			return ((FtileDiamond) diamond2).getWestEastLabelHeight(stringBounder);

		return 0;
	}

	class ConnectionHorizontalThenVertical extends AbstractConnection implements ConnectionTranslatable {

		private final Rainbow color;
		private final UPolygon usingArrow;

		public ConnectionHorizontalThenVertical(Ftile tile, Branch branch) {
			super(diamond1, tile);
			color = branch.getInColor(arrowColor);
			if (color.size() == 0)
				throw new IllegalArgumentException();

			usingArrow = branch.isEmpty() ? null : skinParam().arrows().asToDown();
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final XPoint2D p1 = getP1(stringBounder);
			final XPoint2D p2 = getP2(stringBounder);
			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final Snake snake = Snake.create(skinParam(), color, usingArrow);
			snake.addPoint(x1, y1);
			snake.addPoint(x2, y1);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

		private XPoint2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final XPoint2D pt;
			if (getFtile2() == tile1)
				pt = dimDiamond1.getPointD();
			else if (getFtile2() == tile2)
				pt = dimDiamond1.getPointB();
			else
				throw new IllegalStateException();

			return getTranslateDiamond1(stringBounder).getTranslated(pt);
		}

		private XPoint2D getP2(final StringBounder stringBounder) {
			return translate(stringBounder).getTranslated(getFtile2().calculateDimension(stringBounder).getPointIn());
		}

		private UTranslate translate(StringBounder stringBounder) {
			if (getFtile2() == tile1)
				return getTranslateBranch1(stringBounder);

			if (getFtile2() == tile2)
				return getTranslateBranch2(stringBounder);

			throw new IllegalStateException();
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			XPoint2D p1 = getP1(stringBounder);
			XPoint2D p2 = getP2(stringBounder);
			final Direction originalDirection = Direction.leftOrRight(p1, p2);
			p1 = translate1.getTranslated(p1);
			p2 = translate2.getTranslated(p2);
			final Direction newDirection = Direction.leftOrRight(p1, p2);
			if (originalDirection != newDirection) {
				final double delta = (originalDirection == Direction.RIGHT ? -1 : 1) * Hexagon.hexagonHalfSize;
				final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
				final Snake small = Snake.create(skinParam(), color);
				small.addPoint(p1);
				small.addPoint(p1.getX() + delta, p1.getY());
				small.addPoint(p1.getX() + delta, p1.getY() + dimDiamond1.getHeight() * .75);
				ug.draw(small);
				p1 = small.getLast();
			}
			final Snake snake = Snake.create(skinParam(), color, usingArrow).withMerge(MergeStrategy.LIMITED);
			snake.addPoint(p1);
			snake.addPoint(p2.getX(), p1.getY());
			snake.addPoint(p2);
			ug.draw(snake);

		}

	}

	class ConnectionVerticalThenHorizontal extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow myArrowColor;
		private final boolean branchEmpty;

		public ConnectionVerticalThenHorizontal(Ftile tile, Rainbow myArrowColor, boolean branchEmpty) {
			super(tile, diamond2);
			this.myArrowColor = myArrowColor == null || myArrowColor.size() == 0 ? arrowColor : myArrowColor;
			this.branchEmpty = branchEmpty;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final FtileGeometry geo = getFtile1().calculateDimension(stringBounder);
			if (geo.hasPointOut() == false)
				return;

			final XPoint2D p1 = geo.translate(translate(stringBounder)).getPointOut();
			final XPoint2D p2 = getP2(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final UPolygon arrow = x2 > x1 ? skinParam().arrows().asToRight() : skinParam().arrows().asToLeft();
			Snake snake = Snake.create(skinParam(), myArrowColor, arrow);
			if (branchEmpty)
				snake = snake.emphasizeDirection(Direction.DOWN);

			snake.addPoint(x1, y1);
			snake.addPoint(x1, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

		private XPoint2D getP2(StringBounder stringBounder) {
			final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final XPoint2D pt;
			if (getFtile1() == tile1)
				pt = dimDiamond2.getPointD();
			else if (getFtile1() == tile2)
				pt = dimDiamond2.getPointB();
			else
				throw new IllegalStateException();

			return getTranslateDiamond2(stringBounder).getTranslated(pt);
		}

		private UTranslate translate(StringBounder stringBounder) {
			if (getFtile1() == tile1)
				return getTranslateBranch1(stringBounder);

			if (getFtile1() == tile2)
				return getTranslateBranch2(stringBounder);

			throw new IllegalStateException();
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			final FtileGeometry geo = getFtile1().calculateDimension(stringBounder);
			if (geo.hasPointOut() == false)
				return;

			final XPoint2D p2 = getP2(stringBounder);
			final XPoint2D p1 = geo.translate(translate(stringBounder)).getPointOut();
			final Direction originalDirection = Direction.leftOrRight(p1, p2);

			final double x1 = p1.getX();
			final double x2 = p2.getX();
			final XPoint2D mp1a = translate1.getTranslated(p1);
			final XPoint2D mp2b = translate2.getTranslated(p2);
			final Direction newDirection = Direction.leftOrRight(mp1a, mp2b);
			final UPolygon arrow = x2 > x1 ? skinParam().arrows().asToRight() : skinParam().arrows().asToLeft();
			if (originalDirection == newDirection) {
				final double delta = (x2 > x1 ? -1 : 1) * 1.5 * Hexagon.hexagonHalfSize;
				final XPoint2D mp2bc = new XPoint2D(mp2b.getX() + delta, mp2b.getY());
				final Snake snake = Snake.create(skinParam(), myArrowColor).withMerge(MergeStrategy.LIMITED);
				final double middle = (mp1a.getY() + mp2b.getY()) / 2.0;
				snake.addPoint(mp1a);
				snake.addPoint(mp1a.getX(), middle);
				snake.addPoint(mp2bc.getX(), middle);
				snake.addPoint(mp2bc);
				ug.draw(snake);
				final Snake small = Snake.create(skinParam(), myArrowColor, arrow).withMerge(MergeStrategy.LIMITED);
				small.addPoint(mp2bc);
				small.addPoint(mp2bc.getX(), mp2b.getY());
				small.addPoint(mp2b);
				ug.draw(small);
			} else {
				final double delta = (x2 > x1 ? -1 : 1) * 1.5 * Hexagon.hexagonHalfSize;
				final XPoint2D mp2bb = new XPoint2D(mp2b.getX() + delta, mp2b.getY() - 1.5 * Hexagon.hexagonHalfSize);
				final Snake snake = Snake.create(skinParam(), myArrowColor).withMerge(MergeStrategy.LIMITED);
				snake.addPoint(mp1a);
				snake.addPoint(mp1a.getX(), mp2bb.getY());
				snake.addPoint(mp2bb);
				ug.draw(snake);
				final Snake small = Snake.create(skinParam(), myArrowColor, arrow).withMerge(MergeStrategy.LIMITED);
				small.addPoint(mp2bb);
				small.addPoint(mp2bb.getX(), mp2b.getY());
				small.addPoint(mp2b);
				ug.draw(small);

			}

		}
	}

	class ConnectionVerticalThenHorizontalDirect extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow myArrowColor;
		private final boolean branchEmpty;

		public ConnectionVerticalThenHorizontalDirect(Ftile tile, Rainbow myArrowColor, boolean branchEmpty) {
			super(tile, diamond2);
			this.myArrowColor = myArrowColor == null || myArrowColor.size() == 0 ? arrowColor : myArrowColor;
			this.branchEmpty = branchEmpty;
		}

		public void drawU(UGraphic ug) {

			final StringBounder stringBounder = ug.getStringBounder();
			final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);

			final FtileGeometry geo = getFtile1().calculateDimension(stringBounder);
			if (geo.hasPointOut() == false)
				return;

			final XPoint2D p1 = geo.translate(translate(stringBounder)).getPointOut();
			final XPoint2D p2 = new XPoint2D(dimTotal.getLeft(), dimTotal.getHeight());

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			Snake snake = Snake.create(skinParam(), myArrowColor);
			if (branchEmpty)
				snake = snake.emphasizeDirection(Direction.DOWN);

			snake.addPoint(x1, y1);
			snake.addPoint(x1, y2);
			snake.addPoint(x2, y2);
			snake.addPoint(x2, dimTotal.getHeight());

			ug.draw(snake);
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);

			final FtileGeometry geo = getFtile1().calculateDimension(stringBounder);
			if (geo.hasPointOut() == false)
				return;

			final XPoint2D p1 = geo.translate(translate(stringBounder)).getPointOut();
			final XPoint2D p2 = new XPoint2D(dimTotal.getLeft(), dimTotal.getHeight() - Hexagon.hexagonHalfSize);

			final XPoint2D mp1a = translate1.getTranslated(p1);
			final XPoint2D mp2b = translate2.getTranslated(p2);

			final Snake snake = Snake.create(skinParam(), myArrowColor).withMerge(MergeStrategy.LIMITED);

			final double x1 = mp1a.getX();
			final double x2 = mp2b.getX();
			final double y2 = mp2b.getY();

			snake.addPoint(mp1a);
			snake.addPoint(x1, y2);
			snake.addPoint(mp2b);
			snake.addPoint(x2, dimTotal.getHeight());

			ug.draw(snake);
		}

		private UTranslate translate(StringBounder stringBounder) {
			if (getFtile1() == tile1)
				return getTranslateBranch1(stringBounder);

			if (getFtile1() == tile2)
				return getTranslateBranch2(stringBounder);

			throw new IllegalStateException();
		}

	}

	// copied from FtileIfLongHorizontal to use with ConditionEndStyle.HLINE
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

			final FtileGeometry geo = getFtile1().calculateDimension(stringBounder);
			if (geo.hasPointOut() == false)
				return;

			final XPoint2D p1 = geo.translate(translate(stringBounder)).getPointOut();

			final double totalHeight = calculateDimensionInternal(stringBounder).getHeight();
			if (p1 == null)
				return;

			final XPoint2D p2 = new XPoint2D(p1.getX(), totalHeight);

			final Snake snake = Snake.create(skinParam(), color, skinParam().arrows().asToDown()).withLabel(out2,
					arrowHorizontalAlignment());
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		private UTranslate translate(StringBounder stringBounder) {
			if (getFtile1() == tile1)
				return getTranslateBranch1(stringBounder);

			if (getFtile1() == tile2)
				return getTranslateBranch2(stringBounder);

			throw new IllegalStateException();
		}
		/*
		 * private XPoint2D getP1(StringBounder stringBounder) {
		 * 
		 * final FtileGeometry geo = getFtile1().calculateDimension(stringBounder); if
		 * (geo.hasPointOut() == false) { return null; } final XPoint2D p =
		 * geo.getPointOut(); return getTranslate1(stringBounder).getTranslated(p); }
		 */
	}

	// copied from FtileIfLongHorizontal to use with ConditionEndStyle.HLINE
	class ConnectionHline extends AbstractConnection {

		private final Rainbow arrowColor;

		public ConnectionHline(Rainbow arrowColor) {
			super(null, null);
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final XDimension2D totalDim = calculateDimensionInternal(stringBounder);

			final List<Ftile> allTiles = new ArrayList<>();
			allTiles.add(tile1);
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
			if (Double.isNaN(minX) || Double.isNaN(maxX))
				return;

			final Snake s = Snake.create(skinParam(), arrowColor).withMerge(MergeStrategy.NONE);
			s.addPoint(minX, totalDim.getHeight());
			s.addPoint(maxX, totalDim.getHeight());
			ug.draw(s);
		}

		private double[] getMinmax(StringBounder stringBounder, double width, List<Ftile> allTiles, Swimlane intoSw,
				List<Swimlane> allSwimlanes) {
			final int current = allSwimlanes.indexOf(intoSw);

			if (current == -1)
				throw new IllegalStateException();

			final int first = getFirstSwimlane(stringBounder, allTiles, allSwimlanes);
			final int last = getLastSwimlane(stringBounder, allTiles, allSwimlanes);
			if (current < first || current > last)
				return new double[] { Double.NaN, Double.NaN };
			double minX = current != first ? 0 : width;
			double maxX = current != last ? width : 0;
			for (Ftile tmp : allTiles) {
				if (tmp.calculateDimension(stringBounder).hasPointOut() == false)
					continue;

				if (ftileDoesOutcomeInThatSwimlane(tmp, intoSw) == false)
					continue;

				final UTranslate ut = getTranslateFor(tmp, stringBounder);
				final double out = tmp.calculateDimension(stringBounder).translate(ut).getLeft();
				minX = Math.min(minX, out);
				maxX = Math.max(maxX, out);
			}
			return new double[] { minX, maxX };
		}

		private double[] getMinmaxSimple(StringBounder stringBounder, double width, List<Ftile> allTiles) {
			double minX = width / 2;
			double maxX = width / 2;
			for (Ftile tmp : allTiles) {
				if (tmp.calculateDimension(stringBounder).hasPointOut() == false)
					continue;

				final UTranslate ut = getTranslateFor(tmp, stringBounder);
				final double out = tmp.calculateDimension(stringBounder).translate(ut).getLeft();
				minX = Math.min(minX, out);
				maxX = Math.max(maxX, out);
			}
			return new double[] { minX, maxX };
		}

		private int getFirstSwimlane(StringBounder stringBounder, List<Ftile> allTiles, List<Swimlane> allSwimlanes) {
			for (int i = 0; i < allSwimlanes.size(); i++)
				if (atLeastOne(stringBounder, allSwimlanes.get(i), allTiles))
					return i;

			throw new IllegalStateException();
		}

		private int getLastSwimlane(StringBounder stringBounder, List<Ftile> allTiles, List<Swimlane> allSwimlanes) {
			for (int i = allSwimlanes.size() - 1; i >= 0; i--)
				if (atLeastOne(stringBounder, allSwimlanes.get(i), allTiles))
					return i;

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

	public Ftile addLinks(Branch branch1, Branch branch2, StringBounder stringBounder) {
		final List<Connection> conns = new ArrayList<>();
		conns.add(new ConnectionHorizontalThenVertical(tile1, branch1));
		conns.add(new ConnectionHorizontalThenVertical(tile2, branch2));
		final boolean hasPointOut1 = tile1.calculateDimension(stringBounder).hasPointOut();
		final boolean hasPointOut2 = tile2.calculateDimension(stringBounder).hasPointOut();
		if (conditionEndStyle == ConditionEndStyle.DIAMOND) {
			if (hasPointOut1 && hasPointOut2) {
				conns.add(new ConnectionVerticalThenHorizontal(tile1, branch1.getOut(), branch1.isEmpty()));
				conns.add(new ConnectionVerticalThenHorizontal(tile2, branch2.getOut(), branch2.isEmpty()));
			} else if (hasPointOut1 && hasPointOut2 == false) {
				conns.add(new ConnectionVerticalThenHorizontalDirect(tile1, branch1.getOut(), branch1.isEmpty()));
			} else if (hasPointOut1 == false && hasPointOut2) {
				conns.add(new ConnectionVerticalThenHorizontalDirect(tile2, branch2.getOut(), branch2.isEmpty()));
			}
		} else if (conditionEndStyle == ConditionEndStyle.HLINE) {
			if (hasPointOut1 && hasPointOut2) {
				conns.add(new ConnectionVerticalOut(tile1, arrowColor, null));
				conns.add(new ConnectionVerticalOut(tile2, arrowColor, null));
				conns.add(new ConnectionHline(arrowColor));
			} else if (hasPointOut1 && hasPointOut2 == false) {
				// this is called when the "else" has a break statement
				conns.add(new ConnectionVerticalThenHorizontalDirect(tile1, branch1.getOut(), branch1.isEmpty()));
			} else if (hasPointOut1 == false && hasPointOut2) {
				// this is called when the "if" has a break statement
				conns.add(new ConnectionVerticalThenHorizontalDirect(tile2, branch2.getOut(), branch2.isEmpty()));
			}
		}
		return FtileUtils.addConnection(this, conns);
	}

}
