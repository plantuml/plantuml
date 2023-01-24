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
 * 
 *
 */
package net.sourceforge.plantuml.sdot;

import static gen.lib.cgraph.attr__c.agsafeset;
import static gen.lib.cgraph.edge__c.agedge;
import static gen.lib.cgraph.graph__c.agopen;
import static gen.lib.cgraph.node__c.agnode;
import static gen.lib.cgraph.subg__c.agsubg;
import static gen.lib.gvc.gvc__c.gvContext;
import static gen.lib.gvc.gvlayout__c.gvLayoutJobs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agraph_s;
import h.ST_Agraphinfo_t;
import h.ST_Agrec_s;
import h.ST_GVC_s;
import h.ST_boxf;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.awt.geom.XPoint2D;
import net.sourceforge.plantuml.baraye.EntityFactory;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.baraye.IEntity;
import net.sourceforge.plantuml.baraye.IGroup;
import net.sourceforge.plantuml.baraye.ILeaf;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.ICucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.QuoteUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.ClusterHeader;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.svek.DotStringFactory;
import net.sourceforge.plantuml.svek.GeneralImageBuilder;
import net.sourceforge.plantuml.svek.GraphvizCrash;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.SvekNode;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import smetana.core.CString;
import smetana.core.JUtils;
import smetana.core.Macro;
import smetana.core.Z;
import smetana.core.debug.SmetanaDebug;

public class CucaDiagramFileMakerSmetana implements CucaDiagramFileMaker {

	private final ICucaDiagram diagram;

	private final StringBounder stringBounder;
	private final Map<ILeaf, ST_Agnode_s> nodes = new LinkedHashMap<ILeaf, ST_Agnode_s>();
	private final Map<Link, ST_Agedge_s> edges = new LinkedHashMap<Link, ST_Agedge_s>();
	private final Map<IGroup, ST_Agraph_s> clusters = new LinkedHashMap<IGroup, ST_Agraph_s>();

	private final DotStringFactory dotStringFactory;

	class Drawing extends AbstractTextBlock implements TextBlockBackcolored {

		private final YMirror ymirror;
		private final MinMax minMax;

		public Drawing(YMirror ymirror, MinMax minMax) {
			this.ymirror = ymirror;
			this.minMax = minMax;
		}

		public void drawU(UGraphic ug) {
			if (minMax != null) {
				// Matches the adjustment in SvekResult.calculateDimension() except no need to
				// adjust for minY because
				// mirroring takes care of that
				ug = ug.apply(new UTranslate(6 - minMax.getMinX(), 6));
			}

			for (Map.Entry<IGroup, ST_Agraph_s> ent : clusters.entrySet())
				drawGroup(ug, ymirror, ent.getKey(), ent.getValue());

			for (Map.Entry<ILeaf, ST_Agnode_s> ent : nodes.entrySet()) {
				final ILeaf leaf = ent.getKey();
				final ST_Agnode_s agnode = ent.getValue();
				final XPoint2D corner = getCorner(agnode);

				final SvekNode node = dotStringFactory.getBibliotekon().getNode(leaf);
				final IEntityImage image = node.getImage();
				image.drawU(ug.apply(new UTranslate(corner)));
			}

			for (Map.Entry<Link, ST_Agedge_s> ent : edges.entrySet()) {
				final Link link = ent.getKey();
				if (link.isInvis())
					continue;

				final ST_Agedge_s edge = ent.getValue();
				new SmetanaPath(link, edge, ymirror, diagram, getLabel(link), getQuantifier(link, 1),
						getQuantifier(link, 2)).drawU(ug);
			}
		}

		public XDimension2D calculateDimension(StringBounder stringBounder) {
			if (minMax == null)
				throw new UnsupportedOperationException();

			return minMax.getDimension();
		}

		private XPoint2D getCorner(ST_Agnode_s n) {
			final ST_Agnodeinfo_t data = (ST_Agnodeinfo_t) Macro.AGDATA(n);
			final double width = data.width * 72;
			final double height = data.height * 72;
			final double x = data.coord.x;
			final double y = data.coord.y;

			if (ymirror == null)
				return new XPoint2D(x - width / 2, y - height / 2);

			return ymirror.getMirrored(new XPoint2D(x - width / 2, y + height / 2));
		}

		public HColor getBackcolor() {
			return null;
		}

	}

	public CucaDiagramFileMakerSmetana(ICucaDiagram diagram, StringBounder stringBounder) {
		this.diagram = diagram;
		this.stringBounder = stringBounder;
		this.dotStringFactory = new DotStringFactory(stringBounder, diagram);

		printAllSubgroups(diagram.getRootGroup());
		printEntities(getUnpackagedEntities());

	}

	public void drawGroup(UGraphic ug, YMirror ymirror, IGroup group, ST_Agraph_s gr) {
		JUtils.LOG2("drawGroup");
		try {
			final ST_Agrec_s tmp1 = Macro.AGDATA(gr);
			final ST_Agraphinfo_t data = (ST_Agraphinfo_t) tmp1;
			final ST_boxf bb = (ST_boxf) data.bb;
			final double llx = bb.LL.x;
			double lly = bb.LL.y;
			final double urx = bb.UR.x;
			double ury = bb.UR.y;
			if (ymirror != null) {
				final double tmpUry = ury;
				ury = ymirror.getMirrored(lly);
				lly = ymirror.getMirrored(tmpUry);
			}

			final Cluster cluster = dotStringFactory.getBibliotekon().getCluster(group);
			cluster.setPosition(new XPoint2D(llx, lly), new XPoint2D(urx, ury));

			final XDimension2D dimTitle = cluster.getTitleDimension(ug.getStringBounder());
			if (dimTitle != null) {
				final double x = (llx + urx) / 2 - dimTitle.getWidth() / 2;
				cluster.setTitlePosition(new XPoint2D(x, lly));
			}
			JUtils.LOG2("cluster=" + cluster);
			// ug.apply(new UTranslate(llx, lly)).apply(new
			// UChangeColor(HtmlColorUtils.BLUE))
			// .draw(new URectangle(urx - llx, ury - lly));
			cluster.drawU(ug, diagram.getUmlDiagramType(), diagram.getSkinParam());
		} catch (Exception e) {
			System.err.println("CANNOT DRAW GROUP");
		}
	}

	private void printAllSubgroups(IGroup parent) {
		for (IGroup g : diagram.getChildrenGroups(parent)) {
			if (g.isRemoved())
				continue;

			if (diagram.isEmpty(g) && g.getGroupType() == GroupType.PACKAGE) {
				final ISkinParam skinParam = diagram.getSkinParam();
				final EntityFactory entityFactory = diagram.getEntityFactory();
				final ILeaf folder = entityFactory.createLeafForEmptyGroup(g, skinParam);
				printEntityNew(folder);
			} else {
				printSingleGroup(g);
			}
		}
	}

	private void printSingleGroup(IGroup g) {
		if (g.getGroupType() == GroupType.CONCURRENT_STATE)
			return;

		final ClusterHeader clusterHeader = new ClusterHeader((EntityImp) g, diagram.getSkinParam(), diagram,
				stringBounder);
		dotStringFactory.openCluster(g, clusterHeader);
		this.printEntities(g.getLeafsDirect());

		printAllSubgroups(g);

		dotStringFactory.closeCluster();
	}

	private void printEntities(Collection<ILeaf> entities) {
		for (ILeaf ent : entities) {
			if (ent.isRemoved())
				continue;

			printEntity(ent);
		}
	}

	private void exportEntities(ST_Agraph_s g, Collection<ILeaf> entities) {
		for (ILeaf ent : entities) {
			if (ent.isRemoved())
				continue;
			exportEntity(g, ent);
		}
	}

	private void exportEntity(ST_Agraph_s g, ILeaf leaf) {
		final SvekNode node = dotStringFactory.getBibliotekon().getNode(leaf);
		if (node == null) {
			System.err.println("CANNOT FIND NODE");
			return;
		}
		// System.err.println("exportEntity " + leaf);
		final ST_Agnode_s agnode = agnode(g, new CString(node.getUid()), true);
		agsafeset(agnode, new CString("shape"), new CString("box"), new CString(""));
		final String width = "" + (node.getWidth() / 72);
		final String height = "" + (node.getHeight() / 72);
		agsafeset(agnode, new CString("width"), new CString(width), new CString(""));
		agsafeset(agnode, new CString("height"), new CString(height), new CString(""));
		// System.err.println("NODE " + leaf.getUid() + " " + width + " " + height);
		nodes.put(leaf, agnode);
	}

	private void printEntity(ILeaf ent) {
		if (ent.isRemoved())
			throw new IllegalStateException();

		final IEntityImage image = printEntityInternal(ent);
		final SvekNode node = getBibliotekon().createNode(ent, image, dotStringFactory.getColorSequence(),
				stringBounder);
		dotStringFactory.addNode(node);
	}

	private Collection<ILeaf> getUnpackagedEntities() {
		final List<ILeaf> result = new ArrayList<>();
		for (ILeaf ent : diagram.getLeafsvalues())
			if (diagram.getEntityFactory().getRootGroup() == ent.getParentContainer())
				result.add(ent);

		return result;
	}

	private void printCluster(ST_Agraph_s g, Cluster cluster) {
		for (SvekNode node : cluster.getNodes()) {
			final ST_Agnode_s agnode = agnode(g, new CString(node.getUid()), true);
			agsafeset(agnode, new CString("shape"), new CString("box"), new CString(""));
			final String width = "" + (node.getWidth() / 72);
			final String height = "" + (node.getHeight() / 72);
			agsafeset(agnode, new CString("width"), new CString(width), new CString(""));
			agsafeset(agnode, new CString("height"), new CString(height), new CString(""));
			final ILeaf leaf = dotStringFactory.getBibliotekon().getLeaf(node);
			nodes.put(leaf, agnode);
		}

	}

	private static final Lock lock = new ReentrantLock();

	public ImageData createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {
		lock.lock();
		try {
			return createFileLocked(os, dotStrings, fileFormatOption);
		} finally {
			lock.unlock();
		}
	}

	private ImageData createFileLocked(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {

		for (ILeaf leaf : diagram.getLeafsvalues())
			printEntityNew(leaf);

		Z.open();
		try {
			final ST_Agraph_s g = agopen(new CString("g"), Z.z().Agdirected, null);

			// printCluster(g, root);
			exportEntities(g, getUnpackagedEntities());
			exportGroups(g, diagram.getEntityFactory().getRootGroup());

			// for (ILeaf leaf : diagram.getLeafsvalues()) {
			// final Shape shape = bibliotekon.getShape(leaf);
			// final Agnode_s node = agnode(g, new CString(shape.getUid()), true);
			// agsafeset(node, new CString("shape"), new CString("box"), new CString(""));
			// final String width = "" + (shape.getWidth() / 72);
			// final String height = "" + (shape.getHeight() / 72);
			// agsafeset(node, new CString("width"), new CString(width), new CString(""));
			// agsafeset(node, new CString("height"), new CString(height), new CString(""));
			// nodes.put(leaf, node);
			// // System.err
			// // .println("NODE " + leaf.getUid() + " [shape=box, width=" + width + ",
			// height=" + height + "]");
			// }
			//
			for (Link link : diagram.getLinks()) {
				// System.err.println("link=" + link);
				final ST_Agedge_s e = createEdge(g, link);
				// System.err.println("Agedge_s=" + e);
				if (e != null)
					edges.put(link, e);

			}

			final ST_GVC_s gvc = gvContext();
			SmetanaDebug.reset();
			gvLayoutJobs(gvc, g);
			SmetanaDebug.printMe();

			// for (Agedge_s e : edges.values()) {
			// DebugUtils.printDebugEdge(e);
			// }

			final MinMax minMax = TextBlockUtils.getMinMax(new Drawing(null, null), stringBounder, false);

			// imageBuilder.setUDrawable(new Drawing(new YMirror(dim.getHeight())));
			final TextBlock drawable = new Drawing(new YMirror(minMax.getMaxY()), minMax);
			return diagram.createImageBuilder(fileFormatOption).drawable(drawable).write(os);
		} catch (Throwable e) {
			SmetanaDebug.printMe();
			UmlDiagram.exportDiagramError(os, e, fileFormatOption, diagram.seed(), diagram.getMetadata(),
					diagram.getFlashData(), getFailureText3(e));
			return ImageDataSimple.error();
		} finally {
			Z.close();
		}
	}

	private void exportGroups(ST_Agraph_s graph, IGroup parent) {
		for (IGroup g : diagram.getChildrenGroups(parent)) {
			if (g.isRemoved())
				continue;

			if (diagram.isEmpty(g) && g.getGroupType() == GroupType.PACKAGE) {
				final EntityFactory entityFactory = diagram.getEntityFactory();
				final ILeaf folder = entityFactory.getLeafForEmptyGroup(g);
				exportEntity(graph, folder);
			} else {
				exportGroup(graph, g);
			}
		}

	}

	private void exportGroup(ST_Agraph_s graph, IGroup group) {
		final Cluster cluster = getBibliotekon().getCluster(group);
		if (cluster == null) {
			System.err.println("CucaDiagramFileMakerJDot::exportGroup issue");
			return;
		}
		JUtils.LOG2("cluster = " + cluster.getClusterId());
		final ST_Agraph_s cluster1 = agsubg(graph, new CString(cluster.getClusterId()), true);
		if (cluster.isLabel()) {
			final double width = cluster.getTitleAndAttributeWidth();
			final double height = cluster.getTitleAndAttributeHeight() - 5;
			agsafeset(cluster1, new CString("label"), Macro.createHackInitDimensionFromLabel((int) width, (int) height),
					new CString(""));
		}
		this.exportEntities(cluster1, group.getLeafsDirect());
		this.clusters.put(group, cluster1);
		this.exportGroups(cluster1, group);
	}

	private Style getStyle() {
		return StyleSignatureBasic
				.of(SName.root, SName.element, diagram.getUmlDiagramType().getStyleName(), SName.arrow)
				.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
	}

	private TextBlock getLabel(Link link) {
		final double marginLabel = 1; // startUid.equals(endUid) ? 6 : 1;
		ISkinParam skinParam = diagram.getSkinParam();
		final Style style = getStyle();
		final FontConfiguration labelFont = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		final TextBlock label = link.getLabel().create(labelFont,
				skinParam.getDefaultTextAlignment(HorizontalAlignment.CENTER), skinParam);
		if (TextBlockUtils.isEmpty(label, stringBounder))
			return label;

		return TextBlockUtils.withMargin(label, marginLabel, marginLabel);
	}

	private TextBlock getQuantifier(Link link, int n) {
		final String tmp = n == 1 ? link.getQuantifier1() : link.getQuantifier2();
		if (tmp == null)
			return null;

		final double marginLabel = 1; // startUid.equals(endUid) ? 6 : 1;
		ISkinParam skinParam = diagram.getSkinParam();
		final Style style = getStyle();
		final FontConfiguration labelFont = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		final TextBlock label = Display.getWithNewlines(tmp).create(labelFont,
				skinParam.getDefaultTextAlignment(HorizontalAlignment.CENTER), skinParam);
		if (TextBlockUtils.isEmpty(label, stringBounder))
			return label;

		return TextBlockUtils.withMargin(label, marginLabel, marginLabel);
	}

	private ST_Agnode_s getAgnodeFromLeaf(IEntity entity) {
		final ST_Agnode_s n = nodes.get(entity);
		if (n != null)
			return n;

		try {
			final String id = getBibliotekon().getNodeUid((ILeaf) entity);
			for (Map.Entry<ILeaf, ST_Agnode_s> ent : nodes.entrySet())
				if (id.equals(getBibliotekon().getNodeUid(ent.getKey())))
					return ent.getValue();

		} catch (IllegalStateException e) {
			System.err.println("UNKNOWN ENTITY");
		}
		return null;

	}

	private ST_Agedge_s createEdge(final ST_Agraph_s g, Link link) {
		final ST_Agnode_s n = getAgnodeFromLeaf(link.getEntity1());
		final ST_Agnode_s m = getAgnodeFromLeaf(link.getEntity2());
		if (n == null)
			return null;

		if (m == null)
			return null;

		final ST_Agedge_s e = agedge(g, n, m, null, true);
		// System.err.println("createEdge " + link);
		agsafeset(e, new CString("arrowtail"), new CString("none"), new CString(""));
		agsafeset(e, new CString("arrowhead"), new CString("none"), new CString(""));

		int length = link.getLength();
		// System.err.println("length=" + length);
		// if (/* pragma.horizontalLineBetweenDifferentPackageAllowed() ||
		// */link.isInvis() || length != 1) {
		agsafeset(e, new CString("minlen"), new CString("" + (length - 1)), new CString(""));
		// }
		// System.err.print("EDGE " + link.getEntity1().getUid() + "->" +
		// link.getEntity2().getUid() + " minlen="
		// + (length - 1) + " ");

		final TextBlock label = getLabel(link);
		if (TextBlockUtils.isEmpty(label, stringBounder) == false) {
			final XDimension2D dimLabel = label.calculateDimension(stringBounder);
			// System.err.println("dimLabel = " + dimLabel);
			final CString hackDim = Macro.createHackInitDimensionFromLabel((int) dimLabel.getWidth(),
					(int) dimLabel.getHeight());
			agsafeset(e, new CString("label"), hackDim, new CString(""));
			// System.err.print("label=" + hackDim.getContent());
		}
		final TextBlock q1 = getQuantifier(link, 1);
		if (q1 != null) {
			final XDimension2D dimLabel = q1.calculateDimension(stringBounder);
			// System.err.println("dimLabel = " + dimLabel);
			final CString hackDim = Macro.createHackInitDimensionFromLabel((int) dimLabel.getWidth(),
					(int) dimLabel.getHeight());
			agsafeset(e, new CString("taillabel"), hackDim, new CString(""));
		}
		final TextBlock q2 = getQuantifier(link, 2);
		if (q2 != null) {
			final XDimension2D dimLabel = q2.calculateDimension(stringBounder);
			// System.err.println("dimLabel = " + dimLabel);
			final CString hackDim = Macro.createHackInitDimensionFromLabel((int) dimLabel.getWidth(),
					(int) dimLabel.getHeight());
			agsafeset(e, new CString("headlabel"), hackDim, new CString(""));
		}
		// System.err.println();
		return e;
	}

	static private List<String> getFailureText3(Throwable exception) {
		Logme.error(exception);
		final List<String> strings = new ArrayList<>();
		strings.add("An error has occured : " + exception);
		final String quote = StringUtils.rot(QuoteUtils.getSomeQuote());
		strings.add("<i>" + quote);
		strings.add(" ");
		GraphvizCrash.addProperties(strings);
		strings.add(" ");
		strings.add("Sorry, the subproject Smetana is not finished yet...");
		strings.add(" ");
		strings.add("You should send this diagram and this image to <b>plantuml@gmail.com</b> or");
		strings.add("post to <b>http://plantuml.com/qa</b> to solve this issue.");
		strings.add(" ");
		return strings;
	}

	private void printEntityNew(ILeaf ent) {
		if (ent.isRemoved()) {
			System.err.println("Jdot STRANGE: entity is removed");
			return;
		}
		final IEntityImage image = printEntityInternal(ent);
		final SvekNode shape = getBibliotekon().createNode(ent, image, dotStringFactory.getColorSequence(),
				stringBounder);
		// dotStringFactory.addShape(shape);
	}

	private Bibliotekon getBibliotekon() {
		return dotStringFactory.getBibliotekon();
	}

	private IEntityImage printEntityInternal(ILeaf ent) {
		if (ent.isRemoved())
			throw new IllegalStateException();

		if (ent.getSvekImage() == null) {
			ISkinParam skinParam = diagram.getSkinParam();
			if (skinParam.sameClassWidth()) {
				System.err.println("NOT YET IMPLEMENED");
//				throw new UnsupportedOperationException();
				// final double width = getMaxWidth();
				// skinParam = new SkinParamSameClassWidth(dotData.getSkinParam(), width);
			}

			return GeneralImageBuilder.createEntityImageBlock(ent, skinParam, diagram.isHideEmptyDescriptionForState(),
					diagram, getBibliotekon(), null, diagram.getUmlDiagramType(), diagram.getLinks());
		}
		return ent.getSvekImage();
	}

}
