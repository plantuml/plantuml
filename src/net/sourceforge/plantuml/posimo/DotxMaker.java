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
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.util.Collection;

public class DotxMaker {

	private final Cluster root;
	private final Collection<Path> paths;

	public DotxMaker(Cluster root, Collection<Path> paths) {
		this.root = root;
		this.paths = paths;
	}

	public String createDotString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("digraph unix {");
		sb.append("compound=true;");

		printCluster(sb, root);

		for (Path p : paths) {
			sb.append(getPathString(p) + ";");
		}

		sb.append("}");

		return sb.toString();
	}

	private void printCluster(StringBuilder sb, Cluster cl) {
		if (cl.getContents().size() == 0) {
			throw new IllegalStateException(cl.toString());
		}
		for (Cluster sub : cl.getSubClusters()) {
			sb.append("subgraph cluster" + sub.getUid() + " {");
			printCluster(sb, sub);
			sb.append("}");

		}
		for (Block b : cl.getContents()) {
			sb.append("b" + b.getUid() + getNodeAttibute(b) + ";");
		}

	}

	private String getPathString(Path p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		final StringBuilder sb = new StringBuilder("b" + p.getStart().getUid() + " -> b" + p.getEnd().getUid());
		//sb.append(" [dir=none, arrowhead=none, headclip=false, tailclip=false");
		sb.append(" [dir=none, arrowhead=none, headclip=true, tailclip=true");
		if (p.getLabel() == null) {
			sb.append("]");
		} else {
			final Dimension2D size = p.getLabel().getSize();
			sb.append(", label=<<TABLE FIXEDSIZE=\"TRUE\" WIDTH=\"" + size.getWidth() + "\" HEIGHT=\""
					+ size.getHeight() + "\"><TR><TD></TD></TR></TABLE>>]");
		}

		if (p.getLength() <= 1) {
			sb.append("{rank=same; b" + p.getStart().getUid() + "; b" + p.getEnd().getUid() + "}");
		}

		return sb.toString();
	}

	private String getNodeAttibute(Block b) {
		final StringBuilder sb = new StringBuilder("[");
		sb.append("label=\"\",");
		sb.append("fixedsize=true,");
		sb.append("width=" + b.getSize().getWidth() / 72.0 + ",");
		sb.append("height=" + b.getSize().getHeight() / 72.0 + ",");
		sb.append("shape=rect");
		sb.append("]");
		return sb.toString();
	}

}
