package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiWhile;
import net.sourceforge.plantuml.nassidiagram.element.NassiIf;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class CommandNassiWhile extends SingleLineCommand2<NassiDiagram> {

    public CommandNassiWhile() {
        super(getRegexConcat());
    }

    static IRegex getRegexConcat() {
        return RegexConcat.build(CommandNassiWhile.class.getName(),
                RegexLeaf.start(),
                new RegexLeaf("while"),
                RegexLeaf.spaceOneOrMore(),
                new RegexLeaf("CONDITION", "\"([^\"]+)\""),
                RegexLeaf.spaceOneOrMore(),
                new RegexLeaf("do"),
                RegexLeaf.end());
    }

    @Override
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location, RegexResult arg, ParserPass pass) {
        String condition = arg.get("CONDITION", 0);
        NassiWhile whileElement = new NassiWhile(condition);
        
        // Get current control structure
        NassiElement current = diagram.getCurrentControlStructure();
        
        // Handle nesting
        if (current != null) {
            if (current instanceof NassiIf) {
                // Nested inside an if statement
                NassiIf parentIf = (NassiIf) current;
                parentIf.addToCurrentBranch(whileElement);
                whileElement.setParent(parentIf);
            } else if (current instanceof NassiWhile) {
                // Nested inside another while loop
                NassiWhile parentWhile = (NassiWhile) current;
                parentWhile.addBodyElement(whileElement);
                whileElement.setParent(parentWhile);
            } else {
                // Unknown parent type
                return CommandExecutionResult.error("Invalid nesting structure");
            }
        } else {
            // Root level while loop
            diagram.addElement(whileElement);
        }
        
        // Set this while as the current control structure
        diagram.setCurrentControlStructure(whileElement);
        
        return CommandExecutionResult.ok();
    }
} 