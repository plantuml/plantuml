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

import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.lang.ComplementColors;
import net.sourceforge.plantuml.project.time.Wink;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class TaskDrawRegular extends AbstractTaskDraw {

	private static final HColor defaultColor = HColorSet.instance().getColorIfValid("GreenYellow");

	private ComplementColors colors;
	private int completion = 100;
	private Url url;
	private final Wink end;
	private final boolean oddStart;
	private final boolean oddEnd;

	private final double margin = 2;

	public TaskDrawRegular(TimeScale timeScale, double y, String prettyDisplay, Wink start, Wink end, boolean oddStart,
			boolean oddEnd) {
		super(timeScale, y, prettyDisplay, start);
		this.end = end;
		this.oddStart = oddStart;
		this.oddEnd = oddEnd;

	}

	public void drawTitle(UGraphic ug) {
		final TextBlock title = Display.getWithNewlines(prettyDisplay).create(getFontConfiguration(),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		final double titleHeight = title.calculateDimension(ug.getStringBounder()).getHeight();
		final double h = (margin + getShapeHeight() - titleHeight) / 2;
		final double endingPosition = timeScale.getEndingPosition(start);
		title.drawU(ug.apply(new UTranslate(endingPosition, h)));
	}

	@Override
	protected FontConfiguration getFontConfiguration() {
		final UFont font = UFont.serif(11);
		return new FontConfiguration(font, HColorUtils.BLACK, HColorUtils.BLACK, false);
	}

	public void drawU(UGraphic ug1) {
		final double startPos = timeScale.getStartingPosition(start);
		ug1 = applyColors(ug1);
		UGraphic ug2 = ug1.apply(new UTranslate(startPos + margin, margin));
		drawShape(ug2);
	}

	private UGraphic applyColors(UGraphic ug) {
		if (colors != null && colors.isOk()) {
			return colors.apply(ug);
		}
		return ug.apply(HColorUtils.BLUE).apply(defaultColor.bg());
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
			return;
		}
		if (!oddStart && oddEnd) {
			ug.draw(PathUtils.UtoLeft(fullLength, getShapeHeight()));
			return;
		}
		final URectangle full = new URectangle(fullLength, getShapeHeight()).rounded(8);
		if (completion == 100) {
			ug.draw(full);
		} else {
			final double partialLength = fullLength * completion / 100.;
			ug.apply(HColorUtils.WHITE).apply(HColorUtils.WHITE.bg())
					.draw(full);
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
		if (url != null) {
			ug.closeAction();
		}
	}

	public void setColorsAndCompletion(ComplementColors colors, int completion, Url url) {
		this.colors = colors;
		this.completion = completion;
		this.url = url;
	}
}
