package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiContinue;
import net.sourceforge.plantuml.nassidiagram.element.NassiWhile;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class CommandNassiContinue extends SingleLineCommand2<NassiDiagram> {

    public CommandNassiContinue() {
        super(getRegexConcat());
    }

    static IRegex getRegexConcat() {
        return RegexConcat.build(CommandNassiContinue.class.getName(),
                RegexLeaf.start(),
                new RegexLeaf("continue"),
                RegexLeaf.spaceOneOrMore(),
                new RegexLeaf("CONTENT", "\"([^\"]+)\""),
                RegexLeaf.end());
    }

    @Override
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location, RegexResult arg, ParserPass pass) {
        String content = arg.get("CONTENT", 0);
        NassiContinue continueElement = new NassiContinue(content);
        
        // Get current control structure
        NassiElement current = diagram.getCurrentControlStructure();
        
        // Find the nearest loop that contains this continue
        NassiElement container = findLoopContainer(current);
        
        if (container == null) {
            return CommandExecutionResult.error("Continue statement must be inside a loop");
        }
        
        // Add continue to the loop
        NassiWhile whileLoop = (NassiWhile) container;
        whileLoop.addBodyElement(continueElement);
        continueElement.setParent(whileLoop);
        
        return CommandExecutionResult.ok();
    }
    
    private NassiElement findLoopContainer(NassiElement current) {
        while (current != null) {
            if (current instanceof NassiWhile) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }
} 