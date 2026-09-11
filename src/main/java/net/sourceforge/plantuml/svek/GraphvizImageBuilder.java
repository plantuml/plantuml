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
 * Contribution :  Hisashi Miyashita
 * Contribution :  Serge Wenger
 * 
 *
 */
package net.sourceforge.plantuml.svek;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.annotation.Fast;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.crash.GraphvizCrash;
import net.sourceforge.plantuml.decoration.symbol.USymbolHexagon;
import net.sourceforge.plantuml.dot.DotData;
import net.sourceforge.plantuml.dot.ExeState;
import net.sourceforge.plantuml.dot.UnparsableGraphvizException;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.MagneticBorder;
import net.sourceforge.plantuml.klimt.geom.MagneticBorderNone;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SecurityProfile;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.PragmaKey;
import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.parser2.StyleQuery;
import net.sourceforge.plantuml.svek.image.EntityImageClass;
import net.sourceforge.plantuml.svek.image.EntityImageNote;
import net.sourceforge.plantuml.svek.layout.SvekLayoutBuilder;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResponse;
import net.sourceforge.plantuml.svek.layout.SvekLayoutValidation;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.text.BackSlash;
import net.sourceforge.plantuml.utils.Log;
import net.sourceforge.plantuml.warning.Warning;

public final class GraphvizImageBuilder {
	public static final class SmetanaFallback extends RuntimeException {
		private SmetanaFallback(String message, Throwable cause) {
			super(message, cause);
		}
	}

	private enum ModelBuildState {
		NEW, BUILDING, BUILT, FAILED
	}
	private enum LayoutState {
		NEW, LAYOUTING, COMPLETE, FAILED
	}

	interface ModelBuildHook {
		void beforeBuild();
	}
	interface LayoutApplier {
		PreparedLayout prepare(net.sourceforge.plantuml.svek.layout.SvekLayoutResult result);
	}
	interface PreparedLayout {
		SvekLayoutValidation getValidation();
		void apply();
	}
	interface LayoutApplierFactory {
		LayoutApplier create(DotStringFactory factory);
	}
	interface GraphvizOperations {
		default boolean isAvailable(DotStringFactory factory) {
			return factory.illegalDotExe() == false;
		}
		String getSvg(StringBounder stringBounder, DotMode dotMode, BaseFile basefile, String[] dotStrings)
				throws IOException;
		void solve(String svg) throws IOException, InterruptedException;
	}

	private final DotData dotData;
	private final DotMode dotMode;

	private final UmlSource source;
	private final Pragma pragma;
	private Map<String, Double> maxX;

	private final SName styleName;
	private final DotStringFactory dotStringFactory;
	private final ClusterManager clusterManager;
	private final ModelBuildHook modelBuildHook;
	private final SvekLayoutBuilder layoutBuilder;
	private final boolean layoutProviderRequested;
	private final GraphvizOperations graphvizOperations;
	private final LayoutApplierFactory layoutApplierFactory;
	private ModelBuildState modelBuildState = ModelBuildState.NEW;
	private LayoutState layoutState = LayoutState.NEW;
	private boolean automaticGraphSupport;

	public GraphvizImageBuilder(DotData dotData, UmlSource source, Pragma pragma, SName styleName, DotMode dotMode,
			DotStringFactory dotStringFactory, ClusterManager clusterManager) {
		this(dotData, source, pragma, styleName, dotMode, dotStringFactory, clusterManager, null, false, null, null,
				null);
	}

	GraphvizImageBuilder(DotData dotData, UmlSource source, Pragma pragma, SName styleName, DotMode dotMode,
			DotStringFactory dotStringFactory, ClusterManager clusterManager, ModelBuildHook modelBuildHook) {
		this(dotData, source, pragma, styleName, dotMode, dotStringFactory, clusterManager, null, false, modelBuildHook,
				null, null);
	}

	GraphvizImageBuilder(DotData dotData, UmlSource source, Pragma pragma, SName styleName, DotMode dotMode,
			DotStringFactory dotStringFactory, ClusterManager clusterManager, SvekLayoutBuilder layoutBuilder,
			boolean layoutProviderRequested) {
		this(dotData, source, pragma, styleName, dotMode, dotStringFactory, clusterManager, layoutBuilder,
				layoutProviderRequested, null, null, null);
	}

	GraphvizImageBuilder(DotData dotData, UmlSource source, Pragma pragma, SName styleName, DotMode dotMode,
			DotStringFactory dotStringFactory, ClusterManager clusterManager, SvekLayoutBuilder layoutBuilder,
			boolean layoutProviderRequested, boolean automaticGraphSupport) {
		this(dotData, source, pragma, styleName, dotMode, dotStringFactory, clusterManager, layoutBuilder,
				layoutProviderRequested, null, null, automaticGraphSupport);
	}

	GraphvizImageBuilder(DotData dotData, UmlSource source, Pragma pragma, SName styleName, DotMode dotMode,
			DotStringFactory dotStringFactory, ClusterManager clusterManager, SvekLayoutBuilder layoutBuilder,
			boolean layoutProviderRequested, GraphvizOperations graphvizOperations,
			LayoutApplierFactory layoutApplierFactory, boolean automaticGraphSupport) {
		this(dotData, source, pragma, styleName, dotMode, dotStringFactory, clusterManager, layoutBuilder,
				layoutProviderRequested, graphvizOperations, layoutApplierFactory);
		this.automaticGraphSupport = automaticGraphSupport;
	}

	GraphvizImageBuilder(DotData dotData, UmlSource source, Pragma pragma, SName styleName, DotMode dotMode,
			DotStringFactory dotStringFactory, ClusterManager clusterManager, SvekLayoutBuilder layoutBuilder,
			boolean layoutProviderRequested, GraphvizOperations graphvizOperations,
			LayoutApplierFactory layoutApplierFactory) {
		this(dotData, source, pragma, styleName, dotMode, dotStringFactory, clusterManager, layoutBuilder,
				layoutProviderRequested, null, graphvizOperations, layoutApplierFactory);
	}

	private GraphvizImageBuilder(DotData dotData, UmlSource source, Pragma pragma, SName styleName, DotMode dotMode,
			DotStringFactory dotStringFactory, ClusterManager clusterManager, SvekLayoutBuilder layoutBuilder,
			boolean layoutProviderRequested, ModelBuildHook modelBuildHook, GraphvizOperations graphvizOperations,
			LayoutApplierFactory layoutApplierFactory) {
		this.dotData = dotData;
		this.dotMode = dotMode;
		this.styleName = styleName;
		this.source = source;
		this.pragma = pragma;
		this.dotStringFactory = dotStringFactory;
		this.clusterManager = clusterManager;
		this.layoutBuilder = layoutBuilder;
		this.layoutProviderRequested = layoutProviderRequested;
		this.modelBuildHook = modelBuildHook;
		this.graphvizOperations = graphvizOperations == null ? new GraphvizOperations() {
			public String getSvg(StringBounder stringBounder, DotMode dotMode, BaseFile basefile, String[] dotStrings)
					throws IOException {
				return dotStringFactory.getSvg(stringBounder, dotMode, basefile, dotStrings);
			}

			public void solve(String svg) throws IOException, InterruptedException {
				dotStringFactory.solve(svg);
			}
		} : graphvizOperations;
		this.layoutApplierFactory = layoutApplierFactory == null ? new LayoutApplierFactory() {
			public LayoutApplier create(DotStringFactory factory) {
				final SvekLayoutResultApplier delegate = new SvekLayoutResultApplier(factory);
				return new LayoutApplier() {
					public PreparedLayout prepare(net.sourceforge.plantuml.svek.layout.SvekLayoutResult result) {
						final SvekLayoutResultApplier.PreparedLayout prepared = delegate.prepare(result);
						return new PreparedLayout() {
							public SvekLayoutValidation getValidation() {
								return prepared.getValidation();
							}

							public void apply() {
								prepared.apply();
							}
						};
					}
				};
			}
		} : layoutApplierFactory;

	}

	final public StyleQuery getDefaultStyleDefinitionArrow(Stereotype stereotype) {
		StyleQuery result = StyleQuery.of(Arrays.asList(SName.root, SName.element, styleName, SName.arrow));
		if (stereotype != null)
			result = result.withTOBECHANGED(stereotype);

		return result;
	}

	final public StyleQuery getStyleArrowCardinality(Stereotype stereotype) {
		StyleQuery result = StyleQuery
				.of(Arrays.asList(SName.root, SName.element, styleName, SName.arrow, SName.cardinality));
		if (stereotype != null)
			result = result.withTOBECHANGED(stereotype);

		return result;
	}

	private boolean isOpalisable(Entity entity) {
		if (dotData.getSkinParam().strictUmlStyle())
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

		@Fast
		@Override
		public XDimension2D calculateDimension(StringBounder stringBounder) {
			return new XDimension2D(10, 10);
		}

		public MinMax getMinMax(StringBounder stringBounder) {
			return MinMax.fromDim(calculateDimension(stringBounder));
		}

		public XRectangle2D getInnerPosition(CharSequence member, StringBounder stringBounder) {
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

		@Override
		public MagneticBorder getMagneticBorder() {
			return new MagneticBorderNone();
		}

	}

	// Duplicate SvekResult / GeneralImageBuilder
	private HColor getBackcolor() {
		final Style style = dotData.getSkinParam().getCurrentStyleBuilder()
				.getMergedStyle(StyleQuery.of(Arrays.asList(SName.root, SName.document)));
		return style.value(PName.BackGroundColor).asColor(dotData.getSkinParam().getIHtmlColorSet());
	}

	public IEntityImage buildImage(StringBounder stringBounder, BaseFile basefile, String dotStrings[],
			boolean fileFormatOptionIsDebugSvek) {
		beginLayout();
		try {
			final IEntityImage result = buildImageInternal(stringBounder, basefile, dotStrings,
					fileFormatOptionIsDebugSvek);
			completeLayout();
			return result;
		} catch (RuntimeException e) {
			failLayout();
			throw e;
		} catch (Error e) {
			failLayout();
			throw e;
		}
	}

	private IEntityImage buildImageInternal(StringBounder stringBounder, BaseFile basefile, String dotStrings[],
			boolean fileFormatOptionIsDebugSvek) {
		if (dotData.isDegeneratedWithFewEntities(0))
			return new EntityImageSimpleEmpty(dotData.getSkinParam().getBackgroundColor());

		if (dotData.isDegeneratedWithFewEntities(1) && dotData.geDiagramType() != DiagramType.STATE) {
			final Entity single = dotData.getLeafs().iterator().next();
			final Entity group = single.getParentContainer();
			if (group.isRoot() && single.getUSymbol() instanceof USymbolHexagon == false) {
				final IEntityImage tmp = GeneralImageBuilder.createEntityImageBlock(single,
						dotData.isHideEmptyDescriptionForState(), dotData.getPortionShower(), null, null,
						dotData.getLinks());
				return new EntityImageDegenerated(tmp, getBackcolor());
			}
		}
		buildModel(stringBounder);
		String layoutDecline = layoutProviderRequested && layoutBuilder == null ? "provider not found" : null;
		PreparedLayout preparedLayout = null;
		if (layoutBuilder != null) {
			SvekLayoutResponse response = null;
			try {
				response = new SvekLayoutEmitter(dotStringFactory, dotData.geDiagramType(), stringBounder)
						.emit(layoutBuilder);
			} catch (RuntimeException e) {
				Logme.error(e);
				layoutDecline = concreteMessage(e);
			} catch (LinkageError e) {
				Logme.error(e);
				layoutDecline = concreteMessage(e);
			}
			if (response != null && response.isSuccess()) {
				try {
					final PreparedLayout candidate = layoutApplierFactory.create(dotStringFactory).prepare(response.result);
					final SvekLayoutValidation validation = candidate.getValidation();
					if (validation.isValid()) {
						preparedLayout = candidate;
					}
					if (validation.isInvalid())
					if (validation.isInvalid())
						layoutDecline = validation.getMessage();
				} catch (RuntimeException e) {
					Logme.error(e);
					layoutDecline = concreteMessage(e);
				} catch (LinkageError e) {
					Logme.error(e);
					layoutDecline = concreteMessage(e);
				}
			} else if (response != null) {
				layoutDecline = response.declineMessage == null ? response.declineCode : response.declineMessage;
			}
		}
		if (preparedLayout != null) {
			try {
				preparedLayout.apply();
			} catch (RuntimeException | LinkageError e) {
				if (automaticGraphSupport)
					throw new SmetanaFallback(concreteMessage(e), e);
				throw e;
			}
			this.maxX = dotStringFactory.getBibliotekon().getMaxX();
			return new SvekResult(dotData, dotStringFactory);
		}
		if (automaticGraphSupport)
			throw new SmetanaFallback(layoutDecline, null);
		if (layoutProviderRequested && layoutDecline != null) {
			pragma.addWarning(new Warning("graph-support layout declined; using Graphviz: " + layoutDecline));
		}
		if (!TeaVM.isTeaVM()) {
			if (graphvizOperations.isAvailable(dotStringFactory) == false)
				return error(dotStringFactory.getDotExe());
		}
		if (basefile == null && (fileFormatOptionIsDebugSvek || isSvekTrace())
				&& (SecurityUtils.getSecurityProfile() == SecurityProfile.INSECURE
						|| SecurityUtils.getSecurityProfile() == SecurityProfile.LEGACY
						|| SecurityUtils.getSecurityProfile() == SecurityProfile.SANDBOX))
			basefile = new BaseFile(null);

		final String svg;
		try {
			if (layoutProviderRequested)
				dotStringFactory.useDetectedGraphvizVersion();
			dotStringFactory.prepareForGraphviz();
			svg = graphvizOperations.getSvg(stringBounder, dotMode, basefile, dotStrings);
		} catch (IOException e) {
			return GraphvizCrash.build(source.getPlainString(BackSlash.lineSeparator()), false, e);
		}
		if (svg.length() == 0)
			return GraphvizCrash.build(source.getPlainString(BackSlash.lineSeparator()), false,
					new EmptySvgException());

		final String graphvizVersion = extractGraphvizVersion(svg);
		try {
			graphvizOperations.solve(svg);
			final SvekResult result = new SvekResult(dotData, dotStringFactory);
			this.maxX = dotStringFactory.getBibliotekon().getMaxX();
			return result;
		} catch (Exception e) {
			Log.error("Exception " + e);
			throw new UnparsableGraphvizException(e, graphvizVersion, svg,
					source.getPlainString(BackSlash.lineSeparator()));
		}

	}

	private String concreteMessage(Throwable exception) {
		if (exception.getMessage() != null && exception.getMessage().length() > 0)
			return exception.getMessage();
		return exception.getClass().getName();
	}

	private synchronized void beginLayout() {
		if (layoutState == LayoutState.COMPLETE)
			throw new IllegalStateException("Svek layout has already completed");
		if (layoutState == LayoutState.LAYOUTING)
			throw new IllegalStateException("Svek layout is already in progress");
		if (layoutState == LayoutState.FAILED)
			throw new IllegalStateException("previous Svek layout failed");
		layoutState = LayoutState.LAYOUTING;
	}

	private synchronized void completeLayout() {
		layoutState = LayoutState.COMPLETE;
	}

	private synchronized void failLayout() {
		layoutState = LayoutState.FAILED;
	}

	synchronized void buildModel(StringBounder stringBounder) {
		if (modelBuildState == ModelBuildState.BUILT)
			return;
		if (modelBuildState == ModelBuildState.BUILDING)
			throw new IllegalStateException("Svek model build is already in progress");
		if (modelBuildState == ModelBuildState.FAILED)
			throw new IllegalStateException("previous Svek model build failed");

		modelBuildState = ModelBuildState.BUILDING;
		try {
			if (modelBuildHook != null)
				modelBuildHook.beforeBuild();
			dotData.removeIrrelevantSametail();
			printGroups(stringBounder, dotData.getRootGroup());
			printEntities(stringBounder, getUnpackagedEntities());
			for (Link link : dotData.getLinks())
				addSvekEdge(stringBounder, link);
			modelBuildState = ModelBuildState.BUILT;
		} catch (RuntimeException e) {
			modelBuildState = ModelBuildState.FAILED;
			throw e;
		} catch (Error e) {
			modelBuildState = ModelBuildState.FAILED;
			throw e;
		}
	}

	void addSvekEdge(StringBounder stringBounder, Link link) {
		if (link.isRemoved())
			return;

		try {
			final ISkinParam skinParam = dotData.getSkinParam();
			final FontConfiguration labelFont = link.getStyleBuilder()
					.getMergedStyle(getDefaultStyleDefinitionArrow(link.getStereotype()))
					.getFontConfiguration(skinParam.getIHtmlColorSet());
			final FontConfiguration cardinalityFont = link.getStyleBuilder()
					.getMergedStyle(getStyleArrowCardinality(link.getStereotype()))
					.getFontConfiguration(skinParam.getIHtmlColorSet());

			final SvekEdge line = new SvekEdge(link, skinParam, stringBounder, labelFont, cardinalityFont,
					dotStringFactory.getBibliotekon(), pragma, dotStringFactory.getGraphvizVersion());
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

	private boolean isSvekTrace() {
		final String value = pragma.getValue(PragmaKey.SVEK_TRACE);
		return "true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value);
	}

	private String extractGraphvizVersion(String svg) {
		final Pattern pGraph = Pattern.compile("(?mi)!-- generated by graphviz(.*)");
		final Matcher mGraph = pGraph.matcher(svg);
		if (mGraph.find())
			return StringUtils.trin(mGraph.group(1));

		return null;
	}

	private Link onlyOneLink(Entity ent) {
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

	// ::comment when __TEAVM__
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
	// ::done

	private void printEntities(StringBounder stringBounder, Collection<Entity> entities2) {
		for (Entity ent : entities2) {
			if (ent.isRemoved())
				continue;

			printEntity(stringBounder, ent);
		}
	}

	private void printEntity(StringBounder stringBounder, Entity ent) {
		if (ent.isRemoved())
			throw new IllegalStateException(ent.toString());

		final IEntityImage image = printEntityInternal(stringBounder, ent);
		final SvekNode node = dotStringFactory.getBibliotekon().createNode(ent, image, stringBounder);
		clusterManager.addNode(node);
	}

	private IEntityImage printEntityInternal(StringBounder stringBounder, Entity ent) {
		if (ent.isRemoved())
			throw new IllegalStateException();

		if (ent.getSvekImage() == null) {
			ISkinParam skinParam = dotData.getSkinParam();
			if (skinParam.sameClassWidth()) {
				final double width = getMaxWidth(stringBounder);
				((SkinParam) skinParam).setParamSameClassWidth(width);
			}

			return GeneralImageBuilder.createEntityImageBlock(ent, dotData.isHideEmptyDescriptionForState(),
					dotData.getPortionShower(), dotStringFactory.getBibliotekon(),
					dotStringFactory.getGraphvizVersion(), dotData.getLinks());
		}
		return ent.getSvekImage();
	}

	private double getMaxWidth(StringBounder stringBounder) {
		double result = 0;
		for (Entity ent : dotData.getLeafs()) {
			if (ent.getLeafType().isLikeClass() == false)
				continue;

			final IEntityImage im = new EntityImageClass(ent, dotData.getPortionShower());
			final double w = im.calculateDimension(stringBounder).getWidth();
			if (w > result)
				result = w;

		}
		return result;
	}

	private Collection<Entity> getUnpackagedEntities() {
		final List<Entity> result = new ArrayList<>();
		for (Entity ent : dotData.getLeafs())
			if (dotData.getTopParent() == ent.getParentContainer())
				result.add(ent);

		return result;
	}

	private void printGroups(StringBounder stringBounder, Entity parent) {
		// System.err.println("PARENT=" + parent);
		final Collection<Entity> groups = dotData.getGroupHierarchy().getChildrenGroups(parent);
		// System.err.println("groups=" + groups);
		for (Entity g : groups) {
			if (g.isRemoved())
				continue;

			if (dotData.isEmpty(g) && g.getGroupType() == GroupType.PACKAGE) {
				g.muteToType(LeafType.EMPTY_PACKAGE);
				printEntity(stringBounder, g);
			} else {
				printGroup(stringBounder, g);
			}
		}
	}

	private void printGroup(StringBounder stringBounder, Entity g) {
		if (g.getGroupType() == GroupType.CONCURRENT_STATE)
			return;

		final ClusterHeader clusterHeader = new ClusterHeader(g, dotData.getPortionShower(), stringBounder);
		clusterManager.openCluster(g, clusterHeader);
		this.printEntities(stringBounder, g.leafs());

		printGroups(stringBounder, g);

		clusterManager.closeCluster();
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
