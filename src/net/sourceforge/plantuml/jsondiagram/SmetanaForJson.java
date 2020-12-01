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
 */
package net.sourceforge.plantuml.jsondiagram;

import static gen.lib.cgraph.attr__c.agsafeset;
import static gen.lib.cgraph.edge__c.agedge;
import static gen.lib.cgraph.graph__c.agopen;
import static gen.lib.cgraph.node__c.agnode;
import static gen.lib.gvc.gvc__c.gvContext;
import static gen.lib.gvc.gvlayout__c.gvLayoutJobs;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agraph_s;
import h.ST_GVC_s;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import smetana.core.CString;
import smetana.core.Macro;
import smetana.core.Z;

public class SmetanaForJson {

	private final UGraphic ug;
	private final ISkinParam skinParam;
	private int num;
	private ST_Agraph_s g;
	private StringBounder stringBounder;

	private final List<Node> nodes = new ArrayList<Node>();
	private final List<ST_Agedge_s> edges = new ArrayList<ST_Agedge_s>();
	private Mirror xMirror;

	static class Node {

		private final TextBlockJson block;
		private final ST_Agnode_s node;

		public Node(TextBlockJson block, ST_Agnode_s node) {
			this.block = block;
			this.node = node;
		}

		double getMaxX() {
			final ST_Agnodeinfo_t data = (ST_Agnodeinfo_t) Macro.AGDATA(node);
			final double height = data.height * 72;
			double y = data.coord.y;
			return y + height / 2;
		}

	}

	public SmetanaForJson(UGraphic ug, ISkinParam skinParam) {
		this.stringBounder = ug.getStringBounder();
		this.skinParam = skinParam;
		this.ug = getStyle().applyStrokeAndLineColor(ug, skinParam.getIHtmlColorSet());
	}

	private Style getStyle() {
		return StyleSignature.of(SName.root, SName.element, SName.jsonDiagram)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private ST_Agnode_s manageOneNode(JsonValue current, List<String> highlighted) {
		final TextBlockJson block = new TextBlockJson(skinParam, current, highlighted);
		final ST_Agnode_s node1 = createNode(block.calculateDimension(stringBounder), block.size());
		nodes.add(new Node(block, node1));
		final List<JsonValue> children = block.children();
		final List<String> keys = block.keys();
		for (int i = 0; i < children.size(); i++) {
			final JsonValue tmp = children.get(i);
			if (tmp != null) {
				final ST_Agnode_s childBloc = manageOneNode(tmp, removeOneLevel(keys.get(i), highlighted));
				final ST_Agedge_s edge = createEdge(node1, childBloc, i);
				edges.add(edge);
			}
		}
		return node1;

	}

	private List<String> removeOneLevel(String key, List<String> list) {
		final List<String> result = new ArrayList<String>();
		for (String tmp : list) {
			if (tmp.startsWith("\"" + key + "\"") == false) {
				continue;
			}
			tmp = tmp.trim().replaceFirst("\"([^\"]+)\"", "").trim();
			if (tmp.length() > 0) {
				tmp = tmp.substring(1).trim();
				result.add(tmp);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public void drawMe(JsonValue root, List<String> highlighted) {

		Z.open();
		try {
			g = agopen(new CString("g"), Z.z().Agdirected, null);
			manageOneNode(root, highlighted);

			final ST_GVC_s gvc = gvContext();
			gvLayoutJobs(gvc, g);

			double max = 0;
			for (Node node : nodes) {
				max = Math.max(max, node.getMaxX());
			}
			xMirror = new Mirror(max);

			for (Node node : nodes) {
				node.block.drawU(ug.apply(getPosition(node.node)));
			}

			final HColor color = getStyle().value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());

			for (ST_Agedge_s edge : edges) {
				final Curve curve = getCurve(edge, 13);
				curve.drawCurve(color, ug.apply(new UStroke(3, 3, 1)));
				curve.drawSpot(ug.apply(color.bg()));
			}
		} finally {
			Z.close();
		}
	}

	private UTranslate getPosition(ST_Agnode_s node) {
		final ST_Agnodeinfo_t data = (ST_Agnodeinfo_t) Macro.AGDATA(node);
		final double width = data.width * 72;
		final double height = data.height * 72;
		double x = data.coord.x;
		double y = data.coord.y;
		return new UTranslate(x - width / 2, xMirror.inv(y + height / 2)).sym();
	}

	private Curve getCurve(ST_Agedge_s e, double veryFirstLine) {
		final ST_Agedgeinfo_t data = (ST_Agedgeinfo_t) Macro.AGDATA(e);
		return new Curve(data, xMirror, veryFirstLine);
	}

	private ST_Agedge_s createEdge(ST_Agnode_s a0, ST_Agnode_s a1, int num) {
		final ST_Agedge_s edge = agedge(g, a0, a1, null, true);

		agsafeset(edge, new CString("arrowsize"), new CString(".75"), new CString(""));
		agsafeset(edge, new CString("arrowtail"), new CString("none"), new CString(""));
		agsafeset(edge, new CString("arrowhead"), new CString("normal"), new CString(""));
		agsafeset(edge, new CString("tailport"), new CString("P" + num), new CString(""));
		return edge;
	}

	private ST_Agnode_s createNode(Dimension2D dim, int size) {
		final String width = "" + (dim.getWidth() / 72);
		final String height = "" + (dim.getHeight() / 72);

		final ST_Agnode_s node = agnode(g, new CString("N" + num), true);
		num++;

		agsafeset(node, new CString("shape"), new CString("record"), new CString(""));
		agsafeset(node, new CString("height"), new CString("" + width), new CString(""));
		agsafeset(node, new CString("width"), new CString("" + height), new CString(""));

		if (size > 0) {
			agsafeset(node, new CString("label"), new CString(getDotLabel(size)), new CString(""));
		}

		return node;
	}

	private String getDotLabel(int size) {
		final StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < size; i++) {
			sb.append("<P" + i + ">");
			sb.append("x");
			if (i < size - 1)
				sb.append("|");
		}
		sb.append("");

		return sb.toString();
	}

}
