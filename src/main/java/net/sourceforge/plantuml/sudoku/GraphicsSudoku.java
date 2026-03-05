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
package net.sourceforge.plantuml.sudoku;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;

public class GraphicsSudoku implements TextBlock {
	// ::remove folder when __MIT__ or __EPL__ or __BSD__ or __ASL__ or __LGPL__

	private final ISudoku sudoku;
	private final UFont numberFont = UFontFactory.sansSerif(20).bold();
	private final UFont font = UFontFactory.sansSerif(11);

	public GraphicsSudoku(ISudoku sudoku) {
		this.sudoku = sudoku;
	}


	final private int xOffset = 5;
	final private int yOffset = 5;

	final private int cellWidth = 30;
	final private int cellHeight = 32;

	final private int numberxOffset = 10;
	final private int numberyOffset = 5;

	final private int textTotalHeight = 50;

	final private int boldWidth = 3;
	final private int sudoHeight = 9 * cellHeight + 2 * yOffset + boldWidth;
	final private int sudoWidth = 9 * cellWidth + 2 * xOffset + boldWidth;

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return new XDimension2D(sudoWidth, sudoHeight + textTotalHeight);
	}

	@Override
	public void drawU(UGraphic ug) {
		ug = ug.apply(new UTranslate(xOffset, yOffset));

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				final int num = sudoku.getGiven(x, y);
				if (num > 0) {
					final TextBlock text = Display.create("" + num).create(FontConfiguration.blackBlueTrue(numberFont),
							HorizontalAlignment.CENTER, new SpriteContainerEmpty());
					text.drawU(ug
							.apply(new UTranslate((numberxOffset + x * cellWidth), (numberyOffset + y * cellHeight))));
				}
			}
		}

		ug = ug.apply(HColors.BLACK.bg()).apply(HColors.none());
		for (int i = 0; i < 10; i++) {
			final boolean bold = i % boldWidth == 0;
			final int w = bold ? boldWidth : 1;
			ug.apply(UTranslate.dy(i * cellHeight)).draw(URectangle.build(9 * cellWidth + boldWidth, w));
		}
		for (int i = 0; i < 10; i++) {
			final boolean bold = i % boldWidth == 0;
			final int w = bold ? boldWidth : 1;
			ug.apply(UTranslate.dx(i * cellWidth)).draw(URectangle.build(w, 9 * cellHeight + boldWidth));
		}

		ug = ug.apply(UTranslate.dy(sudoHeight));
		final List<String> texts = new ArrayList<>();
		texts.add("https://plantuml.com");
		texts.add("Seed " + Long.toString(sudoku.getSeed(), 36));
		texts.add("Difficulty " + sudoku.getRatting());
		final TextBlock textBlock = Display.create(texts).create(FontConfiguration.blackBlueTrue(font),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		textBlock.drawU(ug);
	}

}
