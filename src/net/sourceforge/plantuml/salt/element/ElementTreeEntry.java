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
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ElementTreeEntry {

	private final Element firstElement;
	private final int level;
	private final List<Element> otherElements = new ArrayList<Element>();

	public ElementTreeEntry(int level, Element elmt) {
		this.firstElement = elmt;
		this.level = level;
	}

	public void addCell(Element elmt) {
		this.otherElements.add(elmt);
	}

	public Dimension2D getPreferredDimensionFirstCell(StringBounder stringBounder) {
		return Dimension2DDouble.delta(firstElement.getPreferredDimension(stringBounder, 0, 0), getXDelta(), 0);
	}

	public ListWidth getPreferredDimensionOtherCell(StringBounder stringBounder) {
		final ListWidth result = new ListWidth();
		for (Element element : otherElements) {
			result.add(element.getPreferredDimension(stringBounder, 0, 0).getWidth());
		}
		return result;
	}

	public double getXDelta() {
		return level * 10;
	}

	public void drawFirstCell(UGraphic ug, double x, double y) {
		firstElement.drawU(ug.apply(new UTranslate(x + getXDelta(), y)), 0, null);
	}

	public void drawSecondCell(UGraphic ug, double x, double y, ListWidth otherWidth, double margin) {
		final Iterator<Double> it = otherWidth.iterator();
		for (Element element : otherElements) {
			element.drawU(ug.apply(new UTranslate(x, y)), 0, null);
			x += it.next() + margin;
		}
	}

}
