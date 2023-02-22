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
package net.sourceforge.plantuml.activitydiagram3.gtile;

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.activitydiagram3.Instruction;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class GtileWhile extends GtileTopDown3 {

	public GtileWhile(Gtile tile1, Gtile tile2, Gtile tile3) {
		super(tile1, tile2, tile3);
	}

	private static StyleSignatureBasic getDefaultStyleDefinitionDiamond() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	private static Gtile getShape1(Gtile toto, Swimlane swimlane, Display test, Display yes) {
		GtileHexagonInside tmp0 = Gtiles.hexagonInside(swimlane, toto.getStringBounder(), toto.skinParam(),
				getDefaultStyleDefinitionDiamond(), HColors.BLUE, test);

//		final TextBlock tmp0 = branches.get(0).getTextBlockPositive();
//		return Gtiles.withSouthMargin(tmp.withSouthLabel(tmp0), 10);
		return Gtiles.withSouthMargin(tmp0, 20);
	}

	public static Gtile createWhile(Swimlane swimlane, Gtile tmp, Display test, Display yes, Instruction specialOut,
			Gtile back) {
		final Gtile shape1 = getShape1(tmp, swimlane, test, yes);
		final Gtile tile3 = new GtileEmpty(tmp.getStringBounder(), tmp.skinParam(), 10, 20, swimlane);
		return new GtileWhile(shape1, tmp, tile3);
	}

	@Override
	public Collection<GConnection> getInnerConnections() {
		final GConnection arrow1 = new GConnectionVerticalDown(getPos1(), tile1.getGPoint(GPoint.SOUTH_HOOK), getPos2(),
				tile2.getGPoint(GPoint.NORTH_HOOK), TextBlockUtils.EMPTY_TEXT_BLOCK);

		final double xright = calculateDimension(stringBounder).getWidth();

		final GConnection arrow2 = new GConnectionVerticalDownThenBack(getPos2(), tile2.getGPoint(GPoint.SOUTH_HOOK),
				getPos1(), tile1.getGPoint(GPoint.EAST_HOOK), TextBlockUtils.EMPTY_TEXT_BLOCK, xright);

		final double xleft = 0;

		final GConnection arrow3 = new GConnectionSideThenVerticalThenSide(getPos1(), tile1.getGPoint(GPoint.WEST_HOOK),
				getPos3(), tile3.getGPoint(GPoint.SOUTH_HOOK), xleft, TextBlockUtils.EMPTY_TEXT_BLOCK);

		return Arrays.asList(arrow1, arrow2, arrow3);
	}

//	private final Ftile whileBlock;
//	private final Ftile diamond1;
//	private final Ftile specialOut;
//	private final Ftile backward;
//
//	@Override
//	public Collection<Ftile> getMyChildren() {
//		if (specialOut == null) {
//			return Arrays.asList(whileBlock, diamond1);
//		}
//		return Arrays.asList(whileBlock, diamond1, specialOut);
//	}
//
//	public Set<Swimlane> getSwimlanes() {
//		final Set<Swimlane> result = new HashSet<>(whileBlock.getSwimlanes());
//		result.add(getSwimlaneIn());
//		return result;
//	}
//
//	public Swimlane getSwimlaneIn() {
//		return diamond1.getSwimlaneIn();
//	}
//
//	public Swimlane getSwimlaneOut() {
//		return getSwimlaneIn();
//	}
//
//	private GtileWhile(Ftile whileBlock, Ftile diamond1, Ftile specialOut, Ftile backward) {
//		super(whileBlock.skinParam());
//		this.whileBlock = whileBlock;
//		this.diamond1 = diamond1;
//		this.specialOut = specialOut;
//		this.backward = backward;
//	}
//
//	public static Ftile create(LinkRendering outColor, Swimlane swimlane, Ftile whileBlock, Display test,
//			HColor borderColor, HColor backColor, Rainbow arrowColor, Display yes, FontConfiguration fontArrow,
//			FtileFactory ftileFactory, ConditionStyle conditionStyle, FontConfiguration fcTest, Instruction specialOut,
//			Ftile backward, LinkRendering incoming1, LinkRendering incoming2) {
//
//		final TextBlock yesTb = yes.create(fontArrow, HorizontalAlignment.LEFT, ftileFactory.skinParam());
//		final TextBlock testTb = test.isWhite() ? TextBlockUtils.empty(0, 0)
//				: test.create(fcTest, whileBlock.skinParam().getDefaultTextAlignment(HorizontalAlignment.LEFT),
//						ftileFactory.skinParam());
//		final TextBlock outTb = outColor.getDisplay().create(fontArrow, HorizontalAlignment.LEFT,
//				ftileFactory.skinParam());
//
//		final Ftile diamond1;
//		if (conditionStyle == ConditionStyle.INSIDE_HEXAGON) {
//			diamond1 = new FtileDiamondInside(testTb, whileBlock.skinParam(), backColor, borderColor, swimlane)
//					.withNorth(yesTb).withWest(outTb);
//		} else if (conditionStyle == ConditionStyle.INSIDE_DIAMOND) {
//			diamond1 = new FtileDiamondSquare(testTb, whileBlock.skinParam(), backColor, borderColor, swimlane)
//					.withNorth(yesTb).withWest(outTb);
//		} else if (conditionStyle == ConditionStyle.EMPTY_DIAMOND) {
//			diamond1 = new FtileDiamond(whileBlock.skinParam(), backColor, borderColor, swimlane).withNorth(testTb)
//					.withSouth(yesTb).withWest(outTb);
//		} else {
//			throw new IllegalStateException();
//		}
//
//		final Ftile special = specialOut == null ? null : specialOut.createFtile(ftileFactory);
//		final GtileWhile result = new GtileWhile(whileBlock, diamond1, special, backward);
//
//		final Dimension2D dim = whileBlock.calculateDimension(ftileFactory.getStringBounder());
//		final TextBlock back1 = incoming1.getDisplay().create(fontArrow, HorizontalAlignment.LEFT,
//				ftileFactory.skinParam());
//
//		final List<Connection> conns = new ArrayList<>();
//		if (dim.getWidth() == 0 || dim.getHeight() == 0) {
//			conns.add(result.new ConnectionBackEmpty(incoming1.getRainbow()));
//		} else {
//			conns.add(result.new ConnectionIn(whileBlock.getInLinkRendering().getRainbow(arrowColor)));
//			if (backward == null) {
//				conns.add(result.new ConnectionBackSimple(incoming1.getRainbow(), back1));
//			} else {
//				final TextBlock back2 = incoming2.getDisplay().create(fontArrow, HorizontalAlignment.LEFT,
//						ftileFactory.skinParam());
//				conns.add(result.new ConnectionBackBackward1(incoming1.getRainbow(), back1));
//				conns.add(result.new ConnectionBackBackward2(incoming2.getRainbow(), back2));
//			}
//		}
//		if (specialOut == null) {
//			conns.add(result.new ConnectionOut(outColor.getRainbow()));
//		} else {
//			conns.add(result.new ConnectionOutSpecial(outColor.getRainbow()));
//		}
//		return FtileUtils.addConnection(result, conns);
//	}
//
//	class ConnectionIn extends AbstractConnection implements ConnectionTranslatable {
//		private final Rainbow arrowColor;
//
//		public ConnectionIn(Rainbow arrowColor) {
//			super(diamond1, whileBlock);
//			this.arrowColor = arrowColor;
//		}
//
//		private XPoint2D getP1(final StringBounder stringBounder) {
//			return getTranslateDiamond1(stringBounder)
//					.getTranslated(getFtile1().calculateDimension(stringBounder).getPointOut());
//		}
//
//		private XPoint2D getP2(final StringBounder stringBounder) {
//			return getTranslateForWhile(stringBounder)
//					.getTranslated(getFtile2().calculateDimension(stringBounder).getPointIn());
//		}
//
//		public void drawU(UGraphic ug) {
//			final StringBounder stringBounder = ug.getStringBounder();
//
//			final Snake snake = Snake.create(arrowColor, Arrows.asToDown());
//			snake.addPoint(getP1(stringBounder));
//			snake.addPoint(getP2(stringBounder));
//
//			ug.draw(snake);
//		}
//
//		@Override
//		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
//			final StringBounder stringBounder = ug.getStringBounder();
//			final XPoint2D p1 = getP1(stringBounder);
//			final XPoint2D p2 = getP2(stringBounder);
//			final Snake snake = Snake.create(arrowColor, Arrows.asToDown()).withMerge(MergeStrategy.LIMITED);
//			final XPoint2D mp1a = translate1.getTranslated(p1);
//			final XPoint2D mp2b = translate2.getTranslated(p2);
//			final double middle = (mp1a.getY() + mp2b.getY()) / 2.0;
//			snake.addPoint(mp1a);
//			snake.addPoint(mp1a.getX(), middle);
//			snake.addPoint(mp2b.getX(), middle);
//			snake.addPoint(mp2b);
//			ug.draw(snake);
//		}
//	}
//
//	class ConnectionBackSimple extends AbstractConnection implements ConnectionTranslatable {
//		private final Rainbow endInlinkColor;
//		private final TextBlock back;
//
//		public ConnectionBackSimple(Rainbow endInlinkColor, TextBlock back) {
//			super(whileBlock, diamond1);
//			this.endInlinkColor = endInlinkColor;
//			this.back = back;
//		}
//
//		private XPoint2D getP1(final StringBounder stringBounder) {
//			final FtileGeometry geo = whileBlock.calculateDimension(stringBounder);
//			if (geo.hasPointOut() == false) {
//				return null;
//			}
//			return getTranslateForWhile(stringBounder).getTranslated(geo.getPointOut());
//		}
//
//		private double getBottom(final StringBounder stringBounder) {
//			final FtileGeometry geo = whileBlock.calculateDimension(stringBounder);
//			return getTranslateForWhile(stringBounder).getDy() + geo.getHeight();
//		}
//
//		private XPoint2D getP2(final StringBounder stringBounder) {
//			return getTranslateDiamond1(stringBounder).getTranslated(new XPoint2D(0, 0));
//		}
//
//		public void drawU(UGraphic ug) {
//			final StringBounder stringBounder = ug.getStringBounder();
//
//			final Dimension2D dimTotal = calculateDimension(stringBounder);
//			final XPoint2D p1 = getP1(stringBounder);
//			if (p1 == null) {
//				return;
//			}
//			final XPoint2D p2 = getP2(stringBounder);
//			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
//
//			final double x1 = p1.getX();
//			final double y1 = p1.getY();
//			final double x2 = p2.getX() + dimDiamond1.getWidth();
//			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
//			final double y2 = p2.getY() + dimDiamond1.getInY() + half;
//
//			final Snake snake = Snake.create(endInlinkColor, Arrows.asToLeft()).emphasizeDirection(Direction.UP)
//					.withLabel(back, arrowHorizontalAlignment());
//			snake.addPoint(x1, y1);
//			final double y1bis = Math.max(y1, getBottom(stringBounder)) + Hexagon.hexagonHalfSize;
//			snake.addPoint(x1, y1bis);
//			final double xx = dimTotal.getWidth();
//			snake.addPoint(xx, y1bis);
//			snake.addPoint(xx, y2);
//			snake.addPoint(x2, y2);
//
//			ug.draw(snake);
//			ug.apply(new UTranslate(x1, y1bis)).draw(new UEmpty(5, Hexagon.hexagonHalfSize));
//
//		}
//
//		@Override
//		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
//			final StringBounder stringBounder = ug.getStringBounder();
//			final Snake snake = Snake.create(endInlinkColor, Arrows.asToLeft()).withMerge(MergeStrategy.LIMITED);
//			final Dimension2D dimTotal = calculateDimension(stringBounder);
//			final XPoint2D ap1 = getP1(stringBounder);
//			final XPoint2D ap2 = getP2(stringBounder);
//			final XPoint2D p1 = translate1.getTranslated(ap1);
//			final XPoint2D p2 = translate2.getTranslated(ap2);
//
//			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
//
//			final double x1 = p1.getX();
//			final double y1 = p1.getY();
//			final double x2 = p2.getX() + dimDiamond1.getWidth();
//			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
//			final double y2 = p2.getY() + dimDiamond1.getInY() + half;
//
//			snake.addPoint(x1, y1);
//			snake.addPoint(x1, y1 + Hexagon.hexagonHalfSize);
//			final double xx = Math.max(translate1.getDx(), translate2.getDx()) + dimTotal.getWidth();
//			snake.addPoint(xx, y1 + Hexagon.hexagonHalfSize);
//			snake.addPoint(xx, y2);
//			snake.addPoint(x2, y2);
//
//			ug.draw(snake);
//
//			ug.apply(new UTranslate(x1, y1 + Hexagon.hexagonHalfSize)).draw(new UEmpty(5, Hexagon.hexagonHalfSize));
//
//			ug = ug.apply(endInlinkColor.getColor()).apply(endInlinkColor.getColor().bg());
//			ug.apply(new UTranslate(xx, (y1 + y2) / 2)).draw(Arrows.asToUp());
//
//		}
//
//	}
//
//	class ConnectionBackBackward1 extends AbstractConnection {
//		private final Rainbow endInlinkColor;
//		private final TextBlock back;
//
//		public ConnectionBackBackward1(Rainbow endInlinkColor, TextBlock back) {
//			super(whileBlock, backward);
//			this.endInlinkColor = endInlinkColor;
//			this.back = back;
//		}
//
//		private XPoint2D getP1(final StringBounder stringBounder) {
//			final FtileGeometry geo = whileBlock.calculateDimension(stringBounder);
//			if (geo.hasPointOut() == false) {
//				return null;
//			}
//			return getTranslateForWhile(stringBounder).getTranslated(geo.getPointOut());
//		}
//
//		private double getBottom(final StringBounder stringBounder) {
//			final FtileGeometry geo = whileBlock.calculateDimension(stringBounder);
//			return getTranslateForWhile(stringBounder).getDy() + geo.getHeight();
//		}
//
//		private XPoint2D getP2(final StringBounder stringBounder) {
//			final FtileGeometry dim = backward.calculateDimension(stringBounder);
//			return getTranslateBackward(stringBounder).getTranslated(new XPoint2D(dim.getLeft(), dim.getOutY()));
//		}
//
//		public void drawU(UGraphic ug) {
//			final StringBounder stringBounder = ug.getStringBounder();
//
//			final XPoint2D p1 = getP1(stringBounder);
//			if (p1 == null) {
//				return;
//			}
//			final XPoint2D p2 = getP2(stringBounder);
//			final double x1 = p1.getX();
//			final double y1 = p1.getY();
//			final double x2 = p2.getX();
//			final double y2 = p2.getY();
//
//			final Snake snake = Snake.create(endInlinkColor, Arrows.asToUp()).withLabel(back,
//					arrowHorizontalAlignment());
//			snake.addPoint(x1, y1);
//			final double y1bis = Math.max(y1, getBottom(stringBounder)) + Hexagon.hexagonHalfSize;
//			snake.addPoint(x1, y1bis);
//			snake.addPoint(x2, y1bis);
//			snake.addPoint(x2, y2);
//
//			ug.draw(snake);
//			ug.apply(new UTranslate(x1, y1bis)).draw(new UEmpty(5, Hexagon.hexagonHalfSize));
//		}
//	}
//
//	class ConnectionBackBackward2 extends AbstractConnection {
//		private final Rainbow endInlinkColor;
//		private final TextBlock back;
//
//		public ConnectionBackBackward2(Rainbow endInlinkColor, TextBlock back) {
//			super(backward, diamond1);
//			this.endInlinkColor = endInlinkColor;
//			this.back = back;
//		}
//
//		private XPoint2D getP1(final StringBounder stringBounder) {
//			final FtileGeometry dim = backward.calculateDimension(stringBounder);
//			return getTranslateBackward(stringBounder).getTranslated(new XPoint2D(dim.getLeft(), dim.getInY()));
//		}
//
//		private XPoint2D getP2(final StringBounder stringBounder) {
//			return getTranslateDiamond1(stringBounder).getTranslated(new XPoint2D(0, 0));
//		}
//
//		public void drawU(UGraphic ug) {
//			final StringBounder stringBounder = ug.getStringBounder();
//
//			final Snake snake = Snake.create(endInlinkColor, Arrows.asToLeft()).withLabel(back,
//					arrowHorizontalAlignment());
//
//			final XPoint2D p1 = getP1(stringBounder);
//			final XPoint2D p2 = getP2(stringBounder);
//			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
//
//			final double x1 = p1.getX();
//			final double y1 = p1.getY();
//			final double x2 = p2.getX() + dimDiamond1.getWidth();
//			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
//			final double y2 = p2.getY() + dimDiamond1.getInY() + half;
//
//			snake.addPoint(x1, y1);
//			snake.addPoint(x1, y2);
//			snake.addPoint(x2, y2);
//
//			ug.draw(snake);
//		}
//	}
//
//	class ConnectionBackEmpty extends AbstractConnection {
//		private final Rainbow endInlinkColor;
//
//		public ConnectionBackEmpty(Rainbow endInlinkColor) {
//			super(diamond1, diamond1);
//			this.endInlinkColor = endInlinkColor;
//		}
//
//		private XPoint2D getP1(final StringBounder stringBounder) {
//			return getTranslateDiamond1(stringBounder)
//					.getTranslated(diamond1.calculateDimension(stringBounder).getPointOut());
//		}
//
//		private double getBottom(final StringBounder stringBounder) {
//			final FtileGeometry geo = whileBlock.calculateDimension(stringBounder);
//			return getTranslateForWhile(stringBounder).getDy() + geo.getHeight();
//		}
//
//		private XPoint2D getP2(final StringBounder stringBounder) {
//			return getTranslateDiamond1(stringBounder).getTranslated(new XPoint2D(0, 0));
//		}
//
//		public void drawU(UGraphic ug) {
//			final StringBounder stringBounder = ug.getStringBounder();
//
//			final Snake snake = Snake.create(endInlinkColor, Arrows.asToLeft()).emphasizeDirection(Direction.UP);
//			final Dimension2D dimTotal = calculateDimension(stringBounder);
//			final XPoint2D p1 = getP1(stringBounder);
//			final XPoint2D p2 = getP2(stringBounder);
//			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
//
//			final double x1 = p1.getX();
//			final double y1 = p1.getY();
//			final double x2 = p2.getX() + dimDiamond1.getWidth();
//			// final double y2 = p2.getY() + dimDiamond1.getOutY() / 2;
//			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
//			final double y2 = p2.getY() + dimDiamond1.getInY() + half;
//
//			snake.addPoint(x1, y1);
//			final double y1bis = Math.max(y1, getBottom(stringBounder)) + Hexagon.hexagonHalfSize;
//			snake.addPoint(x1, y1bis);
//			final double xx = dimTotal.getWidth();
//			snake.addPoint(xx, y1bis);
//			snake.addPoint(xx, y2);
//			snake.addPoint(x2, y2);
//
//			ug.draw(snake);
//
//			ug.apply(new UTranslate(x1, y1bis)).draw(new UEmpty(5, Hexagon.hexagonHalfSize));
//
//		}
//
//	}
//
//	class ConnectionOut extends AbstractConnection {
//		private final Rainbow afterEndwhileColor;
//
//		public ConnectionOut(Rainbow afterEndwhileColor) {
//			super(diamond1, null);
//			this.afterEndwhileColor = afterEndwhileColor;
//		}
//
//		private XPoint2D getP1(final StringBounder stringBounder) {
//			return getTranslateDiamond1(stringBounder).getTranslated(new XPoint2D(0, 0));
//		}
//
//		private XPoint2D getP2(final StringBounder stringBounder) {
//			final FtileGeometry dimTotal = calculateDimension(stringBounder);
//			return new XPoint2D(dimTotal.getLeft(), dimTotal.getHeight());
//		}
//
//		public void drawU(UGraphic ug) {
//			final StringBounder stringBounder = ug.getStringBounder();
//
//			final Snake snake = Snake.create(afterEndwhileColor).withMerge(MergeStrategy.LIMITED)
//					.emphasizeDirection(Direction.DOWN);
//
//			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
//			final XPoint2D p1 = getP1(stringBounder);
//			final XPoint2D p2 = getP2(stringBounder);
//
//			final double x1 = p1.getX();
//			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
//			final double y1 = p1.getY() + dimDiamond1.getInY() + half;
//			final double x2 = p2.getX();
//			final double y2 = p2.getY();
//
//			snake.addPoint(x1, y1);
//			snake.addPoint(Hexagon.hexagonHalfSize, y1);
//			snake.addPoint(Hexagon.hexagonHalfSize, y2);
//
//			ug.draw(snake);
//
//			final Snake snake2 = Snake.create(afterEndwhileColor);
//			snake2.addPoint(Hexagon.hexagonHalfSize, y2);
//			snake2.addPoint(x2, y2);
//			// snake2.goUnmergeable(MergeStrategy.LIMITED);
//			ug.draw(snake2);
//
//		}
//	}
//
//	class ConnectionOutSpecial extends AbstractConnection {
//		private final Rainbow afterEndwhileColor;
//
//		public ConnectionOutSpecial(Rainbow afterEndwhileColor) {
//			super(diamond1, specialOut);
//			this.afterEndwhileColor = afterEndwhileColor;
//		}
//
//		private XPoint2D getP1(final StringBounder stringBounder) {
//			return getTranslateDiamond1(stringBounder).getTranslated(new XPoint2D(0, 0));
//		}
//
//		private XPoint2D getP2(final StringBounder stringBounder) {
//			return getTranslateForSpecial(stringBounder)
//					.getTranslated(specialOut.calculateDimension(stringBounder).getPointIn());
//		}
//
//		public void drawU(UGraphic ug) {
//			final StringBounder stringBounder = ug.getStringBounder();
//
//			final Snake snake = Snake.create(afterEndwhileColor, Arrows.asToDown());
//
//			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
//			final XPoint2D p1 = getP1(stringBounder);
//			final XPoint2D p2 = getP2(stringBounder);
//
//			final double x1 = p1.getX();
//			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
//			final double y1 = p1.getY() + dimDiamond1.getInY() + half;
//			final double x2 = p2.getX();
//			final double y2 = p2.getY();
//
//			snake.addPoint(x1, y1);
//			snake.addPoint(x2, y1);
//			snake.addPoint(x2, y2);
//
//			ug.draw(snake);
//
//		}
//	}
//
//	public void drawU(UGraphic ug) {
//		final StringBounder stringBounder = ug.getStringBounder();
//		ug.apply(getTranslateForWhile(stringBounder)).draw(whileBlock);
//		ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
//		if (specialOut != null) {
//			ug.apply(getTranslateForSpecial(stringBounder)).draw(specialOut);
//		}
//		if (backward != null) {
//			ug.apply(getTranslateBackward(stringBounder)).draw(backward);
//		}
//	}
//
//	private UTranslate getTranslateBackward(StringBounder stringBounder) {
//		final Dimension2D dimTotal = calculateDimensionFtile(stringBounder);
//		final Dimension2D dimBackward = backward.calculateDimension(stringBounder);
//		final double x = dimTotal.getWidth() - dimBackward.getWidth();
//		final double y = (dimTotal.getHeight() - dimBackward.getHeight()) / 2;
//
//		return new UTranslate(x, y);
//	}
//
//	@Override
//	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
//		final FtileGeometry geoDiamond1 = diamond1.calculateDimension(stringBounder);
//		FtileGeometry geoWhile = whileBlock.calculateDimension(stringBounder);
//		final double diff = -geoWhile.getWidth();
//		if (diff > 0) {
//			geoWhile = geoWhile.addMarginX(diff / 2);
//			assert false;
//		}
//		final FtileGeometry geo = geoDiamond1.appendBottom(geoWhile);
//		final double height = geo.getHeight() + 4 * Hexagon.hexagonHalfSize;
//		final double dx = 2 * Hexagon.hexagonHalfSize;
//		double backwardWidth = 0;
//		if (backward != null) {
//			backwardWidth += backward.calculateDimension(stringBounder).getWidth();
//		}
//		return new FtileGeometry(
//				xDeltaBecauseSpecial(stringBounder) + geo.getWidth() + dx + Hexagon.hexagonHalfSize + backwardWidth,
//				height, xDeltaBecauseSpecial(stringBounder) + geo.getLeft() + dx, geoDiamond1.getInY(), height);
//
//	}
//
//	private double xDeltaBecauseSpecial(StringBounder stringBounder) {
//		if (specialOut == null) {
//			return 0;
//		}
//		return specialOut.calculateDimension(stringBounder).getWidth();
//	}
//
//	@Override
//	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
//		if (child == whileBlock) {
//			return getTranslateForWhile(stringBounder);
//		}
//		if (child == diamond1) {
//			return getTranslateDiamond1(stringBounder);
//		}
//		throw new UnsupportedOperationException();
//	}
//
//	private UTranslate getTranslateForWhile(StringBounder stringBounder) {
//		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
//
//		final FtileGeometry dimTotal = calculateDimension(stringBounder);
//		final FtileGeometry dimWhile = whileBlock.calculateDimension(stringBounder);
//
//		final double y = dimDiamond1.getHeight()
//				+ (dimTotal.getHeight() - dimDiamond1.getHeight() - dimWhile.getHeight()) / 2;
//
//		final double x = dimTotal.getLeft() - dimWhile.getLeft();
//		return new UTranslate(x, y);
//
//	}
//
//	private UTranslate getTranslateDiamond1(StringBounder stringBounder) {
//		final FtileGeometry dimTotal = calculateDimension(stringBounder);
//		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
//
//		final double y1 = 0;
//		final double x1 = dimTotal.getLeft() - dimDiamond1.getLeft();
//		return new UTranslate(x1, y1);
//	}
//
//	private UTranslate getTranslateForSpecial(StringBounder stringBounder) {
//		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
//		final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
//		final double y1 = Math.max(3 * half, 4 * Hexagon.hexagonHalfSize);
//		final double xWhile = getTranslateForWhile(stringBounder).getDx() - Hexagon.hexagonHalfSize;
//		final double xDiamond = getTranslateDiamond1(stringBounder).getDx();
//		// final double x1 = xWhile - xDeltaBecauseSpecial(stringBounder);
//		final double x1 = Math.min(xWhile, xDiamond) - xDeltaBecauseSpecial(stringBounder);
//		// final double x1 = getTranslateForWhile(stringBounder).getDx() -
//		// dimDiamond1.getWidth()
//		// - xDeltaBecauseSpecial(stringBounder);
//		return new UTranslate(x1, y1);
//	}

}
