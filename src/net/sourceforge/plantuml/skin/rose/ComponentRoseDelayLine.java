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
 * Revision $Revision: 5937 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.AbstractComponent;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ComponentRoseDelayLine extends AbstractComponent {

	private final HtmlColor color;

	public ComponentRoseDelayLine(HtmlColor color) {
		this.color = color;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Dimension2D dimensionToUse, boolean withShadow) {
		ug.getParam().setColor(color);
		// stroke(ug, 0.4, 2.5);
		stroke(ug, 1, 4);
		final int x = (int) (dimensionToUse.getWidth() / 2);
		ug.setAntiAliasing(false);
		ug.draw(x, 0, new ULine(0, dimensionToUse.getHeight()));
		ug.setAntiAliasing(true);
		final double dx = 5;
		final double dy = 1.5;
		final double space = 1.5;
		final double middle = dimensionToUse.getHeight() / 2;
		ug.getParam().setStroke(new UStroke(1));
//		ug.draw(x - dx, middle + dy - space, new ULine(2 * dx, -2 * dy));
//		ug.draw(x - dx, middle + dy + space, new ULine(2 * dx, -2 * dy));
		ug.getParam().setStroke(new UStroke());
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return 20;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return 1;
	}

}
