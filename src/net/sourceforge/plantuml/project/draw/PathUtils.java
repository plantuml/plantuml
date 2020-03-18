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
package net.sourceforge.plantuml.project.draw;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UPath;

public class PathUtils {

	private final static double round = 4;

	public static UPath UtoRight(double width, double height) {
		final UPath result = new UPath();
		result.moveTo(0, 0);
		result.lineTo(width - round, 0);
		result.arcTo(new Point2D.Double(width, round), round, 0, 1);
		result.lineTo(width, height - round);
		result.arcTo(new Point2D.Double(width - round, height), round, 0, 1);
		result.lineTo(0, height);
		return result;
	}

	public static UPath UtoLeft(double width, double height) {
		final UPath result = new UPath();
		result.moveTo(width, height);
		result.lineTo(round, height);
		result.arcTo(new Point2D.Double(0, height - round), round, 0, 1);
		result.lineTo(0, round);
		result.arcTo(new Point2D.Double(round, 0), round, 0, 1);
		result.lineTo(width, 0);
		return result;
	}

}
