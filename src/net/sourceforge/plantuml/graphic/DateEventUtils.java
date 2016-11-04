/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.graphic;

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.eggs.PSystemMemorial;
import net.sourceforge.plantuml.ugraphic.LimitFinder;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.version.PSystemVersion;
import net.sourceforge.plantuml.webp.Portrait;
import net.sourceforge.plantuml.webp.Portraits;

public class DateEventUtils {

	public static TextBlock addEvent(final TextBlock textBlock, final HtmlColor color) {
		try {
			final String today = new SimpleDateFormat("MM-dd", Locale.US).format(new Date());
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
			} else if ("01-07".equals(today) || ("01-08".equals(today) && getDayOfWeek() == Calendar.MONDAY)) {
				return addCharlie(textBlock);
			} else if ("11-13".equals(today) || ("11-14".equals(today) && getDayOfWeek() == Calendar.MONDAY)) {
				return addMemorial(textBlock, color);
			}
		} catch (Throwable t) {
			Log.debug("Error " + t);
		}
		return textBlock;
	}

	private synchronized static int getDayOfWeek() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	private static TextBlock addMemorial(TextBlock textBlock, HtmlColor color) {
		final Portrait portrait = Portraits.getOne();
		if (portrait == null) {
			return textBlock;
		}
		final BufferedImage im = portrait.getBufferedImage();
		if (im == null) {
			return textBlock;
		}

		final String name = portrait.getName();
		final String quote = portrait.getQuote();
		final String age = "" + portrait.getAge() + " years old";
		final UFont font12 = new UFont("SansSerif", Font.BOLD, 12);
		TextBlock comment = Display.create(name, age, quote).create(
				new FontConfiguration(font12, color, HtmlColorUtils.BLUE, true), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
		comment = TextBlockUtils.withMinWidth(TextBlockUtils.withMargin(comment, 4, 4), 800, HorizontalAlignment.LEFT);

		final TextBlock bottom0 = getComment(Arrays.asList(PSystemMemorial.PARIS), color);
		final TextBlock bottom1 = new AbstractTextBlock() {
			private double margin = 10;

			public void drawU(UGraphic ug) {
				ug = ug.apply(new UTranslate(0, margin));
				ug.draw(new UImage(im));
				if (ug instanceof LimitFinder) {
					return;
				}
				Portraits.nextOne();
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(im.getWidth(), margin + im.getHeight());
			}
		};
		final TextBlock bottom = TextBlockUtils.mergeTB(bottom0,
				TextBlockUtils.mergeLR(bottom1, comment, VerticalAlignment.CENTER), HorizontalAlignment.LEFT);
		final TextBlock mergeTB = TextBlockUtils.mergeTB(textBlock, bottom, HorizontalAlignment.LEFT);
		return addMajesty(mergeTB, color);
	}

	public static TextBlock addMajesty(TextBlock block, HtmlColor color) {
		final UFont font12 = new UFont("SansSerif", Font.BOLD, 12);
		final String arabic1 = "<size:16>\u0625\u0646 \u0627\u0644\u0625\u0631\u0647\u0627\u0628\u064A\u064A\u0646 \u0628\u0627\u0633\u0645 \u0627\u0644\u0625\u0633\u0644\u0627\u0645 \u0644\u064A\u0633\u0648\u0627 \u0645\u0633\u0644\u0645\u064A\u0646\u060C \u0648\u0644\u0627 \u064A\u0631\u0628\u0637\u0647\u0645 \u0628\u0627\u0644\u0625\u0633\u0644\u0627\u0645 \u0625\u0644\u0627 \u0627\u0644\u062F\u0648\u0627\u0641\u0639 \u0627\u0644\u062A\u064A \u064A\u0631\u0643\u0628\u0648\u0646 \u0639\u0644\u064A\u0647\u0627 \u0644\u062A\u0628\u0631\u064A\u0631 \u062C\u0631\u0627\u0626\u0645\u0647\u0645 \u0648\u062D\u0645\u0627\u0642\u0627\u062A\u0647\u0645.";
		final String arabic2 = "<size:16>\u0641\u0647\u0645 \u0642\u0648\u0645 \u0636\u0627\u0644\u0648\u0646\u060C \u0645\u0635\u064A\u0631\u0647\u0645 \u062C\u0647\u0646\u0645 \u062E\u0627\u0644\u062F\u064A\u0646 \u0641\u064A\u0647\u0627 \u0623\u0628\u062F\u0627.";
		final String english1 = "<size:10>Those who engage in terrorism, in the name of Islam, are not Muslims.";
		final String english2 = "<size:10>Their only link to Islam is the pretexts they use to justify their crimes and their folly.";
		final String english3 = "<size:10>They have strayed from the right path, and their fate is to dwell forever in hell.";
		final TextBlock arabic = Display
				.create(" ",
						arabic1,
						arabic2,
						"<size:16>\u0635\u0627\u062D\u0628 \u0627\u0644\u062C\u0644\u0627\u0644\u0629 \u0627\u0644\u0645\u0644\u0643 \u0645\u062D\u0645\u062F \u0627\u0644\u0633\u0627\u062F\u0633 \u0623\u0645\u064A\u0631 \u0627\u0644\u0645\u0624\u0645\u0646\u064A\u0646 \u0646\u0635\u0631\u0647 \u0627\u0644\u0644\u0647")
				.create(new FontConfiguration(font12, color, HtmlColorUtils.BLUE, true), HorizontalAlignment.RIGHT,
						new SpriteContainerEmpty());
		final TextBlock english = Display.create(english1, english2, english3,
				"<size:10>-- His Majesty the King Mohammed the Sixth, Commander of the Faithful").create(
				new FontConfiguration(font12, color, HtmlColorUtils.BLUE, true), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
		return TextBlockUtils.mergeTB(block, TextBlockUtils.mergeTB(arabic, english, HorizontalAlignment.LEFT),
				HorizontalAlignment.LEFT);
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

	public static TextBlock getComment(final List<String> asList, HtmlColor color) {
		final UFont font = new UFont("SansSerif", Font.BOLD, 14);
		TextBlock comment = Display.create(asList).create(
				new FontConfiguration(font, color, HtmlColorUtils.BLUE, true), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
		comment = TextBlockUtils.withMargin(comment, 4, 4);
		comment = new SimpleTextBlockBordered(comment, color);
		comment = TextBlockUtils.withMargin(comment, 10, 10);
		return comment;
	}
}
