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
 *
 */
package net.sourceforge.plantuml.salt.element;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.ISkinSimple;

public class ElementMenuEntry extends AbstractElement {

	private final TextBlock block;
	private final String text;
	private HColor background;
	private double xxx;

	public ElementMenuEntry(String text, UFont font, ISkinSimple spriteContainer) {
		final FontConfiguration config = FontConfiguration.blackBlueTrue(font);
		this.block = Display.create(text).create(config, HorizontalAlignment.LEFT, spriteContainer);
		this.text = text;
	}

	public XDimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		if (text.equals("-")) {
			return new XDimension2D(10, 5);
		}
		return block.calculateDimension(stringBounder);
	}

	public void drawU(UGraphic ug, int zIndex, XDimension2D dimToUse) {
		ug = ug.apply(getBlack());
		if (background != null) {
			final XDimension2D dim = getPreferredDimension(ug.getStringBounder(), 0, 0);
			ug.apply(background.bg()).draw(URectangle.build(dim.getWidth(), dim.getHeight()));
		}
		block.drawU(ug);
	}

	public double getX() {
		return xxx;
	}

	public void setX(double x) {
		this.xxx = x;
	}

	public String getText() {
		return text;
	}

	public HColor getBackground() {
		return background;
	}

	public void setBackground(HColor background) {
		this.background = background;
	}
}
