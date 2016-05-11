/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 18280 $
 *
 */
package net.sourceforge.plantuml.jdot;

import static gen.lib.cgraph.attr__c.agsafeset;
import static gen.lib.cgraph.edge__c.agedge;
import static gen.lib.cgraph.graph__c.agopen;
import static gen.lib.cgraph.node__c.agnode;
import static gen.lib.cgraph.subg__c.agsubg;
import static gen.lib.gvc.gvc__c.gvContext;
import static gen.lib.gvc.gvlayout__c.gvLayoutJobs;
import h.Agedge_s;
import h.Agnode_s;
import h.Agnodeinfo_t;
import h.Agraph_s;
import h.Agraphinfo_t;
import h.GVC_s;
import h.boxf;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.MethodsOrFieldsArea;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.QuoteUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.ColorSequence;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.svek.CucaDiagramFileMakerSvek2;
import net.sourceforge.plantuml.svek.DotStringFactory;
import net.sourceforge.plantuml.svek.GraphvizCrash;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.Shape;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.sprite.Sprite;
import smetana.core.CString;
import smetana.core.JUtils;
import smetana.core.Macro;
import smetana.core.Z;
import smetana.core.__ptr__;
import smetana.core.__struct__;

public class CucaDiagramFileMakerJDot implements CucaDiagramFileMaker {

	private final CucaDiagram diagram;

	private final StringBounder stringBounder = TextBlockUtils.getDummyStringBounder();
	private final Map<ILeaf, Agnode_s> nodes = new LinkedHashMap<ILeaf, Agnode_s>();
	private final Map<Link, Agedge_s> edges = new LinkedHashMap<Link, Agedge_s>();
	private final Map<IGroup, Agraph_s> clusters = new LinkedHashMap<IGroup, Agraph_s>();
	private Map<IGroup, ILeaf> emptyGroups = new HashMap<IGroup, ILeaf>();

	private final DotStringFactory dotStringFactory;

	class Drawing implements UDrawable {

		private final YMirror ymirror;

		public Drawing(YMirror ymirror) {
			this.ymirror = ymirror;
		}

		public void drawU(UGraphic ug) {

			for (Map.Entry<IGroup, Agraph_s> ent : clusters.entrySet()) {
				drawGroup(ug, ymirror, ent.getKey(), ent.getValue());
			}

			for (Map.Entry<ILeaf, Agnode_s> ent : nodes.entrySet()) {
				final ILeaf leaf = ent.getKey();
				final Agnode_s node = ent.getValue();
				final Point2D corner = getCorner(node);

				final Shape shape = dotStringFactory.getBibliotekon().getShape(leaf);
				final IEntityImage image = shape.getImage();
				image.drawU(ug.apply(new UTranslate(corner)));
			}

			for (Map.Entry<Link, Agedge_s> ent : edges.entrySet()) {
				final Link link = ent.getKey();
				final Agedge_s edge = ent.getValue();
				new JDotPath(link, edge, ymirror, diagram, getLabel(link)).drawU(ug);
			}
		}

		private Point2D getCorner(Agnode_s n) {
			final Agnodeinfo_t data = (Agnodeinfo_t) Macro.AGDATA(n).castTo(Agnodeinfo_t.class);
			final double width = data.getDouble("width") * 72;
			final double height = data.getDouble("height") * 72;
			final double x = data.getStruct("coord").getDouble("x");
			final double y = data.getStruct("coord").getDouble("y");

			if (ymirror == null) {
				return new Point2D.Double(x - width / 2, y - height / 2);
			}
			return ymirror.getMirrored(new Point2D.Double(x - width / 2, y + height / 2));
		}

	}

	public CucaDiagramFileMakerJDot(CucaDiagram diagram) {
		this.diagram = diagram;
		this.dotStringFactory = new DotStringFactory(new ColorSequence(), stringBounder, diagram);

		printGroups(diagram.getRootGroup());
		printEntities(getUnpackagedEntities());

	}

	public void drawGroup(UGraphic ug, YMirror ymirror, IGroup group, Agraph_s gr) {
		JUtils.LOG2("drawGroup");
		final __ptr__ data = Macro.AGDATA(gr).castTo(Agraphinfo_t.class);
		final __struct__<boxf> bb = data.getStruct("bb");
		final double llx = bb.getStruct("LL").getDouble("x");
		double lly = bb.getStruct("LL").getDouble("y");
		final double urx = bb.getStruct("UR").getDouble("x");
		double ury = bb.getStruct("UR").getDouble("y");
		if (ymirror != null) {
			final double tmpUry = ury;
			ury = ymirror.getMirrored(lly);
			lly = ymirror.getMirrored(tmpUry);
		}

		final Cluster cluster = dotStringFactory.getBibliotekon().getCluster(group);
		cluster.setPosition(llx, lly, urx, ury);
		JUtils.LOG2("cluster=" + cluster);
		// ug.apply(new UTranslate(llx, lly)).apply(new UChangeColor(HtmlColorUtils.BLUE))
		// .draw(new URectangle(urx - llx, ury - lly));
		cluster.drawU(ug, new UStroke(1.5), diagram.getUmlDiagramType(), diagram.getSkinParam());
	}

	private void printGroups(IGroup parent) {
		for (IGroup g : diagram.getChildrenGroups(parent)) {
			if (g.isRemoved()) {
				continue;
			}
			if (diagram.isEmpty(g) && g.getGroupType() == GroupType.PACKAGE) {
				final ILeaf folder = diagram.getEntityFactory().createLeaf(g.getCode(), g.getDisplay(),
						LeafType.EMPTY_PACKAGE, g.getParentContainer(), null, diagram.getNamespaceSeparator());
				emptyGroups.put(g, folder);
				final USymbol symbol = g.getUSymbol();
				folder.setUSymbol(symbol);
				folder.setStereotype(g.getStereotype());
				if (g.getColors(diagram.getSkinParam()).getColor(ColorType.BACK) == null) {
					final ColorParam param = symbol == null ? ColorParam.packageBackground : symbol.getColorParamBack();
					final HtmlColor c1 = diagram.getSkinParam().getHtmlColor(param, g.getStereotype(), false);
					folder.setSpecificColorTOBEREMOVED(ColorType.BACK, c1 == null ? diagram.getSkinParam()
							.getBackgroundColor() : c1);
				} else {
					folder.setSpecificColorTOBEREMOVED(ColorType.BACK,
							g.getColors(diagram.getSkinParam()).getColor(ColorType.BACK));
				}
				printEntityNew(folder);
			} else {
				printGroup(g);
			}
		}
	}

	private void printGroup(IGroup g) {
		if (g.getGroupType() == GroupType.CONCURRENT_STATE) {
			return;
		}
		int titleAndAttributeWidth = 0;
		int titleAndAttributeHeight = 0;

		final TextBlock title = getTitleBlock(g);
		final TextBlock stereo = getStereoBlock(g);
		final TextBlock stereoAndTitle = TextBlockUtils.mergeTB(stereo, title, HorizontalAlignment.CENTER);
		final Dimension2D dimLabel = stereoAndTitle.calculateDimension(stringBounder);
		if (dimLabel.getWidth() > 0) {
			final List<Member> members = ((IEntity) g).getBodier().getFieldsToDisplay();
			final TextBlockWidth attribute;
			if (members.size() == 0) {
				attribute = new TextBlockEmpty();
			} else {
				attribute = new MethodsOrFieldsArea(members, FontParam.STATE_ATTRIBUTE, diagram.getSkinParam(),
						g.getStereotype());
			}
			final Dimension2D dimAttribute = attribute.calculateDimension(stringBounder);
			final double attributeHeight = dimAttribute.getHeight();
			final double attributeWidth = dimAttribute.getWidth();
			final double marginForFields = attributeHeight > 0 ? IEntityImage.MARGIN : 0;
			final USymbol uSymbol = g.getUSymbol();
			final int suppHeightBecauseOfShape = uSymbol == null ? 0 : uSymbol.suppHeightBecauseOfShape();
			final int suppWidthBecauseOfShape = uSymbol == null ? 0 : uSymbol.suppWidthBecauseOfShape();

			titleAndAttributeWidth = (int) Math.max(dimLabel.getWidth(), attributeWidth) + suppWidthBecauseOfShape;
			titleAndAttributeHeight = (int) (dimLabel.getHeight() + attributeHeight + marginForFields + suppHeightBecauseOfShape);
		}

		dotStringFactory.openCluster(g, titleAndAttributeWidth, titleAndAttributeHeight, title, stereo);
		this.printEntities(g.getLeafsDirect());

		printGroups(g);

		dotStringFactory.closeCluster();
	}

	private void printEntities(Collection<ILeaf> entities2) {
		for (ILeaf ent : entities2) {
			if (ent.isRemoved()) {
				continue;
			}
			printEntity(ent);
		}
	}

	private void exportEntities(Agraph_s g, Collection<ILeaf> entities2) {
		for (ILeaf ent : entities2) {
			if (ent.isRemoved()) {
				continue;
			}
			exportEntity(g, ent);
		}
	}

	private void exportEntity(Agraph_s g, ILeaf leaf) {
		final Shape shape = dotStringFactory.getBibliotekon().getShape(leaf);
		final Agnode_s node = agnode(g, new CString(shape.getUid()), true);
		agsafeset(node, new CString("shape"), new CString("box"), new CString(""));
		final String width = "" + (shape.getWidth() / 72);
		final String height = "" + (shape.getHeight() / 72);
		agsafeset(node, new CString("width"), new CString(width), new CString(""));
		agsafeset(node, new CString("height"), new CString(height), new CString(""));
		// System.err.println("NODE " + leaf.getUid() + " " + width + " " + height);
		nodes.put(leaf, node);
	}

	private void printEntity(ILeaf ent) {
		if (ent.isRemoved()) {
			throw new IllegalStateException();
		}
		final IEntityImage image = printEntityInternal(ent);
		final Dimension2D dim = image.calculateDimension(stringBounder);
		final Shape shape = new Shape(image, image.getShapeType(), dim.getWidth(), dim.getHeight(),
				dotStringFactory.getColorSequence(), ent.isTop(), image.getShield(), ent.getEntityPosition());
		dotStringFactory.addShape(shape);
		getBibliotekon().putShape(ent, shape);
	}

	private TextBlock getTitleBlock(IGroup g) {
		final Display label = g.getDisplay();
		if (label == null) {
			return TextBlockUtils.empty(0, 0);
		}

		final ISkinParam skinParam = diagram.getSkinParam();
		final FontConfiguration fontConfiguration = g.getFontConfigurationForTitle(skinParam);
		return label.create(fontConfiguration, HorizontalAlignment.CENTER, skinParam);
	}

	private TextBlock getStereoBlock(IGroup g) {
		final Stereotype stereotype = g.getStereotype();
		if (stereotype == null) {
			return TextBlockUtils.empty(0, 0);
		}
		if (stereotype.getSprite() != null) {
			final Sprite tmp = diagram.getSkinParam().getSprite(stereotype.getSprite());
			if (tmp != null) {
				return tmp.asTextBlock(stereotype.getHtmlColor(), 1);
			}
		}
		final List<String> stereos = stereotype.getLabels(diagram.getSkinParam().useGuillemet());
		if (stereos == null) {
			return TextBlockUtils.empty(0, 0);
		}
		final boolean show = diagram.showPortion(EntityPortion.STEREOTYPE, g);
		if (show == false) {
			return TextBlockUtils.empty(0, 0);
		}

		final FontParam fontParam = FontParam.PACKAGE_STEREOTYPE;
		return Display.create(stereos).create(new FontConfiguration(diagram.getSkinParam(), fontParam, stereotype),
				HorizontalAlignment.CENTER, diagram.getSkinParam());
	}

	private Collection<ILeaf> getUnpackagedEntities() {
		final List<ILeaf> result = new ArrayList<ILeaf>();
		for (ILeaf ent : diagram.getLeafsvalues()) {
			if (diagram.getEntityFactory().getRootGroup() == ent.getParentContainer()) {
				result.add(ent);
			}
		}
		return result;
	}

	private void printCluster(Agraph_s g, Cluster cluster) {
		for (Shape shape : cluster.getShapes()) {
			final Agnode_s node = agnode(g, new CString(shape.getUid()), true);
			agsafeset(node, new CString("shape"), new CString("box"), new CString(""));
			final String width = "" + (shape.getWidth() / 72);
			final String height = "" + (shape.getHeight() / 72);
			agsafeset(node, new CString("width"), new CString(width), new CString(""));
			agsafeset(node, new CString("height"), new CString(height), new CString(""));
			final ILeaf leaf = dotStringFactory.getBibliotekon().getLeaf(shape);
			nodes.put(leaf, node);
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

		for (ILeaf leaf : diagram.getLeafsvalues()) {
			printEntityNew(leaf);
		}

		Z.open();
		try {
			final Agraph_s g = agopen(new CString("g"), Z.z().Agdirected, null);

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
			// // .println("NODE " + leaf.getUid() + " [shape=box, width=" + width + ", height=" + height + "]");
			// }
			//
			for (Link link : diagram.getLinks()) {
				// System.err.println("link=" + link);
				final Agedge_s e = createEdge(g, link);
				// System.err.println("Agedge_s=" + e);
				if (e != null) {
					edges.put(link, e);
				}
			}

			final GVC_s gvc = gvContext();
			gvLayoutJobs(gvc, g);

			// for (Agedge_s e : edges.values()) {
			// DebugUtils.printDebugEdge(e);
			// }

			final double scale = 1;

			final ImageBuilder imageBuilder = new ImageBuilder(diagram.getSkinParam().getColorMapper(), scale, null,
					fileFormatOption.isWithMetadata() ? diagram.getMetadata() : null, null, 0, 10,
					diagram.getAnimation(), diagram.getSkinParam().handwritten());

			imageBuilder.setUDrawable(new Drawing(null));
			final Dimension2D dim = imageBuilder.getFinalDimension();

			imageBuilder.setUDrawable(new Drawing(new YMirror(dim.getHeight())));

			return imageBuilder.writeImageTOBEMOVED(fileFormatOption, os);
		} catch (Throwable e) {
			UmlDiagram.exportDiagramError(os, e, fileFormatOption, diagram.getMetadata(), diagram.getFlashData(),
					getFailureText3(e));
			return new ImageDataSimple();
		} finally {
			Z.close();
		}
	}

	private void exportGroups(Agraph_s graph, IGroup parent) {
		for (IGroup g : diagram.getChildrenGroups(parent)) {
			if (g.isRemoved()) {
				continue;
			}
			if (diagram.isEmpty(g) && g.getGroupType() == GroupType.PACKAGE) {
				final ILeaf folder = emptyGroups.get(g);
				exportEntity(graph, folder);
			} else {
				exportGroup(graph, g);
			}
		}

	}

	private void exportGroup(Agraph_s graph, IGroup group) {
		final Cluster cluster = getBibliotekon().getCluster(group);
		JUtils.LOG2("cluster = " + cluster.getClusterId());
		final Agraph_s cluster1 = agsubg(graph, new CString(cluster.getClusterId()), true);
		if (cluster.isLabel()) {
			final double width = cluster.getTitleAndAttributeWidth();
			final double height = cluster.getTitleAndAttributeHeight() - 5;
			agsafeset(cluster1, new CString("label"),
					Macro.createHackInitDimensionFromLabel((int) width, (int) height), new CString(""));
		}
		this.exportEntities(cluster1, group.getLeafsDirect());
		this.clusters.put(group, cluster1);
		this.exportGroups(cluster1, group);
	}

	private TextBlock getLabel(Link link) {
		final double marginLabel = 1; // startUid.equals(endUid) ? 6 : 1;
		ISkinParam skinParam = diagram.getSkinParam();
		final FontConfiguration labelFont = new FontConfiguration(skinParam, FontParam.GENERIC_ARROW, null);
		final TextBlock label = link.getLabel().create(labelFont, skinParam.getDefaultTextAlignment(), skinParam);
		if (TextBlockUtils.isEmpty(label)) {
			return label;
		}
		return TextBlockUtils.withMargin(label, marginLabel, marginLabel);

	}

	private Agnode_s getAgnodeFromLeaf(IEntity entity) {
		final Agnode_s n = nodes.get(entity);
		if (n != null) {
			return n;
		}
		final String id = getBibliotekon().getShapeUid((ILeaf) entity);
		for (Map.Entry<ILeaf, Agnode_s> ent : nodes.entrySet()) {
			if (id.equals(getBibliotekon().getShapeUid(ent.getKey()))) {
				return ent.getValue();
			}
		}
		return null;

	}

	private Agedge_s createEdge(final Agraph_s g, Link link) {
		final Agnode_s n = getAgnodeFromLeaf(link.getEntity1());
		final Agnode_s m = getAgnodeFromLeaf(link.getEntity2());
		if (n == null) {
			return null;
		}
		if (m == null) {
			return null;
		}
		final Agedge_s e = agedge(g, n, m, null, true);
		agsafeset(e, new CString("arrowtail"), new CString("none"), new CString(""));
		agsafeset(e, new CString("arrowhead"), new CString("none"), new CString(""));

		int length = link.getLength();
		// System.err.println("length=" + length);
		// if (/* pragma.horizontalLineBetweenDifferentPackageAllowed() || */link.isInvis() || length != 1) {
		agsafeset(e, new CString("minlen"), new CString("" + (length - 1)), new CString(""));
		// }
		// System.err.print("EDGE " + link.getEntity1().getUid() + "->" + link.getEntity2().getUid() + " minlen="
		// + (length - 1) + " ");

		final TextBlock label = getLabel(link);
		if (TextBlockUtils.isEmpty(label) == false) {
			final Dimension2D dimLabel = label.calculateDimension(stringBounder);
			// System.err.println("dimLabel = " + dimLabel);
			final CString hackDim = Macro.createHackInitDimensionFromLabel((int) dimLabel.getWidth(),
					(int) dimLabel.getHeight());
			agsafeset(e, new CString("label"), hackDim, new CString(""));
			// System.err.print("label=" + hackDim.getContent());
		}
		// System.err.println();
		return e;
	}

	static private List<String> getFailureText3(Throwable exception) {
		exception.printStackTrace();
		final List<String> strings = new ArrayList<String>();
		strings.add("An error has occured : " + exception);
		final String quote = QuoteUtils.getSomeQuote();
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
			throw new IllegalStateException();
		}
		final IEntityImage image = printEntityInternal(ent);
		final Dimension2D dim = image.calculateDimension(stringBounder);
		final Shape shape = new Shape(image, image.getShapeType(), dim.getWidth(), dim.getHeight(),
				dotStringFactory.getColorSequence(), ent.isTop(), image.getShield(), ent.getEntityPosition());
		// dotStringFactory.addShape(shape);
		getBibliotekon().putShape(ent, shape);
	}

	private Bibliotekon getBibliotekon() {
		return dotStringFactory.getBibliotekon();
	}

	private IEntityImage printEntityInternal(ILeaf ent) {
		if (ent.isRemoved()) {
			throw new IllegalStateException();
		}
		if (ent.getSvekImage() == null) {
			ISkinParam skinParam = diagram.getSkinParam();
			if (skinParam.sameClassWidth()) {
				throw new UnsupportedOperationException();
				// final double width = getMaxWidth();
				// skinParam = new SkinParamSameClassWidth(dotData.getSkinParam(), width);
			}

			return CucaDiagramFileMakerSvek2.createEntityImageBlock(ent, skinParam,
					diagram.isHideEmptyDescriptionForState(), diagram, getBibliotekon(), null,
					diagram.getUmlDiagramType());
		}
		return ent.getSvekImage();
	}

}
