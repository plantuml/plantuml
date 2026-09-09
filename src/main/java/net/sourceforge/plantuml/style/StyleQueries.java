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

	public static final StyleQuery ACTIVITYDIAG_ACTIVITY_ARROW = StyleQuery.of3(SName.root, SName.element,
			SName.activityDiagram, SName.activity, SName.arrow);

	public static final StyleQuery ACTIVITYDIAG_ARROW = StyleQuery.of3(SName.root, SName.element,
			SName.activityDiagram, SName.arrow);

	public static final StyleQuery ACTIVITYDIAG_ACTIVITY = StyleQuery.of3(SName.root, SName.element,
			SName.activityDiagram, SName.activity);

	public static final StyleQuery ACTIVITYDIAG_ACTIVITY_DIAMOND = StyleQuery.of3(SName.root, SName.element,
			SName.activityDiagram, SName.activity, SName.diamond);

	public static final StyleQuery ACTIVITYDIAG_NOTE = StyleQuery.of3(SName.root, SName.element,
			SName.activityDiagram, SName.note);

	public static final StyleQuery ACTIVITYDIAG_CIRCLE = StyleQuery.of3(SName.root, SName.element,
			SName.activityDiagram, SName.circle);

	public static final StyleQuery ACTIVITYDIAG_SWIMLANE = StyleQuery.of3(SName.root, SName.element,
			SName.activityDiagram, SName.swimlane);

	public static final StyleQuery ACTIVITYDIAG_GOTO = StyleQuery.of3(SName.root, SName.element,
			SName.activityDiagram, SName.goto_);

	public static final StyleQuery ACTIVITYDIAG_ACTIVITYBAR = StyleQuery.of3(SName.root, SName.element,
			SName.activityDiagram, SName.activityBar);

	/** No sub-element -- the diagram's own root style, e.g. a background rectangle. */
	public static final StyleQuery DOCUMENT = StyleQuery.of3(SName.root, SName.document);

	public static final StyleQuery GITDIAG = StyleQuery.of3(SName.root, SName.element, SName.gitDiagram);

	public static final StyleQuery STATEDIAG_STATE = StyleQuery.of3(SName.root, SName.element, SName.stateDiagram,
			SName.state);

	public static final StyleQuery SEQUENCEDIAG_ARROW = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.arrow);

	/** No sub-element -- the diagram's own root style. */
	public static final StyleQuery TIMINGDIAG = StyleQuery.of3(SName.root, SName.element, SName.timingDiagram);

	public static final StyleQuery CHENEER_ENTITY = StyleQuery.of3(SName.root, SName.element, SName.chenEerDiagram,
			SName.chenEntity);

	public static final StyleQuery CHENEER_ATTRIBUTE = StyleQuery.of3(SName.root, SName.element,
			SName.chenEerDiagram, SName.chenAttribute);

	public static final StyleQuery CHENEER_CIRCLE = StyleQuery.of3(SName.root, SName.element, SName.chenEerDiagram,
			SName.circle);

	public static final StyleQuery CLASSDIAG_CLASS = StyleQuery.of3(SName.root, SName.element, SName.classDiagram,
			SName.class_);

	public static final StyleQuery GANTTDIAG_TASK = StyleQuery.of3(SName.root, SName.element, SName.ganttDiagram,
			SName.task);

	public static final StyleQuery TIMINGDIAG_ARROW = StyleQuery.of3(SName.root, SName.element, SName.timingDiagram,
			SName.arrow);

	public static final StyleQuery SEQUENCEDIAG_NOTE = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.note);

	public static final StyleQuery SEQUENCEDIAG_PARTICIPANT = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.participant);

	public static final StyleQuery SEQUENCEDIAG_LIFELINE_DELAY = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.lifeLine, SName.delay);

	/** No diagram context -- applies across every diagram type, e.g. the exported page chrome. */
	public static final StyleQuery DOCUMENT_HEADER = StyleQuery.of3(SName.root, SName.document, SName.header);

	public static final StyleQuery DOCUMENT_FOOTER = StyleQuery.of3(SName.root, SName.document, SName.footer);

	public static final StyleQuery DOCUMENT_TITLE = StyleQuery.of3(SName.root, SName.document, SName.title);

	public static final StyleQuery CHARTDIAG_BAR = StyleQuery.of3(SName.root, SName.element, SName.chartDiagram,
			SName.bar);

	public static final StyleQuery CHARTDIAG_SCATTER = StyleQuery.of3(SName.root, SName.element, SName.chartDiagram,
			SName.scatter);

	/** No sub-element -- the diagram's own root style. */
	public static final StyleQuery CHARTDIAG = StyleQuery.of3(SName.root, SName.element, SName.chartDiagram);

	public static final StyleQuery CHARTDIAG_LINE = StyleQuery.of3(SName.root, SName.element, SName.chartDiagram,
			SName.line);

	public static final StyleQuery CHARTDIAG_AREA = StyleQuery.of3(SName.root, SName.element, SName.chartDiagram,
			SName.area);

	public static final StyleQuery CHARTDIAG_GRID = StyleQuery.of3(SName.root, SName.element, SName.chartDiagram,
			SName.grid);

	public static final StyleQuery CHARTDIAG_LEGEND = StyleQuery.of3(SName.root, SName.element, SName.chartDiagram,
			SName.legend);

	public static final StyleQuery CHARTDIAG_ANNOTATION = StyleQuery.of3(SName.root, SName.element,
			SName.chartDiagram, SName.annotation);

	public static final StyleQuery GANTTDIAG_TIMELINE = StyleQuery.of3(SName.root, SName.element,
			SName.ganttDiagram, SName.timeline);

	public static final StyleQuery GANTTDIAG_ARROW = StyleQuery.of3(SName.root, SName.element, SName.ganttDiagram,
			SName.arrow);

	public static final StyleQuery GANTTDIAG_UNDONE = StyleQuery.of3(SName.root, SName.element, SName.ganttDiagram,
			SName.undone);

	public static final StyleQuery GANTTDIAG_MILESTONE = StyleQuery.of3(SName.root, SName.element,
			SName.ganttDiagram, SName.milestone);

	public static final StyleQuery GANTTDIAG_TASK_UNSTARTED = StyleQuery.of3(SName.root, SName.element,
			SName.ganttDiagram, SName.task, SName.unstarted);

	public static final StyleQuery GANTTDIAG_SEPARATOR = StyleQuery.of3(SName.root, SName.element,
			SName.ganttDiagram, SName.separator);

	/**
	 * The class-header icon shown for each classifier kind ({@code class}, {@code interface}, an
	 * annotation, ...) -- one constant per {@link SName} the header can carry, so the enumeration
	 * in {@code EntityImageClassHeader} reads as a closed set rather than 13 near-identical
	 * one-off literals.
	 */
	public static final StyleQuery SPOT_ANNOTATION = StyleQuery.of3(SName.root, SName.element, SName.spot,
			SName.spotAnnotation);

	public static final StyleQuery SPOT_ABSTRACT_CLASS = StyleQuery.of3(SName.root, SName.element, SName.spot,
			SName.spotAbstractClass);

	public static final StyleQuery SPOT_CLASS = StyleQuery.of3(SName.root, SName.element, SName.spot, SName.spotClass);

	public static final StyleQuery SPOT_INTERFACE = StyleQuery.of3(SName.root, SName.element, SName.spot,
			SName.spotInterface);

	public static final StyleQuery SPOT_ENUM = StyleQuery.of3(SName.root, SName.element, SName.spot, SName.spotEnum);

	public static final StyleQuery SPOT_ENTITY = StyleQuery.of3(SName.root, SName.element, SName.spot,
			SName.spotEntity);

	public static final StyleQuery SPOT_PROTOCOL = StyleQuery.of3(SName.root, SName.element, SName.spot,
			SName.spotProtocol);

	public static final StyleQuery SPOT_STRUCT = StyleQuery.of3(SName.root, SName.element, SName.spot,
			SName.spotStruct);

	public static final StyleQuery SPOT_EXCEPTION = StyleQuery.of3(SName.root, SName.element, SName.spot,
			SName.spotException);

	public static final StyleQuery SPOT_META_CLASS = StyleQuery.of3(SName.root, SName.element, SName.spot,
			SName.spotMetaClass);

	public static final StyleQuery SPOT_STEREOTYPE = StyleQuery.of3(SName.root, SName.element, SName.spot,
			SName.spotStereotype);

	public static final StyleQuery SPOT_DATA_CLASS = StyleQuery.of3(SName.root, SName.element, SName.spot,
			SName.spotDataClass);

	public static final StyleQuery SPOT_RECORD = StyleQuery.of3(SName.root, SName.element, SName.spot,
			SName.spotRecord);

	/**
	 * The visibility-modifier icon shown next to a class member ({@code +}, {@code -}, {@code #},
	 * {@code ~}, or the IE-notation mandatory marker) -- another closed set, mirroring the
	 * {@code SPOT_*} family above for {@code VisibilityModifier}.
	 */
	public static final StyleQuery VISIBILITYICON_IE_MANDATORY = StyleQuery.of3(SName.root, SName.element,
			SName.visibilityIcon, SName.IEMandatory);

	public static final StyleQuery VISIBILITYICON_PUBLIC = StyleQuery.of3(SName.root, SName.element,
			SName.visibilityIcon, SName.public_);

	public static final StyleQuery VISIBILITYICON_PRIVATE = StyleQuery.of3(SName.root, SName.element,
			SName.visibilityIcon, SName.private_);

	public static final StyleQuery VISIBILITYICON_PROTECTED = StyleQuery.of3(SName.root, SName.element,
			SName.visibilityIcon, SName.protected_);

	public static final StyleQuery VISIBILITYICON_PACKAGE = StyleQuery.of3(SName.root, SName.element,
			SName.visibilityIcon, SName.package_);

	/** Duplicated literally in two files -- {@code WBSTextBlock} and {@code WBSDiagram}. */
	public static final StyleQuery WBSDIAG_ARROW = StyleQuery.of3(SName.root, SName.element, SName.wbsDiagram,
			SName.arrow);

	/** Duplicated literally in two files -- {@code EntityImageClassHeader} and {@code EntityImageClass}. */
	public static final StyleQuery CLASSDIAG_CLASS_HEADER = StyleQuery.of3(SName.root, SName.element,
			SName.classDiagram, SName.class_, SName.header);

	/** No diagram context, same family as {@code DOCUMENT_HEADER}/{@code _FOOTER}/{@code _TITLE}. */
	public static final StyleQuery DOCUMENT_MAINFRAME = StyleQuery.of3(SName.root, SName.document,
			SName.mainframe);

	public static final StyleQuery DOCUMENT_CAPTION = StyleQuery.of3(SName.root, SName.document, SName.caption);

	/**
	 * A mindmap node's shape ({@code Idea}), keyed by root/leaf and boxless -- a closed set the
	 * same way {@code SPOT_*} and {@code VISIBILITYICON_*} are, one constant per combination
	 * actually used.
	 */
	public static final StyleQuery MINDMAPDIAG_NODE = StyleQuery.of3(SName.root, SName.element, SName.mindmapDiagram,
			SName.node);

	public static final StyleQuery MINDMAPDIAG_NODE_ROOT = StyleQuery.of3(SName.root, SName.element,
			SName.mindmapDiagram, SName.node, SName.rootNode);

	public static final StyleQuery MINDMAPDIAG_NODE_ROOT_BOXLESS = StyleQuery.of3(SName.root, SName.element,
			SName.mindmapDiagram, SName.node, SName.rootNode, SName.boxless);

	public static final StyleQuery MINDMAPDIAG_NODE_LEAF = StyleQuery.of3(SName.root, SName.element,
			SName.mindmapDiagram, SName.node, SName.leafNode);

	public static final StyleQuery MINDMAPDIAG_NODE_LEAF_BOXLESS = StyleQuery.of3(SName.root, SName.element,
			SName.mindmapDiagram, SName.node, SName.leafNode, SName.boxless);

	public static final StyleQuery MINDMAPDIAG_NODE_BOXLESS = StyleQuery.of3(SName.root, SName.element,
			SName.mindmapDiagram, SName.node, SName.boxless);

	public static final StyleQuery MINDMAPDIAG_ARROW = StyleQuery.of3(SName.root, SName.element,
			SName.mindmapDiagram, SName.arrow);

	/** A WBS node's shape ({@code WElement}) -- same closed-set shape as {@code MINDMAPDIAG_NODE*}. */
	public static final StyleQuery WBSDIAG_NODE = StyleQuery.of3(SName.root, SName.element, SName.wbsDiagram,
			SName.node);

	public static final StyleQuery WBSDIAG_NODE_ROOT = StyleQuery.of3(SName.root, SName.element, SName.wbsDiagram,
			SName.node, SName.rootNode);

	public static final StyleQuery WBSDIAG_NODE_ROOT_BOXLESS = StyleQuery.of3(SName.root, SName.element,
			SName.wbsDiagram, SName.node, SName.rootNode, SName.boxless);

	public static final StyleQuery WBSDIAG_NODE_LEAF = StyleQuery.of3(SName.root, SName.element, SName.wbsDiagram,
			SName.node, SName.leafNode);

	public static final StyleQuery WBSDIAG_NODE_LEAF_BOXLESS = StyleQuery.of3(SName.root, SName.element,
			SName.wbsDiagram, SName.node, SName.leafNode, SName.boxless);

	public static final StyleQuery WBSDIAG_NODE_BOXLESS = StyleQuery.of3(SName.root, SName.element, SName.wbsDiagram,
			SName.node, SName.boxless);

	/**
	 * The participant kind icon ({@code ParticipantType}) -- actor/boundary/control/entity/
	 * queue/database/collections -- a closed set alongside {@code SEQUENCEDIAG_PARTICIPANT}
	 * itself (the plain, kind-less participant).
	 */
	public static final StyleQuery SEQUENCEDIAG_ACTOR = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.actor);

	public static final StyleQuery SEQUENCEDIAG_BOUNDARY = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.boundary);

	public static final StyleQuery SEQUENCEDIAG_CONTROL = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.control);

	public static final StyleQuery SEQUENCEDIAG_ENTITY = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.entity);

	public static final StyleQuery SEQUENCEDIAG_QUEUE = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.queue);

	public static final StyleQuery SEQUENCEDIAG_DATABASE = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.database);

	public static final StyleQuery SEQUENCEDIAG_COLLECTIONS = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.collections);

	/**
	 * Structural components ({@code ComponentType}) -- lifeline/activation box/separator/box/
	 * newpage -- completing the sequence-diagram catalog alongside {@code SEQUENCEDIAG_NOTE}
	 * and {@code SEQUENCEDIAG_PARTICIPANT}, which come from the same file.
	 */
	public static final StyleQuery SEQUENCEDIAG_LIFELINE = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.lifeLine);

	public static final StyleQuery SEQUENCEDIAG_ACTIVATIONBOX = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.activationBox);

	public static final StyleQuery SEQUENCEDIAG_SEPARATOR = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.separator);

	public static final StyleQuery SEQUENCEDIAG_BOX = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.box);

	public static final StyleQuery SEQUENCEDIAG_NEWPAGE = StyleQuery.of3(SName.root, SName.element,
			SName.sequenceDiagram, SName.newpage);

}
