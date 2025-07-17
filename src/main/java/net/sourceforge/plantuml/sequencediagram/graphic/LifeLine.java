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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SkinParamBackcolored;
import net.sourceforge.plantuml.skin.SkinParamForceColor;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;

public class LifeLine {

	static class Variation {
		final private LifeSegmentVariation type;
		final private Fashion colors;
		final private double y;
		final private StyleBuilder styleBuilder;

		Variation(LifeSegmentVariation type, double y, Fashion backcolor, StyleBuilder styleBuilder) {
			this.type = type;
			this.y = y;
			this.colors = backcolor;
			this.styleBuilder = styleBuilder;
		}

		@Override
		public String toString() {
			return type + " " + y;
		}
	}

	private final Pushable participant;
	private final double nominalPreferredWidth;

	private final List<Variation> events = new ArrayList<>();
	private final Stairs stairs = new Stairs();
	private int maxLevel = 0;
	private final boolean shadowing;
	private final Display participantDisplay;

	public LifeLine(Pushable participant, double nominalPreferredWidth, boolean shadowing, Display participantDisplay) {
		this.participant = participant;
		this.nominalPreferredWidth = nominalPreferredWidth;
		this.shadowing = shadowing;
		this.participantDisplay = participantDisplay;
	}

	public void addSegmentVariation(LifeSegmentVariation type, double y, Fashion colors, StyleBuilder styleBuilder) {
		if (events.size() > 0) {
			final Variation last = events.get(events.size() - 1);
			if (y < last.y) {
				return;
				// throw new IllegalArgumentException();
			}
			if (y == last.y && type != last.type) {
				return;
				// throw new IllegalArgumentException();
			}
		}
		events.add(new Variation(type, y, colors, styleBuilder));
		final int currentLevel = type.apply(stairs.getLastValue());
		stairs.addStep(y, currentLevel);
		assert getLevel(y) == stairs.getValue(y);
		assert currentLevel == stairs.getValue(y);
		assert getLevel(y) == currentLevel;
		maxLevel = Math.max(getLevel(y), maxLevel);
	}

	public void finish(double y) {
		final int missingClose = getMissingClose();
		for (int i = 0; i < missingClose; i++)
			addSegmentVariation(LifeSegmentVariation.SMALLER, y, null, null);

	}

	int getMissingClose() {
		int level = 0;
		for (Variation ev : events)
			if (ev.type == LifeSegmentVariation.LARGER)
				level++;
			else
				level--;

		return level;
	}

	int getLevel(double y) {
		return stairs.getValue(y);
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public double getRightShift(double y) {
		return getRightShiftAtLevel(getLevel(y));
	}

	public double getLeftShift(double y) {
		return getLeftShiftAtLevel(getLevel(y));
	}

	public double getMaxRightShift() {
		return getRightShiftAtLevel(getMaxLevel());
	}

	public double getMaxLeftShift() {
		return getLeftShiftAtLevel(getMaxLevel());
	}

	private double getRightShiftAtLevel(int level) {
		if (level == 0) {
			return 0;
		}
		return level * (nominalPreferredWidth / 2.0);
	}

	private double getLeftShiftAtLevel(int level) {
		if (level == 0)
			return 0;

		return nominalPreferredWidth / 2.0;
	}

	private double getStartingX(StringBounder stringBounder) {
		final double delta = participant.getCenterX(stringBounder) - nominalPreferredWidth / 2.0;
		return delta;
	}

	private SegmentColored getSegment(int i) {
		if (events.get(i).type != LifeSegmentVariation.LARGER)
			return null;

		int level = 1;
		for (int j = i + 1; j < events.size(); j++) {
			if (events.get(j).type == LifeSegmentVariation.LARGER)
				level++;
			else
				level--;

			if (level == 0) {
				final double y1 = events.get(i).y;
				final double y2 = events.get(j).y;
				return SegmentColored.create(y1, y2, events.get(i).colors, shadowing);
			}
		}
		return SegmentColored.create(events.get(i).y, events.get(events.size() - 1).y, events.get(i).colors, shadowing);
	}

	private Collection<SegmentColored> getSegmentsCutted(StringBounder stringBounder, int i) {
		final SegmentColored seg = getSegment(i);
		if (seg != null)
			return seg.cutSegmentIfNeed(participant.getDelays(stringBounder));

		return Collections.emptyList();
	}

	public void drawU(UGraphic ug, Rose skin, ISkinParam skinParam) {
		final StringBounder stringBounder = ug.getStringBounder();

		ug = ug.apply(UTranslate.dx(getStartingX(stringBounder)));

		int eventLevel = 0;
		for (int i = 0; i < events.size(); i++) {
			ComponentType type = ComponentType.ACTIVATION_BOX_CLOSE_OPEN;
			Collection<SegmentColored> segmentsCutted = getSegmentsCutted(stringBounder, i);
			if (events.get(i).type == LifeSegmentVariation.LARGER)
				eventLevel++;
			else if (events.get(i).type == LifeSegmentVariation.SMALLER)
				eventLevel = Math.max(0, eventLevel - 1);
			for (final Iterator<SegmentColored> it = segmentsCutted.iterator(); it.hasNext();) {
				final SegmentColored seg = it.next();
				final HColor specificBackColor = seg.getSpecificBackColor();
				ISkinParam skinParam2 = new SkinParamBackcolored(skinParam, specificBackColor);
				final HColor specificLineColor = seg.getSpecificLineColor();
				if (specificLineColor != null)
					skinParam2 = new SkinParamForceColor(skinParam2, ColorParam.sequenceLifeLineBorder,
							specificLineColor);

				if (it.hasNext() == false)
					type = type == ComponentType.ACTIVATION_BOX_CLOSE_OPEN ? ComponentType.ACTIVATION_BOX_CLOSE_CLOSE
							: ComponentType.ACTIVATION_BOX_OPEN_CLOSE;

				StyleBuilder currentStyleBuilder = events.get(i).styleBuilder;
				if (currentStyleBuilder == null)
					currentStyleBuilder = skinParam2.getCurrentStyleBuilder();

				Style style = type.getStyleSignature().withTOBECHANGED(participant.getStereotype())
						.getMergedStyle(currentStyleBuilder);

				if (style != null) {
					style = style.eventuallyOverride(PName.BackGroundColor, specificBackColor);
					style = style.eventuallyOverride(PName.LineColor, specificLineColor);
				}

				final Component compAliveBox = skin.createComponent(new Style[] { style }, type, null, skinParam2,
						participantDisplay);
				type = ComponentType.ACTIVATION_BOX_OPEN_OPEN;
				final int currentLevel = Math.min(eventLevel, getLevel(seg.getPos1Initial()));
				seg.drawU(ug, compAliveBox, currentLevel);
			}
		}
	}

	private double create = 0;

	// private double destroy = 0;

	public final void setCreate(double create) {
		this.create = create;
	}

	public final double getCreate() {
		return create;
	}

	public final double getDestroy() {
		return 0;
	}

	// public final void setDestroy(double destroy) {
	// this.destroy = destroy;
	// }

	public final boolean shadowing() {
		return shadowing;
	}

	public Fashion getColors() {
		if (events.size() == 0)
			return null;

		return events.get(events.size() - 1).colors;
	}
}
