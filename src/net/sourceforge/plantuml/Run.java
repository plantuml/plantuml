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

import javax.imageio.ImageIO;
import javax.swing.UIManager;

import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagramFactory;
import net.sourceforge.plantuml.ftp.FtpServer;
import net.sourceforge.plantuml.png.MetadataTag;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.sprite.SpriteGrayLevel;
import net.sourceforge.plantuml.sprite.SpriteUtils;
import net.sourceforge.plantuml.statediagram.StateDiagramFactory;
import net.sourceforge.plantuml.stats.StatsUtils;
import net.sourceforge.plantuml.swing.MainWindow2;
import net.sourceforge.plantuml.syntax.LanguageDescriptor;
import net.sourceforge.plantuml.utils.Cypher;
import net.sourceforge.plantuml.version.Version;

public class Run {

	private static Cypher cypher;

	public static void main(String[] argsArray) throws NoPlantumlCompressionException, IOException, InterruptedException {
		System.setProperty("log4j.debug", "false");
		final long start = System.currentTimeMillis();
		if (argsArray.length > 0 && argsArray[0].equalsIgnoreCase("-headless")) {
			System.setProperty("java.awt.headless", "true");
		}
		saveCommandLine(argsArray);
		final Option option = new Option(argsArray);
		ProgressBar.setEnable(option.isTextProgressBar());
		if (OptionFlags.getInstance().isClipboardLoop()) {
			ClipboardLoop.runLoop();
			return;
		}
		if (OptionFlags.getInstance().isClipboard()) {
			ClipboardLoop.runOnce();
			return;
		}
		if (OptionFlags.getInstance().isExtractStdLib()) {
			Stdlib.extractStdLib();
			return;
		}
		if (OptionFlags.getInstance().isStdLib()) {
			Stdlib.printStdLib();
			return;
		}
		if (OptionFlags.getInstance().isDumpStats()) {
			StatsUtils.dumpStats();
			return;
		}
		if (OptionFlags.getInstance().isLoopStats()) {
			StatsUtils.loopStats();
			return;
		}
		if (OptionFlags.getInstance().isDumpHtmlStats()) {
			StatsUtils.outHtml();
			return;
		}
		if (OptionFlags.getInstance().isEncodesprite()) {
			encodeSprite(option.getResult());
			return;
		}
		if (OptionFlags.getInstance().isVerbose()) {
			Log.info("PlantUML Version " + Version.versionString());
			Log.info("GraphicsEnvironment.isHeadless() " + GraphicsEnvironment.isHeadless());
		}
		if (GraphicsEnvironment.isHeadless()) {
			Log.info("Forcing -Djava.awt.headless=true");
			System.setProperty("java.awt.headless", "true");
			Log.info("java.awt.headless set as true");

		}
		if (OptionFlags.getInstance().isPrintFonts()) {
			printFonts();
			return;
		}

		if (option.getFtpPort() != -1) {
			goFtp(option);
			return;
		}

		forceOpenJdkResourceLoad();
		if (option.getPreprocessorOutputMode() == OptionPreprocOutputMode.CYPHER) {
			cypher = new LanguageDescriptor().getCypher();
		}
		final ErrorStatus error = ErrorStatus.init();
		boolean forceQuit = false;
		if (option.isPattern()) {
			managePattern();
		} else if (OptionFlags.getInstance().isGui()) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
			}
			final List<String> list = option.getResult();
			File dir = null;
			if (list.size() == 1) {
				final File f = new File(list.get(0));
				if (f.exists() && f.isDirectory()) {
					dir = f;
				}
			}
			new MainWindow2(option, dir);
		} else if (option.isPipe() || option.isPipeMap() || option.isSyntax()) {
			managePipe(option, error);
			forceQuit = true;
		} else if (option.isFailfast2()) {
			if (option.isSplash()) {
				Splash.createSplash();
			}
			final long start2 = System.currentTimeMillis();
			option.setCheckOnly(true);
			manageAllFiles(option, error);
			option.setCheckOnly(false);
			if (option.isDuration()) {
				final double duration = (System.currentTimeMillis() - start2) / 1000.0;
				Log.error("Check Duration = " + duration + " seconds");
			}
			if (error.hasError() == false) {
				manageAllFiles(option, error);
			}
			forceQuit = true;
		} else {
			if (option.isSplash()) {
				Splash.createSplash();
			}
			manageAllFiles(option, error);
			forceQuit = true;
		}

		if (option.isDuration()) {
			final double duration = (System.currentTimeMillis() - start) / 1000.0;
			Log.error("Duration = " + duration + " seconds");
		}

		if (OptionFlags.getInstance().isGui() == false) {
			if (error.hasError()) {
				Log.error("Some diagram description contains errors");
				System.exit(error.getExitCode());
			}
			if (error.isNoData()) {
				Log.error("No diagram found");
				System.exit(error.getExitCode());
			}

			if (forceQuit && OptionFlags.getInstance().isSystemExit()) {
				System.exit(0);
			}
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
			Log.info("Forcing resource load on OpenJdk");
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
			source = new URL(path);
			final String p = source.getPath();
			fileName = p.substring(p.lastIndexOf('/') + 1, p.length());
		} else {
			final File f = new File(path);
			source = f.toURI().toURL();
			fileName = f.getName();
		}

		InputStream stream = null;
		final BufferedImage im;
		try {
			stream = source.openStream();
			im = ImageIO.read(stream);
		} finally {
			if (stream != null) {
				stream.close();
			}
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

	private static void goFtp(Option option) throws IOException {
		final int ftpPort = option.getFtpPort();
		System.err.println("ftpPort=" + ftpPort);
		final FtpServer ftpServer = new FtpServer(ftpPort, option.getFileFormatOption().getFileFormat());
		ftpServer.go();
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

	private static void managePattern() {
		printPattern(new SequenceDiagramFactory(null));
		printPattern(new ClassDiagramFactory(null));
		printPattern(new ActivityDiagramFactory(null));
		printPattern(new DescriptionDiagramFactory(null));
		// printPattern(new ComponentDiagramFactory());
		printPattern(new StateDiagramFactory(null));
		// printPattern(new ObjectDiagramFactory(null));
	}

	private static void printPattern(UmlDiagramFactory factory) {
		System.out.println();
		System.out.println(factory.getClass().getSimpleName().replaceAll("Factory", ""));
		final List<String> descriptions = factory.getDescription();
		for (String s : descriptions) {
			System.out.println(s);
		}
	}

	private static void managePipe(Option option, ErrorStatus error) throws IOException {
		final String charset = option.getCharset();
		new Pipe(option, System.out, System.in, charset).managePipe(error);
	}

	private static void manageAllFiles(Option option, ErrorStatus error) throws NoPlantumlCompressionException, InterruptedException {

		File lockFile = null;
		try {
			if (OptionFlags.getInstance().isWord()) {
				final File dir = new File(option.getResult().get(0));
				final File javaIsRunningFile = new File(dir, "javaisrunning.tmp");
				javaIsRunningFile.delete();
				lockFile = new File(dir, "javaumllock.tmp");
			}
			processArgs(option, error);
		} finally {
			if (lockFile != null) {
				lockFile.delete();
			}
		}

	}

	private static void processArgs(Option option, ErrorStatus error) throws NoPlantumlCompressionException, InterruptedException {
		if (option.isDecodeurl() == false && option.getNbThreads() > 1 && option.isCheckOnly() == false
				&& OptionFlags.getInstance().isExtractFromMetadata() == false) {
			multithread(option, error);
			return;
		}
		final List<File> files = new ArrayList<File>();
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
				e.printStackTrace();
			}
		}
	}

	private static void multithread(final Option option, final ErrorStatus error) throws InterruptedException {
		Log.info("Using several threads: " + option.getNbThreads());
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
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
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
		Log.info("Found " + nb + " files");
	}

	private static void incDone(boolean error) {
		Splash.incDone(error);
		ProgressBar.incDone(error);
	}

	private static void incTotal(int nb) {
		Splash.incTotal(nb);
		ProgressBar.incTotal(nb);
	}

	private static void manageFileInternal(File f, Option option, ErrorStatus error)
			throws IOException, InterruptedException {
		Log.info("Working on " + f.getAbsolutePath());
		if (OptionFlags.getInstance().isExtractFromMetadata()) {
			System.out.println("------------------------");
			System.out.println(f);
			// new Metadata().readAndDisplayMetadata(f);
			System.out.println();
			error.goOk();
			final String data = new MetadataTag(f, "plantuml").getData();
			// File file = new File("tmp.txt");
			// PrintWriter pw = new PrintWriter(file, "UTF-8");
			// pw.println(NastyEncoder.fromISO_8859_1(data));
			// pw.close();

			System.out.println(data);
			System.out.println("------------------------");
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
			Log.error("Warning: no image in " + f.getCanonicalPath());
			rpt.printInfo(System.err, null);
			// error.goNoData();
			return;
		}
		for (BlockUml s : sourceFileReader.getBlocks()) {
			rpt.printInfo(System.err, s.getDiagram());
		}

		hasErrors(f, result, error);
	}

	private static void extractPreproc(Option option, final ISourceFileReader sourceFileReader) throws IOException {
		final String charset = option.getCharset();
		for (BlockUml blockUml : sourceFileReader.getBlocks()) {
			final SuggestedFile suggested = ((SourceFileReaderAbstract) sourceFileReader).getSuggestedFile(blockUml)
					.withPreprocFormat();
			final File file = suggested.getFile(0);
			Log.info("Export preprocessing source to " + file.getAbsolutePath());
			final PrintWriter pw = charset == null ? new PrintWriter(file) : new PrintWriter(file, charset);
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
			pw.close();
		}
	}

	private static void hasErrors(File f, final List<GeneratedImage> list, ErrorStatus error) throws IOException {
		if (list.size() == 0) {
			// error.goNoData();
			return;
		}
		for (GeneratedImage i : list) {
			final int lineError = i.lineErrorRaw();
			if (lineError != -1) {
				Log.error("Error line " + lineError + " in file: " + f.getCanonicalPath());
				error.goWithError();
				return;
			}
		}
		error.goOk();
	}

}
