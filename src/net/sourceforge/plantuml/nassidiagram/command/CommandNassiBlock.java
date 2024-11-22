package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiBlock;
import net.sourceforge.plantuml.nassidiagram.element.NassiIf;
import net.sourceforge.plantuml.nassidiagram.element.NassiWhile;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

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
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location, RegexResult arg, ParserPass pass) {
        String content = arg.get("CONTENT", 0);
        NassiBlock blockElement = new NassiBlock(content);
        
        // Get current control structure
        NassiElement current = diagram.getCurrentControlStructure();
        
        // Handle nesting
        if (current != null) {
            if (current instanceof NassiIf) {
                // Inside an if statement
                NassiIf parentIf = (NassiIf) current;
                parentIf.addToCurrentBranch(blockElement);
                blockElement.setParent(parentIf);
            } else if (current instanceof NassiWhile) {
                // Inside a while loop
                NassiWhile parentWhile = (NassiWhile) current;
                parentWhile.addBodyElement(blockElement);
                blockElement.setParent(parentWhile);
            } else {
                // Unknown parent type - add to diagram
                diagram.addElement(blockElement);
            }
        } else {
            // Root level block
            diagram.addElement(blockElement);
        }
        
        return CommandExecutionResult.ok();
    }
}