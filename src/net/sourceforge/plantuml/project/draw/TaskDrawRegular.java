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

import java.util.Collection;

import net.sourceforge.plantuml.FontParam;
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
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.ToTaskDraw;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskImpl;
import net.sourceforge.plantuml.project.time.Wink;
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

	private final Wink end;
	private final boolean oddStart;
	private final boolean oddEnd;
	private final Collection<Wink> paused;

	private final double margin = 2;

	public TaskDrawRegular(TimeScale timeScale, double y, String prettyDisplay, Wink start, Wink end, boolean oddStart,
			boolean oddEnd, ISkinParam skinParam, Task task, ToTaskDraw toTaskDraw) {
		super(timeScale, y, prettyDisplay, start, skinParam, task, toTaskDraw);
		this.end = end;
		this.oddStart = oddStart;
		this.oddEnd = oddEnd;
		this.paused = ((TaskImpl) task).getAllPaused();
	}

	public void drawTitle(UGraphic ug) {
		final TextBlock title = Display.getWithNewlines(prettyDisplay).create(getFontConfiguration(),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		final double titleHeight = title.calculateDimension(ug.getStringBounder()).getHeight();
		final double h = (margin + getShapeHeight() - titleHeight) / 2;
		final double endingPosition = timeScale.getEndingPosition(start);
		title.drawU(ug.apply(new UTranslate(endingPosition, h)));
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
		drawNote(ug.apply(UTranslate.dy(getShapeHeight() + margin * 3)));

		final double startPos = timeScale.getStartingPosition(start);
		ug = applyColors(ug);
		ug = ug.apply(new UTranslate(startPos + margin, margin));
		drawShape(ug);
	}

	private void drawNote(UGraphic ug) {
		if (note == null) {
			return;
		}

		final Style style = StyleSignature.of(SName.root, SName.element, SName.ganttDiagram, SName.note)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		FontConfiguration fc = new FontConfiguration(style, skinParam, null, FontParam.NOTE);

		final Sheet sheet = Parser
				.build(fc, skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT), skinParam, CreoleMode.FULL)
				.createSheet(note);
		final SheetBlock1 sheet1 = new SheetBlock1(sheet, LineBreakStrategy.NONE, skinParam.getPadding());

		final HColor noteBackgroundColor = style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
		final HColor borderColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final double shadowing = style.value(PName.Shadowing).asDouble();

		Opale opale = new Opale(shadowing, borderColor, noteBackgroundColor, sheet1, false);
		opale.drawU(ug);

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

		final double fullLength = endPos - startPos - 2 * margin;
		if (fullLength < 10) {
			return;
		}
		if (url != null) {
			ug.startUrl(url);
		}
		if (oddStart && !oddEnd) {
			ug.draw(PathUtils.UtoRight(fullLength, getShapeHeight()));
		} else if (!oddStart && oddEnd) {
			ug.draw(PathUtils.UtoLeft(fullLength, getShapeHeight()));
		} else {
			final URectangle full = new URectangle(fullLength, getShapeHeight()).rounded(8);
			if (completion == 100) {
				ug.draw(full);
			} else {
				final double partialLength = fullLength * completion / 100.;
				ug.apply(HColorUtils.WHITE).apply(HColorUtils.WHITE.bg()).draw(full);
				if (partialLength > 2) {
					final URectangle partial = new URectangle(partialLength, getShapeHeight()).rounded(8);
					ug.apply(new HColorNone()).draw(partial);
				}
				if (partialLength > 10 && partialLength < fullLength - 10) {
					final URectangle patch = new URectangle(8, getShapeHeight());
					ug.apply(new HColorNone()).apply(UTranslate.dx(partialLength - 8)).draw(patch);
				}
				ug.apply(new HColorNone().bg()).draw(full);
			}
		}
		Wink begin = null;
		for (Wink pause : paused) {
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
		if (url != null) {
			ug.closeUrl();
		}
	}

	private void drawPause(UGraphic ug, Wink start1, Wink end) {
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
