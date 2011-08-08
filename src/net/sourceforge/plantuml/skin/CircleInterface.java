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
 * Revision $Revision: 6576 $
 *
 */
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class CircleInterface implements UDrawable {

	private final float thickness;
	private final double headDiam;
	private final HtmlColor backgroundColor;
	private final HtmlColor foregroundColor;

	public CircleInterface(HtmlColor backgroundColor, HtmlColor foregroundColor) {
		this(backgroundColor, foregroundColor, 16, 2);
	}

	public CircleInterface(HtmlColor backgroundColor, HtmlColor foregroundColor, double headDiam, float thickness) {
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.headDiam = headDiam;
		this.thickness = thickness;
	}

//	public void draw(Graphics2D g2d) {
//
//		g2d.setStroke(new BasicStroke(thickness));
//
//		final Shape head = new Ellipse2D.Double(thickness, thickness, headDiam, headDiam);
//
//		g2d.setColor(backgroundColor);
//		g2d.fill(head);
//
//		g2d.setColor(foregroundColor);
//		g2d.draw(head);
//
//		g2d.setStroke(new BasicStroke());
//		throw new UnsupportedOperationException();
//	}
	
	public void drawU(UGraphic ug) {

		ug.getParam().setStroke(new UStroke(thickness));

		final UEllipse head = new UEllipse(headDiam, headDiam);

		ug.getParam().setBackcolor(backgroundColor);
		ug.getParam().setColor(foregroundColor);
		ug.draw(thickness, thickness, head);

		ug.getParam().setStroke(new UStroke());
	}


	public double getPreferredWidth(StringBounder stringBounder) {
		return headDiam + 2 * thickness;
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return headDiam + 2 * thickness;
	}

}
