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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.posimo.Moveable;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class Cluster implements Moveable {

	private final Cluster parent;
	private final Group group;
	private final List<Shape> shapes = new ArrayList<Shape>();
	private final List<Cluster> children = new ArrayList<Cluster>();
	private final boolean special;
	private final int color;
	private final int colorTitle;

	private int titleWidth;
	private int titleHeight;
	private TextBlock title;

	private double xTitle;
	private double yTitle;

	private double minX;
	private double minY;
	private double maxX;
	private double maxY;

	public void moveSvek(double deltaX, double deltaY) {
		this.xTitle += deltaX;
		this.minX += deltaX;
		this.maxX += deltaX;
		this.yTitle += deltaY;
		this.minY += deltaY;
		this.maxY += deltaY;

	}

	public Cluster(ColorSequence colorSequence) {
		this(null, null, false, colorSequence);
	}

	private int getUid2() {
		return group.getUid2();
	}

	private Cluster(Cluster parent, Group group, boolean special, ColorSequence colorSequence) {
		this.parent = parent;
		this.group = group;
		this.special = special;
		this.color = colorSequence.getValue();
		this.colorTitle = colorSequence.getValue();
	}

	@Override
	public String toString() {
		return super.toString() + " " + group;
	}

	public final Cluster getParent() {
		return parent;
	}

	public void addShape(Shape sh) {
		this.shapes.add(sh);
		sh.setCluster(this);
	}

	public final List<Shape> getShapes() {
		return Collections.unmodifiableList(shapes);
	}

	private List<Shape> getShapesOrdered(Collection<Line> lines) {
		final List<Shape> all = new ArrayList<Shape>(shapes);
		final List<Shape> firsts = new ArrayList<Shape>();
		final Set<String> tops = new HashSet<String>();
		final Map<String, Shape> shs = new HashMap<String, Shape>();

		for (final Iterator<Shape> it = all.iterator(); it.hasNext();) {
			final Shape sh = it.next();
			shs.put(sh.getUid(), sh);
			if (sh.isTop()) {
				firsts.add(sh);
				tops.add(sh.getUid());
				it.remove();
			}
		}

		for (Line l : lines) {
			if (tops.contains(l.getStartUid())) {
				final Shape sh = shs.get(l.getEndUid());
				all.remove(sh);
				firsts.add(0, sh);
			}
			if (l.isInverted()) {
				final Shape sh = shs.get(l.getStartUid());
				all.remove(sh);
				firsts.add(0, sh);
			}
		}

		all.addAll(0, firsts);
		return all;
	}

	public final List<Cluster> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public Cluster createChild(Group g, int titleWidth, int titleHeight, TextBlock title, boolean special,
			ColorSequence colorSequence) {
		final Cluster child = new Cluster(this, g, special, colorSequence);
		child.titleWidth = titleWidth;
		child.titleHeight = titleHeight;
		child.title = title;
		this.children.add(child);
		return child;
	}

	public final Group getGroup() {
		return group;
	}

	public final int getTitleWidth() {
		return titleWidth;
	}

	public final int getTitleHeight() {
		return titleHeight;
	}

	public void setTitlePosition(double x, double y) {
		this.xTitle = x;
		this.yTitle = y;
	}

	public void drawU(UGraphic ug, double x, double y, HtmlColor borderColor, DotData dotData) {
		final boolean isState = dotData.getUmlDiagramType() == UmlDiagramType.STATE;
		if (isState) {
			drawUState(ug, x, y, borderColor, dotData);
			return;
		}
		if (title != null) {
			drawWithTitle(ug, x, y, borderColor, dotData);
			return;
		}
		final URectangle rect = new URectangle(maxX - minX, maxY - minY);
		if (dotData.getSkinParam().shadowing()) {
			rect.setDeltaShadow(3.0);
		}
		HtmlColor stateBack = getBackColor();
		if (stateBack == null) {
			stateBack = dotData.getSkinParam().getHtmlColor(ColorParam.packageBackground, group.getStereotype());
		}
		if (stateBack == null) {
			stateBack = dotData.getSkinParam().getHtmlColor(ColorParam.background, group.getStereotype());
		}
		ug.getParam().setBackcolor(stateBack);
		ug.getParam().setColor(borderColor);
		ug.getParam().setStroke(new UStroke(2));
		ug.draw(x + minX, y + minY, rect);
		ug.getParam().setStroke(new UStroke());
	}

	private UPolygon getSpecificFrontier(StringBounder stringBounder) {
		final double width = maxX - minX;
		final double height = maxY - minY;
		final Dimension2D dimTitle = title.calculateDimension(stringBounder);
		final double wtitle = dimTitle.getWidth() + marginTitleX1 + marginTitleX2;
		final double htitle = dimTitle.getHeight() + marginTitleY1 + marginTitleY2;
		final UPolygon shape = new UPolygon();
		shape.addPoint(0, 0);
		shape.addPoint(wtitle, 0);
		shape.addPoint(wtitle + marginTitleX3, htitle);
		shape.addPoint(width, htitle);
		shape.addPoint(width, height);
		shape.addPoint(0, height);
		shape.addPoint(0, 0);
		return shape;
	}

	// private UPolygon specificFrontier;
	//
	// public final UPolygon getSpecificFrontier() {
	// return specificFrontier;
	// }
	//
	private void drawWithTitle(UGraphic ug, double x, double y, HtmlColor borderColor, DotData dotData) {

		// y += marginTitleY0;

		final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double wtitle = dimTitle.getWidth() + marginTitleX1 + marginTitleX2;
		final double htitle = dimTitle.getHeight() + marginTitleY1 + marginTitleY2;
		final UPolygon shape = getSpecificFrontier(ug.getStringBounder());
		if (dotData.getSkinParam().shadowing()) {
			shape.setDeltaShadow(3.0);
		}

		HtmlColor stateBack = getBackColor();
		if (stateBack == null) {
			stateBack = dotData.getSkinParam().getHtmlColor(ColorParam.packageBackground, group.getStereotype());
		}
		if (stateBack == null) {
			stateBack = dotData.getSkinParam().getHtmlColor(ColorParam.background, group.getStereotype());
		}
		if (stateBack == null) {
			stateBack = HtmlColor.WHITE;
		}
		ug.getParam().setBackcolor(stateBack);
		ug.getParam().setColor(borderColor);
		ug.getParam().setStroke(new UStroke(2));
		ug.draw(x + minX, y + minY, shape);
		// specificFrontier = shape.translate(x + minX, y + minY);
		ug.draw(x + minX, y + minY + htitle, new ULine(wtitle + marginTitleX3, 0));
		ug.getParam().setStroke(new UStroke());
		title.drawU(ug, x + minX + marginTitleX1, y + minY + marginTitleY1);
	}

	private HtmlColor getColor(DotData dotData, ColorParam colorParam, String stereo) {
		return new Rose().getHtmlColor(dotData.getSkinParam(), colorParam, stereo);
	}

	private void drawUState(UGraphic ug, double x, double y, HtmlColor borderColor, DotData dotData) {

		final Dimension2D total = new Dimension2DDouble(maxX - minX, maxY - minY);
		final double suppY = title.calculateDimension(ug.getStringBounder()).getHeight() + EntityImageState.MARGIN
				+ EntityImageState.MARGIN_LINE;
		HtmlColor stateBack = getBackColor();
		if (stateBack == null) {
			stateBack = getColor(dotData, ColorParam.stateBackground, group.getStereotype());
		}
		final HtmlColor background = getColor(dotData, ColorParam.background, null);
		final RoundedContainer r = new RoundedContainer(total, suppY, borderColor, stateBack, background);
		r.drawU(ug, x + minX, y + minY);

		title.drawU(ug, x + xTitle, y + yTitle);

	}

	public void setPosition(double minX, double minY, double maxX, double maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;

	}

	public boolean isSpecial() {
		return special;
	}

	public void printCluster(StringBuilder sb, Collection<Line> lines) {
		// System.err.println("Cluster::printCluster " + this);

		final Set<String> rankSame = new HashSet<String>();
		for (Line l : lines) {
			final String startUid = l.getStartUid();
			final String endUid = l.getEndUid();
			if (isInCluster(startUid) && isInCluster(endUid)) {
				final String same = l.rankSame();
				if (same != null) {
					rankSame.add(same);
				}
			}
		}

		for (Shape sh : getShapesOrdered(lines)) {
			sh.appendShape(sb);
		}

		for (String same : rankSame) {
			sb.append(same);
			SvekUtils.println(sb);
		}

		for (Cluster child : getChildren()) {
			child.printInternal(sb, lines);
		}
	}

	public void fillRankMin(Set<String> rankMin) {
		for (Shape sh : getShapes()) {
			if (sh.isTop()) {
				rankMin.add(sh.getUid());
			}
		}

		for (Cluster child : getChildren()) {
			child.fillRankMin(rankMin);
		}
	}

	private boolean isInCluster(String uid) {
		for (Shape sh : shapes) {
			if (sh.getUid().equals(uid)) {
				return true;
			}
		}
		return false;
	}

	public String getClusterId() {
		return "cluster" + color;
	}

	public String getSpecialPointId() {
		return "za" + getUid2();
	}

	private int marginTitleX1 = 3;
	private int marginTitleX2 = 3;
	private int marginTitleX3 = 7;
	private int marginTitleY0 = 10;
	private int marginTitleY1 = 3;
	private int marginTitleY2 = 3;

	private final boolean protection0 = true;
	private final boolean protection1 = true;

	private void printInternal(StringBuilder sb, Collection<Line> lines) {
		if (isSpecial()) {
			subgraphCluster(sb, "a");
		}
		if (protection0) {
			subgraphCluster(sb, "p0");
		}
		sb.append("subgraph " + getClusterId() + " {");
		sb.append("style=solid;");
		sb.append("color=\"" + StringUtils.getAsHtml(color) + "\";");

		final int titleWidth = getTitleWidth() + marginTitleX1 + marginTitleX2 + marginTitleX3;
		final int titleHeight = getTitleHeight() + marginTitleY0 + marginTitleY1 + marginTitleY2;
		if (titleHeight > 0 && titleWidth > 0) {
			sb.append("label=<");
			Line.appendTable(sb, titleWidth, titleHeight, colorTitle);
			sb.append(">;");
		}
		SvekUtils.println(sb);

		if (isSpecial()) {
			// subgraphCluster(sb, "za");
			if (OptionFlags.getInstance().isDebugDot()) {
				sb.append(getSpecialPointId() + ";");
			} else {
				sb.append(getSpecialPointId() + " [shape=point,width=.01,label=\"\"];");
			}
			// sb.append("}");
			subgraphCluster(sb, "i");
		}
		if (protection1) {
			subgraphCluster(sb, "p1");
		}
		printCluster(sb, lines);
		if (protection1) {
			sb.append("}");
		}
		if (isSpecial()) {
			sb.append("}");
			// subgraphCluster(sb, "zb");
			// if (OptionFlags.getInstance().isDebugDot()) {
			// sb.append("zb" + child.getUid2() + ";");
			// } else {
			// sb.append("zb" + child.getUid2() + "
			// [shape=point,width=.01,label=\"\"];");
			// }
			// sb.append("}");
		}
		sb.append("}");
		if (protection0) {
			sb.append("}");
		}
		if (this.isSpecial()) {
			// a
			sb.append("}");
		}
		SvekUtils.println(sb);
	}

	private void subgraphCluster(StringBuilder sb, String id) {
		final String uid = getClusterId() + id;
		sb.append("subgraph " + uid + " {");
		if (OptionFlags.getInstance().isDebugDot()) {
			sb.append("style=dotted;");
			sb.append("label=\"" + id + "\";");
		} else {
			sb.append("style=invis;");
			sb.append("label=\"\";");
		}
	}

	public int getColor() {
		return color;
	}

	public int getTitleColor() {
		return colorTitle;
	}

	private final HtmlColor getBackColor() {
		if (group == null) {
			return null;
		}
		final HtmlColor result = group.getBackColor();
		if (result != null) {
			return result;
		}
		if (parent == null) {
			return null;
		}
		return parent.getBackColor();
	}

	public boolean isClusterOf(IEntity ent) {
		if (ent.getType() != EntityType.GROUP) {
			return false;
		}
		return group.getEntityCluster() == ent;
	}

}
