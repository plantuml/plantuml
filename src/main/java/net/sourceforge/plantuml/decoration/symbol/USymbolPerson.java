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
package net.sourceforge.plantuml.decoration.symbol;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphicStencil;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.SName;

class USymbolPerson extends USymbol {

	@Override
	public SName[] getSNames() {
		return new SName[] { SName.person };
	}

	private void drawHeadAndBody(UGraphic ug, double shadowing, XDimension2D dimBody, double headSize) {
		final UEllipse head = UEllipse.build(headSize, headSize);
		final URectangle body = URectangle.build(dimBody).rounded(headSize);

		body.setDeltaShadow(shadowing);
		head.setDeltaShadow(shadowing);

		final double posx = (dimBody.getWidth() - headSize) / 2;
		ug.apply(UTranslate.dx(posx)).draw(head);
		ug.apply(UTranslate.dy(headSize)).draw(body);
	}

	private double headSize(XDimension2D dimBody) {
		final double surface = dimBody.getWidth() * dimBody.getHeight();
		return Math.sqrt(surface) * .42;
	}

	private Margin getMargin() {
		return new Margin(10, 10, 10, 10);
	}

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final Fashion symbolContext, final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final XDimension2D dimFull = calculateDimension(ug.getStringBounder());
				final XDimension2D dimBody = bodyDimension(ug.getStringBounder());
				ug = UGraphicStencil.create(ug, dimFull);
				ug = symbolContext.apply(ug);
				final double headSize = headSize(dimBody);
				drawHeadAndBody(ug, symbolContext.getDeltaShadow(), dimBody, headSize);
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, stereoAlignment);
				final Margin margin = getMargin();
				tb.drawU(ug.apply(new UTranslate(margin.getX1(), margin.getY1() + headSize)));
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final XDimension2D body = bodyDimension(stringBounder);
				return body.delta(0, headSize(body));
			}

			private XDimension2D bodyDimension(StringBounder stringBounder) {
				final XDimension2D dimLabel = label.calculateDimension(stringBounder);
				final XDimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(dimStereo.mergeTB(dimLabel));
			}
		};
	}

	@Override
	public TextBlock asBig(final TextBlock title, final HorizontalAlignment labelAlignment, final TextBlock stereotype,
			final double width, final double height, final Fashion symbolContext,
			final HorizontalAlignment stereoAlignment) {
		throw new UnsupportedOperationException();
	}

}