/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 11985 $
 *
 */
package net.sourceforge.plantuml;

import java.awt.Font;

interface FontParamConstant {
	String FAMILY = "SansSerif";
	String COLOR = "black";
}

public enum FontParam {
	ACTIVITY(14, Font.PLAIN), //
	// ACTIVITY_ARROW(13, Font.PLAIN), //
	GENERIC_ARROW(13, Font.PLAIN), //
	ACTIVITY2(12, Font.PLAIN), //
	ACTIVITY_ARROW2(11, Font.PLAIN), //
	CIRCLED_CHARACTER(17, Font.BOLD, FontParamConstant.COLOR, "Monospaced"), //
	// OBJECT_ARROW(10, Font.PLAIN), //
	OBJECT_ATTRIBUTE(10, Font.PLAIN), //
	OBJECT(12, Font.PLAIN), //
	OBJECT_STEREOTYPE(12, Font.ITALIC), //
	// CLASS_ARROW(10, Font.PLAIN), //
	CLASS_ATTRIBUTE(10, Font.PLAIN), //
	CLASS(12, Font.PLAIN), //
	CLASS_STEREOTYPE(12, Font.ITALIC), //
	COMPONENT(14, Font.PLAIN), //
	COMPONENT_STEREOTYPE(14, Font.ITALIC), //
	// COMPONENT_ARROW(13, Font.PLAIN), //
	NOTE(13, Font.PLAIN), //
	PACKAGE(14, Font.PLAIN), //
	SEQUENCE_ACTOR(13, Font.PLAIN), //
	SEQUENCE_ARROW(13, Font.PLAIN), //
	SEQUENCE_BOX(13, Font.BOLD), //
	SEQUENCE_DIVIDER(13, Font.BOLD), //
	SEQUENCE_REFERENCE(13, Font.PLAIN), //
	SEQUENCE_DELAY(11, Font.PLAIN), //
	SEQUENCE_GROUP(11, Font.BOLD), //
	SEQUENCE_GROUP_HEADER(13, Font.BOLD), //
	SEQUENCE_PARTICIPANT(13, Font.PLAIN), //
	SEQUENCE_TITLE(13, Font.BOLD), //
	STATE(14, Font.PLAIN), //
	// STATE_ARROW(13, Font.PLAIN), //
	STATE_ATTRIBUTE(12, Font.PLAIN), //
	LEGEND(14, Font.PLAIN), //
	TITLE(18, Font.PLAIN), //
	FOOTER(10, Font.PLAIN, "#888888", FontParamConstant.FAMILY), //
	HEADER(10, Font.PLAIN, "#888888", FontParamConstant.FAMILY), //
	USECASE(14, Font.PLAIN), //
	USECASE_STEREOTYPE(14, Font.ITALIC), //
	USECASE_ACTOR(14, Font.PLAIN), //
	USECASE_ACTOR_STEREOTYPE(14, Font.ITALIC); //
	// USECASE_ARROW(13, Font.PLAIN, FontParamConstant.COLOR, FontParamConstant.FAMILY);

	private final int defaultSize;
	private final int fontStyle;
	private final String defaultColor;
	private final String defaultFamily;

	private FontParam(int defaultSize, int fontStyle, String defaultColor, String defaultFamily) {
		this.defaultSize = defaultSize;
		this.fontStyle = fontStyle;
		this.defaultColor = defaultColor;
		this.defaultFamily = defaultFamily;
	}

	private FontParam(int defaultSize, int fontStyle) {
		this(defaultSize, fontStyle, FontParamConstant.COLOR, FontParamConstant.FAMILY);
	}

	public final int getDefaultSize(ISkinParam skinParam) {
		if (this == CLASS_ATTRIBUTE) {
			return 11;
		}
		return defaultSize;
	}

	public final int getDefaultFontStyle(ISkinParam skinParam) {
		if (this == PACKAGE) {
			return Font.BOLD;
		}
		return fontStyle;
	}

	public final String getDefaultColor() {
		return defaultColor;
	}

	public String getDefaultFamily() {
		return defaultFamily;
	}

}
