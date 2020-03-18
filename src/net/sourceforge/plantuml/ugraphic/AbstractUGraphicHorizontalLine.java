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
package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.graphic.UGraphicDelegator;

public abstract class AbstractUGraphicHorizontalLine extends UGraphicDelegator {

	private UTranslate translate = new UTranslate();

	public UGraphic apply(UChange change) {
		final AbstractUGraphicHorizontalLine result;
		if (change instanceof UTranslate) {
			result = copy(getUg());
			result.translate = this.translate.compose((UTranslate) change);
		} else if (change instanceof UClip) {
			final UClip clip = ((UClip) change).translate(translate);
			result = copy(getUg().apply(clip));
			result.translate = this.translate;
		} else {
			result = copy(getUg().apply(change));
			result.translate = this.translate;
		}
		return result;
	}

	protected abstract AbstractUGraphicHorizontalLine copy(UGraphic ug);

	protected AbstractUGraphicHorizontalLine(UGraphic ug) {
		super(ug);
	}

	protected abstract void drawHline(UGraphic ug, UHorizontalLine line, UTranslate translate);

	public void draw(UShape shape) {
		if (shape instanceof UHorizontalLine) {
			drawHline(getUg(), (UHorizontalLine) shape, UTranslate.dy(translate.getDy()));
		} else {
			getUg().apply(translate).draw(shape);
		}
	}

}
