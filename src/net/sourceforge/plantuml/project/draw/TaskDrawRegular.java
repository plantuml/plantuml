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
package net.sourceforge.plantuml.project.draw;

import java.awt.geom.Dimension2D;
import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.Parser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.GanttConstraint;
import net.sourceforge.plantuml.project.ToTaskDraw;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskImpl;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.image.Opale;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class TaskDrawRegular extends AbstractTaskDraw {

	private final Day end;
	private final boolean oddStart;
	private final boolean oddEnd;
	private final Collection<Day> paused;
	private final Collection<GanttConstraint> constraints;

	private final double margin = 2;

	public TaskDrawRegular(TimeScale timeScale, double y, String prettyDisplay, Day start, Day end, boolean oddStart,
			boolean oddEnd, ISkinParam skinParam, Task task, ToTaskDraw toTaskDraw,
			Collection<GanttConstraint> constraints) {
		super(timeScale, y, prettyDisplay, start, skinParam, task, toTaskDraw);
		this.constraints = constraints;
		this.end = end;
		this.oddStart = oddStart;
		this.oddEnd = oddEnd;
		this.paused = new TreeSet<Day>(((TaskImpl) task).getAllPaused());
		for (Day tmp = start; tmp.compareTo(end) <= 0; tmp = tmp.increment()) {
			final int load = toTaskDraw.getDefaultPlan().getLoadAt(tmp);
			if (load == 0) {
				this.paused.add(tmp);
			}

		}
	}

	public void drawTitle(UGraphic ug) {
		final TextBlock title = Display.getWithNewlines(prettyDisplay).create(getFontConfiguration(),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		final Dimension2D dim = title.calculateDimension(ug.getStringBounder());
		final double h = (margin + getShapeHeight() - dim.getHeight()) / 2;
		final double pos1 = timeScale.getStartingPosition(start) + 6;
		final double pos2 = timeScale.getEndingPosition(end) - 6;
		final double pos;
		if (pos2 - pos1 > dim.getWidth())
			pos = pos1;
		else
			pos = getOutPosition(pos2);
		title.drawU(ug.apply(new UTranslate(pos, h)));
	}

	private double getOutPosition(double pos2) {
		if (isThereRightArrow()) {
			return pos2 + 18;
		}
		return pos2 + 8;
	}

	private boolean isThereRightArrow() {
		for (GanttConstraint constraint : constraints) {
			if (constraint.isThereRightArrow(getTask())) {
				return true;
			}
		}
		return false;
	}

//		final UFont font = UFont.serif(11);
//		return new FontConfiguration(font, HColorUtils.BLACK, HColorUtils.BLACK, false);

	@Override
	protected Style getStyle() {
		final Style style = StyleSignature.of(SName.root, SName.element, SName.ganttDiagram, SName.task)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		return style;
	}

	public void drawU(UGraphic ug) {
		final double startPos = timeScale.getStartingPosition(start);
		drawNote(ug.apply((new UTranslate(startPos + margin, getYNotePosition()))));

		ug = applyColors(ug).apply(new UTranslate(margin, margin));
		drawShape(ug);
	}

	private double getYNotePosition() {
		return getShapeHeight() + margin * 3;
	}

	private void drawNote(UGraphic ug) {
		if (note == null) {
			return;
		}
		getOpaleNote().drawU(ug);

	}

	public double getHeightMax(StringBounder stringBounder) {
		if (note == null) {
			return getHeightTask();
		}
		return getYNotePosition() + getOpaleNote().calculateDimension(stringBounder).getHeight();
	}

	private Opale getOpaleNote() {
		final Style style = StyleSignature.of(SName.root, SName.element, SName.ganttDiagram, SName.note)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		FontConfiguration fc = new FontConfiguration(skinParam, style);

		final Sheet sheet = Parser
				.build(fc, skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT), skinParam, CreoleMode.FULL)
				.createSheet(note);
		final SheetBlock1 sheet1 = new SheetBlock1(sheet, LineBreakStrategy.NONE, skinParam.getPadding());

		final HColor noteBackgroundColor = style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
		final HColor borderColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final double shadowing = style.value(PName.Shadowing).asDouble();

		Opale opale = new Opale(shadowing, borderColor, noteBackgroundColor, sheet1, false);
		return opale;
	}

	public FingerPrint getFingerPrint() {
		final double h = getHeightTask();
		final double startPos = timeScale.getStartingPosition(start);
		final double endPos = timeScale.getEndingPosition(end);
		return new FingerPrint(startPos, getY(), endPos - startPos, h);
	}

	public FingerPrint getFingerPrintNote(StringBounder stringBounder) {
		if (note == null) {
			return null;
		}
		final Dimension2D dim = getOpaleNote().calculateDimension(stringBounder);
		final double startPos = timeScale.getStartingPosition(start);
		// final double endPos = timeScale.getEndingPosition(end);
		return new FingerPrint(startPos, getY() + getYNotePosition(), dim.getWidth(), dim.getHeight());
	}

	private UGraphic applyColors(UGraphic ug) {
		if (colors != null && colors.isOk()) {
			return colors.apply(ug);
		}
		return ug.apply(getLineColor()).apply(getBackgroundColor().bg());
	}

	private void drawShape(UGraphic ug) {
		final double startPos = timeScale.getStartingPosition(start);
		final double endPos = timeScale.getEndingPosition(end);

		double fullLength = endPos - startPos - 2 * margin;
		if (fullLength < 3) {
			fullLength = 3;
		}
		if (url != null) {
			ug.startUrl(url);
		}
		if (oddStart && !oddEnd) {
			ug.apply(UTranslate.dx(startPos)).draw(PathUtils.UtoRight(fullLength, getShapeHeight()));
		} else if (!oddStart && oddEnd) {
			ug.apply(UTranslate.dx(startPos)).draw(PathUtils.UtoLeft(fullLength, getShapeHeight()));
		} else {
			final URectangle full = new URectangle(fullLength, getShapeHeight()).rounded(8);
			if (completion == 100) {
				ug.apply(UTranslate.dx(startPos)).draw(full);
			} else {
				final double partialLength = fullLength * completion / 100.;
				ug.apply(UTranslate.dx(startPos)).apply(HColorUtils.WHITE).apply(HColorUtils.WHITE.bg()).draw(full);
				if (partialLength > 2) {
					final URectangle partial = new URectangle(partialLength, getShapeHeight()).rounded(8);
					ug.apply(UTranslate.dx(startPos)).apply(new HColorNone()).draw(partial);
				}
				if (partialLength > 10 && partialLength < fullLength - 10) {
					final URectangle patch = new URectangle(8, getShapeHeight());
					ug.apply(UTranslate.dx(startPos)).apply(new HColorNone()).apply(UTranslate.dx(partialLength - 8))
							.draw(patch);
				}
				ug.apply(UTranslate.dx(startPos)).apply(new HColorNone().bg()).draw(full);
			}
		}
		if (url != null) {
			ug.closeUrl();
		}
		Day begin = null;
		for (Day pause : paused) {
			if (paused.contains(pause.increment())) {
				if (begin == null)
					begin = pause;
			} else {
				if (begin == null)
					drawPause(ug, pause, pause);
				else
					drawPause(ug, begin, pause);
				begin = null;
			}
		}
	}

	private void drawPause(UGraphic ug, Day start1, Day end) {
		final double x1 = timeScale.getStartingPosition(start1);
		final double x2 = timeScale.getEndingPosition(end);
		final URectangle small = new URectangle(x2 - x1 - 1, getShapeHeight() + 1);
		final ULine line = ULine.hline(x2 - x1 - 1);
		ug = ug.apply(UTranslate.dx(x1 - 1));
		ug.apply(HColorUtils.WHITE).apply(HColorUtils.WHITE.bg()).draw(small);
		final UGraphic ugLine = ug.apply(new UStroke(2, 3, 1));
		ugLine.draw(line);
		ugLine.apply(UTranslate.dy(getShapeHeight())).draw(line);
	}

}
