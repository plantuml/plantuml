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
import java.util.List;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;

public abstract class CucaDiagramFileMaker {

	protected final CucaDiagram diagram;
	protected final Bibliotekon bibliotekon;
	protected final ClusterManager clusterManager;

	public CucaDiagramFileMaker(CucaDiagram diagram) {
		this.diagram = diagram;
		this.bibliotekon = new Bibliotekon(diagram.getLinks());
		final Cluster root = new Cluster(null, diagram, bibliotekon.getColorSequence(), diagram.getRootGroup());
		this.clusterManager = new ClusterManager(bibliotekon, root);

	}

	protected final Bibliotekon getBibliotekon() {
		return bibliotekon;
	}

	public abstract ImageData createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException;

	public abstract void createOneGraphic(UGraphic ug);
}
