package net.sourceforge.plantuml.elk.proxy.graph.util;

import net.sourceforge.plantuml.elk.proxy.Reflect;
import net.sourceforge.plantuml.elk.proxy.graph.ElkEdge;
import net.sourceforge.plantuml.elk.proxy.graph.ElkLabel;
import net.sourceforge.plantuml.elk.proxy.graph.ElkNode;

public class ElkGraphUtil {

	public static ElkLabel createLabel(ElkEdge edge) {
		return new ElkLabel(Reflect.callStatic2("org.eclipse.elk.graph.util.ElkGraphUtil", "createLabel", edge.obj));
	}

	public static ElkLabel createLabel(ElkNode node) {
		return new ElkLabel(Reflect.callStatic2("org.eclipse.elk.graph.util.ElkGraphUtil", "createLabel", node.obj));
	}

	public static ElkNode createNode(ElkNode root) {
		return new ElkNode(Reflect.callStatic2("org.eclipse.elk.graph.util.ElkGraphUtil", "createNode", root.obj));
	}

	public static ElkEdge createSimpleEdge(ElkNode node1, ElkNode node2) {
		return new ElkEdge(Reflect.callStatic2("org.eclipse.elk.graph.util.ElkGraphUtil", "createSimpleEdge", node1.obj,
				node2.obj));
	}

	public static ElkNode createGraph() {
		return new ElkNode(Reflect.callStatic("org.eclipse.elk.graph.util.ElkGraphUtil", "createGraph"));
	}

}
