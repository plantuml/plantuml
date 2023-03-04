package net.sourceforge.plantuml.elk.proxy.graph;

import java.util.Objects;

import net.sourceforge.plantuml.elk.proxy.Reflect;

public class ElkBendPoint {

	public final Object obj;

	public ElkBendPoint(Object obj) {
		this.obj = Objects.requireNonNull(obj);
	}

	@Override
	public int hashCode() {
		return this.obj.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		return this.obj.equals(((ElkBendPoint) other).obj);
	}

	public double getX() {
		return (Double) Reflect.call(obj, "getX");
	}

	public double getY() {
		return (Double) Reflect.call(obj, "getY");
	}

}
