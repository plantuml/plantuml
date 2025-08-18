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

import java.util.List;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.UnicodeBracketedExpression;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.creole.legacy.StripeSimple;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontStyle;

public class CommandCreoleStyle implements Command {

	@Override
	public String startingChars() {
		return "</*_~-";
	}

	private final FontStyle style;
	private final boolean tryExtendedColor;
	private final UnicodeBracketedExpression ubrex;

	public static Command createCreole(FontStyle style) {
		final String ubrexString = style.getUbrexCreoleSyntax() + "〶$V=〄+〴.->〘" + style.getUbrexCreoleSyntax() + "〙";
		return new CommandCreoleStyle(ubrexString, style, false);
	}

	public static Command createLegacy(FontStyle style) {
		final String ubrexString = style.getUbrexActivationPattern() + "〶$V=〄>〘" + style.getUbrexDeactivationPattern()
				+ "〙";
		return new CommandCreoleStyle(ubrexString, style, style.canHaveExtendedColor());
	}

	public static Command createLegacyEol(FontStyle style) {
		final String ubrexString = style.getUbrexActivationPattern() + "〶$V=〇+〴.";
		return new CommandCreoleStyle(ubrexString, style, style.canHaveExtendedColor());
	}

	private CommandCreoleStyle(String ubrexString, FontStyle style, boolean tryExtendedColor) {
		this.ubrex = UnicodeBracketedExpression.build(ubrexString);
		this.style = style;
		this.tryExtendedColor = tryExtendedColor;
	}

	private HColor getExtendedColor(UMatcher matcher) {
		if (tryExtendedColor) {
			final List<String> extendedColor = matcher.getCapture("XC");
			if (extendedColor.size() > 0)
				return HColorSet.instance().getColorOrWhite(extendedColor.get(0));
		}

		return null;
	}

	public String executeAndGetRemaining(final String line, StripeSimple stripe) {
		final UMatcher matcher = ubrex.match(line);

		final List<String> value = matcher.getCapture("V");
		final String accepted = matcher.getAcceptedMatch();
		if (value.size() == 0)
			throw new IllegalStateException();

		final FontConfiguration fc1 = stripe.getActualFontConfiguration();
		final FontConfiguration fc2 = new AddStyle(style, getExtendedColor(matcher)).apply(fc1);
		stripe.setActualFontConfiguration(fc2);

		stripe.analyzeAndAdd(value.get(0));
		stripe.setActualFontConfiguration(fc1);
		return line.substring(accepted.length());
	}

	public int matchingSize(String line) {
		final UMatcher matcher = ubrex.match(line);
		final List<String> value = matcher.getCapture("V");
		if (value.size() == 0)
			return 0;

		return value.get(0).length();
	}

}
