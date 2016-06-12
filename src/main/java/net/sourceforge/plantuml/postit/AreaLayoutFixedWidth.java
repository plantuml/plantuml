/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 4167 $
 *
 */
package net.sourceforge.plantuml.postit;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;

public class AreaLayoutFixedWidth implements AreaLayout {

	private final double width;

	public AreaLayoutFixedWidth(double width) {
		this.width = width;
	}

	public Map<PostIt, Point2D> getPositions(Collection<PostIt> all, StringBounder stringBounder) {
		double x = 0;
		double y = 0;
		double maxY = 0;
		final Map<PostIt, Point2D> result = new LinkedHashMap<PostIt, Point2D>();

		for (PostIt p : all) {
			final Dimension2D dim = p.getDimension(stringBounder);
			if (x + dim.getWidth() > width) {
				x = 0;
				y = maxY;
			}
			result.put(p, new Point2D.Double(x, y));
			x += dim.getWidth();
			maxY = Math.max(maxY, y + dim.getHeight());
		}

		return Collections.unmodifiableMap(result);
	}

}
