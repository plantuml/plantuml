/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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

import net.sourceforge.plantuml.annotation.HaxeIgnored;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

@HaxeIgnored
public enum ColorParam {
	background(HColorUtils.WHITE, true, ColorType.BACK), 
	hyperlink(HColorUtils.BLUE),

	activityBackground(HColorUtils.MY_YELLOW, true, ColorType.BACK), 
	activityBorder(HColorUtils.MY_RED, ColorType.LINE),

	classBackground(HColorUtils.MY_YELLOW, true, ColorType.BACK),

	classBorder(HColorUtils.MY_RED, ColorType.LINE),

	arrowHead(HColorUtils.MY_RED, null),

	stateBorder(HColorUtils.MY_RED, ColorType.LINE),

	noteBackground(HColorUtils.COL_FBFB77, true, ColorType.BACK), 
	noteBorder(HColorUtils.MY_RED, ColorType.LINE),

	diagramBorder(null, ColorType.LINE),

	actorBackground(HColorUtils.MY_YELLOW, true, ColorType.BACK), 
	actorBorder(HColorUtils.MY_RED, ColorType.LINE),
	sequenceGroupBodyBackground(HColorUtils.RED, true, ColorType.BACK),
	sequenceReferenceHeaderBackground(HColorUtils.COL_EEEEEE, true, ColorType.BACK),
	sequenceReferenceBackground(HColorUtils.WHITE, true, ColorType.BACK),
	sequenceLifeLineBorder(HColorUtils.MY_RED, ColorType.LINE),
	sequenceNewpageSeparator(HColorUtils.BLACK, ColorType.LINE), 
	sequenceBoxBorder(HColorUtils.MY_RED, ColorType.LINE),

	iconPrivate(HColorUtils.COL_C82930), 
	iconPrivateBackground(HColorUtils.COL_F24D5C),
	iconPackage(HColorUtils.COL_1963A0), 
	iconPackageBackground(HColorUtils.COL_4177AF),
	iconProtected(HColorUtils.COL_B38D22), 
	iconProtectedBackground(HColorUtils.COL_FFFF44),
	iconPublic(HColorUtils.COL_038048), 
	iconPublicBackground(HColorUtils.COL_84BE84),
	iconIEMandatory(HColorUtils.BLACK),

	arrowLollipop(HColorUtils.WHITE),

	machineBackground(HColorUtils.WHITE), 
	machineBorder(HColorUtils.BLACK, ColorType.LINE),
	requirementBackground(HColorUtils.WHITE), 
	requirementBorder(HColorUtils.BLACK, ColorType.LINE),
	designedBackground(HColorUtils.WHITE), 
	designedBorder(HColorUtils.BLACK, ColorType.LINE),
	domainBackground(HColorUtils.WHITE), 
	domainBorder(HColorUtils.BLACK, ColorType.LINE),
	lexicalBackground(HColorUtils.WHITE), 
	lexicalBorder(HColorUtils.BLACK, ColorType.LINE),
	biddableBackground(HColorUtils.WHITE), 
	biddableBorder(HColorUtils.BLACK, ColorType.LINE);

	private final boolean isBackground;
	private final HColor defaultValue;
	private final ColorType colorType;

	private ColorParam(HColor defaultValue, ColorType colorType) {
		this(defaultValue, false, colorType);
	}

	private ColorParam(HColor defaultValue) {
		this(defaultValue, false, null);
	}

	private ColorParam() {
		this(null, false, null);
	}

	private ColorParam(boolean isBackground) {
		this(null, isBackground, null);
	}

	private ColorParam(HColor defaultValue, boolean isBackground, ColorType colorType) {
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

	public final HColor getDefaultValue() {
		return defaultValue;
	}

	public ColorType getColorType() {
		return colorType;
	}
}
