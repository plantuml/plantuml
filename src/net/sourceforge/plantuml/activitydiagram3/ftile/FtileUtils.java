/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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

import java.util.Collection;

public class FtileUtils {

	public static Ftile addConnection(Ftile ftile, Connection connection) {
		return new FtileWithConnection(ftile, connection);
	}

	public static Ftile addConnection(Ftile ftile, Collection<Connection> connections) {
		return new FtileWithConnection(ftile, connections);
	}

	public static Ftile withSwimlaneOut(Ftile ftile, Swimlane out) {
		return new FtileWithSwimlanes(ftile, ftile.getSwimlaneIn(), out);
	}

	public static Ftile addBottom(Ftile ftile, double marginBottom) {
		return new FtileMargedVertically(ftile, 0, marginBottom);
	}

	public static Ftile addVerticalMargin(Ftile ftile, double marginTop, double marginBottom) {
		if (marginTop == 0 && marginBottom == 0) {
			return ftile;
		}
		return new FtileMargedVertically(ftile, marginTop, marginBottom);
	}

	public static Ftile addHorizontalMargin(Ftile ftile, double margin1, double margin2) {
		if (margin1 == 0 && margin2 == 0) {
			return ftile;
		}
		return new FtileMarged(ftile, margin1, margin2);
	}

	public static Ftile addHorizontalMargin(Ftile ftile, double margin) {
		if (margin == 0) {
			return ftile;
		}
		return new FtileMarged(ftile, margin, margin);
	}

	// public static Ftile addHorizontalMargin(Ftile ftile, double margin) {
	// return new FtileMarged(ftile, margin);
	// }

	private static Ftile neverNull(Ftile ftile) {
		if (ftile == null) {
			return new FtileEmpty(false);
		}
		return ftile;
	}

}
