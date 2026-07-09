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
package net.sourceforge.plantuml.asciiverse;

// A generic AsciiBlock decorator: stacks a "top" block above a "bottom"
// block, each centered within their combined width, separated by a single
// blank row. This is the ASCII counterpart of klimt's
// DecorateEntityImage.addTop() (used by DiagramChromeFactory to attach the
// title/caption/header/footer on the pixel/SVG/PNG path): same "both blocks
// centered within the total width" rule, reimplemented on the character grid
// instead of floating-point coordinates.
//
// Written generically — nothing here is title-specific — even though the
// only caller today is DiagramChromeFactory's ASCII title support:
// SequenceDiagram.exportTxt() was drawing the raw AsciiBlock
// straight to the InfinitePlan, bypassing addChrome() entirely, so the title
// (and caption/header/footer, not yet handled) never appeared in -ttxt/-tutxt
// output. Caption/header/footer can reuse this same class later.
public final class AsciiBlockStackTB implements AsciiBlock {

	private final AsciiBlock top;
	private final AsciiBlock bottom;

	public AsciiBlockStackTB(AsciiBlock top, AsciiBlock bottom) {
		this.top = top;
		this.bottom = bottom;
	}

	@Override
	public ADimension2D asciiDimension() {
		final ADimension2D dimTop = top.asciiDimension();
		if (dimTop.getWidth() == 0)
			// Nothing to show on top (e.g. an empty title): don't reserve the
			// separator row either, same "no-op when the wrapped content is
			// empty" guard as AsciiBlockMarginLR.
			return bottom.asciiDimension();

		final ADimension2D dimBottom = bottom.asciiDimension();
		final int width = Math.max(dimTop.getWidth(), dimBottom.getWidth());
		final int height = dimTop.getHeight() + 1 + dimBottom.getHeight();
		return new ADimension2D(width, height);
	}

	@Override
	public void asciiDraw(InfinitePlan plan) {
		final ADimension2D dimTop = top.asciiDimension();
		if (dimTop.getWidth() == 0) {
			bottom.asciiDraw(plan);
			return;
		}

		final ADimension2D dimBottom = bottom.asciiDimension();
		final int totalWidth = Math.max(dimTop.getWidth(), dimBottom.getWidth());

		final int xTop = (totalWidth - dimTop.getWidth()) / 2;
		top.asciiDraw(plan.move(xTop, 0));

		final int xBottom = (totalWidth - dimBottom.getWidth()) / 2;
		bottom.asciiDraw(plan.move(xBottom, dimTop.getHeight() + 1));
	}

}
