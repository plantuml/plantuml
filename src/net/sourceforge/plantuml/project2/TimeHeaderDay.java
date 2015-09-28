/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.project2;

import java.awt.Font;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TimeHeaderDay extends AbstractTextBlock implements TextBlock {

	private final Day start;
	private final Day end;
	private final TimeLine timeline;
	private final double dayWidth;

	private final UFont font = new UFont("Serif", Font.PLAIN, 9);
	private final FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(font);

	public TimeHeaderDay(Day start, Day end, TimeLine timeline, double dayWidth) {
		this.start = start;
		this.end = end;
		this.timeline = timeline;
		this.dayWidth = dayWidth;
	}

	public void drawU(UGraphic ug) {
		int n = 0;
		for (Day d = start; d.compareTo(end) <= 0; d = (Day) timeline.next(d)) {
			final String text = "" + d.getNumDay();
			final TextBlock b = Display.create(text).create(fontConfig, HorizontalAlignment.LEFT,
					new SpriteContainerEmpty());
			final Dimension2D dimText = b.calculateDimension(ug.getStringBounder());
			final double diffX = dayWidth - dimText.getWidth();
			final double diffY = getHeight() - dimText.getHeight();
			ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK));
			ug = ug.apply(new UChangeBackColor(HtmlColorUtils.WHITE));
			ug.apply(new UTranslate(n * dayWidth, 0)).draw(new URectangle(dayWidth, getHeight()));
			b.drawU(ug.apply(new UTranslate((n * dayWidth + diffX / 2), (diffY / 2))));
			n++;
		}
	}

	private double getHeight() {
		return 20;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		int n = 0;
		for (Day d = start; d.compareTo(end) <= 0; d = (Day) timeline.next(d)) {
			n++;
		}
		return new Dimension2DDouble(n * dayWidth, getHeight());
	}
}