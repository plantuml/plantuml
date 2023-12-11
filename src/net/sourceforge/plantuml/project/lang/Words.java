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
	public final static String FOR = "for";
	public final static String FROM = "from";
	public final static String ON = "on";
	public final static String THE = "the";
	public final static String TO = "to";

	public static IRegex zeroOrMore(String... words) {
		final IRegex tmp[] = new IRegex[words.length];
		for (int i = 0; i < words.length; i++)
			tmp[i] = new RegexLeaf(words[i]);

		final RegexOr or = new RegexOr(tmp);
		return new RegexRepeatedZeroOrMore(new RegexConcat(RegexLeaf.spaceOneOrMore(), or));
	}

	public static IRegex exactly(String... words) {
		final IRegex tmp[] = new IRegex[words.length];
		for (int i = 0; i < words.length; i++)
			tmp[i] = new RegexConcat(RegexLeaf.spaceOneOrMore(), new RegexLeaf(words[i]));

		return new RegexConcat(tmp);
	}

	public static IRegex concat(IRegex... expressions) {
		return new RegexConcat(expressions);
	}

}
