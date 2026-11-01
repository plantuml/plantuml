/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.posimo;

import static gen.lib.cgraph.attr__c.agsafeset;
import static gen.lib.cgraph.edge__c.agedge;
import static gen.lib.cgraph.graph__c.agopen;
import static gen.lib.cgraph.node__c.agnode;
import static gen.lib.gvc.gvc__c.gvContext;
import static gen.lib.gvc.gvlayout__c.gvLayoutJobs;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agraph_s;
import h.ST_Agraphinfo_t;
import h.ST_GVC_s;
import h.ST_bezier;
import h.ST_boxf;
import h.ST_pointf;
import h.ST_splines;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import net.sourceforge.plantuml.teavm.TeaVM;
import smetana.core.CString;
import smetana.core.Globals;

/**
 * Same job as {@link GraphvizSolverB}, but with Smetana (the Java port of
 * Graphviz) instead of the external {@code dot} program: it places the blocks
 * and computes the paths between them. It is what the builds without any
 * process (TeaVM) have to use.
 * <p>
 * Only the blocks directly inside the root cluster are supported, which is all
 * the text (TXT) output of the class diagrams needs.
 */
public class SmetanaSolverB {

	// The smetana port keeps its state in Globals, so layout runs are serialized
	// process-wide
	private static final Object lock = new Object();

	public XDimension2D solve(Cluster root, Collection<Path> paths) {
		if (root.getSubClusters().size() > 0)
			throw new UnsupportedOperationException("Clusters are not supported by SmetanaSolverB");

		// No monitor in the C build: it is single-threaded, and its monitors do not
		// survive a garbage collection happening while they are held
		if (TeaVM.isC())
			return solveOpen(root, paths);

		synchronized (lock) {
			return solveOpen(root, paths);
		}
	}

	private XDimension2D solveOpen(Cluster root, Collection<Path> paths) {
		final Globals zz = Globals.open();
		try {
			return solveInternal(zz, root, paths);
		} finally {
			Globals.close();
		}
	}

	private XDimension2D solveInternal(Globals zz, Cluster root, Collection<Path> paths) {
		final ST_Agraph_s g = agopen(zz, new CString("g"), zz.Agdirected, null);
		// The same separations as in GraphvizSolverB, in inches
		agsafeset(zz, g, new CString("nodesep"), new CString("0.2"), new CString(""));
		agsafeset(zz, g, new CString("ranksep"), new CString("0.2"), new CString(""));

		final Map<Block, ST_Agnode_s> nodes = new LinkedHashMap<>();
		for (Block b : root.getContents()) {
			final ST_Agnode_s node = agnode(zz, g, new CString("b" + b.getUid()), true);
			agsafeset(zz, node, new CString("shape"), new CString("box"), new CString(""));
			// Without an explicit empty label, the node is enlarged to fit its name
			agsafeset(zz, node, new CString("label"), new CString(""), new CString(""));
			final XDimension2D size = b.getSize();
			agsafeset(zz, node, new CString("width"), new CString("" + size.getWidth() / 72.0), new CString(""));
			agsafeset(zz, node, new CString("height"), new CString("" + size.getHeight() / 72.0), new CString(""));
			nodes.put(b, node);
		}

		final Map<Path, ST_Agedge_s> edges = new LinkedHashMap<>();
		for (Path p : paths) {
			final ST_Agnode_s node1 = nodes.get(p.getStart());
			final ST_Agnode_s node2 = nodes.get(p.getEnd());
			if (node1 == null || node2 == null)
				continue;

			final ST_Agedge_s e = agedge(zz, g, node1, node2, null, true);
			agsafeset(zz, e, new CString("arrowtail"), new CString("none"), new CString(""));
			agsafeset(zz, e, new CString("arrowhead"), new CString("none"), new CString(""));
			agsafeset(zz, e, new CString("minlen"), new CString("" + (p.getLength() - 1)), new CString(""));
			edges.put(p, e);
		}

		final ST_GVC_s gvc = gvContext(zz);
		gvLayoutJobs(zz, gvc, g);

		// Graphviz has the y axis going up, and the result is expected with the origin
		// at the top left corner, like the SVG output read by GraphvizSolverB
		final ST_boxf bb = (ST_boxf) ((ST_Agraphinfo_t) g.data).bb;
		final double minX = bb.LL.x;
		final double maxY = bb.UR.y;

		for (Map.Entry<Block, ST_Agnode_s> entry : nodes.entrySet()) {
			final ST_Agnodeinfo_t data = (ST_Agnodeinfo_t) entry.getValue().data;
			final double width = data.width * 72;
			final double height = data.height * 72;
			entry.getKey().setX(data.coord.x - width / 2 - minX);
			entry.getKey().setY(maxY - (data.coord.y + height / 2));
		}

		for (Map.Entry<Path, ST_Agedge_s> entry : edges.entrySet())
			entry.getKey().setDotPath(toDotPath((ST_Agedgeinfo_t) entry.getValue().data, minX, maxY));

		return new XDimension2D(bb.UR.x - bb.LL.x, bb.UR.y - bb.LL.y);
	}

	private static DotPath toDotPath(ST_Agedgeinfo_t data, double minX, double maxY) {
		DotPath result = new DotPath();
		final ST_splines splines = data.spl;
		if (splines == null)
			return result;

		for (int i = 0; i < splines.size; i++) {
			final ST_bezier bezier = splines.list.get__(i);
			for (int j = 0; j + 3 < bezier.size; j += 3)
				result = result.addCurve(getPoint(bezier, j, minX, maxY), getPoint(bezier, j + 1, minX, maxY),
						getPoint(bezier, j + 2, minX, maxY), getPoint(bezier, j + 3, minX, maxY));

		}
		return result;
	}

	private static XPoint2D getPoint(ST_bezier bezier, int i, double minX, double maxY) {
		final ST_pointf pt = bezier.list.get__(i);
		return new XPoint2D(pt.x - minX, maxY - pt.y);
	}

}
