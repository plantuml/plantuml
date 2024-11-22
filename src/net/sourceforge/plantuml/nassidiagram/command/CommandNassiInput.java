package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiIO;
import net.sourceforge.plantuml.nassidiagram.element.NassiIf;
import net.sourceforge.plantuml.nassidiagram.element.NassiWhile;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class CommandNassiInput extends SingleLineCommand2<NassiDiagram> {

    public CommandNassiInput() {
        super(getRegexConcat());
    }

    static IRegex getRegexConcat() {
        return RegexConcat.build(CommandNassiInput.class.getName(),
                RegexLeaf.start(),
                new RegexLeaf("input"),
                RegexLeaf.spaceOneOrMore(),
                new RegexLeaf("CONTENT", "\"([^\"]+)\""),
                RegexLeaf.end());
    }

    @Override
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location, RegexResult arg, ParserPass pass) {
        String content = arg.get("CONTENT", 0);
        NassiIO inputElement = new NassiIO(content, true);
        
        // Get current control structure
        NassiElement current = diagram.getCurrentControlStructure();
        
        // Handle nesting
        if (current != null) {
            if (current instanceof NassiIf) {
                // Inside an if statement
                NassiIf parentIf = (NassiIf) current;
                parentIf.addToCurrentBranch(inputElement);
                inputElement.setParent(parentIf);
            } else if (current instanceof NassiWhile) {
                // Inside a while loop
                NassiWhile parentWhile = (NassiWhile) current;
                parentWhile.addBodyElement(inputElement);
                inputElement.setParent(parentWhile);
            } else {
                // Unknown parent type - add to diagram
                diagram.addElement(inputElement);
            }
        } else {
            // Root level input
            diagram.addElement(inputElement);
        }
        
        return CommandExecutionResult.ok();
    }
} 