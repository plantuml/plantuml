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

import net.atmp.InnerStrategy;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.WithPorts;

class TextBlockMarged extends AbstractTextBlock implements TextBlock, WithPorts {
	// ::remove file when __HAXE__

	private final TextBlock textBlock;
	private final double top;
	private final double right;
	private final double bottom;
	private final double left;

	TextBlockMarged(TextBlock textBlock, double top, double right, double bottom, double left) {
		this.textBlock = textBlock;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	TextBlockMarged(TextBlock textBlock, ClockwiseTopRightBottomLeft margins) {
		this.textBlock = textBlock;
		this.top = margins.getTop();
		this.right = margins.getRight();
		this.bottom = margins.getBottom();
		this.left = margins.getLeft();
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D dim = textBlock.calculateDimension(stringBounder);
		return dim.delta(left + right, top + bottom);
	}

	public void drawU(UGraphic ug) {
		// ug.apply(HColors.BLUE).draw(URectangle.build(calculateDimension(ug.getStringBounder())));
		final XDimension2D dim = calculateDimension(ug.getStringBounder());
		if (dim.getWidth() > 0) {
			ug.draw(UEmpty.create(dim));
			final UTranslate translate = new UTranslate(left, top);
			textBlock.drawU(ug.apply(translate));
		}
	}

	@Override
	public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		final XRectangle2D parent = textBlock.getInnerPosition(member, stringBounder, strategy);
		if (parent == null)
			return null;

		final UTranslate translate = new UTranslate(left, top);
		return translate.apply(parent);
	}

	@Override
	public Ports getPorts(StringBounder stringBounder) {
		return ((WithPorts) textBlock).getPorts(stringBounder).translateY(top);
	}

}