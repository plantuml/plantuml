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
import java.util.Map.Entry;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.OptionPreprocOutputMode;
import net.sourceforge.plantuml.Stdrpt;
import net.sourceforge.plantuml.StdrptNull;
import net.sourceforge.plantuml.StdrptPipe0;
import net.sourceforge.plantuml.StdrptV1;
import net.sourceforge.plantuml.StdrptV2;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.api.ApiWarning;
import net.sourceforge.plantuml.dot.GraphvizUtils;
import net.sourceforge.plantuml.file.FileGroup;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.security.SFile;

public class CliOptions {
	// ::remove file when __CORE__
	// ::remove file when __HAXE__

	private final List<String> config = new ArrayList<>();

	private int imageIndex = 0;
	private String fileDir;

	private File outputDir = null;

	public final CliParsed flags;

	public CliOptions() {
		this.flags = new CliParsed();
	}

	private FileFormatOption fileFormatOption = new FileFormatOption(FileFormat.PNG);

	/**
	 * @deprecated Use {@link #setFileFormatOption(FileFormatOption)} instead
	 */
	@Deprecated
	@ApiWarning(willBeRemoved = "in next major release")
	final public void setFileFormat(FileFormat fileFormat) {
		setFileFormatOption(new FileFormatOption(fileFormat));
	}

	final public void setFileFormatOption(FileFormatOption newFormat) {
		this.fileFormatOption = newFormat;
	}

	// ::comment when __CORE__
	CliOptions(String... arg) throws InterruptedException, IOException {
		if (arg.length == 0)
			GlobalConfig.getInstance().put(GlobalConfigKey.GUI, true);

		initInclude(GraphvizUtils.getenvDefaultConfigFilename());

		flags = CliParsed.parse(arg);

		final Map<String, String> flagInclude = flags.getMap(CliFlag.INCLUDE);
		System.err.println("flagInclude=" + flagInclude);
		if (flagInclude != null)
			for (String key : flagInclude.keySet())
				initInclude(key);

		final List<Object> flagTheme = flags.getList(CliFlag.THEME);
		System.err.println("flagTheme=" + flagTheme);
		if (flagTheme != null)
			for (Object theme : flagTheme)
				config.add("!theme " + theme);

		final String flagConfig = flags.getString(CliFlag.CONFIG);
		System.err.println("flagConfig=" + flagConfig);

		final Map<String, String> flagPragma = flags.getMap(CliFlag.PRAGMA);
		System.err.println("flagPragma=" + flagPragma);
		if (flagPragma != null)
			for (Entry<String, String> ent : flagPragma.entrySet())
				config.add("!pragma " + ent.getKey() + " " + ent.getValue());

//		for (int i = 0; i < arg.length; i++) {
//			String s = arg[i];
//			if (s.equalsIgnoreCase("-headless")) {
//				// Useless because done in Run.java
//				if (i != 0)
//					Log.error("Warning: -headless flag must be the first one in the command line");
//
//				System.setProperty("java.awt.headless", "true");
//			} else if (s.equalsIgnoreCase("-tsvg") || s.equalsIgnoreCase("-svg")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.SVG));
//
//			} else if (s.equalsIgnoreCase("-tsvg:nornd") || s.equalsIgnoreCase("-svg:nornd")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.SVG));
//
//			} else if (s.equalsIgnoreCase("-thtml") || s.equalsIgnoreCase("-html")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.HTML));
//
//			} else if (s.equalsIgnoreCase("-tscxml") || s.equalsIgnoreCase("-scxml")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.SCXML));
//
//			} else if (s.equalsIgnoreCase("-txmi") || s.equalsIgnoreCase("-xmi")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.XMI_STANDARD));
//
//			} else if (s.equalsIgnoreCase("-txmi:argo") || s.equalsIgnoreCase("-xmi:argo")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.XMI_ARGO));
//
//			} else if (s.equalsIgnoreCase("-txmi:custom") || s.equalsIgnoreCase("-xmi:custom")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.XMI_CUSTOM));
//
//			} else if (s.equalsIgnoreCase("-txmi:script") || s.equalsIgnoreCase("-xmi:script")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.XMI_SCRIPT));
//
//			} else if (s.equalsIgnoreCase("-txmi:star") || s.equalsIgnoreCase("-xmi:star")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.XMI_STAR));
//
//			} else if (s.equalsIgnoreCase("-teps") || s.equalsIgnoreCase("-eps")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.EPS));
//
//			} else if (s.equalsIgnoreCase("-teps:text") || s.equalsIgnoreCase("-eps:text")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.EPS_TEXT));
//
//			} else if (s.equalsIgnoreCase("-ttxt") || s.equalsIgnoreCase("-txt")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.ATXT));
//
//			} else if (s.equalsIgnoreCase("-tutxt") || s.equalsIgnoreCase("-utxt")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.UTXT));
//
//			} else if (s.equalsIgnoreCase("-braille") || s.equalsIgnoreCase("-tbraille")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.BRAILLE_PNG));
//
//			} else if (s.equalsIgnoreCase("-png") || s.equalsIgnoreCase("-tpng")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.PNG));
//
//			} else if (s.equalsIgnoreCase("-vdx") || s.equalsIgnoreCase("-tvdx")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.VDX));
//
//			} else if (s.equalsIgnoreCase("-latex") || s.equalsIgnoreCase("-tlatex")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.LATEX));
//
//			} else if (s.equalsIgnoreCase("-latex:nopreamble") || s.equalsIgnoreCase("-tlatex:nopreamble")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.LATEX_NO_PREAMBLE));
//
//			} else if (s.equalsIgnoreCase("-base64") || s.equalsIgnoreCase("-tbase64")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.BASE64));
//
//			} else if (s.equalsIgnoreCase("-pdf") || s.equalsIgnoreCase("-tpdf")) {
//				setFileFormatOption(new FileFormatOption(FileFormat.PDF));
//
//			} else if (s.equalsIgnoreCase("-darkmode")) {
//				setFileFormatOption(this.fileFormatOption.withColorMapper(ColorMapper.DARK_MODE));
//
//			} else if (s.equalsIgnoreCase("-overwrite")) {
//				GlobalConfig.getInstance().put(GlobalConfigKey.OVERWRITE, true);
//
//			} else if (s.equalsIgnoreCase("-filename")) {
//				i++;
//				if (i == arg.length)
//					continue;
//
//				filename = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]);
//			} else if (s.equalsIgnoreCase("-filedir")) {
//				i++;
//				if (i == arg.length)
//					continue;
//
//				fileDir = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]);
//			} else if (s.startsWith("-o") && s.length() > 3) {
//				s = s.substring(2);
//				outputDir = new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s));
//			} else if (s.equalsIgnoreCase("-timeout")) {
//				i++;
//				if (i == arg.length)
//					continue;
//
//				final String timeSeconds = arg[i];
//				if (timeSeconds.matches("\\d+"))
//					GlobalConfig.getInstance().put(GlobalConfigKey.TIMEOUT_MS, Integer.parseInt(timeSeconds) * 1000L);
//
//			} else if (s.equalsIgnoreCase("-config")) {
//				i++;
//				if (i == arg.length)
//					continue;
//
//				initConfig(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
//			} else if (s.startsWith("-I")) {
//				initInclude(s.substring(2));
//
//			} else if (s.equalsIgnoreCase("-pipe") || s.equalsIgnoreCase("-p")) {
//				pipe = true;
//
//			} else if (s.equalsIgnoreCase("-pipedelimitor")) {
//				i++;
//				if (i == arg.length)
//					continue;
//
//				pipeDelimitor = arg[i];
//			} else if (s.equalsIgnoreCase("-pipemap")) {
//				pipeMap = true;
//
//			} else if (s.equalsIgnoreCase("-pipenostderr")) {
//				pipeNoStdErr = true;
//
//			} else if (s.equalsIgnoreCase("-syntax")) {
//				syntax = true;
//				// GlobalConfig.getInstance().setQuiet(true);
//
//			} else if (s.equalsIgnoreCase("-word")) {
//				GlobalConfig.getInstance().put(GlobalConfigKey.WORD, true);
//				// GlobalConfig.getInstance().setQuiet(true);
//				this.charset = "UTF-8";
//
//			} else if (s.startsWith("-DPLANTUML_LIMIT_SIZE=")) {
//				final String v = s.substring("-DPLANTUML_LIMIT_SIZE=".length());
//				if (v.matches("\\d+"))
//					System.setProperty("PLANTUML_LIMIT_SIZE", v);
//
//			} else if (s.startsWith("-S")) {
//				manageSkinParam(s.substring(2));
//
//			} else if (s.equalsIgnoreCase("-help") || s.equalsIgnoreCase("-h") || s.equalsIgnoreCase("-?")) {
//				OptionPrint.printHelp();
//
//			} else if (s.equalsIgnoreCase("-gui")) {
//				GlobalConfig.getInstance().put(GlobalConfigKey.GUI, true);
//
//			} else if (s.equalsIgnoreCase("-encodesprite")) {
//				GlobalConfig.getInstance().put(GlobalConfigKey.ENCODESPRITE, true);
//
//			} else if (s.equalsIgnoreCase("-dumphtmlstats")) {
//				GlobalConfig.getInstance().put(GlobalConfigKey.DUMP_HTML_STATS, true);
//
//			} else if (s.equalsIgnoreCase("-dumpstats")) {
//				GlobalConfig.getInstance().put(GlobalConfigKey.DUMP_STATS, true);
//
//			} else if (s.equalsIgnoreCase("-loopstats")) {
//				GlobalConfig.getInstance().put(GlobalConfigKey.LOOP_STATS, true);
//
//			} else if (s.equalsIgnoreCase("-enablestats")) {
//				GlobalConfig.getInstance().put(GlobalConfigKey.ENABLE_STATS, true);
//
//			} else if (s.equalsIgnoreCase("-disablestats")) {
//				GlobalConfig.getInstance().put(GlobalConfigKey.ENABLE_STATS, true);
//
//			} else if (s.equalsIgnoreCase("-clipboard")) {
//				GlobalConfig.getInstance().put(GlobalConfigKey.CLIPBOARD, true);
//
//			} else if (s.equalsIgnoreCase("-clipboardloop")) {
//				GlobalConfig.getInstance().put(GlobalConfigKey.CLIPBOARD_LOOP, true);
//
//			} else if (s.equalsIgnoreCase("-htmlstats")) {
//				StatsUtils.setHtmlStats(true);
//
//			} else if (s.equalsIgnoreCase("-xmlstats")) {
//				StatsUtils.setXmlStats(true);
//
//			} else if (s.equalsIgnoreCase("-realtimestats")) {
//				StatsUtils.setRealTimeStats(true);
//
//			} else if (s.equalsIgnoreCase("-useseparatorminus")) {
//				GlobalConfig.getInstance().put(GlobalConfigKey.FILE_SEPARATOR, "-");
//
//			} else if (s.equalsIgnoreCase("-splash")) {
//				splash = true;
//
//			} else if (s.equalsIgnoreCase("-progress")) {
//				textProgressBar = true;
//
//			} else if (s.equalsIgnoreCase("-nometadata")) {
//				hideMetadata = true;
//
//			} else if (s.equalsIgnoreCase("-preproc")) {
//				preprocessorOutput = OptionPreprocOutputMode.NORMAL;
//				setFileFormatOption(new FileFormatOption(FileFormat.PREPROC));
//
//			} else if (s.equalsIgnoreCase("-cypher")) {
//				preprocessorOutput = OptionPreprocOutputMode.CYPHER;
//				setFileFormatOption(new FileFormatOption(FileFormat.PREPROC));
//
//			} else if (s.equalsIgnoreCase("-stdrpt:1")) {
//				stdrpt = 1;
//
//			} else if (s.equalsIgnoreCase("-stdrpt:2")) {
//				stdrpt = 2;
//
//			} else if (s.equalsIgnoreCase("-stdrpt")) {
//				stdrpt = 2;
//
//			} else if (s.equalsIgnoreCase("-pipeimageindex")) {
//				i++;
//				if (i == arg.length)
//					continue;
//
//				final String nb = arg[i];
//				if (nb.matches("\\d+"))
//					this.imageIndex = Integer.parseInt(nb);
//
//			} else if (s.startsWith("-c")) {
//				s = s.substring(2);
//				config.add(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s));
//
//			} else {
//				result.add(s);
//			}
//		}
	}

	public Stdrpt getStdrpt() {

		final String stdrpt = flags.getString(CliFlag.STDRPT);
		if ("1".equals(stdrpt))
			return new StdrptV1();

		if ("2".equals(stdrpt))
			return new StdrptV2();

		// Legacy case
		if (isTrue(CliFlag.PIPE) || isTrue(CliFlag.PIPEMAP) || isTrue(CliFlag.SYNTAX))
			return new StdrptPipe0();

		return new StdrptNull();
	}

	public int getFtpPort() {
		final String ftp = flags.getString(CliFlag.FTP);
		if (ftp == null)
			return 4242;
		return Integer.parseInt(ftp);
	}

	public String getPicowebBindAddress() {
		final String picoweb = flags.getString(CliFlag.PICOWEB);
		if (picoweb == null)
			return null;

		final String[] parts = picoweb.split(":");
		return parts.length > 1 ? parts[1] : null;
	}

	public int getPicowebPort() {
		final String picoweb = flags.getString(CliFlag.PICOWEB);
		if (picoweb == null)
			return 4242;

		final String[] parts = picoweb.split(":");
		return Integer.parseInt(parts[0]);
	}

	public boolean getPicowebEnableStop() {
		final String picoweb = flags.getString(CliFlag.PICOWEB);
		if (picoweb == null)
			return false;
		return picoweb.toLowerCase().contains("stop");
	}
	// ::done

	private void addInConfig(BufferedReader br) throws IOException {
		if (br == null)
			return;

		try {
			String s = null;
			while ((s = br.readLine()) != null)
				config.add(s);

		} finally {
			br.close();
		}
	}

	public void addInConfig(String filename) throws IOException {
		final BufferedReader br = new BufferedReader(new FileReader(filename));
		addInConfig(br);
	}

	private void initInclude(String filename) throws IOException {
		if (filename == null)
			return;

		if (filename.contains("*")) {
			final FileGroup group = new FileGroup(filename, Collections.<String>emptyList());
			for (File f : group.getFiles())
				if (f.exists() && f.canRead())
					addInConfig(new BufferedReader(new FileReader(f)));

		} else {
			final File f = new File(filename);
			if (f.exists() && f.canRead())
				addInConfig(new BufferedReader(new FileReader(f)));

		}
	}

	private void managePragma(String s) {
		final Pattern2 p = Pattern2.cmpile("^(\\w+)(?:=(.*))?$");
		final Matcher2 m = p.matcher(s);
		if (m.find()) {
			final String var = m.group(1);
			final String value = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(m.group(2));
			if (var != null && value != null)
				config.add("!pragma " + var + " " + value);

		}
	}

	private void manageSkinParam(String s) {
		final Pattern2 p = Pattern2.cmpile("^(\\w+)(?:=(.*))?$");
		final Matcher2 m = p.matcher(s);
		if (m.find()) {
			final String var = m.group(1);
			final String value = m.group(2);
			if (var != null && value != null)
				config.add("skinparamlocked " + var + " " + value);

		}
	}

	public final File getOutputDir() {
		if (outputDir != null)
			return outputDir;

		final String tmp = flags.getString(CliFlag.OUTPUT_DIR);
		if (tmp != null)
			return new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(tmp));

		return null;
	}

	public final static String getPattern() {
		return "(?i)^.*\\.(txt|tex|java|htm|html|c|h|cpp|apt|pu|puml|hpp|hh)$";
	}

	public void setOutputDir(File f) {
		outputDir = f;
	}

	public final List<String> getExcludes() {
		if (isTrue(CliFlag.EXCLUDE)) {
			final List<String> result = new ArrayList<>();
			for (Object s : flags.getList(CliFlag.EXCLUDE))
				result.add(s.toString());
			return Collections.unmodifiableList(result);
		}

		return Collections.emptyList();
	}

	// ::comment when __CORE__
	public Defines getDefaultDefines(SFile f) {
		final Defines result = Defines.createWithFileName(f);
		for (Map.Entry<String, String> ent : defines().entrySet()) {
			String value = ent.getValue();
			if (value == null)
				value = "";

			result.define(ent.getKey(), Arrays.asList(value), false, null);
		}
		return result;
	}

	public Defines getDefaultDefines(java.io.File f) {
		final Defines result = Defines.createWithFileName(f);
		for (Map.Entry<String, String> ent : defines().entrySet()) {
			String value = ent.getValue();
			if (value == null)
				value = "";

			result.define(ent.getKey(), Arrays.asList(value), false, null);
		}
		return result;
	}

	public Defines getDefaultDefines() {
		final Defines result = Defines.createEmpty();
		final String filename = flags.getString(CliFlag.FILENAME);
		result.overrideFilename(filename);
		result.overrideDirPath(fileDir);
		for (Map.Entry<String, String> ent : defines().entrySet())
			result.define(ent.getKey(), Arrays.asList(ent.getValue()), false, null);

		return result;
	}
	// ::done

//	public void define(String name, String value) {
//		defines.put(name, value);
//	}

	private Map<String, String> defines() {
		final Map<String, String> result = flags.getMap(CliFlag.DEFINE);
		if (result == null)
			return Collections.emptyMap();
		return Collections.unmodifiableMap(result);
	}

	public List<String> getConfig() {
		return Collections.unmodifiableList(config);
	}

	public final List<String> getRemainingArgs() {
		return flags.getRemainingArgs();
	}

//	public void setCharset(String s) {
//		this.charset = s;
//	}

	// ::comment when __CORE__
//	public final boolean isDecodeurl() {
//		return decodeurl;
//	}
//
//	public final boolean isPipe() {
//		return pipe;
//	}
//
//	public final boolean isPipeMap() {
//		return pipeMap;
//	}
//
//	public final boolean isSyntax() {
//		return syntax;
//	}
//
	public FileFormatOption getFileFormatOption() {
		FileFormat format = (FileFormat) flags.getFromType(FileFormat.class);
		if (format == null)
			format = FileFormat.PNG;

		fileFormatOption = new FileFormatOption(format);
		if (flags.isTrue(CliFlag.DEBUG_SVEK))
			fileFormatOption.setDebugSvek(true);

		if (flags.isTrue(CliFlag.NO_METADATA))
			fileFormatOption.hideMetadata();

		return fileFormatOption;
	}
//	// ::done
//
//	public final boolean isDuration() {
//		return duration;
//	}

	public final int getNbThreads() {
		final String value = flags.getString(CliFlag.NB_THREAD);
		if (value != null && "auto".equalsIgnoreCase(value) == false)
			try {
				return Integer.parseInt(value);
			} catch (Throwable t) {
			}
		return defaultNbThreads();
	}

//	public final void setNbThreads(int nb) {
//		this.nbThreads = nb;
//	}

	public static int defaultNbThreads() {
		return Runtime.getRuntime().availableProcessors();
	}

//	// ::comment when __CORE__
//	public final boolean isCheckOnly() {
//		return checkOnly;
//	}
//
//	public final void setCheckOnly(boolean checkOnly) {
//		this.checkOnly = checkOnly;
//	}
//	// ::done

	public final boolean isFailfastOrFailfast2() {
		return isTrue(CliFlag.FAIL_FAST) || isTrue(CliFlag.FAIL_FAST2);
	}

//	public final void setDebugSvek(boolean debugsvek) {
//		this.debugsvek = debugsvek;
//	}
//
//	boolean isDebugSvek() {
//		return debugsvek;
//	}
//
//	// ::comment when __CORE__
//	public final boolean isSplash() {
//		return splash;
//	}
//
//	public final void setSplash(boolean splash) {
//		this.splash = splash;
//	}
//
//	public final boolean isTextProgressBar() {
//		return textProgressBar;
//	}
//
//	public String getPipeDelimitor() {
//		return pipeDelimitor;
//	}
//
//	public final boolean isPipeNoStdErr() {
//		return pipeNoStdErr;
//	}
//
//	public final boolean isCheckMetadata() {
//		return checkMetadata;
//	}
//
//	public final void setFilename(String filename) {
//		this.filename = filename;
//	}

	// ::done

	public final int getImageIndex() {
		return imageIndex;
	}

	public final OptionPreprocOutputMode getPreprocessorOutputMode() {
		if (isTrue(CliFlag.CYPHER))
			return OptionPreprocOutputMode.CYPHER;
		if (isTrue(CliFlag.PREPROCESS))
			return OptionPreprocOutputMode.NORMAL;
		return null;
//	} else if (s.equalsIgnoreCase("-preproc")) {
//	preprocessorOutput = OptionPreprocOutputMode.NORMAL;
//	setFileFormatOption(new FileFormatOption(FileFormat.PREPROC));
//
//} else if (s.equalsIgnoreCase("-cypher")) {
//	preprocessorOutput = OptionPreprocOutputMode.CYPHER;
//	setFileFormatOption(new FileFormatOption(FileFormat.PREPROC));
	}

	public String getFileDir() {
		return fileDir;
	}

	public boolean isTrue(CliFlag flag) {
		return flags.isTrue(flag);
	}

	public String getString(CliFlag flag) {
		return flags.getString(flag);
	}

	public void setValue(CliFlag flag, Object value) {
		flags.putValue(flag, value);

	}

}
