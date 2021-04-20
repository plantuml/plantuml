package net.sourceforge.plantuml.elk.proxy.core.options;

import net.sourceforge.plantuml.elk.proxy.EnumProxy;
import net.sourceforge.plantuml.elk.proxy.Reflect;

public enum EdgeLabelPlacement implements EnumProxy {

	TAIL, HEAD;

	@Override
	public Enum getTrueObject() {
		return Reflect.getEnum("org.eclipse.elk.core.options.EdgeLabelPlacement", name());
	}

}
