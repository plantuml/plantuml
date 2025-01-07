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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;

public class ResourceDrawNumbers implements ResourceDraw {

	private final Resource res;
	private final TimeScale timeScale;
	private final double y;
	private final Day min;
	private final Day max;
	private final GanttDiagram gantt;

	public ResourceDrawNumbers(GanttDiagram gantt, Resource res, TimeScale timeScale, double y, Day min, Day max) {
		this.res = res;
		this.timeScale = timeScale;
		this.y = y;
		this.min = min;
		this.max = max;
		this.gantt = gantt;
	}

	public void drawU(UGraphic ug) {
		final TextBlock title = Display.getWithNewlines(gantt.getPragma(), res.getName())
				.create(getFontConfiguration(13), HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		title.drawU(ug);
		final ULine line = ULine.hline(timeScale.getEndingPosition(max) - timeScale.getStartingPosition(min));
		ug.apply(HColors.BLACK).apply(UTranslate.dy(title.calculateDimension(ug.getStringBounder()).getHeight()))
				.draw(line);

		double startingPosition = -1;
		int totalLoad = 0;
		boolean isRed = false;
		for (Day i = min; i.compareTo(max) <= 0; i = i.increment()) {
			final boolean isBreaking = timeScale.isBreaking(i);
			final int load = gantt.getLoadForResource(res, i);
			if (load > 100)
				isRed = true;
			totalLoad += load;
			if (isBreaking) {
				if (totalLoad > 0) {
					final TextBlock value = getTextBlock(totalLoad, isRed);
					if (startingPosition == -1)
						startingPosition = timeScale.getStartingPosition(i);
					final double endingPosition = timeScale.getEndingPosition(i);
					final double start = (startingPosition + endingPosition) / 2
							- value.calculateDimension(ug.getStringBounder()).getWidth() / 2;
					value.drawU(ug.apply(new UTranslate(start, 16)));
				}
				startingPosition = -1;
				totalLoad = 0;
				isRed = false;
			} else {
				if (startingPosition == -1)
					startingPosition = timeScale.getStartingPosition(i);
			}
		}

	}

	private TextBlock getTextBlock(int totalLoad, boolean isRed) {
		final Display display = Display.getWithNewlines(gantt.getSkinParam().getPragma(), "" + totalLoad);
		final FontConfiguration fontConfiguration = getFontConfiguration(isRed);
		return display.create(fontConfiguration, HorizontalAlignment.LEFT, new SpriteContainerEmpty());
	}

	private FontConfiguration getFontConfiguration(boolean isRed) {
		return getFontConfiguration(9, isRed ? HColors.RED : HColors.BLACK);
	}

	private FontConfiguration getFontConfiguration(int size) {
		return getFontConfiguration(size, HColors.BLACK);
	}

	private FontConfiguration getFontConfiguration(int size, HColor color) {
		final UFont font = UFont.serif(size);
		return FontConfiguration.create(font, color, color, null);
	}

	@Override
	public double getHeight(StringBounder stringBounder) {
		return 16 * 2;
	}

	@Override
	public final double getY() {
		return y;
	}

}
