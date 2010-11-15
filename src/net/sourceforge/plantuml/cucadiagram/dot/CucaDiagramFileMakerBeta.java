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
 * Revision $Revision: 4302 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.png.PngSplitter;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public final class CucaDiagramFileMakerBeta {

	private final CucaDiagram diagram;

	public CucaDiagramFileMakerBeta(CucaDiagram diagram) throws IOException {
		this.diagram = diagram;
	}

	public List<File> createFile(File suggested, List<String> dotStrings, FileFormat fileFormat) throws IOException,
			InterruptedException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(suggested);
			createFile(os, dotStrings, fileFormat);
		} finally {
			if (os != null) {
				os.close();
			}
		}

		if (fileFormat == FileFormat.PNG) {
			final List<File> result = new PngSplitter(suggested, diagram.getHorizontalPages(), diagram
					.getVerticalPages(), diagram.getMetadata()).getFiles();
			for (File f : result) {
				Log.info("Creating file: " + f);
			}
			return result;
		}
		Log.info("Creating file: " + suggested);
		return Arrays.asList(suggested);
	}

	public void createFile(OutputStream os, List<String> dotStrings, FileFormat fileFormat) throws IOException,
			InterruptedException {
		if (fileFormat == FileFormat.PNG) {
			createPng(os, dotStrings);
			// } else if (fileFormat == FileFormat.SVG) {
			// createSvg(os, dotStrings);
			// } else if (fileFormat == FileFormat.EPS) {
			// createEps(os, dotStrings);
		} else {
			throw new UnsupportedOperationException();
		}

	}

	private void createPng(OutputStream os, List<String> dotStrings) throws IOException, InterruptedException {

		final Color background = diagram.getSkinParam().getBackgroundColor().getColor();
		EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, background);
		BufferedImage im = builder.getBufferedImage();
		Graphics2D g2d = builder.getGraphics2D();
		UGraphicG2d ug = new UGraphicG2d(g2d, im);
		final PlayField playField = new PlayField(diagram.getSkinParam());

		final Collection<IEntity> entities = getFirstLevelEntities();
		System.err.println("entities=" + entities);
		// final Collection<Link> links = getLinks(entities);
		final Collection<Link> links = diagram.getLinks();

		playField.initInternal(entities, links, ug.getStringBounder());
		g2d.dispose();

		final Dimension2D dim = playField.solve();

		builder = new EmptyImageBuilder((int) (dim.getWidth() + 1), (int) (dim.getHeight() + 1), background);
		im = builder.getBufferedImage();
		g2d = builder.getGraphics2D();
		g2d.translate(10, 0);
		ug = new UGraphicG2d(g2d, im);

		playField.drawInternal(ug);

		PngIO.write(im, os);

	}

	// private List<Link> getLinks(Collection<IEntity> entities) {
	// final List<Link> result = new ArrayList<Link>();
	// for (Link link : diagram.getLinks()) {
	// if (entities.contains(link.getEntity1()) &&
	// entities.contains(link.getEntity2())) {
	// result.add(link);
	// }
	// }
	// return result;
	// }

	private Collection<IEntity> getFirstLevelEntities() {
		final Collection<IEntity> result = new HashSet<IEntity>();
		diagram.computeAutonomyOfGroups();
		System.err.println("diagramEntities = " + diagram.entities());
		System.err.println("diagramGroups = " + diagram.getGroups());
		addEntitiesOfGroup(result, null);
		return result;
	}

	private void addEntitiesOfGroup(final Collection<IEntity> result, Group parent) {
		System.err.println("addEntitiesOfGroup parent=" + parent);
		for (IEntity ent : diagram.entities().values()) {
			if (ent.getParent() == parent) {
				result.add(ent);
				System.err.println("addingA " + ent);
			}
		}
		final Collection<Group> groups = parent == null ? diagram.getGroups() : parent.getChildren();
		for (Group g : groups) {
			System.err.println("g=" + g + " parent = " + g.getParent());
			if (g.isAutonom() == false) {
				addEntitiesOfGroup(result, g);
				result.add(g.getEntityCluster());
				System.err.println("addingB " + g.getEntityCluster());
			} else if (g.getParent() == parent) {
				assert g.isAutonom();
				assert result.contains(g.getEntityCluster()) == false;
				result.add(g.getEntityCluster());
				System.err.println("addingC " + g.getEntityCluster());
			}

		}
	}

}
