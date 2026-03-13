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
package net.sourceforge.plantuml.emoji;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.plantuml.UgSimpleDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;

public class PSystemListEmoji extends UgSimpleDiagram {

	private final String text;

	public PSystemListEmoji(UmlSource source, String text, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
		this.text = text;
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return getTextBlock().calculateDimension(stringBounder);
	}

	@Override
	public void drawU(UGraphic ug) {
		getTextBlock().drawU(ug);
	}

	private TextBlock getTextBlock() {
		final TextBlock header = GraphicStrings
				.createBlackOnWhite(Arrays.asList("<b><size:16>Emoji available on Unicode Block " + text,
						"(Blocks available: 26, 27, 1F3, 1F4, 1F5, 1F6, 1F9)"));

		final Map<String, Emoji> some = new TreeMap<>();
		for (Map.Entry<String, Emoji> ent : Emoji.getAll().entrySet())
			if (ent.getKey().startsWith(text))
				some.put(ent.getKey(), ent.getValue());

		final List<TextBlock> items = new ArrayList<>();
		for (Map.Entry<String, Emoji> ent : some.entrySet()) {
			final String code = ent.getKey();
			final String shortcut = ent.getValue().getShortcut();
			final StringBuilder sb = new StringBuilder();
			sb.append("<size:13>");
			sb.append("\"\"<U+003C>:" + code + ":<U+003E> \"\"");
			sb.append("<:" + code + ":>");
			sb.append(" ");
			sb.append("<#0:" + code + ":>");
			if (shortcut != null) {
				sb.append(" ");
				sb.append("\"\"<U+003C>:" + shortcut + ":<U+003E> \"\"");
			}
			items.add(GraphicStrings.createBlackOnWhite(Arrays.asList(sb.toString())));
		}

		final int third = (items.size() + 2) / 3;

		return new TextBlock() {
			@Override
			public void drawU(UGraphic ug) {
				header.drawU(ug);
				final StringBounder stringBounder = ug.getStringBounder();
				ug = ug.apply(UTranslate.dy(header.calculateDimension(stringBounder).getHeight()));
				final UGraphic top = ug;
				int i = 0;
				for (TextBlock item : items) {
					item.drawU(ug);
					ug = ug.apply(UTranslate.dy(item.calculateDimension(stringBounder).getHeight()));
					i++;
					if (i == third)
						ug = top.apply(UTranslate.dx(500));
					if (i == 2 * third)
						ug = top.apply(UTranslate.dx(1000));
				}
			}

			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final double headerHeight = header.calculateDimension(stringBounder).getHeight();
				double col1Height = 0;
				double col2Height = 0;
				double col3Height = 0;
				int i = 0;
				for (TextBlock item : items) {
					final double h = item.calculateDimension(stringBounder).getHeight();
					if (i < third)
						col1Height += h;
					else if (i < 2 * third)
						col2Height += h;
					else
						col3Height += h;
					i++;
				}
				final double maxColHeight = Math.max(col1Height, Math.max(col2Height, col3Height));
				return new XDimension2D(1500, headerHeight + maxColHeight);
			}
		};
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(List Emoji)");
	}

}
