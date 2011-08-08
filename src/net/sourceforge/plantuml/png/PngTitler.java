/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 6922 $
 *
 */
package net.sourceforge.plantuml.png;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.util.List;

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.VerticalPosition;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class PngTitler {

	private final HtmlColor textColor;
	private final List<? extends CharSequence> text;
	private final int fontSize;
	private final String fontFamily;
	private final HorizontalAlignement horizontalAlignement;
	private final VerticalPosition verticalPosition;
	private final ColorMapper colorMapper;

	public PngTitler(ColorMapper colorMapper, HtmlColor textColor, List<? extends CharSequence> text, int fontSize, String fontFamily,
			HorizontalAlignement horizontalAlignement, VerticalPosition verticalPosition) {
		this.textColor = textColor;
		this.colorMapper = colorMapper;
		this.text = text;
		this.fontSize = fontSize;
		this.fontFamily = fontFamily;
		this.horizontalAlignement = horizontalAlignement;
		this.verticalPosition = verticalPosition;

	}

	public BufferedImage processImage(BufferedImage im, HtmlColor background, int margin) {
		if (text != null && text.size() > 0) {
			im = addTitle(colorMapper, im, background, textColor, text, fontSize, fontFamily, horizontalAlignement,
					verticalPosition, margin);
		}
		return im;

	}

	public Dimension2D getTextDimension(StringBounder stringBounder) {
		final TextBlock textBloc = getTextBlock();
		if (textBloc == null) {
			return null;
		}
		return textBloc.calculateDimension(stringBounder);
	}

	public TextBlock getTextBlock() {
		if (text == null || text.size() == 0) {
			return null;
		}
		final UFont normalFont = new UFont(fontFamily, Font.PLAIN, fontSize);
		return TextBlockUtils.create(text, new FontConfiguration(normalFont, textColor), horizontalAlignement);
	}

	static private BufferedImage addTitle(ColorMapper colorMapper, BufferedImage im, HtmlColor background, HtmlColor textColor, List<? extends CharSequence> text,
			int fontSize, String fontFamily, HorizontalAlignement horizontalAlignement,
			VerticalPosition verticalPosition, int margin) {

		final UFont normalFont = new UFont(fontFamily, Font.PLAIN, fontSize);
		final Graphics2D oldg2d = im.createGraphics();
		final TextBlock textBloc = TextBlockUtils.create(text, new FontConfiguration(normalFont, textColor),
				horizontalAlignement);
		final Dimension2D dimText = textBloc.calculateDimension(StringBounderUtils.asStringBounder(oldg2d));
		oldg2d.dispose();

		final double width = Math.max(im.getWidth(), dimText.getWidth());
		final double height = im.getHeight() + dimText.getHeight() + margin;

		final BufferedImage newIm = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = newIm.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setColor(colorMapper.getMappedColor(background));
		g2d.fillRect(0, 0, newIm.getWidth(), newIm.getHeight());
		final double xText;
		if (horizontalAlignement == HorizontalAlignement.LEFT) {
			xText = 2;
		} else if (horizontalAlignement == HorizontalAlignement.RIGHT) {
			xText = width - dimText.getWidth() - 2;
		} else if (horizontalAlignement == HorizontalAlignement.CENTER) {
			xText = (width - dimText.getWidth()) / 2;
		} else {
			xText = 0;
			assert false;
		}

		final int yText;
		final int yImage;

		if (verticalPosition == VerticalPosition.TOP) {
			yText = 0;
			yImage = (int) dimText.getHeight() + margin;
		} else {
			yText = im.getHeight() + margin;
			yImage = 0;
		}

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		textBloc.drawU(new UGraphicG2d(colorMapper, g2d, null, 1.0), xText, yText);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		final double delta2 = (width - im.getWidth()) / 2;

		g2d.drawImage(im, (int) delta2, yImage, null);
		g2d.dispose();
		return newIm;

	}

	public double getOffsetX(double imWidth, StringBounder stringBounder) {
		final TextBlock textBloc = getTextBlock();
		if (textBloc == null) {
			return 0;
		}
		final Dimension2D dimText = textBloc.calculateDimension(stringBounder);

		if (imWidth >= dimText.getWidth()) {
			return 0;
		}
		return (dimText.getWidth() - imWidth) / 2;
	}

	public double getOffsetY(StringBounder stringBounder) {
		final TextBlock textBloc = getTextBlock();
		if (textBloc == null) {
			return 0;
		}
		final Dimension2D dimText = textBloc.calculateDimension(stringBounder);
		final double height = dimText.getHeight();
		return height;
	}
}
