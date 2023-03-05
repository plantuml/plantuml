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
package net.sourceforge.plantuml.klimt;

import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.utils.Direction;

public abstract class Arrows {
	// ::remove file when __HAXE__

	public abstract UPolygon asToUp();

	public abstract UPolygon asToDown();

	public abstract UPolygon asToRight();

	public abstract UPolygon asToLeft();

	public final UPolygon asTo(Direction direction) {
		if (direction == Direction.UP)
			return asToUp();

		if (direction == Direction.DOWN)
			return asToDown();

		if (direction == Direction.LEFT)
			return asToLeft();

		if (direction == Direction.RIGHT)
			return asToRight();

		throw new IllegalArgumentException();
	}

}
