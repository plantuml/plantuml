/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.api.ApiWarning;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.stats.StatsUtils;

public class Option {

	private final List<String> excludes = new ArrayList<String>();
	private final List<String> config = new ArrayList<String>();
	private final Map<String, String> defines = new LinkedHashMap<String, String>();
	private String charset;
	private boolean computeurl = false;
	private boolean decodeurl = false;
	private boolean pipe = false;
	private String pipeDelimitor;
	private boolean pipeMap = false;
	private boolean pipeNoStdErr = false;
	private boolean syntax = false;
	private boolean checkOnly = false;
	private OptionPreprocOutputMode preprocessorOutput = null;
	private boolean failfast = false;
	private boolean failfast2 = false;
	private boolean pattern = false;
	private boolean duration = false;
	private boolean debugsvek = false;
	private boolean splash = false;
	private boolean textProgressBar = false;
	private int nbThreads = 0;
	private int ftpPort = -1;
	private boolean hideMetadata = false;
	private boolean checkMetadata = false;
	private int stdrpt = 0;
	private int imageIndex = 0;

	private File outputDir = null;
	private File outputFile = null;
	private String filename;

	private final List<String> result = new ArrayList<String>();

	public Option() {
	}

	private FileFormatOption fileFormatOption = new FileFormatOption(FileFormat.PNG);

	@Deprecated
	@ApiWarning(willBeRemoved = "in next major release")
	final public void setFileFormat(FileFormat fileFormat) {
		setFileFormatOption(new FileFormatOption(fileFormat));
	}

	final public void setFileFormatOption(FileFormatOption newFormat) {
		this.fileFormatOption = newFormat;
	}

	public Option(String... arg) throws InterruptedException, IOException {
		if (arg.length == 0) {
			OptionFlags.getInstance().setGui(true);
		}
		initInclude(GraphvizUtils.getenvDefaultConfigFilename());
		for (int i = 0; i < arg.length; i++) {
			String s = arg[i];
			if (s.equalsIgnoreCase("-headless")) {
				// Useless because done in Run.java
				if (i != 0) {
					Log.error("Warning: -headless flag must be the first one in the command line");
				}
				System.setProperty("java.awt.headless", "true");
			} else if (s.equalsIgnoreCase("-tsvg") || s.equalsIgnoreCase("-svg")) {
				setFileFormatOption(new FileFormatOption(FileFormat.SVG));
			} else if (s.equalsIgnoreCase("-tsvg:nornd") || s.equalsIgnoreCase("-svg:nornd")) {
				setFileFormatOption(new FileFormatOption(FileFormat.SVG));
			} else if (s.equalsIgnoreCase("-thtml") || s.equalsIgnoreCase("-html")) {
				setFileFormatOption(new FileFormatOption(FileFormat.HTML));
			} else if (s.equalsIgnoreCase("-tscxml") || s.equalsIgnoreCase("-scxml")) {
				setFileFormatOption(new FileFormatOption(FileFormat.SCXML));
			} else if (s.equalsIgnoreCase("-txmi") || s.equalsIgnoreCase("-xmi")) {
				setFileFormatOption(new FileFormatOption(FileFormat.XMI_STANDARD));
			} else if (s.equalsIgnoreCase("-txmi:argo") || s.equalsIgnoreCase("-xmi:argo")) {
				setFileFormatOption(new FileFormatOption(FileFormat.XMI_ARGO));
			} else if (s.equalsIgnoreCase("-txmi:star") || s.equalsIgnoreCase("-xmi:star")) {
				setFileFormatOption(new FileFormatOption(FileFormat.XMI_STAR));
			} else if (s.equalsIgnoreCase("-teps") || s.equalsIgnoreCase("-eps")) {
				setFileFormatOption(new FileFormatOption(FileFormat.EPS));
			} else if (s.equalsIgnoreCase("-teps:text") || s.equalsIgnoreCase("-eps:text")) {
				setFileFormatOption(new FileFormatOption(FileFormat.EPS_TEXT));
			} else if (s.equalsIgnoreCase("-ttxt") || s.equalsIgnoreCase("-txt")) {
				setFileFormatOption(new FileFormatOption(FileFormat.ATXT));
			} else if (s.equalsIgnoreCase("-tutxt") || s.equalsIgnoreCase("-utxt")) {
				setFileFormatOption(new FileFormatOption(FileFormat.UTXT));
			} else if (s.equalsIgnoreCase("-braille") || s.equalsIgnoreCase("-tbraille")) {
				setFileFormatOption(new FileFormatOption(FileFormat.BRAILLE_PNG));
			} else if (s.equalsIgnoreCase("-png") || s.equalsIgnoreCase("-tpng")) {
				setFileFormatOption(new FileFormatOption(FileFormat.PNG));
			} else if (s.equalsIgnoreCase("-vdx") || s.equalsIgnoreCase("-tvdx")) {
				setFileFormatOption(new FileFormatOption(FileFormat.VDX));
			} else if (s.equalsIgnoreCase("-latex") || s.equalsIgnoreCase("-tlatex")) {
				setFileFormatOption(new FileFormatOption(FileFormat.LATEX));
			} else if (s.equalsIgnoreCase("-latex:nopreamble") || s.equalsIgnoreCase("-tlatex:nopreamble")) {
				setFileFormatOption(new FileFormatOption(FileFormat.LATEX_NO_PREAMBLE));
			} else if (s.equalsIgnoreCase("-base64") || s.equalsIgnoreCase("-tbase64")) {
				setFileFormatOption(new FileFormatOption(FileFormat.BASE64));
			} else if (s.equalsIgnoreCase("-pdf") || s.equalsIgnoreCase("-tpdf")) {
				setFileFormatOption(new FileFormatOption(FileFormat.PDF));
			} else if (s.equalsIgnoreCase("-overwrite")) {
				OptionFlags.getInstance().setOverwrite(true);
			} else if (s.equalsIgnoreCase("-output") || s.equalsIgnoreCase("-o")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				outputDir = new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
			} else if (s.equalsIgnoreCase("-ofile")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				outputFile = new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
			} else if (s.equalsIgnoreCase("-graphvizdot") || s.equalsIgnoreCase("-graphviz_dot")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				GraphvizUtils.setDotExecutable(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
			} else if (s.equalsIgnoreCase("-charset")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				charset = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]);
			} else if (s.equalsIgnoreCase("-filename")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				filename = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]);
			} else if (s.startsWith("-o") && s.length() > 3) {
				s = s.substring(2);
				outputDir = new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s));
			} else if (s.equalsIgnoreCase("-recurse") || s.equalsIgnoreCase("-r")) {
				// recurse = true;
			} else if (s.equalsIgnoreCase("-exclude") || s.equalsIgnoreCase("-x")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				excludes.add(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
			} else if (s.equalsIgnoreCase("-nbthread") || s.equalsIgnoreCase("-nbthreads")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				final String nb = arg[i];
				if ("auto".equalsIgnoreCase(nb)) {
					this.nbThreads = defaultNbThreads();
				} else if (nb.matches("\\d+")) {
					this.nbThreads = Integer.parseInt(nb);
				}
			} else if (s.equalsIgnoreCase("-timeout")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				final String timeSeconds = arg[i];
				if (timeSeconds.matches("\\d+")) {
					OptionFlags.getInstance().setTimeoutMs(Integer.parseInt(timeSeconds) * 1000L);
				}
			} else if (s.equalsIgnoreCase("-failfast")) {
				this.failfast = true;
			} else if (s.equalsIgnoreCase("-failfast2")) {
				this.failfast2 = true;
			} else if (s.equalsIgnoreCase("-checkonly")) {
				this.checkOnly = true;
			} else if (s.equalsIgnoreCase("-config")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				initConfig(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
			} else if (s.startsWith("-I")) {
				initInclude(s.substring(2));
			} else if (s.equalsIgnoreCase("-computeurl") || s.equalsIgnoreCase("-encodeurl")) {
				this.computeurl = true;
			} else if (s.startsWith("-x")) {
				s = s.substring(2);
				excludes.add(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s));
			} else if (s.equalsIgnoreCase("-verbose") || s.equalsIgnoreCase("-v")) {
				OptionFlags.getInstance().setVerbose(true);
			} else if (s.equalsIgnoreCase("-pipe") || s.equalsIgnoreCase("-p")) {
				pipe = true;
			} else if (s.equalsIgnoreCase("-pipedelimitor")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				pipeDelimitor = arg[i];
			} else if (s.equalsIgnoreCase("-pipemap")) {
				pipeMap = true;
			} else if (s.equalsIgnoreCase("-pipenostderr")) {
				pipeNoStdErr = true;
			} else if (s.equalsIgnoreCase("-pattern")) {
				pattern = true;
			} else if (s.equalsIgnoreCase("-syntax")) {
				syntax = true;
				OptionFlags.getInstance().setQuiet(true);
			} else if (s.equalsIgnoreCase("-duration")) {
				duration = true;
			} else if (s.equalsIgnoreCase("-debugsvek") || s.equalsIgnoreCase("-debug_svek")) {
				debugsvek = true;
			} else if (s.equalsIgnoreCase("-keepfiles") || s.equalsIgnoreCase("-keepfile")) {
				System.err.println("-keepfiles option has been removed. Please consider -debugsvek instead");
			} else if (s.equalsIgnoreCase("-metadata")) {
				OptionFlags.getInstance().setExtractFromMetadata(true);
			} else if (s.equalsIgnoreCase("-logdata")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				OptionFlags.getInstance().setLogData(
						new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i])));
			} else if (s.equalsIgnoreCase("-word")) {
				OptionFlags.getInstance().setWord(true);
				OptionFlags.getInstance().setQuiet(true);
				this.charset = "UTF-8";
			} else if (s.equalsIgnoreCase("-quiet")) {
				OptionFlags.getInstance().setQuiet(true);
			} else if (s.equalsIgnoreCase("-decodeurl")) {
				this.decodeurl = true;
			} else if (s.equalsIgnoreCase("-version")) {
				OptionPrint.printVersion();
			} else if (s.matches("(?i)^-li[sc][ea]n[sc]e\\s*$")) {
				OptionPrint.printLicense();
			} else if (s.equalsIgnoreCase("-checkversion")) {
				OptionPrint.checkVersion();
			} else if (s.startsWith("-DPLANTUML_LIMIT_SIZE=")) {
				final String v = s.substring("-DPLANTUML_LIMIT_SIZE=".length());
				if (v.matches("\\d+")) {
					System.setProperty("PLANTUML_LIMIT_SIZE", v);
				}
			} else if (s.startsWith("-D")) {
				manageDefine(s.substring(2));
			} else if (s.startsWith("-S")) {
				manageSkinParam(s.substring(2));
			} else if (s.equalsIgnoreCase("-testdot")) {
				OptionPrint.printTestDot();
			} else if (s.equalsIgnoreCase("-about") || s.equalsIgnoreCase("-author") || s.equalsIgnoreCase("-authors")) {
				OptionPrint.printAbout();
			} else if (s.equalsIgnoreCase("-help") || s.equalsIgnoreCase("-h") || s.equalsIgnoreCase("-?")) {
				OptionPrint.printHelp();
			} else if (s.equalsIgnoreCase("-language")) {
				OptionPrint.printLanguage();
			} else if (s.equalsIgnoreCase("-gui")) {
				OptionFlags.getInstance().setGui(true);
			} else if (s.equalsIgnoreCase("-encodesprite")) {
				OptionFlags.getInstance().setEncodesprite(true);
				// } else if (s.equalsIgnoreCase("-nosuggestengine")) {
				// OptionFlags.getInstance().setUseSuggestEngine(false);
			} else if (s.equalsIgnoreCase("-printfonts")) {
				OptionFlags.getInstance().setPrintFonts(true);
			} else if (s.equalsIgnoreCase("-dumphtmlstats")) {
				OptionFlags.getInstance().setDumpHtmlStats(true);
			} else if (s.equalsIgnoreCase("-dumpstats")) {
				OptionFlags.getInstance().setDumpStats(true);
			} else if (s.equalsIgnoreCase("-loopstats")) {
				OptionFlags.getInstance().setLoopStats(true);
			} else if (s.equalsIgnoreCase("-enablestats")) {
				OptionFlags.getInstance().setEnableStats(true);
			} else if (s.equalsIgnoreCase("-disablestats")) {
				OptionFlags.getInstance().setEnableStats(false);
			} else if (s.equalsIgnoreCase("-extractstdlib")) {
				OptionFlags.getInstance().setExtractStdLib(true);
			} else if (s.equalsIgnoreCase("-stdlib")) {
				OptionFlags.getInstance().setStdLib(true);
			} else if (s.equalsIgnoreCase("-clipboard")) {
				OptionFlags.getInstance().setClipboard(true);
			} else if (s.equalsIgnoreCase("-clipboardloop")) {
				OptionFlags.getInstance().setClipboardLoop(true);
			} else if (s.equalsIgnoreCase("-htmlstats")) {
				StatsUtils.setHtmlStats(true);
			} else if (s.equalsIgnoreCase("-xmlstats")) {
				StatsUtils.setXmlStats(true);
			} else if (s.equalsIgnoreCase("-realtimestats")) {
				StatsUtils.setRealTimeStats(true);
			} else if (s.equalsIgnoreCase("-useseparatorminus")) {
				OptionFlags.getInstance().setFileSeparator("-");
			} else if (s.equalsIgnoreCase("-splash")) {
				splash = true;
			} else if (s.equalsIgnoreCase("-progress")) {
				textProgressBar = true;
			} else if (s.equalsIgnoreCase("-nometadata")) {
				hideMetadata = true;
			} else if (s.equalsIgnoreCase("-preproc")) {
				preprocessorOutput = OptionPreprocOutputMode.NORMAL;
			} else if (s.equalsIgnoreCase("-cypher")) {
				preprocessorOutput = OptionPreprocOutputMode.CYPHER;
			} else if (s.equalsIgnoreCase("-checkmetadata")) {
				checkMetadata = true;
			} else if (s.equalsIgnoreCase("-stdrpt:1")) {
				stdrpt = 1;
			} else if (s.equalsIgnoreCase("-stdrpt")) {
				stdrpt = 1;
			} else if (s.equalsIgnoreCase("-pipeimageindex")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				final String nb = arg[i];
				if (nb.matches("\\d+")) {
					this.imageIndex = Integer.parseInt(nb);
				}
			} else if (StringUtils.goLowerCase(s).startsWith("-ftp")) {
				final int x = s.indexOf(':');
				if (x == -1) {
					this.ftpPort = 4242;
				} else {
					this.ftpPort = Integer.parseInt(s.substring(x + 1));
				}
			} else if (s.startsWith("-c")) {
				s = s.substring(2);
				config.add(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s));
			} else {
				result.add(s);
			}
		}
	}

	public Stdrpt getStdrpt() {
		if (stdrpt == 1) {
			return new StdrptV1();
		}
		// Legacy case
		if (isPipe() || isPipeMap() || isSyntax()) {
			return new StdrptPipe0();
		}
		return new StdrptNull();
	}

	public int getFtpPort() {
		return ftpPort;
	}

	private void addInConfig(final FileReader source) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(source);
			String s = null;
			while ((s = br.readLine()) != null) {
				config.add(s);
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	public void initConfig(String filename) throws IOException {
		addInConfig(new FileReader(filename));
	}

	private void initInclude(String filename) throws IOException {
		if (filename == null) {
			return;
		}
		if (filename.contains("*")) {
			final FileGroup group = new FileGroup(filename, Collections.<String> emptyList(), null);
			for (File f : group.getFiles()) {
				if (f.exists() && f.canRead()) {
					addInConfig(new FileReader(f));
				}
			}
		} else {
			final File f = new File(filename);
			if (f.exists() && f.canRead()) {
				addInConfig(new FileReader(f));
			}
		}
	}

	private void manageDefine(String s) {
		final Pattern2 p = MyPattern.cmpile("^(\\w+)(?:=(.*))?$");
		final Matcher2 m = p.matcher(s);
		if (m.find()) {
			define(m.group(1), m.group(2));
		}
	}

	private void manageSkinParam(String s) {
		final Pattern2 p = MyPattern.cmpile("^(\\w+)(?:=(.*))?$");
		final Matcher2 m = p.matcher(s);
		if (m.find()) {
			skinParam(m.group(1), m.group(2));
		}
	}

	private void skinParam(String var, String value) {
		if (var != null && value != null) {
			config.add("skinparamlocked " + var + " " + value);
		}

	}

	public final File getOutputDir() {
		return outputDir;
	}

	public final static String getPattern() {
		return "(?i)^.*\\.(txt|tex|java|htm|html|c|h|cpp|apt|pu|puml|hpp|hh)$";
	}

	public void setOutputDir(File f) {
		outputDir = f;
	}

	public final List<String> getExcludes() {
		return Collections.unmodifiableList(excludes);
	}

	public Defines getDefaultDefines(File f) {
		final Defines result = Defines.createWithFileName(f);
		for (Map.Entry<String, String> ent : defines.entrySet()) {
			String value = ent.getValue();
			if (value == null) {
				value = "";
			}
			result.define(ent.getKey(), Arrays.asList(value), false, null);
		}
		return result;
	}

	public Defines getDefaultDefines() {
		final Defines result = Defines.createEmpty();
		result.overrideFilename(filename);
		for (Map.Entry<String, String> ent : defines.entrySet()) {
			result.define(ent.getKey(), Arrays.asList(ent.getValue()), false, null);
		}
		return result;
	}

	public void define(String name, String value) {
		defines.put(name, value);
	}

	public List<String> getConfig() {
		return Collections.unmodifiableList(config);
	}

	public final List<String> getResult() {
		return Collections.unmodifiableList(result);
	}

	public final String getCharset() {
		return charset;
	}

	public void setCharset(String s) {
		this.charset = s;

	}

	public final boolean isComputeurl() {
		return computeurl;
	}

	public final boolean isDecodeurl() {
		return decodeurl;
	}

	public final boolean isPipe() {
		return pipe;
	}

	public final boolean isPipeMap() {
		return pipeMap;
	}

	public final boolean isSyntax() {
		return syntax;
	}

	public final boolean isPattern() {
		return pattern;
	}

	public FileFormatOption getFileFormatOption() {
		if (debugsvek) {
			fileFormatOption.setDebugSvek(true);
		}
		if (hideMetadata) {
			fileFormatOption.hideMetadata();
		}
		return fileFormatOption;
	}

	public final boolean isDuration() {
		return duration;
	}

	public final int getNbThreads() {
		return nbThreads;
	}

	public final void setNbThreads(int nb) {
		this.nbThreads = nb;
	}

	public static int defaultNbThreads() {
		return Runtime.getRuntime().availableProcessors();
	}

	public final boolean isCheckOnly() {
		return checkOnly;
	}

	public final void setCheckOnly(boolean checkOnly) {
		this.checkOnly = checkOnly;
	}

	public final boolean isFailfastOrFailfast2() {
		return failfast || failfast2;
	}

	public final boolean isFailfast2() {
		return failfast2;
	}

	public final void setFailfast(boolean failfast) {
		this.failfast = failfast;
	}

	public final void setFailfast2(boolean failfast2) {
		this.failfast2 = failfast2;
	}

	public final File getOutputFile() {
		return outputFile;
	}

	public final void setDebugSvek(boolean debugsvek) {
		this.debugsvek = debugsvek;
	}

	boolean isDebugSvek() {
		return debugsvek;
	}

	public final boolean isSplash() {
		return splash;
	}

	public final void setSplash(boolean splash) {
		this.splash = splash;
	}

	public final boolean isTextProgressBar() {
		return textProgressBar;
	}

	public String getPipeDelimitor() {
		return pipeDelimitor;
	}

	public final boolean isPipeNoStdErr() {
		return pipeNoStdErr;
	}

	public final int getImageIndex() {
		return imageIndex;
	}

	public final void setFilename(String filename) {
		this.filename = filename;
	}

	public final boolean isCheckMetadata() {
		return checkMetadata;
	}

	public final OptionPreprocOutputMode getPreprocessorOutputMode() {
		return preprocessorOutput;
	}

	// public final void setPreprocessorOutput(boolean preprocessorOutput) {
	// this.preprocessorOutput = preprocessorOutput;
	// }

}
