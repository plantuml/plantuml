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
package net.sourceforge.plantuml.graphic;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.CornerParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class SkinParameter {

	public static final SkinParameter DATABASE = new SkinParameter(SName.databse, "DATABASE",
			ColorParam.databaseBackground, ColorParam.databaseBorder, FontParam.DATABASE,
			FontParam.DATABASE_STEREOTYPE);

	public static final SkinParameter ARTIFACT = new SkinParameter(SName.artifact, "ARTIFACT",
			ColorParam.artifactBackground, ColorParam.artifactBorder, FontParam.ARTIFACT,
			FontParam.ARTIFACT_STEREOTYPE);

	public static final SkinParameter COMPONENT1 = new SkinParameter(SName.component, "COMPONENT1",
			ColorParam.componentBackground, ColorParam.componentBorder, FontParam.COMPONENT,
			FontParam.COMPONENT_STEREOTYPE, LineParam.componentBorder, CornerParam.component);

	public static final SkinParameter NODE = new SkinParameter(SName.node, "NODE", ColorParam.nodeBackground,
			ColorParam.nodeBorder, FontParam.NODE, FontParam.NODE_STEREOTYPE);

	public static final SkinParameter STORAGE = new SkinParameter(SName.storage, "STORAGE",
			ColorParam.storageBackground, ColorParam.storageBorder, FontParam.STORAGE, FontParam.STORAGE_STEREOTYPE);

	public static final SkinParameter QUEUE = new SkinParameter(SName.queue, "QUEUE", ColorParam.queueBackground,
			ColorParam.queueBorder, FontParam.QUEUE, FontParam.QUEUE_STEREOTYPE);

	public static final SkinParameter STACK = new SkinParameter(SName.stack, "STACK", ColorParam.stackBackground,
			ColorParam.stackBorder, FontParam.STACK, FontParam.STACK_STEREOTYPE);

	public static final SkinParameter CLOUD = new SkinParameter(SName.cloud, "CLOUD", ColorParam.cloudBackground,
			ColorParam.cloudBorder, FontParam.CLOUD, FontParam.CLOUD_STEREOTYPE);

	public static final SkinParameter FRAME = new SkinParameter(SName.frame, "FRAME", ColorParam.frameBackground,
			ColorParam.frameBorder, FontParam.FRAME, FontParam.FRAME_STEREOTYPE);

	public static final SkinParameter COMPONENT2 = new SkinParameter(SName.component, "COMPONENT2",
			ColorParam.componentBackground, ColorParam.componentBorder, FontParam.COMPONENT,
			FontParam.COMPONENT_STEREOTYPE, LineParam.componentBorder, CornerParam.component);

	public static final SkinParameter AGENT = new SkinParameter(SName.agent, "AGENT", ColorParam.agentBackground,
			ColorParam.agentBorder, FontParam.AGENT, FontParam.AGENT_STEREOTYPE, LineParam.agentBorder,
			CornerParam.agent);

	public static final SkinParameter FOLDER = new SkinParameter(SName.folder, "FOLDER", ColorParam.folderBackground,
			ColorParam.folderBorder, FontParam.FOLDER, FontParam.FOLDER_STEREOTYPE);

	public static final SkinParameter FILE = new SkinParameter(SName.file, "FILE", ColorParam.fileBackground,
			ColorParam.fileBorder, FontParam.FILE, FontParam.FILE_STEREOTYPE);

	public static final SkinParameter PACKAGE = new SkinParameter(SName.package_, "PACKAGE",
			ColorParam.packageBackground, ColorParam.packageBorder, FontParam.PACKAGE, FontParam.PACKAGE_STEREOTYPE);

	public static final SkinParameter CARD = new SkinParameter(SName.card, "CARD", ColorParam.cardBackground,
			ColorParam.cardBorder, FontParam.CARD, FontParam.CARD_STEREOTYPE, LineParam.cardBorder, CornerParam.card);

	public static final SkinParameter RECTANGLE = new SkinParameter(SName.rectangle, "RECTANGLE",
			ColorParam.rectangleBackground, ColorParam.rectangleBorder, FontParam.RECTANGLE,
			FontParam.RECTANGLE_STEREOTYPE, LineParam.rectangleBorder, CornerParam.rectangle);

	public static final SkinParameter ARCHIMATE = new SkinParameter(SName.archimate, "ARCHIMATE",
			ColorParam.archimateBackground, ColorParam.archimateBorder, FontParam.ARCHIMATE,
			FontParam.ARCHIMATE_STEREOTYPE, LineParam.archimateBorder, CornerParam.archimate);

	public static final SkinParameter COLLECTIONS = new SkinParameter(SName.collection, "COLLECTIONS",
			ColorParam.collectionsBackground, ColorParam.collectionsBorder, FontParam.RECTANGLE,
			FontParam.RECTANGLE_STEREOTYPE);

	public static final SkinParameter ACTOR = new SkinParameter(SName.actor, "ACTOR", ColorParam.actorBackground,
			ColorParam.actorBorder, FontParam.ACTOR, FontParam.ACTOR_STEREOTYPE);

	public static final SkinParameter USECASE = new SkinParameter(SName.usecase, "USECASE",
			ColorParam.usecaseBackground, ColorParam.usecaseBorder, FontParam.USECASE, FontParam.USECASE_STEREOTYPE);

	public static final SkinParameter BOUNDARY = new SkinParameter(SName.boundary, "BOUNDARY",
			ColorParam.boundaryBackground, ColorParam.boundaryBorder, FontParam.BOUNDARY,
			FontParam.BOUNDARY_STEREOTYPE);

	public static final SkinParameter CONTROL = new SkinParameter(SName.control, "CONTROL",
			ColorParam.controlBackground, ColorParam.controlBorder, FontParam.CONTROL, FontParam.CONTROL_STEREOTYPE);

	public static final SkinParameter ENTITY = new SkinParameter(SName.entity, "ENTITY", ColorParam.entityBackground,
			ColorParam.entityBorder, FontParam.ENTITY, FontParam.ENTITY_STEREOTYPE);

	public static final SkinParameter INTERFACE = new SkinParameter(SName.interface_, "INTERFACE",
			ColorParam.interfaceBackground, ColorParam.interfaceBorder, FontParam.INTERFACE,
			FontParam.INTERFACE_STEREOTYPE);

	public static final SkinParameter PARTICIPANT = new SkinParameter(SName.participant, "PARTICIPANT",
			ColorParam.participantBackground, ColorParam.participantBorder, FontParam.PARTICIPANT,
			FontParam.PARTICIPANT_STEREOTYPE);

	private final ColorParam colorParamBorder;
	private final ColorParam colorParamBack;
	private final FontParam fontParam;
	private final FontParam fontParamStereotype;
	private final String name;
	private final LineParam lineParam;
	private final CornerParam roundParam;
	private final SName styleName;

	public SName getStyleName() {
		return styleName;
	}

	private SkinParameter(SName styleName, String name, ColorParam colorParamBack, ColorParam colorParamBorder,
			FontParam fontParam, FontParam fontParamStereotype, LineParam lineParam, CornerParam roundParam) {
		this.name = name;
		this.styleName = styleName;
		this.colorParamBack = colorParamBack;
		this.colorParamBorder = colorParamBorder;
		this.fontParam = fontParam;
		this.fontParamStereotype = fontParamStereotype;
		this.lineParam = lineParam;
		this.roundParam = roundParam;
	}

	private SkinParameter(SName styleName, String name, ColorParam colorParamBack, ColorParam colorParamBorder,
			FontParam fontParam, FontParam fontParamStereotype) {
		this(styleName, name, colorParamBack, colorParamBorder, fontParam, fontParamStereotype, null,
				CornerParam.DEFAULT);
	}

	public String getUpperCaseName() {
		if (name.endsWith("1") || name.endsWith("2")) {
			return name.substring(0, name.length() - 1);
		}
		return name;
	}

	public ColorParam getColorParamBorder() {
		return colorParamBorder;
	}

	public ColorParam getColorParamBack() {
		return colorParamBack;
	}

	public FontParam getFontParam() {
		return fontParam;
	}

	public FontParam getFontParamStereotype() {
		return fontParamStereotype;
	}

	public double getRoundCorner(ISkinParam skinParam, Stereotype stereotype) {
		return skinParam.getRoundCorner(roundParam, stereotype);
	}

	public double getDiagonalCorner(ISkinParam skinParam, Stereotype stereotype) {
		return skinParam.getDiagonalCorner(roundParam, stereotype);
	}

	public UStroke getStroke(ISkinParam skinParam, Stereotype stereotype) {
		UStroke result = null;
		if (lineParam != null) {
			result = skinParam.getThickness(lineParam, stereotype);
		}
		if (result == null) {
			result = new UStroke(1.5);
		}
		return result;
	}

}