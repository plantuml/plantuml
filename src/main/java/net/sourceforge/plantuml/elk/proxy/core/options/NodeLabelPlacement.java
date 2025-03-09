package net.sourceforge.plantuml.elk.proxy.core.options;

import net.sourceforge.plantuml.elk.proxy.ElkObjectProxy;
import net.sourceforge.plantuml.elk.proxy.Reflect;

public enum NodeLabelPlacement implements ElkObjectProxy {

	INSIDE, V_CENTER, H_CENTER;

	@Override
	public Enum getTrueObject() {
		return Reflect.getEnum("org.eclipse.elk.core.options.NodeLabelPlacement", name());
	}

}
