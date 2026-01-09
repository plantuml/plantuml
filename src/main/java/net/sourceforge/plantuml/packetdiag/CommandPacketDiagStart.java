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

public class CommandPacketDiagStart extends SingleLineCommand2<PacketDiagram> {

	public CommandPacketDiagStart() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPacketDiagStart.class.getName(), RegexLeaf.start(), //
						new RegexLeaf(1, "TYPE", "(packetdiag)?"), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("\\{"), RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(PacketDiagram system, LineLocation location, RegexResult arg, ParserPass currentPass) throws NoSuchColorException {
		return CommandExecutionResult.ok();
	}
}
