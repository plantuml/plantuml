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
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageLollipopInterface extends AbstractEntityImage {

	private static final int SIZE = 10;
	private final TextBlock desc;

	public EntityImageLollipopInterface(IEntity entity, ISkinParam skinParam) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();
		this.desc = TextBlockUtils.create(entity.getDisplay2(), new FontConfiguration(getFont(FontParam.CLASS,
				stereotype), getFontColor(FontParam.CLASS, stereotype)), HorizontalAlignement.CENTER);

	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(SIZE, SIZE);
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		final UEllipse circle = new UEllipse(SIZE, SIZE);
		if (getSkinParam().shadowing()) {
			circle.setDeltaShadow(4);
		}
		ug.getParam().setStroke(new UStroke(1.5));
		ug.getParam().setColor(getColor(ColorParam.classBorder, getStereo()));
		ug.getParam().setBackcolor(getColor(ColorParam.classBackground, getStereo()));
		ug.draw(xTheoricalPosition, yTheoricalPosition, circle);
		ug.getParam().setStroke(new UStroke());

		final Dimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
		final double widthDesc = dimDesc.getWidth();
		// final double totalWidth = Math.max(widthDesc, SIZE);

		final double x = xTheoricalPosition + SIZE / 2 - widthDesc / 2;
		final double y = yTheoricalPosition + SIZE;
		desc.drawU(ug, x, y);
	}

	public ShapeType getShapeType() {
		return ShapeType.CIRCLE_IN_RECT;
	}

	public int getShield() {
		return 0;
	}

}
