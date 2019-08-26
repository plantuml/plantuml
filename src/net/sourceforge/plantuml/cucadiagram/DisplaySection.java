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

import java.util.EnumMap;
import java.util.Map;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;

public class DisplaySection {

	private final Map<HorizontalAlignment, Display> map = new EnumMap<HorizontalAlignment, Display>(
			HorizontalAlignment.class);

	private DisplaySection() {
	}

	public DisplaySection withPage(int page, int lastpage) {
		final DisplaySection result = new DisplaySection();
		for (Map.Entry<HorizontalAlignment, Display> ent : this.map.entrySet()) {
			result.map.put(ent.getKey(), ent.getValue().withPage(page, lastpage));
		}
		return result;
	}

	public Display getDisplay() {
		if (map.size() == 0) {
			return null;
		}
		return map.values().iterator().next();
	}

	public static DisplaySection none() {
		return new DisplaySection();
	}

	public final HorizontalAlignment getHorizontalAlignment() {
		if (map.size() == 0) {
			return HorizontalAlignment.CENTER;
		}
		return map.keySet().iterator().next();
	}

	public boolean isNull() {
		if (map.size() == 0) {
			return true;
		}
		final Display display = map.values().iterator().next();
		return Display.isNull(display);
	}

	public TextBlock createRibbon(FontConfiguration fontConfiguration, ISkinSimple spriteContainer) {
		if (map.size() == 0) {
			return null;
		}
		final Display display = map.values().iterator().next();
		if (Display.isNull(display) || display.size() == 0) {
			return null;
		}
		// if (SkinParam.USE_STYLES()) {
		// throw new UnsupportedOperationException();
		// }
		return display.create(fontConfiguration, getHorizontalAlignment(), spriteContainer);
	}

	public void putDisplay(Display display, HorizontalAlignment horizontalAlignment) {
		this.map.put(horizontalAlignment, display);

	}

}
