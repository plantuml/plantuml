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
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class EntityImageNote extends AbstractEntityImage {

	private final Component comp;

	public EntityImageNote(IEntity entity, ISkinParam skinParam) {
		super(entity, getSkin(skinParam, entity));

		final Rose skin = new Rose();

		comp = skin.createComponent(ComponentType.NOTE, getSkinParam(), entity.getDisplay2());

	}

	private static ISkinParam getSkin(ISkinParam skinParam, IEntity entity) {
		final Stereotype stereotype = entity.getStereotype();
		HtmlColor back = entity.getSpecificBackColor();
		if (back != null) {
			return new SkinParamBackcolored(skinParam, back);
		}
		back = getColorStatic(skinParam, ColorParam.noteBackground, stereotype);
		if (back != null) {
			return new SkinParamBackcolored(skinParam, back);
		}
		return skinParam;
	}

	private static HtmlColor getColorStatic(ISkinParam skinParam, ColorParam colorParam, Stereotype stereo) {
		final String s = stereo == null ? null : stereo.getLabel();
		final Rose rose = new Rose();
		return rose.getHtmlColor(skinParam, colorParam, s);
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final double height = comp.getPreferredHeight(stringBounder);
		final double width = comp.getPreferredWidth(stringBounder);
		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		final double dx = ug.getTranslateX();
		final double dy = ug.getTranslateY();
		ug.translate(xTheoricalPosition, yTheoricalPosition);
		comp.drawU(ug, getDimension(ug.getStringBounder()), new SimpleContext2D(false, true));
		ug.setTranslate(dx, dy);

	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}
	
	public int getShield() {
		return 0;
	}


}
