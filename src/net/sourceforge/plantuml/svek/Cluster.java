/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
 * Contribution :  Hisashi Miyashita
 *
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.AlignmentParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.api.ThemeStyle;
import net.sourceforge.plantuml.awt.geom.Dimension2D;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizVersion;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.USymbols;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.posimo.Moveable;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.image.EntityImageState;
import net.sourceforge.plantuml.ugraphic.UComment;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGroupType;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColors;

public class Cluster implements Moveable {

	// /* private */ static final String RANK_SAME = "same";
	/* private */ static final String RANK_SOURCE = "source";
	/* private */ static final String RANK_SINK = "sink";
	public final static String CENTER_ID = "za";

	private final Cluster parentCluster;
	private final IGroup group;
	private final List<SvekNode> nodes = new ArrayList<>();
	private final List<Cluster> children = new ArrayList<>();
	private final int color;
	private final int colorTitle;
	private final ISkinParam skinParam;
	protected final CucaDiagram diagram;

	private int titleAndAttributeWidth;
	private int titleAndAttributeHeight;
	private TextBlock ztitle;
	private TextBlock zstereo;

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

	private Set<EntityPosition> entityPositionsExceptNormal() {
		final Set<EntityPosition> result = EnumSet.<EntityPosition>noneOf(EntityPosition.class);
		for (SvekNode sh : nodes)
			if (sh.getEntityPosition() != EntityPosition.NORMAL)
				result.add(sh.getEntityPosition());

		return Collections.unmodifiableSet(result);
	}

	public Cluster(CucaDiagram diagram, ColorSequence colorSequence, ISkinParam skinParam, IGroup root) {
		this(diagram, null, colorSequence, skinParam, root);
	}

	private Cluster(CucaDiagram diagram, Cluster parentCluster, ColorSequence colorSequence, ISkinParam skinParam,
			IGroup group) {
		if (group == null)
			throw new IllegalStateException();

		this.parentCluster = parentCluster;
		this.group = group;
		this.diagram = diagram;

		this.color = colorSequence.getValue();
		this.colorTitle = colorSequence.getValue();
		this.skinParam = group.getColors().mute(skinParam);
	}

	@Override
	public String toString() {
		return super.toString() + " " + group;
	}

	public final Cluster getParentCluster() {
		return parentCluster;
	}

	public void addNode(SvekNode node) {
		this.nodes.add(Objects.requireNonNull(node));
		node.setCluster(this);
	}

	public final List<SvekNode> getNodes() {
		return Collections.unmodifiableList(nodes);
	}

	private List<SvekNode> getNodesOrderedTop(Collection<SvekLine> lines) {
		final List<SvekNode> firsts = new ArrayList<>();
		final Set<String> tops = new HashSet<>();
		final Map<String, SvekNode> shs = new HashMap<String, SvekNode>();

		for (final Iterator<SvekNode> it = nodes.iterator(); it.hasNext();) {
			final SvekNode node = it.next();
			shs.put(node.getUid(), node);
			if (node.isTop() && node.getEntityPosition() == EntityPosition.NORMAL) {
				firsts.add(node);
				tops.add(node.getUid());
			}
		}

		for (SvekLine l : lines) {
			if (tops.contains(l.getStartUidPrefix())) {
				final SvekNode sh = shs.get(l.getEndUidPrefix());
				if (sh != null && sh.getEntityPosition() == EntityPosition.NORMAL)
					firsts.add(0, sh);
			}

			if (l.isInverted()) {
				final SvekNode sh = shs.get(l.getStartUidPrefix());
				if (sh != null && sh.getEntityPosition() == EntityPosition.NORMAL)
					firsts.add(0, sh);
			}
		}

		return firsts;
	}

	private List<SvekNode> getNodesOrderedWithoutTop(Collection<SvekLine> lines) {
		final List<SvekNode> all = new ArrayList<>(nodes);
		final Set<String> tops = new HashSet<>();
		final Map<String, SvekNode> shs = new HashMap<String, SvekNode>();

		for (final Iterator<SvekNode> it = all.iterator(); it.hasNext();) {
			final SvekNode sh = it.next();
			if (sh.getEntityPosition() != EntityPosition.NORMAL) {
				it.remove();
				continue;
			}
			shs.put(sh.getUid(), sh);
			if (sh.isTop()) {
				tops.add(sh.getUid());
				it.remove();
			}
		}

		for (SvekLine l : lines) {
			if (tops.contains(l.getStartUidPrefix())) {
				final SvekNode sh = shs.get(l.getEndUidPrefix());
				if (sh != null)
					all.remove(sh);
			}

			if (l.isInverted()) {
				final SvekNode sh = shs.get(l.getStartUidPrefix());
				if (sh != null)
					all.remove(sh);
			}
		}

		return all;
	}

	public final List<Cluster> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public Cluster createChild(int titleAndAttributeWidth, int titleAndAttributeHeight, TextBlock title,
			TextBlock stereo, ColorSequence colorSequence, ISkinParam skinParam, IGroup g) {
		final Cluster child = new Cluster(diagram, this, colorSequence, skinParam, g);
		child.titleAndAttributeWidth = titleAndAttributeWidth;
		child.titleAndAttributeHeight = titleAndAttributeHeight;
		child.ztitle = title;
		child.zstereo = stereo;
		this.children.add(child);
		return child;
	}

	public final Set<IGroup> getGroups() {
		return Collections.singleton(group);
	}

	final IGroup getGroup() {
		return group;
	}

	public final int getTitleAndAttributeWidth() {
		return titleAndAttributeWidth;
	}

	public final int getTitleAndAttributeHeight() {
		return titleAndAttributeHeight;
	}

	public double getWidth() {
		return maxX - minX;
	}

	public double getMinX() {
		return minX;
	}

	public ClusterPosition getClusterPosition() {
		return new ClusterPosition(minX, minY, maxX, maxY);
	}

	public void setTitlePosition(double x, double y) {
		this.xTitle = x;
		this.yTitle = y;
	}

	static public StyleSignatureBasic getDefaultStyleDefinition(SName diagramStyleName, USymbol symbol) {
		if (diagramStyleName == SName.stateDiagram)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.state, SName.group);
		if (symbol == null)
			return StyleSignatureBasic.of(SName.root, SName.element, diagramStyleName, SName.group);
		return StyleSignatureBasic.of(SName.root, SName.element, diagramStyleName, SName.group, symbol.getSName());
	}

	static public StyleSignature getDefaultStyleDefinitionStateGroup(Stereotype stereotype) {
		if (stereotype == null)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.state, SName.group);
		return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.state, SName.group)
				.withTOBECHANGED(stereotype);
	}

	public void drawU(UGraphic ug, UmlDiagramType umlDiagramType, ISkinParam skinParam2unused) {
		if (group.isHidden())
			return;

		final String fullName = group.getCodeGetName();
		if (fullName.startsWith("##") == false)
			ug.draw(new UComment("cluster " + fullName));

		final USymbol uSymbol = group.getUSymbol() == null ? USymbols.PACKAGE : group.getUSymbol();
		Style style = getDefaultStyleDefinition(umlDiagramType.getStyleName(), uSymbol)
				.withTOBECHANGED(group.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
		final double shadowing = style.value(PName.Shadowing).asDouble();
		HColor borderColor;
		if (group.getColors().getColor(ColorType.LINE) != null)
			borderColor = group.getColors().getColor(ColorType.LINE);
		else
			borderColor = style.value(PName.LineColor).asColor(skinParam.getThemeStyle(), skinParam.getIHtmlColorSet());
		final double rounded = style.value(PName.RoundCorner).asDouble();
		final double diagonalCorner = style.value(PName.DiagonalCorner).asDouble();

		ug.startGroup(Collections.singletonMap(UGroupType.ID, "cluster_" + fullName));

		final Url url = group.getUrl99();
		if (url != null)
			ug.startUrl(url);

		try {
			if (entityPositionsExceptNormal().size() > 0)
				manageEntryExitPoint(ug.getStringBounder());

			if (skinParam.useSwimlanes(umlDiagramType)) {
				drawSwinLinesState(ug, borderColor);
				return;
			}
			final boolean isState = umlDiagramType == UmlDiagramType.STATE;

			if (isState && group.getUSymbol() == null) {
				drawUState(ug, umlDiagramType, rounded, shadowing);
				return;
			}
			PackageStyle packageStyle = group.getPackageStyle();
			if (packageStyle == null)
				packageStyle = skinParam.packageStyle();

			final UStroke stroke = getStrokeInternal(group, style);

			HColor backColor = getBackColor(umlDiagramType, style);
			backColor = getBackColor(backColor, group.getStereotype(), umlDiagramType.getStyleName(),
					group.getUSymbol(), skinParam.getCurrentStyleBuilder(), skinParam.getThemeStyle(),
					skinParam.getIHtmlColorSet());
			if (ztitle != null || zstereo != null) {
				final ClusterDecoration decoration = new ClusterDecoration(packageStyle, group.getUSymbol(), ztitle,
						zstereo, minX, minY, maxX, maxY, stroke);
				decoration.drawU(ug, backColor, borderColor, shadowing, rounded,
						skinParam.getHorizontalAlignment(AlignmentParam.packageTitleAlignment, null, false, null),
						skinParam.getStereotypeAlignment(), diagonalCorner);
				return;
			}
			final URectangle rect = new URectangle(maxX - minX, maxY - minY);
			rect.setDeltaShadow(shadowing);
			ug = ug.apply(backColor.bg()).apply(borderColor);
			ug.apply(new UStroke(2)).apply(new UTranslate(minX, minY)).draw(rect);

		} finally {
			if (url != null)
				ug.closeUrl();
			ug.closeGroup();
		}
	}

	static public UStroke getStrokeInternal(IGroup group, Style style) {
		final Colors colors = group.getColors();
		if (colors.getSpecificLineStroke() != null)
			return colors.getSpecificLineStroke();

		return style.getStroke();
	}

	void manageEntryExitPoint(StringBounder stringBounder) {
		final Collection<ClusterPosition> insides = new ArrayList<>();
		final List<Point2D> points = new ArrayList<>();
		for (SvekNode sh : nodes)
			if (sh.getEntityPosition() == EntityPosition.NORMAL)
				insides.add(sh.getClusterPosition());
			else
				points.add(sh.getClusterPosition().getPointCenter());

		for (Cluster in : children)
			insides.add(in.getClusterPosition());

		final FrontierCalculator frontierCalculator = new FrontierCalculator(getClusterPosition(), insides, points);
		if (titleAndAttributeHeight > 0 && titleAndAttributeWidth > 0)
			frontierCalculator.ensureMinWidth(titleAndAttributeWidth + 10);

		final ClusterPosition forced = frontierCalculator.getSuggestedPosition();
		xTitle += ((forced.getMinX() - minX) + (forced.getMaxX() - maxX)) / 2;
		minX = forced.getMinX();
		minY = forced.getMinY();
		maxX = forced.getMaxX();
		maxY = forced.getMaxY();
		yTitle = minY + IEntityImage.MARGIN;
		final double widthTitle = ztitle.calculateDimension(stringBounder).getWidth();
		xTitle = minX + ((maxX - minX - widthTitle) / 2);
	}

	private void drawSwinLinesState(UGraphic ug, HColor borderColor) {
		if (ztitle != null)
			ztitle.drawU(ug.apply(UTranslate.dx(xTitle)));

		final ULine line = ULine.vline(maxY - minY);
		ug = ug.apply(borderColor);
		ug.apply(UTranslate.dx(minX)).draw(line);
		ug.apply(UTranslate.dx(maxX)).draw(line);

	}

	// GroupPngMakerState
	private Style getStyleStateHeader() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.state, SName.header)
				.withTOBECHANGED(group.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private Style getStyleState() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.state)
				.withTOBECHANGED(group.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private Style getStyleStateBody() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.stateBody)
				.withTOBECHANGED(group.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	// GroupPngMakerState

	private void drawUState(UGraphic ug, UmlDiagramType umlDiagramType, double rounded, double shadowing) {
		final Dimension2D total = new Dimension2DDouble(maxX - minX, maxY - minY);
		final double suppY;
		if (ztitle == null)
			suppY = 0;
		else
			suppY = ztitle.calculateDimension(ug.getStringBounder()).getHeight() + IEntityImage.MARGIN
					+ IEntityImage.MARGIN_LINE;

		final Style styleGroup = getDefaultStyleDefinitionStateGroup(group.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());

		HColor borderColor = group.getColors().getColor(ColorType.LINE);
		if (borderColor == null)
			borderColor = getStyleState().value(PName.LineColor).asColor(skinParam.getThemeStyle(),
					skinParam.getIHtmlColorSet());

		HColor backColor = group.getColors().getColor(ColorType.BACK);
		if (backColor == null)
			backColor = getStyleState().value(PName.BackGroundColor).asColor(skinParam.getThemeStyle(),
					skinParam.getIHtmlColorSet());

		// final HColor imgBackcolor = HColorUtils.transparent();

		final HColor imgBackcolor = getStyleStateBody().value(PName.BackGroundColor).asColor(skinParam.getThemeStyle(),
				skinParam.getIHtmlColorSet());

		// final HColor imgBackcolor = getBackColor(umlDiagramType, styleGroup);

		// final Style style = getStyle(FontParam.STATE_ATTRIBUTE, skinParam2);

		final TextBlock attribute = GeneralImageBuilder.stateHeader(group, styleGroup, skinParam);
		final double attributeHeight = attribute.calculateDimension(ug.getStringBounder()).getHeight();
		if (total.getWidth() == 0) {
			System.err.println("Cluster::drawUState issue");
			return;
		}

		UStroke stroke = group.getColors().getSpecificLineStroke();
		if (stroke == null)
			stroke = getStyleState().getStroke();

		final RoundedContainer r = new RoundedContainer(total, suppY,
				attributeHeight + (attributeHeight > 0 ? IEntityImage.MARGIN : 0), borderColor, backColor, imgBackcolor,
				stroke, rounded, shadowing);
		r.drawU(ug.apply(new UTranslate(minX, minY)));

		if (ztitle != null)
			ztitle.drawU(ug.apply(new UTranslate(xTitle, yTitle)));

		if (attributeHeight > 0)
			attribute.drawU(
					ug.apply(new UTranslate(minX + IEntityImage.MARGIN, minY + suppY + IEntityImage.MARGIN / 2.0)));

		final Stereotype stereotype = group.getStereotype();
		final boolean withSymbol = stereotype != null && stereotype.isWithOOSymbol();
		if (withSymbol)
			EntityImageState.drawSymbol(ug.apply(borderColor), maxX, maxY);

	}

	public void setPosition(double minX, double minY, double maxX, double maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public void printCluster1(StringBuilder sb, Collection<SvekLine> lines, StringBounder stringBounder) {
		for (SvekNode node : getNodesOrderedTop(lines))
			node.appendShape(sb, stringBounder);

	}

	public SvekNode printCluster2(StringBuilder sb, Collection<SvekLine> lines, StringBounder stringBounder,
			DotMode dotMode, GraphvizVersion graphvizVersion, UmlDiagramType type) {

		SvekNode added = null;
		for (SvekNode node : getNodesOrderedWithoutTop(lines)) {
			node.appendShape(sb, stringBounder);
			added = node;
		}

		if (skinParam.useRankSame() && dotMode != DotMode.NO_LEFT_RIGHT_AND_XLABEL
				&& graphvizVersion.ignoreHorizontalLinks() == false)
			appendRankSame(sb, lines);

		for (Cluster child : getChildren())
			child.printInternal(sb, lines, stringBounder, dotMode, graphvizVersion, type);

		return added;
	}

	private void printInternal(StringBuilder sb, Collection<SvekLine> lines, StringBounder stringBounder,
			DotMode dotMode, GraphvizVersion graphvizVersion, UmlDiagramType type) {
		new ClusterDotString(this, skinParam).printInternal(sb, lines, stringBounder, dotMode, graphvizVersion, type);
	}

	private void appendRankSame(StringBuilder sb, Collection<SvekLine> lines) {
		for (String same : getRankSame(lines)) {
			sb.append(same);
			SvekUtils.println(sb);
		}
	}

	private Set<String> getRankSame(Collection<SvekLine> lines) {
		final Set<String> rankSame = new HashSet<>();
		for (SvekLine l : lines) {
			if (l.hasEntryPoint())
				continue;

			final String startUid = l.getStartUidPrefix();
			final String endUid = l.getEndUidPrefix();
			if (isInCluster(startUid) && isInCluster(endUid)) {
				final String same = l.rankSame();
				if (same != null)
					rankSame.add(same);

			}
		}
		return rankSame;
	}

	void fillRankMin(Set<String> rankMin) {
		for (SvekNode sh : getNodes())
			if (sh.isTop())
				rankMin.add(sh.getUid());

		for (Cluster child : getChildren())
			child.fillRankMin(rankMin);

	}

	private boolean isInCluster(String uid) {
		for (SvekNode node : nodes)
			if (node.getUid().equals(uid))
				return true;

		return false;
	}

	public String getClusterId() {
		return "cluster" + color;
	}

	static String getSpecialPointId(IEntity group) {
		return CENTER_ID + group.getUid();
	}

	String getMinPoint(UmlDiagramType type) {
		if (skinParam.useSwimlanes(type))
			return "minPoint" + color;

		return null;
	}

	String getMaxPoint(UmlDiagramType type) {
		if (skinParam.useSwimlanes(type))
			return "maxPoint" + color;

		return null;
	}

	public boolean isLabel() {
		return getTitleAndAttributeHeight() > 0 && getTitleAndAttributeWidth() > 0;
	}

	int getColor() {
		return color;
	}

	int getTitleColor() {
		return colorTitle;
	}

	private final HColor getBackColor(UmlDiagramType umlDiagramType, Style style) {
		if (EntityUtils.groupRoot(group))
			return null;

		final HColor result = group.getColors().getColor(ColorType.BACK);
		if (result != null)
			return result;

		final Stereotype stereo = group.getStereotype();

		return style.value(PName.BackGroundColor).asColor(skinParam.getThemeStyle(), skinParam.getIHtmlColorSet());

//		final USymbol sym = group.getUSymbol() == null ? USymbols.PACKAGE : group.getUSymbol();
//		final ColorParam backparam = umlDiagramType == UmlDiagramType.ACTIVITY ? ColorParam.partitionBackground
//				: sym.getColorParamBack();
//		final HColor c1 = skinParam.getHtmlColor(backparam, stereo, false);
//		if (c1 != null)
//			return c1;
//
//		if (parentCluster == null)
//			return null;
//
//		return parentCluster.getBackColor(umlDiagramType, style);
	}

	boolean isClusterOf(IEntity ent) {
		if (ent.isGroup() == false)
			return false;

		return group == ent;
	}

	public static HColor getBackColor(HColor backColor, Stereotype stereotype, SName styleName, USymbol symbol,
			StyleBuilder styleBuilder, ThemeStyle themeStyle, HColorSet colorSet) {

		final Style style = getDefaultStyleDefinition(styleName, symbol).getMergedStyle(styleBuilder);
		if (backColor == null)
			backColor = style.value(PName.BackGroundColor).asColor(themeStyle, colorSet);

		if (backColor == null || backColor.equals(HColors.transparent()))
			backColor = HColors.transparent();

		return backColor;
	}

	double checkFolderPosition(Point2D pt, StringBounder stringBounder) {
		if (getClusterPosition().isPointJustUpper(pt)) {
			if (ztitle == null)
				return 0;

			final Dimension2D dimTitle = ztitle.calculateDimension(stringBounder);

			if (pt.getX() < getClusterPosition().getMinX() + dimTitle.getWidth())
				return 0;

			return getClusterPosition().getMinY() - pt.getY() + dimTitle.getHeight();
		}
		return 0;
	}

	// public Point2D projection(double x, double y) {
	// final double v1 = Math.abs(minX - x);
	// final double v2 = Math.abs(maxX - x);
	// final double v3 = Math.abs(minY - y);
	// final double v4 = Math.abs(maxY - y);
	// if (v1 <= v2 && v1 <= v3 && v1 <= v4) {
	// return new Point2D.Double(minX, y);
	// }
	// if (v2 <= v1 && v2 <= v3 && v2 <= v4) {
	// return new Point2D.Double(maxX, y);
	// }
	// if (v3 <= v1 && v3 <= v2 && v3 <= v4) {
	// return new Point2D.Double(x, minY);
	// }
	// if (v4 <= v1 && v4 <= v1 && v4 <= v3) {
	// return new Point2D.Double(x, maxY);
	// }
	// throw new IllegalStateException();
	// }

}
