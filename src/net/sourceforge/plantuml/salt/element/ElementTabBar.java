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
import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ElementTabBar extends AbstractElement {

	private final Collection<Element> tabs = new ArrayList<Element>();
	private final UFont font;
	private final ISkinSimple spriteContainer;

	private final double margin1 = 2;
	private final double margin2 = 3;
	private final double margin3 = 10;

	private boolean vertical = false;

	public ElementTabBar(UFont font, ISkinSimple spriteContainer) {
		this.font = font;
		this.spriteContainer = spriteContainer;
	}

	public void addTab(String tab) {
		final Element elt = new ElementText(Arrays.asList(tab), font, spriteContainer);
		tabs.add(elt);
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		if (vertical) {
			return getPreferredDimensionVertical(stringBounder, x, y);
		}
		return getPreferredDimensionHorizontal(stringBounder, x, y);

	}

	private Dimension2D getPreferredDimensionHorizontal(StringBounder stringBounder, double x, double y) {
		double w = 0;
		double h = 0;
		for (Element elt : tabs) {
			final Dimension2D dim = elt.getPreferredDimension(stringBounder, x, y);
			w += dim.getWidth() + margin1 + margin2 + margin3;
			h = Math.max(h, dim.getHeight());
		}
		return new Dimension2DDouble(w, h);
	}

	public void drawU(UGraphic ug, int zIndex, Dimension2D dimToUse) {
		if (zIndex != 0) {
			return;
		}
		if (vertical) {
			drawUVertical(ug, 0, 0, zIndex, dimToUse);
		} else {
			drawUHorizontal(ug, 0, 0, zIndex, dimToUse);
		}
	}

	private void drawUHorizontal(UGraphic ug, final double x, final double y, int zIndex, Dimension2D dimToUse) {
		double x1 = x;
		for (Element elt : tabs) {
			elt.drawU(ug.apply(new UTranslate(x1 + margin1, y)), zIndex, dimToUse);
			final Dimension2D dimText = elt.getPreferredDimension(ug.getStringBounder(), x1, y);
			final double w = dimText.getWidth();
			ug.apply(new UTranslate(x1, y)).draw(ULine.vline(dimText.getHeight()));
			ug.apply(new UTranslate(x1, y)).draw(ULine.hline(w + margin1 + margin2));
			ug.apply(new UTranslate(x1 + w + margin1 + margin2, y)).draw(ULine.vline(dimText.getHeight()));
			ug.apply(new UTranslate(x1 + w + margin1 + margin2, y + dimText.getHeight())).draw(ULine.hline(margin3));
			x1 += w + margin1 + margin2 + margin3;
		}
	}

	private Dimension2D getPreferredDimensionVertical(StringBounder stringBounder, double x, double y) {
		double w = 0;
		double h = 0;
		for (Element elt : tabs) {
			final Dimension2D dim = elt.getPreferredDimension(stringBounder, x, y);
			h += dim.getHeight() + margin1 + margin2 + margin3;
			w = Math.max(w, dim.getWidth());
		}
		return new Dimension2DDouble(w, h);
	}

	private void drawUVertical(UGraphic ug, final double x, final double y, int zIndex, Dimension2D dimToUse) {
		final Dimension2D preferred = getPreferredDimension(ug.getStringBounder(), x, y);
		ug = ug.apply(new UTranslate(x, y));
		double y1 = x;
		for (Element elt : tabs) {
			elt.drawU(ug.apply(UTranslate.dy(y1 + margin1)), zIndex, dimToUse);
			final Dimension2D dimText = elt.getPreferredDimension(ug.getStringBounder(), x, y1);
			final double h = dimText.getHeight();
			ug.apply(UTranslate.dy(y1)).draw(ULine.hline(preferred.getWidth()));
			ug.apply(UTranslate.dy(y1)).draw(ULine.vline(h + margin1 + margin2));
			ug.apply(UTranslate.dy(y1 + h + margin1 + margin2)).draw(ULine.hline(preferred.getWidth()));
			ug.apply(new UTranslate(preferred.getWidth(), y1 + h + margin1 + margin2)).draw(ULine.vline(margin3));
			y1 += h + margin1 + margin2 + margin3;
		}
	}

	public boolean isVertical() {
		return vertical;
	}

	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}

}
