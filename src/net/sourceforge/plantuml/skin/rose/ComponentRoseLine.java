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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 19945 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.AbstractComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentRoseLine extends AbstractComponent {

	private final HtmlColor color;
	private final boolean continueLine;
	private final UStroke stroke;

	public ComponentRoseLine(HtmlColor color, boolean continueLine, UStroke stroke) {
		this.color = color;
		this.continueLine = continueLine;
		this.stroke = stroke;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		ug = ug.apply(new UChangeColor(color));
		if (continueLine) {
			ug = ug.apply(new UStroke());
		} else {
			ug = ArrowConfiguration.stroke(ug, 5, 5, stroke.getThickness());
		}
		final int x = (int) (dimensionToUse.getWidth() / 2);
		ug.apply(new UTranslate(x, 0)).draw(new ULine(0, dimensionToUse.getHeight()));
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return 20;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return 1;
	}

}
