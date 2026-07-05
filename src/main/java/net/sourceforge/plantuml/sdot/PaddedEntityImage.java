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
package net.sourceforge.plantuml.sdot;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.Margins;
import net.sourceforge.plantuml.svek.ShapeType;

// Adds fixed pixel padding around an IEntityImage: the wrapped image is drawn
// shifted by (left, top), and the declared dimension grows by the padding on
// all requested sides.
//
// Used by GroupMakerStateSmetana to give Smetana-rendered concurrent regions
// some breathing room when stacked by ConcurrentStates (which otherwise places
// adjacent regions edge-to-edge with no margin of its own), and to reserve
// space for ConcurrentStates' own separator dash overshoot, without touching
// ConcurrentStates itself (shared with the dot pipeline).
final class PaddedEntityImage implements IEntityImage {

	private final IEntityImage image;
	private final double left;
	private final double top;
	private final double right;
	private final double bottom;

	PaddedEntityImage(IEntityImage image, double left, double top, double right, double bottom) {
		this.image = image;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	static PaddedEntityImage uniform(IEntityImage image, double padding) {
		return new PaddedEntityImage(image, padding, padding, padding, padding);
	}

	public void drawU(UGraphic ug) {
		image.drawU(ug.apply(new UTranslate(left, top)));
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D dim = image.calculateDimension(stringBounder);
		return new XDimension2D(dim.getWidth() + left + right, dim.getHeight() + top + bottom);
	}

	public HColor getBackcolor() {
		return image.getBackcolor();
	}

	public ShapeType getShapeType() {
		return image.getShapeType();
	}

	public Margins getShield(StringBounder stringBounder) {
		return image.getShield(stringBounder);
	}

	public double getOverscanX(StringBounder stringBounder) {
		return image.getOverscanX(stringBounder);
	}

	public boolean isHidden() {
		return image.isHidden();
	}

}
