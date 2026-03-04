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
package net.sourceforge.plantuml.teavm;

import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UClip;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.shape.ULine;

public class DriverLineTeaVM implements UDriver<ULine, SvgGraphicsTeaVM> {
	// ::remove file when JAVA8

	private final ClipContainer clipContainer;

	public DriverLineTeaVM(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	@Override
	public void draw(ULine line, double x, double y, ColorMapper mapper, UParam param, SvgGraphicsTeaVM svg) {
		double x2 = x + line.getDX();
		double y2 = y + line.getDY();

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			if (clip.isInside(x, y) == false && clip.isInside(x2, y2) == false) {
				if (x == x2) {
					final double newY1 = clipY(clip, y);
					final double newY2 = clipY(clip, y2);
					if (newY1 == newY2)
						return;
					y = newY1;
					y2 = newY2;
				} else {
					return;
				}
			} else if (clip.isInside(x, y) == false || clip.isInside(x2, y2) == false) {
				if (x != x2 && y != y2)
					return;
				if (y == y2) {
					x = clipX(clip, x);
					x2 = clipX(clip, x2);
				} else if (x == x2) {
					y = clipY(clip, y);
					y2 = clipY(clip, y2);
				}
			}
		}

		DriverRectangleTeaVM.applyStrokeColor(svg, mapper, param);
		svg.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDasharraySvg());

		svg.drawLine(x, y, x2, y2);
	}

	private static double clipX(UClip clip, double xp) {
		return Math.max(clip.getX(), Math.min(xp, clip.getX() + clip.getWidth()));
	}

	private static double clipY(UClip clip, double yp) {
		return Math.max(clip.getY(), Math.min(yp, clip.getY() + clip.getHeight()));
	}
}
