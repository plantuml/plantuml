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
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_Agraphinfo_t;
import h.ST_Agrec_s;
import h.ST_GVC_s;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.abel.CucaNote;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.ICucaDiagram;
import net.sourceforge.plantuml.eggs.QuoteUtils;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MinMaxMutable;
import net.sourceforge.plantuml.klimt.geom.Rankdir;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.style.ISkinParam;
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
import net.sourceforge.plantuml.svek.image.EntityImageNote;
import net.sourceforge.plantuml.svek.image.EntityImageNoteLink;
import net.sourceforge.plantuml.utils.Position;
import smetana.core.CString;
import smetana.core.Globals;
import smetana.core.JUtils;
import smetana.core.Macro;
import smetana.core.debug.SmetanaDebug;

public class CucaDiagramFileMakerSmetana implements CucaDiagramFileMaker {
	// ::remove folder when __HAXE__

	private final ICucaDiagram diagram;

	private final StringBounder stringBounder;
	private final Map<Entity, ST_Agnode_s> nodes = new LinkedHashMap<Entity, ST_Agnode_s>();
	private final Map<Entity, ST_Agnode_s> coreNodes = new LinkedHashMap<Entity, ST_Agnode_s>();
	private final Map<Link, ST_Agedge_s> edges = new LinkedHashMap<Link, ST_Agedge_s>();
	private final Map<Link, SmetanaPath> smetanaPathes = new LinkedHashMap<Link, SmetanaPath>();
	private final Map<Entity, ST_Agraph_s> clusters = new LinkedHashMap<Entity, ST_Agraph_s>();

	private final DotStringFactory dotStringFactory;
	private final Rankdir rankdir;

	private MinMaxMutable getSmetanaMinMax() {
		final MinMaxMutable result = MinMaxMutable.getEmpty(false);
		for (ST_Agnode_s n : nodes.values()) {
			final BoxInfo data = BoxInfo.fromNode(n);
			result.addPoint(data.getUpperRight());
			result.addPoint(data.getLowerLeft());
		}
		for (ST_Agraph_s gr : clusters.values()) {
			final ST_Agrec_s tmp1 = gr.data;
			if (tmp1 instanceof ST_Agraphinfo_t == false) {
				System.err.println("ERROR IN CucaDiagramFileMakerSmetana");
				continue;
			}
			final ST_Agraphinfo_t info = (ST_Agraphinfo_t) tmp1;
			final BoxInfo data = BoxInfo.fromGraphInfo(info);
			result.addPoint(data.getUpperRight());
			result.addPoint(data.getLowerLeft());

		}
		return result;
	}

	class Drawing extends AbstractTextBlock {

		private final YMirror ymirror;
		private final MinMaxMutable minMax;

		public Drawing() {
			this.minMax = getSmetanaMinMax();
			this.ymirror = new YMirror(minMax.getMaxY() + 6);
		}

		public void drawU(UGraphic ug) {

			smetanaPathes.clear();

			ug = ug.apply(new UTranslate(6, 6 - minMax.getMinY()));

			for (Map.Entry<Link, ST_Agedge_s> ent : edges.entrySet()) {
				final Link link = ent.getKey();
				if (link.isInvis())
					continue;

				final ST_Agedge_s edge = ent.getValue();
				final SmetanaPath smetanaPath = new SmetanaPath(link, edge, ymirror, diagram, getLabel(link),
						getQuantifier(link, 1), getQuantifier(link, 2), dotStringFactory.getBibliotekon());
				smetanaPathes.put(link, smetanaPath);
			}

			for (Map.Entry<Entity, ST_Agraph_s> ent : clusters.entrySet())
				drawGroup(ug, ymirror, ent.getKey(), ent.getValue());

			for (Map.Entry<Entity, ST_Agnode_s> ent : nodes.entrySet()) {
				final Entity leaf = ent.getKey();
				final ST_Agnode_s agnode = ent.getValue();
				final XPoint2D corner = getCorner(agnode);

				final SvekNode node = dotStringFactory.getBibliotekon().getNode(leaf);
				node.resetMoveSvek();
				node.moveSvek(corner.getX(), corner.getY());
				final IEntityImage image = node.getImage();
				image.drawU(ug.apply(UTranslate.point(corner)));
			}

			for (Entry<Link, SmetanaPath> ent : smetanaPathes.entrySet())
				if (ent.getKey().isOpale() == false)
					ent.getValue().drawU(ug);

		}

		public XDimension2D calculateDimension(StringBounder stringBounder) {
			return minMax.getDimension().delta(6);
		}

		private XPoint2D getCorner(ST_Agnode_s n) {
			final BoxInfo data = BoxInfo.fromNode(n);
			return ymirror.getMirrored(data.getLowerLeft());
		}

		public HColor getBackcolor() {
			return null;
		}

	}

	public CucaDiagramFileMakerSmetana(ICucaDiagram diagram, StringBounder stringBounder) {
		this.diagram = diagram;
		this.stringBounder = stringBounder;
		this.dotStringFactory = new DotStringFactory(stringBounder, diagram);
		this.rankdir = diagram.getSkinParam().getRankdir();

		printAllSubgroups(diagram.getRootGroup());
		printEntities(getUnpackagedEntities());

		for (Link link : diagram.getLinks()) {
			if (link.isRemoved())
				continue;

			if (isOpalisable(link.getEntity1())) {
				final SvekNode node = dotStringFactory.getBibliotekon().getNode(link.getEntity1());
				final SvekNode other = dotStringFactory.getBibliotekon().getNode(link.getEntity2());
				if (other != null) {
					((EntityImageNote) node.getImage()).setOpaleLink(link, node, other, smetanaPathes);
					link.setOpale(true);
				}
			} else if (isOpalisable(link.getEntity2())) {
				final SvekNode node = dotStringFactory.getBibliotekon().getNode(link.getEntity2());
				final SvekNode other = dotStringFactory.getBibliotekon().getNode(link.getEntity1());
				if (other != null) {
					((EntityImageNote) node.getImage()).setOpaleLink(link, node, other, smetanaPathes);
					link.setOpale(true);
				}
			}

		}

	}

	// Duplicate GeneralImageBuilder
	private boolean isOpalisable(Entity entity) {
		if (entity.isGroup())
			return false;

		if (entity.getLeafType() != LeafType.NOTE)
			return false;

		final Link single = onlyOneLink(entity);
		if (single == null)
			return false;

		return single.getOther(entity).getLeafType() != LeafType.NOTE;
	}

	// Duplicate GeneralImageBuilder
	private Link onlyOneLink(Entity ent) {
		Link single = null;
		for (Link link : diagram.getLinks()) {
			if (link.isInvis())
				continue;
			if (link.contains(ent) == false)
				continue;

			if (single != null)
				return null;
			single = link;
		}
		return single;
	}

	private void drawGroup(UGraphic ug, YMirror ymirror, Entity group, ST_Agraph_s gr) {
		JUtils.LOG2("drawGroup");
		try {
			final ST_Agrec_s tmp1 = gr.data;
			final ST_Agraphinfo_t data = (ST_Agraphinfo_t) tmp1;
			final BoxInfo boxInfo = BoxInfo.fromGraphInfo(data);
			final XPoint2D upperRight = ymirror.getMirrored(boxInfo.getUpperRight());
			final XPoint2D lowerLeft = ymirror.getMirrored(boxInfo.getLowerLeft());

			final Cluster cluster = dotStringFactory.getBibliotekon().getCluster(group);
			cluster.setPosition(upperRight, lowerLeft);

			final XDimension2D dimTitle = cluster.getTitleDimension(ug.getStringBounder());
			if (dimTitle != null) {
				final double x = (upperRight.getX() + lowerLeft.getX()) / 2 - dimTitle.getWidth() / 2;
				cluster.setTitlePosition(new XPoint2D(x, Math.min(upperRight.getY(), lowerLeft.getY())));
			}
			JUtils.LOG2("cluster=" + cluster);
			cluster.drawU(ug, diagram.getUmlDiagramType());
			// ug.apply(new UTranslate(llx, lly)).apply(HColors.BLUE).draw(new
			// URectangle(urx - llx, ury - lly));
		} catch (Exception e) {
			System.err.println("CANNOT DRAW GROUP");
		}
	}

	private void printAllSubgroups(Entity parent) {
		for (Entity g : diagram.getChildrenGroups(parent)) {
			if (g.isRemoved())
				continue;

			if (diagram.isEmpty(g) && g.getGroupType() == GroupType.PACKAGE) {
				g.muteToType(LeafType.EMPTY_PACKAGE);
				printEntity(g);
			} else {
				printSingleGroup(g);
			}
		}
	}

	private void printSingleGroup(Entity g) {
		if (g.getGroupType() == GroupType.CONCURRENT_STATE)
			return;

		if (g.isPacked() == false) {
			final ClusterHeader clusterHeader = new ClusterHeader(g, diagram.getSkinParam(), diagram, stringBounder);
			dotStringFactory.openCluster(g, clusterHeader);
		}

		this.printEntities(g.leafs());

		printAllSubgroups(g);

		if (g.isPacked() == false) {
			dotStringFactory.closeCluster();
		}
	}

	private void printEntities(Collection<Entity> entities) {
		for (Entity ent : entities) {
			if (ent.isRemoved())
				continue;

			printEntity(ent);
		}
	}

	private void exportEntities(Globals zz, ST_Agraph_s cluster, Collection<Entity> entities) {
		for (Entity ent : entities) {
			if (ent.isRemoved())
				continue;
			exportEntity(zz, cluster, ent);
		}
	}

	private XDimension2D getDim(SvekNode node) {
		final double width = node.getWidth() / 72;
		final double height = node.getHeight() / 72;
		return new XDimension2D(width, height);
	}

	private ST_Agnode_s getCoreFromGroup(Globals zz, Entity group) {
		ST_Agnode_s result = coreNodes.get(group);
		if (result != null)
			return result;

		final ST_Agraph_s cluster = clusters.get(group);
		if (cluster == null)
			throw new IllegalStateException();

		result = agnode(zz, cluster, new CString("z" + group.getUid()), true);
		agsafeset(zz, result, new CString("shape"), new CString("box"), new CString(""));
		agsafeset(zz, result, new CString("width"), new CString("0.1"), new CString(""));
		agsafeset(zz, result, new CString("height"), new CString("0.1"), new CString(""));
		coreNodes.put(group, result);
		return result;
	}

	private void exportEntity(Globals zz, ST_Agraph_s cluster, Entity leaf) {
		final SvekNode node = dotStringFactory.getBibliotekon().getNode(leaf);
		if (node == null) {
			System.err.println("CANNOT FIND NODE");
			return;
		}
		final ST_Agnode_s agnode = agnode(zz, cluster, new CString(node.getUid()), true);
		agsafeset(zz, agnode, new CString("shape"), new CString("box"), new CString(""));
		final XDimension2D dim = getDim(node);
		final String width = "" + dim.getWidth();
		final String height = "" + dim.getHeight();
		agsafeset(zz, agnode, new CString("width"), new CString(width), new CString(""));
		agsafeset(zz, agnode, new CString("height"), new CString(height), new CString(""));
		nodes.put(leaf, agnode);
	}

	private void printEntity(Entity ent) {
		if (ent.isRemoved())
			throw new IllegalStateException();

		final IEntityImage image = printEntityInternal(ent);
		final SvekNode node = getBibliotekon().createNode(ent, image, dotStringFactory.getColorSequence(),
				stringBounder);
		dotStringFactory.addNode(node);
	}

	private Collection<Entity> getUnpackagedEntities() {
		final List<Entity> result = new ArrayList<>();
		for (Entity ent : diagram.getEntityFactory().leafs())
			if (diagram.getEntityFactory().getRootGroup() == ent.getParentContainer())
				result.add(ent);

		return result;
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

	@Override
	public void createOneGraphic(UGraphic ug) {
		final Globals zz = Globals.open();
		try {
			final TextBlock textBlock = getTextBlock(zz);
			textBlock.drawU(ug);
		} catch (Throwable e) {
			SmetanaDebug.printMe();
		} finally {
			Globals.close();
		}
	}

	private ImageData createFileLocked(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {

		final Globals zz = Globals.open();
		try {
			final TextBlock drawable = getTextBlock(zz);
			return diagram.createImageBuilder(fileFormatOption).drawable(drawable).write(os);
		} catch (Throwable e) {
			SmetanaDebug.printMe();
			UmlDiagram.exportDiagramError(os, e, fileFormatOption, diagram.seed(), diagram.getMetadata(),
					diagram.getFlashData(), getFailureText3(e));
			return ImageDataSimple.error();
		} finally {
			Globals.close();
		}
	}

	private TextBlock getTextBlock(Globals zz) {
		final ST_Agraph_s g = agopen(zz, new CString("g"), zz.Agdirected, null);

		exportEntities(zz, g, getUnpackagedEntities());
		exportGroups(zz, g, diagram.getEntityFactory().getRootGroup());

		for (Link link : diagram.getLinks()) {
			final ST_Agedge_s e = createEdge(zz, g, link);
			if (e != null)
				edges.put(link, e);

		}

		final ST_GVC_s gvc = gvContext(zz);
		SmetanaDebug.reset();
		if (rankdir == Rankdir.LEFT_TO_RIGHT)
			agsafeset(zz, g, new CString("rankdir"), new CString("LR"), new CString("LR"));
		gvLayoutJobs(zz, gvc, g);
		SmetanaDebug.printMe();

		final TextBlock drawable = new Drawing();
		return drawable;
	}

	private void exportGroups(Globals zz, ST_Agraph_s graph, Entity parent) {
		for (Entity g : diagram.getChildrenGroups(parent)) {
			if (g.isRemoved())
				continue;

			if (diagram.isEmpty(g) && g.getGroupType() == GroupType.PACKAGE)
				exportEntity(zz, graph, g);
			else
				exportGroup(zz, graph, g);

		}

	}

	private void exportGroup(Globals zz, ST_Agraph_s graph, Entity group) {
		if (group.isPacked()) {
			this.exportEntities(zz, graph, group.leafs());
			this.exportGroups(zz, graph, group);
			return;
		}
		final Cluster cluster = getBibliotekon().getCluster(group);
		if (cluster == null) {
			System.err.println("CucaDiagramFileMakerSmetana::exportGroup issue");
			return;
		}
		JUtils.LOG2("cluster = " + cluster.getClusterId());
		final ST_Agraph_s cluster1 = agsubg(zz, graph, new CString(cluster.getClusterId()), true);
		if (cluster.isLabel()) {
			final double width = cluster.getTitleAndAttributeWidth();
			final double height = cluster.getTitleAndAttributeHeight() - 5;
			agsafeset(zz, cluster1, new CString("label"), createLabelDim(width, height), new CString(""));
		}
		this.exportEntities(zz, cluster1, group.leafs());
		this.clusters.put(group, cluster1);
		this.exportGroups(zz, cluster1, group);
	}

	private CString createLabelDim(final double width, final double height) {
		return Macro.createHackInitDimensionFromLabel((int) width, (int) height);
	}

	private Style getStyle() {
		return StyleSignatureBasic
				.of(SName.root, SName.element, diagram.getUmlDiagramType().getStyleName(), SName.arrow)
				.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
	}

	private TextBlock getLabel(Link link) {
		ISkinParam skinParam = diagram.getSkinParam();
		final double marginLabel = 1; // startUid.equals(endUid) ? 6 : 1;
		final Style style = getStyle();

		final FontConfiguration labelFont = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		TextBlock labelOnly = link.getLabel().create(labelFont,
				skinParam.getDefaultTextAlignment(HorizontalAlignment.CENTER), skinParam);

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

	private ST_Agnode_s getAgnodeFromLeaf(Entity entity) {
		final ST_Agnode_s n = nodes.get(entity);
		if (n != null)
			return n;

		try {
			final String id = getBibliotekon().getNodeUid((Entity) entity);
			for (Map.Entry<Entity, ST_Agnode_s> ent : nodes.entrySet())
				if (id.equals(getBibliotekon().getNodeUid(ent.getKey())))
					return ent.getValue();

		} catch (IllegalStateException e) {
			System.err.println("UNKNOWN ENTITY");
		}
		return null;

	}

	private ST_Agedge_s createEdge(Globals zz, final ST_Agraph_s g, Link link) {

		final ST_Agnode_s node1;
		final ST_Agnode_s node2;

		if (link.getEntity1().isGroup())
			node1 = getCoreFromGroup(zz, link.getEntity1());
		else
			node1 = getAgnodeFromLeaf(link.getEntity1());

		if (link.getEntity2().isGroup())
			node2 = getCoreFromGroup(zz, link.getEntity2());
		else
			node2 = getAgnodeFromLeaf(link.getEntity2());

		if (node1 == null || node2 == null)
			return null;
		// throw new IllegalStateException();

		final ST_Agedge_s e = agedge(zz, g, node1, node2, null, true);
		agsafeset(zz, e, new CString("arrowtail"), new CString("none"), new CString(""));
		agsafeset(zz, e, new CString("arrowhead"), new CString("none"), new CString(""));

		int length = link.getLength();
		agsafeset(zz, e, new CString("minlen"), new CString("" + (length - 1)), new CString(""));

		final TextBlock label = getLabel(link);
		if (TextBlockUtils.isEmpty(label, stringBounder) == false) {
			final XDimension2D dimLabel = label.calculateDimension(stringBounder);
			final CString hackDim = createLabelDim(dimLabel.getWidth(), dimLabel.getHeight());
			agsafeset(zz, e, new CString("label"), hackDim, new CString(""));
		}
		final TextBlock q1 = getQuantifier(link, 1);
		if (q1 != null) {
			final XDimension2D dimLabel = q1.calculateDimension(stringBounder);
			final CString hackDim = createLabelDim(dimLabel.getWidth(), dimLabel.getHeight());
			agsafeset(zz, e, new CString("taillabel"), hackDim, new CString(""));
		}
		final TextBlock q2 = getQuantifier(link, 2);
		if (q2 != null) {
			final XDimension2D dimLabel = q2.calculateDimension(stringBounder);
			final CString hackDim = createLabelDim(dimLabel.getWidth(), dimLabel.getHeight());
			agsafeset(zz, e, new CString("headlabel"), hackDim, new CString(""));
		}
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
		strings.add("post to <b>https://plantuml.com/qa</b> to solve this issue.");
		strings.add(" ");
		return strings;
	}

	private Bibliotekon getBibliotekon() {
		return dotStringFactory.getBibliotekon();
	}

	private IEntityImage printEntityInternal(Entity ent) {
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

			final IEntityImage result = GeneralImageBuilder.createEntityImageBlock(ent, skinParam,
					diagram.isHideEmptyDescriptionForState(), diagram, getBibliotekon(), null,
					diagram.getUmlDiagramType(), diagram.getLinks());
			ent.setSvekImage(result);
			return result;
		}
		return ent.getSvekImage();
	}

}
