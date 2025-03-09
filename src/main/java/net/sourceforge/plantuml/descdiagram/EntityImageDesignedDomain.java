/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
 *
 * Original Author:  Yijun Yu
 * 
 *
 */
package net.sourceforge.plantuml.descdiagram;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.PlacementStrategyY1Y2;
import net.sourceforge.plantuml.klimt.geom.ULayoutGroup;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.LineParam;
import net.sourceforge.plantuml.skin.SkinParamUtils;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.text.Guillemet;
import net.sourceforge.plantuml.url.Url;

public class EntityImageDesignedDomain extends AbstractEntityImage {
	final private TextBlock name;
	final private TextBlock stereo;
	final private Url url;

	public EntityImageDesignedDomain(Entity entity) {
		super(entity);
		final Stereotype stereotype = entity.getStereotype();
		this.name = TextBlockUtils.withMargin(entity.getDisplay().create(
				FontConfiguration.create(getSkinParam(), FontParam.DESIGNED_DOMAIN, stereotype),
				HorizontalAlignment.CENTER, getSkinParam()), 2, 2);
		if (stereotype == null || stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR) == null) {
			this.stereo = null;
		} else {
			this.stereo = Display.create(stereotype.getLabels(getSkinParam().guillemet())).create(
					FontConfiguration.create(getSkinParam(), FontParam.DESIGNED_DOMAIN_STEREOTYPE, stereotype),
					HorizontalAlignment.CENTER, getSkinParam());
		}
		this.url = entity.getUrl99();
		// System.out.println(entity.getStereotype());
	}

	private UStroke getStroke() {
		UStroke stroke = getSkinParam().getThickness(LineParam.designedDomainBorder, getStereo());

		if (stroke == null) {
			stroke = UStroke.withThickness(1.5);
		}
		return stroke;
	}

	private XDimension2D getTitleDimension(StringBounder stringBounder) {
		return getNameAndSteretypeDimension(stringBounder);
	}

	private XDimension2D getNameAndSteretypeDimension(StringBounder stringBounder) {
		final XDimension2D nameDim = name.calculateDimension(stringBounder);
		final XDimension2D stereoDim = stereo == null ? new XDimension2D(0, 0)
				: stereo.calculateDimension(stringBounder);
		final XDimension2D nameAndStereo = new XDimension2D(Math.max(nameDim.getWidth(), stereoDim.getWidth()),
				nameDim.getHeight() + stereoDim.getHeight());
		return nameAndStereo;
	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return 0;
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return calculateDimension(stringBounder).getWidth();
	}

	final public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimTotal = calculateDimension(stringBounder);
		final XDimension2D dimTitle = getTitleDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final URectangle rect = URectangle.build(widthTotal, heightTotal);
		URectangle rect2 = URectangle.build(4, heightTotal);

		ug = ug.apply(SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.designedBorder));
		HColor backcolor = getEntity().getColors().getColor(ColorType.BACK);
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.designedBackground);
		}
		ug = ug.apply(backcolor.bg());
		if (url != null) {
			ug.startUrl(url);
		}

		final UStroke stroke = getStroke();
		ug.apply(stroke).draw(rect);
		ug.apply(stroke).draw(rect2);

		final ULayoutGroup header = new ULayoutGroup(new PlacementStrategyY1Y2(ug.getStringBounder()));
		header.add(name);
		header.drawU(ug.apply(UTranslate.dx(4)), dimTotal.getWidth(), dimTitle.getHeight());

		if (url != null) {
			ug.closeUrl();
		}
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D dimTitle = getTitleDimension(stringBounder);
		final double width = dimTitle.getWidth();
		final double height = dimTitle.getHeight();
		return new XDimension2D(width + 5, height);
	}

}
