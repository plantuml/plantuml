/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.style.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.style.AutomaticCounter;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleLoader;
import net.sourceforge.plantuml.style.StyleScheme;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.style.Value;
import net.sourceforge.plantuml.style.ValueImpl;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.StringLocated;

public class StyleParserLegacy {

	private final static String KEYNAMES = "[-.\\w(), ]+?";
	private final static Pattern2 keyName = MyPattern.cmpile("^[:]?(" + KEYNAMES + ")([%s]+\\*)?[%s]*\\{$");
	private final static Pattern2 propertyAndValue = MyPattern.cmpile("^([\\w]+):?[%s]+(.*?);?$");
	private final static Pattern2 closeBracket = MyPattern.cmpile("^\\}$");

	public static Collection<Style> parse(BlocLines lines, AutomaticCounter counter) throws StyleParsingException {

		final Collection<Style> foo = StyleParser.parse(lines, counter);

		lines = lines.eventuallyMoveAllEmptyBracket();
		final List<Style> result = new ArrayList<>();
		final CssVariables variables = new CssVariables();
		StyleScheme scheme = StyleScheme.REGULAR;

		Context context = new Context();
		final List<Map<PName, Value>> maps = new ArrayList<Map<PName, Value>>();
		boolean inComment = false;
		for (StringLocated s : lines) {
			String trimmed = s.getTrimmed().getString();

			if (trimmed.startsWith("/*") || trimmed.endsWith("*/"))
				continue;
			if (trimmed.startsWith("/'") || trimmed.endsWith("'/"))
				continue;

			if (trimmed.startsWith("/*") || trimmed.startsWith("/'")) {
				inComment = true;
				continue;
			}
			if (trimmed.endsWith("*/") || trimmed.endsWith("'/")) {
				inComment = false;
				continue;
			}
			if (inComment)
				continue;

			if (trimmed.matches("@media.*dark.*\\{")) {
				scheme = StyleScheme.DARK;
				continue;
			}

			if (trimmed.startsWith("--")) {
				variables.learn(trimmed);
				continue;
			}

			final int x = trimmed.lastIndexOf("//");
			if (x != -1)
				trimmed = trimmed.substring(0, x).trim();

			final Matcher2 mKeyNames = keyName.matcher(trimmed);
			if (mKeyNames.find()) {
				String names = mKeyNames.group(1);
				final boolean isRecurse = mKeyNames.group(2) != null;
				if (isRecurse)
					names += "*";

				context = context.push(names);
				maps.add(new EnumMap<PName, Value>(PName.class));
				continue;
			}
			final Matcher2 mPropertyAndValue = propertyAndValue.matcher(trimmed);
			if (mPropertyAndValue.find()) {
				final PName key = PName.getFromName(mPropertyAndValue.group(1), scheme);
				final String value = variables.value(mPropertyAndValue.group(2));
				if (key != null && maps.size() > 0)
					maps.get(maps.size() - 1).put(key, //
							scheme == StyleScheme.REGULAR ? //
									ValueImpl.regular(value, counter) : ValueImpl.dark(value, counter));

				continue;
			}
			final Matcher2 mCloseBracket = closeBracket.matcher(trimmed);
			if (mCloseBracket.find()) {
				if (context.size() > 0) {
					final Collection<StyleSignatureBasic> signatures = context.toSignatures();
					for (StyleSignatureBasic signature : signatures) {
						Map<PName, Value> tmp = maps.get(maps.size() - 1);
						if (signature.isWithDot())
							tmp = StyleLoader.addPriorityForStereotype(tmp);
						if (tmp.size() > 0) {
							final Style style = new Style(signature, tmp);
							result.add(style);
						}
					}
					context = context.pop();
					maps.remove(maps.size() - 1);
				} else {
					scheme = StyleScheme.REGULAR;
				}
			}
		}

		System.err.println("foo1=" + foo.size());
		System.err.println("result=" + result.size());
		if (foo.size() != result.size() || foo.size() < 10) {
			print_debug(foo);
			print_debug(result);
		}

		// return Collections.unmodifiableList(result);
		return Collections.unmodifiableCollection(foo);

	}

	private static void print_debug(Collection<Style> list) {
		System.err.println("=====================");
		int i = 0;
		for (Style style : list) {
			System.err.println("style=" + i + " " + style.getSignature());
			i++;
		}
		System.err.println("=====================");

	}

}