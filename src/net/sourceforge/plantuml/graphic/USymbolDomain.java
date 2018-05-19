/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class USymbolDomain extends USymbol {

	private final SkinParameter skinParameter;
	private final char typeLetter;
	
	public USymbolDomain(SkinParameter skinParameter, char typeLetter) {
		this.skinParameter = skinParameter;
		this.typeLetter = typeLetter;
	}

	@Override
	public SkinParameter getSkinParameter() {
		return skinParameter;
	}

	private void drawRect(UGraphic ug, double width, double height, boolean shadowing, double roundCorner,
			double diagonalCorner) {
		final Shadowable shape = diagonalCorner > 0 ? getDiagonalShape(width, height, diagonalCorner) : new URectangle(
				width, height, roundCorner, roundCorner);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		ug.draw(shape);
	}

	private Shadowable getDiagonalShape(double width, double height, double diagonalCorner) {
		final UPath result = new UPath();
		result.moveTo(diagonalCorner, 0);
		result.lineTo(width - diagonalCorner, 0);
		result.lineTo(width, diagonalCorner);
		result.lineTo(width, height - diagonalCorner);
		result.lineTo(width - diagonalCorner, height);
		result.lineTo(diagonalCorner, height);
		result.lineTo(0, height - diagonalCorner);
		result.lineTo(0, diagonalCorner);
		result.lineTo(diagonalCorner, 0);
		return result;
	}

	private Margin getMargin() {
		return new Margin(10, 10, 10, 10);
	}

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final SymbolContext symbolContext) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = UGraphicStencil.create(ug, getRectangleStencil(dim), new UStroke());
				ug = symbolContext.apply(ug);
				drawRect(ug, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing(),
						symbolContext.getRoundCorner(), symbolContext.getDiagonalCorner());
				if (typeLetter == 'P')
					drawRect(ug, 4, dim.getHeight(), symbolContext.isShadowing(),
						symbolContext.getRoundCorner(), 0);
				else {
					BoxedCharacter tag = new BoxedCharacter(typeLetter, 
							8, UFont.byDefault(8), 
							skinParameter.getColorParamBack().getDefaultValue(),
							null, 
							skinParameter.getColorParamBorder().getDefaultValue());
					tag.drawU(ug.apply(new UTranslate(dim.getWidth() - tag.getPreferredWidth(ug.getStringBounder()), 
							dim.getHeight() - tag.getPreferredHeight(ug.getStringBounder()))));
				}
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dimLabel = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(Dimension2DDouble.mergeTB(dimStereo, dimLabel));
			}
		};
	}

	@Override
	public TextBlock asBig(final TextBlock title, final HorizontalAlignment labelAlignment, final TextBlock stereotype,
			final double width, final double height, final SymbolContext symbolContext) {
		return new AbstractTextBlock() {
			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = symbolContext.apply(ug);
				drawRect(ug, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing(),
						symbolContext.getRoundCorner(), 0);
				if (typeLetter == 'P')
					drawRect(ug, 4, dim.getHeight(), symbolContext.isShadowing(),
						symbolContext.getRoundCorner(), 0);
				else {
					BoxedCharacter tag = new BoxedCharacter(typeLetter, 
							8, UFont.byDefault(8), 
							skinParameter.getColorParamBack().getDefaultValue(),
							null, 
							skinParameter.getColorParamBorder().getDefaultValue());
					tag.drawU(ug.apply(new UTranslate(dim.getWidth() - tag.getPreferredWidth(ug.getStringBounder()), 
							dim.getHeight() - tag.getPreferredHeight(ug.getStringBounder()))));
				}
				final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
				final double posTitle;
				if (labelAlignment == HorizontalAlignment.LEFT) {
					posTitle = 3;
				} else if (labelAlignment == HorizontalAlignment.RIGHT) {
					posTitle = width - dimTitle.getWidth() - 3;
				} else {
					posTitle = (width - dimTitle.getWidth()) / 2;
				}
				title.drawU(ug.apply(new UTranslate(posTitle, 2)));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(width+6, height);
			}
		};
	}

	@Override
	public boolean manageHorizontalLine() {
		return true;
	}

}