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
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.NamespaceStrategy;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;

public class CommandNamespaceEmpty extends SingleLineCommand2<ClassDiagram> {

	public CommandNamespaceEmpty() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandNamespaceEmpty.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("namespace"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("NAME", "([\\p{L}0-9_][-\\p{L}0-9_.:\\\\]*)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp1(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\{"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\}"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram diagram, LineLocation location, RegexResult arg) {
		final String idShort = arg.get("NAME", 0);
		final Ident idNewLong = diagram.buildLeafIdent(idShort);
		final Code code = diagram.V1972() ? idNewLong : diagram.buildCode(idShort);
		final IGroup currentPackage = diagram.getCurrentGroup();
		final Display display = Display.getWithNewlines(code);
		diagram.gotoGroup(idNewLong, code, display, GroupType.PACKAGE, currentPackage, NamespaceStrategy.MULTIPLE);
		final IEntity p = diagram.getCurrentGroup();
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

		final String color = arg.get("COLOR", 0);
		if (color != null) {
			p.setSpecificColorTOBEREMOVED(ColorType.BACK,
					diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(color));
		}
		diagram.endGroup();
		return CommandExecutionResult.ok();
	}

}
