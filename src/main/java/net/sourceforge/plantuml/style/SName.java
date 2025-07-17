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

import java.util.HashMap;
import java.util.Map;

public enum SName {
	action, //
	activationBox, //
	activity, //
	activityBar, //
	activityDiagram, //
	actor, //
	agent, //
	analog, //
	archimate, //
	arrow, //
	artifact, //
	binary, //
	boundary, //
	box, //
	boxless, //
	business, //
	caption, //
	card, //
	cardinality, //
	circle, //
	classDiagram, //
	class_, //
	clickable, //
	cloud, //
	closed, //
	collection, //
	collections, //
	component, //
	composite, //
	robust, //
	chenAttribute, //
	chenEerDiagram, //
	chenEntity, //
	chenRelationship, //
	concise, //
	clock, //
	componentDiagram, //
	constraintArrow, //
	control, //
	database, //
	day, //
	delay, //
	destroy, //
	diamond, //
	document, //
	ebnf, //
	element, //
	entity, //
	end, //
	start, //
	stop, //
	file, //
	filesDiagram, //
	folder, //
	footer, //
	frame, //
	ganttDiagram, //
	generic, //
	goto_, //
	group, //
	groupHeader, //
	header, //
	hexagon, //
	highlight, //
	hnote, //
	interface_, //
	json, //
	jsonDiagram, //
	gitDiagram, //
	label, //
	leafNode, //
	legend, //
	lifeLine, //
	mainframe, //
	map, //
	milestone, //
	mindmapDiagram, //
	month, //
	network, //
	newpage, //
	node, //
	note, //
	nwdiagDiagram, //
	objectDiagram, //
	object, //
	package_, //
	participant, //
	partition, //
	person, //
	port, //
	process, //
	qualified, //
	queue, //
	rectangle, //
	reference, //
	referenceHeader, //
	regex, //
	requirement, //
	rnote, //
	root, //
	rootNode, //
	saltDiagram, //
	separator, //
	sequenceDiagram, //
	server, //
	stack, //
	stateDiagram, //
	state, //
	stateBody, //
	stereotype, //
	storage, //
	swimlane, //
	task, //
	timegrid, //
	timeline, //
	timingDiagram, //
	title, //
	undone, //
	unstarted, //
	usecase, //
	verticalSeparator, //
	year, //

	visibilityIcon, //
	private_, //
	protected_, //
	public_, //
	IEMandatory, //
	spot, //
	spotAnnotation, //
	spotInterface, //
	spotEnum, //
	spotProtocol, //
	spotStruct, //
	spotEntity, //
	spotException, //
	spotClass, //
	spotAbstractClass, //
	spotMetaClass, //
	spotStereotype, //
	spotDataClass, //
	spotRecord, //

	wbsDiagram, //
	yamlDiagram; //

	private static final Map<String, SName> ALL = new HashMap<>();

	static {
		for (SName sname : SName.values()) {
			final String s = sname.name().replace("_", "").toLowerCase();
			ALL.put(s, sname);
		}
	}

	public static SName retrieve(String s) {
		return ALL.get(s.toLowerCase());
	}

}
