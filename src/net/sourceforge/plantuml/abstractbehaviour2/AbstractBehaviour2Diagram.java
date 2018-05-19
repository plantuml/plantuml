package net.sourceforge.plantuml.abstractbehaviour2;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.objectdiagram.ObjectDiagram;

public class AbstractBehaviour2Diagram extends ObjectDiagram {

	public ILeaf getOrCreateLeaf(Code code, LeafType type, USymbol symbol) {
		if (type == null) {
			type = LeafType.OBJECT;
		}
		return getOrCreateLeafDefault(code, type, symbol);
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.PROBLEM;
	}

}
