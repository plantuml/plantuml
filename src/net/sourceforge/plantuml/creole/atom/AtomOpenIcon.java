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
package net.sourceforge.plantuml.creole.atom;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.openiconic.OpenIcon;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class AtomOpenIcon extends AbstractAtom implements Atom {

	private final OpenIcon openIcon;
	private final double factor;
	private final Url url;
	private final HColor color;

	public AtomOpenIcon(HColor newColor, double scale, OpenIcon openIcon, FontConfiguration fontConfiguration,
			Url url) {
		this.url = url;
		this.openIcon = openIcon;
		this.factor = scale * fontConfiguration.getSize2D() / 12.0;
		this.color = newColor == null ? fontConfiguration.getColor() : newColor;
	}

	private TextBlock asTextBlock() {
		return TextBlockUtils.withMargin(openIcon.asTextBlock(color, factor), 1, 0);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return asTextBlock().calculateDimension(stringBounder);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return -3 * factor;
	}

	public void drawU(UGraphic ug) {
		if (url != null) {
			ug.startUrl(url);
		}
		asTextBlock().drawU(ug);
		if (url != null) {
			ug.closeAction();
		}
	}

}
