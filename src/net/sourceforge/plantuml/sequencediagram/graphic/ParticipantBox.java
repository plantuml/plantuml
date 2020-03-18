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

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ParticipantBox implements Pushable {

	private static int CPT = 0;

	private final int outMargin;

	private double startingX;

	private final Component head;
	private final Component line;
	private final Component tail;
	private final Component delayLine;

	private int cpt = CPT++;

	public ParticipantBox(Component head, Component line, Component tail, Component delayLine, double startingX, int outMargin) {
		this.outMargin = outMargin;
		this.startingX = startingX;
		this.head = head;
		this.line = line;
		this.tail = tail;
		this.delayLine = delayLine;
	}

	@Override
	public String toString() {
		return "PB" + cpt;
	}

	public double getMinX() {
		return startingX + outMargin;
	}

	public double getMaxX(StringBounder stringBounder) {
		return startingX + head.getPreferredWidth(stringBounder) + 2 * outMargin;
	}

	public double getCenterX(StringBounder stringBounder) {
		return startingX + head.getPreferredWidth(stringBounder) / 2.0 + outMargin;
	}

	public double getHeadHeight(StringBounder stringBounder) {
		return head.getPreferredHeight(stringBounder) + line.getPreferredHeight(stringBounder) / 2.0;
	}

	public double getHeadHeightOnly(StringBounder stringBounder) {
		return head.getPreferredHeight(stringBounder);
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		return head.getPreferredWidth(stringBounder);
	}

	public double getTailHeight(StringBounder stringBounder) {
		return tail.getPreferredHeight(stringBounder) + line.getPreferredHeight(stringBounder) / 2.0;
	}

	public void pushToLeft(double deltaX) {
		startingX += deltaX;
	}

	public void drawHeadTailU(UGraphic ug, double topStartingY, boolean showHead, double positionTail) {
		if (topStartingY == 0) {
			throw new IllegalStateException("setTopStartingY cannot be zero");
		}

		// final double atX = ug.getTranslateX();
		// final double atY = ug.getTranslateY();
		final StringBounder stringBounder = ug.getStringBounder();

		if (showHead) {
			final double y1 = topStartingY - head.getPreferredHeight(stringBounder)
					- line.getPreferredHeight(stringBounder) / 2;
			head.drawU(
					ug.apply(new UTranslate(getMinX(), y1)),
					new Area(new Dimension2DDouble(head.getPreferredWidth(stringBounder), head
							.getPreferredHeight(stringBounder))), new SimpleContext2D(false));
			// ug.setTranslate(atX, atY);
		}

		if (positionTail > 0) {
			// final double y2 = positionTail - topStartingY +
			// line.getPreferredHeight(stringBounder) / 2 - 1;
			positionTail += line.getPreferredHeight(stringBounder) / 2 - 1;
			// if (y2 != y22) {
			// throw new IllegalStateException();
			// }
			ug = ug.apply(new UTranslate(getMinX(), positionTail));
			tail.drawU(
					ug,
					new Area(new Dimension2DDouble(tail.getPreferredWidth(stringBounder), tail
							.getPreferredHeight(stringBounder))), new SimpleContext2D(false));
			// ug.setTranslate(atX, atY);
		}
	}

	public void drawParticipantHead(UGraphic ug) {
		// ug.translate(outMargin, 0);
		final StringBounder stringBounder = ug.getStringBounder();
		head.drawU(
				ug.apply(UTranslate.dx(outMargin)),
				new Area(new Dimension2DDouble(head.getPreferredWidth(stringBounder), head
						.getPreferredHeight(stringBounder))), new SimpleContext2D(false));
		// ug.translate(-outMargin, 0);
	}

	public void drawLineU22(UGraphic ug, double startingY, final double endingY, boolean showTail, double myDelta) {
		ug = ug.apply(UTranslate.dx(startingX));
		if (delays.size() > 0) {
			final StringBounder stringBounder = ug.getStringBounder();
			for (GraphicalDelayText delay : delays) {
				if (delay.getStartingY() - myDelta >= startingY) {
					drawLineIfLowerThan(ug, startingY, delay.getStartingY() - myDelta, line, endingY);
					drawLineIfLowerThan(ug, delay.getStartingY() - myDelta, delay.getEndingY(stringBounder) - myDelta,
							delayLine, endingY);
					startingY = delay.getEndingY(stringBounder) - myDelta;
				}
			}
			if (delays.get(delays.size() - 1).getEndingY(stringBounder) - myDelta > startingY) {
				startingY = delays.get(delays.size() - 1).getEndingY(stringBounder) - myDelta;
			}
		}
		drawLineIfLowerThan(ug, startingY, endingY, line, endingY);
	}

	private void drawLineIfLowerThan(UGraphic ug, double startingY, double endingY, Component comp, double limitY) {
		startingY = Math.min(startingY, limitY);
		endingY = Math.min(endingY, limitY);
		if (startingY < limitY || endingY < limitY) {
			drawLine(ug, startingY, endingY, comp);
		}

	}

	private void drawLine(UGraphic ug, double startingY, double endingY, Component comp) {
		final StringBounder stringBounder = ug.getStringBounder();
		comp.drawU(ug.apply(UTranslate.dy(startingY)),
				new Area(new Dimension2DDouble(head.getPreferredWidth(stringBounder) + outMargin * 2, endingY
						- startingY)), new SimpleContext2D(false));
	}

	public double magicMargin(StringBounder stringBounder) {
		return line.getPreferredHeight(stringBounder) / 2;
	}

	public double getStartingX() {
		return startingX;
	}

	private final List<GraphicalDelayText> delays = new ArrayList<GraphicalDelayText>();

	public void addDelay(GraphicalDelayText delay) {
		this.delays.add(delay);
	}

	public Collection<Segment> getDelays(final StringBounder stringBounder) {
		return new AbstractCollection<Segment>() {

			@Override
			public Iterator<Segment> iterator() {
				return new Iterator<Segment>() {

					private final Iterator<GraphicalDelayText> it = delays.iterator();

					public boolean hasNext() {
						return it.hasNext();
					}

					public Segment next() {
						final GraphicalDelayText d = it.next();
						return new Segment(d.getStartingY(), d.getEndingY(stringBounder));
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

			@Override
			public int size() {
				return delays.size();
			}
		};
	}

}
