/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2026, Arnaud Roques
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
 * Original Author:  kolulu23
 * Contribution: The-Lum
 * 
 */
package net.sourceforge.plantuml.packetdiag.command;

import java.util.Optional;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.packetdiag.PacketDiagram;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

/**
 * Parses and applies the {@code scale_direction=<ltr|rtl>} directive for {@code packetdiag} diagrams.
 * <p>
 * Controls the direction in which the bit scale is rendered by calling
 * {@link PacketDiagram#setScaleDirection(String)}.
 * </p>
 */
public class CommandScaleDirection extends SingleLineCommand2<PacketDiagram> {

	public CommandScaleDirection() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandScaleDirection.class.getName(), RegexLeaf.start(), //
						new RegexLeaf("scale_direction"), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("="), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf(1, "DIR", "(ltr|rtl);?"), //
						RegexLeaf.spaceZeroOrMore(), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(PacketDiagram system, LineLocation location, RegexResult arg, ParserPass currentPass) throws NoSuchColorException {
		final String dir = Optional.ofNullable(arg.get("DIR", 0))
				.map(String::toUpperCase)
				.orElse("LTR");

		system.setScaleDirection(dir);
		return CommandExecutionResult.ok();
	}
}
