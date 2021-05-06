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
package net.sourceforge.plantuml.nwdiag;

import java.awt.geom.Dimension2D;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class NwGroup {

	public static final HColorSet colors = HColorSet.instance();

	private final String name;
	private final Network network;
	private final Set<String> elements = new HashSet<String>();
	private HColor color;
	private String description;

	@Override
	public String toString() {
		return name + " " + network + " " + elements;
	}

	public NwGroup(String name, Network network) {
		this.name = name;
		this.network = network;
	}

	public int size() {
		return elements.size();
	}

	public final String getName() {
		return name;
	}

	public void addElement(String name) {
		this.elements.add(name);
	}

	public boolean matches(LinkedElement tested) {
		if (network != null && network != tested.getNetwork()) {
			return false;
		}
		return elements.contains(tested.getElement().getName());
	}

	public final HColor getColor() {
		return color;
	}

	public final void setColor(HColor color) {
		this.color = color;
	}

	public void setDescription(String value) {
		this.description = value;
	}

	public void drawGroup(UGraphic ug, MinMax size, ISkinParam skinParam) {
		TextBlock block = null;
		Dimension2D blockDim = null;
		if (description != null) {
			block = Display.getWithNewlines(description).create(getGroupDescriptionFontConfiguration(),
					HorizontalAlignment.LEFT, skinParam);
			blockDim = block.calculateDimension(ug.getStringBounder());
			final double dy = size.getMinY() - blockDim.getHeight();
			size = size.addPoint(size.getMinX(), dy);
		}
		HColor color = getColor();
		if (color == null) {
			color = colors.getColorOrWhite(skinParam.getThemeStyle(), "#AAA");
		}
		size.draw(ug, color);

		if (block != null) {
			block.drawU(ug.apply(new UTranslate(size.getMinX() + 5, size.getMinY())));
		}
	}

	private FontConfiguration getGroupDescriptionFontConfiguration() {
		final UFont font = UFont.serif(11);
		return new FontConfiguration(font, HColorUtils.BLACK, HColorUtils.BLACK, false);
	}

	public final Network getNetwork() {
		return network;
	}

}
