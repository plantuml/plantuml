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

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.openiconic.OpenIcon;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class AtomOpenIcon implements Atom {

	private final OpenIcon openIcon;
	private final FontConfiguration fontConfiguration;
	private final double factor;

	public AtomOpenIcon(OpenIcon openIcon, FontConfiguration fontConfiguration) {
		this.openIcon = openIcon;
		this.fontConfiguration = fontConfiguration;
		this.factor = fontConfiguration.getSize2D() / 12;
	}

	private TextBlock asTextBlock() {
		return TextBlockUtils.withMargin(openIcon.asTextBlock(fontConfiguration.getColor(), factor), 1, 0);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return asTextBlock().calculateDimension(stringBounder);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return -3 * factor;
	}

	public void drawU(UGraphic ug) {
		asTextBlock().drawU(ug);
	}
	
}
