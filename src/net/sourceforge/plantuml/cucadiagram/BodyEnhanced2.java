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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.EmbeddedDiagram;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.TextBlockVertical2;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class BodyEnhanced2 extends BodyEnhancedAbstract {

	private final Display rawBody;
	private final ISkinSimple skinParam;

	private final LineBreakStrategy lineBreakStrategy;

	BodyEnhanced2(Display rawBody, ISkinSimple skinParam, HorizontalAlignment align, FontConfiguration titleConfig,
			LineBreakStrategy lineBreakStrategy, Style style) {
		super(align, titleConfig, style);
		this.rawBody = rawBody;
		this.lineBreakStrategy = lineBreakStrategy;
		this.skinParam = skinParam;

	}

	@Override
	protected double getMarginX() {
		return 0;
	}

	@Override
	protected TextBlock getArea(StringBounder stringBounder) {
		if (area != null)
			return area;

		// urls.clear();
		final List<TextBlock> blocks = new ArrayList<>();

		char separator = 0;
		TextBlock title = null;
		Display display = Display.empty();
		final Iterator<CharSequence> it = rawBody.iterator();
		while (it.hasNext()) {
			final CharSequence s = it.next();
			final String type = EmbeddedDiagram.getEmbeddedType(StringUtils.trinNoTrace(s));
			if (type != null) {
				display = display.add(s);
				display = addOneSingleLineManageEmbedded2(it, display);
			} else if (isBlockSeparator(s.toString())) {
				blocks.add(decorate(getTextBlock(display), separator, title, stringBounder));
				separator = s.charAt(0);
				title = getTitle(s.toString(), skinParam);
				display = Display.empty();
			} else {
				// if (s instanceof String)
				// s = Guillemet.GUILLEMET.manageGuillemet(s.toString());
				display = display.add(s);
			}
		}

		blocks.add(decorate(getTextBlock(display), separator, title, stringBounder));

		if (blocks.size() == 1)
			this.area = blocks.get(0);
		else
			this.area = new TextBlockVertical2(blocks, align);

		final double minClassWidth = getStyle().value(PName.MinimumWidth).asDouble();
		if (minClassWidth > 0)
			this.area = TextBlockUtils.withMinWidth(this.area, minClassWidth, align);

		return area;
	}

	private static Display addOneSingleLineManageEmbedded2(Iterator<CharSequence> it, Display display) {
		int nested = 1;
		while (it.hasNext()) {
			final CharSequence s = it.next();
			display = display.add(s);
			if (EmbeddedDiagram.getEmbeddedType(StringUtils.trinNoTrace(s)) != null)
				// if (s.getTrimmed().getString().startsWith(EmbeddedDiagram.EMBEDDED_START))
				nested++;
			else if (StringUtils.trinNoTrace(s).equals(EmbeddedDiagram.EMBEDDED_END)) {
				nested--;
				if (nested == 0)
					return display;
			}
		}
		return display;
	}

	private TextBlock getTextBlock(Display display) {
		final TextBlock result = display.create9(titleConfig, align, skinParam, lineBreakStrategy);
		return result;
	}

}
