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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class TextBlockGeneric extends AbstractTextBlock implements TextBlock {

	private final TextBlock textBlock;
	private final HColor background;
	private final HColor border;

	public TextBlockGeneric(TextBlock textBlock, HColor background, HColor border) {
		this.textBlock = textBlock;
		this.border = border;
		this.background = background;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		return dim;
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(background.bg());
		ug = ug.apply(border);
		final Dimension2D dim = calculateDimension(ug.getStringBounder());
		ug.apply(new UStroke(2, 2, 1)).draw(new URectangle(dim.getWidth(), dim.getHeight()));
		
		textBlock.drawU(ug);
	}

}