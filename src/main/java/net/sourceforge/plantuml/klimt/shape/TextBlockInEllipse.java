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
package net.sourceforge.plantuml.klimt.shape;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.svek.image.ContainingEllipse;
import net.sourceforge.plantuml.svek.image.Footprint;

public class TextBlockInEllipse extends AbstractTextBlock implements TextBlock {
    // ::remove file when __HAXE__

	private final TextBlock text;
	private final ContainingEllipse ellipse;

	public TextBlockInEllipse(TextBlock text, StringBounder stringBounder) {
		this.text = text;
		final XDimension2D textDim = text.calculateDimension(stringBounder);
		double alpha = textDim.getHeight() / textDim.getWidth();
		if (alpha < .2) {
			alpha = .2;
		} else if (alpha > .8) {
			alpha = .8;
		}
		final Footprint footprint = new Footprint(stringBounder);
		ellipse = footprint.getEllipse(text, alpha);

	}

	public UEllipse getUEllipse() {
		return ellipse.asUEllipse().bigger(6);
	}

	public void drawU(UGraphic ug) {
		final UEllipse sh = getUEllipse();
		final XPoint2D center = ellipse.getCenter();

		final double dx = sh.getWidth() / 2 - center.getX();
		final double dy = sh.getHeight() / 2 - center.getY();

		ug.draw(sh);

		text.drawU(ug.apply(new UTranslate(dx, (dy - 2))));
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return getUEllipse().getDimension();
	}

	public void setDeltaShadow(double deltaShadow) {
		ellipse.setDeltaShadow(deltaShadow);
	}
}
