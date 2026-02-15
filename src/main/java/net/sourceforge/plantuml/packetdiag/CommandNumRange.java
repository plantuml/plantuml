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
 *
 * 
 */
package net.sourceforge.plantuml.packetdiag;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.nwdiag.NwDiagram;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

import java.util.Map;
import java.util.Optional;

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
		// local height attribute for current packet block
		int height = getHeightAttribute(attr);
		PacketDiagram.PacketItem packetItem = PacketDiagram.PacketItem.ofRange(start, end, height, desc);
		packetItem.textRotation = getRotationAttribute(attr);

		system.addPacketItem(packetItem);
		return CommandExecutionResult.ok();
	}
}
