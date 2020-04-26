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
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class PiecewiseAffineOnXorYBuilder extends AbstractTextBlock implements TextBlock, TextBlockBackcolored {

	private final TextBlock textBlock;
	private final CompressionMode mode;
	private final PiecewiseAffineTransform piecewiseAffineTransform;

	public static TextBlock build(CompressionMode mode, TextBlock textBlock,
			PiecewiseAffineTransform piecewiseAffineTransform) {
		return new PiecewiseAffineOnXorYBuilder(mode, textBlock, piecewiseAffineTransform);
	}

	private PiecewiseAffineOnXorYBuilder(CompressionMode mode, TextBlock textBlock,
			PiecewiseAffineTransform piecewiseAffineTransform) {
		this.textBlock = textBlock;
		this.mode = mode;
		this.piecewiseAffineTransform = piecewiseAffineTransform;
	}

	public void drawU(final UGraphic ug) {
		textBlock.drawU(new UGraphicCompressOnXorY(mode, ug, piecewiseAffineTransform));
	}

	private MinMax cachedMinMax;

	@Override
	public MinMax getMinMax(StringBounder stringBounder) {
		if (cachedMinMax == null) {
			cachedMinMax = TextBlockUtils.getMinMax(this, stringBounder, false);
		}
		return cachedMinMax;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		if (mode == CompressionMode.ON_X) {
			return new Dimension2DDouble(piecewiseAffineTransform.transform(dim.getWidth()), dim.getHeight());
		} else {
			return new Dimension2DDouble(dim.getWidth(), piecewiseAffineTransform.transform(dim.getHeight()));
		}
	}

	public HColor getBackcolor() {
		return null;
	}

}