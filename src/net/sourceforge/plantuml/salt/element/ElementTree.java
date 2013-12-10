/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 3835 $
 *
 */
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ElementTree implements Element {

	private final Collection<ElementTreeEntry> entries = new ArrayList<ElementTreeEntry>();
	private final UFont font;
	private final SpriteContainer spriteContainer;

	public ElementTree(UFont font, SpriteContainer spriteContainer) {
		this.font = font;
		this.spriteContainer = spriteContainer;
	}

	public void addEntry(String s) {
		int level = 0;
		while (s.startsWith("+")) {
			level++;
			s = s.substring(1);
		}
		entries.add(new ElementTreeEntry(level, s.trim(), font, spriteContainer));
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		double w = 0;
		double h = 0;
		for (ElementTreeEntry entry : entries) {
			final Dimension2D dim = entry.getPreferredDimension(stringBounder, x, y);
			w = Math.max(w, dim.getWidth());
			h += dim.getHeight();
		}
		return new Dimension2DDouble(w, h);
	}

	public void drawU(UGraphic ug, final double x, final double y, int zIndex, Dimension2D dimToUse) {
		if (zIndex != 0) {
			return;
		}

		final Skeleton skeleton = new Skeleton();

		double yvar = y;

		for (ElementTreeEntry entry : entries) {
			entry.drawU(ug, x, yvar, zIndex, dimToUse);
			final double h = entry.getPreferredDimension(ug.getStringBounder(), x, yvar).getHeight();
			skeleton.add(x + entry.getXDelta() - 10, yvar + h / 2 - 1);
			yvar += h;
		}
		ug = ug.apply(new UChangeColor(HtmlColorUtils.getColorIfValid("#888888")));
		skeleton.draw(ug, x, y);
	}

	static class Skeleton {

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
			ug.apply(new UTranslate(parent.xpos + 1, parent.ypos + 3)).draw(new ULine(0, dy));

			final double dx = child.xpos - parent.xpos - 2;
			ug.apply(new UTranslate(parent.xpos + 1, child.ypos + 1)).draw(new ULine(dx, 0));

		}

	}

}
