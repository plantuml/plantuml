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

	private static int getLengthAttribute(Map<String, String> attr) {
		return Optional.ofNullable(attr.get("len")).map(v -> {
			try {
				return Integer.parseInt(v);
			} catch (NumberFormatException ignore) {
				return 1;
			}
		}).orElse(1);
	}

	private static int getRotationAttribute(Map<String, String> attr) {
		return Optional.ofNullable(attr.get("rotate")).map(v -> {
			try {
				return Integer.parseInt(v);
			} catch (NumberFormatException ignore) {
				return 0;
			}
		}).orElse(0);
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
		PacketDiagram.PacketItem packetItem = PacketDiagram.PacketItem.ofRange(start, end, desc);
		packetItem.textRotation = getRotationAttribute(attr);
		system.addPacketItem(packetItem);
		return CommandExecutionResult.ok();
	}
}
