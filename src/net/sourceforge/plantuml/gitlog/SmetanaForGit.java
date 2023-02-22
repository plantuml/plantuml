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
 */
package net.sourceforge.plantuml.gitlog;

import static gen.lib.cgraph.attr__c.agsafeset;
import static gen.lib.cgraph.edge__c.agedge;
import static gen.lib.cgraph.graph__c.agopen;
import static gen.lib.cgraph.node__c.agnode;
import static gen.lib.gvc.gvc__c.gvContext;
import static gen.lib.gvc.gvlayout__c.gvLayoutJobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agraph_s;
import h.ST_GVC_s;
import net.sourceforge.plantuml.jsondiagram.Mirror;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import smetana.core.CString;
import smetana.core.Globals;

public class SmetanaForGit {

	private final UGraphic ug;
	private final ISkinParam skinParam;
	private int num;
	private ST_Agraph_s g;
	private StringBounder stringBounder;

	private final Map<GNode, ST_Agnode_s> nodes = new LinkedHashMap<GNode, ST_Agnode_s>();
	private final List<ST_Agedge_s> edges = new ArrayList<>();
	private Mirror yMirror;

	public SmetanaForGit(UGraphic ug, ISkinParam skinParam) {
		this.stringBounder = ug.getStringBounder();
		this.skinParam = skinParam;
		this.ug = getStyle().applyStrokeAndLineColor(ug, skinParam.getIHtmlColorSet());
	}

	private Style getStyle() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.gitDiagram)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private HColor arrowColor() {
		return getStyle().value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
	}

	public void drawMe(Collection<GNode> gnodes) {
		initGraph(gnodes);

		for (Entry<GNode, ST_Agnode_s> ent : nodes.entrySet()) {
			final ST_Agnode_s node = ent.getValue();
			final UTranslate pos = getPosition(node);

			final MagicBox magicBox = new MagicBox(skinParam, ent.getKey());
			magicBox.drawBorder(ug.apply(pos), getSize(node));
		}

		for (ST_Agedge_s edge : edges) {
			final ST_Agedgeinfo_t data = (ST_Agedgeinfo_t) edge.data;
			new GitCurve(data, yMirror).drawCurve(arrowColor(), ug);
		}
	}

	private XDimension2D getSize(ST_Agnode_s node) {
		final ST_Agnodeinfo_t data = (ST_Agnodeinfo_t) node.data;
		final double width = data.width * 72;
		final double height = data.height * 72;
		return new XDimension2D(width, height);
	}

	private UTranslate getPosition(ST_Agnode_s node) {
		final ST_Agnodeinfo_t data = (ST_Agnodeinfo_t) node.data;
		final double width = data.width * 72;
		final double height = data.height * 72;
		final double x = data.coord.x;
		final double y = data.coord.y;
		return new UTranslate(x - width / 2, yMirror.inv(y + height / 2));
	}

	private void initGraph(Collection<GNode> gnodes) {
		if (g != null)
			return;

		Globals zz = Globals.open();
		try {
			g = agopen(zz, new CString("g"), zz.Agdirected, null);
			agsafeset(zz, g, new CString("ranksep"), new CString("0.35"), new CString(""));
			for (GNode gnode : gnodes) {
				final XDimension2D dim = new MagicBox(skinParam, gnode).getBigDim(stringBounder);
				nodes.put(gnode, createNode(zz, dim));
			}

			for (GNode gnode : nodes.keySet()) {
				for (GNode parent : gnode.getDowns()) {
					final ST_Agedge_s edge = createEdge(zz, gnode, parent);
					if (edge != null)
						edges.add(edge);

				}
			}

			final ST_GVC_s gvc = gvContext(zz);
			gvLayoutJobs(zz, gvc, g);

			double max = 0;
			for (ST_Agnode_s node : nodes.values()) {
				final ST_Agnodeinfo_t data = (ST_Agnodeinfo_t) node.data;
				final double height = data.height * 72;
				final double y = data.coord.y;
				max = Math.max(max, y + height);
			}
			yMirror = new Mirror(max);

		} finally {
			Globals.close();
		}

	}

	private ST_Agedge_s createEdge(Globals zz, GNode node0, GNode node1) {
		final ST_Agnode_s a0 = nodes.get(node0);
		final ST_Agnode_s a1 = nodes.get(node1);
		if (a0 == null || a1 == null) {
			return null;
		}

		final ST_Agedge_s edge = agedge(zz, g, a0, a1, null, true);
		agsafeset(zz, edge, new CString("arrowsize"), new CString(".7"), new CString(""));
		agsafeset(zz, edge, new CString("arrowtail"), new CString("none"), new CString(""));
		agsafeset(zz, edge, new CString("arrowhead"), new CString("normal"), new CString(""));
		return edge;
	}

	private ST_Agnode_s createNode(Globals zz, XDimension2D dim) {
		final String width = "" + (dim.getWidth() / 72);
		final String height = "" + (dim.getHeight() / 72);

		final ST_Agnode_s node = agnode(zz, g, new CString("N" + num), true);
		node.NAME = "N " + num;
		num++;

		agsafeset(zz, node, new CString("shape"), new CString("box"), new CString(""));
		agsafeset(zz, node, new CString("height"), new CString("" + height), new CString(""));
		agsafeset(zz, node, new CString("width"), new CString("" + width), new CString(""));

		return node;
	}

}
