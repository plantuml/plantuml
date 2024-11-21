package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiIO;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.command.ParserPass;

public class CommandNassiOutput extends SingleLineCommand2<NassiDiagram> {

    public CommandNassiOutput() {
        super(getRegexConcat());
    }

    static IRegex getRegexConcat() {
        return RegexConcat.build(CommandNassiOutput.class.getName(),
                RegexLeaf.start(),
                new RegexLeaf("output"),
                RegexLeaf.spaceOneOrMore(),
                new RegexLeaf("CONTENT", "\"([^\"]+)\""),
                RegexLeaf.end());
    }

    @Override
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location, RegexResult arg, ParserPass pass) {
        String content = arg.get("CONTENT", 0);
        diagram.addElement(new NassiIO(content, false));
        return CommandExecutionResult.ok();
    }
} 