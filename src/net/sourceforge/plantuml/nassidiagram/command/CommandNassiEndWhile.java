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
        NassiElement current = diagram.getCurrentControlStructure();
        
        // Find the most recent while block by traversing up the parent chain
        while (current != null && !(current instanceof NassiWhile)) {
            current = current.getParent();
        }
        
        if (current == null) {
            return CommandExecutionResult.error("No matching while block found");
        }
        
        // Validate that we're not ending a while block from inside an if block
        NassiElement parent = current.getParent();
        while (parent != null) {
            if (parent instanceof NassiWhile) {
                return CommandExecutionResult.error("Cannot end while block from within another control structure");
            }
            parent = parent.getParent();
        }
        
        // End the while block and update current control structure
        diagram.endControlStructure();
        return CommandExecutionResult.ok();
    }
} 