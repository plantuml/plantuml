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
 * Revision $Revision: 6475 $
 * 
 */
package net.sourceforge.plantuml;


public enum ColorParam {
	background,
	
	activityBackground,
	activityBorder,
	activityStart,
	activityEnd,
	activityBar,
	activityArrow,

	usecaseActorBackground,
	usecaseActorBorder,
	usecaseBorder,
	usecaseBackground,
	usecaseArrow,

	objectBackground,
	objectBorder,
	objectArrow,
	
	classBackground,
	classBorder,
	stereotypeCBackground,
	stereotypeABackground,
	stereotypeIBackground,
	stereotypeEBackground,
	classArrow,
	
	packageBackground,
	packageBorder,

	componentBackground,
	componentBorder,
	componentInterfaceBackground,
	componentInterfaceBorder,
	componentArrow,

	stateBackground,
	stateBorder,
	stateArrow,
	stateStart,
	stateEnd,

	noteBackground(true),
	noteBorder,
	
	sequenceActorBackground(true),
	sequenceActorBorder,
	sequenceGroupBorder,
	sequenceGroupBackground(true),
	sequenceReferenceBackground(true),
	sequenceDividerBackground(true),
	sequenceLifeLineBackground(true),
	sequenceLifeLineBorder,
	sequenceParticipantBackground(true),
	sequenceParticipantBorder,
	sequenceArrow,
	sequenceBoxBorder,
	sequenceBoxBackground(true),
	
	iconPrivate,
	iconPrivateBackground,
	iconPackage,
	iconPackageBackground,
	iconProtected,
	iconProtectedBackground,
	iconPublic,
	iconPublicBackground;
	
	private final boolean isBackground;
	
	private ColorParam() {
		this(false);
	}
	
	private ColorParam(boolean isBackground) {
		this.isBackground = isBackground;
	}

	protected boolean isBackground() {
		return isBackground;
	}
	
	
}
