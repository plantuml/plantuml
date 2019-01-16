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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;

public class ConstraintSet {

	private final ParticipantBoxSimple firstBorder;

	private final ParticipantBoxSimple lastborder;

	final private List<Pushable> participantList = new ArrayList<Pushable>();
	final private Map<List<Pushable>, Constraint> constraints = new HashMap<List<Pushable>, Constraint>();

	public ConstraintSet(Collection<? extends Pushable> all, double freeX) {
		this.participantList.add(firstBorder = new ParticipantBoxSimple(0, "LEFT"));
		this.participantList.addAll(all);
		this.participantList.add(lastborder = new ParticipantBoxSimple(freeX, "RIGHT"));
	}

	@Override
	public String toString() {
		return constraints.values().toString();
	}

	public double getMaxX() {
		return lastborder.getCenterX(null);
	}

	public Constraint getConstraint(Pushable p1, Pushable p2) {
		if (p1 == null || p2 == null || p1 == p2) {
			throw new IllegalArgumentException();
		}
		final int i1 = participantList.indexOf(p1);
		final int i2 = participantList.indexOf(p2);
		if (i1 == -1 || i2 == -1) {
			throw new IllegalArgumentException();
		}
		if (i1 > i2) {
			return getConstraint(p2, p1);
		}
		final List<Pushable> key = Arrays.asList(p1, p2);
		Constraint result = constraints.get(key);
		if (result == null) {
			result = new Constraint(p1, p2);
			constraints.put(key, result);
		}
		return result;
	}

	public Constraint getConstraintAfter(Pushable p1) {
		if (p1 == null) {
			throw new IllegalArgumentException();
		}
		return getConstraint(p1, getNext(p1));
	}

	public Constraint getConstraintBefore(Pushable p1) {
		if (p1 == null) {
			throw new IllegalArgumentException();
		}
		return getConstraint(p1, getPrevious(p1));
	}

	public Pushable getPrevious(Pushable p) {
		return getOtherParticipant(p, -1);
	}

	public Pushable getNext(Pushable p) {
		return getOtherParticipant(p, 1);
	}

	private Pushable getOtherParticipant(Pushable p, int delta) {
		final int i = participantList.indexOf(p);
		if (i == -1) {
			throw new IllegalArgumentException();
		}
		return participantList.get(i + delta);
	}

	public void takeConstraintIntoAccount(StringBounder stringBounder) {
		for (int dist = 1; dist < participantList.size(); dist++) {
			pushEverybody(stringBounder, dist);
		}
	}

	private void pushEverybody(StringBounder stringBounder, int dist) {
		for (int i = 0; i < participantList.size() - dist; i++) {
			final Pushable p1 = participantList.get(i);
			final Pushable p2 = participantList.get(i + dist);
			final Constraint c = getConstraint(p1, p2);
			ensureSpaceAfter(stringBounder, p1, p2, c.getValue());
		}
	}

	public void pushToLeftParticipantBox(double deltaX, Pushable firstToChange, boolean including) {
		if (deltaX <= 0) {
			throw new IllegalArgumentException();
		}
		if (firstToChange == null) {
			throw new IllegalArgumentException();
		}
		// freeX += deltaX;
		boolean founded = false;
		for (Pushable box : participantList) {
			if (box.equals(firstToChange)) {
				founded = true;
				if (including == false) {
					continue;
				}
			}
			if (founded) {
				box.pushToLeft(deltaX);
			}
		}
	}

	public void pushToLeft(double delta) {
		pushToLeftParticipantBox(delta, firstBorder, true);
	}

	private void ensureSpaceAfter(StringBounder stringBounder, Pushable p1, Pushable p2, double space) {
		if (p1.equals(p2)) {
			throw new IllegalArgumentException();
		}
		if (p1.getCenterX(stringBounder) > p2.getCenterX(stringBounder)) {
			ensureSpaceAfter(stringBounder, p2, p1, space);
			return;
		}
		assert p1.getCenterX(stringBounder) < p2.getCenterX(stringBounder);
		final double existingSpace = p2.getCenterX(stringBounder) - p1.getCenterX(stringBounder);
		if (existingSpace < space) {
			pushToLeftParticipantBox(space - existingSpace, p2, true);
		}

	}

	public final Pushable getFirstBorder() {
		return firstBorder;
	}

	public final Pushable getLastborder() {
		return lastborder;
	}

}
