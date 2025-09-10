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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum CliFlag {

	AUTHOR(Arity.BOOLEAN, "-author", aliases("-authors", "-about"), "Print information about PlantUML authors"), //
	CHECK_METADATA(Arity.BOOLEAN, "-checkmetadata", "Skip PNG files that don't need to be regenerated"), //
	CHECK_ONLY(Arity.BOOLEAN, "-checkonly", "Check syntax of files without generating images"), //
	CHARSET(Arity.SINGLE_VALUE, "-charset", "Use a specific charset for input files"), //
	COMPUTE_URL(Arity.BOOLEAN, "-computeurl", aliases("-encodeurl"),
			"Compute the encoded URL of a PlantUML source file"), //
	CONFIG(Arity.SINGLE_VALUE, "-config", "???"), //
	CYPHER(Arity.BOOLEAN, "-cypher", "Encrypt diagram texts so they can be shared securely"), //
	DEFINE(Arity.KEY_VALUE, "-D", "Set a preprocessing variable as if '!define VAR value' were used"), //
	DARK_MODE(Arity.BOOLEAN, "-darkmode", "Use dark mode for diagrams"), //
	DEBUG_SVEK(Arity.BOOLEAN, "-debugsvek", "Generate intermediate Svek files"), //
	DECODE_URL(Arity.BOOLEAN, "-decodeurl", "Retrieve PlantUML source from an encoded URL"), //
	DISABLE_STATS(Arity.BOOLEAN, "-disablestats", "Disable statistics computation (default)"), //
	DURATION(Arity.BOOLEAN, "-duration", "Print total duration of diagram processing"), //
	EXCLUDE(Arity.SINGLE_VALUE, "-exclude", aliases("-xclude"), "Exclude files matching a given pattern"), //
	ENABLE_STATS(Arity.BOOLEAN, "-enablestats", "Enable statistics computation"), //
	ENCODE_SPRITE(Arity.BOOLEAN, "-encodesprite", "Encode a sprite from an image"), //
	FAIL_FAST(Arity.BOOLEAN, "-failfast", "Stop processing on first syntax error"), //
	FAIL_FAST2(Arity.BOOLEAN, "-failfast2", "Syntax check before processing for faster failure"), //
	FILE_DIR(Arity.SINGLE_VALUE, "-filedir", "Pretend input files are located in given directory"), //
	FILENAME(Arity.SINGLE_VALUE, "-filename", "Override %filename% variable"), //
	FTP(Arity.KEY_OPTIONAL_COLON_VALUE, "-ftp", "???"), //
	GRAPHVIZ_DOT(Arity.SINGLE_VALUE, "-graphvizdot", aliases("-graphviz_dot"), "Specify dot executable path"), //
	GUI(Arity.BOOLEAN, "-gui", "Run the graphical user interface"), //
	HELP(Arity.BOOLEAN, "-help", aliases("-h"), "Display help message"), //
	HTML_STATS(Arity.BOOLEAN, "-htmlstats", "Output general statistics in HTML format"), //
	HEADLESS(Arity.BOOLEAN, "-headless", "?"), //
	INCLUDE(Arity.BOOLEAN, "-I", "Include external files as if '!include file' were used"), //
	LANGUAGE(Arity.BOOLEAN, "-language", "Print the list of PlantUML keywords"), //
	LOOP_STATS(Arity.BOOLEAN, "-loopstats", "Continuously print usage statistics"), //
	METADATA(Arity.BOOLEAN, "-metadata", "Retrieve PlantUML sources from PNG images"), //
	NB_THREAD(Arity.SINGLE_VALUE, "-nbthread", "Use N threads for processing (or 'auto')"), //
	NO_ERROR(Arity.BOOLEAN, "-noerror", "Skip images when diagrams contain errors"), //
	NO_METADATA(Arity.BOOLEAN, "-nometadata", "Do not export metadata in generated files"), //
	OFILE(Arity.SINGLE_VALUE, "-ofile", "???"), //
	OUTPUT(Arity.SINGLE_VALUE, "-output", "Generate images in the specified directory"), //
	OVERWRITE(Arity.BOOLEAN, "-overwrite", "Allow overwriting read-only files"), //
	PRAGMA(Arity.BOOLEAN, "-P", "Set pragma as if '!pragma key value' were used"), //
	PIPE(Arity.BOOLEAN, "-pipe", aliases("-p"), "Use stdin for source, stdout for output"), //
	PICOWEB(Arity.BOOLEAN, "-picoweb", "Start internal HTTP server for rendering"), //
	PIPE_IMAGE_INDEX(Arity.BOOLEAN, "-pipeimageindex", "Generate the Nth image with pipe option"), //
	PREPROCESS(Arity.BOOLEAN, "-preproc", "Output preprocessor text of diagrams"), //
	PRINT_FONTS(Arity.BOOLEAN, "-printfonts", "List fonts available on your system"), //
	PROGRESS(Arity.BOOLEAN, "-progress", "Display a textual progress bar in console"), //
	REALTIME_STATS(Arity.BOOLEAN, "-realtimestats", "Generate statistics on the fly"), //
	SKIN_PARAM(Arity.BOOLEAN, "-S", "Set a skin parameter as if 'skinparam key value' were used"), //
	SPLASH(Arity.BOOLEAN, "-splash", "Display splash screen with progress bar"), //
	STD_LIB(Arity.BOOLEAN, "-stdlib", "Print standard library information"), //
	SYNTAX(Arity.BOOLEAN, "-syntax", "Report syntax errors from stdin without generating images"), //
	TEST_DOT(Arity.BOOLEAN, "-testdot", "Test Graphviz installation"), //
	THEME(Arity.SINGLE_VALUE, "-theme", "Use a specific theme"), //
	TIMEOUT(Arity.SINGLE_VALUE, "-timeout", "Set processing timeout in seconds"), //
	T_EPS(Arity.BOOLEAN, "-teps", aliases("-eps"), "Generate images in EPS format"), //
	T_HTML(Arity.BOOLEAN, "-thtml", aliases("-html"), "Generate HTML file for class diagram"), //
	T_LATEX_NOPREAMBLE(Arity.BOOLEAN, "-tlatex:nopreamble", aliases("-latex:nopreamble"),
			"Generate LaTeX/Tikz without preamble"), //
	T_LATEX(Arity.BOOLEAN, "-tlatex", aliases("-latex"), "Generate LaTeX/Tikz output"), //
	T_PDF(Arity.BOOLEAN, "-tpdf", aliases("-pdf"), "Generate PDF images"), //
	T_PNG(Arity.BOOLEAN, "-tpng", aliases("-png"), "Generate PNG images (default)"), //
	T_SCXML(Arity.BOOLEAN, "-tscxml", "Generate SCXML file for state diagram"), //
	T_SVG(Arity.BOOLEAN, "-tsvg", aliases("-svg"), "Generate SVG images"), //
	T_TXT(Arity.BOOLEAN, "-ttxt", aliases("-txt"), "Generate ASCII art diagrams"), //
	T_UTXT(Arity.BOOLEAN, "-tutxt", aliases("-utxt"), "Generate ASCII art diagrams using Unicode"), //
	T_VDX(Arity.BOOLEAN, "-tvdx", aliases("-vdx"), "Generate VDX images"), //
	T_XMI(Arity.BOOLEAN, "-txmi", aliases("-xmi"), "Generate XMI files for class diagrams"), //
	VERBOSE(Arity.BOOLEAN, "-verbose", aliases("-v"), "Enable verbose log output"), //
	VERSION(Arity.BOOLEAN, "-version", "Display PlantUML and Java version info"), //
	XML_STATS(Arity.BOOLEAN, "-xmlstats", "Output general statistics in XML format"); //

	private final String flag;
	private final List<String> aliases;
	private final String description;
	private final Arity type;

	CliFlag(Arity type, String flag, String description) {
		this(type, flag, Collections.emptyList(), description);
	}

	CliFlag(Arity type, String flag, List<String> aliases, String description) {
		this.type = type;
		this.flag = flag;
		this.aliases = aliases;
		this.description = description;
	}

	private static List<String> aliases(String... names) {
		return Arrays.asList(names);
	}

	public String getFlag() {
		return flag;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public String getDescription() {
		return description;
	}

	public Arity getType() {
		return type;
	}

	boolean match(String tmp) {
		if (type == Arity.KEY_VALUE || type == Arity.KEY_OPTIONAL_COLON_VALUE)
			return (tmp.startsWith(flag));

		for (String alias : aliases)
			if (tmp.equals(alias))
				return true;

		return tmp.equals(flag);
	}
}
