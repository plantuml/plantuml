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
package net.sourceforge.plantuml.ugraphic.comp;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class TextBlockCompressedOnXorY extends AbstractTextBlock implements TextBlock {

	private final TextBlock textBlock;
	private final CompressionMode mode;

	public TextBlockCompressedOnXorY(CompressionMode mode, TextBlock textBlock) {
		this.textBlock = textBlock;
		this.mode = mode;
	}

	public void drawU(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final CompressionTransform compressionTransform = getCompressionTransform(stringBounder);
		textBlock.drawU(new UGraphicCompressOnXorY(mode, ug, compressionTransform));
	}

	private MinMax cachedMinMax;

	@Override
	public MinMax getMinMax(StringBounder stringBounder) {
		if (cachedMinMax == null) {
			cachedMinMax = TextBlockUtils.getMinMax(this, stringBounder);
		}
		return cachedMinMax;
	}

	private CompressionTransform cachedCompressionTransform;

	private CompressionTransform getCompressionTransform(final StringBounder stringBounder) {
		if (cachedCompressionTransform == null) {
			cachedCompressionTransform = getCompressionTransformSlow(stringBounder);
		}
		return cachedCompressionTransform;
	}

	private CompressionTransform getCompressionTransformSlow(final StringBounder stringBounder) {
		final SlotFinder slotFinder = new SlotFinder(mode, stringBounder);
		textBlock.drawU(slotFinder);
		final SlotSet ysSlotSet = slotFinder.getSlotSet().reverse().smaller(5.0);
		final CompressionTransform compressionTransform = new CompressionTransform(ysSlotSet);
		return compressionTransform;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final CompressionTransform compressionTransform = getCompressionTransform(stringBounder);
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		if (mode == CompressionMode.ON_X) {
			return new Dimension2DDouble(compressionTransform.transform(dim.getWidth()), dim.getHeight());
		} else {
			return new Dimension2DDouble(dim.getWidth(), compressionTransform.transform(dim.getHeight()));
		}
	}
}