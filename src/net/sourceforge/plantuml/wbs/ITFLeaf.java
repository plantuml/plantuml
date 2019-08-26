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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBox;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class ITFLeaf extends AbstractTextBlock implements ITF {

	private final TextBlock box;

	public ITFLeaf(Style style, ISkinParam skinParam, Display label, IdeaShape shape) {
		// this.skinParam = skinParam;

		if (shape == IdeaShape.BOX) {
			this.box = FtileBox.createWbs(style, skinParam, label);
		} else {
			final UFont font = skinParam.getFont(null, false, FontParam.ACTIVITY);
			final TextBlock text = label.create(FontConfiguration.blackBlueTrue(font), HorizontalAlignment.LEFT,
					skinParam);
			this.box = TextBlockUtils.withMargin(text, 0, 3, 1, 1);
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return box.calculateDimension(stringBounder);
	}

	public void drawU(UGraphic ug) {
		box.drawU(ug);
	}

	public Point2D getT1(StringBounder stringBounder) {
		final Dimension2D dim = calculateDimension(stringBounder);
		return new Point2D.Double(dim.getWidth() / 2, 0);
	}

	public Point2D getT2(StringBounder stringBounder) {
		final Dimension2D dim = calculateDimension(stringBounder);
		return new Point2D.Double(dim.getWidth() / 2, dim.getHeight());
	}

	public Point2D getF1(StringBounder stringBounder) {
		final Dimension2D dim = calculateDimension(stringBounder);
		return new Point2D.Double(0, dim.getHeight() / 2);
	}

	public Point2D getF2(StringBounder stringBounder) {
		final Dimension2D dim = calculateDimension(stringBounder);
		return new Point2D.Double(dim.getWidth(), dim.getHeight() / 2);
	}

}
