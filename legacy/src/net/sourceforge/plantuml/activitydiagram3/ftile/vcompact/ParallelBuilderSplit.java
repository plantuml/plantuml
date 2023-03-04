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

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileAssemblySimple;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileKilled;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileThinSplit;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class ParallelBuilderSplit extends AbstractParallelFtilesBuilder {

	public ParallelBuilderSplit(ISkinParam skinParam, StringBounder stringBounder, List<Ftile> all) {
		super(skinParam, stringBounder, all);
	}

	@Override
	public StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	@Override
	protected Ftile doStep1(Ftile inner) {
		Ftile result = inner;
		final List<Connection> conns = new ArrayList<>();
		final Style style = getStyleSignature().getMergedStyle(skinParam().getCurrentStyleBuilder());
		final Rainbow thinColor = Rainbow.build(style, skinParam().getIHtmlColorSet());

		final Ftile thin = new FtileThinSplit(skinParam(), getThin1Color(thinColor), list99.get(0).getSwimlaneIn());
		double x = 0;
		double first = 0;
		double last = 0;
		for (Ftile tmp : list99) {
			final FtileGeometry dim = tmp.calculateDimension(getStringBounder());
			if (first == 0)
				first = x + dim.getLeft();

			last = x + dim.getLeft();

			final LinkRendering inLinkRendering = tmp.getInLinkRendering();
			final Rainbow rainbow = inLinkRendering.getRainbow(Rainbow.build(style, skinParam().getIHtmlColorSet()));

			conns.add(new ConnectionIn(thin, tmp, x, rainbow));
			x += dim.getWidth();
		}

		result = FtileUtils.addConnection(result, conns);
		final FtileGeometry geom = result.calculateDimension(getStringBounder());
		if (last < geom.getLeft())
			last = geom.getLeft();

		if (first > geom.getLeft())
			first = geom.getLeft();

		((FtileThinSplit) thin).setGeom(first, last, result.calculateDimension(getStringBounder()).getWidth());

		return new FtileAssemblySimple(thin, result);
	}

	private HColor getThin1Color(final Rainbow thinColor) {
		final Style style = getStyleSignature().getMergedStyle(skinParam().getCurrentStyleBuilder());
		for (Ftile tmp : list99) {
			final LinkRendering inLinkRendering = tmp.getInLinkRendering();
			final Rainbow rainbow = inLinkRendering.getRainbow(Rainbow.build(style, skinParam().getIHtmlColorSet()));

			if (rainbow.isInvisible() == false)
				return thinColor.getColor();

		}
		return null;
	}

	private boolean hasOut() {
		for (Ftile tmp : list99)
			if (tmp.calculateDimension(getStringBounder()).hasPointOut())
				return true;

		return false;
	}

	@Override
	protected Ftile doStep2(Ftile inner, Ftile result) {

		final FtileGeometry geom = result.calculateDimension(getStringBounder());
		if (hasOut() == false)
			return new FtileKilled(result);

		final Style style = getStyleSignature().getMergedStyle(skinParam().getCurrentStyleBuilder());
		final LinkRendering inLinkRendering = result.getInLinkRendering();
		final Rainbow thinColor = inLinkRendering.getRainbow(Rainbow.build(style, skinParam().getIHtmlColorSet()));

		final Ftile out = new FtileThinSplit(skinParam(), thinColor.getColor(), swimlaneOutForStep2());
		result = new FtileAssemblySimple(result, out);
		final List<Connection> conns = new ArrayList<>();
		double x = 0;
		double first = 0;
		double last = 0;
		for (Ftile tmp : list99) {
			final UTranslate translate0 = UTranslate.dy(1.5);
			final FtileGeometry dim = tmp.calculateDimension(getStringBounder());
			if (dim.hasPointOut()) {
				if (first == 0)
					first = x + dim.getLeft();

				last = x + dim.getLeft();
			}

			final LinkRendering outLinkRendering = tmp.getOutLinkRendering();
			final Rainbow rainbow = outLinkRendering.getRainbow(Rainbow.build(style, skinParam().getIHtmlColorSet()));

			if (tmp.calculateDimension(getStringBounder()).hasPointOut())
				conns.add(new ConnectionOut(translate0, tmp, out, x, rainbow, getHeightOfMiddle(inner)));

			x += dim.getWidth();
		}
		if (last < geom.getLeft())
			last = geom.getLeft();

		if (first > geom.getLeft())
			first = geom.getLeft();

		((FtileThinSplit) out).setGeom(first, last, geom.getWidth());
		result = FtileUtils.addConnection(result, conns);
		return result;
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

	class ConnectionOut extends AbstractConnection implements ConnectionTranslatable {

		private final double x;
		private final Rainbow arrowColor;
		private final double height;
		private final Display label;
		private final UTranslate translate0;

		public ConnectionOut(UTranslate translate0, Ftile ftile1, Ftile ftile2, double x, Rainbow arrowColor,
				double height) {
			super(ftile1, ftile2);
			this.translate0 = translate0;
			this.label = ftile1.getOutLinkRendering().getDisplay();
			this.x = x;
			this.arrowColor = arrowColor;
			this.height = height;
		}

		public void drawU(UGraphic ug) {
			ug = ug.apply(UTranslate.dx(x));
			final FtileGeometry geo = getFtile1().calculateDimension(getStringBounder());
			if (geo.hasPointOut() == false)
				return;

			Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown());
			if (Display.isNull(label) == false)
				snake = snake.withLabel(getTextBlock(label), arrowHorizontalAlignment());

			final XPoint2D p1 = translate0.getTranslated(new XPoint2D(geo.getLeft(), geo.getOutY()));
			final XPoint2D p2 = translate0.getTranslated(new XPoint2D(geo.getLeft(), height));
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

			final XPoint2D p1 = translate0.getTranslated(new XPoint2D(geo.getLeft(), geo.getOutY()));
			final XPoint2D p2 = translate0.getTranslated(new XPoint2D(geo.getLeft(), height));

			Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown());
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
