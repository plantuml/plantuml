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
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramSimplifier2;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.cucadiagram.dot.ICucaDiagramFileMaker;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;

public final class CucaDiagramFileMakerSvek implements ICucaDiagramFileMaker {

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

	public String createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {
		try {
			return createFileInternal(os, dotStrings, fileFormatOption);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;

	}

	private String createFileInternal(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {
		if (diagram.getUmlDiagramType() == UmlDiagramType.STATE
				|| diagram.getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			new CucaDiagramSimplifier2(diagram, dotStrings);
		}

		double deltaX = 0;
		double deltaY = 0;

		final DotData dotData = new DotData(null, diagram.getLinks(), diagram.entities(), diagram.getUmlDiagramType(),
				diagram.getSkinParam(), diagram.getRankdir(), diagram, diagram, diagram.getColorMapper());
		final CucaDiagramFileMakerSvek2 svek2 = new CucaDiagramFileMakerSvek2(dotData);

		IEntityImage result = svek2.createFile(((CucaDiagram) diagram).getDotStringSkek());
		result = addTitle(result);
		result = addHeaderAndFooter(result);

		// final Dimension2D dim =
		// Dimension2DDouble.delta(result.getDimension(stringBounder), 10);
		final Dimension2D dim = result.getDimension(stringBounder);

		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		if (fileFormat == FileFormat.PNG) {
			createPng(os, fileFormatOption, result, dim);
		} else if (fileFormat == FileFormat.SVG) {
			createSvg(os, fileFormatOption, result, dim);
		} else if (fileFormat == FileFormat.EPS) {
			createEps(os, fileFormatOption, result, dim);
		} else {
			throw new UnsupportedOperationException(fileFormat.toString());
		}
		
		if (result instanceof DecorateEntityImage) {
			deltaX += ((DecorateEntityImage) result).getDeltaX();
			deltaY += ((DecorateEntityImage) result).getDeltaY();
		}


		if (diagram.hasUrl()) {
			return cmapString(svek2, deltaX, deltaY);
		}
		return null;

	}

	private String cmapString(CucaDiagramFileMakerSvek2 svek2, double deltaX, double deltaY) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<map id=\"unix\" name=\"unix\">\n");
		for (IEntity ent : diagram.entities().values()) {
			final Url url = ent.getUrl();
			if (url == null) {
				continue;
			}
			sb.append("<area shape=\"rect\" id=\"");
			sb.append(ent.getUid());
			sb.append("\" href=\"");
			sb.append(url.getUrl());
			sb.append("\" title=\"");
			sb.append(url.getTooltip());
			sb.append("\" alt=\"\" coords=\"");

			final Shape sh = svek2.getShape(ent);
			sb.append(sh.getCoords(deltaX, deltaY));

			sb.append("\"/>");

			sb.append("\n");
		}
		sb.append("</map>\n");
		return sb.toString();
	}

	private IEntityImage addHeaderAndFooter(IEntityImage original) {
		final List<String> footer = diagram.getFooter();
		final List<String> header = diagram.getHeader();
		if (footer == null && header == null) {
			return original;
		}
		final TextBlock textFooter = footer == null ? null : TextBlockUtils.create(footer, new FontConfiguration(
				getFont(FontParam.FOOTER), getFontColor(FontParam.FOOTER, null)), diagram.getFooterAlignement());
		final TextBlock textHeader = header == null ? null : TextBlockUtils.create(header, new FontConfiguration(
				getFont(FontParam.HEADER), getFontColor(FontParam.HEADER, null)), diagram.getHeaderAlignement());

		return new DecorateEntityImage(original, textHeader, diagram.getHeaderAlignement(), textFooter, diagram
				.getFooterAlignement());
	}

	private IEntityImage addTitle(IEntityImage original) {
		final List<? extends CharSequence> title = diagram.getTitle();
		if (title == null) {
			return original;
		}
		final TextBlock text = TextBlockUtils.create(title, new FontConfiguration(getFont(FontParam.TITLE),
				getFontColor(FontParam.TITLE, null)), HorizontalAlignement.CENTER);

		return new DecorateEntityImage(original, text, HorizontalAlignement.CENTER);
	}

	private final UFont getFont(FontParam fontParam) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return skinParam.getFont(fontParam, null);
	}

	private final HtmlColor getFontColor(FontParam fontParam, String stereo) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return skinParam.getFontHtmlColor(fontParam, stereo);
	}

	private void createPng(OutputStream os, FileFormatOption fileFormatOption, final IEntityImage result,
			final Dimension2D dim) throws IOException {
		Color backColor = Color.WHITE;
		if (result.getBackcolor() != null) {
			backColor = diagram.getSkinParam().getColorMapper().getMappedColor(result.getBackcolor());
		}

		final EmptyImageBuilder builder = new EmptyImageBuilder((int) dim.getWidth(), (int) dim.getHeight(), backColor);
		final Graphics2D graphics2D = builder.getGraphics2D();

		final UGraphic ug = new UGraphicG2d(diagram.getSkinParam().getColorMapper(), graphics2D, builder
				.getBufferedImage(), 1.0);
		result.drawU(ug, 0, 0);

		final BufferedImage im = ((UGraphicG2d) ug).getBufferedImage();
		PngIO.write(im, os, diagram.getMetadata(), diagram.getDpi(fileFormatOption));
	}

	private void createSvg(OutputStream os, FileFormatOption fileFormatOption, final IEntityImage result,
			final Dimension2D dim) throws IOException {

		Color backColor = Color.WHITE;
		if (result.getBackcolor() != null) {
			backColor = diagram.getSkinParam().getColorMapper().getMappedColor(result.getBackcolor());
		}
		final UGraphicSvg ug;
		if (backColor.equals(Color.WHITE)) {
			ug = new UGraphicSvg(diagram.getSkinParam().getColorMapper(), false);
		} else {
			ug = new UGraphicSvg(diagram.getSkinParam().getColorMapper(), StringUtils.getAsHtml(backColor), false);
		}

		result.drawU(ug, 0, 0);

		ug.createXml(os);

	}

	private void createEps(OutputStream os, FileFormatOption fileFormatOption, final IEntityImage result,
			final Dimension2D dim) throws IOException {

		final UGraphicEps ug = new UGraphicEps(diagram.getSkinParam().getColorMapper(), EpsStrategy.getDefault2());

		result.drawU(ug, 0, 0);
		os.write(ug.getEPSCode().getBytes());

	}

}
