/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class EntityImageArcCircle extends AbstractEntityImage {

	// private static final int SIZE = 16;

	private final TextBlock name;
	private final TextBlock stereo;

	public EntityImageArcCircle(ILeaf entity, ISkinParam skinParam) {
		super(entity, skinParam);

		final Stereotype stereotype = entity.getStereotype();

		this.name = entity.getDisplay().create(new FontConfiguration(SkinParamUtils.getFont(getSkinParam(),
				FontParam.COMPONENT, stereotype), SkinParamUtils.getFontColor(getSkinParam(),
		FontParam.COMPONENT, stereotype), getSkinParam().getHyperlinkColor(), getSkinParam().useUnderlineForHyperlink()), HorizontalAlignment.CENTER, skinParam);

		if (stereotype == null || stereotype.getLabel(false) == null) {
			this.stereo = null;
		} else {
			this.stereo = Display.getWithNewlines(stereotype.getLabel(getSkinParam().useGuillemet())).create(new FontConfiguration(SkinParamUtils.getFont(getSkinParam(),
					FontParam.COMPONENT_STEREOTYPE, stereotype), SkinParamUtils.getFontColor(getSkinParam(),
			FontParam.COMPONENT_STEREOTYPE, null), getSkinParam().getHyperlinkColor(), getSkinParam().useUnderlineForHyperlink()), HorizontalAlignment.CENTER, skinParam);
		}

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dimName = name.calculateDimension(stringBounder);
		final Dimension2D dimStereo = getStereoDimension(stringBounder);
		// final Dimension2D circle = new Dimension2DDouble(SIZE, SIZE);
		return Dimension2DDouble.mergeTB(dimStereo, dimName);
	}

	private Dimension2D getStereoDimension(StringBounder stringBounder) {
		if (stereo == null) {
			return new Dimension2DDouble(0, 0);
		}
		return stereo.calculateDimension(stringBounder);
	}

	final public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimStereo = getStereoDimension(stringBounder);
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Dimension2D dimName = name.calculateDimension(stringBounder);

		final double nameX = (dimTotal.getWidth() - dimName.getWidth()) / 2;
		final double nameY = dimStereo.getHeight();
		name.drawU(ug.apply(new UTranslate(nameX, nameY)));

		if (stereo != null) {
			final double stereoX = (dimTotal.getWidth() - dimStereo.getWidth()) / 2;
			stereo.drawU(ug.apply(new UTranslate(stereoX, 0)));
		}
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
