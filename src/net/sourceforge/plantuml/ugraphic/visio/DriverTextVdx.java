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
 */
package net.sourceforge.plantuml.ugraphic.visio;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UFontContext;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;

public class DriverTextVdx implements UDriver<VisioGraphics> {

	private final StringBounder stringBounder;

	public DriverTextVdx(StringBounder stringBounder) {
		this.stringBounder = stringBounder;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, VisioGraphics visio) {
		final UText shape = (UText) ushape;
		Thread.dumpStack();

		final FontConfiguration fontConfiguration = shape.getFontConfiguration();
		final UFont font = fontConfiguration.getFont();

		String text = shape.getText();
		if (text.startsWith(" ")) {
			final double space = stringBounder.calculateDimension(font, " ").getWidth();
			while (text.startsWith(" ")) {
				x += space;
				text = text.substring(1);
			}
		}

		text = StringUtils.trin(text);
		final Dimension2D dim = stringBounder.calculateDimension(font, text);
		visio.text(text, x, y, font.getFamily(UFontContext.SVG), font.getSize(), dim.getWidth(), dim.getHeight(),
				fontConfiguration.getAttributes());

	}

}
