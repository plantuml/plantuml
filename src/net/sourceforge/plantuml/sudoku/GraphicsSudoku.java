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
 * Revision $Revision: 14708 $
 *
 */
package net.sourceforge.plantuml.sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class GraphicsSudoku {

	private final ISudoku sudoku;
	private final UFont numberFont = new UFont("SansSerif", Font.BOLD, 20);
	private final UFont font = new UFont("SansSerif", Font.PLAIN, 11);

	public GraphicsSudoku(ISudoku sudoku) {
		this.sudoku = sudoku;
	}

	public ImageData writeImage(OutputStream os) throws IOException {
		final BufferedImage im = createImage();
		PngIO.write(im, os, 96);
		return new ImageDataSimple(im.getWidth(), im.getHeight());
	}

	final private int xOffset = 5;
	final private int yOffset = 5;

	final private int cellWidth = 30;
	final private int cellHeight = 32;

	final private int numberxOffset = 10;
	final private int numberyOffset = 5;

	final private int textTotalHeight = 50;

	private BufferedImage createImage() {
		final int boldWidth = 3;
		final int sudoHeight = 9 * cellHeight + 2 * yOffset + boldWidth;
		final int sudoWidth = 9 * cellWidth + 2 * xOffset + boldWidth;

		final EmptyImageBuilder builder = new EmptyImageBuilder(sudoWidth, sudoHeight + textTotalHeight, Color.WHITE);
		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g3d = builder.getGraphics2D();

		UGraphic ug = new UGraphicG2d(new ColorMapperIdentity(), g3d, 1.0);

		ug = ug.apply(new UTranslate(xOffset, yOffset));

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				final int num = sudoku.getGiven(x, y);
				if (num > 0) {
					final TextBlock text = TextBlockUtils.create(Display.create("" + num), new FontConfiguration(
							numberFont, HtmlColorUtils.BLACK, HtmlColorUtils.BLUE, true), HorizontalAlignment.CENTER,
							new SpriteContainerEmpty());
					text.drawU(ug.apply(new UTranslate((numberxOffset + x * cellWidth),
							(numberyOffset + y * cellHeight))));
				}
			}
		}

		ug = ug.apply(new UChangeBackColor(HtmlColorUtils.BLACK)).apply(new UChangeColor(null));
		for (int i = 0; i < 10; i++) {
			final boolean bold = i % boldWidth == 0;
			final int w = bold ? boldWidth : 1;
			ug.apply(new UTranslate(0, i * cellHeight)).draw(new URectangle(9 * cellWidth + boldWidth, w));
		}
		for (int i = 0; i < 10; i++) {
			final boolean bold = i % boldWidth == 0;
			final int w = bold ? boldWidth : 1;
			ug.apply(new UTranslate(i * cellWidth, 0)).draw(new URectangle(w, 9 * cellHeight + boldWidth));
		}

		ug = ug.apply(new UTranslate(0, sudoHeight));
		final List<String> texts = new ArrayList<String>();
		texts.add("http://plantuml.sourceforge.net");
		texts.add("Seed " + Long.toString(sudoku.getSeed(), 36));
		texts.add("Difficulty " + sudoku.getRatting());
		final TextBlock textBlock = TextBlockUtils.create(Display.create(texts), new FontConfiguration(
				font, HtmlColorUtils.BLACK, HtmlColorUtils.BLUE, true), HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		textBlock.drawU(ug);
		g3d.dispose();
		return im;
	}

}
