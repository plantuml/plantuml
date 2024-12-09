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
package net.sourceforge.plantuml.sequencediagram.graphic;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.InGroupableList;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;

public class GroupingGraphicalElementElse extends GroupingGraphicalElement implements InGroupable {

	private final Component compElse;
	private final Lazy afterY;
	private final boolean parallel;

	public GroupingGraphicalElementElse(double startingY, Component compElse, InGroupableList inGroupableList,
			boolean parallel, Lazy afterY) {
		super(startingY, inGroupableList);
		this.parallel = parallel;
		this.compElse = compElse;
		this.afterY = afterY;
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double x1 = getInGroupableList().getMinX(stringBounder);
		final double x2 = getInGroupableList().getMaxX(stringBounder) - getInGroupableList().getHack2();
		ug = ug.apply(new UTranslate(x1, getStartingY()));

		final double height = afterY.getNow() - getStartingY();
		if (height <= 0) {
			return;
		}
		final XDimension2D dim = new XDimension2D(x2 - x1, height);

		if (parallel == false) {
			compElse.drawU(ug, new Area(dim), context);
		}
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		if (parallel) {
			return 0;
		}
		return compElse.getPreferredHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return compElse.getPreferredWidth(stringBounder);
	}

	public double getMinX(StringBounder stringBounder) {
		return getStartingX(stringBounder);
	}

	public double getMaxX(StringBounder stringBounder) {
		return getMinX(stringBounder) + getPreferredWidth(stringBounder);
	}

	public String toString(StringBounder stringBounder) {
		return toString();
	}

}
