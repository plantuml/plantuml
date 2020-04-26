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
package net.sourceforge.plantuml.sequencediagram;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.sequencediagram.teoz.YPositionedTile;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class LinkAnchor {

	private final String anchor1;
	private final String anchor2;
	private final String message;

	public LinkAnchor(String anchor1, String anchor2, String message) {
		this.anchor1 = anchor1;
		this.anchor2 = anchor2;
		this.message = message;
	}

	@Override
	public String toString() {
		return anchor1 + "<->" + anchor2 + " " + message;
	}

	public final String getAnchor1() {
		return anchor1;
	}

	public final String getAnchor2() {
		return anchor2;
	}

	public final String getMessage() {
		return message;
	}

	public void drawAnchor(UGraphic ug, YPositionedTile tile1, YPositionedTile tile2, ISkinParam param) {

		final StringBounder stringBounder = ug.getStringBounder();
		final double y1 = tile1.getY(stringBounder);
		final double y2 = tile2.getY(stringBounder);
		final double xx1 = tile1.getMiddleX(stringBounder);
		final double xx2 = tile2.getMiddleX(stringBounder);
		final double x = (xx1 + xx2) / 2;
		final double ymin = Math.min(y1, y2);
		final double ymax = Math.max(y1, y2);

		final HColor color = new Rose().getHtmlColor(param, ColorParam.arrow);
		final Rainbow rainbow = Rainbow.fromColor(color, null);
		final Snake snake = new Snake(Arrows.asToUp(), HorizontalAlignment.CENTER, rainbow, Arrows.asToDown());

		final Display display = Display.getWithNewlines(message);
		final TextBlock title = display.create(new FontConfiguration(param, FontParam.ARROW, null),
				HorizontalAlignment.CENTER, param);
		snake.setLabel(title);

		snake.addPoint(x, ymin + 2);
		snake.addPoint(x, ymax - 2);
		snake.drawInternal(ug);
	}

}
