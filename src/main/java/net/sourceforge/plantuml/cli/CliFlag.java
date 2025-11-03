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

	@CliFlagDoc(value = "Show help and usage information", level = 0)
	HELP("--help", aliases("-h", "-?", DEPRECATED("-help")), Arity.UNARY_IMMEDIATE_ACTION, HelpPrint::printHelp),

	@CliFlagDoc(value = "Show extended help (advanced options)", level = 0)
	HELP_MORE("--help-more", aliases(DEPRECATED("-h:more"), DEPRECATED("-help:more")), Arity.UNARY_IMMEDIATE_ACTION,
			HelpPrint::printHelpMore),

	@CliFlagDoc(value = "Show PlantUML and Java version", level = 0)
	VERSION("--version", aliases(DEPRECATED("-version")), Arity.UNARY_IMMEDIATE_ACTION, OptionPrint::printVersion),

	@CliFlagDoc(value = "Show information about PlantUML authors", level = 0)
	AUTHOR("--author", aliases("--about", DEPRECATED("-authors"), DEPRECATED("-about")), Arity.UNARY_IMMEDIATE_ACTION,
			OptionPrint::printAbout), //

	@CliFlagDoc(value = "Launch the graphical user interface", level = 0)
	GUI("--gui", aliases(DEPRECATED("-gui")), Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.GUI, true)),

	@CliFlagDoc(value = "Render diagrams in dark mode", level = 0)
	DARK_MODE("--dark-mode", aliases(DEPRECATED("-darkmode")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Enable verbose logging", level = 0)
	VERBOSE("--verbose", aliases(DEPRECATED("-v"), DEPRECATED("-verbose")), Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.VERBOSE, true)),

	@CliFlagDoc(value = "Print total processing time", level = 0)
	DURATION("--duration", aliases(DEPRECATED("-duration")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Show a textual progress bar", level = 0)
	PROGRESS("--progress-bar", aliases(DEPRECATED("-progress")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Show splash screen with progress bar", level = 0)
	SPLASH("--splash-screen", aliases(DEPRECATED("-splash")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Check Graphviz installation", level = 0)
	TEST_DOT("--check-graphviz", aliases(DEPRECATED("-testdot")), Arity.UNARY_IMMEDIATE_ACTION,
			OptionPrint::printCheckGraphviz),

	@CliFlagDoc(value = "Start internal HTTP server for rendering (default port : 8080)", usage = "--http-server[:<port>]", level = 0)
	PICOWEB("--http-server", aliases(DEPRECATED("-picoweb")), Arity.UNARY_OPTIONAL_COLON),

	// Input & preprocessing:

	@CliFlagDoc(value = "Exclude input files matching the given pattern", usage = "--exclude <pattern>", level = 1, newGroup = "Input & preprocessing")
	EXCLUDE("--exclude", aliases("-x", DEPRECATED("-exclude")), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Read source from stdin, write result to stdout", level = 0)
	PIPE("--pipe", aliases("-p", DEPRECATED("-pipe")), Arity.UNARY_BOOLEAN),

	PIPEMAP("-pipemap", Arity.UNARY_BOOLEAN), //
	PIPEDELIMITOR("-pipedelimitor", Arity.BINARY_NEXT_ARGUMENT_VALUE), //
	PIPENOSTDERR("-pipenostderr", Arity.UNARY_BOOLEAN), //

	@CliFlagDoc("Generate the Nth image with pipe option")
	@CliDefaultValue("0")
	PIPE_IMAGE_INDEX("--pipe-image-index", aliases(DEPRECATED("-pipeimageindex")), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Define a preprocessing variable (equivalent to '!define <var> <value>')", usage = "-d, --define <VAR>=<value>", level = 0)
	DEFINE("-D", Arity.UNARY_INLINE_KEY_OR_KEY_VALUE), //
	DEFINE_LONG("--define", Arity.BINARY_NEXT_ARGUMENT_VALUE), //

	@CliFlagDoc(value = "Include external file (as with '!include <file>')", usage = "-I, --include <file>", level = 1)
	INCLUDE("-I", Arity.UNARY_INLINE_KEY_OR_KEY_VALUE), //
	INCLUDE_LONG("--include", Arity.BINARY_NEXT_ARGUMENT_VALUE), //

	@CliFlagDoc(value = "Set pragma (equivalent to '!pragma <key> <value>')", usage = "-P, --pragma <key>=<value>", level = 1)
	PRAGMA("-P", Arity.UNARY_INLINE_KEY_OR_KEY_VALUE), //
	PRAGMA_LONG("--pragma", Arity.BINARY_NEXT_ARGUMENT_VALUE), //

	@CliFlagDoc(value = "Set skin parameter (equivalent to 'skinparam <key> <value>')", usage = "--skinparam <key>=<value>", level = 1)
	SKINPARAM("-S", Arity.UNARY_INLINE_KEY_OR_KEY_VALUE), //
	SKINPARAM_LONG("--skinparam", Arity.BINARY_NEXT_ARGUMENT_VALUE), //

	@CliFlagDoc(value = "Apply a theme", usage = "--theme <name>", level = 1)
	THEME("--theme", aliases(DEPRECATED("-theme")), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Specify configuration file", usage = "--config <file>", level = 1)
	CONFIG("--config", aliases(DEPRECATED("-config")), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Use a specific input charset", usage = "--charset <name>", level = 1)
	@CliDefaultValue("UTF-8")
	CHARSET("--charset", aliases(DEPRECATED("-charset")), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	// Execution control:

	@CliFlagDoc(value = "Check diagram syntax without generating images", level = 0, newGroup = "Execution control")
	CHECK_ONLY("--check-syntax", aliases("--syntax-check", DEPRECATED("-checkonly")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Stop at the first syntax error", level = 0)
	FAIL_FAST("--stop-on-error", aliases(DEPRECATED("-failfast")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Pre-check syntax of all inputs and stop faster on error", level = 0)
	FAIL_FAST2("--check-before-run", aliases(DEPRECATED("-failfast2")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Do not generate error images for diagrams with syntax errors", level = 0)
	NO_ERROR_IMAGE("--no-error-image", aliases(DEPRECATED("-noerror")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Set Graphviz processing timeout (in seconds)", usage = "--graphviz-timeout <seconds>", level = 1)
	TIMEOUT("--graphviz-timeout", aliases(DEPRECATED("-timeout")), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Use <n> threads for processing  (auto = available processors)", usage = "--threads <n|auto>", level = 1)
	NB_THREAD("--threads", aliases(DEPRECATED("-nbthread")), Arity.BINARY_NEXT_ARGUMENT_VALUE),

	// Metadata & assets:
	@CliFlagDoc(value = "Extract embedded PlantUML source from PNG or SVG metadata", level = 0, newGroup = "Metadata & assets")
	RETRIEVE_METADATA("--extract-source", aliases(DEPRECATED("-metadata")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Do not include metadata in generated files", level = 1)
	NO_METADATA("--disable-metadata", aliases(DEPRECATED("-nometadata")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Skip PNG/SVG files that are already up-to-date (using metadata)", level = 0)
	CHECK_METADATA("--skip-fresh", aliases(DEPRECATED("-checkmetadata")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Encode a sprite definition from an image file", usage = "--sprite <4|8|16[z]> <file>", level = 0)
	ENCODE_SPRITE("--encode-sprite", aliases(DEPRECATED("-sprite"), DEPRECATED("-encodesprite")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Generate an encoded PlantUML URL from a source file", level = 1)
	COMPUTE_URL("--encode-url", aliases("--compute-url", DEPRECATED("-computeurl"), DEPRECATED("-encodeurl")),
			Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Decode a PlantUML encoded URL back to its source", usage = "--decode-url <string>", level = 1)
	DECODE_URL("--decode-url", aliases(DEPRECATED("-decodeurl")), Arity.UNARY_BOOLEAN),

	@CliFlagDoc(value = "Print the list of PlantUML language keywords", level = 1)
	LANGUAGE("--list-keywords", aliases(DEPRECATED("-language")), Arity.UNARY_IMMEDIATE_ACTION,
			OptionPrint::printListKeywords),

	@CliFlagDoc(value = "Specify the path to the Graphviz 'dot' executable", usage = "--dot-path <path-to-dot-exe>", level = 1)
	GRAPHVIZ_DOT("--dot-path", aliases(DEPRECATED("-graphvizdot"), DEPRECATED("-graphviz_dot")),
			Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Start a local FTP server for diagram rendering (rarely used)", level = 1)
	FTP("--ftp-server", aliases(DEPRECATED("-ftp")), Arity.UNARY_OPTIONAL_COLON),

	// Output control
	@CliFlagDoc(value = "Generate output files in the specified directory", usage = "--output-dir <dir>", level = 1, newGroup = "Output control")
	OUTPUT_DIR("--output-dir", aliases("-o", DEPRECATED("output_dir"), DEPRECATED("-output"), DEPRECATED("-odir")),
			Arity.BINARY_NEXT_ARGUMENT_VALUE),

	@CliFlagDoc(value = "Allow overwriting of read-only output files", level = 1)
	OVERWRITE("--overwrite", aliases("--force-overwrite", DEPRECATED("-overwrite")), Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.OVERWRITE, true)),

	// Other

	CLIPBOARD("--clipboard", Arity.UNARY_IMMEDIATE_ACTION, ClipboardLoop::runOnce), //

	CLIPBOARDLOOP("--clipboardloop", Arity.UNARY_IMMEDIATE_ACTION, ClipboardLoop::runLoop), //

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

	@CliFlagDoc(value = "Set the output format for generated diagrams\n(e.g. png, svg, pdf, eps, latex, txt, utxt, obfuscate, preproc...)", level = 0, usage = "-f, --format <name>", newGroup = "Output format (choose one)")
	FORMAT("--format", aliases("-f"), Arity.BINARY_NEXT_ARGUMENT_VALUE),
	
	@CliFlagDoc(value = "Generate images in EPS format", level = 0, newGroup = "Available formats")
	T_EPS("--eps", aliases(DEPRECATED("-teps"), DEPRECATED("-eps")), Arity.UNARY_BOOLEAN, FileFormat.EPS),
	T_EPS_TEXT("--teps:text", aliases(DEPRECATED("-teps:text"), DEPRECATED("-eps:text")), Arity.UNARY_BOOLEAN,
			FileFormat.EPS_TEXT),

	@CliFlagDoc(value = "Generate HTML files for class diagrams", level = 1)
	T_HTML("--html", aliases(DEPRECATED("-thtml"), DEPRECATED("-html")), Arity.UNARY_BOOLEAN, FileFormat.HTML),

	@CliFlagDoc(value = "Generate LaTeX/TikZ output", level = 0)
	T_LATEX("--latex", aliases(DEPRECATED("-tlatex"), DEPRECATED("-latex")), Arity.UNARY_BOOLEAN, FileFormat.LATEX),

	@CliFlagDoc(value = "Generate LaTeX/TikZ output without preamble", level = 1)
	T_LATEX_NOPREAMBLE("--latex-nopreamble", aliases(DEPRECATED("-tlatex:nopreamble"), DEPRECATED("-latex:nopreamble")),
			Arity.UNARY_BOOLEAN, FileFormat.LATEX_NO_PREAMBLE),
	
	@CliFlagDoc(value = "Replace text in diagrams with obfuscated strings to share diagrams safely", level = 0)
	OBFUSCATE("--obfuscate", aliases(DEPRECATED("-cypher")), Arity.UNARY_BOOLEAN, FileFormat.OBFUSCATE),

	@CliFlagDoc(value = "Generate PDF images", level = 1)
	T_PDF("--pdf", aliases(DEPRECATED("-tpdf"), DEPRECATED("-pdf")), Arity.UNARY_BOOLEAN, FileFormat.PDF),

	@CliFlagDoc(value = "Generate PNG images (default)", level = 0)
	T_PNG("--png", aliases(DEPRECATED("-tpng"), DEPRECATED("-png")), Arity.UNARY_BOOLEAN, FileFormat.PNG),

	@CliFlagDoc(value = "Generate the preprocessed source after applying !include, !define... (no rendering)", level = 0)
	PREPROCESS("--preproc", aliases(DEPRECATED("-preproc")),  Arity.UNARY_BOOLEAN, FileFormat.PREPROC),

	@CliFlagDoc(value = "Generate SCXML files for state diagrams", level = 1)
	T_SCXML("--scxml", aliases(DEPRECATED("-tscxml")), Arity.UNARY_BOOLEAN, FileFormat.SCXML),

	@CliFlagDoc(value = "Generate SVG images", level = 0)
	T_SVG("--svg", aliases(DEPRECATED("-tsvg"), DEPRECATED("-svg")), Arity.UNARY_BOOLEAN, FileFormat.SVG),

	@CliFlagDoc(value = "Generate ASCII art diagrams", level = 0)
	T_TXT("--txt", aliases(DEPRECATED("-ttxt"), DEPRECATED("-txt")), Arity.UNARY_BOOLEAN, FileFormat.ATXT),

	@CliFlagDoc(value = "Generate ASCII art diagrams using Unicode characters", level = 0)
	T_UTXT("--utxt", aliases(DEPRECATED("-tutxt"), DEPRECATED("-utxt")), Arity.UNARY_BOOLEAN, FileFormat.UTXT),

	@CliFlagDoc(value = "Generate VDX files", level = 1)
	T_VDX("--vdx", aliases(DEPRECATED("-tvdx"), DEPRECATED("-vdx")), Arity.UNARY_BOOLEAN, FileFormat.VDX),

	@CliFlagDoc(value = "Generate XMI files for class diagrams", level = 1)
	T_XMI("--xmi", aliases(DEPRECATED("-txmi"), DEPRECATED("-xmi")), Arity.UNARY_BOOLEAN, FileFormat.XMI_STANDARD),
	T_XMI_ARGO("--txmi:argo", aliases(DEPRECATED("-xmi:argo")), Arity.UNARY_BOOLEAN, FileFormat.XMI_ARGO),
	T_XMI_CUSTOM("--txmi:custom", aliases(DEPRECATED("-xmi:custom")), Arity.UNARY_BOOLEAN, FileFormat.XMI_CUSTOM),
	T_XMI_SCRIPT("--txmi:script", aliases(DEPRECATED("-xmi:script")), Arity.UNARY_BOOLEAN, FileFormat.XMI_SCRIPT),
	T_XMI_STAR("--txmi:star", aliases(DEPRECATED("-xmi:star")), Arity.UNARY_BOOLEAN, FileFormat.XMI_STAR),

	T_BASE64("--tbase64", Arity.UNARY_BOOLEAN, FileFormat.BASE64),
	T_BRAILLE("--tbraille", Arity.UNARY_BOOLEAN, FileFormat.BRAILLE_PNG),


	// ************************ stats

	@CliFlagDoc(value = "Disable statistics collection (default behavior)", level = 1, newGroup = "Statistics")
	DISABLE_STATS("--disable-stats", aliases(DEPRECATED("-disablestats")), Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.ENABLE_STATS, false)),

	@CliFlagDoc(value = "Enable statistics collection", level = 1)
	ENABLE_STATS("--enable-stats", aliases(DEPRECATED("-enablestats")), Arity.UNARY_BOOLEAN,
			() -> GlobalConfig.getInstance().put(GlobalConfigKey.ENABLE_STATS, true)),

	@CliFlagDoc(value = "Export collected statistics to an HTML report and exit", level = 1)
	DUMPHTMLSTATS("--export-stats-html", Arity.UNARY_IMMEDIATE_ACTION, StatsUtils::outHtml),

	@CliFlagDoc(value = "Export collected statistics to a text report and exit", level = 1)
	DUMPSTATS("--export-stats", Arity.UNARY_IMMEDIATE_ACTION, StatsUtils::dumpStats),

	@CliFlagDoc(value = "Output general statistics in HTML format", level = 1)
	HTML_STATS("--html-stats", aliases(DEPRECATED("-htmlstats")), Arity.UNARY_BOOLEAN,
			() -> StatsUtils.setHtmlStats(true)),

	@CliFlagDoc(value = "Output general statistics in XML format", level = 1)
	XML_STATS("--xml-stats", aliases(DEPRECATED("-xmlstats")), Arity.UNARY_BOOLEAN, () -> StatsUtils.setXmlStats(true)),

	@CliFlagDoc(value = "Generate statistics in real time during processing", level = 1)
	REALTIME_STATS("--realtime-stats", aliases(DEPRECATED("-realtimestats")), Arity.UNARY_BOOLEAN,
			() -> StatsUtils.setRealTimeStats(true)),

	@CliFlagDoc(value = "Continuously print usage statistics during execution", level = 1)
	LOOP_STATS("--loop-stats", aliases(DEPRECATED("-loopstats")), Arity.UNARY_IMMEDIATE_ACTION, StatsUtils::loopStats);

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

	private static String DEPRECATED(String option) {
		return option;
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
		if (type == Arity.UNARY_INLINE_KEY_OR_KEY_VALUE || type == Arity.UNARY_OPTIONAL_COLON) {
			for (String alias : aliases)
				if (tmp.startsWith(alias))
					return true;

			return tmp.startsWith(flag);
		}

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
			if (annotation != null && !annotation.usage().isEmpty()) {
				final String usage = annotation.usage();
				if (usage.startsWith("--"))
					return "     " + usage;
				return " " + usage;
			}

		} catch (NoSuchFieldException e) {
			// Should never happen for enum constants
		}
		for (String alias : aliases)
			if (alias.length() == 2)
				return " " + alias + ", " + getFlag();

		return "     " + getFlag();
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
