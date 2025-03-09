/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml.ebnf;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.style.ISkinParam;

public class ETileOneOrMore extends ETile {

	private final double deltax = 15;
	private final double deltay = 12;
	private final ETile orig;
	private final UText loop;
	private final FontConfiguration fc;

	public ETileOneOrMore(ETile orig, String loop, FontConfiguration fc, ISkinParam skinParam) {
		this.orig = orig;
		this.fc = fc;
		this.loop = loop == null ? null : UText.build(loop, fc);
	}

	public ETileOneOrMore(ETile orig) {
		this(orig, null, null, null);
	}

	@Override
	public double getH1(StringBounder stringBounder) {
		double h1 = deltay + orig.getH1(stringBounder);
		if (loop != null)
			h1 += getBraceHeight();
		return h1;
	}

	private double getBraceHeight() {
		if (loop == null)
			return 0;
		return 15;
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		return orig.getH2(stringBounder);
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		return orig.getWidth(stringBounder) + 2 * deltax;
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D fullDim = calculateDimension(stringBounder);
		if (TRACE)
			ug.apply(HColors.RED).draw(URectangle.build(fullDim));

		final double h1 = getH1(stringBounder);

		CornerCurved.createSW(8).drawU(ug.apply(new UTranslate(8, h1)));
		drawVline(ug, 8, 8 + 5 + getBraceHeight(), h1 - 8);
		CornerCurved.createNW(8).drawU(ug.apply(new UTranslate(8, 5 + getBraceHeight())));

		drawHlineAntiDirected(ug, 5 + getBraceHeight(), deltax, fullDim.getWidth() - deltax, 0.6);

		CornerCurved.createSE(8).drawU(ug.apply(new UTranslate(fullDim.getWidth() - 8, h1)));
		drawVline(ug, fullDim.getWidth() - 8, 8 + 5 + getBraceHeight(), h1 - 8);
		CornerCurved.createNE(8).drawU(ug.apply(new UTranslate(fullDim.getWidth() - 8, 5 + getBraceHeight())));

		drawHline(ug, h1, 0, deltax);
		drawHline(ug, h1, fullDim.getWidth() - deltax, fullDim.getWidth());

		orig.drawU(ug.apply(new UTranslate(deltax, deltay + getBraceHeight())));

		if (loop != null) {
			new Brace(fullDim.getWidth(), 10).drawU(ug.apply(new UTranslate(0, 10)));
			final XDimension2D dimText = stringBounder.calculateDimension(fc.getFont(), loop.getText());
			final double descent = stringBounder.getDescent(fc.getFont(), loop.getText());
			ug.apply(new UTranslate((fullDim.getWidth() - dimText.getWidth()) / 2, descent)).draw(loop);
//			final TextBlock icon = OpenIcon.retrieve("loop-circular").asTextBlock(fc.getColor(), 1.5);
//			icon.drawU(ug);
		}
	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

}
