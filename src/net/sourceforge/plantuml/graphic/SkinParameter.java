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
package net.sourceforge.plantuml.graphic;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Stereotype;

public class SkinParameter {

	public static final SkinParameter DATABASE = new SkinParameter("DATABASE", ColorParam.databaseBackground,
			ColorParam.databaseBorder, FontParam.DATABASE, FontParam.DATABASE_STEREOTYPE);

	public static final SkinParameter ARTIFACT = new SkinParameter("ARTIFACT", ColorParam.artifactBackground,
			ColorParam.artifactBorder, FontParam.ARTIFACT, FontParam.ARTIFACT_STEREOTYPE);

	public static final SkinParameter COMPONENT1 = new SkinParameter("COMPONENT1", ColorParam.componentBackground,
			ColorParam.componentBorder, FontParam.COMPONENT, FontParam.COMPONENT_STEREOTYPE);

	public static final SkinParameter NODE = new SkinParameter("NODE", ColorParam.nodeBackground,
			ColorParam.nodeBorder, FontParam.NODE, FontParam.NODE_STEREOTYPE);

	public static final SkinParameter STORAGE = new SkinParameter("STORAGE", ColorParam.storageBackground,
			ColorParam.storageBorder, FontParam.STORAGE, FontParam.STORAGE_STEREOTYPE);

	public static final SkinParameter QUEUE = new SkinParameter("QUEUE", ColorParam.queueBackground,
			ColorParam.queueBorder, FontParam.QUEUE, FontParam.QUEUE_STEREOTYPE);

	public static final SkinParameter CLOUD = new SkinParameter("CLOUD", ColorParam.cloudBackground,
			ColorParam.cloudBorder, FontParam.CLOUD, FontParam.CLOUD_STEREOTYPE);

	public static final SkinParameter FRAME = new SkinParameter("FRAME", ColorParam.frameBackground,
			ColorParam.frameBorder, FontParam.FRAME, FontParam.FRAME_STEREOTYPE);

	public static final SkinParameter COMPONENT2 = new SkinParameter("COMPONENT2", ColorParam.componentBackground,
			ColorParam.componentBorder, FontParam.COMPONENT, FontParam.COMPONENT_STEREOTYPE);

	public static final SkinParameter AGENT = new SkinParameter("AGENT", ColorParam.agentBackground,
			ColorParam.agentBorder, FontParam.AGENT, FontParam.AGENT_STEREOTYPE);

	public static final SkinParameter FOLDER = new SkinParameter("FOLDER", ColorParam.folderBackground,
			ColorParam.folderBorder, FontParam.FOLDER, FontParam.FOLDER_STEREOTYPE);

	public static final SkinParameter FILE = new SkinParameter("FILE", ColorParam.fileBackground,
			ColorParam.fileBorder, FontParam.FILE, FontParam.FILE_STEREOTYPE);

	public static final SkinParameter PACKAGE = new SkinParameter("PACKAGE", ColorParam.packageBackground,
			ColorParam.packageBorder, FontParam.FOLDER, FontParam.FOLDER_STEREOTYPE);

	public static final SkinParameter CARD = new SkinParameter("CARD", ColorParam.rectangleBackground,
			ColorParam.rectangleBorder, FontParam.RECTANGLE, FontParam.RECTANGLE_STEREOTYPE);

	public static final SkinParameter RECTANGLE = new SkinParameter("RECTANGLE", ColorParam.rectangleBackground,
			ColorParam.rectangleBorder, FontParam.RECTANGLE, FontParam.RECTANGLE_STEREOTYPE);

	public static final SkinParameter ACTOR = new SkinParameter("ACTOR", ColorParam.actorBackground,
			ColorParam.actorBorder, FontParam.ACTOR, FontParam.ACTOR_STEREOTYPE);

	public static final SkinParameter BOUNDARY = new SkinParameter("BOUNDARY", ColorParam.boundaryBackground,
			ColorParam.boundaryBorder, FontParam.BOUNDARY, FontParam.BOUNDARY_STEREOTYPE);

	public static final SkinParameter CONTROL = new SkinParameter("CONTROL", ColorParam.controlBackground,
			ColorParam.controlBorder, FontParam.CONTROL, FontParam.CONTROL_STEREOTYPE);

	public static final SkinParameter ENTITY_DOMAIN = new SkinParameter("ENTITY_DOMAIN", ColorParam.entityBackground,
			ColorParam.entityBorder, FontParam.ENTITY, FontParam.ENTITY_STEREOTYPE);

	public static final SkinParameter INTERFACE = new SkinParameter("INTERFACE", ColorParam.interfaceBackground,
			ColorParam.interfaceBorder, FontParam.INTERFACE, FontParam.INTERFACE_STEREOTYPE);

	private final ColorParam colorParamBorder;
	private final ColorParam colorParamBack;
	private final FontParam fontParam;
	private final FontParam fontParamStereotype;
	private final String name;

	private SkinParameter(String name, ColorParam colorParamBack, ColorParam colorParamBorder, FontParam fontParam,
			FontParam fontParamStereotype) {
		this.name = name;
		this.colorParamBack = colorParamBack;
		this.colorParamBorder = colorParamBorder;
		this.fontParam = fontParam;
		this.fontParamStereotype = fontParamStereotype;
	}

	public String getUpperCaseName() {
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
		return skinParam.getRoundCorner(name.toLowerCase(), stereotype);
	}

}