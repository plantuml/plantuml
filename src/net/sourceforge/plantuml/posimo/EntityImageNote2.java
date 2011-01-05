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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.util.Collection;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class EntityImageNote2 extends AbstractEntityImage2 {

	private final Component comp;

	public EntityImageNote2(IEntity entity, ISkinParam skinParam,
			Collection<Link> links) {
		super(entity, skinParam);

		final Rose skin = new Rose();

		comp = skin.createComponent(ComponentType.NOTE, skinParam, StringUtils
				.getWithNewlines(entity.getDisplay()));

	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final double height =  comp.getPreferredHeight(stringBounder);
		final double width =  comp.getPreferredWidth(stringBounder);
		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug, double xTheoricalPosition,
			double yTheoricalPosition, double marginWidth, double marginHeight) {
		final double dx = ug.getTranslateX();
		final double dy = ug.getTranslateY();
		ug.translate(xTheoricalPosition, yTheoricalPosition);
		comp.drawU(ug, getDimension(ug.getStringBounder()), new SimpleContext2D(false));
		ug.setTranslate(dx, dy);

	}

}
