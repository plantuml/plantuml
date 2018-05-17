package net.sourceforge.plantuml.abstractbehaviour2.command;

import java.util.List;

import net.sourceforge.plantuml.abstractbehaviour2.AbstractBehaviour2Diagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.IEntity;

public class CommandEndAbstractBehaviour2 extends SingleLineCommand<AbstractBehaviour2Diagram> {

	public CommandEndAbstractBehaviour2() {
		super("(?i)^(\\})$");
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractBehaviour2Diagram diagram, List<String> arg) {
		final IEntity currentPackage = diagram.getCurrentGroup();
		diagram.endGroup();
		return CommandExecutionResult.ok();
	}

}