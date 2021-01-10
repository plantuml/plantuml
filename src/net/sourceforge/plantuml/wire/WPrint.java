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
package net.sourceforge.plantuml.wire;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class WPrint {

	private final UTranslate position;
	private final HColor color;
	private final Display label;
	private final ISkinParam skinParam;

	public WPrint(ISkinParam skinParam, UTranslate position, HColor color, Display label) {
		this.position = position;
		this.skinParam = skinParam;
		this.label = label;
		this.color = color == null ? HColorUtils.BLACK : color;
	}

	private TextBlock getTextBlock() {
		final FontConfiguration fontConfiguration = FontConfiguration.blackBlueTrue(UFont.sansSerif(10))
				.changeColor(color);
		return label.create(fontConfiguration, HorizontalAlignment.LEFT, skinParam);
	}

	public UChange getPosition() {
		return position;
	}

	public void drawMe(UGraphic ug) {
		getTextBlock().drawU(ug);
	}

	public double getHeight(StringBounder stringBounder) {
		return getTextBlock().calculateDimension(stringBounder).getHeight();
	}

}
