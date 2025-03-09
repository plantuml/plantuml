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
import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.svek.SvekNode;
import net.sourceforge.plantuml.url.Url;

public class EntityImageActivity extends AbstractEntityImage {

	private double shadowing = 0;
	public static final int CORNER = 25;
	final private TextBlock desc;
	final private static int MARGIN = 10;
	final private Url url;
	private final Bibliotekon bibliotekon;

	public EntityImageActivity(Entity entity, Bibliotekon bibliotekon) {
		super(entity);
		this.bibliotekon = bibliotekon;

		final Style style = getDefaultStyleDefinition().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
		final FontConfiguration fontConfiguration = style.getFontConfiguration(getSkinParam().getIHtmlColorSet());
		final HorizontalAlignment horizontalAlignment = style.getHorizontalAlignment();
		this.shadowing = style.getShadowing();

		this.desc = entity.getDisplay().create(fontConfiguration, horizontalAlignment, getSkinParam());
		this.url = entity.getUrl99();
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D dim = desc.calculateDimension(stringBounder);
		return dim.delta(MARGIN * 2);
	}

	final public void drawU(UGraphic ug) {
		if (url != null)
			ug.startUrl(url);

		if (getShapeType() == ShapeType.ROUND_RECTANGLE)
			ug = drawNormal(ug);
		else if (getShapeType() == ShapeType.OCTAGON)
			ug = drawOctagon(ug);
		else
			throw new UnsupportedOperationException();

		if (url != null)
			ug.closeUrl();

	}

	private UGraphic drawOctagon(UGraphic ug) {
		final SvekNode node = bibliotekon.getNode(getEntity());
		final Shadowable octagon = node.getPolygon();
		if (octagon == null)
			return drawNormal(ug);

		octagon.setDeltaShadow(shadowing);
		ug = applyColors(ug);
		ug.apply(UStroke.withThickness(1.5)).draw(octagon);
		desc.drawU(ug.apply(new UTranslate(MARGIN, MARGIN)));
		return ug;

	}

	private UGraphic drawNormal(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimTotal = calculateDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final Shadowable rect = URectangle.build(widthTotal, heightTotal).rounded(CORNER);
		rect.setDeltaShadow(shadowing);

		ug = applyColors(ug);

		final Style style = getDefaultStyleDefinition().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
		final UStroke stroke = style.getStroke();

		ug.apply(stroke).draw(rect);

		desc.drawU(ug.apply(new UTranslate(MARGIN, MARGIN)));
		return ug;
	}

	public StyleSignature getDefaultStyleDefinition() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity)
				.withTOBECHANGED(getStereo());
	}

	private UGraphic applyColors(UGraphic ug) {

		final Style style = getDefaultStyleDefinition().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
		final HColor borderColor = style.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
		HColor backcolor = getEntity().getColors().getColor(ColorType.BACK);
		if (backcolor == null)
			backcolor = style.value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());

		ug = ug.apply(borderColor);
		ug = ug.apply(backcolor.bg());
		return ug;
	}

	public ShapeType getShapeType() {
		final Stereotype stereotype = getStereo();
		if (getSkinParam().useOctagonForActivity(stereotype))
			return ShapeType.OCTAGON;

		return ShapeType.ROUND_RECTANGLE;
	}

}
