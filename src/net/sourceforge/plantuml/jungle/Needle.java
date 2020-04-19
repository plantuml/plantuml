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
package net.sourceforge.plantuml.jungle;

import java.util.List;

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class Needle implements UDrawable {

	private final double length;
	private final Display display;
	private final double degreePosition;
	private final double degreeOperture;

	private Needle(Display display, double length, double degreePosition, double degreeOperture) {
		this.display = display;
		this.degreePosition = degreePosition;
		this.degreeOperture = degreeOperture;
		this.length = length;
	}

	public void drawU(UGraphic ug) {
		GTileNode.getTextBlock(display);
		ug.draw(getLine());

		ug = ug.apply(getTranslate(length));
		GTileNode.getTextBlock(display).drawU(ug);
	}

	private ULine getLine() {
		final UTranslate translate = getTranslate(length);
		return new ULine(translate.getDx(), translate.getDy());
	}

	public UTranslate getTranslate(double dist) {
		final double angle = degreePosition * Math.PI / 180.0;
		final double dx = dist * Math.cos(angle);
		final double dy = dist * Math.sin(angle);
		return new UTranslate(dx, dy);
	}

	public UDrawable addChildren(final List<GNode> children) {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				Needle.this.drawU(ug);
				if (children.size() == 0) {
					return;
				}
				ug = ug.apply(getTranslate(length / 2));
				final UDrawable child1 = getNeedle(children.get(0), length / 2, degreePosition + degreeOperture,
						degreeOperture / 2);
				child1.drawU(ug);
				if (children.size() == 1) {
					return;
				}
				final UDrawable child2 = getNeedle(children.get(1), length / 2, degreePosition - degreeOperture,
						degreeOperture / 2);
				child2.drawU(ug);

			}
		};
	}

	public static UDrawable getNeedle(GNode root, double length, double degree, double degreeOperture) {
		final Needle needle0 = new Needle(root.getDisplay(), length, degree, degreeOperture);
		final UDrawable n1 = needle0.addChildren(root.getChildren());
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				ug = ug.apply(HColorUtils.BLACK);
				n1.drawU(ug);
			}
		};
	}

}