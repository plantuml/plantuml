/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
	ACTOR_HEAD, ACTOR_LINE, ACTOR_TAIL,

	ALIVE_LINE, DESTROY,

	ARROW, DOTTED_ARROW, DOTTED_SELF_ARROW, RETURN_ARROW, RETURN_DOTTED_ARROW, SELF_ARROW,

	ASYNC_ARROW, ASYNC_DOTTED_ARROW, ASYNC_RETURN_ARROW, ASYNC_RETURN_DOTTED_ARROW,

	GROUPING_BODY, GROUPING_ELSE, GROUPING_HEADER, GROUPING_TAIL,

	NEWPAGE, NOTE, DIVIDER,
	
	ENGLOBER,

	PARTICIPANT_HEAD, PARTICIPANT_LINE, PARTICIPANT_TAIL,

	TITLE, SIGNATURE;

	public ComponentType getDotted() {
		if (this == ComponentType.ARROW) {
			return ComponentType.DOTTED_ARROW;
		} else if (this == ComponentType.DOTTED_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.DOTTED_SELF_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.RETURN_ARROW) {
			return ComponentType.RETURN_DOTTED_ARROW;
		} else if (this == ComponentType.RETURN_DOTTED_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.SELF_ARROW) {
			return ComponentType.DOTTED_SELF_ARROW;
		} else if (this == ComponentType.ASYNC_ARROW) {
			return ComponentType.ASYNC_DOTTED_ARROW;
		} else if (this == ComponentType.ASYNC_DOTTED_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.ASYNC_RETURN_ARROW) {
			return ComponentType.ASYNC_RETURN_DOTTED_ARROW;
		} else if (this == ComponentType.ASYNC_RETURN_DOTTED_ARROW) {
			throw new IllegalStateException();
		}
		throw new UnsupportedOperationException();
	}
	
	public ComponentType getAsync() {
		if (this == ComponentType.ARROW) {
			return ComponentType.ASYNC_ARROW;
		} else if (this == ComponentType.DOTTED_ARROW) {
			return ComponentType.ASYNC_DOTTED_ARROW;
		} else if (this == ComponentType.DOTTED_SELF_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.RETURN_ARROW) {
			return ComponentType.ASYNC_RETURN_ARROW;
		} else if (this == ComponentType.RETURN_DOTTED_ARROW) {
			return ComponentType.ASYNC_RETURN_DOTTED_ARROW;
		} else if (this == ComponentType.SELF_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.ASYNC_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.ASYNC_DOTTED_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.ASYNC_RETURN_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.ASYNC_RETURN_DOTTED_ARROW) {
			throw new IllegalStateException();
		}
		throw new UnsupportedOperationException();
	}

	public ComponentType getReverse() {
		if (this == ComponentType.ARROW) {
			return ComponentType.RETURN_ARROW;
		} else if (this == ComponentType.DOTTED_ARROW) {
			return ComponentType.RETURN_DOTTED_ARROW;
		} else if (this == ComponentType.DOTTED_SELF_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.RETURN_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.RETURN_DOTTED_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.SELF_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.ASYNC_ARROW) {
			return ComponentType.ASYNC_RETURN_ARROW;
		} else if (this == ComponentType.ASYNC_DOTTED_ARROW) {
			return ComponentType.ASYNC_RETURN_DOTTED_ARROW;
		} else if (this == ComponentType.ASYNC_RETURN_ARROW) {
			throw new IllegalStateException();
		} else if (this == ComponentType.ASYNC_RETURN_DOTTED_ARROW) {
			throw new IllegalStateException();
		}
		throw new UnsupportedOperationException();
	}

}
