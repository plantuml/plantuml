/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 3837 $
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

public class PlacementStrategyVisibility extends AbstractPlacementStrategy {

	private final int col2;

	public PlacementStrategyVisibility(StringBounder stringBounder, int col2) {
		super(stringBounder);
		this.col2 = col2;
	}

	public Map<TextBlock, Point2D> getPositions(double width, double height) {
		final Map<TextBlock, Point2D> result = new LinkedHashMap<TextBlock, Point2D>();
		double y = 0;
		for (Iterator<Map.Entry<TextBlock, Dimension2D>> it = getDimensions().entrySet().iterator(); it.hasNext();) {
			Map.Entry<TextBlock, Dimension2D> ent1 = it.next();
			Map.Entry<TextBlock, Dimension2D> ent2 = it.next();
			final double height1 = ent1.getValue().getHeight();
			final double height2 = ent2.getValue().getHeight();
			final double maxHeight = Math.max(height1, height2);
			result.put(ent1.getKey(), new Point2D.Double(0, 2 + y + (maxHeight - height1) / 2));
			result.put(ent2.getKey(), new Point2D.Double(col2, y + (maxHeight - height2) / 2));
			y += maxHeight;
		}
		return result;
	}

}
