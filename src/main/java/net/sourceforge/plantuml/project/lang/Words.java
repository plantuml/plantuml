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
package net.sourceforge.plantuml.project.lang;

import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexOr;
import com.plantuml.ubrex.builder.UBrexPart;
import com.plantuml.ubrex.builder.UBrexZeroOrMore;

import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexRepeatedZeroOrMore;

public class Words {

	public final static String AFTER = "after";
	public final static String AND = "and";
	public final static String AS = "as";
	public final static String AT = "at";
	public final static String BEFORE = "before";
	public final static String COMPLETION = "completion";
	public final static String DAY = "day";
	public final static String END = "end";
	public final static String FOR = "for";
	public final static String FROM = "from";
	public final static String ON = "on";
	public final static String START = "start";
	public final static String THE = "the";
	public final static String TO = "to";
	public final static String WEEK = "week";
	public final static String WITH = "with";
	public final static String WORKING = "working";

	public final static String THEN = "then";
	public final static String IT = "it";

	public static IRegex zeroOrMore(String... words) {
		final IRegex tmp[] = new IRegex[words.length];
		for (int i = 0; i < words.length; i++)
			tmp[i] = new RegexLeaf(words[i]);

		final RegexOr or = new RegexOr(tmp);
		return new RegexRepeatedZeroOrMore(new RegexConcat(RegexLeaf.spaceOneOrMore(), or));
	}

	public static UBrexPart uzeroOrMore(String... words) {
		final UBrexLeaf tmp[] = new UBrexLeaf[words.length];
		for (int i = 0; i < words.length; i++)
			tmp[i] = new UBrexLeaf(words[i]);

		final UBrexOr or = new UBrexOr(tmp);
		return new UBrexZeroOrMore(UBrexConcat.build(or, UBrexLeaf.spaceOneOrMore()));
	}

	public static UBrexPart uoneOf(String... words) {
		final UBrexLeaf tmp[] = new UBrexLeaf[words.length];
		for (int i = 0; i < words.length; i++)
			tmp[i] = new UBrexLeaf(words[i]);

		return new UBrexOr(tmp);
	}

	public static IRegex exactly(String... words) {
		final IRegex tmp[] = new IRegex[words.length];
		for (int i = 0; i < words.length; i++)
			tmp[i] = new RegexConcat(RegexLeaf.spaceOneOrMore(), new RegexLeaf(words[i]));

		return new RegexConcat(tmp);
	}

	public static UBrexPart uexactly(String... words) {
		final UBrexPart tmp[] = new UBrexPart[words.length * 2];
		for (int i = 0; i < words.length; i++) {
			tmp[i * 2] = UBrexLeaf.spaceOneOrMore();
			tmp[i * 2 + 1] = new UBrexLeaf(words[i]);
		}
		return UBrexConcat.build(tmp);
	}

	public static UBrexPart uexactly2(String... words) {
		final UBrexPart tmp[] = new UBrexPart[words.length * 2 - 1];
		for (int i = 0; i < words.length; i++) {
			if (i > 0)
				tmp[i * 2 - 1] = UBrexLeaf.spaceOneOrMore();
			tmp[i * 2] = new UBrexLeaf(words[i]);
		}
		return UBrexConcat.build(tmp);
	}

	public static UBrexPart usingle(String word) {
		return new UBrexLeaf(word);
	}

	public static IRegex single(String word) {
		return new RegexLeaf(word);
	}

	public static IRegex namedSingle(String name, String word) {
		return new RegexLeaf(1, name, word);
	}

	public static IRegex namedOneOf(String name, String... words) {
		final IRegex tmp[] = new IRegex[words.length];
		for (int i = 0; i < words.length; i++)
			tmp[i] = new RegexLeaf(words[i]);

		return new RegexOr(name, tmp);
	}

	public static IRegex concat(IRegex... expressions) {
		return new RegexConcat(expressions);
	}

	public static UBrexPart uconcat(UBrexPart... expressions) {
		return UBrexConcat.build(expressions);
	}

}
