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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileBreak;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Genealogy;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.WeldingPoint;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.svek.ConditionStyle;

public class FtileFactoryDelegatorRepeat extends FtileFactoryDelegator {

	public FtileFactoryDelegatorRepeat(FtileFactory factory) {
		super(factory);
	}

	@Override
	public Ftile repeat(BoxStyle boxStyleIn, Stereotype stereotype, Swimlane swimlane, Swimlane swimlaneOut,
			Display startLabel, final Ftile repeat, Display test, Display yes, Display out, Colors colors,
			Ftile backward, boolean noOut, LinkRendering incoming1, LinkRendering incoming2,
			StyleBuilder currentStyleBuilder) {

		final ConditionStyle conditionStyle = skinParam().getConditionStyle();

		final Style styleArrow = getDefaultStyleDefinitionArrow().getMergedStyle(currentStyleBuilder);
		final Style styleDiamond = getDefaultStyleDefinitionDiamond().withTOBECHANGED(stereotype)
				.getMergedStyle(currentStyleBuilder);
		final HColor borderColor = styleDiamond.value(PName.LineColor).asColor(skinParam().getIHtmlColorSet());
		final HColor diamondColor = styleDiamond.value(PName.BackGroundColor).asColor(skinParam().getIHtmlColorSet());
		final Rainbow arrowColor = Rainbow.build(styleArrow, skinParam().getIHtmlColorSet());
		final FontConfiguration fcDiamond = styleDiamond.getFontConfiguration(skinParam().getIHtmlColorSet());
		final FontConfiguration fcArrow = styleArrow.getFontConfiguration(skinParam().getIHtmlColorSet());

		final LinkRendering endRepeatLinkRendering = repeat.getOutLinkRendering();
		final Rainbow endRepeatLinkColor = endRepeatLinkRendering == null ? null : endRepeatLinkRendering.getRainbow();

		final Ftile entry = getEntry(swimlane, startLabel, colors, boxStyleIn, stereotype);

		Ftile result = FtileRepeat.create(swimlane, swimlaneOut, entry, repeat, test, yes, out, borderColor,
				diamondColor, arrowColor, endRepeatLinkColor, conditionStyle, this.skinParam(), fcDiamond, fcArrow,
				backward, noOut, incoming1, incoming2);

		final List<WeldingPoint> weldingPoints = repeat.getWeldingPoints();
		if (weldingPoints.size() > 0) {

			final Ftile diamondBreak = new FtileDiamond(repeat.skinParam(), diamondColor, borderColor, swimlane);
			result = assembly(FtileUtils.addHorizontalMargin(result, 10, 0), diamondBreak);
			final Genealogy genealogy = new Genealogy(result);

			final Collection<Connection> connections = new ArrayList<Connection>();

			for (int i = 0; i < weldingPoints.size(); i++) {
				final FtileBreak ftileBreak = (FtileBreak) weldingPoints.get(i);
				final boolean first = i == 0;
				connections.add(new Connection() {
					public void drawU(UGraphic ug) {
						final UTranslate tr1 = genealogy.getTranslate(ftileBreak, ug.getStringBounder());
						final UTranslate tr2 = genealogy.getTranslate(diamondBreak, ug.getStringBounder());
						final XDimension2D dimDiamond = diamondBreak.calculateDimension(ug.getStringBounder());

						final Snake snake;
						if (first) {
							snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToRight());
							snake.addPoint(tr1.getDx(), tr1.getDy());
							snake.addPoint(0, tr1.getDy());
							snake.addPoint(0, tr2.getDy() + dimDiamond.getHeight() / 2);
							snake.addPoint(tr2.getDx(), tr2.getDy() + dimDiamond.getHeight() / 2);
						} else {
							snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToLeft());
							snake.addPoint(tr1.getDx(), tr1.getDy());
							snake.addPoint(0, tr1.getDy());
						}
						ug.draw(snake);
					}

					public Ftile getFtile1() {
						return ftileBreak;
					}

					public Ftile getFtile2() {
						return diamondBreak;
					}

				});
			}

			result = FtileUtils.addConnection(result, connections);

		}
		return result;
	}

	private Ftile getEntry(Swimlane swimlane, Display startLabel, Colors colors, BoxStyle boxStyleIn,
			Stereotype stereotype) {
		if (Display.isNull(startLabel))
			return null;

		return this.activity(startLabel, swimlane, boxStyleIn, colors, stereotype, null);
	}
}
