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
package net.sourceforge.plantuml.klimt.shape;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

class TextBlockMinWidth extends AbstractTextBlock implements TextBlock {
    // ::remove file when __HAXE__

	private final TextBlock textBlock;
	private final double minWidth;
	private final HorizontalAlignment horizontalAlignment;

	TextBlockMinWidth(TextBlock textBlock, double minWidth, HorizontalAlignment horizontalAlignment) {
		this.textBlock = textBlock;
		this.minWidth = minWidth;
		this.horizontalAlignment = horizontalAlignment;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D dim = textBlock.calculateDimension(stringBounder);
		return dim.atLeast(minWidth, 0);
	}

	public void drawU(UGraphic ug) {
		if (horizontalAlignment == HorizontalAlignment.LEFT) {
			textBlock.drawU(ug);
		} else if (horizontalAlignment == HorizontalAlignment.CENTER) {
			final XDimension2D dimText = textBlock.calculateDimension(ug.getStringBounder());
			final XDimension2D dimFull = calculateDimension(ug.getStringBounder());
			final double diffx = dimFull.getWidth() - dimText.getWidth();
			textBlock.drawU(ug.apply(UTranslate.dx(diffx / 2)));
		} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
			final XDimension2D dimText = textBlock.calculateDimension(ug.getStringBounder());
			final XDimension2D dimFull = calculateDimension(ug.getStringBounder());
			final double diffx = dimFull.getWidth() - dimText.getWidth();
			textBlock.drawU(ug.apply(UTranslate.dx(diffx)));
		} else {
			throw new UnsupportedOperationException();
		}
	}
}