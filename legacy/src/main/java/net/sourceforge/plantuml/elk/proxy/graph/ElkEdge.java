package net.sourceforge.plantuml.elk.proxy.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.elk.proxy.Reflect;

public class ElkEdge extends ElkWithProperty {
    // ::remove folder when __HAXE__

	public ElkEdge(Object obj) {
		super(obj);
	}

	public ElkNode getContainingNode() {
		return new ElkNode(Reflect.call(obj, "getContainingNode"));
	}

	public Collection<ElkLabel> getLabels() {
		final List<ElkLabel> result = new ArrayList<>();
		Collection internal = (Collection) Reflect.call(obj, "getLabels");
		for (Object element : internal) {
			result.add(new ElkLabel(element));
		}
		return result;
	}

	public List<ElkEdgeSection> getSections() {
		final List<ElkEdgeSection> result = new ArrayList<>();
		Collection internal = (Collection) Reflect.call(obj, "getSections");
		for (Object element : internal) {
			result.add(new ElkEdgeSection(element));
		}
		return result;
	}

	public boolean isHierarchical() {
		throw new UnsupportedOperationException();
	}

}
