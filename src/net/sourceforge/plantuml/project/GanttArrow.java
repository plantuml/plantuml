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
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class GanttArrow implements UDrawable {

	private final TimeScale timeScale;
	private final Direction atStart;
	private final TaskInstant source;
	private final Direction atEnd;
	private final TaskInstant dest;

	private final HColorSet colorSet;
	private final Style style;
	private final ToTaskDraw toTaskDraw;
	private final StyleBuilder styleBuilder;

	public GanttArrow(HColorSet colorSet, Style style, TimeScale timeScale, TaskInstant source, TaskInstant dest,
			ToTaskDraw toTaskDraw, StyleBuilder styleBuilder) {
		this.styleBuilder = styleBuilder;
		this.toTaskDraw = toTaskDraw;
		this.style = style;
		this.colorSet = colorSet;
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
		ug = style.applyStrokeAndLineColor(ug, colorSet, styleBuilder.getSkinParam().getThemeStyle());

		double x1 = getX(source.getAttribute(), getSource(), atStart);
		final StringBounder stringBounder = ug.getStringBounder();
		double y1 = getSource().getY(stringBounder, atStart);

		final double x2 = getX(dest.getAttribute(), getDestination(), atEnd.getInv());
		final double y2 = getDestination().getY(stringBounder, atEnd);

		if (atStart == Direction.DOWN && y2 < y1) {
			y1 = getSource().getY(stringBounder, atStart.getInv());
		}

		final double minimalWidth = 8;
//		final Style style = getStyleSignatureTask().getMergedStyle(styleBuilder);
//		final ClockwiseTopRightBottomLeft margin = style.getMargin();
//		final ClockwiseTopRightBottomLeft padding = style.getPadding();

		if (this.atStart == Direction.DOWN && this.atEnd == Direction.RIGHT) {
			if (x2 > x1) {
				if (x2 - x1 < minimalWidth) {
					x1 = x2 - minimalWidth;
				}
				drawLine(ug, x1, y1, x1, y2, x2, y2);
			} else {
				x1 = getX(source.getAttribute(), getSource(), Direction.RIGHT);
				y1 = getSource().getY(stringBounder, Direction.RIGHT);
				final double y1b = getDestination().getY(stringBounder).getCurrentValue();
				drawLine(ug, x1, y1, x1 + 6, y1, x1 + 6, y1b, x2 - 8, y1b, x2 - 8, y2, x2, y2);
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

		ug = ug.apply(new UStroke(1.5)).apply(
				style.value(PName.LineColor).asColor(styleBuilder.getSkinParam().getThemeStyle(), colorSet).bg());
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

	private StyleSignature getStyleSignatureTask() {
		return StyleSignature.of(SName.root, SName.element, SName.ganttDiagram, SName.task);
	}

	private double getX(TaskAttribute taskAttribute, TaskDraw task, Direction direction) {
		if (direction == Direction.LEFT) {
			return task.getX1(taskAttribute) - 1;
		}
		if (direction == Direction.RIGHT) {
			return task.getX2(taskAttribute) + 1;
		}
		return (task.getX1(taskAttribute) + (task.getX2(taskAttribute))) / 2;
	}
}
