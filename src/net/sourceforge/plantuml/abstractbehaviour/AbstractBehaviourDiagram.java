package net.sourceforge.plantuml.abstractbehaviour;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class AbstractBehaviourDiagram extends SequenceDiagram {

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.PROBLEM;
	}

}
