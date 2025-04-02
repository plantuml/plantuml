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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import net.sourceforge.plantuml.abel.DisplayPositioned;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.crash.CrashReportHandler;
import net.sourceforge.plantuml.dot.UnparsableGraphvizException;
import net.sourceforge.plantuml.file.SuggestedFile;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.pdf.PdfConverter;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.NoStyleAvailableException;
import net.sourceforge.plantuml.svek.EmptySvgException;
import net.sourceforge.plantuml.version.Version;

public abstract class UmlDiagram extends TitledDiagram implements Diagram, Annotated, WithSprite {
	// ::remove file when __HAXE__

	private boolean rotation;

	private int minwidth = Integer.MAX_VALUE;

//	public UmlDiagram(ThemeStyle style, UmlSource source, UmlDiagramType type) {
//		super(style, source, type);
//	}

	public UmlDiagram(UmlSource source, UmlDiagramType type, Previous previous, PreprocessingArtifact preprocessing) {
		super(source, type, previous, preprocessing);
	}

	final public int getMinwidth() {
		return minwidth;
	}

	final public void setMinwidth(int minwidth) {
		this.minwidth = minwidth;
	}

	final public boolean isRotation() {
		return rotation;
	}

	final public void setRotation(boolean rotation) {
		this.rotation = rotation;
	}

	public final DisplayPositioned getFooterOrHeaderTeoz(FontParam param) {
		if (param == FontParam.FOOTER)
			return getFooter();

		if (param == FontParam.HEADER)
			return getHeader();

		throw new IllegalArgumentException();
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		fileFormatOption = fileFormatOption.withTikzFontDistortion(getSkinParam().getTikzFontDistortion());
		fileFormatOption.getTikzFontDistortion().updateFromPragma(getPragma());

		// ::comment when __CORE__
		if (fileFormatOption.getFileFormat() == FileFormat.PDF)
			return exportDiagramInternalPdf(os, index);
		// ::done

		try {
			final ImageData imageData = exportDiagramInternal(os, index, fileFormatOption);
			this.lastInfo = new XDimension2D(imageData.getWidth(), imageData.getHeight());
			return imageData;
		} catch (NoStyleAvailableException e) {
			Logme.error(e);
			final CrashReportHandler report = new CrashReportHandler(null, getMetadata(), getFlashData());

			report.add("There is an issue with your plantuml.jar file:");
			report.add("We cannot load any style from it!");

			report.checkOldVersionWarning();
			report.addProperties();
			report.addEmptyLine();

			report.exportDiagramError(fileFormatOption, seed(), os);
			return ImageDataSimple.error(e);
		} catch (UnparsableGraphvizException e) {
			Logme.error(e);
			final CrashReportHandler report = new CrashReportHandler(e.getCause(), getMetadata(), getFlashData());

			report.anErrorHasOccured(e.getCause(), getFlashData());
			report.add("PlantUML (" + Version.versionString() + ") cannot parse result from dot/GraphViz.");
			if (e.getCause() instanceof EmptySvgException)
				report.add("Because dot/GraphViz returns an empty string.");

			if (e.getGraphvizVersion() != null) {
				report.addEmptyLine();
				report.add("GraphViz version used : " + e.getGraphvizVersion());
			}
			report.pleaseCheckYourGraphVizVersion();
			report.addProperties();
			report.addEmptyLine();
			report.thisMayBeCaused();
			report.addEmptyLine();
			report.youShouldSendThisDiagram();
			report.addEmptyLine();

			report.exportDiagramError(fileFormatOption, seed(), os);
			return ImageDataSimple.error(e);
		} catch (Throwable e) {
			Logme.error(e);
			final CrashReportHandler report = new CrashReportHandler(e, getMetadata(), getFlashData());
			report.anErrorHasOccured(e, getFlashData());
			report.addProperties();
			report.addEmptyLine();
			report.youShouldSendThisDiagram();
			report.addEmptyLine();
			report.exportDiagramError(fileFormatOption, seed(), os);
			return ImageDataSimple.error(e);
		}
	}

	// ::comment when __CORE__
	private ImageData exportDiagramInternalPdf(OutputStream os, int index) throws IOException {
		final File svg = FileUtils.createTempFileLegacy("pdf", ".svf");
		final File pdfFile = FileUtils.createTempFileLegacy("pdf", ".pdf");
		final ImageData result;
		try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(svg))) {
			result = exportDiagram(fos, index, new FileFormatOption(FileFormat.SVG));
		}
		PdfConverter.convert(svg, pdfFile);
		FileUtils.copyToStream(pdfFile, os);
		return result;
	}

	final protected void exportCmap(SuggestedFile suggestedFile, int index, final ImageData cmapdata)
			throws FileNotFoundException {
		final String name = changeName(suggestedFile.getFile(index).getAbsolutePath());
		final SFile cmapFile = new SFile(name);
		try (PrintWriter pw = cmapFile.createPrintWriter()) {
			if (PSystemUtils.canFileBeWritten(cmapFile) == false)
				return;

			pw.print(cmapdata.getCMapData(cmapFile.getName().substring(0, cmapFile.getName().length() - 6)));
		}
	}

	static String changeName(String name) {
		return name.replaceAll("(?i)\\.\\w{3}$", ".cmapx");
	}
	// ::done

	private XDimension2D lastInfo;

	protected abstract ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException;

	@Override
	public String getWarningOrError() {
		if (lastInfo == null)
			return null;

		final double actualWidth = lastInfo.getWidth();
		if (actualWidth == 0)
			return null;

		final String value = getSkinParam().getValue("widthwarning");
		if (value == null)
			return null;

		if (value.matches("\\d+") == false)
			return null;

		final int widthwarning = Integer.parseInt(value);
		if (actualWidth > widthwarning)
			return "The image is " + ((int) actualWidth) + " pixel width. (Warning limit is " + widthwarning + ")";

		return null;
	}

	public void setHideEmptyDescription(boolean hideEmptyDescription) {
	}

	public Previous getPrevious() {
		return Previous.createFrom(getSkinParam().values());
	}

}
