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
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.posimo;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;

public class Frame implements Component {

	private final List<String> name;
	private final Color textColor;
	private final Color lineColor;
	private final Font font;

	public Frame(List<String> name, Color textColor, Font font, Color lineColor) {
		this.name = name;
		this.textColor = textColor;
		this.lineColor = lineColor;
		this.font = font;
	}

	public void drawU(UGraphic ug, Dimension2D dimensionToUse, Context2D context) {
		ug.getParam().setColor(lineColor);
		ug.getParam().setBackcolor(null);
		ug.draw(0, 0, new URectangle(dimensionToUse.getWidth(), dimensionToUse.getHeight()));

		final TextBlock textBlock = createTextBloc();
		textBlock.drawU(ug, 2, 2);

		final Dimension2D textDim = getTextDim(ug.getStringBounder());
		final double x = textDim.getWidth() + 6;
		final double y = textDim.getHeight() + 6;
		final UPolygon poly = new UPolygon();
		poly.addPoint(x, 0);
		poly.addPoint(x, y - 6);
		poly.addPoint(x - 6, y);
		poly.addPoint(0, y);
		poly.addPoint(0, 0);
		ug.getParam().setColor(lineColor);
		ug.draw(0, 0, poly);

	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final Dimension2D dim = getTextDim(stringBounder);
		return dim.getHeight() + 8;
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		final Dimension2D dim = getTextDim(stringBounder);
		return dim.getWidth() + 8;
	}

	private Dimension2D getTextDim(StringBounder stringBounder) {
		final TextBlock bloc = createTextBloc();
		return bloc.calculateDimension(stringBounder);
	}

	private TextBlock createTextBloc() {
		final TextBlock bloc = TextBlockUtils.create(name, font, textColor, HorizontalAlignement.LEFT);
		return bloc;
	}

}
