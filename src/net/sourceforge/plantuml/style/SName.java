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
 *
 *
 */
package net.sourceforge.plantuml.style;

public enum SName {
	activity, //
	activityBar, //
	activityDiagram, //
	actor, //
	agent, //
	archimate, //
	arrow, //
	artifact, //
	boundary, //
	box, //
	boxless, //
	caption, //
	card, //
	circle, //
	classDiagram, //
	class_, //
	clickable, //
	cloud, //
	closed, //
	collection, //
	collections, //
	component, //
	componentDiagram, //
	constraintArrow, //
	control, //
	database, //
	delay, //
	destroy, //
	diamond, //
	document, //
	element, //
	entity, //
	file, //
	folder, //
	footer, //
	frame, //
	ganttDiagram, //
	group, //
	groupHeader, //
	header, //
	hexagon, //
	highlight, //
	interface_, //
	jsonDiagram, //
	gitDiagram, //
	label, //
	leafNode, //
	legend, //
	lifeLine, //
	map, //
	milestone, //
	mindmapDiagram, //
	network, //
	node, //
	note, //
	nwdiagDiagram, //
	objectDiagram, //
	object, //
	package_, //
	participant, //
	partition, //
	person, //
	queue, //
	rectangle, //
	reference, //
	referenceHeader, //
	root, //
	rootNode, //
	saltDiagram, //
	separator, //
	sequenceDiagram, //
	server, //
	stack, //
	stateDiagram, //
	state, //
	stereotype, //
	storage, //
	swimlane, //
	task, //
	timeline, //
	timingDiagram, //
	title, //
	unstartedTask, //
	usecase, //
	wbsDiagram, //
	yamlDiagram; //

	public static String depth(int level) {
		return "depth(" + level + ")";
	}
}