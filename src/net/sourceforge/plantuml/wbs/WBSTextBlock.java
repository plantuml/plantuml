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
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBox;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

abstract class WBSTextBlock extends AbstractTextBlock {

	protected final ISkinParam skinParam;
	private final StyleBuilder styleBuilder;
	private final int level;

	public WBSTextBlock(ISkinParam skinParam, StyleBuilder styleBuilder, int level) {
		this.skinParam = skinParam;
		this.styleBuilder = styleBuilder;
		this.level = level;
	}

	final protected void drawLine(UGraphic ug, Point2D p1, Point2D p2) {
		final ULine line = new ULine(p1, p2);
		if (SkinParam.USE_STYLES()) {
			getStyleUsed().applyStrokeAndLineColor(ug.apply(new UTranslate(p1)), skinParam.getIHtmlColorSet()).draw(
					line);
		} else {
			final HColor color = ColorParam.activityBorder.getDefaultValue();
			ug.apply(new UTranslate(p1)).apply(color).draw(line);
		}
	}

	private Style getStyleUsed() {
		return getDefaultStyleDefinitionArrow().getMergedStyle(styleBuilder);
	}

	final protected void drawLine(UGraphic ug, double x1, double y1, double x2, double y2) {
		drawLine(ug, new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
	}

	final public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.wbsDiagram, SName.arrow).add(SName.depth(level));
	}

	final protected TextBlock buildMain(WElement idea) {
		Display label = idea.getLabel();
		final UFont font = skinParam.getFont(null, false, FontParam.ACTIVITY);

		if (idea.getShape() == IdeaShape.BOX) {
			final FtileBox box = FtileBox.createWbs(idea.getStyle(), skinParam, label);
			return box;
		}

		final TextBlock text = label.create(FontConfiguration.blackBlueTrue(font), HorizontalAlignment.LEFT, skinParam);
		return TextBlockUtils.withMargin(text, 0, 3, 1, 1);
	}

}
