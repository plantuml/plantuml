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
 *
 */
package net.sourceforge.plantuml.elk;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.math.ElkPadding;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.options.Direction;
import org.eclipse.elk.core.options.EdgeType;
import org.eclipse.elk.core.options.NodeLabelPlacement;
import org.eclipse.elk.core.options.SizeConstraint;
import org.eclipse.elk.core.options.SizeOptions;
import org.eclipse.elk.core.util.NullElkProgressMonitor;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkLabel;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.QuoteUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.svek.DotStringFactory;
import net.sourceforge.plantuml.svek.GeneralImageBuilder;
import net.sourceforge.plantuml.svek.GraphvizCrash;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class CucaDiagramFileMakerElk implements CucaDiagramFileMaker {

	private final CucaDiagram diagram;
	private final StringBounder stringBounder;
	private final DotStringFactory dotStringFactory;

	private final Map<ILeaf, ElkNode> nodes = new LinkedHashMap<ILeaf, ElkNode>();
	private final Map<Link, ElkEdge> edges = new LinkedHashMap<Link, ElkEdge>();

	public CucaDiagramFileMakerElk(CucaDiagram diagram, StringBounder stringBounder) {
		this.diagram = diagram;
		this.stringBounder = stringBounder;
		this.dotStringFactory = new DotStringFactory(stringBounder, diagram);

	}

	// Unused right now
	private TextBlock getLabel(Link link) {
		final double marginLabel = 1; // startUid.equals(endUid) ? 6 : 1;
		ISkinParam skinParam = diagram.getSkinParam();
		final FontConfiguration labelFont = new FontConfiguration(skinParam, FontParam.ARROW, null);
		final TextBlock label = link.getLabel().create(labelFont,
				skinParam.getDefaultTextAlignment(HorizontalAlignment.CENTER), skinParam);
		if (TextBlockUtils.isEmpty(label, stringBounder)) {
			return label;
		}
		return TextBlockUtils.withMargin(label, marginLabel, marginLabel);
	}

	// Unused right now
	private TextBlock getQualifier(Link link, int n) {
		final String tmp = n == 1 ? link.getQualifier1() : link.getQualifier2();
		if (tmp == null) {
			return null;
		}
		final double marginLabel = 1; // startUid.equals(endUid) ? 6 : 1;
		ISkinParam skinParam = diagram.getSkinParam();
		final FontConfiguration labelFont = new FontConfiguration(skinParam, FontParam.ARROW, null);
		final TextBlock label = Display.getWithNewlines(tmp).create(labelFont,
				skinParam.getDefaultTextAlignment(HorizontalAlignment.CENTER), skinParam);
		if (TextBlockUtils.isEmpty(label, stringBounder)) {
			return label;
		}
		return TextBlockUtils.withMargin(label, marginLabel, marginLabel);
	}

	// The Drawing class does the real drawing
	class Drawing extends AbstractTextBlock implements TextBlockBackcolored {

		// min and max of all coord
		private final MinMax minMax;

		public Drawing(MinMax minMax) {
			this.minMax = minMax;
		}

		public void drawU(UGraphic ug) {

			// Draw all nodes
			for (Entry<ILeaf, ElkNode> ent : nodes.entrySet()) {
				final ILeaf leaf = ent.getKey();
				final ElkNode agnode = ent.getValue();

				final IEntityImage image = printEntityInternal(leaf);

				// Retrieve coord from ELK
				final Point2D corner = new Point2D.Double(agnode.getX(), agnode.getY());

				// Print the node image at right coord
				image.drawU(ug.apply(new UTranslate(corner)));
			}

			// Draw all edges
			for (Entry<Link, ElkEdge> ent : edges.entrySet()) {
				final Link link = ent.getKey();
				if (link.isInvis()) {
					continue;
				}
				final ElkEdge edge = ent.getValue();
				new ElkPath(link, edge, diagram, getLabel(link), getQualifier(link, 1), getQualifier(link, 2))
						.drawU(ug);
				if (Display.isNull(link.getLabel()) == false) {
					final ElkLabel label = edge.getLabels().get(0);
					final double x = label.getX();
					final double y = label.getY();
					final TextBlock labelLink = getLabel(link);
					labelLink.drawU(ug.apply(new UTranslate(x, y)));
				}
			}

		}

		public Dimension2D calculateDimension(StringBounder stringBounder) {
			if (minMax == null) {
				throw new UnsupportedOperationException();
			}
			return minMax.getDimension();
		}

		public HColor getBackcolor() {
			return null;
		}

	}

	@Override
	public ImageData createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {

		try {
			final ElkNode root = ElkGraphUtil.createGraph();
			root.setProperty(CoreOptions.DIRECTION, Direction.DOWN);

			// This padding setting have no impact ?
			final ElkPadding labelPadding = new ElkPadding(100.0);

			// Convert all "leaf" to ELK node
			for (ILeaf leaf : diagram.getLeafsvalues()) {
				final IEntityImage image = printEntityInternal(leaf);

				// Expected dimension of the node
				final Dimension2D dimension = image.calculateDimension(stringBounder);

				// Here, we try to tell ELK to use this dimension as node dimension
				final ElkNode node = ElkGraphUtil.createNode(root);
				node.setDimensions(dimension.getWidth(), dimension.getHeight());

				// There is no real "label" here
				// We just would like to force node dimension
				final ElkLabel label = ElkGraphUtil.createLabel(node);
				label.setText("X");

				// I don't know why we have to do this hack, but somebody has to fix it
				final double VERY_STRANGE_OFFSET = 10;
				label.setDimensions(dimension.getWidth(), dimension.getHeight() - VERY_STRANGE_OFFSET);

				// No idea of what we are doing here :-)
				label.setProperty(CoreOptions.NODE_LABELS_PLACEMENT, EnumSet.of(NodeLabelPlacement.INSIDE,
						NodeLabelPlacement.H_CENTER, NodeLabelPlacement.V_CENTER));
				label.setProperty(CoreOptions.NODE_LABELS_PADDING, labelPadding);
				node.setProperty(CoreOptions.NODE_SIZE_CONSTRAINTS, EnumSet.of(SizeConstraint.NODE_LABELS));
				node.setProperty(CoreOptions.NODE_SIZE_OPTIONS, EnumSet.noneOf(SizeOptions.class));

				// Let's store this
				nodes.put(leaf, node);
			}

			// https://www.eclipse.org/forums/index.php/t/1095737/

			for (final Link link : diagram.getLinks()) {
				final ElkEdge edge = ElkGraphUtil.createEdge(root);
				if (Display.isNull(link.getLabel()) == false) {
					final ElkLabel edgeLabel = ElkGraphUtil.createLabel(edge);
					final TextBlock labelLink = getLabel(link);
					final Dimension2D labelLinkDim = labelLink.calculateDimension(stringBounder);
					edgeLabel.setText("X");
					edgeLabel.setDimensions(labelLinkDim.getWidth(), labelLinkDim.getHeight());
					edge.setProperty(CoreOptions.EDGE_LABELS_INLINE, true);
					edge.setProperty(CoreOptions.EDGE_TYPE, EdgeType.ASSOCIATION);

				}
				edge.getSources().add(nodes.get(link.getEntity1()));
				edge.getTargets().add(nodes.get(link.getEntity2()));
				edges.put(link, edge);
			}

			final RecursiveGraphLayoutEngine engine = new RecursiveGraphLayoutEngine();
			engine.layout(root, new NullElkProgressMonitor());

			// Debug
//			for (final ElkNode node : nodes.values()) {
//				final String name = node.getLabels().get(0).getText();
//				System.out.println("node " + name + " : " + node.getX() + ", " + node.getY() + " (" + node.getWidth()
//						+ ", " + node.getHeight() + ")");
//			}
//			for (final ElkEdge edge : edges.values()) {
//				final EList<ElkEdgeSection> sections = edge.getSections();
//				System.out.println("edge=" + edge.getSections());
//				System.out.println("edge=" + edge.getProperty(LayeredOptions.JUNCTION_POINTS));
//				for (ElkEdgeSection s : sections)
//					System.out.println(s.getBendPoints());
//			}

			final MinMax minMax = TextBlockUtils.getMinMax(new Drawing(null), stringBounder, false);

			final TextBlock drawable = new Drawing(minMax);
			return diagram.createImageBuilder(fileFormatOption) //
					.drawable(drawable) //
					.write(os); //

		} catch (Throwable e) {
			UmlDiagram.exportDiagramError(os, e, fileFormatOption, diagram.seed(), diagram.getMetadata(),
					diagram.getFlashData(), getFailureText3(e));
			return ImageDataSimple.error();
		}

	}

	static private List<String> getFailureText3(Throwable exception) {
		exception.printStackTrace();
		final List<String> strings = new ArrayList<String>();
		strings.add("An error has occured : " + exception);
		final String quote = StringUtils.rot(QuoteUtils.getSomeQuote());
		strings.add("<i>" + quote);
		strings.add(" ");
		GraphvizCrash.addProperties(strings);
		strings.add(" ");
		strings.add("Sorry, ELK intregration is really alpha feature...");
		strings.add(" ");
		strings.add("You should send this diagram and this image to <b>plantuml@gmail.com</b> or");
		strings.add("post to <b>http://plantuml.com/qa</b> to solve this issue.");
		strings.add(" ");
		return strings;
	}

	private Bibliotekon getBibliotekon() {
		return dotStringFactory.getBibliotekon();
	}

	private IEntityImage printEntityInternal(ILeaf ent) {
		if (ent.isRemoved()) {
			throw new IllegalStateException();
		}
		if (ent.getSvekImage() == null) {
			final ISkinParam skinParam = diagram.getSkinParam();
			if (skinParam.sameClassWidth()) {
				System.err.println("NOT YET IMPLEMENED");
			}

			return GeneralImageBuilder.createEntityImageBlock(ent, skinParam, diagram.isHideEmptyDescriptionForState(),
					diagram, getBibliotekon(), null, diagram.getUmlDiagramType(), diagram.getLinks());
		}
		return ent.getSvekImage();
	}

}
