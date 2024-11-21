package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiIf;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.command.ParserPass;

public class CommandNassiIf extends SingleLineCommand2<NassiDiagram> {

    public CommandNassiIf() {
        super(getRegexConcat());
    }

    static IRegex getRegexConcat() {
        return RegexConcat.build(CommandNassiIf.class.getName(),
                RegexLeaf.start(),
                new RegexLeaf("if"),
                RegexLeaf.spaceOneOrMore(),
                new RegexLeaf("CONDITION", "\"([^\"]+)\""),
                RegexLeaf.spaceOneOrMore(),
                new RegexLeaf("then"),
                RegexLeaf.end());
    }

    @Override
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location, RegexResult arg, ParserPass pass) {
        String condition = arg.get("CONDITION", 0);
        NassiIf ifElement = new NassiIf(condition);
        diagram.addElement(ifElement);
        return CommandExecutionResult.ok();
    }
} 