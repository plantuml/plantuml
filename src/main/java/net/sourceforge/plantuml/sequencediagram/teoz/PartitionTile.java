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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.Iterator;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.GroupingStart;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;

public class PartitionTile extends GroupingTile {

	public PartitionTile(Iterator<Event> it, GroupingStart start, TileArguments tileArgumentsBackColorChanged,
			TileArguments tileArgumentsOriginal, YGauge currentY) {

		super(it, start, tileArgumentsBackColorChanged, tileArgumentsOriginal, currentY);
	}

	@Override
	protected Component getComponent(StringBounder stringBounder) {

		return new Component() {

			@Override
			public StyleSignature getStyleSignature() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Style[] getUsedStyles() {
				throw new UnsupportedOperationException();
			}

			@Override
			public double getPreferredWidth(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			@Override
			public double getPreferredHeight(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			@Override
			public XDimension2D getPreferredDimension(StringBounder stringBounder) {
				return new XDimension2D(10, 10);
			}

			@Override
			public void drawU(UGraphic ug, Area area, Context2D context) {
				final Style style = getGroupingStart().getUsedStyles()[1];
				final Display display = Display.create(getGroupingStart().getComment());
				final TextBlock title = display.create(style.getFontConfiguration(getSkinParam().getIHtmlColorSet()),
						HorizontalAlignment.LEFT, getSkinParam());
				final double border1 = getBorder1();
				final double delta = (getWidth() - title.calculateDimension(stringBounder).getWidth()) / 2;

				title.drawU(ug.apply(UTranslate.dx(border1 + delta)));

				final URectangle rect = URectangle.build(area.getDimensionToUse());

				ug = style.applyStrokeAndLineColor(ug, getSkinParam().getIHtmlColorSet());
				ug.apply(UTranslate.dx(border1)).draw(rect);

			}
		};
	}

	@Override
	protected Area getArea(final StringBounder stringBounder) {
		return Area.create(getWidth(), getTotalHeight(stringBounder));
	}

	private double getWidth() {
		return getBorder2() - getBorder1();
	}

	private double getBorder2() {
		return getTileArguments().getBorder2();
	}

	private double getBorder1() {
		return getTileArguments().getBorder1();
	}

	@Override
	protected double minCurrentValueForDrawing() {
		return 0;
	}

	@Override
	protected void drawCompBackground(UGraphic ug, Area area, final HColor back, final double round) {
		final URectangle rect = URectangle.build(area.getDimensionToUse());
		ug.apply(UTranslate.dx(getBorder1())).apply(back.bg()).draw(rect);
	}

}
