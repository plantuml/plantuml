/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 9591 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class MainTileAdapter extends AbstractTextBlock implements TextBlock {

	private final MainTile mainTile;
	private Dimension2D cacheDimension;

	public MainTileAdapter(MainTile mainTile) {
		if (mainTile == null) {
			throw new IllegalArgumentException();
		}
		this.mainTile = mainTile;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		if (cacheDimension == null) {
			final double width = mainTile.getMaxX(stringBounder).getCurrentValue()
					- mainTile.getMinX(stringBounder).getCurrentValue();

			final int factor = mainTile.isShowFootbox() ? 2 : 1;
			final double height = mainTile.getPreferredHeight(stringBounder) + factor
					* mainTile.getLivingSpaces().getHeadHeight(stringBounder);

			cacheDimension = new Dimension2DDouble(width, height);
		}
		return cacheDimension;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Context2D context = new SimpleContext2D(false);
		final double height = mainTile.getPreferredHeight(stringBounder);
		final LivingSpaces livingSpaces = mainTile.getLivingSpaces();

		final double headHeight = livingSpaces.getHeadHeight(stringBounder);

		mainTile.drawU(ug.apply(new UTranslate(0, headHeight)));
		livingSpaces.drawLifeLines(ug.apply(new UTranslate(0, headHeight)), height, context);
		livingSpaces.drawHeads(ug, context, VerticalAlignment.BOTTOM);
		if (mainTile.isShowFootbox()) {
			livingSpaces.drawHeads(ug.apply(new UTranslate(0, height + headHeight)), context, VerticalAlignment.TOP);
		}
		mainTile.drawForeground(ug.apply(new UTranslate(0, headHeight)));
	}

	public Real getMinX(StringBounder stringBounder) {
		return mainTile.getMinX(stringBounder);
	}

}
