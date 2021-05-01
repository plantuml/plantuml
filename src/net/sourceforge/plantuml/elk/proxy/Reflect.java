/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.elk.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*
 * Various methods to do Java introspection
 */
public class Reflect {

	public static Class clazz(String className) {
		try {
			return Class.forName(className);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Enum getEnum(String clazz, String name) {
		try {
			final Class cl = clazz(clazz);
			for (Object en : cl.getEnumConstants()) {
				if (en.toString().equals(name)) {
					return (Enum) en;
				}
			}
			throw new UnsupportedOperationException(name);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object opt(String className, String fieldname) {
		try {
			final Class<?> cl = Class.forName(className);
			final Field field = cl.getField(fieldname);
			return field.get(null);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object newInstance(String className) {
		try {
			final Class<?> cl = Class.forName(className);
			return cl.newInstance();
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object newInstance(String className, Object arg1) {
		try {
			final Class<?> cl = Class.forName(className);
			final Constructor<?> m = cl.getConstructor(arg1.getClass());
			return m.newInstance(arg1);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object newInstance(String className, double arg1, double arg2, double arg3, double arg4) {
		try {
			final Class<?> cl = Class.forName(className);
			final Constructor<?> m = cl.getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE);
			return m.newInstance(arg1, arg2, arg3, arg4);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object field(String className, String fieldName) {
		try {
			final Class<?> cl = Class.forName(className);
			final Field f = cl.getField(fieldName);
			return f.get(null);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object callStatic(String className, String method) {
		try {
			final Class<?> cl = Class.forName(className);
			final Method m = cl.getMethod(method);
			return m.invoke(null);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object callStatic(String className, String method, Object arg1) {
		try {
			final Class<?> cl = Class.forName(className);
			final Method m = cl.getMethod(method, arg1.getClass());
			return m.invoke(null, arg1);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object callStatic(String className, String method, Object arg1, Object arg2) {
		try {
			final Class<?> cl = Class.forName(className);
			final Method m = cl.getMethod(method, arg1.getClass(), arg2.getClass());
			return m.invoke(null, arg1, arg2);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object callStatic2(String className, String method, Object arg1) {
		try {
			final Class<?> cl = Class.forName(className);
			final Method m = getStaticMethod(cl, method, 1);
			return m.invoke(null, arg1);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object callStatic2(String className, String method, Object arg1, Object arg2) {
		try {
			final Class<?> cl = Class.forName(className);
			final Method m = getStaticMethod(cl, method, 2);
			return m.invoke(null, arg1, arg2);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Method getStaticMethod(Class<?> cl, String method, int nbArgs) {
		for (Method m : cl.getMethods()) {
			if (m.getName().equals(method) && m.getParameters().length == nbArgs) {
				return m;
			}
		}
		throw new IllegalArgumentException();
	}

	public static Object call(Object instance, String method) {
		try {
			final Method m = instance.getClass().getMethod(method);
			return m.invoke(instance);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object call(Object instance, String method, Object arg1) {
		try {
			final Method m = instance.getClass().getMethod(method, arg1.getClass());
			return m.invoke(instance, arg1);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object call(Object instance, String method, Object arg1, Object arg2) {
		try {
			final Method m = instance.getClass().getMethod(method, arg1.getClass(), arg2.getClass());
			return m.invoke(instance, arg1, arg2);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Object call2(Object instance, String method, Object arg1, Object arg2) {
		try {
			final Method m = getMethod(instance, method, 2);
			return m.invoke(instance, arg1, arg2);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException(t);
		}
	}

	public static Method getMethod(Object instance, String method, int nbArgs) {
		for (Method m : instance.getClass().getMethods()) {
			if (m.getName().equals(method) && m.getParameters().length == nbArgs) {
				return m;
			}
		}
		throw new IllegalArgumentException();
	}

}
