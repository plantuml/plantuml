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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class EntityImageGroup extends AbstractEntityImage {

//	final private TextBlock desc;
//	final private static int MARGIN = 10;

	public EntityImageGroup(IEntity entity, ISkinParam skinParam) {
		super(entity, skinParam);
//		this.desc = TextBlockUtils.create(StringUtils.getWithNewlines(entity.getDisplay()), new FontConfiguration(
//				getFont(FontParam.ACTIVITY), HtmlColor.BLACK), HorizontalAlignement.CENTER);
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
//		final Dimension2D dim = desc.calculateDimension(stringBounder);
//		return Dimension2DDouble.delta(dim, MARGIN * 2);
		return new Dimension2DDouble(30, 30);
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
//		final StringBounder stringBounder = ug.getStringBounder();
//		final Dimension2D dimTotal = getDimension(stringBounder);
//
//		final double widthTotal = dimTotal.getWidth();
//		final double heightTotal = dimTotal.getHeight();
//		final URectangle rect = new URectangle(widthTotal, heightTotal, 25, 25);
//
//		ug.getParam().setStroke(new UStroke(1.5));
//		ug.getParam().setColor(getColor(ColorParam.activityBorder));
//		ug.getParam().setBackcolor(getColor(ColorParam.activityBackground));
//
//		ug.draw(xTheoricalPosition, yTheoricalPosition, rect);
//		ug.getParam().setStroke(new UStroke());
//
//		final double x = xTheoricalPosition + MARGIN;
//		final double y = yTheoricalPosition + MARGIN;
//		desc.drawU(ug, x, y);

	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}
}
