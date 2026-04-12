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

import java.util.Map;
import java.util.Optional;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.nwdiag.NwDiagram;
import net.sourceforge.plantuml.packetdiag.PacketDiagram;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

/**
 * Parses packet field declarations expressed as a bit number, a bit range, or a {@code *} list of bit.
 * <p>
 * This command is responsible for turning a textual field declaration into one or more packet items
 * added to the current {@link PacketDiagram} (for example using explicit ranges and optional attributes).
 * </p>
 * <p>
 * Supported forms include:
 * </p>
 * <ul>
 *   <li>{@code N: <desc>} for a single-bit field,</li>
 *   <li>{@code A-B: <desc>} for a multi-bit field,</li>
 *   <li>{@code * <desc>} for an auto-positioned field (when supported by the parser).</li>
 * </ul>
 * Attributes (such as length) may be provided in brackets depending on the implementation.
 */
public class CommandNumRange extends SingleLineCommand2<PacketDiagram> {

	public CommandNumRange() {
		super(getRegexConcat());
	}

	//	^(\*|\d{1,7}(?:-\d{1,7})?)(?::)?\s+(.*?)(?:\s+\[(.*?)\])?$
	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandNumRange.class.getName(), RegexLeaf.start(), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexOr( //
										new RegexLeaf(1, "RANGE_UNIT", "(\\d{1,7})(?::)?"),
										new RegexLeaf(2, "RANGE", "(\\d{1,7})(?:-(\\d{1,7}))(?::)?"), //
										new RegexLeaf("\\*") //
						), //
						new RegexLeaf(1, "DESC", "\\s+(.*?)"), //
						new RegexLeaf(1, "ATTRIBUTE", "(?:\\s*\\[(.*?)\\])?"), //
						RegexLeaf.spaceZeroOrMore(), RegexLeaf.end());
	}

	private static Integer getIntAttribute(String name, Map<String, String> attr, Integer defaultValue) {
		return Optional.ofNullable(attr.get(name)).map(v -> {
			try {
				return Integer.parseInt(v);
			} catch (NumberFormatException ignore) {
				return defaultValue;
			}
		}).orElse(defaultValue);
	}

	private static int getLengthAttribute(Map<String, String> attr) {
		return getIntAttribute("len", attr, 1);
	}

	private static int getRotationAttribute(Map<String, String> attr) {
		return getIntAttribute("rotate", attr, 0);
	}

	private static int getHeightAttribute(Map<String, String> attr) {
		return getIntAttribute("height", attr, 1);
	}

	@Override
	protected CommandExecutionResult executeArg(PacketDiagram system, LineLocation location, RegexResult arg, ParserPass currentPass) throws NoSuchColorException {
		String r1 = arg.get("RANGE_UNIT", 0);
		String r2 = arg.get("RANGE", 0);
		String r3 = arg.get("RANGE", 1);
		String desc = arg.get("DESC", 0);
		Map<String, String> attr = NwDiagram.toSet(arg.get("ATTRIBUTE", 0));
		int start, end;
		if (r1 != null) {
			try {
				start = Integer.parseInt(r1);
				end = start + getLengthAttribute(attr) - 1;
			} catch (NumberFormatException e) {
				return CommandExecutionResult.error("Invalid bit start " + r1, e);
			}
		} else if (r2 != null && r3 != null) {
			try {
				start = Integer.parseInt(r2);
				end = Integer.parseInt(r3);
			} catch (NumberFormatException e) {
				return CommandExecutionResult.error("Invalid bit range " + r2 + " and " + r3, e);
			}
		} else {
			// auto length and start position, requires diagram context
			start = system.getLastPacketEnd().map(v -> v + 1).orElse(0);
			end = start + getLengthAttribute(attr) - 1;
		}
		// local height and rotation attribute for current packet block
		final int height = getHeightAttribute(attr);
		final int rotation = getRotationAttribute(attr);

		system.addPacketItemRange(start, end, height, desc, rotation);

		return CommandExecutionResult.ok();
	}
}
