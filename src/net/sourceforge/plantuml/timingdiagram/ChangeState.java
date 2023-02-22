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
package net.sourceforge.plantuml.timingdiagram;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class ChangeState implements Comparable<ChangeState> {

	private final TimeTick when;
	private final String[] states;
	private final String comment;
	private final Colors colors;

	public ChangeState(TimeTick when, String comment, Colors colors, String... states) {
		if (states.length == 0)
			throw new IllegalArgumentException();

		this.when = when;
		this.states = states;
		this.comment = comment;
		this.colors = colors;
	}

	public int compareTo(ChangeState other) {
		return this.when.compareTo(other.when);
	}

	public final TimeTick getWhen() {
		return when;
	}

	public final String getState() {
		return states[0];
	}

	public final List<String> getStates() {
		return Arrays.asList(states);
	}

	public String getComment() {
		return comment;
	}

	public final HColor getBackColor(ISkinParam skinParam, Style style) {
		if (colors == null || colors.getColor(ColorType.BACK) == null)
			return style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());

		return colors.getColor(ColorType.BACK);
	}

	private final HColor getLineColor(ISkinParam skinParam, Style style) {
		if (colors == null || colors.getColor(ColorType.LINE) == null)
			return style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());

		return colors.getColor(ColorType.LINE);
	}

	private UStroke getStroke(Style style) {
		return style.getStroke();
	}

	public Fashion getContext(ISkinParam skinParam, Style style) {
		return new Fashion(getBackColor(skinParam, style), getLineColor(skinParam, style)).withStroke(getStroke(style));
	}

	public final boolean isBlank() {
		return states[0].equals("{...}");
	}

	public final boolean isCompletelyHidden() {
		return states[0].equals("{hidden}");
	}

	public final boolean isFlat() {
		return states[0].equals("{-}");
	}

	// public final boolean isUnknown() {
	// return states[0].equals("{?}");
	// }

}
