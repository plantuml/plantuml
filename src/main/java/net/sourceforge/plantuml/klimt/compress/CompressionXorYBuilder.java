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
package net.sourceforge.plantuml.klimt.compress;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;

public class CompressionXorYBuilder implements TextBlock {
	private final CompressionMode mode;
	private final TextBlock textBlock;

	private PiecewiseAffineTransform cachedAffine;
	private MinMax cachedMinMax;

	public static TextBlock build(CompressionMode mode, TextBlock textBlock) {
		return new CompressionXorYBuilder(mode, textBlock);
	}

	private CompressionXorYBuilder(CompressionMode mode, TextBlock textBlock) {
		this.mode = mode;
		this.textBlock = textBlock;
	}

	private PiecewiseAffineTransform getAffineTransform(StringBounder stringBounder) {
		if (cachedAffine == null) {
			final SlotFinder slotFinder = SlotFinder.create(mode, stringBounder);
			textBlock.drawU(slotFinder);
			final SlotSet slotSet = slotFinder.getSlotSet().reverse().smaller(5.0);
			cachedAffine = new CompressionTransform(slotSet);
		}
		return cachedAffine;
	}

	public void drawU(final UGraphic ug) {
		final PiecewiseAffineTransform affine = getAffineTransform(ug.getStringBounder());
		textBlock.drawU(UGraphicCompressOnXorY.create(mode, ug, affine));
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final PiecewiseAffineTransform affine = getAffineTransform(stringBounder);
		final XDimension2D dim = textBlock.calculateDimension(stringBounder);
		if (mode == CompressionMode.ON_X)
			return new XDimension2D(affine.transform(dim.getWidth()), dim.getHeight());
		else
			return new XDimension2D(dim.getWidth(), affine.transform(dim.getHeight()));
	}

	@Override
	public MinMax getMinMax(StringBounder stringBounder) {
		if (cachedMinMax == null)
			cachedMinMax = TextBlockUtils.getMinMax(this, stringBounder, false);

		return cachedMinMax;
	}

	public HColor getBackcolor() {
		return null;
	}

}