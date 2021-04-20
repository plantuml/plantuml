package net.sourceforge.plantuml.elk.proxy.graph;

import net.sourceforge.plantuml.elk.proxy.Reflect;

public class ElkLabel extends ElkWithProperty {

	public ElkLabel(Object obj) {
		super(obj);
	}

	public void setText(String text) {
		Reflect.call(obj, "setText", text);
	}

	public void setDimensions(double width, double height) {
		Reflect.call2(obj, "setDimensions", width, height);
	}

	public String getText() {
		return (String) Reflect.call(obj, "getText");
	}

	public double getX() {
		return (Double) Reflect.call(obj, "getX");
	}

	public double getY() {
		return (Double) Reflect.call(obj, "getY");
	}

}
