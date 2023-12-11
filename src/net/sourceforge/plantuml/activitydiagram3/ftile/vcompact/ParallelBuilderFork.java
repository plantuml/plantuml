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
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileHeightFixedCentered;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileHeightFixedMarged;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBlackBlock;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;

public class ParallelBuilderFork extends AbstractParallelFtilesBuilder {

	private final String label;
	private final Swimlane in;
	private final Swimlane out;

	public ParallelBuilderFork(ISkinParam skinParam, StringBounder stringBounder, String label, Swimlane in,
			Swimlane out, List<Ftile> all) {
		super(skinParam, stringBounder, all);
		this.label = label;
		this.in = in;
		this.out = out;
	}

	@Override
	protected Swimlane swimlaneOutForStep2() {
		return out;
	}

	@Override
	protected Ftile doStep1(Ftile middle) {
		Ftile result = middle;
		final List<Connection> conns = new ArrayList<>();
		final Swimlane swimlaneBlack = in;
		final Style style = getStyleSignatureArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
		final Ftile black = new FtileBlackBlock(skinParam(), swimlaneBlack);
		double x = 0;
		for (Ftile tmp : list99) {
			final XDimension2D dim = tmp.calculateDimension(getStringBounder());
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

	private double getJustBeforeBar2(Ftile middle, StringBounder stringBounder) {
		return barHeight + getHeightOfMiddle(middle);
	}

	@Override
	protected Ftile doStep2(Ftile middle, Ftile result) {
		final Swimlane swimlaneBlack = out;
		final Ftile out = new FtileBlackBlock(skinParam(), swimlaneBlack);
		((FtileBlackBlock) out).setBlackBlockDimension(result.calculateDimension(getStringBounder()).getWidth(),
				barHeight);	
		if (label != null)
			((FtileBlackBlock) out).setLabel(getTextBlock(Display.getWithNewlines(label)));

		result = new FtileAssemblySimple(result, out);
		final List<Connection> conns = new ArrayList<>();
		final Style style = getStyleSignatureArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
		double x = 0;
		for (Ftile tmp : list99) {
			final XDimension2D dim = tmp.calculateDimension(getStringBounder());
			final Rainbow def = Rainbow.build(style, skinParam().getIHtmlColorSet());
			final Rainbow rainbow = tmp.getOutLinkRendering().getRainbow(def);
			if (tmp.calculateDimension(getStringBounder()).hasPointOut())
				conns.add(new ConnectionOut(tmp, out, x, rainbow, getJustBeforeBar2(middle, getStringBounder())));

			x += dim.getWidth();
		}
		result = FtileUtils.addConnection(result, conns);
		return result;
	}

//	private HColor barColor() {
//		return getRose().getHtmlColor(skinParam(), ColorParam.activityBar);
//	}

	class ConnectionIn extends AbstractConnection implements ConnectionTranslatable {

		private final double x;
		private final Rainbow arrowColor;
		private final Display label;

		public ConnectionIn(Ftile ftile1, Ftile ftile2, double x, Rainbow arrowColor) {
			super(ftile1, ftile2);
			this.label = ftile2.getInLinkRendering().getDisplay();
			this.x = x;
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			ug = ug.apply(UTranslate.dx(x));
			final FtileGeometry geo2 = getFtile2().calculateDimension(getStringBounder());
			Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown());
			if (Display.isNull(label) == false)
				snake = snake.withLabel(getTextBlock(label), arrowHorizontalAlignment());

			final XPoint2D p1 = new XPoint2D(geo2.getLeft(), 0);
			final XPoint2D p2 = new XPoint2D(geo2.getLeft(), geo2.getInY());
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			ug = ug.apply(UTranslate.dx(x));
			final FtileGeometry geo2 = getFtile2().calculateDimension(getStringBounder());
			final XPoint2D p1 = new XPoint2D(geo2.getLeft(), 0);
			final XPoint2D p2 = new XPoint2D(geo2.getLeft(), geo2.getInY());

			Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown()).ignoreForCompression();
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

	class ConnectionOut extends AbstractConnection implements ConnectionTranslatable {

		private final double x;
		private final Rainbow arrowColor;
		private final Display label;
		private final double justBeforeBar2;

		public ConnectionOut(Ftile ftile1, Ftile ftile2, double x, Rainbow arrowColor, double justBeforeBar2) {
			super(ftile1, ftile2);
			this.justBeforeBar2 = justBeforeBar2;
			this.label = ftile1.getOutLinkRendering().getDisplay();
			this.x = x;
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			ug = ug.apply(UTranslate.dx(x));
			final FtileGeometry geo1 = getFtile1().calculateDimension(getStringBounder());
			if (geo1.hasPointOut() == false)
				return;

			Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown());
			if (Display.isNull(label) == false)
				snake = snake.withLabel(getTextBlock(label), arrowHorizontalAlignment());

			final XPoint2D p1 = new XPoint2D(geo1.getLeft(), barHeight + geo1.getOutY());
			final XPoint2D p2 = new XPoint2D(geo1.getLeft(), justBeforeBar2);
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			ug = ug.apply(UTranslate.dx(x));
			final FtileGeometry geo = getFtile1().calculateDimension(getStringBounder());
			if (geo.hasPointOut() == false)
				return;

			final XPoint2D p1 = new XPoint2D(geo.getLeft(), barHeight + geo.getOutY());
			final XPoint2D p2 = new XPoint2D(geo.getLeft(), justBeforeBar2);

			Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown()).ignoreForCompression();
			if (Display.isNull(label) == false)
				snake = snake.withLabel(getTextBlock(label), arrowHorizontalAlignment());

			final XPoint2D mp1a = translate1.getTranslated(p1);
			final XPoint2D mp2b = translate2.getTranslated(p2);
			final double middle = mp2b.getY() - 14;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			snake.addPoint(mp2b);
			ug.draw(snake);
		}

	}

}
