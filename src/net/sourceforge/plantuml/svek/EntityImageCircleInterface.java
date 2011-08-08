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

public class EntityImageCircleInterface extends AbstractEntityImage {

	private static final int SIZE = 16;

	private final TextBlock name;
	private final TextBlock stereo;

	public EntityImageCircleInterface(IEntity entity, ISkinParam skinParam) {
		super(entity, skinParam);

		final Stereotype stereotype = entity.getStereotype();

		this.name = TextBlockUtils.create(entity.getDisplay2(), new FontConfiguration(
				getFont(FontParam.COMPONENT, stereotype), getFontColor(FontParam.COMPONENT, stereotype)),
				HorizontalAlignement.CENTER);

		if (stereotype == null || stereotype.getLabel() == null) {
			this.stereo = null;
		} else {
			this.stereo = TextBlockUtils.create(StringUtils.getWithNewlines(stereotype.getLabel()),
					new FontConfiguration(getFont(FontParam.COMPONENT_STEREOTYPE, stereotype), getFontColor(
							FontParam.COMPONENT_STEREOTYPE, null)), HorizontalAlignement.CENTER);
		}

	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dimName = name.calculateDimension(stringBounder);
		final Dimension2D dimStereo = getStereoDimension(stringBounder);
		final Dimension2D circle = new Dimension2DDouble(SIZE, SIZE);
		return Dimension2DDouble.mergeTB(dimStereo, circle, dimName);
	}

	private Dimension2D getStereoDimension(StringBounder stringBounder) {
		if (stereo == null) {
			return new Dimension2DDouble(0, 0);
		}
		return stereo.calculateDimension(stringBounder);
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimStereo = getStereoDimension(stringBounder);
		final Dimension2D dimTotal = getDimension(stringBounder);
		final Dimension2D dimName = name.calculateDimension(stringBounder);

		final double circleX = (dimTotal.getWidth() - SIZE) / 2;
		final double circleY = dimStereo.getHeight();

		final UShape circle = new UEllipse(SIZE, SIZE);
		ug.getParam().setStroke(new UStroke(2));
		ug.getParam().setColor(getColor(ColorParam.componentInterfaceBorder, getStereo()));
		ug.getParam().setBackcolor(getColor(ColorParam.componentInterfaceBackground, getStereo()));
		ug.draw(xTheoricalPosition + circleX, yTheoricalPosition + circleY, circle);
		ug.getParam().setStroke(new UStroke());

		final double nameX = (dimTotal.getWidth() - dimName.getWidth()) / 2;
		final double nameY = SIZE + dimStereo.getHeight();
		name.drawU(ug, xTheoricalPosition + nameX, yTheoricalPosition + nameY);

		if (stereo != null) {
			final double stereoX = (dimTotal.getWidth() - dimStereo.getWidth()) / 2;
			stereo.drawU(ug, xTheoricalPosition + stereoX, yTheoricalPosition);
		}

	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

}
