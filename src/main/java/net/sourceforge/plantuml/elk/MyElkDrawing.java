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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.abel.CucaNote;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.annotation.DuplicateCode;
import net.sourceforge.plantuml.decoration.symbol.USymbolFolder;
import net.sourceforge.plantuml.elk.proxy.graph.ElkEdge;
import net.sourceforge.plantuml.elk.proxy.graph.ElkNode;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.skin.AlignmentParam;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.ClusterManager;
import net.sourceforge.plantuml.svek.GeneralImageBuilder;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.IEntityImageUtils;
import net.sourceforge.plantuml.svek.SvekNode;
import net.sourceforge.plantuml.svek.image.EntityImageNoteLink;
import net.sourceforge.plantuml.utils.Position;

// The Drawing class does the real drawing
class MyElkDrawing extends AbstractTextBlock {

	// min and max of all coord
	private final MinMax minMax;

	private final CucaDiagram diagram;

	private final Map<Entity, ElkNode> clusters;
	private final Map<Link, ElkEdge> edges;
	private final Map<Entity, ElkNode> nodes;

	private final ClusterManager clusterManager;

	public MyElkDrawing(ClusterManager clusterManager, CucaDiagram diagram, MinMax minMax,
			Map<Entity, ElkNode> clusters, Map<Link, ElkEdge> edges, Map<Entity, ElkNode> nodes) {
		this.clusterManager = clusterManager;
		this.minMax = minMax;
		this.diagram = diagram;
		this.clusters = clusters;
		this.edges = edges;
		this.nodes = nodes;
	}

	public void drawU(UGraphic ug) {
		final Map<Entity, MyElkCluster> clusters = drawAllClusters(ug);
		final Map<Entity, IEntityImage> nodes = drawAllNodes(ug);
		drawAllEdges(ug, clusters, nodes);
	}

	private Map<Entity, MyElkCluster> drawAllClusters(UGraphic ug) {
		final Map<Entity, MyElkCluster> elkClusters = new HashMap<>();
		for (Entry<Entity, ElkNode> ent : clusters.entrySet()) {
			final Entity entity = ent.getKey();
			final MyElkCluster elkCluster = new MyElkCluster(diagram, entity, ent.getValue());
			elkCluster.drawSingleCluster(ug);
			elkClusters.put(entity, elkCluster);
		}
		return elkClusters;

	}

	private Map<Entity, IEntityImage> drawAllNodes(UGraphic ug) {
		final Map<Entity, IEntityImage> elkNodes = new HashMap<>();
		for (Entry<Entity, ElkNode> ent : nodes.entrySet()) {
			final Entity entity = ent.getKey();
			// Retrieve coord from ELK
			final XPoint2D corner = CucaDiagramFileMakerElk.getPosition(ent.getValue());
			final SvekNode svekNode = clusterManager.getBibliotekon().getNode(entity);
			svekNode.resetMove();
			svekNode.moveDelta(corner.x, corner.y);

			final IEntityImage image = IEntityImageUtils.translate(printEntityInternal(entity),
					UTranslate.point(corner));

			// Print the node image at right coord
			image.drawU(ug);

			elkNodes.put(entity, image);
		}
		return elkNodes;

	}

	private void drawAllEdges(UGraphic ug, Map<Entity, MyElkCluster> elkClusters,
			Map<Entity, IEntityImage> nodeImages) {
		for (Entry<Link, ElkEdge> ent : edges.entrySet()) {
			final Link link = ent.getKey();
			if (link.isInvis())
				continue;

			drawSingleEdge(ug, link, ent.getValue(), elkClusters, nodeImages);
		}
	}

	private IEntityImage printEntityInternal(Entity ent) {
		if (ent.isRemoved())
			throw new IllegalStateException();

		if (ent.getSvekImage() == null) {
			final ISkinParam skinParam = diagram.getSkinParam();
			if (skinParam.sameClassWidth())
				System.err.println("NOT YET IMPLEMENED");

			return GeneralImageBuilder.createEntityImageBlock(ent, diagram.isHideEmptyDescriptionForState(), diagram,
					getBibliotekon(), null, diagram.getLinks());
		}
		return ent.getSvekImage();
	}

	private Bibliotekon getBibliotekon() {
		return clusterManager.getBibliotekon();
	}

	private void drawSingleEdge(UGraphic ug, Link link, ElkEdge edge, Map<Entity, MyElkCluster> elkClusters,
			Map<Entity, IEntityImage> nodeImages) {
		// Unfortunately, we have to translate "edge" in its own "cluster" coordinate
		final XPoint2D translate = CucaDiagramFileMakerElk.getPosition(edge.getContainingNode());

		final double magicY2 = 0;
		final Entity dest = link.getEntity2();
		if (dest.getUSymbol() instanceof USymbolFolder) {
//				System.err.println("dest=" + dest);
//				final IEntityImage image = printEntityInternal((ILeaf) dest);
//				System.err.println("image=" + image);

		}
		final TextBlock label = getLabel(ug.getStringBounder(), link);
		final TextBlock quantifier1 = getQuantifier(ug.getStringBounder(), link, 1);
		final TextBlock quantifier2 = getQuantifier(ug.getStringBounder(), link, 2);
		final MyElkEdge elkPath = new MyElkEdge(diagram, SName.classDiagram, link, edge, label, quantifier1,
				quantifier2, magicY2, elkClusters, UTranslate.point(translate), nodeImages);
		elkPath.drawU(ug);
	}

	private FontConfiguration getFontForLink(Link link, final ISkinParam skinParam) {
		final SName styleName = skinParam.getUmlDiagramType().getStyleName();

		final Style style = getDefaultStyleDefinitionArrow(link.getStereotype(), styleName)
				.getMergedStyle(link.getStyleBuilder());
		return style.getFontConfiguration(skinParam.getIHtmlColorSet());
	}

	// Duplication from SvekEdge
	final public StyleSignature getDefaultStyleDefinitionArrow(Stereotype stereotype, SName styleName) {
		StyleSignature result = StyleSignatureBasic.of(SName.root, SName.element, styleName, SName.arrow);
		if (stereotype != null)
			result = result.withTOBECHANGED(stereotype);

		return result;
	}

	private HorizontalAlignment getMessageTextAlignment(UmlDiagramType umlDiagramType, ISkinParam skinParam) {
		if (umlDiagramType == UmlDiagramType.STATE)
			return skinParam.getHorizontalAlignment(AlignmentParam.stateMessageAlignment, null, false, null);

		return skinParam.getDefaultTextAlignment(HorizontalAlignment.CENTER);
	}

	private TextBlock addVisibilityModifier(TextBlock block, Link link, ISkinParam skinParam) {
		final VisibilityModifier visibilityModifier = link.getVisibilityModifier();
		if (visibilityModifier != null) {
			final Rose rose = new Rose();
			final HColor fore = rose.getHtmlColor(skinParam, visibilityModifier.getForeground());
			TextBlock visibility = visibilityModifier.getUBlock(skinParam.classAttributeIconSize(), fore, null, false);
			visibility = TextBlockUtils.withMargin(visibility, 0, 1, 2, 0);
			block = TextBlockUtils.mergeLR(visibility, block, VerticalAlignment.CENTER);
		}
		final double marginLabel = 1; // startUid.equalsId(endUid) ? 6 : 1;
		return TextBlockUtils.withMargin(block, marginLabel, marginLabel);
	}

	private TextBlock getLabel(StringBounder stringBounder, Link link) {
		ISkinParam skinParam = diagram.getSkinParam();
		final double marginLabel = 1; // startUid.equals(endUid) ? 6 : 1;

		// final FontConfiguration labelFont =
		// style.getFontConfiguration(skinParam.getIHtmlColorSet());
//		TextBlock labelOnly = link.getLabel().create(labelFont,
//				skinParam.getDefaultTextAlignment(HorizontalAlignment.CENTER), skinParam);

		final UmlDiagramType type = skinParam.getUmlDiagramType();
		final FontConfiguration font = getFontForLink(link, skinParam);

		TextBlock labelOnly;
		// toto2
		if (Display.isNull(link.getLabel())) {
			labelOnly = TextBlockUtils.EMPTY_TEXT_BLOCK;
//			if (getLinkArrow(link) != LinkArrow.NONE_OR_SEVERAL) {
//				// labelOnly = StringWithArrow.addMagicArrow(labelOnly, this, font);
//			}

		} else {
			final HorizontalAlignment alignment = getMessageTextAlignment(type, skinParam);
			final boolean hasSeveralGuideLines = link.getLabel().hasSeveralGuideLines();
			final TextBlock block;
			// if (hasSeveralGuideLines)
			// block = StringWithArrow.addSeveralMagicArrows(link.getLabel(), this, font,
			// alignment, skinParam);
			// else
			block = link.getLabel().create0(font, alignment, skinParam, skinParam.maxMessageSize(),
					CreoleMode.SIMPLE_LINE, null, null);

			labelOnly = addVisibilityModifier(block, link, skinParam);
//			if (getLinkArrow(link) != LinkArrow.NONE_OR_SEVERAL && hasSeveralGuideLines == false) {
//				// labelOnly = StringWithArrow.addMagicArrow(labelOnly, this, font);
//			}

		}

		final CucaNote note = link.getNote();
		if (note == null) {
			if (TextBlockUtils.isEmpty(labelOnly, stringBounder) == false)
				labelOnly = TextBlockUtils.withMargin(labelOnly, marginLabel, marginLabel);
			return labelOnly;
		}
		final TextBlock noteOnly = new EntityImageNoteLink(note.getDisplay(), note.getColors(), skinParam,
				link.getStyleBuilder());

		if (note.getPosition() == Position.LEFT)
			return TextBlockUtils.mergeLR(noteOnly, labelOnly, VerticalAlignment.CENTER);
		else if (note.getPosition() == Position.RIGHT)
			return TextBlockUtils.mergeLR(labelOnly, noteOnly, VerticalAlignment.CENTER);
		else if (note.getPosition() == Position.TOP)
			return TextBlockUtils.mergeTB(noteOnly, labelOnly, HorizontalAlignment.CENTER);
		else
			return TextBlockUtils.mergeTB(labelOnly, noteOnly, HorizontalAlignment.CENTER);

	}

	@DuplicateCode(reference = "CucaDiagramFile")
	private TextBlock getQuantifier(StringBounder stringBounder, Link link, int n) {
		final String tmp = n == 1 ? link.getQuantifier1() : link.getQuantifier2();
		if (tmp == null)
			return null;

		final ISkinParam skinParam = diagram.getSkinParam();
		final FontConfiguration labelFont = FontConfiguration.create(skinParam, FontParam.ARROW, null);
		final TextBlock label = Display.getWithNewlines(diagram.getPragma(), tmp).create(labelFont,
				skinParam.getDefaultTextAlignment(HorizontalAlignment.CENTER), skinParam);
		if (TextBlockUtils.isEmpty(label, stringBounder))
			return null;

		return label;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		if (minMax == null)
			throw new UnsupportedOperationException();

		return minMax.getDimension();
	}

	public HColor getBackcolor() {
		return null;
	}

}
