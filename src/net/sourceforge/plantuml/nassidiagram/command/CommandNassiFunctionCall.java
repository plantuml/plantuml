package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiFunctionCall;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class CommandNassiFunctionCall extends SingleLineCommand2<NassiDiagram> {

    public CommandNassiFunctionCall() {
        super(getRegexConcat());
    }

    static IRegex getRegexConcat() {
        return RegexConcat.build(CommandNassiFunctionCall.class.getName(),
                RegexLeaf.start(),
                new RegexLeaf("call"),
                RegexLeaf.spaceOneOrMore(),
                new RegexLeaf("CONTENT", "\"([^\"]+)\""),
                RegexLeaf.end());
    }

    @Override
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location, RegexResult arg, ParserPass pass) {
        String content = arg.get("CONTENT", 0);
        NassiFunctionCall functionCall = new NassiFunctionCall(content);
        
        // Set parent if inside a control structure
        NassiElement lastElement = diagram.getLastElement();
        if (lastElement != null) {
            functionCall.setParent(lastElement);
        }
        
        diagram.addElement(functionCall);
        return CommandExecutionResult.ok();
    }
} 