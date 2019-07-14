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
package net.sourceforge.plantuml.style;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.IHtmlColorSet;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class Style {

	private final Map<PName, Value> map;
	private final String name;
	private final StyleKind kind;

	public Style(StyleKind kind, String name, Map<PName, Value> map) {
		this.map = map;
		this.name = name;
		this.kind = kind;
	}

	public final StyleKind getKind() {
		return kind;
	}

	@Override
	public String toString() {
		return name + " " + map;
	}

	public Value value(PName name) {
		final Value result = map.get(name);
		if (result == null) {
			return ValueNull.NULL;
		}
		return result;
	}

	public Style mergeWith(Style other) {
		if (other == null) {
//			System.err.println("other="+other);
//			return this;
			throw new IllegalArgumentException();
		}
		final EnumMap<PName, Value> both = new EnumMap<PName, Value>(this.map);
		for (Entry<PName, Value> ent : other.map.entrySet()) {
			final Value previous = this.map.get(ent.getKey());
			if (previous == null || ent.getValue().getPriority() > previous.getPriority()) {
				both.put(ent.getKey(), ent.getValue());
			}
		}
		if (this.name.equals(other.name)) {
			return new Style(this.kind.add(other.kind), this.name, both);
		}
		return new Style(this.kind.add(other.kind), this.name + "," + other.name, both);
	}

	// public Style mergeWith(List<Style> others) {
	// Style result = this;
	// for (Style other : others) {
	// result = result.mergeWith(other);
	// }
	// return result;
	// }

	public Style eventuallyOverride(PName param, HtmlColor color) {
		if (color == null) {
			return this;
		}
		final EnumMap<PName, Value> result = new EnumMap<PName, Value>(this.map);
		result.put(param, new ValueColor(color));
		return new Style(kind, name + "-" + color, result);
	}

	public Style eventuallyOverride(Colors colors) {
		Style result = this;
		final HtmlColor back = colors.getColor(ColorType.BACK);
		if (back != null) {
			result = this.eventuallyOverride(PName.BackGroundColor, back);
		}
		return result;
	}

	public String getStyleName() {
		return name;
	}

	public UFont getUFont() {
		final String family = value(PName.FontName).asString();
		final int fontStyle = value(PName.FontStyle).asFontStyle();
		final int size = value(PName.FontSize).asInt();
		return new UFont(family, fontStyle, size);
	}

	public FontConfiguration getFontConfiguration(IHtmlColorSet set) {
		final UFont font = getUFont();
		final HtmlColor color = value(PName.FontColor).asColor(set);
		final HtmlColor hyperlinkColor = value(PName.HyperLinkColor).asColor(set);
		return new FontConfiguration(font, color, hyperlinkColor, true);
	}

	public SymbolContext getSymbolContext(IHtmlColorSet set) {
		final HtmlColor backColor = value(PName.BackGroundColor).asColor(set);
		final HtmlColor foreColor = value(PName.LineColor).asColor(set);
		final double deltaShadowing = value(PName.Shadowing).asDouble();
		return new SymbolContext(backColor, foreColor).withStroke(getStroke()).withDeltaShadow(deltaShadowing);
	}

	public UStroke getStroke() {
		final double thickness = value(PName.LineThickness).asDouble();
		return new UStroke(thickness);
	}

	private TextBlock createTextBlockInternal(Display display, IHtmlColorSet set, ISkinSimple spriteContainer) {
		final FontConfiguration fc = getFontConfiguration(set);
		return display.create(fc, HorizontalAlignment.LEFT, spriteContainer);
	}

	public TextBlock createTextBlockBordered(Display note, IHtmlColorSet set, ISkinSimple spriteContainer) {
		final TextBlock textBlock = this.createTextBlockInternal(note, set, spriteContainer);

		final HtmlColor legendBackgroundColor = this.value(PName.BackGroundColor).asColor(set);
		final HtmlColor legendColor = this.value(PName.LineColor).asColor(set);
		final UStroke stroke = this.getStroke();
		final int cornersize = this.value(PName.RoundCorner).asInt();
		final double margin = this.value(PName.Margin).asDouble();
		final double padding = this.value(PName.Padding).asDouble();

		final TextBlock result = TextBlockUtils.bordered(textBlock, stroke, legendColor, legendBackgroundColor,
				cornersize, padding, padding);
		return TextBlockUtils.withMargin(result, margin, margin);
	}

}