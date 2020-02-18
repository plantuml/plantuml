/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml;

import java.awt.Font;

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignature;

interface FontParamConstant {
	String FAMILY = "SansSerif";
	String COLOR = "black";
}

public enum FontParam {
	TIMING(12, Font.PLAIN), //
	ACTIVITY(12, Font.PLAIN), //
	ACTIVITY_DIAMOND(11, Font.PLAIN), //
	// ACTIVITY_ARROW(11, Font.PLAIN), //
	// GENERIC_ARROW(13, Font.PLAIN), //
	ARROW(13, Font.PLAIN), //
	CIRCLED_CHARACTER(17, Font.BOLD, FontParamConstant.COLOR, "Monospaced"), //
	OBJECT_ATTRIBUTE(10, Font.PLAIN), //
	OBJECT(12, Font.PLAIN), //
	OBJECT_STEREOTYPE(12, Font.ITALIC), //
	CLASS_ATTRIBUTE(10, Font.PLAIN), //
	CLASS(12, Font.PLAIN), //
	CLASS_STEREOTYPE(12, Font.ITALIC), //
	COMPONENT(14, Font.PLAIN), //
	INTERFACE(14, Font.PLAIN), //
	INTERFACE_STEREOTYPE(14, Font.ITALIC), //
	COMPONENT_STEREOTYPE(14, Font.ITALIC), //
	NOTE(13, Font.PLAIN), //
	PACKAGE(14, Font.PLAIN), //
	PACKAGE_STEREOTYPE(14, Font.ITALIC), //
	ACTOR(14, Font.PLAIN), //
	ARTIFACT(14, Font.PLAIN), //
	CLOUD(14, Font.PLAIN), //
	FOLDER(14, Font.PLAIN), //
	FILE(14, Font.PLAIN), //
	FRAME(14, Font.PLAIN), //
	STORAGE(14, Font.PLAIN), //
	BOUNDARY(14, Font.PLAIN), //
	CONTROL(14, Font.PLAIN), //
	ENTITY(14, Font.PLAIN), //
	AGENT(14, Font.PLAIN), //
	RECTANGLE(14, Font.PLAIN), //
	ARCHIMATE(14, Font.PLAIN), //
	CARD(14, Font.PLAIN), //
	NODE(14, Font.PLAIN), //
	DATABASE(14, Font.PLAIN), //
	QUEUE(14, Font.PLAIN), //
	STACK(14, Font.PLAIN), //
	// SEQUENCE_ARROW(13, Font.PLAIN), //
	SEQUENCE_BOX(13, Font.BOLD), //
	SEQUENCE_DIVIDER(13, Font.BOLD), //
	SEQUENCE_REFERENCE(13, Font.PLAIN), //
	SEQUENCE_DELAY(11, Font.PLAIN), //
	SEQUENCE_GROUP(11, Font.BOLD), //
	SEQUENCE_GROUP_HEADER(13, Font.BOLD), //
	PARTICIPANT(14, Font.PLAIN), //
	PARTICIPANT_STEREOTYPE(14, Font.ITALIC), //
	STATE(14, Font.PLAIN), //
	STATE_ATTRIBUTE(12, Font.PLAIN), //
	LEGEND(14, Font.PLAIN), //
	TITLE(18, Font.PLAIN), //
	// SEQUENCE_TITLE(14, Font.BOLD), //
	CAPTION(14, Font.PLAIN), //
	SWIMLANE_TITLE(18, Font.PLAIN), //
	FOOTER(10, Font.PLAIN, "#888888", FontParamConstant.FAMILY), //
	HEADER(10, Font.PLAIN, "#888888", FontParamConstant.FAMILY), //
	USECASE(14, Font.PLAIN), //
	USECASE_STEREOTYPE(14, Font.ITALIC), //
	ARTIFACT_STEREOTYPE(14, Font.ITALIC), //
	CLOUD_STEREOTYPE(14, Font.ITALIC), //
	STORAGE_STEREOTYPE(14, Font.ITALIC), //
	BOUNDARY_STEREOTYPE(14, Font.ITALIC), //
	CONTROL_STEREOTYPE(14, Font.ITALIC), //
	ENTITY_STEREOTYPE(14, Font.ITALIC), //
	AGENT_STEREOTYPE(14, Font.ITALIC), //
	RECTANGLE_STEREOTYPE(14, Font.ITALIC), //
	ARCHIMATE_STEREOTYPE(14, Font.ITALIC), //
	CARD_STEREOTYPE(14, Font.ITALIC), //
	NODE_STEREOTYPE(14, Font.ITALIC), //
	FOLDER_STEREOTYPE(14, Font.ITALIC), //
	FILE_STEREOTYPE(14, Font.ITALIC), //
	FRAME_STEREOTYPE(14, Font.ITALIC), //
	DATABASE_STEREOTYPE(14, Font.ITALIC), //
	QUEUE_STEREOTYPE(14, Font.ITALIC), //
	STACK_STEREOTYPE(14, Font.ITALIC), //
	ACTOR_STEREOTYPE(14, Font.ITALIC), //
	SEQUENCE_STEREOTYPE(14, Font.ITALIC), //
	PARTITION(14, Font.PLAIN), DESIGNED_DOMAIN(12, Font.PLAIN), //
	DESIGNED_DOMAIN_STEREOTYPE(12, Font.ITALIC), //
	DOMAIN(12, Font.PLAIN), //
	DOMAIN_STEREOTYPE(12, Font.ITALIC), //
	MACHINE(12, Font.PLAIN), //
	MACHINE_STEREOTYPE(12, Font.ITALIC), //
	REQUIREMENT(12, Font.PLAIN), //
	REQUIREMENT_STEREOTYPE(12, Font.ITALIC); //

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
		if (this == ARROW && skinParam.getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			return 11;
		}
		if (this == CLASS_ATTRIBUTE) {
			return 11;
		}
		return defaultSize;
	}

	public final int getDefaultFontStyle(ISkinParam skinParam, boolean inPackageTitle) {
		if (this == STATE) {
			return fontStyle;
		}
		if (inPackageTitle || this == PACKAGE) {
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

	public FontConfiguration getFontConfiguration(ISkinParam skinParam) {
		return new FontConfiguration(skinParam, this, null);
	}

	public StyleSignature getStyleDefinition() {
		if (this == FOOTER) {
			return StyleSignature.of(SName.root, SName.footer);
		}
		if (this == HEADER) {
			return StyleSignature.of(SName.root, SName.header);
		}
		if (this == TITLE) {
			return StyleSignature.of(SName.root, SName.title);
		}
		System.err.println("Warning " + this);
		return null;
	}

}
