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

import java.util.Optional;

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
		PacketDiagram.ScaleDirection dir = Optional.ofNullable(arg.get("DIR", 0))
						.map(String::toUpperCase)
						.map(PacketDiagram.ScaleDirection::valueOf)
						.orElse(PacketDiagram.ScaleDirection.LTR);
		system.setScaleDirection(dir);
		return CommandExecutionResult.ok();
	}
}
