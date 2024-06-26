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
 * Modified by : Arno Peterson
 *
 *
 */
package net.sourceforge.plantuml.svek.image;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityPortion;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.cucadiagram.BodyFactory;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.decoration.symbol.USymbolActorBusiness;
import net.sourceforge.plantuml.decoration.symbol.USymbols;
import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphicStencil;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MagneticBorder;
import net.sourceforge.plantuml.klimt.geom.MagneticBorderNone;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UComment;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Margins;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.svek.SvekNode;
import net.sourceforge.plantuml.text.Guillemet;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.utils.MathUtils;

public class EntityImageDescription extends AbstractEntityImage {

	private final ShapeType shapeType;

	final private Url url;

	private final TextBlock asSmall;

	private final TextBlock name;
	private final TextBlock desc;

	private TextBlock stereo;

	private final boolean hideText;
	private final Collection<Link> links;
	private final boolean useRankSame;
	private final boolean fixCircleLabelOverlapping;
	private final Bibliotekon bibliotekon;
	private final Fashion ctx;

	public EntityImageDescription(Entity entity, ISkinParam skinParam2, PortionShower portionShower,
			Collection<Link> links, SName styleName, Bibliotekon bibliotekon) {
		super(entity, entity.getColors().mute(skinParam2));
		this.useRankSame = getSkinParam().useRankSame();
		this.bibliotekon = bibliotekon;
		this.fixCircleLabelOverlapping = getSkinParam().fixCircleLabelOverlapping();

		this.links = links;
		USymbol symbol = getUSymbol(entity);
		if (symbol == USymbols.FOLDER || symbol == USymbols.PACKAGE)
			this.shapeType = ShapeType.FOLDER;
		else if (symbol == USymbols.HEXAGON)
			this.shapeType = ShapeType.HEXAGON;
		else if (symbol == USymbols.INTERFACE)
			this.shapeType = getSkinParam().fixCircleLabelOverlapping() ? ShapeType.RECTANGLE_WITH_CIRCLE_INSIDE
					: ShapeType.RECTANGLE;
		else
			this.shapeType = ShapeType.RECTANGLE;

		this.hideText = symbol == USymbols.INTERFACE;

		this.url = entity.getUrl99();

		final Colors colors = entity.getColors();

		final StyleSignatureBasic tmp;
		if (symbol instanceof USymbolActorBusiness)
			tmp = StyleSignatureBasic.of(SName.root, SName.element, styleName, SName.actor, SName.business,
					SName.title);
		else
			tmp = StyleSignatureBasic.of(SName.root, SName.element, styleName, symbol.getSName(), SName.title);

		final Stereotype stereotype = entity.getStereotype();
		final Style styleTitle = tmp.withTOBECHANGED(stereotype).getMergedStyle(getSkinParam().getCurrentStyleBuilder())
				.eventuallyOverride(colors);

		final Style styleStereo = tmp.forStereotypeItself(stereotype)
				.getMergedStyle(getSkinParam().getCurrentStyleBuilder());

		final StyleSignatureBasic tmp2 = StyleSignatureBasic.of(SName.root, SName.element, styleName,
				symbol.getSName());
		final Style style = tmp2.withTOBECHANGED(stereotype).getMergedStyle(getSkinParam().getCurrentStyleBuilder())
				.eventuallyOverride(colors);

		final HColor forecolor = styleTitle.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());

		HColor backcolor = colors.getColor(ColorType.BACK);
		if (backcolor == null)
			backcolor = styleTitle.value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());

		final double roundCorner = styleTitle.value(PName.RoundCorner).asDouble();
		final double diagonalCorner = styleTitle.value(PName.DiagonalCorner).asDouble();
		final double deltaShadow = styleTitle.value(PName.Shadowing).asDouble();
		final UStroke stroke = styleTitle.getStroke(colors);
		final FontConfiguration fcTitle = styleTitle.getFontConfiguration(getSkinParam().getIHtmlColorSet());
		final FontConfiguration fc = style.getFontConfiguration(getSkinParam().getIHtmlColorSet());
		final FontConfiguration fcStereo = styleStereo.getFontConfiguration(getSkinParam().getIHtmlColorSet());
		final HorizontalAlignment defaultAlign = styleTitle.getHorizontalAlignment();

		assert getStereo() == stereotype;

		ctx = new Fashion(backcolor, forecolor).withStroke(stroke).withShadow(deltaShadow).withCorner(roundCorner,
				diagonalCorner);

		final Display codeDisplay = Display.getWithNewlines(entity.getName());
		if ((entity.getDisplay().equalsLike(codeDisplay) && symbol.getSName() == SName.package_)
				|| entity.getDisplay().isWhite())
			desc = TextBlockUtils.empty(style.value(PName.MinimumWidth).asDouble(), 0);
		else if (entity.getDisplay().equalsLike(codeDisplay))
			desc = BodyFactory.create3(entity.getDisplay(), getSkinParam(), defaultAlign, fcTitle, style.wrapWidth(),
					styleTitle);
		else
			desc = BodyFactory.create3(entity.getDisplay(), getSkinParam(), defaultAlign, fc, style.wrapWidth(), style);

		stereo = TextBlockUtils.empty(0, 0);

		if (stereotype != null && stereotype.getSprite(getSkinParam()) != null)
			stereo = stereotype.getSprite(getSkinParam());
		else if (stereotype != null && stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR) != null
				&& portionShower.showPortion(EntityPortion.STEREOTYPE, entity))
			stereo = Display.getWithNewlines(stereotype.getLabel(getSkinParam().guillemet())).create(fcStereo,
					HorizontalAlignment.CENTER, getSkinParam());

		name = BodyFactory.create2(getSkinParam().getDefaultTextAlignment(HorizontalAlignment.CENTER), codeDisplay,
				getSkinParam(), stereotype, entity, styleTitle);

		if (hideText)
			asSmall = symbol.asSmall(TextBlockUtils.empty(0, 0), TextBlockUtils.empty(0, 0), TextBlockUtils.empty(0, 0),
					ctx, getSkinParam().getStereotypeAlignment());
		else
			asSmall = symbol.asSmall(name, desc, stereo, ctx, getSkinParam().getStereotypeAlignment());

	}

	private USymbol getUSymbol(Entity entity) {
		final USymbol result;
		if (entity.getUSymbol() == null)
			result = getSkinParam().componentStyle().toUSymbol();
		else
			result = entity.getUSymbol();
		return Objects.requireNonNull(result);
	}

	public XDimension2D getNameDimension(StringBounder stringBounder) {
		if (hideText)
			return new XDimension2D(0, 0);

		return name.calculateDimension(stringBounder);
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return asSmall.calculateDimension(stringBounder);
	}

	@Override
	public Margins getShield(StringBounder stringBounder) {
		if (hideText == false)
			return Margins.NONE;

		if (isThereADoubleLink((Entity) getEntity(), links))
			return Margins.NONE;

		if (fixCircleLabelOverlapping == false && hasSomeHorizontalLinkVisible((Entity) getEntity(), links))
			return Margins.NONE;

		if (hasSomeHorizontalLinkDoubleDecorated((Entity) getEntity(), links))
			return Margins.NONE;

		final XDimension2D dimStereo = stereo.calculateDimension(stringBounder);
		final XDimension2D dimDesc = desc.calculateDimension(stringBounder);
		final XDimension2D dimSmall = asSmall.calculateDimension(stringBounder);
		final double x = Math.max(dimStereo.getWidth(), dimDesc.getWidth());
		double suppX = x - dimSmall.getWidth();
		if (suppX < 1)
			suppX = 1;

		final double y = MathUtils.max(1, dimDesc.getHeight(), dimStereo.getHeight());
		return new Margins(suppX / 2, suppX / 2, y, y);
	}

	private boolean hasSomeHorizontalLinkVisible(Entity leaf, Collection<Link> links) {
		for (Link link : links)
			if (link.getLength() == 1 && link.contains(leaf) && link.isInvis() == false)
				return true;

		return false;
	}

	private boolean isThereADoubleLink(Entity leaf, Collection<Link> links) {
		final Set<Entity> others = new HashSet<>();
		for (Link link : links) {
			if (link.contains(leaf)) {
				final Entity other = link.getOther(leaf);
				final boolean changed = others.add(other);
				if (changed == false)
					return true;

			}
		}
		return false;
	}

	private boolean hasSomeHorizontalLinkDoubleDecorated(Entity leaf, Collection<Link> links) {
		for (Link link : links)
			if (link.getLength() == 1 && link.contains(leaf) && link.getType().isDoubleDecorated())
				return true;

		return false;
	}

	final public void drawU(UGraphic ug) {
		ug.draw(new UComment("entity " + getEntity().getName()));
		final Map<UGroupType, String> typeIDent = new EnumMap<>(UGroupType.class);
		typeIDent.put(UGroupType.CLASS, "elem " + getEntity().getName() + " selected");
		typeIDent.put(UGroupType.ID, "elem_" + getEntity().getName());
		ug.startGroup(typeIDent);

		if (url != null)
			ug.startUrl(url);

		if (shapeType == ShapeType.HEXAGON)
			drawHexagon(ctx.apply(ug));

		asSmall.drawU(ug);

		if (hideText) {
			final double space = 8;
			final XDimension2D dimSmall = asSmall.calculateDimension(ug.getStringBounder());
			final XDimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
			final double posx1 = (dimSmall.getWidth() - dimDesc.getWidth()) / 2;

			UGraphic ugDesc = ug.apply(new UTranslate(posx1, space + dimSmall.getHeight()));
			ugDesc = UGraphicStencil.create(ugDesc, dimDesc);
			desc.drawU(ugDesc);

			final XDimension2D dimStereo = stereo.calculateDimension(ug.getStringBounder());
			final double posx2 = (dimSmall.getWidth() - dimStereo.getWidth()) / 2;
			stereo.drawU(ug.apply(new UTranslate(posx2, -space - dimStereo.getHeight())));
		}

		if (url != null)
			ug.closeUrl();

		ug.closeGroup();
	}

	private void drawHexagon(UGraphic ug) {
		if (bibliotekon == null)
			throw new IllegalStateException();

		final SvekNode node = bibliotekon.getNode(getEntity());
		final Shadowable hexagon = node.getPolygon();
		if (hexagon != null) {
			hexagon.setDeltaShadow(ctx.getDeltaShadow());
			ug.draw(hexagon);
		}
	}

	public ShapeType getShapeType() {
		return shapeType;
	}

	@Override
	public double getOverscanX(StringBounder stringBounder) {
		if (hideText) {
			final XDimension2D dimSmall = asSmall.calculateDimension(stringBounder);
			final XDimension2D dimDesc = desc.calculateDimension(stringBounder);
			final XDimension2D dimStereo = stereo.calculateDimension(stringBounder);
			final double posx1 = (dimSmall.getWidth() - dimDesc.getWidth()) / 2;
			final double posx2 = (dimSmall.getWidth() - dimStereo.getWidth()) / 2;
			return MathUtils.max(-posx1, -posx2, 0);
		}
		return 0;
	}

	@Override
	public MagneticBorder getMagneticBorder() {
		if (shapeType == ShapeType.FOLDER)
			return asSmall.getMagneticBorder();
//			return new MagneticBorder() {
//
//				@Override
//				public UTranslate getForceAt(StringBounder stringBounder, XPoint2D pt) {
//					if ((pt.getX() >= 0 && pt.getX() <= 0 + calculateDimension(stringBounder).getWidth()
//							&& pt.getY() <= 0)) {
//						final XDimension2D dimName = getNameDimension(stringBounder);
//						if (pt.getX() < 0 + dimName.getWidth())
//							return UTranslate.none();
//
//						return new UTranslate(0, dimName.getHeight() + 4);
//					}
//					return UTranslate.none();
//				}
//			};
		return new MagneticBorderNone();
	}
}
