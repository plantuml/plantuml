package net.sourceforge.plantuml.elk.proxy.graph;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;

import net.sourceforge.plantuml.elk.proxy.ElkObjectProxy;
import net.sourceforge.plantuml.elk.proxy.Reflect;

public class ElkWithProperty {

	public final Object obj;

	public ElkWithProperty(Object obj) {
		this.obj = Objects.requireNonNull(obj);
	}

	@Override
	final public int hashCode() {
		return this.obj.hashCode();
	}

	@Override
	final public boolean equals(Object other) {
		return this.obj.equals(((ElkWithProperty) other).obj);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	final public void setProperty(Object key, Object value) {
		if (value instanceof EnumSet) {
			EnumSet result = null;
			for (Object foo : (Collection) value) {
				final ElkObjectProxy elk = (ElkObjectProxy) foo;
				if (result == null) {
					result = EnumSet.noneOf((Class) elk.getClass());
				}
				result.add(elk);
			}
			Reflect.call2(obj, "setProperty", key, result);
		} else if (value instanceof ElkObjectProxy) {
			final Object elk = ((ElkObjectProxy) value).getTrueObject();
			Reflect.call2(obj, "setProperty", key, elk);
		} else {
			Reflect.call2(obj, "setProperty", key, value);
		}
	}

}
