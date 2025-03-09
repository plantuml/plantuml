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
package net.sourceforge.plantuml.abel;

import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class LinkArg {

	private final Display label;
	private final String quantifier1;
	private final String quantifier2;
	private final String labeldistance;
	private final String labelangle;

	private final String kal1;
	private final String kal2;

	private VisibilityModifier visibilityModifier;
	private int length;

	public static LinkArg build(final Display label, int length) {
		return build(label, length, true);
	}

	public static LinkArg noDisplay(int length) {
		return build(Display.NULL, length, true);
	}

	public static LinkArg build(final Display label, int length, boolean manageVisibilityModifier) {
		VisibilityModifier visibilityModifier = null;
		final Display newLabel;
		if (Display.isNull(label)) {
			newLabel = Display.NULL;
		} else {
			newLabel = label.manageGuillemet(manageVisibilityModifier);
			if (manageVisibilityModifier && VisibilityModifier.isVisibilityCharacter(label.get(0)))
				visibilityModifier = VisibilityModifier.getVisibilityModifier(label.get(0), false);
		}
		return new LinkArg(newLabel, length, null, null, null, null, visibilityModifier, null, null);
	}

	public LinkArg withQuantifier(String quantifier1, String quantifier2) {
		return new LinkArg(label, length, quantifier1, quantifier2, labeldistance, labelangle, visibilityModifier, kal1,
				kal2);
	}

	public LinkArg withKal(String kal1, String kal2) {
		return new LinkArg(label, length, quantifier1, quantifier2, labeldistance, labelangle, visibilityModifier, kal1,
				kal2);
	}

	public LinkArg withDistanceAngle(String labeldistance, String labelangle) {
		return new LinkArg(label, length, quantifier1, quantifier2, labeldistance, labelangle, visibilityModifier, kal1,
				kal2);
	}

	private LinkArg(Display label, int length, String quantifier1, String quantifier2, String labeldistance,
			String labelangle, VisibilityModifier visibilityModifier, String kal1, String kal2) {

		this.label = label;
		this.visibilityModifier = visibilityModifier;
		this.length = length;
		this.quantifier1 = quantifier1;
		this.quantifier2 = quantifier2;
		this.labeldistance = labeldistance;
		this.labelangle = labelangle;
		this.kal1 = kal1;
		this.kal2 = kal2;
	}

	public LinkArg getInv() {
		return new LinkArg(label, length, quantifier2, quantifier1, labeldistance, labelangle, visibilityModifier, kal2,
				kal1);
	}

	public final Display getLabel() {
		return label;
	}

	public final int getLength() {
		return length;
	}

	public final String getQuantifier1() {
		return quantifier1;
	}

	public final String getQuantifier2() {
		return quantifier2;
	}

	public final String getLabeldistance() {
		return labeldistance;
	}

	public final String getLabelangle() {
		return labelangle;
	}

	public final VisibilityModifier getVisibilityModifier() {
		return visibilityModifier;
	}

	public final void setVisibilityModifier(VisibilityModifier visibilityModifier) {
		this.visibilityModifier = visibilityModifier;
	}

	public final void setLength(int length) {
		this.length = length;
	}

	public final String getKal1() {
		return kal1;
	}

	public final String getKal2() {
		return kal2;
	}

}
