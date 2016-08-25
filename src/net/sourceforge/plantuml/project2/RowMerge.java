/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.project2;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class RowMerge implements Row {

	private final Row r1;
	private final Row r2;

	public RowMerge(Row r1, Row r2) {
		this.r1 = r1;
		this.r2 = r2;
	}

	public TextBlock asTextBloc(final TimeConverter timeConverter) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				r1.asTextBloc(timeConverter).drawU(ug);
				r2.asTextBloc(timeConverter).drawU(ug.apply(new UTranslate(0, r1.getHeight())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final double width = getMaxXwithoutHeader(timeConverter) - getMinXwithoutHeader(timeConverter);
				final double height = getHeight();
				return new Dimension2DDouble(width, height);
			}
		};
	}

	public double getMinXwithoutHeader(TimeConverter timeConverter) {
		return Math.min(r1.getMinXwithoutHeader(timeConverter), r2.getMinXwithoutHeader(timeConverter));
	}

	public double getMaxXwithoutHeader(TimeConverter timeConverter) {
		return Math.max(r1.getMaxXwithoutHeader(timeConverter), r2.getMaxXwithoutHeader(timeConverter));
	}

	public double getHeight() {
		return r1.getHeight() + r2.getHeight();
	}

	public TextBlock header() {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				r1.header().drawU(ug);
				r2.header().drawU(ug.apply(new UTranslate(0, r1.getHeight())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final double width = Math.max(r1.header().calculateDimension(stringBounder).getWidth(), r2.header()
						.calculateDimension(stringBounder).getWidth());
				final double height = getHeight();
				return new Dimension2DDouble(width, height);
			}
		};
	}

}
