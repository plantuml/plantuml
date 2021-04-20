package net.sourceforge.plantuml.elk.proxy.core;

import net.sourceforge.plantuml.elk.proxy.Reflect;
import net.sourceforge.plantuml.elk.proxy.core.util.NullElkProgressMonitor;
import net.sourceforge.plantuml.elk.proxy.graph.ElkNode;

public class RecursiveGraphLayoutEngine {

	private final Object obj = Reflect.newInstance("org.eclipse.elk.core.RecursiveGraphLayoutEngine");

	public void layout(ElkNode root, NullElkProgressMonitor monitor) {
		Reflect.call2(obj, "layout", root.obj, monitor.obj);
	}

}
