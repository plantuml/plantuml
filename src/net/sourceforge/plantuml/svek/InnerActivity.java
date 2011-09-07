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
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public final class InnerActivity implements IEntityImage {

	private final IEntityImage im;
	private final HtmlColor borderColor;

	public InnerActivity(final IEntityImage im, HtmlColor borderColor) {
		this.im = im;
		this.borderColor = borderColor;
	}

	public final static double THICKNESS_BORDER = 1.5;

	public void drawU(UGraphic ug, double x, double y) {
		final Dimension2D total = getDimension(ug.getStringBounder());

		ug.getParam().setColor(borderColor);
		ug.getParam().setBackcolor(null);
		ug.getParam().setStroke(new UStroke(THICKNESS_BORDER));
		ug.draw(x, y, new URectangle(total.getWidth(), total.getHeight(), EntityImageState.CORNER,
				EntityImageState.CORNER));
		ug.getParam().setStroke(new UStroke());
		im.drawU(ug, x, y);
	}

	public HtmlColor getBackcolor() {
		return im.getBackcolor();
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D img = im.getDimension(stringBounder);
		return img;
	}

	public ShapeType getShapeType() {
		return ShapeType.ROUND_RECTANGLE;
	}
	
	public int getShield() {
		return 0;
	}


}
