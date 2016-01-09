/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile;

import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.USegmentType;
import net.sourceforge.plantuml.ugraphic.UTranslate;

// Created from Luc Trudeau original work
public enum BoxStyle {
	PLAIN {
		@Override
		protected Shadowable getShape(double width, double height) {
			return new URectangle(width, height, CORNER, CORNER);
		}
	},
	SDL_INPUT('<') {
		@Override
		protected Shadowable getShape(double width, double height) {
			final UPolygon result = new UPolygon();
			result.addPoint(0, 0);
			result.addPoint(width + DELTA_INPUT_OUTPUT, 0);
			result.addPoint(width, height / 2);
			result.addPoint(width + DELTA_INPUT_OUTPUT, height);
			result.addPoint(0, height);
			return result;
		}
	},
	SDL_OUTPUT('>') {
		@Override
		protected Shadowable getShape(double width, double height) {
			final UPolygon result = new UPolygon();
			result.addPoint(0.0, 0.0);
			result.addPoint(width, 0.0);
			result.addPoint(width + DELTA_INPUT_OUTPUT, height / 2);
			result.addPoint(width, height);
			result.addPoint(0.0, height);
			return result;
		}
	},
	SDL_PROCEDURE('|') {
		@Override
		protected void drawInternal(UGraphic ug, double width, double height, boolean shadowing) {
			final URectangle rect = new URectangle(width, height);
			if (shadowing) {
				rect.setDeltaShadow(3);
			}
			ug.draw(rect);
			final ULine vline = new ULine(0, height);
			ug.apply(new UTranslate(PADDING, 0)).draw(vline);
			ug.apply(new UTranslate(width - PADDING, 0)).draw(vline);
		}
	},
	SDL_SAVE('/') {
		@Override
		protected Shadowable getShape(double width, double height) {
			final UPolygon result = new UPolygon();
			result.addPoint(0.0, 0.0);
			result.addPoint(width - DELTA_INPUT_OUTPUT, 0.0);
			result.addPoint(width, height);
			result.addPoint(DELTA_INPUT_OUTPUT, height);
			return result;
		}
	},
	SDL_CONTINUOUS('}') {
		@Override
		protected Shadowable getShape(double width, double height) {
			final UPath result = new UPath();
			final double c1[] = { DELTA_CONTINUOUS, 0 };
			final double c2[] = { 0, height / 2 };
			final double c3[] = { DELTA_CONTINUOUS, height };

			result.add(c1, USegmentType.SEG_MOVETO);
			result.add(c2, USegmentType.SEG_LINETO);
			result.add(c3, USegmentType.SEG_LINETO);

			final double c4[] = { width - DELTA_CONTINUOUS, 0 };
			final double c5[] = { width, height / 2 };
			final double c6[] = { width - DELTA_CONTINUOUS, height };

			result.add(c4, USegmentType.SEG_MOVETO);
			result.add(c5, USegmentType.SEG_LINETO);
			result.add(c6, USegmentType.SEG_LINETO);
			return result;
		}
	},
	SDL_TASK(']') {
		@Override
		protected Shadowable getShape(double width, double height) {
			return new URectangle(width, height);
		}
	};

	private static final int CORNER = 25;
	private final char style;
	private static int DELTA_INPUT_OUTPUT = 10;
	private static double DELTA_CONTINUOUS = 5.0;
	private static int PADDING = 5;

	private BoxStyle() {
		this('\0');
	}

	private BoxStyle(char style) {
		this.style = style;
	}

	public static BoxStyle fromChar(char style) {
		for (BoxStyle bs : BoxStyle.values()) {
			if (bs.style == style) {
				return bs;
			}
		}
		return PLAIN;
	}

	public final UDrawable getUDrawable(final double width, final double height, final boolean shadowing) {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				drawInternal(ug, width, height, shadowing);
			}
		};
	}

	protected Shadowable getShape(double width, double height) {
		return null;
	}

	protected void drawInternal(UGraphic ug, double width, double height, boolean shadowing) {
		final Shadowable s = getShape(width, height);
		if (shadowing) {
			s.setDeltaShadow(3);
		}
		ug.draw(s);

	}

}
