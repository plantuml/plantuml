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

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.file.SuggestedFile;
import net.sourceforge.plantuml.html.CucaDiagramHtmlMaker;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.png.PngSplitter;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.SplitParam;
import net.sourceforge.plantuml.utils.Log;

public class PSystemUtils {
	// ::remove file when __CORE__
	// ::remove file when __HAXE__

	public static List<FileImageData> exportDiagrams(Diagram system, SuggestedFile suggested,
			FileFormatOption fileFormatOption) throws IOException {
		return exportDiagrams(system, suggested, fileFormatOption, false);
	}

	public static List<FileImageData> exportDiagrams(Diagram system, SuggestedFile suggestedFile,
			FileFormatOption fileFormatOption, boolean checkMetadata) throws IOException {

		// ::comment when __CORE__
		final SFile existingFile = suggestedFile.getFile(0);
		if (checkMetadata && fileFormatOption.getFileFormat().doesSupportMetadata() && existingFile.exists()) {
			// && system.getNbImages() == 1) {
			final boolean sameMetadata = fileFormatOption.getFileFormat().equalsMetadata(system.getMetadata(),
					existingFile);
			if (sameMetadata) {
				Log.info(() -> "Skipping " + existingFile.getPrintablePath() + " because metadata has not changed.");
				return Arrays.asList(new FileImageData(existingFile, null));
			}
		}
		// ::done

		if (system instanceof NewpagedDiagram)
			return exportDiagramsNewpaged((NewpagedDiagram) system, suggestedFile, fileFormatOption);

		if (system instanceof SequenceDiagram)
			return exportDiagramsSequence((SequenceDiagram) system, suggestedFile, fileFormatOption);

		// ::comment when __CORE__
		if (system instanceof CucaDiagram && fileFormatOption.getFileFormat() == FileFormat.HTML)
			return createFilesHtml((CucaDiagram) system, suggestedFile);
		// ::done

		return exportDiagramsDefault(system, suggestedFile, fileFormatOption);
	}

	private static List<FileImageData> exportDiagramsNewpaged(NewpagedDiagram system, SuggestedFile suggestedFile,
			FileFormatOption fileFormat) throws IOException {
		final List<FileImageData> result = new ArrayList<>();
		final int nbImages = system.getNbImages();
		for (int i = 0; i < nbImages; i++) {

			final SFile f = suggestedFile.getFile(i);
			if (canFileBeWritten(f) == false)
				return result;

			final OutputStream fos = f.createBufferedOutputStream();
			ImageData cmap = null;
			try {
				system.exportDiagram(fos, i, fileFormat);
			} finally {
				fos.close();
			}
			// if (system.hasUrl() && cmap != null && cmap.containsCMapData()) {
			// system.exportCmap(suggestedFile, cmap);
			// }
			Log.info(() -> "File size : " + f.length());
			result.add(new FileImageData(f, cmap));
		}
		return result;
	}

	public static boolean canFileBeWritten(SFile f) {
		Log.info(() -> "Creating file: " + f.getAbsolutePath());
		if (f.exists() && f.canWrite() == false) {
			if (OptionFlags.getInstance().isOverwrite()) {
				Log.info(() -> "Overwrite " + f);
				f.setWritable(true);
				f.delete();
				return true;
			}
			Log.error("Cannot write to file " + f.getAbsolutePath());
			return false;
		}
		return true;
	}

	private static List<FileImageData> exportDiagramsSequence(SequenceDiagram system, SuggestedFile suggestedFile,
			FileFormatOption fileFormat) throws IOException {
		final List<FileImageData> result = new ArrayList<>();
		final int nbImages = system.getNbImages();
		for (int i = 0; i < nbImages; i++) {

			final SFile f = suggestedFile.getFile(i);
			if (PSystemUtils.canFileBeWritten(suggestedFile.getFile(i)) == false)
				return result;

			final OutputStream fos = f.createBufferedOutputStream();
			ImageData cmap = null;
			try {
				cmap = system.exportDiagram(fos, i, fileFormat);
			} finally {
				fos.close();
			}
			// ::comment when __CORE__
			if (cmap != null && cmap.containsCMapData())
				system.exportCmap(suggestedFile, i, cmap);

			Log.info(() -> "File size : " + f.length());
			// ::done
			result.add(new FileImageData(f, cmap));
		}
		return result;
	}

	private static List<FileImageData> createFilesHtml(CucaDiagram system, SuggestedFile suggestedFile)
			throws IOException {
		final String name = suggestedFile.getName();
		final int idx = name.lastIndexOf('.');
		final SFile dir = suggestedFile.getParentFile().file(name.substring(0, idx));
		final CucaDiagramHtmlMaker maker = new CucaDiagramHtmlMaker(system, dir);
		return maker.create();
	}

	private static List<FileImageData> splitPng(TitledDiagram diagram, SuggestedFile pngFile, ImageData imageData,
			FileFormatOption fileFormatOption) throws IOException {

		final List<SFile> files = new PngSplitter(fileFormatOption.getColorMapper(), pngFile,
				diagram.getSplitPagesHorizontal(), diagram.getSplitPagesVertical(),
				fileFormatOption.isWithMetadata() ? diagram.getMetadata() : null, diagram.getSkinParam().getDpi(),
				diagram instanceof GanttDiagram ? new SplitParam(HColors.BLACK, null, 5) // for backwards compatibility
						: diagram.getSkinParam().getSplitParam())
				.getFiles();

		final List<FileImageData> result = new ArrayList<>();
		for (SFile f : files)
			result.add(new FileImageData(f, imageData));

		return result;
	}

	private static List<FileImageData> exportDiagramsDefault(Diagram system, SuggestedFile suggestedFile,
			FileFormatOption fileFormatOption) throws IOException {

		final SFile outputFile = suggestedFile.getFile(0);

		if (outputFile.isDirectory())
			throw new IllegalArgumentException("File is a directory " + suggestedFile);

		if (!canFileBeWritten(outputFile))
			return emptyList();

		final ImageData imageData;

		try (OutputStream os = outputFile.createBufferedOutputStream()) {
			imageData = system.exportDiagram(os, 0, fileFormatOption);
		}

		if (imageData == null)
			return emptyList();

		// ::comment when __CORE__
		if (imageData.containsCMapData() && system instanceof UmlDiagram)
			((UmlDiagram) system).exportCmap(suggestedFile, 0, imageData);
		// ::done

		if (system instanceof TitledDiagram && fileFormatOption.getFileFormat() == FileFormat.PNG)
			return splitPng((TitledDiagram) system, suggestedFile, imageData, fileFormatOption);

		return singletonList(new FileImageData(outputFile, imageData));
	}
}
