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
 * Revision $Revision: 4129 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable3;

public abstract class UGraphicUtils {

	public static UDrawable3 translate(final UDrawable3 d, final double dx, final double dy) {
		return new UDrawable3() {
			@Override
			public void drawU(UGraphic ug, double x, double y) {
				d.drawU(ug, x + dx, y + dy);
			}
		};

	}

	public static UGraphic translate(final UGraphic g, final double tx, final double ty) {
		return new UGraphic() {

			public void centerChar(double x, double y, char c, UFont font) {
				g.centerChar(tx + x, ty + y, c, font);
			}

			public void draw(double x, double y, UShape shape) {
				g.draw(tx + x, ty + y, shape);
			}

			public UParam getParam() {
				return g.getParam();
			}

			public StringBounder getStringBounder() {
				return g.getStringBounder();
			}

			public double getTranslateX() {
				return g.getTranslateX();
			}

			public double getTranslateY() {
				return g.getTranslateY();
			}

			public void setClip(UClip clip) {
				throw new UnsupportedOperationException();
			}

			public void setTranslate(double dx, double dy) {
				g.setTranslate(dx, dy);
			}

			public void translate(double dx, double dy) {
				g.translate(dx, dy);
			}

			public void setAntiAliasing(boolean trueForOn) {
				g.setAntiAliasing(trueForOn);
			}

			public void setUrl(String url, String tooltip) {
				g.setUrl(url, tooltip);
			}

			public ColorMapper getColorMapper() {
				return g.getColorMapper();
			}
		};
	}

}
