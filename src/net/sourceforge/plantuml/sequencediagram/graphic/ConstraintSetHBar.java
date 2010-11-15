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
 * Revision $Revision: 3836 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupableList;

class ConstraintSetHBar {

	final private List<Pushable> pushables = new ArrayList<Pushable>();
	final private List<InGroupableList> inGroupableLists = new ArrayList<InGroupableList>();

	public ConstraintSetHBar(Collection<? extends Pushable> all) {
		this.pushables.addAll(all);
	}

	public void add(InGroupableList inGroupableList) {
		this.inGroupableLists.add(inGroupableList);
	}

	public double takeConstraintIntoAccount(StringBounder stringBounder, double freeX) {
		for (InGroupableList list : inGroupableLists) {
			if (list.isEmpty()) {
				list.getBarEnd().pushToLeft(list.getMaxX(stringBounder));
				continue;
			}
			final ParticipantBox first = list.getFirstParticipantBox();
			final ParticipantBox last = list.getLastParticipantBox();
			final int idxFirst = pushables.indexOf(first);
			final int idxLast = pushables.indexOf(last);
			if (idxFirst == -1 || idxLast == -1) {
				throw new IllegalStateException();
			}
			pushables.add(idxFirst, list.getBarStart());
			pushables.add(idxLast + 2, list.getBarEnd());
			list.getBarStart().pushToLeft(list.getMinX(stringBounder));
			list.getBarEnd().pushToLeft(list.getMaxX(stringBounder));
		}

		double result = freeX;
		for (int i = 0; i < pushables.size(); i++) {
			final Pushable pushable = pushables.get(i);
			if (pushable instanceof VirtualHBar) {
				final VirtualHBar bar = (VirtualHBar) pushable;
				final int j = getVirtualNext(i);
				pushAllToLeft(j, bar.getWidth());
				result += bar.getWidth();
				i = j;
			}
		}

		for (InGroupableList list : inGroupableLists) {
			final double endBar = list.getBarEnd().getCenterX(stringBounder) + 5;
			if (endBar > result) {
				result = endBar;
			}
		}

		return result;
	}

	private int getVirtualNext(int i) {
		final VirtualHBar ref = (VirtualHBar) pushables.get(i);
		for (int j = i + 1; j < pushables.size(); j++) {
			if (pushables.get(j) instanceof VirtualHBar == false) {
				return j - 1;
			}
			final VirtualHBar other = (VirtualHBar) pushables.get(j);
			if (other.canBeOnTheSameLine(ref) == false) {
				return j - 1;
			}
		}
		return pushables.size() - 1;
	}

	private void pushAllToLeft(int afterThisOne, double deltaX) {
		for (int i = afterThisOne + 1; i < pushables.size(); i++) {
			pushables.get(i).pushToLeft(deltaX);
		}
	}

}
