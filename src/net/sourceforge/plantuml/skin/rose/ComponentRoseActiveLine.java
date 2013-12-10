/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 10925 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.AbstractComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentRoseActiveLine extends AbstractComponent {

	private final HtmlColor foregroundColor;
	private final HtmlColor lifeLineBackground;
	private final boolean closeUp;
	private final boolean closeDown;
	private final boolean withShadow;

	public ComponentRoseActiveLine(HtmlColor foregroundColor, HtmlColor lifeLineBackground, boolean closeUp,
			boolean closeDown, boolean withShadow) {
		this.foregroundColor = foregroundColor;
		this.lifeLineBackground = lifeLineBackground;
		this.closeUp = closeUp;
		this.closeDown = closeDown;
		this.withShadow = withShadow;
	}

	protected void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final StringBounder stringBounder = ug.getStringBounder();
		final int x = (int) (dimensionToUse.getWidth() - getPreferredWidth(stringBounder)) / 2;

		final URectangle rect = new URectangle(getPreferredWidth(stringBounder), dimensionToUse.getHeight());
		if (withShadow) {
			rect.setDeltaShadow(1);
		}
		ug = ug.apply(new UChangeColor(foregroundColor));
		if (closeUp && closeDown) {
			ug.apply(new UChangeBackColor(lifeLineBackground)).apply(new UTranslate(x, 0)).draw(rect);
			return;
		}
		ug.apply(new UChangeBackColor(lifeLineBackground)).apply(new UChangeColor(lifeLineBackground)).apply(new UTranslate(x, 0)).draw(rect);

		final ULine vline = new ULine(0, dimensionToUse.getHeight());
		ug.apply(new UTranslate(x, 0)).draw(vline);
		ug.apply(new UTranslate(x + getPreferredWidth(stringBounder), 0)).draw(vline);

		final ULine hline = new ULine(getPreferredWidth(stringBounder), 0);
		if (closeUp) {
			ug.apply(new UTranslate(x, 0)).draw(hline);
		}
		if (closeDown) {
			ug.apply(new UTranslate(x, dimensionToUse.getHeight())).draw(hline);
		}
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return 0;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return 10;
	}

}
