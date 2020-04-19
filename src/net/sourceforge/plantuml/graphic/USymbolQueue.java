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
import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphicHorizontalLine;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UHorizontalLine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

class USymbolQueue extends USymbol {

	@Override
	public SkinParameter getSkinParameter() {
		return SkinParameter.QUEUE;
	}

	private final double dx = 5;

	private void drawQueue(UGraphic ug, double width, double height, boolean shadowing) {
		final UPath shape = new UPath();
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		shape.moveTo(dx, 0);
		shape.lineTo(width - dx, 0);
		shape.cubicTo(width, 0, width, height / 2, width, height / 2);
		shape.cubicTo(width, height / 2, width, height, width - dx, height);
		shape.lineTo(dx, height);

		shape.cubicTo(0, height, 0, height / 2, 0, height / 2);
		shape.cubicTo(0, height / 2, 0, 0, dx, 0);

		ug.draw(shape);

		final UPath closing = getClosingPath(width, height);
		ug.apply(new HColorNone().bg()).draw(closing);

	}

	private UPath getClosingPath(double width, double height) {
		final UPath closing = new UPath();
		closing.moveTo(width - dx, 0);
		closing.cubicTo(width - dx * 2, 0, width - dx * 2, height / 2, width - dx * 2, height / 2);
		closing.cubicTo(width - dx * 2, height, width - dx, height, width - dx, height);
		return closing;
	}

	class MyUGraphicQueue extends AbstractUGraphicHorizontalLine implements Stencil {

		private final double x1;
		private final double x2;
		private final double fullHeight;

		@Override
		protected AbstractUGraphicHorizontalLine copy(UGraphic ug) {
			return new MyUGraphicQueue(ug, x1, x2, fullHeight);
		}

		public MyUGraphicQueue(UGraphic ug, double x1, double x2, double fullHeight) {
			super(ug);
			this.x1 = x1;
			this.x2 = x2;
			this.fullHeight = fullHeight;
		}

		@Override
		protected void drawHline(UGraphic ug, UHorizontalLine line, UTranslate translate) {
			line.drawLineInternal(ug, this, translate.getDy(), line.getStroke());
		}

		public double getStartingX(StringBounder stringBounder, double y) {
			return 0;
		}

		public double getEndingX(StringBounder stringBounder, double y) {
			final double factor2;
			final double halfHeight = fullHeight / 2;
			if (y <= halfHeight) {
				factor2 = 1 - (y / halfHeight);
			} else {
				factor2 = (y - halfHeight) / halfHeight;
			}
			return (x2 - x1) * factor2 + x1;
		}
	}

	private Margin getMargin() {
		return new Margin(5, 15, 5, 5);
	}

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final SymbolContext symbolContext, final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = symbolContext.apply(ug);
				drawQueue(ug, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
				final Margin margin = getMargin();
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, HorizontalAlignment.CENTER);
				final UGraphic ug2 = new MyUGraphicQueue(ug, dim.getWidth() - 2 * dx, dim.getWidth() - dx,
						dim.getHeight());
				tb.drawU(ug2.apply(new UTranslate(margin.getX1(), margin.getY1())));
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
				drawQueue(ug, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
				final Dimension2D dimStereo = stereotype.calculateDimension(ug.getStringBounder());
				final double posStereo = (width - dimStereo.getWidth()) / 2;
				stereotype.drawU(ug.apply(new UTranslate(posStereo, 2)));

				final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
				final double posTitle = (width - dimTitle.getWidth()) / 2;
				title.drawU(ug.apply(new UTranslate(posTitle, 2 + dimStereo.getHeight())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(width, height);
			}
		};
	}

	public boolean manageHorizontalLine() {
		return true;
	}

}