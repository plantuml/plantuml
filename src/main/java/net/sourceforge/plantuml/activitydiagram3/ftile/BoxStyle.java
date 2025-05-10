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
 * Contribution: Luc Trudeau
 * Contribution: The-Lum
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile;

import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.USegmentType;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.warning.Warning;

// Created from Luc Trudeau original work
public enum BoxStyle {
	PLAIN(null, '\0', 0) {
		// Shape: (=)
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return URectangle.build(width, height).rounded(roundCorner);
		}
	},
	SDL_INPUT("input", '<', 10) {
		// Shape: |=<
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return getShapeInput(width, height);
		}
	},
	SDL_OUTPUT("output", '>', 10) {
		// Shape: |=>
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return getShapeOutput(width, height);
		}
	},
	SDL_PROCEDURE("procedure", '|', 0) {
		// Shape: [|=|]
		@Override
		protected void drawInternal(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
			final URectangle rect = URectangle.build(width, height);
			rect.setDeltaShadow(shadowing);
			ug.draw(rect);
			final ULine vline = ULine.vline(height);
			ug.apply(UTranslate.dx(PADDING)).draw(vline);
			ug.apply(UTranslate.dx(width - PADDING)).draw(vline);
		}
	},
	SDL_LOAD("load", '\\', 0) {
		// Shape: \=\
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			final UPolygon result = new UPolygon();
			result.addPoint(0.0, 0.0);
			result.addPoint(width - DELTA_INPUT_OUTPUT, 0.0);
			result.addPoint(width, height);
			result.addPoint(DELTA_INPUT_OUTPUT, height);
			return result;
		}
	},
	SDL_SAVE("save", '/', 0) {
		// Shape: /=/
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			final UPolygon result = new UPolygon();
			result.addPoint(DELTA_INPUT_OUTPUT, 0.0);
			result.addPoint(width, 0.0);
			result.addPoint(width - DELTA_INPUT_OUTPUT, height);
			result.addPoint(0, height);
			return result;
		}
	},
	SDL_CONTINUOUS("continuous", '}', 0) {
		// Shape: < >
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			final UPath result = UPath.none();
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
	SDL_TASK("task", ']', 0) {
		// Shape: [=]
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return URectangle.build(width, height);
		}
	},
	UML_OBJECT("object", ']', 0) {
		// Shape: [=]
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return URectangle.build(width, height);
		}
	},
	UML_OBJECT_SIGNAL("objectSignal", '\0', 10) {
		// Shape: >=>
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			final UPolygon result = new UPolygon();
			result.addPoint(- DELTA_INPUT_OUTPUT, 0.0);
			result.addPoint(width, 0.0);
			result.addPoint(width + DELTA_INPUT_OUTPUT, height / 2);
			result.addPoint(width, height);
			result.addPoint(- DELTA_INPUT_OUTPUT, height);
			result.addPoint(0, height / 2);
			return result;
		}
	},
	UML_TRIGGER("trigger", '\0', 10) {
		// Shape: |=<
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return getShapeInput(width, height);
		}
	},
	UML_SEND_SIGNAL("sendSignal", '\0', 10) {
		// Shape: |=>
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return getShapeOutput(width, height);
		}
	},
	UML_ACCEPT_EVENT("acceptEvent", '\0', 10) {
		// Shape: >=|
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			final UPolygon result = new UPolygon();
			result.addPoint(- DELTA_INPUT_OUTPUT, 0.0);
			result.addPoint(width, 0.0);
			result.addPoint(width, height);
			result.addPoint(- DELTA_INPUT_OUTPUT, height);
			result.addPoint(0, height / 2);
			return result;
		}
	},
	UML_TIME_EVENT("timeEvent", '\0', 10) {
		// Shape: X
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			final UPolygon result = new UPolygon();
			final double halfWidth = width / 2;
			final double thirdHeight = height / 3;
			result.addPoint(halfWidth - thirdHeight, thirdHeight);
			result.addPoint(halfWidth + thirdHeight, thirdHeight);
			result.addPoint(halfWidth - thirdHeight, height);
			result.addPoint(halfWidth + thirdHeight, height);
			return result;
		}
	};

	private final String stereotype;
	private final char style;
	private final double shield;

	private static int DELTA_INPUT_OUTPUT = 10;
	private static double DELTA_CONTINUOUS = 5.0;
	private static int PADDING = 5;

	private BoxStyle(String stereotype, char style, double shield) {
		this.stereotype = stereotype;
		this.style = style;
		this.shield = shield;
	}

	public static void checkDeprecatedWarning(TitledDiagram diagram, String style) {
		if (style != null && style.length() == 1) {
			final BoxStyle boxStyle = fromString(style);
			if (boxStyle != PLAIN)
				diagram.addWarning(new Warning(//
						"The syntax using the " + boxStyle.style + " character is now obsolete.",
						"Please use the <<" + boxStyle.stereotype + ">> stereotype instead.",
						"For more details, visit https://plantuml.com/activity-diagram-beta"));
		}

	}

	public static BoxStyle fromString(String style) {
		if (style != null) {
			if (style.length() == 1)
				for (BoxStyle bs : BoxStyle.values())
					if (bs.style == style.charAt(0))
						return bs;

			for (String s : style.split("\\s")) {
				s = s.replaceAll("\\W", "");
				for (BoxStyle bs : BoxStyle.values())
					if (s.equalsIgnoreCase(bs.stereotype))
						return bs;
			}
		}
		return PLAIN;
	}

	public final UDrawable getUDrawable(final double width, final double height, final double shadowing,
			final double roundCorner) {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				drawInternal(ug, width - getShield(), height, shadowing, roundCorner);
			}
		};
	}

	protected Shadowable getShape(double width, double height, double roundCorner) {
		return null;
	}

	protected void drawInternal(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);

	}

	public final double getShield() {
		return shield;
	}

	public Stereotype getStereotype() {
		if (stereotype == null)
			return null;
		return Stereotype.build("<<" + stereotype + ">>");
	}

	// Shape: |=<
	private static Shadowable getShapeInput(double width, double height) {
		final UPolygon result = new UPolygon();
		result.addPoint(0, 0);
		result.addPoint(width + DELTA_INPUT_OUTPUT, 0);
		result.addPoint(width, height / 2);
		result.addPoint(width + DELTA_INPUT_OUTPUT, height);
		result.addPoint(0, height);
		return result;
	}

	// Shape: |=>
	private static Shadowable getShapeOutput(double width, double height) {
		final UPolygon result = new UPolygon();
		result.addPoint(0.0, 0.0);
		result.addPoint(width, 0.0);
		result.addPoint(width + DELTA_INPUT_OUTPUT, height / 2);
		result.addPoint(width, height);
		result.addPoint(0.0, height);
		return result;
	}

}
