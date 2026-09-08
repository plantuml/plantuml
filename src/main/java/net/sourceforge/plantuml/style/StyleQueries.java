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

}
