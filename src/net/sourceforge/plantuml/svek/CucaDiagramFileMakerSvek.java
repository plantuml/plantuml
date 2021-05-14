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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.BaseFile;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.NamedOutputStream;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramSimplifierActivity;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramSimplifierState;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.StringBounder;

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
		final StringBounder stringBounder = fileFormatOption.getDefaultStringBounder(diagram.getSkinParam());
		if (diagram.getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			new CucaDiagramSimplifierActivity(diagram, dotStrings, stringBounder);
		} else if (diagram.getUmlDiagramType() == UmlDiagramType.STATE) {
			new CucaDiagramSimplifierState(diagram, dotStrings, stringBounder);
		}

		// System.err.println("FOO11 type=" + os.getClass());
		GeneralImageBuilder svek2 = createDotDataImageBuilder(DotMode.NORMAL, stringBounder);
		BaseFile basefile = null;
		if (fileFormatOption.isDebugSvek() && os instanceof NamedOutputStream) {
			basefile = ((NamedOutputStream) os).getBasefile();
		}
		// System.err.println("FOO11 basefile=" + basefile);

		TextBlockBackcolored result = svek2.buildImage(basefile, diagram.getDotStringSkek());
		if (result instanceof GraphvizCrash) {
			svek2 = createDotDataImageBuilder(DotMode.NO_LEFT_RIGHT_AND_XLABEL, stringBounder);
			result = svek2.buildImage(basefile, diagram.getDotStringSkek());
		}
		// TODO There is something strange with the left margin of mainframe, I think because AnnotatedWorker is used here
		//      It can be looked at in another PR
		result = new AnnotatedWorker(diagram, diagram.getSkinParam(), stringBounder).addAdd(result);

		// TODO UmlDiagram.getWarningOrError() looks similar so this might be simplified? - will leave for a separate PR
		final String widthwarning = diagram.getSkinParam().getValue("widthwarning");
		String warningOrError = null;
		if (widthwarning != null && widthwarning.matches("\\d+")) {
			warningOrError = svek2.getWarningOrError(Integer.parseInt(widthwarning));
		}
		
		// Sorry about this hack. There is a side effect in SvekResult::calculateDimension()
		result.calculateDimension(stringBounder);  // Ensure text near the margins is not cut off

		return diagram.createImageBuilder(fileFormatOption)
				.annotations(false)  // backwards compatibility (AnnotatedWorker is used above)
				.drawable(result)
				.status(result instanceof GraphvizCrash ? 503 : 0)
				.warningOrError(warningOrError)
				.write(os);
	}

	private List<Link> getOrderedLinks() {
		final List<Link> result = new ArrayList<>();
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

}
