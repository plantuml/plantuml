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
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class TextBlockRecentred extends AbstractTextBlock {
    // ::remove file when __HAXE__

	private final TextBlock textBlock;

	public TextBlockRecentred(TextBlock textBlock) {
		this.textBlock = textBlock;
	}

	public void drawU(final UGraphic ug) {
		StringBounder stringBounder = ug.getStringBounder();
		final MinMax minMax = getMinMax(stringBounder);
		textBlock.drawU(ug.apply(new UTranslate(-minMax.getMinX(), -minMax.getMinY())));
	}

	// private MinMax cachedMinMax;

	public MinMax getMinMax(StringBounder stringBounder) {
		return textBlock.getMinMax(stringBounder);
		// if (cachedMinMax == null) {
		// cachedMinMax = getMinMaxSlow(stringBounder);
		// }
		// // assert
		// cachedMinMax.toString().equals(getMinMaxSlow(stringBounder).toString());
		// return cachedMinMax;
	}

	// private MinMax getMinMaxSlow(StringBounder stringBounder) {
	// final MinMax result = TextBlockUtils.getMinMax(textBlock, stringBounder);
	// return result;
	// }

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final MinMax minMax = getMinMax(stringBounder);
		return minMax.getDimension();
	}

	public HColor getBackcolor() {
		return textBlock.getBackcolor();
	}

}