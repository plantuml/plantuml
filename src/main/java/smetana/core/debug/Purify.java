/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
 */
package smetana.core.debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import gen.annotation.HasND_Rank;
import gen.annotation.Original;
import gen.annotation.Reviewed;

public final class Purify {
	// ::remove file when __CORE__

	private final Map<String, Method> methods = new LinkedHashMap<>();
	private final File out2 = new File("../out-smetana", "smetana.txt");
	private PrintWriter pw2;
	private int currentLevel;

	public Purify() {
		try {
			out2.getParentFile().mkdirs();
			pw2 = new PrintWriter(out2);
			System.err.println("CREATING " + out2.getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	synchronized void logline(String s) {
		System.err.println(s);
		if (pw2 == null)
			return;
		pw2.println(s);
		pw2.flush();
	}

	public synchronized void entering(String signature, String methodNameDeclared) {
		if (methods.containsKey(signature) == false) {
			try {
				final Method method = recordMe(methodNameDeclared);
				methods.put(signature, method);
				final String key = getKey(method);
				if (key.length() > 2 && key.equals(signature) == false) {
					throw new IllegalStateException(signature);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		currentLevel++;
		traceCall(pw2, signature, methodNameDeclared);
	}

	public synchronized void leaving(String signature, String methodName) {
		currentLevel--;
	}

	private void traceCall(PrintWriter pw, String signature, String methodNameDeclared) {
		final Method m = methods.get(signature);
		final String line = String.format("(%02d) %-26.26s [%-10.10s] %s", currentLevel, signature, getReviewedWhen(m),
				methodNameDeclared);
//		if (SmetanaDebug.VERY_VERBOSE)
//			System.err.println(line);
		if (pw == null)
			return;
		pw.println(line);
		pw.flush();
	}

	private synchronized Method recordMe(String methodNameDeclared) throws ClassNotFoundException {
		final Throwable creationPoint = new Throwable();
		creationPoint.fillInStackTrace();
		final StackTraceElement ste3 = creationPoint.getStackTrace()[3];
		final StackTraceElement ste4 = creationPoint.getStackTrace()[4];
		final String className = ste3.getClassName();
		final String methodName3 = ste3.getMethodName();
		final String methodName4 = ste4.getMethodName();

		final Class<?> theClass = Class.forName(className);
		final Method theMethod3 = getTheMethod(theClass, methodName3);

		if (methodNameDeclared.equals(theMethod3.getName())) {
			return theMethod3;
		}

		final Method theMethod4 = getTheMethod(theClass, methodName4);
		if (checkWarning(methodNameDeclared, theMethod3) && methodNameDeclared.equals(theMethod4.getName())) {
			return theMethod4;
		}
		creationPoint.printStackTrace();
		System.exit(0);
		throw new IllegalArgumentException();
	}

	private synchronized boolean checkWarning(String methodNameDeclared, final Method theMethod3) {
		if ((methodNameDeclared + "_").equals(theMethod3.getName())) {
			return true;
		}
		if ((methodNameDeclared + "_w_").equals(theMethod3.getName())) {
			return true;
		}
		return false;
	}

	private synchronized Method getTheMethod(Class<?> theClass, String methodName2) {
		for (Method method : theClass.getDeclaredMethods()) {
			if (method.getName().equals(methodName2)) {
				return method;
			}
		}
		final Throwable creationPoint = new Throwable();
		creationPoint.fillInStackTrace();
		creationPoint.printStackTrace();
		System.exit(0);
		throw new IllegalArgumentException();
	}

	public void reset() {
		methods.clear();
	}

	public void printMe() {
		final List<Entry<String, Method>> reverse = new ArrayList<>(methods.entrySet());
		Collections.reverse(reverse);
		for (Entry<String, Method> ent : reverse) {
			final String signature = ent.getKey();
			final Method m = ent.getValue();
			final String reviewedWhen = getReviewedWhen(m);
			final String version = getVersion(m);
			final String path = getPath(m);
			final String hasND_Rank = hasND_Rank(m) ? "*" : " ";
			System.err.printf("%-8s %-26s %-12s %s %-30s %s%n", version, signature, reviewedWhen, hasND_Rank,
					m.getName(), path);
		}
	}

	private String getVersion(Method method) {
		final Original original = method.getDeclaredAnnotation(Original.class);
		if (original == null)
			return "XX";
		return original.version();
	}

	private String getPath(Method method) {
		final Original original = method.getDeclaredAnnotation(Original.class);
		if (original == null)
			return "XX";
		return original.path();
	}

	private String getReviewedWhen(Method method) {
		final Reviewed reviewed = method.getDeclaredAnnotation(Reviewed.class);
		if (reviewed == null) {
			return "?";
		}
		return reviewed.when();
	}

	private boolean hasND_Rank(Method method) {
		final HasND_Rank reviewed = method.getDeclaredAnnotation(HasND_Rank.class);
		return reviewed != null;
	}

	private String getKey(Method method) {
		final Original original = method.getDeclaredAnnotation(Original.class);
		if (original == null) {
			return "?";
		}
		return original.key();
	}

}
