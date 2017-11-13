/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.util.Collection;

import net.sourceforge.plantuml.Log;

public class DotxMaker {

	private final Cluster root;
	private final Collection<Path> paths;

	public DotxMaker(Cluster root, Collection<Path> paths) {
		this.root = root;
		this.paths = paths;
	}

	public String createDotString(String... dotStrings) {
		final StringBuilder sb = new StringBuilder();
		sb.append("digraph unix {");

		for (String s : dotStrings) {
			sb.append(s);
		}

		sb.append("compound=true;");

		printCluster(sb, root);

		for (Path p : paths) {
			sb.append(getPathString(p)).append(";");
		}

		sb.append("}");

		return sb.toString();
	}

	private void printCluster(StringBuilder sb, Cluster cl) {
		if (cl.getContents().isEmpty() && cl.getSubClusters().isEmpty()) {
			throw new IllegalStateException(cl.toString());
		}
		for (Cluster sub : cl.getSubClusters()) {
			sb.append("subgraph cluster").append(sub.getUid()).append(" {");
			if (sub.getTitleWidth() > 0 && sub.getTitleHeight() > 0) {
				sb.append("label=<<TABLE FIXEDSIZE=\"TRUE\" WIDTH=\"").append(sub.getTitleWidth()).append("\" HEIGHT=\"")
                  .append(sub.getTitleHeight()).append("\"><TR><TD></TD></TR></TABLE>>");
			}

			printCluster(sb, sub);
			sb.append("}");

		}
		for (Block b : cl.getContents()) {
			sb.append("b").append(b.getUid()).append(getNodeAttibute(b)).append(";");
		}

	}

	private String getPathString(Path p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		final StringBuilder sb = new StringBuilder("b" + p.getStart().getUid() + " -> b" + p.getEnd().getUid());
		sb.append(" [dir=none, arrowhead=none, headclip=true, tailclip=true");
		final int len = p.getLength();
		if (len >= 3) {
			sb.append(",minlen=").append(len - 1);
		}
		if (p.getLabel() == null) {
			sb.append("]");
		} else {
			final Dimension2D size = p.getLabel().getSize();
			sb.append(", label=<<TABLE FIXEDSIZE=\"TRUE\" WIDTH=\"").append(size.getWidth()).append("\" HEIGHT=\"").append(size.getHeight())
              .append("\"><TR><TD></TD></TR></TABLE>>]");
		}

		if (p.getLength() <= 1) {
			final boolean samePackage = p.getStart().getParent() == p.getEnd().getParent();
			if (samePackage) {
				sb.append("{rank=same; b").append(p.getStart().getUid()).append("; b").append(p.getEnd().getUid()).append("}");
			} else {
				Log.println("!!!!!!!!!!!!!!!!!TURNING ARROUND DOT BUG!!!!!!!!!!!!!!!!!!");
			}
		}

		return sb.toString();
	}

	private String getNodeAttibute(Block b) {
		return "[" + "label=\"\"," +
			"fixedsize=true," +
			"width=" + b.getSize().getWidth() / 72.0 + "," +
			"height=" + b.getSize().getHeight() / 72.0 + "," +
			"shape=rect" +
			"]";
	}

}
