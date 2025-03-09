package net.sourceforge.plantuml.elk.proxy.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.elk.proxy.Reflect;

public class ElkEdgeSection {

	public final Object obj;

	public ElkEdgeSection(Object obj) {
		this.obj = Objects.requireNonNull(obj);
	}

	@Override
	public int hashCode() {
		return this.obj.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		return this.obj.equals(((ElkEdgeSection) other).obj);
	}

	public double getStartX() {
		return (Double) Reflect.call(obj, "getStartX");
	}

	public double getStartY() {
		return (Double) Reflect.call(obj, "getStartY");
	}

	public double getEndX() {
		return (Double) Reflect.call(obj, "getEndX");
	}

	public double getEndY() {
		return (Double) Reflect.call(obj, "getEndY");
	}

	public Collection<ElkBendPoint> getBendPoints() {
		final List<ElkBendPoint> result = new ArrayList<>();
		Collection internal = (Collection) Reflect.call(obj, "getBendPoints");
		for (Object element : internal) {
			result.add(new ElkBendPoint(element));
		}
		return result;
	}

}
