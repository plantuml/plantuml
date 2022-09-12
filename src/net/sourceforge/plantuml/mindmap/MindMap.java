/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.style.NoStyleAvailableException;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class MindMap implements UDrawable {

	private final Branch regular = new Branch();
	private final Branch reverse = new Branch();

	private final ISkinParam skinParam;

	public MindMap(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	private void computeFinger() {
		if (this.reverse.hasFinger() == false && this.regular.hasFinger() == false) {
			if (this.reverse.hasChildren())
				reverse.initFinger(skinParam, false);

			if (this.reverse.hasFinger() == false || this.regular.hasChildren())
				regular.initFinger(skinParam, true);

			if (this.reverse.hasFinger() && this.regular.hasFinger())
				this.reverse.doNotDrawFirstPhalanx();

		}
	}

	XDimension2D calculateDimension(StringBounder stringBounder) {
		this.computeFinger();
		final double y1 = this.regular.getHalfThickness(stringBounder);
		final double y2 = this.reverse.getHalfThickness(stringBounder);
		final double y = Math.max(y1, y2);

		final double width = this.reverse.getX12(stringBounder) + this.regular.getX12(stringBounder);
		final double height = y
				+ Math.max(this.reverse.getHalfThickness(stringBounder), this.regular.getHalfThickness(stringBounder));
		if (skinParam.getRankdir() == Rankdir.TOP_TO_BOTTOM)
			return new XDimension2D(height, width);
		else
			return new XDimension2D(width, height);

	}

	@Override
	public void drawU(UGraphic ug) {
		if (this.reverse.hasRoot() == false && this.regular.hasRoot() == false)
			return;

		this.computeFinger();

		final StringBounder stringBounder = ug.getStringBounder();
		final double y1 = this.regular.getHalfThickness(stringBounder);
		final double y2 = this.reverse.getHalfThickness(stringBounder);
		final double y = Math.max(y1, y2);

		final double x = this.reverse.getX12(stringBounder);
		if (skinParam.getRankdir() == Rankdir.TOP_TO_BOTTOM)
			ug = ug.apply(new UTranslate(y, x));
		else
			ug = ug.apply(new UTranslate(x, y));
		this.regular.drawU(ug);
		this.reverse.drawU(ug);
	}

	CommandExecutionResult addIdeaInternal(String stereotype, HColor backColor, int level, Display label,
			IdeaShape shape, boolean direction) {
		try {
			if (this.reverse.hasRoot() == false && this.regular.hasRoot() == false)
				level = 0;

			if (level == 0) {
				this.regular.initRoot(skinParam.getCurrentStyleBuilder(), backColor, label, shape, stereotype);
				this.reverse.initRoot(skinParam.getCurrentStyleBuilder(), backColor, label, shape, stereotype);
				return CommandExecutionResult.ok();
			}
			if (direction == false)
				return this.reverse.add(skinParam.getCurrentStyleBuilder(), backColor, level, label, shape, stereotype);

			return this.regular.add(skinParam.getCurrentStyleBuilder(), backColor, level, label, shape, stereotype);
		} catch (NoStyleAvailableException e) {
			// Logme.error(e);
			return CommandExecutionResult.error("General failure: no style available.");
		}
	}

	boolean isFull(int level) {
		return level == 0 && this.regular.hasRoot();
	}

}
