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
 * Revision $Revision: 4258 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ComponentRoseGroupingHeader extends AbstractTextualComponent {

	private final int cornersize = 10;
	private final int commentMargin = 0; // 8;

	private final TextBlock commentTextBlock;

	private final Color groupBackground;
	private final Color background;

	public ComponentRoseGroupingHeader(Color fontColor, Color background,
			Color groupBackground, Font bigFont, Font smallFont,
			List<? extends CharSequence> strings) {
		super(strings.get(0), fontColor, bigFont, HorizontalAlignement.LEFT,
				15, 30, 1);
		this.groupBackground = groupBackground;
		this.background = background;
		if (strings.size() == 1 || strings.get(1) == null) {
			this.commentTextBlock = null;
		} else {
			this.commentTextBlock = TextBlockUtils.create(Arrays.asList("["
					+ strings.get(1) + "]"), smallFont, fontColor,
					HorizontalAlignement.LEFT);
		}
	}

	@Override
	public double getPaddingY() {
		return 6;
	}

	@Override
	final public double getPreferredWidth(StringBounder stringBounder) {
		final double sup;
		if (commentTextBlock == null) {
			sup = commentMargin * 2;
		} else {
			final Dimension2D size = commentTextBlock
					.calculateDimension(stringBounder);
			sup = getMarginX1() + commentMargin + size.getWidth();

		}
		return getTextWidth(stringBounder) + sup;
	}

	@Override
	final public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 2 * getPaddingY();
	}

	@Override
	protected void drawBackgroundInternalU(UGraphic ug,
			Dimension2D dimensionToUse) {
		if (this.background == null) {
			return;
		}
		ug.getParam().setColor(null);
		ug.getParam().setBackcolor(background);
		ug.draw(0, 0, new URectangle(dimensionToUse.getWidth(), dimensionToUse
				.getHeight()));
	}

	@Override
	protected void drawInternalU(UGraphic ug, Dimension2D dimensionToUse) {
		final StringBounder stringBounder = ug.getStringBounder();
		final int textWidth = (int) getTextWidth(stringBounder);
		final int textHeight = (int) getTextHeight(stringBounder);

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(textWidth, 0);

		polygon.addPoint(textWidth, textHeight - cornersize);
		polygon.addPoint(textWidth - cornersize, textHeight);

		polygon.addPoint(0, textHeight);
		polygon.addPoint(0, 0);

		ug.getParam().setStroke(new UStroke(2));
		ug.getParam().setColor(getFontColor());
		ug.getParam().setBackcolor(this.groupBackground);
		ug.draw(0, 0, polygon);

		ug.draw(0, 0, new ULine(dimensionToUse.getWidth(), 0));
		ug.draw(dimensionToUse.getWidth(), 0, new ULine(0, dimensionToUse.getHeight()));
		ug.draw(0, textHeight, new ULine(0, dimensionToUse.getHeight()-textHeight));
		ug.getParam().setStroke(new UStroke());

		getTextBlock().drawU(ug, getMarginX1(), getMarginY());

		if (commentTextBlock != null) {
			// final Dimension2D size =
			// commentTextBlock.calculateDimension(stringBounder);
			// ug.getParam().setColor(null/*this.background*/);
			// ug.getParam().setBackcolor(null);
			final int x1 = getMarginX1() + textWidth;
			final int y2 = getMarginY() + 1;
			// ug.draw(x1, y2, new URectangle(size.getWidth() + 2 *
			// commentMargin, size.getHeight()));

			commentTextBlock.drawU(ug, x1 + commentMargin, y2);
		}
	}

}
