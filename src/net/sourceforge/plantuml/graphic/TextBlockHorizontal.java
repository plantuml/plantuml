/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 6577 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class TextBlockHorizontal extends AbstractTextBlock implements TextBlock {

	private final TextBlock b1;
	private final TextBlock b2;
	private final VerticalAlignment alignment;

	public TextBlockHorizontal(TextBlock b1, TextBlock b2, VerticalAlignment alignment) {
		this.b1 = b1;
		this.b2 = b2;
		this.alignment = alignment;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim1 = b1.calculateDimension(stringBounder);
		final Dimension2D dim2 = b2.calculateDimension(stringBounder);
		return Dimension2DDouble.mergeLR(dim1, dim2);
	}

	public void drawU(UGraphic ug) {
		final Dimension2D dim = calculateDimension(ug.getStringBounder());
		final Dimension2D dimb1 = b1.calculateDimension(ug.getStringBounder());
		final Dimension2D dimb2 = b2.calculateDimension(ug.getStringBounder());
		final Dimension2D dim1 = b1.calculateDimension(ug.getStringBounder());
		if (alignment == VerticalAlignment.CENTER) {
			b1.drawU(ug.apply(new UTranslate(0, ((dim.getHeight() - dimb1.getHeight()) / 2))));
			b2.drawU(ug.apply(new UTranslate(dim1.getWidth(), ((dim.getHeight() - dimb2.getHeight()) / 2))));
		} else {
			b1.drawU(ug);
			b2.drawU(ug.apply(new UTranslate(dim1.getWidth(), 0)));
		}
	}

}