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
package net.sourceforge.plantuml.klimt.creole.command;

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Parser;
import net.sourceforge.plantuml.klimt.creole.legacy.StripeSimple;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.style.ISkinSimple;

public class CommandCreoleOpenIcon implements Command {

	@Override
	public Collection<String> starters() {
		return Arrays.asList("<#", "<&");
	}

	private static final Pattern2 pattern = Pattern2.cmpile("^(" + Splitter.openiconPattern + ")");

	private CommandCreoleOpenIcon() {
	}

	public static Command create() {
		return new CommandCreoleOpenIcon();
	}

	@Override
	public int matchingSize(String line, int pos) {
		final Matcher2 m = pattern.matcher(line, pos);
		if (m.find() == false)
			return 0;

		return m.group(1).length();
	}

	@Override
	public int executeAndAdvance(ISkinSimple skinSimple, String line, int pos, StripeSimple stripe) {
		final Matcher2 m = pattern.matcher(line, pos);
		if (m.find() == false)
			throw new IllegalStateException();

		final String colorName1 = m.group(2);
		final String src = m.group(3);
		final double scale = Parser.getScale(m.group(4), 1);
		final String colorName2 = Parser.getColor(m.group(4));

		final String colorName = colorName1 == null ? colorName2 : colorName1;
		HColor color = null;
		if (colorName != null) {
			final ISkinSimple skinParam = stripe.getSkinParam();
			color = skinParam.getIHtmlColorSet().getColorOrWhite(colorName);
		}
		stripe.addOpenIcon(src, scale, color);
		return m.group(1).length();
	}

}
