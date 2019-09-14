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
package net.sourceforge.plantuml.activitydiagram;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;

public class ConditionalContext {

	private final IEntity branch;
	private final Direction direction;
	private final ConditionalContext parent;

	public ConditionalContext(ConditionalContext parent, IEntity branch, Direction direction) {
		if (branch == null) {
			throw new IllegalArgumentException("branch is null");
		}
		if (branch.getLeafType() != LeafType.BRANCH) {
			throw new IllegalArgumentException();
		}
		this.branch = branch;
		this.direction = direction;
		this.parent = parent;
	}

	public Direction getDirection() {
		return direction;
	}

	public final ConditionalContext getParent() {
		return parent;
	}

	public final IEntity getBranch() {
		return branch;
	}

}
