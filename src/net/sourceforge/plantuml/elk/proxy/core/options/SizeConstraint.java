package net.sourceforge.plantuml.elk.proxy.core.options;

import net.sourceforge.plantuml.elk.proxy.EnumProxy;
import net.sourceforge.plantuml.elk.proxy.Reflect;

public enum SizeConstraint implements EnumProxy {
	NODE_LABELS;
	
	@Override
	public Enum getTrueObject() {
		return Reflect.getEnum("org.eclipse.elk.core.options.SizeConstraint", name());
	}


}
