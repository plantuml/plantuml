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
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
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
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

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

	protected List<Ftile> getFoo2(List<Ftile> all) {
		final double maxHeight = computeMaxHeight(all);
		final double ymargin1 = getSuppSpace1(all, getStringBounder());
		final double ymargin2 = getSuppSpace2(all, getStringBounder());
		final List<Ftile> result = new ArrayList<Ftile>();
		for (Ftile ftile : all) {
			final Ftile newFtile = computeNewFtile(ftile, maxHeight, ymargin1, ymargin2);
			result.add(newFtile);
		}
		return result;
	}

	private Ftile computeNewFtile(Ftile ftile, double maxHeight, double ymargin1, double ymargin2) {
		final double spaceArroundBlackBar = 20;
		final double xMargin = 14;
		Ftile tmp;
		tmp = FtileUtils.addHorizontalMargin(ftile, xMargin, xMargin + getSuppForIncomingArrow(ftile));
		tmp = new FtileHeightFixedCentered(tmp, maxHeight + 2 * spaceArroundBlackBar);
		tmp = new FtileHeightFixedMarged(ymargin1, tmp, ymargin2);
		return tmp;
	}

	private double getSuppForIncomingArrow(Ftile ftile) {
		final double x1 = getXSuppForDisplay(ftile, ftile.getInLinkRendering().getDisplay());
		final double x2 = getXSuppForDisplay(ftile, ftile.getOutLinkRendering().getDisplay());
		return Math.max(x1, x2);
	}

	private double getXSuppForDisplay(Ftile ftile, Display label) {
		final TextBlock text = getTextBlock(label);
		if (text == null) {
			return 0;
		}
		final double textWidth = text.calculateDimension(getStringBounder()).getWidth();
		final FtileGeometry ftileDim = ftile.calculateDimension(getStringBounder());
		final double pos2 = ftileDim.getLeft() + textWidth;
		if (pos2 > ftileDim.getWidth()) {
			return pos2 - ftileDim.getWidth();
		}
		return 0;
	}

	@Override
	protected Swimlane swimlaneOutForStep2() {
		return out;
	}

	@Override
	protected Ftile doStep1(Ftile middle) {
		Ftile result = middle;
		final List<Connection> conns = new ArrayList<Connection>();
		final Swimlane swimlaneBlack = in;
		final Ftile black = new FtileBlackBlock(skinParam(),
				getRose().getHtmlColor(skinParam(), ColorParam.activityBar), swimlaneBlack);
		double x = 0;
		for (Ftile tmp : list99) {
			final Dimension2D dim = tmp.calculateDimension(getStringBounder());
			final Rainbow def;
			if (SkinParam.USE_STYLES()) {
				Style style = getDefaultStyleDefinition().getMergedStyle(skinParam().getCurrentStyleBuilder());
				def = Rainbow.build(style, skinParam().getIHtmlColorSet());
			} else {
				def = Rainbow.build(skinParam());
			}
			final Rainbow rainbow = tmp.getInLinkRendering().getRainbow(def);
			conns.add(new ConnectionIn(black, tmp, x, rainbow));
			x += dim.getWidth();
		}

		result = FtileUtils.addConnection(result, conns);
		((FtileBlackBlock) black).setBlackBlockDimension(result.calculateDimension(getStringBounder()).getWidth(),
				barHeight);

		return new FtileAssemblySimple(black, result);
	}

	private double getSuppSpace1(List<Ftile> all, StringBounder stringBounder) {
		double result = 0;
		for (Ftile child : all) {
			final TextBlock text = getTextBlock(child.getInLinkRendering().getDisplay());
			if (text == null) {
				continue;
			}
			final Dimension2D dim = text.calculateDimension(stringBounder);
			result = Math.max(result, dim.getHeight());

		}
		return result;
	}

	private double getSuppSpace2(List<Ftile> all, StringBounder stringBounder) {
		double result = 0;
		for (Ftile child : all) {
			final TextBlock text = getTextBlock(child.getOutLinkRendering().getDisplay());
			if (text == null) {
				continue;
			}
			final Dimension2D dim = text.calculateDimension(stringBounder);
			result = Math.max(result, dim.getHeight());
		}
		return result;
	}

	private double getJustBeforeBar2(Ftile middle, StringBounder stringBounder) {
		return barHeight + getHeightOfMiddle(middle);
	}

	@Override
	protected Ftile doStep2(Ftile middle, Ftile result) {
		final Swimlane swimlaneBlack = out;
		final Ftile out = new FtileBlackBlock(skinParam(), getRose().getHtmlColor(skinParam(), ColorParam.activityBar),
				swimlaneBlack);
		((FtileBlackBlock) out).setBlackBlockDimension(result.calculateDimension(getStringBounder()).getWidth(),
				barHeight);
		if (label != null) {
			((FtileBlackBlock) out).setLabel(getTextBlock(Display.getWithNewlines(label)));
		}
		result = new FtileAssemblySimple(result, out);
		final List<Connection> conns = new ArrayList<Connection>();
		double x = 0;
		for (Ftile tmp : list99) {
			final Dimension2D dim = tmp.calculateDimension(getStringBounder());
			final Rainbow def;
			if (SkinParam.USE_STYLES()) {
				Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
				def = Rainbow.build(style, skinParam().getIHtmlColorSet());
			} else {
				def = Rainbow.build(skinParam());
			}
			final Rainbow rainbow = tmp.getOutLinkRendering().getRainbow(def);
			conns.add(new ConnectionOut(tmp, out, x, rainbow, getJustBeforeBar2(middle, getStringBounder())));
			x += dim.getWidth();
		}
		result = FtileUtils.addConnection(result, conns);
		return result;
	}

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
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			if (Display.isNull(label) == false) {
				snake.setLabel(getTextBlock(label));
			}
			final Point2D p1 = new Point2D.Double(geo2.getLeft(), 0);
			final Point2D p2 = new Point2D.Double(geo2.getLeft(), geo2.getInY());
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			ug = ug.apply(UTranslate.dx(x));
			final FtileGeometry geo2 = getFtile2().calculateDimension(getStringBounder());
			final Point2D p1 = new Point2D.Double(geo2.getLeft(), 0);
			final Point2D p2 = new Point2D.Double(geo2.getLeft(), geo2.getInY());

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			if (Display.isNull(label) == false) {
				snake.setLabel(getTextBlock(label));
			}
			final Point2D mp1a = translate1.getTranslated(p1);
			final Point2D mp2b = translate2.getTranslated(p2);
			final double middle = mp1a.getY() + 4;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			snake.addPoint(mp2b);
			snake.setIgnoreForCompression();
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
			if (geo1.hasPointOut() == false) {
				return;
			}
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			if (Display.isNull(label) == false) {
				snake.setLabel(getTextBlock(label));
			}
			final Point2D p1 = new Point2D.Double(geo1.getLeft(), barHeight + geo1.getOutY());
			final Point2D p2 = new Point2D.Double(geo1.getLeft(), justBeforeBar2);
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			ug = ug.apply(UTranslate.dx(x));
			final FtileGeometry geo = getFtile1().calculateDimension(getStringBounder());
			if (geo.hasPointOut() == false) {
				return;
			}
			final Point2D p1 = new Point2D.Double(geo.getLeft(), barHeight + geo.getOutY());
			final Point2D p2 = new Point2D.Double(geo.getLeft(), justBeforeBar2);

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			if (Display.isNull(label) == false) {
				snake.setLabel(getTextBlock(label));
			}
			final Point2D mp1a = translate1.getTranslated(p1);
			final Point2D mp2b = translate2.getTranslated(p2);
			final double middle = mp2b.getY() - 14;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			snake.addPoint(mp2b);
			snake.setIgnoreForCompression();
			ug.draw(snake);
		}

	}

}
