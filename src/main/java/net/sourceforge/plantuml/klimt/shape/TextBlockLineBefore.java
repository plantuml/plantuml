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

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.WithPorts;

public class TextBlockLineBefore extends AbstractTextBlock implements TextBlock, WithPorts {
    // ::remove file when __HAXE__

	private final TextBlock textBlock;
	private final char separator;
	private final TextBlock title;
	private final double defaultThickness;

	public TextBlockLineBefore(double defaultThickness, TextBlock textBlock, char separator, TextBlock title) {
		this.textBlock = textBlock;
		this.separator = separator;
		this.title = title;
		this.defaultThickness = defaultThickness;
	}

	public TextBlockLineBefore(double defaultThickness, TextBlock textBlock, char separator) {
		this(defaultThickness, textBlock, separator, null);
	}

	public TextBlockLineBefore(double defaultThickness, TextBlock textBlock) {
		this(defaultThickness, textBlock, '\0');
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D dim = textBlock.calculateDimension(stringBounder);
		if (title != null) {
			final XDimension2D dimTitle = title.calculateDimension(stringBounder);
			return dim.atLeast(dimTitle.getWidth() + 8, dimTitle.getHeight());
		}
		return dim;
	}

	public void drawU(UGraphic ug) {
		final HColor color = ug.getParam().getColor();
		if (title == null)
			UHorizontalLine.infinite(defaultThickness, 1, 1, separator).drawMe(ug);

		textBlock.drawU(ug);
		if (color == null)
			ug = ug.apply(HColors.none());
		else
			ug = ug.apply(color);

		if (title != null)
			UHorizontalLine.infinite(defaultThickness, 1, 1, title, separator).drawMe(ug);

	}

	@Override
	public XRectangle2D getInnerPosition(CharSequence member, StringBounder stringBounder) {
		return textBlock.getInnerPosition(member, stringBounder);
	}

	@Override
	public Ports getPorts(StringBounder stringBounder) {
		if (textBlock instanceof WithPorts)
			return ((WithPorts) textBlock).getPorts(stringBounder);
		return new Ports();
	}

}