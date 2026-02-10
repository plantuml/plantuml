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
package net.sourceforge.plantuml.cli;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HelpPrint {
	// ::remove file when __CORE__ or __TEAVM__
	// ::remove file when __HAXE__

	private static void printHeader() {
		System.out.println("plantuml - generate diagrams from plain text");
		System.out.println();
		System.out.println("Usage:");
		System.out.println("  java -jar plantuml.jar [options] [file|dir]...");
		System.out.println("  java -jar plantuml.jar [options] --gui");
		System.out.println();
		System.out.println("Description:");
		System.out.println(
				"  Process PlantUML sources from files, directories (optionally recursive), or stdin (-pipe).");
		System.out.println();
		System.out.println("Wildcards (for files/dirs):");
		System.out.println("  *   any characters except '/' and '\\'");
		System.out.println("  ?   exactly one character except '/' and '\\'");
		System.out.println("  **  any characters across directories (recursive)");
		System.out.println("  Tip: quote patterns to avoid shell expansion (e.g., \"**/*.puml\").");
		System.out.println();
		System.out.println("General:");
	}

	static public void printHelp() {
		final String charset = Charset.defaultCharset().displayName();
		printHeader();
		getHelpTable(0).printMe(System.out);
		printFooter();
	}

	static public void printHelpMore() {
		final String charset = Charset.defaultCharset().displayName();
		printHeader();
		getHelpTable(1).printMe(System.out);
		printFooter();
	}

	private static void printFooter() {
		System.out.println();
		System.out.println();
		System.out.println("Examples:");
		System.out.println("  # Process all .puml recursively");
		System.out.println("  java -jar plantuml.jar \"**/*.puml\"");
		System.out.println();
		System.out.println("  # Check syntax only (CI)");
		System.out.println("  java -jar plantuml.jar --check-syntax src/diagrams");
		System.out.println();
		System.out.println("  # Read from stdin and write to stdout (SVG)");
		System.out.println("  cat diagram.puml | java -jar plantuml.jar --svg -pipe > out.svg");
		System.out.println();
		System.out.println("  # Encode a sprite from an image");
		System.out.println("  java -jar plantuml.jar --sprite 16z myicon.png");
		System.out.println();
		System.out.println("  # Use a define");
		System.out.println("  java -jar plantuml.jar -DAUTHOR=John diagram.puml");
		System.out.println();
		System.out.println("  # Change output directory");
		System.out.println("  java -jar plantuml.jar --format svg --output-dir out diagrams/");
		System.out.println();
		System.out.println("Exit codes:");
		ExitStatus.printExitCodes();
		System.out.println();
		System.out.println("See also:");
		System.out.println("  java -jar plantuml.jar --help-more");
		System.out.println("  Documentation: https://plantuml.com");
	}

	private static HelpTable getHelpTable(int limit) {
		final HelpTable table = new HelpTable();

		final List<HelpGroup> groups = new ArrayList<>();
		groups.add(new HelpGroup("General"));

		for (CliFlag flag : CliFlag.values()) {
			final String doc = flag.getFlagDoc();
			final int level = flag.getFlagLevel();
			final String newgroup = flag.getNewgroup();

			if (doc != null && level <= limit) {
				if (newgroup.length() > 0)
					groups.add(new HelpGroup(newgroup));

				groups.get(groups.size() - 1).append(flag);
			}
		}

		for (HelpGroup group : groups) {
			table.newLine("", group.getTitle());

			for (CliFlag flag : group)
				table.newLine(flag.getUsage(), flag.getFlagDoc());
		}

		return table;
	}

}
