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
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.security.SFile;

public class CliOptions {
	// ::remove file when __CORE__
	// ::remove file when __HAXE__

	private final List<String> config = new ArrayList<>();

	private File outputDir = null;

	public final CliParsed flags;

	public CliOptions() {
		this.flags = new CliParsed();
	}

	private FileFormatOption fileFormatOption = null;

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

		final String limitSize = defines().get("PLANTUML_LIMIT_SIZE");
		if (limitSize != null)
			System.setProperty("PLANTUML_LIMIT_SIZE", limitSize);

		for (String key : flags.getMap(CliFlag.INCLUDE, CliFlag.INCLUDE_LONG).keySet())
			initInclude(key);

		for (Object theme : flags.getList(CliFlag.THEME))
			config.add("!theme " + theme);

		for (Object fileName : flags.getList(CliFlag.CONFIG))
			addInConfig(fileName.toString());

		for (Entry<String, String> ent : flags.getMap(CliFlag.PRAGMA, CliFlag.PRAGMA_LONG).entrySet())
			config.add("!pragma " + ent.getKey() + " " + ent.getValue());

		for (Entry<String, String> ent : flags.getMap(CliFlag.SKINPARAM, CliFlag.SKINPARAM_LONG).entrySet())
			config.add("skinparamlocked " + ent.getKey() + " " + ent.getValue());

//		for (int i = 0; i < arg.length; i++) {
//			String s = arg[i];
//			if (s.equalsIgnoreCase("-headless")) {
//				// Useless because done in Run.java
//				if (i != 0)
//					Log.error("Warning: -headless flag must be the first one in the command line");
//
//				System.setProperty("java.awt.headless", "true");
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

//			} else if (s.equalsIgnoreCase("-help") || s.equalsIgnoreCase("-h") || s.equalsIgnoreCase("-?")) {
//				OptionPrint.printHelp();
//
//			} else if (s.equalsIgnoreCase("-splash")) {
//				splash = true;
//
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

	private void addFileInConfig(File file) throws IOException {
		if (file.exists() && file.canRead())
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String s = null;
				while ((s = br.readLine()) != null)
					config.add(s);
			}
	}

	public void addInConfig(String filename) throws IOException {
		addFileInConfig(new File(filename));
	}

	private void initInclude(String filename) throws IOException {
		if (filename == null)
			return;

		if (filename.contains("*"))
			for (File f : new FileGroup(filename, Collections.<String>emptyList()).getFiles())
				addFileInConfig(f);
		else
			addFileInConfig(new File(filename));

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
		result.overrideFilename(flags.getString(CliFlag.FILENAME));
		result.overrideDirPath(flags.getString(CliFlag.FILE_DIR));
		for (Map.Entry<String, String> ent : defines().entrySet())
			result.define(ent.getKey(), Arrays.asList(ent.getValue()), false, null);

		return result;
	}
	// ::done

//	public void define(String name, String value) {
//		defines.put(name, value);
//	}

	private Map<String, String> defines() {
		final Map<String, String> result = flags.getMap(CliFlag.DEFINE, CliFlag.DEFINE_LONG);
		return Collections.unmodifiableMap(result);
	}

	public List<String> getConfig() {
		return Collections.unmodifiableList(config);
	}

	public final List<String> getRemainingArgs() {
		return flags.getRemainingArgs();
	}

	public FileFormatOption getFileFormatOption() {
		if (this.fileFormatOption != null)
			return fileFormatOption;

		FileFormat format = null;
		final String formatFlag = flags.getString(CliFlag.FORMAT);
		if (formatFlag != null)
			format = FileFormat.fromCli(formatFlag);

		if (format == null)
			format = (FileFormat) flags.getFromType(FileFormat.class);

		if (format == null)
			format = FileFormat.PNG;

		this.fileFormatOption = new FileFormatOption(format);

		if (flags.isTrue(CliFlag.DEBUG_SVEK))
			fileFormatOption.setDebugSvek(true);

		if (flags.isTrue(CliFlag.NO_METADATA))
			fileFormatOption.hideMetadata();

		if (flags.isTrue(CliFlag.DARK_MODE))
			this.fileFormatOption = this.fileFormatOption.withColorMapper(ColorMapper.DARK_MODE);

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
		try {
			return Integer.parseInt(flags.getString(CliFlag.PIPE_IMAGE_INDEX));
		} catch (Exception e) {
			return 0;
		}
	}

	public final OptionPreprocOutputMode getPreprocessorOutputMode() {
		if (isTrue(CliFlag.CYPHER))
			return OptionPreprocOutputMode.CYPHER;
		if (isTrue(CliFlag.PREPROCESS))
			return OptionPreprocOutputMode.NORMAL;
		return null;
	}

	public String getFileDir() {
		return flags.getString(CliFlag.FILE_DIR);
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
