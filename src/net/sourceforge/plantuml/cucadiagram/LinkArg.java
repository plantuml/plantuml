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
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class LinkArg {

	private final Display label;
	private final int length;
	private final String qualifier1;
	private final String qualifier2;
	private final String labeldistance;
	private final String labelangle;
	private final HColor specificColor;
	private VisibilityModifier visibilityModifier;

	public LinkArg(Display label, int length) {
		this(label, length, null, null, null, null, null);
	}

	public LinkArg withQualifier(String qualifier1, String qualifier2) {
		return new LinkArg(label, length, qualifier1, qualifier2, labeldistance, labelangle, specificColor);
	}

	public LinkArg withDistanceAngle(String labeldistance, String labelangle) {
		return new LinkArg(label, length, qualifier1, qualifier2, labeldistance, labelangle, specificColor);
	}

	public LinkArg withColor(HColor specificColor) {
		return new LinkArg(label, length, qualifier1, qualifier2, labeldistance, labelangle, specificColor);
	}

	private LinkArg(Display label, int length, String qualifier1, String qualifier2, String labeldistance,
			String labelangle, HColor specificColor) {

		if (Display.isNull(label)) {
			this.label = Display.NULL;
		} else {
			this.label = label.manageGuillemet();
			if (VisibilityModifier.isVisibilityCharacter(label.get(0)))
				visibilityModifier = VisibilityModifier.getVisibilityModifier(label.get(0), false);
		}

		this.length = length;
		this.qualifier1 = qualifier1;
		this.qualifier2 = qualifier2;
		this.labeldistance = labeldistance;
		this.labelangle = labelangle;
		this.specificColor = specificColor;
	}

	public final Display getLabel() {
		return label;
	}

	public final int getLength() {
		return length;
	}

	public final String getQualifier1() {
		return qualifier1;
	}

	public final String getQualifier2() {
		return qualifier2;
	}

	public final String getLabeldistance() {
		return labeldistance;
	}

	public final String getLabelangle() {
		return labelangle;
	}

	public final HColor getSpecificColor() {
		return specificColor;
	}

	public final VisibilityModifier getVisibilityModifier() {
		return visibilityModifier;
	}
}
