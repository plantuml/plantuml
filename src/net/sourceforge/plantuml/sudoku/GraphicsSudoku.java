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
 * Revision $Revision: 6591 $
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
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.UFont;

public class GraphicsSudoku {

	private final ISudoku sudoku;
	private final UFont numberFont = new UFont("SansSerif", Font.BOLD, 20);
	private final UFont font = new UFont("SansSerif", Font.PLAIN, 11);

	public GraphicsSudoku(ISudoku sudoku) {
		this.sudoku = sudoku;
	}

	public void writeImage(OutputStream os) throws IOException {
		final BufferedImage im = createImage();
		PngIO.write(im, os, 96);
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
		final Graphics2D g2d = builder.getGraphics2D();

		g2d.translate(xOffset, yOffset);

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				final int num = sudoku.getGiven(x, y);
				if (num > 0) {
					final TextBlock text = TextBlockUtils.create(Arrays.asList("" + num), new FontConfiguration(
							numberFont, HtmlColor.BLACK), HorizontalAlignement.CENTER);
					text.drawTOBEREMOVED(new ColorMapperIdentity(), g2d, numberxOffset + x * cellWidth, numberyOffset
							+ y * cellHeight);
				}
			}
		}

		for (int i = 0; i < 10; i++) {
			final boolean bold = i % boldWidth == 0;
			final int w = bold ? boldWidth : 1;
			g2d.fillRect(0, i * cellHeight, 9 * cellWidth + boldWidth, w);
		}
		for (int i = 0; i < 10; i++) {
			final boolean bold = i % boldWidth == 0;
			final int w = bold ? boldWidth : 1;
			g2d.fillRect(i * cellWidth, 0, w, 9 * cellHeight + boldWidth);
		}

		g2d.translate(0, sudoHeight);
		final List<String> texts = new ArrayList<String>();
		texts.add("http://plantuml.sourceforge.net");
		texts.add("Seed " + Long.toString(sudoku.getSeed(), 36));
		texts.add("Difficulty " + sudoku.getRatting());
		final TextBlock textBlock = TextBlockUtils.create(texts, new FontConfiguration(font, HtmlColor.BLACK),
				HorizontalAlignement.LEFT);
		textBlock.drawTOBEREMOVED(new ColorMapperIdentity(), g2d, 0, 0);

		g2d.dispose();
		return im;
	}

}
