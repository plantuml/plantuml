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
package net.sourceforge.plantuml.creole.atom;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class AtomWithMargin extends AbstractAtom implements Atom {

	private final double marginY1;
	private final double marginY2;
	private final Atom atom;

	public AtomWithMargin(Atom atom, double marginY1, double marginY2) {
		this.atom = atom;
		this.marginY1 = marginY1;
		this.marginY2 = marginY2;
	}

	@Override
	public List<Atom> splitInTwo(StringBounder stringBounder, double width) {
		final List<Atom> result = new ArrayList<Atom>();
		final List<Atom> list = atom.splitInTwo(stringBounder, width);
		for (Atom a : list) {
			double y1 = marginY1;
			double y2 = marginY2;
			if (list.size() == 2 && result.size() == 0) {
				y2 = 0;
			}
			if (list.size() == 2 && result.size() == 1) {
				y1 = 0;
			}
			result.add(new AtomWithMargin(a, y1, y2));
		}
		return Collections.unmodifiableList(result);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return Dimension2DDouble.delta(atom.calculateDimension(stringBounder), 0, marginY1 + marginY2);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return atom.getStartingAltitude(stringBounder);
	}

	public void drawU(UGraphic ug) {
		atom.drawU(ug.apply(UTranslate.dy(marginY1)));
	}

}
