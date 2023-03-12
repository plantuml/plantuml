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

import java.util.ArrayList;
import java.util.List;

import net.atmp.InnerStrategy;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.WithPorts;

public class TextBlockVertical2 extends AbstractTextBlock implements TextBlock, WithPorts {
    // ::remove file when __HAXE__

	private final List<TextBlock> blocks = new ArrayList<>();
	private final HorizontalAlignment horizontalAlignment;

	TextBlockVertical2(TextBlock b1, TextBlock b2, HorizontalAlignment horizontalAlignment) {
		this.blocks.add(b1);
		this.blocks.add(b2);
		this.horizontalAlignment = horizontalAlignment;
	}

	TextBlockVertical2(TextBlock b1, final UImage image, HorizontalAlignment horizontalAlignment) {
		this(b1, convertImage(image), horizontalAlignment);
	}

	static private AbstractTextBlock convertImage(final UImage image) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				ug.draw(image);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(image.getWidth(), image.getHeight());
			}
		};
	}

	public TextBlockVertical2(List<TextBlock> all, HorizontalAlignment horizontalAlignment) {
		if (all.size() < 2)
			throw new IllegalArgumentException();

		this.blocks.addAll(all);
		this.horizontalAlignment = horizontalAlignment;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		XDimension2D dim = blocks.get(0).calculateDimension(stringBounder);
		for (int i = 1; i < blocks.size(); i++)
			dim = dim.mergeTB(blocks.get(i).calculateDimension(stringBounder));

		return dim;
	}

	public void drawU(UGraphic ug) {
		double y = 0;
		final XDimension2D dimtotal = calculateDimension(ug.getStringBounder());

		for (TextBlock block : blocks) {
			final XDimension2D dimb = block.calculateDimension(ug.getStringBounder());

			final HColor back = block.getBackcolor();
			if (back != null && back.isTransparent() == false)
				ug.apply(UTranslate.dy(y)).apply(back).apply(back.bg())
						.draw(URectangle.build(dimtotal.getWidth(), dimb.getHeight()));

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

	@Override
	public Ports getPorts(StringBounder stringBounder) {
		double y = 0;
		// final Dimension2D dimtotal = calculateDimension(stringBounder);
		final Ports result = new Ports();
		for (TextBlock block : blocks) {
			final XDimension2D dimb = block.calculateDimension(stringBounder);
			final Ports tmp = ((WithPorts) block).getPorts(stringBounder).translateY(y);
			result.addThis(tmp);
			y += dimb.getHeight();
		}
		return result;
	}

	@Override
	public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		double y = 0;
		for (TextBlock block : blocks) {
			final XDimension2D dimb = block.calculateDimension(stringBounder);
			final XRectangle2D result = block.getInnerPosition(member, stringBounder, strategy);
			if (result != null) {
				return UTranslate.dy(y).apply(result);
			}
			y += dimb.getHeight();
		}
		return null;
	}

}