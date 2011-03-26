/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 6234 $
 *
 */
package net.sourceforge.plantuml;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import javax.swing.UIManager;

import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.command.AbstractUmlSystemCommandFactory;
import net.sourceforge.plantuml.componentdiagram.ComponentDiagramFactory;
import net.sourceforge.plantuml.objectdiagram.ObjectDiagramFactory;
import net.sourceforge.plantuml.png.MetadataTag;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.statediagram.StateDiagramFactory;
import net.sourceforge.plantuml.swing.MainWindow;
import net.sourceforge.plantuml.usecasediagram.UsecaseDiagramFactory;

public class Run {

	public static void main(String[] argsArray) throws IOException, InterruptedException {
		final Option option = new Option(argsArray);
		if (OptionFlags.getInstance().isVerbose()) {
			Log.info("GraphicsEnvironment.isHeadless() " + GraphicsEnvironment.isHeadless());
		}
		if (option.isPattern()) {
			managePattern();
		} else if (OptionFlags.getInstance().isGui()) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
			}
			new MainWindow(option);
		} else if (option.isPipe() || option.isSyntax()) {
			managePipe(option);
		} else {
			manageFiles(option);
		}
	}

	private static void managePattern() {
		printPattern(new SequenceDiagramFactory());
		printPattern(new ClassDiagramFactory());
		printPattern(new ActivityDiagramFactory());
		printPattern(new UsecaseDiagramFactory());
		printPattern(new ComponentDiagramFactory());
		printPattern(new StateDiagramFactory());
		printPattern(new ObjectDiagramFactory());
	}

	private static void printPattern(AbstractUmlSystemCommandFactory factory) {
		System.out.println();
		System.out.println(factory.getClass().getSimpleName().replaceAll("Factory", ""));
		for (String s : factory.getDescription()) {
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

	static void managePipe(Option option, final BufferedReader br, final PrintStream ps) throws IOException {
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
			try {
				final PSystem system = sourceStringReader.getBlocks().get(0).getSystem();
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
			} catch (InterruptedException e) {
				Log.error("InterruptedException " + e);
			}
		} else if (option.isPipe()) {
			final String result = sourceStringReader.generateImage(ps, 0, option.getFileFormatOption());
		}
	}

	private static void manageFile(File f, Option option) throws IOException, InterruptedException {
		if (OptionFlags.getInstance().isMetadata()) {
			System.out.println("------------------------");
			System.out.println(f);
			// new Metadata().readAndDisplayMetadata(f);
			System.out.println();
			System.out.println(new MetadataTag(f, "plantuml").getData());
			System.out.println("------------------------");
		} else {
			final SourceFileReader sourceFileReader = new SourceFileReader(option.getDefaultDefines(), f,
					option.getOutputDir(), option.getConfig(), option.getCharset(), option.getFileFormatOption());
			if (option.isComputeurl()) {
				final List<String> urls = sourceFileReader.getEncodedUrl();
				for (String s : urls) {
					System.out.println(s);
				}
			} else {
				sourceFileReader.getGeneratedImages();

			}
		}
	}

	private static void manageFiles(Option option) throws IOException, InterruptedException {

		File lockFile = null;
		try {
			if (OptionFlags.getInstance().isWord()) {
				final File dir = new File(option.getResult().get(0));
				final File javaIsRunningFile = new File(dir, "javaisrunning.tmp");
				javaIsRunningFile.delete();
				lockFile = new File(dir, "javaumllock.tmp");
			}
			processArgs(option);
		} finally {
			if (lockFile != null) {
				lockFile.delete();
			}
		}

	}

	private static void processArgs(Option option) throws IOException, InterruptedException {
		for (String s : option.getResult()) {
			if (option.isDecodeurl()) {
				final Transcoder transcoder = TranscoderUtil.getDefaultTranscoder();
				System.out.println("@startuml");
				System.out.println(transcoder.decode(s));
				System.out.println("@enduml");
			} else {
				final FileGroup group = new FileGroup(s, option.getExcludes(), option);
				for (File f : group.getFiles()) {
					manageFile(f, option);
				}
			}
		}
	}

}
