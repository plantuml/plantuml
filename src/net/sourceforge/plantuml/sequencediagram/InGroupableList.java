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

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.graphic.LivingParticipantBox;
import net.sourceforge.plantuml.sequencediagram.graphic.ParticipantBox;
import net.sourceforge.plantuml.sequencediagram.graphic.VirtualHBar;
import net.sourceforge.plantuml.sequencediagram.graphic.VirtualHBarType;

public class InGroupableList implements InGroupable {

	//public static boolean NEW_METHOD = true;

	private final GroupingStart groupingStart;
	private final Set<InGroupable> inGroupables = new HashSet<InGroupable>();
	private final VirtualHBar barStart;
	private final VirtualHBar barEnd;

	private double minWidth;

	public InGroupableList(GroupingStart groupingStart, double startingY) {
		this.groupingStart = groupingStart;
		this.barStart = new VirtualHBar(10, VirtualHBarType.START, startingY);
		this.barEnd = new VirtualHBar(10, VirtualHBarType.END, startingY);
	}

	public final void setEndingY(double endingY) {
		this.barStart.setEndingY(endingY);
		this.barEnd.setEndingY(endingY);
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

	public double getMinX(StringBounder stringBounder) {
		if (inGroupables.size() == 0) {
			return 0;
		}
		double result = Double.MAX_VALUE;
		for (InGroupable in : inGroupables) {
			final double v = in.getMinX(stringBounder);
			if (v < result) {
				result = v;
			}
		}
		return result;
	}

	public double getMaxX(StringBounder stringBounder) {
		if (inGroupables.size() == 0) {
			return minWidth;
		}
		double result = 0;
		for (InGroupable in : inGroupables) {
			final double v = in.getMaxX(stringBounder);
			if (v > result) {
				result = v;
			}
		}
		final double minX = getMinX(stringBounder);
		if (result < minX + minWidth) {
			return minX + minWidth;
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

	// public void pushAllToLeft(double delta) {
	// for (InGroupable in : inGroupables) {
	// System.err.println("in=" + in);
	// if (in instanceof LivingParticipantBox) {
	// final ParticipantBox participantBox = ((LivingParticipantBox)
	// in).getParticipantBox();
	// System.err.println("PUSHING " + participantBox + " " + delta);
	// participantBox.pushToLeft(delta);
	// }
	// }
	// }
	//
	// public final double getBarStartX(StringBounder stringBounder) {
	// return getMinX(stringBounder) - barStart.getWidth() / 2;
	// }

	public final VirtualHBar getBarStart() {
		return barStart;
	}

	public final VirtualHBar getBarEnd() {
		return barEnd;
	}

}
