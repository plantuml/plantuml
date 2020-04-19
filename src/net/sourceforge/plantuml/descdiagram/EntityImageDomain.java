/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyY1Y2;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULayoutGroup;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class EntityImageDomain extends AbstractEntityImage {
	final private TextBlock name;
	final private TextBlock tag;
	final private TextBlock stereo;
	final private Url url;

	public EntityImageDomain(ILeaf entity, ISkinParam skinParam, char typeLetter) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();
		FontConfiguration fc = new FontConfiguration(getSkinParam(), FontParam.DESIGNED_DOMAIN, stereotype);
		this.name = TextBlockUtils.withMargin(entity.getDisplay().create(fc, HorizontalAlignment.CENTER, skinParam), 2,
				2);
		if (stereotype == null || stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR) == null) {
			this.stereo = null;
		} else {
			this.stereo = Display.create(stereotype.getLabels(skinParam.guillemet())).create(
					new FontConfiguration(getSkinParam(), FontParam.DESIGNED_DOMAIN_STEREOTYPE, stereotype),
					HorizontalAlignment.CENTER, skinParam);
		}
		this.tag = new BoxedCharacter(typeLetter, 8, UFont.byDefault(8), stereotype.getHtmlColor(), null, fc.getColor());

		this.url = entity.getUrl99();
	}

	private UStroke getStroke() {
		UStroke stroke = getSkinParam().getThickness(LineParam.domainBorder, getStereo());

		if (stroke == null) {
			stroke = new UStroke(1.5);
		}
		return stroke;
	}

	private Dimension2D getTitleDimension(StringBounder stringBounder) {
		return getNameAndSteretypeDimension(stringBounder);
	}

	private Dimension2D getNameAndSteretypeDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final Dimension2D stereoDim = stereo == null ? new Dimension2DDouble(0, 0) : stereo
				.calculateDimension(stringBounder);
		final Dimension2D nameAndStereo = new Dimension2DDouble(Math.max(nameDim.getWidth(), stereoDim.getWidth()),
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
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Dimension2D dimTitle = getTitleDimension(stringBounder);
		final Dimension2D dimTag = getTagDimension(stringBounder);
		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final URectangle rect = new URectangle(widthTotal, heightTotal);

		ug = ug.apply(SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.domainBorder));
		HColor backcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.BACK);
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.domainBackground);
		}
		ug = ug.apply(backcolor.bg());
		if (url != null) {
			ug.startUrl(url);
		}

		final UStroke stroke = getStroke();
		ug.apply(stroke).draw(rect);

		final ULayoutGroup header = new ULayoutGroup(new PlacementStrategyY1Y2(ug.getStringBounder()));
		header.add(name);
		header.drawU(ug, dimTotal.getWidth(), dimTitle.getHeight());
		final ULayoutGroup footer = new ULayoutGroup(new PlacementStrategyY1Y2(ug.getStringBounder()));
		footer.add(tag);
		footer.drawU(ug.apply(new UTranslate(dimTotal.getWidth() - dimTag.getWidth(), dimTitle.getHeight())),
				dimTag.getWidth(), dimTag.getHeight());
		if (url != null) {
			ug.closeAction();
		}
	}

	private Dimension2D getTagDimension(StringBounder stringBounder) {
		final Dimension2D tagDim = tag == null ? new Dimension2DDouble(0, 0) : tag.calculateDimension(stringBounder);
		return tagDim;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dimTitle = getTitleDimension(stringBounder);
		final double width = dimTitle.getWidth();
		final double height = dimTitle.getHeight();
		final Dimension2D dimTag = getTagDimension(stringBounder);
		return new Dimension2DDouble(width, height + dimTag.getHeight());
	}

}
