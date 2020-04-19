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
package net.sourceforge.plantuml.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.Option;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.Splash;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.stats.StatsUtils;

// <?xml version="1.0"?>
//
// <project name="OwnTaskExample" default="main" basedir=".">
// <taskdef name="plot" classname="plot.PlotTask" classpath="build"/>
//
// <target name="main">
// <mytask message="Hello World! MyVeryOwnTask works!"/>
// </target>
// </project>

// Carriage Return in UTF-8 XML: &#13;
// Line Feed in UTF-8 XML: &#10;
public class PlantUmlTask extends Task {

	private String dir = null;
	private final Option option = new Option();
	private List<FileSet> filesets = new ArrayList<FileSet>();
	private List<FileList> filelists = new ArrayList<FileList>();
	private AtomicInteger nbFiles = new AtomicInteger(0);
	private ExecutorService executorService;

	/**
	 * Add a set of files to touch
	 */
	public void addFileset(FileSet set) {
		filesets.add(set);
	}

	/**
	 * Add a filelist to touch
	 */
	public void addFilelist(FileList list) {
		filelists.add(list);
	}

	// The method executing the task
	@Override
	public void execute() throws BuildException {

		if (option.isSplash()) {
			Splash.createSplash();
		}

		this.log("Starting PlantUML");

		try {
			if (dir != null) {
				final File error = processingSingleDirectory(new File(dir));
				eventuallyFailfast(error);
			}
			for (FileSet fileSet : filesets) {
				final File error = manageFileSet(fileSet);
				eventuallyFailfast(error);
			}
			for (FileList fileList : filelists) {
				final File error = manageFileList(fileList);
				eventuallyFailfast(error);
			}
			if (executorService != null) {
				executorService.shutdown();
				executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				if (option.isSplash()) {
					Splash.disposeSplash();
				}
			}
			this.log("Nb images generated: " + nbFiles.get());
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new BuildException(e.toString());
		}

	}

	private void eventuallyFailfast(final File error) throws IOException {
		if (error != null && option.isFailfastOrFailfast2()) {
			this.log("Error in file " + error.getCanonicalPath());
			throw new BuildException("Error in file " + error.getCanonicalPath());
		}
	}

	private File manageFileList(FileList fl) throws IOException, InterruptedException {
		final File fromDir = fl.getDir(getProject());

		final String[] srcFiles = fl.getFiles(getProject());

		for (String src : srcFiles) {
			final File f = new File(fromDir, src);
			final boolean error = processingSingleFile(f);
			if (error) {
				return f;
			}
		}
		return null;
	}

	private File manageFileSet(FileSet fs) throws IOException, InterruptedException {
		final DirectoryScanner ds = fs.getDirectoryScanner(getProject());
		final File fromDir = fs.getDir(getProject());

		final String[] srcFiles = ds.getIncludedFiles();
		final String[] srcDirs = ds.getIncludedDirectories();

		for (String src : srcFiles) {
			final File f = new File(fromDir, src);
			final boolean error = processingSingleFile(f);
			if (error) {
				return f;
			}
		}

		for (String src : srcDirs) {
			final File dir = new File(fromDir, src);
			final File errorFile = processingSingleDirectory(dir);
			if (errorFile != null) {
				return errorFile;
			}
		}
		return null;

	}

	private boolean processingSingleFile(final File f) throws IOException, InterruptedException {
		if (OptionFlags.getInstance().isVerbose()) {
			this.log("Processing " + f.getAbsolutePath());
		}
		final SourceFileReader sourceFileReader = new SourceFileReader(Defines.createWithFileName(f), f,
				option.getOutputDir(), option.getConfig(), option.getCharset(), option.getFileFormatOption());

		if (option.isCheckOnly()) {
			return sourceFileReader.hasError();
		}
		if (executorService == null) {
			return doFile(f, sourceFileReader);
		}

		Splash.incTotal(1);
		executorService.submit(new Callable<Boolean>() {
			public Boolean call() throws Exception {
				return doFile(f, sourceFileReader);
			}
		});

		return false;
	}

	private boolean doFile(final File f, final SourceFileReader sourceFileReader) throws IOException,
			InterruptedException {
		final Collection<GeneratedImage> result = sourceFileReader.getGeneratedImages();
		boolean error = false;
		for (GeneratedImage g : result) {
			if (OptionFlags.getInstance().isVerbose()) {
				myLog(g + " " + g.getDescription());
			}
			nbFiles.addAndGet(1);
			if (g.lineErrorRaw() != -1) {
				error = true;
			}
		}
		Splash.incDone(error);
		if (error) {
			myLog("Error: " + f.getCanonicalPath());
		}
		if (error && option.isFailfastOrFailfast2()) {
			return true;
		}
		return false;
	}

	private synchronized void myLog(String s) {
		this.log(s);
	}

	private File processingSingleDirectory(File dir) throws IOException, InterruptedException {
		if (dir.exists() == false) {
			final String s = "The file " + dir.getAbsolutePath() + " does not exists.";
			this.log(s);
			throw new BuildException(s);
		}
		for (File f : dir.listFiles()) {
			if (f.isFile() == false) {
				continue;
			}
			if (fileToProcess(f.getName()) == false) {
				continue;
			}
			final boolean error = processingSingleFile(f);
			if (error) {
				return f;
			}
		}
		return null;
	}

	private boolean fileToProcess(String name) {
		return name.matches(Option.getPattern());
	}

	public void setDir(String s) {
		this.dir = s;
	}

	public void setOutput(String s) {
		option.setOutputDir(new File(s));
	}

	public void setCharset(String s) {
		option.setCharset(s);
	}

	public void setConfig(String s) {
		try {
			option.initConfig(s);
		} catch (IOException e) {
			log("Error reading " + s);
		}
	}

	public void setKeepTmpFiles(String s) {
		// Deprecated
	}

	public void setDebugSvek(String s) {
		if ("true".equalsIgnoreCase(s)) {
			option.setDebugSvek(true);
		}
	}

	public void setVerbose(String s) {
		if ("true".equalsIgnoreCase(s)) {
			OptionFlags.getInstance().setVerbose(true);
		}
	}

	public void setFormat(String s) {
		if ("scxml".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.SCXML));
		}
		if ("xmi".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.XMI_STANDARD));
		}
		if ("xmi:argo".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.XMI_ARGO));
		}
		if ("xmi:start".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.XMI_STAR));
		}
		if ("eps".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.EPS));
		}
		if ("braille".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.BRAILLE_PNG));
		}
		if ("pdf".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.PDF));
		}
		if ("latex".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.LATEX));
		}
		if ("latex:nopreamble".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.LATEX_NO_PREAMBLE));
		}
		if ("eps:text".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.EPS_TEXT));
		}
		if ("svg".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.SVG));
		}
		if ("txt".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.ATXT));
		}
		if ("utxt".equalsIgnoreCase(s)) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.UTXT));
		}
	}

	public void setGraphvizDot(String s) {
		GraphvizUtils.setDotExecutable(s);
	}

	public void setNbThread(String s) {
		if (s != null && s.matches("\\d+")) {
			option.setNbThreads(Integer.parseInt(s));
			final int nbThreads = option.getNbThreads();
			this.executorService = Executors.newFixedThreadPool(nbThreads);
		}
		if ("auto".equalsIgnoreCase(s)) {
			option.setNbThreads(Option.defaultNbThreads());
			final int nbThreads = option.getNbThreads();
			this.executorService = Executors.newFixedThreadPool(nbThreads);
		}
	}

	public void setNbThreads(String s) {
		setNbThread(s);
	}

//	public void setSuggestEngine(String s) {
//		OptionFlags.getInstance().setUseSuggestEngine(
//				"true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s));
//	}

	public void setFailFast(String s) {
		final boolean flag = "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s);
		option.setFailfast(flag);
	}

	public void setFailFast2(String s) {
		final boolean flag = "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s);
		option.setFailfast2(flag);
	}

	public void setCheckOnly(String s) {
		final boolean flag = "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s);
		option.setCheckOnly(flag);
	}

	public void setOverwrite(String s) {
		final boolean flag = "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s);
		OptionFlags.getInstance().setOverwrite(flag);
	}

	public void setFileSeparator(String s) {
		OptionFlags.getInstance().setFileSeparator(s);
	}

	public void setHtmlStats(String s) {
		final boolean flag = "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s);
		StatsUtils.setHtmlStats(flag);
	}

	public void setXmlStats(String s) {
		final boolean flag = "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s);
		StatsUtils.setXmlStats(flag);
	}

	public void setRealTimeStats(String s) {
		final boolean flag = "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s);
		StatsUtils.setRealTimeStats(flag);
	}

	public void setEnableStats(String s) {
		final boolean flag = "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s);
		OptionFlags.getInstance().setEnableStats(flag);
	}

	public void setSplash(String s) {
		final boolean flag = "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s);
		option.setSplash(flag);
	}

}
