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
package net.sourceforge.plantuml.svek;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.OptionPrint;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.dot.GraphvizUtils;
import net.sourceforge.plantuml.eggs.QuoteUtils;
import net.sourceforge.plantuml.flashcode.FlashCodeFactory;
import net.sourceforge.plantuml.fun.IconLoader;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.GraphicPosition;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.text.BackSlash;
import net.sourceforge.plantuml.version.PSystemVersion;
import net.sourceforge.plantuml.version.Version;

public class GraphvizCrash extends AbstractTextBlock implements IEntityImage {

	private final TextBlock text1;
	private final String text;
	// ::comment when __CORE__
	private final BufferedImage flashCode;
	private final boolean graphviz244onWindows;
	// ::done

	public GraphvizCrash(String text, boolean graphviz244onWindows, Throwable rootCause) {
		this.text = text;
		// ::comment when __CORE__
		this.graphviz244onWindows = graphviz244onWindows;
		this.flashCode = FlashCodeFactory.getFlashCodeUtils().exportFlashcode(text, Color.BLACK, Color.WHITE);
		// ::done
		this.text1 = GraphicStrings.createBlackOnWhite(init(rootCause), IconLoader.getRandom(),
				GraphicPosition.BACKGROUND_CORNER_TOP_RIGHT);
	}

	public static List<String> anErrorHasOccured(Throwable exception, String text) {
		final List<String> strings = new ArrayList<>();
		if (exception == null)
			strings.add("An error has occured!");
		else
			strings.add("An error has occured : " + exception);

		final String quote = StringUtils.rot(QuoteUtils.getSomeQuote());
		strings.add("<i>" + quote);
		strings.add(" ");
		strings.add("Diagram size: " + lines(text) + " lines / " + text.length() + " characters.");
		strings.add(" ");
		return strings;
	}

	private static int lines(String text) {
		int result = 0;
		for (int i = 0; i < text.length(); i++)
			if (text.charAt(i) == BackSlash.CHAR_NEWLINE)
				result++;

		return result;
	}

	public static void checkOldVersionWarning(List<String> strings) {
		final long days = (System.currentTimeMillis() - Version.compileTime()) / 1000L / 3600 / 24;
		if (days >= 90) {
			strings.add(" ");
			strings.add("<b>This version of PlantUML is " + days + " days old, so you should");
			strings.add("<b>consider upgrading from https://plantuml.com/download");
		}
	}

	public static void pleaseGoTo(List<String> strings) {
		strings.add(" ");
		strings.add("Please go to https://plantuml.com/graphviz-dot to check your GraphViz version.");
		strings.add(" ");
	}

	public static void youShouldSendThisDiagram(List<String> strings) {
		strings.add("You should send this diagram and this image to <b>plantuml@gmail.com</b> or");
		strings.add("post to <b>https://plantuml.com/qa</b> to solve this issue.");
		strings.add("You can try to turn around this issue by simplifing your diagram.");
	}

	public static void thisMayBeCaused(final List<String> strings) {
		strings.add("This may be caused by :");
		strings.add(" - a bug in PlantUML");
		strings.add(" - a problem in GraphViz");
	}

	private List<String> init(Throwable rootCause) {
		final List<String> strings = anErrorHasOccured(null, text);
		strings.add("For some reason, dot/GraphViz has crashed.");
		strings.add("");
		strings.add("RootCause " + rootCause);
		if (rootCause != null)
			strings.addAll(CommandExecutionResult.getStackTrace(rootCause));

		strings.add("");
		strings.add("This has been generated with PlantUML (" + Version.versionString() + ").");
		checkOldVersionWarning(strings);
		strings.add(" ");
		addProperties(strings);
		strings.add(" ");
		// ::comment when __CORE__
		try {
			final String dotVersion = GraphvizUtils.dotVersion();
			strings.add("Default dot version: " + dotVersion);
		} catch (Throwable e) {
			strings.add("Cannot determine dot version: " + e.toString());
		}
		// ::done
		pleaseGoTo(strings);
		youShouldSendThisDiagram(strings);
		// ::comment when __CORE__
		if (flashCode != null)
			addDecodeHint(strings);
		// ::done

		return strings;
	}

	private List<String> getText2() {
		final List<String> strings = new ArrayList<>();
		strings.add(" ");
		strings.add("<b>It looks like you are running GraphViz 2.44 under Windows.");
		strings.add("If you have just installed GraphViz, you <i>may</i> have to execute");
		strings.add("the post-install command <b>dot -c</b> like in the following example:");
		return strings;
	}

	private List<String> getText3() {
		final List<String> strings = new ArrayList<>();
		strings.add(" ");
		strings.add("You may have to have <i>Administrator rights</i> to avoid the following error message:");
		return strings;
	}

	public static void addDecodeHint(final List<String> strings) {
		strings.add(" ");
		strings.add(" Diagram source: (Use http://zxing.org/w/decode.jspx to decode the qrcode)");
	}

	public static void addProperties(final List<String> strings) {
		// ::comment when __CORE__
		strings.addAll(OptionPrint.interestingProperties());
		strings.addAll(OptionPrint.interestingValues());
		// ::done
	}

	public boolean isHidden() {
		return false;
	}

	public HColor getBackcolor() {
		return HColors.WHITE;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return getMain().calculateDimension(stringBounder);
	}

	public void drawU(UGraphic ug) {
		getMain().drawU(ug);
	}

	private TextBlock getMain() {
		TextBlock result = text1;
		// ::comment when __CORE__
		if (flashCode != null) {
			final UImage flash = new UImage(new PixelImage(flashCode, AffineTransformType.TYPE_NEAREST_NEIGHBOR))
					.scale(3);
			result = TextBlockUtils.mergeTB(result, flash, HorizontalAlignment.LEFT);
		}

		if (graphviz244onWindows) {
			final TextBlock text2 = GraphicStrings.createBlackOnWhite(getText2());
			result = TextBlockUtils.mergeTB(result, text2, HorizontalAlignment.LEFT);

			final UImage dotc = new UImage(new PixelImage(PSystemVersion.getDotc(), AffineTransformType.TYPE_BILINEAR));
			result = TextBlockUtils.mergeTB(result, dotc, HorizontalAlignment.LEFT);

			final TextBlock text3 = GraphicStrings.createBlackOnWhite(getText3());
			result = TextBlockUtils.mergeTB(result, text3, HorizontalAlignment.LEFT);

			final UImage dotd = new UImage(new PixelImage(PSystemVersion.getDotd(), AffineTransformType.TYPE_BILINEAR));
			result = TextBlockUtils.mergeTB(result, dotd, HorizontalAlignment.LEFT);
		}
		// ::done

		return result;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public Margins getShield(StringBounder stringBounder) {
		return Margins.NONE;
	}

	public double getOverscanX(StringBounder stringBounder) {
		return 0;
	}

}
