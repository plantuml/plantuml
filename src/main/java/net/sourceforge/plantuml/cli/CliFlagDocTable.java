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
 *
 */

package net.sourceforge.plantuml.cli;

/**
 * The values of {@link CliFlagDoc} and {@link CliDefaultValue} of every {@link CliFlag}, as plain
 * code: the TeaVM C build cannot read these annotations (no reflection on fields), but it prints
 * the same help as the JVM and needs the default values to parse the command line.
 *
 * <p>
 * Generated from the annotations of {@link CliFlag}: when you add or change one, update this class
 * as well ({@code CliFlagDocTableTest} fails otherwise).
 */
final class CliFlagDocTable {

	private CliFlagDocTable() {
	}

	/** The description of the flag, or null when it is not documented (not in the help). */
	static String doc(CliFlag flag) {
		switch (flag) {
		case HELP:
			return "Show help and usage information";
		case HELP_MORE:
			return "Show extended help (advanced options)";
		case VERSION:
			return "Show PlantUML and Java version";
		case AUTHOR:
			return "Show information about PlantUML authors";
		case GUI:
			return "Launch the graphical user interface";
		case DARK_MODE:
			return "Render diagrams in dark mode";
		case VERBOSE:
			return "Enable verbose logging";
		case DURATION:
			return "Print total processing time";
		case PROGRESS:
			return "Show a textual progress bar";
		case SPLASH:
			return "Show splash screen with progress bar";
		case TEST_DOT:
			return "Check Graphviz installation";
		case PICOWEB:
			return "Start internal HTTP server for rendering (default port : 8080)";
		case EXCLUDE:
			return "Exclude input files matching the given pattern";
		case PIPE:
			return "Read source from stdin, write result to stdout";
		case PIPE_IMAGE_INDEX:
			return "Generate the Nth image with pipe option";
		case DEFINE:
			return "Define a preprocessing variable (equivalent to '!define <var> <value>')";
		case INCLUDE:
			return "Include external file (as with '!include <file>')";
		case PRAGMA:
			return "Set pragma (equivalent to '!pragma <key> <value>')";
		case SKINPARAM:
			return "Set skin parameter (equivalent to 'skinparam <key> <value>')";
		case THEME:
			return "Apply a theme";
		case CONFIG:
			return "Specify configuration file";
		case CHARSET:
			return "Use a specific input charset";
		case CHECK_ONLY:
			return "Check diagram syntax without generating images";
		case FAIL_FAST:
			return "Stop at the first syntax error";
		case FAIL_FAST2:
			return "Pre-check syntax of all inputs and stop faster on error";
		case NO_ERROR_IMAGE:
			return "Do not generate error images for diagrams with syntax errors";
		case TIMEOUT:
			return "Set Graphviz processing timeout (in seconds)";
		case IGNORE_STARTUML_FILENAME:
			return "Ignore '@startuml <name>' and always derive output filenames from input files";
		case NB_THREAD:
			return "Use <n> threads for processing  (auto = available processors)";
		case LOOP:
			return "Runs the generation process <n> times, useful for performance testing";
		case RETRIEVE_METADATA:
			return "Extract embedded PlantUML source from PNG or SVG metadata";
		case NO_METADATA:
			return "Do not include metadata in generated files";
		case CHECK_METADATA:
			return "Skip PNG/SVG files that are already up-to-date (using metadata)";
		case ENCODE_SPRITE:
			return "Encode a sprite definition from an image file";
		case COMPUTE_URL:
			return "Generate an encoded PlantUML URL from a source file";
		case DECODE_URL:
			return "Decode a PlantUML encoded URL back to its source";
		case LANGUAGE:
			return "Print the list of PlantUML language keywords";
		case GRAPHVIZ_DOT:
			return "Specify the path to the Graphviz 'dot' executable";
		case FTP:
			return "Start a local FTP server for diagram rendering (rarely used)";
		case OUTPUT_DIR:
			return "Generate output files in the specified directory";
		case OVERWRITE:
			return "Allow overwriting of read-only output files";
		case DEBUG_SVEK:
			return "Generate intermediate Svek files";
		case FILE_DIR:
			return "Pretend input files are located in given directory";
		case FILENAME:
			return "Override %filename% variable";
		case PRINT_FONTS:
			return "List fonts available on your system";
		case STD_LIB:
			return "Print standard library information";
		case SYNTAX:
			return "Report syntax errors from stdin without generating images";
		case FORMAT:
			return "Set the output format for generated diagrams\n(e.g. png, svg, pdf, eps, latex, txt, utxt, obfuscate, preproc...)";
		case T_EPS:
			return "Generate images in EPS format";
		case T_HTML:
			return "Generate HTML files for class diagrams";
		case T_LATEX:
			return "Generate LaTeX/TikZ output";
		case T_LATEX_NOPREAMBLE:
			return "Generate LaTeX/TikZ output without preamble";
		case OBFUSCATE:
			return "Replace text in diagrams with obfuscated strings to share diagrams safely";
		case T_PDF:
			return "Generate PDF images";
		case T_PNG:
			return "Generate PNG images (default)";
		case PREPROCESS:
			return "Generate the preprocessed source after applying !include, !define... (no rendering)";
		case T_SCXML:
			return "Generate SCXML files for state diagrams";
		case T_SVG:
			return "Generate SVG images";
		case T_TXT:
			return "Generate ASCII art diagrams";
		case T_UTXT:
			return "Generate ASCII art diagrams using Unicode characters";
		case T_VDX:
			return "Generate VDX files";
		case T_XMI:
			return "Generate XMI files for class diagrams";
		case DISABLE_STATS:
			return "Disable statistics collection (default behavior)";
		case ENABLE_STATS:
			return "Enable statistics collection";
		case DUMPHTMLSTATS:
			return "Export collected statistics to an HTML report and exit";
		case DUMPSTATS:
			return "Export collected statistics to a text report and exit";
		case HTML_STATS:
			return "Output general statistics in HTML format";
		case XML_STATS:
			return "Output general statistics in XML format";
		case REALTIME_STATS:
			return "Generate statistics in real time during processing";
		case LOOP_STATS:
			return "Continuously print usage statistics during execution";
		default:
			return null;
		}
	}

	/** The title of the group of the help the flag starts, or an empty string. */
	static String newGroup(CliFlag flag) {
		switch (flag) {
		case EXCLUDE:
			return "Input & preprocessing";
		case CHECK_ONLY:
			return "Execution control";
		case RETRIEVE_METADATA:
			return "Metadata & assets";
		case OUTPUT_DIR:
			return "Output control";
		case FORMAT:
			return "Output format (choose one)";
		case T_EPS:
			return "Available formats";
		case DISABLE_STATS:
			return "Statistics";
		default:
			return "";
		}
	}

	/** The usage text of the flag, as written in its annotation, or null. */
	static String usage(CliFlag flag) {
		switch (flag) {
		case PICOWEB:
			return "--http-server[:<port>]";
		case EXCLUDE:
			return "--exclude <pattern>";
		case DEFINE:
			return "-d, --define <VAR>=<value>";
		case INCLUDE:
			return "-I, --include <file>";
		case PRAGMA:
			return "-P, --pragma <key>=<value>";
		case SKINPARAM:
			return "--skinparam <key>=<value>";
		case THEME:
			return "--theme <name>";
		case CONFIG:
			return "--config <file>";
		case CHARSET:
			return "--charset <name>";
		case TIMEOUT:
			return "--graphviz-timeout <seconds>";
		case NB_THREAD:
			return "--threads <n|auto>";
		case LOOP:
			return "--loop <n>";
		case ENCODE_SPRITE:
			return "--sprite <4|8|16[z]> <file>";
		case DECODE_URL:
			return "--decode-url <string>";
		case GRAPHVIZ_DOT:
			return "--dot-path <path-to-dot-exe>";
		case OUTPUT_DIR:
			return "--output-dir <dir>";
		case FORMAT:
			return "-f, --format <name>";
		default:
			return null;
		}
	}

	/** The level of the flag in the help (-1 when it is not documented). */
	static int level(CliFlag flag) {
		switch (flag) {
		case HELP:
			return 0;
		case HELP_MORE:
			return 0;
		case VERSION:
			return 0;
		case AUTHOR:
			return 0;
		case GUI:
			return 0;
		case DARK_MODE:
			return 0;
		case VERBOSE:
			return 0;
		case DURATION:
			return 0;
		case PROGRESS:
			return 0;
		case SPLASH:
			return 0;
		case TEST_DOT:
			return 0;
		case PICOWEB:
			return 0;
		case EXCLUDE:
			return 1;
		case PIPE:
			return 0;
		case PIPE_IMAGE_INDEX:
			return 999;
		case DEFINE:
			return 0;
		case INCLUDE:
			return 1;
		case PRAGMA:
			return 1;
		case SKINPARAM:
			return 1;
		case THEME:
			return 1;
		case CONFIG:
			return 1;
		case CHARSET:
			return 1;
		case CHECK_ONLY:
			return 0;
		case FAIL_FAST:
			return 0;
		case FAIL_FAST2:
			return 0;
		case NO_ERROR_IMAGE:
			return 0;
		case TIMEOUT:
			return 1;
		case IGNORE_STARTUML_FILENAME:
			return 1;
		case NB_THREAD:
			return 1;
		case LOOP:
			return 1;
		case RETRIEVE_METADATA:
			return 0;
		case NO_METADATA:
			return 1;
		case CHECK_METADATA:
			return 0;
		case ENCODE_SPRITE:
			return 0;
		case COMPUTE_URL:
			return 1;
		case DECODE_URL:
			return 1;
		case LANGUAGE:
			return 1;
		case GRAPHVIZ_DOT:
			return 1;
		case FTP:
			return 1;
		case OUTPUT_DIR:
			return 1;
		case OVERWRITE:
			return 1;
		case DEBUG_SVEK:
			return 999;
		case FILE_DIR:
			return 999;
		case FILENAME:
			return 999;
		case PRINT_FONTS:
			return 999;
		case STD_LIB:
			return 999;
		case SYNTAX:
			return 999;
		case FORMAT:
			return 0;
		case T_EPS:
			return 0;
		case T_HTML:
			return 1;
		case T_LATEX:
			return 0;
		case T_LATEX_NOPREAMBLE:
			return 1;
		case OBFUSCATE:
			return 0;
		case T_PDF:
			return 1;
		case T_PNG:
			return 0;
		case PREPROCESS:
			return 0;
		case T_SCXML:
			return 1;
		case T_SVG:
			return 0;
		case T_TXT:
			return 0;
		case T_UTXT:
			return 0;
		case T_VDX:
			return 1;
		case T_XMI:
			return 1;
		case DISABLE_STATS:
			return 1;
		case ENABLE_STATS:
			return 1;
		case DUMPHTMLSTATS:
			return 1;
		case DUMPSTATS:
			return 1;
		case HTML_STATS:
			return 1;
		case XML_STATS:
			return 1;
		case REALTIME_STATS:
			return 1;
		case LOOP_STATS:
			return 1;
		default:
			return -1;
		}
	}

	/** The default value of the flag, or null. */
	static String defaultValue(CliFlag flag) {
		switch (flag) {
		case PIPE_IMAGE_INDEX:
			return "0";
		case CHARSET:
			return "UTF-8";
		case LOOP:
			return "1";
		default:
			return null;
		}
	}
}
