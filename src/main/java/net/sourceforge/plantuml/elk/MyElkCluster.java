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
package net.sourceforge.plantuml.elk;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.annotation.DuplicateCode;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.decoration.symbol.USymbols;
import net.sourceforge.plantuml.elk.proxy.graph.ElkNode;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.MagneticBorder;
import net.sourceforge.plantuml.klimt.geom.MagneticBorderNone;
import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.skin.AlignmentParam;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.ClusterDecoration;
import net.sourceforge.plantuml.svek.ClusterHeader;
import net.sourceforge.plantuml.svek.PackageStyle;

public class MyElkCluster {

	private final Entity group;
	private final ElkNode elkNode;
	private final CucaDiagram diagram;
	private final ISkinParam skinParam;

	public MyElkCluster(CucaDiagram diagram, Entity group, ElkNode elkNode) {
		this.group = group;
		this.elkNode = elkNode;
		this.diagram = diagram;
		this.skinParam = diagram.getSkinParam();

	}

	public void drawSingleCluster(UGraphic ug) {
		final XPoint2D corner = CucaDiagramFileMakerElk.getPosition(elkNode);
		final URectangle rect = URectangle.build(elkNode.getWidth(), elkNode.getHeight());

		PackageStyle packageStyle = group.getPackageStyle();
		final ISkinParam skinParam = diagram.getSkinParam();
		if (packageStyle == null)
			packageStyle = skinParam.packageStyle();

		final UmlDiagramType umlDiagramType = diagram.getUmlDiagramType();

		final Style style = Cluster
				.getDefaultStyleDefinition(umlDiagramType.getStyleName(), group.getUSymbol(), group.getGroupType())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		final double shadowing = style.getShadowing();
		final UStroke stroke = Cluster.getStrokeInternal(group, style);

		HColor backColor = getBackColor(umlDiagramType);
		backColor = Cluster.getBackColor(backColor, group.getStereotype(), umlDiagramType.getStyleName(),
				group.getUSymbol(), skinParam.getCurrentStyleBuilder(), skinParam.getIHtmlColorSet(),
				group.getGroupType());

		final double roundCorner = style.value(PName.RoundCorner).asDouble();
//			final double roundCorner = group.getUSymbol() == null ? 0
//					: group.getUSymbol().getSkinParameter().getRoundCorner(skinParam, group.getStereotype());

		final RectangleArea rectangleArea = new RectangleArea(0, 0, elkNode.getWidth(), elkNode.getHeight());
		final ClusterHeader clusterHeader = new ClusterHeader(group, diagram, ug.getStringBounder());

		final ClusterDecoration decoration = new ClusterDecoration(packageStyle, group.getUSymbol(),
				clusterHeader.getTitle(), clusterHeader.getStereo(), rectangleArea, stroke);

		final HColor borderColor = HColors.BLACK;
		decoration.drawU(ug.apply(UTranslate.point(corner)), backColor, borderColor, shadowing, roundCorner,
				skinParam.getHorizontalAlignment(AlignmentParam.packageTitleAlignment, null, false, null),
				skinParam.getStereotypeAlignment(), 0);

//			// Print a simple rectangle right now
//			ug.apply(HColorUtils.BLACK).apply(UStroke.withThickness(1.5)).apply(new UTranslate(corner)).draw(rect);
	}

	private HColor getBackColor(UmlDiagramType umlDiagramType) {
		return null;
	}

	@DuplicateCode(reference = "Cluster")
	public MagneticBorder getMagneticBorder(StringBounder stringBounder) {

		USymbol uSymbol = group.getUSymbol();
		PackageStyle packageStyle = group.getPackageStyle();
		if (packageStyle == null)
			packageStyle = skinParam.packageStyle();

		if (uSymbol == null && packageStyle == PackageStyle.FOLDER)
			uSymbol = USymbols.FOLDER;

		if (uSymbol == null)
			return new MagneticBorderNone();

		final XPoint2D corner = CucaDiagramFileMakerElk.getPosition(elkNode);

		final UmlDiagramType umlDiagramType = UmlDiagramType.CLASS;

		final Style style = Cluster
				.getDefaultStyleDefinition(umlDiagramType.getStyleName(), uSymbol, group.getGroupType())
				.withTOBECHANGED(group.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());

		final UStroke stroke = Cluster.getStrokeInternal(group, style);

		final RectangleArea rectangleArea = new RectangleArea(0, 0, elkNode.getWidth(), elkNode.getHeight());
		final ClusterHeader clusterHeader = new ClusterHeader(group, diagram, stringBounder);

		final ClusterDecoration decoration = new ClusterDecoration(packageStyle, group.getUSymbol(),
				clusterHeader.getTitle(), clusterHeader.getStereo(), rectangleArea, stroke);

		final TextBlock textBlock = decoration.getTextBlock(HColors.BLACK, HColors.BLACK, 0, 0,
				skinParam.getHorizontalAlignment(AlignmentParam.packageTitleAlignment, null, false, null),
				skinParam.getStereotypeAlignment(), 0);

		final MagneticBorder orig = textBlock.getMagneticBorder();

		return new MagneticBorder() {
			public UTranslate getForceAt(StringBounder stringBounder, XPoint2D position) {
				return orig.getForceAt(stringBounder, position.move(-corner.x, -corner.y));
			}
		};
	}

}
