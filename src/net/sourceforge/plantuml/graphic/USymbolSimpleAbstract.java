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
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

abstract class USymbolSimpleAbstract extends USymbol {

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final SymbolContext symbolContext, final HorizontalAlignment stereoAlignment) {
		if (stereotype == null) {
			throw new IllegalArgumentException();
		}
		final TextBlock stickman = getDrawing(symbolContext);
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dimLabel = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				final Dimension2D dimStickMan = stickman.calculateDimension(stringBounder);
				final Dimension2D dimTotal = calculateDimension(stringBounder);
				final double stickmanX = (dimTotal.getWidth() - dimStickMan.getWidth()) / 2;
				final double stickmanY = dimStereo.getHeight();
				ug = symbolContext.apply(ug);
				stickman.drawU(ug.apply(new UTranslate(stickmanX, stickmanY)));
				final double labelX = (dimTotal.getWidth() - dimLabel.getWidth()) / 2;
				final double labelY = dimStickMan.getHeight() + dimStereo.getHeight();

				// Actor bug?
				// final UGraphic ug2 = UGraphicStencil.create(ug, getRectangleStencil(dimLabel), new UStroke());
				// label.drawU(ug2.apply(new UTranslate(labelX, labelY)));
				label.drawU(ug.apply(new UTranslate(labelX, labelY)));

				final double stereoX = (dimTotal.getWidth() - dimStereo.getWidth()) / 2;
				stereotype.drawU(ug.apply(UTranslate.dx(stereoX)));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dimLabel = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				final Dimension2D dimActor = stickman.calculateDimension(stringBounder);
				return Dimension2DDouble.mergeLayoutT12B3(dimStereo, dimActor, dimLabel);
			}
		};
	}

	abstract protected TextBlock getDrawing(final SymbolContext symbolContext);

	@Override
	public TextBlock asBig(final TextBlock title, HorizontalAlignment labelAlignment, TextBlock stereotype,
			final double width, final double height, final SymbolContext symbolContext,
			final HorizontalAlignment stereoAlignment) {
		throw new UnsupportedOperationException();
	}

}