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
package net.sourceforge.plantuml.klimt.shape;

import net.atmp.InnerStrategy;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MagneticBorder;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.style.ISkinSimple;

public class TextBlockTitle implements TextBlock {
    // ::remove file when __HAXE__

	private final double outMargin = 2;

	private final TextBlock textBlock;

	TextBlockTitle(FontConfiguration font, Display stringsToDisplay, ISkinSimple spriteContainer) {
		if (stringsToDisplay.size() == 1 && stringsToDisplay.get(0).length() == 0) {
			throw new IllegalArgumentException();
		}
		final LineBreakStrategy lineBreak = LineBreakStrategy.NONE;
		textBlock = stringsToDisplay.create0(font, HorizontalAlignment.CENTER, spriteContainer, lineBreak,
				CreoleMode.FULL, null, null);
	}

	public final void drawU(UGraphic ug) {
		textBlock.drawU(ug.apply(UTranslate.dx(outMargin)));
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D textDim = textBlock.calculateDimension(stringBounder);
		final double width = textDim.getWidth() + outMargin * 2;
		final double height = textDim.getHeight();
		return new XDimension2D(width, height);
	}

	public MinMax getMinMax(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

	public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return null;
	}

	@Override
	public MagneticBorder getMagneticBorder() {
		return new MagneticBorder() {

			@Override
			public UTranslate getForceAt(StringBounder stringBounder, XPoint2D position) {
				return textBlock.getMagneticBorder().getForceAt(stringBounder, position.move(-outMargin, 0));
			}
		};
	}

	@Override
	public HColor getBackcolor() {
		return textBlock.getBackcolor();
	}

}
