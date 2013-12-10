/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.ClusterDecoration;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class EntityImageEmptyPackage2 extends AbstractEntityImage {

	final private TextBlock desc;
	final private static int MARGIN = 10;
	final private HtmlColor specificBackColor;
	final private ISkinParam skinParam;
	final private Stereotype stereotype;

	public EntityImageEmptyPackage2(ILeaf entity, ISkinParam skinParam) {
		super(entity, skinParam);
		this.skinParam = skinParam;
		this.specificBackColor = entity.getSpecificBackColor();
		this.stereotype = entity.getStereotype();
		this.desc = TextBlockUtils.create(entity.getDisplay(),
				new FontConfiguration(SkinParamUtils.getFont(getSkinParam(), FontParam.PACKAGE, stereotype),
						SkinParamUtils.getFontColor(getSkinParam(), FontParam.PACKAGE, stereotype)),
				HorizontalAlignment.CENTER, skinParam);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = desc.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, MARGIN * 2, MARGIN * 2 + dim.getHeight() * 2);
	}

	final public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();

		final HtmlColor stateBack = Cluster.getStateBackColor(specificBackColor, skinParam, stereotype == null ? null
				: stereotype.getLabel());

		final ClusterDecoration decoration = new ClusterDecoration(getSkinParam().getPackageStyle(), null, desc,
				TextBlockUtils.empty(0, 0), stateBack, 0, 0, widthTotal, heightTotal);

		decoration.drawU(ug, SkinParamUtils.getColor(getSkinParam(), ColorParam.packageBorder, getStereo()),
				getSkinParam().shadowing());
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
