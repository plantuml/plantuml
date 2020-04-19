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
package net.sourceforge.plantuml.bpm;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class ConnectorPuzzleEmpty extends AbstractConnectorPuzzle implements Placeable, TextBlock, ConnectorPuzzle {

	public static ConnectorPuzzleEmpty get(String value) {
		final ConnectorPuzzleEmpty result = new ConnectorPuzzleEmpty();
		for (Where w : Where.values()) {
			if (value.contains(w.toShortString())) {
				result.append(w);
			}
		}
		return result;
	}

	public boolean checkDirections(String directions) {
		return connections().equals(get(directions).connections());
	}

	@Override
	public String toString() {
		if (connections().size() == 0) {
			return "NONE";

		}
		return connections().toString();
	}

	public Dimension2D getDimension(StringBounder stringBounder, ISkinParam skinParam) {
		return new Dimension2DDouble(20, 20);
	}

	public TextBlock toTextBlock(ISkinParam skinParam) {
		return this;
	}

	public String getId() {
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug) {
		// System.err.println("DRAWING " + toString());
		ug = ug.apply(HColorUtils.BLUE);
		for (Where w : Where.values()) {
			if (have(w)) {
				drawLine(ug, w);
			}
		}

	}

	private void drawLine(UGraphic ug, Where w) {
		if (w == Where.WEST) {
			ug.apply(UTranslate.dy(10)).draw(ULine.hline(10));
		}
		if (w == Where.EAST) {
			ug.apply(new UTranslate(10, 10)).draw(ULine.hline(10));
		}
		if (w == Where.NORTH) {
			ug.apply(UTranslate.dx(10)).draw(ULine.vline(10));
		}
		if (w == Where.SOUTH) {
			ug.apply(new UTranslate(10, 10)).draw(ULine.vline(10));
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(20, 20);
	}
	
	public MinMax getMinMax(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}


	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return null;
	}

}
