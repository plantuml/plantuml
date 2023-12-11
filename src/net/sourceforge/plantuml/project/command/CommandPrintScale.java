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
package net.sourceforge.plantuml.project.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandPrintScale extends SingleLineCommand2<GanttDiagram> {

	public CommandPrintScale() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPrintScale.class.getName(), RegexLeaf.start(), //
				new RegexOr(new RegexLeaf("projectscale"), //
						new RegexLeaf("ganttscale"), //
						new RegexLeaf("printscale")), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr("SCALE", //
						new RegexLeaf("yearly"), //
						new RegexLeaf("quarterly"), //
						new RegexLeaf("monthly"), //
						new RegexLeaf("daily"), //
						new RegexLeaf("weekly")), //
				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("DATE", "(with)"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("calendar"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("date"))), //
				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("zoom"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("ZOOM", "([.\\d]+)"))), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(GanttDiagram diagram, LineLocation location, RegexResult arg) {
		final String scaleString = arg.get("SCALE", 0);
		final PrintScale scale = PrintScale.fromString(scaleString);
		diagram.setPrintScale(scale);

		final String zoom = arg.get("ZOOM", 0);
		if (zoom != null)
			diagram.setFactorScale(Double.parseDouble(zoom));

		final String withCalendarDate = arg.get("DATE", 0);
		if (withCalendarDate != null)
			diagram.setWithCalendarDate(true);

		return CommandExecutionResult.ok();
	}

}
