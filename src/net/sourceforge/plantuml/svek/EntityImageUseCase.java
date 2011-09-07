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
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageUseCase extends AbstractEntityImage {

	final private TextBlock desc;
	final private static int MARGIN = 10;
	final private TextBlock stereo;

	public EntityImageUseCase(IEntity entity, ISkinParam skinParam) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();

		this.desc = TextBlockUtils.create(entity.getDisplay2(), new FontConfiguration(getFont(FontParam.USECASE,
				stereotype), getFontColor(FontParam.USECASE, stereotype)), HorizontalAlignement.CENTER);

		if (stereotype == null || stereotype.getLabel() == null) {
			this.stereo = null;
		} else {
			this.stereo = TextBlockUtils.create(StringUtils.getWithNewlines(stereotype.getLabel()),
					new FontConfiguration(getFont(FontParam.USECASE_ACTOR_STEREOTYPE, stereotype), getFontColor(
							FontParam.USECASE_ACTOR_STEREOTYPE, null)), HorizontalAlignement.CENTER);
		}

	}

	private Dimension2D getStereoDimension(StringBounder stringBounder) {
		if (stereo == null) {
			return new Dimension2DDouble(0, 0);
		}
		return stereo.calculateDimension(stringBounder);
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dim = Dimension2DDouble.mergeTB(desc.calculateDimension(stringBounder),
				getStereoDimension(stringBounder));
		return Dimension2DDouble.delta(dim, MARGIN * 2);
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = getDimension(stringBounder);
		final Dimension2D dimStereo = getStereoDimension(stringBounder);
		final Dimension2D dimDesc = desc.calculateDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final UEllipse ellipse = new UEllipse(widthTotal, heightTotal);
		ellipse.setDeltaShadow(3);

		ug.getParam().setStroke(new UStroke(1.5));
		ug.getParam().setColor(getColor(ColorParam.usecaseBorder, getStereo()));
		ug.getParam().setBackcolor(getColor(ColorParam.usecaseBackground, getStereo()));
		ug.draw(xTheoricalPosition, yTheoricalPosition, ellipse);
		ug.getParam().setStroke(new UStroke());

		final double x = (dimTotal.getWidth() - dimDesc.getWidth()) / 2;
		final double y = MARGIN + dimStereo.getHeight();
		desc.drawU(ug, xTheoricalPosition + x, yTheoricalPosition + y);

		if (stereo != null) {
			final double stereoX = (dimTotal.getWidth() - dimStereo.getWidth()) / 2;
			stereo.drawU(ug, xTheoricalPosition + stereoX, yTheoricalPosition + MARGIN);
		}

	}

	public ShapeType getShapeType() {
		return ShapeType.OVAL;
	}
	
	public int getShield() {
		return 0;
	}

}
