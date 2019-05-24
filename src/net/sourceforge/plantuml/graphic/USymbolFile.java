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
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class USymbolFile extends USymbol {

	private final HorizontalAlignment stereotypeAlignement = HorizontalAlignment.CENTER;

	@Override
	public SkinParameter getSkinParameter() {
		return SkinParameter.FILE;
	}

	private void drawFile(UGraphic ug, double width, double height, boolean shadowing, double roundCorner) {
		final int cornersize = 10;
		final Shadowable out;
		if (roundCorner == 0) {
			final UPolygon polygon = new UPolygon();
			polygon.addPoint(0, 0);
			polygon.addPoint(0, height);
			polygon.addPoint(width, height);
			polygon.addPoint(width, cornersize);
			polygon.addPoint(width - cornersize, 0);
			polygon.addPoint(0, 0);
			out = polygon;
		} else {
			final UPath path = new UPath();
			path.moveTo(0, roundCorner / 2);
			path.lineTo(0, height - roundCorner / 2);
			path.arcTo(new Point2D.Double(roundCorner / 2, height), roundCorner / 2, 0, 0);
			path.lineTo(width - roundCorner / 2, height);
			path.arcTo(new Point2D.Double(width, height - roundCorner / 2), roundCorner / 2, 0, 0);
			path.lineTo(width, cornersize);
			path.lineTo(width - cornersize, 0);
			path.lineTo(roundCorner / 2, 0);
			path.arcTo(new Point2D.Double(0, roundCorner / 2), roundCorner / 2, 0, 0);
			out = path;
		}

		if (shadowing) {
			out.setDeltaShadow(3.0);
		}
		ug.draw(out);

		final UPath path = new UPath();
		path.moveTo(width - cornersize, 0);
		if (roundCorner == 0) {
			path.lineTo(width - cornersize, cornersize);
		} else {
			path.lineTo(width - cornersize, cornersize - roundCorner / 2);
			path.arcTo(new Point2D.Double(width - cornersize + roundCorner / 2, cornersize), roundCorner / 2, 0, 0);
		}
		path.lineTo(width, cornersize);
		ug.draw(path);
	}

	private Margin getMargin() {
		return new Margin(10, 10, 10, 10);
	}

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final SymbolContext symbolContext, final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = UGraphicStencil.create(ug, getRectangleStencil(dim), new UStroke());
				ug = symbolContext.apply(ug);
				drawFile(ug, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing(),
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
			final double width, final double height, final SymbolContext symbolContext, final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = symbolContext.apply(ug);
				drawFile(ug, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing(),
						symbolContext.getRoundCorner());
				final Dimension2D dimStereo = stereotype.calculateDimension(ug.getStringBounder());
				final double posStereoX;
				final double posStereoY;
				if (stereotypeAlignement == HorizontalAlignment.RIGHT) {
					posStereoX = width - dimStereo.getWidth() - getMargin().getX1() / 2;
					posStereoY = getMargin().getY1() / 2;
				} else {
					posStereoX = (width - dimStereo.getWidth()) / 2;
					posStereoY = 2;
				}
				stereotype.drawU(ug.apply(new UTranslate(posStereoX, posStereoY)));
				final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
				final double posTitle = (width - dimTitle.getWidth()) / 2;
				title.drawU(ug.apply(new UTranslate(posTitle, 2 + dimStereo.getHeight())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(width, height);
			}
		};
	}

	@Override
	public boolean manageHorizontalLine() {
		return true;
	}

}