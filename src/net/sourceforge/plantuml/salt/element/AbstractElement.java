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
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.skin.Pragma;

public abstract class AbstractElement implements Element {

	final protected HColor getBlack() {
		return HColors.BLACK.withDark(HColors.WHITE);
	}

	final protected HColor getColor88() {
		return buildColor("#8", "#8");
	}

	final protected HColor getColorAA() {
		return buildColor("#A", "#6");
	}

	final protected HColor getColorBB() {
		return buildColor("#B", "#5");
	}

	final protected HColor getColorDD() {
		return buildColor("#D", "#3");
	}

	final protected HColor getColorEE() {
		return buildColor("#E", "#2");
	}

	final protected HColor getWhite() {
		return HColors.WHITE.withDark(HColors.BLACK);
	}

	private HColor buildColor(String color1, String color2) {
		final HColor tmp1 = HColorSet.instance().getColorOrWhite(color1);
		final HColor tmp2 = HColorSet.instance().getColorOrWhite(color2);
		return tmp1.withDark(tmp2);
	}

	final protected FontConfiguration blackBlueTrue(UFont font) {
		return FontConfiguration.blackBlueTrue(font);
	}
	
	final protected Pragma getPragma() {
		return Pragma.createEmpty();
	}

}
