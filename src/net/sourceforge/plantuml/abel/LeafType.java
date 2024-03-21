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
 * Contribution :  The-Lum
 *
 */
package net.sourceforge.plantuml.abel;

import net.sourceforge.plantuml.StringUtils;

public enum LeafType {

	EMPTY_PACKAGE,

	ABSTRACT_CLASS, CLASS, INTERFACE, ANNOTATION, PROTOCOL, STRUCT, EXCEPTION, METACLASS, STEREOTYPE, LOLLIPOP_FULL,
	LOLLIPOP_HALF, NOTE, TIPS, OBJECT, MAP, JSON, ASSOCIATION, ENUM, CIRCLE,

	USECASE, USECASE_BUSINESS,

	DESCRIPTION,

	ARC_CIRCLE,

	ACTIVITY, BRANCH, SYNCHRO_BAR, CIRCLE_START, CIRCLE_END, POINT_FOR_ASSOCIATION, ACTIVITY_CONCURRENT,

	STATE, STATE_CONCURRENT, PSEUDO_STATE, DEEP_HISTORY, STATE_CHOICE, STATE_FORK_JOIN,

	BLOCK, ENTITY,

	DOMAIN, REQUIREMENT,

	PORTIN, PORTOUT,

	CHEN_ENTITY, CHEN_RELATIONSHIP, CHEN_ATTRIBUTE, CHEN_CIRCLE,

	STILL_UNKNOWN;

	public static LeafType getLeafType(String type) {
		type = StringUtils.goUpperCase(type);
		if (type.startsWith("ABSTRACT"))
			return LeafType.ABSTRACT_CLASS;

		if (type.startsWith("DIAMOND"))
			return LeafType.STATE_CHOICE;

		if (type.startsWith("STATIC"))
			return LeafType.CLASS;

		return LeafType.valueOf(type);
	}

	public boolean isLikeClass() {
		return this == LeafType.ANNOTATION || this == LeafType.ABSTRACT_CLASS || this == LeafType.CLASS
				|| this == LeafType.INTERFACE || this == LeafType.ENUM || this == LeafType.ENTITY
				|| this == LeafType.PROTOCOL || this == LeafType.STRUCT || this == LeafType.EXCEPTION
				|| this == LeafType.METACLASS || this == LeafType.STEREOTYPE;
	}

	public String toHtml() {
		final String html = StringUtils.goLowerCase(toString().replace('_', ' '));
		return StringUtils.capitalize(html);
	}

//	public boolean manageModifier() {
//		if (this == ANNOTATION || this == ABSTRACT_CLASS || this == CLASS || this == INTERFACE || this == ENUM
//				|| this == OBJECT || this == ENTITY) {
//			return true;
//		}
//		return false;
//	}
}
