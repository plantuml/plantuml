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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.USegmentType;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.warning.Warning;

//Created from Luc Trudeau original work
public abstract class BoxStyle {

	private static final List<BoxStyle> values = new ArrayList<>();

	// Shape: (=)
	public static final BoxStyle PLAIN = new BoxStylePlain(null, '\0', 0);

	// Shape: |=<
	public static final BoxStyle SDL_INPUT = new BoxStyleInput("input", '<', 10);

	// Shape: |=>
	public static final BoxStyle SDL_OUTPUT = new BoxStyleOutput("output", '>', 10);

	// Shape: [|=|]
	public static final BoxStyle SDL_PROCEDURE = new BoxStyleProcedure("procedure", '|', 0);

	// Shape: \=\
	public static final BoxStyle SDL_LOAD = new BoxStyleLoad("load", '\\', 0);

	// Shape: /=/
	public static final BoxStyle SDL_SAVE = new BoxStyleSave("save", '/', 0);

	// Shape: < >
	public static final BoxStyle SDL_CONTINUOUS = new BoxStyleContinuous("continuous", '}', 0);

	// Shape: [=]
	public static final BoxStyle SDL_TASK = new BoxStyleTask("task", ']', 0);

	// Shape: [=]
	public static final BoxStyle UML_OBJECT = new BoxStyleObject("object", ']', 0);

	// Shape: >=>
	public static final BoxStyle UML_OBJECT_SIGNAL = new BoxStyleObjectSignal("objectSignal", '\0', 10);

	// Shape: |=<
	public static final BoxStyle UML_TRIGGER = new BoxStyleTrigger("trigger", '\0', 10);

	// Shape: |=>
	public static final BoxStyle UML_SEND_SIGNAL = new BoxStyleSendSignal("sendSignal", '\0', 10);

	// Shape: >=|
	public static final BoxStyle UML_ACCEPT_EVENT = new BoxStyleAcceptEvent("acceptEvent", '\0', 10);

	// Shape: X
	public static final BoxStyle UML_TIME_EVENT = new BoxStyleTimeEvent("timeEvent", '\0', 10);

	/**
	 * Represents the stereotype associated with the box style. This is used for
	 * rendering and identifying the style uniquely.
	 */
	protected final String stereotype;

	/**
	 * Represents the character style of the box. This is used for parsing and
	 * identifying the style.
	 */
	protected final char style;

	/**
	 * Represents the shield value, which is used in rendering.
	 */
	protected final double shield;

	protected static int DELTA_INPUT_OUTPUT = 10;
	protected static double DELTA_CONTINUOUS = 5.0;
	protected static int PADDING = 5;

	public abstract void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner);

	protected BoxStyle(String stereotype, char style, double shield) {
		this.stereotype = stereotype;
		this.style = style;
		this.shield = shield;
		values.add(this);
	}

	public double getShield() {
		return shield;
	}

	public static BoxStyle fromString(String style) {
		if (style != null) {
			if (style.length() == 1)
				for (BoxStyle bs : values)
					if (bs.style == style.charAt(0))
						return bs;

			for (String s : style.split("\\s")) {
				s = s.replaceAll("\\W", "");
				for (BoxStyle bs : values)
					if (s.equalsIgnoreCase(bs.stereotype))
						return bs;
			}
		}
		return PLAIN;
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

	public Stereotype getStereotype() {
		if (stereotype == null)
			return null;
		return Stereotype.build("<<" + stereotype + ">>");
	}

	public void drawMeDebug(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		final Shadowable s = URectangle.build(width, height);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

}

class BoxStylePlain extends BoxStyle {

	public BoxStylePlain(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

	protected Shadowable getShape(double width, double height, double roundCorner) {
		return URectangle.build(width, height).rounded(roundCorner);
	}
}

class BoxStyleInput extends BoxStyle {

	public BoxStyleInput(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

	protected Shadowable getShape(double width, double height, double roundCorner) {
		return getShapeInput(width, height);
	}
	
	private static Shadowable getShapeInput(double width, double height) {
		final UPolygon result = new UPolygon();
		result.addPoint(0, 0);
		result.addPoint(width + DELTA_INPUT_OUTPUT, 0);
		result.addPoint(width, height / 2);
		result.addPoint(width + DELTA_INPUT_OUTPUT, height);
		result.addPoint(0, height);
		return result;
	}

}


class BoxStyleOutput extends BoxStyle {

	public BoxStyleOutput(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

	protected Shadowable getShape(double width, double height, double roundCorner) {
		return getShapeOutput(width, height);
	}
	
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


class BoxStyleProcedure extends BoxStyle {

	public BoxStyleProcedure(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final URectangle rect = URectangle.build(width, height);
		rect.setDeltaShadow(shadowing);
		ug.draw(rect);
		final ULine vline = ULine.vline(height);
		ug.apply(UTranslate.dx(PADDING)).draw(vline);
		ug.apply(UTranslate.dx(width - PADDING)).draw(vline);
	}
}


class BoxStyleLoad extends BoxStyle {

	public BoxStyleLoad(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

	protected Shadowable getShape(double width, double height, double roundCorner) {
		final UPolygon result = new UPolygon();
		result.addPoint(0.0, 0.0);
		result.addPoint(width - DELTA_INPUT_OUTPUT, 0.0);
		result.addPoint(width, height);
		result.addPoint(DELTA_INPUT_OUTPUT, height);
		return result;
	}
}


class BoxStyleSave extends BoxStyle {

	public BoxStyleSave(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

	protected Shadowable getShape(double width, double height, double roundCorner) {
		final UPolygon result = new UPolygon();
		result.addPoint(DELTA_INPUT_OUTPUT, 0.0);
		result.addPoint(width, 0.0);
		result.addPoint(width - DELTA_INPUT_OUTPUT, height);
		result.addPoint(0, height);
		return result;
	}
}


class BoxStyleContinuous extends BoxStyle {

	public BoxStyleContinuous(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

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
}




class BoxStyleTask extends BoxStyle {

	public BoxStyleTask(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

	protected Shadowable getShape(double width, double height, double roundCorner) {
		return URectangle.build(width, height);
	}
}


class BoxStyleObject extends BoxStyle {

	public BoxStyleObject(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

	protected Shadowable getShape(double width, double height, double roundCorner) {
		return URectangle.build(width, height);
	}
}


class BoxStyleObjectSignal extends BoxStyle {

	public BoxStyleObjectSignal(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

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
}


class BoxStyleTrigger extends BoxStyle {

	public BoxStyleTrigger(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

	protected Shadowable getShape(double width, double height, double roundCorner) {
		return getShapeInput(width, height);
	}
	
	private static Shadowable getShapeInput(double width, double height) {
		final UPolygon result = new UPolygon();
		result.addPoint(0, 0);
		result.addPoint(width + DELTA_INPUT_OUTPUT, 0);
		result.addPoint(width, height / 2);
		result.addPoint(width + DELTA_INPUT_OUTPUT, height);
		result.addPoint(0, height);
		return result;
	}

}


class BoxStyleSendSignal extends BoxStyle {

	public BoxStyleSendSignal(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShapeOutput(width, height);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

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


class BoxStyleAcceptEvent extends BoxStyle {

	public BoxStyleAcceptEvent(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

	protected Shadowable getShape(double width, double height, double roundCorner) {
		final UPolygon result = new UPolygon();
		result.addPoint(- DELTA_INPUT_OUTPUT, 0.0);
		result.addPoint(width, 0.0);
		result.addPoint(width, height);
		result.addPoint(- DELTA_INPUT_OUTPUT, height);
		result.addPoint(0, height / 2);
		return result;
	}
	
}


class BoxStyleTimeEvent extends BoxStyle {

	public BoxStyleTimeEvent(String stereotype, char style, double shield) {
		super(stereotype, style, shield);
	}

	@Override
	public void drawMe(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		width -= getShield();
		final Shadowable s = getShape(width, height, roundCorner);
		s.setDeltaShadow(shadowing);
		ug.draw(s);
	}

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
	
}
