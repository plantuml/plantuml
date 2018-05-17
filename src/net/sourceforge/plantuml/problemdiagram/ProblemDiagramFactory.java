package net.sourceforge.plantuml.problemdiagram;

import java.util.List;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.objectdiagram.ObjectDiagramFactory;
import net.sourceforge.plantuml.problemdiagram.command.CommandCreateDomain;

public class ProblemDiagramFactory extends ObjectDiagramFactory {

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = super.createCommands();
		cmds.add(new CommandCreateDomain());
		return cmds;
	}

	@Override
	public ProblemDiagram createEmptyDiagram() {
		return new ProblemDiagram();
	}
}
