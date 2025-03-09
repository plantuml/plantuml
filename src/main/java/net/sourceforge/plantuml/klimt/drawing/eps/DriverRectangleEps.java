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
 */
package net.sourceforge.plantuml.klimt.drawing.eps;

import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UClip;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorGradient;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.shape.URectangle;

public class DriverRectangleEps implements UDriver<URectangle, EpsGraphics> {

	private final ClipContainer clipContainer;

	public DriverRectangleEps(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(URectangle rect, double x, double y, ColorMapper mapper, UParam param, EpsGraphics eps) {
		double width = rect.getWidth();
		double height = rect.getHeight();

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			final Rectangle2D.Double orig = new Rectangle2D.Double(x, y, width, height);
			final Rectangle2D.Double r = clip.getClippedRectangle(orig);
			if (r.height < 0) {
				return;
			}
			x = r.x;
			y = r.y;
			width = r.width;
			height = r.height;
		}

		final double rx = rect.getRx();
		final double ry = rect.getRy();

		// Shadow
		if (rect.getDeltaShadow() != 0) {
			eps.epsRectangleShadow(x, y, width, height, rx / 2, ry / 2, rect.getDeltaShadow());
		}

		final HColor back = param.getBackcolor();
		if (back instanceof HColorGradient) {
			eps.setStrokeColor(param.getColor().toColor(mapper));
			eps.epsRectangle(x, y, width, height, rx / 2, ry / 2, (HColorGradient) back, mapper);
		} else {
			eps.setStrokeColor(param.getColor().toColor(mapper));
			eps.setFillColor(param.getBackcolor().toColor(mapper));
			eps.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDashVisible(),
					param.getStroke().getDashSpace());
			eps.epsRectangle(x, y, width, height, rx / 2, ry / 2);
		}
	}
}
