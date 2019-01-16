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
package net.sourceforge.plantuml.graph2;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class MagicPointsFactory {

	private MagicPointsFactory() {

	}

	public static List<Point2D.Double> get(Rectangle2D.Double rect) {
		final List<Point2D.Double> result = new ArrayList<Point2D.Double>();
		result.add(new Point2D.Double(rect.x - rect.width, rect.y - rect.height));
		result.add(new Point2D.Double(rect.x, rect.y - rect.height));
		result.add(new Point2D.Double(rect.x + rect.width, rect.y - rect.height));
		result.add(new Point2D.Double(rect.x + 2 * rect.width, rect.y - rect.height));

		result.add(new Point2D.Double(rect.x - rect.width, rect.y));
		result.add(new Point2D.Double(rect.x + 2 * rect.width, rect.y));

		result.add(new Point2D.Double(rect.x - rect.width, rect.y + rect.height));
		result.add(new Point2D.Double(rect.x + 2 * rect.width, rect.y + rect.height));

		result.add(new Point2D.Double(rect.x - rect.width, rect.y + 2 * rect.height));
		result.add(new Point2D.Double(rect.x, rect.y + 2 * rect.height));
		result.add(new Point2D.Double(rect.x + rect.width, rect.y + 2 * rect.height));
		result.add(new Point2D.Double(rect.x + 2 * rect.width, rect.y + 2 * rect.height));
		return result;
	}

	public static List<Point2D.Double> get(Point2D.Double p1, Point2D.Double p2) {
		final List<Point2D.Double> result = new ArrayList<Point2D.Double>();
		result.add(new Point2D.Double(p1.x, p2.y));
		result.add(new Point2D.Double(p2.x, p1.y));
		return result;
	}
}
