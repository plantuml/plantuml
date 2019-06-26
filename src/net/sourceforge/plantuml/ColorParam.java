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

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.color.ColorType;


public enum ColorParam {
	background(HtmlColorUtils.WHITE, true, ColorType.BACK),
	hyperlink(HtmlColorUtils.BLUE),
	
	activityDiamondBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	activityDiamondBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	activityBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	activityBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	activityStart(HtmlColorUtils.BLACK),
	activityEnd(HtmlColorUtils.BLACK),
	activityBar(HtmlColorUtils.BLACK),
	swimlaneBorder(HtmlColorUtils.BLACK),
	swimlaneTitleBackground(null),
	
	usecaseBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	usecaseBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),

	objectBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	objectBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	
	classHeaderBackground(null, true, ColorType.BACK),
	classBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	enumBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	classBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	stereotypeCBackground(HtmlColorUtils.COL_ADD1B2),
	stereotypeNBackground(HtmlColorUtils.COL_E3664A),
	stereotypeABackground(HtmlColorUtils.COL_A9DCDF),
	stereotypeIBackground(HtmlColorUtils.COL_B4A7E5),
	stereotypeEBackground(HtmlColorUtils.COL_EB937F),
	stereotypeCBorder(null),
	stereotypeNBorder(null),
	stereotypeABorder(null),
	stereotypeIBorder(null),
	stereotypeEBorder(null),
		
	packageBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	packageBorder(HtmlColorUtils.BLACK, ColorType.LINE),

	partitionBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	partitionBorder(HtmlColorUtils.BLACK, ColorType.LINE),

	componentBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	componentBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	interfaceBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	interfaceBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	arrow(HtmlColorUtils.MY_RED, ColorType.ARROW),

	stateBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	stateBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	stateStart(HtmlColorUtils.BLACK),
	stateEnd(HtmlColorUtils.BLACK),

	noteBackground(HtmlColorUtils.COL_FBFB77, true, ColorType.BACK),
	noteBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	
	legendBackground(HtmlColorUtils.COL_DDDDDD, true, ColorType.BACK),
	legendBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	
	titleBackground(null, true, ColorType.BACK),
	titleBorder(null, ColorType.LINE),

	diagramBorder(null, ColorType.LINE),
	
	actorBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	actorBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	participantBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	participantBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	sequenceGroupBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	sequenceGroupBackground(HtmlColorUtils.COL_EEEEEE, true, ColorType.BACK),
	sequenceGroupBodyBackground(HtmlColorUtils.RED, true, ColorType.BACK),
	sequenceReferenceBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	sequenceReferenceHeaderBackground(HtmlColorUtils.COL_EEEEEE, true, ColorType.BACK),
	sequenceReferenceBackground(HtmlColorUtils.WHITE, true, ColorType.BACK),
	sequenceDividerBackground(HtmlColorUtils.COL_EEEEEE, true, ColorType.BACK),
	sequenceDividerBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	sequenceLifeLineBackground(HtmlColorUtils.WHITE, true, ColorType.BACK),
	sequenceLifeLineBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	sequenceNewpageSeparator(HtmlColorUtils.BLACK, ColorType.LINE),
	sequenceBoxBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	sequenceBoxBackground(HtmlColorUtils.COL_DDDDDD, true, ColorType.BACK),
	
	artifactBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	artifactBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	cloudBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	cloudBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	queueBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	queueBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	stackBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	stackBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	databaseBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	databaseBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	folderBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	folderBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	fileBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	fileBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	frameBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	frameBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	nodeBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	nodeBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	rectangleBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	rectangleBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	archimateBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	archimateBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	cardBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	cardBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	agentBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	agentBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	storageBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	storageBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	boundaryBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	boundaryBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	collectionsBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	collectionsBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	controlBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	controlBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	entityBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	entityBorder(HtmlColorUtils.MY_RED, ColorType.LINE),

	
	iconPrivate(HtmlColorUtils.COL_C82930),
	iconPrivateBackground(HtmlColorUtils.COL_F24D5C),
	iconPackage(HtmlColorUtils.COL_1963A0),
	iconPackageBackground(HtmlColorUtils.COL_4177AF),
	iconProtected(HtmlColorUtils.COL_B38D22),
	iconProtectedBackground(HtmlColorUtils.COL_FFFF44),
	iconPublic(HtmlColorUtils.COL_038048),
	iconPublicBackground(HtmlColorUtils.COL_84BE84),
	iconIEMandatory(HtmlColorUtils.BLACK),
	
	arrowLollipop(HtmlColorUtils.WHITE),
	
	machineBackground(HtmlColorUtils.WHITE), 
	machineBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	requirementBackground(HtmlColorUtils.WHITE), 
	requirementBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	designedBackground(HtmlColorUtils.WHITE), 
	designedBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	domainBackground(HtmlColorUtils.WHITE), 
	domainBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	lexicalBackground(HtmlColorUtils.WHITE), 
	lexicalBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	biddableBackground(HtmlColorUtils.WHITE), 
	biddableBorder(HtmlColorUtils.BLACK, ColorType.LINE);

	private final boolean isBackground;
	private final HtmlColor defaultValue;
	private final ColorType colorType;
	
	private ColorParam(HtmlColor defaultValue, ColorType colorType) {
		this(defaultValue, false, colorType);
	}
	
	private ColorParam(HtmlColor defaultValue) {
		this(defaultValue, false, null);
	}
	
	private ColorParam() {
		this(null, false, null);
	}
	
	private ColorParam(boolean isBackground) {
		this(null, isBackground, null);
	}
	
	private ColorParam(HtmlColor defaultValue, boolean isBackground, ColorType colorType) {
		this.isBackground = isBackground;
		this.defaultValue = defaultValue;
		this.colorType = colorType;
		if (colorType == ColorType.BACK && isBackground == false) {
			System.err.println(this);
			throw new IllegalStateException();
		}
	}

	protected boolean isBackground() {
		return isBackground;
	}

	public final HtmlColor getDefaultValue() {
		return defaultValue;
	}

	public ColorType getColorType() {
		return colorType;
	}
}
