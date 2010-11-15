/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 4696 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupableList;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class GroupingHeader extends GraphicalElement {

	private final Component comp;
	private final double xpos;
	private final InGroupableList inGroupableList;

	public GroupingHeader(double currentY, Component comp, double xpos,
			InGroupableList inGroupableList) {
		super(currentY);
		this.xpos = xpos;
		this.comp = comp;
		this.inGroupableList = inGroupableList;
		if (inGroupableList == null) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString() {
		return super.toString() + " "
				+ (inGroupableList == null ? "no" : inGroupableList.toString());
	}

	@Override
	final public double getPreferredWidth(StringBounder stringBounder) {
		return comp.getPreferredWidth(stringBounder);
	}

	@Override
	final public double getPreferredHeight(StringBounder stringBounder) {
		return comp.getPreferredHeight(stringBounder);
	}

	@Override
	public double getStartingX(StringBounder stringBounder) {
		return xpos;
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		final StringBounder stringBounder = ug.getStringBounder();
		// final double x1 = inGroupableList.getMinX(stringBounder);
		final double x1 = inGroupableList.getBarStart().getCenterX(
				stringBounder);
		final double x2 = inGroupableList.getBarEnd().getCenterX(stringBounder);
		// final double x2 = inGroupableList.getMaxX(stringBounder);
		ug.translate(x1, getStartingY());
		final Dimension2D dim = new Dimension2DDouble(x2 - x1, comp
				.getPreferredHeight(stringBounder));
		comp.drawU(ug, dim, context);
	}

}
