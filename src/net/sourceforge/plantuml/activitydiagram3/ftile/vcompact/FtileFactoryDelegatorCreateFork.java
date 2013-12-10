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
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileAssemblySimple;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileHeightFixed;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMarged;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBlackBlock;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileFactoryDelegatorCreateFork extends FtileFactoryDelegator {

	private final double spaceArroundBlackBar = 20;
	private final double barHeight = 6;
	private final double xMargin = 14;

	private final Rose rose = new Rose();

	public FtileFactoryDelegatorCreateFork(FtileFactory factory, ISkinParam skinParam) {
		super(factory, skinParam);
	}

	@Override
	public Ftile createFork(List<Ftile> all) {
		final HtmlColor colorBar = rose.getHtmlColor(getSkinParam(), ColorParam.activityBar);
		final HtmlColor arrowColor = rose.getHtmlColor(getSkinParam(), ColorParam.activityArrow);

		final Dimension2D dimSuper = super.createFork(all).asTextBlock().calculateDimension(getStringBounder());
		final double height1 = dimSuper.getHeight() + 2 * spaceArroundBlackBar;

		final List<Ftile> list = new ArrayList<Ftile>();
		for (Ftile tmp : all) {
			list.add(new FtileHeightFixed(new FtileMarged(tmp, xMargin), height1));
		}

		Ftile inner = super.createFork(list);

		final List<Connection> conns = new ArrayList<Connection>();

		double x = 0;
		for (Ftile tmp : list) {
			final Dimension2D dim = tmp.asTextBlock().calculateDimension(getStringBounder());
			conns.add(new ConnectionIn(tmp, x, arrowColor));
			conns.add(new ConnectionOut(tmp, x, arrowColor, height1));
			x += dim.getWidth();
		}

		inner = FtileUtils.addConnection(inner, conns);
		final Ftile black = new FtileBlackBlock(shadowing(), inner.asTextBlock().calculateDimension(getStringBounder())
				.getWidth(), barHeight, colorBar, list.get(0).getSwimlaneIn());
		return new FtileAssemblySimple(new FtileAssemblySimple(black, inner), black);
	}

	class ConnectionIn extends AbstractConnection {

		private final double x;
		private final HtmlColor arrowColor;

		public ConnectionIn(Ftile tmp, double x, HtmlColor arrowColor) {
			super(null, tmp);
			this.x = x;
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			ug = ug.apply(new UTranslate(x, 0));
			final Point2D p = getFtile2().getPointIn(getStringBounder());
			final Snake s = new Snake(arrowColor, Arrows.asToDown());
			s.addPoint(p.getX(), 0);
			s.addPoint(p.getX(), p.getY());
			ug.draw(s);
		}
	}

	class ConnectionOut extends AbstractConnection {

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
			final Point2D p = getFtile1().getPointOut(getStringBounder());
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
