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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public abstract class WithLinkType {

	protected LinkType type;
	protected boolean hidden = false;
	private boolean single = false;

	private Colors colors = Colors.empty();

	private List<Colors> supplementary = new ArrayList<Colors>();

	public final HColor getSpecificColor() {
		return colors.getColor(ColorType.LINE);
	}

	public final void setSpecificColor(HColor specificColor) {
		setSpecificColor(specificColor, 0);
	}

	public final void setSpecificColor(HColor specificColor, int i) {
		if (i == 0) {
			colors = colors.add(ColorType.LINE, specificColor);
		} else {
			supplementary.add(colors.add(ColorType.LINE, specificColor));
		}
	}

	public List<Colors> getSupplementaryColors() {
		return Collections.unmodifiableList(supplementary);
	}

	public void setColors(Colors colors) {
		this.colors = colors;
	}

	public final Colors getColors() {
		return colors;
	}

	final public void goDashed() {
		type = type.goDashed();
	}

	final public void goDotted() {
		type = type.goDotted();
	}

	final public void goThickness(double thickness) {
		type = type.goThickness(thickness);
	}

	final public void goHidden() {
		this.hidden = true;
	}

	public abstract void goNorank();

	final public void goBold() {
		type = type.goBold();
	}

	public final void goSingle() {
		this.single = true;
	}

	public boolean isSingle() {
		return single;
	}

	public void applyStyle(String arrowStyle) {
		if (arrowStyle == null) {
			return;
		}
		final StringTokenizer st = new StringTokenizer(arrowStyle, ";");
		int i = 0;
		while (st.hasMoreTokens()) {
			final String s = st.nextToken();
			applyOneStyle(s, i);
			i++;
		}
	}

	private void applyOneStyle(String arrowStyle, int i) {
		final StringTokenizer st = new StringTokenizer(arrowStyle, ",");
		while (st.hasMoreTokens()) {
			final String s = st.nextToken();
			if (s.equalsIgnoreCase("dashed")) {
				this.goDashed();
			} else if (s.equalsIgnoreCase("bold")) {
				this.goBold();
			} else if (s.equalsIgnoreCase("dotted")) {
				this.goDotted();
			} else if (s.equalsIgnoreCase("hidden")) {
				this.goHidden();
			} else if (s.equalsIgnoreCase("single")) {
				this.goSingle();
			} else if (s.equalsIgnoreCase("plain")) {
				// Do nothing
			} else if (s.equalsIgnoreCase("norank")) {
				this.goNorank();
			} else if (s.startsWith("thickness=")) {
				this.goThickness(Double.parseDouble(s.substring("thickness=".length())));
			} else {
				final HColor tmp = HColorSet.instance().getColorIfValid(s);
				setSpecificColor(tmp, i);
			}
		}
	}

	public LinkType getType() {
		return type;
	}

}
