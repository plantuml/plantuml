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
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.style.SName;

class USymbolProcess extends USymbol {

	private final SName sname;

	public USymbolProcess(SName sname) {
		this.sname = sname;
	}

	@Override
	public SName[] getSNames() {
		return new SName[] { sname };
	}

	private void drawProcess(UGraphic ug, double width, double height, double shadowing, double roundCorner,
			double diagonalCorner) {
		final UPolygon shape = new UPolygon();
		shape.addPoint(0, 0);
		shape.addPoint(width - 10, 0);
		shape.addPoint(width, height / 2);
		shape.addPoint(width - 10, height);
		shape.addPoint(0, height);
		shape.addPoint(10, height / 2);
		ug.draw(shape);
	}

	private Margin getMargin() {
		return new Margin(20, 20, 10, 10);
	}

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final Fashion symbolContext, final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final XDimension2D dim = calculateDimension(ug.getStringBounder());
				ug = UGraphicStencil.create(ug, dim);
				ug = symbolContext.apply(ug);
				drawProcess(ug, dim.getWidth(), dim.getHeight(), symbolContext.getDeltaShadow(),
						symbolContext.getRoundCorner(), symbolContext.getDiagonalCorner());
				final Margin margin = getMargin();
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, stereoAlignment);
				tb.drawU(ug.apply(new UTranslate(margin.getX1(), margin.getY1())));
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final XDimension2D dimLabel = label.calculateDimension(stringBounder);
				final XDimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(dimStereo.mergeTB(dimLabel));
			}
		};
	}

	private double getHTitle(XDimension2D dimTitle) {
		final double htitle;
		if (dimTitle.getWidth() == 0)
			htitle = 10;
		else
			htitle = dimTitle.getHeight();

		return htitle;
	}

	@Override
	public TextBlock asBig(final TextBlock title, final HorizontalAlignment labelAlignment, final TextBlock stereotype,
			final double width, final double height, final Fashion symbolContext,
			final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final XDimension2D dim = calculateDimension(stringBounder);
				ug = symbolContext.apply(ug);
				final XDimension2D dimTitle = title.calculateDimension(stringBounder);
				drawProcess(ug, dim.getWidth(), dim.getHeight(), symbolContext.getDeltaShadow(),
						symbolContext.getRoundCorner(), symbolContext.getDiagonalCorner());
				final double posTitle = (width - dimTitle.getWidth()) / 2;
				title.drawU(ug.apply(new UTranslate(posTitle, 2)));
				final XDimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				final double posStereo = (width - dimStereo.getWidth()) / 2;

				stereotype.drawU(ug.apply(new UTranslate(4 + posStereo, 2 + getHTitle(dimTitle))));
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(width, height);
			}
		};
	}

}