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
package net.sourceforge.plantuml.klimt.geom;

import net.sourceforge.plantuml.klimt.UClip;

/**
 * How a driver tells the {@code UGraphic} it draws into that a point belongs to what was just
 * drawn, so that the clickable area of whatever {@code Url} is currently open grows to cover it.
 *
 * Only {@code UGraphicG2d} does anything with it, and only because PNG cannot carry a hyperlink:
 * the links of a PNG are emitted beside it as an HTML image map, whose {@code <area coords>}
 * rectangles have to be discovered by watching every shape drawn while a url is open. Every other
 * backend either writes real link markup (svg) or keeps that accounting inside its own graphics
 * object (eps), so none of their drivers report anything.
 *
 * {@code clip} is the clip in force for the shape being reported, and is passed in rather than
 * read from the {@code UGraphic}: a driver is built once and then used by every copy
 * {@code UGraphic#apply} makes, so the instance it was built from is not the one being drawn
 * into, and its clip is not the one that applies. A point outside the clip was not actually
 * drawn and must not enlarge the link area.
 */
public interface EnsureVisible {

	public void ensureVisible(double x, double y, UClip clip);

}
