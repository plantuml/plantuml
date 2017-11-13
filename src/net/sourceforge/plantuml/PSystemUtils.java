/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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

import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.html.CucaDiagramHtmlMaker;
import net.sourceforge.plantuml.png.PngSplitter;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PSystemUtils {

	public static List<FileImageData> exportDiagrams(Diagram system, SuggestedFile suggestedFile,
			FileFormatOption fileFormatOption) throws IOException {
		if (system instanceof NewpagedDiagram) {
			return exportDiagramsNewpaged((NewpagedDiagram) system, suggestedFile, fileFormatOption);
		}
		if (system instanceof SequenceDiagram) {
			return exportDiagramsSequence((SequenceDiagram) system, suggestedFile, fileFormatOption);
		}
		if (system instanceof CucaDiagram) {
			return exportDiagramsCuca((CucaDiagram) system, suggestedFile, fileFormatOption);
		}
		if (system instanceof ActivityDiagram3) {
			return exportDiagramsActivityDiagram3((ActivityDiagram3) system, suggestedFile, fileFormatOption);
		}
		return exportDiagramsDefault(system, suggestedFile, fileFormatOption);
	}

	private static List<FileImageData> exportDiagramsNewpaged(NewpagedDiagram system, SuggestedFile suggestedFile,
			FileFormatOption fileFormat) throws IOException {
		final List<FileImageData> result = new ArrayList<FileImageData>();
		final int nbImages = system.getNbImages();
		for (int i = 0; i < nbImages; i++) {

			final File f = suggestedFile.getFile(i);
			if (!canFileBeWritten(f)) {
				return result;
			}
			ImageData cmap = null;

			try (final FileOutputStream fs = new FileOutputStream(f);
			     OutputStream fos = new BufferedOutputStream(fs)) {
				system.exportDiagram(fos, i, fileFormat);
			}
			Log.info("File size : " + f.length());
			result.add(new FileImageData(f, cmap));
		}
		return result;
	}

	public static boolean canFileBeWritten(final File f) {
		Log.info("Creating file: " + f);
		if (f.exists() && !f.canWrite()) {
			if (OptionFlags.getInstance().isOverwrite()) {
				Log.info("Overwrite " + f);
				f.setWritable(true);
				f.delete();
				return true;
			}
			Log.error("Cannot write to file " + f);
			return false;
		}
		return true;
	}

	private static List<FileImageData> exportDiagramsDefault(Diagram system, SuggestedFile suggestedFile,
	                                                         FileFormatOption fileFormat) throws IOException {
		if (suggestedFile.getFile(0).exists() && suggestedFile.getFile(0).isDirectory()) {
			throw new IllegalArgumentException("File is a directory " + suggestedFile);
		}
		if (!PSystemUtils.canFileBeWritten(suggestedFile.getFile(0))) {
			return Collections.emptyList();
		}
		ImageData imageData;
		try (final OutputStream os = new BufferedOutputStream(new FileOutputStream(suggestedFile.getFile(0)))) {
			imageData = system.exportDiagram(os, 0, fileFormat);
		}
		return Arrays.asList(new FileImageData(suggestedFile.getFile(0), imageData));
	}

	private static List<FileImageData> exportDiagramsActivityDiagram3(ActivityDiagram3 system,
                                                                      SuggestedFile suggestedFile, FileFormatOption fileFormat) throws IOException {
		if (suggestedFile.getFile(0).exists() && suggestedFile.getFile(0).isDirectory()) {
			throw new IllegalArgumentException("File is a directory " + suggestedFile);
		}
		ImageData cmap;
		ImageData imageData;
		if (!PSystemUtils.canFileBeWritten(suggestedFile.getFile(0))) {
			return Collections.emptyList();
		}
		try (OutputStream os = new BufferedOutputStream(new FileOutputStream(suggestedFile.getFile(0)))) {
			imageData = cmap = system.exportDiagram(os, 0, fileFormat);
		}
		if (cmap != null && cmap.containsCMapData()) {
			system.exportCmap(suggestedFile, 0, cmap);
		}
		return Arrays.asList(new FileImageData(suggestedFile.getFile(0), imageData));
	}

	private static List<FileImageData> exportDiagramsSequence(SequenceDiagram system, SuggestedFile suggestedFile,
			FileFormatOption fileFormat) throws IOException {
		final List<FileImageData> result = new ArrayList<FileImageData>();
		final int nbImages = system.getNbImages();
		for (int i = 0; i < nbImages; i++) {

			final File f = suggestedFile.getFile(i);
			if (!PSystemUtils.canFileBeWritten(suggestedFile.getFile(i))) {
				return result;
			}
			ImageData cmap;

			try (final FileOutputStream fs = new FileOutputStream(f);
			     OutputStream fos = new BufferedOutputStream(fs)) {
				cmap = system.exportDiagram(fos, i, fileFormat);
			}
			if (cmap != null && cmap.containsCMapData()) {
				system.exportCmap(suggestedFile, i, cmap);
			}
			Log.info("File size : " + f.length());
			result.add(new FileImageData(f, cmap));
		}
		return result;
	}

	private static List<FileImageData> exportDiagramsCuca(CucaDiagram system, SuggestedFile suggestedFile,
                                                          FileFormatOption fileFormat) throws IOException {
		if (suggestedFile.getFile(0).exists() && suggestedFile.getFile(0).isDirectory()) {
			throw new IllegalArgumentException("File is a directory " + suggestedFile);
		}

		if (fileFormat.getFileFormat() == FileFormat.HTML) {
			return createFilesHtml(system, suggestedFile);
		}

		if (!PSystemUtils.canFileBeWritten(suggestedFile.getFile(0))) {
			return Collections.emptyList();
		}
		ImageData cmap;
		try (final OutputStream os = new NamedOutputStream(suggestedFile.getFile(0))) {
			cmap = system.exportDiagram(os, 0, fileFormat);
		}
		List<File> result = Arrays.asList(suggestedFile.getFile(0));

		if (cmap != null && cmap.containsCMapData()) {
			system.exportCmap(suggestedFile, 0, cmap);
		}

		if (fileFormat.getFileFormat() == FileFormat.PNG) {
			result = new PngSplitter(suggestedFile, system.getHorizontalPages(), system.getVerticalPages(),
					system.getMetadata(), system.getDpi(fileFormat), fileFormat.isWithMetadata(), system.getSkinParam()
							.getSplitParam()).getFiles();
		}
		final List<FileImageData> result2 = new ArrayList<FileImageData>();
		for (File f : result) {
			result2.add(new FileImageData(f, cmap));
		}
		return result2;

	}

	private static List<FileImageData> createFilesHtml(CucaDiagram system, SuggestedFile suggestedFile)
			throws IOException {
		final String name = suggestedFile.getName();
		final int idx = name.lastIndexOf('.');
		final File dir = new File(suggestedFile.getParentFile(), name.substring(0, idx));
		final CucaDiagramHtmlMaker maker = new CucaDiagramHtmlMaker(system, dir);
		return maker.create();
	}

}
