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
 * Revision $Revision: 3828 $
 *
 */
package net.sourceforge.plantuml.activitydiagram2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;

public class ConditionalContext2 {

	private IEntity pending;
	private final IEntity branch;
	private final Direction direction;
	private final ConditionalContext2 parent;

	public ConditionalContext2(ConditionalContext2 parent, IEntity branch, Direction direction) {
		if (branch.getType() != EntityType.BRANCH) {
			throw new IllegalArgumentException();
		}
		this.branch = branch;
		this.direction = direction;
		this.parent = parent;
		this.pending = branch;
	}

	public Direction getDirection() {
		return direction;
	}

	public final ConditionalContext2 getParent() {
		return parent;
	}

	public final IEntity getPending() {
		return pending;
	}

	public final void setPending(IEntity pending) {
		this.pending = pending;
	}

	public final IEntity getBranch() {
		return branch;
	}

}
