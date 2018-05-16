package net.sourceforge.plantuml.problemdiagram.command;

import java.util.List;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.problemdiagram.ProblemDiagram;

public class CommandEndMachine extends SingleLineCommand<ProblemDiagram> {

	public CommandEndMachine() {
		super("(?i)^(\\})$");
	}

	@Override
	protected CommandExecutionResult executeArg(ProblemDiagram diagram, List<String> arg) {
		final IEntity currentPackage = diagram.getCurrentGroup();
		diagram.endGroup();
		return CommandExecutionResult.ok();
	}

}