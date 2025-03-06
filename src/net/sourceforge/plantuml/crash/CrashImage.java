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
package net.sourceforge.plantuml.crash;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.flashcode.FlashCodeFactory;
import net.sourceforge.plantuml.flashcode.FlashCodeUtils;
import net.sourceforge.plantuml.fun.IconLoader;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.GraphicPosition;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.utils.Log;

public class CrashImage implements UDrawable {

	private final BufferedImage flashcodeImage;
	private final TextBlock graphicStrings;

	public CrashImage(Throwable exception, String flash, ReportLog strings) {
		if (exception != null)
			strings.addAll(CommandExecutionResult.getStackTrace(exception));

		this.flashcodeImage = flash == null ? null : generateFlashcodeImage(flash, strings);

		this.graphicStrings = GraphicStrings.createBlackOnWhite(strings.asList(), IconLoader.getRandom(),
				GraphicPosition.BACKGROUND_CORNER_TOP_RIGHT);

	}

	@Override
	public void drawU(UGraphic ug) {
		graphicStrings.drawU(ug);
		if (flashcodeImage == null)
			return;

		final double height = graphicStrings.calculateDimension(ug.getStringBounder()).getHeight();
		ug = ug.apply(UTranslate.dy(height));
		ug.draw(new UImage(new PixelImage(flashcodeImage, AffineTransformType.TYPE_NEAREST_NEIGHBOR)).scale(3));

	}

	private BufferedImage generateFlashcodeImage(String flash, ReportLog strings) {
		assert flash != null;
		// ::comment when __CORE__
		final FlashCodeUtils utils = FlashCodeFactory.getFlashCodeUtils();
		try {
			final BufferedImage flashcodeImage = utils.exportFlashcode(flash, Color.BLACK, Color.WHITE);
			if (flashcodeImage != null)
				strings.addDecodeHint();
			return flashcodeImage;
		} catch (Throwable e) {
			Log.error("Issue in flashcode generation " + e);
			// Logme.error(e);
		}
		// ::done
		return null;
	}

}
