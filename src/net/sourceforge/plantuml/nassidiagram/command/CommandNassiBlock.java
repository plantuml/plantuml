package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.core.LineLocation;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiBlock;

public class CommandNassiBlock extends SingleLineCommand2<NassiDiagram> {

    public CommandNassiBlock() {
        super(getRegexConcat());
    }

    static IRegex getRegexConcat() {
        return RegexConcat.build(CommandNassiBlock.class.getName(),
            RegexLeaf.start(),
            new RegexLeaf("block"),
            RegexLeaf.spaceOneOrMore(),
            new RegexLeaf("CONTENT", "\"([^\"]+)\""),
            RegexLeaf.end());
    }

    @Override
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location,
        RegexResult result) {
        final String content = result.get("CONTENT", 0);
        diagram.addElement(new NassiBlock(content));
        return CommandExecutionResult.ok();
    }
}