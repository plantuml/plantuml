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
package net.sourceforge.plantuml.wbs;

import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBoxOld;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.AbstractCommonUGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;

class ITFLeaf extends AbstractTextBlock implements ITF {

	private final TextBlock box;
	private final WElement idea;

	public ITFLeaf(WElement idea, ISkinParam skinParam) {
		final IdeaShape shape = idea.getShape();
		final Style style = idea.getStyle();
		final Display label = idea.getLabel();
		this.idea = idea;
		if (shape == IdeaShape.BOX) {
			this.box = FtileBoxOld.createWbs(style, skinParam, label);
		} else {
			final TextBlock text = label.create0(style.getFontConfiguration(skinParam.getIHtmlColorSet()),
					style.getHorizontalAlignment(), skinParam, style.wrapWidth(), CreoleMode.FULL, null, null);
			this.box = TextBlockUtils.withMargin(text, 0, 3, 1, 1);
		}
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return box.calculateDimension(stringBounder);
	}

	public void drawU(UGraphic ug) {
		if (ug instanceof AbstractCommonUGraphic) {
			final UTranslate translate = ((AbstractCommonUGraphic) ug).getTranslate();
			idea.setGeometry(translate, calculateDimension(ug.getStringBounder()));
		}
		box.drawU(ug);
	}

	public XPoint2D getT1(StringBounder stringBounder) {
		final XDimension2D dim = calculateDimension(stringBounder);
		return new XPoint2D(dim.getWidth() / 2, 0);
	}

	public XPoint2D getT2(StringBounder stringBounder) {
		final XDimension2D dim = calculateDimension(stringBounder);
		return new XPoint2D(dim.getWidth() / 2, dim.getHeight());
	}

	public XPoint2D getF1(StringBounder stringBounder) {
		final XDimension2D dim = calculateDimension(stringBounder);
		return new XPoint2D(0, dim.getHeight() / 2);
	}

	public XPoint2D getF2(StringBounder stringBounder) {
		final XDimension2D dim = calculateDimension(stringBounder);
		return new XPoint2D(dim.getWidth(), dim.getHeight() / 2);
	}

}
