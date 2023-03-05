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
 * Modified by : Arno Peterson
 * 
 * 
 */
package net.sourceforge.plantuml.svek;

import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;

public class ClusterDecoration {

	private final UStroke defaultStroke;
	final private USymbol symbol;
	final private TextBlock title;
	final private TextBlock stereo;

	final private RectangleArea rectangleArea;

	public ClusterDecoration(PackageStyle style, USymbol symbol, TextBlock title, TextBlock stereo,
			RectangleArea rectangleArea, UStroke stroke) {
		this.symbol = guess(symbol, style);
		this.stereo = stereo;
		this.title = title;
		this.rectangleArea = rectangleArea;
		this.defaultStroke = stroke;
	}

	private static USymbol guess(USymbol symbol, PackageStyle style) {
		if (symbol != null)
			return symbol;

		return style.toUSymbol();
	}

	public void drawU(UGraphic ug, HColor backColor, HColor borderColor, double shadowing, double roundCorner,
			HorizontalAlignment titleAlignment, HorizontalAlignment stereoAlignment, double diagonalCorner) {
		final Fashion biColor = new Fashion(backColor, borderColor);
		if (symbol == null)
			throw new UnsupportedOperationException();

		final Fashion symbolContext = biColor.withShadow(shadowing).withStroke(defaultStroke).withCorner(roundCorner,
				diagonalCorner);
		symbol.asBig(title, titleAlignment, stereo, rectangleArea.getWidth(), rectangleArea.getHeight(),
				symbolContext, stereoAlignment).drawU(ug.apply(rectangleArea.getPosition()));
	}

}
