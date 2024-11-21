package net.sourceforge.plantuml.nassidiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.nassidiagram.NassiDiagram;
import net.sourceforge.plantuml.nassidiagram.element.NassiConnector;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class CommandNassiConnector extends SingleLineCommand2<NassiDiagram> {

    public CommandNassiConnector() {
        super(getRegexConcat());
    }

    static IRegex getRegexConcat() {
        return RegexConcat.build(CommandNassiConnector.class.getName(),
                RegexLeaf.start(),
                new RegexLeaf("connector"),
                RegexLeaf.spaceOneOrMore(),
                new RegexLeaf("CONTENT", "\"([^\"]+)\""),
                RegexLeaf.end());
    }

    @Override
    protected CommandExecutionResult executeArg(NassiDiagram diagram, LineLocation location, RegexResult arg, ParserPass pass) {
        String content = arg.get("CONTENT", 0);
        NassiConnector connector = new NassiConnector(content);
        
        // Set parent if inside a control structure
        NassiElement lastElement = diagram.getLastElement();
        if (lastElement != null) {
            connector.setParent(lastElement);
        }
        
        diagram.addElement(connector);
        return CommandExecutionResult.ok();
    }
} 