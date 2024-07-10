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
package net.sourceforge.plantuml.mindmap;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.StyleBuilder;

class Branch implements UDrawable {
	private Idea root;
	private Idea last;
	private Finger finger;

	void initRoot(StyleBuilder styleBuilder, HColor backColor, Display label, IdeaShape shape, Stereotype stereotype) {
		root = Idea.createIdeaSimple(styleBuilder, backColor, label, shape, stereotype);
		last = root;
	}

	void initFinger(ISkinParam skinParam, boolean direction) {
		finger = FingerImpl.build(root, skinParam, direction);
	}

	Idea getParentOfLast(int nb) {
		Idea result = last;
		for (int i = 0; i < nb; i++)
			result = result.getParent();

		return result;
	}

	CommandExecutionResult add(StyleBuilder styleBuilder, HColor backColor, int level, Display label, IdeaShape shape,
			Stereotype stereotype) {
		if (last == null)
			return CommandExecutionResult.error("Check your indentation ?");

		if (level == last.getLevel() + 1) {
			final Idea newIdea = last.createIdea(styleBuilder, backColor, level, label, shape, stereotype);
			last = newIdea;
			return CommandExecutionResult.ok();
		}
		if (level <= last.getLevel()) {
			final int diff = last.getLevel() - level + 1;
			final Idea newIdea = getParentOfLast(diff).createIdea(styleBuilder, backColor, level, label, shape,
					stereotype);
			last = newIdea;
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("error42L");
	}

	public boolean hasFinger() {
		return finger != null;
	}

	public void drawU(UGraphic ug) {
		if (finger != null)
			finger.drawU(ug);
	}

	public double getHalfThickness(StringBounder stringBounder) {
		if (finger == null)
			return 0;
		return finger.getFullThickness(stringBounder) / 2;
	}

	public double getFullElongation(StringBounder stringBounder) {
		if (finger == null)
			return 0;
		return finger.getFullElongation(stringBounder);
	}

	public boolean hasChildren() {
		return root.hasChildren();
	}

	public boolean hasRoot() {
		return root != null;
	}

	public void doNotDrawFirstPhalanx() {
		finger.doNotDrawFirstPhalanx();
	}

	public double getX12(StringBounder stringBounder) {
		if (finger == null)
			return 0;
		return finger.getFullElongation(stringBounder) + ((FingerImpl) finger).getX12();
	}

}
