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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.Map;

import net.sourceforge.plantuml.activitydiagram3.gtile.Gtile;
import net.sourceforge.plantuml.klimt.UChange;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphicDelegator;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.svek.UGraphicForSnake;

public class UGraphicInterceptorUDrawable2 extends UGraphicDelegator {

	private final Map<String, UTranslate> positions;
	private final HColor gotoColor;
	private final boolean isDebug;

	public UGraphicInterceptorUDrawable2(UGraphic ug, Map<String, UTranslate> positions, HColor gotoColor, boolean isDebug) {
		super(ug);
		this.positions = positions;
		this.gotoColor = gotoColor;
		this.isDebug = isDebug;
	}

	public void draw(UShape shape) {
		// :: comment when __CORE__
		if (shape instanceof Gtile) {
			final Gtile gtile = (Gtile) shape;
			// System.err.println("gtile=" + gtile);
			gtile.drawU(this);
		} else
		// :: done
		if (shape instanceof Ftile) {
			final Ftile ftile = (Ftile) shape;
			// System.err.println("ftile=" + ftile);
			ftile.drawU(this);
			if (ftile instanceof FtileLabel) {
				positions.put(((FtileLabel) ftile).getName(), getPosition());
				// System.err.println("ug=" + getUg().getClass());
			}
			if (ftile instanceof FtileGoto) {
				// System.err.println("positions=" + positions);
				drawGoto((FtileGoto) ftile);
			}
		} else if (shape instanceof UDrawable) {
			final UDrawable drawable = (UDrawable) shape;
			drawable.drawU(this);
		} else {
			getUg().draw(shape);
		}

	}

	private UTranslate getPosition() {
		return ((UGraphicForSnake) getUg()).getTranslation();
	}

	private void drawGoto(FtileGoto ftile) {
		//final HColor gotoColor = HColors.MY_RED;

		final FtileGeometry geom = ftile.calculateDimension(getStringBounder());
		final XPoint2D pt = geom.getPointIn();
		UGraphic ugGoto = getUg().apply(gotoColor).apply(gotoColor.bg());
		ugGoto = ugGoto.apply(UTranslate.point(pt));
		final UTranslate posNow = getPosition();
		final UTranslate dest = positions.get(ftile.getName());
		if (dest == null)
			return;
		final double dx = dest.getDx() - posNow.getDx();
		final double dy = dest.getDy() - posNow.getDy();
		if (isDebug) {
		ugGoto.draw(UEllipse.build(3, 3));
		ugGoto.apply(new UTranslate(dx, dy)).draw(UEllipse.build(3, 3));
		}
		ugGoto.draw(ULine.hline(dx));
		ugGoto.apply(UTranslate.dx(dx)).draw(ULine.vline(dy));
		// ugGoto.draw(new ULine(dx, dy));
	}

	public UGraphic apply(UChange change) {
		return new UGraphicInterceptorUDrawable2(getUg().apply(change), positions, gotoColor, isDebug);
	}

}