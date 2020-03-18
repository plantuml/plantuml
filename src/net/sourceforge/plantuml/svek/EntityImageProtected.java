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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.cucadiagram.dot.Neighborhood;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class EntityImageProtected extends AbstractTextBlock implements IEntityImage, Untranslated {

	private final IEntityImage orig;
	private final double border;
	private final Bibliotekon bibliotekon;
	private final Neighborhood neighborhood;
	
	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		throw new UnsupportedOperationException();
	}


	public EntityImageProtected(IEntityImage orig, double border, Neighborhood neighborhood, Bibliotekon bibliotekon) {
		this.orig = orig;
		this.border = border;
		this.bibliotekon = bibliotekon;
		this.neighborhood = neighborhood;
	}

	public boolean isHidden() {
		return orig.isHidden();
	}

	public HColor getBackcolor() {
		return orig.getBackcolor();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return Dimension2DDouble.delta(orig.calculateDimension(stringBounder), 2 * border);
	}

	public void drawU(UGraphic ug) {
		orig.drawU(ug.apply(new UTranslate(border, border)));
	}

	public void drawUntranslated(UGraphic ug, double minX, double minY) {
		final Dimension2D dim = orig.calculateDimension(ug.getStringBounder());
		neighborhood.drawU(ug, minX + border, minY + border, bibliotekon, dim);
	}

	public ShapeType getShapeType() {
		return orig.getShapeType();
	}

	public Margins getShield(StringBounder stringBounder) {
		return orig.getShield(stringBounder);
	}
	
	public double getOverscanX(StringBounder stringBounder) {
		return orig.getOverscanX(stringBounder);
	}


}