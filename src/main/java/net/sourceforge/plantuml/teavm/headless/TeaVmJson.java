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
package net.sourceforge.plantuml.teavm.headless;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

/**
 * Small TeaVM helper to build JSON results from Java without hand-rolling the
 * escaping.
 *
 * <p>
 * Instead of assembling JSON text character by character (and getting quoting,
 * newlines and control-character escapes right by hand), the headless entry
 * point builds a native JS object/array through these {@code @JSBody} helpers
 * and serializes it with the platform's {@code JSON.stringify}. This mirrors
 * the {@code stringify} helper already used by
 * {@link net.sourceforge.plantuml.teavm.browser.TeaVmScriptLoader}.
 *
 * <p>
 * All methods are TeaVM-only (they compile to plain JS); they are not meant to
 * be called from a normal JVM.
 */
public final class TeaVmJson {
	// ::remove file when JAVA8

	/** @return a fresh, empty JS object ({@code {}}). */
	@JSBody(script = "return {};")
	public static native JSObject newObject();

	/** @return a fresh, empty JS array ({@code []}). */
	@JSBody(script = "return [];")
	public static native JSObject newArray();

	/** Sets {@code obj[key] = value} for a string value. */
	@JSBody(params = { "obj", "key", "value" }, script = "obj[key] = value;")
	public static native void putString(JSObject obj, String key, String value);

	public static void addStringSafe(final JSObject json, String key, String value) {
		if (value != null && value.isEmpty() == false)
			TeaVmJson.putString(json, key, value);
	}

	/** Sets {@code obj[key] = value} for an integer value. */
	@JSBody(params = { "obj", "key", "value" }, script = "obj[key] = value;")
	public static native void putInt(JSObject obj, String key, int value);

	/** Sets {@code obj[key] = value} for a boolean value. */
	@JSBody(params = { "obj", "key", "value" }, script = "obj[key] = value;")
	public static native void putBoolean(JSObject obj, String key, boolean value);

	/** Sets {@code obj[key] = value} for a nested object/array value. */
	@JSBody(params = { "obj", "key", "value" }, script = "obj[key] = value;")
	public static native void putObject(JSObject obj, String key, JSObject value);

	/** Appends a string to a JS array. */
	@JSBody(params = { "arr", "value" }, script = "arr.push(value);")
	public static native void push(JSObject arr, String value);

	/**
	 * Serializes a JS object/array to a JSON string with the native
	 * {@code JSON.stringify}.
	 *
	 * @param obj a JS object or array
	 * @return its JSON representation, or {@code null} if {@code obj} is null
	 */
	@JSBody(params = "obj", script = "return obj == null ? null : JSON.stringify(obj);")
	public static native String stringify(JSObject obj);

	private TeaVmJson() {
	}
}
