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
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class USymbolPerson extends USymbol {

	@Override
	public SkinParameter getSkinParameter() {
		return SkinParameter.PERSON;
	}

	private void drawHeadAndBody(UGraphic ug, boolean shadowing, Dimension2D dimBody, double headSize) {
		final UEllipse head = new UEllipse(headSize, headSize);
		final URectangle body = new URectangle(dimBody).rounded(headSize);
		if (shadowing) {
			body.setDeltaShadow(3.0);
			head.setDeltaShadow(1.0);
		}
		final double posx = (dimBody.getWidth() - headSize) / 2;
		ug.apply(UTranslate.dx(posx)).draw(head);
		ug.apply(UTranslate.dy(headSize)).draw(body);
	}

	private double headSize(Dimension2D dimBody) {
		final double surface = dimBody.getWidth() * dimBody.getHeight();
		return Math.sqrt(surface) * .42;
	}

	private Margin getMargin() {
		return new Margin(10, 10, 10, 10);
	}

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final SymbolContext symbolContext, final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dimFull = calculateDimension(ug.getStringBounder());
				final Dimension2D dimBody = bodyDimension(ug.getStringBounder());
				ug = UGraphicStencil.create(ug, dimFull);
				ug = symbolContext.apply(ug);
				final double headSize = headSize(dimBody);
				drawHeadAndBody(ug, symbolContext.isShadowing(), dimBody, headSize);
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, stereoAlignment);
				final Margin margin = getMargin();
				tb.drawU(ug.apply(new UTranslate(margin.getX1(), margin.getY1() + headSize)));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D body = bodyDimension(stringBounder);
				return Dimension2DDouble.delta(body, 0, headSize(body));
			}

			private Dimension2D bodyDimension(StringBounder stringBounder) {
				final Dimension2D dimLabel = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(Dimension2DDouble.mergeTB(dimStereo, dimLabel));
			}
		};
	}

	@Override
	public TextBlock asBig(final TextBlock title, final HorizontalAlignment labelAlignment, final TextBlock stereotype,
			final double width, final double height, final SymbolContext symbolContext,
			final HorizontalAlignment stereoAlignment) {
		throw new UnsupportedOperationException();
	}

}