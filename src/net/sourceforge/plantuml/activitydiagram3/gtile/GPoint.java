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
package net.sourceforge.plantuml.activitydiagram3.gtile;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GPoint {

	public static final String NORTH_HOOK = "NORTH_HOOK";
	public static final String SOUTH_HOOK = "SOUTH_HOOK";
	public static final String WEST_HOOK = "WEST_HOOK";
	public static final String EAST_HOOK = "EAST_HOOK";

	public static final String NORTH_BORDER = "NORTH_BORDER";
	public static final String SOUTH_BORDER = "SOUTH_BORDER";
	public static final String WEST_BORDER = "WEST_BORDER";
	public static final String EAST_BORDER = "EAST_BORDER";

	private final Gtile gtile;
	private final String name;
	private final LinkRendering linkRendering;

	public GPoint(Gtile gtile, String name, LinkRendering linkRendering) {
		this.gtile = gtile;
		this.name = name;
		this.linkRendering = linkRendering;
	}

	public GPoint(Gtile gtile, String name) {
		this(gtile, name, LinkRendering.none());
	}

	@Override
	public String toString() {
		return gtile + "@" + name;
	}

	public Gtile getGtile() {
		return gtile;
	}

	public String getName() {
		return name;
	}

	public UTranslate getCoord() {
		return gtile.getCoord(name);
	}

	public Point2D getPoint2D() {
		return getCoord().getPosition();
	}

	public LinkRendering getLinkRendering() {
		return linkRendering;
	}

	public boolean match(Swimlane swimlane) {
		final Swimlane tmp = gtile.getSwimlane(name);
		return tmp == swimlane;
	}

	public Swimlane getSwimlane() {
		final Swimlane result = gtile.getSwimlane(name);
		if (result == null) {
			throw new IllegalStateException(name + " " + gtile.getClass().toString() + " " + gtile);
		}
		return result;
	}

}
