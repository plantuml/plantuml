/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 3835 $
 *
 */
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;

public class ElementMenuPopup implements Element {

	private final Collection<ElementMenuEntry> entries = new ArrayList<ElementMenuEntry>();
	private final UFont font;

	public ElementMenuPopup(UFont font) {
		this.font = font;
	}

	public void addEntry(String s) {
		entries.add(new ElementMenuEntry(s, font));
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		double w = 0;
		double h = 0;
		for (ElementMenuEntry entry : entries) {
			final Dimension2D dim = entry.getPreferredDimension(stringBounder, x, y);
			w = Math.max(w, dim.getWidth());
			h += dim.getHeight();
		}
		return new Dimension2DDouble(w, h);
	}

	public void drawU(UGraphic ug, double x, double y, int zIndex, Dimension2D dimToUse) {
		if (zIndex != 1) {
			return;
		}
		ug.getParam().setBackcolor(HtmlColor.getColorIfValid("#DDDDDD"));
		ug.draw(x, y, new URectangle(dimToUse.getWidth(), dimToUse.getHeight()));
		ug.getParam().setBackcolor(null);

		for (ElementMenuEntry entry : entries) {
			final double h = entry.getPreferredDimension(ug.getStringBounder(), x, y).getHeight();
			if (entry.getText().equals("-")) {
				ug.draw(x, y + h / 2, new ULine(dimToUse.getWidth(), 0));
			} else {
				entry.drawU(ug, x, y, zIndex, dimToUse);
			}
			y += h;
		}
	}

}
