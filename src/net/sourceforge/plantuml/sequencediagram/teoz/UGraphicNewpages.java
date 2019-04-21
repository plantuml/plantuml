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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.graphic.UGraphicDelegator;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class UGraphicNewpages extends UGraphicDelegator {

	private final double ymin;
	private final double ymax;
	private final double dy;

	public UGraphicNewpages(UGraphic ug, double ymin, double ymax) {
		this(ug, ymin, ymax, 0);
	}

	private UGraphicNewpages(UGraphic ug, double ymin, double ymax, double dy) {
		super(ug);
		this.ymin = ymin;
		this.ymax = ymax;
		this.dy = dy;
	}

	public void draw(UShape shape) {
		System.err.println("UGraphicNewpages " + shape.getClass());
		if (dy >= ymin && dy < ymax) {
			getUg().draw(shape);
		} else {
			System.err.println("Removing " + shape);
		}

	}

	public UGraphic apply(UChange change) {
		double newdy = dy;
		if (change instanceof UTranslate) {
			newdy += ((UTranslate) change).getDy();
		}
		return new UGraphicNewpages(getUg().apply(change), ymin, ymax, newdy);
	}

}