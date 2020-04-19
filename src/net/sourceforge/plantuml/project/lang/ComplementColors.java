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
package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class ComplementColors implements Complement {

	private final HColor center;
	private final HColor border;

	public ComplementColors(HColor center, HColor border) {
		this.center = center;
		this.border = border;
	}

	public UGraphic apply(UGraphic ug) {
		if (isOk() == false) {
			throw new IllegalStateException();
		}
		ug = ug.apply(center.bg());
		if (border == null) {
			ug = ug.apply(center);
		} else {
			ug = ug.apply(border);
		}
		return ug;
	}

	public boolean isOk() {
		return center != null;
	}

	public HColor getCenter() {
		return center;
	}
}
