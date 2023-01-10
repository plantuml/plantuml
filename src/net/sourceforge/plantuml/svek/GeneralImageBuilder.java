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
 * Contribution :  Serge Wenger
 * 
 *
 */
package net.sourceforge.plantuml.svek;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.BaseFile;
import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.Pragma;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.awt.geom.XRectangle2D;
import net.sourceforge.plantuml.baraye.EntityFactory;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.baraye.IEntity;
import net.sourceforge.plantuml.baraye.IGroup;
import net.sourceforge.plantuml.baraye.ILeaf;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.GroupRoot;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.UnparsableGraphvizException;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.cucadiagram.dot.ExeState;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizVersion;
import net.sourceforge.plantuml.cucadiagram.dot.Neighborhood;
import net.sourceforge.plantuml.descdiagram.EntityImageDesignedDomain;
import net.sourceforge.plantuml.descdiagram.EntityImageDomain;
import net.sourceforge.plantuml.descdiagram.EntityImageMachine;
import net.sourceforge.plantuml.descdiagram.EntityImageRequirement;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.USymbolHexagon;
import net.sourceforge.plantuml.graphic.USymbolInterface;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SecurityProfile;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.image.EntityImageActivity;
import net.sourceforge.plantuml.svek.image.EntityImageArcCircle;
import net.sourceforge.plantuml.svek.image.EntityImageAssociation;
import net.sourceforge.plantuml.svek.image.EntityImageAssociationPoint;
import net.sourceforge.plantuml.svek.image.EntityImageBranch;
import net.sourceforge.plantuml.svek.image.EntityImageCircleEnd;
import net.sourceforge.plantuml.svek.image.EntityImageCircleStart;
import net.sourceforge.plantuml.svek.image.EntityImageClass;
import net.sourceforge.plantuml.svek.image.EntityImageDeepHistory;
import net.sourceforge.plantuml.svek.image.EntityImageDescription;
import net.sourceforge.plantuml.svek.image.EntityImageEmptyPackage;
import net.sourceforge.plantuml.svek.image.EntityImageGroup;
import net.sourceforge.plantuml.svek.image.EntityImageJson;
import net.sourceforge.plantuml.svek.image.EntityImageLollipopInterface;
import net.sourceforge.plantuml.svek.image.EntityImageLollipopInterfaceEye1;
import net.sourceforge.plantuml.svek.image.EntityImageLollipopInterfaceEye2;
import net.sourceforge.plantuml.svek.image.EntityImageMap;
import net.sourceforge.plantuml.svek.image.EntityImageNote;
import net.sourceforge.plantuml.svek.image.EntityImageObject;
import net.sourceforge.plantuml.svek.image.EntityImagePort;
import net.sourceforge.plantuml.svek.image.EntityImagePseudoState;
import net.sourceforge.plantuml.svek.image.EntityImageState;
import net.sourceforge.plantuml.svek.image.EntityImageState2;
import net.sourceforge.plantuml.svek.image.EntityImageStateBorder;
import net.sourceforge.plantuml.svek.image.EntityImageStateEmptyDescription;
import net.sourceforge.plantuml.svek.image.EntityImageSynchroBar;
import net.sourceforge.plantuml.svek.image.EntityImageTips;
import net.sourceforge.plantuml.svek.image.EntityImageUseCase;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.utils.Log;

public final class GeneralImageBuilder {

	public static IEntityImage createEntityImageBlock(ILeaf leaf, ISkinParam skinParam,
			boolean isHideEmptyDescriptionForState, PortionShower portionShower, Bibliotekon bibliotekon,
			GraphvizVersion graphvizVersion, UmlDiagramType umlDiagramType, Collection<Link> links) {
		final IEntityImage result = createEntityImageBlockInternal(leaf, skinParam, isHideEmptyDescriptionForState,
				portionShower, bibliotekon, graphvizVersion, umlDiagramType, links);
		// System.err.println("leaf " + leaf + " " + result.getClass());
		return result;
	}

	private static IEntityImage createEntityImageBlockInternal(ILeaf leaf, ISkinParam skinParam,
			boolean isHideEmptyDescriptionForState, PortionShower portionShower, Bibliotekon bibliotekon,
			GraphvizVersion graphvizVersion, UmlDiagramType umlDiagramType, Collection<Link> links) {
		if (leaf.isRemoved())
			throw new IllegalStateException();

		if (leaf.getLeafType().isLikeClass()) {
			final EntityImageClass entityImageClass = new EntityImageClass((ILeaf) leaf, skinParam, portionShower);
			final Neighborhood neighborhood = leaf.getNeighborhood();
			if (neighborhood != null)
				return new EntityImageProtected(entityImageClass, 20, neighborhood, bibliotekon);

			return entityImageClass;
		}
		if (leaf.getLeafType() == LeafType.NOTE)
			return new EntityImageNote(leaf, skinParam, umlDiagramType);

		if (leaf.getLeafType() == LeafType.ACTIVITY)
			return new EntityImageActivity(leaf, skinParam, bibliotekon);

		if (/* (leaf.getLeafType() == LeafType.PORT) || */leaf.getLeafType() == LeafType.PORTIN
				|| leaf.getLeafType() == LeafType.PORTOUT) {
			final Cluster parent = bibliotekon.getCluster(leaf.getParentContainer());
			return new EntityImagePort(leaf, skinParam, parent, bibliotekon, umlDiagramType.getStyleName());
		}

		if (leaf.getLeafType() == LeafType.STATE) {
			if (leaf.getEntityPosition() != EntityPosition.NORMAL) {
				final Cluster stateParent = bibliotekon.getCluster(leaf.getParentContainer());
				return new EntityImageStateBorder(leaf, skinParam, stateParent, bibliotekon,
						umlDiagramType.getStyleName());
			}
			if (isHideEmptyDescriptionForState && leaf.getBodier().getRawBody().size() == 0)
				return new EntityImageStateEmptyDescription(leaf, skinParam);

			if (leaf.getStereotype() != null
					&& "<<sdlreceive>>".equals(leaf.getStereotype().getLabel(Guillemet.DOUBLE_COMPARATOR)))
				return new EntityImageState2(leaf, skinParam, umlDiagramType.getStyleName());

			return new EntityImageState(leaf, skinParam);

		}
		if (leaf.getLeafType() == LeafType.CIRCLE_START)
			return new EntityImageCircleStart(leaf, skinParam);

		if (leaf.getLeafType() == LeafType.CIRCLE_END)
			return new EntityImageCircleEnd(leaf, skinParam);

		if (leaf.getLeafType() == LeafType.BRANCH || leaf.getLeafType() == LeafType.STATE_CHOICE)
			return new EntityImageBranch(leaf, skinParam);

		if (leaf.getLeafType() == LeafType.LOLLIPOP_FULL || leaf.getLeafType() == LeafType.LOLLIPOP_HALF)
			return new EntityImageLollipopInterface(leaf, skinParam, umlDiagramType.getStyleName());

		if (leaf.getLeafType() == LeafType.CIRCLE)
			return new EntityImageDescription(leaf, skinParam, portionShower, links, umlDiagramType.getStyleName(),
					bibliotekon);

		if (leaf.getLeafType() == LeafType.DESCRIPTION) {
			if (OptionFlags.USE_INTERFACE_EYE1 && leaf.getUSymbol() instanceof USymbolInterface) {
				return new EntityImageLollipopInterfaceEye1(leaf, skinParam, bibliotekon);
			} else if (OptionFlags.USE_INTERFACE_EYE2 && leaf.getUSymbol() instanceof USymbolInterface) {
				return new EntityImageLollipopInterfaceEye2(leaf, skinParam, portionShower);
			} else {
				return new EntityImageDescription(leaf, skinParam, portionShower, links, umlDiagramType.getStyleName(),
						bibliotekon);
			}
		}
		if (leaf.getLeafType() == LeafType.USECASE)
			return new EntityImageUseCase(leaf, skinParam, portionShower);

		if (leaf.getLeafType() == LeafType.USECASE_BUSINESS)
			return new EntityImageUseCase(leaf, skinParam, portionShower);

		// if (leaf.getEntityType() == LeafType.CIRCLE_INTERFACE) {
		// return new EntityImageCircleInterface(leaf, skinParam);
		// }
		if (leaf.getLeafType() == LeafType.OBJECT)
			return new EntityImageObject(leaf, skinParam, portionShower);

		if (leaf.getLeafType() == LeafType.MAP)
			return new EntityImageMap(leaf, skinParam, portionShower);

		if (leaf.getLeafType() == LeafType.JSON)
			return new EntityImageJson(leaf, skinParam, portionShower);

		if (leaf.getLeafType() == LeafType.SYNCHRO_BAR || leaf.getLeafType() == LeafType.STATE_FORK_JOIN)
			return new EntityImageSynchroBar(leaf, skinParam, umlDiagramType.getStyleName());

		if (leaf.getLeafType() == LeafType.ARC_CIRCLE)
			return new EntityImageArcCircle(leaf, skinParam);

		if (leaf.getLeafType() == LeafType.POINT_FOR_ASSOCIATION)
			return new EntityImageAssociationPoint(leaf, skinParam);

		if (leaf.isGroup())
			return new EntityImageGroup(leaf, skinParam);

		if (leaf.getLeafType() == LeafType.EMPTY_PACKAGE) {
			if (leaf.getUSymbol() != null)
				return new EntityImageDescription(leaf, skinParam, portionShower, links, umlDiagramType.getStyleName(),
						bibliotekon);

			return new EntityImageEmptyPackage(leaf, skinParam, portionShower, umlDiagramType.getStyleName());
		}
		if (leaf.getLeafType() == LeafType.ASSOCIATION)
			return new EntityImageAssociation(leaf, skinParam, umlDiagramType.getStyleName());

		if (leaf.getLeafType() == LeafType.PSEUDO_STATE)
			return new EntityImagePseudoState(leaf, skinParam, umlDiagramType.getStyleName());

		if (leaf.getLeafType() == LeafType.DEEP_HISTORY)
			return new EntityImageDeepHistory(leaf, skinParam, umlDiagramType.getStyleName());

		if (leaf.getLeafType() == LeafType.TIPS)
			return new EntityImageTips(leaf, skinParam, bibliotekon, umlDiagramType);

		// TODO Clean
		if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isMachineOrSpecification())
			return new EntityImageMachine(leaf, skinParam);
		else if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isDesignedOrSolved())
			return new EntityImageDesignedDomain(leaf, skinParam);
		else if (leaf.getLeafType() == LeafType.REQUIREMENT)
			return new EntityImageRequirement(leaf, skinParam);
		else if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isLexicalOrGiven())
			return new EntityImageDomain(leaf, skinParam, 'X');
		else if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isCausal())
			return new EntityImageDomain(leaf, skinParam, 'C');
		else if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isBiddableOrUncertain())
			return new EntityImageDomain(leaf, skinParam, 'B');
		else if (leaf.getLeafType() == LeafType.DOMAIN)
			return new EntityImageDomain(leaf, skinParam, 'P');
		else
			throw new UnsupportedOperationException(leaf.getLeafType().toString());
	}

	public static UStroke getForcedStroke(Stereotype stereotype, ISkinParam skinParam) {
		UStroke stroke = skinParam.getThickness(LineParam.packageBorder, stereotype);
		if (stroke == null)
			stroke = new UStroke(1.5);

		return stroke;
	}

	private final DotData dotData;
	private final EntityFactory entityFactory;
	private final UmlSource source;
	private final Pragma pragma;
	private final boolean strictUmlStyle;
	private Map<String, Double> maxX;

	private final StringBounder stringBounder;
	private final boolean mergeIntricated;
	private final SName styleName;

	public GeneralImageBuilder(boolean mergeIntricated, DotData dotData, EntityFactory entityFactory, UmlSource source,
			Pragma pragma, StringBounder stringBounder, SName styleName) {
		this.dotData = dotData;
		this.styleName = styleName;
		this.entityFactory = entityFactory;
		this.source = source;
		this.pragma = pragma;
		this.stringBounder = stringBounder;
		this.strictUmlStyle = dotData.getSkinParam().strictUmlStyle();
		this.mergeIntricated = mergeIntricated;
	}

	final public StyleSignature getDefaultStyleDefinitionArrow(Stereotype stereotype) {
		StyleSignature result = StyleSignatureBasic.of(SName.root, SName.element, styleName, SName.arrow);
		if (stereotype != null)
			result = result.withTOBECHANGED(stereotype);

		return result;
	}

	private boolean isOpalisable(IEntity entity) {
		if (strictUmlStyle)
			return false;

		if (entity.isGroup())
			return false;

		if (entity.getLeafType() != LeafType.NOTE)
			return false;

		final Link single = onlyOneLink(entity);
		if (single == null)
			return false;

		return single.getOther(entity).getLeafType() != LeafType.NOTE;
	}

	static class EntityImageSimpleEmpty implements IEntityImage {

		private final HColor backColor;

		EntityImageSimpleEmpty(HColor backColor) {
			this.backColor = backColor;
		}

		public boolean isHidden() {
			return false;
		}

		public HColor getBackcolor() {
			return backColor;
		}

		public XDimension2D calculateDimension(StringBounder stringBounder) {
			return new XDimension2D(10, 10);
		}

		public MinMax getMinMax(StringBounder stringBounder) {
			return MinMax.fromDim(calculateDimension(stringBounder));
		}

		public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
			return null;
		}

		public void drawU(UGraphic ug) {
		}

		public ShapeType getShapeType() {
			return ShapeType.RECTANGLE;
		}

		public Margins getShield(StringBounder stringBounder) {
			return Margins.NONE;
		}

		public double getOverscanX(StringBounder stringBounder) {
			return 0;
		}

	}

	// Duplicate SvekResult / GeneralImageBuilder
	private HColor getBackcolor() {
		final Style style = StyleSignatureBasic.of(SName.root, SName.document)
				.getMergedStyle(dotData.getSkinParam().getCurrentStyleBuilder());
		return style.value(PName.BackGroundColor).asColor(dotData.getSkinParam().getIHtmlColorSet());
	}

	public IEntityImage buildImage(BaseFile basefile, String dotStrings[]) {
		if (dotData.isDegeneratedWithFewEntities(0))
			return new EntityImageSimpleEmpty(dotData.getSkinParam().getBackgroundColor());

		if (dotData.isDegeneratedWithFewEntities(1) && dotData.getUmlDiagramType() != UmlDiagramType.STATE) {
			final ILeaf single = dotData.getLeafs().iterator().next();
			final IGroup group = single.getParentContainer();
			if (group instanceof GroupRoot && single.getUSymbol() instanceof USymbolHexagon == false) {
				final IEntityImage tmp = GeneralImageBuilder.createEntityImageBlock(single, dotData.getSkinParam(),
						dotData.isHideEmptyDescriptionForState(), dotData, null, null, dotData.getUmlDiagramType(),
						dotData.getLinks());
				return new EntityImageDegenerated(tmp, getBackcolor());
			}
		}
		dotData.removeIrrelevantSametail();
		final DotStringFactory dotStringFactory = new DotStringFactory(stringBounder, dotData);

		printGroups(dotStringFactory, dotData.getRootGroup());
		printEntities(dotStringFactory, getUnpackagedEntities());

		for (Link link : dotData.getLinks()) {
			if (link.isRemoved())
				continue;

			try {
				final ISkinParam skinParam = dotData.getSkinParam();
				final FontConfiguration labelFont = getFontForLink(link, skinParam);

				final SvekLine line = new SvekLine(link, dotStringFactory.getColorSequence(), skinParam, stringBounder,
						labelFont, dotStringFactory.getBibliotekon(), pragma, dotStringFactory.getGraphvizVersion());

				dotStringFactory.getBibliotekon().addLine(line);

				if (isOpalisable(link.getEntity1())) {
					final SvekNode node = dotStringFactory.getBibliotekon().getNode(link.getEntity1());
					final SvekNode other = dotStringFactory.getBibliotekon().getNode(link.getEntity2());
					if (other != null) {
						((EntityImageNote) node.getImage()).setOpaleLine(line, node, other);
						line.setOpale(true);
					}
				} else if (isOpalisable(link.getEntity2())) {
					final SvekNode node = dotStringFactory.getBibliotekon().getNode(link.getEntity2());
					final SvekNode other = dotStringFactory.getBibliotekon().getNode(link.getEntity1());
					if (other != null) {
						((EntityImageNote) node.getImage()).setOpaleLine(line, node, other);
						line.setOpale(true);
					}
				}
			} catch (IllegalStateException e) {
				Logme.error(e);
			}
		}

		if (dotStringFactory.illegalDotExe())
			return error(dotStringFactory.getDotExe());

		if (basefile == null && isSvekTrace()
				&& (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE
						|| SecurityUtils.getSecurityProfile() == SecurityProfile.LEGACY
						|| SecurityUtils.getSecurityProfile() == SecurityProfile.SANDBOX))
			basefile = new BaseFile(null);

		final String svg;
		try {
			svg = dotStringFactory.getSvg(basefile, dotStrings);
		} catch (IOException e) {
			return new GraphvizCrash(source.getPlainString(), GraphvizUtils.graphviz244onWindows(), e);
		}
		if (svg.length() == 0)
			return new GraphvizCrash(source.getPlainString(), GraphvizUtils.graphviz244onWindows(),
					new EmptySvgException());

		final String graphvizVersion = extractGraphvizVersion(svg);
		try {
			dotStringFactory.solve(mergeIntricated, dotData.getEntityFactory(), svg);
			final SvekResult result = new SvekResult(dotData, dotStringFactory);
			this.maxX = dotStringFactory.getBibliotekon().getMaxX();
			return result;
		} catch (Exception e) {
			Log.error("Exception " + e);
			throw new UnparsableGraphvizException(e, graphvizVersion, svg, source.getPlainString());
		}

	}

	private FontConfiguration getFontForLink(Link link, final ISkinParam skinParam) {
		final Style style = getDefaultStyleDefinitionArrow(link.getStereotype()).getMergedStyle(link.getStyleBuilder());
		return style.getFontConfiguration(skinParam.getIHtmlColorSet());
	}

	private boolean isSvekTrace() {
		final String value = pragma.getValue("svek_trace");
		return "true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value);
	}

	private String extractGraphvizVersion(String svg) {
		final Pattern pGraph = Pattern.compile("(?mi)!-- generated by graphviz(.*)");
		final Matcher mGraph = pGraph.matcher(svg);
		if (mGraph.find())
			return StringUtils.trin(mGraph.group(1));

		return null;
	}

	private Link onlyOneLink(IEntity ent) {
		Link single = null;
		for (Link link : dotData.getLinks()) {
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

	private IEntityImage error(File dotExe) {

		final List<String> msg = new ArrayList<>();
		msg.add("Dot Executable: " + dotExe);
		final ExeState exeState = ExeState.checkFile(dotExe);
		msg.add(exeState.getTextMessage());
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
		return GraphicStrings.createForError(msg, false);
	}

	private void printEntities(DotStringFactory dotStringFactory, Collection<ILeaf> entities2) {
		for (ILeaf ent : entities2) {
			if (ent.isRemoved())
				continue;

			printEntity(dotStringFactory, ent);
		}
	}

	private void printEntity(DotStringFactory dotStringFactory, ILeaf ent) {
		if (ent.isRemoved())
			throw new IllegalStateException();

		final IEntityImage image = printEntityInternal(dotStringFactory, ent);
		final SvekNode node = dotStringFactory.getBibliotekon().createNode(ent, image,
				dotStringFactory.getColorSequence(), stringBounder);
		dotStringFactory.addNode(node);
	}

	private IEntityImage printEntityInternal(DotStringFactory dotStringFactory, ILeaf ent) {
		if (ent.isRemoved())
			throw new IllegalStateException();

		if (ent.getSvekImage() == null) {
			ISkinParam skinParam = dotData.getSkinParam();
			if (skinParam.sameClassWidth()) {
				final double width = getMaxWidth();
				((SkinParam) skinParam).setParamSameClassWidth(width);
			}

			return createEntityImageBlock(ent, skinParam, dotData.isHideEmptyDescriptionForState(), dotData,
					dotStringFactory.getBibliotekon(), dotStringFactory.getGraphvizVersion(),
					dotData.getUmlDiagramType(), dotData.getLinks());
		}
		return ent.getSvekImage();
	}

	private double getMaxWidth() {
		double result = 0;
		for (ILeaf ent : dotData.getLeafs()) {
			if (ent.getLeafType().isLikeClass() == false)
				continue;

			final IEntityImage im = new EntityImageClass(ent, dotData.getSkinParam(), dotData);
			final double w = im.calculateDimension(stringBounder).getWidth();
			if (w > result)
				result = w;

		}
		return result;
	}

	private Collection<ILeaf> getUnpackagedEntities() {
		final List<ILeaf> result = new ArrayList<>();
		for (ILeaf ent : dotData.getLeafs())
			if (dotData.getTopParent() == ent.getParentContainer())
				result.add(ent);

		return result;
	}

	private void printGroups(DotStringFactory dotStringFactory, IGroup parent) {
		final Collection<IGroup> groups = dotData.getGroupHierarchy().getChildrenGroups(parent);
		for (IGroup g : groups) {
			if (g.isRemoved())
				continue;

			if (dotData.isEmpty(g)
					&& (g.getGroupType() == GroupType.PACKAGE || g.getGroupType() == GroupType.TOGETHER)) {
				final ISkinParam skinParam = dotData.getSkinParam();
				final ILeaf folder = entityFactory.createLeafForEmptyGroup(g, skinParam);
				printEntity(dotStringFactory, folder);
			} else {
				printGroup(dotStringFactory, g);
			}
		}
	}

	private void printGroup(DotStringFactory dotStringFactory, IGroup g) {
		if (g.getGroupType() == GroupType.CONCURRENT_STATE)
			return;

		if (mergeIntricated) {
			final IGroup intricated = dotData.getEntityFactory().isIntricated(g);
			if (intricated != null) {
				printGroup(dotStringFactory, intricated);
				return;
			}
		}

		final ClusterHeader clusterHeader = new ClusterHeader((EntityImp) g, dotData.getSkinParam(), dotData,
				stringBounder);
		dotStringFactory.openCluster(g, clusterHeader);
		this.printEntities(dotStringFactory, g.getLeafsDirect());

		printGroups(dotStringFactory, g);

		dotStringFactory.closeCluster();
	}

	public String getWarningOrError(int warningOrError) {
		if (maxX == null)
			return "";

		final StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Double> ent : maxX.entrySet())
			if (ent.getValue() > warningOrError) {
				sb.append(ent.getKey() + " is overpassing the width limit.");
				sb.append("\n");
			}

		return sb.length() == 0 ? "" : sb.toString();
	}
}
