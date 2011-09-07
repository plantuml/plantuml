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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public final class InnerStateAutonom implements IEntityImage {

	private final IEntityImage im;
	private final TextBlock title;
	private final HtmlColor borderColor;
	private final HtmlColor backColor;

	public InnerStateAutonom(final IEntityImage im, final TextBlock title, HtmlColor borderColor, HtmlColor backColor) {
		this.im = im;
		this.title = title;
		this.borderColor = borderColor;
		this.backColor = backColor;
	}

	
	public final static double THICKNESS_BORDER = 1.5;

	public void drawU(UGraphic ug, double x, double y) {
		final Dimension2D text = title.calculateDimension(ug.getStringBounder());
		final Dimension2D total = getDimension(ug.getStringBounder());

		final double suppY = EntityImageState.MARGIN + text.getHeight() + EntityImageState.MARGIN_LINE;
		final RoundedContainer r = new RoundedContainer(total, suppY, borderColor, backColor, im.getBackcolor()); 
		
		r.drawU(ug, x, y);
		title.drawU(ug, x + (total.getWidth() - text.getWidth()) / 2, y + EntityImageState.MARGIN);

		im.drawU(ug, x + EntityImageState.MARGIN, y+suppY + EntityImageState.MARGIN_LINE);
	}

	public HtmlColor getBackcolor() {
		return null;
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D img = im.getDimension(stringBounder);
		final Dimension2D text = title.calculateDimension(stringBounder);

		final Dimension2D dim = Dimension2DDouble.mergeTB(text, img);

		final Dimension2D result = Dimension2DDouble.delta(dim, EntityImageState.MARGIN * 2 + 2
				* EntityImageState.MARGIN_LINE);

		return result;
	}

	public ShapeType getShapeType() {
		return ShapeType.ROUND_RECTANGLE;
	}
	
	public int getShield() {
		return 0;
	}


}
