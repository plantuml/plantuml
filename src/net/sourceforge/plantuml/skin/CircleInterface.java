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
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class CircleInterface implements UDrawable {

	private final float thickness;
	private final double headDiam;
	private final HColor backgroundColor;
	private final HColor foregroundColor;

	public CircleInterface(HColor backgroundColor, HColor foregroundColor) {
		this(backgroundColor, foregroundColor, 16, 2);
	}

	public CircleInterface(HColor backgroundColor, HColor foregroundColor, double headDiam, float thickness) {
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.headDiam = headDiam;
		this.thickness = thickness;
	}

	public void drawU(UGraphic ug) {
		final UEllipse head = new UEllipse(headDiam, headDiam);
		
		ug.apply(new UStroke(thickness)).apply(backgroundColor.bg())
		.apply(foregroundColor).apply(new UTranslate((double) thickness, (double) thickness)).draw(head);
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		return headDiam + 2 * thickness;
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return headDiam + 2 * thickness;
	}

}
