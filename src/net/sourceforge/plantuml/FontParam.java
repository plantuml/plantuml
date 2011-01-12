/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 5942 $
 *
 */
package net.sourceforge.plantuml;

import java.awt.Font;

public enum FontParam {

	ACTIVITY(14, Font.PLAIN, "black", null),
	ACTIVITY_ARROW(13, Font.PLAIN, "black", null),
	CIRCLED_CHARACTER(17, Font.BOLD, "black", "Courier"),
	OBJECT_ARROW(10, Font.PLAIN, "black", null),
	OBJECT_ATTRIBUTE(10, Font.PLAIN, "black", null),
	OBJECT(12, Font.PLAIN, "black", null),
	OBJECT_STEREOTYPE(12, Font.ITALIC, "black", null),
	CLASS_ARROW(10, Font.PLAIN, "black", null),
	CLASS_ATTRIBUTE(10, Font.PLAIN, "black", null),
	CLASS(12, Font.PLAIN, "black", null),
	CLASS_STEREOTYPE(12, Font.ITALIC, "black", null),
	COMPONENT(14, Font.PLAIN, "black", null),
	COMPONENT_STEREOTYPE(14, Font.ITALIC, "black", null),
	COMPONENT_ARROW(13, Font.PLAIN, "black", null),
	NOTE(13, Font.PLAIN, "black", null),
	PACKAGE(14, Font.PLAIN, "black", null),
	SEQUENCE_ACTOR(13, Font.PLAIN, "black", null),
	SEQUENCE_ARROW(13, Font.PLAIN, "black", null),
	SEQUENCE_ENGLOBER(13, Font.BOLD, "black", null),
	SEQUENCE_DIVIDER(13, Font.BOLD, "black", null),
	SEQUENCE_DELAY(11, Font.PLAIN, "black", null),
	SEQUENCE_GROUPING(11, Font.BOLD, "black", null),
	SEQUENCE_GROUPING_HEADER(13, Font.BOLD, "black", null),
	SEQUENCE_PARTICIPANT(13, Font.PLAIN, "black", null),
	SEQUENCE_TITLE(13, Font.BOLD, "black", null),
	STATE(14, Font.PLAIN, "black", null),
	STATE_ARROW(13, Font.PLAIN, "black", null),
	STATE_ATTRIBUTE(12, Font.PLAIN, "black", null),
	TITLE(18, Font.PLAIN, "black", null),
	FOOTER(10, Font.PLAIN, "#888888", null),
	HEADER(10, Font.PLAIN, "#888888", null),
	USECASE(14, Font.PLAIN, "black", null),
	USECASE_STEREOTYPE(14, Font.ITALIC, "black", null),
	USECASE_ACTOR(14, Font.PLAIN, "black", null),
	USECASE_ACTOR_STEREOTYPE(14, Font.ITALIC, "black", null),
	USECASE_ARROW(13, Font.PLAIN, "black", null);
	
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

	public final int getDefaultSize() {
		return defaultSize;
	}

	public final int getDefaultFontStyle() {
		return fontStyle;
	}

	public final String getDefaultColor() {
		return defaultColor;
	}

	public String getDefaultFamily() {
		return defaultFamily;
	}
	
	
}
