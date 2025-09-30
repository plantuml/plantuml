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

import net.sourceforge.plantuml.security.SFile;

public class HelpPrint {
	// ::remove file when __CORE__
	// ::remove file when __HAXE__

	private static void printHeader() {
		System.out.println("plantuml - generate diagrams from plain text");
		System.out.println();
		System.out.println("Usage:");
		System.out.println("  java -jar plantuml.jar [options] [file|dir]...");
		System.out.println("  java -jar plantuml.jar [options] -gui");
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
		System.out.println("  java -jar plantuml.jar -checkonly src/diagrams");
		System.out.println();
		System.out.println("  # Read from stdin and write to stdout (SVG)");
		System.out.println("  cat diagram.puml | java -jar plantuml.jar -tsvg -pipe > out.svg");
		System.out.println();
		System.out.println("  # Encode a sprite from an image");
		System.out.println("  java -jar plantuml.jar -encodesprite myicon.png");
		System.out.println();
		System.out.println("  # Use a define");
		System.out.println("  java -jar plantuml.jar -DAUTHOR=John diagram.puml");
		System.out.println();
		System.out.println("Exit codes:");
		System.out.println("  0   Success");
		System.out.println("  >0  Error (syntax error or processing failure)");
		System.out.println();
		System.out.println("See also:");
		System.out.println("  java -jar plantuml.jar -help:more");
		System.out.println("  Documentation: https://plantuml.com");
	}

	private static HelpTable getHelpTable(int limit) {
		final HelpTable table = new HelpTable();

		for (CliFlag flag : CliFlag.values()) {
			final String doc = flag.getFlagDoc();
			final int level = flag.getFlagLevel();
			final String newgroup = flag.getNewgroup();

			if (doc != null && level <= limit) {
				if (newgroup.length() > 0)
					table.newLine("", newgroup);

				table.newLine(flag.getUsage(), doc);
			}
		}
		return table;
	}

	private void printHelpOld() throws InterruptedException {

		final char separator = SFile.separatorChar;
		System.out.println("where options include:");
		System.out.println("    -debugsvek\t\tTo generate intermediate svek files");
		System.out.println(
				"    -encodesprite 4|8|16[z] \"file\"\tTo encode a sprite at gray level (z for compression) from an image");
		// System.out.println(" -extractstdlib\tTo extract PlantUML Standard Library
		// into stdlib folder");
		System.out.println("    -filename \"example.puml\"\tTo override %filename% variable");
		System.out.println("    -pipeimageindex N\tTo generate the Nth image with pipe option");
		System.out.println("    -printfonts\t\tTo print fonts available on your system");
		System.out.println("    -stdlib\t\tTo print standard library info");
		System.out.println("    -syntax\t\tTo report any syntax error from standard input without generating images");
		// exit(0);
	}

}
