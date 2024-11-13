package net.sourceforge.plantuml.nassidiagram;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.core.UmlSource;

public class NassiDiagramFactory extends UmlDiagramFactory {

    @Override
    public NassiDiagram createEmptyDiagram(UmlSource source) {
        return new NassiDiagram(source);
    }

    @Override
    protected void initCommands() {
        addCommand(new CommandNassiBlock());
        addCommand(new CommandNassiIf());
        addCommand(new CommandNassiWhile());
        addCommand(new CommandNassiFor());
        addCommand(new CommandNassiCase());
    }
}