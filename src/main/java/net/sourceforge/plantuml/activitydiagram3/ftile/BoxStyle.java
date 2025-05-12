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
/**
 * The BoxStyle enum represents different styles of boxes used in Activity Diagrams.
 * Each enum constant specifies a unique box style, its associated stereotype,
 * and the shield value for rendering.
 * 
 * The enum also provides methods to generate shapes and drawable representations
 * of the box, as well as utilities for parsing and validating styles.
 */
public enum BoxStyle {

    /**
     * Plain box style with no additional shape or decorations.
     */
	PLAIN(null, '\0', 0) {
		// Shape: (=)
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return URectangle.build(width, height).rounded(roundCorner);
		}
	},

    /**
     * SDL input box style, represented with an input arrow shape.
     */
	SDL_INPUT("input", '<', 10) {
		// Shape: |=<
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return getShapeInput(width, height);
		}
	},

    /**
     * SDL output box style, represented with an output arrow shape.
     */
	SDL_OUTPUT("output", '>', 10) {
		// Shape: |=>
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return getShapeOutput(width, height);
		}
	},

    /**
     * SDL procedure box style, represented with vertical bars on both sides.
     */
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

    /**
     * SDL load box style, represented with a backslash diamond pattern.
     */
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

    /**
     * SDL save box style, represented with a forward slash diamond pattern.
     */
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

    /**
     * SDL continuous box style, represented with a continuous curve shape.
     */
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

    /**
     * SDL task box style, represented with square brackets.
     */
	SDL_TASK("task", ']', 0) {
		// Shape: [=]
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return URectangle.build(width, height);
		}
	},

    /**
     * UML object box style, represented with square brackets.
     */
	UML_OBJECT("object", ']', 0) {
		// Shape: [=]
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return URectangle.build(width, height);
		}
	},

    /**
     * UML object signal box style, represented with a signal arrow shape.
     */
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

    /**
     * UML trigger box style, represented with an input arrow shape.
     */
	UML_TRIGGER("trigger", '\0', 10) {
		// Shape: |=<
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return getShapeInput(width, height);
		}
	},

    /**
     * UML send signal box style, represented with an output arrow shape.
     */
	UML_SEND_SIGNAL("sendSignal", '\0', 10) {
		// Shape: |=>
		@Override
		protected Shadowable getShape(double width, double height, double roundCorner) {
			return getShapeOutput(width, height);
		}
	},

    /**
     * UML accept event box style, represented with an accept signal arrow shape.
     */
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

    /**
     * UML time event box style, represented with an hour glass shape.
     */
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

    /**
     * Represents the stereotype associated with the box style.
     * This is used for rendering and identifying the style uniquely.
     */
	private final String stereotype;

    /**
     * Represents the character style of the box.
     * This is used for parsing and identifying the style.
     */
	private final char style;

    /**
     * Represents the shield value, which is used in rendering.
     */
	private final double shield;

	private static int DELTA_INPUT_OUTPUT = 10;
	private static double DELTA_CONTINUOUS = 5.0;
	private static int PADDING = 5;

    /**
     * Constructor for BoxStyle enum with the specified parameters.
     * 
     * @param stereotype The stereotype associated with the box style.
     * @param style The character style of the box.
     * @param shield The shield value for rendering.
     */
	private BoxStyle(String stereotype, char style, double shield) {
		this.stereotype = stereotype;
		this.style = style;
		this.shield = shield;
	}

    /**
     * Checks for deprecated styles and adds a warning to the diagram.
     * 
     * @param diagram The diagram to which warnings will be added.
     * @param style The style to check for deprecation.
     */
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

    /**
     * Parses a string to determine the corresponding BoxStyle.
     * 
     * @param style The string representation of the style.
     * @return The BoxStyle corresponding to the input string.
     */
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

    /**
     * Generates a drawable representation of the box style.
     * This method returns a {@link UDrawable} object that encapsulates the rendering logic
     * for the box style with the specified dimensions and effects.
     * 
     * @param width The width of the box.
     * @param height The height of the box.
     * @param shadowing The shadowing effect to apply.
     * @param roundCorner The corner radius for rounding.
     * @return A {@link UDrawable} object for rendering the box style.
     */
	public final UDrawable getUDrawable(final double width, final double height, final double shadowing,
			final double roundCorner) {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				drawInternal(ug, width - getShield(), height, shadowing, roundCorner);
			}
		};
	}

    /**
     * Generates a shape representation of the box style.
     * This method is overridden by each enum constant to define its specific shape.
     * 
     * @param width The width of the box.
     * @param height The height of the box.
     * @param roundCorner The corner radius for rounding.
     * @return A {@link Shadowable} object representing the shape of the box.
     */
	protected Shadowable getShape(double width, double height, double roundCorner) {
		return null;
	}

    /**
     * Draws the box style using a graphical context.
     * This method can be overridden by specific box styles to apply custom drawing logic.
     * 
     * @param ug The {@link UGraphic} object used for drawing.
     * @param width The width of the box.
     * @param height The height of the box.
     * @param shadowing The shadowing effect to apply.
     * @param roundCorner The corner radius for rounding.
     */
	protected void drawInternal(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);

	}

    /**
     * Retrieves the shield value associated with the box style.
     * The shield value is used to adjust the rendering dimensions of the box.
     * 
     * @return The shield value.
     */
	public final double getShield() {
		return shield;
	}

    /**
     * Retrieves the stereotype associated with the box style.
     * The stereotype is represented as a {@link Stereotype} object, which includes
     * additional metadata for rendering and identification.
     * 
     * @return A {@link Stereotype} object representing the stereotype, or {@code null} if none is defined.
     */
	public Stereotype getStereotype() {
		if (stereotype == null)
			return null;
		return Stereotype.build("<<" + stereotype + ">>");
	}

	// Shape: |=<
    /**
     * Generates the shape for an SDL input style box.
     * This helper method creates a custom {@link UPolygon} shape with an input arrow.
     * 
     * @param width The width of the box.
     * @param height The height of the box.
     * @return A {@link Shadowable} object representing the SDL input box shape.
     */
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
    /**
     * Generates the shape for an SDL output style box.
     * This helper method creates a custom {@link UPolygon} shape with an output arrow.
     * 
     * @param width The width of the box.
     * @param height The height of the box.
     * @return A {@link Shadowable} object representing the SDL output box shape.
     */
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
