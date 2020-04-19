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
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.AbstractComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class ComponentRoseLine extends AbstractComponent {

	private final HColor color;
	private final boolean continueLine;
	private final UStroke stroke;

	public ComponentRoseLine(Style style, HColor color, boolean continueLine, UStroke stroke, HColorSet set) {
		super(style);
		if (SkinParam.USE_STYLES()) {
			this.color = style.value(PName.LineColor).asColor(set);
		} else {
			this.color = color;
		}
		this.continueLine = continueLine;
		this.stroke = stroke;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		ug = ug.apply(color);
		if (continueLine) {
			ug = ug.apply(new UStroke());
		} else {
			ug = ArrowConfiguration.stroke(ug, 5, 5, stroke.getThickness());
		}
		final int x = (int) (dimensionToUse.getWidth() / 2);
		ug.apply(UTranslate.dx(x)).draw(ULine.vline(dimensionToUse.getHeight()));
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
