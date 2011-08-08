/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6590 $
 *
 */
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.StickMan;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class ComponentBlueModernActor extends AbstractTextualComponent {

	private final StickMan stickman;
	private final boolean head;

	public ComponentBlueModernActor(HtmlColor backgroundColor, HtmlColor foregroundColor, HtmlColor fontColor, UFont font,
			List<? extends CharSequence> stringsToDisplay, boolean head) {
		super(stringsToDisplay, fontColor, font, HorizontalAlignement.LEFT, 3, 3, 0);
		this.head = head;
		stickman = new StickMan(backgroundColor, foregroundColor);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Dimension2D dimensionToUse) {
		ug.getParam().setColor(getFontColor());
		final TextBlock textBlock = getTextBlock();
		final StringBounder stringBounder = ug.getStringBounder();
		final double delta = (getPreferredWidth(stringBounder) - stickman.getPreferredWidth(stringBounder)) / 2;

		if (head) {
			textBlock.drawU(ug, getTextMiddlePostion(stringBounder), stickman.getPreferredHeight(stringBounder));
			ug.translate(delta, 0);
		} else {
			textBlock.drawU(ug, getTextMiddlePostion(stringBounder), 0);
			ug.translate(delta, getTextHeight(stringBounder));
		}
		stickman.drawU(ug);

	}

	private double getTextMiddlePostion(StringBounder stringBounder) {
		return (getPreferredWidth(stringBounder) - getTextWidth(stringBounder)) / 2.0;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return stickman.getPreferredHeight(stringBounder) + getTextHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return Math.max(stickman.getPreferredWidth(stringBounder), getTextWidth(stringBounder));
	}

}
