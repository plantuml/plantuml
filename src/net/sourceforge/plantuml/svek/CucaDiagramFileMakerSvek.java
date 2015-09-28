/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.activitydiagram3.ftile.EntityImageLegend;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramSimplifierActivity;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramSimplifierState;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UFont;

public final class CucaDiagramFileMakerSvek implements CucaDiagramFileMaker {

	private final CucaDiagram diagram;

	static private final StringBounder stringBounder;

	static {
		final EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		stringBounder = StringBounderUtils.asStringBounder(builder.getGraphics2D());
	}

	public CucaDiagramFileMakerSvek(CucaDiagram diagram) throws IOException {
		this.diagram = diagram;
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
		final DotData dotData = new DotData(diagram.getEntityFactory().getRootGroup(), getOrderedLinks(),
				diagram.getLeafsvalues(), diagram.getUmlDiagramType(), diagram.getSkinParam(), diagram, diagram,
				diagram.getColorMapper(), diagram.getEntityFactory(), diagram.isHideEmptyDescriptionForState(),
				dotMode, diagram.getNamespaceSeparator(), diagram.getPragma());
		final CucaDiagramFileMakerSvek2 svek2 = new CucaDiagramFileMakerSvek2(dotData, diagram.getEntityFactory(),
				diagram.getSource(), diagram.getPragma());
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

		final String widthwarning = diagram.getSkinParam().getValue("widthwarning");
		if (widthwarning != null && widthwarning.matches("\\d+")) {
			this.warningOrError = svek2.getBibliotekon().getWarningOrError(Integer.parseInt(widthwarning));
		} else {
			this.warningOrError = null;
		}
		final Dimension2D dim = result.calculateDimension(stringBounder);
		final double scale = getScale(fileFormatOption, dim);

		final ImageBuilder imageBuilder = new ImageBuilder(diagram.getSkinParam().getColorMapper(), scale,
				result.getBackcolor(), fileFormatOption.isWithMetadata() ? diagram.getMetadata() : null,
				warningOrError, 0, 10, diagram.getAnimation(), diagram.getSkinParam().handwritten());
		imageBuilder.addUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, os);

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
		if (Display.isNull(footer) && Display.isNull(header)) {
			return original;
		}
		final TextBlock textFooter = Display.isNull(footer) ? null : footer.create(
				new FontConfiguration(diagram.getSkinParam(), FontParam.FOOTER, null), diagram.getFooterAlignment(),
				diagram.getSkinParam());
		final TextBlock textHeader = Display.isNull(header) ? null : header.create(
				new FontConfiguration(diagram.getSkinParam(), FontParam.HEADER, null), diagram.getHeaderAlignment(),
				diagram.getSkinParam());

		return new DecorateEntityImage(original, textHeader, diagram.getHeaderAlignment(), textFooter,
				diagram.getFooterAlignment());
	}

	private TextBlockBackcolored addTitle(TextBlockBackcolored original) {
		final Display title = diagram.getTitle();
		if (Display.isNull(title)) {
			return original;
		}
		final TextBlock text = title.create(new FontConfiguration(diagram.getSkinParam(), FontParam.TITLE, null),
				HorizontalAlignment.CENTER, diagram.getSkinParam());

		return DecorateEntityImage.addTop(original, text, HorizontalAlignment.CENTER);
	}

	private TextBlockBackcolored addLegend(TextBlockBackcolored original) {
		final Display legend = diagram.getLegend();
		if (Display.isNull(legend)) {
			return original;
		}
		final TextBlock text = EntityImageLegend.create(legend, diagram.getSkinParam());

		return DecorateEntityImage.add(original, text, diagram.getLegendAlignment(),
				diagram.getLegendVerticalAlignment());
	}

	private final UFont getFont(FontParam fontParam) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return skinParam.getFont(fontParam, null, false);
	}

	private final HtmlColor getFontColor(FontParam fontParam, Stereotype stereo) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return skinParam.getFontHtmlColor(fontParam, stereo);
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

}
