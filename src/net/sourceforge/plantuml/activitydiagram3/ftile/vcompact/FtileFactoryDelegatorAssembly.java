/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 *
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMargedVertically;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidth;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileFactoryDelegatorAssembly extends FtileFactoryDelegator {

	public FtileFactoryDelegatorAssembly(FtileFactory factory, ISkinParam skinParam) {
		super(factory, skinParam);
	}

	@Override
	public Ftile assembly(final Ftile tile1, final Ftile tile2) {
		double height = 35;
		final TextBlock textBlock = getTextBlock(getInLinkRenderingDisplay(tile2));
		final StringBounder stringBounder = getStringBounder();
		if (textBlock != null) {
			height += textBlock.calculateDimension(stringBounder).getHeight();
		}
		// final Ftile space = new FtileEmpty(getFactory().shadowing(), 1, height);
		final Ftile tile1andSpace = FtileUtils.addBottom(tile1, height);
		Ftile result = super.assembly(tile1andSpace, tile2);
		final FtileGeometry geo = tile1.calculateDimension(stringBounder);
		if (geo.hasPointOut() == false) {
			return result;
		}
		final UTranslate translate1 = result.getTranslateFor(tile1andSpace, stringBounder);
		final Point2D p1 = geo.translate(translate1).getPointOut();
		final UTranslate translate2 = result.getTranslateFor(tile2, stringBounder);
		final Point2D p2 = tile2.calculateDimension(stringBounder).translate(translate2).getPointIn();

		final HtmlColor color = getInLinkRenderingColor(tile2);

		final ConnectionVerticalDown connection = new ConnectionVerticalDown(tile1, tile2, p1, p2, color, textBlock);
		result = FtileUtils.addConnection(result, connection);
		if (textBlock != null) {
			final double width = result.calculateDimension(stringBounder).getWidth();
			// System.err.println("width=" + width);
			// System.err.println("p1=" + p1);
			// System.err.println("p2=" + p2);
			final double maxX = connection.getMaxX(stringBounder);
			// System.err.println("maxX=" + maxX);
			final double needed = (maxX - width) * 2;
			result = new FtileMinWidth(result, needed);
		}
		return result;
	}

	private final Rose rose = new Rose();

	private TextBlock getTextBlock(Display display) {
		// DUP1433
		if (display == null) {
			return null;
		}
		final ISkinParam skinParam = getSkinParam();
		final UFont font = skinParam.getFont(FontParam.ACTIVITY_ARROW, null, false);
		final HtmlColor color = rose.getFontColor(skinParam, FontParam.ACTIVITY_ARROW);
		final FontConfiguration fontConfiguration = new FontConfiguration(font, color, skinParam.getHyperlinkColor(),
				skinParam.useUnderlineForHyperlink());
		return TextBlockUtils.create(display, fontConfiguration, HorizontalAlignment.LEFT, null, true);
	}
}
