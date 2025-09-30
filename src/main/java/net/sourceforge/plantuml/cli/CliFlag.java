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

	// General:

	@CliFlagDoc(value = "Show this help", level = 0)
	HELP("-help", aliases("-h", "-?", "--help"), Arity.UNARY_IMMEDIATE_ACTION, HelpPrint::printHelp),

	@CliFlagDoc(value = "Show extended help (advanced options)", level = 0)
	HELP_MORE("-help:more", aliases("-h:more", "-?:more", "--help:more"), Arity.UNARY_IMMEDIATE_ACTION,
			HelpPrint::printHelpMore),

	@CliFlagDoc(value = "Show PlantUML and Java version", level = 0)
	VERSION("-version", aliases("--version"), Arity.UNARY_IMMEDIATE_ACTION, OptionPrint::printVersion),

	@CliFlagDoc(value = "Show information about PlantUML authors", level = 0)
	AUTHOR("-author", aliases("-authors", "-about"), Arity.UNARY_IMMEDIATE_ACTION, OptionPrint::printAbout), //

	@CliFlagDoc(value = "Launch the graphical user interface", level = 0)
	GUI("-gui", Arity.UNARY_BOOLEAN, () -> GlobalConfig.getInstance().put(GlobalConfigKey.GUI, true)),

	@CliFlagDoc(value = "Render diagrams in dark mode", level = 0)
	DARK_MODE("-darkmode", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Enable verbose logging", level = 0)
	VERBOSE("-verbose", aliases("-v", "--verbose"), Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.VERBOSE, true)),

	@CliFlagDoc(value = "Print total processing time", level = 0)
	DURATION("-duration", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Show a textual progress bar", level = 0)
	PROGRESS("-progress", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Show splash screen with progress bar", level = 0)
	SPLASH("-splash", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Check Graphviz installation", level = 0)
	TEST_DOT("-testdot", Arity.UNARY_IMMEDIATE_ACTION, OptionPrint::printTestDot2),

	@CliFlagDoc(value = "Start internal HTTP server for rendering", level = 0)
	PICOWEB("-picoweb", Arity.UNARY_OPTIONAL_COLON),

	// Input & preprocessing:

	@CliFlagDoc(value = "Read source from stdin, write result to stdout", level = 0, newGroup = "Input & preprocessing")
	PIPE("-pipe", aliases("-p"), Arity.UNARY_BOOLEAN),

	PIPEMAP("-pipemap", Arity.UNARY_BOOLEAN), //
	PIPEDELIMITOR("-pipedelimitor", Arity.BINARY_NEXT_ARGUMENT_VALUE), //
	PIPENOSTDERR("-pipenostderr", Arity.UNARY_BOOLEAN), //

	@CliFlagDoc("Generate the Nth image with pipe option")
	@CliDefaultValue("0")
	PIPE_IMAGE_INDEX("-pipeimageindex", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Define a preprocessing variable (as with '!define VAR value')", usage="-DVAR=value", level = 0)
	DEFINE("-D", Arity.UNARY_INLINE_KEY_OR_KEY_VALUE), //

	@CliFlagDoc(value = "Include external file (as with '!include file')", usage="-Ifile", level = 1)
	INCLUDE("-I", Arity.UNARY_INLINE_KEY_OR_KEY_VALUE), //

	@CliFlagDoc(value = "Set pragma (as with '!pragma key value')", usage="-Pkey=value", level = 1)
	PRAGMA("-P", Arity.UNARY_INLINE_KEY_OR_KEY_VALUE), //

	@CliFlagDoc(value = "Set skin parameter (as with 'skinparam key value')", usage="-Skey=value", level = 1)
	SKINPARAM("-S", Arity.UNARY_INLINE_KEY_OR_KEY_VALUE), //

	@CliFlagDoc(value = "Apply a theme", usage="-theme name", level = 1)
	THEME("-theme", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Specify configuration file", usage="-config file", level = 1)
	CONFIG("-config", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Use a specific input charset", usage="-charset name", level = 1)
	@CliDefaultValue("UTF-8")
	CHARSET("-charset", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	// Execution control:

	@CliFlagDoc(value = "Check syntax without generating images", level = 0, newGroup = "Execution control")
	CHECK_ONLY("-checkonly", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Skip image generation when syntax errors are present", level = 0)
	NO_ERROR("-noerror", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Stop at the first syntax error", level = 0)
	FAIL_FAST("-failfast", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Pre-check syntax of all inputs and stop faster on error", level = 0)
	FAIL_FAST2("-failfast2", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Set GraphViz processing timeout in seconds", usage="-timeout N",  level = 1)
	TIMEOUT("-timeout", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Use N threads for processing", usage="-nbthread N|auto",  level = 1)
	NB_THREAD("-nbthread", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	// Metadata & assets:
	@CliFlagDoc(value = "Extract PlantUML source from PNG/SVG with metadata", level = 0, newGroup = "Metadata & assets")
	RETRIEVE_METADATA("-metadata", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Do not export metadata in generated files", level = 1)
	NO_METADATA("-nometadata", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Skip PNG/SVG files that are already up-to-date (faster rebuilds)", level = 0)
	CHECK_METADATA("-checkmetadata", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Encode a sprite from an image", usage="-sprite 4|8|16[z] file",  level = 0)
	ENCODE_SPRITE("-sprite", aliases("-encodesprite"), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Obfuscate diagram texts for secure sharing", level = 0)
	CYPHER("-cypher", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Compute encoded URL from a PlantUML source file", level = 1)
	COMPUTE_URL("-computeurl", aliases("-encodeurl"), Arity.UNARY_BOOLEAN), //

	@CliFlagDoc(value = "Decode an encoded PlantUML URL", usage="-decodeurl string",  level = 1)
	DECODE_URL("-decodeurl", Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Print the list of PlantUML keywords", level = 1)
	LANGUAGE("-language", Arity.UNARY_IMMEDIATE_ACTION, OptionPrint::printLanguage),

	@CliFlagDoc(value = "Specify Graphviz 'dot' executable path", usage="-graphvizdot path",  level = 1)
	GRAPHVIZ_DOT("-graphvizdot", aliases("-graphviz_dot"), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Start an FTP server (rarely used)", level = 1)
	FTP("-ftp", Arity.UNARY_OPTIONAL_COLON),

	
	// Output control
	@CliFlagDoc(value = "Generate images in the specified directory", usage="-output dir",  level = 1, newGroup = "Output control")
	OUTPUT_DIR("-output", aliases("-odir", "output_dir"), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Allow overwriting read-only files", level = 1)
	OVERWRITE("-overwrite", Arity.UNARY_BOOLEAN, () -> GlobalConfig.getInstance().put(GlobalConfigKey.OVERWRITE, true)),

	@CliFlagDoc(value = "Exclude files matching the given pattern", usage="-exclude pattern",  level = 1)
	EXCLUDE("-exclude", aliases("-x"), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	
	// Other

	CLIPBOARD("-clipboard", Arity.UNARY_IMMEDIATE_ACTION, ClipboardLoop::runOnce), //

	CLIPBOARDLOOP("-clipboardloop", Arity.UNARY_IMMEDIATE_ACTION, ClipboardLoop::runLoop), //

	@CliFlagDoc("Generate intermediate Svek files")
	DEBUG_SVEK("-debugsvek", aliases("-debug_svek"), Arity.UNARY_BOOLEAN),

	@CliFlagDoc("Pretend input files are located in given directory")
	FILE_DIR("-filedir", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc("Override %filename% variable")
	FILENAME("-filename", Arity.BINARY_NEXT_ARGUMENT_VALUE),

	HEADLESS("-headless", Arity.UNARY_BOOLEAN, () -> System.setProperty("java.awt.headless", "true")),

	@CliFlagDoc("List fonts available on your system")
	PRINT_FONTS("-printfonts", Arity.UNARY_IMMEDIATE_ACTION, Run::printFonts),

	@CliFlagDoc("Print standard library information")
	STD_LIB("-stdlib", Arity.UNARY_IMMEDIATE_ACTION, Stdlib::printStdLib),

	STDRPT("-stdrpt", Arity.UNARY_OPTIONAL_COLON),

	@CliFlagDoc("Report syntax errors from stdin without generating images")
	SYNTAX("-syntax", Arity.UNARY_BOOLEAN),

	LICENSE("-license", aliases("-licence"), Arity.UNARY_IMMEDIATE_ACTION, OptionPrint::printLicense),

	WORD("-word", Arity.UNARY_BOOLEAN, () -> GlobalConfig.getInstance().put(GlobalConfigKey.WORD, true)),

	USE_SEPARATOR_MINUS("-useseparatorminus", Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.FILE_SEPARATOR, "-")),

	// Output format (choose one)

	@CliFlagDoc(value = "Generate images in EPS format", level = 0, newGroup = "Output format (choose one)")
	T_EPS("-teps", aliases("-eps"), Arity.UNARY_BOOLEAN, FileFormat.EPS),
	T_EPS_TEXT("-teps:text", aliases("-eps:text"), Arity.UNARY_BOOLEAN, FileFormat.EPS_TEXT),

	@CliFlagDoc(value = "Generate HTML file for class diagram", level = 1)
	T_HTML("-thtml", aliases("-html"), Arity.UNARY_BOOLEAN, FileFormat.HTML),

	@CliFlagDoc(value = "Generate LaTeX/Tikz without preamble", level = 1)
	T_LATEX_NOPREAMBLE("-tlatex:nopreamble", aliases("-latex:nopreamble"), Arity.UNARY_BOOLEAN,
			FileFormat.LATEX_NO_PREAMBLE),

	@CliFlagDoc(value = "Generate LaTeX/Tikz output", level = 0)
	T_LATEX("-tlatex", aliases("-latex"), Arity.UNARY_BOOLEAN, FileFormat.LATEX),

	@CliFlagDoc(value = "Generate PDF images", level = 1)
	T_PDF("-tpdf", aliases("-pdf"), Arity.UNARY_BOOLEAN, FileFormat.PDF),

	@CliFlagDoc(value = "Generate PNG images (default)", level = 0)
	T_PNG("-tpng", aliases("-png"), Arity.UNARY_BOOLEAN, FileFormat.PNG),

	T_BASE64("-tbase64", aliases("-base64"), Arity.UNARY_BOOLEAN, FileFormat.BASE64),
	T_BRAILLE("-tbraille", aliases("-braille"), Arity.UNARY_BOOLEAN, FileFormat.BRAILLE_PNG),

	@CliFlagDoc(value = "Generate SCXML file for state diagram", level = 1)
	T_SCXML("-tscxml", Arity.UNARY_BOOLEAN, FileFormat.SCXML),

	@CliFlagDoc(value = "Generate SVG images", level = 0)
	T_SVG("-tsvg", aliases("-svg"), Arity.UNARY_BOOLEAN, FileFormat.SVG),

	@CliFlagDoc(value = "Generate ASCII art diagrams", level = 0)
	T_TXT("-ttxt", aliases("-txt"), Arity.UNARY_BOOLEAN, FileFormat.ATXT),

	@CliFlagDoc(value = "Generate ASCII art diagrams using Unicode", level = 0)
	T_UTXT("-tutxt", aliases("-utxt"), Arity.UNARY_BOOLEAN, FileFormat.UTXT),

	@CliFlagDoc(value = "Generate VDX images", level = 1)
	T_VDX("-tvdx", aliases("-vdx"), Arity.UNARY_BOOLEAN, FileFormat.VDX),

	@CliFlagDoc(value = "Generate XMI files for class diagrams", level = 1)
	T_XMI("-txmi", aliases("-xmi"), Arity.UNARY_BOOLEAN, FileFormat.XMI_STANDARD),
	T_XMI_ARGO("-txmi:argo", aliases("-xmi:argo"), Arity.UNARY_BOOLEAN, FileFormat.XMI_ARGO),
	T_XMI_CUSTOM("-txmi:custom", aliases("-xmi:custom"), Arity.UNARY_BOOLEAN, FileFormat.XMI_CUSTOM),
	T_XMI_SCRIPT("-txmi:script", aliases("-xmi:script"), Arity.UNARY_BOOLEAN, FileFormat.XMI_SCRIPT),
	T_XMI_STAR("-txmi:star", aliases("-xmi:star"), Arity.UNARY_BOOLEAN, FileFormat.XMI_STAR),

	@CliFlagDoc(value = "Output preprocessor text of diagrams", level = 1)
	PREPROCESS("-preproc", Arity.UNARY_BOOLEAN, FileFormat.PREPROC),

	// ************************ stats

	@CliFlagDoc(value = "Disable statistics computation (default)", level = 1, newGroup = "Statistics")
	DISABLE_STATS("-disablestats", Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.ENABLE_STATS, true)),

	@CliFlagDoc(value = "Enable statistics computation", level = 1)
	ENABLE_STATS("-enablestats", Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.ENABLE_STATS, true)),

	DUMPHTMLSTATS("-dumphtmlstats", Arity.UNARY_IMMEDIATE_ACTION, StatsUtils::outHtml),

	DUMPSTATS("-dumpstats", Arity.UNARY_IMMEDIATE_ACTION, StatsUtils::dumpStats),

	@CliFlagDoc(value = "Output general statistics in HTML format", level = 1)
	HTML_STATS("-htmlstats", Arity.UNARY_BOOLEAN, () -> StatsUtils.setHtmlStats(true)),

	@CliFlagDoc(value = "Generate statistics on the fly", level = 1)
	REALTIME_STATS("-realtimestats", Arity.UNARY_BOOLEAN, () -> StatsUtils.setRealTimeStats(true)),

	@CliFlagDoc(value = "Output general statistics in XML format", level = 1)
	XML_STATS("-xmlstats", Arity.UNARY_BOOLEAN, () -> StatsUtils.setXmlStats(true)),
	
	@CliFlagDoc(value = "Continuously print usage statistics", level = 1)
	LOOP_STATS("-loopstats", Arity.UNARY_IMMEDIATE_ACTION, StatsUtils::loopStats);



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

	public String getNewgroup() {
		try {
			final CliFlagDoc annotation = CliFlag.class.getField(this.name()).getAnnotation(CliFlagDoc.class);
			if (annotation != null && !annotation.value().isEmpty())
				return annotation.newGroup();

		} catch (NoSuchFieldException e) {
			// Should never happen for enum constants
		}
		return "";
	}

	public String getUsage() {
		try {
			final CliFlagDoc annotation = CliFlag.class.getField(this.name()).getAnnotation(CliFlagDoc.class);
			if (annotation != null && !annotation.usage().isEmpty())
				return annotation.usage();

		} catch (NoSuchFieldException e) {
			// Should never happen for enum constants
		}
		return getFlag();
	}

	public int getFlagLevel() {
		try {
			final CliFlagDoc annotation = CliFlag.class.getField(this.name()).getAnnotation(CliFlagDoc.class);
			if (annotation != null)
				return annotation.level();

		} catch (NoSuchFieldException e) {
			// Should never happen for enum constants
		}
		return -1;
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
