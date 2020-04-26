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
package net.sourceforge.plantuml.ugraphic.color;

import java.awt.Color;

import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class HColorUtils {

	public static final HColor BLACK;
	public static final HColor WHITE;
	public static final HColor RED_LIGHT;
	public static final HColor RED_DARK;
	public static final HColor RED;
	public static final HColor GREEN;
	public static final HColor BLUE;
	public static final HColor GRAY;
	public static final HColor LIGHT_GRAY;
	public static final HColor MY_YELLOW;
	public static final HColor MY_RED;
	public static final HColor MY_GREEN;

	public static final HColor COL_C82930;
	public static final HColor COL_F24D5C;
	public static final HColor COL_1963A0;
	public static final HColor COL_4177AF;
	public static final HColor COL_B38D22;
	public static final HColor COL_FFFF44;
	public static final HColor COL_038048;
	public static final HColor COL_84BE84;
	public static final HColor COL_DDDDDD;
	public static final HColor COL_EEEEEE;
	public static final HColor COL_FBFB77;
	public static final HColor COL_ADD1B2;
	public static final HColor COL_A9DCDF;
	public static final HColor COL_E3664A;
	public static final HColor COL_EB937F;
	public static final HColor COL_B4A7E5;
	public static final HColor COL_527BC6;
	public static final HColor COL_D1DBEF;
	public static final HColor COL_D7E0F2;
	public static final HColor COL_989898;
	public static final HColor COL_BBBBBB;

	static {

		final HColorSet set = HColorSet.instance();

		BLACK = set.getColorIfValid("black");
		WHITE = set.getColorIfValid("white");
		RED_LIGHT = set.getColorIfValid("#FEF6F3");
		RED_DARK = set.getColorIfValid("#CD0A0A");
		RED = set.getColorIfValid("#FF0000");
		GREEN = set.getColorIfValid("#00FF00");
		BLUE = set.getColorIfValid("#0000FF");
		GRAY = set.getColorIfValid("#808080");
		LIGHT_GRAY = set.getColorIfValid("#C0C0C0");
		MY_YELLOW = set.getColorIfValid("#FEFECE");
		MY_RED = set.getColorIfValid("#A80036");
		MY_GREEN = set.getColorIfValid("#33FF02");

		COL_C82930 = set.getColorIfValid("#C82930");
		COL_F24D5C = set.getColorIfValid("#F24D5C");
		COL_1963A0 = set.getColorIfValid("#1963A0");
		COL_4177AF = set.getColorIfValid("#4177AF");
		COL_B38D22 = set.getColorIfValid("#B38D22");
		COL_FFFF44 = set.getColorIfValid("#FFFF44");
		COL_038048 = set.getColorIfValid("#038048");
		COL_84BE84 = set.getColorIfValid("#84BE84");
		COL_DDDDDD = set.getColorIfValid("#DDDDDD");
		COL_EEEEEE = set.getColorIfValid("#EEEEEE");
		COL_FBFB77 = set.getColorIfValid("#FBFB77");
		COL_ADD1B2 = set.getColorIfValid("#ADD1B2");
		COL_A9DCDF = set.getColorIfValid("#A9DCDF");
		COL_E3664A = set.getColorIfValid("#E3664A");
		COL_EB937F = set.getColorIfValid("#EB937F");
		COL_B4A7E5 = set.getColorIfValid("#B4A7E5");
		COL_527BC6 = set.getColorIfValid("#527BC6");
		COL_D1DBEF = set.getColorIfValid("#D1DBEF");
		COL_D7E0F2 = set.getColorIfValid("#D7E0F2");
		COL_989898 = set.getColorIfValid("#989898");
		COL_BBBBBB = set.getColorIfValid("#BBBBBB");

	}

	public static HColor noGradient(HColor color) {
		if (color instanceof HColorGradient) {
			return ((HColorGradient) color).getColor1();
		}
		return color;
	}

	public static UChange changeBack(UGraphic ug) {
		final HColor color = ug.getParam().getColor();
		if (color == null) {
			return new HColorNone().bg();
		}
		return color.bg();
	}

	public static HColor transparent() {
		return new HColorSimple(new Color(0, 0, 0, 0), false);
	}

}
