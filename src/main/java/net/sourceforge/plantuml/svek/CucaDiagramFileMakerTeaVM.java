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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.atmp.CucaDiagram;
import net.atmp.ImageBuilder;
import net.sourceforge.plantuml.AnnotatedBuilder;
import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.crash.GraphvizCrash;
import net.sourceforge.plantuml.dot.CucaDiagramSimplifierActivity;
import net.sourceforge.plantuml.dot.CucaDiagramSimplifierState;
import net.sourceforge.plantuml.dot.DotData;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.teavm.StringBounderTeaVM;

public final class CucaDiagramFileMakerTeaVM extends CucaDiagramFileMaker {

	public CucaDiagramFileMakerTeaVM(CucaDiagram diagram) {
		super(diagram);

	}

	@Override
	public ImageData createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {
		throw new UnsupportedOperationException("TEAVM100");
	}

	@Override
	public void createOneGraphic(UGraphic ug) {
		String[] dot = diagram.getDotStringSkek();
		final List<String> dots = new ArrayList<String>();
		for (String s : dot) {
			System.err.println("-->" + s);
			dots.add(s);
		}

		try {
			createFileInternal(ug, dots);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException("TEAVM103");
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException("TEAVM105");
		}
	}


	private void createFileInternal(UGraphic ug, List<String> dotStrings) throws IOException, InterruptedException {

		final StringBounder stringBounder = ug.getStringBounder();
		System.err.println("createFileInternal 0000");

//		if (diagram.getUmlDiagramType() == UmlDiagramType.ACTIVITY)
//			new CucaDiagramSimplifierActivity().simplify(diagram, stringBounder, DotMode.NORMAL);
//		else if (diagram.getUmlDiagramType() == UmlDiagramType.STATE)
//			new CucaDiagramSimplifierState().simplify(diagram, stringBounder, DotMode.NORMAL);

		final DotStringFactory dotStringFactory = new DotStringFactory(bibliotekon, clusterManager.getCurrent(),
				diagram.getUmlDiagramType(), diagram.getSkinParam());

		final DotData dotData = new DotData(diagram, diagram.getRootGroup(), getOrderedLinks(), diagram.leafs(),
				diagram, diagram);

		System.err.println("createFileInternal 0010");

		GraphvizImageBuilder imageBuilder = new GraphvizImageBuilder(dotData, diagram.getSource(), diagram.getPragma(),
				diagram.getUmlDiagramType().getStyleName(), DotMode.NORMAL, dotStringFactory, clusterManager);
		BaseFile basefile = null;
//		if (fileFormatOption.isDebugSvek() && os instanceof NamedOutputStream)
//			basefile = ((NamedOutputStream) os).getBasefile();

		System.err.println("createFileInternal 0020");
		TextBlock result = imageBuilder.buildImage(stringBounder, basefile, diagram.getDotStringSkek(), false);
		System.err.println("createFileInternal 0030");

		int status = 0;

		if (result instanceof IEntityImage && ((IEntityImage) result).isCrash())
			status = 503;

		if (result instanceof GraphvizCrash) {
			status = 503;
			imageBuilder = new GraphvizImageBuilder(dotData, diagram.getSource(), diagram.getPragma(),
					diagram.getUmlDiagramType().getStyleName(), DotMode.NO_LEFT_RIGHT_AND_XLABEL, dotStringFactory,
					clusterManager);
			result = imageBuilder.buildImage(stringBounder, basefile, diagram.getDotStringSkek(), false);
		}
		// TODO There is something strange with the left margin of mainframe, I think
		// because AnnotatedWorker is used here
		// It can be looked at in another PR

		System.err.println("createFileInternal 0040");

		final AnnotatedBuilder builder = new AnnotatedBuilder(diagram, diagram.getSkinParam(), stringBounder);
		result = new AnnotatedWorker(diagram, builder).addAdd(result);

		// TODO UmlDiagram.getWarningOrError() looks similar so this might be
		// simplified? - will leave for a separate PR
		final String widthwarning = diagram.getSkinParam().getValue("widthwarning");
		String warningOrError = null;
		if (widthwarning != null && widthwarning.matches("\\d+"))
			warningOrError = imageBuilder.getWarningOrError(Integer.parseInt(widthwarning));

		System.err.println("createFileInternal 0050");
		System.err.println("createFileInternal 0051 " + result);
		// Sorry about this hack. There is a side effect in
		// SvekResult::calculateDimension()
		result.calculateDimension(stringBounder); // Ensure text near the margins is not cut off
		System.err.println("createFileInternal 0060");

		ImageBuilder ib = diagram.createImageBuilder(new FileFormatOption(FileFormat.SVG)) //
				.annotations(false) // backwards compatibility (AnnotatedWorker is used above)
				.drawable(result) //
				.status(status) //
				.warningOrError(warningOrError);
		
		ib.udrawable.drawU(ug);

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
