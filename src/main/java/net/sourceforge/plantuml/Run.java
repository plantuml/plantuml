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
package net.sourceforge.plantuml;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.UIManager;

import net.sourceforge.plantuml.cli.CliOptions;
import net.sourceforge.plantuml.cli.CliParser;
import net.sourceforge.plantuml.cli.GlobalConfig;
import net.sourceforge.plantuml.cli.GlobalConfigKey;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.file.FileGroup;
import net.sourceforge.plantuml.file.SuggestedFile;
import net.sourceforge.plantuml.ftp.FtpServer;
import net.sourceforge.plantuml.klimt.drawing.svg.SvgGraphics;
import net.sourceforge.plantuml.klimt.sprite.SpriteGrayLevel;
import net.sourceforge.plantuml.klimt.sprite.SpriteUtils;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.picoweb.PicoWebServer;
import net.sourceforge.plantuml.png.MetadataTag;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.stats.StatsUtils;
import net.sourceforge.plantuml.swing.ClipboardLoop;
import net.sourceforge.plantuml.swing.MainWindow;
import net.sourceforge.plantuml.syntax.LanguageDescriptor;
import net.sourceforge.plantuml.utils.Cypher;
import net.sourceforge.plantuml.utils.Log;
import net.sourceforge.plantuml.version.Version;

public class Run {
	// ::remove file when __CORE__
	// ::remove file when __HAXE__

	private static Cypher cypher;

	public static void main(String[] argsArray)
			throws NoPlantumlCompressionException, IOException, InterruptedException {
		System.setProperty("log4j.debug", "false");
		final long start = System.currentTimeMillis();
		if (argsArray.length > 0 && argsArray[0].equalsIgnoreCase("-headless"))
			System.setProperty("java.awt.headless", "true");
		saveCommandLine(argsArray);

		final String display = System.getenv("DISPLAY");
		final String waylandDisplay = System.getenv("WAYLAND_DISPLAY");

		if (display == null && waylandDisplay != null)
			Log.info(() -> "Wayland detected; X11 compatibility may be required via XWayland.");
		else if (display != null)
			Log.info(() -> "X11 display available: " + display);
		else
			Log.info(() -> "No display detected; you may need the -headless flag.");

		if (GraphicsEnvironment.isHeadless()) {
			Log.info(() -> "Forcing -Djava.awt.headless=true");
			System.setProperty("java.awt.headless", "true");
		}

		final String javaAwtHeadless = System.getProperty("java.awt.headless");
		if (javaAwtHeadless == null)
			Log.info(() -> "java.awt.headless not set");
		else
			Log.info(() -> "java.awt.headless set as '" + javaAwtHeadless + "'");

		final CliOptions option = CliParser.parse(argsArray);
		ProgressBar.setEnable(option.isTextProgressBar());
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.CLIPBOARD_LOOP)) {
			ClipboardLoop.runLoop();
			return;
		}
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.CLIPBOARD)) {
			ClipboardLoop.runOnce();
			return;
		}
//		if (GlobalConfig.getInstance().isExtractStdLib()) {
//			Stdlib.extractStdLib();
//			return;
//		}
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.STD_LIB)) {
			Stdlib.printStdLib();
			return;
		}
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.DUMP_STATS)) {
			StatsUtils.dumpStats();
			return;
		}
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.LOOP_STATS)) {
			StatsUtils.loopStats();
			return;
		}
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.DUMP_HTML_STATS)) {
			StatsUtils.outHtml();
			return;
		}
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.ENCODESPRITE)) {
			encodeSprite(option.getResult());
			return;
		}
		Log.info(() -> "SecurityProfile " + SecurityUtils.getSecurityProfile());
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.VERBOSE)) {
			Log.info(() -> "PlantUML Version " + Version.versionString());
			Log.info(() -> "GraphicsEnvironment.isHeadless() " + GraphicsEnvironment.isHeadless());
		}

		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.PRINT_FONTS)) {
			printFonts();
			return;
		}

		if (option.getFtpPort() != -1) {
			goFtp(option);
			return;
		}

		if (option.getPicowebPort() != -1) {
			goPicoweb(option);
			return;
		}

		forceOpenJdkResourceLoad();
		if (option.getPreprocessorOutputMode() == OptionPreprocOutputMode.CYPHER)
			cypher = new LanguageDescriptor().getCypher();

		final ErrorStatus error = ErrorStatus.init();
		boolean forceQuit = false;
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.GUI)) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
			}
			final List<String> list = option.getResult();
			File dir = null;
			if (list.size() == 1) {
				final File f = new File(list.get(0));
				if (f.exists() && f.isDirectory())
					dir = f;

			}
			try {
				new MainWindow(option, dir);
			} catch (java.awt.HeadlessException e) {
				System.err.println("There is an issue with your server. You will find some tips here:");
				System.err.println("https://forum.plantuml.net/3399/problem-with-x11-and-headless-exception");
				System.err.println("https://plantuml.com/en/faq#239d64f675c3e515");
				throw e;
			}

		} else if (option.isPipe() || option.isPipeMap() || option.isSyntax()) {
			managePipe(option, error);
			forceQuit = true;
		} else if (option.isFailfast2()) {
			if (option.isSplash())
				Splash.createSplash();

			final long start2 = System.currentTimeMillis();
			option.setCheckOnly(true);
			manageAllFiles(option, error);
			option.setCheckOnly(false);
			if (option.isDuration()) {
				final double duration = (System.currentTimeMillis() - start2) / 1000.0;
				Log.error("Check Duration = " + duration + " seconds");
			}
			if (error.hasError() == false)
				manageAllFiles(option, error);

			forceQuit = true;
		} else {
			if (option.isSplash())
				Splash.createSplash();

			manageAllFiles(option, error);
			forceQuit = true;
		}

		if (option.isDuration()) {
			final double duration = (System.currentTimeMillis() - start) / 1000.0;
			Log.error("Duration = " + duration + " seconds");
		}

		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.GUI) == false) {
			if (error.hasError() || error.isNoData())
				option.getStdrpt().finalMessage(error);

			if (error.hasError())
				System.exit(error.getExitCode());

			if (forceQuit && GlobalConfig.getInstance().boolValue(GlobalConfigKey.SYSTEM_EXIT))
				System.exit(0);

		}
	}

	private static String commandLine = "";

	public static final String getCommandLine() {
		return commandLine;
	}

	private static void saveCommandLine(String[] argsArray) {
		final StringBuilder sb = new StringBuilder();
		for (String s : argsArray) {
			sb.append(s);
			sb.append(" ");
		}
		commandLine = sb.toString();
	}

	public static void forceOpenJdkResourceLoad() {
		if (isOpenJdkRunning()) {
			// see https://github.com/plantuml/plantuml/issues/123
			Log.info(() -> "Forcing resource load on OpenJdk");
			final BufferedImage imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
			final Graphics2D gg = imDummy.createGraphics();
			final String text = "Alice";
			final Font font = new Font("SansSerif", Font.PLAIN, 12);
			final FontMetrics fm = gg.getFontMetrics(font);
			final Rectangle2D rect = fm.getStringBounds(text, gg);
		}
	}

	public static boolean isOpenJdkRunning() {
		final String jvmName = System.getProperty("java.vm.name");
		if (jvmName != null && jvmName.toLowerCase().contains("openjdk")) {
			return true;
		}
		return false;
	}

	static private final String httpProtocol = "http://";
	static private final String httpsProtocol = "https://";

	private static void encodeSprite(List<String> result) throws IOException {
		SpriteGrayLevel level = SpriteGrayLevel.GRAY_16;
		boolean compressed = false;
		final String path;
		if (result.size() > 1 && result.get(0).matches("(4|8|16)z?")) {
			if (result.get(0).startsWith("8")) {
				level = SpriteGrayLevel.GRAY_8;
			}
			if (result.get(0).startsWith("4")) {
				level = SpriteGrayLevel.GRAY_4;
			}
			compressed = StringUtils.goLowerCase(result.get(0)).endsWith("z");
			path = result.get(1);
		} else {
			path = result.get(0);
		}

		final String fileName;
		final URL source;
		final String lowerPath = StringUtils.goLowerCase(path);
		if (lowerPath.startsWith(httpProtocol) || lowerPath.startsWith(httpsProtocol)) {
			source = new java.net.URL(path);
			final String p = source.getPath();
			fileName = p.substring(p.lastIndexOf('/') + 1, p.length());
		} else {
			final SFile f = new SFile(path);
			source = f.toURI().toURL();
			fileName = f.getName();
		}

		if (source == null) {
			return;
		}

		final BufferedImage im;
		try (InputStream stream = source.openStream()) {
			im = SImageIO.read(stream);
		}
		final String name = getSpriteName(fileName);
		final String s = compressed ? SpriteUtils.encodeCompressed(im, name, level)
				: SpriteUtils.encode(im, name, level);
		System.out.println(s);
	}

	private static String getSpriteName(String fileName) {
		final String s = getSpriteNameInternal(fileName);
		if (s.length() == 0) {
			return "test";
		}
		return s;
	}

	private static String getSpriteNameInternal(String fileName) {
		final StringBuilder sb = new StringBuilder();
		for (char c : fileName.toCharArray()) {
			if (("" + c).matches("[\\p{L}0-9_]")) {
				sb.append(c);
			} else {
				return sb.toString();
			}
		}
		return sb.toString();
	}

	private static void goFtp(CliOptions option) throws IOException {
		final int ftpPort = option.getFtpPort();
		System.err.println("ftpPort=" + ftpPort);
		final FtpServer ftpServer = new FtpServer(ftpPort, option.getFileFormatOption().getFileFormat());
		ftpServer.go();
	}

	private static void goPicoweb(CliOptions option) throws IOException {
		PicoWebServer.startServer(option.getPicowebPort(), option.getPicowebBindAddress(),
				option.getPicowebEnableStop());
	}

	public static void printFonts() {
		final Font fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		for (Font f : fonts) {
			System.out.println(
					"f=" + f + "/" + f.getPSName() + "/" + f.getName() + "/" + f.getFontName() + "/" + f.getFamily());
		}
		final String name[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (String n : name) {
			System.out.println("n=" + n);
		}
	}

	private static void managePipe(CliOptions option, ErrorStatus error) throws IOException {
		final String charset = option.getCharset();
		new Pipe(option, System.out, System.in, charset).managePipe(error);
	}

	private static void manageAllFiles(CliOptions option, ErrorStatus error)
			throws NoPlantumlCompressionException, InterruptedException {

		SFile lockFile = null;
		try {
			if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.WORD)) {
				final SFile dir = new SFile(option.getResult().get(0));
				final SFile javaIsRunningFile = dir.file("javaisrunning.tmp");
				javaIsRunningFile.delete();
				lockFile = dir.file("javaumllock.tmp");
			}
			processArgs(option, error);
		} finally {
			if (lockFile != null) {
				lockFile.delete();
			}
		}

	}

	private static void processArgs(CliOptions option, ErrorStatus error)
			throws NoPlantumlCompressionException, InterruptedException {
		if (option.isDecodeurl() == false && option.getNbThreads() > 1 && option.isCheckOnly() == false
				&& GlobalConfig.getInstance().boolValue(GlobalConfigKey.EXTRACT_FROM_METADATA) == false) {
			multithread(option, error);
			return;
		}
		final List<File> files = new ArrayList<>();
		for (String s : option.getResult()) {
			if (option.isDecodeurl()) {
				error.goOk();
				final Transcoder transcoder = TranscoderUtil.getDefaultTranscoder();
				System.out.println("@startuml");
				System.out.println(transcoder.decode(s));
				System.out.println("@enduml");
			} else {
				final FileGroup group = new FileGroup(s, option.getExcludes(), option);
				incTotal(group.getFiles().size());
				files.addAll(group.getFiles());
			}
		}
		foundNbFiles(files.size());
		for (File f : files) {
			try {
				manageFileInternal(f, option, error);
				incDone(error.hasError());
				if (error.hasError() && option.isFailfastOrFailfast2()) {
					return;
				}
			} catch (IOException e) {
				Logme.error(e);
			}
		}
	}

	private static void multithread(CliOptions option, ErrorStatus error) throws InterruptedException {
		Log.info(() -> "Using several threads: " + option.getNbThreads());
		final ExecutorService executor = Executors.newFixedThreadPool(option.getNbThreads());

		int nb = 0;
		for (String s : option.getResult()) {
			final FileGroup group = new FileGroup(s, option.getExcludes(), option);
			for (final File f : group.getFiles()) {
				incTotal(1);
				nb++;
				executor.submit(new Runnable() {
					public void run() {
						if (error.hasError() && option.isFailfastOrFailfast2()) {
							return;
						}
						try {
							manageFileInternal(f, option, error);
						} catch (IOException e) {
							Logme.error(e);
						} catch (InterruptedException e) {
							Logme.error(e);
						}
						incDone(error.hasError());
					}
				});
			}
		}
		foundNbFiles(nb);
		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
	}

	private static void foundNbFiles(int nb) {
		Log.info(() -> "Found " + nb + " files");
	}

	private static void incDone(boolean error) {
		Splash.incDone(error);
		ProgressBar.incDone(error);
	}

	private static void incTotal(int nb) {
		Splash.incTotal(nb);
		ProgressBar.incTotal(nb);
	}

	private static void manageFileInternal(File f, CliOptions option, ErrorStatus error)
			throws IOException, InterruptedException {
		Log.info(() -> "Working on " + f.getPath());
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.EXTRACT_FROM_METADATA)) {
			error.goOk();
			extractMetadata(f);
			return;
		}
		final ISourceFileReader sourceFileReader;
		if (option.getOutputFile() == null) {
			File outputDir = option.getOutputDir();
			if (outputDir != null && outputDir.getPath().endsWith("$")) {
				final String path = outputDir.getPath();
				outputDir = new File(path.substring(0, path.length() - 1)).getAbsoluteFile();
				sourceFileReader = new SourceFileReaderCopyCat(option.getDefaultDefines(f), f, outputDir,
						option.getConfig(), option.getCharset(), option.getFileFormatOption());
			} else {
				sourceFileReader = new SourceFileReader(option.getDefaultDefines(f), f, outputDir, option.getConfig(),
						option.getCharset(), option.getFileFormatOption());
			}
		} else {
			sourceFileReader = new SourceFileReaderHardFile(option.getDefaultDefines(f), f, option.getOutputFile(),
					option.getConfig(), option.getCharset(), option.getFileFormatOption());
		}
		sourceFileReader.setCheckMetadata(option.isCheckMetadata());
		((SourceFileReaderAbstract) sourceFileReader).setNoerror(option.isNoerror());

		if (option.isComputeurl()) {
			error.goOk();
			for (BlockUml s : sourceFileReader.getBlocks()) {
				System.out.println(s.getEncodedUrl());
			}
			return;
		}
		if (option.isCheckOnly()) {
			error.goOk();
			final boolean hasError = sourceFileReader.hasError();
			if (hasError) {
				error.goWithError();
			}
			// final List<GeneratedImage> result = sourceFileReader.getGeneratedImages();
			// hasErrors(f, result, error);
			return;
		}
		if (option.getPreprocessorOutputMode() != null) {
			extractPreproc(option, sourceFileReader);
			error.goOk();
			return;
		}
		final List<GeneratedImage> result = sourceFileReader.getGeneratedImages();
		final Stdrpt rpt = option.getStdrpt();
		if (result.size() == 0) {
			Log.error("Warning: no image in " + f.getPath());
			rpt.printInfo(System.err, null);
			// error.goNoData();
			return;
		}
		for (BlockUml s : sourceFileReader.getBlocks()) {
			rpt.printInfo(System.err, s.getDiagram());
		}

		hasErrors(f, result, error, rpt);
	}

	private static void extractPreproc(CliOptions option, final ISourceFileReader sourceFileReader) throws IOException {
		final String charset = option.getCharset();
		for (BlockUml blockUml : sourceFileReader.getBlocks()) {
			final SuggestedFile suggested = ((SourceFileReaderAbstract) sourceFileReader).getSuggestedFile(blockUml)
					.withPreprocFormat();
			final SFile file = suggested.getFile(0);
			Log.info(() -> "Export preprocessing source to " + file.getPrintablePath());
			try (final PrintWriter pw = charset == null ? file.createPrintWriter() : file.createPrintWriter(charset)) {
				int level = 0;
				for (CharSequence cs : blockUml.getDefinition(true)) {
					String s = cs.toString();
					if (cypher != null) {
						if (s.contains("skinparam") && s.contains("{")) {
							level++;
						}
						if (level == 0 && s.contains("skinparam") == false) {
							s = cypher.cypher(s);
						}
						if (level > 0 && s.contains("}")) {
							level--;
						}
					}
					pw.println(s);
				}
			}
		}
	}

	private static void hasErrors(File file, final List<GeneratedImage> list, ErrorStatus error, Stdrpt stdrpt)
			throws IOException {
		if (list.size() == 0) {
			// error.goNoData();
			return;
		}
		for (GeneratedImage image : list) {
			final int lineError = image.lineErrorRaw();
			if (lineError != -1) {
				stdrpt.errorLine(lineError, file);
				error.goWithError();
				return;
			}
		}
		error.goOk();
	}

	private static void extractMetadata(File f) throws IOException {
		System.out.println("------------------------");
		System.out.println(f);
		System.out.println();
		if (f.getName().endsWith(".svg")) {
			final SFile file = SFile.fromFile(f);
			final String svg = FileUtils.readFile(file);
			final int idx = svg.lastIndexOf(SvgGraphics.META_HEADER);
			if (idx > 0) {
				String part = svg.substring(idx + SvgGraphics.META_HEADER.length());
				final int idxEnd = part.indexOf("]");
				if (idxEnd > 0) {
					part = part.substring(0, idxEnd);
					part = part.replace("- -", "--");
					final String decoded = TranscoderUtil.getDefaultTranscoderProtected().decode(part);
					System.err.println(decoded);
				}
			}
		} else {
			final String data = new MetadataTag(f, "plantuml").getData();
			System.out.println(data);
		}
		System.out.println("------------------------");
	}

}
