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
package net.sourceforge.plantuml.svek;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.crash.GraphvizCrash;
import net.sourceforge.plantuml.dot.CucaDiagramSimplifierActivity;
import net.sourceforge.plantuml.dot.CucaDiagramSimplifierState;
import net.sourceforge.plantuml.dot.DotData;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.teavm.StringBounderTeaVM;
import net.sourceforge.plantuml.teavm.browser.BrowserLog;

public final class CucaDiagramFileMakerTeaVM extends CucaDiagramFileMaker {
	// ::remove file when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__ JAVA8

	public CucaDiagramFileMakerTeaVM(CucaDiagram diagram) {
		super(diagram);
	}

	@Override
	public TextBlock getTextBlock12026(List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {

		BrowserLog.consoleLog(CucaDiagramFileMakerTeaVM.class, "getTextBlock12026");
		final StringBounder stringBounder = new StringBounderTeaVM();

		if (diagram.getUmlDiagramType() == UmlDiagramType.ACTIVITY)
			new CucaDiagramSimplifierActivity().simplify(diagram, stringBounder, DotMode.NORMAL);
		else if (diagram.getUmlDiagramType() == UmlDiagramType.STATE)
			new CucaDiagramSimplifierState().simplify(diagram, stringBounder, DotMode.NORMAL);

		final DotStringFactory dotStringFactory = new DotStringFactory(bibliotekon, clusterManager.getCurrent(),
				diagram.getUmlDiagramType(), diagram.getSkinParam());

		final DotData dotData = new DotData(diagram, diagram.getRootGroup(), getOrderedLinks(), diagram.leafs(),
				diagram, diagram);

		GraphvizImageBuilder imageBuilder = new GraphvizImageBuilder(dotData, diagram.getSource(), diagram.getPragma(),
				diagram.getUmlDiagramType().getStyleName(), DotMode.NORMAL, dotStringFactory, clusterManager);

		TextBlock result = imageBuilder.buildImage(stringBounder, null, diagram.getDotStringSkek(), false);

		int status = 0;

		if (result instanceof IEntityImage && ((IEntityImage) result).isCrash())
			status = 503;

		if (result instanceof GraphvizCrash) {
			status = 503;
			imageBuilder = new GraphvizImageBuilder(dotData, diagram.getSource(), diagram.getPragma(),
					diagram.getUmlDiagramType().getStyleName(), DotMode.NO_LEFT_RIGHT_AND_XLABEL, dotStringFactory,
					clusterManager);
			result = imageBuilder.buildImage(stringBounder, null, diagram.getDotStringSkek(), false);
		}

		// Ensure text near the margins is not cut off (side effect in
		// SvekResult::calculateDimension())
		result.calculateDimension(stringBounder);

		return result;

//		final String widthwarning = diagram.getSkinParam().getValue("widthwarning");
//		String warningOrError = null;
//		if (widthwarning != null && widthwarning.matches("\\d+"))
//			warningOrError = imageBuilder.getWarningOrError(Integer.parseInt(widthwarning));
//
//		// Use ImageBuilder.drawU() to apply all decoration logic (annotations,
//		// margins, handwritten mode, border) on the externally provided UGraphic.
//		final ImageBuilder ib = diagram.createImageBuilder(new FileFormatOption(FileFormat.SVG)).drawable(result)
//				.status(status).warningOrError(warningOrError);
//
//		ib.udrawable.drawU(ug);

	}

	private List<Link> getOrderedLinks() {
		final List<Link> result = new ArrayList<>();
		for (Link l : diagram.getLinks())
			addLinkNew(result, l);

		return result;
	}

	private void addLinkNew(List<Link> result, Link link) {
		for (int i = 0; i < result.size(); i++) {
			final Link other = result.get(i);
			if (other.sameConnections(link)) {
				while (i < result.size() && result.get(i).sameConnections(link))
					i++;

				if (i == result.size())
					result.add(link);
				else
					result.add(i, link);

				return;
			}
		}
		result.add(link);
	}

}
