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

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class LiveBoxes {

	private final EventsHistory eventsHistory;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final Map<Double, Double> delays = new TreeMap<Double, Double>();

	public LiveBoxes(EventsHistory eventsHistory, Rose skin, ISkinParam skinParam, Participant participant) {
		this.eventsHistory = eventsHistory;
		this.skin = skin;
		this.skinParam = skinParam;
	}

	public double getMaxPosition(StringBounder stringBounder) {
		final int max = eventsHistory.getMaxValue();
		final LiveBoxesDrawer drawer = new LiveBoxesDrawer(new SimpleContext2D(true), skin, skinParam, delays);
		return drawer.getWidth(stringBounder) / 2.0 * max;
	}

	public void drawBoxes(UGraphic ug, Context2D context, double createY, double endY) {
		final Stairs2 stairs = eventsHistory.getStairs(createY, endY);
		final int max = stairs.getMaxValue();
		if (max == 0) {
			drawDestroys(ug, stairs, context);
		}
		for (int i = 1; i <= max; i++) {
			drawOneLevel(ug, i, stairs, context);
		}
	}

	private void drawDestroys(UGraphic ug, Stairs2 stairs, Context2D context) {
		final LiveBoxesDrawer drawer = new LiveBoxesDrawer(context, skin, skinParam, delays);
		for (StairsPosition yposition : stairs.getYs()) {
			drawer.drawDestroyIfNeeded(ug, yposition);
		}
	}

	private void drawOneLevel(UGraphic ug, int levelToDraw, Stairs2 stairs, Context2D context) {
		final LiveBoxesDrawer drawer = new LiveBoxesDrawer(context, skin, skinParam, delays);
		ug = ug.apply(UTranslate.dx((levelToDraw - 1) * drawer.getWidth(ug.getStringBounder()) / 2.0));

		boolean pending = true;
		for (Iterator<StairsPosition> it = stairs.getYs().iterator(); it.hasNext();) {
			final StairsPosition yposition = it.next();
			final IntegerColored integerColored = stairs.getValue(yposition.getValue());
			final int level = integerColored.getValue();
			if (pending && level == levelToDraw) {
				drawer.addStart(yposition.getValue(), integerColored.getColors());
				pending = false;
			} else if (pending == false && (it.hasNext() == false || level < levelToDraw)) {
				drawer.doDrawing(ug, yposition);
				drawer.drawDestroyIfNeeded(ug, yposition);
				pending = true;
			}
		}
	}

	public void delayOn(double y, double height) {
		delays.put(y, height);
	}

}
