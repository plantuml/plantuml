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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;
import java.util.Iterator;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class LiveBoxes implements UDrawable {

	private final EventsHistory eventsHistory;
	private final Skin skin;
	private final ISkinParam skinParam;
	private final double totalHeight;
	private final Context2D context;

	public LiveBoxes(EventsHistory eventsHistory, Skin skin, ISkinParam skinParam, double totalHeight, Context2D context) {
		this.eventsHistory = eventsHistory;
		this.skin = skin;
		this.skinParam = skinParam;
		this.totalHeight = totalHeight;
		this.context = context;
	}

	public void drawU(UGraphic ug) {
		final Stairs2 stairs = eventsHistory.getStairs(totalHeight);
		final int max = stairs.getMaxValue();
		for (int i = 1; i <= max; i++) {
			drawOneLevel(ug, i, stairs, context);
		}
	}

	private void drawOneLevel(UGraphic ug, int levelToDraw, Stairs2 stairs, Context2D context) {
		final Component comp1 = skin.createComponent(ComponentType.ALIVE_BOX_CLOSE_CLOSE, null, skinParam, null);
		final Component cross = skin.createComponent(ComponentType.DESTROY, null, skinParam, null);
		final Dimension2D dimCross = cross.getPreferredDimension(ug.getStringBounder());
		final double width = comp1.getPreferredWidth(ug.getStringBounder());
		ug = ug.apply(new UTranslate((levelToDraw - 1) * width / 2.0, 0));

		double y1 = Double.MAX_VALUE;
		HtmlColor color = null;
		for (Iterator<StairsPosition> it = stairs.getYs().iterator(); it.hasNext();) {
			final StairsPosition yposition = it.next();
			System.err.println("LiveBoxes::drawOneLevel " + levelToDraw + " " + yposition);
			final IntegerColored integerColored = stairs.getValue(yposition.getValue());
			System.err.println("integerColored=" + integerColored);
			final int level = integerColored.getValue();
			if (y1 == Double.MAX_VALUE && level == levelToDraw) {
				y1 = yposition.getValue();
				color = integerColored.getColor();
			} else if (y1 != Double.MAX_VALUE && (it.hasNext() == false || level < levelToDraw)) {
				final double y2 = yposition.getValue();
				final Area area = new Area(width, y2 - y1);

				final ISkinParam skinParam2 = new SkinParamBackcolored(skinParam, color);
				final Component comp = skin
						.createComponent(ComponentType.ALIVE_BOX_CLOSE_CLOSE, null, skinParam2, null);

				comp.drawU(ug.apply(new UTranslate(-width / 2, y1)), area, context);
				System.err.println("LiveBoxes::drawOneLevel one block " + y1 + " " + y2);
				if (yposition.isDestroy()) {
					System.err.println("LiveBoxes::drawOneLevel DESTROY " + yposition);
					cross.drawU(ug.apply(new UTranslate(-dimCross.getWidth() / 2, y2 - dimCross.getHeight() / 2)),
							null, context);
				} else {
					System.err.println("LiveBoxes::drawOneLevel NOTDESTROY " + yposition);
				}
				y1 = Double.MAX_VALUE;
			}
		}
	}

	private UGraphic withColor(UGraphic ug) {
		return ug;
	}

}
