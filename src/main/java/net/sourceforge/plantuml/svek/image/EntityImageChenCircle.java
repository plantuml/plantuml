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

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.url.Url;

public class EntityImageChenCircle extends AbstractEntityImage {

	final static private int MARGIN = 6;

	final private boolean isKey;
	final private boolean isMulti;
	final private boolean isDerived;

	final private TextBlock title;
	final private Url url;

	public EntityImageChenCircle(Entity entity) {
		super(entity);

		isKey = hasStereotype("<<key>>");
		isMulti = hasStereotype("<<multi>>");
		isDerived = hasStereotype("<<derived>>");

		FontConfiguration titleFontConfiguration = getStyleTitle(entity, getSkinParam())
				.getFontConfiguration(getSkinParam().getIHtmlColorSet(), entity.getColors());
		if (isKey) {
			titleFontConfiguration = titleFontConfiguration.underline();
		}

		title = entity.getDisplay().create8(titleFontConfiguration, HorizontalAlignment.CENTER, getSkinParam(), CreoleMode.FULL,
				getStyle().wrapWidth());

		url = entity.getUrl99();
	}

	private boolean hasStereotype(String stereotype) {
		return getEntity().getStereotype() != null && getEntity().getStereotype().toString().contains(stereotype);
	}

	private Style getStyle() {
		return getStyle(getEntity(), getSkinParam());
	}

	private static Style getStyle(Entity group, ISkinParam skinParam) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chenEerDiagram, SName.circle)
				.withTOBECHANGED(group.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private static Style getStyleTitle(Entity group, ISkinParam skinParam) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chenEerDiagram, SName.circle, SName.title)
				.withTOBECHANGED(group.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	@Override
	public ShapeType getShapeType() {
		return ShapeType.OVAL;
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return new XDimension2D(25, 25);
	}

	@Override
	public void drawU(UGraphic ug) {
		ug.startGroup(UGroup.singletonMap(UGroupType.ID, getEntity().getQuark().toStringPoint()));
		if (url != null)
			ug.startUrl(url);

		final XDimension2D dimTotal = calculateDimension(ug.getStringBounder());
		final XDimension2D dimTitle = title.calculateDimension(ug.getStringBounder());

		UStroke stroke = getStyle().getStroke(getEntity().getColors());
		if (isDerived) {
			stroke = new UStroke(10, 10, stroke.getThickness());
		}

		ug = applyColor(ug);
		ug = ug.apply(stroke);
		ug.draw(getShape(dimTotal));
		if (isMulti) {
			ug.apply(new UTranslate(3, 3)).draw(getShape(dimTotal.delta(-6)));
		}

		final double xTitle = (dimTotal.getWidth() - dimTitle.getWidth()) / 2;
		final double yTitle = (dimTotal.getHeight() - dimTitle.getHeight()) / 2;
		title.drawU(ug.apply(new UTranslate(xTitle, yTitle)));

		if (url != null)
			ug.closeUrl();

		ug.closeGroup();
	}

	final protected UGraphic applyColor(UGraphic ug) {
		Colors colors = getEntity().getColors();

		HColor border = colors.getColor(ColorType.LINE);
		if (border == null)
			border = getStyle().value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
		ug = ug.apply(border);

		HColor backcolor = colors.getColor(ColorType.BACK);
		if (backcolor == null)
			backcolor = getStyle().value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());
		ug = ug.apply(backcolor.bg());

		return ug;
	}

	private UShape getShape(XDimension2D dimTotal) {
		return UEllipse.build(dimTotal.getWidth(), dimTotal.getHeight());
	}

}
