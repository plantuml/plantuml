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
package net.sourceforge.plantuml.style;

public final class StyleQueries {

	public static final StyleQuery ROOT = StyleQuery.of3(SName.root);

	public static final StyleQuery ELEMENT = StyleQuery.of3(SName.root, SName.element);

	public static final StyleQuery STEREOTYPE = ELEMENT.add(SName.stereotype);

	public static final StyleQuery ACTIVITYDIAG_ACTIVITY_ARROW = ELEMENT.add(SName.activityDiagram, SName.activity,
			SName.arrow);

	public static final StyleQuery ACTIVITYDIAG_ARROW = ELEMENT.add(SName.activityDiagram, SName.arrow);

	public static final StyleQuery ACTIVITYDIAG_ACTIVITY = ELEMENT.add(SName.activityDiagram, SName.activity);

	public static final StyleQuery ACTIVITYDIAG_ACTIVITY_DIAMOND = ELEMENT.add(SName.activityDiagram, SName.activity,
			SName.diamond);

	public static final StyleQuery ACTIVITYDIAG_NOTE = ELEMENT.add(SName.activityDiagram, SName.note);

	public static final StyleQuery ACTIVITYDIAG_CIRCLE = ELEMENT.add(SName.activityDiagram, SName.circle);

	public static final StyleQuery ACTIVITYDIAG_SWIMLANE = ELEMENT.add(SName.activityDiagram, SName.swimlane);

	public static final StyleQuery ACTIVITYDIAG_GOTO = ELEMENT.add(SName.activityDiagram, SName.goto_);

	public static final StyleQuery ACTIVITYDIAG_ACTIVITYBAR = ELEMENT.add(SName.activityDiagram, SName.activityBar);

	/**
	 * No sub-element -- the diagram's own root style, e.g. a background rectangle.
	 */
	public static final StyleQuery DOCUMENT = StyleQuery.of3(SName.root, SName.document);

	public static final StyleQuery GITDIAG = ELEMENT.add(SName.gitDiagram);

	public static final StyleQuery STATEDIAG_STATE = ELEMENT.add(SName.stateDiagram, SName.state);

	public static final StyleQuery SEQUENCEDIAG_ARROW = ELEMENT.add(SName.sequenceDiagram, SName.arrow);

	/** No sub-element -- the diagram's own root style. */
	public static final StyleQuery TIMINGDIAG = ELEMENT.add(SName.timingDiagram);

	public static final StyleQuery CHENEER_ENTITY = ELEMENT.add(SName.chenEerDiagram, SName.chenEntity);

	public static final StyleQuery CHENEER_ATTRIBUTE = ELEMENT.add(SName.chenEerDiagram, SName.chenAttribute);

	public static final StyleQuery CHENEER_CIRCLE = ELEMENT.add(SName.chenEerDiagram, SName.circle);

	public static final StyleQuery CLASSDIAG_CLASS = ELEMENT.add(SName.classDiagram, SName.class_);

	public static final StyleQuery GANTTDIAG_TASK = ELEMENT.add(SName.ganttDiagram, SName.task);

	public static final StyleQuery TIMINGDIAG_ARROW = ELEMENT.add(SName.timingDiagram, SName.arrow);

	public static final StyleQuery SEQUENCEDIAG_NOTE = ELEMENT.add(SName.sequenceDiagram, SName.note);

	public static final StyleQuery SEQUENCEDIAG_PARTICIPANT = ELEMENT.add(SName.sequenceDiagram, SName.participant);

	public static final StyleQuery SEQUENCEDIAG_LIFELINE_DELAY = ELEMENT.add(SName.sequenceDiagram, SName.lifeLine,
			SName.delay);

	/**
	 * No diagram context -- applies across every diagram type, e.g. the exported
	 * page chrome.
	 */
	public static final StyleQuery DOCUMENT_HEADER = StyleQuery.of3(SName.root, SName.document, SName.header);

	public static final StyleQuery DOCUMENT_FOOTER = StyleQuery.of3(SName.root, SName.document, SName.footer);

	public static final StyleQuery DOCUMENT_TITLE = StyleQuery.of3(SName.root, SName.document, SName.title);

	public static final StyleQuery CHARTDIAG_BAR = ELEMENT.add(SName.chartDiagram, SName.bar);

	public static final StyleQuery CHARTDIAG_SCATTER = ELEMENT.add(SName.chartDiagram, SName.scatter);

	/** No sub-element -- the diagram's own root style. */
	public static final StyleQuery CHARTDIAG = ELEMENT.add(SName.chartDiagram);

	public static final StyleQuery CHARTDIAG_LINE = ELEMENT.add(SName.chartDiagram, SName.line);

	public static final StyleQuery CHARTDIAG_AREA = ELEMENT.add(SName.chartDiagram, SName.area);

	public static final StyleQuery CHARTDIAG_GRID = ELEMENT.add(SName.chartDiagram, SName.grid);

	public static final StyleQuery CHARTDIAG_LEGEND = ELEMENT.add(SName.chartDiagram, SName.legend);

	public static final StyleQuery CHARTDIAG_ANNOTATION = ELEMENT.add(SName.chartDiagram, SName.annotation);

	public static final StyleQuery GANTTDIAG_TIMELINE = ELEMENT.add(SName.ganttDiagram, SName.timeline);

	public static final StyleQuery GANTTDIAG_ARROW = ELEMENT.add(SName.ganttDiagram, SName.arrow);

	public static final StyleQuery GANTTDIAG_UNDONE = ELEMENT.add(SName.ganttDiagram, SName.undone);

	public static final StyleQuery GANTTDIAG_MILESTONE = ELEMENT.add(SName.ganttDiagram, SName.milestone);

	public static final StyleQuery GANTTDIAG_TASK_UNSTARTED = ELEMENT.add(SName.ganttDiagram, SName.task,
			SName.unstarted);

	public static final StyleQuery GANTTDIAG_SEPARATOR = ELEMENT.add(SName.ganttDiagram, SName.separator);

	/**
	 * The class-header icon shown for each classifier kind ({@code class},
	 * {@code interface}, an annotation, ...) -- one constant per {@link SName} the
	 * header can carry, so the enumeration in {@code EntityImageClassHeader} reads
	 * as a closed set rather than 13 near-identical one-off literals.
	 */
	public static final StyleQuery SPOT_ANNOTATION = ELEMENT.add(SName.spot, SName.spotAnnotation);

	public static final StyleQuery SPOT_ABSTRACT_CLASS = ELEMENT.add(SName.spot, SName.spotAbstractClass);

	public static final StyleQuery SPOT_CLASS = ELEMENT.add(SName.spot, SName.spotClass);

	public static final StyleQuery SPOT_INTERFACE = ELEMENT.add(SName.spot, SName.spotInterface);

	public static final StyleQuery SPOT_ENUM = ELEMENT.add(SName.spot, SName.spotEnum);

	public static final StyleQuery SPOT_ENTITY = ELEMENT.add(SName.spot, SName.spotEntity);

	public static final StyleQuery SPOT_PROTOCOL = ELEMENT.add(SName.spot, SName.spotProtocol);

	public static final StyleQuery SPOT_STRUCT = ELEMENT.add(SName.spot, SName.spotStruct);

	public static final StyleQuery SPOT_EXCEPTION = ELEMENT.add(SName.spot, SName.spotException);

	public static final StyleQuery SPOT_META_CLASS = ELEMENT.add(SName.spot, SName.spotMetaClass);

	public static final StyleQuery SPOT_STEREOTYPE = ELEMENT.add(SName.spot, SName.spotStereotype);

	public static final StyleQuery SPOT_DATA_CLASS = ELEMENT.add(SName.spot, SName.spotDataClass);

	public static final StyleQuery SPOT_RECORD = ELEMENT.add(SName.spot, SName.spotRecord);

	/**
	 * The visibility-modifier icon shown next to a class member ({@code +},
	 * {@code -}, {@code #}, {@code ~}, or the IE-notation mandatory marker) --
	 * another closed set, mirroring the {@code SPOT_*} family above for
	 * {@code VisibilityModifier}.
	 */
	public static final StyleQuery VISIBILITYICON_IE_MANDATORY = ELEMENT.add(SName.visibilityIcon, SName.IEMandatory);

	public static final StyleQuery VISIBILITYICON_PUBLIC = ELEMENT.add(SName.visibilityIcon, SName.public_);

	public static final StyleQuery VISIBILITYICON_PRIVATE = ELEMENT.add(SName.visibilityIcon, SName.private_);

	public static final StyleQuery VISIBILITYICON_PROTECTED = ELEMENT.add(SName.visibilityIcon, SName.protected_);

	public static final StyleQuery VISIBILITYICON_PACKAGE = ELEMENT.add(SName.visibilityIcon, SName.package_);

	/**
	 * Duplicated literally in two files -- {@code WBSTextBlock} and
	 * {@code WBSDiagram}.
	 */
	public static final StyleQuery WBSDIAG_ARROW = ELEMENT.add(SName.wbsDiagram, SName.arrow);

	/**
	 * Duplicated literally in two files -- {@code EntityImageClassHeader} and
	 * {@code EntityImageClass}.
	 */
	public static final StyleQuery CLASSDIAG_CLASS_HEADER = ELEMENT.add(SName.classDiagram, SName.class_, SName.header);

	/**
	 * No diagram context, same family as
	 * {@code DOCUMENT_HEADER}/{@code _FOOTER}/{@code _TITLE}.
	 */
	public static final StyleQuery DOCUMENT_MAINFRAME = StyleQuery.of3(SName.root, SName.document, SName.mainframe);

	public static final StyleQuery DOCUMENT_CAPTION = StyleQuery.of3(SName.root, SName.document, SName.caption);

	/**
	 * A mindmap node's shape ({@code Idea}), keyed by root/leaf and boxless -- a
	 * closed set the same way {@code SPOT_*} and {@code VISIBILITYICON_*} are, one
	 * constant per combination actually used.
	 */
	public static final StyleQuery MINDMAPDIAG_NODE = ELEMENT.add(SName.mindmapDiagram, SName.node);

	public static final StyleQuery MINDMAPDIAG_NODE_ROOT = ELEMENT.add(SName.mindmapDiagram, SName.node,
			SName.rootNode);

	public static final StyleQuery MINDMAPDIAG_NODE_ROOT_BOXLESS = ELEMENT.add(SName.mindmapDiagram, SName.node,
			SName.rootNode, SName.boxless);

	public static final StyleQuery MINDMAPDIAG_NODE_LEAF = ELEMENT.add(SName.mindmapDiagram, SName.node,
			SName.leafNode);

	public static final StyleQuery MINDMAPDIAG_NODE_LEAF_BOXLESS = ELEMENT.add(SName.mindmapDiagram, SName.node,
			SName.leafNode, SName.boxless);

	public static final StyleQuery MINDMAPDIAG_NODE_BOXLESS = ELEMENT.add(SName.mindmapDiagram, SName.node,
			SName.boxless);

	public static final StyleQuery MINDMAPDIAG_ARROW = ELEMENT.add(SName.mindmapDiagram, SName.arrow);

	/**
	 * A WBS node's shape ({@code WElement}) -- same closed-set shape as
	 * {@code MINDMAPDIAG_NODE*}.
	 */
	public static final StyleQuery WBSDIAG_NODE = ELEMENT.add(SName.wbsDiagram, SName.node);

	public static final StyleQuery WBSDIAG_NODE_ROOT = ELEMENT.add(SName.wbsDiagram, SName.node, SName.rootNode);

	public static final StyleQuery WBSDIAG_NODE_ROOT_BOXLESS = ELEMENT.add(SName.wbsDiagram, SName.node, SName.rootNode,
			SName.boxless);

	public static final StyleQuery WBSDIAG_NODE_LEAF = ELEMENT.add(SName.wbsDiagram, SName.node, SName.leafNode);

	public static final StyleQuery WBSDIAG_NODE_LEAF_BOXLESS = ELEMENT.add(SName.wbsDiagram, SName.node, SName.leafNode,
			SName.boxless);

	public static final StyleQuery WBSDIAG_NODE_BOXLESS = ELEMENT.add(SName.wbsDiagram, SName.node, SName.boxless);

	/**
	 * The participant kind icon ({@code ParticipantType}) --
	 * actor/boundary/control/entity/ queue/database/collections -- a closed set
	 * alongside {@code SEQUENCEDIAG_PARTICIPANT} itself (the plain, kind-less
	 * participant).
	 */
	public static final StyleQuery SEQUENCEDIAG_ACTOR = ELEMENT.add(SName.sequenceDiagram, SName.actor);

	public static final StyleQuery SEQUENCEDIAG_BOUNDARY = ELEMENT.add(SName.sequenceDiagram, SName.boundary);

	public static final StyleQuery SEQUENCEDIAG_CONTROL = ELEMENT.add(SName.sequenceDiagram, SName.control);

	public static final StyleQuery SEQUENCEDIAG_ENTITY = ELEMENT.add(SName.sequenceDiagram, SName.entity);

	public static final StyleQuery SEQUENCEDIAG_QUEUE = ELEMENT.add(SName.sequenceDiagram, SName.queue);

	public static final StyleQuery SEQUENCEDIAG_DATABASE = ELEMENT.add(SName.sequenceDiagram, SName.database);

	public static final StyleQuery SEQUENCEDIAG_COLLECTIONS = ELEMENT.add(SName.sequenceDiagram, SName.collections);

	/**
	 * Structural components ({@code ComponentType}) -- lifeline/activation
	 * box/separator/box/ newpage -- completing the sequence-diagram catalog
	 * alongside {@code SEQUENCEDIAG_NOTE} and {@code SEQUENCEDIAG_PARTICIPANT},
	 * which come from the same file.
	 */
	public static final StyleQuery SEQUENCEDIAG_LIFELINE = ELEMENT.add(SName.sequenceDiagram, SName.lifeLine);

	public static final StyleQuery SEQUENCEDIAG_ACTIVATIONBOX = ELEMENT.add(SName.sequenceDiagram, SName.activationBox);

	public static final StyleQuery SEQUENCEDIAG_SEPARATOR = ELEMENT.add(SName.sequenceDiagram, SName.separator);

	public static final StyleQuery SEQUENCEDIAG_BOX = ELEMENT.add(SName.sequenceDiagram, SName.box);

	public static final StyleQuery SEQUENCEDIAG_NEWPAGE = ELEMENT.add(SName.sequenceDiagram, SName.newpage);

	/** No sub-element -- the diagram's own root style. */
	public static final StyleQuery ACTIVITYDIAG = ELEMENT.add(SName.activityDiagram);

	/**
	 * The four circle markers (start/end/stop/spot) drawn by a compact
	 * activity-diagram tile -- closed set completing {@code ACTIVITYDIAG_CIRCLE}
	 * (the bare circle style), the same way {@code SPOT_*} completes {@code SPOT}.
	 */
	public static final StyleQuery ACTIVITYDIAG_CIRCLE_END = ELEMENT.add(SName.activityDiagram, SName.circle,
			SName.end);

	public static final StyleQuery ACTIVITYDIAG_CIRCLE_STOP = ELEMENT.add(SName.activityDiagram, SName.circle,
			SName.stop);

	public static final StyleQuery ACTIVITYDIAG_CIRCLE_SPOT = ELEMENT.add(SName.activityDiagram, SName.circle,
			SName.spot);

	public static final StyleQuery ACTIVITYDIAG_CIRCLE_START = ELEMENT.add(SName.activityDiagram, SName.circle,
			SName.start);

	/** No sub-element -- the diagram's own root style. */
	public static final StyleQuery EBNF = ELEMENT.add(SName.ebnf);

	/** No sub-element -- the diagram's own root style. */
	public static final StyleQuery FILESDIAG = ELEMENT.add(SName.filesDiagram);

	public static final StyleQuery FILESDIAG_NOTE = ELEMENT.add(SName.filesDiagram, SName.note);

	/** No sub-element -- the diagram's own root style. */
	public static final StyleQuery PACKETDIAG = ELEMENT.add(SName.packetdiagDiagram);

	public static final StyleQuery PACKETDIAG_RECTANGLE = ELEMENT.add(SName.packetdiagDiagram, SName.rectangle);

	public static final StyleQuery NWDIAG_GROUP = ELEMENT.add(SName.nwdiagDiagram, SName.group);

	public static final StyleQuery COMPONENTDIAG_COMPONENT = ELEMENT.add(SName.componentDiagram, SName.component);

	public static final StyleQuery GANTTDIAG_NOTE = ELEMENT.add(SName.ganttDiagram, SName.note);

	/** No sub-element -- the diagram's own root style. */
	public static final StyleQuery SEQUENCEDIAG = ELEMENT.add(SName.sequenceDiagram);

	/**
	 * {@code partition}/{@code group} (a plain {@code group {}} block versus the
	 * dedicated {@code partition} construct) and their headers, both the legacy
	 * flat selector and the nested {@code header {}} form added for #2679 -- see
	 * {@code Grouping} and {@code Reference} below for why each pair keeps a flat
	 * and a nested constant.
	 */
	public static final StyleQuery SEQUENCEDIAG_PARTITION = ELEMENT.add(SName.sequenceDiagram, SName.partition);

	public static final StyleQuery SEQUENCEDIAG_GROUP = ELEMENT.add(SName.sequenceDiagram, SName.group);

	public static final StyleQuery SEQUENCEDIAG_PARTITION_HEADER = ELEMENT.add(SName.sequenceDiagram, SName.partition,
			SName.header);

	public static final StyleQuery SEQUENCEDIAG_GROUPHEADER = ELEMENT.add(SName.sequenceDiagram, SName.groupHeader);

	public static final StyleQuery SEQUENCEDIAG_GROUP_HEADER = ELEMENT.add(SName.sequenceDiagram, SName.group,
			SName.header);

	/**
	 * Pairs with {@code SEQUENCEDIAG_LIFELINE_DELAY} -- the other
	 * lifeline-decoration kind.
	 */
	public static final StyleQuery SEQUENCEDIAG_LIFELINE_DESTROY = ELEMENT.add(SName.sequenceDiagram, SName.lifeLine,
			SName.destroy);

	/** The two note shapes, alongside the plain {@code SEQUENCEDIAG_NOTE}. */
	public static final StyleQuery SEQUENCEDIAG_NOTE_HNOTE = ELEMENT.add(SName.sequenceDiagram, SName.note,
			SName.hnote);

	public static final StyleQuery SEQUENCEDIAG_NOTE_RNOTE = ELEMENT.add(SName.sequenceDiagram, SName.note,
			SName.rnote);

	/**
	 * {@code reference} and its header, flat and nested -- same shape as
	 * {@code SEQUENCEDIAG_GROUP*}.
	 */
	public static final StyleQuery SEQUENCEDIAG_REFERENCE = ELEMENT.add(SName.sequenceDiagram, SName.reference);

	public static final StyleQuery SEQUENCEDIAG_REFERENCEHEADER = ELEMENT.add(SName.sequenceDiagram,
			SName.referenceHeader);

	public static final StyleQuery SEQUENCEDIAG_REFERENCE_HEADER = ELEMENT.add(SName.sequenceDiagram, SName.reference,
			SName.header);

	/**
	 * {@code map}/{@code object}/{@code json} and their headers, from the three
	 * object-diagram shapes.
	 */
	public static final StyleQuery OBJECTDIAG_MAP = ELEMENT.add(SName.objectDiagram, SName.map);

	public static final StyleQuery OBJECTDIAG_MAP_HEADER = ELEMENT.add(SName.objectDiagram, SName.map, SName.header);

	public static final StyleQuery OBJECTDIAG_OBJECT = ELEMENT.add(SName.objectDiagram, SName.object);

	public static final StyleQuery OBJECTDIAG_OBJECT_HEADER = ELEMENT.add(SName.objectDiagram, SName.object,
			SName.header);

	public static final StyleQuery OBJECTDIAG_JSON = ELEMENT.add(SName.objectDiagram, SName.json);

	public static final StyleQuery OBJECTDIAG_JSON_HEADER = ELEMENT.add(SName.objectDiagram, SName.json, SName.header);

	public static final StyleQuery CLASSDIAG_ARROW = ELEMENT.add(SName.classDiagram, SName.arrow);

	/** The plain and "business" use-case shapes. */
	public static final StyleQuery COMPONENTDIAG_USECASE = ELEMENT.add(SName.componentDiagram, SName.usecase);

	public static final StyleQuery COMPONENTDIAG_USECASE_BUSINESS = ELEMENT.add(SName.componentDiagram, SName.usecase,
			SName.business);

	/**
	 * The title style for each Chen-ER shape, alongside the plain
	 * {@code CHENEER_ATTRIBUTE}/ {@code _CIRCLE}/{@code _ENTITY} and the
	 * newly-added {@code CHENEER_RELATIONSHIP}.
	 */
	public static final StyleQuery CHENEER_ATTRIBUTE_TITLE = ELEMENT.add(SName.chenEerDiagram, SName.chenAttribute,
			SName.title);

	public static final StyleQuery CHENEER_CIRCLE_TITLE = ELEMENT.add(SName.chenEerDiagram, SName.circle, SName.title);

	public static final StyleQuery CHENEER_ENTITY_TITLE = ELEMENT.add(SName.chenEerDiagram, SName.chenEntity,
			SName.title);

	public static final StyleQuery CHENEER_RELATIONSHIP = ELEMENT.add(SName.chenEerDiagram, SName.chenRelationship);

	public static final StyleQuery CHENEER_RELATIONSHIP_TITLE = ELEMENT.add(SName.chenEerDiagram,
			SName.chenRelationship, SName.title);

	public static final StyleQuery ACTIVITYDIAG_GROUP = ELEMENT.add(SName.activityDiagram, SName.group);

	public static final StyleQuery TIMINGDIAG_HIGHLIGHT = ELEMENT.add(SName.timingDiagram, SName.highlight);

	public static final StyleQuery TIMINGDIAG_CONSTRAINTARROW = ELEMENT.add(SName.timingDiagram, SName.constraintArrow);

	public static final StyleQuery TIMINGDIAG_TIMEGRID = ELEMENT.add(SName.timingDiagram, SName.timegrid);

	public static final StyleQuery TIMINGDIAG_TIMELINE = ELEMENT.add(SName.timingDiagram, SName.timeline);

	public static final StyleQuery TIMINGDIAG_NOTE = ELEMENT.add(SName.timingDiagram, SName.note);

	/**
	 * The state-diagram special cases inside
	 * {@code Cluster.getDefaultStyleDefinition} and
	 * {@code ClusterHeader.getSignature} -- the one branch of each that doesn't
	 * depend on the caller-supplied diagram style name, alongside the plain
	 * {@code STATEDIAG_STATE}.
	 */
	public static final StyleQuery STATEDIAG_STATE_GROUP = ELEMENT.add(SName.stateDiagram, SName.state, SName.group);

	public static final StyleQuery STATEDIAG_STATE_NAME = ELEMENT.add(SName.stateDiagram, SName.state, SName.name);

	public static final StyleQuery COMPONENTDIAG_REQUIREMENT = ELEMENT.add(SName.componentDiagram, SName.requirement);

	/**
	 * The diagram-type-less base for a note, meant to be completed with
	 * {@code .addSName(sname)} once the caller-supplied diagram style name is known
	 * -- {@link StyleQuery}'s atoms are an unordered set, so this is exactly
	 * equivalent to {@code ELEMENT.add( sname, SName.note)}.
	 */
	public static final StyleQuery NOTE = ELEMENT.add(SName.note);

	/**
	 * The remaining diagram-type-less bases below follow the same
	 * addSName(dynamic-diagram-style-name)/addSNames(...) pattern as {@link #NOTE}
	 * -- each is completed with the caller-supplied diagram style name (and
	 * sometimes a {@code USymbol}'s own names) once known.
	 */
	public static final StyleQuery DIAMOND = ELEMENT.add(SName.diamond);

	public static final StyleQuery PORT = ELEMENT.add(SName.port);

	public static final StyleQuery STATE = ELEMENT.add(SName.state);

	public static final StyleQuery CIRCLE = ELEMENT.add(SName.circle);

	public static final StyleQuery CIRCLE_END = ELEMENT.add(SName.circle, SName.end);

	public static final StyleQuery CIRCLE_START = ELEMENT.add(SName.circle, SName.start);

	public static final StyleQuery ACTOR_BUSINESS_TITLE = ELEMENT.add(SName.actor, SName.business, SName.title);

	/**
	 * Shared by the empty-package shape and {@code ClusterHeader}'s package title.
	 */
	public static final StyleQuery PACKAGE_TITLE = ELEMENT.add(SName.package_, SName.title);

	public static final StyleQuery ARROW = ELEMENT.add(SName.arrow);

	public static final StyleQuery NODE = ELEMENT.add(SName.node);

	public static final StyleQuery NODE_HEADER = ELEMENT.add(SName.header, SName.node);

	public static final StyleQuery NODE_HIGHLIGHT = ELEMENT.add(SName.node, SName.highlight);

	public static final StyleQuery NODE_HEADER_HIGHLIGHT = ELEMENT.add(SName.header, SName.node, SName.highlight);

	public static final StyleQuery NODE_SEPARATOR = ELEMENT.add(SName.node, SName.separator);

	public static final StyleQuery GANTTDIAG = ELEMENT.add(SName.ganttDiagram);

	public static final StyleQuery NWDIAG = ELEMENT.add(SName.nwdiagDiagram);

	public static final StyleQuery COMPONENT = ELEMENT.add(SName.component);

	/**
	 * Shared by {@code Cluster.getDefaultStyleDefinition}'s symbol and plain-group
	 * branches.
	 */
	public static final StyleQuery GROUP = ELEMENT.add(SName.group);

	public static final StyleQuery PACKAGE_GROUP = ELEMENT.add(SName.package_, SName.group);

	/**
	 * Shared by {@code ClusterHeader.getSignature}'s uSymbol and plain-composite
	 * branches.
	 */
	public static final StyleQuery COMPOSITE_TITLE = ELEMENT.add(SName.composite, SName.title);

	public static final StyleQuery ARROW_CARDINALITY = ELEMENT.add(SName.arrow, SName.cardinality);

	public static final StyleQuery CLASSDIAG_CLASS_QUALIFIED = ELEMENT.add(SName.classDiagram, SName.class_,
			SName.qualified);

	public static final StyleQuery CLASSDIAG_CLASS_GENERIC = ELEMENT.add(SName.classDiagram, SName.class_,
			SName.generic);

	public static final StyleQuery CHARTDIAG_AXIS = ELEMENT.add(SName.chartDiagram, SName.axis);

}
