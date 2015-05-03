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
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class LivingSpaces {

	private final Map<Participant, LivingSpace> all = new LinkedHashMap<Participant, LivingSpace>();

	public Collection<LivingSpace> values() {
		return all.values();
	}

	public void put(Participant participant, LivingSpace livingSpace) {
		all.put(participant, livingSpace);
	}

	public LivingSpace get(Participant participant) {
		return all.get(participant);
	}

	public void drawHeads(final UGraphic ug, Context2D context, VerticalAlignment verticalAlignment) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double headHeight = getHeadHeight(stringBounder);
		for (LivingSpace livingSpace : values()) {
			final double x = livingSpace.getPosB().getCurrentValue();
			double y = 0;
			if (verticalAlignment == VerticalAlignment.BOTTOM) {
				final Dimension2D dimHead = livingSpace.getHeadPreferredDimension(stringBounder);
				y = headHeight - dimHead.getHeight();
			}
			livingSpace.drawHead(ug.apply(new UTranslate(x, y)), context);
		}
	}

	public double getHeadHeight(StringBounder stringBounder) {
		double headHeight = 0;
		for (LivingSpace livingSpace : values()) {
			final Dimension2D headDim = livingSpace.getHeadPreferredDimension(stringBounder);
			headHeight = Math.max(headHeight, headDim.getHeight());
		}
		return headHeight;
	}

	public void drawLifeLines(final UGraphic ug, double height, Context2D context) {
		int i = 0;
		for (LivingSpace livingSpace : values()) {
			// if (i++ == 0) {
			// System.err.println("TEMPORARY SKIPPING OTHERS");
			// continue;
			// }
			// System.err.println("drawing lines " + livingSpace);
			final double x = livingSpace.getPosC(ug.getStringBounder()).getCurrentValue();
			livingSpace.drawLineAndLiveBoxes(ug.apply(new UTranslate(x, 0)), height, context);
		}
	}

}
