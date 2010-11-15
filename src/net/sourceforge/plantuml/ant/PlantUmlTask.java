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
 * Revision $Revision: 5354 $
 *
 */
package net.sourceforge.plantuml.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.DirWatcher;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.Option;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.preproc.Defines;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;

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

		this.log("Starting PlantUML");

		try {
			if (dir != null) {
				processingSingleDirectory(new File(dir));
			}
			for (FileSet fileSet : filesets) {
				manageFileSet(fileSet);
			}
			for (FileList fileList : filelists) {
				manageFileList(fileList);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new BuildException(e.toString());
		}

	}

	private void manageFileList(FileList fl) throws IOException, InterruptedException {
		final File fromDir = fl.getDir(getProject());

		final String[] srcFiles = fl.getFiles(getProject());

		for (String src : srcFiles) {
			final File f = new File(fromDir, src);
			processingSingleFile(f);
		}
	}

	private void manageFileSet(FileSet fs) throws IOException, InterruptedException {
		final DirectoryScanner ds = fs.getDirectoryScanner(getProject());
		final File fromDir = fs.getDir(getProject());

		final String[] srcFiles = ds.getIncludedFiles();
		final String[] srcDirs = ds.getIncludedDirectories();

		for (String src : srcFiles) {
			final File f = new File(fromDir, src);
			processingSingleFile(f);
		}

		for (String src : srcDirs) {
			final File dir = new File(fromDir, src);
			processingSingleDirectory(dir);
		}

	}

	private void processingSingleFile(final File f) throws IOException, InterruptedException {
		this.log("Processing " + f.getAbsolutePath());
		final SourceFileReader sourceFileReader = new SourceFileReader(new Defines(), f, option.getOutputDir(), option
				.getConfig(), option.getCharset(), option.getFileFormat());
		final Collection<GeneratedImage> result = sourceFileReader.getGeneratedImages();
		for (GeneratedImage g : result) {
			this.log(g + " " + g.getDescription());
		}
	}

	private void processingSingleDirectory(File f) throws IOException, InterruptedException {
		if (f.exists() == false) {
			final String s = "The file " + f.getAbsolutePath() + " does not exists.";
			this.log(s);
			throw new BuildException(s);
		}
		final DirWatcher dirWatcher = new DirWatcher(f, option, Option.getPattern());
		final Collection<GeneratedImage> result = dirWatcher.buildCreatedFiles();
		for (GeneratedImage g : result) {
			this.log(g + " " + g.getDescription());
		}
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
		if ("true".equalsIgnoreCase(s)) {
			OptionFlags.getInstance().setKeepTmpFiles(true);
		}
	}

	public void setVerbose(String s) {
		if ("true".equalsIgnoreCase(s)) {
			OptionFlags.getInstance().setVerbose(true);
		}
	}

	public void setFormat(String s) {
		if ("eps".equalsIgnoreCase(s)) {
			option.setFileFormat(FileFormat.EPS);
		}
		if ("svg".equalsIgnoreCase(s)) {
			option.setFileFormat(FileFormat.SVG);
		}
		if ("txt".equalsIgnoreCase(s)) {
			option.setFileFormat(FileFormat.ATXT);
		}
		if ("utxt".equalsIgnoreCase(s)) {
			option.setFileFormat(FileFormat.UTXT);
		}
	}

	public void setGraphvizDot(String s) {
		OptionFlags.getInstance().setDotExecutable(s);
	}

	public void setForcegd(String s) {
		if ("true".equalsIgnoreCase(s)) {
			OptionFlags.getInstance().setForceGd(true);
		}
	}

	public void setForcecairo(String s) {
		if ("true".equalsIgnoreCase(s)) {
			OptionFlags.getInstance().setForceCairo(true);
		}
	}

}
