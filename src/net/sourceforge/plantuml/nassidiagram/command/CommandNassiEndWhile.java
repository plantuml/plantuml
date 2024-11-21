package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiWhile;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class CommandNassiEndWhile extends SingleLineCommand2<NassiDiagram> {

    public CommandNassiEndWhile() {
        super(getRegexConcat());
    }

    static IRegex getRegexConcat() {
        return RegexConcat.build(CommandNassiEndWhile.class.getName(),
                RegexLeaf.start(),
                new RegexLeaf("endwhile"),
                RegexLeaf.end());
    }

    @Override
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location, RegexResult arg, ParserPass pass) {
        // Find the last while element and close it
        NassiElement lastElement = diagram.getLastElement();
        while (lastElement != null && !(lastElement instanceof NassiWhile)) {
            lastElement = lastElement.getParent();
        }
        
        if (lastElement == null) {
            return CommandExecutionResult.error("No matching while block found");
        }
        
        return CommandExecutionResult.ok();
    }
} 