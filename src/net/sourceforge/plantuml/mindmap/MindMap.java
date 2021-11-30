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
package net.sourceforge.plantuml.mindmap;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.style.NoStyleAvailableException;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class MindMap implements UDrawable {

	private final Branch left = new Branch();
	private final Branch right = new Branch();

	private final ISkinParam skinParam;

	public MindMap(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	private void computeFinger() {
		if (this.left.hasFinger() == false && this.right.hasFinger() == false) {
			if (this.left.hasChildren())
				left.initFinger(skinParam, Direction.LEFT);

			if (this.left.hasFinger() == false || this.right.hasChildren())
				right.initFinger(skinParam, Direction.RIGHT);

			if (this.left.hasFinger() && this.right.hasFinger())
				this.left.doNotDrawFirstPhalanx();

		}
	}

	Dimension2D calculateDimension(StringBounder stringBounder) {
		this.computeFinger();
		final double y1 = this.right.getHalfThickness(stringBounder);
		final double y2 = this.left.getHalfThickness(stringBounder);
		final double y = Math.max(y1, y2);

		final double x = this.left.getFullElongation(stringBounder);

		final double width = x + this.right.getFullElongation(stringBounder);
		final double height = y
				+ Math.max(this.left.getHalfThickness(stringBounder), this.right.getHalfThickness(stringBounder));
		return new Dimension2DDouble(width, height);

	}

	@Override
	public void drawU(UGraphic ug) {
		if (this.left.hasRoot() == false && this.right.hasRoot() == false)
			return;

		this.computeFinger();

		final StringBounder stringBounder = ug.getStringBounder();
		final double y1 = this.right.getHalfThickness(stringBounder);
		final double y2 = this.left.getHalfThickness(stringBounder);
		final double y = Math.max(y1, y2);

		final double x = this.left.getX12(stringBounder);
		this.right.drawU(ug.apply(new UTranslate(x, y)));
		this.left.drawU(ug.apply(new UTranslate(x, y)));
	}

	CommandExecutionResult addIdeaInternal(String stereotype, HColor backColor, int level, Display label,
			IdeaShape shape, Direction direction) {
		try {
			if (this.left.hasRoot() == false && this.right.hasRoot() == false)
				level = 0;

			if (level == 0) {
				this.right.initRoot(skinParam.getCurrentStyleBuilder(), backColor, label, shape, stereotype);
				this.left.initRoot(skinParam.getCurrentStyleBuilder(), backColor, label, shape, stereotype);
				return CommandExecutionResult.ok();
			}
			if (direction == Direction.LEFT)
				return this.left.add(skinParam.getCurrentStyleBuilder(), backColor, level, label, shape, stereotype);

			return this.right.add(skinParam.getCurrentStyleBuilder(), backColor, level, label, shape, stereotype);
		} catch (NoStyleAvailableException e) {
			// e.printStackTrace();
			return CommandExecutionResult.error("General failure: no style available.");
		}
	}

	boolean isFull(int level) {
		return level == 0 && this.right.hasRoot();
	}

}
