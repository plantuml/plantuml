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
 * Revision $Revision: 3837 $
 *
 */
package net.sourceforge.plantuml.ugraphic.eps;

import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverDotPathEps implements UDriver<EpsGraphics> {

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, EpsGraphics eps) {
		final DotPath shape = (DotPath) ushape;
		//DriverLineG2d.manageStroke(param, g2d);

		if (param.getColor() != null) {
			eps.setStrokeColor(mapper.getMappedColor(param.getColor()));
			eps.setStrokeWidth("" + param.getStroke().getThickness(), param.getStroke().getDashVisible(), param.getStroke()
					.getDashSpace());
			shape.draw(eps, x, y);
		}
	}
}
