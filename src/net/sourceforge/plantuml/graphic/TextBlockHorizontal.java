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

public class TextBlockHorizontal extends AbstractTextBlock implements TextBlock {

	private final List<TextBlock> blocks = new ArrayList<TextBlock>();
	private final VerticalAlignment alignment;

	TextBlockHorizontal(TextBlock b1, TextBlock b2, VerticalAlignment alignment) {
		this.blocks.add(b1);
		this.blocks.add(b2);
		this.alignment = alignment;
	}

	public TextBlockHorizontal(List<TextBlock> all, VerticalAlignment alignment) {
		if (all.size() < 2) {
			throw new IllegalArgumentException();
		}
		this.blocks.addAll(all);
		this.alignment = alignment;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D dim = blocks.get(0).calculateDimension(stringBounder);
		for (int i = 1; i < blocks.size(); i++) {
			dim = Dimension2DDouble.mergeLR(dim, blocks.get(i).calculateDimension(stringBounder));
		}
		return dim;
	}

	public void drawU(UGraphic ug) {
		double x = 0;
		final Dimension2D dimtotal = calculateDimension(ug.getStringBounder());
		for (TextBlock block : blocks) {
			final Dimension2D dimb = block.calculateDimension(ug.getStringBounder());
			if (alignment == VerticalAlignment.CENTER) {
				final double dy = (dimtotal.getHeight() - dimb.getHeight()) / 2;
				block.drawU(ug.apply(new UTranslate(x, dy)));
			} else {
				block.drawU(ug.apply(UTranslate.dx(x)));
			}
			x += dimb.getWidth();
		}
	}

}