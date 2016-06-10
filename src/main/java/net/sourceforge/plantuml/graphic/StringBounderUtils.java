/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 19265 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UFont;

public class StringBounderUtils {

	final static BufferedImage imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
	final static Graphics2D gg = imDummy.createGraphics();
	
	public static StringBounder asStringBounder() {

		return new StringBounder() {
			public Dimension2D calculateDimension(UFont font, String text) {
				final FontMetrics fm = gg.getFontMetrics(font.getFont());
				final Rectangle2D rect = fm.getStringBounds(text, gg);
				return new Dimension2DDouble(rect.getWidth(), rect.getHeight());
			}
		};
	}
}
