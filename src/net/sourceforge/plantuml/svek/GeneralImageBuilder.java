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
 * Contribution :  Hisashi Miyashita
 * 
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.BaseFile;
import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.Pragma;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.SkinParamForecolored;
import net.sourceforge.plantuml.SkinParamSameClassWidth;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.activitydiagram3.ftile.EntityImageLegend;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositionned;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.GroupRoot;
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
import net.sourceforge.plantuml.cucadiagram.dot.ExeState;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizVersion;
import net.sourceforge.plantuml.cucadiagram.dot.Neighborhood;
import net.sourceforge.plantuml.cucadiagram.entity.EntityFactory;
import net.sourceforge.plantuml.descdiagram.EntityImageDesignedDomain;
import net.sourceforge.plantuml.descdiagram.EntityImageDomain;
import net.sourceforge.plantuml.descdiagram.EntityImageMachine;
import net.sourceforge.plantuml.descdiagram.EntityImageRequirement;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.USymbolInterface;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.image.EntityImageActivity;
import net.sourceforge.plantuml.svek.image.EntityImageArcCircle;
import net.sourceforge.plantuml.svek.image.EntityImageAssociation;
import net.sourceforge.plantuml.svek.image.EntityImageAssociationPoint;
import net.sourceforge.plantuml.svek.image.EntityImageBranch;
import net.sourceforge.plantuml.svek.image.EntityImageCircleEnd;
import net.sourceforge.plantuml.svek.image.EntityImageCircleStart;
import net.sourceforge.plantuml.svek.image.EntityImageClass;
import net.sourceforge.plantuml.svek.image.EntityImageDescription;
import net.sourceforge.plantuml.svek.image.EntityImageEmptyPackage;
import net.sourceforge.plantuml.svek.image.EntityImageGroup;
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
import net.sourceforge.plantuml.ugraphic.color.HColor;

public final class GeneralImageBuilder {

	public static IEntityImage createEntityImageBlock(ILeaf leaf, ISkinParam skinParam,
			boolean isHideEmptyDescriptionForState, PortionShower portionShower, Bibliotekon bibliotekon,
			GraphvizVersion graphvizVersion, UmlDiagramType umlDiagramType, Collection<Link> links) {
		if (leaf.isRemoved()) {
			throw new IllegalStateException();
		}
		if (leaf.getLeafType().isLikeClass()) {
			final EntityImageClass entityImageClass = new EntityImageClass(graphvizVersion, (ILeaf) leaf, skinParam,
					portionShower);
			final Neighborhood neighborhood = leaf.getNeighborhood();
			if (neighborhood != null) {
				return new EntityImageProtected(entityImageClass, 20, neighborhood, bibliotekon);
			}
			return entityImageClass;
		}
		if (leaf.getLeafType() == LeafType.NOTE) {
			return new EntityImageNote(leaf, skinParam);
		}
		if (leaf.getLeafType() == LeafType.ACTIVITY) {
			return new EntityImageActivity(leaf, skinParam, bibliotekon);
		}
		if ((leaf.getLeafType() == LeafType.PORT) || (leaf.getLeafType() == LeafType.PORTIN)
				|| (leaf.getLeafType() == LeafType.PORTOUT)) {
			final Cluster parent = bibliotekon.getCluster(leaf.getParentContainer());
			return new EntityImagePort(leaf, skinParam, parent, bibliotekon);
		}
		if (leaf.getLeafType() == LeafType.STATE) {
			if (leaf.getEntityPosition() != EntityPosition.NORMAL) {
				final Cluster stateParent = bibliotekon.getCluster(leaf.getParentContainer());
				return new EntityImageStateBorder(leaf, skinParam, stateParent, bibliotekon);
			}
			if (isHideEmptyDescriptionForState && leaf.getBodier().getFieldsToDisplay().size() == 0) {
				return new EntityImageStateEmptyDescription(leaf, skinParam);
			}
			if (leaf.getStereotype() != null
					&& "<<sdlreceive>>".equals(leaf.getStereotype().getLabel(Guillemet.DOUBLE_COMPARATOR))) {
				return new EntityImageState2(leaf, skinParam);
			}
			return new EntityImageState(leaf, skinParam);

		}
		if (leaf.getLeafType() == LeafType.CIRCLE_START) {
			ColorParam param = ColorParam.activityStart;
			if (umlDiagramType == UmlDiagramType.STATE) {
				param = ColorParam.stateStart;
			}
			return new EntityImageCircleStart(leaf, skinParam, param);
		}
		if (leaf.getLeafType() == LeafType.CIRCLE_END) {
			ColorParam param = ColorParam.activityEnd;
			if (umlDiagramType == UmlDiagramType.STATE) {
				param = ColorParam.stateEnd;
			}
			return new EntityImageCircleEnd(leaf, skinParam, param);
		}
		if (leaf.getLeafType() == LeafType.BRANCH || leaf.getLeafType() == LeafType.STATE_CHOICE) {
			return new EntityImageBranch(leaf, skinParam);
		}
		if (leaf.getLeafType() == LeafType.LOLLIPOP_FULL || leaf.getLeafType() == LeafType.LOLLIPOP_HALF) {
			return new EntityImageLollipopInterface(leaf, skinParam);
		}
		if (leaf.getLeafType() == LeafType.CIRCLE) {
			return new EntityImageDescription(leaf, skinParam, portionShower, links, umlDiagramType.getStyleName());
		}

		if (leaf.getLeafType() == LeafType.DESCRIPTION) {
			if (OptionFlags.USE_INTERFACE_EYE1 && leaf.getUSymbol() instanceof USymbolInterface) {
				return new EntityImageLollipopInterfaceEye1(leaf, skinParam, bibliotekon);
			} else if (OptionFlags.USE_INTERFACE_EYE2 && leaf.getUSymbol() instanceof USymbolInterface) {
				return new EntityImageLollipopInterfaceEye2(leaf, skinParam, portionShower);
			} else {
				return new EntityImageDescription(leaf, skinParam, portionShower, links, umlDiagramType.getStyleName());
			}
		}
		if (leaf.getLeafType() == LeafType.USECASE) {
			return new EntityImageUseCase(leaf, skinParam, portionShower);
		}
		// if (leaf.getEntityType() == LeafType.CIRCLE_INTERFACE) {
		// return new EntityImageCircleInterface(leaf, skinParam);
		// }
		if (leaf.getLeafType() == LeafType.OBJECT) {
			return new EntityImageObject(leaf, skinParam, portionShower);
		}
		if (leaf.getLeafType() == LeafType.MAP) {
			return new EntityImageMap(leaf, skinParam, portionShower);
		}
		if (leaf.getLeafType() == LeafType.SYNCHRO_BAR || leaf.getLeafType() == LeafType.STATE_FORK_JOIN) {
			return new EntityImageSynchroBar(leaf, skinParam);
		}
		if (leaf.getLeafType() == LeafType.ARC_CIRCLE) {
			return new EntityImageArcCircle(leaf, skinParam);
		}
		if (leaf.getLeafType() == LeafType.POINT_FOR_ASSOCIATION) {
			return new EntityImageAssociationPoint(leaf, skinParam);
		}
		if (leaf.isGroup()) {
			return new EntityImageGroup(leaf, skinParam);
		}
		if (leaf.getLeafType() == LeafType.EMPTY_PACKAGE) {
			if (leaf.getUSymbol() != null) {
				// final HtmlColor black = HtmlColorUtils.BLACK;
				final HColor black = SkinParamUtils.getColor(skinParam, leaf.getStereotype(),
						leaf.getUSymbol().getColorParamBorder());
				return new EntityImageDescription(leaf, new SkinParamForecolored(skinParam, black), portionShower,
						links, umlDiagramType.getStyleName());
			}
			return new EntityImageEmptyPackage(leaf, skinParam, portionShower, umlDiagramType.getStyleName());
		}
		if (leaf.getLeafType() == LeafType.ASSOCIATION) {
			return new EntityImageAssociation(leaf, skinParam);
		}
		if (leaf.getLeafType() == LeafType.PSEUDO_STATE) {
			return new EntityImagePseudoState(leaf, skinParam);
		}
		if (leaf.getLeafType() == LeafType.TIPS) {
			return new EntityImageTips(leaf, skinParam, bibliotekon);
		}
		// TODO Clean
		if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isMachineOrSpecification()) {
			return new EntityImageMachine(leaf, skinParam);
		} else if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isDesignedOrSolved()) {
			return new EntityImageDesignedDomain(leaf, skinParam);
		} else if (leaf.getLeafType() == LeafType.REQUIREMENT) {
			return new EntityImageRequirement(leaf, skinParam);
		} else if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isLexicalOrGiven()) {
			return new EntityImageDomain(leaf, skinParam, 'X');
		} else if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isCausal()) {
			return new EntityImageDomain(leaf, skinParam, 'C');
		} else if (leaf.getLeafType() == LeafType.DOMAIN && leaf.getStereotype() != null
				&& leaf.getStereotype().isBiddableOrUncertain()) {
			return new EntityImageDomain(leaf, skinParam, 'B');
		} else if (leaf.getLeafType() == LeafType.DOMAIN) {
			return new EntityImageDomain(leaf, skinParam, 'P');
		} else
			throw new UnsupportedOperationException(leaf.getLeafType().toString());
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

	final public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, styleName, SName.arrow);
	}

	private boolean isOpalisable(IEntity entity) {
		if (strictUmlStyle) {
			return false;
		}
		return entity.isGroup() == false && entity.getLeafType() == LeafType.NOTE && onlyOneLink(entity);
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

		public Dimension2D calculateDimension(StringBounder stringBounder) {
			return new Dimension2DDouble(10, 10);
		}

		public MinMax getMinMax(StringBounder stringBounder) {
			return MinMax.fromDim(calculateDimension(stringBounder));
		}

		public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
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

	public IEntityImage buildImage(BaseFile basefile, String dotStrings[]) {
		if (dotData.isDegeneratedWithFewEntities(0)) {
			return new EntityImageSimpleEmpty(dotData.getSkinParam().getBackgroundColor(false));
		}
		if (dotData.isDegeneratedWithFewEntities(1) && dotData.getUmlDiagramType() != UmlDiagramType.STATE) {
			final ILeaf single = dotData.getLeafs().iterator().next();
			final IGroup group = single.getParentContainer();
			if (group instanceof GroupRoot) {
				return new IEntityImageMoved(GeneralImageBuilder.createEntityImageBlock(single, dotData.getSkinParam(),
						dotData.isHideEmptyDescriptionForState(), dotData, null, null, dotData.getUmlDiagramType(),
						dotData.getLinks()));
			}
		}
		dotData.removeIrrelevantSametail();
		final DotStringFactory dotStringFactory = new DotStringFactory(stringBounder, dotData);

		printGroups(dotStringFactory, dotData.getRootGroup());
		printEntities(dotStringFactory, getUnpackagedEntities());

		for (Link link : dotData.getLinks()) {
			if (link.isRemoved()) {
				continue;
			}
			try {
				final ISkinParam skinParam = dotData.getSkinParam();
				final FontConfiguration labelFont;
				if (SkinParam.USE_STYLES()) {
					final Style style = getDefaultStyleDefinitionArrow()
							.getMergedStyle(skinParam.getCurrentStyleBuilder());
					labelFont = style.getFontConfiguration(skinParam.getIHtmlColorSet());
				} else {
					labelFont = new FontConfiguration(skinParam, FontParam.ARROW, null);
				}

				final Line line = new Line(link, dotStringFactory.getColorSequence(), skinParam, stringBounder,
						labelFont, dotStringFactory.getBibliotekon(), dotData.getPragma());

				dotStringFactory.getBibliotekon().addLine(line);

				if (isOpalisable(link.getEntity1())) {
					final Node node = dotStringFactory.getBibliotekon().getNode(link.getEntity1());
					final Node other = dotStringFactory.getBibliotekon().getNode(link.getEntity2());
					if (other != null) {
						((EntityImageNote) node.getImage()).setOpaleLine(line, node, other);
						line.setOpale(true);
					}
				} else if (isOpalisable(link.getEntity2())) {
					final Node node = dotStringFactory.getBibliotekon().getNode(link.getEntity2());
					final Node other = dotStringFactory.getBibliotekon().getNode(link.getEntity1());
					if (other != null) {
						((EntityImageNote) node.getImage()).setOpaleLine(line, node, other);
						line.setOpale(true);
					}
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}

		if (dotStringFactory.illegalDotExe()) {
			return error(dotStringFactory.getDotExe());
		}

		if (basefile == null && isSvekTrace()) {
			basefile = new BaseFile();
		}

		final String svg;
		try {
			svg = dotStringFactory.getSvg(basefile, dotStrings);
		} catch (IOException e) {
			return new GraphvizCrash(source.getPlainString());
		}
		if (svg.length() == 0) {
			return new GraphvizCrash(source.getPlainString());
		}
		final String graphvizVersion = extractGraphvizVersion(svg);
		try {
			final ClusterPosition position = dotStringFactory.solve(mergeIntricated, dotData.getEntityFactory(), svg)
					.delta(10, 10);
			final double minY = position.getMinY();
			final double minX = position.getMinX();
			if (minX > 0 || minY > 0) {
				throw new IllegalStateException();
			}
			final SvekResult result = new SvekResult(position, dotData, dotStringFactory);
			result.moveSvek(6 - minX, -minY);
			this.maxX = dotStringFactory.getBibliotekon().getMaxX();
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
			return StringUtils.trin(mGraph.group(1));
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

	private IEntityImage error(File dotExe) {

		final List<String> msg = new ArrayList<String>();
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
			if (ent.isRemoved()) {
				continue;
			}
			printEntity(dotStringFactory, ent);
		}
	}

	private void printEntity(DotStringFactory dotStringFactory, ILeaf ent) {
		if (ent.isRemoved()) {
			throw new IllegalStateException();
		}
		final IEntityImage image = printEntityInternal(dotStringFactory, ent);
		final Node node = dotStringFactory.getBibliotekon().createNode(ent, image, dotStringFactory.getColorSequence(),
				stringBounder);
		dotStringFactory.addNode(node);
	}

	private IEntityImage printEntityInternal(DotStringFactory dotStringFactory, ILeaf ent) {
		if (ent.isRemoved()) {
			throw new IllegalStateException();
		}
		if (ent.getSvekImage() == null) {
			ISkinParam skinParam = dotData.getSkinParam();
			if (skinParam.sameClassWidth()) {
				final double width = getMaxWidth(dotStringFactory);
				skinParam = new SkinParamSameClassWidth(skinParam, width);
			}

			return createEntityImageBlock(ent, skinParam, dotData.isHideEmptyDescriptionForState(), dotData,
					dotStringFactory.getBibliotekon(), dotStringFactory.getGraphvizVersion(),
					dotData.getUmlDiagramType(), dotData.getLinks());
		}
		return ent.getSvekImage();
	}

	private double getMaxWidth(DotStringFactory dotStringFactory) {
		double result = 0;
		for (ILeaf ent : dotData.getLeafs()) {
			if (ent.getLeafType().isLikeClass() == false) {
				continue;
			}
			final IEntityImage im = new EntityImageClass(dotStringFactory.getGraphvizVersion(), ent,
					dotData.getSkinParam(), dotData);
			final double w = im.calculateDimension(stringBounder).getWidth();
			if (w > result) {
				result = w;
			}
		}
		return result;
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

	private void printGroups(DotStringFactory dotStringFactory, IGroup parent) {
		final Collection<IGroup> groups = dotData.getGroupHierarchy().getChildrenGroups(parent);
		for (IGroup g : groups) {
			if (g.isRemoved()) {
				continue;
			}
			if (dotData.isEmpty(g) && g.getGroupType() == GroupType.PACKAGE) {
				final ISkinParam skinParam = dotData.getSkinParam();
				entityFactory.thisIsGoingToBeALeaf(g.getIdent());
				final ILeaf folder = entityFactory.createLeafForEmptyGroup(g, skinParam);
				printEntity(dotStringFactory, folder);
			} else {
				printGroup(dotStringFactory, g);
			}
		}
	}

	private void printGroup(DotStringFactory dotStringFactory, IGroup g) {
		if (g.getGroupType() == GroupType.CONCURRENT_STATE) {
			return;
		}
		if (mergeIntricated) {
			final IGroup intricated = dotData.getEntityFactory().isIntricated(g);
			if (intricated != null) {
				printGroup(dotStringFactory, intricated);
				return;
			}
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
				attribute = new MethodsOrFieldsArea(members, FontParam.STATE_ATTRIBUTE, dotData.getSkinParam(),
						g.getStereotype(), null);
			}
			final Dimension2D dimAttribute = attribute.calculateDimension(stringBounder);
			final double attributeHeight = dimAttribute.getHeight();
			final double attributeWidth = dimAttribute.getWidth();
			final double marginForFields = attributeHeight > 0 ? IEntityImage.MARGIN : 0;
			final USymbol uSymbol = g.getUSymbol();
			final int suppHeightBecauseOfShape = uSymbol == null ? 0 : uSymbol.suppHeightBecauseOfShape();
			final int suppWidthBecauseOfShape = uSymbol == null ? 0 : uSymbol.suppWidthBecauseOfShape();

			titleAndAttributeWidth = (int) Math.max(dimLabel.getWidth(), attributeWidth) + suppWidthBecauseOfShape;
			titleAndAttributeHeight = (int) (dimLabel.getHeight() + attributeHeight + marginForFields
					+ suppHeightBecauseOfShape);
		}

		dotStringFactory.openCluster(titleAndAttributeWidth, titleAndAttributeHeight, title, stereo, g);
		this.printEntities(dotStringFactory, g.getLeafsDirect());

		printGroups(dotStringFactory, g);

		dotStringFactory.closeCluster();
	}

	private TextBlock getTitleBlock(IGroup g) {
		final Display label = g.getDisplay();
		if (label == null) {
			return TextBlockUtils.empty(0, 0);
		}

		final ISkinParam skinParam = dotData.getSkinParam();
		final FontConfiguration fontConfiguration = g.getFontConfigurationForTitle(skinParam);
		return label.create(fontConfiguration, HorizontalAlignment.CENTER, skinParam);
	}

	private TextBlock addLegend(TextBlock original, DisplayPositionned legend) {
		if (legend == null || legend.isNull()) {
			return original;
		}
		final TextBlock legendBlock = EntityImageLegend.create(legend.getDisplay(), dotData.getSkinParam());
		return DecorateEntityImage.add(legendBlock, original, legend.getHorizontalAlignment(),
				legend.getVerticalAlignment());
	}

	private TextBlock getStereoBlock(IGroup g) {
		final DisplayPositionned legend = g.getLegend();
		return addLegend(getStereoBlockWithoutLegend(g), legend);
	}

	private TextBlock getStereoBlockWithoutLegend(IGroup g) {
		final Stereotype stereotype = g.getStereotype();
		// final DisplayPositionned legend = g.getLegend();
		if (stereotype == null) {
			return TextBlockUtils.empty(0, 0);
		}
		final TextBlock tmp = stereotype.getSprite(dotData.getSkinParam());
		if (tmp != null) {
			return tmp;
		}

		final List<String> stereos = stereotype.getLabels(dotData.getSkinParam().guillemet());
		if (stereos == null) {
			return TextBlockUtils.empty(0, 0);
		}
		final boolean show = dotData.showPortion(EntityPortion.STEREOTYPE, g);
		if (show == false) {
			return TextBlockUtils.empty(0, 0);
		}

		final FontParam fontParam = FontParam.PACKAGE_STEREOTYPE;
		return Display.create(stereos).create(new FontConfiguration(dotData.getSkinParam(), fontParam, stereotype),
				HorizontalAlignment.CENTER, dotData.getSkinParam());
	}

	public String getWarningOrError(int warningOrError) {
		if (maxX == null) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Double> ent : maxX.entrySet()) {
			if (ent.getValue() > warningOrError) {
				sb.append(ent.getKey() + " is overpassing the width limit.");
				sb.append("\n");
			}
		}
		return sb.length() == 0 ? "" : sb.toString();
	}
}
