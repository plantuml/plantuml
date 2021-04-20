package net.sourceforge.plantuml.elk.proxy.graph;

import java.util.Collection;
import java.util.EnumSet;

import net.sourceforge.plantuml.elk.proxy.EnumProxy;
import net.sourceforge.plantuml.elk.proxy.Reflect;

public class ElkWithProperty {

	public final Object obj;

	public ElkWithProperty(Object obj) {
		if (obj == null) {
			throw new IllegalArgumentException();
		}
		this.obj = obj;
	}

	@Override
	final public int hashCode() {
		return this.obj.hashCode();
	}

	@Override
	final public boolean equals(Object other) {
		return this.obj.equals(((ElkWithProperty) other).obj);
	}

	final public void setProperty(Object key, Object value) {
		if (value instanceof EnumSet) {
			EnumSet result = null;
			for (Object foo : (Collection) value) {
				final EnumProxy elk = (EnumProxy) foo;
				if (result == null) {
					result = EnumSet.noneOf((Class<Enum>) elk.getClass());
				}
				result.add(elk);
			}
			Reflect.call2(obj, "setProperty", key, result);
		} else if (value instanceof EnumProxy) {
			final Enum elk = ((EnumProxy) value).getTrueObject();
			Reflect.call2(obj, "setProperty", key, elk);
		} else {
			Reflect.call2(obj, "setProperty", key, value);
		}
	}

}
