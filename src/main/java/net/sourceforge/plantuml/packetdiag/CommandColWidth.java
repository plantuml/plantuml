package net.sourceforge.plantuml.packetdiag;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandColWidth extends SingleLineCommand2<PacketDiagram> {

	public CommandColWidth() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandColWidth.class.getName(), RegexLeaf.start(), //
						new RegexLeaf("colwidth"), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("="), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf(1, "WIDTH", "(\\d{1,3});?"), //
						RegexLeaf.spaceZeroOrMore(), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(PacketDiagram system, LineLocation location, RegexResult arg, ParserPass currentPass) throws NoSuchColorException {
		try {
			String width = arg.get("WIDTH", 0);
			if (width != null) {
				system.setColWidth(Integer.parseInt(width));
			}
		} catch (NumberFormatException e) {
			return CommandExecutionResult.error("Width must be an integer", e);
		}
		return CommandExecutionResult.ok();
	}
}
