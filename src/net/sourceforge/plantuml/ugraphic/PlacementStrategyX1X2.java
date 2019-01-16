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
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;

public class PlacementStrategyX1X2 extends AbstractPlacementStrategy {

	public PlacementStrategyX1X2(StringBounder stringBounder) {
		super(stringBounder);
	}

	public Map<TextBlock, Point2D> getPositions(double width, double height) {
		final double usedWidth = getSumWidth();
		//double maxHeight = getMaxHeight();

		final double space = (width - usedWidth) / (getDimensions().size() + 1);
		final Map<TextBlock, Point2D> result = new LinkedHashMap<TextBlock, Point2D>();
		double x = space;
		for (Map.Entry<TextBlock, Dimension2D> ent : getDimensions().entrySet()) {
			final double y = (height - ent.getValue().getHeight()) / 2;
			result.put(ent.getKey(), new Point2D.Double(x, y));
			x += ent.getValue().getWidth() + space;
		}
		return result;
	}

}
