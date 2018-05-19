package net.sourceforge.plantuml.abstractbehaviour;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.abstractbehaviour.command.CommandArrow;
import net.sourceforge.plantuml.abstractbehaviour.command.CommandCreateAbstractBehaviour;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.note.sequence.FactorySequenceNoteCommand;
import net.sourceforge.plantuml.command.note.sequence.FactorySequenceNoteOnArrowCommand;
import net.sourceforge.plantuml.command.note.sequence.FactorySequenceNoteOverSeveralCommand;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.sequencediagram.command.CommandActivate;
import net.sourceforge.plantuml.sequencediagram.command.CommandActivate2;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutoNewpage;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutoactivate;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutonumber;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutonumberIncrement;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutonumberResume;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutonumberStop;
import net.sourceforge.plantuml.sequencediagram.command.CommandBoxEnd;
import net.sourceforge.plantuml.sequencediagram.command.CommandBoxStart;
import net.sourceforge.plantuml.sequencediagram.command.CommandDeactivateShort;
import net.sourceforge.plantuml.sequencediagram.command.CommandDelay;
import net.sourceforge.plantuml.sequencediagram.command.CommandDivider;
import net.sourceforge.plantuml.sequencediagram.command.CommandExoArrowLeft;
import net.sourceforge.plantuml.sequencediagram.command.CommandExoArrowRight;
import net.sourceforge.plantuml.sequencediagram.command.CommandFootbox;
import net.sourceforge.plantuml.sequencediagram.command.CommandFootboxOld;
import net.sourceforge.plantuml.sequencediagram.command.CommandGrouping;
import net.sourceforge.plantuml.sequencediagram.command.CommandHSpace;
import net.sourceforge.plantuml.sequencediagram.command.CommandIgnoreNewpage;
import net.sourceforge.plantuml.sequencediagram.command.CommandNewpage;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipantA;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipantA2;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipantA3;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipantA4;
import net.sourceforge.plantuml.sequencediagram.command.CommandReferenceMultilinesOverSeveral;
import net.sourceforge.plantuml.sequencediagram.command.CommandReferenceOverSeveral;
import net.sourceforge.plantuml.sequencediagram.command.CommandReturn;
import net.sourceforge.plantuml.sequencediagram.command.CommandSkin;
import net.sourceforge.plantuml.sequencediagram.command.CommandUrl;

public class AbstractBehaviourDiagramFactory extends SequenceDiagramFactory {

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<Command>();

		addCommonCommands(cmds);

		cmds.add(new CommandActivate());
		cmds.add(new CommandDeactivateShort());

		cmds.add(new CommandParticipantA());
		cmds.add(new CommandParticipantA2());
		cmds.add(new CommandParticipantA3());
		cmds.add(new CommandParticipantA4());
//		cmds.add(new CommandArrow());

		cmds.add(new CommandExoArrowLeft());
		cmds.add(new CommandExoArrowRight());

		final FactorySequenceNoteCommand factorySequenceNoteCommand = new FactorySequenceNoteCommand();
		cmds.add(factorySequenceNoteCommand.createSingleLine());

		final FactorySequenceNoteOverSeveralCommand factorySequenceNoteOverSeveralCommand = new FactorySequenceNoteOverSeveralCommand();
		cmds.add(factorySequenceNoteOverSeveralCommand.createSingleLine());

		cmds.add(new CommandBoxStart());
		cmds.add(new CommandBoxEnd());
		cmds.add(new CommandGrouping());

		cmds.add(new CommandActivate2());
		cmds.add(new CommandReturn());

		final FactorySequenceNoteOnArrowCommand factorySequenceNoteOnArrowCommand = new FactorySequenceNoteOnArrowCommand();
		cmds.add(factorySequenceNoteOnArrowCommand.createSingleLine());

		cmds.add(factorySequenceNoteCommand.createMultiLine(false));
		cmds.add(factorySequenceNoteOverSeveralCommand.createMultiLine(false));
		cmds.add(factorySequenceNoteOnArrowCommand.createMultiLine(false));

		cmds.add(new CommandNewpage());
		cmds.add(new CommandIgnoreNewpage());
		cmds.add(new CommandAutoNewpage());
		cmds.add(new CommandDivider());
		cmds.add(new CommandHSpace());
		cmds.add(new CommandReferenceOverSeveral());
		cmds.add(new CommandReferenceMultilinesOverSeveral());
		cmds.add(new CommandSkin());
		cmds.add(new CommandAutonumber());
		cmds.add(new CommandAutonumberStop());
		cmds.add(new CommandAutonumberResume());
		cmds.add(new CommandAutonumberIncrement());
		cmds.add(new CommandAutoactivate());
		cmds.add(new CommandFootbox());
		cmds.add(new CommandDelay());
		cmds.add(new CommandFootboxOld());
		cmds.add(new CommandUrl());		
		cmds.add(new CommandArrow());
		cmds.add(new CommandCreateAbstractBehaviour());
//		System.out.println("length " + cmds.size());
		return cmds;
	}

	@Override
	public AbstractBehaviourDiagram createEmptyDiagram() {
		return new AbstractBehaviourDiagram();
	}
}
