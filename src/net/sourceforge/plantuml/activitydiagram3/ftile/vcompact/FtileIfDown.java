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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Hexagon;
import net.sourceforge.plantuml.activitydiagram3.ftile.MergeStrategy;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond.FtileIfWithDiamonds;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UEmpty;
import net.sourceforge.plantuml.svek.ConditionEndStyle;
import net.sourceforge.plantuml.utils.Direction;

public class FtileIfDown extends AbstractFtile {

	private final Ftile thenBlock;
	private final Ftile diamond1;
	private final Ftile diamond2;
	private final Ftile optionalStop;
	private final ConditionEndStyle conditionEndStyle;
	final private TextBlock opale;

	@Override
	public Collection<Ftile> getMyChildren() {
		if (optionalStop == null)
			return Arrays.asList(thenBlock, diamond1, diamond2);

		return Arrays.asList(thenBlock, diamond1, diamond2, optionalStop);
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<>(thenBlock.getSwimlanes());
		result.add(getSwimlaneIn());
		return result;
	}

	public Swimlane getSwimlaneIn() {
		return diamond1.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		if (optionalStop == null)
			return getSwimlaneIn();

		return thenBlock.getSwimlaneOut();
	}

	private FtileIfDown(Ftile thenBlock, Ftile diamond1, Ftile diamond2, Ftile optionalStop,
			ConditionEndStyle conditionEndStyle, Collection<PositionedNote> notes) {
		super(thenBlock.skinParam());
		this.thenBlock = thenBlock;
		this.diamond1 = diamond1;
		this.diamond2 = diamond2;
		this.optionalStop = optionalStop;
		this.conditionEndStyle = conditionEndStyle;
		if (notes.size() == 1) {
			final PositionedNote first = notes.iterator().next();
			this.opale = FtileIfWithDiamonds.createOpale(first, skinParam());
		} else {
			this.opale = TextBlockUtils.EMPTY_TEXT_BLOCK;
		}
	}

	public static Ftile create(Ftile diamond1, Ftile diamond2, Swimlane swimlane, Ftile thenBlock, Rainbow arrowColor,
			ConditionEndStyle conditionEndStyle, FtileFactory ftileFactory, Ftile optionalStop, Rainbow elseColor,
			Collection<PositionedNote> notes) {

		elseColor = elseColor.withDefault(arrowColor);

		final FtileIfDown result = new FtileIfDown(thenBlock, diamond1,
				optionalStop == null ? diamond2 : new FtileEmpty(ftileFactory.skinParam()), optionalStop,
				conditionEndStyle, notes);

		final List<Connection> conns = new ArrayList<>();
		conns.add(result.new ConnectionIn(thenBlock.getInLinkRendering().getRainbow(arrowColor)));
		final boolean hasPointOut1 = thenBlock.calculateDimension(ftileFactory.getStringBounder()).hasPointOut();
		if (optionalStop == null) {
			if (hasPointOut1) {
				if (conditionEndStyle == ConditionEndStyle.DIAMOND) {
					if (swimlane != null && swimlane.isSmallerThanAllOthers(thenBlock.getSwimlanes())) {
						conns.add(result.new ConnectionElse1(elseColor));
						if (diamond1 instanceof FtileDiamondInside)
							((FtileDiamondInside) diamond1).swapEastWest();
					} else {
						conns.add(result.new ConnectionElse2(elseColor));
					}
				} else if (conditionEndStyle == ConditionEndStyle.HLINE) {
					conns.add(result.new ConnectionElseHline(elseColor));
					conns.add(result.new ConnectionHline(elseColor));
				}
			} else {
				conns.add(result.new ConnectionElseNoDiamond(elseColor));
			}
		} else {
			conns.add(result.new ConnectionHorizontal(elseColor));
		}
		conns.add(result.new ConnectionOut(thenBlock.getOutLinkRendering().getRainbow(arrowColor)));
		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionHorizontal extends AbstractConnection {

		private final Rainbow color;

		public ConnectionHorizontal(Rainbow color) {
			super(diamond1, optionalStop);
			this.color = color;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final XPoint2D p1 = getP1(stringBounder);
			final XPoint2D p2 = getP2(stringBounder);

			final Snake snake = Snake.create(skinParam(), color, skinParam().arrows().asToRight());
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		private XPoint2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = getFtile1().calculateDimension(stringBounder);
			final XPoint2D p = new XPoint2D(dimDiamond1.getWidth(), (dimDiamond1.getInY() + dimDiamond1.getOutY()) / 2);

			return getTranslateDiamond1(stringBounder).getTranslated(p);
		}

		private XPoint2D getP2(StringBounder stringBounder) {
			final XDimension2D dimStop = getFtile2().calculateDimension(stringBounder);
			final XPoint2D p = new XPoint2D(0, dimStop.getHeight() / 2);
			return getTranslateOptionalStop(stringBounder).getTranslated(p);
		}

	}

	class ConnectionIn extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;

		public ConnectionIn(Rainbow arrowColor) {
			super(diamond1, thenBlock);
			this.arrowColor = arrowColor;
		}

		private XPoint2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder)
					.getTranslated(getFtile1().calculateDimension(stringBounder).getPointOut());
		}

		private XPoint2D getP2(final StringBounder stringBounder) {
			return getTranslateForThen(stringBounder)
					.getTranslated(getFtile2().calculateDimension(stringBounder).getPointIn());
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown());
			snake.addPoint(getP1(stringBounder));
			snake.addPoint(getP2(stringBounder));

			ug.draw(snake);
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			final XPoint2D p1 = getP1(stringBounder);
			final XPoint2D p2 = getP2(stringBounder);
			final Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown());
			final XPoint2D mp1a = translate1.getTranslated(p1);
			final XPoint2D mp2b = translate2.getTranslated(p2);
			final double middle = (mp1a.getY() + mp2b.getY()) / 2.0;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			snake.addPoint(mp2b);
			ug.draw(snake);
		}
	}

	class ConnectionOut extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;

		public ConnectionOut(Rainbow arrowColor) {
			super(thenBlock, diamond2);
			this.arrowColor = arrowColor;
		}

		private XPoint2D getP1(final StringBounder stringBounder) {
			return getTranslateForThen(stringBounder)
					.getTranslated(getFtile1().calculateDimension(stringBounder).getPointOut());
		}

		private XPoint2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder)
					.getTranslated(getFtile2().calculateDimension(stringBounder).getPointIn());
		}

		private XPoint2D getP2hline(final StringBounder stringBounder) {
			final FtileGeometry dimDiamond2 = getFtile2().calculateDimension(stringBounder);
			final double x = dimDiamond2.getWidth();
			final double half = (dimDiamond2.getOutY() - dimDiamond2.getInY()) / 2;
			return getTranslateDiamond2(stringBounder).getTranslated(new XPoint2D(x, dimDiamond2.getInY() + half));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			if (getFtile1().calculateDimension(ug.getStringBounder()).hasPointOut() == false)
				return;

			final Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown());
			snake.addPoint(getP1(stringBounder));

			if (conditionEndStyle == ConditionEndStyle.DIAMOND)
				snake.addPoint(getP2(stringBounder));
			else if (conditionEndStyle == ConditionEndStyle.HLINE)
				snake.addPoint(getP2hline(stringBounder));

			ug.draw(snake);
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {

			if (getFtile1().calculateDimension(ug.getStringBounder()).hasPointOut() == false)
				return;

			final StringBounder stringBounder = ug.getStringBounder();
			final XPoint2D p1 = getP1(stringBounder);
			final XPoint2D p2 = getP2(stringBounder);
			final Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown());
			final XPoint2D mp1a = translate1.getTranslated(p1);
			final XPoint2D mp2b = translate2.getTranslated(p2);
			final double middle = (mp1a.getY() + mp2b.getY()) / 2.0;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			snake.addPoint(mp2b);
			ug.draw(snake);
		}
	}

	class ConnectionElse1 extends AbstractConnection {
		private final Rainbow endInlinkColor;

		public ConnectionElse1(Rainbow endInlinkColor) {
			super(diamond1, diamond2);
			this.endInlinkColor = endInlinkColor;
		}

		protected XPoint2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final double x = 0;
			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
			return getTranslateDiamond1(stringBounder).getTranslated(new XPoint2D(x, dimDiamond1.getInY() + half));
		}

		protected XPoint2D getP2(final StringBounder stringBounder) {
			final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x = 0;
			final double half = (dimDiamond2.getOutY() - dimDiamond2.getInY()) / 2;
			return getTranslateDiamond2(stringBounder).getTranslated(new XPoint2D(x, dimDiamond2.getInY() + half));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final XPoint2D p1 = getP1(stringBounder);
			if (calculateDimension(stringBounder).hasPointOut() == false)
				return;

			final XPoint2D p2 = getP2(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final double t11 = getTranslateForThen(stringBounder).getDx();
			final double xmin = Math.min(x1 - Hexagon.hexagonHalfSize, getTranslateForThen(stringBounder).getDx());

			final Snake snake = Snake.create(skinParam(), endInlinkColor, skinParam().arrows().asToRight())
					.emphasizeDirection(Direction.DOWN);
			snake.addPoint(x1, y1);
			snake.addPoint(xmin, y1);
			snake.addPoint(xmin, y2);
			snake.addPoint(x2, y2);
			ug.apply(new UTranslate(x2, y2 - Hexagon.hexagonHalfSize)).draw(new UEmpty(5, Hexagon.hexagonHalfSize));
			ug.draw(snake);

		}

	}

	class ConnectionElse2 extends AbstractConnection {
		private final Rainbow endInlinkColor;

		public ConnectionElse2(Rainbow endInlinkColor) {
			super(diamond1, diamond2);
			this.endInlinkColor = endInlinkColor;
		}

		protected XPoint2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final double x = dimDiamond1.getWidth();
			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
			return getTranslateDiamond1(stringBounder).getTranslated(new XPoint2D(x, dimDiamond1.getInY() + half));
		}

		protected XPoint2D getP2(final StringBounder stringBounder) {
			final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x = dimDiamond2.getWidth();
			final double half = (dimDiamond2.getOutY() - dimDiamond2.getInY()) / 2;
			return getTranslateDiamond2(stringBounder).getTranslated(new XPoint2D(x, dimDiamond2.getInY() + half));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final XPoint2D p1 = getP1(stringBounder);
			if (calculateDimension(stringBounder).hasPointOut() == false)
				return;

			final XPoint2D p2 = getP2(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final FtileGeometry thenGeom = thenBlock.calculateDimension(stringBounder);
			final double xmax = Math.max(x1 + Hexagon.hexagonHalfSize,
					getTranslateForThen(stringBounder).getDx() + thenGeom.getWidth());

			final Snake snake = Snake.create(skinParam(), endInlinkColor, skinParam().arrows().asToLeft())
					.emphasizeDirection(Direction.DOWN);
			snake.addPoint(x1, y1);
			snake.addPoint(xmax, y1);
			snake.addPoint(xmax, y2);
			snake.addPoint(x2, y2);
			ug.apply(new UTranslate(x2, y2 - Hexagon.hexagonHalfSize)).draw(new UEmpty(5, Hexagon.hexagonHalfSize));
			ug.draw(snake);

		}

	}

	class ConnectionElseHline extends ConnectionElse2 {
		private final Rainbow endInlinkColor;

		public ConnectionElseHline(Rainbow endInlinkColor) {
			super(endInlinkColor);
			this.endInlinkColor = endInlinkColor;
		}

		@Override
		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final XPoint2D p1 = getP1(stringBounder);
			if (calculateDimension(stringBounder).hasPointOut() == false)
				return;

			final XPoint2D p2 = getP2(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final FtileGeometry thenGeom = thenBlock.calculateDimension(stringBounder);
			final double xmax = Math.max(x1 + Hexagon.hexagonHalfSize,
					getTranslateForThen(stringBounder).getDx() + thenGeom.getWidth());

			final Snake snake = Snake.create(skinParam(), endInlinkColor, skinParam().arrows().asToDown());
			snake.addPoint(x1, y1);
			snake.addPoint(xmax, y1);
			snake.addPoint(xmax, y2);
			ug.apply(new UTranslate(xmax, y2 - Hexagon.hexagonHalfSize)).draw(new UEmpty(5, Hexagon.hexagonHalfSize));
			ug.draw(snake);

		}

	}

	class ConnectionElseNoDiamond extends ConnectionElse2 {

		public ConnectionElseNoDiamond(Rainbow endInlinkColor) {
			super(endInlinkColor);
		}

		@Override
		protected XPoint2D getP2(final StringBounder stringBounder) {
			return calculateDimension(stringBounder).getPointOut();
		}

	}

	// copied from FtileIfLongHorizontal to use with ConditionEndStyle.HLINE
	class ConnectionHline extends AbstractConnection {
		private final Rainbow endInlinkColor;

		public ConnectionHline(Rainbow endInlinkColor) {
			super(diamond1, diamond2);
			this.endInlinkColor = endInlinkColor;
		}

		private XPoint2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final double x = dimDiamond1.getWidth();
			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
			return getTranslateDiamond1(stringBounder).getTranslated(new XPoint2D(x, dimDiamond1.getInY() + half));
		}

		protected XPoint2D getP2(final StringBounder stringBounder) {
			final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x = dimDiamond2.getWidth();
			final double half = (dimDiamond2.getOutY() - dimDiamond2.getInY()) / 2;
			return getTranslateDiamond2(stringBounder).getTranslated(new XPoint2D(x, dimDiamond2.getInY() + half));
		}

		// the bottom or south point of the diamond that we omitted
		protected XPoint2D getP3(final StringBounder stringBounder) {
			final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x = dimDiamond2.getWidth();
			return getTranslateDiamond2(stringBounder).getTranslated(new XPoint2D(x, dimDiamond2.getOutY()));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final XPoint2D p1 = getP1(stringBounder);
			if (calculateDimension(stringBounder).hasPointOut() == false)
				return;

			final XPoint2D p2 = getP2(stringBounder);
			final XPoint2D p3 = getP3(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final double x3 = p3.getX();
			final double y3 = p3.getY();

			final FtileGeometry thenGeom = thenBlock.calculateDimension(stringBounder);
			final double xmax = Math.max(x1 + Hexagon.hexagonHalfSize,
					getTranslateForThen(stringBounder).getDx() + thenGeom.getWidth());

			final Snake snake = Snake.create(skinParam(), endInlinkColor).withMerge(MergeStrategy.NONE);
			snake.addPoint(xmax, y2);
			// ug.apply(new UTranslate(xmax, y2 - Diamond.diamondHalfSize)).draw(new
			// UEmpty(5,
			// Diamond.diamondHalfSize));
			snake.addPoint(x2, y2);
			snake.addPoint(x3, y3);
			ug.draw(snake);

		}
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		if (TextBlockUtils.isEmpty(opale, stringBounder) == false) {
			final double xOpale = getTranslateDiamond1(stringBounder).getDx()
					- opale.calculateDimension(stringBounder).getWidth();
			opale.drawU(ug.apply(UTranslate.dx(xOpale)));
		}

		ug.apply(getTranslateForThen(stringBounder)).draw(thenBlock);
		ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
		if (optionalStop == null)
			ug.apply(getTranslateDiamond2(stringBounder)).draw(diamond2);
		else
			ug.apply(getTranslateOptionalStop(stringBounder)).draw(optionalStop);

	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {

		final XDimension2D dimOpale = opale.calculateDimension(stringBounder);

		final FtileGeometry geoDiamond1 = diamond1.calculateDimension(stringBounder);
		final FtileGeometry geoThen = thenBlock.calculateDimension(stringBounder);
		final FtileGeometry geoDiamond2 = diamond2.calculateDimension(stringBounder);
		final FtileGeometry geo = geoDiamond1.appendBottom(geoThen).appendBottom(geoDiamond2);

		final double opaleWidth = dimOpale.getWidth();
		final double opaleHeight = dimOpale.getHeight();

		final double height = geo.getHeight() + 3 * Hexagon.hexagonHalfSize
				+ Math.max(Hexagon.hexagonHalfSize * 1, getSouthLabelHeight(stringBounder)) + opaleHeight;

		double supp = 0;
		if (opaleWidth > geo.getLeft())
			supp = opaleWidth - geo.getLeft();

		double width = supp + geo.getWidth() + Hexagon.hexagonHalfSize;
		if (optionalStop != null)
			width += optionalStop.calculateDimension(stringBounder).getWidth() + getAdditionalWidth(stringBounder);

		final FtileGeometry result;
		if (supp > 0)
			result = new FtileGeometry(width, height, opaleWidth + geoDiamond1.getLeft(),
					geoDiamond1.getInY() + opaleHeight, height);
		else
			result = new FtileGeometry(width, height, geo.getLeft(), geoDiamond1.getInY() + opaleHeight, height);

		if (geoThen.hasPointOut() == false && optionalStop != null)
			return result.withoutPointOut();

		return result;

	}

	private double getAdditionalWidth(StringBounder stringBounder) {
		final FtileGeometry dimStop = optionalStop.calculateDimension(stringBounder);
		final double val1 = getEastLabelWidth(stringBounder);
		final double stopWidth = dimStop.getWidth();
		return Math.max(stopWidth, val1 + stopWidth / 2);
	}

	private double getSouthLabelHeight(StringBounder stringBounder) {
		if (diamond1 instanceof FtileDiamondInside)
			return ((FtileDiamondInside) diamond1).getSouthLabelHeight(stringBounder);

		if (diamond1 instanceof FtileDiamond)
			return ((FtileDiamond) diamond1).getSouthLabelHeight(stringBounder);

		return 0;
	}

	private double getEastLabelWidth(StringBounder stringBounder) {
		if (diamond1 instanceof FtileDiamondInside)
			return ((FtileDiamondInside) diamond1).getEastLabelWidth(stringBounder);

		if (diamond1 instanceof FtileDiamond)
			return ((FtileDiamond) diamond1).getEastLabelWidth(stringBounder);

		return 0;
	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == thenBlock)
			return getTranslateForThen(stringBounder);

		if (child == diamond1)
			return getTranslateDiamond1(stringBounder);

		if (child == optionalStop)
			return getTranslateOptionalStop(stringBounder);

		if (child == diamond2)
			return getTranslateDiamond2(stringBounder);

		throw new UnsupportedOperationException();
	}

	private UTranslate getTranslateForThen(StringBounder stringBounder) {
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);

		final FtileGeometry dimTotal = calculateDimension(stringBounder);
		final FtileGeometry dimThen = thenBlock.calculateDimension(stringBounder);

		final double opaleHeight = opale.calculateDimension(stringBounder).getHeight();
		final double y = opaleHeight + dimDiamond1.getHeight() + (dimTotal.getHeight() - opaleHeight
				- dimDiamond1.getHeight() - dimDiamond2.getHeight() - dimThen.getHeight()) / 2;

		final double x = dimTotal.getLeft() - dimThen.getLeft();
		return new UTranslate(x, y);

	}

	private UTranslate getTranslateDiamond1(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimension(stringBounder);
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final double y1 = opale.calculateDimension(stringBounder).getHeight();
		final double x1 = dimTotal.getLeft() - dimDiamond1.getLeft();
		return new UTranslate(x1, y1);
	}

	private UTranslate getTranslateOptionalStop(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimension(stringBounder);
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final FtileGeometry dimStop = optionalStop.calculateDimension(stringBounder);
		final double labelNorth = dimDiamond1.getInY();
		final double y1 = labelNorth + (dimDiamond1.getHeight() - labelNorth - dimStop.getHeight()) / 2;
		final double x1 = dimTotal.getLeft() - dimDiamond1.getLeft() + dimDiamond1.getWidth()
				+ getAdditionalWidth(stringBounder);
		return new UTranslate(x1, y1);
	}

	private UTranslate getTranslateDiamond2(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimension(stringBounder);
		final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final double y2 = dimTotal.getHeight() - dimDiamond2.getHeight();
		final double x2 = dimTotal.getLeft() - dimDiamond2.getLeft();
		return new UTranslate(x2, y2);
	}

}
