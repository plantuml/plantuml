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

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class GraphicalDelayText extends GraphicalElement {

	private final Component compText;

	private final ParticipantBox p1;
	private final ParticipantBox p2;

	public GraphicalDelayText(double startingY, Component compText, ParticipantBox first, ParticipantBox last) {
		super(startingY);
		this.compText = compText;
		this.p1 = first;
		this.p2 = last;
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double x1 = p1.getCenterX(stringBounder);
		final double x2 = p2.getCenterX(stringBounder);
		final double middle = (x1 + x2) / 2;
		final double textWidth = compText.getPreferredWidth(stringBounder);
		ug = ug.apply(new UTranslate(middle - textWidth / 2, getStartingY()));
		// ug.translate(x1, getStartingY());
		final Dimension2D dim = new Dimension2DDouble(textWidth, compText.getPreferredHeight(stringBounder));
		compText.drawU(ug, new Area(dim), context);
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return compText.getPreferredHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return compText.getPreferredWidth(stringBounder);
	}

	@Override
	public double getStartingX(StringBounder stringBounder) {
		return 0;
	}

	public double getEndingY(StringBounder stringBounder) {
		return getStartingY() + compText.getPreferredHeight(stringBounder);
	}

}
