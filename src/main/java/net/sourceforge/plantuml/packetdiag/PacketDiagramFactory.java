package net.sourceforge.plantuml.packetdiag;

import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommonCommands;
import net.sourceforge.plantuml.command.PSystemCommandFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;

import java.util.List;

public class PacketDiagramFactory extends PSystemCommandFactory {
	public PacketDiagramFactory() {
		super(DiagramType.PACKET);
	}

	@Override
	protected void initCommandsList(List<Command> cmds) {
		CommonCommands.addCommonCommands1(cmds);
		cmds.add(new CommandPacketDiagStart());
		cmds.add(new CommandColWidth());
		cmds.add(new CommandNodeHeight());
		cmds.add(new CommandScaleDirection());
		cmds.add(new CommandScaleInterval());
		cmds.add(new CommandNumRange());
		cmds.add(new CommandPacketDiagEnd());
	}

	@Override
	public PacketDiagram createEmptyDiagram(PathSystem pathSystem, UmlSource source, Previous previous, PreprocessingArtifact preprocessing) {
		return new PacketDiagram(source, preprocessing);
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.PACKET;
	}
}
