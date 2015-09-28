/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4762 $
 *
 */
package net.sourceforge.plantuml.statediagram.command;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.statediagram.StateDiagram;

public class CommandCreatePackageState extends SingleLineCommand2<StateDiagram> {

	public CommandCreatePackageState() {
		super(getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^state[%s]+"), //
				new RegexOr(//
						new RegexConcat(//
								new RegexLeaf("CODE1", "([\\p{L}0-9_.]+)[%s]+"), //
								new RegexLeaf("DISPLAY1", "as[%s]+[%g]([^%g]+)[%g]")), //
						new RegexConcat(//
								new RegexLeaf("DISPLAY2", "(?:[%g]([^%g]+)[%g][%s]+as[%s]+)?"), //
								new RegexLeaf("CODE2", "([\\p{L}0-9_.]+)"))), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				new RegexLeaf("[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("LINECOLOR", "(?:##(?:\\[(dotted|dashed|bold)\\])?(\\w+)?)?"), //
				new RegexLeaf("(?:[%s]*\\{|[%s]+begin)$"));
	}
	
	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}


	private String getNotNull(RegexResult arg, String v1, String v2) {
		if (arg.get(v1, 0) == null) {
			return arg.get(v2, 0);
		}
		return arg.get(v1, 0);
	}

	@Override
	protected CommandExecutionResult executeArg(StateDiagram diagram, RegexResult arg) {
		final IGroup currentPackage = diagram.getCurrentGroup();
		final Code code = Code.of(getNotNull(arg, "CODE1", "CODE2"));
		String display = getNotNull(arg, "DISPLAY1", "DISPLAY2");
		if (display == null) {
			display = code.getFullName();
		}
		final IEntity p = diagram.getOrCreateGroup(code, Display.getWithNewlines(display), GroupType.STATE,
				currentPackage);
		final String stereotype = arg.get("STEREOTYPE", 0);
		if (stereotype != null) {
			p.setStereotype(new Stereotype(stereotype));
		}
		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			p.addUrl(url);
		}
		
		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());

		final HtmlColor lineColor = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("LINECOLOR", 1));
		if (lineColor != null) {
			colors = colors.add(ColorType.LINE, lineColor);
		}
		if (arg.get("LINECOLOR", 0) != null) {
			colors = colors.addLegacyStroke(arg.get("LINECOLOR", 0));
		}
		p.setColors(colors);

//		p.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("COLOR", 0)));
//		p.setSpecificColorTOBEREMOVED(ColorType.LINE, diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("LINECOLOR", 1)));
//		p.applyStroke(arg.get("LINECOLOR", 0));
		return CommandExecutionResult.ok();
	}

}
