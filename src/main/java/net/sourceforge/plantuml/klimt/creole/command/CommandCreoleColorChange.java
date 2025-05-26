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

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.color.NoSuchColorRuntimeException;
import net.sourceforge.plantuml.klimt.creole.legacy.StripeSimple;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.MyPattern;
import net.sourceforge.plantuml.regex.Pattern2;

public class CommandCreoleColorChange implements Command {

	@Override
	public String startingChars() {
		return "<";
	}

	private static final Pattern2 pattern = MyPattern.cmpile("^(" + Splitter.fontColorPattern + "(.*?)\\</color\\>)");

	private static final Pattern2 patternEol = MyPattern.cmpile("^(" + Splitter.fontColorPattern + "(.*)$)");

	private final Pattern2 mypattern;

	public static Command create() {
		return new CommandCreoleColorChange(pattern);
	}

	public static Command createEol() {
		return new CommandCreoleColorChange(patternEol);
	}

	private CommandCreoleColorChange(Pattern2 pattern) {
		this.mypattern = pattern;
	}

	public int matchingSize(String line) {
		final Matcher2 m = mypattern.matcher(line);
		if (m.find() == false)
			return 0;

		return m.group(2).length();
	}

	public String executeAndGetRemaining(String line, StripeSimple stripe) throws NoSuchColorRuntimeException {
		final Matcher2 m = mypattern.matcher(line);
		if (m.find() == false)
			throw new IllegalStateException();

		final FontConfiguration fc1 = stripe.getActualFontConfiguration();
		final String s = m.group(2);
		try {
			final HColor color = HColorSet.instance().getColor(s);
			final FontConfiguration fc2 = fc1.changeColor(color);
			stripe.setActualFontConfiguration(fc2);
		} catch (NoSuchColorException e) {
			// Too late for parsing error
			// So we just ignore
		}
		stripe.analyzeAndAdd(m.group(3));
		stripe.setActualFontConfiguration(fc1);
		return line.substring(m.group(1).length());
	}

}
