/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 4632 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ComponentRoseArrow extends AbstractComponentRoseArrow {

	private final int direction;

	public ComponentRoseArrow(Color foregroundColor, Color fontColor, Font font,
			List<? extends CharSequence> stringsToDisplay, int direction, boolean dotted, boolean full) {
		super(foregroundColor, fontColor, font, stringsToDisplay, dotted, full);
		this.direction = direction;
		if (direction != 1 && direction != -1) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void drawInternalU(UGraphic ug, Dimension2D dimensionToUse) {
		final StringBounder stringBounder = ug.getStringBounder();
		final int textHeight = (int) getTextHeight(stringBounder);
		ug.getParam().setColor(getForegroundColor());

		final int x2 = (int) dimensionToUse.getWidth();

		if (isDotted()) {
			stroke(ug, 2);
		}

		//
		ug.draw(0, textHeight, new ULine(x2, 0));
		if (isDotted()) {
			ug.getParam().setStroke(new UStroke());
		}
		final int direction = getDirection();
		if (direction == 1) {
			if (isFull()) {
				ug.getParam().setBackcolor(getForegroundColor());
				final UPolygon polygon = new UPolygon();
				polygon.addPoint(x2 - getArrowDeltaX(), textHeight - getArrowDeltaY());
				polygon.addPoint(x2, textHeight);
				polygon.addPoint(x2 - getArrowDeltaX(), textHeight + getArrowDeltaY());
				ug.draw(0, 0, polygon);
				ug.getParam().setBackcolor(null);
			} else {
				ug.draw(x2, textHeight, new ULine(-getArrowDeltaX(), -getArrowDeltaY()));
				ug.draw(x2, textHeight, new ULine(-getArrowDeltaX(), getArrowDeltaY()));
			}
		} else {
			if (isFull()) {
				ug.getParam().setBackcolor(getForegroundColor());
				final UPolygon polygon = new UPolygon();
				polygon.addPoint(getArrowDeltaX(), textHeight - getArrowDeltaY());
				polygon.addPoint(0, textHeight);
				polygon.addPoint(getArrowDeltaX(), textHeight + getArrowDeltaY());
				ug.draw(0, 0, polygon);
				ug.getParam().setBackcolor(null);
			} else {
				ug.draw(0, textHeight, new ULine(getArrowDeltaX(), -getArrowDeltaY()));
				ug.draw(0, textHeight, new ULine(getArrowDeltaX(), getArrowDeltaY()));
			}
		}
		getTextBlock().drawU(ug, getMarginX1(), 0);
	}

	public Point2D getStartPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(stringBounder);
		if (direction == 1) {
			return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
		}
		return new Point2D.Double(dimensionToUse.getWidth() + getPaddingX(), textHeight + getPaddingY());
	}

	public Point2D getEndPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(stringBounder);
		if (direction == 1) {
			return new Point2D.Double(dimensionToUse.getWidth() + getPaddingX(), textHeight + getPaddingY());
		}
		return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
	}

	final protected int getDirection() {
		return direction;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + getArrowDeltaY() + 2 * getPaddingY();
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder);
	}

}
