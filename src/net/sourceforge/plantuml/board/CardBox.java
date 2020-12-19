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
package net.sourceforge.plantuml.board;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class CardBox extends AbstractTextBlock {

	private final Display label;
	private final ISkinParam skinParam;

	public CardBox(Display label, ISkinParam skinParam) {
		this.label = label;
		this.skinParam = skinParam;
	}

//	private StyleSignature getDefaultStyleDefinitionNode() {
//		return StyleSignature.of(SName.root, SName.element, SName.mindmapDiagram, SName.node);
//	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(150, 70);
	}

	public void drawU(UGraphic ug) {
		final URectangle rect = new URectangle(calculateDimension(ug.getStringBounder()));
		rect.setDeltaShadow(1);

		ug.apply(HColorUtils.BLACK).apply(HColorUtils.LIGHT_GRAY.bg()).draw(rect);

		label.create(FontConfiguration.blackBlueTrue(UFont.sansSerif(14)), HorizontalAlignment.LEFT, skinParam)
				.drawU(ug.apply(new UTranslate(3, 3)));

	}

}
