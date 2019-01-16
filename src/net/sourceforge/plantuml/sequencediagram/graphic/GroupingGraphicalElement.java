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
package net.sourceforge.plantuml.sequencediagram.graphic;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupableList;

abstract class GroupingGraphicalElement extends GraphicalElement {

	private final InGroupableList inGroupableList;

	public GroupingGraphicalElement(double currentY, InGroupableList inGroupableList) {
		super(currentY);
		this.inGroupableList = inGroupableList;
		if (inGroupableList == null) {
			throw new IllegalArgumentException();
		}
	}

	final public double getActualWidth(StringBounder stringBounder) {
		return Math.max(getPreferredWidth(stringBounder),
				inGroupableList.getMaxX(stringBounder) - inGroupableList.getMinX(stringBounder) + 2
						* InGroupableList.MARGIN10);
	}

	@Override
	final public double getStartingX(StringBounder stringBounder) {
		return inGroupableList.getMinX(stringBounder) - InGroupableList.MARGIN10;
	}

	protected final InGroupableList getInGroupableList() {
		return inGroupableList;
	}

}
