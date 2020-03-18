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
package net.sourceforge.plantuml.salt.element;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Skeleton2 {

	private final List<Entry> entries = new ArrayList<Entry>();
	private static final double sizeX = 8;

	static class Entry {
		private final int level;
		private final double ypos;

		Entry(int level, double y) {
			// System.err.println("level=" + level);
			this.level = level;
			this.ypos = y;
		}

		void drawHline(UGraphic ug) {
			final double xpos = getXStartForLevel(level);
			ug.apply(new UTranslate(xpos + sizeX - 1, ypos - 1)).draw(new URectangle(2, 2));
			ug.apply(new UTranslate(xpos, ypos)).draw(ULine.hline(sizeX));
		}

		public void drawVline(UGraphic ug, double lastY) {
			// System.err.println("ypos=" + ypos);
			final double xpos = getXStartForLevel(level);
			ug.apply(new UTranslate(xpos, lastY)).draw(ULine.vline(ypos - lastY));
		}
	}

	public void add(int level, double y) {
		entries.add(new Entry(level, y));
	}

	public void draw(UGraphic ug) {
		for (int i = 0; i < entries.size(); i++) {
			final Entry en = entries.get(i);
			en.drawHline(ug);
			final Entry up = getMotherOrSister(i);
			en.drawVline(ug, up == null ? 0 : up.ypos);
		}
	}

	private Entry getMotherOrSister(int idx) {
		final int currentLevel = entries.get(idx).level;
		for (int i = idx - 1; i >= 0; i--) {
			final int otherLevel = entries.get(i).level;
			if (otherLevel == currentLevel || otherLevel == currentLevel - 1) {
				return entries.get(i);
			}
		}
		return null;
	}

	private static double getXStartForLevel(int level) {
		return level * sizeX;
	}

	public double getXEndForLevel(int level) {
		return getXStartForLevel(level) + sizeX;
	}

	// public void drawOld(UGraphic ug, double x, double y) {
	// for (int i = 0; i < entries.size(); i++) {
	// final Entry en = entries.get(i);
	// if (i + 1 < entries.size() && entries.get(i + 1).xpos > en.xpos) {
	// en.drawRectangle(ug);
	// }
	// Entry parent = null;
	// for (int j = 0; j < i; j++) {
	// final Entry en0 = entries.get(j);
	// if (en0.xpos < en.xpos) {
	// parent = en0;
	// }
	// }
	// if (parent != null) {
	// drawChild(ug, parent, en);
	// }
	// }
	// }
	//
	// private void drawChild(UGraphic ug, Entry parent, Entry child) {
	// final double dy = child.ypos - parent.ypos - 2;
	// ug.apply(new UTranslate(parent.xpos + 1, parent.ypos + 3)).draw(ULine.dy(dy));
	//
	// final double dx = child.xpos - parent.xpos - 2;
	// ug.apply(new UTranslate(parent.xpos + 1, child.ypos + 1)).draw(new ULine(dx, 0));
	//
	// }

}
