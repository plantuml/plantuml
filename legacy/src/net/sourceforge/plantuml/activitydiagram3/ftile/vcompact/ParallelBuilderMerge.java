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
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileAssemblySimple;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBlackBlock;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class ParallelBuilderMerge extends AbstractParallelFtilesBuilder {

	public ParallelBuilderMerge(ISkinParam skinParam, StringBounder stringBounder, List<Ftile> all) {
		super(skinParam, stringBounder, all);
	}

	@Override
	protected Ftile doStep1(Ftile inner) {
		Ftile result = inner;
		final List<Connection> conns = new ArrayList<>();

		final Ftile black = new FtileBlackBlock(skinParam(), list99.get(0).getSwimlaneIn());
		double x = 0;
		for (Ftile tmp : list99) {
			final XDimension2D dim = tmp.calculateDimension(getStringBounder());
			Style style = getStyleSignature().getMergedStyle(skinParam().getCurrentStyleBuilder());
			final Rainbow def = Rainbow.build(style, skinParam().getIHtmlColorSet());
			final Rainbow rainbow = tmp.getInLinkRendering().getRainbow(def);
			conns.add(new ConnectionIn(black, tmp, x, rainbow));
			x += dim.getWidth();
		}

		result = FtileUtils.addConnection(result, conns);
		((FtileBlackBlock) black).setBlackBlockDimension(result.calculateDimension(getStringBounder()).getWidth(),
				barHeight);

		return new FtileAssemblySimple(black, result);
	}

	@Override
	protected Ftile doStep2(Ftile inner, Ftile result) {
		final Style style = getStyleSignature().getMergedStyle(skinParam().getCurrentStyleBuilder());

		final HColor borderColor = style.value(PName.LineColor).asColor(skinParam().getIHtmlColorSet());
		final HColor backColor = style.value(PName.BackGroundColor).asColor(skinParam().getIHtmlColorSet());

		final Ftile out = new FtileDiamond(skinParam(), backColor, borderColor, swimlaneOutForStep2());
		result = new FtileAssemblySimple(result, out);
		final List<Connection> conns = new ArrayList<>();
		final UTranslate diamondTranslate = result.getTranslateFor(out, getStringBounder());

		double x = 0;
		for (Ftile tmp : list99) {
			final XDimension2D dim = tmp.calculateDimension(getStringBounder());
			final UTranslate translate0 = new UTranslate(x, barHeight);
			final Rainbow def = Rainbow.build(style, skinParam().getIHtmlColorSet());
			final Rainbow rainbow = tmp.getOutLinkRendering().getRainbow(def);
			if (tmp.calculateDimension(getStringBounder()).hasPointOut())
				conns.add(new ConnectionHorizontalThenVertical(tmp, out, rainbow, translate0, diamondTranslate));

			x += dim.getWidth();

		}
		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionHorizontalThenVertical extends AbstractConnection /* implements ConnectionTranslatable */ {

		private final Rainbow arrowColor;
		private final UTranslate diamondTranslate;
		private final UTranslate translate0;

		public ConnectionHorizontalThenVertical(Ftile tile, Ftile diamond, Rainbow arrowColor, UTranslate translate0,
				UTranslate diamondTranslate) {
			super(tile, diamond);
			this.arrowColor = arrowColor;
			this.diamondTranslate = diamondTranslate;
			this.translate0 = translate0;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final XPoint2D p1 = getP1(stringBounder);
			final XPoint2D p2 = getP2(stringBounder, p1.getX());
			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final UTranslate arrival = arrivalOnDiamond(stringBounder, p1.getX());
			final UPolygon endDecoration;
			if (arrival.getDx() < 0)
				endDecoration = skinParam().arrows().asToRight();
			else if (arrival.getDx() > 0)
				endDecoration = skinParam().arrows().asToLeft();
			else
				endDecoration = skinParam().arrows().asToDown();

			final Snake snake = Snake.create(skinParam(), arrowColor, endDecoration);
			snake.addPoint(x1, y1);
			snake.addPoint(x1, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

		private XPoint2D getP1(StringBounder stringBounder) {
			return translate0.getTranslated(getFtile1().calculateDimension(stringBounder).getPointOut());
		}

		private XPoint2D getP2(StringBounder stringBounder, double startX) {
			final UTranslate arrival = arrivalOnDiamond(stringBounder, startX);
			return arrival.getTranslated(getDiamondOut(stringBounder));
		}

		public XPoint2D getDiamondOut(StringBounder stringBounder) {
			return diamondTranslate.getTranslated(getFtile2().calculateDimension(stringBounder).getPointOut());
		}

		public UTranslate arrivalOnDiamond(StringBounder stringBounder, double startX) {
			final XPoint2D result = getDiamondOut(stringBounder);
			final XDimension2D dim = getFtile2().calculateDimension(stringBounder);
			final double a = result.getX() - dim.getWidth() / 2;
			final double b = result.getX() + dim.getWidth() / 2;

			final UTranslate arrival;
			if (startX < a)
				arrival = new UTranslate(-dim.getWidth() / 2, -dim.getHeight() / 2);
			else if (startX > b)
				arrival = new UTranslate(dim.getWidth() / 2, -dim.getHeight() / 2);
			else
				arrival = new UTranslate(0, -dim.getHeight());

			return arrival;
		}

	}

	class ConnectionIn extends AbstractConnection implements ConnectionTranslatable {

		private final double x;
		private final Rainbow arrowColor;
		private final Display label;

		public ConnectionIn(Ftile ftile1, Ftile ftile2, double x, Rainbow arrowColor) {
			super(ftile1, ftile2);
			label = ftile2.getInLinkRendering().getDisplay();
			this.x = x;
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			ug = ug.apply(UTranslate.dx(x));
			final FtileGeometry geo = getFtile2().calculateDimension(getStringBounder());
			Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown());
			if (Display.isNull(label) == false)
				snake = snake.withLabel(getTextBlock(label), arrowHorizontalAlignment());

			snake.addPoint(geo.getLeft(), 0);
			snake.addPoint(geo.getLeft(), geo.getInY());
			ug.draw(snake);
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			ug = ug.apply(UTranslate.dx(x));
			final FtileGeometry geo = getFtile2().calculateDimension(getStringBounder());
			final XPoint2D p1 = new XPoint2D(geo.getLeft(), 0);
			final XPoint2D p2 = new XPoint2D(geo.getLeft(), geo.getInY());

			Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown());
			if (Display.isNull(label) == false)
				snake = snake.withLabel(getTextBlock(label), arrowHorizontalAlignment());

			final XPoint2D mp1a = translate1.getTranslated(p1);
			final XPoint2D mp2b = translate2.getTranslated(p2);
			final double middle = mp1a.getY() + 4;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			snake.addPoint(mp2b);
			ug.draw(snake);
		}
	}

}
