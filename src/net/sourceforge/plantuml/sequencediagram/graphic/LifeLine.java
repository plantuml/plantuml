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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.SkinParamForceColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class LifeLine {

	static class Variation {
		final private LifeSegmentVariation type;
		final private SymbolContext colors;
		final private double y;

		Variation(LifeSegmentVariation type, double y, SymbolContext backcolor) {
			this.type = type;
			this.y = y;
			this.colors = backcolor;
		}

		@Override
		public String toString() {
			return type + " " + y;
		}
	}

	private final Pushable participant;
	private final double nominalPreferredWidth;

	private final List<Variation> events = new ArrayList<Variation>();
	private final Stairs stairs = new Stairs();
	private int maxLevel = 0;
	private final boolean shadowing;

	public LifeLine(Pushable participant, double nominalPreferredWidth, boolean shadowing) {
		this.participant = participant;
		this.nominalPreferredWidth = nominalPreferredWidth;
		this.shadowing = shadowing;
	}

	public void addSegmentVariation(LifeSegmentVariation type, double y, SymbolContext colors) {
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
		events.add(new Variation(type, y, colors));
		final int currentLevel = type.apply(stairs.getLastValue());
		stairs.addStep(y, currentLevel);
		assert getLevel(y) == stairs.getValue(y);
		assert currentLevel == stairs.getValue(y);
		assert getLevel(y) == currentLevel;
		maxLevel = Math.max(getLevel(y), maxLevel);
	}

	public void finish(double y) {
		final int missingClose = getMissingClose();
		for (int i = 0; i < missingClose; i++) {
			addSegmentVariation(LifeSegmentVariation.SMALLER, y, null);
		}
	}

	int getMissingClose() {
		int level = 0;
		for (Variation ev : events) {
			if (ev.type == LifeSegmentVariation.LARGER) {
				level++;
			} else {
				level--;
			}
		}
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
		if (level == 0) {
			return 0;
		}
		return nominalPreferredWidth / 2.0;
	}

	private double getStartingX(StringBounder stringBounder) {
		final double delta = participant.getCenterX(stringBounder) - nominalPreferredWidth / 2.0;
		return delta;
	}

	private SegmentColored getSegment(int i) {
		if (events.get(i).type != LifeSegmentVariation.LARGER) {
			return null;
		}
		int level = 1;
		for (int j = i + 1; j < events.size(); j++) {
			if (events.get(j).type == LifeSegmentVariation.LARGER) {
				level++;
			} else {
				level--;
			}
			if (level == 0) {
				final double y1 = events.get(i).y;
				final double y2 = events.get(j).y;
				return new SegmentColored(y1, y2, events.get(i).colors, shadowing);
			}
		}
		return new SegmentColored(events.get(i).y, events.get(events.size() - 1).y, events.get(i).colors, shadowing);
	}

	private Collection<SegmentColored> getSegmentsCutted(StringBounder stringBounder, int i) {
		final SegmentColored seg = getSegment(i);
		if (seg != null) {
			return seg.cutSegmentIfNeed(participant.getDelays(stringBounder));
		}
		return Collections.emptyList();
	}

	public void drawU(UGraphic ug, Rose skin, ISkinParam skinParam) {
		final StringBounder stringBounder = ug.getStringBounder();

		ug = ug.apply(UTranslate.dx(getStartingX(stringBounder)));

		for (int i = 0; i < events.size(); i++) {
			ComponentType type = ComponentType.ALIVE_BOX_CLOSE_OPEN;
			for (final Iterator<SegmentColored> it = getSegmentsCutted(stringBounder, i).iterator(); it.hasNext();) {
				final SegmentColored seg = it.next();
				final HColor specificBackColor = seg.getSpecificBackColor();
				ISkinParam skinParam2 = new SkinParamBackcolored(skinParam, specificBackColor);
				final HColor specificLineColor = seg.getSpecificLineColor();
				if (specificLineColor != null) {
					skinParam2 = new SkinParamForceColor(skinParam2, ColorParam.sequenceLifeLineBorder,
							specificLineColor);
				}
				if (it.hasNext() == false) {
					type = type == ComponentType.ALIVE_BOX_CLOSE_OPEN ? ComponentType.ALIVE_BOX_CLOSE_CLOSE
							: ComponentType.ALIVE_BOX_OPEN_CLOSE;
				}
				Style style = type.getDefaultStyleDefinition().getMergedStyle(skinParam2.getCurrentStyleBuilder());
				if (style != null) {
					style = style.eventuallyOverride(PName.BackGroundColor, specificBackColor);
					style = style.eventuallyOverride(PName.LineColor, specificLineColor);
				}
				final Component compAliveBox = skin
						.createComponent(new Style[] { style }, type, null, skinParam2, null);
				type = ComponentType.ALIVE_BOX_OPEN_OPEN;
				final int currentLevel = getLevel(seg.getPos1Initial());
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

	public SymbolContext getColors() {
		if (events.size() == 0) {
			return null;
		}
		return events.get(events.size() - 1).colors;
	}
}
