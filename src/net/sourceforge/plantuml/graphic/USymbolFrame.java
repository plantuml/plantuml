/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

class USymbolFrame extends USymbol {

	@Override
	public SkinParameter getSkinParameter() {
		return SkinParameter.FRAME;
	}

	private void drawFrame(UGraphic ug, double width, double height, Dimension2D dimTitle, boolean shadowing,
			double roundCorner) {
		final Shadowable shape = new URectangle(width, height).rounded(roundCorner).ignoreForCompressionOnX()
				.ignoreForCompressionOnY();
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}

		ug.draw(shape);

		final double textWidth;
		final int cornersize;
		if (dimTitle.getWidth() == 0) {
			textWidth = width / 3;
			cornersize = 7;
		} else {
			textWidth = dimTitle.getWidth() + 10;
			cornersize = 10;
		}
		final double textHeight = getYpos(dimTitle);

		final UPath polygon = new UPath();
		polygon.setIgnoreForCompressionOnX();
		polygon.moveTo(textWidth, 1);

		polygon.lineTo(textWidth, textHeight - cornersize);
		polygon.lineTo(textWidth - cornersize, textHeight);

		polygon.lineTo(0, textHeight);
		ug.apply(new HColorNone().bg()).draw(polygon);

	}

	private double getYpos(Dimension2D dimTitle) {
		if (dimTitle.getWidth() == 0) {
			return 12;
		}
		return dimTitle.getHeight() + 3;
	}

	private Margin getMargin() {
		return new Margin(10 + 5, 20 + 5, 15 + 5, 5 + 5);
	}

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final SymbolContext symbolContext, final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = UGraphicStencil.create(ug, getRectangleStencil(dim), new UStroke());
				ug = symbolContext.apply(ug);
				drawFrame(ug, dim.getWidth(), dim.getHeight(), new Dimension2DDouble(0, 0), symbolContext.isShadowing(),
						symbolContext.getRoundCorner());
				final Margin margin = getMargin();
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, HorizontalAlignment.CENTER);
				tb.drawU(ug.apply(new UTranslate(margin.getX1(), margin.getY1())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dimLabel = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(Dimension2DDouble.mergeTB(dimStereo, dimLabel));
			}
		};
	}

	@Override
	public TextBlock asBig(final TextBlock title, HorizontalAlignment labelAlignment, final TextBlock stereotype,
			final double width, final double height, final SymbolContext symbolContext,
			final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dim = calculateDimension(stringBounder);
				ug = symbolContext.apply(ug);
				final Dimension2D dimTitle = title.calculateDimension(stringBounder);
				final double widthFull = dim.getWidth();
				drawFrame(ug, widthFull, dim.getHeight(), dimTitle, symbolContext.isShadowing(),
						symbolContext.getRoundCorner());
				final double widthTitle = title.calculateDimension(stringBounder).getWidth();

				// Temporary hack...
				if (widthFull - widthTitle < 25) {
					title.drawU(ug.apply(new UTranslate(3, 1)));
				} else {
					ug.apply(new UTranslate(3, 1)).draw(new SpecialText(title));
				}

				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				final double posStereo = (width - dimStereo.getWidth()) / 2;

				stereotype.drawU(ug.apply(new UTranslate(4 + posStereo, 2 + getYpos(dimTitle))));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(width, height);
			}
		};
	}

//	static class Interceptor extends UGraphicDelegator {
//
//		public Interceptor(UGraphic ug) {
//			super(ug);
//		}
//
//		@Override
//		public void draw(UShape shape) {
//			if (shape instanceof SpecialText) {
//				final SpecialText specialText = (SpecialText) shape;
//				specialText.title.drawU(getUg());
//				// System.err.println("getug=" + getUg());
//				return;
//			}
//			super.draw(shape);
//		}
//
//		public UGraphic apply(UChange change) {
//			return new Interceptor(getUg().apply(change));
//		}
//
//	}

	@Override
	public boolean manageHorizontalLine() {
		return true;
	}

}