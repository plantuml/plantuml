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
package net.sourceforge.plantuml.sequencediagram;

public enum MessageExoType {
	FROM_LEFT, TO_LEFT, FROM_RIGHT, TO_RIGHT;

	public int getDirection() {
		switch (this) {
		case FROM_LEFT:
			return 1;
		case TO_LEFT:
			return -1;
		case TO_RIGHT:
			return 1;
		case FROM_RIGHT:
			return -1;
		}
		throw new IllegalStateException();
	}

	public boolean isLeftBorder() {
		return this == MessageExoType.FROM_LEFT || this == MessageExoType.TO_LEFT;
	}

	public boolean isRightBorder() {
		return this == MessageExoType.FROM_RIGHT || this == MessageExoType.TO_RIGHT;
	}

	public MessageExoType reverse() {
		switch (this) {
		case FROM_LEFT:
			return TO_LEFT;
		case TO_RIGHT:
			return FROM_RIGHT;
		case FROM_RIGHT:
			return TO_RIGHT;
		case TO_LEFT:
			return FROM_LEFT;
		}
		throw new IllegalStateException();
	}

}
