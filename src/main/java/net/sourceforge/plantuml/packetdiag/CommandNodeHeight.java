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

public class CommandNodeHeight extends SingleLineCommand2<PacketDiagram> {

	public CommandNodeHeight() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandNodeHeight.class.getName(), RegexLeaf.start(), //
						new RegexLeaf("node_height"), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("="), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf(1, "HEIGHT", "(\\d{1,3});?"), //
						RegexLeaf.spaceZeroOrMore(), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(PacketDiagram system, LineLocation location, RegexResult arg, ParserPass currentPass) throws NoSuchColorException {
		try {
			// The original python implementation takes int, but it really should be a float number
			system.updateNodeHeight(Integer.parseInt(arg.get("HEIGHT", 0)));
		} catch (NumberFormatException e) {
			return CommandExecutionResult.error("Height must be an integer", e);
		}
		return CommandExecutionResult.ok();
	}
}
