/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.CMapData;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.ftile.EntityImageLegend;
import net.sourceforge.plantuml.api.ImageDataComplex;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramSimplifierActivity;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramSimplifierState;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorGradient;
import net.sourceforge.plantuml.graphic.HtmlColorSimple;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;
import net.sourceforge.plantuml.ugraphic.visio.UGraphicVdx;

public final class CucaDiagramFileMakerSvek implements CucaDiagramFileMaker {

	private final CucaDiagram diagram;
	private final List<BufferedImage> flashcodes;

	static private final StringBounder stringBounder;

	static {
		final EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		stringBounder = StringBounderUtils.asStringBounder(builder.getGraphics2D());
	}

	public CucaDiagramFileMakerSvek(CucaDiagram diagram, List<BufferedImage> flashcodes) throws IOException {
		this.diagram = diagram;
		this.flashcodes = flashcodes;
	}

	public ImageData createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {
		try {
			return createFileInternal(os, dotStrings, fileFormatOption);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	private CucaDiagramFileMakerSvek2 buildCucaDiagramFileMakerSvek2(DotMode dotMode) {
		final DotData dotData = new DotData(diagram.getEntityFactory().getRootGroup(), getOrderedLinks(), diagram
				.getLeafs().values(), diagram.getUmlDiagramType(), diagram.getSkinParam(), diagram.getRankdir(),
				diagram, diagram, diagram.getColorMapper(), diagram.getEntityFactory(),
				diagram.isHideEmptyDescriptionForState(), dotMode);
		final CucaDiagramFileMakerSvek2 svek2 = new CucaDiagramFileMakerSvek2(dotData, diagram.getEntityFactory(),
				false, diagram.getSource(), diagram.getPragma());
		return svek2;

	}

	private ImageData createFileInternal(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {
		if (diagram.getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			new CucaDiagramSimplifierActivity(diagram, dotStrings);
		} else if (diagram.getUmlDiagramType() == UmlDiagramType.STATE) {
			new CucaDiagramSimplifierState(diagram, dotStrings);
		}

		CucaDiagramFileMakerSvek2 svek2 = buildCucaDiagramFileMakerSvek2(DotMode.NORMAL);
		TextBlockBackcolored result = svek2.createFile(diagram.getDotStringSkek());
		if (result instanceof GraphvizCrash) {
			svek2 = buildCucaDiagramFileMakerSvek2(DotMode.NO_LEFT_RIGHT);
			result = svek2.createFile(diagram.getDotStringSkek());
		}
		result = addLegend(result);
		result = addTitle(result);
		result = addHeaderAndFooter(result);

		final Dimension2D dim = result.calculateDimension(stringBounder);

		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		Set<Url> allUrlEncountered = null;
		double scale = 0;
		if (fileFormat == FileFormat.PNG) {
			allUrlEncountered = new HashSet<Url>();
			scale = createPng(os, fileFormatOption, result, dim, allUrlEncountered, fileFormatOption.isWithMetadata());
		} else if (fileFormat == FileFormat.SVG) {
			createSvg(os, fileFormatOption, result, dim);
		} else if (fileFormat == FileFormat.VDX) {
			createVdx(os, fileFormatOption, result, dim);
		} else if (fileFormat == FileFormat.EPS) {
			createEps(os, fileFormatOption, result, dim);
		} else {
			throw new UnsupportedOperationException(fileFormat.toString());
		}

		double deltaX = 0;
		double deltaY = 0;
		if (result instanceof DecorateEntityImage) {
			deltaX += ((DecorateEntityImage) result).getDeltaX();
			deltaY += ((DecorateEntityImage) result).getDeltaY();
		}

		final Dimension2D finalDimension = Dimension2DDouble.delta(dim, deltaX, deltaY);

		CMapData cmap = null;
		if (diagram.hasUrl() && fileFormatOption.getFileFormat() == FileFormat.PNG) {
			cmap = CMapData.cmapString(allUrlEncountered, scale);
		}

		final String widthwarning = diagram.getSkinParam().getValue("widthwarning");
		if (widthwarning != null && widthwarning.matches("\\d+")) {
			this.warningOrError = svek2.getBibliotekon().getWarningOrError(Integer.parseInt(widthwarning));
		} else {
			this.warningOrError = null;
		}

		return new ImageDataComplex(finalDimension, cmap, getWarningOrError());
	}

	private List<Link> getOrderedLinks() {
		final List<Link> result = new ArrayList<Link>();
		for (Link l : diagram.getLinks()) {
			addLinkNew(result, l);
		}
		return result;
	}

	private void addLinkNew(List<Link> result, Link link) {
		for (int i = 0; i < result.size(); i++) {
			final Link other = result.get(i);
			if (other.sameConnections(link)) {
				while (i < result.size() && result.get(i).sameConnections(link)) {
					i++;
				}
				if (i == result.size()) {
					result.add(link);
				} else {
					result.add(i, link);
				}
				return;
			}
		}
		result.add(link);
	}

	private String warningOrError;

	private String getWarningOrError() {
		return warningOrError;
	}

	private TextBlockBackcolored addHeaderAndFooter(TextBlockBackcolored original) {
		final Display footer = diagram.getFooter();
		final Display header = diagram.getHeader();
		if (footer == null && header == null) {
			return original;
		}
		final TextBlock textFooter = footer == null ? null : TextBlockUtils.create(footer, new FontConfiguration(
				getFont(FontParam.FOOTER), getFontColor(FontParam.FOOTER, null)), diagram.getFooterAlignment(), diagram
				.getSkinParam());
		final TextBlock textHeader = header == null ? null : TextBlockUtils.create(header, new FontConfiguration(
				getFont(FontParam.HEADER), getFontColor(FontParam.HEADER, null)), diagram.getHeaderAlignment(), diagram
				.getSkinParam());

		return new DecorateEntityImage(original, textHeader, diagram.getHeaderAlignment(), textFooter,
				diagram.getFooterAlignment());
	}

	private TextBlockBackcolored addTitle(TextBlockBackcolored original) {
		final Display title = diagram.getTitle();
		if (title == null) {
			return original;
		}
		final TextBlock text = TextBlockUtils.create(title, new FontConfiguration(getFont(FontParam.TITLE),
				getFontColor(FontParam.TITLE, null)), HorizontalAlignment.CENTER, diagram.getSkinParam());

		return DecorateEntityImage.addTop(original, text, HorizontalAlignment.CENTER);
	}

	private TextBlockBackcolored addLegend(TextBlockBackcolored original) {
		final Display legend = diagram.getLegend();
		if (legend == null) {
			return original;
		}
		final TextBlock text = EntityImageLegend.create(legend, diagram.getSkinParam());

		return DecorateEntityImage.addBottom(original, text, diagram.getLegendAlignment());
	}

	private final UFont getFont(FontParam fontParam) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return skinParam.getFont(fontParam, null);
	}

	private final HtmlColor getFontColor(FontParam fontParam, String stereo) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return skinParam.getFontHtmlColor(fontParam, stereo);
	}

	private double createPng(OutputStream os, FileFormatOption fileFormatOption, final TextBlockBackcolored result,
			final Dimension2D dim, Set<Url> allUrlEncountered, boolean isWithMetadata) throws IOException {
		final double scale = getScale(fileFormatOption, dim);
		final UGraphicG2d ug = (UGraphicG2d) fileFormatOption.createUGraphic(diagram.getSkinParam().getColorMapper(),
				scale, dim, result.getBackcolor(), diagram.isRotation());
		result.drawU(ug);

		PngIO.write(ug.getBufferedImage(), os, isWithMetadata ? diagram.getMetadata() : null,
				diagram.getDpi(fileFormatOption));
		allUrlEncountered.addAll(ug.getAllUrlsEncountered());
		return scale;

	}

	private double getScale(FileFormatOption fileFormatOption, final Dimension2D dim) {
		final double scale;
		final Scale diagScale = diagram.getScale();
		if (diagScale == null) {
			scale = diagram.getDpiFactor(fileFormatOption);
		} else {
			scale = diagScale.getScale(dim.getWidth(), dim.getHeight());
		}
		return scale;
	}

	private void createSvg(OutputStream os, FileFormatOption fileFormatOption, final TextBlockBackcolored result,
			final Dimension2D dim) throws IOException {
		final double scale = getScale(fileFormatOption, dim);

		Color backColor = Color.WHITE;
		if (result.getBackcolor() instanceof HtmlColorSimple) {
			backColor = diagram.getSkinParam().getColorMapper().getMappedColor(result.getBackcolor());
		}
		final UGraphicSvg ug;
		if (result.getBackcolor() instanceof HtmlColorGradient) {
			ug = new UGraphicSvg(diagram.getSkinParam().getColorMapper(), (HtmlColorGradient) result.getBackcolor(),
					false, scale);
		} else if (backColor == null || backColor.equals(Color.WHITE)) {
			ug = new UGraphicSvg(diagram.getSkinParam().getColorMapper(), false, scale);
		} else {
			ug = new UGraphicSvg(diagram.getSkinParam().getColorMapper(), StringUtils.getAsHtml(backColor), false,
					scale);
		}
		result.drawU(ug);
		ug.createXml(os);
	}

	private void createVdx(OutputStream os, FileFormatOption fileFormatOption, final TextBlockBackcolored result,
			final Dimension2D dim) throws IOException {

		final UGraphicVdx ug = new UGraphicVdx(diagram.getSkinParam().getColorMapper());
		result.drawU(ug);
		ug.createVsd(os);
	}

	private void createEps(OutputStream os, FileFormatOption fileFormatOption, final TextBlockBackcolored result,
			final Dimension2D dim) throws IOException {

		final UGraphicEps ug = new UGraphicEps(diagram.getSkinParam().getColorMapper(), EpsStrategy.getDefault2());

		result.drawU(ug);
		os.write(ug.getEPSCode().getBytes());
	}

}
