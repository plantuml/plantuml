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
 * Revision $Revision: 5385 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupHierarchy;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.skin.rose.Rose;

public final class GroupPngMaker {

	private final CucaDiagram diagram;
	private final Group group;
	private final Rose rose = new Rose();
	private final FileFormat fileFormat;

	class InnerGroupHierarchy implements GroupHierarchy {

		public Collection<Group> getChildrenGroups(Group parent) {
			if (parent == null) {
				return diagram.getChildrenGroups(group);
			}
			return diagram.getChildrenGroups(parent);
		}

		public boolean isEmpty(Group g) {
			return diagram.isEmpty(g);
		}

	}

	public GroupPngMaker(CucaDiagram diagram, Group group, FileFormat fileFormat) throws IOException {
		this.diagram = diagram;
		this.group = group;
		this.fileFormat = fileFormat;
	}

	public void createPng(OutputStream os, List<String> dotStrings) throws IOException, InterruptedException {
		final Map<Entity, File> imageFiles = new HashMap<Entity, File>();
		// final Map<Link, File> imagesLink = new HashMap<Link, File>();
		try {
			// populateImages(imageFiles);
			// populateImagesLink(imagesLink);
			final GraphvizMaker dotMaker = createDotMaker(dotStrings);
			final String dotString = dotMaker.createDotString();

			// if (OptionFlags.getInstance().isKeepTmpFiles()) {
			// traceDotString(dotString);
			// }

			// final boolean isUnderline = dotMaker.isUnderline();
			final Graphviz graphviz = GraphvizUtils.create(dotString, "png");

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			graphviz.createPng(baos);
			baos.close();

			final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			final BufferedImage im = ImageIO.read(bais);
			bais.close();
			// if (isUnderline) {
			// new UnderlineTrick(im, new Color(Integer.parseInt("FEFECF", 16)),
			// Color.BLACK).process();
			// }

			PngIO.write(im, os, diagram.getMetadata());
		} finally {
			cleanTemporaryFiles(imageFiles);
		}
	}

	public String createSvg(List<String> dotStrings) throws IOException, InterruptedException {
		final Map<Entity, File> imageFiles = new HashMap<Entity, File>();
		// final Map<Link, File> imagesLink = new HashMap<Link, File>();
		try {
			// populateImages(imageFiles);
			// populateImagesLink(imagesLink);
			final GraphvizMaker dotMaker = createDotMaker(dotStrings);
			final String dotString = dotMaker.createDotString();

			// if (OptionFlags.getInstance().isKeepTmpFiles()) {
			// traceDotString(dotString);
			// }

			// final boolean isUnderline = dotMaker.isUnderline();
			final Graphviz graphviz = GraphvizUtils.create(dotString, "svg");

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			graphviz.createPng(baos);
			baos.close();

			String svg = new String(baos.toByteArray(), "UTF-8");
			svg = removeSvgXmlHeader(svg);
			return svg;

		} finally {
			cleanTemporaryFiles(imageFiles);
		}
	}

	private static String removeSvgXmlHeader(String svg) {
		svg = svg.replaceFirst("(?i)<\\?xml[\\s\\S]*?<svg[^>]*>", "");
		svg = svg.replaceFirst("(?i)</svg>", "");

		return svg;
	}

	private void cleanTemporaryFiles(final Map<Entity, File> imageFiles) {
		if (OptionFlags.getInstance().isKeepTmpFiles() == false) {
			for (File f : imageFiles.values()) {
				StaticFiles.delete(f);
			}
		}
	}

	GraphvizMaker createDotMaker(List<String> dotStrings) {
		final List<Link> links = getPureInnerLinks();
		final DotData dotData = new DotData(group, links, group.entities(), diagram.getUmlDiagramType(), diagram
				.getSkinParam(), group.getRankdir(), new InnerGroupHierarchy());
		// dotData.putAllImages(images);
		// dotData.putAllStaticImages(staticImages);
		// dotData.putAllImagesLink(imagesLink);

		return new DotMaker(dotData, dotStrings, fileFormat);
	}

	private List<Link> getPureInnerLinks() {
		final List<Link> result = new ArrayList<Link>();
		for (Link link : diagram.getLinks()) {
			final IEntity e1 = link.getEntity1();
			final IEntity e2 = link.getEntity2();
			if (e1.getParent() == group && e1.getType() != EntityType.GROUP && e2.getParent() == group
					&& e2.getType() != EntityType.GROUP) {
				result.add(link);
			}
		}
		return result;
	}

}
