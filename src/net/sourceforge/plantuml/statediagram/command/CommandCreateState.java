/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 12075 $
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
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.statediagram.StateDiagram;

public class CommandCreateState extends SingleLineCommand2<StateDiagram> {

	public CommandCreateState() {
		super(getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("(?:state\\s+)"), //
				new RegexLeaf("DISPLAY", "(?:\"([^\"]+)\"\\s+as\\s+)?"), //
				new RegexLeaf("CODE", "([\\p{L}0-9_.]+)"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(StateDiagram system, RegexResult arg2) {
		String display = arg2.get("DISPLAY", 0);
		final Code code = Code.of(arg2.get("CODE", 0));
		if (display == null) {
			display = code.getCode();
		}
		final String stereotype = arg2.get("STEREOTYPE", 0);
		final LeafType type = getTypeFromStereotype(stereotype);
		final IEntity ent = system.getOrCreateLeaf(code, type);
		if (system.checkConcurrentStateOk(code) == false) {
			return CommandExecutionResult.error("The state " + code
					+ " has been created in a concurrent state : it cannot be used here.");
		}
		ent.setDisplay(Display.getWithNewlines(display));

		if (stereotype != null) {
			ent.setStereotype(new Stereotype(stereotype));
		}
		final String urlString = arg2.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(system.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			ent.addUrl(url);
		}
		final String color = arg2.get("COLOR", 0);
		if (color != null) {
			ent.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(color));
		}
		return CommandExecutionResult.ok();
	}

	private LeafType getTypeFromStereotype(String stereotype) {
		if ("<<choice>>".equalsIgnoreCase(stereotype)) {
			return LeafType.STATE_CHOICE;
		}
		if ("<<fork>>".equalsIgnoreCase(stereotype)) {
			return LeafType.STATE_FORK_JOIN;
		}
		if ("<<join>>".equalsIgnoreCase(stereotype)) {
			return LeafType.STATE_FORK_JOIN;
		}
		if ("<<end>>".equalsIgnoreCase(stereotype)) {
			return LeafType.CIRCLE_END;
		}
		return null;
	}

}
