/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class TextBlockMarged extends AbstractTextBlock implements TextBlock {

	private final TextBlock textBlock;
	private final double x1;
	private final double x2;
	private final double y1;
	private final double y2;

	public TextBlockMarged(TextBlock textBlock, double x1, double x2, double y1, double y2) {
		this.textBlock = textBlock;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, x1 + x2, y1 + y2);
	}

	public void drawU(UGraphic ug) {
		final UTranslate translate = new UTranslate(x1, y1);
		textBlock.drawU(ug.apply(translate));
	}

	@Override
	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder) {
		final Rectangle2D parent = textBlock.getInnerPosition(member, stringBounder);
		if (parent == null) {
			return null;
		}
		final UTranslate translate = new UTranslate(x1, y1);
		return translate.apply(parent);
	}

}