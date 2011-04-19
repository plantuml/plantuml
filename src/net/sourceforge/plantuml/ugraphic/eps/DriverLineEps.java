/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 */
package net.sourceforge.plantuml.ugraphic.eps;

import java.awt.geom.Line2D;

import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverLineEps implements UDriver<EpsGraphics> {

	private final ClipContainer clipContainer;

	public DriverLineEps(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(UShape ushape, double x, double y, UParam param, EpsGraphics eps) {
		final ULine shape = (ULine) ushape;

		double x2 = x + shape.getDX();
		double y2 = y + shape.getDY();

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			final Line2D.Double line = clip.getClippedLine(new Line2D.Double(x, y, x2, y2));
			if (line == null) {
				return;
			}
			x = line.x1;
			y = line.y1;
			x2 = line.x2;
			y2 = line.y2;
		}

		eps.setStrokeColor(param.getColor());
		eps.setStrokeWidth("" + param.getStroke().getThickness(), param.getStroke().getDashVisible(), param.getStroke()
				.getDashSpace());
		eps.epsLine(x, y, x2, y2);
	}
}
