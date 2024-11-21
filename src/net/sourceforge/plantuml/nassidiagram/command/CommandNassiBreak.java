package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiBreak;
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
        
        // Find parent loop or if block if any
        NassiElement lastElement = diagram.getLastElement();
        if (lastElement != null) {
            breakElement.setParent(lastElement);
        }
        
        diagram.addElement(breakElement);
        return CommandExecutionResult.ok();
    }
} 