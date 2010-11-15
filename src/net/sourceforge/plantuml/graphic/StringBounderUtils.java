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
 * Revision $Revision: 4110 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;

public class StringBounderUtils {

	public static StringBounder asStringBounder(final Graphics2D g2d) {
		
		return new StringBounder() {
			public Dimension2D calculateDimension(Font font, String text) {
				final FontMetrics fm = g2d.getFontMetrics(font);
				final Rectangle2D rect = fm.getStringBounds(text, g2d);
				return new Dimension2DDouble(rect.getWidth(), rect.getHeight());
			}

			public double getFontDescent(Font font) {
				final FontMetrics fm = g2d.getFontMetrics(font);
				return fm.getDescent();
			}

			public double getFontAscent(Font font) {
				final FontMetrics fm = g2d.getFontMetrics(font);
				return fm.getAscent();
			}

//			public UnusedSpace getUnusedSpace(Font font, char c) {
//				//return new UnusedSpace(7, 6, 0, 2);
//				return new UnusedSpace(font, c);
//			}
		};
	}
}
