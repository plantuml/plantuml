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
package net.sourceforge.plantuml.wire;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class WLinkVertical {

	private final UTranslate start;
	private final double destination;
	private final WLinkType type;
	private final WArrowDirection direction;
	private final HColor color;
	private final Display label;
	private final ISkinParam skinParam;

	public WLinkVertical(ISkinParam skinParam, UTranslate start, double destination, WLinkType type,
			WArrowDirection direction, HColor color, Display label) {
		this.start = start;
		this.destination = destination;
		this.skinParam = skinParam;
		this.direction = direction;
		this.type = type;
		this.label = label;
		this.color = color == null ? HColorUtils.BLACK : color;
	}

	private TextBlock getTextBlock() {
		final FontConfiguration fontConfiguration = FontConfiguration.blackBlueTrue(UFont.sansSerif(10))
				.changeColor(color);
		return label.create(fontConfiguration, HorizontalAlignment.LEFT, skinParam);
	}

	public void drawMe(UGraphic ug) {
		ug = ug.apply(color);
		if (type == WLinkType.NORMAL) {
			ug = ug.apply(color.bg());
			drawNormalArrow(ug);
		} else if (type == WLinkType.BUS) {
			ug = ug.apply(HColorUtils.WHITE.bg());
			drawBusArrow(ug);
		}
	}

	private void drawBusArrow(UGraphic ug) {
		final double dy = destination - start.getDy() - 2;
		final UPath path = new UPath();
		if (direction == WArrowDirection.NONE) {
			path.moveTo(0, 0);
			path.lineTo(0, dy);
			path.lineTo(10, dy);
			path.lineTo(10, 0);
			path.lineTo(0, 0);
			path.closePath();
			ug.apply(start.compose(UTranslate.dy(1))).draw(path);
		}
		if (direction == WArrowDirection.NORMAL) {
			path.moveTo(0, 0);
			path.lineTo(0, dy - 15);
			path.lineTo(-5, dy - 15);
			path.lineTo(5, dy);
			path.lineTo(15, dy - 15);
			path.lineTo(10, dy - 15);
			path.lineTo(10, 0);
			path.lineTo(0, 0);
			path.closePath();
			ug.apply(start.compose(UTranslate.dy(1))).draw(path);
		}
		if (direction == WArrowDirection.BOTH) {
			path.moveTo(5, 0);
			path.lineTo(-5, 15);
			path.lineTo(0, 15);
			path.lineTo(0, dy - 15);
			path.lineTo(-5, dy - 15);
			path.lineTo(5, dy);
			path.lineTo(15, dy - 15);
			path.lineTo(10, dy - 15);
			path.lineTo(10, 15);
			path.lineTo(15, 15);
			path.lineTo(5, 0);
			path.closePath();
			ug.apply(start.compose(UTranslate.dy(1))).draw(path);
		}
		if (direction == WArrowDirection.REVERSE) {
			path.moveTo(5, 0);
			path.lineTo(-5, 15);
			path.lineTo(0, 15);
			path.lineTo(0, dy);
			path.lineTo(10, dy);
			path.lineTo(10, 15);
			path.lineTo(15, 15);
			path.lineTo(5, 0);
			path.closePath();
			ug.apply(start.compose(UTranslate.dy(1))).draw(path);
		}	}

	private void drawNormalArrow(UGraphic ug) {
		final double dy = destination - start.getDy() - 2;
		if (direction == WArrowDirection.BOTH || direction == WArrowDirection.NORMAL) {
			final UPath path = new UPath();
			path.moveTo(0, 0);
			path.lineTo(5, -5);
			path.lineTo(-5, -5);
			path.lineTo(0, 0);
			path.closePath();
			ug.apply(start.compose(UTranslate.dy(dy))).draw(path);
		}
		if (direction == WArrowDirection.BOTH || direction == WArrowDirection.REVERSE) {
			final UPath path = new UPath();
			path.moveTo(0, 0);
			path.lineTo(5, 5);
			path.lineTo(-5, 5);
			path.lineTo(0, 0);
			path.closePath();
			ug.apply(start.compose(UTranslate.dy(1))).draw(path);
		}
		ug.apply(start.compose(UTranslate.dy(1))).draw(ULine.vline(dy));
	}

}
