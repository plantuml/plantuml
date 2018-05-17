package net.sourceforge.plantuml.problemdiagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.classdiagram.command.CommandUrl;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandEndPackage;
import net.sourceforge.plantuml.command.CommandFootboxIgnored;
import net.sourceforge.plantuml.command.CommandPackage;
import net.sourceforge.plantuml.command.CommandPage;
import net.sourceforge.plantuml.command.CommandRankDir;
import net.sourceforge.plantuml.command.note.FactoryNoteCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnEntityCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnLinkCommand;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.objectdiagram.ObjectDiagramFactory;
import net.sourceforge.plantuml.objectdiagram.command.CommandAddData;
import net.sourceforge.plantuml.objectdiagram.command.CommandCreateEntityObject;
import net.sourceforge.plantuml.objectdiagram.command.CommandCreateEntityObjectMultilines;
import net.sourceforge.plantuml.problemdiagram.command.CommandCreateDomain;

public class ProblemDiagramFactory extends ObjectDiagramFactory {

	@Override
	protected List<Command> createCommands() {

		final List<Command> cmds = new ArrayList<Command>();
		cmds.add(new CommandFootboxIgnored());

		addCommonCommands(cmds);
		cmds.add(new CommandRankDir());
		cmds.add(new CommandPage());
		cmds.add(new CommandAddData());
		// cmds.add(new CommandLinkClass(UmlDiagramType.OBJECT));
		cmds.add(new net.sourceforge.plantuml.problemdiagram.CommandLinkClass(UmlDiagramType.OBJECT));
		
		cmds.add(new CommandCreateEntityObject());
		final FactoryNoteCommand factoryNoteCommand = new FactoryNoteCommand();

		cmds.add(factoryNoteCommand.createSingleLine());
		cmds.add(new CommandPackage());
		cmds.add(new CommandEndPackage());
		// addCommand(new CommandNamespace());
		// addCommand(new CommandEndNamespace());
		// addCommand(new CommandStereotype());
		//
		// addCommand(new CommandImport());
		final FactoryNoteOnEntityCommand factoryNoteOnEntityCommand = new FactoryNoteOnEntityCommand(new RegexLeaf(
				"ENTITY", "([\\p{L}0-9_.]+|[%g][^%g]+[%g])"));
		cmds.add(factoryNoteOnEntityCommand.createSingleLine());

		cmds.add(new CommandUrl());

		cmds.add(factoryNoteCommand.createMultiLine(false));
		cmds.add(factoryNoteOnEntityCommand.createMultiLine(true));
		cmds.add(factoryNoteOnEntityCommand.createMultiLine(false));
		cmds.add(new CommandCreateEntityObjectMultilines());

		final FactoryNoteOnLinkCommand factoryNoteOnLinkCommand = new FactoryNoteOnLinkCommand();
		cmds.add(factoryNoteOnLinkCommand.createSingleLine());
		cmds.add(factoryNoteOnLinkCommand.createMultiLine(false));

		// addCommand(new CommandNoopClass());
		cmds.add(new CommandCreateDomain());
		return cmds;
	}

	@Override
	public ProblemDiagram createEmptyDiagram() {
		return new ProblemDiagram();
	}
}
