package net.sourceforge.plantuml.elk.proxy.graph;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.plantuml.elk.proxy.Reflect;

public class ElkNode extends ElkWithProperty {

	public ElkNode(Object obj) {
		super(obj);
	}

	public ElkNode getParent() {
		final Object tmp = Reflect.call(obj, "getParent");
		if (tmp == null) {
			return null;
		}
		return new ElkNode(tmp);
	}

	public double getX() {
		return (Double) Reflect.call(obj, "getX");
	}

	public double getY() {
		return (Double) Reflect.call(obj, "getY");
	}

	public Collection<ElkLabel> getLabels() {
		final Collection<ElkLabel> result = new ArrayList<>();
		final Collection internal = (Collection) Reflect.call(obj, "getLabels");
		for (Object element : internal) {
			result.add(new ElkLabel(element));
		}
		return result;
	}

	public double getWidth() {
		return (Double) Reflect.call(obj, "getWidth");
	}

	public double getHeight() {
		return (Double) Reflect.call(obj, "getHeight");
	}

	public void setDimensions(double width, double height) {
		Reflect.call2(obj, "setDimensions", width, height);
	}

}
