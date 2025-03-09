package net.sourceforge.plantuml.utils;

public class ObjectUtils {
	// ::remove file when __HAXE__

	public static boolean instanceOfAny(Object object, Class<?>... classes) {
		for (Class<?> c : classes)
			if (c.isInstance(object))
				return true;

		return false;
	}
}
