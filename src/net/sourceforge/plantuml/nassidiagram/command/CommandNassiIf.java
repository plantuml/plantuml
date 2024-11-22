package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiIf;
import net.sourceforge.plantuml.nassidiagram.element.NassiWhile;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

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
        
        // Get current control structure
        NassiElement current = diagram.getCurrentControlStructure();
        
        // Handle nesting
        if (current != null) {
            if (current instanceof NassiIf) {
                // Nested inside another if
                NassiIf parentIf = (NassiIf) current;
                parentIf.addToCurrentBranch(ifElement);
                ifElement.setParent(parentIf);
            } else if (current instanceof NassiWhile) {
                // Nested inside a while loop
                NassiWhile parentWhile = (NassiWhile) current;
                parentWhile.addBodyElement(ifElement);
                ifElement.setParent(parentWhile);
            } else {
                // Unknown parent type
                return CommandExecutionResult.error("Invalid nesting structure");
            }
        } else {
            // Root level if statement
            diagram.addElement(ifElement);
        }
        
        // Set this if as the current control structure
        diagram.setCurrentControlStructure(ifElement);
        
        return CommandExecutionResult.ok();
    }
} 