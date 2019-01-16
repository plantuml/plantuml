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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.utils.MathUtils;

public class HeaderLayout {

	final private TextBlock name;
	final private TextBlock stereo;
	final private TextBlock generic;
	final private TextBlock circledCharacter;

	public HeaderLayout(TextBlock circledCharacter, TextBlock stereo, TextBlock name, TextBlock generic) {
		this.circledCharacter = protectAgaintNull(circledCharacter);
		this.stereo = protectAgaintNull(stereo);
		this.name = protectAgaintNull(name);
		this.generic = protectAgaintNull(generic);
	}

	private static TextBlock protectAgaintNull(TextBlock block) {
		if (block == null) {
			return new TextBlockEmpty();
		}
		return block;
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final Dimension2D genericDim = generic.calculateDimension(stringBounder);
		final Dimension2D stereoDim = stereo.calculateDimension(stringBounder);
		final Dimension2D circleDim = circledCharacter.calculateDimension(stringBounder);

		final double width = circleDim.getWidth() + Math.max(stereoDim.getWidth(), nameDim.getWidth())
				+ genericDim.getWidth();
		final double height = MathUtils.max(circleDim.getHeight(), stereoDim.getHeight() + nameDim.getHeight() + 10,
				genericDim.getHeight());
		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug, double width, double height) {

		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final Dimension2D genericDim = generic.calculateDimension(stringBounder);
		final Dimension2D stereoDim = stereo.calculateDimension(stringBounder);
		final Dimension2D circleDim = circledCharacter.calculateDimension(stringBounder);

		final double widthStereoAndName = Math.max(stereoDim.getWidth(), nameDim.getWidth());
		final double suppWith = Math.max(0, width - circleDim.getWidth() - widthStereoAndName - genericDim.getWidth());
		assert suppWith >= 0;

		final double h2 = Math.min(circleDim.getWidth() / 4, suppWith * 0.1);
		final double h1 = (suppWith - h2) / 2;
		assert h1 >= 0;
		assert h2 >= 0;

		final double xCircle = h1;
		final double yCircle = (height - circleDim.getHeight()) / 2;
		circledCharacter.drawU(ug.apply(new UTranslate(xCircle, yCircle)));

		final double diffHeight = height - stereoDim.getHeight() - nameDim.getHeight();
		final double xStereo = circleDim.getWidth() + (widthStereoAndName - stereoDim.getWidth()) / 2 + h1 + h2;
		final double yStereo = diffHeight / 2;
		stereo.drawU(ug.apply(new UTranslate(xStereo, yStereo)));

		final double xName = circleDim.getWidth() + (widthStereoAndName - nameDim.getWidth()) / 2 + h1 + h2;
		final double yName = diffHeight / 2 + stereoDim.getHeight();
		name.drawU(ug.apply(new UTranslate(xName, yName)));

		if (genericDim.getWidth() > 0) {
			final double delta = 4;
			final double xGeneric = width - genericDim.getWidth() + delta;
			final double yGeneric = -delta;
			generic.drawU(ug.apply(new UTranslate(xGeneric, yGeneric)));
		}
	}

}
