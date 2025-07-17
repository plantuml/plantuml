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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.sequencediagram.graphic.Segment;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SkinParamBackcolored;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;

public class LiveBoxesDrawer {

	private double y1;
	private Fashion symbolContext;
	private StyleBuilder styleBuilder;

	private final Component cross;
	private final Context2D context;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final Component compForWidth;
	private final Collection<Segment> delays;
	private final Stereotype stereotype;

	public LiveBoxesDrawer(Context2D context, Rose skin, ISkinParam skinParam, Map<Double, Double> delays,
			Stereotype stereotype) {
		this.stereotype = stereotype;
		this.cross = skin.createComponent(
				new Style[] {
						ComponentType.DESTROY.getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder()) },
				ComponentType.DESTROY, null, skinParam, null);
		this.compForWidth = skin.createComponent(
				new Style[] { ComponentType.ACTIVATION_BOX_CLOSE_CLOSE.getStyleSignature()
						.getMergedStyle(skinParam.getCurrentStyleBuilder()) },
				ComponentType.ACTIVATION_BOX_CLOSE_CLOSE, null, skinParam, null);
		this.context = context;
		this.skin = skin;
		this.skinParam = skinParam;
		this.delays = new HashSet<>();
		for (Map.Entry<Double, Double> ent : delays.entrySet())
			this.delays.add(new Segment(ent.getKey(), ent.getKey() + ent.getValue()));

	}

	public double getWidth(StringBounder stringBounder) {
		return compForWidth.getPreferredWidth(stringBounder);
	}

	public void addStart(double y1, Fashion symbolContext, StyleBuilder styleBuilder) {
		this.y1 = y1;
		this.symbolContext = symbolContext;
		this.styleBuilder = styleBuilder;
	}

	public void doDrawing(UGraphic ug, double yposition) {
		final Segment full = new Segment(y1, yposition);
		final Collection<Segment> segments = full.cutSegmentIfNeed(delays);
		ComponentType type = ComponentType.ACTIVATION_BOX_CLOSE_CLOSE;
		if (segments.size() > 1)
			type = ComponentType.ACTIVATION_BOX_CLOSE_OPEN;

		for (Iterator<Segment> it = segments.iterator(); it.hasNext();) {
			final Segment seg = it.next();
			if (it.hasNext() == false && type != ComponentType.ACTIVATION_BOX_CLOSE_CLOSE)
				type = ComponentType.ACTIVATION_BOX_OPEN_CLOSE;

			drawInternal(ug, seg.getPos1(), seg.getPos2(), type);
			type = ComponentType.ACTIVATION_BOX_OPEN_OPEN;
		}
		y1 = Double.MAX_VALUE;
	}

	public void drawDestroyIfNeeded(UGraphic ug, Step step) {
		if (step.isDestroy()) {
			final XDimension2D dimCross = cross.getPreferredDimension(ug.getStringBounder());
			cross.drawU(ug.apply(new UTranslate(-dimCross.getWidth() / 2, step.getValue() - dimCross.getHeight() / 2)),
					null, context);
		}
	}

	private void drawInternal(UGraphic ug, double ya, double yb, ComponentType type) {
		final double width = getWidth(ug.getStringBounder());
		final Area area = Area.create(width, yb - ya);
		SkinParamBackcolored skinParam2 = new SkinParamBackcolored(skinParam,
				symbolContext == null ? null : symbolContext.getBackColor());

		final StyleBuilder currentStyleBuilder = styleBuilder == null ? skinParam.getCurrentStyleBuilder()
				: styleBuilder;

		Style style = type.getStyleSignature().withTOBECHANGED(stereotype).getMergedStyle(currentStyleBuilder);

		if (style == null) {
			if (symbolContext != null)
				skinParam2.forceColor(ColorParam.sequenceLifeLineBorder, symbolContext.getForeColor());
		} else {
			style = style.eventuallyOverride(symbolContext);
		}
		final Component comp = skin.createComponent(new Style[] { style }, type, null, skinParam2, null);
		comp.drawU(ug.apply(new UTranslate(-width / 2, ya)), area, context);
	}

}
