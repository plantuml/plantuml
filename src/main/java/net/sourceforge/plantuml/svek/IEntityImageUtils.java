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
package net.sourceforge.plantuml.svek;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.MagneticBorder;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;

public class IEntityImageUtils {

	public static IEntityImage translate(final IEntityImage orig, final UTranslate translate) {
		return new IEntityImage() {

			@Override
			public void drawU(UGraphic ug) {
				orig.drawU(ug.apply(translate));

			}

			@Override
			public MinMax getMinMax(StringBounder stringBounder) {
				return orig.getMinMax(stringBounder);

			}

			@Override
			public XRectangle2D getInnerPosition(CharSequence member, StringBounder stringBounder) {
				return orig.getInnerPosition(member, stringBounder);
			}

			@Override
			public HColor getBackcolor() {
				return orig.getBackcolor();
			}

			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return orig.calculateDimension(stringBounder);
			}

			@Override
			public boolean isHidden() {
				return orig.isHidden();
			}

			@Override
			public Margins getShield(StringBounder stringBounder) {
				return orig.getShield(stringBounder);
			}

			@Override
			public ShapeType getShapeType() {
				return orig.getShapeType();
			}

			@Override
			public double getOverscanX(StringBounder stringBounder) {
				return orig.getOverscanX(stringBounder);
			}

			@Override
			public MagneticBorder getMagneticBorder() {
				final MagneticBorder magneticBorder = orig.getMagneticBorder();
				return new MagneticBorder() {

					@Override
					public UTranslate getForceAt(StringBounder stringBounder, XPoint2D position) {
						return magneticBorder.getForceAt(stringBounder, translate.reverse().getTranslated(position));
					}
				};
			}

			@Override
			public boolean isCrash() {
				return orig.isCrash();
			}
		};
	}

}