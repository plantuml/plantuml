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
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import smetana.core.CString;
import smetana.core.Macro;
import smetana.core.Z;
import smetana.core.debug.SmetanaDebug;

public class SmetanaForJson {

	private static int NUM = 0;
	private final static boolean printFirst = false;
	private final static boolean exitAfterFirst = false;

	private final UGraphic ug;
	private final ISkinParam skinParam;
	private int num;
	private ST_Agraph_s g;
	private StringBounder stringBounder;

	private final List<InternalNode> nodes = new ArrayList<>();
	private final List<ST_Agedge_s> edges = new ArrayList<>();
	private Mirror xMirror;

	static class InternalNode {

		private final TextBlockJson block;
		private final ST_Agnode_s node;

		public InternalNode(TextBlockJson block, ST_Agnode_s node) {
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
		this.ug = ug;
	}

	private UGraphic getUgFor(SName name) {
		return getStyle(name).applyStrokeAndLineColor(ug, skinParam.getIHtmlColorSet(), skinParam.getThemeStyle());
	}

	private SName getDiagramType() {
		return skinParam.getUmlDiagramType() == UmlDiagramType.YAML ? SName.yamlDiagram : SName.jsonDiagram;
	}

	private Style getStyle(SName name) {
		return StyleSignature.of(SName.root, SName.element, getDiagramType(), name)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private Style getStyleNodeHighlight() {
		return StyleSignature.of(SName.root, SName.element, getDiagramType(), SName.node, SName.highlight)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private ST_Agnode_s manageOneNode(JsonValue current, List<String> highlighted) {
		final TextBlockJson block = new TextBlockJson(skinParam, current, highlighted, getStyle(SName.node),
				getStyleNodeHighlight());
		final ST_Agnode_s node1 = createNode(block.calculateDimension(stringBounder), block.size(), current.isArray(),
				(int) block.getWidthColA(stringBounder), (int) block.getWidthColB(stringBounder));
		nodes.add(new InternalNode(block, node1));
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
		final List<String> result = new ArrayList<>();
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
		initGraph(root, highlighted);
		double max = 0;
		for (InternalNode node : nodes) {
			max = Math.max(max, node.getMaxX());
		}
		xMirror = new Mirror(max);

		for (InternalNode node : nodes) {
			node.block.drawU(getUgFor(SName.node).apply(getPosition(node.node)));
		}
		final HColor color = getStyle(SName.arrow).value(PName.LineColor).asColor(skinParam.getThemeStyle(),
				skinParam.getIHtmlColorSet());

		for (ST_Agedge_s edge : edges) {
			final JsonCurve curve = getCurve(edge, 13);
			// curve.drawCurve(color, getUgFor(SName.arrow).apply(new UStroke(3, 3, 1)));
			curve.drawCurve(color, getUgFor(SName.arrow));
			curve.drawSpot(getUgFor(SName.arrow).apply(color.bg()));
		}
	}

	private void initGraph(JsonValue root, List<String> highlighted) {
		if (g != null) {
			return;
		}
		Z.open();
		try {

			g = agopen(new CString("g"), Z.z().Agdirected, null);
			manageOneNode(root, highlighted);

			final ST_GVC_s gvc = gvContext();
			gvLayoutJobs(gvc, g);
		} finally {
			Z.close();
			NUM++;
		}
		if (exitAfterFirst) {
			System.err.println("-----------------------------------");
			SmetanaDebug.printMe();
			System.err.println("-----------------------------------");
			System.exit(0);
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

	private JsonCurve getCurve(ST_Agedge_s e, double veryFirstLine) {
		final ST_Agedgeinfo_t data = (ST_Agedgeinfo_t) Macro.AGDATA(e);
		return new JsonCurve(data, xMirror, veryFirstLine);
	}

	private ST_Agedge_s createEdge(ST_Agnode_s a0, ST_Agnode_s a1, int num) {
		final ST_Agedge_s edge = agedge(g, a0, a1, null, true);
		edge.NAME = a0.NAME + "-" + a1.NAME;

		agsafeset(edge, new CString("arrowsize"), new CString(".75"), new CString(""));
		agsafeset(edge, new CString("arrowtail"), new CString("none"), new CString(""));
		agsafeset(edge, new CString("arrowhead"), new CString("normal"), new CString(""));
		agsafeset(edge, new CString("tailport"), new CString("P" + num), new CString(""));

		StringBuilder sb = new StringBuilder();
		sb.append("N" + a0.UID + " -> N" + a1.UID + " [tailport=\"P" + num + "\", arrowsize=.75]");
		if (NUM == 0 && printFirst)
			System.err.println(sb);

		return edge;
	}

	private ST_Agnode_s createNode(Dimension2D dim, int size, boolean isArray, int colAwidth, int colBwidth) {
		final String width = "" + (dim.getWidth() / 72);
		final String height = "" + (dim.getHeight() / 72);

		final ST_Agnode_s node = agnode(g, new CString("N" + num), true);
		node.NAME = "N " + num;
		num++;

		agsafeset(node, new CString("shape"), new CString("record"), new CString(""));
		agsafeset(node, new CString("height"), new CString("" + width), new CString(""));
		agsafeset(node, new CString("width"), new CString("" + height), new CString(""));

		final int lineHeight = 0;
		final String dotLabel = getDotLabel(size, isArray, colAwidth - 8, colBwidth - 8, lineHeight);
		if (size > 0) {
			agsafeset(node, new CString("label"), new CString(dotLabel), new CString(""));
		}

		StringBuilder sb = new StringBuilder();
		sb.append("N" + node.UID + " [");
		sb.append("shape=record, height=" + width + ", width=" + height + ", label=\"" + dotLabel.replace('x', '.')
				+ "\"]");
		if (NUM == 0 && printFirst)
			System.err.println(sb);

		return node;
	}

	private String getDotLabel(int size, boolean isArray, int widthA, int widthB, int height) {
		final StringBuilder sb = new StringBuilder("");
		if (isArray == false) {
			// "+height+"
			sb.append("{_dim_" + height + "_" + widthA + "_|{");
		}
		for (int i = 0; i < size; i++) {
			sb.append("<P" + i + ">");
			sb.append("_dim_" + height + "_" + widthB + "_");
			if (i < size - 1)
				sb.append("|");
		}
		if (isArray == false) {
			sb.append("}}");
		}

		return sb.toString();
	}

}
