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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.klimt.UClip;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockMemoized;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;

public class PlayingSpaceWithParticipants extends TextBlockMemoized {

	private final PlayingSpace playingSpace;

	private int pageIndex;

	public PlayingSpaceWithParticipants(PlayingSpace playingSpace) {
		this.playingSpace = Objects.requireNonNull(playingSpace);
	}

	@Override
	public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		final double width = playingSpace.getMaxX(stringBounder).getCurrentValue()
				- playingSpace.getMinX(stringBounder).getCurrentValue();

		// getPreferredHeight() triggers the layout pass that positions the tiles,
		// so it must be called before getYMin()/getYMax()
		final double fullHeight = playingSpace.getPreferredHeight(stringBounder);
		final double pageHeight = getYMax(fullHeight) - getYMin();

		final int factor = playingSpace.isShowFootbox() ? 2 : 1;
		final double height = pageHeight + factor * playingSpace.getLivingSpaces().getHeadHeight(stringBounder);

		return new XDimension2D(width, height);
	}

	// Warning: both methods below rely on yNewPages(), which reads the tiles
	// TimeHook: they are meaningful only after a layout pass, that is after
	// playingSpace.getPreferredHeight() has been called at least once.

	private double getYMin() {
		if (playingSpace.getNbPages() == 1)
			return 0;

		return playingSpace.yNewPages().get(pageIndex);
	}

	private double getYMax(double fullHeight) {
		if (playingSpace.getNbPages() == 1)
			return fullHeight;

		return Math.min(playingSpace.yNewPages().get(pageIndex + 1), fullHeight);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Context2D context = new SimpleContext2D(false);
		final LivingSpaces livingSpaces = playingSpace.getLivingSpaces();
		final double headHeight = livingSpaces.getHeadHeight(stringBounder);

		// getPreferredHeight() triggers the layout pass that positions the tiles,
		// so it must be called before getYMin()/getYMax()
		final double fullHeight = playingSpace.getPreferredHeight(stringBounder);
		final double ymin = getYMin();
		final double ymax = getYMax(fullHeight);
		final double pageHeight = ymax - ymin;

		// The body is translated so that the current page starts right below the
		// heads, and clipped so that the content of the other pages stays hidden.
		UGraphic ugBody = ug.apply(UTranslate.dy(headHeight - ymin));
		if (playingSpace.getNbPages() > 1) {
			final UClip clip = new UClip(-1000, ymin, Double.MAX_VALUE, pageHeight + 1);
			ugBody = ugBody.apply(clip);
		}

		playingSpace.drawBackground(ugBody);
		// Lifelines and liveboxes use absolute positions over the whole diagram:
		// they must be drawn with the full height, the clip trims them to the page
		livingSpaces.drawLifeLines(ugBody, fullHeight, context);

		livingSpaces.drawHeads(ug, context, VerticalAlignment.BOTTOM);
		if (playingSpace.isShowFootbox())
			livingSpaces.drawHeads(ug.apply(UTranslate.dy(pageHeight + headHeight)), context, VerticalAlignment.TOP);

		playingSpace.drawForeground(ugBody);
		// drawNewPages(ug.apply(UTranslate.dy(headHeight)));
	}

	public Real getMinX(StringBounder stringBounder) {
		return playingSpace.getMinX(stringBounder);
	}

	public int getNbPages() {
		return playingSpace.getNbPages();
	}

	public void setIndex(int index) {
		if (index != this.pageIndex) {
			this.pageIndex = index;
			invalidateDimensionCache();
		}
	}

	private List<Double> yNewPages() {
		return playingSpace.yNewPages();
	}

	private void drawNewPages(UGraphic ug) {
		ug = ug.apply(HColors.BLUE);
		for (Double change : yNewPages()) {
			if (change == 0 || change == Double.MAX_VALUE) {
				continue;
			}
			ug.apply(UTranslate.dy(change)).draw(ULine.hline(100));
		}
	}

}
