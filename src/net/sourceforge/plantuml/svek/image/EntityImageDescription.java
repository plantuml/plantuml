/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 * Modified by : Arno Peterson
 * 
 *
 */
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.BodyEnhanced;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.USymbolFolder;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Margins;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UComment;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.utils.MathUtils;

public class EntityImageDescription extends AbstractEntityImage {

	private final ShapeType shapeType;

	final private Url url;

	private final TextBlock asSmall;

	private final TextBlock name;
	private final TextBlock desc;

	private TextBlock stereo;

	private final boolean hideText;

	public EntityImageDescription(ILeaf entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, entity.getColors(skinParam).mute(skinParam));
		final Stereotype stereotype = entity.getStereotype();
		USymbol symbol = getUSymbol(entity);
		this.shapeType = symbol == USymbol.FOLDER ? ShapeType.FOLDER : ShapeType.RECTANGLE;
		this.hideText = symbol == USymbol.INTERFACE;

		final Display codeDisplay = Display.getWithNewlines(entity.getCode());
		desc = (entity.getDisplay().equals(codeDisplay) && symbol instanceof USymbolFolder)
				|| entity.getDisplay().isWhite() ? TextBlockUtils.empty(0, 0) : new BodyEnhanced(entity.getDisplay(),
				symbol.getFontParam(), getSkinParam(), HorizontalAlignment.LEFT, stereotype,
				symbol.manageHorizontalLine(), false, entity);

		this.url = entity.getUrl99();

		HtmlColor backcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.BACK);
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), symbol.getColorParamBack(), getStereo());
		}

		assert getStereo() == stereotype;
		final HtmlColor forecolor = SkinParamUtils.getColor(getSkinParam(), symbol.getColorParamBorder(), stereotype);
		final double roundCorner = symbol.getSkinParameter().getRoundCorner(getSkinParam(), stereotype);
		final UStroke stroke = symbol.getSkinParameter().getStroke(getSkinParam(), stereotype);
		final SymbolContext ctx = new SymbolContext(backcolor, forecolor).withStroke(stroke)
				.withShadow(getSkinParam().shadowing2(symbol.getSkinParameter())).withRoundCorner(roundCorner);

		stereo = TextBlockUtils.empty(0, 0);

		if (stereotype != null && stereotype.getSprite() != null
				&& getSkinParam().getSprite(stereotype.getSprite()) != null) {
			symbol = symbol.withStereoAlignment(HorizontalAlignment.RIGHT);
			stereo = getSkinParam().getSprite(stereotype.getSprite()).asTextBlock(stereotype.getHtmlColor(), 1);
		} else if (stereotype != null && stereotype.getLabel(false) != null
				&& portionShower.showPortion(EntityPortion.STEREOTYPE, entity)) {
			stereo = Display.getWithNewlines(stereotype.getLabel(getSkinParam().useGuillemet())).create(
					new FontConfiguration(getSkinParam(), symbol.getFontParamStereotype(), stereotype),
					HorizontalAlignment.CENTER, getSkinParam());
		}

		name = new BodyEnhanced(codeDisplay, symbol.getFontParam(), getSkinParam(), HorizontalAlignment.CENTER,
				stereotype, symbol.manageHorizontalLine(), false, entity);

		if (hideText) {
			asSmall = symbol.asSmall(TextBlockUtils.empty(0, 0), TextBlockUtils.empty(0, 0),
					TextBlockUtils.empty(0, 0), ctx);
		} else {
			asSmall = symbol.asSmall(name, desc, stereo, ctx);
		}
	}

	private USymbol getUSymbol(ILeaf entity) {
		final USymbol result = entity.getUSymbol() == null ? (getSkinParam().useUml2ForComponent() ? USymbol.COMPONENT2
				: USymbol.COMPONENT1) : entity.getUSymbol();
		if (result == null) {
			throw new IllegalArgumentException();
		}
		return result;
	}

	public Dimension2D getNameDimension(StringBounder stringBounder) {
		if (hideText) {
			return new Dimension2DDouble(0, 0);
		}
		return name.calculateDimension(stringBounder);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return asSmall.calculateDimension(stringBounder);
	}

	@Override
	public Margins getShield(StringBounder stringBounder) {
		if (hideText) {
			final Dimension2D dimStereo = stereo.calculateDimension(stringBounder);
			final Dimension2D dimDesc = desc.calculateDimension(stringBounder);
			final Dimension2D dimSmall = asSmall.calculateDimension(stringBounder);
			final double x = Math.max(dimStereo.getWidth(), dimDesc.getWidth());
			double suppX = x - dimSmall.getWidth();
			if (suppX < 1) {
				suppX = 1;
			}
			final double y = MathUtils.max(1, dimDesc.getHeight(), dimStereo.getHeight());
			return new Margins(suppX / 2, suppX / 2, y, y);

		}
		return Margins.NONE;
	}

	final public void drawU(UGraphic ug) {
		ug.draw(new UComment("entity " + getEntity().getCode().getFullName()));
		if (url != null) {
			ug.startUrl(url);
		}
		asSmall.drawU(ug);
		if (hideText) {
			final double space = 8;
			final Dimension2D dimSmall = asSmall.calculateDimension(ug.getStringBounder());
			final Dimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
			desc.drawU(ug.apply(new UTranslate((dimSmall.getWidth() - dimDesc.getWidth()) / 2, space
					+ dimSmall.getHeight())));
			final Dimension2D dimStereo = stereo.calculateDimension(ug.getStringBounder());
			stereo.drawU(ug.apply(new UTranslate((dimSmall.getWidth() - dimStereo.getWidth()) / 2, -space
					- dimStereo.getHeight())));
		}

		if (url != null) {
			ug.closeAction();
		}
	}

	public ShapeType getShapeType() {
		return shapeType;
	}

}
