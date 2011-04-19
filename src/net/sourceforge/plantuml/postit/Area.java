/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 4167 $
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

public class Area implements Elastic {

	private final String title;
	private final char id;

	private Dimension2D minimunDimension;

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

	public Dimension2D getMinimunDimension() {
		return minimunDimension;
	}

	public void setMinimunDimension(Dimension2D minimunDimension) {
		this.minimunDimension = minimunDimension;
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
		final double tx = ug.getTranslateX();
		final double ty = ug.getTranslateY();
		for (Map.Entry<PostIt, Point2D> ent : pos.entrySet()) {
			ug.translate(ent.getValue().getX(), ent.getValue().getY());
			ent.getKey().drawU(ug);
			ug.setTranslate(tx, ty);
		}

	}

}
