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
package net.sourceforge.plantuml.wbs;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBox;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public abstract class WBSTextBlock extends AbstractTextBlock {

	protected final ISkinParam skinParam;

	public WBSTextBlock(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	final protected HtmlColor getLinkColor() {
		return ColorParam.activityBorder.getDefaultValue();
	}

	final protected void drawLine(UGraphic ug, Point2D p1, Point2D p2) {
		final ULine line = new ULine(p1, p2);
		ug.apply(new UTranslate(p1)).apply(new UChangeColor(getLinkColor())).draw(line);
	}

	final protected void drawLine(UGraphic ug, double x1, double y1, double x2, double y2) {
		drawLine(ug, new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
	}

	final protected TextBlock buildMain(WElement idea) {
		Display label = idea.getLabel();
		final UFont font = skinParam.getFont(null, false, FontParam.ACTIVITY);
		
		if (idea.getShape() == IdeaShape.BOX) {
			final FtileBox box = new FtileBox(Colors.empty().mute(skinParam), label, font, null, BoxStyle.SDL_TASK);
			return box;
		}
		
		final TextBlock text = label.create(FontConfiguration.blackBlueTrue(font), HorizontalAlignment.LEFT, skinParam);
		// if (direction == Direction.RIGHT) {
		// return TextBlockUtils.withMargin(text, 3, 0, 1, 1);
		// }
		return TextBlockUtils.withMargin(text, 0, 3, 1, 1);
	}



}
