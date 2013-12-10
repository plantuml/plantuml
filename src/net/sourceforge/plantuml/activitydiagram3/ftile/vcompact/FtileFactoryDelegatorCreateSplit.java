/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileHeightFixed;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMarged;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileFactoryDelegatorCreateSplit extends FtileFactoryDelegator {

	private final double spaceArroundBlackBar = 20;
	private final double barHeight = 6;
	private final double xMargin = 14;

	private final Rose rose = new Rose();

	public FtileFactoryDelegatorCreateSplit(FtileFactory factory, ISkinParam skinParam) {
		super(factory, skinParam);
	}

	@Override
	public Ftile createSplit(List<Ftile> all) {
		// final HtmlColor colorBar = rose.getHtmlColor(getSkinParam(), ColorParam.activityBar);
		final HtmlColor arrowColor = rose.getHtmlColor(getSkinParam(), ColorParam.activityArrow);

		final Dimension2D dimSuper = super.createSplit(all).asTextBlock().calculateDimension(getStringBounder());
		final double height1 = dimSuper.getHeight() + 2 * spaceArroundBlackBar;

		final List<Ftile> list = new ArrayList<Ftile>();
		for (Ftile tmp : all) {
			list.add(new FtileHeightFixed(new FtileMarged(tmp, xMargin), height1));
		}

		Ftile inner = super.createSplit(list);

		final List<Connection> conns = new ArrayList<Connection>();

		double x = 0;
		for (Ftile tmp : list) {
			final Dimension2D dim = tmp.asTextBlock().calculateDimension(getStringBounder());
			conns.add(new ConnectionIn(tmp, x, arrowColor));
			conns.add(new ConnectionOut(tmp, x, arrowColor, height1));
			x += dim.getWidth();
		}
		final double totalWidth = inner.asTextBlock().calculateDimension(getStringBounder()).getWidth();
		conns.add(new ConnectionHline2(inner, arrowColor, 0, list, totalWidth));
		conns.add(new ConnectionHline2(inner, arrowColor, height1, list, totalWidth));

		inner = FtileUtils.addConnection(inner, conns);
		return inner;
	}

	static class ConnectionHline2 extends AbstractConnection {

		private final Ftile inner;
		private final double y;
		private final HtmlColor arrowColor;
		private final List<Ftile> list;
		private final double totalWidth;

		public ConnectionHline2(Ftile inner, HtmlColor arrowColor, double y, List<Ftile> list, double totalWidth) {
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
			final StringBounder stringBounder = ug.getStringBounder();
			for (Ftile tmp : list) {
				if (y > 0 && tmp.isKilled()) {
					continue;
				}
				final UTranslate ut = inner.getTranslateFor(tmp, stringBounder);
				final double middle = ut.getTranslated(tmp.getPointIn(stringBounder)).getX();
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
			s.addPoint(minX, y);
			s.addPoint(maxX, y);
			ug.draw(s);
		}
	}

	static class ConnectionIn extends AbstractConnection {

		private final double x;
		private final HtmlColor arrowColor;

		public ConnectionIn(Ftile tmp, double x, HtmlColor arrowColor) {
			super(null, tmp);
			this.x = x;
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			ug = ug.apply(new UTranslate(x, 0));
			final Point2D p = getFtile2().getPointIn(ug.getStringBounder());
			final Snake s = new Snake(arrowColor, Arrows.asToDown());
			s.addPoint(p.getX(), 0);
			s.addPoint(p.getX(), p.getY());
			ug.draw(s);
		}
	}

	static class ConnectionOut extends AbstractConnection {

		private final double x;
		private final HtmlColor arrowColor;
		private final double height;

		public ConnectionOut(Ftile tmp, double x, HtmlColor arrowColor, double height) {
			super(tmp, null);
			this.x = x;
			this.arrowColor = arrowColor;
			this.height = height;
		}

		public void drawU(UGraphic ug) {
			ug = ug.apply(new UTranslate(x, 0));
			final Point2D p = getFtile1().getPointOut(ug.getStringBounder());
			if (p == null) {
				return;
			}
			final Snake s = new Snake(arrowColor, Arrows.asToDown());
			s.addPoint(p.getX(), p.getY());
			s.addPoint(p.getX(), height);
			ug.draw(s);
		}
	}

}
