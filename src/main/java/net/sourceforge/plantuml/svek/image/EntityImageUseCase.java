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
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.annotation.DeadCode;
import net.sourceforge.plantuml.cucadiagram.BodyFactory;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.Stencil;
import net.sourceforge.plantuml.klimt.drawing.AbstractUGraphicHorizontalLine;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockInEllipse;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UHorizontalLine;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.text.Guillemet;
import net.sourceforge.plantuml.url.Url;

@DeadCode
public class EntityImageUseCase extends AbstractEntityImage {

	final private TextBlock desc;

	final private Url url;

	private EntityImageUseCase(Entity entity, PortionShower portionShower) {
		super(entity);
		final Stereotype stereotype = entity.getStereotype();

		final Style style = getStyle();
		final HorizontalAlignment align = style.getHorizontalAlignment();

		final TextBlock tmp = BodyFactory.create2(getSkinParam().getDefaultTextAlignment(align), entity.getDisplay(),
				getSkinParam(), stereotype, entity, getStyle());

		final TextBlock stereo;
		final List<String> stereotypeLabels = portionShower.getVisibleStereotypeLabels(entity);
		if (stereotype != null && stereotype.getSprite(getSkinParam()) != null)
			stereo = stereotype.getSprite(getSkinParam());
		else if (stereotype == null || stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR) == null
				|| stereotypeLabels.isEmpty())
			stereo = TextBlockUtils.EMPTY_TEXT_BLOCK;
		else {
			final FontConfiguration fcStereo = FontConfiguration.create(getSkinParam(), FontParam.USECASE_STEREOTYPE,
					stereotype);
			final Display display = Display.create(stereotypeLabels);
			stereo = display.create(fcStereo, HorizontalAlignment.CENTER, getSkinParam());
		}
		this.desc = TextBlockUtils.mergeTB(stereo, tmp, HorizontalAlignment.CENTER);

		this.url = entity.getUrl99();

	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return new TextBlockInEllipse(desc, stringBounder).calculateDimension(stringBounder);
	}

	final public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Style style = getStyle();
		final double shadow = style.getShadowing();

		final TextBlockInEllipse ellipse = new TextBlockInEllipse(desc, stringBounder);
		ellipse.setDeltaShadow(shadow);

		if (url != null)
			ug.startUrl(url);

		ug = ug.apply(getStyle().getStroke(getEntity().getColors()));
		final HColor linecolor = getLineColor();
		ug = ug.apply(linecolor);
		final HColor backcolor = getBackColor();
		ug = ug.apply(backcolor.bg());
		final UGraphic ug2 = new MyUGraphicEllipse(ug, 0, 0, ellipse.getUEllipse());

		final UGroup group = new UGroup(getEntity().getLocation());
		group.put(UGroupType.CLASS, "entity");
		group.put(UGroupType.ID, "entity_" + getEntity().getName());
		group.put(UGroupType.DATA_ENTITY, getEntity().getName());
		group.put(UGroupType.DATA_UID, getEntity().getUid());
		group.put(UGroupType.DATA_QUALIFIED_NAME, getEntity().getQuark().getQualifiedName());
		ug.startGroup(group);
		ellipse.drawU(ug2);
		ug2.closeGroup();

		if (getEntity().getLeafType() == LeafType.USECASE_BUSINESS)
			specialBusiness(ug, ellipse.getUEllipse());

		if (url != null)
			ug.closeUrl();

	}

	private void specialBusiness(UGraphic ug, UEllipse frontier) {
		final RotatedEllipse rotatedEllipse = new RotatedEllipse(frontier, Math.PI / 4);

		final double theta1 = 20.0 * Math.PI / 180;
		final double theta2 = rotatedEllipse.getOtherTheta(theta1);

		final UEllipse frontier2 = frontier.scale(0.99);
		final XPoint2D p1 = frontier2.getPointAtAngle(-theta1);
		final XPoint2D p2 = frontier2.getPointAtAngle(-theta2);
		drawLine(ug, p1, p2);
	}

	private void specialBusiness0(UGraphic ug, UEllipse frontier) {
		final double c = frontier.getWidth() / frontier.getHeight();
		final double ouverture = Math.PI / 2;
		final XPoint2D p1 = frontier.getPointAtAngle(getTrueAngle(c, Math.PI / 4 - ouverture));
		final XPoint2D p2 = frontier.getPointAtAngle(getTrueAngle(c, Math.PI / 4 + ouverture));
		drawLine(ug, p1, p2);
	}

	private void drawLine(UGraphic ug, final XPoint2D p1, final XPoint2D p2) {
		ug = ug.apply(UTranslate.point(p1));
		ug.draw(new ULine(p2.getX() - p1.getX(), p2.getY() - p1.getY()));
	}

	private double getTrueAngle(final double c, final double gamma) {
		return Math.atan2(Math.sin(gamma), Math.cos(gamma) / c);
	}

	private HColor getBackColor() {
		HColor backcolor = getEntity().getColors().getColor(ColorType.BACK);
		if (backcolor == null) {
			final Colors colors = getEntity().getColors();
			Style style = getStyle();
			style = style.eventuallyOverride(colors);
			backcolor = style.value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());
		}
		return backcolor;
	}

	private Style getStyle() {
		return getDefaultStyleDefinition().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
	}

	private StyleSignature getDefaultStyleDefinition() {
		final LeafType type = getEntity().getLeafType();
		if (type == LeafType.USECASE_BUSINESS)
			return StyleSignatureBasic
					.of(SName.root, SName.element, SName.componentDiagram, SName.usecase, SName.business)
					.withTOBECHANGED(getStereo());
		return StyleSignatureBasic.of(SName.root, SName.element, SName.componentDiagram, SName.usecase)
				.withTOBECHANGED(getStereo());
	}

	private HColor getLineColor() {
		HColor linecolor = getEntity().getColors().getColor(ColorType.LINE);
		if (linecolor == null) {
			final Style style = getStyle();
			linecolor = style.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
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
			if (y < yTheoricalPosition)
				throw new IllegalArgumentException();

			y = y - yTheoricalPosition;
			if (y > ellipse.getHeight())
				throw new IllegalArgumentException();

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
			final UStroke stroke = UStroke.withThickness(1.5);
			line.drawLineInternal(ug.apply(translate), getStencil2(translate), 0, stroke);
		}

	}

}
