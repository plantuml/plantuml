package net.sourceforge.plantuml.elk.proxy.core.options;

import net.sourceforge.plantuml.elk.proxy.EnumProxy;
import net.sourceforge.plantuml.elk.proxy.Reflect;

public enum NodeLabelPlacement implements EnumProxy {

	INSIDE, V_CENTER, H_CENTER;

	@Override
	public Enum getTrueObject() {
		return Reflect.getEnum("org.eclipse.elk.core.options.NodeLabelPlacement", name());
	}

}
