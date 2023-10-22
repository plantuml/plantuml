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

import static net.atmp.ImageBuilder.plainImageBuilder;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.DisplaySection;
import net.sourceforge.plantuml.dot.UnparsableGraphvizException;
import net.sourceforge.plantuml.file.SuggestedFile;
import net.sourceforge.plantuml.flashcode.FlashCodeFactory;
import net.sourceforge.plantuml.flashcode.FlashCodeUtils;
import net.sourceforge.plantuml.fun.IconLoader;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.geom.GraphicPosition;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.pdf.PdfConverter;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.NoStyleAvailableException;
import net.sourceforge.plantuml.svek.EmptySvgException;
import net.sourceforge.plantuml.svek.GraphvizCrash;
import net.sourceforge.plantuml.utils.Log;
import net.sourceforge.plantuml.version.Version;

public abstract class UmlDiagram extends TitledDiagram implements Diagram, Annotated, WithSprite {
	// ::remove file when __HAXE__

	private boolean rotation;

	private int minwidth = Integer.MAX_VALUE;

//	public UmlDiagram(ThemeStyle style, UmlSource source, UmlDiagramType type) {
//		super(style, source, type);
//	}

	public UmlDiagram(UmlSource source, UmlDiagramType type, Map<String, String> orig) {
		super(source, type, orig);
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

	public final DisplaySection getFooterOrHeaderTeoz(FontParam param) {
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

		// ::comment when __CORE__
		if (fileFormatOption.getFileFormat() == FileFormat.PDF)
			return exportDiagramInternalPdf(os, index);
		// ::done

		try {
			final ImageData imageData = exportDiagramInternal(os, index, fileFormatOption);
			this.lastInfo = new XDimension2D(imageData.getWidth(), imageData.getHeight());
			return imageData;
		} catch (NoStyleAvailableException e) {
			// Logme.error(e);
			exportDiagramError(os, e, fileFormatOption, null);
		} catch (UnparsableGraphvizException e) {
			Logme.error(e);
			exportDiagramError(os, e.getCause(), fileFormatOption, e.getGraphvizVersion());
		} catch (Throwable e) {
			// Logme.error(e);
			exportDiagramError(os, e, fileFormatOption, null);
		}
		return ImageDataSimple.error();
	}

	private void exportDiagramError(OutputStream os, Throwable exception, FileFormatOption fileFormat,
			String graphvizVersion) throws IOException {
		exportDiagramError(os, exception, fileFormat, seed(), getMetadata(), getFlashData(),
				getFailureText1(exception, graphvizVersion, getFlashData()));
	}

	public static void exportDiagramError(OutputStream os, Throwable exception, FileFormatOption fileFormat, long seed,
			String metadata, String flash, List<String> strings) throws IOException {

		// ::comment when __CORE__
		if (fileFormat.getFileFormat() == FileFormat.ATXT || fileFormat.getFileFormat() == FileFormat.UTXT) {
			exportDiagramErrorText(os, exception, strings);
			return;
		}
		// ::done

		strings.addAll(CommandExecutionResult.getStackTrace(exception));

		BufferedImage im2 = null;
		// ::comment when __CORE__
		if (flash != null) {
			final FlashCodeUtils utils = FlashCodeFactory.getFlashCodeUtils();
			try {
				im2 = utils.exportFlashcode(flash, Color.BLACK, Color.WHITE);
			} catch (Throwable e) {
				Log.error("Issue in flashcode generation " + e);
				// Logme.error(e);
			}
			if (im2 != null)
				GraphvizCrash.addDecodeHint(strings);
		}
		// ::done

		final BufferedImage im = im2;
		final TextBlock graphicStrings = GraphicStrings.createBlackOnWhite(strings, IconLoader.getRandom(),
				GraphicPosition.BACKGROUND_CORNER_TOP_RIGHT);

		final UDrawable drawable = (im == null) ? graphicStrings : new UDrawable() {
			public void drawU(UGraphic ug) {
				graphicStrings.drawU(ug);
				final double height = graphicStrings.calculateDimension(ug.getStringBounder()).getHeight();
				ug = ug.apply(UTranslate.dy(height));
				ug.draw(new UImage(new PixelImage(im, AffineTransformType.TYPE_NEAREST_NEIGHBOR)).scale(3));
			}
		};

		plainImageBuilder(drawable, fileFormat).metadata(metadata).seed(seed).write(os);
	}

	// ::comment when __CORE__
	private static void exportDiagramErrorText(OutputStream os, Throwable exception, List<String> strings) {
		final PrintWriter pw = SecurityUtils.createPrintWriter(os);
		exception.printStackTrace(pw);
		pw.println();
		pw.println();
		for (String s : strings) {
			s = s.replaceAll("\\</?\\w+?\\>", "");
			pw.println(s);
		}
		pw.flush();
	}
	// ::done

	public String getFlashData() {
		final UmlSource source = getSource();
		if (source == null)
			return "";

		return source.getPlainString("\n");
	}

	static private List<String> getFailureText1(Throwable exception, String graphvizVersion, String textDiagram) {
		final List<String> strings = GraphvizCrash.anErrorHasOccured(exception, textDiagram);
		strings.add("PlantUML (" + Version.versionString() + ") cannot parse result from dot/GraphViz.");
		if (exception instanceof EmptySvgException)
			strings.add("Because dot/GraphViz returns an empty string.");

		GraphvizCrash.checkOldVersionWarning(strings);
		if (graphvizVersion != null) {
			strings.add(" ");
			strings.add("GraphViz version used : " + graphvizVersion);
		}
		GraphvizCrash.pleaseGoTo(strings);
		GraphvizCrash.addProperties(strings);
		strings.add(" ");
		GraphvizCrash.thisMayBeCaused(strings);
		strings.add(" ");
		GraphvizCrash.youShouldSendThisDiagram(strings);
		strings.add(" ");
		return strings;
	}

	public static List<String> getFailureText2(Throwable exception, String textDiagram) {
		final List<String> strings = GraphvizCrash.anErrorHasOccured(exception, textDiagram);
		strings.add("PlantUML (" + Version.versionString() + ") has crashed.");
		GraphvizCrash.checkOldVersionWarning(strings);
		strings.add(" ");
		GraphvizCrash.youShouldSendThisDiagram(strings);
		strings.add(" ");
		return strings;
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

}
