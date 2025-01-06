/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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

import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.project.core.GArrowType;
import net.sourceforge.plantuml.project.core.GSide;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class GanttArrow implements UDrawable {

	private final TimeScale timeScale;
	private final GSide atStart;
	private final TaskInstant source;
	private final GSide atEnd;
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
			this.atStart = source.sameRowAs(dest) ? GSide.RIGHT : GSide.BOTTOM_RIGHT;
			this.atEnd = GSide.LEFT;
		} else if (source.getAttribute() == TaskAttribute.END && dest.getAttribute() == TaskAttribute.END) {
			this.atStart = GSide.RIGHT;
			this.atEnd = GSide.RIGHT;
		} else if (source.getAttribute() == TaskAttribute.START && dest.getAttribute() == TaskAttribute.START) {
			this.atStart = GSide.LEFT;
			this.atEnd = GSide.LEFT;
		} else if (source.getAttribute() == TaskAttribute.START && dest.getAttribute() == TaskAttribute.END) {
			this.atStart = source.sameRowAs(dest) ? GSide.LEFT : GSide.BOTTOM_LEFT;
			this.atEnd = GSide.RIGHT;
		} else {
			throw new IllegalArgumentException();
		}
	}

	private TaskDraw getSource() {
		if (source.getMoment() instanceof Task)
			return toTaskDraw.getTaskDraw((Task) source.getMoment());

		return null;

	}

	private TaskDraw getDestination() {
		if (dest.getMoment() instanceof Task)
			return toTaskDraw.getTaskDraw((Task) dest.getMoment());

		return null;
	}

	public void drawU(UGraphic ug) {
		ug = style.applyStrokeAndLineColor(ug, colorSet);

		final TaskDraw start = getSource();
		final TaskDraw end = getDestination();
		if (start == null || end == null)
			return;

		final StringBounder stringBounder = ug.getStringBounder();
		double x1 = start.getX(stringBounder, atStart, GArrowType.OUTGOING);
		double y1 = start.getY(stringBounder, atStart);

		final double x2 = end.getX(stringBounder, atEnd, GArrowType.INCOMING);
		final double y2 = end.getY(stringBounder, atEnd);

		if (this.atStart.isBottom() && y2 < y1)
			y1 = start.getY(stringBounder, atStart.reverseBottomTop());

		final double minimalWidth = 6;
		final GArrows arrows = new GArrows(this.atEnd, style.value(PName.LineColor).asColor(colorSet));

		if (this.atStart.isBottom() && this.atEnd == GSide.LEFT) {
			if (x2 > x1 + minimalWidth) {
				arrows.addPoint(x1, y1);
				arrows.addPoint(x1, y2);
				arrows.addPoint(x2, y2);

			} else {
				x1 = start.getX(stringBounder, GSide.RIGHT, GArrowType.OUTGOING);
				y1 = start.getY(stringBounder, GSide.RIGHT);
				final double y1b = end.getY(stringBounder).getCurrentValue();
				arrows.addPoint(x1, y1);
				arrows.addPoint(x1 + 6, y1);
				arrows.addPoint(x1 + 6, y1b);
				arrows.addPoint(x2 - 8, y1b);
				arrows.addPoint(x2 - 8, y2);
				arrows.addPoint(x2, y2);
			}
		} else if (this.atStart == GSide.RIGHT && this.atEnd == GSide.RIGHT) {
			final double xmax = Math.max(x1, x2) + 8;
			arrows.addPoint(x1, y1);
			arrows.addPoint(xmax, y1);
			arrows.addPoint(xmax, y2);
			arrows.addPoint(x2, y2);
		} else if (this.atStart == GSide.LEFT && this.atEnd == GSide.LEFT) {
			final double xmin = Math.min(x1, x2) - 8;
			arrows.addPoint(x1, y1);
			arrows.addPoint(xmin, y1);
			arrows.addPoint(xmin, y2);
			arrows.addPoint(x2, y2);
		} else /* if (this.atStart == Direction.DOWN && this.atEnd == Direction.LEFT) */ {
			arrows.addPoint(x1, y1);
			arrows.addPoint(x1, y2);
			arrows.addPoint(x2, y2);
//		} else {
//			throw new IllegalArgumentException();
		}

		arrows.drawU(ug);

	}

	private StyleSignatureBasic getStyleSignatureTask() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, SName.task);
	}
}
