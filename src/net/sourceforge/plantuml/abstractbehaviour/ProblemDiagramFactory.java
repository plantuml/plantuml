package net.sourceforge.plantuml.abstractbehaviour;

import java.util.List;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.problemdiagram.command.CommandCreateMachine;
import net.sourceforge.plantuml.statediagram.StateDiagramFactory;

public class ProblemDiagramFactory extends StateDiagramFactory {

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = super.createCommands();
		cmds.add(new CommandCreateMachine());
		return cmds;
	}

	@Override
	public ProblemDiagram createEmptyDiagram() {
		return new ProblemDiagram();
	}
}
