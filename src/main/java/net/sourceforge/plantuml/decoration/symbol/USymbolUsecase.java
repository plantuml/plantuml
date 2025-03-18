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
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Stencil;
import net.sourceforge.plantuml.klimt.drawing.AbstractUGraphicHorizontalLine;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockInEllipse;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UHorizontalLine;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.svek.image.RotatedEllipse;

class USymbolUsecase extends USymbol {

	private final boolean isBusiness;

	public USymbolUsecase(boolean isBusiness) {
		this.isBusiness = isBusiness;
	}

	@Override
	public SName[] getSNames() {
		if (isBusiness)
			return new SName[] { SName.usecase, SName.business };
		return new SName[] { SName.usecase };
	}

//	private Margin getMargin() {
//		return new Margin(10, 10, 10, 10);
//	}

	private void specialBusiness(UGraphic ug, UEllipse frontier) {
		final RotatedEllipse rotatedEllipse = new RotatedEllipse(frontier, Math.PI / 4);

		final double theta1 = 20.0 * Math.PI / 180;
		final double theta2 = rotatedEllipse.getOtherTheta(theta1);

		final UEllipse frontier2 = frontier.scale(0.99);
		final XPoint2D p1 = frontier2.getPointAtAngle(-theta1);
		final XPoint2D p2 = frontier2.getPointAtAngle(-theta2);
		drawLine(ug, p1, p2);
	}

	private void drawLine(UGraphic ug, final XPoint2D p1, final XPoint2D p2) {
		ug = ug.apply(UTranslate.point(p1));
		ug.draw(new ULine(p2.getX() - p1.getX(), p2.getY() - p1.getY()));
	}

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final Fashion symbolContext, final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			final TextBlock tmp = TextBlockUtils.mergeTB(stereotype, label, HorizontalAlignment.CENTER);
			final TextBlock desc = isBusiness ? TextBlockUtils.withMargin(tmp, 7, 0) : tmp;

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				ug = symbolContext.apply(ug);

				final TextBlockInEllipse ellipse = new TextBlockInEllipse(desc, stringBounder);

				final UGraphic ug2 = new MyUGraphicEllipse(ug, 0, 0, ellipse.getUEllipse());

				ellipse.drawU(ug2);
				if (isBusiness)
					specialBusiness(ug, ellipse.getUEllipse());

			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new TextBlockInEllipse(desc, stringBounder).calculateDimension(stringBounder);
			}
		};
	}

	@Override
	public TextBlock asBig(final TextBlock title, HorizontalAlignment labelAlignment, final TextBlock stereotype,
			final double width, final double height, final Fashion symbolContext,
			final HorizontalAlignment stereoAlignment) {
		throw new UnsupportedOperationException();
	}

	static class MyUGraphicEllipse extends AbstractUGraphicHorizontalLine {

		private final double startingX;
		private final double yTheoricalPosition;
		private final UEllipse ellipse;

		@Override
		protected AbstractUGraphicHorizontalLine copy(UGraphic ug) {
			return new MyUGraphicEllipse(ug, startingX, yTheoricalPosition, ellipse);
		}

		MyUGraphicEllipse(UGraphic ug, double startingX, double yTheoricalPosition, UEllipse ellipse) {
			super(ug);
			this.startingX = startingX;
			this.ellipse = ellipse;
			this.yTheoricalPosition = yTheoricalPosition;
		}

		private double getNormalized(double y) {
			if (y < yTheoricalPosition)
				throw new IllegalArgumentException();

			y = y - yTheoricalPosition;
			if (y > ellipse.getHeight())
				throw new IllegalArgumentException();

			return y;
		}

		private double getStartingXInternal(double y) {
			return startingX + ellipse.getStartingX(getNormalized(y));
		}

		private double getEndingXInternal(double y) {
			return startingX + ellipse.getEndingX(getNormalized(y));
		}

		private Stencil getStencil2(UTranslate translate) {
			final double dy = translate.getDy();
			return new Stencil() {

				public double getStartingX(StringBounder stringBounder, double y) {
					return getStartingXInternal(y + dy);
				}

				public double getEndingX(StringBounder stringBounder, double y) {
					return getEndingXInternal(y + dy);
				}
			};
		}

		@Override
		protected void drawHline(UGraphic ug, UHorizontalLine line, UTranslate translate) {
			final UStroke stroke = UStroke.withThickness(1.5);
			line.drawLineInternal(ug.apply(translate), getStencil2(translate), 0, stroke);
		}

	}

}