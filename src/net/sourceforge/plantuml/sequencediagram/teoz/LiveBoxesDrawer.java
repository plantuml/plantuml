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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.sequencediagram.graphic.Segment;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class LiveBoxesDrawer {

	private double y1;
	private SymbolContext symbolContext;

	private final Component cross;
	private final Context2D context;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final Component compForWidth;
	private final Collection<Segment> delays;

	public LiveBoxesDrawer(Context2D context, Rose skin, ISkinParam skinParam, Map<Double, Double> delays) {
		this.cross = skin.createComponent(new Style[] { ComponentType.DESTROY.getDefaultStyleDefinition()
				.getMergedStyle(skinParam.getCurrentStyleBuilder()) }, ComponentType.DESTROY, null, skinParam, null);
		this.compForWidth = skin.createComponent(new Style[] { ComponentType.ALIVE_BOX_CLOSE_CLOSE
				.getDefaultStyleDefinition().getMergedStyle(skinParam.getCurrentStyleBuilder()) },
				ComponentType.ALIVE_BOX_CLOSE_CLOSE, null, skinParam, null);
		this.context = context;
		this.skin = skin;
		this.skinParam = skinParam;
		this.delays = new HashSet<Segment>();
		for (Map.Entry<Double, Double> ent : delays.entrySet()) {
			this.delays.add(new Segment(ent.getKey(), ent.getKey() + ent.getValue()));
		}
	}

	public double getWidth(StringBounder stringBounder) {
		return compForWidth.getPreferredWidth(stringBounder);
	}

	public void addStart(double y1, SymbolContext symbolContext) {
		this.y1 = y1;
		this.symbolContext = symbolContext;
	}

	public void doDrawing(UGraphic ug, StairsPosition yposition) {
		final Segment full = new Segment(y1, yposition.getValue());
		final Collection<Segment> segments = full.cutSegmentIfNeed(delays);
		ComponentType type = ComponentType.ALIVE_BOX_CLOSE_CLOSE;
		if (segments.size() > 1) {
			type = ComponentType.ALIVE_BOX_CLOSE_OPEN;
		}
		for (Iterator<Segment> it = segments.iterator(); it.hasNext();) {
			final Segment seg = it.next();
			if (it.hasNext() == false && type != ComponentType.ALIVE_BOX_CLOSE_CLOSE) {
				type = ComponentType.ALIVE_BOX_OPEN_CLOSE;
			}
			drawInternal(ug, yposition, seg.getPos1(), seg.getPos2(), type);
			type = ComponentType.ALIVE_BOX_OPEN_OPEN;
		}
		y1 = Double.MAX_VALUE;
	}

	public void drawDestroyIfNeeded(UGraphic ug, StairsPosition yposition) {
		if (yposition.isDestroy()) {
			final Dimension2D dimCross = cross.getPreferredDimension(ug.getStringBounder());
			cross.drawU(
					ug.apply(new UTranslate(-dimCross.getWidth() / 2, yposition.getValue() - dimCross.getHeight() / 2)),
					null, context);
		}
	}

	private void drawInternal(UGraphic ug, StairsPosition yposition, double ya, double yb, ComponentType type) {
		final double width = getWidth(ug.getStringBounder());
		final Area area = new Area(width, yb - ya);
		ISkinParam skinParam2 = new SkinParamBackcolored(skinParam, symbolContext == null ? null
				: symbolContext.getBackColor());
		Style style = type.getDefaultStyleDefinition().getMergedStyle(skinParam.getCurrentStyleBuilder());
		if (style != null) {
			style = style.eventuallyOverride(symbolContext);
		}
		final Component comp = skin.createComponent(new Style[] { style }, type, null, skinParam2, null);
		comp.drawU(ug.apply(new UTranslate(-width / 2, ya)), area, context);
	}

}
