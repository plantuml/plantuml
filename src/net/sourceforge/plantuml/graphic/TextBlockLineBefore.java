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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.WithPorts;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UHorizontalLine;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

public class TextBlockLineBefore extends AbstractTextBlock implements TextBlock, WithPorts {

	private final TextBlock textBlock;
	private final char separator;
	private final TextBlock title;

	public TextBlockLineBefore(TextBlock textBlock, char separator, TextBlock title) {
		this.textBlock = textBlock;
		this.separator = separator;
		this.title = title;
	}

	public TextBlockLineBefore(TextBlock textBlock, char separator) {
		this(textBlock, separator, null);
	}

	public TextBlockLineBefore(TextBlock textBlock) {
		this(textBlock, '\0');
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		if (title != null) {
			final Dimension2D dimTitle = title.calculateDimension(stringBounder);
			return Dimension2DDouble.atLeast(dim, dimTitle.getWidth() + 8, dimTitle.getHeight());
		}
		return dim;
	}

	public void drawU(UGraphic ug) {
		final HColor color = ug.getParam().getColor();
		if (title == null) {
			UHorizontalLine.infinite(1, 1, separator).drawMe(ug);
		}
		textBlock.drawU(ug);
		if (color == null) {
			ug = ug.apply(new HColorNone());
		} else {
			ug = ug.apply(color);
		}
		if (title != null) {
			UHorizontalLine.infinite(1, 1, title, separator).drawMe(ug);
		}
	}

	@Override
	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return textBlock.getInnerPosition(member, stringBounder, strategy);
	}

	public Ports getPorts(StringBounder stringBounder) {
		return ((WithPorts) textBlock).getPorts(stringBounder);
	}

}