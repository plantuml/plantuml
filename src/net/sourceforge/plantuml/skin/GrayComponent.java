/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.skin;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;

class GrayComponent extends AbstractComponent {

	private static final UFont NORMAL = new UFont("SansSerif", Font.PLAIN, 7);

	private final ComponentType type;

	public GrayComponent(ComponentType type) {
		this.type = type;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(new UChangeBackColor(HtmlColorUtils.LIGHT_GRAY)).apply(new UChangeColor(HtmlColorUtils.BLACK));
		ug.draw(new URectangle(getPreferredWidth(stringBounder), getPreferredHeight(stringBounder)));

		final String n = type.name();
		final int split = 9;
		final List<String> strings = new ArrayList<String>();
		for (int i = 0; i < n.length(); i += split) {
			strings.add(n.substring(i, Math.min(i + split, n.length())));
		}

		final TextBlock textBlock = Display.create(strings).create(FontConfiguration.blackBlueTrue(NORMAL),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		textBlock.drawU(ug);
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return 42;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return 42;
	}

}
