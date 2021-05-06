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
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.cucadiagram.BodyFactory;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.SkinParameter;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphicHorizontalLine;
import net.sourceforge.plantuml.ugraphic.TextBlockInEllipse;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGroupType;
import net.sourceforge.plantuml.ugraphic.UHorizontalLine;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class EntityImageUseCase extends AbstractEntityImage {

	final private TextBlock desc;

	final private Url url;

	public EntityImageUseCase(ILeaf entity, ISkinParam skinParam2, PortionShower portionShower) {
		super(entity, entity.getColors(skinParam2).mute(skinParam2));
		final Stereotype stereotype = entity.getStereotype();

		final HorizontalAlignment align;
		if (UseStyle.useBetaStyle()) {
			final Style style = getStyle();
			align = style.getHorizontalAlignment();
		} else {
			align = HorizontalAlignment.CENTER;
		}
		final TextBlock tmp = BodyFactory.create2(getSkinParam().getDefaultTextAlignment(align), entity.getDisplay(),
				FontParam.USECASE, getSkinParam(), stereotype, entity, getStyle());

		if (stereotype == null || stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR) == null
				|| portionShower.showPortion(EntityPortion.STEREOTYPE, entity) == false) {
			this.desc = tmp;
		} else {
			final TextBlock stereo;
			if (stereotype.getSprite(getSkinParam()) != null) {
				stereo = stereotype.getSprite(getSkinParam());
			} else {
				stereo = Display.getWithNewlines(stereotype.getLabel(getSkinParam().guillemet())).create(
						new FontConfiguration(getSkinParam(), FontParam.USECASE_STEREOTYPE, stereotype),
						HorizontalAlignment.CENTER, getSkinParam());
			}
			this.desc = TextBlockUtils.mergeTB(stereo, tmp, HorizontalAlignment.CENTER);
		}
		this.url = entity.getUrl99();

	}

	private UStroke getStroke() {
		if (UseStyle.useBetaStyle()) {
			final Style style = getStyle();
			return style.getStroke();
		}
		UStroke stroke = getSkinParam().getThickness(LineParam.usecaseBorder, getStereo());
		if (stroke == null) {
			stroke = new UStroke(1.5);
		}
		final Colors colors = getEntity().getColors(getSkinParam());
		stroke = colors.muteStroke(stroke);
		return stroke;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new TextBlockInEllipse(desc, stringBounder).calculateDimension(stringBounder);
	}

	final public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final TextBlockInEllipse ellipse = new TextBlockInEllipse(desc, stringBounder);
		if (getSkinParam().shadowing2(getEntity().getStereotype(), SkinParameter.USECASE)) {
			ellipse.setDeltaShadow(3);
		}

		if (url != null) {
			ug.startUrl(url);
		}

		ug = ug.apply(getStroke());
		final HColor linecolor = getLineColor();
		ug = ug.apply(linecolor);
		final HColor backcolor = getBackColor();
		ug = ug.apply(backcolor.bg());
		final UGraphic ug2 = new MyUGraphicEllipse(ug, 0, 0, ellipse.getUEllipse());

		ug2.startGroup(UGroupType.CLASS, "elem " + getEntity().getCode() + " selected");
		ellipse.drawU(ug2);
		ug2.closeGroup();

		if (getEntity().getLeafType() == LeafType.USECASE_BUSINESS) {
			specialBusiness(ug, ellipse.getUEllipse());
		}

		if (url != null) {
			ug.closeUrl();
		}
	}

	private void specialBusiness(UGraphic ug, UEllipse frontier) {
		final RotatedEllipse rotatedEllipse = new RotatedEllipse(frontier, Math.PI / 4);

		final double theta1 = 20.0 * Math.PI / 180;
		final double theta2 = rotatedEllipse.getOtherTheta(theta1);

		final UEllipse frontier2 = frontier.scale(0.99);
		final Point2D p1 = frontier2.getPointAtAngle(-theta1);
		final Point2D p2 = frontier2.getPointAtAngle(-theta2);
		drawLine(ug, p1, p2);
	}

	private void specialBusiness0(UGraphic ug, UEllipse frontier) {
		final double c = frontier.getWidth() / frontier.getHeight();
		final double ouverture = Math.PI / 2;
		final Point2D p1 = frontier.getPointAtAngle(getTrueAngle(c, Math.PI / 4 - ouverture));
		final Point2D p2 = frontier.getPointAtAngle(getTrueAngle(c, Math.PI / 4 + ouverture));
		drawLine(ug, p1, p2);
	}

	private void drawLine(UGraphic ug, final Point2D p1, final Point2D p2) {
		ug = ug.apply(new UTranslate(p1));
		ug.draw(new ULine(p2.getX() - p1.getX(), p2.getY() - p1.getY()));
	}

	private double getTrueAngle(final double c, final double gamma) {
		return Math.atan2(Math.sin(gamma), Math.cos(gamma) / c);
	}

	private HColor getBackColor() {
		HColor backcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.BACK);
		if (backcolor == null) {
			if (UseStyle.useBetaStyle()) {
				Style style = getStyle();
				final Colors colors = getEntity().getColors(getSkinParam());
				style = style.eventuallyOverride(colors);
				backcolor = style.value(PName.BackGroundColor).asColor(getSkinParam().getThemeStyle(),
						getSkinParam().getIHtmlColorSet());
			} else {
				backcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.usecaseBackground);
			}
		}
		return backcolor;
	}

	private Style getStyle() {
		return getDefaultStyleDefinition().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
	}

	private StyleSignature getDefaultStyleDefinition() {
		return StyleSignature.of(SName.root, SName.element, SName.componentDiagram, SName.usecase).with(getStereo());
	}

	private HColor getLineColor() {
		HColor linecolor = getEntity().getColors(getSkinParam()).getColor(ColorType.LINE);
		if (linecolor == null) {
			if (UseStyle.useBetaStyle()) {
				final Style style = getStyle();
				linecolor = style.value(PName.LineColor).asColor(getSkinParam().getThemeStyle(),
						getSkinParam().getIHtmlColorSet());
			} else {
				linecolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.usecaseBorder);
			}
		}
		return linecolor;
	}

	public ShapeType getShapeType() {
		return ShapeType.OVAL;
	}

	static class MyUGraphicEllipse extends AbstractUGraphicHorizontalLine {

		private final double startingX;
		private final double yTheoricalPosition;
		private final UEllipse ellipse;

		@Override
		protected AbstractUGraphicHorizontalLine copy(UGraphic ug) {
			return new MyUGraphicEllipse(ug, startingX, yTheoricalPosition, ellipse);
		}

		MyUGraphicEllipse(UGraphic ug, double startingX, double yTheoricalPosition, UEllipse ellipse) {
			super(ug);
			this.startingX = startingX;
			this.ellipse = ellipse;
			this.yTheoricalPosition = yTheoricalPosition;
		}

		private double getNormalized(double y) {
			if (y < yTheoricalPosition) {
				throw new IllegalArgumentException();
			}
			y = y - yTheoricalPosition;
			if (y > ellipse.getHeight()) {
				throw new IllegalArgumentException();
			}
			return y;
		}

		private double getStartingXInternal(double y) {
			return startingX + ellipse.getStartingX(getNormalized(y));
		}

		private double getEndingXInternal(double y) {
			return startingX + ellipse.getEndingX(getNormalized(y));
		}

		private Stencil getStencil2(UTranslate translate) {
			final double dy = translate.getDy();
			return new Stencil() {

				public double getStartingX(StringBounder stringBounder, double y) {
					return getStartingXInternal(y + dy);
				}

				public double getEndingX(StringBounder stringBounder, double y) {
					return getEndingXInternal(y + dy);
				}
			};
		}

		@Override
		protected void drawHline(UGraphic ug, UHorizontalLine line, UTranslate translate) {
			final UStroke stroke = new UStroke(1.5);
			line.drawLineInternal(ug.apply(translate), getStencil2(translate), 0, stroke);
		}

	}

}
