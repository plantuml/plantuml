/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.svek.extremity;

import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class MiddleCircleCircled extends Extremity {

	private final double angle;
	private final MiddleCircleCircledMode mode;
	private final double radius1 = 6;
	private final UEllipse circle = new UEllipse(2 * radius1, 2 * radius1);

	private final double radius2 = 10;
	private final UEllipse bigcircle = new UEllipse(2 * radius2, 2 * radius2);

	public MiddleCircleCircled(double angle, MiddleCircleCircledMode mode) {
		this.angle = angle;
		this.mode = mode;
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(new UChangeBackColor(HtmlColorUtils.WHITE));
		if (mode == MiddleCircleCircledMode.BOTH) {
			ug.apply(new UChangeColor(HtmlColorUtils.WHITE)).apply(new UTranslate(-radius2, -radius2)).draw(bigcircle);
		}

		ug = ug.apply(new UStroke(1.5));

		final double d = 0;
		if (mode == MiddleCircleCircledMode.MODE1 || mode == MiddleCircleCircledMode.BOTH) {
			final UEllipse arc1 = new UEllipse(2 * radius2, 2 * radius2, angle, 90);
			ug.apply(new UTranslate(-radius2 + d, -radius2 + d)).draw(arc1);
		}
		if (mode == MiddleCircleCircledMode.MODE2 || mode == MiddleCircleCircledMode.BOTH) {
			final UEllipse arc2 = new UEllipse(2 * radius2, 2 * radius2, angle + 180, 90);
			ug.apply(new UTranslate(-radius2 + d, -radius2 + d)).draw(arc2);
		}
		ug.apply(new UTranslate(-radius1, -radius1)).draw(circle);
	}

}
