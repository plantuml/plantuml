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
package net.sourceforge.plantuml.wbs;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.utils.Direction;

class Fork extends WBSTextBlock {

	private final TextBlock main;
	private final List<ITF> right = new ArrayList<>();

	public Fork(ISkinParam skinParam, WElement idea) {
		super(idea.withBackColor(skinParam), idea.getStyleBuilder(), idea.getLevel());
		if (idea.getLevel() != 0)
			throw new IllegalArgumentException();

		this.main = buildMain(idea);
		for (WElement child : idea.getChildren(Direction.RIGHT))
			this.right.add(ITFComposed.build2(skinParam, child));

	}

	final private double delta1x = 20;
	final private double deltay = 40;

	public void drawU(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D mainDim = main.calculateDimension(stringBounder);

		final double y0 = mainDim.getHeight();
		final double y1 = y0 + deltay / 2;
		final double y2 = y0 + deltay;
		final double mainWidth = mainDim.getWidth();

		if (right.size() == 0) {
			main.drawU(ug);
			drawLine(ug, mainWidth / 2, y0, mainWidth / 2, y1);
			return;
		}

		double x = 0;
		final double firstX = right.get(0).getT1(stringBounder).getX();
		double lastX = firstX;

		for (ITF child : right) {
			lastX = x + child.getT1(stringBounder).getX();
			drawLine(ug, lastX, y1, lastX, y2);
			child.drawU(ug.apply(new UTranslate(x, y2)));
			x += child.calculateDimension(stringBounder).getWidth() + delta1x;
		}

		final double posMain;
		if (lastX > firstX) {
			drawLine(ug, firstX, y1, lastX, y1);
			posMain = firstX + (lastX - firstX - mainWidth) / 2;
		} else {
			assert lastX == firstX;
			final XDimension2D fullDim = calculateDimension(stringBounder);
			posMain = (fullDim.getWidth() - mainWidth) / 2;
			drawLine(ug, firstX, y1, posMain + mainWidth / 2, y1);
		}
		main.drawU(ug.apply(UTranslate.dx(posMain)));
		drawLine(ug, posMain + mainWidth / 2, y0, posMain + mainWidth / 2, y1);

	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (ITF child : right) {
			final XDimension2D childDim = child.calculateDimension(stringBounder);
			height = Math.max(height, childDim.getHeight());
			width += childDim.getWidth();
		}
		final XDimension2D mainDim = main.calculateDimension(stringBounder);
		height += mainDim.getHeight();
		height += deltay;
		width = Math.max(width, mainDim.getWidth());
		return new XDimension2D(width, height);
	}

}
