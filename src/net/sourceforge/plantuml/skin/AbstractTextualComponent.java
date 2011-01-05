/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 5741 $
 *
 */
package net.sourceforge.plantuml.skin;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockUtils;

public abstract class AbstractTextualComponent extends AbstractComponent {

	private final List<? extends CharSequence> strings;

	private final int marginX1;
	private final int marginX2;
	private final int marginY;

	private final TextBlock textBlock;

	private final Font font;
	private final Color fontColor;

	public AbstractTextualComponent(CharSequence label, Color fontColor, Font font,
			HorizontalAlignement horizontalAlignement, int marginX1, int marginX2, int marginY) {
		this(Arrays.asList(label == null ? "" : label), fontColor, font, horizontalAlignement, marginX1, marginX2,
				marginY);
	}

	public AbstractTextualComponent(List<? extends CharSequence> strings, Color fontColor, Font font,
			HorizontalAlignement horizontalAlignement, int marginX1, int marginX2, int marginY) {
		this.font = font;
		this.fontColor = fontColor;
		this.marginX1 = marginX1;
		this.marginX2 = marginX2;
		this.marginY = marginY;
		this.strings = strings;

		if (strings.size() == 1 && strings.get(0).length() == 0) {
			textBlock = new TextBlockEmpty();
		} else {
			textBlock = TextBlockUtils.create(strings, font, fontColor, horizontalAlignement);
		}
	}

	final protected TextBlock getTextBlock() {
		return textBlock;
	}

	final protected double getPureTextWidth(StringBounder stringBounder) {
		final TextBlock textBlock = getTextBlock();
		final Dimension2D size = getSize(stringBounder, textBlock);
		return size.getWidth();
	}

	final public double getTextWidth(StringBounder stringBounder) {
		return getPureTextWidth(stringBounder) + marginX1 + marginX2;
	}

	// For cache
	private Dimension2D size;

	private Dimension2D getSize(StringBounder stringBounder, final TextBlock textBlock) {
		if (size == null) {
			size = textBlock.calculateDimension(stringBounder);
		}
		return size;
	}

	final protected double getTextHeight(StringBounder stringBounder) {
		final TextBlock textBlock = getTextBlock();
		final Dimension2D size = getSize(stringBounder, textBlock);
		return size.getHeight() + 2 * marginY;
	}

	final protected List<? extends CharSequence> getLabels() {
		return strings;
	}

	final protected int getMarginX1() {
		return marginX1;
	}

	final protected int getMarginX2() {
		return marginX2;
	}

	final protected int getMarginY() {
		return marginY;
	}

	final protected Font getFont() {
		return font;
	}

	protected Color getFontColor() {
		return fontColor;
	}

}
