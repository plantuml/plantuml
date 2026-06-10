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

import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.annotation.Explain;
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
import net.sourceforge.plantuml.stereo.Stereogroup;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.warning.Warning;

public class CommandActivity3 extends SingleLineCommand2<ActivityDiagram3> {

	public CommandActivity3() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandActivity3.class.getName(), RegexLeaf.start(), //
				UrlBuilder.OPTIONAL, //
				color().getRegex(), //
				StereotypePattern.optional("IGNORED"), //
				new RegexLeaf(":"), //
				new RegexLeaf(1, "LABEL", "(.*?)"), //
				new RegexLeaf(";"), //
				RegexLeaf.spaceZeroOrMore(), //
				Stereogroup.optionalStereogroup(), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final StringBuilder sb = new StringBuilder();

		// ':label;' appends an activity to the current flow. The stereotypes
		// written after the ';' carry the colors and may select a box style
		// (see Stereogroup).
		sb.append("Adding the activity \"").append(arg.get("LABEL", 0)).append("\"");

		final Stereogroup stereogroup = Stereogroup.build(arg);
		if (stereogroup.isEmpty() == false) {
			sb.append(", stereotyped ").append(arg.get("STEREOGROUP", 0));
			final BoxStyle style = stereogroup.getBoxStyle();
			if (style != BoxStyle.PLAIN)
				sb.append(" (box style: ").append(style.name()).append(")");
		}

		if (arg.get(UrlBuilder.URL_KEY, 0) != null)
			sb.append(", with a URL link");

		// Mirror the two deprecation warnings emitted by executeArg; note that
		// the leading color is parsed but no longer applied.
		if (arg.get("IGNORED", 0) != null)
			sb.append(" (the stereotype before the ':' is ignored: write it after the ';')");

		if (arg.get("COLOR", 0) != null)
			sb.append(" (deprecated and ignored color syntax: write <<").append(arg.get("COLOR", 0))
					.append(">> after the ';')");

		return sb.toString();
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram3 diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {

		final Url url;
		if (arg.get(UrlBuilder.URL_KEY, 0) == null) {
			url = null;
		} else {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			url = urlBuilder.getUrl(arg.get(UrlBuilder.URL_KEY, 0));
		}

		final Stereogroup stereogroup = Stereogroup.build(arg);
		final Colors colors = stereogroup.getInnerColors(diagram.getSkinParam().getIHtmlColorSet());

		if (arg.get("IGNORED", 0) != null)
			diagram.addWarning(new Warning("You must use stereotype at the end of the line after the ';'"));

		if (arg.get("COLOR", 0) != null)
			diagram.addWarning(new Warning("This syntax is deprecated, you must add <<" + arg.get("COLOR", 0)
					+ ">> at the end of the line, after the ';'"));

		final Stereotype stereotype = stereogroup.buildStereotype();

		final BoxStyle style = stereogroup.getBoxStyle();

		final Display display = Display.getWithNewlines2(diagram.getPragma(), arg.get("LABEL", 0));
		return diagram.addActivity(display, style, url, colors, stereotype);
	}

}
