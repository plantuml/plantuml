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

import net.sourceforge.plantuml.cheneer.command.CommandCreateAttribute;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandControl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.utils.BlocLines;

final class CommandCreateAttributeDispatch implements Command<ChenEerDiagram> {

	private final CommandCreateAttribute standard = new CommandCreateAttribute();
	private final CommandCreateCompactAttribute compact = new CommandCreateCompactAttribute();

	@Override
	public CommandExecutionResult execute(ChenEerDiagram diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {
		return (diagram.isCompactNotation() ? compact : standard).execute(diagram, lines, currentPass);
	}

	@Override
	public String explain(BlocLines lines) {
		return standard.explain(lines);
	}

	@Override
	public CommandControl isValid(BlocLines lines) {
		return standard.isValid(lines);
	}

	@Override
	public boolean isEligibleFor(ParserPass pass) {
		return standard.isEligibleFor(pass);
	}

	@Override
	public boolean isCommandForbidden(BlocLines lines) {
		return standard.isCommandForbidden(lines);
	}

}
