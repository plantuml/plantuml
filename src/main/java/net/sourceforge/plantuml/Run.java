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
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.UIManager;

import net.sourceforge.plantuml.cli.CliAction;
import net.sourceforge.plantuml.cli.CliFlag;
import net.sourceforge.plantuml.cli.CliOptions;
import net.sourceforge.plantuml.cli.CliParser;
import net.sourceforge.plantuml.cli.CliParsingException;
import net.sourceforge.plantuml.cli.ErrorStatus;
import net.sourceforge.plantuml.cli.Exit;
import net.sourceforge.plantuml.cli.GlobalConfig;
import net.sourceforge.plantuml.cli.GlobalConfigKey;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment;
import net.sourceforge.plantuml.file.FileGroup;
import net.sourceforge.plantuml.file.SuggestedFile;
import net.sourceforge.plantuml.ftp.FtpServer;
import net.sourceforge.plantuml.klimt.drawing.svg.SvgGraphics;
import net.sourceforge.plantuml.klimt.sprite.SpriteGrayLevel;
import net.sourceforge.plantuml.klimt.sprite.SpriteUtils;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.picoweb.PicoWebServer;
import net.sourceforge.plantuml.png.MetadataTag;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.swing.MainWindow;
import net.sourceforge.plantuml.syntax.LanguageDescriptor;
import net.sourceforge.plantuml.utils.Obfuscate;
import net.sourceforge.plantuml.utils.Log;
import net.sourceforge.plantuml.version.Version;

public class Run {
	// ::remove file when __CORE__
	// ::remove file when __HAXE__

	public static void main(String[] argsArray)
			throws NoPlantumlCompressionException, IOException, InterruptedException {
		System.setProperty("log4j.debug", "false");

		final long start = System.currentTimeMillis();
		if (argsArray.length > 0 && argsArray[0].equalsIgnoreCase("-headless"))
			System.setProperty("java.awt.headless", "true");

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

		final ErrorStatus errorStatus = ErrorStatus.init();
		CliOptions option = null;

		try {
			option = CliParser.parse(argsArray);

			final String timeout = option.getString(CliFlag.TIMEOUT);
			if (timeout != null && timeout.matches("\\d+"))
				GlobalConfig.getInstance().put(GlobalConfigKey.TIMEOUT_MS, Integer.parseInt(timeout) * 1000L);

			final String charset = option.getString(CliFlag.CHARSET);
			Log.info(() -> "Using charset " + charset);

			option.flags.triggerNonImmediateCliAction();

			final CliAction action = option.flags.getImmediateAction();
			if (action != null) {
				action.runAction();
				return;
			}

			if (option.isTrue(CliFlag.GRAPHVIZ_DOT)) {
				final String v = option.flags.getString(CliFlag.GRAPHVIZ_DOT);
				GraphvizRuntimeEnvironment.getInstance()
						.setDotExecutable(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(v));
			}

			if (option.flags.isTrue(CliFlag.FTP)) {
				goFtp(option);
				return;
			}

			final List<String> remainingArgs = option.flags.getRemainingArgs();

			ProgressBar.setEnable(option.isTrue(CliFlag.PROGRESS));

			Log.info(() -> "SecurityProfile " + SecurityUtils.getSecurityProfile());
			if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.VERBOSE)) {
				Log.info(() -> "PlantUML Version " + Version.versionString());
				Log.info(() -> "GraphicsEnvironment.isHeadless() " + GraphicsEnvironment.isHeadless());
			}

			if (option.isTrue(CliFlag.ENCODE_SPRITE)) {
				encodeSprite(remainingArgs);
				return;
			}

			if (option.isTrue(CliFlag.PICOWEB) && option.getPicowebPort() != -1) {
				goPicoweb(option);
				return;
			}

			forceOpenJdkResourceLoad();

			if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.GUI)) {
				runGui(option);
				return;
			}

			if (option.isTrue(CliFlag.PIPE) || option.isTrue(CliFlag.PIPEMAP) || option.isTrue(CliFlag.SYNTAX)) {
				new Pipe(option, System.out, System.in, charset).managePipe(errorStatus);
				final int exitCode = errorStatus.getExitCode();
				if (exitCode != 0)
					Exit.exit(exitCode);
				return;
			}

			if (option.isTrue(CliFlag.DECODE_URL)) {
				for (String s : option.getRemainingArgs()) {
					final Transcoder transcoder = TranscoderUtil.getDefaultTranscoder();
					System.out.println("@startuml");
					System.out.println(transcoder.decode(s));
					System.out.println("@enduml");
				}
				return;
			}

			if (option.isTrue(CliFlag.RETRIEVE_METADATA)) {
				final List<File> files = new ArrayList<>();
				for (String s : option.getRemainingArgs()) {
					final FileGroup group = new FileGroup(s, option.getExcludes());
					incTotal(group.getFiles().size());
					files.addAll(group.getFiles());
				}
				for (File f : files)
					extractMetadata(f);

				return;
			}
			if (option.isTrue(CliFlag.SPLASH))
				Splash.createSplash();

			final Run runner = new Run(option, errorStatus, charset);

			if (runner.size() == 0) {
				Log.error("No file found");
				Exit.exit(ErrorStatus.ERROR_NO_FILE_FOUND);
			}

			incTotal(runner.size());

			if (option.isTrue(CliFlag.COMPUTE_URL)) {
				runner.computeUrl();
				return;
			} else if (option.isTrue(CliFlag.FAIL_FAST2) && runner.checkError())
				errorStatus.incError();
			else
				runner.processInputsInParallel();

		} catch (CliParsingException e) {
			System.err.println(e.getMessage());
			Exit.exit(42);
		} finally {
			if (option != null && option.isTrue(CliFlag.DURATION)) {
				final double duration = (System.currentTimeMillis() - start) / 1000.0;
				Log.error("Duration = " + duration + " seconds");
			}
		}

		if (option != null)
			option.getStdrpt().finalMessage(errorStatus);

		final int exitCode = errorStatus.getExitCode();
		if (exitCode != 0)
			Exit.exit(exitCode);
	}

	private final CliOptions option;
	private final ErrorStatus errorStatus;
	private final String charset;
	private final List<File> files = new ArrayList<>();

	public Run(CliOptions option, ErrorStatus errorStatus, String charset) {
		this.option = option;
		this.errorStatus = errorStatus;
		this.charset = charset;

		for (String s : option.getRemainingArgs()) {
			final FileGroup group = new FileGroup(s, option.getExcludes());
			for (final File f : group.getFiles())
				files.add(f);
		}

		Log.info(() -> "Found " + size() + " files");
	}

	public int size() {
		return files.size();
	}

	@FunctionalInterface
	static interface FileTask {
		public void processFile(File f) throws IOException, InterruptedException;

	}

	private boolean checkError() throws InterruptedException {
		final AtomicBoolean hasError = new AtomicBoolean();
		processInParallel(file -> {
			if (hasError.get())
				return;
			final ISourceFileReader sourceFileReader = getSourceFileReader(file, option, charset);
			if (sourceFileReader.hasError()) {
				hasError.set(true);
				errorStatus.incError42();
			} else
				errorStatus.incOk();
		});

		return hasError.get();

	}

	private void computeUrl() throws IOException {
		for (File f : files) {
			final ISourceFileReader sourceFileReader = getSourceFileReader(f, option, charset);
			for (BlockUml s : sourceFileReader.getBlocks())
				System.out.println(s.getEncodedUrl());
		}
	}

	private void processInputsInParallel() throws InterruptedException {
		SFile lockFile = null;
		try {
			if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.WORD)) {
				final SFile dir = new SFile(option.getRemainingArgs().get(0));
				final SFile javaIsRunningFile = dir.file("javaisrunning.tmp");
				javaIsRunningFile.delete();
				lockFile = dir.file("javaumllock.tmp");
			}
			processInParallel(file -> {
				if (errorStatus.hasError() && option.isFailfastOrFailfast2())
					return;

				manageFileInternal(file);
				incDone(errorStatus.hasError());
			});
		} finally {
			if (lockFile != null)
				lockFile.delete();
		}
	}

	private void processInParallel(FileTask task) throws InterruptedException {
		Log.info(() -> "Using several threads: " + option.getNbThreads());
		final ExecutorService executor = Executors.newFixedThreadPool(option.getNbThreads());

		for (File f : files)
			executor.submit(() -> {
				try {
					task.processFile(f);
				} catch (IOException | InterruptedException e) {
					Logme.error(e);
				}
			});

		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
	}

	private void manageFileInternal(File f) throws IOException, InterruptedException {
		Log.info(() -> "Working on " + f.getPath());
		final ISourceFileReader sourceFileReader = getSourceFileReader(f, option, charset);
		sourceFileReader.updateStatus(errorStatus);

		if (sourceFileReader.hasError())
			errorStatus.incError();
		else
			errorStatus.incOk();

		if (option.isTrue(CliFlag.CHECK_ONLY))
			return;

		if (option.getFileFormatOption().getFileFormat() == FileFormat.PREPROC
				|| option.getFileFormatOption().getFileFormat() == FileFormat.OBFUSCATE) {
			extractPreprocessingSource(sourceFileReader);
			return;
		}
		final List<GeneratedImage> result = sourceFileReader.getGeneratedImages();
		final Stdrpt rpt = option.getStdrpt();
		if (result.size() == 0) {
			Log.error("Warning: no image in " + f.getPath());
			rpt.printInfo(System.err, null);
			return;
		}
		for (BlockUml s : sourceFileReader.getBlocks())
			rpt.printInfo(System.err, s.getDiagram());

		if (result.size() != 0) {
			for (GeneratedImage image : result) {
				final int lineError = image.lineErrorRaw();
				if (lineError != -1) {
					rpt.errorLine(lineError, f);
					errorStatus.incError();
					return;
				}
			}
			errorStatus.incOk();
		}
	}

	private static void runGui(final CliOptions option) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		final List<String> list = option.getRemainingArgs();
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
		if (jvmName != null && jvmName.toLowerCase().contains("openjdk"))
			return true;

		return false;
	}

	static private final String httpProtocol = "http://";
	static private final String httpsProtocol = "https://";

	private static void encodeSprite(List<String> remainingArgs) throws IOException {
		SpriteGrayLevel level = SpriteGrayLevel.GRAY_16;
		boolean compressed = false;
		final String path;
		if (remainingArgs.size() > 1 && remainingArgs.get(0).matches("(4|8|16)z?")) {
			if (remainingArgs.get(0).startsWith("8"))
				level = SpriteGrayLevel.GRAY_8;

			if (remainingArgs.get(0).startsWith("4"))
				level = SpriteGrayLevel.GRAY_4;

			compressed = StringUtils.goLowerCase(remainingArgs.get(0)).endsWith("z");
			path = remainingArgs.get(1);
		} else {
			path = remainingArgs.get(0);
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

		if (source == null)
			return;

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
		if (s.length() == 0)
			return "test";

		return s;
	}

	private static String getSpriteNameInternal(String fileName) {
		final StringBuilder sb = new StringBuilder();
		for (char c : fileName.toCharArray())
			if (("" + c).matches("[\\p{L}0-9_]"))
				sb.append(c);
			else
				return sb.toString();

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
		for (Font f : fonts)
			System.out.println(
					"f=" + f + "/" + f.getPSName() + "/" + f.getName() + "/" + f.getFontName() + "/" + f.getFamily());

		final String name[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (String n : name)
			System.out.println("n=" + n);

	}

	private static void incDone(boolean error) {
		Splash.incDone(error);
		ProgressBar.incDone(error);
	}

	private static void incTotal(int nb) {
		Splash.incTotal(nb);
		ProgressBar.incTotal(nb);
	}

	private static ISourceFileReader getSourceFileReader(File f, CliOptions option, String charset) throws IOException {
		final ISourceFileReader sourceFileReader;
		final FileFormatOption fileFormatOption = option.getFileFormatOption();

		File outputDir = option.getOutputDir();
		if (outputDir != null && outputDir.getPath().endsWith("$")) {
			final String path = outputDir.getPath();
			outputDir = new File(path.substring(0, path.length() - 1)).getAbsoluteFile();
			sourceFileReader = new SourceFileReaderCopyCat(option.isTrue(CliFlag.IGNORE_STARTUML_FILENAME),
					option.getDefaultDefines(f), f, outputDir, option.getConfig(), charset, fileFormatOption);
		} else {
			sourceFileReader = new SourceFileReader(option.isTrue(CliFlag.IGNORE_STARTUML_FILENAME),
					option.getDefaultDefines(f), f, outputDir, option.getConfig(), charset, fileFormatOption);
		}

		sourceFileReader.setCheckMetadata(option.isTrue(CliFlag.CHECK_METADATA));
		((SourceFileReaderAbstract) sourceFileReader).setNoErrorImage(option.isTrue(CliFlag.NO_ERROR_IMAGE));
		return sourceFileReader;
	}

	private void extractPreprocessingSource(final ISourceFileReader sourceFileReader) throws IOException {

		final FileFormat format = option.getFileFormatOption().getFileFormat();
		final Obfuscate obfuscate = format == FileFormat.OBFUSCATE ? new LanguageDescriptor().getObfuscate() : null;

		for (BlockUml blockUml : sourceFileReader.getBlocks()) {
			final SuggestedFile suggested = ((SourceFileReaderAbstract) sourceFileReader).getSuggestedFile(blockUml)
					.withPreprocFormat(option.getFileFormatOption().getFileFormat());
			final SFile file = suggested.getFile(0);
			Log.info(() -> "Export preprocessing source to " + file.getPrintablePath());
			try (final PrintWriter pw = charset == null ? file.createPrintWriter() : file.createPrintWriter(charset)) {
				int level = 0;
				for (CharSequence cs : blockUml.getDefinition(true)) {
					String s = cs.toString();
					if (obfuscate != null) {
						if (s.contains("skinparam") && s.contains("{"))
							level++;

						if (level == 0 && s.contains("skinparam") == false)
							s = obfuscate.obfuscate(s);

						if (level > 0 && s.contains("}"))
							level--;

					}
					pw.println(s);
				}
			}
		}
	}

	private static final String META_HEADER_NEW = "<?plantuml-src ";

	private static void extractMetadata(File f) throws IOException {
		System.out.println("------------------------");
		System.out.println(f);
		System.out.println();
		if (f.getName().endsWith(".svg")) {
			final SFile file = SFile.fromFile(f);
			final String svg = FileUtils.readFile(file);
			final String decoded = extractMetadataFromSvg(svg);
			if (decoded != null)
				System.out.println(decoded);
		} else {
			final String data = new MetadataTag(f, "plantuml").getData();
			System.out.println(data);
		}
		System.out.println("------------------------");
	}

	private static String extractMetadataFromSvg(String svg) throws NoPlantumlCompressionException {
		// New format: <?plantuml-src ...?>
		final int idxNew = svg.lastIndexOf(META_HEADER_NEW);
		if (idxNew > 0) {
			String part = svg.substring(idxNew + META_HEADER_NEW.length());
			final int idxEnd = part.indexOf("?>");
			if (idxEnd > 0) {
				part = part.substring(0, idxEnd);
				return TranscoderUtil.getDefaultTranscoderProtected().decode(part);
			}
		}
		// Old format: <!--SRC=[...]-->
		final int idxOld = svg.lastIndexOf(SvgGraphics.META_HEADER);
		if (idxOld > 0) {
			String part = svg.substring(idxOld + SvgGraphics.META_HEADER.length());
			final int idxEnd = part.indexOf("]");
			if (idxEnd > 0) {
				part = part.substring(0, idxEnd);
				part = part.replace("- -", "--");
				return TranscoderUtil.getDefaultTranscoderProtected().decode(part);
			}
		}
		return null;
	}

}
