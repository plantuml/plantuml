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

import static net.sourceforge.plantuml.utils.ObjectUtils.instanceOfAny;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class TextLimitFinder extends UGraphicNo {

	@Override
	public UGraphic apply(UChange change) {
		return new TextLimitFinder(this, change);
	}

	private final MinMaxMutable minmax;

	public TextLimitFinder(StringBounder stringBounder, boolean initToZero) {
		super(stringBounder);
		this.minmax = MinMaxMutable.getEmpty(initToZero);
	}

	private TextLimitFinder(TextLimitFinder other, UChange change) {
		super(other, change);
		if (!instanceOfAny(change,
				UBackground.class,
				HColor.class,
				UStroke.class,
				UTranslate.class
		)) {
			throw new UnsupportedOperationException(change.getClass().toString());
		}
		this.minmax = other.minmax;
	}

	public void draw(UShape shape) {
		if (shape instanceof UText) {
			final double x = getTranslate().getDx();
			final double y = getTranslate().getDy();
			drawText(x, y, (UText) shape);
		}
	}

	private void drawText(double x, double y, UText text) {
		final Dimension2D dim = getStringBounder().calculateDimension(text.getFontConfiguration().getFont(), text.getText());
		y -= dim.getHeight() - 1.5;
		minmax.addPoint(x, y);
		minmax.addPoint(x, y + dim.getHeight());
		minmax.addPoint(x + dim.getWidth(), y);
		minmax.addPoint(x + dim.getWidth(), y + dim.getHeight());
	}

	public double getMaxX() {
		return minmax.getMaxX();
	}

	public double getMaxY() {
		return minmax.getMaxY();
	}

	public double getMinX() {
		return minmax.getMinX();
	}

	public double getMinY() {
		return minmax.getMinY();
	}

}
