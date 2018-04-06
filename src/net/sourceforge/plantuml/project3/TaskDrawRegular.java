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

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TaskDrawRegular implements TaskDraw {

	private final TaskImpl task;
	private final TimeScale timeScale;
	private final double y;
	private ComplementColors colors;

	private final double margin = 2;

	public TaskDrawRegular(TaskImpl task, TimeScale timeScale, double y) {
		this.y = y;
		this.task = task;
		this.timeScale = timeScale;
	}

	public void drawTitle(UGraphic ug) {
		final TextBlock title = Display.getWithNewlines(task.getPrettyDisplay()).create(getFontConfiguration(),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		title.drawU(ug.apply(new UTranslate(timeScale.getEndingPosition(task.getStart()), 0)));
	}

	private FontConfiguration getFontConfiguration() {
		final UFont font = UFont.serif(11);
		return new FontConfiguration(font, HtmlColorUtils.BLACK, HtmlColorUtils.BLACK, false);
	}

	public void drawU(UGraphic ug1) {
		final double start = timeScale.getStartingPosition(task.getStart());
		ug1 = applyColors(ug1);
		UGraphic ug2 = ug1.apply(new UTranslate(start + margin, margin));
		// final int load = 42; // task.getLoad();
		final UShape shapeFull = getShape(100);
		if (shapeFull instanceof UPolygon) {
			ug2.draw(shapeFull);
		} else {
			final double fullHeight = ((URectangle) shapeFull).getHeight();
			ug2.apply(new UChangeBackColor(HtmlColorUtils.WHITE)).apply(new UChangeColor(HtmlColorUtils.WHITE))
					.draw(shapeFull);
			drawInside(ug1, fullHeight);
			ug2.apply(new UChangeBackColor(null)).draw(shapeFull);
		}
	}

	private void drawInside(UGraphic ug, double fullHeight) {
		for (Instant i = task.getStart(); i.compareTo(task.getEnd()) <= 0; i = i.increment()) {
			final int load = task.getLoadAt(i);
			final URectangle shapeLoad = getShapeInside(load, i);
			final double diffHeight = fullHeight - shapeLoad.getHeight();
			final double start = timeScale.getStartingPosition(i);
			ug.apply(new UChangeColor(null)).apply(new UTranslate(start, diffHeight + margin)).draw(shapeLoad);
		}
	}

	private UGraphic applyColors(UGraphic ug) {
		if (colors != null && colors.isOk()) {
			return colors.apply(ug);
		}
		if (isDiamond()) {
			return ug.apply(new UChangeColor(HtmlColorUtils.BLACK)).apply(new UChangeBackColor(HtmlColorUtils.BLACK));
		}
		return ug.apply(new UChangeColor(HtmlColorUtils.BLUE)).apply(new UChangeBackColor(HtmlColorUtils.COL_84BE84));
	}

	private URectangle getShapeInside(int load, Instant instant) {
		final double start = timeScale.getStartingPosition(instant);
		final double end = timeScale.getEndingPosition(instant);
		final double height = (getHeight() - 2 * margin) * load / 100.0;
		return new URectangle(end - start, height);
	}

	private UShape getShape(int load) {
		if (isDiamond()) {
			return getDiamond();
		}
		final Instant instantStart = task.getStart();
		final Instant instantEnd = task.getEnd();
		final double start = timeScale.getStartingPosition(instantStart);
		final double end = timeScale.getEndingPosition(instantEnd);
		final double height = (getHeight() - 2 * margin) * load / 100.0;
		return new URectangle(end - start - 2 * margin, height, 8, 8);
	}

	private boolean isDiamond() {
		final Instant instantStart = task.getStart();
		final Instant instantEnd = task.getEnd();
		return instantStart.compareTo(instantEnd) == 0;
	}

	private UShape getDiamond() {
		final double h = getHeight() - 2 * margin;
		final UPolygon result = new UPolygon();
		result.addPoint(h / 2, 0);
		result.addPoint(h, h / 2);
		result.addPoint(h / 2, h);
		result.addPoint(0, h / 2);
		return result;
		// return result.translate(2, 2);
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

	public void setColors(ComplementColors colors) {
		this.colors = colors;
	}
}
