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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;

public class PlacementStrategyX1Y2Y3 extends AbstractPlacementStrategy {

	public PlacementStrategyX1Y2Y3(StringBounder stringBounder) {
		super(stringBounder);
	}

	public Map<TextBlock, Point2D> getPositions(double width, double height) {
		final Dimension2D first = getDimensions().values().iterator().next();

		final double maxWidthButFirst = getMaxWidth(butFirst());
		final double sumHeightButFirst = getSumHeight(butFirst());

		final double space = (width - first.getWidth() - maxWidthButFirst) / 3;

		final Map<TextBlock, Point2D> result = new LinkedHashMap<TextBlock, Point2D>();
		// double x = space * 2;

		final Iterator<Map.Entry<TextBlock, Dimension2D>> it = getDimensions().entrySet().iterator();
		final Map.Entry<TextBlock, Dimension2D> ent = it.next();
		double y = (height - ent.getValue().getHeight()) / 2;
		result.put(ent.getKey(), new Point2D.Double(space, y));

		// x += ent.getValue().getWidth() + space;

		y = (height - sumHeightButFirst) / 2;
		while (it.hasNext()) {
			final Map.Entry<TextBlock, Dimension2D> ent2 = it.next();
			final TextBlock textBlock = ent2.getKey();
			final Dimension2D dim = getDimensions().get(textBlock);
			final double x = 2 * space + first.getWidth() + (maxWidthButFirst - dim.getWidth()) / 2;
			result.put(textBlock, new Point2D.Double(x, y));
			y += ent2.getValue().getHeight();
		}
		return result;
	}

	private Iterator<Dimension2D> butFirst() {
		final Iterator<Dimension2D> iterator = getDimensions().values().iterator();
		iterator.next();
		return iterator;
	}

}
