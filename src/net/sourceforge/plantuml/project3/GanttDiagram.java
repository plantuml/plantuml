/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.project3;

import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorSetSimple;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.IHtmlColorSet;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GanttDiagram extends AbstractPSystem implements Subject {

	private final Map<TaskCode, Task> tasks = new LinkedHashMap<TaskCode, Task>();
	private final Map<String, Task> byShortName = new HashMap<String, Task>();
	private final List<GanttConstraint> constraints = new ArrayList<GanttConstraint>();
	private final IHtmlColorSet colorSet = new HtmlColorSetSimple();
	private GCalendar calendar;

	private Instant min;
	private Instant max;

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Project)");
	}

	@Override
	protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption, long seed)
			throws IOException {
		final double dpiFactor = 1;
		final double margin = 10;

		// public ImageBuilder(ColorMapper colorMapper, double dpiFactor, HtmlColor mybackcolor, String metadata,
		// String warningOrError, double margin1, double margin2, Animation animation, boolean useHandwritten) {

		sortTasks();

		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1, null, "", "", 0, 0, null,
				false);
		imageBuilder.setUDrawable(getUDrawable());

		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, seed, os);
	}

	private void sortTasks() {
		final TaskCodeSimpleOrder order = getCanonicalOrder(1);
		final List<Task> list = new ArrayList<Task>(tasks.values());
		Collections.sort(list, new Comparator<Task>() {
			public int compare(Task task1, Task task2) {
				return order.compare(task1.getCode(), task2.getCode());
			}
		});
		tasks.clear();
		for (Task task : list) {
			tasks.put(task.getCode(), task);
		}
	}

	private UDrawable getUDrawable() {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				initMinMax();
				final TimeScale timeScale = getTimeScale();
				drawTimeHeader(ug, timeScale);
				drawTasks(ug, timeScale);
				drawConstraints(ug, timeScale);
			}
		};
	}

	private TimeScale getTimeScale() {
		return new TimeScaleBasic();
		// return new TimeScaleWithoutWeekEnd(calendar);
	}

	private void drawConstraints(final UGraphic ug, TimeScale timeScale) {
		for (GanttConstraint constraint : constraints) {
			constraint.getUDrawable(timeScale).drawU(ug);
		}

	}

	private void drawTimeHeader(final UGraphic ug, TimeScale timeScale) {

		final double yTotal = initTaskDraws(timeScale);

		final double xmin = timeScale.getStartingPosition(min);
		final double xmax = timeScale.getStartingPosition(max.increment());
		ug.apply(new UChangeColor(HtmlColorUtils.LIGHT_GRAY)).draw(new ULine(xmax - xmin, 0));
		ug.apply(new UChangeColor(HtmlColorUtils.LIGHT_GRAY)).apply(new UTranslate(0, getHeaderHeight() - 3))
				.draw(new ULine(xmax - xmin, 0));
		if (calendar == null) {
			drawSimpleDayCounter(ug, timeScale, yTotal);
		} else {
			drawCalendar(ug, timeScale, yTotal);
		}

	}

	private void drawCalendar(final UGraphic ug, TimeScale timeScale, final double yTotal) {
		final int magic = 12;
		final ULine vbar = new ULine(0, yTotal - magic);
		Month lastMonth = null;
		for (Instant i = min; i.compareTo(max.increment()) <= 0; i = i.increment()) {
			final DayAsDate day = calendar.toDayAsDate((InstantDay) i);
			final String d1 = "" + day.getDayOfMonth();
			final TextBlock num = Display.getWithNewlines(d1).create(getFontConfiguration(), HorizontalAlignment.LEFT,
					new SpriteContainerEmpty());
			final double x1 = timeScale.getStartingPosition(i);
			final double x2 = timeScale.getStartingPosition(i.increment());
			if (i.compareTo(max.increment()) < 0) {
				final TextBlock weekDay = Display.getWithNewlines(day.getDayOfWeek().shortName()).create(
						getFontConfiguration(), HorizontalAlignment.LEFT, new SpriteContainerEmpty());

				drawCenter(ug.apply(new UTranslate(0, magic * 2)), num, x1, x2);
				drawCenter(ug.apply(new UTranslate(0, magic)), weekDay, x1, x2);
				if (lastMonth != day.getMonth()) {
					final TextBlock month = Display.getWithNewlines(day.getMonth().name()).create(
							getFontConfiguration(), HorizontalAlignment.LEFT, new SpriteContainerEmpty());
					month.drawU(ug.apply(new UTranslate(x1, 0)));
				}
				lastMonth = day.getMonth();
			}
			ug.apply(new UChangeColor(HtmlColorUtils.LIGHT_GRAY)).apply(new UTranslate(x1, magic)).draw(vbar);
		}
	}

	private double getHeaderHeight() {
		if (calendar != null) {
			return 40;
		}
		return 16;
	}

	private void drawCenter(final UGraphic ug, final TextBlock text, final double x1, final double x2) {
		final double width = text.calculateDimension(ug.getStringBounder()).getWidth();
		final double delta = (x2 - x1) - width;
		if (delta < 0) {
			return;
		}
		text.drawU(ug.apply(new UTranslate(x1 + delta / 2, 0)));
	}

	private void drawSimpleDayCounter(final UGraphic ug, TimeScale timeScale, final double yTotal) {
		final ULine vbar = new ULine(0, yTotal);
		for (Instant i = min; i.compareTo(max.increment()) <= 0; i = i.increment()) {
			final TextBlock num = Display.getWithNewlines(i.toShortString()).create(getFontConfiguration(),
					HorizontalAlignment.LEFT, new SpriteContainerEmpty());
			final double x1 = timeScale.getStartingPosition(i);
			final double x2 = timeScale.getStartingPosition(i.increment());
			final double width = num.calculateDimension(ug.getStringBounder()).getWidth();
			final double delta = (x2 - x1) - width;
			if (i.compareTo(max.increment()) < 0) {
				num.drawU(ug.apply(new UTranslate(x1 + delta / 2, 0)));
			}
			ug.apply(new UChangeColor(HtmlColorUtils.LIGHT_GRAY)).apply(new UTranslate(x1, 0)).draw(vbar);
		}
	}

	private double initTaskDraws(TimeScale timeScale) {
		double y = getHeaderHeight();
		for (Task task : tasks.values()) {
			final TaskDraw draw = new TaskDraw(task, timeScale, y);
			task.setTaskDraw(draw);
			y += draw.getHeight();
		}
		return y;
	}

	private void initMinMax() {
		min = tasks.values().iterator().next().getStart();
		max = tasks.values().iterator().next().getEnd();
		for (Task task : tasks.values()) {
			final Instant start = task.getStart();
			final Instant end = task.getEnd();
			if (min.compareTo(start) > 0) {
				min = start;
			}
			if (max.compareTo(end) < 0) {
				max = end;
			}
		}
	}

	private void drawTasks(final UGraphic ug, TimeScale timeScale) {
		for (Task task : tasks.values()) {
			final TaskDraw draw = task.getTaskDraw();
			draw.drawU(ug.apply(new UTranslate(0, draw.getY())));
			draw.getTitle().drawU(
					ug.apply(new UTranslate(timeScale.getStartingPosition(task.getStart().increment()), draw.getY())));
		}
	}

	private FontConfiguration getFontConfiguration() {
		final UFont font = UFont.serif(10);
		return new FontConfiguration(font, HtmlColorUtils.LIGHT_GRAY, HtmlColorUtils.LIGHT_GRAY, false);
	}

	public Task getExistingTask(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		Task result = byShortName.get(id);
		if (result != null) {
			return result;
		}
		final TaskCode code = new TaskCode(id);
		return tasks.get(code);
	}

	public Task getOrCreateTask(String codeOrShortName, String shortName) {
		if (codeOrShortName == null) {
			throw new IllegalArgumentException();
		}
		Task result = shortName == null ? null : byShortName.get(shortName);
		if (result != null) {
			return result;
		}
		result = byShortName.get(codeOrShortName);
		if (result != null) {
			return result;
		}
		final TaskCode code = new TaskCode(codeOrShortName);
		result = tasks.get(code);
		if (result == null) {
			result = new TaskImpl(code);
			tasks.put(code, result);
			if (byShortName != null) {
				byShortName.put(shortName, result);
			}
		}
		return result;
	}

	private TaskCodeSimpleOrder getCanonicalOrder(int hierarchyHeader) {
		final List<TaskCode> codes = new ArrayList<TaskCode>();
		for (TaskCode code : tasks.keySet()) {
			if (code.getHierarchySize() >= hierarchyHeader) {
				codes.add(code.truncateHierarchy(hierarchyHeader));
			}
		}
		return new TaskCodeSimpleOrder(codes, hierarchyHeader);
	}

	private int getMaxHierarchySize() {
		int max = Integer.MIN_VALUE;
		for (TaskCode code : tasks.keySet()) {
			max = Math.max(max, code.getHierarchySize());
		}
		return max;
	}

	public void addContraint(GanttConstraint constraint) {
		constraints.add(constraint);
	}

	public IHtmlColorSet getIHtmlColorSet() {
		return colorSet;
	}

	public void setStartingDate(DayAsDate start) {
		this.calendar = new GCalendarSimple(start);
	}

}
