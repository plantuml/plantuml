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
package net.sourceforge.plantuml.project.draw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.project.GanttConstraint;
import net.sourceforge.plantuml.project.LabelStrategy;
import net.sourceforge.plantuml.project.ToTaskDraw;
import net.sourceforge.plantuml.project.core.GArrowType;
import net.sourceforge.plantuml.project.core.GSide;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskImpl;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.graphic.Segment;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class TaskDrawRegular extends AbstractTaskDraw {

	private final Day end;
	private final boolean oddStart;
	private final boolean oddEnd;
	private final Collection<Day> paused;
	private final Collection<GanttConstraint> constraints;

	public TaskDrawRegular(TimeScale timeScale, Real y, String prettyDisplay, Day start, Day end, boolean oddStart,
			boolean oddEnd, ISkinParam skinParam, Task task, ToTaskDraw toTaskDraw,
			Collection<GanttConstraint> constraints, StyleBuilder styleBuilder) {
		super(timeScale, y, prettyDisplay, start, task, toTaskDraw, styleBuilder, skinParam);
		this.constraints = constraints;
		this.end = end;
		this.oddStart = oddStart;
		this.oddEnd = oddEnd;
		this.paused = new TreeSet<>(((TaskImpl) task).getAllPaused());
		for (Day tmp = start; tmp.compareTo(end) <= 0; tmp = tmp.increment()) {
			final int load = ((TaskImpl) task).getDefaultPlan().getLoadAt(tmp);
			if (load == 0)
				this.paused.add(tmp);

		}
	}

	@Override
	public double getShapeHeight(StringBounder stringBounder) {
		final Style style = getStyle();
		final ClockwiseTopRightBottomLeft padding = style.getPadding();
		return padding.getTop() + getTitle().calculateDimension(stringBounder).getHeight() + padding.getBottom();
	}

	@Override
	public void drawTitle(UGraphic ug, LabelStrategy labelStrategy, double colTitles, double colBars) {
		final TextBlock title = getTitle();
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dim = title.calculateDimension(stringBounder);

		final Style style = getStyleSignature().getMergedStyle(getStyleBuilder());
		final ClockwiseTopRightBottomLeft margin = style.getMargin();
		final ClockwiseTopRightBottomLeft padding = style.getPadding();

		ug = ug.apply(UTranslate.dy(margin.getTop() + padding.getTop()));

		if (labelStrategy.titleInFirstColumn()) {
			if (labelStrategy.rightAligned())
				title.drawU(ug.apply(UTranslate.dx(colTitles - dim.getWidth() - margin.getRight())));
			else
				title.drawU(ug.apply(UTranslate.dx(margin.getLeft())));
			return;
		} else if (labelStrategy.titleInLastColumn()) {
			title.drawU(ug.apply(UTranslate.dx(colBars + margin.getLeft())));
			return;
		}

		final double pos1 = timeScale.getStartingPosition(start) + 6;
		final double pos2 = timeScale.getEndingPosition(end) - 6;
		final double pos;
		if (pos2 - pos1 > dim.getWidth())
			pos = pos1;
		else
			pos = getOutPosition(pos2);
		title.drawU(ug.apply(UTranslate.dx(pos)));
	}

	@Override
	protected TextBlock getTitle() {
		return Display.getWithNewlines(getPragma(), prettyDisplay).create(getFontConfiguration(),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
	}

	private double getOutPosition(double pos2) {
		if (isThereRightArrow())
			return pos2 + 18;

		return pos2 + 8;
	}

	private boolean isThereRightArrow() {
		for (GanttConstraint constraint : constraints)
			if (constraint.isThereRightArrow(getTask()))
				return true;

		return false;
	}

	@Override
	StyleSignature getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, SName.task)
				.withTOBECHANGED(getTask().getStereotype());
	}

	public void drawU(UGraphic ug) {
		final double startPos = timeScale.getStartingPosition(start);
		drawNote(ug.apply((new UTranslate(startPos, getYNotePosition(ug.getStringBounder())))));

		drawShape(ug);
	}

	private double getYNotePosition(StringBounder stringBounder) {
		final Style style = getStyle();
		final ClockwiseTopRightBottomLeft margin = style.getMargin();
		return margin.getTop() + getShapeHeight(stringBounder) + margin.getBottom();
	}

	@Override
	public double getHeightMax(StringBounder stringBounder) {
		if (note == null)
			return getFullHeightTask(stringBounder);

		return getYNotePosition(stringBounder) + getOpaleNote().calculateDimension(stringBounder).getHeight();
	}

	public FingerPrint getFingerPrint(StringBounder stringBounder) {
		final double h = getFullHeightTask(stringBounder);
		final double startPos = timeScale.getStartingPosition(start);
		final double endPos = timeScale.getEndingPosition(end);
		return new FingerPrint(startPos, getY(stringBounder).getCurrentValue(), endPos - startPos, h);
	}

	public FingerPrint getFingerPrintNote(StringBounder stringBounder) {
		if (note == null)
			return null;

		final XDimension2D dim = getOpaleNote().calculateDimension(stringBounder);
		final double startPos = timeScale.getStartingPosition(start);
		// final double endPos = timeScale.getEndingPosition(end);
		return new FingerPrint(startPos, getY(stringBounder).getCurrentValue() + getYNotePosition(stringBounder),
				dim.getWidth(), dim.getHeight());
	}

	private UGraphic applyColors(UGraphic ug) {
		final CenterBorderColor col = this.getColors();
		if (col != null && col.isOk())
			return col.apply(ug);

		return ug.apply(getLineColor()).apply(getBackgroundColor().bg());
	}

	@Override
	public double getX(StringBounder stringBounder, GSide side, GArrowType arrowType) {
		double x;
		if (side == GSide.LEFT)
			x = timeScale.getStartingPosition(start);
		else if (side == GSide.RIGHT)
			x = timeScale.getEndingPosition(end);
		else if (side == GSide.TOP_LEFT || side == GSide.BOTTOM_LEFT) {
			x = timeScale.getStartingPosition(start) + 8;
			if (x > timeScale.getStartingPosition(end))
				x = (timeScale.getStartingPosition(start) + timeScale.getEndingPosition(start)) / 2;
		} else if (side == GSide.TOP_RIGHT || side == GSide.BOTTOM_RIGHT) {
			x = timeScale.getEndingPosition(end) - 8;
			if (x < timeScale.getEndingPosition(start))
				x = (timeScale.getStartingPosition(end) + timeScale.getEndingPosition(end)) / 2;
		} else
			throw new IllegalArgumentException();

		if (arrowType == GArrowType.OUTGOING) {
			final Style style = getStyleSignature().getMergedStyle(getStyleBuilder());
			final ClockwiseTopRightBottomLeft margin = style.getMargin();
			if (side == GSide.LEFT)
				x += margin.getLeft();
			else if (side == GSide.RIGHT)
				x -= margin.getLeft();
		}

		return x;
	}

	public void drawShape(UGraphic ug) {
		ug = applyColors(ug);
		final Style style = getStyleSignature().getMergedStyle(getStyleBuilder());
		final ClockwiseTopRightBottomLeft margin = style.getMargin();

		final double startPos = timeScale.getStartingPosition(start) + margin.getLeft();
		final double endPos = timeScale.getEndingPosition(end) - margin.getRight();

		if (url != null)
			ug.startUrl(url);

		ug = ug.apply(UTranslate.dy(margin.getTop()));

		final StringBounder stringBounder = ug.getStringBounder();

		final double round = style.value(PName.RoundCorner).asDouble();

		final Collection<Segment> off = new ArrayList<>();
		for (Day pause : paused) {
			final double x1 = timeScale.getStartingPosition(pause);
			final double x2 = timeScale.getEndingPosition(pause);
			off.add(new Segment(x1, x2));
		}

		final HColor backUndone = StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, SName.undone)
				.getMergedStyle(getStyleBuilder()).value(PName.BackGroundColor).asColor(getColorSet());

		final RectangleTask rectangleTask = new RectangleTask(startPos, endPos, round, getCompletion(), off);

		rectangleTask.draw(ug, getShapeHeight(stringBounder), backUndone, oddStart, oddEnd);

		if (url != null)
			ug.closeUrl();

	}

}
