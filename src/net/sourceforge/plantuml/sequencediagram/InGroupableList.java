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
 * Revision $Revision: 4259 $
 *
 */
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.graphic.LivingParticipantBox;
import net.sourceforge.plantuml.sequencediagram.graphic.MessageExoArrow;
import net.sourceforge.plantuml.sequencediagram.graphic.ParticipantBox;

public class InGroupableList implements InGroupable {

	private static final int MARGIN5 = 5;
	public static final int MARGIN10 = 10;

	private final GroupingStart groupingStart;
	private final Set<InGroupable> inGroupables = new HashSet<InGroupable>();

	private double minWidth;

	public List<InGroupableList> getInnerList() {
		final List<InGroupableList> result = new ArrayList<InGroupableList>();
		for (InGroupable i : inGroupables) {
			if (i instanceof InGroupableList) {
				result.add((InGroupableList) i);
			}
		}
		return result;
	}

	public InGroupableList(GroupingStart groupingStart, double startingY) {
		this.groupingStart = groupingStart;
	}

	public void addInGroupable(InGroupable in) {
		this.inGroupables.add(in);
	}

	public boolean isEmpty() {
		return inGroupables.isEmpty();
	}

	@Override
	public String toString() {
		return "GS " + groupingStart + " " + inGroupables.toString();
	}

	public String toString(StringBounder stringBounder) {
		final StringBuilder sb = new StringBuilder("GS " + groupingStart + " ");
		for (InGroupable in : inGroupables) {
			sb.append(in.toString(stringBounder));
			sb.append(' ');
		}
		return sb.toString();
	}

	private InGroupable getMin(StringBounder stringBounder) {
		InGroupable result = null;
		for (InGroupable in : inGroupables) {
			if (result == null || in.getMinX(stringBounder) < result.getMinX(stringBounder)) {
				result = in;
			}
		}
		return result;
	}

	private InGroupable getMax(StringBounder stringBounder) {
		InGroupable result = null;
		for (InGroupable in : inGroupables) {
			if (result == null || in.getMaxX(stringBounder) > result.getMaxX(stringBounder)) {
				result = in;
			}
		}
		return result;
	}

	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
	}

	public ParticipantBox getFirstParticipantBox() {
		ParticipantBox first = null;
		for (InGroupable in : inGroupables) {
			if (in instanceof LivingParticipantBox) {
				final ParticipantBox participantBox = ((LivingParticipantBox) in).getParticipantBox();
				if (first == null || participantBox.getStartingX() < first.getStartingX()) {
					first = participantBox;
				}
			}
		}
		return first;
	}

	public ParticipantBox getLastParticipantBox() {
		ParticipantBox last = null;
		for (InGroupable in : inGroupables) {
			if (in instanceof LivingParticipantBox) {
				final ParticipantBox participantBox = ((LivingParticipantBox) in).getParticipantBox();
				if (last == null || participantBox.getStartingX() > last.getStartingX()) {
					last = participantBox;
				}
			}
		}
		return last;
	}

	public double getMinX(StringBounder stringBounder) {
		final InGroupable min = getMin(stringBounder);
		if (min == null) {
			return MARGIN10 + MARGIN5;
		}
		double m = min.getMinX(stringBounder);
		if (min instanceof MessageExoArrow
				&& (((MessageExoArrow) min).getType() == MessageExoType.FROM_LEFT || ((MessageExoArrow) min).getType() == MessageExoType.TO_LEFT)) {
			m += 3;
		} else if (min instanceof InGroupableList) {
			m -= MARGIN10;
		} else {
			m -= MARGIN5;
		}
		return m;
	}

	public double getMaxX(StringBounder stringBounder) {
		final double min = getMinX(stringBounder);
		final double max = getMaxXInternal(stringBounder);
		assert max - min >= 0;
		if (max - min < minWidth) {
			return min + minWidth;
		}
		return max;
	}

	private final double getMaxXInternal(StringBounder stringBounder) {
		final InGroupable max = getMax(stringBounder);
		if (max == null) {
			return MARGIN10 + MARGIN5 + minWidth;
		}
		double m = max.getMaxX(stringBounder);
		if (max instanceof MessageExoArrow
				&& (((MessageExoArrow) max).getType() == MessageExoType.FROM_RIGHT || ((MessageExoArrow) max).getType() == MessageExoType.TO_RIGHT)) {
			m -= 3;
		} else if (max instanceof InGroupableList) {
			m += MARGIN10;
		} else {
			m += MARGIN5;
		}
		return m;
	}

}
