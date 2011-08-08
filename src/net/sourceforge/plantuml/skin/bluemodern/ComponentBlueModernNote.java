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
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;

final public class ComponentBlueModernNote extends AbstractTextualComponent {

	private final int shadowview = 4;
	private final int cornersize = 10;
	private final HtmlColor back;
	private final HtmlColor foregroundColor;

	public ComponentBlueModernNote(HtmlColor back, HtmlColor foregroundColor, HtmlColor fontColor, UFont font,
			List<? extends CharSequence> strings) {
		super(strings, fontColor, font, HorizontalAlignement.LEFT, 6, 15, 5);
		this.back = back;
		this.foregroundColor = foregroundColor;
	}

	@Override
	final public double getPreferredWidth(StringBounder stringBounder) {
		final double result = getTextWidth(stringBounder) + 2 * getPaddingX();
		return result;
	}

	@Override
	final public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 2 * getPaddingY();
	}

	@Override
	public double getPaddingX() {
		return 9;
	}

	@Override
	public double getPaddingY() {
		return 9;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Dimension2D dimensionToUse) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double textHeight = getTextHeight(stringBounder);

		final double textWidth = getTextWidth(stringBounder);

		final ShadowShape shadowShape = new ShadowShape(textWidth, textHeight, 3);
		ug.translate(shadowview, shadowview);
		shadowShape.drawU(ug);
		ug.translate(-shadowview, -shadowview);

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, textHeight);
		polygon.addPoint(textWidth, textHeight);
		polygon.addPoint(textWidth, cornersize);
		polygon.addPoint(textWidth - cornersize, 0);
		polygon.addPoint(0, 0);

		ug.getParam().setBackcolor(back);
		ug.getParam().setColor(foregroundColor);
		ug.draw(0, 0, polygon);

		ug.draw(textWidth - cornersize, 0, new ULine(0, cornersize));
		ug.draw(textWidth, cornersize, new ULine(-cornersize, 0));

		getTextBlock().drawU(ug, getMarginX1(), getMarginY());

	}

}
