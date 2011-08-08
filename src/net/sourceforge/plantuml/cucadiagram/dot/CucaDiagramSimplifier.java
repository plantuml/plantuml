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
 * Revision $Revision: 6710 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.skin.rose.Rose;

public final class CucaDiagramSimplifier {

	private final CucaDiagram diagram;
	private final FileFormat fileFormat;

	public CucaDiagramSimplifier(CucaDiagram diagram, List<String> dotStrings, FileFormat fileFormat)
			throws IOException, InterruptedException {
		this.diagram = diagram;
		this.fileFormat = fileFormat;
		boolean changed;
		do {
			changed = false;
			final Collection<Group> groups = new ArrayList<Group>(diagram.getGroups());
			for (Group g : groups) {
				if (diagram.isAutarkic(g)) {
					final EntityType type;
					if (g.getType() == GroupType.CONCURRENT_STATE) {
						type = EntityType.STATE_CONCURRENT;
					} else if (g.getType() == GroupType.STATE) {
						type = EntityType.STATE;
					} else if (g.getType() == GroupType.INNER_ACTIVITY) {
						type = EntityType.ACTIVITY;
					} else if (g.getType() == GroupType.CONCURRENT_ACTIVITY) {
						type = EntityType.ACTIVITY_CONCURRENT;
					} else {
						throw new IllegalStateException();
					}
					final Entity proxy = new Entity("#" + g.getCode(), g.getDisplay(), type, g.getParent(),
							diagram.getHides());
					if (type == EntityType.STATE) {
						manageBackColorForState(diagram, g, proxy);
					}
					for (Member field : g.getEntityCluster().getFieldsToDisplay()) {
						proxy.addField(field);
					}
					computeImageGroup(g, proxy, dotStrings);
					diagram.overideGroup(g, proxy);
					if (proxy.getImageFile() != null) {
						diagram.ensureDelete(proxy.getImageFile());
					}

//					final IEntity entityCluster = g.getEntityCluster();
//					if (entityCluster != null && entityCluster.getImageFile() != null) {
//						proxy.addSubImage(entityCluster.getImageFile());
//					}
//					if (entityCluster != null) {
//						proxy.addSubImage((Entity) entityCluster);
//					}

					for (IEntity sub : g.entities().values()) {
						final DrawFile subImage = sub.getImageFile();
						if (subImage != null) {
							proxy.addSubImage(subImage);
						}
					}

					changed = true;
				}
			}
		} while (changed);
	}

	private void computeImageGroup(final Group group, final Entity entity, List<String> dotStrings) throws IOException,
			FileNotFoundException, InterruptedException {
		if (group.entities().size()==0) {
			return;
		}
		final GroupPngMaker maker = new GroupPngMaker(diagram, group, fileFormat);
		final File f = FileUtils.createTempFile("inner", ".png");
		OutputStream fos = null;
		try {
			fos = new BufferedOutputStream(new FileOutputStream(f));
			maker.createPng(fos, dotStrings);
			final String svg = maker.createSvg(dotStrings);
			// final Pattern pImage = Pattern.compile("(?i)<image\\W[^>]*>");
			// final Matcher mImage = pImage.matcher(svg);
			// if (mImage.find()) {
			// throw new IllegalStateException();
			// }
			entity.setImageFile(DrawFile.createFromFile(f, svg, null));
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	private void manageBackColorForState(CucaDiagram diagram, Group g, final Entity proxy) {
		if (OptionFlags.PBBACK == false) {
			return;
		}
		if (g.getBackColor() != null) {
			proxy.setSpecificBackcolor(g.getBackColor());
			return;
		}
		assert g.getBackColor() == null;
		if (g.getStereotype() != null) {
			proxy.setStereotype(new Stereotype(g.getStereotype()));
		}
		// PBBACK
		final Rose rose = new Rose();
		final HtmlColor back = rose.getHtmlColor(diagram.getSkinParam(), ColorParam.stateBackground, g.getStereotype());
		// final HtmlColor back = diagram.getSkinParam().getHtmlColor(ColorParam.stateBackground, g.getStereotype());
		// if (back != null) {
		// proxy.setSpecificBackcolor(back.getAsHtml());
		// }
		assert g.getBackColor() == null;
		g.setBackColor(back);
	}

}
