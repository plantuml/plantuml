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
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.sequencediagram.Delay;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class MutingLine {

	private final Rose skin;
	private final ISkinParam skinParam;
	private final boolean useContinueLineBecauseOfDelay;
	private final Map<Double, Double> delays = new TreeMap<Double, Double>();
	private final StyleBuilder styleBuilder;

	public MutingLine(Rose skin, ISkinParam skinParam, List<Event> events) {
		this.skin = skin;
		this.skinParam = skinParam;
		this.useContinueLineBecauseOfDelay = useContinueLineBecauseOfDelay(events);
		this.styleBuilder = skinParam.getCurrentStyleBuilder();
	}

	private boolean useContinueLineBecauseOfDelay(List<Event> events) {
		final String strategy = skinParam.getValue("lifelineStrategy");
		if ("nosolid".equalsIgnoreCase(strategy)) {
			return false;
		}
		for (Event ev : events) {
			if (ev instanceof Delay) {
				return true;
			}
		}
		return false;
	}

	public void drawLine(UGraphic ug, Context2D context, double createY, double endY) {
		final ComponentType defaultLineType = useContinueLineBecauseOfDelay ? ComponentType.CONTINUE_LINE
				: ComponentType.PARTICIPANT_LINE;
		if (delays.size() > 0) {
			double y = createY;
			for (Map.Entry<Double, Double> ent : delays.entrySet()) {
				if (ent.getKey() >= createY) {
					drawInternal(ug, context, y, ent.getKey(), defaultLineType);
					drawInternal(ug, context, ent.getKey(), ent.getKey() + ent.getValue(), ComponentType.DELAY_LINE);
					y = ent.getKey() + ent.getValue();
				}
			}
			drawInternal(ug, context, y, endY, defaultLineType);
		} else {
			drawInternal(ug, context, createY, endY, defaultLineType);
		}
	}

	private void drawInternal(UGraphic ug, Context2D context, double y1, double y2, final ComponentType defaultLineType) {
		if (y2 == y1) {
			return;
		}
		if (y2 < y1) {
			throw new IllegalArgumentException();
		}
		final Style style = defaultLineType.getDefaultStyleDefinition().getMergedStyle(styleBuilder);
		final Component comp = skin.createComponent(new Style[] { style }, defaultLineType, null, skinParam, null);
		final Dimension2D dim = comp.getPreferredDimension(ug.getStringBounder());
		final Area area = new Area(dim.getWidth(), y2 - y1);
		comp.drawU(ug.apply(UTranslate.dy(y1)), area, context);
	}

	public void delayOn(double y, double height) {
		delays.put(y, height);
	}

}
