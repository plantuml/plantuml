package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiBreak;
import net.sourceforge.plantuml.nassidiagram.element.NassiWhile;
import net.sourceforge.plantuml.nassidiagram.element.NassiIf;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class CommandNassiBreak extends SingleLineCommand2<NassiDiagram> {

    public CommandNassiBreak() {
        super(getRegexConcat());
    }

    static IRegex getRegexConcat() {
        return RegexConcat.build(CommandNassiBreak.class.getName(),
                RegexLeaf.start(),
                new RegexLeaf("break"),
                RegexLeaf.spaceOneOrMore(),
                new RegexLeaf("CONTENT", "\"([^\"]+)\""),
                RegexLeaf.end());
    }

    @Override
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location, RegexResult arg, ParserPass pass) {
        String content = arg.get("CONTENT", 0);
        NassiBreak breakElement = new NassiBreak(content);
        
        // Get current control structure
        NassiElement current = diagram.getCurrentControlStructure();
        
        // Find the nearest loop or if block that contains this break
        NassiElement container = findBreakContainer(current);
        
        if (container == null) {
            return CommandExecutionResult.error("Break statement must be inside a loop or if block");
        }
        
        // Add break to the appropriate container
        if (container instanceof NassiWhile) {
            NassiWhile whileLoop = (NassiWhile) container;
            whileLoop.addBodyElement(breakElement);
            breakElement.setParent(whileLoop);
        } else if (container instanceof NassiIf) {
            NassiIf ifBlock = (NassiIf) container;
            ifBlock.addToCurrentBranch(breakElement);
            breakElement.setParent(ifBlock);
        }
        
        return CommandExecutionResult.ok();
    }
    
    private NassiElement findBreakContainer(NassiElement current) {
        while (current != null) {
            if (current instanceof NassiWhile || current instanceof NassiIf) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }
} 