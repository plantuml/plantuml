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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;

public abstract class AbstractPlacementStrategy implements PlacementStrategy {

	private final StringBounder stringBounder;
	private final Map<TextBlock, Dimension2D> dimensions = new LinkedHashMap<TextBlock, Dimension2D>();

	public AbstractPlacementStrategy(StringBounder stringBounder) {
		this.stringBounder = stringBounder;
	}

	public void add(TextBlock block) {
		this.dimensions.put(block, block.calculateDimension(stringBounder));
	}

	protected Map<TextBlock, Dimension2D> getDimensions() {
		return dimensions;
	}

	protected double getSumWidth() {
		return getSumWidth(dimensions.values().iterator());
	}

	protected double getSumHeight() {
		return getSumHeight(dimensions.values().iterator());
	}

	protected double getMaxHeight() {
		return getMaxHeight(dimensions.values().iterator());
	}

	protected double getMaxWidth() {
		return getMaxWidth(dimensions.values().iterator());
	}

	protected double getSumWidth(Iterator<Dimension2D> it) {
		double result = 0;
		while (it.hasNext()) {
			result += it.next().getWidth();
		}
		return result;
	}

	protected double getSumHeight(Iterator<Dimension2D> it) {
		double result = 0;
		while (it.hasNext()) {
			result += it.next().getHeight();
		}
		return result;
	}

	protected double getMaxWidth(Iterator<Dimension2D> it) {
		double result = 0;
		while (it.hasNext()) {
			result = Math.max(result, it.next().getWidth());
		}
		return result;
	}

	protected double getMaxHeight(Iterator<Dimension2D> it) {
		double result = 0;
		while (it.hasNext()) {
			result = Math.max(result, it.next().getHeight());
		}
		return result;
	}

	protected final StringBounder getStringBounder() {
		return stringBounder;
	}

}
