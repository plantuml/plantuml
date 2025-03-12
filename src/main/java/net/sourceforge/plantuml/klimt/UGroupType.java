/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.klimt;

public enum UGroupType {
	
	ID,
	CLASS,
	TITLE,
	DATA_ENTITY,
	DATA_QUALIFIED_NAME,
	DATA_ENTITY_1,
	DATA_ENTITY_2,
	DATA_ENTITY_UID,
	DATA_ENTITY_1_UID,
	DATA_ENTITY_2_UID,
	DATA_PARTICIPANT,
	DATA_PARTICIPANT_1,
	DATA_PARTICIPANT_2,
	DATA_UID,
	DATA_SOURCE_LINE;

	public String getSvgKeyAttributeName() {
		return name().toLowerCase().replace('_', '-');
	}
}
