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
package net.sourceforge.plantuml.project.draw;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;

public class PathUtils {

	public static UPath UtoRight(double width, double height, double round) {
		final UPath result = UPath.none();
		if (round == 0) {
			result.moveTo(0, 0);
			result.lineTo(width, 0);
			result.lineTo(width, height);
			result.lineTo(0, height);
		} else {
			final double halfRound = round / 2;
			result.moveTo(0, 0);
			result.lineTo(width - halfRound, 0);
			result.arcTo(new XPoint2D(width, halfRound), halfRound, 0, 1);
			result.lineTo(width, height - halfRound);
			result.arcTo(new XPoint2D(width - halfRound, height), halfRound, 0, 1);
			result.lineTo(0, height);
		}
		return result;
	}

	public static UPath UtoLeft(double width, double height, double round) {
		final UPath result = UPath.none();
		if (round == 0) {
			result.moveTo(width, height);
			result.lineTo(0, height);
			result.lineTo(0, 0);
			result.lineTo(width, 0);
		} else {
			final double halfRound = round / 2;
			result.moveTo(width, height);
			result.lineTo(halfRound, height);
			result.arcTo(new XPoint2D(0, height - halfRound), halfRound, 0, 1);
			result.lineTo(0, halfRound);
			result.arcTo(new XPoint2D(halfRound, 0), halfRound, 0, 1);
			result.lineTo(width, 0);
		}
		return result;
	}

}
