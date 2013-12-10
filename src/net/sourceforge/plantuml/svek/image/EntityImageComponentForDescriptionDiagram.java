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
 * Modified by : Arno Peterson
 * 
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.BodyEnhanced;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageComponentForDescriptionDiagram extends AbstractEntityImage {

	final private Url url;

	private final TextBlock asSmall;

	public EntityImageComponentForDescriptionDiagram(ILeaf entity, ISkinParam skinParam) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();
		final USymbol symbol = entity.getUSymbol();
		if (symbol == null) {
			throw new IllegalArgumentException();
		}

		final TextBlock desc = new BodyEnhanced(entity.getDisplay(), getFontParam(symbol), skinParam,
				HorizontalAlignment.CENTER, stereotype, symbol.manageHorizontalLine(), false);

		// final TextBlock desc = TextBlockUtils.create(
		// entity.getDisplay(),
		// new FontConfiguration(getFont(getFontParam(symbol), stereotype), getFontColor(getFontParam(symbol),
		// stereotype)), HorizontalAlignment.CENTER, skinParam);

		this.url = entity.getUrl99();

		HtmlColor backcolor = getEntity().getSpecificBackColor();
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), getColorParamBack(symbol), getStereo());
		}
		final HtmlColor forecolor = SkinParamUtils.getColor(getSkinParam(), getColorParamBorder(symbol), getStereo());
		final SymbolContext ctx = new SymbolContext(backcolor, forecolor).withStroke(new UStroke(1.5)).withShadow(
				getSkinParam().shadowing());

		TextBlock stereo = TextBlockUtils.empty(0, 0);
		if (stereotype != null && stereotype.getLabel() != null) {
			stereo = TextBlockUtils.create(
					Display.getWithNewlines(stereotype.getLabel()),
					new FontConfiguration(SkinParamUtils.getFont(getSkinParam(), getFontParamStereotype(symbol),
							stereotype), SkinParamUtils.getFontColor(getSkinParam(), getFontParamStereotype(symbol),
							null)), HorizontalAlignment.CENTER, skinParam);
		}

		asSmall = symbol.asSmall(desc, stereo, ctx);
	}

	private FontParam getFontParamStereotype(USymbol symbol) {
		if (symbol == USymbol.COMPONENT1 || symbol == USymbol.COMPONENT2 || symbol == USymbol.INTERFACE) {
			return FontParam.COMPONENT_STEREOTYPE;
		}
		if (symbol == USymbol.ACTOR) {
			return FontParam.USECASE_ACTOR_STEREOTYPE;
		}
		if (symbol == USymbol.ARTIFACT || symbol == USymbol.FOLDER || symbol == USymbol.BOUNDARY
				|| symbol == USymbol.ENTITY_DOMAIN || symbol == USymbol.CONTROL) {
			return FontParam.USECASE_ACTOR_STEREOTYPE;
		}
		return FontParam.USECASE_ACTOR_STEREOTYPE;
		// throw new UnsupportedOperationException("symbol=" + symbol.getClass());
	}

	private FontParam getFontParam(USymbol symbol) {
		if (symbol == USymbol.COMPONENT1 || symbol == USymbol.COMPONENT2 || symbol == USymbol.INTERFACE) {
			return FontParam.COMPONENT;
		}
		if (symbol == USymbol.ACTOR) {
			return FontParam.USECASE_ACTOR;
		}
		if (symbol == USymbol.ARTIFACT || symbol == USymbol.FOLDER || symbol == USymbol.BOUNDARY
				|| symbol == USymbol.ENTITY_DOMAIN || symbol == USymbol.CONTROL) {
			return FontParam.USECASE_ACTOR;
		}
		return FontParam.USECASE_ACTOR;
		// throw new UnsupportedOperationException("symbol=" + symbol.getClass());
	}

	private ColorParam getColorParamBorder(USymbol symbol) {
		if (symbol == USymbol.COMPONENT1 || symbol == USymbol.COMPONENT2 || symbol == USymbol.INTERFACE) {
			return ColorParam.componentBorder;
		}
		if (symbol == USymbol.ACTOR) {
			return ColorParam.usecaseActorBorder;
		}
		if (symbol == USymbol.ARTIFACT || symbol == USymbol.FOLDER || symbol == USymbol.BOUNDARY
				|| symbol == USymbol.ENTITY_DOMAIN || symbol == USymbol.CONTROL) {
			return ColorParam.usecaseActorBorder;
		}
		return ColorParam.usecaseActorBorder;
		// throw new UnsupportedOperationException("symbol=" + symbol.getClass());
	}

	private ColorParam getColorParamBack(USymbol symbol) {
		if (symbol == USymbol.COMPONENT1 || symbol == USymbol.COMPONENT2 || symbol == USymbol.INTERFACE) {
			return ColorParam.componentBackground;
		}
		if (symbol == USymbol.ACTOR) {
			return ColorParam.usecaseActorBackground;
		}
		if (symbol == USymbol.ARTIFACT || symbol == USymbol.FOLDER || symbol == USymbol.BOUNDARY
				|| symbol == USymbol.ENTITY_DOMAIN || symbol == USymbol.CONTROL) {
			return ColorParam.usecaseActorBackground;
		}
		return ColorParam.usecaseActorBackground;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return asSmall.calculateDimension(stringBounder);
	}

	final public void drawU(UGraphic ug) {
		if (url != null) {
			ug.startUrl(url);
		}
		asSmall.drawU(ug);

		if (url != null) {
			ug.closeAction();
		}
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
