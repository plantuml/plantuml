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
package net.sourceforge.plantuml.activitydiagram.command;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandPartition extends SingleLineCommand2<ActivityDiagram> {

	public CommandPartition() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPartition.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("partition"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("NAME", "([%g][^%g]+[%g]|\\S+)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOr(//
						color().getRegex(), //
						new RegexLeaf("LEGACYCOLORIGNORED", "(#[0-9a-fA-F]{6}|#?\\w+)?")), //
				StereotypePattern.optional("STEREOTYPE"), //
				new RegexLeaf("\\{?"), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(arg.get("NAME", 0)));

		diagram.gotoGroup(quark, Display.getWithNewlines(quark.getName()), GroupType.PACKAGE);
		final Entity p = diagram.getCurrentGroup();

		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		if (colors.isEmpty() == false)
			p.setColors(colors);

		p.setStereotype(Stereotype.build(arg.get("STEREOTYPE", 0)));

		return CommandExecutionResult.ok();
	}

}
