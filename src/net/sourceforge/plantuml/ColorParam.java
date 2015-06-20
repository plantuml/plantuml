/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 16364 $
 * 
 */
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;


public enum ColorParam {
	background(HtmlColorUtils.WHITE),
	hyperlink(HtmlColorUtils.BLUE),
	
	activityBackground(HtmlColorUtils.MY_YELLOW),
	activityBorder(HtmlColorUtils.MY_RED),
	activityStart(HtmlColorUtils.BLACK),
	activityEnd(HtmlColorUtils.BLACK),
	activityBar(HtmlColorUtils.BLACK),
	activityArrow(HtmlColorUtils.MY_RED),
	
	usecaseBorder(HtmlColorUtils.MY_RED),
	usecaseBackground(HtmlColorUtils.MY_YELLOW),
	usecaseArrow(HtmlColorUtils.MY_RED),

	objectBackground(HtmlColorUtils.MY_YELLOW),
	objectBorder(HtmlColorUtils.MY_RED),
	objectArrow(HtmlColorUtils.MY_RED),
	
	classHeaderBackground(null),
	classBackground(HtmlColorUtils.MY_YELLOW),
	classBorder(HtmlColorUtils.MY_RED),
	stereotypeCBackground(HtmlColorUtils.COL_ADD1B2),
	stereotypeABackground(HtmlColorUtils.COL_A9DCDF),
	stereotypeIBackground(HtmlColorUtils.COL_B4A7E5),
	stereotypeEBackground(HtmlColorUtils.COL_EB937F),
	classArrow(HtmlColorUtils.MY_RED),
		
	packageBackground(HtmlColorUtils.MY_YELLOW),
	packageBorder(HtmlColorUtils.BLACK),

	partitionBackground(HtmlColorUtils.MY_YELLOW),
	partitionBorder(HtmlColorUtils.BLACK),

	componentBackground(HtmlColorUtils.MY_YELLOW),
	componentBorder(HtmlColorUtils.MY_RED),
	interfaceBackground(HtmlColorUtils.MY_YELLOW),
	interfaceBorder(HtmlColorUtils.MY_RED),
	// componentArrow,

	stateBackground(HtmlColorUtils.MY_YELLOW),
	stateBorder(HtmlColorUtils.MY_RED),
	stateArrow(HtmlColorUtils.MY_RED),
	stateStart(HtmlColorUtils.BLACK),
	stateEnd(HtmlColorUtils.BLACK),

	noteBackground(HtmlColorUtils.COL_FBFB77, true),
	noteBorder(HtmlColorUtils.MY_RED),
	
	legendBackground(HtmlColorUtils.COL_DDDDDD, true),
	legendBorder(HtmlColorUtils.BLACK),
	
	actorBackground(HtmlColorUtils.MY_YELLOW, true),
	actorBorder(HtmlColorUtils.MY_RED),
	participantBackground(HtmlColorUtils.MY_YELLOW, true),
	participantBorder(HtmlColorUtils.MY_RED),
	sequenceGroupBorder(HtmlColorUtils.BLACK),
	sequenceGroupBackground(HtmlColorUtils.COL_EEEEEE, true),
	sequenceReferenceBorder(HtmlColorUtils.BLACK),
	sequenceReferenceHeaderBackground(HtmlColorUtils.COL_EEEEEE, true),
	sequenceReferenceBackground(HtmlColorUtils.WHITE, true),
	sequenceDividerBackground(HtmlColorUtils.COL_EEEEEE, true),
	sequenceLifeLineBackground(HtmlColorUtils.WHITE, true),
	sequenceLifeLineBorder(HtmlColorUtils.MY_RED),
	sequenceArrow(HtmlColorUtils.MY_RED),
	sequenceBoxBorder(HtmlColorUtils.MY_RED),
	sequenceBoxBackground(HtmlColorUtils.COL_DDDDDD, true),
	
	artifactBackground(HtmlColorUtils.MY_YELLOW),
	artifactBorder(HtmlColorUtils.BLACK),
	cloudBackground(HtmlColorUtils.MY_YELLOW),
	cloudBorder(HtmlColorUtils.BLACK),
	queueBackground(HtmlColorUtils.MY_YELLOW),
	queueBorder(HtmlColorUtils.MY_RED),
	databaseBackground(HtmlColorUtils.MY_YELLOW),
	databaseBorder(HtmlColorUtils.BLACK),
	folderBackground(HtmlColorUtils.MY_YELLOW),
	folderBorder(HtmlColorUtils.BLACK),
	frameBackground(HtmlColorUtils.MY_YELLOW),
	frameBorder(HtmlColorUtils.BLACK),
	nodeBackground(HtmlColorUtils.MY_YELLOW),
	nodeBorder(HtmlColorUtils.BLACK),
	rectangleBackground(HtmlColorUtils.MY_YELLOW),
	rectangleBorder(HtmlColorUtils.BLACK),
	agentBackground(HtmlColorUtils.MY_YELLOW),
	agentBorder(HtmlColorUtils.MY_RED),
	storageBackground(HtmlColorUtils.MY_YELLOW),
	storageBorder(HtmlColorUtils.BLACK),
	boundaryBackground(HtmlColorUtils.MY_YELLOW),
	boundaryBorder(HtmlColorUtils.MY_RED),
	controlBackground(HtmlColorUtils.MY_YELLOW),
	controlBorder(HtmlColorUtils.MY_RED),
	entityBackground(HtmlColorUtils.MY_YELLOW),
	entityBorder(HtmlColorUtils.MY_RED),

	
	iconPrivate(HtmlColorUtils.COL_C82930),
	iconPrivateBackground(HtmlColorUtils.COL_F24D5C),
	iconPackage(HtmlColorUtils.COL_1963A0),
	iconPackageBackground(HtmlColorUtils.COL_4177AF),
	iconProtected(HtmlColorUtils.COL_B38D22),
	iconProtectedBackground(HtmlColorUtils.COL_FFFF44),
	iconPublic(HtmlColorUtils.COL_038048),
	iconPublicBackground(HtmlColorUtils.COL_84BE84);
	
	private final boolean isBackground;
	private final HtmlColor defaultValue;
	
	private ColorParam(HtmlColor defaultValue) {
		this(defaultValue, false);
	}
	
	private ColorParam() {
		this(null, false);
	}
	
	private ColorParam(boolean isBackground) {
		this(null, isBackground);
	}
	
	private ColorParam(HtmlColor defaultValue, boolean isBackground) {
		this.isBackground = isBackground;
		this.defaultValue = defaultValue;
	}

	protected boolean isBackground() {
		return isBackground;
	}

	public final HtmlColor getDefaultValue() {
		return defaultValue;
	}
}
