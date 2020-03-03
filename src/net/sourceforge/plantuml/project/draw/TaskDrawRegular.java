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

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorSetSimple;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.core.TaskImpl;
import net.sourceforge.plantuml.project.core.Wink;
import net.sourceforge.plantuml.project.lang.ComplementColors;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TaskDrawRegular implements TaskDraw {

	// private static final HtmlColor defaultColor = HtmlColorUtils.COL_84BE84;
	private static final HtmlColor defaultColor = new HtmlColorSetSimple().getColorIfValid("GreenYellow");
	private final TaskImpl task;
	private final TimeScale timeScale;
	private final double y;
	private ComplementColors colors;
	private int completion = 100;
	private Url url;

	private final double margin = 2;

	public TaskDrawRegular(TaskImpl task, TimeScale timeScale, double y) {
		this.y = y;
		this.task = task;
		this.timeScale = timeScale;
	}

	public void drawTitle(UGraphic ug) {
		final TextBlock title = Display.getWithNewlines(task.getPrettyDisplay()).create(getFontConfiguration(),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		final double titleHeight = title.calculateDimension(ug.getStringBounder()).getHeight();
		final double h = (margin + getShapeHeight() - titleHeight) / 2;
		final double endingPosition;
		if (isDiamond()) {
			endingPosition = timeScale.getStartingPosition(task.getStart()) + getHeight();
		} else {
			endingPosition = timeScale.getEndingPosition(task.getStart());
		}
		title.drawU(ug.apply(new UTranslate(endingPosition, h)));
	}

	private FontConfiguration getFontConfiguration() {
		final UFont font = UFont.serif(11);
		return new FontConfiguration(font, HtmlColorUtils.BLACK, HtmlColorUtils.BLACK, false);
	}

	public void drawU(UGraphic ug1) {
		final double start = timeScale.getStartingPosition(task.getStart());
		ug1 = applyColors(ug1);
		UGraphic ug2 = ug1.apply(new UTranslate(start + margin, margin));
		drawShape(ug2);
	}

	private UGraphic applyColors(UGraphic ug) {
		if (colors != null && colors.isOk()) {
			return colors.apply(ug);
		}
		if (isDiamond()) {
			return ug.apply(new UChangeColor(HtmlColorUtils.BLACK)).apply(new UChangeBackColor(HtmlColorUtils.BLACK));
		}
		return ug.apply(new UChangeColor(HtmlColorUtils.BLUE)).apply(new UChangeBackColor(defaultColor));
	}

	private void drawShape(UGraphic ug) {
		if (isDiamond()) {
			ug.draw(getDiamond());
			return;
		}
		final Wink instantStart = task.getStart();
		final Wink instantEnd = task.getEnd();
		final double start = timeScale.getStartingPosition(instantStart);
		final double end = timeScale.getEndingPosition(instantEnd);

		final double fullLength = end - start - 2 * margin;
		if (fullLength < 10) {
			return;
		}
		if (url != null) {
			ug.startUrl(url);
		}
		final URectangle full = new URectangle(fullLength, getShapeHeight(), 8, 8);
		if (completion == 100) {
			ug.draw(full);
		} else {
			final double partialLength = fullLength * completion / 100.;
			ug.apply(new UChangeColor(HtmlColorUtils.WHITE)).apply(new UChangeBackColor(HtmlColorUtils.WHITE))
					.draw(full);
			if (partialLength > 2) {
				final URectangle partial = new URectangle(partialLength, getShapeHeight(), 8, 8);
				ug.apply(new UChangeColor(null)).draw(partial);
			}
			if (partialLength > 10 && partialLength < fullLength - 10) {
				final URectangle patch = new URectangle(8, getShapeHeight());
				ug.apply(new UChangeColor(null)).apply(new UTranslate(partialLength - 8, 0)).draw(patch);
			}
			ug.apply(new UChangeBackColor(null)).draw(full);
		}
		if (url != null) {
			ug.closeAction();
		}

	}

	private double getShapeHeight() {
		return getHeight() - 2 * margin;
	}

	private boolean isDiamond() {
		if (task.isDiamond()) {
			final Wink instantStart = task.getStart();
			final Wink instantEnd = task.getEnd();
			return instantStart.compareTo(instantEnd) == 0;
		}
		return false;
	}

	private UShape getDiamond() {
		final double h = getHeight() - 2 * margin;
		final UPolygon result = new UPolygon();
		result.addPoint(h / 2, 0);
		result.addPoint(h, h / 2);
		result.addPoint(h / 2, h);
		result.addPoint(0, h / 2);
		return result;
	}

	public double getHeight() {
		return 16;
	}

	public double getY() {
		return y;
	}

	public double getY(Direction direction) {
		if (direction == Direction.UP) {
			return y;
		}
		if (direction == Direction.DOWN) {
			return y + getHeight();
		}
		return y + getHeight() / 2;
	}

	public void setColorsAndCompletion(ComplementColors colors, int completion, Url url) {
		this.colors = colors;
		this.completion = completion;
		this.url = url;
	}
}
