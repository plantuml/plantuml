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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class DecorateEntityImage extends AbstractTextBlock implements TextBlockBackcolored {

	private final TextBlock original;
	private final HorizontalAlignment horizontal1;
	private final TextBlock text1;
	private final HorizontalAlignment horizontal2;
	private final TextBlock text2;

	private double deltaX;
	private double deltaY;

	public static TextBlock addTop(TextBlock original, TextBlock text, HorizontalAlignment horizontal) {
		return new DecorateEntityImage(original, text, horizontal, null, null);
	}

	public static TextBlock addBottom(TextBlock original, TextBlock text, HorizontalAlignment horizontal) {
		return new DecorateEntityImage(original, null, null, text, horizontal);
	}

	public static TextBlock add(TextBlock original, TextBlock text, HorizontalAlignment horizontal,
			VerticalAlignment verticalAlignment) {
		if (verticalAlignment == VerticalAlignment.TOP) {
			return addTop(original, text, horizontal);
		}
		return addBottom(original, text, horizontal);
	}

	public static TextBlock addTopAndBottom(TextBlock original, TextBlock text1, HorizontalAlignment horizontal1,
			TextBlock text2, HorizontalAlignment horizontal2) {
		return new DecorateEntityImage(original, text1, horizontal1, text2, horizontal2);
	}

	private DecorateEntityImage(TextBlock original, TextBlock text1, HorizontalAlignment horizontal1, TextBlock text2,
			HorizontalAlignment horizontal2) {
		this.original = original;
		this.horizontal1 = horizontal1;
		this.text1 = text1;
		this.horizontal2 = horizontal2;
		this.text2 = text2;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimOriginal = original.calculateDimension(stringBounder);
		final Dimension2D dimText1 = getTextDim(text1, stringBounder);
		final Dimension2D dimText2 = getTextDim(text2, stringBounder);
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		final double yImage = dimText1.getHeight();
		final double yText2 = yImage + dimOriginal.getHeight();

		final double xImage = (dimTotal.getWidth() - dimOriginal.getWidth()) / 2;

		if (text1 != null) {
			final double xText1 = getTextX(dimText1, dimTotal, horizontal1);
			text1.drawU(ug.apply(UTranslate.dx(xText1)));
		}
		original.drawU(ug.apply(new UTranslate(xImage, yImage)));
		deltaX = xImage;
		deltaY = yImage;
		if (text2 != null) {
			final double xText2 = getTextX(dimText2, dimTotal, horizontal2);
			text2.drawU(ug.apply(new UTranslate(xText2, yText2)));
		}
	}

	private Dimension2D getTextDim(TextBlock text, StringBounder stringBounder) {
		if (text == null) {
			return new Dimension2DDouble(0, 0);
		}
		return text.calculateDimension(stringBounder);
	}

	private double getTextX(final Dimension2D dimText, final Dimension2D dimTotal, HorizontalAlignment h) {
		if (h == HorizontalAlignment.CENTER) {
			return (dimTotal.getWidth() - dimText.getWidth()) / 2;
		} else if (h == HorizontalAlignment.LEFT) {
			return 0;
		} else if (h == HorizontalAlignment.RIGHT) {
			return dimTotal.getWidth() - dimText.getWidth();
		} else {
			throw new IllegalStateException();
		}
	}

	public HColor getBackcolor() {
		if (original instanceof TextBlockBackcolored) {
			return ((TextBlockBackcolored) original).getBackcolor();
		}
		throw new UnsupportedOperationException();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dimOriginal = original.calculateDimension(stringBounder);
		final Dimension2D dimText = Dimension2DDouble.mergeTB(getTextDim(text1, stringBounder),
				getTextDim(text2, stringBounder));
		return Dimension2DDouble.mergeTB(dimOriginal, dimText);
	}

	@Override
	public MinMax getMinMax(StringBounder stringBounder) {
		return MinMax.fromDim(calculateDimension(stringBounder));
	}

	public final double getDeltaX() {
		if (original instanceof DecorateEntityImage) {
			return deltaX + ((DecorateEntityImage) original).deltaX;
		}
		return deltaX;
	}

	public final double getDeltaY() {
		if (original instanceof DecorateEntityImage) {
			return deltaY + ((DecorateEntityImage) original).deltaY;
		}
		return deltaY;
	}

}