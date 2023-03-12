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

import java.util.Collections;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;

public class EntityImageState extends EntityImageStateCommon {

	final private TextBlock fields;

	final private static int MIN_WIDTH = 50;
	final private static int MIN_HEIGHT = 50;

	final private boolean withSymbol;

	final static private double smallRadius = 3;
	final static private double smallLine = 3;
	final static private double smallMarginX = 7;
	final static private double smallMarginY = 4;

	public EntityImageState(Entity entity, ISkinParam skinParam) {
		super(entity, skinParam);

		final Stereotype stereotype = entity.getStereotype();

		this.withSymbol = stereotype != null && stereotype.isWithOOSymbol();
		final Display list = Display.create(entity.getBodier().getRawBody());

		final FontConfiguration fieldsFontConfiguration = getStyleStateHeader()
				.getFontConfiguration(getSkinParam().getIHtmlColorSet());

		this.fields = list.create8(fieldsFontConfiguration, HorizontalAlignment.LEFT, skinParam, CreoleMode.FULL,
				getStyleState().wrapWidth());

	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D dim = title.calculateDimension(stringBounder)
				.mergeTB(fields.calculateDimension(stringBounder));
		double heightSymbol = 0;
		if (withSymbol)
			heightSymbol += 2 * smallRadius + smallMarginY;

		final XDimension2D result = dim.delta(MARGIN * 2 + 2 * MARGIN_LINE + heightSymbol);
		return result.atLeast(MIN_WIDTH, MIN_HEIGHT);
	}

	final public void drawU(UGraphic ug) {
		ug.startGroup(Collections.singletonMap(UGroupType.ID, getEntity().getQuark().toStringPoint()));
		if (url != null)
			ug.startUrl(url);

		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimTotal = calculateDimension(stringBounder);
		final XDimension2D dimDesc = title.calculateDimension(stringBounder);

		final UStroke stroke = getStyleState().getStroke(lineConfig.getColors());

		ug = applyColor(ug);
		ug = ug.apply(stroke);
		ug.draw(getShape(dimTotal));

		final double yLine = MARGIN + dimDesc.getHeight() + MARGIN_LINE;
		ug.apply(UTranslate.dy(yLine)).draw(ULine.hline(dimTotal.getWidth()));

		if (withSymbol) {
			final double xSymbol = dimTotal.getWidth();
			final double ySymbol = dimTotal.getHeight();
			drawSymbol(ug, xSymbol, ySymbol);
		}

		final double xDesc = (dimTotal.getWidth() - dimDesc.getWidth()) / 2;
		final double yDesc = MARGIN;
		title.drawU(ug.apply(new UTranslate(xDesc, yDesc)));

		final double xFields = MARGIN;
		final double yFields = yLine + MARGIN_LINE;
		fields.drawU(ug.apply(new UTranslate(xFields, yFields)));

		if (url != null)
			ug.closeUrl();

		ug.closeGroup();
	}

	public static void drawSymbol(UGraphic ug, double xSymbol, double ySymbol) {
		xSymbol -= 4 * smallRadius + smallLine + smallMarginX;
		ySymbol -= 2 * smallRadius + smallMarginY;
		final UEllipse small = UEllipse.build(2 * smallRadius, 2 * smallRadius);
		ug.apply(new UTranslate(xSymbol, ySymbol)).draw(small);
		ug.apply(new UTranslate(xSymbol + smallLine + 2 * smallRadius, ySymbol)).draw(small);
		ug.apply(new UTranslate(xSymbol + 2 * smallRadius, ySymbol + smallLine)).draw(ULine.hline(smallLine));
	}

}
