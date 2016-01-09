/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 10930 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.version.PSystemVersion;
import net.sourceforge.plantuml.webp.Portrait;
import net.sourceforge.plantuml.webp.Portraits;

public class DateEventUtils {

	public static TextBlock addEvent(TextBlock textBlock, HtmlColor color) {
		final DateFormat dateFormat = new SimpleDateFormat("MM-dd");
		final String today = dateFormat.format(new Date());

		if ("11-05".equals(today)) {
			final List<String> asList = Arrays.asList("<u>November 5th, 1955",
					"Doc Brown's discovery of the Flux Capacitor, that makes time-travel possible.");
			return TextBlockUtils.mergeTB(textBlock, getComment(asList, color), HorizontalAlignment.LEFT);
		} else if ("08-29".equals(today)) {
			final List<String> asList = Arrays.asList("<u>August 29th, 1997",
					"Skynet becomes self-aware at 02:14 AM Eastern Time.");
			return TextBlockUtils.mergeTB(textBlock, getComment(asList, color), HorizontalAlignment.LEFT);
		} else if ("06-29".equals(today)) {
			final List<String> asList = Arrays.asList("<u>June 29th, 1975",
					"\"It was the first time in history that anyone had typed",
					"a character on a keyboard and seen it show up on their",
					"own computer's screen right in front of them.\"", "\t\t\t\t\t\t\t\t\t\t<i>Steve Wozniak");
			return TextBlockUtils.mergeTB(textBlock, getComment(asList, color), HorizontalAlignment.LEFT);
		} else if ("01-07".equals(today) || "01-08".equals(today)) {
			return addCharlie(textBlock);
		}

		// return addMemorial(textBlock, color);
		return textBlock;
	}

	private static TextBlock addMemorial(TextBlock textBlock, HtmlColor color) {
		final Portrait portrait = new Portraits().getOne();
		if (portrait == null) {
			return textBlock;
		}
		final BufferedImage im = portrait.getBufferedImage();
		if (im == null) {
			return textBlock;
		}

		final List<String> asList = Arrays.asList("A thought for those who died in Paris the 13th November 2015.");

		final String name = portrait.getName();
		final UFont font = new UFont("SansSerif", Font.BOLD, 12);
		TextBlock comment = Display.create(name).create(new FontConfiguration(font, color, HtmlColorUtils.BLUE, true),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		comment = TextBlockUtils.withMargin(comment, 4, 4);

		final TextBlock bottom0 = getComment(asList, color);
		final TextBlock bottom1 = new AbstractTextBlock() {
			private double margin = 10;

			public void drawU(UGraphic ug) {
				ug = ug.apply(new UTranslate(0, margin));
				ug.draw(new UImage(im));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(im.getWidth(), margin + im.getHeight());
			}
		};
		final TextBlock bottom = TextBlockUtils.mergeTB(bottom0,
				TextBlockUtils.mergeLR(bottom1, comment, VerticalAlignment.CENTER), HorizontalAlignment.LEFT);
		return TextBlockUtils.mergeTB(textBlock, bottom, HorizontalAlignment.LEFT);
	}

	private static TextBlock addCharlie(TextBlock textBlock) {
		final TextBlock charlie = new AbstractTextBlock() {
			private final BufferedImage charlie = PSystemVersion.getCharlieImage();

			public void drawU(UGraphic ug) {
				ug.draw(new UImage(charlie));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(charlie.getWidth(), charlie.getHeight());
			}
		};
		return TextBlockUtils.mergeTB(charlie, textBlock, HorizontalAlignment.LEFT);

	}

	private static TextBlock getComment(final List<String> asList, HtmlColor color) {
		final UFont font = new UFont("SansSerif", Font.BOLD, 14);
		TextBlock comment = Display.create(asList).create(
				new FontConfiguration(font, color, HtmlColorUtils.BLUE, true), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
		comment = TextBlockUtils.withMargin(comment, 4, 4);
		comment = new TextBlockBordered(comment, color);
		comment = TextBlockUtils.withMargin(comment, 10, 10);
		return comment;
	}
}
