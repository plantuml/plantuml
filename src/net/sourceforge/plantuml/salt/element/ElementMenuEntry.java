/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 3835 $
 *
 */
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ElementMenuEntry implements Element {

	private final TextBlock block;
	private final String text;
	private HtmlColor background;
	private double x;

	public ElementMenuEntry(String text, UFont font, SpriteContainer spriteContainer) {
		final FontConfiguration config = new FontConfiguration(font, HtmlColorUtils.BLACK);
		this.block = TextBlockUtils.create(Display.asList(text), config, HorizontalAlignment.LEFT, spriteContainer);
		this.text = text;
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		if (text.equals("-")) {
			return new Dimension2DDouble(10, 5);
		}
		return block.calculateDimension(stringBounder);
	}

	public void drawU(UGraphic ug, double x, double y, int zIndex, Dimension2D dimToUse) {
		if (background != null) {
			final Dimension2D dim = getPreferredDimension(ug.getStringBounder(), x, y);
			ug.apply(new UChangeBackColor(background)).apply(new UTranslate(x, y)).draw(new URectangle(dim.getWidth(), dim.getHeight()));
		}
		block.drawU(ug.apply(new UTranslate(x, y)));
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public String getText() {
		return text;
	}

	public HtmlColor getBackground() {
		return background;
	}

	public void setBackground(HtmlColor background) {
		this.background = background;
	}
}
