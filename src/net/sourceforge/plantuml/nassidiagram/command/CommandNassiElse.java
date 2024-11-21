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

public class CommandNassiElse extends SingleLineCommand2<NassiDiagram> {

    public CommandNassiElse() {
        super(getRegexConcat());
    }

    static IRegex getRegexConcat() {
        return RegexConcat.build(CommandNassiElse.class.getName(),
                RegexLeaf.start(),
                new RegexLeaf("else"),
                RegexLeaf.end());
    }

    @Override
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location, RegexResult arg, ParserPass pass) {
        // Find the last if element by traversing up the parent chain
        NassiElement current = diagram.getLastElement();
        NassiIf ifElement = null;
        
        while (current != null) {
            if (current instanceof NassiIf) {
                ifElement = (NassiIf) current;
                break;
            }
            current = current.getParent();
        }
        
        if (ifElement == null) {
            return CommandExecutionResult.error("No matching if block found");
        }
        
        // Switch to else branch
        ifElement.switchToElseBranch();
        return CommandExecutionResult.ok();
    }
} 