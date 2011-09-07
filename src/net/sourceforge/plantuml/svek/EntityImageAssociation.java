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
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageAssociation extends AbstractEntityImage {

	final private static int SIZE = 12;

	public EntityImageAssociation(IEntity entity, ISkinParam skinParam) {
		super(entity, skinParam);
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(SIZE * 2, SIZE * 2);
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		// final StringBounder stringBounder = ug.getStringBounder();
		// final Dimension2D dimTotal = getDimension(stringBounder);
		//
		// final double widthTotal = dimTotal.getWidth();
		// final double heightTotal = dimTotal.getHeight();

		final UPolygon diams = new UPolygon();
		diams.setDeltaShadow(5);
		diams.addPoint(SIZE, 0);
		diams.addPoint(SIZE * 2, SIZE);
		diams.addPoint(SIZE, SIZE * 2);
		diams.addPoint(0, SIZE);
		diams.addPoint(SIZE, 0);

		ug.getParam().setStroke(new UStroke(1.5));
		ug.getParam().setColor(getColor(ColorParam.classBorder, getStereo()));
		ug.getParam().setBackcolor(getColor(ColorParam.classBackground, getStereo()));
		ug.draw(xTheoricalPosition, yTheoricalPosition, diams);
		ug.getParam().setStroke(new UStroke());
	}

	public ShapeType getShapeType() {
		return ShapeType.DIAMOND;
	}
	
	public int getShield() {
		return 0;
	}

}
