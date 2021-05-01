package net.sourceforge.plantuml.elk.proxy.core.math;

import net.sourceforge.plantuml.elk.proxy.ElkObjectProxy;
import net.sourceforge.plantuml.elk.proxy.Reflect;

public class ElkPadding implements ElkObjectProxy {

	public final Object obj;

	public ElkPadding(double top, double right, double bottom, double left) {
		this.obj = Reflect.newInstance("org.eclipse.elk.core.math.ElkPadding", top, right, bottom, left);
	}

	@Override
	public Object getTrueObject() {
		return obj;
	}

}
