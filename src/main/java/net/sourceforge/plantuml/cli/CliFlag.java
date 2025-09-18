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

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.stats.StatsUtils;
import net.sourceforge.plantuml.swing.ClipboardLoop;

public enum CliFlag {

	@CliFlagDoc("Print information about PlantUML authors")
	AUTHOR("-author", aliases("-authors", "-about"), Arity.UNARY_IMMEDIATE_ACTION, OptionPrint::printAbout), //

	@CliFlagDoc("Skip PNG files that don't need to be regenerated")
	CHECK_METADATA("-checkmetadata", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Check syntax of files without generating images")
	CHECK_ONLY("-checkonly", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Use a specific charset for input files")
	@CliDefaultValue("UTF-8")
	CHARSET("-charset", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc("Compute the encoded URL of a PlantUML source file")
	COMPUTE_URL("-computeurl", aliases("-encodeurl"), Arity.UNARY_BOOLEAN), //

	CLIPBOARD("-clipboard", Arity.UNARY_IMMEDIATE_ACTION, ClipboardLoop::runOnce), //

	CLIPBOARDLOOP("-clipboardloop", Arity.UNARY_IMMEDIATE_ACTION, ClipboardLoop::runLoop), //

	@CliFlagDoc("Specify configuration file")
	CONFIG("-config", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc("Encrypt diagram texts so they can be shared securely")
	CYPHER("-cypher", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Set a preprocessing variable as if '!define VAR value' were used")
	DEFINE("-D", Arity.UNARY_INLINE_KEY_OR_KEY_VALUE), //
	// !PLANTUML_LIMIT_SIZE

	@CliFlagDoc("Use dark mode for diagrams")
	DARK_MODE("-darkmode", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Generate intermediate Svek files")
	DEBUG_SVEK("-debugsvek", aliases("-debug_svek"), Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Retrieve PlantUML source from an encoded URL")
	DECODE_URL("-decodeurl", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Disable statistics computation (default)")
	DISABLE_STATS("-disablestats", Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.ENABLE_STATS, true)),

	@CliFlagDoc("Print total duration of diagram processing")
	DURATION("-duration", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Exclude files matching a given pattern")
	EXCLUDE("-exclude", aliases("-x"), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc("Enable statistics computation")
	ENABLE_STATS("-enablestats", Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.ENABLE_STATS, true)),

	DUMPHTMLSTATS("-dumphtmlstats", Arity.UNARY_IMMEDIATE_ACTION, StatsUtils::outHtml),

	DUMPSTATS("-dumpstats", Arity.UNARY_IMMEDIATE_ACTION, StatsUtils::dumpStats),

	@CliFlagDoc("Encode a sprite from an image")
	ENCODE_SPRITE("-encodesprite", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Stop processing on first syntax error")
	FAIL_FAST("-failfast", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Syntax check before processing for faster failure")
	FAIL_FAST2("-failfast2", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Pretend input files are located in given directory")
	FILE_DIR("-filedir", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc("Override %filename% variable")
	FILENAME("-filename", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	FTP("-ftp", Arity.UNARY_OPTIONAL_COLON),

	@CliFlagDoc("Specify dot executable path")
	GRAPHVIZ_DOT("-graphvizdot", aliases("-graphviz_dot"), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc("Run the graphical user interface")
	GUI("-gui", Arity.UNARY_BOOLEAN, () -> GlobalConfig.getInstance().put(GlobalConfigKey.GUI, true)),

	@CliFlagDoc("Display help message")
	HELP("-help", aliases("-h", "-?", "--help"), Arity.UNARY_IMMEDIATE_ACTION, OptionPrint::printHelp2),

	@CliFlagDoc("Output general statistics in HTML format")
	HTML_STATS("-htmlstats", Arity.UNARY_BOOLEAN, () -> StatsUtils.setHtmlStats(true)),

	HEADLESS("-headless", Arity.UNARY_BOOLEAN, () -> System.setProperty("java.awt.headless", "true")),

	@CliFlagDoc("Include external files as if '!include file' were used")
	INCLUDE("-I", Arity.UNARY_INLINE_KEY_OR_KEY_VALUE), //

	@CliFlagDoc("Print the list of PlantUML keywords")
	LANGUAGE("-language", Arity.UNARY_IMMEDIATE_ACTION, OptionPrint::printLanguage),

	@CliFlagDoc("Continuously print usage statistics")
	LOOP_STATS("-loopstats", Arity.UNARY_IMMEDIATE_ACTION, StatsUtils::loopStats),

	@CliFlagDoc("Retrieve PlantUML sources from PNG/SVG images")
	RETRIEVE_METADATA("-metadata", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Use N threads for processing (or 'auto')")
	NB_THREAD("-nbthread", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc("Skip images when diagrams contain errors")
	NO_ERROR("-noerror", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Do not export metadata in generated files")
	NO_METADATA("-nometadata", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Generate images in the specified directory")
	OUTPUT_DIR("-output", aliases("-odir"), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc("Allow overwriting read-only files")
	OVERWRITE("-overwrite", Arity.UNARY_BOOLEAN, () -> GlobalConfig.getInstance().put(GlobalConfigKey.OVERWRITE, true)),

	@CliFlagDoc("Set pragma as if '!pragma key value' were used")
	PRAGMA("-P", Arity.UNARY_INLINE_KEY_OR_KEY_VALUE), //

	@CliFlagDoc("Use stdin for source, stdout for output")
	PIPE("-pipe", aliases("-p"), Arity.UNARY_BOOLEAN),

	PIPEMAP("-pipemap", Arity.UNARY_BOOLEAN), //
	PIPEDELIMITOR("-pipedelimitor", Arity.BINARY_NEXT_ARGUMENT_VALUE), //
	PIPENOSTDERR("-pipenostderr", Arity.UNARY_BOOLEAN), //

	@CliFlagDoc("Start internal HTTP server for rendering")
	PICOWEB("-picoweb", Arity.UNARY_OPTIONAL_COLON),

	@CliFlagDoc("Generate the Nth image with pipe option")
	PIPE_IMAGE_INDEX("-pipeimageindex", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc("Output preprocessor text of diagrams")
	PREPROCESS("-preproc", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("List fonts available on your system")
	PRINT_FONTS("-printfonts", Arity.UNARY_IMMEDIATE_ACTION, Run::printFonts),

	@CliFlagDoc("Display a textual progress bar in console")
	PROGRESS("-progress", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Generate statistics on the fly")
	REALTIME_STATS("-realtimestats", Arity.UNARY_BOOLEAN, () -> StatsUtils.setRealTimeStats(true)),

	@CliFlagDoc("Set a skin parameter as if 'skinparam key value' were used")
	SKINPARAM("-S", Arity.UNARY_INLINE_KEY_OR_KEY_VALUE), //

	@CliFlagDoc("Display splash screen with progress bar")
	SPLASH("-splash", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Print standard library information")
	STD_LIB("-stdlib", Arity.UNARY_IMMEDIATE_ACTION, Stdlib::printStdLib),

	STDRPT("-stdrpt", Arity.UNARY_OPTIONAL_COLON),

	@CliFlagDoc("Report syntax errors from stdin without generating images")
	SYNTAX("-syntax", Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Test Graphviz installation")
	TEST_DOT("-testdot", Arity.UNARY_IMMEDIATE_ACTION, OptionPrint::printTestDot2),

	@CliFlagDoc("Use a specific theme")
	THEME("-theme", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc("Set processing timeout in seconds")
	TIMEOUT("-timeout", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc("Generate images in EPS format")
	T_EPS("-teps", aliases("-eps"), Arity.UNARY_BOOLEAN, FileFormat.EPS),
	T_EPS_TEXT("-teps:text", aliases("-eps:text"), Arity.UNARY_BOOLEAN, FileFormat.EPS_TEXT),

	@CliFlagDoc("Generate HTML file for class diagram")
	T_HTML("-thtml", aliases("-html"), Arity.UNARY_BOOLEAN, FileFormat.HTML),

	@CliFlagDoc("Generate LaTeX/Tikz without preamble")
	T_LATEX_NOPREAMBLE("-tlatex:nopreamble", aliases("-latex:nopreamble"), Arity.UNARY_BOOLEAN,
			FileFormat.LATEX_NO_PREAMBLE),

	@CliFlagDoc("Generate LaTeX/Tikz output")
	T_LATEX("-tlatex", aliases("-latex"), Arity.UNARY_BOOLEAN, FileFormat.LATEX),

	@CliFlagDoc("Generate PDF images")
	T_PDF("-tpdf", aliases("-pdf"), Arity.UNARY_BOOLEAN, FileFormat.PDF),

	@CliFlagDoc("Generate PNG images (default)")
	T_PNG("-tpng", aliases("-png"), Arity.UNARY_BOOLEAN, FileFormat.PNG),

	T_BASE64("-tbase64", aliases("-base64"), Arity.UNARY_BOOLEAN, FileFormat.BASE64),
	T_BRAILLE("-tbraille", aliases("-braille"), Arity.UNARY_BOOLEAN, FileFormat.BRAILLE_PNG),

	@CliFlagDoc("Generate SCXML file for state diagram")
	T_SCXML("-tscxml", Arity.UNARY_BOOLEAN, FileFormat.SCXML),

	@CliFlagDoc("Generate SVG images")
	T_SVG("-tsvg", aliases("-svg"), Arity.UNARY_BOOLEAN, FileFormat.SVG),

	@CliFlagDoc("Generate ASCII art diagrams")
	T_TXT("-ttxt", aliases("-txt"), Arity.UNARY_BOOLEAN, FileFormat.ATXT),

	@CliFlagDoc("Generate ASCII art diagrams using Unicode")
	T_UTXT("-tutxt", aliases("-utxt"), Arity.UNARY_BOOLEAN, FileFormat.UTXT),

	@CliFlagDoc("Generate VDX images")
	T_VDX("-tvdx", aliases("-vdx"), Arity.UNARY_BOOLEAN, FileFormat.VDX),

	@CliFlagDoc("Generate XMI files for class diagrams")
	T_XMI("-txmi", aliases("-xmi"), Arity.UNARY_BOOLEAN, FileFormat.XMI_STANDARD),
	T_XMI_ARGO("-txmi:argo", aliases("-xmi:argo"), Arity.UNARY_BOOLEAN, FileFormat.XMI_ARGO),
	T_XMI_CUSTOM("-txmi:custom", aliases("-xmi:custom"), Arity.UNARY_BOOLEAN, FileFormat.XMI_CUSTOM),
	T_XMI_SCRIPT("-txmi:script", aliases("-xmi:script"), Arity.UNARY_BOOLEAN, FileFormat.XMI_SCRIPT),
	T_XMI_STAR("-txmi:star", aliases("-xmi:star"), Arity.UNARY_BOOLEAN, FileFormat.XMI_STAR),

	@CliFlagDoc("Enable verbose log output")
	VERBOSE("-verbose", aliases("-v", "--verbose"), Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.VERBOSE, true)),

	@CliFlagDoc("Display PlantUML and Java version info")
	VERSION("-version", aliases("--version"), Arity.UNARY_IMMEDIATE_ACTION, OptionPrint::printVersion),

	LICENSE("-license", aliases("-licence"), Arity.UNARY_IMMEDIATE_ACTION, OptionPrint::printLicense),

	WORD("-word", Arity.UNARY_BOOLEAN),
	USE_SEPARATOR_MINUS("-useseparatorminus", Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.FILE_SEPARATOR, "-")),

	@CliFlagDoc("Output general statistics in XML format")
	XML_STATS("-xmlstats", Arity.UNARY_BOOLEAN, () -> StatsUtils.setXmlStats(true));

	private final String flag;
	private final List<String> aliases;
	private final Arity type;
	private final Object foo;

	CliFlag(String flag, Arity type) {
		this(flag, Collections.emptyList(), type, null);
	}

	CliFlag(String flag, List<String> aliases, Arity type) {
		this(flag, aliases, type, null);
	}

	CliFlag(String flag, Arity type, CliAction foo) {
		this(flag, Collections.emptyList(), type, foo);
	}

	CliFlag(String flag, Arity type, Object foo) {
		this(flag, Collections.emptyList(), type, foo);
	}

	CliFlag(String flag, List<String> aliases, Arity type, Object foo) {
		this.type = type;
		this.flag = flag;
		this.aliases = aliases;
		this.foo = foo;
	}

	CliFlag(String flag, List<String> aliases, Arity type, CliAction foo) {
		this.type = type;
		this.flag = flag;
		this.aliases = aliases;
		this.foo = foo;
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

	public Arity getType() {
		return type;
	}

	boolean match(String tmp) {
		if (type == Arity.UNARY_INLINE_KEY_OR_KEY_VALUE || type == Arity.UNARY_OPTIONAL_COLON)
			return (tmp.startsWith(flag));

		for (String alias : aliases)
			if (tmp.equals(alias))
				return true;

		return tmp.equals(flag);
	}

	public String getFlagDoc() {
		try {
			final CliFlagDoc annotation = CliFlag.class.getField(this.name()).getAnnotation(CliFlagDoc.class);
			if (annotation != null && !annotation.value().isEmpty())
				return annotation.value();

		} catch (NoSuchFieldException e) {
			// Should never happen for enum constants
		}
		return null;
	}

	public String getDefaultValue() {
		try {
			final CliDefaultValue annotation = CliFlag.class.getField(this.name()).getAnnotation(CliDefaultValue.class);
			if (annotation != null && !annotation.value().isEmpty())
				return annotation.value();

		} catch (NoSuchFieldException e) {
			// Should never happen for enum constants
		}
		return null;
	}

	public Object getFoo() {
		return foo;
	}

}
