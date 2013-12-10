/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile;

import net.sourceforge.plantuml.ugraphic.UPolygon;

public class Diamond {

	final static public double diamondHalfSize = 12;

	public static UPolygon asPolygon(boolean shadowing) {
		final UPolygon diams = new UPolygon();

		diams.addPoint(diamondHalfSize, 0);
		diams.addPoint(diamondHalfSize * 2, diamondHalfSize);
		diams.addPoint(diamondHalfSize, diamondHalfSize * 2);
		diams.addPoint(0, diamondHalfSize);
		diams.addPoint(diamondHalfSize, 0);

		if (shadowing) {
			diams.setDeltaShadow(3);
		}

		return diams;
	}

	public static UPolygon asPolygon(boolean shadowing, double width, double height) {
		final UPolygon diams = new UPolygon();

		diams.addPoint(diamondHalfSize, 0);
		diams.addPoint(width - diamondHalfSize, 0);
		diams.addPoint(width, height / 2);
		diams.addPoint(width - diamondHalfSize, height);
		diams.addPoint(diamondHalfSize, height);
		diams.addPoint(0, height / 2);
		diams.addPoint(diamondHalfSize, 0);

		if (shadowing) {
			diams.setDeltaShadow(3);
		}

		return diams;
	}

	public static UPolygon asPolygonFoo1(boolean shadowing, double width, double height) {
		final UPolygon diams = new UPolygon();

		diams.addPoint(width / 2, 0);
		diams.addPoint(width, height / 2);
		diams.addPoint(width / 2, height);
		diams.addPoint(0, height / 2);

		if (shadowing) {
			diams.setDeltaShadow(3);
		}

		return diams;
	}

}
