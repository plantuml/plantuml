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
package net.sourceforge.plantuml.ant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;

import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SecurityUtils;

public class CheckZipTask extends Task {
	// ::remove folder when __CORE__

	private String zipfile = null;
	private List<FileSet> filesets = new ArrayList<>();
	private List<FileList> filelists = new ArrayList<>();

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

		myLog("Check " + zipfile);

		try {
			loadZipFile(new SFile(zipfile));
			for (FileList fileList : filelists) {
				manageFileList(fileList);
			}
		} catch (IOException e) {
			Logme.error(e);
			throw new BuildException(e.toString());
		}
	}

	private void manageFileList(FileList fileList) {
		boolean error = false;
		final String[] srcFiles = fileList.getFiles(getProject());
		for (String s : srcFiles) {
			if (isPresentInFile(s) == false) {
				myLog("Missing " + s);
				error = true;
			}
		}
		if (error) {
			throw new BuildException("Some entries are missing in the zipfile");
		}
	}

	private boolean isPresentInFile(String s) {
		return entries.contains(s);
	}

	private final List<String> entries = new ArrayList<>();

	private void loadZipFile(SFile file) throws IOException {

		this.entries.clear();
		final InputStream tmp = file.openFile();
		if (tmp == null) {
			throw new FileNotFoundException();
		}
		try (final PrintWriter pw = SecurityUtils.createPrintWriter("tmp.txt");
				final ZipInputStream zis = new ZipInputStream(tmp);) {
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				final String fileName = ze.getName();
				this.entries.add(fileName);
				if (fileName.endsWith("/") == false) {
					pw.println("<file name=\"" + fileName + "\" />");
				}
				ze = zis.getNextEntry();
			}
		}
	}

	private synchronized void myLog(String s) {
		this.log(s);
	}

	public void setZipfile(String s) {
		this.zipfile = s;
	}

}
