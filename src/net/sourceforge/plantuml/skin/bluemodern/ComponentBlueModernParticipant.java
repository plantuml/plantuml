/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 15908 $
 *
 */
package net.sourceforge.plantuml.skin.bluemodern;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont2;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentBlueModernParticipant extends AbstractTextualComponent {

	private final int shadowview = 3;
	private final HtmlColor blue1;
	private final HtmlColor blue2;

	public ComponentBlueModernParticipant(HtmlColor blue1, HtmlColor blue2, UFont2 font,
			Display stringsToDisplay, ISkinSimple spriteContainer) {
		super(stringsToDisplay, font, HorizontalAlignment.CENTER, 7, 7, 7,
				spriteContainer, 0, false, null, null);
		this.blue1 = blue1;
		this.blue2 = blue2;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final StringBounder stringBounder = ug.getStringBounder();

		final ShadowShape shadowShape = new ShadowShape(getTextWidth(stringBounder), getTextHeight(stringBounder), 10);
		final UGraphic ugShadow = ug.apply(new UTranslate(shadowview, shadowview)).apply(new UChangeColor(null));
		// ug.translate(shadowview, shadowview);
		shadowShape.drawU(ugShadow);
		// ug.translate(-shadowview, -shadowview);

		final FillRoundShape shape = new FillRoundShape(getTextWidth(stringBounder), getTextHeight(stringBounder),
				blue1, blue2, 10);
		shape.drawU(ug);

		getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), getMarginY())));
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + shadowview;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder);
	}

}
