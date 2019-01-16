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
package net.sourceforge.plantuml.jungle;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.List;

public class GTileGeometry extends Dimension2D {

	private final double width;
	private final double height;
	private final List<Double> westPositions;

	public GTileGeometry(Dimension2D dim, List<Double> westPositions) {
		this(dim.getWidth(), dim.getHeight(), westPositions);
	}

	@Override
	public void setSize(double width, double height) {
		throw new UnsupportedOperationException();
	}

	public GTileGeometry(double width, double height, List<Double> westPositions) {
		this.width = width;
		this.height = height;
		this.westPositions = westPositions;
	}

	@Override
	public final double getWidth() {
		return width;
	}

	@Override
	public final double getHeight() {
		return height;
	}

	public List<Double> getWestPositions() {
		return Collections.unmodifiableList(westPositions);
	}
}
