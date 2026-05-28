/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2021, Arnaud Roques
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
package net.sourceforge.plantuml.theme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Generates {@code ThemeList.java} from the bundled theme names returned by
 * {@link ThemeUtils#getAllThemeNames()}.
 * <p>
 * Run this class on a standard JVM (NOT under TeaVM) from the project root:
 *
 * <pre>
 * java net.sourceforge.plantuml.theme.ThemeListGenerator
 * </pre>
 *
 * The generated class exposes {@code getAll()} which returns the list of theme
 * names, so it works inside a TeaVM-compiled context where the bundled
 * resource directory cannot be enumerated at runtime.
 */
public final class ThemeListGenerator {

	public static void main(String[] args) throws IOException {
		final List<String> names = ThemeUtils.getAllThemeNames();

		final File output = new File("src/main/java/net/sourceforge/plantuml/theme/ThemeList.java");
		if (output.getParentFile().isDirectory() == false)
			throw new IOException("Target directory does not exist: " + output.getParentFile());

		try (PrintStream out = new PrintStream(new FileOutputStream(output), false, StandardCharsets.UTF_8.name())) {
			out.println("package net.sourceforge.plantuml.theme;");
			out.println();
			out.println("import java.util.ArrayList;");
			out.println("import java.util.List;");
			out.println();
			out.println("// Generated \u2014 do not edit");
			out.println("// Built by ThemeListGenerator");
			out.println("public class ThemeList {");
			out.println();
			out.println("\tprivate final List<String> all = new ArrayList<>();");
			out.println();
			out.println("\tpublic ThemeList() {");
			for (String name : names)
				out.println("\t\tall.add(" + toJavaLiteral(name) + ");");
			out.println("\t}");
			out.println();
			out.println("\tpublic List<String> getAll() {");
			out.println("\t\treturn all;");
			out.println("\t}");
			out.println("}");
		}

		System.out.println("Generated " + output.getAbsolutePath());
	}

	private static String toJavaLiteral(String value) {
		return '"' + value.replace("\\", "\\\\").replace("\"", "\\\"") + '"';
	}

}
