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
package net.sourceforge.plantuml.project;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class GanttArrow implements UDrawable {

	private final TimeScale timeScale;
	private final Direction atStart;
	private final TaskInstant source;
	private final Direction atEnd;
	private final TaskInstant dest;
	private final HColor color;
	private final LinkType style;
	private final ToTaskDraw toTaskDraw;

	public GanttArrow(TimeScale timeScale, TaskInstant source, TaskInstant dest, HColor color, LinkType style,
			ToTaskDraw toTaskDraw) {
		this.toTaskDraw = toTaskDraw;
		this.style = style;
		this.color = color;
		this.timeScale = timeScale;
		this.source = source;
		this.dest = dest;
		if (source.getAttribute() == TaskAttribute.END && dest.getAttribute() == TaskAttribute.START) {
			this.atStart = source.sameRowAs(dest) ? Direction.LEFT : Direction.DOWN;
			this.atEnd = Direction.RIGHT;
		} else if (source.getAttribute() == TaskAttribute.END && dest.getAttribute() == TaskAttribute.END) {
			this.atStart = Direction.RIGHT;
			this.atEnd = Direction.LEFT;
		} else if (source.getAttribute() == TaskAttribute.START && dest.getAttribute() == TaskAttribute.START) {
			this.atStart = Direction.LEFT;
			this.atEnd = Direction.RIGHT;
		} else if (source.getAttribute() == TaskAttribute.START && dest.getAttribute() == TaskAttribute.END) {
			this.atStart = source.sameRowAs(dest) ? Direction.RIGHT : Direction.DOWN;
			this.atEnd = Direction.LEFT;
		} else {
			throw new IllegalArgumentException();
		}
	}

	private TaskDraw getSource() {
		return toTaskDraw.getTaskDraw((Task) source.getMoment());
	}

	private TaskDraw getDestination() {
		return toTaskDraw.getTaskDraw((Task) dest.getMoment());
	}

	public void drawU(UGraphic ug) {
		// ug = ug.apply(color.bg()).apply(color).apply(new UStroke(1.5));
		ug = ug.apply(color.bg()).apply(color).apply(style.getStroke3(new UStroke(1.5)));

		double x1 = getX(source.withDelta(0), atStart);
		double y1 = getSource().getY(atStart);

		final double x2 = getX(dest, atEnd.getInv());
		final double y2 = getDestination().getY(atEnd);

		if (atStart == Direction.DOWN && y2 < y1) {
			y1 = getSource().getY(atStart.getInv());
		}

		if (this.atStart == Direction.DOWN && this.atEnd == Direction.RIGHT) {
			if (x2 > x1) {
				if (x2 - x1 < 8) {
					x1 = x2 - 8;
				}
				drawLine(ug, x1, y1, x1, y2, x2, y2);
			} else {
				x1 = getX(source.withDelta(0), Direction.RIGHT);
				y1 = getSource().getY(Direction.RIGHT);
				drawLine(ug, x1, y1, x1 + 6, y1, x1 + 6, y1 + 8, x2 - 8, y1 + 8, x2 - 8, y2, x2, y2);
			}
		} else if (this.atStart == Direction.RIGHT && this.atEnd == Direction.LEFT) {
			final double xmax = Math.max(x1, x2) + 8;
			drawLine(ug, x1, y1, xmax, y1, xmax, y2, x2, y2);
		} else if (this.atStart == Direction.LEFT && this.atEnd == Direction.RIGHT) {
			final double xmin = Math.min(x1, x2) - 8;
			drawLine(ug, x1, y1, xmin, y1, xmin, y2, x2, y2);
		} else if (this.atStart == Direction.DOWN && this.atEnd == Direction.LEFT) {
			drawLine(ug, x1, y1, x1, y2, x2, y2);
		} else {
			throw new IllegalArgumentException();
		}

		ug = ug.apply(new UStroke(1.5));
		ug.apply(new UTranslate(x2, y2)).draw(Arrows.asTo(atEnd));

	}

	private void drawLine(UGraphic ug, double... coord) {
		for (int i = 0; i < coord.length - 2; i += 2) {
			final double x1 = coord[i];
			final double y1 = coord[i + 1];
			final double x2 = coord[i + 2];
			final double y2 = coord[i + 3];
			ug.apply(new UTranslate(x1, y1)).draw(new ULine(x2 - x1, y2 - y1));
		}

	}

	private double getX(TaskInstant when, Direction direction) {
		final double x1 = timeScale.getStartingPosition(when.getInstantTheorical());
		final double x2 = timeScale.getEndingPosition(when.getInstantTheorical());
		if (direction == Direction.LEFT) {
			return x1;
		}
		if (direction == Direction.RIGHT) {
			return x2;
		}
		return (x1 + x2) / 2;
	}
}
