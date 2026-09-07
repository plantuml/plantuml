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
package net.sourceforge.plantuml.salt.element;

import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public interface Element {

	public XDimension2D getPreferredDimension(StringBounder stringBounder, double x, double y);

	public void drawU(UGraphic ug, int zIndex, XDimension2D dimToUse);

	/**
	 * Whether this element - or any element reachable through it - may draw
	 * content that extends past its own {@link #getPreferredDimension}. An
	 * open salt droplist (see ElementDroplist) is the current example: its
	 * drop-down list is meant to float over / overlap whatever the layout put
	 * after it instead of reserving room for itself, so
	 * {@code getPreferredDimension} deliberately stays unaware of it - every
	 * grid/row/column in a salt diagram is sized from that method (see
	 * ElementPyramid#init), and growing it would defeat the "floats over"
	 * behavior.
	 *
	 * <p>
	 * That is fine for layout, but it means {@code getPreferredDimension}
	 * alone is not always enough to size the final image without clipping
	 * (see PSystemSalt#getTextBlock, issue #2882): PSystemSalt only pays for
	 * the more expensive - and not pixel-identical, see the caller - actual
	 * drawn-extent measurement when this returns {@code true} somewhere in
	 * the tree, so a diagram with nothing that overflows renders exactly as
	 * it did before this method existed. Composite elements must delegate to
	 * their children; the default is {@code false}.
	 */
	default boolean mayDrawBeyondPreferredDimension() {
		return false;
	}

}
