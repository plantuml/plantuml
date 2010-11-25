/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 5636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class LifeLine {

	static class Variation {
		final private LifeSegmentVariation type;
		final private HtmlColor backcolor;
		final private double y;

		Variation(LifeSegmentVariation type, double y, HtmlColor backcolor) {
			this.type = type;
			this.y = y;
			this.backcolor = backcolor;
		}

		@Override
		public String toString() {
			return type + " " + y;
		}
	}

	private final Pushable participant;
	private final double nominalPreferredWidth;

	private final List<Variation> events = new ArrayList<Variation>();
	private int maxLevel = 0;

	public LifeLine(Pushable participant, double nominalPreferredWidth) {
		this.participant = participant;
		this.nominalPreferredWidth = nominalPreferredWidth;
	}

	public void addSegmentVariation(LifeSegmentVariation type, double y, HtmlColor backcolor) {
		if (events.size() > 0) {
			final Variation last = events.get(events.size() - 1);
			if (y < last.y) {
				throw new IllegalArgumentException();
			}
			if (y == last.y && type != last.type) {
				throw new IllegalArgumentException();
			}
		}
		events.add(new Variation(type, y, backcolor));
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
		int level = 0;
		for (Variation ev : events) {
			if (ev.y > y) {
				return level;
			}
			if (ev.type == LifeSegmentVariation.LARGER) {
				level++;
			} else {
				level--;
				if (level < 0) {
					level = 0;
				}
			}
		}
		assert level >= 0;
		return level;
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

	Collection<Segment> getSegments() {
		final Collection<Segment> result = new ArrayList<Segment>();
		for (int i = 0; i < events.size(); i++) {
			final Segment seg = getSegment(i);
			if (seg != null) {
				result.add(seg);
			}
		}
		return result;
	}

	private Segment getSegment(int i) {
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
				return new Segment(events.get(i).y, events.get(j).y, events.get(i).backcolor);
			}
		}
		return new Segment(events.get(i).y, events.get(events.size() - 1).y, events.get(i).backcolor);
	}

	public void drawU(UGraphic ug, Skin skin, ISkinParam skinParam) {
		final StringBounder stringBounder = ug.getStringBounder();

		final double atX = ug.getTranslateX();
		final double atY = ug.getTranslateY();

		ug.translate(getStartingX(stringBounder), 0);

		for (Segment seg : getSegments()) {
			final ISkinParam skinParam2 = new SkinParamBackcolored(skinParam, seg.getSpecificBackColor());
			final Component comp = skin.createComponent(ComponentType.ALIVE_LINE, skinParam2, null);
			final int currentLevel = getLevel(seg.getPos1());
			seg.drawU(ug, comp, currentLevel);
		}

		ug.setTranslate(atX, atY);

	}

	private double create = 0;

	public final void setCreate(double create) {
		this.create = create;
	}

	public final double getCreate() {
		return create;
	}
}
