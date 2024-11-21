package net.sourceforge.plantuml.nassidiagram;

import java.util.List;
import java.util.Map;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommonCommands;
import net.sourceforge.plantuml.command.PSystemCommandFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.nassidiagram.command.*;

public class NassiDiagramFactory extends PSystemCommandFactory {

    public NassiDiagramFactory() {
        super(DiagramType.NASSI);
    }

    @Override
    protected void initCommandsList(List<Command> cmds) {
        CommonCommands.addCommonCommands1(cmds);
        cmds.add(new CommandNassiBlock());
        cmds.add(new CommandNassiIf());
        cmds.add(new CommandNassiElse());
        cmds.add(new CommandNassiEndIf());
        cmds.add(new CommandNassiInput());
        cmds.add(new CommandNassiOutput());
        cmds.add(new CommandNassiWhile());
        cmds.add(new CommandNassiEndWhile());
        cmds.add(new CommandNassiBreak());
        cmds.add(new CommandNassiFunctionCall());
        cmds.add(new CommandNassiConnector());
    }

    @Override
    public NassiDiagram createEmptyDiagram(UmlSource source, Map<String, String> skinParam) {
        return new NassiDiagram(source, skinParam);
    }
}