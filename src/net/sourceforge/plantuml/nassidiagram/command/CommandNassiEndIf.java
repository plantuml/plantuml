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
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class CommandNassiEndIf extends SingleLineCommand2<NassiDiagram> {

    public CommandNassiEndIf() {
        super(getRegexConcat());
    }

    static IRegex getRegexConcat() {
        return RegexConcat.build(CommandNassiEndIf.class.getName(),
                RegexLeaf.start(),
                new RegexLeaf("endif"),
                RegexLeaf.end());
    }

    @Override
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location, RegexResult arg, ParserPass pass) {
        NassiElement current = diagram.getCurrentControlStructure();
        
        // Find the most recent if block
        while (current != null && !(current instanceof NassiIf)) {
            current = current.getParent();
        }
        
        if (current == null) {
            return CommandExecutionResult.error("No matching if block found");
        }
        
        // End the if block
        diagram.endControlStructure();
        return CommandExecutionResult.ok();
    }
} 