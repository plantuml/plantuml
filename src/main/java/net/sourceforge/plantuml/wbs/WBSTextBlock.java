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
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

abstract class WBSTextBlock extends AbstractTextBlock {

	protected final ISkinParam skinParam;
	private final Stereotype stereotype;
	private final StyleBuilder styleBuilder;
	private final int level;

	public WBSTextBlock(ISkinParam skinParam, StyleBuilder styleBuilder, int level, Stereotype stereotype) {
		this.skinParam = skinParam;
		this.styleBuilder = styleBuilder;
		this.level = level;
		this.stereotype = stereotype;
	}

	private Style getStyleUsed() {
		final StyleSignature signature = StyleSignatureBasic.of(SName.root, SName.element, SName.wbsDiagram, SName.arrow)
				.addLevel(level).withTOBECHANGED(stereotype);
		return signature.getMergedStyle(styleBuilder);
	}

	final protected void drawLine(UGraphic ug, double x1, double y1, double x2, double y2) {
		ug = getStyleUsed().applyStrokeAndLineColor(ug, skinParam.getIHtmlColorSet());
		final XPoint2D p1 = new XPoint2D(Math.min(x1, x2), y1);
		final XPoint2D p2 = new XPoint2D(Math.max(x1, x2), y2);
		final ULine line = ULine.create(p1, p2);
		ug = ug.apply(UTranslate.point(p1));
		ug.draw(line);
	}

	final protected TextBlock buildMain(WElement idea) {
		final Display label = idea.getLabel();
		final Style style = idea.getStyle();
		if (idea.getShape() == IdeaShape.BOX)
			return FtileBoxOld.createWbs(style, idea.withBackColor(skinParam), label);

		final TextBlock text = label.create0(style.getFontConfiguration(skinParam.getIHtmlColorSet()),
				style.getHorizontalAlignment(), skinParam, style.wrapWidth(), CreoleMode.FULL, null, null);
		return TextBlockUtils.withMargin(text, 0, 3, 1, 1);
	}

}
