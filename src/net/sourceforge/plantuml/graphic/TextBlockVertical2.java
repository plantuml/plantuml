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
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.svek.WithPorts;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class TextBlockVertical2 extends AbstractTextBlock implements TextBlock, WithPorts {

	private final List<TextBlock> blocks = new ArrayList<TextBlock>();
	private final HorizontalAlignment horizontalAlignment;

	TextBlockVertical2(TextBlock b1, TextBlock b2, HorizontalAlignment horizontalAlignment) {
		this.blocks.add(b1);
		this.blocks.add(b2);
		this.horizontalAlignment = horizontalAlignment;
	}

	public TextBlockVertical2(List<TextBlock> all, HorizontalAlignment horizontalAlignment) {
		if (all.size() < 2) {
			throw new IllegalArgumentException();
		}
		this.blocks.addAll(all);
		this.horizontalAlignment = horizontalAlignment;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D dim = blocks.get(0).calculateDimension(stringBounder);
		for (int i = 1; i < blocks.size(); i++) {
			dim = Dimension2DDouble.mergeTB(dim, blocks.get(i).calculateDimension(stringBounder));
		}
		return dim;
	}

	public void drawU(UGraphic ug) {
		double y = 0;
		final Dimension2D dimtotal = calculateDimension(ug.getStringBounder());

		for (TextBlock block : blocks) {
			final Dimension2D dimb = block.calculateDimension(ug.getStringBounder());
			if (block instanceof TextBlockBackcolored) {
				final HColor back = ((TextBlockBackcolored) block).getBackcolor();
				if (back != null) {
					ug.apply(UTranslate.dy(y)).apply(back).apply(back.bg())
							.draw(new URectangle(dimtotal.getWidth(), dimb.getHeight()));
				}
			}
			if (horizontalAlignment == HorizontalAlignment.LEFT) {
				block.drawU(ug.apply(UTranslate.dy(y)));
			} else if (horizontalAlignment == HorizontalAlignment.CENTER) {
				final double dx = (dimtotal.getWidth() - dimb.getWidth()) / 2;
				block.drawU(ug.apply(new UTranslate(dx, y)));
			} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
				final double dx = dimtotal.getWidth() - dimb.getWidth();
				block.drawU(ug.apply(new UTranslate(dx, y)));
			} else {
				throw new UnsupportedOperationException();
			}
			y += dimb.getHeight();
		}
	}

	public Ports getPorts(StringBounder stringBounder) {
		double y = 0;
		// final Dimension2D dimtotal = calculateDimension(stringBounder);
		final Ports result = new Ports();
		for (TextBlock block : blocks) {
			final Dimension2D dimb = block.calculateDimension(stringBounder);
			final Ports tmp = ((WithPorts) block).getPorts(stringBounder).translateY(y);
			result.addThis(tmp);
			y += dimb.getHeight();
		}
		return result;
	}

	@Override
	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		double y = 0;
		for (TextBlock block : blocks) {
			final Dimension2D dimb = block.calculateDimension(stringBounder);
			final Rectangle2D result = block.getInnerPosition(member, stringBounder, strategy);
			if (result != null) {
				return UTranslate.dy(y).apply(result);
			}
			y += dimb.getHeight();
		}
		return null;
	}

}