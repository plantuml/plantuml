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

public class Skeleton {

	private final List<Entry> entries = new ArrayList<Entry>();

	static class Entry {
		private final double xpos;
		private final double ypos;

		public Entry(double x, double y) {
			this.xpos = x;
			this.ypos = y;
		}

		public void drawRectangle(UGraphic ug) {
			ug.apply(new UTranslate(xpos, ypos)).draw(new URectangle(2, 2));
		}
	}

	public void add(double x, double y) {
		entries.add(new Entry(x, y));
	}

	public void draw(UGraphic ug, double x, double y) {
		for (int i = 0; i < entries.size(); i++) {
			final Entry en = entries.get(i);
			if (i + 1 < entries.size() && entries.get(i + 1).xpos > en.xpos) {
				en.drawRectangle(ug);
			}
			Entry parent = null;
			for (int j = 0; j < i; j++) {
				final Entry en0 = entries.get(j);
				if (en0.xpos < en.xpos) {
					parent = en0;
				}
			}
			if (parent != null) {
				drawChild(ug, parent, en);
			}
		}
	}

	private void drawChild(UGraphic ug, Entry parent, Entry child) {
		final double dy = child.ypos - parent.ypos - 2;
		ug.apply(new UTranslate(parent.xpos + 1, parent.ypos + 3)).draw(ULine.vline(dy));

		final double dx = child.xpos - parent.xpos - 2;
		ug.apply(new UTranslate(parent.xpos + 1, child.ypos + 1)).draw(ULine.hline(dx));

	}

}
