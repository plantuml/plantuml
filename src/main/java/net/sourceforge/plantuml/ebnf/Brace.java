/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml.ebnf;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.ULine;

public class Brace implements UDrawable {

	private final double width;
	private final double height;

	public Brace(double width, double height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void drawU(UGraphic ug) {
		ug = ug.apply(UStroke.withThickness(0.5));

		final double cinq = 5;
		CornerCurved.createNW(cinq).drawU(ug);
		CornerCurved.createSE(cinq).drawU(ug.apply(new UTranslate(width / 2, 0)));
		CornerCurved.createSW(cinq).drawU(ug.apply(new UTranslate(width / 2, 0)));
		CornerCurved.createNE(cinq).drawU(ug.apply(new UTranslate(width, 0)));

		ug.apply(new UTranslate(cinq, 0)).draw(new ULine(width / 2 - 2 * cinq, 0));
		ug.apply(new UTranslate(cinq + width / 2, 0)).draw(new ULine(width / 2 - 2 * cinq, 0));
//		ug.apply(new UTranslate(width / 2, -height)).draw(new ULine(width / 2, height));

	}

}
