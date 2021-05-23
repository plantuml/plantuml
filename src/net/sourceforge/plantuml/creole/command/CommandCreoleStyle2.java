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
package net.sourceforge.plantuml.creole.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.creole.legacy.StripeSimple;
import net.sourceforge.plantuml.graphic.FontStyle;

public class CommandCreoleStyle2 extends CommandCreoleCache implements Command {

	public static Command createCreole(FontStyle style) {
		return new CommandCreoleStyle2("^(" + style.getCreoleSyntax() + "(.+?)" + style.getCreoleSyntax() + ")", style);
	}

	public static Command createLegacy(FontStyle style) {
		return new CommandCreoleStyle2(
				"^((" + style.getActivationPattern() + ")(.+?)" + style.getDeactivationPattern() + ")", style);
	}

	public static Command createLegacyEol(FontStyle style) {
		return new CommandCreoleStyle2("^((" + style.getActivationPattern() + ")(.+))$", style);
	}

	private CommandCreoleStyle2(String p, FontStyle style) {
		super(p);
	}

	public String executeAndGetRemaining(final String line, StripeSimple stripe) {
		final Matcher2 m = mypattern.matcher(line);
		if (m.find() == false) {
			throw new IllegalStateException();
		}

		final int groupCount = m.groupCount();
		final String part1 = m.group(groupCount);
		final String part2 = line.substring(m.group(1).length());
		return StringUtils.BOLD_START + part1 + StringUtils.BOLD_END + part2;

	}

	public int matchingSize(String line) {
		final Matcher2 m = mypattern.matcher(line);
		if (m.find() == false) {
			return 0;
		}
		return m.group(1).length();
	}

}
