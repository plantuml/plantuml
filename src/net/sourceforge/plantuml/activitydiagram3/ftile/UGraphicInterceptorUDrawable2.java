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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Point2D;
import java.util.Map;

import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.UGraphicDelegator;
import net.sourceforge.plantuml.svek.UGraphicForSnake;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class UGraphicInterceptorUDrawable2 extends UGraphicDelegator {

	private final Map<String, UTranslate> positions;

	public UGraphicInterceptorUDrawable2(UGraphic ug, Map<String, UTranslate> positions) {
		super(ug);
		this.positions = positions;
	}

	public void draw(UShape shape) {
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
		final FtileGeometry geom = ftile.calculateDimension(getStringBounder());
		final Point2D pt = geom.getPointIn();
		UGraphic ugGoto = getUg().apply(HColorUtils.GREEN).apply(
				HColorUtils.GREEN.bg());
		ugGoto = ugGoto.apply(new UTranslate(pt));
		final UTranslate posNow = getPosition();
		final UTranslate dest = positions.get(ftile.getName());
		final double dx = dest.getDx() - posNow.getDx();
		final double dy = dest.getDy() - posNow.getDy();
		ugGoto.draw(new UEllipse(3, 3));
		ugGoto.apply(new UTranslate(dx, dy)).draw(new UEllipse(3, 3));
		ugGoto.draw(ULine.hline(dx));
		ugGoto.apply(UTranslate.dx(dx)).draw(ULine.vline(dy));
		// ugGoto.draw(new ULine(dx, dy));
	}

	public UGraphic apply(UChange change) {
		return new UGraphicInterceptorUDrawable2(getUg().apply(change), positions);
	}

}