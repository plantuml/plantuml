/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 5191 $
 *
 */
package net.sourceforge.plantuml.skin;

public enum ComponentType {

	ARROW,

	ACTOR_HEAD, ACTOR_TAIL,
	
	BOUNDARY_HEAD, BOUNDARY_TAIL,
	CONTROL_HEAD, CONTROL_TAIL,
	ENTITY_HEAD, ENTITY_TAIL,
	DATABASE_HEAD, DATABASE_TAIL,

	//
	ALIVE_BOX_CLOSE_CLOSE, ALIVE_BOX_CLOSE_OPEN, ALIVE_BOX_OPEN_CLOSE, ALIVE_BOX_OPEN_OPEN,

	DELAY_TEXT, DESTROY,

	DELAY_LINE, PARTICIPANT_LINE, CONTINUE_LINE,

	//
	GROUPING_ELSE, GROUPING_HEADER, GROUPING_SPACE,
	//
	NEWPAGE, NOTE, NOTE_HEXAGONAL, NOTE_BOX, DIVIDER, REFERENCE, ENGLOBER,

	//
	PARTICIPANT_HEAD, PARTICIPANT_TAIL,

	//
	TITLE, SIGNATURE;

	public boolean isArrow() {
		return this == ARROW;
	}
}
