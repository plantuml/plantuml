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

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class EntityImageCircleStart extends AbstractEntityImage {

	private static final int SIZE = 20;

	public EntityImageCircleStart(IEntity entity, ISkinParam skinParam) {
		super(entity, skinParam);
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(SIZE, SIZE);
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		final UEllipse circle = new UEllipse(SIZE, SIZE);
		circle.setDeltaShadow(3);
		ug.getParam().setColor(null);
		ug.getParam().setBackcolor(getColor(ColorParam.activityStart, getStereo()));
		ug.draw(xTheoricalPosition, yTheoricalPosition, circle);
	}

	public ShapeType getShapeType() {
		return ShapeType.CIRCLE;
	}

	public int getShield() {
		return 0;
	}

}
