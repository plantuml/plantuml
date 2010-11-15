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
 * Revision $Revision: 5386 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class MethodsOrFieldsArea {

	private final Font font;
	private final List<String> strings = new ArrayList<String>();

	public MethodsOrFieldsArea(List<Member> attributes, Font font) {
		this.font = font;
		for (Member att : attributes) {
			this.strings.add(att.getDisplayWithVisibilityChar());
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		double x = 0;
		double y = 0;
		for (String s : strings) {
			final TextBlock bloc = createTextBlock(s);
			final Dimension2D dim = bloc.calculateDimension(stringBounder);
			y += dim.getHeight();
			x = Math.max(dim.getWidth(), x);
		}
		return new Dimension2DDouble(x, y);
	}

	private TextBlock createTextBlock(String s) {
		return TextBlockUtils.create(Arrays.asList(s), font, Color.BLACK, HorizontalAlignement.LEFT);
	}

	public void drawTOBEREMOVED(Graphics2D g2d, double x, double y) {
		for (String s : strings) {
			final TextBlock bloc = createTextBlock(s);
			bloc.drawTOBEREMOVED(g2d, x, y);
			y += bloc.calculateDimension(StringBounderUtils.asStringBounder(g2d)).getHeight();
		}
	}

	public void draw(UGraphic ug, double x, double y) {
		for (String s : strings) {
			final TextBlock bloc = createTextBlock(s);
			bloc.drawU(ug, x, y);
			y += bloc.calculateDimension(ug.getStringBounder()).getHeight();
		}
	}

}
