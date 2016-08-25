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
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.creole;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class AtomWithMargin implements Atom {

	private final double marginY1;
	private final double marginY2;
	private final Atom atom;

	public AtomWithMargin(Atom atom, double marginY1, double marginY2) {
		this.atom = atom;
		this.marginY1 = marginY1;
		this.marginY2 = marginY2;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return Dimension2DDouble.delta(atom.calculateDimension(stringBounder), 0, marginY1 + marginY2);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return atom.getStartingAltitude(stringBounder);
	}

	public void drawU(UGraphic ug) {
		atom.drawU(ug.apply(new UTranslate(0, marginY1)));
	}
	
}
