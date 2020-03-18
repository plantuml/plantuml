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
package net.sourceforge.plantuml.creole;

import java.awt.geom.Dimension2D;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.creole.atom.Atom;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.MinMax;

public class Sea {

	private double currentX;
	private final Map<Atom, Position> positions = new HashMap<Atom, Position>();
	private final StringBounder stringBounder;

	public Sea(StringBounder stringBounder) {
		this.stringBounder = stringBounder;
	}

	public void add(Atom atom) {
		final Dimension2D dim = atom.calculateDimension(stringBounder);
		final double y = 0;
		final Position position = new Position(currentX, y, dim);
		positions.put(atom, position);
		currentX += dim.getWidth();
	}

	public Position getPosition(Atom atom) {
		return positions.get(atom);
	}

	public void doAlign() {
		for (Map.Entry<Atom, Position> ent : new HashMap<Atom, Position>(positions).entrySet()) {
			final Position pos = ent.getValue();
			final Atom atom = ent.getKey();
			final double height = atom.calculateDimension(stringBounder).getHeight();
			final Position newPos = pos.translateY(-height + atom.getStartingAltitude(stringBounder));
			positions.put(atom, newPos);
		}
	}

	public void translateMinYto(double newValue) {
		final double delta = newValue - getMinY();
		for (Map.Entry<Atom, Position> ent : new HashMap<Atom, Position>(positions).entrySet()) {
			final Position pos = ent.getValue();
			final Atom atom = ent.getKey();
			positions.put(atom, pos.translateY(delta));
		}
	}

	public void exportAllPositions(Map<Atom, Position> destination) {
		destination.putAll(positions);
	}

	public double getMinY() {
		if (positions.size() == 0) {
			throw new IllegalStateException();
		}
		double result = Double.MAX_VALUE;
		for (Position pos : positions.values()) {
			if (result > pos.getMinY()) {
				result = pos.getMinY();
			}
		}
		return result;
	}

	public double getMaxY() {
		if (positions.size() == 0) {
			throw new IllegalStateException();
		}
		double result = -Double.MAX_VALUE;
		for (Position pos : positions.values()) {
			if (result < pos.getMaxY()) {
				result = pos.getMaxY();
			}
		}
		return result;
	}

	public double getHeight() {
		return getMaxY() - getMinY();
	}

	public MinMax update(MinMax minMax) {
		for (Position position : positions.values()) {
			minMax = position.update(minMax);
		}
		return minMax;
	}

	public final double getWidth() {
		return currentX;
	}

}
