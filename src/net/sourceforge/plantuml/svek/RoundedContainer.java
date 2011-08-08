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
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public final class RoundedContainer {

	private final Dimension2D dim;
	private final double titleHeight;
	private final HtmlColor borderColor;
	private final HtmlColor backColor;
	private final HtmlColor imgBackcolor;

	public RoundedContainer(Dimension2D dim, double titleHeight, HtmlColor borderColor, HtmlColor backColor,
			HtmlColor imgBackcolor) {
		this.dim = dim;
		this.imgBackcolor = imgBackcolor;
		this.titleHeight = titleHeight;
		this.borderColor = borderColor;
		this.backColor = backColor;
	}

	public final static double THICKNESS_BORDER = 1.5;

	public void drawU(UGraphic ug, double x, double y) {

		ug.getParam().setColor(borderColor);
		ug.getParam().setBackcolor(backColor);
		ug.getParam().setStroke(new UStroke(THICKNESS_BORDER));
		ug
				.draw(x, y, new URectangle(dim.getWidth(), dim.getHeight(), EntityImageState.CORNER,
						EntityImageState.CORNER));

		final double yLine = y + titleHeight;

		ug.getParam().setBackcolor(imgBackcolor);
		ug.getParam().setColor(imgBackcolor);
		ug.getParam().setStroke(new UStroke());
		ug.draw(x + 2 * THICKNESS_BORDER, yLine + 2 * THICKNESS_BORDER, new URectangle(dim.getWidth() - 4
				* THICKNESS_BORDER, dim.getHeight() - titleHeight - 4 * THICKNESS_BORDER, EntityImageState.CORNER,
				EntityImageState.CORNER));

		ug.getParam().setColor(borderColor);
		ug.getParam().setStroke(new UStroke(THICKNESS_BORDER));
		ug.draw(x, yLine, new ULine(dim.getWidth(), 0));
		ug.getParam().setStroke(new UStroke());

	}

}
