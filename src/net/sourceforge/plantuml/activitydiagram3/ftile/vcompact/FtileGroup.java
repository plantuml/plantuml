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
 *
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMarged;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileGroup extends AbstractFtile {

	private final double diffYY = 25;
	private final Ftile inner;
	private final TextBlock name;
	private final HtmlColor color;

	public FtileGroup(Ftile inner, Display test, HtmlColor color, SpriteContainer spriteContainer) {
		super(inner.shadowing());
		this.inner = new FtileMarged(inner, 10);
		this.color = color;
		final UFont font = new UFont("Serif", Font.PLAIN, 14);
		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		if (test == null) {
			this.name = TextBlockUtils.empty(0, 0);
		} else {
			this.name = TextBlockUtils.create(test, fc, HorizontalAlignment.LEFT, spriteContainer);
		}
	}

	public Set<Swimlane> getSwimlanes() {
		return inner.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return inner.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return inner.getSwimlaneOut();
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		final Dimension2D dim = inner.asTextBlock().calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, 0, diffYY * 2);
	}

	private UTranslate getTranslate() {
		return new UTranslate(0, diffYY);
	}

	public Point2D getPointIn(StringBounder stringBounder) {
		return getTranslate().getTranslated(inner.getPointIn(stringBounder));
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		return getTranslate().getTranslated(inner.getPointOut(stringBounder));
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dimTotal = calculateDimension(ug.getStringBounder());

				final SymbolContext symbolContext = new SymbolContext(HtmlColorUtils.WHITE, HtmlColorUtils.BLACK)
						.withShadow(shadowing()).withStroke(new UStroke(2));
				USymbol.FRAME.asBig(name, TextBlockUtils.empty(0, 0), dimTotal.getWidth(), dimTotal.getHeight(),
						symbolContext).drawU(ug);

				ug.apply(getTranslate()).draw(inner);

			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return calculateDimensionInternal(stringBounder);
			}
		};
	}

	public boolean isKilled() {
		return false;
	}

}
