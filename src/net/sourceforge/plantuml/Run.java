/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 19462 $
 *
 */
package net.sourceforge.plantuml;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;
import javax.swing.UIManager;

import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagramFactory;
import net.sourceforge.plantuml.ftp.FtpServer;
import net.sourceforge.plantuml.objectdiagram.ObjectDiagramFactory;
import net.sourceforge.plantuml.png.MetadataTag;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.statediagram.StateDiagramFactory;
import net.sourceforge.plantuml.swing.MainWindow2;
import net.sourceforge.plantuml.ugraphic.sprite.SpriteGrayLevel;
import net.sourceforge.plantuml.ugraphic.sprite.SpriteUtils;
import net.sourceforge.plantuml.version.Version;

public class Run {

	public static void main(String[] argsArray) throws IOException, InterruptedException {
		final long start = System.currentTimeMillis();
		final Option option = new Option(argsArray);
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

		boolean error = false;
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
		} else if (option.isPipe() || option.isSyntax()) {
			managePipe(option);
			forceQuit = true;
		} else if (option.isFailfast2()) {
			final long start2 = System.currentTimeMillis();
			option.setCheckOnly(true);
			error = manageAllFiles(option);
			option.setCheckOnly(false);
			if (option.isDuration()) {
				final double duration = (System.currentTimeMillis() - start2) / 1000.0;
				Log.error("Check Duration = " + duration + " seconds");
			}
			if (error == false) {
				error = manageAllFiles(option);
			}
			forceQuit = true;
		} else {
			error = manageAllFiles(option);
			forceQuit = true;
		}

		if (option.isDuration()) {
			final double duration = (System.currentTimeMillis() - start) / 1000.0;
			Log.error("Duration = " + duration + " seconds");
		}

		if (error) {
			Log.error("Some diagram description contains errors");
			System.exit(1);
		}

		if (forceQuit && OptionFlags.getInstance().isSystemExit()) {
			System.exit(0);
		}
	}

	private static void encodeSprite(List<String> result) throws IOException {
		SpriteGrayLevel level = SpriteGrayLevel.GRAY_16;
		boolean compressed = false;
		final File f;
		if (result.size() > 1 && result.get(0).matches("(4|8|16)z?")) {
			if (result.get(0).startsWith("8")) {
				level = SpriteGrayLevel.GRAY_8;
			}
			if (result.get(0).startsWith("4")) {
				level = SpriteGrayLevel.GRAY_4;
			}
			compressed = StringUtils.goLowerCase(result.get(0)).endsWith("z");
			f = new File(result.get(1));
		} else {
			f = new File(result.get(0));
		}
		final BufferedImage im = ImageIO.read(f);
		final String name = getSpriteName(f);
		final String s = compressed ? SpriteUtils.encodeCompressed(im, name, level) : SpriteUtils.encode(im, name,
				level);
		System.out.println(s);
	}

	private static String getSpriteName(File f) {
		final String s = getSpriteNameInternal(f);
		if (s.length() == 0) {
			return "test";
		}
		return s;
	}

	private static String getSpriteNameInternal(File f) {
		final StringBuilder sb = new StringBuilder();
		for (char c : f.getName().toCharArray()) {
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
		final FtpServer ftpServer = new FtpServer(ftpPort, option.getFileFormat());
		ftpServer.go();
	}

	public static void printFonts() {
		final Font fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		for (Font f : fonts) {
			System.out.println("f=" + f + "/" + f.getPSName() + "/" + f.getName() + "/" + f.getFontName() + "/"
					+ f.getFamily());
		}
		final String name[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (String n : name) {
			System.out.println("n=" + n);
		}

	}

	private static void managePattern() {
		printPattern(new SequenceDiagramFactory());
		printPattern(new ClassDiagramFactory());
		printPattern(new ActivityDiagramFactory());
		printPattern(new DescriptionDiagramFactory());
		// printPattern(new ComponentDiagramFactory());
		printPattern(new StateDiagramFactory());
		printPattern(new ObjectDiagramFactory());
	}

	private static void printPattern(UmlDiagramFactory factory) {
		System.out.println();
		System.out.println(factory.getClass().getSimpleName().replaceAll("Factory", ""));
		final List<String> descriptions = factory.getDescription();
		for (String s : descriptions) {
			System.out.println(s);
		}
	}

	private static void managePipe(Option option) throws IOException {
		final String charset = option.getCharset();
		final BufferedReader br;
		if (charset == null) {
			br = new BufferedReader(new InputStreamReader(System.in));
		} else {
			br = new BufferedReader(new InputStreamReader(System.in, charset));
		}
		managePipe(option, br, System.out);
	}

	public static void managePipe(Option option, final BufferedReader br, final PrintStream ps) throws IOException {
		final StringBuilder sb = new StringBuilder();
		String s = null;
		while ((s = br.readLine()) != null) {
			sb.append(s);
			sb.append("\n");
		}
		String source = sb.toString();
		if (source.contains("@startuml") == false) {
			source = "@startuml\n" + source + "\n@enduml";
		}
		final SourceStringReader sourceStringReader = new SourceStringReader(new Defines(), source, option.getConfig());

		if (option.isSyntax()) {
			final Diagram system = sourceStringReader.getBlocks().get(0).getDiagram();
			if (system instanceof UmlDiagram) {
				ps.println(((UmlDiagram) system).getUmlDiagramType().name());
				ps.println(system.getDescription());
			} else if (system instanceof PSystemError) {
				ps.println("ERROR");
				final PSystemError sys = (PSystemError) system;
				ps.println(sys.getHigherErrorPosition());
				for (ErrorUml er : sys.getErrorsUml()) {
					ps.println(er.getError());
				}
			} else {
				ps.println("OTHER");
				ps.println(system.getDescription());
			}
		} else if (option.isPipe()) {
			final String result = sourceStringReader.generateImage(ps, 0, option.getFileFormatOption());
			if ("(error)".equalsIgnoreCase(result)) {
				System.err.println("ERROR");
				final Diagram system = sourceStringReader.getBlocks().get(0).getDiagram();
				final PSystemError sys = (PSystemError) system;
				System.err.println(sys.getHigherErrorPosition());
				for (ErrorUml er : sys.getErrorsUml()) {
					System.err.println(er.getError());
				}
			}
		}
	}

	private static boolean manageAllFiles(Option option) throws IOException, InterruptedException {

		File lockFile = null;
		try {
			if (OptionFlags.getInstance().isWord()) {
				final File dir = new File(option.getResult().get(0));
				final File javaIsRunningFile = new File(dir, "javaisrunning.tmp");
				javaIsRunningFile.delete();
				lockFile = new File(dir, "javaumllock.tmp");
			}
			return processArgs(option);
		} finally {
			if (lockFile != null) {
				lockFile.delete();
			}
		}

	}

	private static boolean processArgs(Option option) throws IOException, InterruptedException {
		if (option.isDecodeurl() == false && option.getNbThreads() > 0 && option.isCheckOnly() == false
				&& OptionFlags.getInstance().isMetadata() == false) {
			return multithread(option);
		}
		boolean errorGlobal = false;
		for (String s : option.getResult()) {
			if (option.isDecodeurl()) {
				final Transcoder transcoder = TranscoderUtil.getDefaultTranscoder();
				System.out.println("@startuml");
				System.out.println(transcoder.decode(s));
				System.out.println("@enduml");
			} else {
				final FileGroup group = new FileGroup(s, option.getExcludes(), option);
				for (File f : group.getFiles()) {
					try {
						final boolean error = manageFileInternal(f, option);
						if (error) {
							errorGlobal = true;
						}
						if (error && option.isFailfastOrFailfast2()) {
							return true;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return errorGlobal;
	}

	private static boolean multithread(final Option option) throws InterruptedException {
		Log.info("Using several threads: " + option.getNbThreads());
		final ExecutorService executor = Executors.newFixedThreadPool(option.getNbThreads());
		final AtomicBoolean errors = new AtomicBoolean(false);
		for (String s : option.getResult()) {
			final FileGroup group = new FileGroup(s, option.getExcludes(), option);
			for (final File f : group.getFiles()) {
				executor.submit(new Runnable() {
					public void run() {
						if (errors.get() && option.isFailfastOrFailfast2()) {
							return;
						}
						try {
							final boolean error = manageFileInternal(f, option);
							if (error) {
								errors.set(true);
							}
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		return errors.get();
	}

	private static boolean manageFileInternal(File f, Option option) throws IOException, InterruptedException {
		if (OptionFlags.getInstance().isMetadata()) {
			System.out.println("------------------------");
			System.out.println(f);
			// new Metadata().readAndDisplayMetadata(f);
			System.out.println();
			System.out.println(new MetadataTag(f, "plantuml").getData());
			System.out.println("------------------------");
			return false;
		}
		final ISourceFileReader sourceFileReader;
		if (option.getOutputFile() == null) {
			sourceFileReader = new SourceFileReader(option.getDefaultDefines(), f, option.getOutputDir(),
					option.getConfig(), option.getCharset(), option.getFileFormatOption());
		} else {
			sourceFileReader = new SourceFileReader2(option.getDefaultDefines(), f, option.getOutputFile(),
					option.getConfig(), option.getCharset(), option.getFileFormatOption());
		}
		if (option.isComputeurl()) {
			final List<String> urls = sourceFileReader.getEncodedUrl();
			for (String s : urls) {
				System.out.println(s);
			}
			return false;
		}
		if (option.isCheckOnly()) {
			final boolean hasError = sourceFileReader.hasError();
			final List<GeneratedImage> result = sourceFileReader.getGeneratedImages();
			hasErrors(f, result);
			return hasError;
		}
		final List<GeneratedImage> result = sourceFileReader.getGeneratedImages();
		return hasErrors(f, result);
	}

	private static boolean hasErrors(File f, final List<GeneratedImage> list) throws IOException {
		boolean result = false;
		for (GeneratedImage i : list) {
			final int lineError = i.lineErrorRaw();
			if (lineError != -1) {
				Log.error("Error line " + lineError + " in file: " + f.getCanonicalPath());
				result = true;
			}
		}
		return result;
	}

}
