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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PlayingSpaceWithParticipants extends AbstractTextBlock implements TextBlock {

	private final PlayingSpace playingSpace;
	private Dimension2D cacheDimension;
	private double ymin;
	private double ymax;

	public PlayingSpaceWithParticipants(PlayingSpace playingSpace) {
		if (playingSpace == null) {
			throw new IllegalArgumentException();
		}
		this.playingSpace = playingSpace;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		if (cacheDimension == null) {
			final double width = playingSpace.getMaxX(stringBounder).getCurrentValue()
					- playingSpace.getMinX(stringBounder).getCurrentValue();

			final int factor = playingSpace.isShowFootbox() ? 2 : 1;
			final double height = playingSpace.getPreferredHeight(stringBounder) + factor
					* playingSpace.getLivingSpaces().getHeadHeight(stringBounder);

			cacheDimension = new Dimension2DDouble(width, height);
		}
		return cacheDimension;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Context2D context = new SimpleContext2D(false);
		final double height = playingSpace.getPreferredHeight(stringBounder);
		final LivingSpaces livingSpaces = playingSpace.getLivingSpaces();

		final double headHeight = livingSpaces.getHeadHeight(stringBounder);

		if (ymax == 0) {
			playingSpace.drawBackground(ug.apply(UTranslate.dy(headHeight)));
		} else {
			final UClip clip = new UClip(-1000, ymin, Double.MAX_VALUE, ymax - ymin + 1);
			playingSpace.drawBackground(ug.apply(UTranslate.dy(headHeight)).apply(clip));
		}

		livingSpaces.drawLifeLines(ug.apply(UTranslate.dy(headHeight)), height, context);

		livingSpaces.drawHeads(ug, context, VerticalAlignment.BOTTOM);
		if (playingSpace.isShowFootbox()) {
			livingSpaces.drawHeads(ug.apply(UTranslate.dy(height + headHeight)), context, VerticalAlignment.TOP);
		}
		if (ymax == 0) {
			playingSpace.drawForeground(ug.apply(UTranslate.dy(headHeight)));
		} else {
			final UClip clip = new UClip(-1000, ymin, Double.MAX_VALUE, ymax - ymin + 1);
			// playingSpace.drawForeground(new UGraphicNewpages(ug.apply(UTranslate.dy(headHeight)), ymin, ymax));
			playingSpace.drawForeground(ug.apply(UTranslate.dy(headHeight)).apply(clip));
		}
		// drawNewPages(ug.apply(UTranslate.dy(headHeight)));
	}

	public Real getMinX(StringBounder stringBounder) {
		return playingSpace.getMinX(stringBounder);
	}

	public int getNbPages() {
		return playingSpace.getNbPages();
	}

	public void setIndex(int index) {
		final List<Double> yNewPages = playingSpace.yNewPages();
		this.ymin = yNewPages.get(index);
		this.ymax = yNewPages.get(index + 1);
	}

	private List<Double> yNewPages() {
		return playingSpace.yNewPages();
	}

	private void drawNewPages(UGraphic ug) {
		ug = ug.apply(HColorUtils.BLUE);
		for (Double change : yNewPages()) {
			if (change == 0 || change == Double.MAX_VALUE) {
				continue;
			}
			ug.apply(UTranslate.dy(change)).draw(ULine.hline(100));
		}
	}

}
