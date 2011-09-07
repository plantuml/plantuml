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
 * Revision $Revision: 6577 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class TextBlockVertical implements TextBlock {

	private final TextBlock b1;
	private final TextBlock b2;

	public TextBlockVertical(TextBlock b1, TextBlock b2) {
		this.b1 = b1;
		this.b2 = b2;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim1 = b1.calculateDimension(stringBounder);
		final Dimension2D dim2 = b2.calculateDimension(stringBounder);
		return Dimension2DDouble.mergeTB(dim1, dim2);
	}

	public void drawTOBEREMOVED(ColorMapper colorMapper, Graphics2D g2d, double x, double y) {
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug, double x, double y) {
		b1.drawU(ug, x, y);
		final Dimension2D dim1 = b1.calculateDimension(ug.getStringBounder());
		b2.drawU(ug, x, y + dim1.getHeight());
	}

}