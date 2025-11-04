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
 */
package net.sourceforge.plantuml.timingdiagram.graphic;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.URectangle;

public class PentaBShapeRectangle implements UDrawable {

	private final double width;
	private final double height;
	private final Fashion context;

	private PentaBShapeRectangle(double width, double height, Fashion context) {
		this.width = width;
		this.height = height;
		this.context = context;
	}

	public static PentaBShapeRectangle create(double width, double height, Fashion context) {
		return new PentaBShapeRectangle(width, height, context);
	}

	public void drawU(UGraphic ug) {
		final URectangle polygon = URectangle.build(width, height);
		context.withForeColor(context.getBackColor()).apply(ug).draw(polygon);

		final UPath path = UPath.none();
		path.moveTo(width, 0);
		path.lineTo(0, 0);
		path.lineTo(0, height);
		path.lineTo(width, height);
		context.apply(ug).draw(path);

	}

}
