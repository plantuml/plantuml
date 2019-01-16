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
package net.sourceforge.plantuml.postit;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Area implements Elastic {

	private final String title;
	private final char id;

	private Dimension2D minimumDimension;

	private final List<PostIt> postIts = new ArrayList<PostIt>();

	public Area(char id, String title) {
		this.id = id;
		this.title = title;
	}

	public char getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Dimension2D getMinimumDimension() {
		return minimumDimension;
	}

	public void setMinimunDimension(Dimension2D minimumDimension) {
		this.minimumDimension = minimumDimension;
	}

	public Dimension2D getDimension() {
		throw new UnsupportedOperationException();
	}

	public double heightWhenWidthIs(double width, StringBounder stringBounder) {
		final AreaLayoutFixedWidth layout = new AreaLayoutFixedWidth(width);
		final Map<PostIt, Point2D> pos = layout.getPositions(postIts, stringBounder);
		double max = 10;
		for (Map.Entry<PostIt, Point2D> ent : pos.entrySet()) {
			final double y = ent.getKey().getDimension(stringBounder).getHeight() + ent.getValue().getY();
			max = Math.max(max, y);
		}

		return max + 10;
	}

	public double widthWhenHeightIs(double height, StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

	public void add(PostIt postIt) {
		postIts.add(postIt);
	}

	public void drawU(UGraphic ug, double width) {
		final AreaLayout layout = new AreaLayoutFixedWidth(width);
		final Map<PostIt, Point2D> pos = layout.getPositions(postIts, ug.getStringBounder());
		for (Map.Entry<PostIt, Point2D> ent : pos.entrySet()) {
			final UGraphic ugTranslated = ug.apply(new UTranslate(ent.getValue().getX(), ent.getValue().getY()));
			ent.getKey().drawU(ugTranslated);
		}

	}

}
