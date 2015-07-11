/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 8066 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphicHorizontalLine;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UHorizontalLine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class USymbolQueue extends USymbol {

	@Override
	public SkinParameter getSkinParameter() {
		return SkinParameter.QUEUE;
	}


	private final double dx = 5;

	private void drawDatabase(UGraphic ug, double width, double height, boolean shadowing) {
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
		ug.apply(new UChangeBackColor(null)).draw(closing);

	}

	private UPath getClosingPath(double width, double height) {
		final UPath closing = new UPath();
		closing.moveTo(width - dx, 0);
		closing.cubicTo(width - dx * 2, 0, width - dx * 2, height / 2, width - dx * 2, height / 2);
		closing.cubicTo(width - dx * 2, height, width - dx, height, width - dx, height);
		return closing;
	}

	class MyUGraphicDatabase extends AbstractUGraphicHorizontalLine {

		private final double endingX;

		@Override
		protected AbstractUGraphicHorizontalLine copy(UGraphic ug) {
			return new MyUGraphicDatabase(ug, endingX);
		}

		public MyUGraphicDatabase(UGraphic ug, double endingX) {
			super(ug);
			this.endingX = endingX;
		}

		@Override
		protected void drawHline(UGraphic ug, UHorizontalLine line, UTranslate translate) {
			// final UPath closing = getClosingPath(endingX);
			// ug = ug.apply(translate);
			// ug.apply(line.getStroke()).apply(new UChangeBackColor(null)).apply(new UTranslate(0, -15)).draw(closing);
			// if (line.isDouble()) {
			// ug.apply(line.getStroke()).apply(new UChangeBackColor(null)).apply(new UTranslate(0, -15 + 2))
			// .draw(closing);
			// }
			line.drawTitleInternal(ug, 0, endingX, 0, true);
		}

	}

	private Margin getMargin() {
		return new Margin(5, 15, 5, 5);
	}

	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype, final SymbolContext symbolContext) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = symbolContext.apply(ug);
				drawDatabase(ug, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
				final Margin margin = getMargin();
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, HorizontalAlignment.CENTER);
				final UGraphic ug2 = new MyUGraphicDatabase(ug, dim.getWidth());
				tb.drawU(ug2.apply(new UTranslate(margin.getX1(), margin.getY1())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dimLabel = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(Dimension2DDouble.mergeTB(dimStereo, dimLabel));
			}
		};
	}

	public TextBlock asBig(final TextBlock title, final TextBlock stereotype, final double width, final double height,
			final SymbolContext symbolContext) {
		throw new UnsupportedOperationException();
		// return new TextBlock() {
		//
		// public void drawU(UGraphic ug) {
		// final Dimension2D dim = calculateDimension(ug.getStringBounder());
		// ug = symbolContext.apply(ug);
		// drawDatabase(ug, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
		// final Dimension2D dimStereo = stereotype.calculateDimension(ug.getStringBounder());
		// final double posStereo = (width - dimStereo.getWidth()) / 2;
		// stereotype.drawU(ug.apply(new UTranslate(posStereo, 0)));
		//
		// final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		// final double posTitle = (width - dimTitle.getWidth()) / 2;
		// title.drawU(ug.apply(new UTranslate(posTitle, 21)));
		// }
		//
		// public Dimension2D calculateDimension(StringBounder stringBounder) {
		// return new Dimension2DDouble(width, height);
		// }
		// };
	}

	public boolean manageHorizontalLine() {
		return true;
	}

	// @Override
	// public int suppHeightBecauseOfShape() {
	// return 15;
	// }

}