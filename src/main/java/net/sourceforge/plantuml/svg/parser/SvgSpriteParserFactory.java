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
 *
 */
package net.sourceforge.plantuml.svg.parser;

import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.PragmaKey;
import net.sourceforge.plantuml.teavm.TeaVM;

public final class SvgSpriteParserFactory {

	private SvgSpriteParserFactory() {
	}

	public static ISvgSpriteParser create(String svg) {
		return create(Collections.singletonList(svg), null);
	}

	public static ISvgSpriteParser create(List<String> svg) {
		return create(svg, null);
	}

	public static ISvgSpriteParser create(String svg, Pragma pragma) {
		return create(Collections.singletonList(svg), pragma);
	}

	public static ISvgSpriteParser create(List<String> svg, Pragma pragma) {
		final String parser = getParserSelector(pragma);
		// TeaVM does not provide javax.xml.parsers; this branch forces Nano and is
		// evaluated at compile time, so TeaVM removes the unreachable JVM-only path
		// from the generated JavaScript output and class stubs.
		if (TeaVM.isTeaVM())
			return new SvgNanoParser(svg);

		if ("sax".equals(parser))
			return new SvgSaxParser(svg);

		// Default parser: nano unless explicitly set to sax.
		return new SvgNanoParser(svg);
	}

	private static String getParserSelector(Pragma pragma) {
		if (pragma != null && pragma.isDefine(PragmaKey.SVG_PARSER)) {
			String value = pragma.getValue(PragmaKey.SVG_PARSER);
			if (value == null)
				return "nano";
			value = value.trim().toLowerCase();
			if (value.length() == 0)
				return "nano";
			return value;
		}

		return "nano";
	}

}
