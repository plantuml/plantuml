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
package net.sourceforge.plantuml.cucadiagram;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.WithPorts;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class Body3 extends AbstractTextBlock implements TextBlock, WithPorts {

	private final List<CharSequence> rawBody = new ArrayList<>();
	private final FontParam fontParam;
	private final ISkinParam skinParam;
	private final Stereotype stereotype;
	private final Style style;

	public Body3(List<CharSequence> rawBody_, FontParam fontParam, ISkinParam skinParam, Stereotype stereotype,
			Style style) {
		for (CharSequence s : rawBody_) {
			this.rawBody.add(VisibilityModifier.replaceVisibilityModifierByUnicodeChar(s.toString(), true));
		}
		this.fontParam = fontParam;
		this.skinParam = skinParam;
		this.stereotype = stereotype;
		this.style = style;
	}

	public void drawU(UGraphic ug) {
		getTextBlock().drawU(ug);

	}

	private TextBlock getTextBlock() {
		Display display = Display.create(rawBody);

		FontConfiguration config;
		if (style != null) {
			config = new FontConfiguration(skinParam, style);
		} else {
			config = new FontConfiguration(skinParam, fontParam, stereotype);
		}

		TextBlock foo = display.create(config, HorizontalAlignment.LEFT, skinParam);
		return foo;
	}

	public Ports getPorts(StringBounder stringBounder) {
		return new Ports();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return getTextBlock().calculateDimension(stringBounder);
	}

}
