/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.klimt.creole;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.klimt.creole.atom.Atom;
import net.sourceforge.plantuml.klimt.creole.legacy.AtomText;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class Sea {

	private double currentX;
	private final Map<Atom, Position> positions = new LinkedHashMap<Atom, Position>();
	private final StringBounder stringBounder;

	public Sea(StringBounder stringBounder) {
		this.stringBounder = Objects.requireNonNull(stringBounder);
	}

	public void add(Atom atom) {
		final XDimension2D dim = atom.calculateDimension(stringBounder);
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

	private AtomText findFirstAtomText() {
		for (Atom atom : positions.keySet()) {
			if (atom instanceof AtomText) {
				AtomText atomText = (AtomText) atom;
				String text = atomText.getText();
				if (text.trim().isEmpty()) {
					continue;
				}
				return (AtomText) atom;
			}
		}
		return null;
	}

	public void doAlignTikz() {
		// #1628, make non-text vertical center with text
		AtomText firstTextAtom = findFirstAtomText();
		if (firstTextAtom == null) {
			return;
		}
		Position firstTextPosition = positions.get(firstTextAtom);
		double firstTextHeight = firstTextAtom.getFontHeight(stringBounder);
		for (Map.Entry<Atom, Position> entry : new LinkedHashMap<>(positions).entrySet()) {
			final Atom atom = entry.getKey();
			if (atom instanceof AtomText) {
				continue;
			}
			Position position = entry.getValue();
			double targetY = firstTextPosition.getMinY() - (position.getHeight() - firstTextHeight) / 2;
			double delta = targetY - position.getMinY();
			if (delta != 0.0) {
				positions.put(atom, position.translateY(delta));
			}
		}
	}

	public void doAlignTikzBaseline() {
		// #1606, make text the same baseline
		AtomText firstTextAtom = findFirstAtomText();
		if (firstTextAtom == null) {
			return;
		}
		double firstTextHeight = firstTextAtom.getFontHeight(stringBounder);
		for (Map.Entry<Atom, Position> entry : new LinkedHashMap<>(positions).entrySet()) {
			final Atom atom = entry.getKey();
			if (!(atom instanceof AtomText)) {
				continue;
			}
			double delta = firstTextHeight - ((AtomText) atom).getFontHeight(stringBounder);
			if (delta != 0.0) {
				positions.put(atom, entry.getValue().translateY(delta));
			}
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
		Atom atom = null;
		double result = -Double.MAX_VALUE;
		for (Map.Entry<Atom, Position> entry : positions.entrySet()) {
			Position pos = entry.getValue();
			if (result < pos.getMaxY()) {
				atom = entry.getKey();
				result = pos.getMaxY();
			}
		}
		if (!stringBounder.matchesProperty("TIKZ")) {
			return result;
		}
		// For TKIZ, make sure the strip has at least 1pt
		if (atom instanceof AtomText) {
			// the delta in AtomText should be larger than 1 already
			AtomText atomText = (AtomText) atom;
			UFont font = atomText.getFontConfiguration().getFont();
			String text = atomText.getText();
			double height = stringBounder.calculateDimension(font, text).getHeight();
			double delta = result - height;
			return result + Math.max(1 - delta, 0);
		} else {
			return result + 1;
		}
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
