/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.teavm.c;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;

/**
 * Smallest possible entry point for the experimental TeaVM C backend.
 *
 * Nothing here touches PlantUML itself: the goal is only to check that the
 * whole tool chain works (Gradle generateC task, then a native C compiler).
 *
 * Each step prints a numbered line, so that if the native binary crashes we
 * immediately see which part of the runtime is involved (strings, collections,
 * lambdas, exceptions, garbage collector).
 */
public class HelloWorldC {

	public static void main(String[] args) {
		System.out.println("1. Hello from PlantUML compiled to C");

		// String concatenation (invokedynamic StringConcatFactory with --release 11)
		final String name = args.length > 0 ? args[0] : "world";
		System.out.println("2. Hello " + name + " (" + args.length + " args)");

		// Collections
		final List<String> list = new ArrayList<>();
		for (int i = 0; i < 5; i++)
			list.add("item" + i);
		final Map<String, Integer> map = new HashMap<>();
		for (String s : list)
			map.put(s, s.length());
		System.out.println("3. Collections: " + list + " " + map.get("item3"));

		// Lambda (invokedynamic LambdaMetafactory)
		final IntUnaryOperator square = x -> x * x;
		System.out.println("4. Lambda: " + square.applyAsInt(12));

		// Exceptions
		try {
			final int[] array = new int[2];
			array[args.length + 5] = 1;
			System.out.println("5. Exceptions: NOT OK, no exception thrown");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("5. Exceptions: OK");
		}

		// Garbage collector: allocate much more than the heap size
		long total = 0;
		for (int i = 0; i < 200_000; i++) {
			final StringBuilder sb = new StringBuilder();
			sb.append("block").append(i);
			total += sb.toString().length();
		}
		System.out.println("6. GC survived, total=" + total);

		// Floating point formatting
		System.out.println("7. Double: " + (Math.sqrt(2) * 100));

		System.out.println("8. Done");
	}

}
