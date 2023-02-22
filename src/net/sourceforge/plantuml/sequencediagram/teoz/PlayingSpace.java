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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.LimitFinder;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.LinkAnchor;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.style.ISkinParam;

public class PlayingSpace implements Bordered {

	private final double startingY = 8;
	private final Real min;
	private final Real max;
	private final boolean isShowFootbox;

	private final List<Tile> tiles = new ArrayList<>();
	private final LivingSpaces livingSpaces;
	private final List<LinkAnchor> linkAnchors;
	private final ISkinParam skinParam;

	public PlayingSpace(SequenceDiagram diagram, Dolls dolls, TileArguments tileArguments) {

		this.livingSpaces = tileArguments.getLivingSpaces();
		this.linkAnchors = diagram.getLinkAnchors();
		this.skinParam = diagram.getSkinParam();

		final List<Real> min2 = new ArrayList<>();
		final List<Real> max2 = new ArrayList<>();

		min2.add(tileArguments.getXOrigin());
		max2.add(tileArguments.getXOrigin());

		if (dolls.size() > 0) {
			min2.add(dolls.getMinX(tileArguments.getStringBounder()));
			max2.add(dolls.getMaxX(tileArguments.getStringBounder()));
		}

		final YGauge ycurrent = YGauge.create(tileArguments.getYOrigin().addAtLeast(0), 0);

		tiles.addAll(TileBuilder.buildSeveral(diagram.events().iterator(), tileArguments, null, ycurrent));

		for (Tile tile : tiles) {
			min2.add(tile.getMinX());
			max2.add(tile.getMaxX());
		}

		for (LivingSpace livingSpace : livingSpaces.values()) {
			max2.add(livingSpace.getPosD(tileArguments.getStringBounder()));
			max2.add(livingSpace.getPosC2(tileArguments.getStringBounder()));
		}

		this.min = RealUtils.min(min2);
		this.max = RealUtils.max(max2);

		this.isShowFootbox = diagram.isShowFootbox();
	}

	public void drawBackground(UGraphic ug) {
		final UGraphicInterceptorTile interceptor = new UGraphicInterceptorTile(ug, true);
		drawUInternal(interceptor, false);
	}

	public void drawForeground(UGraphic ug) {
		final UGraphicInterceptorTile interceptor = new UGraphicInterceptorTile(ug, false);
		drawUInternal(interceptor, false);
	}

	private double drawUInternal(UGraphic ug, boolean trace) {
		final StringBounder stringBounder = ug.getStringBounder();
		final List<CommonTile> local = new ArrayList<>();
		final List<CommonTile> full = new ArrayList<>();
		final TimeHook y = GroupingTile.fillPositionelTiles(stringBounder, new TimeHook(startingY), tiles, local, full);
		for (CommonTile tile : local) {
			if (YGauge.USE_ME) {
				((CommonTile) tile).drawU(ug);
			} else {
				final double posy = ((CommonTile) tile).getTimeHook().getValue();
				((CommonTile) tile).drawU(ug.apply(UTranslate.dy(posy)));
			}
		}
		for (LinkAnchor linkAnchor : linkAnchors) {
			final CommonTile ytile1 = getFromAnchor(full, linkAnchor.getAnchor1());
			final CommonTile ytile2 = getFromAnchor(full, linkAnchor.getAnchor2());
			if (ytile1 != null && ytile2 != null)
				linkAnchor.drawAnchor(ug, ytile1, ytile2, skinParam);

		}
		// System.err.println("MainTile::drawUInternal finalY=" + y);
		return y.getValue();
	}

	private CommonTile getFromAnchor(List<CommonTile> positionedTiles, String anchor) {
		for (CommonTile ytile : positionedTiles)
			if (ytile.matchAnchor(anchor))
				return ytile;

		return null;
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final LimitFinder limitFinder = LimitFinder.create(stringBounder, true);
		final UGraphicInterceptorTile interceptor = new UGraphicInterceptorTile(limitFinder, false);
		final double finalY = drawUInternal(interceptor, false);
		final double result = Math.max(limitFinder.getMinMax().getDimension().getHeight(), finalY) + 10;
		// System.err.println("MainTile::getPreferredHeight=" + result);
		return result;
	}

	public void addConstraints() {
		for (Tile tile : tiles)
			tile.addConstraints();

	}

	public Real getMinX(StringBounder stringBounder) {
		return min;
	}

	public Real getMaxX(StringBounder stringBounder) {
		return max;
	}

	public boolean isShowFootbox() {
		return isShowFootbox;
	}

	public LivingSpaces getLivingSpaces() {
		return livingSpaces;
	}

	public double getBorder1() {
		return min.getCurrentValue();
	}

	public double getBorder2() {
		return max.getCurrentValue();
	}

	public List<Double> yNewPages() {
		final List<Double> yNewPages = new ArrayList<>();
		yNewPages.add((double) 0);
		for (Tile tile : tiles) {
			if (tile instanceof GroupingTile)
				((GroupingTile) tile).addYNewPages(yNewPages);

			if (tile instanceof NewpageTile) {
				final double y = ((NewpageTile) tile).getTimeHook().getValue();
				yNewPages.add(y);
			}
		}
		yNewPages.add(Double.MAX_VALUE);
		return yNewPages;
	}

	public int getNbPages() {
		return yNewPages().size() - 1;
	}

}
