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
package net.sourceforge.plantuml.graph;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class MethodsOrFieldsArea {

	private final UFont font;
	private final List<String> strings = new ArrayList<String>();

	public MethodsOrFieldsArea(List<Member> attributes, UFont font) {
		this.font = font;
		for (Member att : attributes) {
			this.strings.add(att.getDisplay(false));
		}
	}

	public VisibilityModifier getVisibilityModifier() {
		throw new UnsupportedOperationException();
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
		return Display.create(s).create(FontConfiguration.blackBlueTrue(font), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
	}

	public void draw(UGraphic ug, double x, double y) {
		for (String s : strings) {
			final TextBlock bloc = createTextBlock(s);
			bloc.drawU(ug.apply(new UTranslate(x, y)));
			y += bloc.calculateDimension(ug.getStringBounder()).getHeight();
		}
	}

}
