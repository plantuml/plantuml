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

	public static final StyleQuery DOCUMENT = StyleQuery.of3(SName.root, SName.document);

	public static final StyleQuery DOCUMENT_HEADER = DOCUMENT.add(SName.header);

	public static final StyleQuery DOCUMENT_FOOTER = DOCUMENT.add(SName.footer);

	public static final StyleQuery DOCUMENT_TITLE = DOCUMENT.add(SName.title);

	public static final StyleQuery DOCUMENT_MAINFRAME = DOCUMENT.add(SName.mainframe);

	public static final StyleQuery DOCUMENT_CAPTION = DOCUMENT.add(SName.caption);

	public static final StyleQuery STEREOTYPE = ELEMENT.add(SName.stereotype);

	public static final StyleQuery ACTIVITYDIAG = ELEMENT.add(SName.activityDiagram);

	public static final StyleQuery MINDMAPDIAG = ELEMENT.add(SName.mindmapDiagram);

	public static final StyleQuery GANTTDIAG = ELEMENT.add(SName.ganttDiagram);

	public static final StyleQuery SEQUENCEDIAG = ELEMENT.add(SName.sequenceDiagram);

	public static final StyleQuery WBSDIAG = ELEMENT.add(SName.wbsDiagram);

	public static final StyleQuery ACTIVITYDIAG_ACTIVITY_ARROW = ACTIVITYDIAG.add(SName.activity, SName.arrow);

	public static final StyleQuery ACTIVITYDIAG_ARROW = ACTIVITYDIAG.add(SName.arrow);

	public static final StyleQuery ACTIVITYDIAG_ACTIVITY = ACTIVITYDIAG.add(SName.activity);

	public static final StyleQuery ACTIVITYDIAG_ACTIVITY_DIAMOND = ACTIVITYDIAG.add(SName.activity, SName.diamond);

	public static final StyleQuery ACTIVITYDIAG_NOTE = ACTIVITYDIAG.add(SName.note);

	public static final StyleQuery ACTIVITYDIAG_CIRCLE = ACTIVITYDIAG.add(SName.circle);

	public static final StyleQuery ACTIVITYDIAG_SWIMLANE = ACTIVITYDIAG.add(SName.swimlane);

	public static final StyleQuery ACTIVITYDIAG_GOTO = ACTIVITYDIAG.add(SName.goto_);

	public static final StyleQuery ACTIVITYDIAG_ACTIVITYBAR = ACTIVITYDIAG.add(SName.activityBar);

	public static final StyleQuery GITDIAG = ELEMENT.add(SName.gitDiagram);

	public static final StyleQuery STATEDIAG_STATE = ELEMENT.add(SName.stateDiagram, SName.state);

	public static final StyleQuery SEQUENCEDIAG_ARROW = SEQUENCEDIAG.add(SName.arrow);

	public static final StyleQuery TIMINGDIAG = ELEMENT.add(SName.timingDiagram);

	public static final StyleQuery CHENEER_ENTITY = ELEMENT.add(SName.chenEerDiagram, SName.chenEntity);

	public static final StyleQuery CHENEER_ATTRIBUTE = ELEMENT.add(SName.chenEerDiagram, SName.chenAttribute);

	public static final StyleQuery CHENEER_CIRCLE = ELEMENT.add(SName.chenEerDiagram, SName.circle);

	public static final StyleQuery CLASSDIAG_CLASS = ELEMENT.add(SName.classDiagram, SName.class_);

	public static final StyleQuery GANTTDIAG_TASK = GANTTDIAG.add(SName.task);

	public static final StyleQuery TIMINGDIAG_ARROW = ELEMENT.add(SName.timingDiagram, SName.arrow);

	public static final StyleQuery SEQUENCEDIAG_NOTE = SEQUENCEDIAG.add(SName.note);

	public static final StyleQuery SEQUENCEDIAG_PARTICIPANT = SEQUENCEDIAG.add(SName.participant);

	public static final StyleQuery SEQUENCEDIAG_LIFELINE_DELAY = SEQUENCEDIAG.add(SName.lifeLine, SName.delay);

	public static final StyleQuery CHARTDIAG_BAR = ELEMENT.add(SName.chartDiagram, SName.bar);

	public static final StyleQuery CHARTDIAG_SCATTER = ELEMENT.add(SName.chartDiagram, SName.scatter);

	public static final StyleQuery CHARTDIAG = ELEMENT.add(SName.chartDiagram);

	public static final StyleQuery CHARTDIAG_LINE = ELEMENT.add(SName.chartDiagram, SName.line);

	public static final StyleQuery CHARTDIAG_AREA = ELEMENT.add(SName.chartDiagram, SName.area);

	public static final StyleQuery CHARTDIAG_GRID = ELEMENT.add(SName.chartDiagram, SName.grid);

	public static final StyleQuery CHARTDIAG_LEGEND = ELEMENT.add(SName.chartDiagram, SName.legend);

	public static final StyleQuery CHARTDIAG_ANNOTATION = ELEMENT.add(SName.chartDiagram, SName.annotation);

	public static final StyleQuery GANTTDIAG_TIMELINE = GANTTDIAG.add(SName.timeline);

	public static final StyleQuery GANTTDIAG_ARROW = GANTTDIAG.add(SName.arrow);

	public static final StyleQuery GANTTDIAG_UNDONE = GANTTDIAG.add(SName.undone);

	public static final StyleQuery GANTTDIAG_MILESTONE = GANTTDIAG.add(SName.milestone);

	public static final StyleQuery GANTTDIAG_TASK_UNSTARTED = GANTTDIAG.add(SName.task, SName.unstarted);

	public static final StyleQuery GANTTDIAG_SEPARATOR = GANTTDIAG.add(SName.separator);

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

	public static final StyleQuery VISIBILITYICON_IE_MANDATORY = ELEMENT.add(SName.visibilityIcon, SName.IEMandatory);

	public static final StyleQuery VISIBILITYICON_PUBLIC = ELEMENT.add(SName.visibilityIcon, SName.public_);

	public static final StyleQuery VISIBILITYICON_PRIVATE = ELEMENT.add(SName.visibilityIcon, SName.private_);

	public static final StyleQuery VISIBILITYICON_PROTECTED = ELEMENT.add(SName.visibilityIcon, SName.protected_);

	public static final StyleQuery VISIBILITYICON_PACKAGE = ELEMENT.add(SName.visibilityIcon, SName.package_);

	public static final StyleQuery WBSDIAG_ARROW = WBSDIAG.add(SName.arrow);

	public static final StyleQuery CLASSDIAG_CLASS_HEADER = ELEMENT.add(SName.classDiagram, SName.class_, SName.header);

	public static final StyleQuery MINDMAPDIAG_NODE = MINDMAPDIAG.add(SName.node);

	public static final StyleQuery MINDMAPDIAG_NODE_ROOT = MINDMAPDIAG.add(SName.node, SName.rootNode);

	public static final StyleQuery MINDMAPDIAG_NODE_ROOT_BOXLESS = MINDMAPDIAG.add(SName.node, SName.rootNode,
			SName.boxless);

	public static final StyleQuery MINDMAPDIAG_NODE_LEAF = MINDMAPDIAG.add(SName.node, SName.leafNode);

	public static final StyleQuery MINDMAPDIAG_NODE_LEAF_BOXLESS = MINDMAPDIAG.add(SName.node, SName.leafNode,
			SName.boxless);

	public static final StyleQuery MINDMAPDIAG_NODE_BOXLESS = MINDMAPDIAG.add(SName.node, SName.boxless);

	public static final StyleQuery MINDMAPDIAG_ARROW = MINDMAPDIAG.add(SName.arrow);

	public static final StyleQuery WBSDIAG_NODE = WBSDIAG.add(SName.node);

	public static final StyleQuery WBSDIAG_NODE_ROOT = WBSDIAG.add(SName.node, SName.rootNode);

	public static final StyleQuery WBSDIAG_NODE_ROOT_BOXLESS = WBSDIAG.add(SName.node, SName.rootNode, SName.boxless);

	public static final StyleQuery WBSDIAG_NODE_LEAF = WBSDIAG.add(SName.node, SName.leafNode);

	public static final StyleQuery WBSDIAG_NODE_LEAF_BOXLESS = WBSDIAG.add(SName.node, SName.leafNode, SName.boxless);

	public static final StyleQuery WBSDIAG_NODE_BOXLESS = WBSDIAG.add(SName.node, SName.boxless);

	public static final StyleQuery SEQUENCEDIAG_ACTOR = SEQUENCEDIAG.add(SName.actor);

	public static final StyleQuery SEQUENCEDIAG_BOUNDARY = SEQUENCEDIAG.add(SName.boundary);

	public static final StyleQuery SEQUENCEDIAG_CONTROL = SEQUENCEDIAG.add(SName.control);

	public static final StyleQuery SEQUENCEDIAG_ENTITY = SEQUENCEDIAG.add(SName.entity);

	public static final StyleQuery SEQUENCEDIAG_QUEUE = SEQUENCEDIAG.add(SName.queue);

	public static final StyleQuery SEQUENCEDIAG_DATABASE = SEQUENCEDIAG.add(SName.database);

	public static final StyleQuery SEQUENCEDIAG_COLLECTIONS = SEQUENCEDIAG.add(SName.collections);

	public static final StyleQuery SEQUENCEDIAG_LIFELINE = SEQUENCEDIAG.add(SName.lifeLine);

	public static final StyleQuery SEQUENCEDIAG_ACTIVATIONBOX = SEQUENCEDIAG.add(SName.activationBox);

	public static final StyleQuery SEQUENCEDIAG_SEPARATOR = SEQUENCEDIAG.add(SName.separator);

	public static final StyleQuery SEQUENCEDIAG_BOX = SEQUENCEDIAG.add(SName.box);

	public static final StyleQuery SEQUENCEDIAG_NEWPAGE = SEQUENCEDIAG.add(SName.newpage);

	public static final StyleQuery ACTIVITYDIAG_CIRCLE_END = ACTIVITYDIAG.add(SName.circle, SName.end);

	public static final StyleQuery ACTIVITYDIAG_CIRCLE_STOP = ACTIVITYDIAG.add(SName.circle, SName.stop);

	public static final StyleQuery ACTIVITYDIAG_CIRCLE_SPOT = ACTIVITYDIAG.add(SName.circle, SName.spot);

	public static final StyleQuery ACTIVITYDIAG_CIRCLE_START = ACTIVITYDIAG.add(SName.circle, SName.start);

	public static final StyleQuery EBNF = ELEMENT.add(SName.ebnf);

	public static final StyleQuery FILESDIAG = ELEMENT.add(SName.filesDiagram);

	public static final StyleQuery FILESDIAG_NOTE = ELEMENT.add(SName.filesDiagram, SName.note);

	public static final StyleQuery PACKETDIAG = ELEMENT.add(SName.packetdiagDiagram);

	public static final StyleQuery PACKETDIAG_RECTANGLE = ELEMENT.add(SName.packetdiagDiagram, SName.rectangle);

	public static final StyleQuery NWDIAG_GROUP = ELEMENT.add(SName.nwdiagDiagram, SName.group);

	public static final StyleQuery COMPONENTDIAG_COMPONENT = ELEMENT.add(SName.componentDiagram, SName.component);

	public static final StyleQuery GANTTDIAG_NOTE = GANTTDIAG.add(SName.note);

	public static final StyleQuery SEQUENCEDIAG_PARTITION = SEQUENCEDIAG.add(SName.partition);

	public static final StyleQuery SEQUENCEDIAG_GROUP = SEQUENCEDIAG.add(SName.group);

	public static final StyleQuery SEQUENCEDIAG_PARTITION_HEADER = SEQUENCEDIAG.add(SName.partition, SName.header);

	public static final StyleQuery SEQUENCEDIAG_GROUPHEADER = SEQUENCEDIAG.add(SName.groupHeader);

	public static final StyleQuery SEQUENCEDIAG_GROUP_HEADER = SEQUENCEDIAG.add(SName.group, SName.header);

	public static final StyleQuery SEQUENCEDIAG_LIFELINE_DESTROY = SEQUENCEDIAG.add(SName.lifeLine, SName.destroy);

	public static final StyleQuery SEQUENCEDIAG_NOTE_HNOTE = SEQUENCEDIAG.add(SName.note, SName.hnote);

	public static final StyleQuery SEQUENCEDIAG_NOTE_RNOTE = SEQUENCEDIAG.add(SName.note, SName.rnote);

	public static final StyleQuery SEQUENCEDIAG_REFERENCE = SEQUENCEDIAG.add(SName.reference);

	public static final StyleQuery SEQUENCEDIAG_REFERENCEHEADER = SEQUENCEDIAG.add(SName.referenceHeader);

	public static final StyleQuery SEQUENCEDIAG_REFERENCE_HEADER = SEQUENCEDIAG.add(SName.reference, SName.header);

	public static final StyleQuery OBJECTDIAG_MAP = ELEMENT.add(SName.objectDiagram, SName.map);

	public static final StyleQuery OBJECTDIAG_MAP_HEADER = ELEMENT.add(SName.objectDiagram, SName.map, SName.header);

	public static final StyleQuery OBJECTDIAG_OBJECT = ELEMENT.add(SName.objectDiagram, SName.object);

	public static final StyleQuery OBJECTDIAG_OBJECT_HEADER = ELEMENT.add(SName.objectDiagram, SName.object,
			SName.header);

	public static final StyleQuery OBJECTDIAG_JSON = ELEMENT.add(SName.objectDiagram, SName.json);

	public static final StyleQuery OBJECTDIAG_JSON_HEADER = ELEMENT.add(SName.objectDiagram, SName.json, SName.header);

	public static final StyleQuery CLASSDIAG_ARROW = ELEMENT.add(SName.classDiagram, SName.arrow);

	public static final StyleQuery COMPONENTDIAG_USECASE = ELEMENT.add(SName.componentDiagram, SName.usecase);

	public static final StyleQuery COMPONENTDIAG_USECASE_BUSINESS = ELEMENT.add(SName.componentDiagram, SName.usecase,
			SName.business);

	public static final StyleQuery CHENEER_ATTRIBUTE_TITLE = ELEMENT.add(SName.chenEerDiagram, SName.chenAttribute,
			SName.title);

	public static final StyleQuery CHENEER_CIRCLE_TITLE = ELEMENT.add(SName.chenEerDiagram, SName.circle, SName.title);

	public static final StyleQuery CHENEER_ENTITY_TITLE = ELEMENT.add(SName.chenEerDiagram, SName.chenEntity,
			SName.title);

	public static final StyleQuery CHENEER_RELATIONSHIP = ELEMENT.add(SName.chenEerDiagram, SName.chenRelationship);

	public static final StyleQuery CHENEER_RELATIONSHIP_TITLE = ELEMENT.add(SName.chenEerDiagram,
			SName.chenRelationship, SName.title);

	public static final StyleQuery ACTIVITYDIAG_GROUP = ACTIVITYDIAG.add(SName.group);

	public static final StyleQuery TIMINGDIAG_HIGHLIGHT = ELEMENT.add(SName.timingDiagram, SName.highlight);

	public static final StyleQuery TIMINGDIAG_CONSTRAINTARROW = ELEMENT.add(SName.timingDiagram, SName.constraintArrow);

	public static final StyleQuery TIMINGDIAG_TIMEGRID = ELEMENT.add(SName.timingDiagram, SName.timegrid);

	public static final StyleQuery TIMINGDIAG_TIMELINE = ELEMENT.add(SName.timingDiagram, SName.timeline);

	public static final StyleQuery TIMINGDIAG_NOTE = ELEMENT.add(SName.timingDiagram, SName.note);

	public static final StyleQuery STATEDIAG_STATE_GROUP = ELEMENT.add(SName.stateDiagram, SName.state, SName.group);

	public static final StyleQuery STATEDIAG_STATE_NAME = ELEMENT.add(SName.stateDiagram, SName.state, SName.name);

	public static final StyleQuery COMPONENTDIAG_REQUIREMENT = ELEMENT.add(SName.componentDiagram, SName.requirement);

	public static final StyleQuery NOTE = ELEMENT.add(SName.note);

	public static final StyleQuery DIAMOND = ELEMENT.add(SName.diamond);

	public static final StyleQuery PORT = ELEMENT.add(SName.port);

	public static final StyleQuery STATE = ELEMENT.add(SName.state);

	public static final StyleQuery CIRCLE = ELEMENT.add(SName.circle);

	public static final StyleQuery CIRCLE_END = ELEMENT.add(SName.circle, SName.end);

	public static final StyleQuery CIRCLE_START = ELEMENT.add(SName.circle, SName.start);

	public static final StyleQuery ACTOR_BUSINESS_TITLE = ELEMENT.add(SName.actor, SName.business, SName.title);

	public static final StyleQuery PACKAGE_TITLE = ELEMENT.add(SName.package_, SName.title);

	public static final StyleQuery ARROW = ELEMENT.add(SName.arrow);

	public static final StyleQuery NODE = ELEMENT.add(SName.node);

	public static final StyleQuery NODE_HEADER = ELEMENT.add(SName.header, SName.node);

	public static final StyleQuery NODE_HIGHLIGHT = ELEMENT.add(SName.node, SName.highlight);

	public static final StyleQuery NODE_HEADER_HIGHLIGHT = ELEMENT.add(SName.header, SName.node, SName.highlight);

	public static final StyleQuery NODE_SEPARATOR = ELEMENT.add(SName.node, SName.separator);

	public static final StyleQuery NWDIAG = ELEMENT.add(SName.nwdiagDiagram);

	public static final StyleQuery COMPONENT = ELEMENT.add(SName.component);

	public static final StyleQuery GROUP = ELEMENT.add(SName.group);

	public static final StyleQuery PACKAGE_GROUP = ELEMENT.add(SName.package_, SName.group);

	public static final StyleQuery COMPOSITE_TITLE = ELEMENT.add(SName.composite, SName.title);

	public static final StyleQuery ARROW_CARDINALITY = ELEMENT.add(SName.arrow, SName.cardinality);

	public static final StyleQuery CLASSDIAG_CLASS_QUALIFIED = ELEMENT.add(SName.classDiagram, SName.class_,
			SName.qualified);

	public static final StyleQuery CLASSDIAG_CLASS_GENERIC = ELEMENT.add(SName.classDiagram, SName.class_,
			SName.generic);

	public static final StyleQuery CHARTDIAG_AXIS = ELEMENT.add(SName.chartDiagram, SName.axis);

}
