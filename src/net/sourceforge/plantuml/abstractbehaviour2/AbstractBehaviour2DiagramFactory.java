package net.sourceforge.plantuml.abstractbehaviour2;

import java.util.List;

import net.sourceforge.plantuml.abstractbehaviour2.command.CommandCreateAbstractBehaviour2;
import net.sourceforge.plantuml.abstractbehaviour2.command.CommandEndAbstractBehaviour2;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.objectdiagram.ObjectDiagramFactory;

public class AbstractBehaviour2DiagramFactory extends ObjectDiagramFactory {

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = super.createCommands();
		cmds.add(new CommandCreateAbstractBehaviour2());
		cmds.add(new CommandEndAbstractBehaviour2());
		return cmds;
	}

	@Override
	public AbstractBehaviour2Diagram createEmptyDiagram() {
		return new AbstractBehaviour2Diagram();
	}
}
