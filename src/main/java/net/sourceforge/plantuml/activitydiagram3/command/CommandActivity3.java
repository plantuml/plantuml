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
package net.sourceforge.plantuml.activitydiagram3.command;

import java.util.regex.Matcher;

import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandActivity3 extends SingleLineCommand2<ActivityDiagram3> {

	public static final String endingGroup() {
		return "(" //
				+ ";[%s]*(\\<\\<[%pLN_-]+\\>\\>(?:[%s]*\\<\\<[%pLN_-]+\\>\\>)*)?" //
				+ "|" //
				+ Matcher.quoteReplacement("\\\\") // that is simply \ character
				+ "|" //
				+ "(?<![/|<}\\]])[/<}]" // About /<}
				+ "|" //
				+ "(?<![/|}\\]])\\]" // About ]
				+ "|" //
				+ "(?<!\\</?\\w{1,5})(?<!\\<img[^>]{1,999})(?<!\\<[&$]\\w{1,999})(?<!\\>)\\>" // About >
				+ "|" //
				+ "(?<!\\|.{1,999})\\|" // About |
				+ ")";
	}

	private static final String endingGroupShort() {
		return "(" //
				+ ";[%s]*(\\<\\<[%pLN_-]+\\>\\>(?:[%s]*\\<\\<[%pLN_-]+\\>\\>)*)?" //
				+ "|" //
				+ Matcher.quoteReplacement("\\\\") // that is simply \ character
				+ "|" //
				+ "(?<![/|<}\\]])[/<}]" // About /<}
				+ "|" //
				+ "(?<![/|}\\]])\\]" // About ]
				+ "|" //
				+ "(?<!\\</?\\w{1,5})(?<!\\<img[^>]{1,999})(?<!\\<[&$]\\w{1,999})(?<!\\>)\\>" // About >
				+ "|" //
				+ "\\|" // About |
				+ ")";
	}

	public static void main(String[] args) {
		System.err.println(Matcher.quoteReplacement("\\\\"));
		System.err.println(Matcher.quoteReplacement("\\\\").equals("\\\\\\\\"));
	}

	public CommandActivity3() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandActivity3.class.getName(), RegexLeaf.start(), //
				UrlBuilder.OPTIONAL, //
				color().getRegex(), //
				StereotypePattern.optional("STEREO"), //
				new RegexLeaf(":"), //
				new RegexLeaf(1, "LABEL", "(.*?)"), //
				new RegexLeaf(2, "STYLE", endingGroupShort()), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram3 diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {

		final Url url;
		if (arg.get("URL", 0) == null) {
			url = null;
		} else {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			url = urlBuilder.getUrl(arg.get("URL", 0));
		}

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		String stereo = arg.get("STEREO", 0);
		if (stereo == null)
			stereo = arg.get("STYLE", 1);

		Stereotype stereotype = null;
		if (stereo != null) {
			stereotype = Stereotype.build(stereo);
			colors = colors.applyStereotype(stereotype, diagram.getSkinParam(), ColorParam.activityBackground);
		}

		BoxStyle.checkDeprecatedWarning(diagram, arg.get("STYLE", 0));

		BoxStyle style = BoxStyle.fromString(arg.get("STEREO", 0));
		if (style == BoxStyle.PLAIN)
			style = BoxStyle.fromString(arg.get("STYLE", 0));

		final Display display = Display.getWithNewlines2(diagram.getPragma(), arg.get("LABEL", 0));
		return diagram.addActivity(display, style, url, colors, stereotype);
	}

}
