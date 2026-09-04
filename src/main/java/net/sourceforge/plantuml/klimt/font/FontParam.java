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
package net.sourceforge.plantuml.klimt.font;

import java.util.Arrays;

import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.parser2.StyleQuery;

interface FontParamConstant {
	String FAMILY = "SansSerif";
	String COLOR = "black";
}

public enum FontParam {
	TIMING(12, UFontFace.normal()), //
	ACTIVITY(12, UFontFace.normal()), //
	ACTIVITY_DIAMOND(11, UFontFace.normal()), //
	// ACTIVITY_ARROW(11, UFontFace.normal()), //
	// GENERIC_ARROW(13, UFontFace.normal()), //
	ARROW(13, UFontFace.normal()), //
	CIRCLED_CHARACTER(17, UFontFace.bold(), FontParamConstant.COLOR, "Monospaced"), //
	OBJECT_ATTRIBUTE(10, UFontFace.normal()), //
	OBJECT(12, UFontFace.normal()), //
	OBJECT_STEREOTYPE(12, UFontFace.italic()), //
	CLASS_ATTRIBUTE(10, UFontFace.normal()), //
	CLASS(12, UFontFace.normal()), //
	CLASS_STEREOTYPE(12, UFontFace.italic()), //
	COMPONENT(14, UFontFace.normal()), //
	INTERFACE(14, UFontFace.normal()), //
	INTERFACE_STEREOTYPE(14, UFontFace.italic()), //
	COMPONENT_STEREOTYPE(14, UFontFace.italic()), //
	NOTE(13, UFontFace.normal()), //
	PACKAGE(14, UFontFace.normal()), //
	PACKAGE_STEREOTYPE(14, UFontFace.italic()), //
	ACTOR(14, UFontFace.normal()), //
	ARTIFACT(14, UFontFace.normal()), //
	CLOUD(14, UFontFace.normal()), //
	FOLDER(14, UFontFace.normal()), //
	FILE(14, UFontFace.normal()), //
	FRAME(14, UFontFace.normal()), //
	STORAGE(14, UFontFace.normal()), //
	BOUNDARY(14, UFontFace.normal()), //
	CONTROL(14, UFontFace.normal()), //
	ENTITY(14, UFontFace.normal()), //
	AGENT(14, UFontFace.normal()), //
	RECTANGLE(14, UFontFace.normal()), //
	LABEL(14, UFontFace.normal()), //
	HEXAGON(14, UFontFace.normal()), //
	PERSON(14, UFontFace.normal()), //
	ARCHIMATE(14, UFontFace.normal()), //
	CARD(14, UFontFace.normal()), //
	NODE(14, UFontFace.normal()), //
	DATABASE(14, UFontFace.normal()), //
	QUEUE(14, UFontFace.normal()), //
	STACK(14, UFontFace.normal()), //
	// SEQUENCE_ARROW(13, UFontFace.normal()), //
	SEQUENCE_BOX(13, UFontFace.bold()), //
	SEQUENCE_DIVIDER(13, UFontFace.bold()), //
	SEQUENCE_REFERENCE(13, UFontFace.normal()), //
	SEQUENCE_DELAY(11, UFontFace.normal()), //
	SEQUENCE_GROUP(11, UFontFace.bold()), //
	SEQUENCE_GROUP_HEADER(13, UFontFace.bold()), //
	PARTICIPANT(14, UFontFace.normal()), //
	PARTICIPANT_STEREOTYPE(14, UFontFace.italic()), //
	STATE(14, UFontFace.normal()), //
	STATE_ATTRIBUTE(12, UFontFace.normal()), //
	LEGEND(14, UFontFace.normal()), //
	TITLE(18, UFontFace.normal()), //
	// SEQUENCE_TITLE(14, UFontFace.bold()), //
	CAPTION(14, UFontFace.normal()), //
	SWIMLANE_TITLE(18, UFontFace.normal()), //
	FOOTER(10, UFontFace.normal(), "#888888", FontParamConstant.FAMILY), //
	HEADER(10, UFontFace.normal(), "#888888", FontParamConstant.FAMILY), //
	USECASE(14, UFontFace.normal()), //
	USECASE_STEREOTYPE(14, UFontFace.italic()), //
	ARTIFACT_STEREOTYPE(14, UFontFace.italic()), //
	CLOUD_STEREOTYPE(14, UFontFace.italic()), //
	STORAGE_STEREOTYPE(14, UFontFace.italic()), //
	BOUNDARY_STEREOTYPE(14, UFontFace.italic()), //
	CONTROL_STEREOTYPE(14, UFontFace.italic()), //
	ENTITY_STEREOTYPE(14, UFontFace.italic()), //
	AGENT_STEREOTYPE(14, UFontFace.italic()), //
	RECTANGLE_STEREOTYPE(14, UFontFace.italic()), //
	LABEL_STEREOTYPE(14, UFontFace.italic()), //
	PERSON_STEREOTYPE(14, UFontFace.italic()), //
	HEXAGON_STEREOTYPE(14, UFontFace.italic()), //
	ARCHIMATE_STEREOTYPE(14, UFontFace.italic()), //
	CARD_STEREOTYPE(14, UFontFace.italic()), //
	NODE_STEREOTYPE(14, UFontFace.italic()), //
	FOLDER_STEREOTYPE(14, UFontFace.italic()), //
	FILE_STEREOTYPE(14, UFontFace.italic()), //
	FRAME_STEREOTYPE(14, UFontFace.italic()), //
	DATABASE_STEREOTYPE(14, UFontFace.italic()), //
	QUEUE_STEREOTYPE(14, UFontFace.italic()), //
	STACK_STEREOTYPE(14, UFontFace.italic()), //
	ACTOR_STEREOTYPE(14, UFontFace.italic()), //
	SEQUENCE_STEREOTYPE(14, UFontFace.italic()), //
	PARTITION(14, UFontFace.normal()), DESIGNED_DOMAIN(12, UFontFace.normal()), //
	DESIGNED_DOMAIN_STEREOTYPE(12, UFontFace.italic()), //
	DOMAIN(12, UFontFace.normal()), //
	DOMAIN_STEREOTYPE(12, UFontFace.italic()), //
	MACHINE(12, UFontFace.normal()), //
	MACHINE_STEREOTYPE(12, UFontFace.italic()), //
	REQUIREMENT(12, UFontFace.normal()), //
	REQUIREMENT_STEREOTYPE(12, UFontFace.italic()); //

	private final int defaultSize;
	private final UFontFace defaultFace;
	private final String defaultColor;
	private final String defaultFamily;

	private FontParam(int defaultSize, UFontFace defaultFace, String defaultColor, String defaultFamily) {
		this.defaultSize = defaultSize;
		this.defaultFace = defaultFace;
		this.defaultColor = defaultColor;
		this.defaultFamily = defaultFamily;
	}

	private FontParam(int defaultSize, UFontFace defaultFace) {
		this(defaultSize, defaultFace, FontParamConstant.COLOR, FontParamConstant.FAMILY);
	}

	public final int getDefaultSize(ISkinParam skinParam) {
		if (this == ARROW && skinParam.getDiagramType() == DiagramType.ACTIVITY) {
			return 11;
		}
		if (this == CLASS_ATTRIBUTE) {
			return 11;
		}
		return defaultSize;
	}

	public final UFontFace getDefaultFontFace(ISkinParam skinParam, boolean inPackageTitle) {
		if (this == STATE)
			return defaultFace;

		if (inPackageTitle || this == PACKAGE)
			return UFontFace.bold();

		return defaultFace;
	}

	public final String getDefaultColor() {
		return defaultColor;
	}

	public String getDefaultFamily() {
		return defaultFamily;
	}

	public FontConfiguration getFontConfiguration(ISkinParam skinParam) {
		return FontConfiguration.create(skinParam, this, null);
	}

	public StyleQuery getStyleDefinition(SName diagramType) {
		if (this == FOOTER) {
			return StyleQuery.of(Arrays.asList(SName.root, SName.document, SName.footer));
		}
		if (this == HEADER) {
			return StyleQuery.of(Arrays.asList(SName.root, SName.document, SName.header));
		}
		if (this == TITLE) {
			return StyleQuery.of(Arrays.asList(SName.root, SName.document, SName.title));
		}
		if (this == CLASS_ATTRIBUTE) {
			return StyleQuery.of(Arrays.asList(SName.root, SName.element, SName.classDiagram, SName.class_));
		}
		if (this == RECTANGLE || this == NODE) {
			return StyleQuery.of(Arrays.asList(SName.root, SName.element, SName.componentDiagram, SName.component));
		}
		return StyleQuery.of(Arrays.asList(SName.root, SName.element, diagramType, SName.component));
//		System.err.println("Warning " + this);
//		throw new UnsupportedOperationException();
	}

}
