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

public class CommandScaleInterval extends SingleLineCommand2<PacketDiagram> {

	public CommandScaleInterval() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandScaleInterval.class.getName(), RegexLeaf.start(), //
						new RegexLeaf("scale_interval"), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("="), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf(1, "INTERVAL", "(\\d+);?"), //
						RegexLeaf.spaceZeroOrMore(), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(PacketDiagram system, LineLocation location, RegexResult arg, ParserPass currentPass) throws NoSuchColorException {
		try {
			system.updateScaleInterval(Integer.parseInt(arg.get("INTERVAL", 0)));
		} catch (NumberFormatException e) {
			return CommandExecutionResult.error("Scale interval invalid", e);
		}
		return CommandExecutionResult.ok();
	}
}
