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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public final class RoundedContainer {

	private final Dimension2D dim;
	private final double titleHeight;
	private final double attributeHeight;
	private final HColor borderColor;
	private final HColor backColor;
	private final HColor imgBackcolor;
	private final UStroke stroke;

	public RoundedContainer(Dimension2D dim, double titleHeight, double attributeHeight, HColor borderColor,
			HColor backColor, HColor imgBackcolor, UStroke stroke) {
		if (dim.getWidth() == 0) {
			throw new IllegalArgumentException();
		}
		this.dim = dim;
		this.imgBackcolor = imgBackcolor;
		this.titleHeight = titleHeight;
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.attributeHeight = attributeHeight;
		this.stroke = stroke;
	}

	public void drawU(UGraphic ug, boolean shadowing) {

		ug = ug.apply(backColor.bg()).apply(borderColor);
		final URectangle rect = new URectangle(dim.getWidth(), dim.getHeight()).rounded(IEntityImage.CORNER);
		if (shadowing) {
			rect.setDeltaShadow(3.0);
		}
		ug.apply(stroke).draw(rect);

		final double yLine = titleHeight + attributeHeight;

		ug = ug.apply(imgBackcolor.bg());

		final double thickness = stroke.getThickness();

		final URectangle inner = new URectangle(dim.getWidth() - 4 * thickness,
				dim.getHeight() - titleHeight - 4 * thickness - attributeHeight).rounded(IEntityImage.CORNER);
		ug.apply(imgBackcolor).apply(new UTranslate(2 * thickness, yLine + 2 * thickness))
				.draw(inner);

		if (titleHeight > 0) {
			ug.apply(stroke).apply(UTranslate.dy(yLine)).draw(ULine.hline(dim.getWidth()));
		}

		if (attributeHeight > 0) {
			ug.apply(stroke).apply(UTranslate.dy(yLine - attributeHeight)).draw(ULine.hline(dim.getWidth()));
		}

	}
}
