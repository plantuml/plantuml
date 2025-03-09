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
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.AbstractUGraphicHorizontalLine;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UEmpty;
import net.sourceforge.plantuml.klimt.shape.UHorizontalLine;
import net.sourceforge.plantuml.style.SName;

class USymbolDatabase extends USymbol {

	@Override
	public SName getSName() {
		return SName.database;
	}

	private void drawDatabase(UGraphic ug, double width, double height, double shadowing) {
		final UPath shape = UPath.none();
		shape.setDeltaShadow(shadowing);

		shape.moveTo(0, 10);
		shape.cubicTo(0, 0, width / 2, 0, width / 2, 0);
		shape.cubicTo(width / 2, 0, width, 0, width, 10);
		shape.lineTo(width, height - 10);
		shape.cubicTo(width, height, width / 2, height, width / 2, height);
		shape.cubicTo(width / 2, height, 0, height, 0, height - 10);
		shape.lineTo(0, 10);

		ug.draw(shape);

		final UPath closing = getClosingPath(width);
		ug.apply(HColors.none().bg()).draw(closing);
		ug.apply(new UTranslate(width, height)).draw(new UEmpty(10, 10));

	}

	private UPath getClosingPath(double width) {
		final UPath closing = UPath.none();
		closing.moveTo(0, 10);
		closing.cubicTo(0, 20, width / 2, 20, width / 2, 20);
		closing.cubicTo(width / 2, 20, width, 20, width, 10);
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
			final UPath closing = getClosingPath(endingX);
			ug = ug.apply(translate);
			ug.apply(line.getStroke()).apply(HColors.none().bg()).apply(UTranslate.dy(-15)).draw(closing);
			if (line.isDouble()) {
				ug.apply(line.getStroke()).apply(HColors.none().bg()).apply(UTranslate.dy(-15 + 2)).draw(closing);
			}
			line.drawTitleInternal(ug, 0, endingX, 0, true);
		}

	}

	private Margin getMargin() {
		return new Margin(10, 10, 24, 5);
	}

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final Fashion symbolContext, final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final XDimension2D dim = calculateDimension(ug.getStringBounder());
				ug = symbolContext.apply(ug);
				drawDatabase(ug, dim.getWidth(), dim.getHeight(), symbolContext.getDeltaShadow());
				final Margin margin = getMargin();
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, HorizontalAlignment.CENTER);
				final UGraphic ug2 = new MyUGraphicDatabase(ug, dim.getWidth());
				tb.drawU(ug2.apply(new UTranslate(margin.getX1(), margin.getY1())));
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final XDimension2D dimLabel = label.calculateDimension(stringBounder);
				final XDimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(dimStereo.mergeTB(dimLabel));
			}
		};
	}

	@Override
	public TextBlock asBig(final TextBlock title, HorizontalAlignment labelAlignment, final TextBlock stereotype,
			final double width, final double height, final Fashion symbolContext,
			final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final XDimension2D dim = calculateDimension(ug.getStringBounder());
				ug = symbolContext.apply(ug);
				drawDatabase(ug, dim.getWidth(), dim.getHeight(), symbolContext.getDeltaShadow());
				final XDimension2D dimStereo = stereotype.calculateDimension(ug.getStringBounder());
				final double posStereo = (width - dimStereo.getWidth()) / 2;
				stereotype.drawU(ug.apply(new UTranslate(posStereo, 2 + 20)));

				final XDimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
				final double posTitle = (width - dimTitle.getWidth()) / 2;
				title.drawU(ug.apply(new UTranslate(posTitle, 2 + 20 + dimStereo.getHeight())));
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(width, height);
			}
		};
	}

	@Override
	public int suppHeightBecauseOfShape() {
		return 15;
	}

}