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
 * Revision $Revision: 6939 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SignatureUtils;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.UDrawable;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;

final public class DotMaker extends DotCommon implements GraphvizMaker {

	private static boolean isJunit = false;

	private final List<String> dotStrings;

	private static String lastDotSignature;

	private final FileFormat fileFormat;

	private final boolean isVisibilityModifierPresent;

	// http://www.graphviz.org/bugs/b2114.html
	private static final boolean TURN_AROUND_B2114 = false;

	private static final boolean NOLABEL = false;

	private final Set<String> hasAlreadyOneIncommingArrowLenghtOne;

	final private Set<String> rankMin = new HashSet<String>();

	public static void goJunit() {
		isJunit = true;
	}

	public DotMaker(DotData data, List<String> dotStrings, FileFormat fileFormat) {
		super(fileFormat, data);
		this.dotStrings = dotStrings;
		this.fileFormat = fileFormat;
		if (data.getSkinParam().classAttributeIconSize() > 0) {
			this.isVisibilityModifierPresent = data.isThereVisibilityImages();
		} else {
			this.isVisibilityModifierPresent = false;
		}
		this.hasAlreadyOneIncommingArrowLenghtOne = TURN_AROUND_B2114 ? new HashSet<String>() : null;
	}

	public String createDotString() throws IOException {

		final StringBuilder sb = new StringBuilder();

		initPrintWriter(sb);
		printGroups(sb, null);
		printEntities(sb, getUnpackagedEntities());
		printLinks(sb, getData().getLinks());
		printRanking(sb);
		sb.append("}");

		// System.err.println(sb);
		if (isJunit) {
			lastDotSignature = SignatureUtils.getSignatureWithoutImgSrc(sb.toString());
		}
		return sb.toString();
	}

	private void printRanking(StringBuilder sb) {
		if (rankMin.size() == 0) {
			return;
		}
		sb.append("{ rank = min;");
		for (String id : rankMin) {
			sb.append(id);
			sb.append(";");
		}
		sb.append("}");

	}

	private void initPrintWriter(StringBuilder sb) {

		Log.info("Entities = " + getData().getEntities().size());
		final boolean huge = getData().getEntities().size() > 800;

		sb.append("digraph unix {");
		for (String s : dotStrings) {
			sb.append(s);
		}
		sb.append("bgcolor=\"" + getAsHtml(getData().getSkinParam().getBackgroundColor()) + "\";");
		if (huge) {
			sb.append("size=\"400,400;\"");
		} else {
			sb.append("ratio=auto;");
			// sb.append("concentrate=true;");
		}
		// sb.append("ordering=out;");
		sb.append("compound=true;");
		final DotSplines dotSplines = getData().getSkinParam().getDotSplines();
		final GraphvizLayoutStrategy strategy = getData().getSkinParam().getStrategy();
		if (dotSplines == DotSplines.ORTHO) {
			sb.append("splines=ortho;");
		} else if (dotSplines == DotSplines.POLYLINE) {
			sb.append("splines=polyline;");
		} else if (strategy != GraphvizLayoutStrategy.DOT) {
			sb.append("splines=true;");
		}

		// if (strategy == GraphvizLayoutStrategy.NEATO) {
		// sb.append("overlap=false;");
		// }
		if (strategy != GraphvizLayoutStrategy.DOT) {
			sb.append("layout=" + strategy.name().toLowerCase() + ";");
			sb.append("overlap=false;");
		}

		sb.append("remincross=true;");
		sb.append("searchsize=500;");
		if (getData().getRankdir() == Rankdir.LEFT_TO_RIGHT) {
			sb.append("rankdir=LR;");
		}

		if (getData().getDpi() != 96) {
			sb.append("dpi=" + getData().getDpi() + ";");
			sb.append("imagescale=both;");
		}
	}

	private Collection<IEntity> getUnpackagedEntities() {
		final List<IEntity> result = new ArrayList<IEntity>();
		for (IEntity ent : getData().getEntities().values()) {
			if (ent.getParent() == getData().getTopParent()) {
				result.add(ent);
			}
		}
		return result;
	}

	private void printGroups(StringBuilder sb, Group parent) throws IOException {
		for (Group g : getData().getGroupHierarchy().getChildrenGroups(parent)) {
			if (getData().isEmpty(g) && g.getType() == GroupType.PACKAGE) {
				final IEntity folder = new Entity(g.getUid1(), g.getUid2(), g.getCode(), g.getDisplay(),
						EntityType.EMPTY_PACKAGE, null, null);
				printEntity(sb, folder);
			} else {
				printGroup(sb, g);
			}
		}
	}

	private void printGroup(StringBuilder sb, Group g) throws IOException {
		if (g.getType() == GroupType.CONCURRENT_STATE) {
			return;
		}

		if (isSpecialGroup(g)) {
			printGroupSpecial(sb, g);
		} else {
			printGroupNormal(sb, g);
		}
	}

	private void printGroupNormal(StringBuilder sb, Group g) throws IOException {

		final String stereo = g.getStereotype();

		sb.append("subgraph " + g.getUid() + " {");
		// sb.append("margin=10;");

		final UFont font = getData().getSkinParam().getFont(getFontParamForGroup(), stereo);
		sb.append("fontsize=\"" + font.getSize() + "\";");
		final String fontFamily = font.getFamily(null);
		if (fontFamily != null) {
			sb.append("fontname=\"" + fontFamily + "\";");
		}

		if (g.getDisplay() != null) {
			sb.append("label=<" + manageHtmlIB(g.getDisplay(), getFontParamForGroup(), stereo) + ">;");
		}
		final String fontColor = getAsHtml(getData().getSkinParam().getFontHtmlColor(getFontParamForGroup(), stereo));
		sb.append("fontcolor=\"" + fontColor + "\";");

		if (getGroupBackColor(g) != null) {
			sb.append("fillcolor=\"" + getAsHtml(getGroupBackColor(g)) + "\";");
		}

		if (g.getType() == GroupType.STATE) {
			sb.append("color=" + getColorString(ColorParam.stateBorder, stereo) + ";");
		} else {
			sb.append("color=" + getColorString(ColorParam.packageBorder, stereo) + ";");
		}
		sb.append("style=\"" + getStyle(g) + "\";");

		printGroups(sb, g);

		this.printEntities(sb, g.entities().values());
		for (Link link : getData().getLinks()) {
			eventuallySameRank(sb, g, link);
		}
		sb.append("}");
	}

	private HtmlColor getGroupBackColor(Group g) {
		HtmlColor value = g.getBackColor();
		if (value == null) {
			value = getData().getSkinParam().getHtmlColor(ColorParam.packageBackground, null);
			// value = rose.getHtmlColor(this.getData().getSkinParam(),
			// ColorParam.packageBackground);
		}
		return value;
	}

	private void printGroupSpecial(StringBuilder sb, Group g) throws IOException {

		sb.append("subgraph " + g.getUid() + "a {");
		if (OptionFlags.getInstance().isDebugDot()) {
			sb.append("style=dotted;");
			sb.append("label=\"a\";");
		} else {
			sb.append("style=invis;");
			sb.append("label=\"\";");
		}

		sb.append("subgraph " + g.getUid() + "v {");
		sb.append("style=solid;");
		// sb.append("margin=10;");

		final List<Link> autolinks = getData().getAutoLinks(g);
		final List<Link> toEdgeLinks = getData().getToEdgeLinks(g);
		final List<Link> fromEdgeLinks = getData().getFromEdgeLinks(g);
		final boolean autoLabel = autolinks.size() == 1;

		final List<Link> nodesHiddenUidOut = getNodesHiddenUidOut(g);
		final List<Link> nodesHiddenUidIn = getNodesHiddenUidIn(g);
		final List<Link> nodesHiddenUid = new ArrayList<Link>(nodesHiddenUidOut);
		nodesHiddenUid.addAll(nodesHiddenUidIn);
		for (Link link : nodesHiddenUid) {
			final String uid = getHiddenNodeUid(g, link);
			// sb.append("subgraph " + g.getUid() + "k" + uid + " {");
			if (OptionFlags.getInstance().isDebugDot()) {
				sb.append("style=dotted;");
				sb.append("label=\"k" + uid + "\";");
			} else {
				sb.append("style=invis;");
				sb.append("label=\"\";");
			}
			if (OptionFlags.getInstance().isDebugDot()) {
				sb.append(uid + ";");
			} else {
				sb.append(uid + " [shape=point,width=.01,style=invis,label=\"\"];");
			}
			// sb.append("}"); // end of k
		}

		for (int j = 1; j < nodesHiddenUidOut.size(); j++) {
			for (int i = 0; i < j; i++) {
				final Link linki = nodesHiddenUidOut.get(i);
				final Link linkj = nodesHiddenUidOut.get(j);
				if (linki.getEntity2() != linkj.getEntity2()) {
					continue;
				}
				final String uidi = getHiddenNodeUid(g, linki);
				final String uidj = getHiddenNodeUid(g, linkj);
				if (OptionFlags.getInstance().isDebugDot()) {
					sb.append(uidi + "->" + uidj + ";");
				} else {
					sb.append(uidi + "->" + uidj + " [style=invis,arrowtail=none,arrowhead=none];");
				}

			}
		}

		if (autoLabel /* || toEdgeLinks.size() > 0 || fromEdgeLinks.size() > 0 */) {
			if (OptionFlags.getInstance().isDebugDot()) {
				sb.append(g.getUid() + "lmin;");
				sb.append(g.getUid() + "lmax;");
				sb.append(g.getUid() + "lmin->" + g.getUid() + "lmax [minlen=2]; ");
			} else {
				sb.append(g.getUid() + "lmin [shape=point,width=.01,style=invis,label=\"\"];");
				sb.append(g.getUid() + "lmax [shape=point,width=.01,style=invis,label=\"\"];");
				sb.append(g.getUid() + "lmin->" + g.getUid()
						+ "lmax [minlen=2,style=invis,arrowtail=none,arrowhead=none]; ");
			}
		}
		// sb.append(g.getUid() + "min->" + g.getUid() + "max;");

		final UFont font = getData().getSkinParam().getFont(getFontParamForGroup(), null);
		sb.append("fontsize=\"" + font.getSize() + "\";");
		final String fontFamily = font.getFamily(null);
		if (fontFamily != null) {
			sb.append("fontname=\"" + fontFamily + "\";");
		}

		if (g.getDisplay() != null) {
			final StringBuilder label = new StringBuilder(manageHtmlIB(g.getDisplay(), getFontParamForGroup(), null));
			if (g.getEntityCluster().getFieldsToDisplay().size() > 0) {
				label.append("<BR ALIGN=\"LEFT\"/>");
				for (Member att : g.getEntityCluster().getFieldsToDisplay()) {
					label.append(manageHtmlIB("  " + att.getDisplayWithVisibilityChar() + "  ",
							FontParam.STATE_ATTRIBUTE, null));
					label.append("<BR ALIGN=\"LEFT\"/>");
				}
			}
			sb.append("label=<" + label + ">;");
		}

		final String fontColor = getAsHtml(getData().getSkinParam().getFontHtmlColor(getFontParamForGroup(), null));
		sb.append("fontcolor=\"" + fontColor + "\";");
		final HtmlColor groupBackColor = getGroupBackColor(g);
		if (groupBackColor != null) {
			sb.append("fillcolor=\"" + getAsHtml(groupBackColor) + "\";");
		}
		if (g.getType() == GroupType.STATE) {
			sb.append("color=" + getColorString(ColorParam.stateBorder, null) + ";");
		} else {
			sb.append("color=" + getColorString(ColorParam.packageBorder, null) + ";");
		}
		sb.append("style=\"" + getStyle(g) + "\";");

		sb.append("subgraph " + g.getUid() + "i {");
		sb.append("label=\"i\";");
		if (OptionFlags.getInstance().isDebugDot()) {
			sb.append("style=dotted;");
			sb.append("label=\"i\";");
		} else {

			if (groupBackColor == null) {
				sb.append("style=invis;");
			} else {
				final String colorBack = getColorString(ColorParam.background, null);
				sb.append("fillcolor=" + colorBack + ";");
				sb.append("color=" + colorBack + ";");
				sb.append("style=\"filled,rounded\";");
			}
			sb.append("label=\"\";");

		}

		printGroups(sb, g);

		this.printEntities(sb, g.entities().values());
		for (Link link : getData().getLinks()) {
			eventuallySameRank(sb, g, link);
		}

		for (int i = 0; i < fromEdgeLinks.size(); i++) {
			if (OptionFlags.getInstance().isDebugDot()) {
				sb.append("eds" + i + ";");
			} else {
				sb.append("eds" + i + " [shape=point,width=.01,style=invis,label=\"\"];");
			}
			sb.append("eds" + i + " ->" + fromEdgeLinks.get(i).getEntity2().getUid()
					+ " [minlen=2,style=invis,arrowtail=none,arrowhead=none]; ");

		}

		sb.append("}"); // end of i
		sb.append("}"); // end of v

		if (autoLabel) {
			sb.append("subgraph " + g.getUid() + "l {");
			if (OptionFlags.getInstance().isDebugDot()) {
				sb.append("style=dotted;");
				sb.append("label=\"l\";");
			} else {
				sb.append("style=invis;");
				sb.append("label=\"\";");
			}
			final String decorationColor = ",color=" + getColorString(getArrowColorParam(), null);

			sb.append(g.getUid() + "lab0 [shape=point,width=.01,label=\"\"" + decorationColor + "]");
			String autolabel = autolinks.get(0).getLabel();
			if (autolabel == null) {
				autolabel = "";
			}
			sb.append(g.getUid() + "lab1 [label=<" + manageHtmlIB(autolabel, getArrowFontParam(), null)
					+ ">,shape=plaintext,margin=0];");
			sb.append(g.getUid() + "lab0 -> " + g.getUid() + "lab1 [minlen=0,style=invis];");
			sb.append("}"); // end of l

			sb.append(g.getUid() + "lmin -> " + g.getUid() + "lab0 [ltail=" + g.getUid()
					+ "v,arrowtail=none,arrowhead=none" + decorationColor + "];");
			sb.append(g.getUid() + "lab0 -> " + g.getUid() + "lmax [lhead=" + g.getUid()
					+ "v,arrowtail=none,arrowhead=open" + decorationColor + "];");
		}

		for (int i = 0; i < fromEdgeLinks.size(); i++) {
			sb.append("subgraph " + g.getUid() + "ed" + i + " {");
			if (OptionFlags.getInstance().isDebugDot()) {
				sb.append("style=dotted;");
				sb.append("label=\"ed" + i + "\";");
			} else {
				sb.append("style=invis;");
				sb.append("label=\"\";");
			}
			final String decorationColor = ",color=" + getColorString(getArrowColorParam(), null);
			String label = fromEdgeLinks.get(i).getLabel();
			if (label == null) {
				label = "";
			}

			sb.append(g.getUid() + "fedge" + i + " [shape=point,width=.01,label=\"\"" + decorationColor + "]");
			sb.append("}"); // end of ed
			sb.append("eds" + i + " -> " + g.getUid() + "fedge" + i + " [ltail=" + g.getUid()
					+ "v,arrowtail=none,arrowhead=none" + decorationColor + "];");
			sb.append(g.getUid() + "fedge" + i + " -> " + fromEdgeLinks.get(i).getEntity2().getUid()
					+ "[arrowtail=none,arrowhead=open" + decorationColor);
			sb.append(",label=<" + manageHtmlIB(label, getArrowFontParam(), null) + ">];");

		}
		sb.append("}"); // end of a
	}

	private FontParam getFontParamForGroup() {
		if (getData().getUmlDiagramType() == UmlDiagramType.STATE) {
			return FontParam.STATE;
		}
		return FontParam.PACKAGE;
	}

	private String getStyle(Group g) {
		final StringBuilder sb = new StringBuilder();
		if (g.isBold()) {
			sb.append("bold");
		} else if (g.isDashed()) {
			sb.append("dashed");
		} else {
			sb.append("solid");

		}
		if (getGroupBackColor(g) != null) {
			sb.append(",filled");
		}
		if (g.isRounded()) {
			sb.append(",rounded");
		}
		return sb.toString();
	}

	private void printLinks(StringBuilder sb, List<Link> links) throws IOException {
		for (Link link : appendPhantomLink(links)) {
			final IEntity entity1 = link.getEntity1();
			final IEntity entity2 = link.getEntity2();
			if (entity1 == entity2 && entity1.getType() == EntityType.GROUP) {
				continue;
			}
			if (entity1.getType() == EntityType.GROUP && entity2.getParent() == entity1.getParent()) {
				continue;
			}
			if (entity2.getType() == EntityType.GROUP && entity1.getParent() == entity2.getParent()) {
				continue;
			}
			if (entity1.getType() == EntityType.LOLLIPOP || entity2.getType() == EntityType.LOLLIPOP) {
				continue;
			}
			// System.err.println("outing " + link);
			printLink(sb, link);
		}
	}

	private void printLink(StringBuilder sb, Link link) throws IOException {
		final StringBuilder decoration = getLinkDecoration(link);

		if (link.getWeight() > 1) {
			decoration.append("weight=" + link.getWeight() + ",");
		}
		if (link.getLabeldistance() != null) {
			decoration.append("labeldistance=" + link.getLabeldistance() + ",");
		}
		if (link.getLabelangle() != null) {
			decoration.append("labelangle=" + link.getLabelangle() + ",");
		}
		if (link.isConstraint() == false) {
			decoration.append("constraint=false,");
		}

		final DrawFile noteLink = link.getImageFile();

		boolean hasLabel = false;

		if (link.getLabel() != null && noteLink != null) {
			decoration.append("label=<"
					+ getHtmlForLinkNote(noteLink.getPngOrEps(fileFormat), manageHtmlIB(link.getLabel(),
							getArrowFontParam(), null), link.getNotePosition()) + ">,");
			hasLabel = true;
		} else if (link.getLabel() != null) {
			decoration.append("label=<" + manageHtmlIB(link.getLabel(), getArrowFontParam(), null) + ">,");
			hasLabel = true;
		} else if (noteLink != null) {
			decoration.append("label=<" + getHtmlForLinkNote(noteLink.getPngOrEps(fileFormat)) + ">,");
			hasLabel = true;
		}

		if (link.getQualifier1() != null) {
			decoration.append("taillabel=<" + manageHtmlIB(link.getQualifier1(), getArrowFontParam(), null) + ">,");
		}
		if (link.getQualifier2() != null) {
			decoration.append("headlabel=<" + manageHtmlIB(link.getQualifier2(), getArrowFontParam(), null) + ">,");
		}
		final int len = link.getLength();
		String uid1 = link.getEntity1().getUid();
		String uid2 = link.getEntity2().getUid();
		LinkType typeToDraw = link.getType();
		if (TURN_AROUND_B2114 && len == 1 && hasAlreadyOneIncommingArrowLenghtOne.contains(uid2) && hasLabel) {
			typeToDraw = typeToDraw.getInv();
		}
		if (TURN_AROUND_B2114 && len == 1) {
			hasAlreadyOneIncommingArrowLenghtOne.add(uid2);
		}
		decoration.append(typeToDraw.getSpecificDecoration());
		if (link.isInvis()) {
			decoration.append(",style=invis");
		}

		final String lenString = len >= 3 ? ",minlen=" + (len - 1) : "";

		if (link.getEntity1().getType() == EntityType.GROUP) {
			uid1 = getHiddenNodeUid(link.getEntity1().getParent(), link);
			decoration.append(",ltail=" + link.getEntity1().getParent().getUid() + "v");
		}
		if (link.getEntity2().getType() == EntityType.GROUP) {
			uid2 = getHiddenNodeUid(link.getEntity2().getParent(), link);
			decoration.append(",lhead=" + link.getEntity2().getParent().getUid() + "v");
		}

		final boolean margin1 = MODE_MARGIN && link.getEntity1().hasNearDecoration();
		final boolean margin2 = MODE_MARGIN && link.getEntity2().hasNearDecoration();

		sb.append(uid1);
		if (margin1) {
			sb.append(":h");
		}
		sb.append(" -> ");
		sb.append(uid2);
		if (margin2) {
			sb.append(":h");
		}
		sb.append(decoration);
		sb.append(lenString + "];");
		eventuallySameRank(sb, getData().getTopParent(), link);
	}

	private List<Link> getNodesHiddenUidOut(Group g) {
		final List<Link> result = new ArrayList<Link>();
		for (Link link : getData().getLinks()) {
			if (link.getEntity1().getParent() == link.getEntity2().getParent()) {
				continue;
			}
			if (link.getEntity1().getType() == EntityType.GROUP && link.getEntity1().getParent() == g) {
				result.add(link);
			}
		}
		return Collections.unmodifiableList(result);
	}

	private List<Link> getNodesHiddenUidIn(Group g) {
		final List<Link> result = new ArrayList<Link>();
		for (Link link : getData().getLinks()) {
			if (link.getEntity1().getParent() == link.getEntity2().getParent()) {
				continue;
			}
			if (link.getEntity2().getType() == EntityType.GROUP && link.getEntity2().getParent() == g) {
				result.add(link);
			}
		}
		return Collections.unmodifiableList(result);
	}

	private String getHiddenNodeUid(Group g, Link link) {
		if (getData().isEmpty(g) && g.getType() == GroupType.PACKAGE) {
			return g.getUid();
		}
		return g.getUid() + "_" + link.getUid();
	}

	private StringBuilder getLinkDecoration(Link link) {
		final StringBuilder decoration = new StringBuilder("[color=");
		if (link.getSpecificColor() == null) {
			decoration.append(getColorString(getArrowColorParam(), null));
		} else {
			decoration.append("\"" + getAsHtml(link.getSpecificColor()) + "\"");
		}
		decoration.append(",");

		decoration.append("fontcolor=" + getFontColorString(getArrowFontParam(), null) + ",");
		final UFont font = getData().getSkinParam().getFont(getArrowFontParam(), null);
		decoration.append("fontsize=\"" + font.getSize() + "\",");

		final String fontName = font.getFamily(null);
		if (fontName != null) {
			decoration.append("fontname=\"" + fontName + "\",");
		}
		return decoration;
	}

	private List<Link> appendPhantomLink(List<Link> links) {
		final List<Link> result = new ArrayList<Link>(links);
		for (Link link : links) {
			if (link.getLength() != 1) {
				continue;
			}
			final DrawFile noteLink = link.getImageFile();
			if (noteLink == null) {
				continue;
			}
			final Link phantom = new Link(link.getEntity1(), link.getEntity2(), link.getType(), null, link.getLength());
			phantom.setInvis(true);
			result.add(phantom);
		}
		return result;
	}

	private String getHtmlForLinkNote(File image) {
		final String circleInterfaceAbsolutePath = StringUtils.getPlateformDependentAbsolutePath(image);
		final StringBuilder sb = new StringBuilder();
		sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">");
		sb.append("<TR><TD><IMG SRC=\"" + circleInterfaceAbsolutePath + "\"/></TD></TR>");
		sb.append("</TABLE>");
		return sb.toString();

	}

	private String getHtmlForLinkNote(File image, String labelHtml, Position position) {
		final String imagePath = StringUtils.getPlateformDependentAbsolutePath(image);
		final StringBuilder sb = new StringBuilder();
		sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"1\" CELLPADDING=\"0\">");
		switch (position) {
		case TOP:
			sb.append("<TR><TD><IMG SRC=\"" + imagePath + "\"/></TD></TR>");
			sb.append("<TR><TD>" + labelHtml + "</TD></TR>");
			break;
		case RIGHT:
			sb.append("<TR><TD><IMG SRC=\"" + imagePath + "\"/></TD>");
			sb.append("<TD>" + labelHtml + "</TD></TR>");
			break;
		case LEFT:
			sb.append("<TR><TD>" + labelHtml + "</TD>");
			sb.append("<TD><IMG SRC=\"" + imagePath + "\"/></TD></TR>");
			break;
		default:
			sb.append("<TR><TD>" + labelHtml + "</TD></TR>");
			sb.append("<TR><TD><IMG SRC=\"" + imagePath + "\"/></TD></TR>");
			break;
		}
		sb.append("</TABLE>");
		return sb.toString();

	}

	private FontParam getArrowFontParam() {
		if (getData().getUmlDiagramType() == UmlDiagramType.CLASS) {
			return FontParam.CLASS_ARROW;
		} else if (getData().getUmlDiagramType() == UmlDiagramType.OBJECT) {
			return FontParam.OBJECT_ARROW;
		} else if (getData().getUmlDiagramType() == UmlDiagramType.USECASE) {
			return FontParam.USECASE_ARROW;
		} else if (getData().getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			return FontParam.ACTIVITY_ARROW;
		} else if (getData().getUmlDiagramType() == UmlDiagramType.COMPONENT) {
			return FontParam.COMPONENT_ARROW;
		} else if (getData().getUmlDiagramType() == UmlDiagramType.STATE) {
			return FontParam.STATE_ARROW;
		}
		throw new IllegalStateException();
	}

	private ColorParam getArrowColorParam() {
		if (getData().getUmlDiagramType() == UmlDiagramType.CLASS) {
			return ColorParam.classArrow;
		} else if (getData().getUmlDiagramType() == UmlDiagramType.OBJECT) {
			return ColorParam.objectArrow;
		} else if (getData().getUmlDiagramType() == UmlDiagramType.USECASE) {
			return ColorParam.usecaseArrow;
		} else if (getData().getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			return ColorParam.activityArrow;
		} else if (getData().getUmlDiagramType() == UmlDiagramType.COMPONENT) {
			return ColorParam.componentArrow;
		} else if (getData().getUmlDiagramType() == UmlDiagramType.STATE) {
			return ColorParam.stateArrow;
		}
		throw new IllegalStateException();
	}

	private String getFontColorString(FontParam fontParam, String stereotype) {
		return "\"" + getAsHtml(getFontHtmlColor(fontParam, stereotype)) + "\"";
	}

	private void eventuallySameRank(StringBuilder sb, Group entityPackage, Link link) {
		final int len = link.getLength();
		if (len == 1 && link.getEntity1().getParent() == entityPackage
				&& link.getEntity2().getParent() == entityPackage) {
			if (link.getEntity1().getType() == EntityType.GROUP) {
				throw new IllegalArgumentException();
			}
			if (link.getEntity2().getType() == EntityType.GROUP) {
				throw new IllegalArgumentException();
			}
			sb.append("{rank=same; " + link.getEntity1().getUid() + "; " + link.getEntity2().getUid() + "}");
		}
	}

	private boolean MODE_LOLLIPOP_BETA = false;

	class EntityComparator implements Comparator<IEntity> {
		public int compare(IEntity e1, IEntity e2) {
			final int xpos1 = e1.getXposition();
			final int xpos2 = e2.getXposition();
			if (xpos1 < xpos2) {
				return -1;
			}
			if (xpos1 > xpos2) {
				return 1;
			}
			return e1.compareTo(e2);
		}
	}

	class EntityComparator2 implements Comparator<IEntity> {
		private final Map<IEntity, Integer> map;

		public EntityComparator2(Map<IEntity, Integer> map) {
			this.map = map;
		}

		public int compare(IEntity e1, IEntity e2) {
			final Integer b1 = map.get(e1);
			final Integer b2 = map.get(e2);
			final int cmp = b1.compareTo(b2);
			if (cmp != 0) {
				return -cmp;
			}
			return e1.compareTo(e2);
		}
	}

	private Map<IEntity, Integer> getMap(Collection<? extends IEntity> entities2) {
		final Map<IEntity, Integer> map = new HashMap<IEntity, Integer>();
		for (IEntity ent : entities2) {
			map.put(ent, Integer.valueOf(0));
		}
		for (Link link : getData().getLinks()) {
			if (link.isConstraint() == false) {
				map.put(link.getEntity2(), Integer.valueOf(1));
			} else if (link.getLength() == 1 && link.isInverted()) {
				// map.put(link.getEntity2(), true);
				map.put(link.getEntity1(), Integer.valueOf(1));
			}

		}
		return map;
	}

	private void printEntities(StringBuilder sb, Collection<? extends IEntity> entities2) throws IOException {
		final List<IEntity> entities = new ArrayList<IEntity>(entities2);
		// Collections.sort(entities, new EntityComparator());
		// if (getData().getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
		Collections.sort(entities, new EntityComparator2(getMap(entities2)));
		// }
		// Collections.sort(entities);
		final Set<IEntity> lollipops = new HashSet<IEntity>();
		final Set<IEntity> lollipopsFriends = new HashSet<IEntity>();
		for (IEntity entity : entities) {
			if (entity.getType() == EntityType.LOLLIPOP) {
				lollipops.add(entity);
				if (MODE_LOLLIPOP_BETA == false) {
					lollipopsFriends.add(getConnectedToLollipop(entity));
				}
			}
		}

		if (MODE_LOLLIPOP_BETA) {
			for (IEntity entity : entities) {
				if (lollipops.contains(entity)) {
					continue;
				}
				printEntity(sb, entity);
			}
		} else {
			for (IEntity entity : entities) {
				if (lollipops.contains(entity) || lollipopsFriends.contains(entity)) {
					continue;
				}
				printEntity(sb, entity);
			}

			for (IEntity ent : lollipopsFriends) {
				sb.append("subgraph cluster" + ent.getUid() + "lol {");
				sb.append("style=invis;");
				sb.append("label=\"\";");
				printEntity(sb, ent);
				for (IEntity lollipop : getAllLollipop(ent)) {
					final Link link = getLinkLollipop(lollipop, ent);
					final String headOrTail = getHeadOrTail(lollipop, link);
					printEntity(sb, lollipop, headOrTail);
					printLink(sb, link);
				}
				sb.append("}");
			}
		}

	}

	private Collection<IEntity> getAllLollipop(IEntity entity) {
		final Collection<IEntity> result = new ArrayList<IEntity>();
		for (IEntity lollipop : getData().getAllLinkedDirectedTo(entity)) {
			if (lollipop.getType() == EntityType.LOLLIPOP) {
				result.add(lollipop);
			}
		}
		return result;
	}

	private IEntity getConnectedToLollipop(IEntity lollipop) {
		assert lollipop.getType() == EntityType.LOLLIPOP;
		final Collection<IEntity> linked = getData().getAllLinkedDirectedTo(lollipop);
		if (linked.size() != 1) {
			throw new IllegalStateException("size=" + linked.size());
		}
		return linked.iterator().next();
	}

	private Link getLinkLollipop(IEntity lollipop, IEntity ent) {
		assert lollipop.getType() == EntityType.LOLLIPOP;
		for (Link link : getData().getLinks()) {
			if (link.isBetween(lollipop, ent)) {
				return link;
			}
		}
		throw new IllegalArgumentException();
	}

	private void printEntity(StringBuilder sb, IEntity entity, String headOrTail) throws IOException {
		final EntityType type = entity.getType();
		if (type == EntityType.LOLLIPOP) {
			final String color1 = getColorString(ColorParam.classBackground, null);
			final String color2 = getColorString(ColorParam.classBorder, null);
			final String colorBack = getColorString(ColorParam.background, null);
			final String labelLo = manageHtmlIB(StringUtils.getMergedLines(entity.getDisplay2()),
					FontParam.CLASS_ATTRIBUTE, null);
			sb.append(entity.getUid() + " [fillcolor=" + color1 + ",color=" + color2 + ",style=\"filled\","
					+ "shape=circle,width=0.12,height=0.12,label=\"\"];");
			sb.append(entity.getUid() + " -> " + entity.getUid() + "[color=" + colorBack
					+ ",arrowtail=none,arrowhead=none," + headOrTail + "=<" + labelLo + ">];");
		} else {
			throw new IllegalStateException(type.toString() + " " + getData().getUmlDiagramType());
		}

	}

	static final boolean MODE_MARGIN = true;
	static public final boolean MODE_BRANCHE_CLUSTER = false;

	private void printEntity(StringBuilder sb, IEntity entity) throws IOException {
		final EntityType type = entity.getType();
		final String label = NOLABEL ? "label=\"" + entity.getUid() + "\"" : getLabel(entity);
		if (type == EntityType.GROUP) {
			return;
		}
		boolean closeBracket = false;
		final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();
		if (type == EntityType.ABSTRACT_CLASS || type == EntityType.CLASS || type == EntityType.INTERFACE
				|| type == EntityType.ENUM) {
			String dec;
			if (MODE_MARGIN && entity.hasNearDecoration() || MODE_LOLLIPOP_BETA) {
				dec = " [fontcolor=" + getFontColorString(FontParam.CLASS, stereo) + "color="
						+ getBackColorAroundEntity(entity) + ",margin=0,style=filled,shape=plaintext," + label;
			} else {
				dec = " [fontcolor=" + getFontColorString(FontParam.CLASS, stereo) + ",margin=0,fillcolor="
						+ getColorString(ColorParam.classBackground, stereo) + ",color="
						+ getColorString(ColorParam.classBorder, stereo) + ",style=filled,shape=box," + label;

			}
			sb.append(entity.getUid() + dec);
		} else if (type == EntityType.OBJECT) {
			sb.append(entity.getUid() + " [fontcolor=" + getFontColorString(FontParam.CLASS, stereo)
					+ ",margin=0,fillcolor=" + getColorString(ColorParam.classBackground, stereo) + ",color="
					+ getColorString(ColorParam.classBorder, stereo) + ",style=filled,shape=record," + label);
		} else if (type == EntityType.USECASE) {
			sb.append(entity.getUid() + " [fontcolor=" + getFontColorString(FontParam.USECASE, stereo) + ",fillcolor="
					+ getColorString(ColorParam.usecaseBackground, stereo) + ",color="
					+ getColorString(ColorParam.usecaseBorder, stereo) + ",style=filled," + label);
		} else if (type == EntityType.ACTOR) {
			sb.append(entity.getUid() + " [fontcolor=" + getFontColorString(FontParam.USECASE_ACTOR, stereo)
					+ ",margin=0,shape=plaintext," + label);
		} else if (type == EntityType.CIRCLE_INTERFACE) {
			sb.append(entity.getUid() + " [margin=0,shape=plaintext," + label);
		} else if (type == EntityType.COMPONENT) {
			sb.append(entity.getUid() + " [margin=0.2,fontcolor=" + getFontColorString(FontParam.COMPONENT, stereo)
					+ ",fillcolor=" + getColorString(ColorParam.componentBackground, stereo) + ",color="
					+ getColorString(ColorParam.componentBorder, stereo) + ",style=filled,shape=component," + label);
		} else if (type == EntityType.NOTE && getData().getDpi() != 96) {
			sb.append(entity.getUid() + " [margin=0,pad=0,shape=plaintext,label=" + getLabelForNoteDpi(entity));
		} else if (type == EntityType.NOTE) {
			final DrawFile file = entity.getImageFile();
			if (file == null) {
				throw new IllegalStateException("No file for NOTE");
			}
			if (file.getPngOrEps(fileFormat).exists() == false) {
				throw new IllegalStateException();
			}
			final String absolutePath = StringUtils.getPlateformDependentAbsolutePath(file.getPngOrEps(fileFormat));
			sb.append(entity.getUid() + " [margin=0,pad=0," + label + ",shape=none,image=\"" + absolutePath + "\"");
		} else if (type == EntityType.ACTIVITY) {
			String shape = "octagon";
			if (getData().getSkinParam().useOctagonForActivity() == false || entity.getImageFile() != null) {
				shape = "rect";
			}
			sb.append(entity.getUid() + " [fontcolor=" + getFontColorString(FontParam.ACTIVITY, stereo) + ",fillcolor="
					+ getBackColorOfEntity(entity) + ",color=" + getColorString(ColorParam.activityBorder, stereo)
					+ ",style=\"rounded,filled\",shape=" + shape + "," + label);
		} else if (type == EntityType.BRANCH) {
			if (MODE_BRANCHE_CLUSTER) {
				sb.append("subgraph cluster" + entity.getUid() + "br {");
				sb.append("label=<"
						+ manageHtmlIB(StringUtils.getMergedLines(entity.getDisplay2()), FontParam.ACTIVITY, null)
						+ ">;");
				sb.append("color=" + getColorString(ColorParam.background, null) + ";");
			}
			sb.append(entity.getUid() + " [fillcolor=" + getBackColorOfEntity(entity) + ",color="
					+ getColorString(ColorParam.activityBorder, stereo)
					+ ",style=\"filled\",shape=diamond,height=.25,width=.25,label=\"\"");
			if (MODE_BRANCHE_CLUSTER) {
				closeBracket = true;
			}
			// if (StringUtils.isNotEmpty(entity.getDisplay())) {
			// sb.append(entity.getUid() + "->" + entity.getUid() +
			// "[taillabel=\"" + entity.getDisplay()
			// + "\",arrowtail=none,arrowhead=none,color=\"white\"];");
			// }
		} else if (type == EntityType.ASSOCIATION) {
			sb.append(entity.getUid() + " [fillcolor=" + getColorString(ColorParam.classBackground, stereo) + ",color="
					+ getColorString(ColorParam.classBorder, stereo)
					+ ",style=\"filled\",shape=diamond,height=.25,width=.25,label=\"\"");
		} else if (type == EntityType.SYNCHRO_BAR) {
			final String color = getColorString(ColorParam.activityBar, null);
			sb.append(entity.getUid() + " [fillcolor=" + color + ",color=" + color + ",style=\"filled\","
					+ "shape=rect,height=.08,width=1.30,label=\"\"");
		} else if (type == EntityType.CIRCLE_START) {
			final String color = getColorString(getStartColorParam(), null);
			sb.append(entity.getUid() + " [fillcolor=" + color + ",color=" + color + ",style=\"filled\","
					+ "shape=circle,width=.20,height=.20,label=\"\"");
		} else if (type == EntityType.CIRCLE_END) {
			final String color = getColorString(getEndColorParam(), null);
			sb.append(entity.getUid() + " [fillcolor=" + color + ",color=" + color + ",style=\"filled\","
					+ "shape=doublecircle,width=.13,height=.13,label=\"\"");
		} else if (type == EntityType.PSEUDO_STATE) {
			final String color = getColorString(getStartColorParam(), null);
			sb.append(entity.getUid() + " [color=" + color + "," + "shape=circle,width=.01,height=.01," + label);
		} else if (type == EntityType.POINT_FOR_ASSOCIATION) {
			sb
					.append(entity.getUid() + " [width=.05,shape=point,color="
							+ getColorString(ColorParam.classBorder, null));
		} else if (type == EntityType.STATE) {
			sb.append(entity.getUid() + " [color=" + getColorString(ColorParam.stateBorder, stereo)
					+ ",shape=record,style=\"rounded,filled\",color=" + getColorString(ColorParam.stateBorder, stereo));
			if (entity.getImageFile() == null) {
				sb.append(",fillcolor=" + getBackColorOfEntity(entity));
			} else {
				sb.append(",fillcolor=" + getBackColorOfEntity(entity));
				// sb.append(",fillcolor=\"" +
				// getData().getSkinParam().getBackgroundColor().getAsHtml() +
				// "\"");
			}
			sb.append("," + label);
		} else if (type == EntityType.STATE_CONCURRENT) {
			final DrawFile file = entity.getImageFile();
			if (file == null) {
				throw new IllegalStateException();
			}
			if (file.getPng().exists() == false) {
				throw new IllegalStateException();
			}
			final String absolutePath = StringUtils.getPlateformDependentAbsolutePath(file.getPng());
			sb.append(entity.getUid() + " [margin=1,pad=1," + label + ",style=dashed,shape=box,image=\"" + absolutePath
					+ "\"");
		} else if (type == EntityType.ACTIVITY_CONCURRENT) {
			final DrawFile file = entity.getImageFile();
			if (file == null) {
				throw new IllegalStateException();
			}
			if (file.getPng().exists() == false) {
				throw new IllegalStateException();
			}
			final String absolutePath = StringUtils.getPlateformDependentAbsolutePath(file.getPng());
			sb.append(entity.getUid() + " [margin=0,pad=0," + label + ",style=dashed,shape=box,image=\"" + absolutePath
					+ "\"");
		} else if (type == EntityType.EMPTY_PACKAGE) {
			sb.append(entity.getUid() + " [margin=0.2,fontcolor=" + getFontColorString(FontParam.PACKAGE, null)
					+ ",fillcolor=" + getColorString(ColorParam.packageBackground, null) + ",color="
					+ getColorString(ColorParam.packageBorder, null) + ",style=filled,shape=tab," + label);
		} else {
			throw new IllegalStateException(type.toString() + " " + getData().getUmlDiagramType());
		}

		if (this.getData().hasUrl() && entity.getUrl() != null) {
			final Url url = entity.getUrl();
			sb.append(",URL=\"" + url.getUrl() + "\"");
			sb.append(",tooltip=\"" + url.getTooltip() + "\"");
		}

		sb.append("];");
		if (closeBracket) {
			sb.append("}");
		}

		if (entity.isTop()) {
			rankMin.add(entity.getUid());
		}

	}

	private ColorParam getEndColorParam() {
		if (getData().getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			return ColorParam.activityEnd;
		}
		if (getData().getUmlDiagramType() == UmlDiagramType.STATE) {
			return ColorParam.stateEnd;
		}
		throw new IllegalStateException(getData().getUmlDiagramType().toString());
	}

	private ColorParam getStartColorParam() {
		if (getData().getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			return ColorParam.activityStart;
		}
		if (getData().getUmlDiagramType() == UmlDiagramType.STATE) {
			return ColorParam.stateStart;
		}
		throw new IllegalStateException(getData().getUmlDiagramType().toString());
	}

	private String getHeadOrTail(IEntity lollipop, Link link) {
		assert lollipop.getType() == EntityType.LOLLIPOP;
		if (link.getLength() > 1 && link.getEntity1() == lollipop) {
			return "taillabel";
		}
		return "headlabel";
	}

	private String getLabel(IEntity entity) throws IOException {
		if (entity.getType() == EntityType.ABSTRACT_CLASS || entity.getType() == EntityType.CLASS
				|| entity.getType() == EntityType.INTERFACE || entity.getType() == EntityType.ENUM) {
			return "label=" + getLabelForClassOrInterfaceOrEnum(entity);
		} else if (entity.getType() == EntityType.LOLLIPOP) {
			return "label=" + getLabelForLollipop(entity);
		} else if (entity.getType() == EntityType.OBJECT) {
			return "label=" + getLabelForObject(entity);
		} else if (entity.getType() == EntityType.ACTOR) {
			return "label=" + getLabelForActor(entity);
		} else if (entity.getType() == EntityType.CIRCLE_INTERFACE) {
			return "label=" + getLabelForCircleInterface(entity);
		} else if (entity.getType() == EntityType.NOTE) {
			return "label=\"\"";
		} else if (entity.getType() == EntityType.STATE_CONCURRENT) {
			return "label=\"\"";
		} else if (entity.getType() == EntityType.ACTIVITY_CONCURRENT) {
			return "label=\"\"";
		} else if (entity.getType() == EntityType.COMPONENT) {
			return "label=" + getLabelForComponent(entity);
		} else if (entity.getType() == EntityType.ACTIVITY) {
			final DrawFile drawFile = entity.getImageFile();
			if (drawFile != null) {
				final String path = StringUtils.getPlateformDependentAbsolutePath(drawFile.getPng());
				final String bgcolor = "\"" + getAsHtml(getData().getSkinParam().getBackgroundColor()) + "\"";
				final StringBuilder sb = new StringBuilder("label=<");
				sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
				sb.append("<TR>");
				sb.append("<TD BGCOLOR=" + bgcolor
						+ " BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"2\">");
				sb.append("<IMG SRC=\"" + path + "\"/></TD></TR>");
				sb.append("</TABLE>");
				sb.append(">");
				return sb.toString();
			}
			final String stereotype = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();
			return "label=" + getSimpleLabelAsHtml(entity, FontParam.ACTIVITY, stereotype);
		} else if (entity.getType() == EntityType.EMPTY_PACKAGE) {
			return "label=" + getSimpleLabelAsHtml(entity, getFontParamForGroup(), null);
		} else if (entity.getType() == EntityType.USECASE) {
			return "label=" + getLabelForUsecase(entity);
		} else if (entity.getType() == EntityType.STATE) {
			return "label=" + getLabelForState(entity);
		} else if (entity.getType() == EntityType.BRANCH) {
			return "label=\"\"";
		} else if (entity.getType() == EntityType.PSEUDO_STATE) {
			return "label=\"H\"";
		}
		return "label=\"" + StringUtils.getMergedLines(entity.getDisplay2()) + "\"";
	}

	private String getSimpleLabelAsHtml(IEntity entity, FontParam param, String stereotype) {
		return "<" + manageHtmlIB(StringUtils.getMergedLines(entity.getDisplay2()), param, stereotype) + ">";
	}

	private String getBackColorOfEntity(IEntity entity) {
		if (entity.getSpecificBackColor() != null) {
			return "\"" + getAsHtml(entity.getSpecificBackColor()) + "\"";
		}
		final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();
		if (entity.getType() == EntityType.STATE || entity.getType() == EntityType.STATE_CONCURRENT) {
			return getColorString(ColorParam.stateBackground, stereo);
		}
		if (entity.getType() == EntityType.ACTIVITY || entity.getType() == EntityType.ACTIVITY_CONCURRENT
				|| entity.getType() == EntityType.BRANCH) {
			return getColorString(ColorParam.activityBackground, stereo);
		}
		throw new IllegalArgumentException(entity.getType().toString());
	}

	private String getLabelForState(IEntity entity) throws IOException {
		final DrawFile cFile = entity.getImageFile();
		final String stateBgcolor = getBackColorOfEntity(entity);

		final String stereotype = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();

		final StringBuilder sb = new StringBuilder("<{<TABLE BGCOLOR=" + stateBgcolor
				+ " BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">");
		sb.append("<TR><TD>"
				+ manageHtmlIB(StringUtils.getMergedLines(entity.getDisplay2()), FontParam.STATE, stereotype)
				+ "</TD></TR>");
		sb.append("</TABLE>");

		if (entity.getFieldsToDisplay().size() > 0) {
			sb.append("|");
			for (Member att : entity.getFieldsToDisplay()) {
				sb.append(manageHtmlIB(att.getDisplayWithVisibilityChar(), FontParam.STATE_ATTRIBUTE, stereotype));
				sb.append("<BR ALIGN=\"LEFT\"/>");
			}
		}

		if (cFile != null) {
			sb.append("|");
			final String path = StringUtils.getPlateformDependentAbsolutePath(cFile.getPng());
			final String bgcolor;
			if (OptionFlags.PBBACK) {
				bgcolor = stateBgcolor;
			} else {
				bgcolor = "\"" + getAsHtml(getData().getSkinParam().getBackgroundColor()) + "\"";
			}
			// PBBACK

			sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
			sb.append("<TR>");
			sb.append("<TD BGCOLOR=" + bgcolor + " BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"2\">");
			sb.append("<IMG SRC=\"" + path + "\"/></TD></TR>");
			sb.append("</TABLE>");
		}

		if (getData().isHideEmptyDescription() == false && entity.getFieldsToDisplay().size() == 0 && cFile == null) {
			sb.append("|");
		}

		sb.append("}>");

		return sb.toString();
	}

	private String getLabelForUsecase(IEntity entity) {
		final Stereotype stereotype = getStereotype(entity);
		final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();
		if (stereotype == null) {
			return getSimpleLabelAsHtml(entity, FontParam.USECASE, stereo);
		}
		final StringBuilder sb = new StringBuilder("<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">");
		if (isThereLabel(stereotype)) {
			sb.append("<TR><TD>" + manageHtmlIB(stereotype.getLabel(), FontParam.USECASE_STEREOTYPE, stereo)
					+ "</TD></TR>");
		}
		sb.append("<TR><TD>"
				+ manageHtmlIB(StringUtils.getMergedLines(entity.getDisplay2()), FontParam.USECASE, stereo)
				+ "</TD></TR>");
		sb.append("</TABLE>>");
		return sb.toString();
	}

	private String getLabelForComponent(IEntity entity) {
		final Stereotype stereotype = getStereotype(entity);
		if (stereotype == null) {
			return getSimpleLabelAsHtml(entity, FontParam.COMPONENT, null);
		}
		final String stereo = stereotype.getLabel();
		final StringBuilder sb = new StringBuilder("<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">");
		if (isThereLabel(stereotype)) {
			sb.append("<TR><TD>" + manageHtmlIB(stereotype.getLabel(), FontParam.COMPONENT_STEREOTYPE, stereo)
					+ "</TD></TR>");
		}
		sb.append("<TR><TD>"
				+ manageHtmlIB(StringUtils.getMergedLines(entity.getDisplay2()), FontParam.COMPONENT, stereo)
				+ "</TD></TR>");
		sb.append("</TABLE>>");
		return sb.toString();
	}

	private String getLabelForNoteDpi(IEntity entity) throws IOException {
		final DrawFile file = entity.getImageFile();
		if (file == null) {
			throw new IllegalStateException("No file for NOTE");
		}
		if (file.getPngOrEps(fileFormat).exists() == false) {
			throw new IllegalStateException();
		}
		final String absolutePath = StringUtils.getPlateformDependentAbsolutePath(file.getPngOrEps(fileFormat));

		final StringBuilder sb = new StringBuilder("<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">");
		sb.append("<TR>");
		addTdImageBugB1983(sb, absolutePath);
		sb.append("</TR>");
		sb.append("</TABLE>>");
		return sb.toString();
	}

	private String getLabelForActor(IEntity entity) throws IOException {
		final String actorAbsolutePath = StringUtils.getPlateformDependentAbsolutePath(entity.getImageFile()
				.getPngOrEps(fileFormat));
		final Stereotype stereotype = getStereotype(entity);
		final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();

		final StringBuilder sb = new StringBuilder("<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">");
		if (isThereLabel(stereotype)) {
			sb.append("<TR><TD>" + manageHtmlIB(stereotype.getLabel(), FontParam.USECASE_ACTOR_STEREOTYPE, stereo)
					+ "</TD></TR>");
		}
		if (getData().getDpi() == 96) {
			sb.append("<TR><TD><IMG SRC=\"" + actorAbsolutePath + "\"/></TD></TR>");
		} else {
			sb.append("<TR>");
			addTdImageBugB1983(sb, actorAbsolutePath);
			sb.append("</TR>");
		}
		sb.append("<TR><TD>"
				+ manageHtmlIB(StringUtils.getMergedLines(entity.getDisplay2()), FontParam.USECASE_ACTOR, stereo)
				+ "</TD></TR>");
		sb.append("</TABLE>>");
		return sb.toString();

	}

	private String getLabelForCircleInterface(IEntity entity) throws IOException {
		final String circleInterfaceAbsolutePath = StringUtils.getPlateformDependentAbsolutePath(entity.getImageFile()
				.getPngOrEps(fileFormat));
		final Stereotype stereotype = getStereotype(entity);
		final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();

		final StringBuilder sb = new StringBuilder("<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">");
		if (isThereLabel(stereotype)) {
			sb.append("<TR><TD>" + manageHtmlIB(stereotype.getLabel(), FontParam.COMPONENT_STEREOTYPE, stereo)
					+ "</TD></TR>");
		}
		sb.append("<TR>");
		if (getData().getDpi() == 96) {
			sb.append("<TD><IMG SRC=\"" + circleInterfaceAbsolutePath + "\"/></TD>");
		} else {
			addTdImageBugB1983(sb, circleInterfaceAbsolutePath);
		}
		sb.append("</TR>");
		sb.append("<TR><TD>"
				+ manageHtmlIB(StringUtils.getMergedLines(entity.getDisplay2()), FontParam.COMPONENT, stereo)
				+ "</TD></TR>");
		sb.append("</TABLE>>");
		return sb.toString();

	}

	private String getLabelForLollipop(IEntity entity) throws IOException {
		final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();
		final String circleInterfaceAbsolutePath = StringUtils.getPlateformDependentAbsolutePath(getData()
				.getStaticImages(EntityType.LOLLIPOP, stereo).getPngOrEps(fileFormat));
		final Stereotype stereotype = getStereotype(entity);

		final StringBuilder sb = new StringBuilder("<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">");
		if (isThereLabel(stereotype)) {
			sb.append("<TR><TD>" + manageHtmlIB(stereotype.getLabel(), FontParam.CLASS, null) + "</TD></TR>");
		}
		sb.append("<TR>");
		if (getData().getDpi() == 96) {
			sb.append("<TD><IMG SRC=\"" + circleInterfaceAbsolutePath + "\"/></TD>");
		} else {
			addTdImageBugB1983(sb, circleInterfaceAbsolutePath);
		}
		sb.append("</TR>");
		sb.append("<TR><TD>" + manageHtmlIB(StringUtils.getMergedLines(entity.getDisplay2()), FontParam.CLASS, null)
				+ "</TD></TR>");
		sb.append("</TABLE>>");
		return sb.toString();

	}

	private String getLabelForClassOrInterfaceOrEnum(IEntity entity) throws IOException {
		if (isVisibilityModifierPresent) {
			return getLabelForClassOrInterfaceOrEnumWithVisibilityImage(entity);
		}
		return getLabelForClassOrInterfaceOrEnumOld(entity);

	}

	private String getLabelForClassOrInterfaceOrEnumOld(IEntity entity) throws IOException {
		LabelBuilder builder = new LabelBuilderClassOld(getFileFormat(), getData(), entity);
		if (MODE_LOLLIPOP_BETA) {
			final DrawFile cFile = getData().getStaticImages(entity.getType(), null);
			final String northPath = StringUtils.getPlateformDependentAbsolutePath(cFile.getPngOrEps(getFileFormat()));
			final String southPath = northPath;
			final String eastPath = northPath;
			final String westPath = northPath;
			builder = new LabelBuilderTableLollipopDecorator(getFileFormat(), getData(), entity, builder, northPath,
					southPath, eastPath, westPath, getAllLollipop(entity));
		} else if (MODE_MARGIN && entity.hasNearDecoration()) {
			builder = new LabelBuilderTableNineDecorator(getFileFormat(), getData(), entity, builder);
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("<");
		builder.appendLabel(sb);
		if (builder.isUnderline()) {
			setUnderline(true);
		}
		sb.append(">");
		return sb.toString();
	}

	final private List<File> fileToClean = new ArrayList<File>();

	private String addFieldsEps(List<Member> members, boolean withVisibilityChar) throws IOException {
		final List<String> texts = new ArrayList<String>();
		for (Member att : members) {
			String s = att.getDisplay(withVisibilityChar);
			if (att.isAbstract()) {
				s = "<i>" + s + "</i>";
			}
			if (att.isStatic()) {
				s = "<u>" + s + "</u>";
			}
			texts.add(s);
		}
		final UFont font = getData().getSkinParam().getFont(FontParam.CLASS_ATTRIBUTE, null);
		final HtmlColor color = getFontHtmlColor(FontParam.CLASS_ATTRIBUTE, null);
		final TextBlock text = TextBlockUtils.create(texts, new FontConfiguration(font, color),
				HorizontalAlignement.LEFT);
		final File feps = FileUtils.createTempFile("member", ".eps");
		UGraphicEps.copyEpsToFile(getData().getColorMapper(), new UDrawable() {
			public void drawU(UGraphic ug) {
				text.drawU(ug, 0, 0);
			}
		}, feps);
		fileToClean.add(feps);

		final String path = StringUtils.getPlateformDependentAbsolutePath(feps);

		return "<TR ALIGN=\"LEFT\"><TD ALIGN=\"LEFT\">" + "<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">"
				+ "<TR><TD><IMG SRC=\"" + path + "\"/>" + "</TD>" + "<TD></TD>" + "</TR></TABLE></TD></TR>";
	}

	private String getLabelForClassOrInterfaceOrEnumWithVisibilityImage(IEntity entity) throws IOException {
		LabelBuilder builder = new LabelBuilderClassWithVisibilityImage(fileFormat, getData(), entity);
		if (MODE_MARGIN && entity.hasNearDecoration()) {
			builder = new LabelBuilderTableNineDecorator(getFileFormat(), getData(), entity, builder);
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("<");
		builder.appendLabel(sb);
		if (builder.isUnderline()) {
			setUnderline(true);
		}
		sb.append(">");
		return sb.toString();
	}

	private String getLabelForObject(IEntity entity) throws IOException {
		if (isVisibilityModifierPresent) {
			return getLabelForObjectWithVisibilityImage(entity);
		}
		return getLabelForObjectOld(entity);
	}

	private String getLabelForObjectWithVisibilityImage(IEntity entity) throws IOException {
		final LabelBuilder builder = new LabelBuilderObjectWithVisibilityImage(getFileFormat(), getData(), entity);
		final StringBuilder sb = new StringBuilder();
		builder.appendLabel(sb);
		if (builder.isUnderline()) {
			setUnderline(true);
		}
		return sb.toString();

	}

	private String getLabelForObjectOld(IEntity entity) throws IOException {
		final LabelBuilder builder = new LabelBuilderObjectOld(getFileFormat(), getData(), entity);
		final StringBuilder sb = new StringBuilder();
		builder.appendLabel(sb);
		if (builder.isUnderline()) {
			setUnderline(true);
		}
		return sb.toString();

	}

	private boolean isSpecialGroup(Group g) {
		if (g.getType() == GroupType.STATE) {
			return true;
		}
		if (g.getType() == GroupType.CONCURRENT_STATE) {
			throw new IllegalStateException();
		}
		if (getData().isThereLink(g)) {
			return true;
		}
		return false;
	}

	private static final String getLastDotSignature() {
		return lastDotSignature;
	}

	public static final void reset() {
		lastDotSignature = null;
	}

	public void clean() {
		if (OptionFlags.getInstance().isKeepTmpFiles()) {
			return;
		}
		for (File f : fileToClean) {
			Log.info("Deleting temporary file " + f);
			final boolean ok = f.delete();
			if (ok == false) {
				Log.error("Cannot delete: " + f);
			}
		}
	}

	public static final boolean isJunit() {
		return isJunit;
	}

}
