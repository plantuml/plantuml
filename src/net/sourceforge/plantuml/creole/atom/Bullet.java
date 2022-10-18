/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.creole.atom;

import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class Bullet extends AbstractAtom implements Atom {

	private final FontConfiguration fontConfiguration;
	private final int order;

	public Bullet(FontConfiguration fontConfiguration, int order) {
		this.fontConfiguration = fontConfiguration;
		this.order = order;
	}

	public void drawU(UGraphic ug) {
		final HColor color = fontConfiguration.getColor();
		ug = ug.apply(color).apply(color.bg()).apply(new UStroke(0));
		if (order == 0) {
			ug = ug.apply(UTranslate.dx(3));
			ug.draw(new UEllipse(5, 5));
		} else {
			ug = ug.apply(UTranslate.dx(1 + 8 * order));
			ug.draw(new URectangle(3.5, 3.5));
		}

	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		if (order == 0)
			return new XDimension2D(12, 5);
		return new XDimension2D(8 + 8 * order, 3);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		if (order == 0)
			return -5;
		return -7;
	}

}
