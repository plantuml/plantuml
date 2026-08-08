/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2026, Arnaud Roques
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 */
package net.sourceforge.plantuml.cheneer;

import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

final class CommandNotationCompact extends SingleLineCommand2<ChenEerDiagram> {

	CommandNotationCompact() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandNotationCompact.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("notation"), RegexLeaf.spaceOneOrMore(), new RegexLeaf("compact"), //
				RegexLeaf.end());
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		return null;
	}

	@Override
	protected CommandExecutionResult executeArg(ChenEerDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		if (diagram.useCompactNotation() == false)
			return CommandExecutionResult.error("notation compact must precede all entities and relationships");

		return CommandExecutionResult.ok();
	}

}
