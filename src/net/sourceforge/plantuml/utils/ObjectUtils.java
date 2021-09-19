package net.sourceforge.plantuml.utils;

public class ObjectUtils {

	public static boolean instanceOfAny(Object object, Class<?>... classes) {
		for (Class<?> c : classes) {
			if (c.isInstance(object)) {
				return true;
			}
		}
		return false;
	}
}
