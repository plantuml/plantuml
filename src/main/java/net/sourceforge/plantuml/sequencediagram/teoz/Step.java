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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.style.StyleBuilder;

public class Step {
	// ::remove folder when __HAXE__

	private final double value;
	private final boolean destroy;
	private final int indent;
	private final Fashion color;
	private final StyleBuilder styleBuilder;

	public Step(double value, boolean destroy, int indent, Fashion color, StyleBuilder styleBuilder) {
		if (indent < 0)
			throw new IllegalArgumentException();

		this.styleBuilder = styleBuilder;
		this.indent = indent;
		this.color = color;
		this.value = value;
		this.destroy = destroy;
	}

	public double getValue() {
		return value;
	}

	public boolean isDestroy() {
		return destroy;
	}

	public int getIndent() {
		return indent;
	}

	public Fashion getColors() {
		return color;
	}

	public StyleBuilder getStyleBuilder() {
		return styleBuilder;
	}

}
