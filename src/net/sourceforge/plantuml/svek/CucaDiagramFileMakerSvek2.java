/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.Pragma;
import net.sourceforge.plantuml.SkinParamForecolored;
import net.sourceforge.plantuml.SkinParamSameClassWidth;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.MethodsOrFieldsArea;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.UnparsableGraphvizException;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.cucadiagram.entity.EntityFactory;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.skin.StickMan;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.image.EntityImageActivity;
import net.sourceforge.plantuml.svek.image.EntityImageActor2;
import net.sourceforge.plantuml.svek.image.EntityImageArcCircle;
import net.sourceforge.plantuml.svek.image.EntityImageAssociation;
import net.sourceforge.plantuml.svek.image.EntityImageAssociationPoint;
import net.sourceforge.plantuml.svek.image.EntityImageBranch;
import net.sourceforge.plantuml.svek.image.EntityImageCircleEnd;
import net.sourceforge.plantuml.svek.image.EntityImageCircleInterface;
import net.sourceforge.plantuml.svek.image.EntityImageCircleStart;
import net.sourceforge.plantuml.svek.image.EntityImageClass;
import net.sourceforge.plantuml.svek.image.EntityImageComponent;
import net.sourceforge.plantuml.svek.image.EntityImageComponentForDescriptionDiagram;
import net.sourceforge.plantuml.svek.image.EntityImageEmptyPackage2;
import net.sourceforge.plantuml.svek.image.EntityImageGroup;
import net.sourceforge.plantuml.svek.image.EntityImageLollipopInterface;
import net.sourceforge.plantuml.svek.image.EntityImageNote;
import net.sourceforge.plantuml.svek.image.EntityImageObject;
import net.sourceforge.plantuml.svek.image.EntityImagePseudoState;
import net.sourceforge.plantuml.svek.image.EntityImageState;
import net.sourceforge.plantuml.svek.image.EntityImageStateBorder;
import net.sourceforge.plantuml.svek.image.EntityImageStateEmptyDescription;
import net.sourceforge.plantuml.svek.image.EntityImageSynchroBar;
import net.sourceforge.plantuml.svek.image.EntityImageUseCase;

public final class CucaDiagramFileMakerSvek2 {

	private final ColorSequence colorSequence = new ColorSequence();

	private final DotData dotData;
	private final EntityFactory entityFactory;
	private final boolean hasVerticalLine;
	private final UmlSource source;
	private final Pragma pragma;

	static private final StringBounder stringBounder;

	static {
		final EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		stringBounder = StringBounderUtils.asStringBounder(builder.getGraphics2D());
	}

	public CucaDiagramFileMakerSvek2(DotData dotData, EntityFactory entityFactory, boolean hasVerticalLine,
			UmlSource source, Pragma pragma) {
		this.dotData = dotData;
		this.entityFactory = entityFactory;
		this.hasVerticalLine = hasVerticalLine;
		this.source = source;
		this.pragma = pragma;
	}

	private DotStringFactory dotStringFactory;

	public Bibliotekon getBibliotekon() {
		return dotStringFactory.getBibliotekon();
	}

	public IEntityImage createFile(String... dotStrings) throws IOException, InterruptedException {

		dotStringFactory = new DotStringFactory(colorSequence, stringBounder, dotData);

		printGroups(dotData.getRootGroup());
		printEntities(getUnpackagedEntities());

		for (Link link : dotData.getLinks()) {
			if (link.isRemoved()) {
				continue;
			}
			try {
				final String shapeUid1 = getBibliotekon().getShapeUid((ILeaf) link.getEntity1());
				final String shapeUid2 = getBibliotekon().getShapeUid((ILeaf) link.getEntity2());

				String ltail = null;
				if (shapeUid1.startsWith(Cluster.CENTER_ID)) {
					// final Group g1 = ((IEntityMutable)
					// link.getEntity1()).getContainerOrEquivalent();
					ltail = getCluster2((IEntity) link.getEntity1()).getClusterId();
				}
				String lhead = null;
				if (shapeUid2.startsWith(Cluster.CENTER_ID)) {
					// final Group g2 = ((IEntityMutable)
					// link.getEntity2()).getContainerOrEquivalent();
					lhead = getCluster2((IEntity) link.getEntity2()).getClusterId();
				}
				final ISkinParam skinParam = dotData.getSkinParam();
				final FontConfiguration labelFont = new FontConfiguration(skinParam.getFont(FontParam.GENERIC_ARROW,
						null), skinParam.getFontHtmlColor(FontParam.GENERIC_ARROW, null));

				final Line line = new Line(shapeUid1, shapeUid2, link, colorSequence, ltail, lhead, skinParam,
						stringBounder, labelFont, getBibliotekon(), dotStringFactory.getGraphvizVersion());

				getBibliotekon().addLine(line);

				if (link.getEntity1().isGroup() == false && link.getEntity1().getEntityType() == LeafType.NOTE
						&& onlyOneLink(link.getEntity1())) {
					final Shape shape = getBibliotekon().getShape(link.getEntity1());
					((EntityImageNote) shape.getImage()).setOpaleLine(line, shape);
					line.setOpale(true);
				} else if (link.getEntity2().isGroup() == false && link.getEntity2().getEntityType() == LeafType.NOTE
						&& onlyOneLink(link.getEntity2())) {
					final Shape shape = getBibliotekon().getShape(link.getEntity2());
					((EntityImageNote) shape.getImage()).setOpaleLine(line, shape);
					line.setOpale(true);
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}

		if (dotStringFactory.illegalDotExe()) {
			return error(dotStringFactory.getDotExe());
		}

		final boolean trace = OptionFlags.getInstance().isKeepTmpFiles() || OptionFlags.TRACE_DOT || isSvekTrace();

		final String svg = dotStringFactory.getSvg(trace, dotStrings);
		if (svg.length() == 0) {
			return new GraphvizCrash();
		}
		final String graphvizVersion = extractGraphvizVersion(svg);
		try {
			final ClusterPosition position = dotStringFactory.solve(svg).delta(10, 10);
			final double minY = position.getMinY();
			final double minX = position.getMinX();
			if (minX > 0 || minY > 0) {
				throw new IllegalStateException();
			}
			final HtmlColor border;
			if (dotData.getUmlDiagramType() == UmlDiagramType.STATE) {
				border = getColor(ColorParam.stateBorder, null, dotData.getSkinParam());
			} else {
				border = getColor(ColorParam.packageBorder, null, dotData.getSkinParam());
			}
			final SvekResult result = new SvekResult(position, dotData, dotStringFactory, border, hasVerticalLine);
			result.moveSvek(6 - minX, -minY);
			return result;
		} catch (Exception e) {
			Log.error("Exception " + e);
			throw new UnparsableGraphvizException(e, graphvizVersion, svg, source.getPlainString());
		}

	}

	private boolean isSvekTrace() {
		final String value = pragma.getValue("svek_trace");
		return "true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value);
	}

	private String extractGraphvizVersion(String svg) {
		final Pattern pGraph = Pattern.compile("(?mi)!-- generated by graphviz(.*)");
		final Matcher mGraph = pGraph.matcher(svg);
		if (mGraph.find()) {
			return mGraph.group(1).trim();
		}
		return null;
	}

	private boolean onlyOneLink(IEntity ent) {
		int nb = 0;
		for (Link link : dotData.getLinks()) {
			if (link.isInvis()) {
				continue;
			}
			if (link.contains(ent)) {
				nb++;
			}
			if (nb > 1) {
				return false;
			}
		}
		return nb == 1;
	}

	private static HtmlColor getColor(ColorParam colorParam, Stereotype stereo, ISkinParam skinParam) {
		final String s = stereo == null ? null : stereo.getLabel();
		return new Rose().getHtmlColor(skinParam, colorParam, s);
	}

	private Cluster getCluster(IEntity g) {
		for (Cluster cl : getBibliotekon().allCluster()) {
			if (cl.getGroup() == g) {
				return cl;
			}
		}
		throw new IllegalArgumentException(g.toString());
	}

	private Cluster getCluster2(IEntity entityMutable) {
		for (Cluster cl : getBibliotekon().allCluster()) {
			if (entityMutable == cl.getGroup()) {
				return cl;
			}
		}
		throw new IllegalArgumentException();
	}

	private IEntityImage error(File dotExe) {

		final List<String> msg = new ArrayList<String>();
		msg.add("Dot Executable: " + dotExe);
		if (dotExe != null) {
			if (dotExe.exists() == false) {
				msg.add("File does not exist");
			} else if (dotExe.isDirectory()) {
				msg.add("It should be an executable, not a directory");
			} else if (dotExe.isFile() == false) {
				msg.add("Not a valid file");
			} else if (dotExe.canRead() == false) {
				msg.add("File cannot be read");
			}
		}
		msg.add("Cannot find Graphviz. You should try");
		msg.add(" ");
		msg.add("@startuml");
		msg.add("testdot");
		msg.add("@enduml");
		msg.add(" ");
		msg.add(" or ");
		msg.add(" ");
		msg.add("java -jar plantuml.jar -testdot");
		msg.add(" ");
		return new GraphicStrings(msg);
	}

	private void printEntities(Collection<ILeaf> entities2) {
		for (ILeaf ent : entities2) {
			if (ent.isRemoved()) {
				continue;
			}
			printEntity(ent);
		}
	}

	private void printEntity(ILeaf ent) {
		if (ent.isRemoved()) {
			throw new IllegalStateException();
		}
		final IEntityImage image = printEntityInternal(ent);
		final Dimension2D dim = image.calculateDimension(stringBounder);
		final Shape shape = new Shape(image, image.getShapeType(), dim.getWidth(), dim.getHeight(), colorSequence,
				ent.isTop(), image.getShield(), ent.getEntityPosition());
		dotStringFactory.addShape(shape);
		getBibliotekon().putShape(ent, shape);
	}

	private IEntityImage printEntityInternal(ILeaf ent) {
		if (ent.isRemoved()) {
			throw new IllegalStateException();
		}
		if (ent.getSvekImage() == null) {
			ISkinParam skinParam = dotData.getSkinParam();
			if (dotData.getSkinParam().sameClassWidth()) {
				final double width = getMaxWidth();
				skinParam = new SkinParamSameClassWidth(dotData.getSkinParam(), width);
			}

			return createEntityImageBlock(ent, skinParam, dotData.isHideEmptyDescriptionForState(), dotData,
					getBibliotekon());
		}
		return ent.getSvekImage();
	}

	private double getMaxWidth() {
		double result = 0;
		for (ILeaf ent : dotData.getLeafs()) {
			if (ent.getEntityType().isLikeClass() == false) {
				continue;
			}
			final IEntityImage im = new EntityImageClass(ent, dotData.getSkinParam(), dotData);
			final double w = im.calculateDimension(stringBounder).getWidth();
			if (w > result) {
				result = w;
			}
		}
		return result;
	}

	public static IEntityImage createEntityImageBlock(ILeaf leaf, ISkinParam skinParam,
			boolean isHideEmptyDescriptionForState, PortionShower portionShower, Bibliotekon bibliotekon) {
		if (leaf.isRemoved()) {
			throw new IllegalStateException();
		}
		if (leaf.getEntityType().isLikeClass()) {
			return new EntityImageClass((ILeaf) leaf, skinParam, portionShower);
		}
		if (leaf.getEntityType() == LeafType.NOTE) {
			return new EntityImageNote(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.ACTIVITY) {
			return new EntityImageActivity(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.STATE) {
			if (leaf.getEntityPosition() != EntityPosition.NORMAL) {
				final Cluster stateParent = bibliotekon.getCluster(leaf.getParentContainer());
				return new EntityImageStateBorder(leaf, skinParam, stateParent, bibliotekon);
			}
			if (isHideEmptyDescriptionForState && leaf.getFieldsToDisplay().size() == 0) {
				return new EntityImageStateEmptyDescription(leaf, skinParam);
			}
			return new EntityImageState(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.CIRCLE_START) {
			return new EntityImageCircleStart(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.CIRCLE_END) {
			return new EntityImageCircleEnd(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.USECASE) {
			return new EntityImageUseCase(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.BRANCH || leaf.getEntityType() == LeafType.STATE_CHOICE) {
			return new EntityImageBranch(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.LOLLIPOP) {
			return new EntityImageLollipopInterface(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.ACTOR) {
			final TextBlock stickman = new StickMan(getColor(ColorParam.usecaseActorBackground, leaf.getStereotype(),
					skinParam), getColor(ColorParam.usecaseActorBorder, leaf.getStereotype(), skinParam),
					skinParam.shadowing() ? 4.0 : 0.0);
			return new EntityImageActor2(leaf, skinParam, FontParam.USECASE_ACTOR_STEREOTYPE, FontParam.USECASE_ACTOR,
					stickman);
		}
		if (leaf.getEntityType() == LeafType.BOUNDARY) {
			final TextBlock stickman = new Boundary(getColor(ColorParam.usecaseActorBackground, leaf.getStereotype(),
					skinParam), getColor(ColorParam.usecaseActorBorder, leaf.getStereotype(), skinParam),
					skinParam.shadowing() ? 4.0 : 0.0, Rose.getStroke(skinParam, LineParam.sequenceActorBorder, 2)
							.getThickness());
			return new EntityImageActor2(leaf, skinParam, FontParam.USECASE_ACTOR_STEREOTYPE, FontParam.USECASE_ACTOR,
					stickman);
		}
		if (leaf.getEntityType() == LeafType.CONTROL) {
			final TextBlock stickman = new Control(getColor(ColorParam.usecaseActorBackground, leaf.getStereotype(),
					skinParam), getColor(ColorParam.usecaseActorBorder, leaf.getStereotype(), skinParam),
					skinParam.shadowing() ? 4.0 : 0.0, Rose.getStroke(skinParam, LineParam.sequenceActorBorder, 2)
							.getThickness());
			return new EntityImageActor2(leaf, skinParam, FontParam.USECASE_ACTOR_STEREOTYPE, FontParam.USECASE_ACTOR,
					stickman);
		}
		if (leaf.getEntityType() == LeafType.ENTITY_DOMAIN) {
			final TextBlock stickman = new EntityDomain(getColor(ColorParam.usecaseActorBackground,
					leaf.getStereotype(), skinParam), getColor(ColorParam.usecaseActorBorder, leaf.getStereotype(),
					skinParam), skinParam.shadowing() ? 4.0 : 0.0, Rose.getStroke(skinParam,
					LineParam.sequenceActorBorder, 2).getThickness());
			return new EntityImageActor2(leaf, skinParam, FontParam.USECASE_ACTOR_STEREOTYPE, FontParam.USECASE_ACTOR,
					stickman);
		}
		if (leaf.getEntityType() == LeafType.COMPONENT) {
			return new EntityImageComponent(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.COMPONENT2) {
			return new EntityImageComponentForDescriptionDiagram(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.OBJECT) {
			return new EntityImageObject(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.SYNCHRO_BAR || leaf.getEntityType() == LeafType.STATE_FORK_JOIN) {
			return new EntityImageSynchroBar(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.CIRCLE_INTERFACE) {
			return new EntityImageCircleInterface(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.ARC_CIRCLE) {
			return new EntityImageArcCircle(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.POINT_FOR_ASSOCIATION) {
			return new EntityImageAssociationPoint(leaf, skinParam);
		}
		if (leaf.isGroup()) {
			return new EntityImageGroup(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.EMPTY_PACKAGE) {
			if (leaf.getUSymbol() != null) {
				return new EntityImageComponentForDescriptionDiagram(leaf, new SkinParamForecolored(skinParam,
						HtmlColorUtils.BLACK));
			}
			return new EntityImageEmptyPackage2(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.ASSOCIATION) {
			return new EntityImageAssociation(leaf, skinParam);
		}
		if (leaf.getEntityType() == LeafType.PSEUDO_STATE) {
			return new EntityImagePseudoState(leaf, skinParam);
		}
		throw new UnsupportedOperationException(leaf.getEntityType().toString());
	}

	private Collection<ILeaf> getUnpackagedEntities() {
		final List<ILeaf> result = new ArrayList<ILeaf>();
		for (ILeaf ent : dotData.getLeafs()) {
			if (dotData.getTopParent() == ent.getParentContainer()) {
				result.add(ent);
			}
		}
		return result;
	}

	private void printGroups(IGroup parent) throws IOException {
		for (IGroup g : dotData.getGroupHierarchy().getChildrenGroups(parent)) {
			if (g.isRemoved()) {
				continue;
			}
			if (dotData.isEmpty(g) && g.getGroupType() == GroupType.PACKAGE) {
				final ILeaf folder = entityFactory.createLeaf(g.getCode(), g.getDisplay(), LeafType.EMPTY_PACKAGE,
						g.getParentContainer(), null);
				folder.setUSymbol(g.getUSymbol());
				if (g.getSpecificBackColor() == null) {
					folder.setSpecificBackcolor(dotData.getSkinParam().getBackgroundColor());
				} else {
					folder.setSpecificBackcolor(g.getSpecificBackColor());
				}
				printEntity(folder);
			} else {
				printGroup(g);
			}
		}
	}

	private void printGroup(IGroup g) throws IOException {
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
			final List<Member> members = ((IEntity) g).getFieldsToDisplay();
			final TextBlockWidth attribute;
			if (members.size() == 0) {
				attribute = new TextBlockEmpty();
			} else {
				attribute = new MethodsOrFieldsArea(members, FontParam.STATE_ATTRIBUTE, dotData.getSkinParam());
			}
			final Dimension2D dimAttribute = attribute.calculateDimension(stringBounder);
			final double attributeHeight = dimAttribute.getHeight();
			final double attributeWidth = dimAttribute.getWidth();
			final double marginForFields = attributeHeight > 0 ? IEntityImage.MARGIN : 0;

			titleAndAttributeWidth = (int) Math.max(dimLabel.getWidth(), attributeWidth);
			titleAndAttributeHeight = (int) (dimLabel.getHeight() + attributeHeight + marginForFields);
		}

		dotStringFactory.openCluster(g, titleAndAttributeWidth, titleAndAttributeHeight, title, stereo);
		this.printEntities(g.getLeafsDirect());

		printGroups(g);

		dotStringFactory.closeCluster();
	}

	private TextBlock getTitleBlock(IGroup g) {
		final Display label = g.getDisplay();
		final String stereo = g.getStereotype() == null ? null : g.getStereotype().getLabel();

		if (label == null) {
			return TextBlockUtils.empty(0, 0);
		}

		final FontParam fontParam = g.getGroupType() == GroupType.STATE ? FontParam.STATE : FontParam.PACKAGE;
		return TextBlockUtils.create(label, new FontConfiguration(dotData.getSkinParam().getFont(fontParam, stereo),
				dotData.getSkinParam().getFontHtmlColor(fontParam, stereo)), HorizontalAlignment.CENTER, dotData
				.getSkinParam());
	}

	private TextBlock getStereoBlock(IGroup g) {
		if (g.getStereotype() == null) {
			return TextBlockUtils.empty(0, 0);
		}
		final List<String> stereos = g.getStereotype().getLabels();
		if (stereos == null) {
			return TextBlockUtils.empty(0, 0);
		}
		final String stereo = g.getStereotype().getLabel();

		final FontParam fontParam = FontParam.COMPONENT_STEREOTYPE;
		return TextBlockUtils.create(new Display(stereos),
				new FontConfiguration(dotData.getSkinParam().getFont(fontParam, stereo), dotData.getSkinParam()
						.getFontHtmlColor(fontParam, stereo)), HorizontalAlignment.CENTER, dotData.getSkinParam());
	}

}
