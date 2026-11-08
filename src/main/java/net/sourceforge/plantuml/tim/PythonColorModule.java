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
package net.sourceforge.plantuml.tim;

import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.color.HSLColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import org.luaj.vm2.python.Builtin;
import org.luaj.vm2.python.PyModule;

/**
 * The text Python module <code>color</code>: the colour functions of the legacy
 * preprocessor (<code>%darken</code>, <code>%lighten</code>,
 * <code>%is_dark</code>, <code>%is_light</code>, <code>%reverse_color</code>,
 * <code>%reverse_hsluv_color</code>, <code>%hsl_color</code>), which call the
 * same code (<code>HColorSet</code>, <code>HColor</code>, <code>HSLColor</code>),
 * so that the result is the same string. A colour is a <code>str</code> as
 * PlantUML writes it (<code>"red"</code>, <code>"#FF0000"</code>); an unknown
 * one raises a <code>ValueError</code> ("No such color").
 */
final class PythonColorModule {

	private PythonColorModule() {
	}

	static PyModule create() {
		final PyModule m = new PyModule("color");
		m.put("darken", (in, a) -> {
			check("darken", a, 2);
			return color("darken", a[0]).darken(integer("darken", a[1])).asString();
		});
		m.put("lighten", (in, a) -> {
			check("lighten", a, 2);
			return color("lighten", a[0]).lighten(integer("lighten", a[1])).asString();
		});
		m.put("reverse_color", (in, a) -> {
			check("reverse_color", a, 1);
			return color("reverse_color", a[0]).reverse().asString();
		});
		m.put("reverse_hsluv_color", (in, a) -> {
			check("reverse_hsluv_color", a, 1);
			return color("reverse_hsluv_color", a[0]).reverseHsluv().asString();
		});
		m.put("is_dark", (in, a) -> {
			check("is_dark", a, 1);
			return Boolean.valueOf(color("is_dark", a[0]).isDark());
		});
		m.put("is_light", (in, a) -> {
			check("is_light", a, 1);
			return Boolean.valueOf(color("is_light", a[0]).isDark() == false);
		});
		m.put("hsl_color", (in, a) -> {
			if (a.length != 3 && a.length != 4)
				throw Builtin.typeError("hsl_color() takes 3 or 4 arguments (" + a.length + " given)");
			final int h = integer("hsl_color", a[0]);
			final int s = integer("hsl_color", a[1]);
			final int l = integer("hsl_color", a[2]);
			final HSLColor hsl = a.length == 3 ? new HSLColor(h, s, l)
					: new HSLColor(h, s, l, (float) (integer("hsl_color", a[3]) / 100.0));
			final XColor rgb = hsl.getRGB();
			return HColors.simple(rgb).asString();
		});
		return m;
	}

	private static void check(String function, Object[] a, int count) {
		if (a.length != count)
			throw Builtin.typeError(function + "() takes exactly " + count + " argument" + (count > 1 ? "s" : "")
					+ " (" + a.length + " given)");
	}

	private static HColor color(String function, Object value) {
		if (value instanceof String == false)
			throw Builtin.typeError(function + "() colour must be str");
		try {
			return HColorSet.instance().getColor((String) value);
		} catch (NoSuchColorException e) {
			throw Builtin.valueError("No such color");
		}
	}

	private static int integer(String function, Object value) {
		if (value instanceof Integer == false)
			throw Builtin.typeError(function + "() argument must be int");
		return ((Integer) value).intValue();
	}
}
