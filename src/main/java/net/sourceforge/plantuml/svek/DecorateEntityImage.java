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
package net.sourceforge.plantuml.svek;

import java.util.Objects;

import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;

public class DecorateEntityImage extends AbstractTextBlock {

	private final TextBlock original;

	private final UGroup group1;
	private final HorizontalAlignment horizontal1;
	private final TextBlock text1;

	private final UGroup group2;
	private final HorizontalAlignment horizontal2;
	private final TextBlock text2;

	private double deltaX;
	private double deltaY;

	public static TextBlock addTop(UGroup group, TextBlock original, TextBlock text, HorizontalAlignment horizontal) {
		return new DecorateEntityImage(original, group, text, horizontal, null, null, null);
	}

	public static TextBlock addBottom(UGroup group, TextBlock original, TextBlock text,
			HorizontalAlignment horizontal) {
		return new DecorateEntityImage(original, null, null, null, group, text, horizontal);
	}

	public static TextBlock add(UGroup group, TextBlock original, TextBlock text, HorizontalAlignment horizontal,
			VerticalAlignment verticalAlignment) {
		if (verticalAlignment == VerticalAlignment.TOP)
			return addTop(group, original, text, horizontal);

		return addBottom(group, original, text, horizontal);
	}

	public static TextBlock addTopAndBottom(TextBlock original, UGroup group1, TextBlock text1,
			HorizontalAlignment horizontal1, UGroup group2, TextBlock text2, HorizontalAlignment horizontal2) {
		return new DecorateEntityImage(original, group1, text1, horizontal1, group2, text2, horizontal2);
	}

	private DecorateEntityImage(TextBlock original, UGroup group1, TextBlock text1, HorizontalAlignment horizontal1,
			UGroup group2, TextBlock text2, HorizontalAlignment horizontal2) {
		this.original = Objects.requireNonNull(original);

		this.group1 = group1;
		this.horizontal1 = horizontal1;
		this.text1 = text1;

		this.group2 = group2;
		this.horizontal2 = horizontal2;
		this.text2 = text2;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimOriginal = original.calculateDimension(stringBounder);
		final XDimension2D dimText1 = getTextDim(text1, stringBounder);
		final XDimension2D dimText2 = getTextDim(text2, stringBounder);
		final XDimension2D dimTotal = calculateDimension(stringBounder);

		final double yImage = dimText1.getHeight();
		final double yText2 = yImage + dimOriginal.getHeight();

		final double xImage = (dimTotal.getWidth() - dimOriginal.getWidth()) / 2;

		if (text1 != null) {
			final double xText1 = getTextX(dimText1, dimTotal, horizontal1);
			if (group1 != null)
				ug.startGroup(group1);
			text1.drawU(ug.apply(UTranslate.dx(xText1)));
			if (group1 != null)
				ug.closeGroup();

		}
		original.drawU(ug.apply(new UTranslate(xImage, yImage)));
		deltaX = xImage;
		deltaY = yImage;
		if (text2 != null) {
			final double xText2 = getTextX(dimText2, dimTotal, horizontal2);
			if (group2 != null)
				ug.startGroup(group2);
			text2.drawU(ug.apply(new UTranslate(xText2, yText2)));
			if (group2 != null)
				ug.closeGroup();
		}
	}

	private XDimension2D getTextDim(TextBlock text, StringBounder stringBounder) {
		if (text == null)
			return new XDimension2D(0, 0);

		return text.calculateDimension(stringBounder);
	}

	private double getTextX(final XDimension2D dimText, final XDimension2D dimTotal, HorizontalAlignment h) {
		if (h == HorizontalAlignment.CENTER)
			return (dimTotal.getWidth() - dimText.getWidth()) / 2;
		else if (h == HorizontalAlignment.LEFT)
			return 0;
		else if (h == HorizontalAlignment.RIGHT)
			return dimTotal.getWidth() - dimText.getWidth();
		else
			throw new IllegalStateException();

	}

	public HColor getBackcolor() {
		return original.getBackcolor();
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D dimOriginal = original.calculateDimension(stringBounder);
		final XDimension2D dim1 = getTextDim(text1, stringBounder);
		final XDimension2D dim2 = getTextDim(text2, stringBounder);
		final XDimension2D dimText = dim1.mergeTB(dim2);
		return dimOriginal.mergeTB(dimText);
	}

	@Override
	public MinMax getMinMax(StringBounder stringBounder) {
		return MinMax.fromDim(calculateDimension(stringBounder));
	}

	public final double getDeltaX() {
		if (original instanceof DecorateEntityImage)
			return deltaX + ((DecorateEntityImage) original).deltaX;

		return deltaX;
	}

	public final double getDeltaY() {
		if (original instanceof DecorateEntityImage)
			return deltaY + ((DecorateEntityImage) original).deltaY;

		return deltaY;
	}

}