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

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.nwdiag.next.NBox;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class NwGroup implements NStackable {

	private final Set<String> names = new HashSet<>();

	private final String name;
	private HColor color;
	private String description;
	private NBox nbox;

	public NBox getNboxInternal() {
		if (nbox == null) {
			nbox = new NBox();
		}
		return nbox;
	}

	public final NBox getNbox(Map<String, ? extends NServer> servers) {
		if (nbox == null) {
			nbox = new NBox();
			for (Entry<String, ? extends NServer> ent : servers.entrySet()) {
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
		return "NwGroup:" + name + " " + names + " " + nbox;
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

	@Override
	public final void setColor(HColor color) {
		this.color = color;
	}

	@Override
	public final void setDescription(String value) {
		this.description = value;
	}

	protected final String getDescription() {
		return description;
	}

	public final Set<String> names() {
		return Collections.unmodifiableSet(names);
	}

	public boolean contains(NServer server) {
		return names.contains(server.getName());
	}

	public double getTopHeaderHeight(StringBounder stringBounder, ISkinParam skinParam) {
		final TextBlock block = buildHeaderName(skinParam);
		if (block == null) {
			return 0;
		}
		final Dimension2D blockDim = block.calculateDimension(stringBounder);
		return blockDim.getHeight();
	}

	private StyleSignature getStyleDefinition() {
		return StyleSignature.of(SName.root, SName.element, SName.nwdiagDiagram, SName.group);
	}

	public void drawGroup(UGraphic ug, MinMax size, ISkinParam skinParam) {
		final StyleBuilder styleBuilder = skinParam.getCurrentStyleBuilder();
		final Style style = getStyleDefinition().getMergedStyle(styleBuilder);
		final TextBlock block = buildHeaderName(skinParam);
		if (block != null) {
			final Dimension2D blockDim = block.calculateDimension(ug.getStringBounder());
			final double dy = size.getMinY() - blockDim.getHeight();
			size = size.addPoint(size.getMinX(), dy);
		}
		HColor color = getColor();
		if (color == null) {
			color = style.value(PName.BackGroundColor).asColor(skinParam.getThemeStyle(), skinParam.getIHtmlColorSet());

		}
		size.draw(ug, color);

		if (block != null) {
			block.drawU(ug.apply(new UTranslate(size.getMinX() + 5, size.getMinY())));
		}
	}

	private TextBlock buildHeaderName(ISkinParam skinParam) {
		if (getDescription() == null) {
			return null;
		}
		final StyleBuilder styleBuilder = skinParam.getCurrentStyleBuilder();
		final Style style = getStyleDefinition().getMergedStyle(styleBuilder);
		return Display.getWithNewlines(getDescription()).create(
				style.getFontConfiguration(skinParam.getThemeStyle(), skinParam.getIHtmlColorSet()),
				HorizontalAlignment.LEFT, skinParam);
	}

}
