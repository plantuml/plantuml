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
 */
package net.sourceforge.plantuml.nwdiag.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.nwdiag.legacy.NServerLegacy;
import net.sourceforge.plantuml.nwdiag.next.NBox;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class NwGroup {

	public static final HColorSet colors = HColorSet.instance();

	private final Set<String> names = new HashSet<>();

	private final String name;
	private HColor color;
	private String description;
	private NBox nbox;

	public final NBox getNbox(Map<String, NServerLegacy> servers) {
		if (nbox == null) {
			nbox = new NBox();
			for (Entry<String, NServerLegacy> ent : servers.entrySet()) {
				if (names.contains(ent.getKey())) {
					nbox.add(ent.getValue().getBar());
				}
			}
		}
		return nbox;
	}

	public void addName(String name) {
		this.names.add(name);
	}

	@Override
	public String toString() {
		return name;
	}

	public NwGroup(String name) {
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	public final HColor getColor() {
		return color;
	}

	public final void setColor(HColor color) {
		this.color = color;
	}

	public final void setDescription(String value) {
		this.description = value;
	}

	public final FontConfiguration getGroupDescriptionFontConfiguration() {
		final UFont font = UFont.serif(11);
		return new FontConfiguration(font, HColorUtils.BLACK, HColorUtils.BLACK, false);
	}

	protected final String getDescription() {
		return description;
	}

	public final Set<String> names() {
		return Collections.unmodifiableSet(names);
	}

}
