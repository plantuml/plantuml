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
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPixel;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ElementImage extends AbstractElement {

	private final List<String> img;

	public ElementImage(List<String> img) {
		this.img = img;
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		return new Dimension2DDouble(img.get(0).length(), img.size());
	}

	public void drawU(UGraphic ug, int zIndex, Dimension2D dimToUse) {
		if (zIndex != 0) {
			return;
		}
		final int w = img.get(0).length();
		final int h = img.size();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				final char c = img.get(j).charAt(i);
				if (c == 'X') {
					ug.apply(new UTranslate(i, j)).draw(new UPixel());
				}
			}
		}
	}
}
