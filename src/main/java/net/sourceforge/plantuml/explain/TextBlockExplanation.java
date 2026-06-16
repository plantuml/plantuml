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
package net.sourceforge.plantuml.explain;

import java.util.List;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.utils.LineLocation;

public class TextBlockExplanation implements TextBlock {

	private final static FontConfiguration MONOSPACED = FontConfiguration.blackBlueTrue(UFontFactory.monospace(10));
	private final static FontConfiguration SANS_SERIF = FontConfiguration.blackBlueTrue(UFontFactory.sansSerif(14));
	private final static FontConfiguration SANS_SERIF_ITALIC = FontConfiguration
			.blackBlueTrue(UFontFactory.sansSerif(14)).italic();

	private static final int DX30 = 30;
	private final LineLocation lineLocation;
	private final List<String> input;
	private final String explain;

	public TextBlockExplanation(LineLocation lineLocation, List<String> input, String explain) {
		this.lineLocation = lineLocation;
		this.input = input;
		this.explain = explain;
	}

	private TextBlock getLinesOfCode() {
		return new TextBlock() {

			@Override
			public void drawU(UGraphic ug) {
				for (String s : input) {
					final XDimension2D dim = ug.getStringBounder().calculateDimension(SANS_SERIF.getFont(), s);
					ug = ug.apply(UTranslate.dy(dim.getHeight()));
					ug.draw(UText.build(s, SANS_SERIF));
				}
			}

			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				double width = 0;
				double height = 0;
				for (String s : input) {
					final XDimension2D dim = stringBounder.calculateDimension(SANS_SERIF.getFont(), s);
					height += dim.getHeight();
					width = Math.max(width, dim.getWidth());
				}
				return new XDimension2D(width, height);
			}
		};
	}

	private TextBlock getExplainTextBlock() {
		return new TextBlock() {

			@Override
			public void drawU(UGraphic ug) {
				final XDimension2D dim = ug.getStringBounder().calculateDimension(SANS_SERIF_ITALIC.getFont(), explain);
				ug = ug.apply(UTranslate.dy(dim.getHeight()));
				ug.draw(UText.build(explain, SANS_SERIF_ITALIC));
			}

			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return stringBounder.calculateDimension(SANS_SERIF_ITALIC.getFont(), explain);
			}
		};
	}

	@Override
	public void drawU(UGraphic ug) {
		final String numLine = "" + (1 + lineLocation.getPosition());
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimNumLine = stringBounder.calculateDimension(MONOSPACED.getFont(), numLine);

		final TextBlock linesBlock = getLinesOfCode();
		final TextBlock explainBlock = getExplainTextBlock();

		ug.apply(new UTranslate(5, 5 + dimNumLine.getHeight())).draw(UText.build(numLine, MONOSPACED));

		linesBlock.drawU(ug.apply(UTranslate.dx(DX30)));

		explainBlock.drawU(ug.apply(new UTranslate(DX30, linesBlock.calculateDimension(stringBounder).getHeight())));

		final double height1 = linesBlock.calculateDimension(stringBounder).getHeight();
		final double height2 = explainBlock.calculateDimension(stringBounder).getHeight();

		ug = ug.apply(UTranslate.dy(height1 + height2));

	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D linesBlock = getLinesOfCode().calculateDimension(stringBounder);
		final XDimension2D explainBlock = getExplainTextBlock().calculateDimension(stringBounder);

		final double height = linesBlock.getHeight() + explainBlock.getHeight();
		final double width = Math.max(linesBlock.getWidth(), explainBlock.getWidth());

		return new XDimension2D(width + DX30 + 5, height + 15);
	}

}
