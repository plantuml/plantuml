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
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class TextBlockWithUrl implements TextBlock {

	private final TextBlock block;
	private final Url url;

	public static TextBlock withUrl(TextBlock block, Url url) {
		if (url == null) {
			return block;
		}
		return new TextBlockWithUrl(block, url);

	}

	private TextBlockWithUrl(TextBlock block, Url url) {
		this.block = block;
		this.url = url;
	}

	public void drawU(UGraphic ug) {
		ug.startUrl(url);
		block.drawU(ug);
		ug.closeAction();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return block.calculateDimension(stringBounder);
	}
	
	public MinMax getMinMax(StringBounder stringBounder) {
		return block.getMinMax(stringBounder);
	}

	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return block.getInnerPosition(member, stringBounder, strategy);
	}

}