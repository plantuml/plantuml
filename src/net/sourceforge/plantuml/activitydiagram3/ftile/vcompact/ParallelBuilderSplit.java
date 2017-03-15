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
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileKilled;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.MergeStrategy;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.HtmlColorAndStyle;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ParallelBuilderSplit extends ParallelFtilesBuilder {

	public ParallelBuilderSplit(ISkinParam skinParam, StringBounder stringBounder,
			final List<Ftile> list, Ftile inner, Swimlane swimlane) {
		super(skinParam, stringBounder, list, inner, swimlane);
	}

	@Override
	protected Ftile doStep1() {
		Ftile result = getMiddle();
		final List<Connection> conns = new ArrayList<Connection>();

		double x1 = 0;
		for (Ftile tmp : getList()) {
			final Dimension2D dim = tmp.calculateDimension(getStringBounder());
			conns.add(new ConnectionIn(tmp, x1, tmp.getInLinkRendering().getRainbow(
					HtmlColorAndStyle.build(skinParam())), getTextBlock(tmp.getInLinkRendering())));
			x1 += dim.getWidth();
		}
		final double totalWidth1 = result.calculateDimension(getStringBounder()).getWidth();
		conns.add(new ConnectionHline2(result, HtmlColorAndStyle.build(skinParam()), 0, getList(), totalWidth1));
		result = FtileUtils.addConnection(result, conns);
		return result;
	}

	@Override
	protected Ftile doStep2(Ftile result) {
		final List<Connection> conns2 = new ArrayList<Connection>();
		double x2 = 0;
		boolean hasOut = false;
		for (Ftile tmp : getList()) {
			final Dimension2D dim = tmp.calculateDimension(getStringBounder());
			final boolean hasOutTmp = tmp.calculateDimension(getStringBounder()).hasPointOut();
			if (hasOutTmp) {
				conns2.add(new ConnectionOut(tmp, x2, tmp.getOutLinkRendering().getRainbow(
						HtmlColorAndStyle.build(skinParam())), getHeightOfMiddle(), getTextBlock(tmp
						.getOutLinkRendering())));
				hasOut = true;
			}
			x2 += dim.getWidth();
		}

		if (hasOut) {
			final double totalWidth2 = result.calculateDimension(getStringBounder()).getWidth();
			conns2.add(new ConnectionHline2(result, HtmlColorAndStyle.build(skinParam()), getHeightOfMiddle(),
					getList(), totalWidth2));
			result = FtileUtils.addConnection(result, conns2);
		} else {
			result = new FtileKilled(result);
		}
		return result;
	}

	static class ConnectionHline2 extends AbstractConnection {

		private final Ftile inner;
		private final double y;
		private final Rainbow arrowColor;
		private final List<Ftile> list;
		private final double totalWidth;

		public ConnectionHline2(Ftile inner, Rainbow arrowColor, double y, List<Ftile> list, double totalWidth) {
			super(null, null);
			this.inner = inner;
			this.y = y;
			this.arrowColor = arrowColor;
			this.list = list;
			this.totalWidth = totalWidth;
		}

		public void drawU(UGraphic ug) {
			double minX = Double.MAX_VALUE;
			double maxX = 0;
			if (y == 0 && ug instanceof UGraphicInterceptorOneSwimlane) {
				final Swimlane intoSw = ((UGraphicInterceptorOneSwimlane) ug).getSwimlane();
				boolean found = false;
				for (Ftile tmp : list) {
					if (tmp.getSwimlaneIn() == intoSw) {
						found = true;
					}
				}
				if (found == false) {
					return;
				}
			}
			final StringBounder stringBounder = ug.getStringBounder();
			for (Ftile tmp : list) {
				if (y > 0 && tmp.calculateDimension(stringBounder).hasPointOut() == false) {
					continue;
				}
				final UTranslate ut = inner.getTranslateFor(tmp, stringBounder);
				if (ut == null) {
					continue;
				}
				final double middle = tmp.calculateDimension(stringBounder).translate(ut).getLeft();
				minX = Math.min(minX, middle);
				maxX = Math.max(maxX, middle);
			}
			if (minX > totalWidth / 2) {
				minX = totalWidth / 2;
			}
			if (maxX < totalWidth / 2) {
				maxX = totalWidth / 2;
			}

			final Snake s = new Snake(arrowColor);
			s.goUnmergeable(MergeStrategy.NONE);
			s.addPoint(minX, y);
			s.addPoint(maxX, y);
			ug.draw(s);
		}
	}

	static class ConnectionHline3 extends AbstractConnection implements ConnectionTranslatable {

		private final Ftile inner;
		private final double y;
		private final Rainbow arrowColor;
		private final List<Ftile> list;
		private final double totalWidth;

		public ConnectionHline3(Ftile inner, Rainbow arrowColor, double y, List<Ftile> list, double totalWidth) {
			super(null, null);
			this.inner = inner;
			this.y = y;
			this.arrowColor = arrowColor;
			this.list = list;
			this.totalWidth = totalWidth;
		}

		public void drawU(UGraphic ug) {
			throw new UnsupportedOperationException();
		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			final UTranslate left = inner.getSwimlaneIn().getTranslate();
			double minX = inner.calculateDimension(stringBounder).getLeft() + left.getDx();
			double maxX = minX;

			for (Ftile tmp : list) {
				final FtileGeometry tmpGeom = tmp.calculateDimension(stringBounder);
				final UTranslate tpos = inner.getTranslateFor(tmp, stringBounder);
				for (Swimlane sw : tmp.getSwimlanes()) {
					final double x = tmpGeom.translate(sw.getTranslate().compose(tpos)).getLeft();
					minX = Math.min(minX, x);
					maxX = Math.max(maxX, x);
				}
			}
			final Dimension2D dimInner = inner.calculateDimension(stringBounder);

			final Snake s = new Snake(arrowColor);
			// final Snake s = new Snake(HtmlColorUtils.GREEN);
			s.goUnmergeable(MergeStrategy.LIMITED);
			s.addPoint(minX, y);
			s.addPoint(maxX, y);
			ug.draw(s);
		}
	}

	static class ConnectionIn extends AbstractConnection {

		private final double x;
		private final Rainbow arrowColor;
		private final TextBlock text;

		public ConnectionIn(Ftile tmp, double x, Rainbow arrowColor, TextBlock text) {
			super(null, tmp);
			this.x = x;
			this.arrowColor = arrowColor;
			this.text = text;
		}

		public void drawU(UGraphic ug) {
			ug = ug.apply(new UTranslate(x, 0));
			final FtileGeometry geo = getFtile2().calculateDimension(ug.getStringBounder());
			final double left = geo.getLeft();
			final Snake s = new Snake(arrowColor, Arrows.asToDown());
			s.setLabel(text);
			s.addPoint(left, 0);
			s.addPoint(left, geo.getInY());
			ug.draw(s);
		}
	}

	static class ConnectionOut extends AbstractConnection {

		private final double x;
		private final Rainbow arrowColor;
		private final double height;
		private final TextBlock text;

		public ConnectionOut(Ftile tmp, double x, Rainbow arrowColor, double height, TextBlock text) {
			super(tmp, null);
			this.x = x;
			this.arrowColor = arrowColor;
			this.height = height;
			this.text = text;
		}

		public void drawU(UGraphic ug) {
			ug = ug.apply(new UTranslate(x, 0));
			final FtileGeometry geo = getFtile1().calculateDimension(ug.getStringBounder());
			if (geo.hasPointOut() == false) {
				assert false;
				return;
			}
			final Snake s = new Snake(arrowColor, Arrows.asToDown());
			s.setLabel(text);
			s.goUnmergeable(MergeStrategy.NONE);
			s.addPoint(geo.getLeft(), geo.getOutY());
			s.addPoint(geo.getLeft(), height);
			ug.draw(s);
		}
	}

}
