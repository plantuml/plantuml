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
package net.sourceforge.plantuml.wire;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class Block extends AbstractTextBlock {

	static class Pos {
		final double x;
		final double y;

		public Pos(double x, double y) {
			this.x = x;
			this.y = y;
		}

		UGraphic move(UGraphic ug) {
			return ug.apply(new UTranslate(x, y));
		}
	}

	private final Map<Block, Pos> children = new LinkedHashMap<Block, Pos>();
	private final Display display;
	private final Dimension2DDouble fixedDim;
	private final ISkinParam skinParam;

	private final List<String> left = new ArrayList<String>();
	private final List<String> right = new ArrayList<String>();
	private final List<String> top = new ArrayList<String>();
	private final List<String> bottom = new ArrayList<String>();

	private double x = 10;
	private double y = 10;

	private MinMax minMax = MinMax.getEmpty(true);
	private Block parent;

	public Block(ISkinParam skinParam) {
		this(skinParam, Display.empty(), null);
	}

	private Block(ISkinParam skinParam, Display display, Dimension2DDouble fixedDim) {
		this.skinParam = skinParam;
		this.display = display;
		this.fixedDim = fixedDim;

	}

	private List<String> getPins(Position position) {
		switch (position) {
		case LEFT:
			return left;
		case RIGHT:
			return right;
		case TOP:
			return top;
		case BOTTOM:
			return bottom;
		}
		throw new IllegalArgumentException();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		if (fixedDim == null) {
			return minMax.getDimension();
		}
		return fixedDim;
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(HColorUtils.BLACK);
		if (children.size() == 0) {
			final TextBlock label = display.create(new FontConfiguration(skinParam, FontParam.COMPONENT, null),
					HorizontalAlignment.CENTER, skinParam);
			label.drawU(ug.apply(new UTranslate(10, 10)));
		} else {
			for (Entry<Block, Pos> ent : children.entrySet()) {
				ent.getKey().drawU(ent.getValue().move(ug));
			}
		}
		ug.draw(new URectangle(calculateDimension(ug.getStringBounder())));

		drawPins(Position.BOTTOM, ug);
		drawPins(Position.TOP, ug);
		drawPins(Position.LEFT, ug);
		drawPins(Position.RIGHT, ug);

	}

	private void drawPins(Position pos, UGraphic ug) {
		double px = -2;
		double py = 10;
		if (pos == Position.RIGHT) {
			px = calculateDimension(ug.getStringBounder()).getWidth() - 2;
		}
		if (pos == Position.TOP) {
			px = 10;
			py = -2;
		}
		if (pos == Position.BOTTOM) {
			px = 10;
			py = calculateDimension(ug.getStringBounder()).getHeight() - 2;
		}
		for (String pin : getPins(pos)) {
			ug.apply(new UTranslate(px, py)).draw(new UEllipse(4, 4));
			if (pos == Position.LEFT || pos == Position.RIGHT) {
				py += 15;
			} else {
				px += 15;
			}
		}
	}

	public Block componentEnd() {
		parent.minMax = parent.minMax.addPoint(parent.x + this.minMax.getMaxX() + 10,
				parent.y + this.minMax.getMaxY() + 10);
		parent.x += this.minMax.getMaxX() + 10;
		return parent;
	}

	public Block addNewBlock(String name, int width, int height) {
		final Dimension2DDouble dim = new Dimension2DDouble(width, height);
		final Block child = new Block(skinParam, Display.create(name), dim);
		children.put(child, new Pos(x, y));
		y += dim.getHeight() + 10;
		minMax = minMax.addPoint(x + dim.getWidth() + 10, y);
		return child;
	}

	public Block createContainer(String name) {
		final Block result = new Block(skinParam);
		result.parent = this;
		children.put(result, new Pos(x, y));
		return result;
	}

	public void vspace(int vspace) {
		y += vspace - 10;
		minMax = minMax.addPoint(x, y);
	}

	public void newColumn() {
		this.x = minMax.getMaxX();
		this.y = 10;
	}

	public void addPin(Position position, String pin) {
		getPins(position).add(pin);
	}

}
