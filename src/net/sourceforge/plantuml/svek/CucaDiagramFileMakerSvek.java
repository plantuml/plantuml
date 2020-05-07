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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.BaseFile;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.NamedOutputStream;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.api.ImageDataAbstract;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramSimplifierActivity;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramSimplifierState;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public final class CucaDiagramFileMakerSvek implements CucaDiagramFileMaker {

	private final CucaDiagram diagram;

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

	private GeneralImageBuilder createDotDataImageBuilder(DotMode dotMode, StringBounder stringBounder) {
		final DotData dotData = new DotData(diagram.getEntityFactory().getRootGroup(), getOrderedLinks(),
				diagram.getLeafsvalues(), diagram.getUmlDiagramType(), diagram.getSkinParam(), diagram, diagram,
				diagram.getColorMapper(), diagram.getEntityFactory(), diagram.isHideEmptyDescriptionForState(), dotMode,
				diagram.getNamespaceSeparator(), diagram.getPragma());
		final boolean intricated = diagram.mergeIntricated();
		return new GeneralImageBuilder(intricated, dotData, diagram.getEntityFactory(), diagram.getSource(),
				diagram.getPragma(), stringBounder, diagram.getUmlDiagramType().getStyleName());

	}

	private ImageData createFileInternal(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {
		if (diagram.getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			new CucaDiagramSimplifierActivity(diagram, dotStrings, fileFormatOption.getDefaultStringBounder());
		} else if (diagram.getUmlDiagramType() == UmlDiagramType.STATE) {
			new CucaDiagramSimplifierState(diagram, dotStrings, fileFormatOption.getDefaultStringBounder());
		}

		// System.err.println("FOO11 type=" + os.getClass());
		GeneralImageBuilder svek2 = createDotDataImageBuilder(DotMode.NORMAL,
				fileFormatOption.getDefaultStringBounder());
		BaseFile basefile = null;
		if (fileFormatOption.isDebugSvek() && os instanceof NamedOutputStream) {
			basefile = ((NamedOutputStream) os).getBasefile();
		}
		// System.err.println("FOO11 basefile=" + basefile);

		TextBlockBackcolored result = svek2.buildImage(basefile, diagram.getDotStringSkek());
		if (result instanceof GraphvizCrash) {
			svek2 = createDotDataImageBuilder(DotMode.NO_LEFT_RIGHT_AND_XLABEL,
					fileFormatOption.getDefaultStringBounder());
			result = svek2.buildImage(basefile, diagram.getDotStringSkek());
		}
		final boolean isGraphvizCrash = result instanceof GraphvizCrash;
		result = new AnnotatedWorker(diagram, diagram.getSkinParam(), fileFormatOption.getDefaultStringBounder())
				.addAdd(result);

		final String widthwarning = diagram.getSkinParam().getValue("widthwarning");
		String warningOrError = null;
		if (widthwarning != null && widthwarning.matches("\\d+")) {
			warningOrError = svek2.getWarningOrError(Integer.parseInt(widthwarning));
		}
		final Dimension2D dim = result.calculateDimension(fileFormatOption.getDefaultStringBounder());
		final double scale = getScale(fileFormatOption, dim);

		final HColor backcolor = result.getBackcolor();
		final ClockwiseTopRightBottomLeft margins;
		if (SkinParam.USE_STYLES()) {
			final Style style = StyleSignature.of(SName.root, SName.document)
					.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
			margins = style.getMargin();
		} else {
			margins = ClockwiseTopRightBottomLeft.margin1margin2(0, 10);
		}
		final ImageBuilder imageBuilder = ImageBuilder.buildC(diagram.getSkinParam(), margins, diagram.getAnimation(),
				fileFormatOption.isWithMetadata() ? diagram.getMetadata() : null, warningOrError, scale, backcolor);
		imageBuilder.setUDrawable(result);
		final ImageData imageData = imageBuilder.writeImageTOBEMOVED(fileFormatOption, diagram.seed(), os);
		if (isGraphvizCrash) {
			((ImageDataAbstract) imageData).setStatus(503);
		}
		return imageData;
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

	private double getScale(FileFormatOption fileFormatOption, final Dimension2D dim) {
		final double scale;
		final Scale diagScale = diagram.getScale();
		if (diagScale == null) {
			scale = diagram.getScaleCoef(fileFormatOption);
		} else {
			scale = diagScale.getScale(dim.getWidth(), dim.getHeight());
		}
		return scale;
	}

}
