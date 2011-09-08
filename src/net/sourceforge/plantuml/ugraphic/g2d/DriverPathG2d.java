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
package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;

import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.USegment;
import net.sourceforge.plantuml.ugraphic.USegmentType;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverPathG2d implements UDriver<Graphics2D> {

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
		final UPath shape = (UPath) ushape;
		DriverLineG2d.manageStroke(param, g2d);

		final GeneralPath p = new GeneralPath();
		for (USegment seg : shape) {
			final USegmentType type = seg.getSegmentType();
			final double coord[] = seg.getCoord();
			if (type == USegmentType.SEG_MOVETO) {
				p.moveTo(x + coord[0], y + coord[1]);
			} else if (type == USegmentType.SEG_LINETO) {
				p.lineTo(x + coord[0], y + coord[1]);
			} else if (type == USegmentType.SEG_CUBICTO) {
				p.curveTo(x + coord[0], y + coord[1], x + coord[2], y + coord[3], x + coord[4], y + coord[5]);
			} else {
				throw new UnsupportedOperationException();
			}
			// bez = new CubicCurve2D.Double(x + bez.x1, y + bez.y1, x +
			// bez.ctrlx1, y + bez.ctrly1, x + bez.ctrlx2, y
			// + bez.ctrly2, x + bez.x2, y + bez.y2);
			// p.append(bez, true);
		}
		// p.closePath();

		if (param.getBackcolor() != null) {
			g2d.setColor(mapper.getMappedColor(param.getBackcolor()));
			g2d.fill(p);
		}
		if (param.getColor() != null) {
			g2d.setColor(mapper.getMappedColor(param.getColor()));
			g2d.draw(p);
		}
	}

}
