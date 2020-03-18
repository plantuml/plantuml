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
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TextBlockRaw extends AbstractTextBlock implements TextBlock {

	private List<Line> lines2;

	private final List<String> strings;
	private final FontConfiguration fontConfiguration;

	public TextBlockRaw(List<String> strings, FontConfiguration fontConfiguration) {
		this.strings = strings;
		this.fontConfiguration = fontConfiguration;
	}

	private List<Line> getLines(StringBounder stringBounder) {
		if (lines2 == null) {
			if (stringBounder == null) {
				throw new IllegalStateException();
			}
			this.lines2 = new ArrayList<Line>();
			for (String s : strings) {
				lines2.add(SingleLine.rawText(s, fontConfiguration));
			}
		}
		return lines2;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return getTextDimension(stringBounder);
	}

	protected final Dimension2D getTextDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (Line line : getLines(stringBounder)) {
			final Dimension2D size2D = line.calculateDimension(stringBounder);
			height += size2D.getHeight();
			width = Math.max(width, size2D.getWidth());
		}
		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug) {
		double y = 0;

		for (Line line : getLines(ug.getStringBounder())) {
			line.drawU(ug.apply(UTranslate.dy(y)));
			y += line.calculateDimension(ug.getStringBounder()).getHeight();
		}
	}

}
