/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 11025 $
 *
 */
package net.sourceforge.plantuml.creole;

import java.awt.geom.Dimension2D;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class SheetBlock implements TextBlock, Atom {

	private final Sheet sheet;
	private final UStroke defaultStroke;
	private final Stencil stencil;
	private Map<Stripe, Double> heights;
	private Map<Atom, Position> positions;
	private MinMax minMax;

	public SheetBlock(Sheet sheet, Stencil stencil, UStroke defaultStroke) {
		this.sheet = sheet;
		this.stencil = stencil;
		this.defaultStroke = defaultStroke;
	}

	private void initMap(StringBounder stringBounder) {
		if (positions != null) {
			return;
		}
		positions = new LinkedHashMap<Atom, Position>();
		heights = new LinkedHashMap<Stripe, Double>();
		minMax = MinMax.getEmpty(true);
		double y = 0;
		for (Stripe stripe : sheet) {
			if (stripe.getAtoms().size() == 0) {
				continue;
			}
			final Sea sea = new Sea(stringBounder);
			for (Atom atom : stripe.getAtoms()) {
				sea.add(atom);
			}
			sea.doAlign();
			sea.translateMinYto(y);
			sea.exportAllPositions(positions);
			minMax = sea.update(minMax);
			final double height = sea.getHeight();
			heights.put(stripe, height);
			y += height;
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		initMap(stringBounder);
		return minMax.getDimension();
	}

	public void drawU(UGraphic ug) {
		if (stencil != null) {
			ug = new UGraphicStencil(ug, stencil, defaultStroke);
		}
		for (Stripe stripe : sheet) {
			for (Atom atom : stripe.getAtoms()) {
				final Position position = positions.get(atom);
				atom.drawU(position.translate(ug));
				// position.drawDebug(ug);
			}
		}
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}
}
