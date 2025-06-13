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
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.svek.image;

import java.util.List;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityPortion;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.CircledCharacter;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockGeneric;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.skin.SkinParamUtils;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.HeaderLayout;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.text.Guillemet;

public class EntityImageClassHeader extends AbstractEntityImage {

	final private HeaderLayout headerLayout;

	public EntityImageClassHeader(Entity entity, PortionShower portionShower) {
		super(entity);

		final boolean italic = entity.getLeafType() == LeafType.ABSTRACT_CLASS
				|| entity.getLeafType() == LeafType.INTERFACE;

		final Stereotype stereotype = entity.getStereotype();
		final boolean displayGenericWithOldFashion = getSkinParam().displayGenericWithOldFashion();
		final String generic = displayGenericWithOldFashion ? null : entity.getGeneric();

		final Style styleHeader = StyleSignatureBasic
				.of(SName.root, SName.element, SName.classDiagram, SName.class_, SName.header) //
				.withTOBECHANGED(stereotype) //
				.with(entity.getStereostyles()) //
				.getMergedStyle(getSkinParam().getCurrentStyleBuilder());

		FontConfiguration fontConfigurationName = FontConfiguration.create(getSkinParam(), styleHeader, entity.getColors());
		if (italic)
			fontConfigurationName = fontConfigurationName.italic();

		Display display = entity.getDisplay();
		if (displayGenericWithOldFashion && entity.getGeneric() != null)
			display = display.addGeneric(entity.getGeneric());

		TextBlock name = display.create8(fontConfigurationName, HorizontalAlignment.CENTER, getSkinParam(),
				CreoleMode.FULL_BUT_UNDERSCORE, styleHeader.wrapWidth());
		final VisibilityModifier modifier = entity.getVisibilityModifier();
		if (modifier == null) {
			name = TextBlockUtils.withMargin(name, 3, 3, 0, 0);
		} else {
			final Rose rose = new Rose();
			final HColor back = rose.getHtmlColor(getSkinParam(), modifier.getBackground());
			final HColor fore = rose.getHtmlColor(getSkinParam(), modifier.getForeground());

			final TextBlock uBlock = modifier.getUBlock(getSkinParam().classAttributeIconSize(), fore, back, false);
			name = TextBlockUtils.mergeLR(uBlock, name, VerticalAlignment.CENTER);
			name = TextBlockUtils.withMargin(name, 3, 3, 0, 0);
		}

		final TextBlock stereo;
		final List<String> stereotypeLabels = portionShower.getVisibleStereotypeLabels(entity);
		if (stereotype == null || stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR) == null
				|| stereotypeLabels.isEmpty())
			stereo = null;
		else
			stereo = TextBlockUtils.withMargin(Display.create(stereotypeLabels).create(
					FontConfiguration.create(getSkinParam(), FontParam.CLASS_STEREOTYPE, stereotype),
					HorizontalAlignment.CENTER, getSkinParam()), 1, 0);

		TextBlock genericBlock;
		if (generic == null) {
			genericBlock = null;
		} else {
			final Style styleGeneric = StyleSignatureBasic
					.of(SName.root, SName.element, SName.classDiagram, SName.class_, SName.generic) //
					.withTOBECHANGED(stereotype) //
					.with(entity.getStereostyles()) //
					.getMergedStyle(getSkinParam().getCurrentStyleBuilder());

			genericBlock = Display.getWithNewlines(getSkinParam().getPragma(), generic).create(
					FontConfiguration.create(getSkinParam(), FontParam.CLASS_STEREOTYPE, stereotype),
					HorizontalAlignment.CENTER, getSkinParam());
			genericBlock = TextBlockUtils.withMargin(genericBlock, 1, 1);

			final HColor classBackground = styleGeneric.value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());
			final HColor classBorder = styleGeneric.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());

			genericBlock = new TextBlockGeneric(genericBlock, classBackground, classBorder);
			genericBlock = TextBlockUtils.withMargin(genericBlock, 1, 1);
		}

		final TextBlock circledCharacter;
		if (portionShower.showPortion(EntityPortion.CIRCLED_CHARACTER, (Entity) getEntity()))
			circledCharacter = TextBlockUtils.withMargin(getCircledCharacter(entity, getSkinParam()), 4, 0, 5, 5);
		else
			circledCharacter = null;

		this.headerLayout = new HeaderLayout(circledCharacter, stereo, name, genericBlock);
	}

	private TextBlock getCircledCharacter(Entity entity, ISkinParam skinParam) {
		final Stereotype stereotype = entity.getStereotype();
		if (stereotype != null && stereotype.getSprite(skinParam) != null)
			return stereotype.getSprite(skinParam);

		final UFont font = SkinParamUtils.getFont(getSkinParam(), FontParam.CIRCLED_CHARACTER, null);

		final LeafType leafType = entity.getLeafType();

		final Style style = spotStyleSignature(leafType).getMergedStyle(skinParam.getCurrentStyleBuilder());
		final HColor spotBorder = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final HColor spotBackColor = style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());

		final HColor fontColor = style.value(PName.FontColor).asColor(skinParam.getIHtmlColorSet());

		if (stereotype != null && stereotype.getCharacter() != 0)
			return new CircledCharacter(stereotype.getCharacter(), getSkinParam().getCircledCharacterRadius(), font,
					stereotype.getHtmlColor() == null ? spotBackColor : stereotype.getHtmlColor(), spotBorder,
					fontColor);

		char circledChar = 0;
		if (stereotype != null)
			circledChar = getSkinParam().getCircledCharacter(stereotype);

		if (circledChar == 0)
			circledChar = getCircledChar(leafType);

		return new CircledCharacter(circledChar, getSkinParam().getCircledCharacterRadius(), font, spotBackColor,
				spotBorder, fontColor);
	}

	private StyleSignatureBasic spotStyleSignature(LeafType leafType) {
		switch (leafType) {
		case ANNOTATION:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotAnnotation);
		case ABSTRACT_CLASS:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotAbstractClass);
		case CLASS:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotClass);
		case INTERFACE:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotInterface);
		case ENUM:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotEnum);
		case ENTITY:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotEntity);
		case PROTOCOL:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotProtocol);
		case STRUCT:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotStruct);
		case EXCEPTION:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotException);
		case METACLASS:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotMetaClass);
		case STEREOTYPE:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotStereotype);
		case DATACLASS:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotDataClass);
		case RECORD:
			return StyleSignatureBasic.of(SName.root, SName.element, SName.spot, SName.spotRecord);
		}
		throw new IllegalStateException();
	}

	private char getCircledChar(LeafType leafType) {
		switch (leafType) {
		case ANNOTATION:
			return '@';
		case ABSTRACT_CLASS:
			return 'A';
		case CLASS:
			return 'C';
		case INTERFACE:
			return 'I';
		case ENUM:
			return 'E';
		case ENTITY:
			return 'E';
		case PROTOCOL:
			return 'P';
		case STRUCT:
			return 'S';
		case EXCEPTION:
			return 'X';
		case METACLASS:
			return 'M';
		case STEREOTYPE:
			return 'S';
		case DATACLASS:
			return 'D';
		case RECORD:
			return 'R';
		}
		assert false;
		return '?';
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return headerLayout.getDimension(stringBounder);
	}

	final public void drawU(UGraphic ug) {
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug, double width, double height) {
		headerLayout.drawU(ug, width, height);
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

}
