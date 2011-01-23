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
 * Revision $Revision: 6016 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class ParticipantBox implements Pushable {

	private static int CPT = 0;

	private final int outMargin = 5;

	private double startingX;

	private final Component head;
	private final Component line;
	private final Component tail;
	private final Component delayLine;
	private int cpt = CPT++;

	public ParticipantBox(Component head, Component line, Component tail, Component delayLine, double startingX) {
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

	public double getMaxX(StringBounder stringBounder) {
		return startingX + head.getPreferredWidth(stringBounder) + 2 * outMargin;
	}

	public double getCenterX(StringBounder stringBounder) {
		return startingX + head.getPreferredWidth(stringBounder) / 2.0 + outMargin;
	}

	public double getHeadHeight(StringBounder stringBounder) {
		return head.getPreferredHeight(stringBounder) + line.getPreferredHeight(stringBounder) / 2.0;
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

		final double atX = ug.getTranslateX();
		final double atY = ug.getTranslateY();
		final StringBounder stringBounder = ug.getStringBounder();

		if (showHead) {
			final double y1 = topStartingY - head.getPreferredHeight(stringBounder)
					- line.getPreferredHeight(stringBounder) / 2;
			ug.translate(startingX + outMargin, y1);
			head.drawU(
					ug,
					new Dimension2DDouble(head.getPreferredWidth(stringBounder), head.getPreferredHeight(stringBounder)),
					new SimpleContext2D(false));
			ug.setTranslate(atX, atY);
		}

		if (positionTail > 0) {
			// final double y2 = positionTail - topStartingY +
			// line.getPreferredHeight(stringBounder) / 2 - 1;
			positionTail += line.getPreferredHeight(stringBounder) / 2 - 1;
			// if (y2 != y22) {
			// throw new IllegalStateException();
			// }
			ug.translate(startingX + outMargin, positionTail);
			tail.drawU(
					ug,
					new Dimension2DDouble(tail.getPreferredWidth(stringBounder), tail.getPreferredHeight(stringBounder)),
					new SimpleContext2D(false));
			ug.setTranslate(atX, atY);
		}
	}

	public void drawParticipantHead(UGraphic ug) {
		ug.translate(outMargin, 0);
		final StringBounder stringBounder = ug.getStringBounder();
		head.drawU(ug,
				new Dimension2DDouble(head.getPreferredWidth(stringBounder), head.getPreferredHeight(stringBounder)),
				new SimpleContext2D(false));
		ug.translate(-outMargin, 0);
	}

	public void drawLineU(UGraphic ug, double startingY, double endingY, boolean showTail) {
		final double atX = ug.getTranslateX();
		final double atY = ug.getTranslateY();

		ug.translate(startingX, 0);
		if (delays.size() > 0) {
			final StringBounder stringBounder = ug.getStringBounder();
			for (GraphicalDelayText delay : delays) {
				drawLine(ug, startingY, delay.getStartingY(), line);
				drawLine(ug, delay.getStartingY(), delay.getEndingY(stringBounder), delayLine);
				startingY = delay.getEndingY(stringBounder);
			}
			drawLine(ug, delays.get(delays.size() - 1).getEndingY(stringBounder), endingY, line);
		} else {
			drawLine(ug, startingY, endingY, line);
		}

		ug.setTranslate(atX, atY);
	}

	private void drawLine(UGraphic ug, double startingY, double endingY, Component comp) {
		final double atY = ug.getTranslateY();
		ug.translate(0, startingY);
		final StringBounder stringBounder = ug.getStringBounder();
		comp.drawU(ug,
				new Dimension2DDouble(head.getPreferredWidth(stringBounder) + outMargin * 2, endingY - startingY),
				new SimpleContext2D(false));
		ug.setTranslate(ug.getTranslateX(), atY);
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

	public Collection<GraphicalDelayText> getDelays() {
		return Collections.unmodifiableCollection(delays);
	}

}
