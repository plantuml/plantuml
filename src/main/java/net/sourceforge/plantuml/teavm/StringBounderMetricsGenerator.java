/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2026, Arnaud Roques
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
package net.sourceforge.plantuml.teavm;

import java.util.Locale;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFactory;

/**
 * Generates the advance-width tables embedded in {@link StringBounderMetrics}.
 * <p>
 * Run this class on a standard JVM (NOT under TeaVM), where AWT
 * {@code FontMetrics} is available through {@code FileFormat.PNG}, then paste the
 * printed {@code ADVANCE}, {@code LATIN1_SUP}, {@code GREEK} and {@code CYRILLIC}
 * arrays back into {@link StringBounderMetrics}:
 *
 * <pre>
 * java net.sourceforge.plantuml.teavm.StringBounderMetricsGenerator
 * </pre>
 *
 * Each table is calibrated for the reference font size used by
 * {@link StringBounderMetrics} (12). A code point whose measured width matches
 * the narrow fallback (so the table would add nothing) is emitted as 0, meaning
 * "let the runtime apply the generic rules".
 */
public final class StringBounderMetricsGenerator {

	private static final int REFERENCE_SIZE = 12;

	/** Code points measured as 0 (or close to it) are emitted as 0. */
	private static final double EPSILON = 0.05;

	private StringBounderMetricsGenerator() {
	}

	public static void main(String[] args) {
		final StringBounder stringBounder = FileFormat.PNG.getDefaultStringBounder();
		final UFont font = UFontFactory.sansSerif(REFERENCE_SIZE);

		System.out.println(emitRange(stringBounder, font, "ADVANCE", 0x0020, 0x007E));
		System.out.println();
		System.out.println(emitRange(stringBounder, font, "LATIN1_SUP", 0x00A0, 0x00FF));
		System.out.println();
		System.out.println(emitRange(stringBounder, font, "GREEK", 0x0370, 0x03FF));
		System.out.println();
		System.out.println(emitRange(stringBounder, font, "CYRILLIC", 0x0400, 0x04FF));
	}

	private static String emitRange(StringBounder stringBounder, UFont font, String name, int first, int last) {
		final StringBuilder result = new StringBuilder();
		result.append("\tprivate static final double[] ").append(name).append(" = {");

		for (int cp = first; cp <= last; cp++) {
			final double w = widthOf(stringBounder, font, cp);
			result.append(String.format(Locale.US, " %3.1f,", w));
		}

		result.append(" };");
		return result.toString();
	}

	private static double widthOf(StringBounder stringBounder, UFont font, int cp) {
		// Skip code points that are not assigned or that are zero-width by nature:
		// the runtime already maps them to 0 through its own rules.
		if (Character.isDefined(cp) == false)
			return 0;
		if (Character.getType(cp) == Character.NON_SPACING_MARK)
			return 0;
		if (Character.getType(cp) == Character.ENCLOSING_MARK)
			return 0;
		if (Character.getType(cp) == Character.COMBINING_SPACING_MARK)
			return 0;

		final String s = new String(Character.toChars(cp));
		final double w = stringBounder.calculateDimension(font, s).getWidth();
		if (w < EPSILON)
			return 0;

		return w;
	}

}
