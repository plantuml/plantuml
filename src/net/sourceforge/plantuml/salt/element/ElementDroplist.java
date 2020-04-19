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
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class ElementDroplist extends AbstractElementText implements Element {

	private final int box = 12;
	private final TextBlock openDrop;

	public ElementDroplist(String text, UFont font, ISkinSimple spriteContainer) {
		super(extract(text), font, true, spriteContainer);
		final StringTokenizer st = new StringTokenizer(text, "^");
		final List<String> drop = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			drop.add(st.nextToken());
		}
		if (drop.size() > 0) {
			drop.remove(0);
		}
		if (drop.size() == 0) {
			this.openDrop = null;
		} else {
			this.openDrop = Display.create(drop).create(getConfig(), HorizontalAlignment.LEFT, spriteContainer);
		}
	}

	private static String extract(String text) {
		final int idx = text.indexOf('^');
		if (idx == -1) {
			return text;
		}
		return text.substring(0, idx);
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		final Dimension2D dim = getTextDimensionAt(stringBounder, x + 2);
		return Dimension2DDouble.delta(dim, 4 + box, 4);
	}

	public void drawU(UGraphic ug, int zIndex, Dimension2D dimToUse) {
		final Dimension2D dim = getPreferredDimension(ug.getStringBounder(), 0, 0);
		if (zIndex == 0) {
			ug.apply(HColorSet.instance().getColorIfValid("#EEEEEE").bg())
					.draw(new URectangle(dim.getWidth() - 1, dim.getHeight() - 1));
			drawText(ug, 2, 2);
			final double xline = dim.getWidth() - box;
			ug.apply(UTranslate.dx(xline)).draw(ULine.vline(dim.getHeight() - 1));

			final UPolygon poly = new UPolygon();
			poly.addPoint(0, 0);
			poly.addPoint(box - 6, 0);
			final Dimension2D dimText = getPureTextDimension(ug.getStringBounder());
			poly.addPoint((box - 6) / 2, dimText.getHeight() - 8);

			ug.apply(HColorUtils.changeBack(ug)).apply(new UTranslate(xline + 3, 6)).draw(poly);
		}

		if (openDrop != null) {
			final Dimension2D dimOpen = Dimension2DDouble.atLeast(openDrop.calculateDimension(ug.getStringBounder()),
					dim.getWidth() - 1, 0);
			ug = ug.apply(UTranslate.dy(dim.getHeight() - 1));
			ug.apply(HColorSet.instance().getColorIfValid("#EEEEEE").bg())
					.draw(new URectangle(dimOpen.getWidth() - 1, dimOpen.getHeight() - 1));
			openDrop.drawU(ug);
		}
	}
}
