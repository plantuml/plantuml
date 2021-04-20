package net.sourceforge.plantuml.elk.proxy.core.util;

import net.sourceforge.plantuml.elk.proxy.Reflect;

public class NullElkProgressMonitor {
	
	public final Object obj = Reflect.newInstance("org.eclipse.elk.core.util.NullElkProgressMonitor");


}
