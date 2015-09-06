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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileDiamond extends AbstractFtile {

	private final HtmlColor backColor;
	private final HtmlColor borderColor;
	private final Swimlane swimlane;
	private final TextBlock north;
	private final TextBlock south;
	private final TextBlock west1;
	private final TextBlock east1;

	public FtileDiamond(boolean shadowing, HtmlColor backColor, HtmlColor borderColor, Swimlane swimlane) {
		this(shadowing, backColor, borderColor, swimlane, TextBlockUtils.empty(0, 0), TextBlockUtils.empty(0, 0),
				TextBlockUtils.empty(0, 0), TextBlockUtils.empty(0, 0));
	}

	public FtileDiamond withNorth(TextBlock north) {
		return new FtileDiamond(shadowing(), backColor, borderColor, swimlane, north, south, east1, west1);
	}

	public FtileDiamond withWest(TextBlock west1) {
		if (west1 == null) {
			return this;
		}
		return new FtileDiamond(shadowing(), backColor, borderColor, swimlane, north, south, east1, west1);
	}

	public FtileDiamond withEast(TextBlock east1) {
		if (east1 == null) {
			return this;
		}
		return new FtileDiamond(shadowing(), backColor, borderColor, swimlane, north, south, east1, west1);
	}

	public FtileDiamond withSouth(TextBlock south) {
		return new FtileDiamond(shadowing(), backColor, borderColor, swimlane, north, south, east1, west1);
	}

	private FtileDiamond(boolean shadowing, HtmlColor backColor, HtmlColor borderColor, Swimlane swimlane,
			TextBlock north, TextBlock south, TextBlock east1, TextBlock west1) {
		super(shadowing);
		this.backColor = backColor;
		this.swimlane = swimlane;
		this.borderColor = borderColor;
		this.north = north;
		this.west1 = west1;
		this.east1 = east1;
		this.south = south;
	}

	public Set<Swimlane> getSwimlanes() {
		if (swimlane == null) {
			return Collections.emptySet();
		}
		return Collections.singleton(swimlane);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

	public void drawU(UGraphic ug) {

		ug.apply(new UChangeColor(borderColor)).apply(new UStroke(1.5)).apply(new UChangeBackColor(backColor))
				.draw(Diamond.asPolygon(shadowing()));
		final Dimension2D dimNorth = north.calculateDimension(ug.getStringBounder());
		north.drawU(ug.apply(new UTranslate(Diamond.diamondHalfSize * 1.5, -dimNorth.getHeight()
				- Diamond.diamondHalfSize)));

		final Dimension2D dimSouth = south.calculateDimension(ug.getStringBounder());
		south.drawU(ug.apply(new UTranslate(-(dimSouth.getWidth() - 2 * Diamond.diamondHalfSize) / 2,
				2 * Diamond.diamondHalfSize)));

		final Dimension2D dimWeat1 = west1.calculateDimension(ug.getStringBounder());
		west1.drawU(ug.apply(new UTranslate(-dimWeat1.getWidth(), -dimWeat1.getHeight() + Diamond.diamondHalfSize)));

		final Dimension2D dimEast1 = east1.calculateDimension(ug.getStringBounder());
		east1.drawU(ug.apply(new UTranslate(Diamond.diamondHalfSize * 2, -dimEast1.getHeight()
				+ Diamond.diamondHalfSize)));
	}

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = new Dimension2DDouble(Diamond.diamondHalfSize * 2, Diamond.diamondHalfSize * 2);
		return new FtileGeometry(dim, dim.getWidth() / 2, 0, dim.getHeight());
	}

	public Ftile withWestAndEast(TextBlock tb1, TextBlock tb2) {
		return withWest(tb1).withEast(tb2);
	}

}
